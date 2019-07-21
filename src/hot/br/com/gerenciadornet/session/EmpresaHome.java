package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import br.com.gerenciadornet.util.Util;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("empresaHome") 
public class EmpresaHome extends EntityHome<Empresa> {

	private static final long serialVersionUID = 1L;

	public void setEmpresaCodEmpresa(Integer id) {
		setId(id);
	}

	public Integer getEmpresaCodEmpresa() {
		return (Integer) getId();
	}

	@Override
	protected Empresa createInstance() {
		Empresa empresa = new Empresa();
		return empresa;
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

	public Empresa getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Override
	public String persist() {
		Empresa empresa = getInstance();
		empresa.setNomeFantasia(Util.formataNome(empresa.getNomeFantasia()));
		empresa.setRazaoSocial(Util.formataNome(empresa.getRazaoSocial()));
		
		return super.persist();
	}
	
	@Override
	public String update() {
		Empresa empresa = getDefinedInstance();
		empresa.setNomeFantasia(Util.formataNome(empresa.getNomeFantasia()));
		empresa.setRazaoSocial(Util.formataNome(empresa.getRazaoSocial()));
		return super.update();
	}

}
