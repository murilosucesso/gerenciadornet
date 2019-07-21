package br.com.gerenciadornet.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Perfil;

@SuppressWarnings("serial")
@Name("perfilList")
public class PerfilList extends EntityQuery<Perfil> {

	private static final String EJBQL = "select perfil from Perfil perfil";

	//private static final String[] RESTRICTIONS = { "lower(categoria.nomeCategoria) like lower(concat('%',concat(#{categoriaList.categoria.nomeCategoria},'%')))", };

	private Perfil perfil = new Perfil();

	public PerfilList() {
		setEjbql(EJBQL);
		//setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}

	public Perfil getPerfil() {
		return perfil;
	}
}
