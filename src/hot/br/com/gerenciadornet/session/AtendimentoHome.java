package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Atendimento;
import br.com.gerenciadornet.entity.Usuario;

@SuppressWarnings("serial")
@Name("atendimentoHome")
public class AtendimentoHome extends EntityHome<Atendimento> {
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	@Override
	public String persist() {
		
		Atendimento atendimento = getInstance();
		atendimento.setDataAtendimento(new Date(System.currentTimeMillis()));
		atendimento.setUsuario(user);
		
		return super.persist();
	}
	
	
	public void setAtendimentoCodAtendimento(Integer id) {
		setId(id);
	}

	public Integer getAtendimentoCodAtendimento() {
		return (Integer) getId();
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

	public Atendimento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Override
	protected Atendimento createInstance() {
		Atendimento atendimento = new Atendimento();
		atendimento.setStatusAtendimento(1);
		return atendimento;
	}

}
