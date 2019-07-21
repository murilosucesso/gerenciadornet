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
import br.com.gerenciadornet.entity.AcessoDefault;
import br.com.gerenciadornet.entity.AcessoDefaultId;
import br.com.gerenciadornet.entity.Transacao;

@SuppressWarnings("serial")
@Scope(ScopeType.CONVERSATION)
@Name("acessoDefaultList")
public class AcessoDefaultList extends EntityQuery<AcessoDefault> {

	private static final String EJBQL = "select acessoDefault from AcessoDefault acessoDefault"
	    + " left join fetch acessoDefault.perfil"
	    + " left join fetch acessoDefault.transacao";

	private static final String[] RESTRICTIONS = {
		"acessoDefault.perfil.codPerfil = #{acessoDefaultList.acessoDefault.perfil.codPerfil}",
		"acessoDefault.transacao.codTransacao = #{acessoDefaultList.acessoDefault.transacao.codTransacao}",
	};

	private AcessoDefault acessoDefault;
	
	private boolean mostrarResultados = false;
	

	public AcessoDefaultList() {
		acessoDefault = new AcessoDefault();
		acessoDefault.setId(new AcessoDefaultId());		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	List<UsuarioAcessoDTO> usuarioAcessoDTOList; 
	
	@SuppressWarnings("unchecked")
	/**
	 * Retorna a lista de todas as transacoes e faz
	 * a lógica pra dizer se o usuário possui ou não acessoDefault
	 * a estas transações
	 * @return
	 */
	public void buscarTransacaoes() {

		StringBuilder query = new StringBuilder();
		query.append("select transacao from Transacao transacao where transacao.inExclusao = :inExclusao ");
		
		if(acessoDefault != null && acessoDefault.getTransacao() != null && acessoDefault.getTransacao().getCodTransacao() != null ){
			query.append("and transacao.codTransacao = :codTransacao ");
		}
		query.append("order by transacao.codTransacao");
		
		Query createQuery = getEntityManager().createQuery(query.toString());
		
		if(acessoDefault != null && acessoDefault.getTransacao() != null && acessoDefault.getTransacao().getCodTransacao() != null ){
			createQuery.setParameter("codTransacao", acessoDefault.getTransacao().getCodTransacao());
		}
		
		createQuery.setParameter("inExclusao", false);

		List<Transacao> transacoesList = (List<Transacao>)createQuery.getResultList(); 
		
		List<AcessoDefault> acessoDefaultsList = super.getResultList();
		
		usuarioAcessoDTOList = new ArrayList<UsuarioAcessoDTO>();
		
		for (Transacao transacao : transacoesList) {
			
			UsuarioAcessoDTO perfilAcessoDefaultDTO = new UsuarioAcessoDTO(transacao);
			perfilAcessoDefaultDTO.setPerfil(acessoDefault.getPerfil()); 
			
			for(AcessoDefault acessoDefault : acessoDefaultsList){
				if(transacao.getCodTransacao() == acessoDefault.getId().getCodTransacao()){
					perfilAcessoDefaultDTO.setPossuiAcesso(true);
					perfilAcessoDefaultDTO.setAcessoDefault(acessoDefault);
					perfilAcessoDefaultDTO.setPerfil(acessoDefault.getPerfil());
				}
			}
			usuarioAcessoDTOList.add(perfilAcessoDefaultDTO);
		}
	}

	public AcessoDefault getAcessoDefault() {
		return acessoDefault;
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

