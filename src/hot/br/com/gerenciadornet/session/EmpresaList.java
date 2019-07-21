package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("empresaList")
public class EmpresaList extends EntityQuery<Empresa> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select empresa from Empresa empresa";

	private static final String[] RESTRICTIONS = {};

	private Empresa empresa = new Empresa();

	public EmpresaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Empresa getEmpresa() {
		return empresa;
	}
}
