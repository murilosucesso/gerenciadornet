package br.com.gerenciadornet.session;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import br.com.gerenciadornet.entity.Acesso;
import br.com.gerenciadornet.entity.Empresa;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Transacoes;

@Name("authenticator")
public class Authenticator {
	
	@Logger
	private Log log;
	
	@In
	Identity identity;
	
	@In
	Credentials credentials;
	
	@In
	EntityManager entityManager;	
	
	@Out(scope=ScopeType.SESSION, required=false)
	Usuario user;
	
	@Out(scope=ScopeType.SESSION, required=false)
	Empresa empresa;
	
	@Out(scope=ScopeType.SESSION, required=false)
	String mensagensGerais;
	
	@In
	FacesMessages facesMessages;
	
	//@In(create=true)
	//ConsultaPedidosList consultaPedidosList;
	
	/**
	 * Método responsavel pela autenticacao de usuarios.
	 * return true if the authentication was 
	 * successful, false otherwise
	 * @return
	 */
	public boolean authenticate() {
		log.info("authenticating {0}", credentials.getUsername());
		
		try {
			user = (Usuario) entityManager
					.createQuery("from Usuario where loginUsuario = :login and senha = :password and inExclusao = :inExclusao")
					.setParameter("login", credentials.getUsername()).setParameter(
							"password", credentials.getPassword()).setParameter("inExclusao", false).getSingleResult();
			
		} catch (NoResultException e) {			
			facesMessages.add("Usuário ou Senha incorretos!", FacesMessage.SEVERITY_INFO);
			return false;
		}
		
		try {
			
			StringBuilder query = new StringBuilder("from Empresa where codEmpresa = :codEmpresa ");
			
			if(user.getCodUsuario() != 1){
				query.append(" and inEmpresaAtiva = :inEmpresaAtiva");
			}
				
			Query queryExecute = entityManager.createQuery(query.toString()).setParameter("codEmpresa", 1);
			
			if(user.getCodUsuario() != 1){
				queryExecute.setParameter(
						"inEmpresaAtiva", true);
			}
			
			empresa = (Empresa) queryExecute.getSingleResult();	
			
			
		} catch (NoResultException e) {
			//SE A EMPRESA ESTÁ INATIVA, NÃO É POSSÍVEL REALIZAR O LOGIN.
			if(empresa == null){
				facesMessages.add("Empresa inativa! Entre em contato com o Administrador.", FacesMessage.SEVERITY_WARN);
			}					
			return false;
		}
		
		if(empresa.isInContaAtrasada() == true){
			mensagensGerais = "Ainda não consta o registro de pagamento do serviço do sistema, favor regularize.";
		}	
		
		if (user == null || user.getPerfil() == null)
			return false;

		//consultaPedidosList.getNovosPedidos();
		//consultaPedidosList.getOrcamentosAbertos(user);
		
		//O usuário Default murilosucesso - Admin tem todos os acessos.
		if("murilosucesso".equalsIgnoreCase(user.getLoginUsuario())){
			addAllRoles();
			return true;
		}
		
		for (Acesso acesso : user.getAcessos()) {		
			identity.addRole(acesso.getTransacao().getSiglaTransacao());		
		}
		
		identity.addRole(user.getPerfil().getNomePerfil());
		
		return true;
	}
	
	private void addAllRoles(){		
		for(String role: Transacoes.getTodasTransacoes()){
			identity.addRole(role);
		}
	}
}
