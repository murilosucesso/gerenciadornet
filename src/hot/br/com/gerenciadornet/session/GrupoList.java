package br.com.gerenciadornet.session;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Grupo;

@SuppressWarnings("serial")
@Name("grupoList")
public class GrupoList extends EntityQuery<Grupo> {
	
	private static final String EJBQL = "select grupo from Grupo grupo";

	private static final String[] RESTRICTIONS = {
			};
	
	public GrupoList() {			
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("grupo.nomeGrupo");
	}
}
