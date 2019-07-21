package br.com.gerenciadornet.session;

import java.util.Arrays;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.FontePagadora;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("fontePagadoraList")
public class FontePagadoraList extends EntityQuery<FontePagadora> {

	private static final String EJBQL = "select fontePagadora from FontePagadora fontePagadora";
	
	private static final String[] RESTRICTIONS = { 
		"lower(fontePagadora.descFontePagadora) like lower( concat('%', concat(#{fontePagadoraList.fontePagadora.descFontePagadora},'%')))", 
		//"fontePagadora.inExclusao = #{fontePagadoraList.fontePagadora.inExclusao}",
		};

	private FontePagadora fontePagadora = new FontePagadora();

	public FontePagadoraList() {		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("fontePagadora.descFontePagadora");
	}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}
}
