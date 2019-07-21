package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("marcaList")
public class MarcaList extends EntityQuery<Marca> {

	private static final String EJBQL = "select marca from Marca marca";

	private static final String[] RESTRICTIONS = { 
		"lower(marca.nomeMarca) like lower(concat('%', concat(#{marcaList.marca.nomeMarca},'%')))", 
		"marca.fornecedor.codFornecedor = #{marcaList.marca.fornecedor.codFornecedor}",
		"marca.inExclusao = #{marcaList.marca.inExclusao}",
		};

	private Marca marca = new Marca();

	public MarcaList() {
		marca.setInExclusao(false);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));	
		setOrderColumn("marca.nomeMarca");
	}

	public Marca getMarca() {
		return marca;
	}
}
