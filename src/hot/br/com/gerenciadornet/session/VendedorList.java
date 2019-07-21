package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("vendedorList")
public class VendedorList extends EntityQuery<Vendedor> {

	private static final String EJBQL = "select vendedor from Vendedor vendedor";

	private static final String[] RESTRICTIONS = {
			"lower(vendedor.nomeVendedor) like lower(concat(#{vendedorList.vendedor.nomeVendedor},'%'))",
			"lower(vendedor.emailVendedor) like lower(concat(#{vendedorList.vendedor.emailVendedor},'%'))",
			"vendedor.fornecedor.codFornecedor  = #{vendedorList.vendedor.fornecedor.codFornecedor}",
			"lower(vendedor.telefoneVendedor) like lower(concat(#{vendedorList.vendedor.telefoneVendedor},'%'))",
			"vendedor.inExclusao = #{vendedorList.vendedor.inExclusao}",
			};

	private Vendedor vendedor;

	public VendedorList() {
		vendedor = new Vendedor();
		//para na consulta s� trazer os ativos
		vendedor.setInExclusao(false);
		vendedor.setId(new VendedorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("vendedor.nomeVendedor");
	}

	public Vendedor getVendedor() {
		return vendedor;
	}
}
