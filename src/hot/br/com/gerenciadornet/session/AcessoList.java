package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.UsuarioAcessoDTO;
import br.com.gerenciadornet.entity.Acesso;
import br.com.gerenciadornet.entity.AcessoId;
import br.com.gerenciadornet.entity.Transacao;

@SuppressWarnings("serial")
@Scope(ScopeType.CONVERSATION)
@Name("acessoList")
public class AcessoList extends EntityQuery<Acesso> {

	private static final String EJBQL = "select acesso from Acesso acesso"
	    + " left join fetch acesso.usuario"
	    + " left join fetch acesso.transacao";

	private static final String[] RESTRICTIONS = {
		"acesso.usuario.codUsuario = #{acessoList.acesso.usuario.codUsuario}",
		"acesso.transacao.codTransacao = #{acessoList.acesso.transacao.codTransacao}",
	};

	private Acesso acesso;
	
	private boolean mostrarResultados = false;
	

	public AcessoList() {
		acesso = new Acesso();
		acesso.setId(new AcessoId());		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	List<UsuarioAcessoDTO> usuarioAcessoDTOList; 
	
	@SuppressWarnings("unchecked")
	/**
	 * Retorna a lista de todas as transacoes e faz
	 * a lógica pra dizer se o usuário possui ou não acesso
	 * a estas transações
	 * @return
	 */
	public void buscarTransacaoes() {

		StringBuilder query = new StringBuilder();
		query.append("select transacao from Transacao transacao where transacao.inExclusao = :inExclusao ");
		
		if(acesso != null && acesso.getTransacao() != null && acesso.getTransacao().getCodTransacao() != null ){
			query.append("and transacao.codTransacao = :codTransacao ");
		}
		query.append("order by transacao.codTransacao");
		
		Query createQuery = getEntityManager().createQuery(query.toString());
		
		if(acesso != null && acesso.getTransacao() != null && acesso.getTransacao().getCodTransacao() != null ){
			createQuery.setParameter("codTransacao", acesso.getTransacao().getCodTransacao());
		}
		
		createQuery.setParameter("inExclusao", false);

		List<Transacao> transacoesList = (List<Transacao>)createQuery.getResultList(); 
		
		List<Acesso> acessosList = super.getResultList();
		
		usuarioAcessoDTOList = new ArrayList<UsuarioAcessoDTO>();
		
		for (Transacao transacao : transacoesList) {
			
			UsuarioAcessoDTO usuarioAcessoDTO = new UsuarioAcessoDTO(transacao);
			usuarioAcessoDTO.setUsuario(acesso.getUsuario()); 
			
			for(Acesso acesso : acessosList){
				if(transacao.getCodTransacao() == acesso.getId().getCodTransacao()){
					usuarioAcessoDTO.setPossuiAcesso(true);
					usuarioAcessoDTO.setAcesso(acesso);
					usuarioAcessoDTO.setUsuario(acesso.getUsuario());
				}
			}
			usuarioAcessoDTOList.add(usuarioAcessoDTO);
		}
	}

	public Acesso getAcesso() {
		return acesso;
	}
	
	public boolean isMostrarResultados(){
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados){
		this.mostrarResultados = mostrarResultados;
	}

	public List<UsuarioAcessoDTO> getUsuarioAcessoDTOList() {
		return usuarioAcessoDTOList;
	}

	public void setUsuarioAcessoDTOList(List<UsuarioAcessoDTO> usuarioAcessoDTOList) {
		this.usuarioAcessoDTOList = usuarioAcessoDTOList;
	}
}
