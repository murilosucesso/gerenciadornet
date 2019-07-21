package br.com.gerenciadornet.session;

import java.util.Arrays;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.TipoDebito;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("tipoDebitoList")
public class TipoDebitoList extends EntityQuery<TipoDebito> {

	private static final String EJBQL = "select tipoDebito from TipoDebito tipoDebito";
	
	private static final String[] RESTRICTIONS = { 
		"lower(tipoDebito.descTipoDebito) like lower( concat('%', concat(#{tipoDebitoList.tipoDebito.descTipoDebito},'%')))", 
		"tipoDebito.inExclusao = #{tipoDebitoList.tipoDebito.inExclusao}",
		};

	private TipoDebito tipoDebito = new TipoDebito();

	public TipoDebitoList() {
		tipoDebito.setInExclusao(false);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("tipoDebito.descTipoDebito");
	}

	public TipoDebito getTipoDebito() {
		return tipoDebito;
	}
}
