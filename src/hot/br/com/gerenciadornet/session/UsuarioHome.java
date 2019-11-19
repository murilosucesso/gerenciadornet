package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Acesso;
import br.com.gerenciadornet.entity.AcessoDefault;
import br.com.gerenciadornet.entity.AcessoId;
import br.com.gerenciadornet.entity.Compra;
import br.com.gerenciadornet.entity.Empresa;
import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Transacao;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("usuarioHome")
public class UsuarioHome extends EntityHome<Usuario> {

	@In(create = true)
	EnderecoHome enderecoHome;
	
	/*@In(create = true)
	PerfilHome perfilHome;*/
	
	@In(create = true)
	AcessoHome acessoHome;
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	@In
	Empresa empresa;
	
	String confirmacaoSenha;
	
	
	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public void setUsuarioCodUsuario(Integer id) {
		setId(id);
	}

	public Integer getUsuarioCodUsuario() {
		return (Integer) getId();
	}

	@Override
	protected Usuario createInstance() {
		Usuario usuario = new Usuario();
		return usuario;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		
		Usuario usuario = getInstance();
		
		//para um novo usuario setar inVendedorAtivo = true
		if(usuario == null || usuario.getCodUsuario() == null){
			usuario.setInVendedorAtivo(true);
		} else {
			this.confirmacaoSenha = usuario.getSenha();
		}
	}

	public boolean isWired() {
		//if (getInstance().getPerfil() == null)
			//return false;
		return true;
	}

	public Usuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Endereco> getEnderecos() {
		return getInstance() == null ? null : new ArrayList<Endereco>(
				getInstance().getEnderecos());
	}

	public List<Venda> getVendas() {
		return getInstance() == null ? null : new ArrayList<Venda>(
				getInstance().getVendas());
	}

	public List<Compra> getCompras() {
		return getInstance() == null ? null : new ArrayList<Compra>(
				getInstance().getCompras());
	}

	public List<Venda> getVendas_1() {
		return getInstance() == null ? null : new ArrayList<Venda>(
				getInstance().getVendas_1());
	}

	public List<Acesso> getAcessos() {
		return getInstance() == null ? null : new ArrayList<Acesso>(
				getInstance().getAcessos());
	}
	
	public List<Historico> getHistoricos() {
		return getInstance() == null ? null : new ArrayList<Historico>(
				getInstance().getHistoricos());
	}
	
	public String removeEndereco(){
		
		String destino = enderecoHome.remove();
		Endereco endereco = enderecoHome.getDefinedInstance();
		Usuario usuario = getInstance();
		usuario.getEnderecos().remove(endereco);
		
		return destino;
	}
	
	
	@Override
	public String persist() {
		
		if(!validarQtdUsuarios()){
			addFacesMessage("Quantidade de usuários superior ao contratado.", "");
			return null;
		}
		
		Usuario usuario = getInstance();
		usuario.setInExclusao(false);
		
		if(!usuario.getSenha().equals(confirmacaoSenha)){
			addFacesMessage("Campo Senha nao confere com o campo Confirmacao Senha.", "");
			return null;
		} 

		Query query = entityManager.createQuery("select usuario from Usuario usuario where lower(usuario.loginUsuario) like :loginUsuario");
		query.setParameter("loginUsuario", usuario.getLoginUsuario().toLowerCase());

		if(query.getResultList().size() == 0){
			
			String log = LogUtil.logHistoricoUsuarioInsert(usuario);
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			//salva o usuario
			String retorno = super.persist();	
			
			//concede ao usuario os acessos default.
			Set<AcessoDefault> acessoDefault = usuario.getPerfil().getAcessosDefault();
			
			for (AcessoDefault acessoDefault2 : acessoDefault) {
				Transacao transacao = acessoDefault2.getTransacao();
				
				Acesso acesso = new Acesso();
				
				AcessoId id = new AcessoId();
				id.setCodTransacao(transacao.getCodTransacao());
				id.setCodUsuario(usuario.getCodUsuario());				
				acesso.setId(id);
				acesso.setUsuario(usuario);
				acesso.setTransacao(transacao);
				//concede o acesso da transacao ao usuario
				acessoHome.setInstance(acesso);
				acessoHome.persist();								
			}
			
			return retorno;
		} 
		addFacesMessage("Login ja cadastrado.", "");
		return null;
				
	}
	
	private boolean validarQtdUsuarios(){
		
		Long qtdUusarios = (Long) entityManager
				.createQuery("select count(usuario) from Usuario usuario where usuario.inExclusao = 0")
				.getSingleResult();
		
		if(qtdUusarios++ > empresa.getQtdUsuarios()){
			return false;
		}
		return true;
	}
	
	@Override
	public String remove() {

		Usuario usuario = getInstance();
		String log = "";
		
		if(usuario.getCompras().size() == 0 && usuario.getHistoricos().size() == 0 && usuario.getVendas().size() == 0){
			
			log = LogUtil.logHistoricoDesativada("Usuario", usuario.getCodUsuario(), usuario.getNomeUsuario().toUpperCase());
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			
			return super.remove();
			
		} else {	
			usuario.setInExclusao(true);
			String retorno = update();
			log = LogUtil.logHistoricoDesativada("Cliente", usuario.getCodUsuario(), usuario.getNomeUsuario().toUpperCase());
			addFacesMessage("O usuario " + usuario.getNomeUsuario().toUpperCase() + " foi desabilitado com sucesso.", "");
			return retorno;
		}
		
		//addFacesMessage("O usuario " + usuario.getLoginUsuario() + " nao pode ser removido.", "");
		//return null;		
	}
	
	@Override
	public String update() {
		
		Usuario usuario = getInstance();
				
		String log = LogUtil.logHistoricoUsuarioUpdate(usuario);
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}

}
