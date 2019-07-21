package br.com.gerenciadornet.session;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.LoteProduto;

@SuppressWarnings("serial")
@Scope(ScopeType.PAGE)
@Name("loteProdutoPopUpList")
public class LoteProdutoPopUpList extends EntityQuery<LoteProduto> {
	
	private static final String EJBQL = "select loteProduto from LoteProduto loteProduto";
	
	private static final String[] RESTRICTIONS = {
		"lower(loteProduto.produto.nomeProduto) like lower ( concat('%', concat(#{loteProdutoPopUpList.loteProduto.produto.nomeProduto},'%')))",
		"lower(loteProduto.produto.codProdutoExterno) like lower ( concat('%', concat(#{loteProdutoPopUpList.loteProduto.produto.codProdutoExterno},'%')))",
		"lower(loteProduto.produto.descProduto) like lower (concat('%', concat(#{loteProdutoPopUpList.loteProduto.produto.descProduto},'%')))",
		"lower(loteProduto.identificacaoLote) like lower (concat('%', concat(#{loteProdutoPopUpList.loteProduto.identificacaoLote},'%')))",
		//"loteProduto.produto.marca.codMarca = #{loteProdutoPopUpList.loteProduto.produto.marca.codMarca}",
		"lower(loteProduto.produto.marca.nomeMarca) like lower (concat('%', concat(#{loteProdutoPopUpList.loteProduto.produto.marca.nomeMarca},'%')))",
		"loteProduto.qtdEstoque > #{loteProdutoPopUpList.qtdMinEstoque}",
		};
	/*
	private static final String[] RESTRICTIONS2 = {
		"lower(loteProduto.produto.nomeProduto) like lower ( concat('%', concat(#{loteProdutoPopUpList.pesquisaGeral},'%')))",
		"lower(loteProduto.produto.codProdutoExterno) like lower ( concat('%', concat(#{loteProdutoPopUpList.pesquisaGeral},'%')))",
		"lower(loteProduto.produto.descProduto) like lower (concat('%', concat(#{loteProdutoPopUpList.pesquisaGeral},'%')))",
		"lower(loteProduto.identificacaoLote) like lower (concat('%', concat(#{loteProdutoPopUpList.pesquisaGeral},'%')))",
		"lower(loteProduto.produto.marca.nomeMarca) like lower (concat('%', concat(#{loteProdutoPopUpList.pesquisaGeral},'%')))",
		//"loteProduto.qtdEstoque > #{loteProdutoPopUpList.qtdMinEstoque}",
		};*/

	private LoteProduto loteProduto = new LoteProduto();

	public LoteProdutoPopUpList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("loteProduto.produto.nomeProduto");		
		setMaxResults(100); 
	}
	
	private boolean listaVazia = false;
	
	/*@Override
    public List<LoteProduto> getResultList() {
		
		System.out.println( "\n\n pesquisaGeral : " + pesquisaGeral);
        if(this.pesquisaGeral != null && !"".equals(this.pesquisaGeral)){
        	setRestrictionLogicOperator("or");
            setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
        }else{
        	System.out.println( "\n\n operador : " + getRestrictionLogicOperator());
        	setRestrictionLogicOperator("and");
        	setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        }
        
        List<LoteProduto> listaRetorno =  super.getResultList();
        
        if(listaRetorno != null && listaRetorno.size() == 0){
        	listaVazia = true;
        } else {
        	listaVazia = false;
        }
        pesquisaGeral = null;
        return pesquisarLotesProdutos();
	}*/

	String ultimaPesquisa;
	
	/**
	 * Realiza a busca de lotes disponvível. Utilizado em nova Venda.
	 * Retorna apenas 100 resultados para otimizar a consulta e reduzir o tráfeco de dados.
	 * Substitui o método getResultList()
	 * Utilizado em LoteProdutoPopList.xhtml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LoteProduto> pesquisarLotesProdutos() {
		
		if(ultimaPesquisa == null){
			 if(this.pesquisaGeral == null || "".equals(this.pesquisaGeral)){
				 listaVazia = true;
				 return null;
			 }
		 } else {
			 if(this.pesquisaGeral == null || "".equals(this.pesquisaGeral)){
				 this.pesquisaGeral = ultimaPesquisa;
			 }
		 }
		 
		 StringBuilder sql = new StringBuilder( 
				 	"select loteProduto from LoteProduto loteProduto " +
					" left join fetch loteproduto.produto prod"+
					" left join fetch loteproduto.produto.marca marc"+
					" where "+
					" (lower(prod.nomeProduto) like lower ( concat('%', concat( :pesquisaGeral1 ,'%')))" +
					" or lower(prod.codProdutoExterno) like lower ( concat('%', concat( :pesquisaGeral1 ,'%')))" +
					" or prod.codigoBarrasProduto = :pesquisaGeral1" +
					" or lower(marc.nomeMarca) like lower ( concat('%', concat( :pesquisaGeral1 ,'%')))" +
					" or lower(loteProduto.identificacaoLote) like lower ( concat('%', concat( :pesquisaGeral1 ,'%')))" +
					" or lower(prod.descProduto) like lower ( concat('%', concat( :pesquisaGeral1 ,'%')))" + 
					" ) and loteProduto.qtdEstoque > 0 "+
					" and prod.inExclusao = 0  "+
					" order by prod.nomeProduto , loteProduto.dataValidade"
					);
			
		 Query query = getEntityManager().createQuery(sql.toString());
		 query.setMaxResults(100);
		 query.setParameter("pesquisaGeral1", pesquisaGeral);
		 
		 List<LoteProduto> lotes = (List<LoteProduto>)query.getResultList();
		 
		if(lotes != null && lotes.size() == 0){
		    listaVazia = true;
		} else {
			listaVazia = false;
		}
		
		ultimaPesquisa = pesquisaGeral;
		 
		return lotes;
	 }
	
	private String pesquisaGeral;

	public LoteProduto getLoteProduto() {
		return loteProduto;
	}

	private Integer qtdMinEstoque = 0;
	private boolean mostrarListaLotesProduto = false;
		
	public Integer getQtdMinEstoque() {
		return qtdMinEstoque;
	}

	public void setQtdMinEstoque(Integer qtdMinEstoque) {
		this.qtdMinEstoque = qtdMinEstoque;
	}	
	
	/**
	 * Para trazer a lista somente quando vier dados do filtro.
	 */
	public void pesquisaLotesProduto() {
		setMostrarListaLotesProduto(true);		
	}

	public boolean getMostrarListaLotesProduto() {
		return mostrarListaLotesProduto;
	}

	public void setMostrarListaLotesProduto(boolean mostrarListaLotesProduto) {
		this.mostrarListaLotesProduto = mostrarListaLotesProduto;
	}
	
	public String getPesquisaGeral()
	{
		return pesquisaGeral;
	}

	public void setPesquisaGeral(String pesquisaGeral)
	{
		this.pesquisaGeral = pesquisaGeral;
	}

	public boolean getListaVazia()
	{
		return listaVazia;
	}

	public void setListaVazia(boolean listaVazia)
	{
		this.listaVazia = listaVazia;
	}
}
