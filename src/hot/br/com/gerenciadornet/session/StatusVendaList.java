package br.com.gerenciadornet.session;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.StatusVenda;

@Name("statusVendaList")
public class StatusVendaList extends EntityQuery<StatusVenda> {

    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select statusVenda from StatusVenda statusVenda";

    private static final String[] RESTRICTIONS = {};

    private StatusVenda statusVenda = new StatusVenda();

    public StatusVendaList() {
	setEjbql(EJBQL);
	setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	setOrderColumn("statusVenda.descStatusVenda");
    }

    public StatusVenda getStatusVenda() {
	return statusVenda;
    }
}
