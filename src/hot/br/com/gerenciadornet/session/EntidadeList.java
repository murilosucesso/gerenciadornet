package br.com.gerenciadornet.session;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Entidade;

@SuppressWarnings("serial")
@Name("entidadeList")
public class EntidadeList extends EntityQuery<Entidade> {
	
	private static final String EJBQL = "select entidade from Entidade entidade";

	private static final String[] RESTRICTIONS = {
			};
	
	public EntidadeList() {			
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("entidade.nome");
	}
}

