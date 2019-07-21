package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("produtoVendidoList")
public class ProdutoVendidoList extends EntityQuery<ProdutoVendido> {

	private static final String EJBQL = "select produtoVendido from ProdutoVendido produtoVendido";

	private static final String[] RESTRICTIONS = {};

	private ProdutoVendido produtoVendido;

	public ProdutoVendidoList() {
		produtoVendido = new ProdutoVendido();
		produtoVendido.setId(new ProdutoVendidoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ProdutoVendido getProdutoVendido() {
		return produtoVendido;
	}
}
