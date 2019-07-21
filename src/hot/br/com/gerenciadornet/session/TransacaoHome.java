package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@SuppressWarnings("serial")
@Name("transacaoHome")
public class TransacaoHome extends EntityHome<Transacao> {

	public void setTransacaoCodTransacao(Integer id) {
		setId(id);
	}

	public Integer getTransacaoCodTransacao() {
		return (Integer) getId();
	}

	@Override
	protected Transacao createInstance() {
		Transacao transacao = new Transacao();
		return transacao;
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

	public Transacao getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@Override
	public String persist() {
		Transacao transacao = getInstance();
		transacao.setNome(transacao.getNome().toUpperCase());
		return super.persist();
	}
}
