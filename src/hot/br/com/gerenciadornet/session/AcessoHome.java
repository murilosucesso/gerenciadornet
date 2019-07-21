package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Acesso;
import br.com.gerenciadornet.entity.AcessoId;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Transacao;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Scope(ScopeType.CONVERSATION)
@Name("acessoHome")
public class AcessoHome extends EntityHome<Acesso> {

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setAcessoId(AcessoId id) {
		setId(id);
	}

	public AcessoId getAcessoId() {
		return (AcessoId) getId();
	}

	public AcessoHome() {
		setAcessoId(new AcessoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAcessoId().getCodUsuario() == 0)
			return false;
		if (getAcessoId().getCodTransacao() == 0)
			return false;
		return true;
	}

	@Override
	protected Acesso createInstance() {
		Acesso acesso = new Acesso();
		acesso.setId(new AcessoId());
		return acesso;
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

	public Acesso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	

	/**
	 * Remove um acesso de um usuario.
	 * Grava historico do acesso removido.
	 */
	@Override
	public String remove() {			
		
		Acesso acesso = getInstance();
		String log = LogUtil.logHistoricoAcessoRemove(acesso);
		
		addFacesMessage(log,"");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.remove();
		
	}
	
	@Override
	public String persist() {
		
		Acesso acesso = getInstance();
		
		AcessoId id = new AcessoId();
		id.setCodUsuario(acesso.getUsuario().getCodUsuario());
		id.setCodTransacao(acesso.getTransacao().getCodTransacao());
		
		if(entityManager.find(Acesso.class, id) != null){
			addFacesMessage("Usuario ja possui acesso a esta transacao.","");
			return null;
		}		
		acesso.setId(id);
		
		String log = LogUtil.logHistoricoAcessoPersit(acesso);
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.persist();
	}
	
	
	
	/**
	 * Método utilizado para conceder ou excluir um acesso da lista de transações/usuário
	 * @param codTransacao
	 * @param codUsuario
	 * @param conferir
	 */
	public void concederAcesso(Usuario usuario, Transacao transacao, boolean possuiAcesso) {
		
		//Se true, conceder acesso
		if(possuiAcesso){		
			
			Acesso acesso = new Acesso();
			
			AcessoId id = new AcessoId();
			id.setCodUsuario(usuario.getCodUsuario());
			id.setCodTransacao(transacao.getCodTransacao());
			
			acesso.setId(id);
			acesso.setUsuario(usuario);
			acesso.setTransacao(transacao);
			
			setInstance(acesso);
			
			super.persist();
			
			//Gravação de log 
			String log = LogUtil.logHistoricoAcessoPersit(acesso);
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			
		} else {
			
			AcessoId id = new AcessoId();
			id.setCodUsuario(usuario.getCodUsuario());
			id.setCodTransacao(transacao.getCodTransacao());
			
			Acesso acesso = entityManager.find(Acesso.class, id);
			setInstance(acesso);

			//Gravação de log 
			String log = LogUtil.logHistoricoAcessoRemove(acesso);
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			
			super.remove();		
		}		
	}

}
