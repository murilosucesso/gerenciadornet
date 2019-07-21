package br.com.gerenciadornet.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.StatusVenda;

@SuppressWarnings("serial")
@Name("statusVendaHome")
public class StatusVendaHome extends EntityHome<StatusVenda>{

	@Override
	protected StatusVenda createInstance() {
		StatusVenda statusVenda = new StatusVenda();
		return statusVenda;
	}
	
	protected StatusVenda createInstance(Integer codStatusVenda) {
		StatusVenda statusVenda = new StatusVenda();
		statusVenda.setCodStatusVenda(codStatusVenda);
		return statusVenda;
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

	public StatusVenda getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
}
