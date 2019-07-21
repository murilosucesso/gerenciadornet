package br.com.gerenciadornet.session;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Anotacao;
import br.com.gerenciadornet.entity.Usuario;

@SuppressWarnings("serial")
@Name("anotacaoHome")
public class AnotacaoHome extends EntityHome<Anotacao> {

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public AnotacaoHome() {
		
	}
	
	public void setAnotacaoCodAnotacao(Integer id) {
		setId(id);
	}

	public Integer getAnotacaoCodAnotacao() {
		return (Integer) getId();
	}

	@Override
	public boolean isIdDefined() {
		return true;
	}

	@Override
	protected Anotacao createInstance() {
		Anotacao anotacao = new Anotacao();
		return anotacao;
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

	public Anotacao getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	

}
