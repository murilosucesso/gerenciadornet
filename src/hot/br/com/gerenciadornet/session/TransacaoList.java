package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("transacaoList")
public class TransacaoList extends EntityQuery<Transacao> {

	private static final String EJBQL = "select transacao from Transacao transacao";

	private static final String[] RESTRICTIONS = {
			"lower(transacao.siglaTransacao) like lower( concat('%', concat(#{transacaoList.transacao.siglaTransacao},'%')))",
			"lower(transacao.nome) like lower( concat('%', concat(#{transacaoList.transacao.nome},'%')))",
			"lower(transacao.descTransacao) like lower( concat('%', concat(#{transacaoList.transacao.descTransacao},'%')))", };

	private Transacao transacao = new Transacao();

	public TransacaoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("transacao.siglaTransacao");
		//setMaxResults(25);
	}

	public Transacao getTransacao() {
		return transacao;
	}
}
