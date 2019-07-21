package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.AcessoDefault;
import br.com.gerenciadornet.entity.AcessoDefaultId;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Perfil;
import br.com.gerenciadornet.entity.Transacao;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("acessoDefaultHome")
public class AcessoDefaultHome extends EntityHome<AcessoDefault> {
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setAcessoDefaultId(AcessoDefaultId id) {
		setId(id);
	}

	public AcessoDefaultId getAcessoDefaultId() {
		return (AcessoDefaultId) getId();
	}

	public AcessoDefaultHome() {
		setAcessoDefaultId(new AcessoDefaultId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAcessoDefaultId().getCodPerfil() == 0)
			return false;
		if (getAcessoDefaultId().getCodTransacao() == 0)
			return false;
		return true;
	}

	@Override
	protected AcessoDefault createInstance() {
		AcessoDefault acessoDefaultDefault = new AcessoDefault();
		acessoDefaultDefault.setId(new AcessoDefaultId());
		return acessoDefaultDefault;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public AcessoDefault getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * Remove um acessoDefault default de um perfil.
	 * Grava historico do acessoDefault default removido.
	 */
	@Override
	public String remove() {			
		
		AcessoDefault acessoDefaultDefault = getInstance();
		String log = LogUtil.logHistoricoAcessoDefaultRemove(acessoDefaultDefault);
		
		addFacesMessage(log,"");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.remove();
		
	}
	
	/**
	 * Inclui um acessoDefault default a um perfil.
	 * Grava historico do acessoDefault default removido.
	 */
	@Override
	public String persist() {
		
		AcessoDefault acessoDefaultDefault = getInstance();
		
		AcessoDefaultId id = new AcessoDefaultId();
		id.setCodPerfil(acessoDefaultDefault.getPerfil().getCodPerfil());
		id.setCodTransacao(acessoDefaultDefault.getTransacao().getCodTransacao());
		
		if(entityManager.find(AcessoDefault.class, id) != null){
			addFacesMessage("AcessoDefault Default ja cadastrado para este Perfil.","");
			return null;
		}		
		acessoDefaultDefault.setId(id);
		
		String log = LogUtil.logHistoricoAcessoDefaultPersit(acessoDefaultDefault);
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.persist();
	}
	
	/**
	 * Método utilizado para conceder ou excluir um acessoDefault da lista de transações/usuário
	 * @param codTransacao
	 * @param codPerfil
	 * @param conferir
	 */
	public void concederAcessoDefault(Perfil perfil, Transacao transacao, boolean possuiAcessoDefault) {
		
		//Se true, conceder acessoDefault
		if(possuiAcessoDefault){		
			
			AcessoDefault acessoDefault = new AcessoDefault();
			
			AcessoDefaultId id = new AcessoDefaultId();
			id.setCodPerfil(perfil.getCodPerfil());
			id.setCodTransacao(transacao.getCodTransacao());
			
			acessoDefault.setId(id);
			acessoDefault.setPerfil(perfil);
			acessoDefault.setTransacao(transacao);
			
			setInstance(acessoDefault);
			
			super.persist();
			
			//Gravação de log 
			String log = LogUtil.logHistoricoAcessoDefaultPersit(acessoDefault);
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			
		} else {
			
			AcessoDefaultId id = new AcessoDefaultId();
			id.setCodPerfil(perfil.getCodPerfil());
			id.setCodTransacao(transacao.getCodTransacao());
			
			AcessoDefault acessoDefault = entityManager.find(AcessoDefault.class, id);
			setInstance(acessoDefault);

			//Gravação de log 
			String log = LogUtil.logHistoricoAcessoDefaultRemove(acessoDefault);
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			
			super.remove();		
		}		
	}
}
