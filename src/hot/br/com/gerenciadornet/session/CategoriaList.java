package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("categoriaList")
public class CategoriaList extends EntityQuery<Categoria> {

	private static final String EJBQL = "select categoria from Categoria categoria";

	private static final String[] RESTRICTIONS = { 
		"lower(categoria.nomeCategoria) like lower(concat('%',concat(#{categoriaList.categoria.nomeCategoria},'%')))", 
		"categoria.inExclusao = #{categoriaList.categoria.inExclusao}",
		};

	private Categoria categoria = new Categoria();

	public CategoriaList() {
		categoria.setInExclusao(false);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("categoria.nomeCategoria");
	}

	public Categoria getCategoria() {
		return categoria;
	}
}
