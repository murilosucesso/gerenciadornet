package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("servicoRealizadoList")
public class ServicoRealizadoList extends EntityQuery<ServicoRealizado> {

	private static final String EJBQL = "select servicoRealizado from ServicoRealizado servicoRealizado";

	private static final String[] RESTRICTIONS = {};

	private ServicoRealizado servicoRealizado = new ServicoRealizado();

	public ServicoRealizadoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ServicoRealizado getServicoRealizado() {
		return servicoRealizado;
	}
}
