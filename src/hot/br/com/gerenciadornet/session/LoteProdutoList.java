package br.com.gerenciadornet.session;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.LoteProduto;

@SuppressWarnings("serial")
@Name("loteProdutoList")
public class LoteProdutoList extends EntityQuery<LoteProduto> {

	private static final String EJBQL = "select loteProduto from LoteProduto loteProduto";

	private static final String[] RESTRICTIONS = { 
	    "loteProduto.compra.codCompra = #{loteProdutoList.codCompra}",
		"lower(loteProduto.identificacaoLote) like lower(concat(#{loteProdutoList.loteProduto.identificacaoLote},'%'))",
		"lower(loteProduto.produto.codProdutoExterno) like lower ( concat('%', concat(#{loteProdutoList.loteProduto.produto.codProdutoExterno},'%')))",
		"loteProduto.produto.codigoBarrasProduto = #{loteProdutoList.loteProduto.produto.codigoBarrasProduto}",
		
		//"lower(loteProduto.produto.marca.nomeMarca) like lower ( concat('%', concat(#{loteProdutoList.loteProduto.produto.marca.nomeMarca},'%')))",
		"lower(loteProduto.produto.nomeProduto) like lower ( concat('%', concat(#{loteProdutoList.loteProduto.produto.nomeProduto},'%')))",
		
		//Substituído a pesquisa por nome pela pesquisa por código
		"loteProduto.produto.codProduto = #{loteProdutoList.loteProduto.produto.codProduto}",
		"loteProduto.produto.marca.codMarca = #{loteProdutoList.codMarca}",
		
		"loteProduto.qtdEstoque < #{loteProdutoList.loteProduto.qtdEstoque}",		
		"loteProduto.qtdEstoque >= #{loteProdutoList.loteProduto.qtdMinimaEstoque}",
		
	}; 
	
	private Integer numeroLinhasDT = 15;
	private boolean mostrarResultados = false;
	private Integer codCompra;
	private Integer codMarca;
	
	private LoteProduto loteProduto = new LoteProduto(1, new Date());

	public LoteProdutoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("loteProduto.produto.nomeProduto");	
		//setMaxResults(numeroLinhasDT);
	}
	
	Float precoCustoTotal = new Float(0);
	Float precoVendaTotal = new Float(0);

	int quantidadeCompradaTotal;
	int quantidadeEstoqueTotal;
	
	@Override
	public List<LoteProduto> getResultList() {

		Float precoCustoTotalAux = new Float(0);
		Float precoVendaTotalAux = new Float(0);
		int quantidadeCompradaAux = 0;
		int quantidadeEstoqueAux = 0;

		List<LoteProduto> loteProdutoList = super.getResultList();
		
		for (LoteProduto loteProdutoAux : loteProdutoList) {								
				precoCustoTotalAux += loteProdutoAux.getQtdEstoque() * loteProdutoAux.getPrecoCompraUnidade();
				precoVendaTotalAux += loteProdutoAux.getQtdEstoque() * loteProdutoAux.getPrecoVendaUnidade();
				quantidadeCompradaAux += loteProdutoAux.getQtdCompra();
				quantidadeEstoqueAux += loteProdutoAux.getQtdEstoque();
		}
		
		setPrecoCustoTotal(precoCustoTotalAux);
		setPrecoVendaTotal(precoVendaTotalAux);
		setQuantidadeCompradaTotal(quantidadeCompradaAux);
		setQuantidadeEstoqueTotal(quantidadeEstoqueAux);
		
		return loteProdutoList;
		
	}
	
	public LoteProduto getLoteProduto() {
		return loteProduto;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}
	public boolean getMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public Integer getCodCompra() {
	    return codCompra;
	}

	public void setCodCompra(Integer novoCodCompra) {
	    codCompra = novoCodCompra;
	}

	public Integer getCodMarca() {
		return codMarca;
	}

	public void setCodMarca(Integer codMarca) {		
		this.codMarca = codMarca;
	}
	
	public Float getPrecoCustoTotal() {
		return precoCustoTotal;
	}

	public void setPrecoCustoTotal(Float precoCustoTotal) {
		this.precoCustoTotal = precoCustoTotal;
	}

	public Float getPrecoVendaTotal() {
		return precoVendaTotal;
	}

	public void setPrecoVendaTotal(Float precoVendaTotal) {
		this.precoVendaTotal = precoVendaTotal;
	}

	public int getQuantidadeCompradaTotal() {
		return quantidadeCompradaTotal;
	}

	public void setQuantidadeCompradaTotal(int quantidadeCompradaTotal) {
		this.quantidadeCompradaTotal = quantidadeCompradaTotal;
	}

	public int getQuantidadeEstoqueTotal() {
		return quantidadeEstoqueTotal;
	}

	public void setQuantidadeEstoqueTotal(int quantidadeEstoqueTotal) {
		this.quantidadeEstoqueTotal = quantidadeEstoqueTotal;
	}
}
