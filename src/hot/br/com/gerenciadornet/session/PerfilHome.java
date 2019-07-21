package br.com.gerenciadornet.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Perfil;

@SuppressWarnings("serial")
@Name("perfilHome")
public class PerfilHome extends EntityHome<Perfil> {
	
	
	public void setPerfilCodPerfil(Integer id) {
		setId(id);
	}

	public Integer getPerfilCodPerfil() {
		return (Integer) getId();
	}

	@Override
	protected Perfil createInstance() {
		Perfil perfil = new Perfil();
		return perfil;
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

	public Perfil getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Override
	public String persist() {
		Perfil perfil = getInstance(); 
		perfil.setNomePerfil(perfil.getNomePerfil().toUpperCase());
		return super.persist();
	}
		 
	@Override
	public String update() {
		Perfil perfil = getInstance(); 
		perfil.setNomePerfil(perfil.getNomePerfil().toUpperCase());
		return super.update();
	}
}
