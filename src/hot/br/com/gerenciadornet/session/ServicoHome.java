package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Servico;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("servicoHome")
public class ServicoHome extends EntityHome<Servico> {

	@In
	EntityManager entityManager;
	
	@In(create = true)
	Usuario user;

	public void setServicoCodServico(Integer id) {
		setId(id);
	}

	public Integer getServicoCodServico() {
		return (Integer) getId();
	}

	@Override
	protected Servico createInstance() {
		Servico servico = new Servico();
		return servico;
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

	public Servico getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@Override
	public String remove() {
		
		Servico servico = getDefinedInstance();
				
		String log = "";
				
		if(servico.getServicoRealizados().size() == 0){
			super.remove();
			addFacesMessage("O servico " + servico.getDescServico().toUpperCase() + " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Servico", servico.getCodServico(), servico.getDescServico().toUpperCase());
			
		} else {		
			servico.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Servico", servico.getCodServico(), servico.getDescServico().toUpperCase());
			addFacesMessage("O servico" + servico.getDescServico().toUpperCase() + " foi desabilitado com sucesso.", "");			
		}

		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
	}
		
	@Override
	public String update() {
		
		Servico servico = getDefinedInstance();
		servico.setDescServico(servico.getDescServico().toUpperCase());
		
		String log = LogUtil.logHistorico("O servico codigo " + servico.getCodServico() + " - " + servico.getDescServico() +
			" teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
	@Override
	public String persist() {
		Servico servico = getInstance();
		servico.setDescServico(servico.getDescServico().toUpperCase());
		return super.persist();
	}
	
}
