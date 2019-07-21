package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import br.com.gerenciadornet.dto.TotaisEstoqueDTO;

@Name("loteProdutoTotaisList")
public class LoteProdutoTotaisList{

	@In
	EntityManager entityManager;
	
	TotaisEstoqueDTO totaisEstoqueDTO;
	List<TotaisEstoqueDTO> totaisEstoqueDTOListMarca;
	List<TotaisEstoqueDTO> totaisEstoqueDTOListCategoria;
	
	@Create
	public void carregar(){
		carregarTotalEstoque();
		carregarTotalEstoquePorMarca();
		carregarTotalEstoquePorCategoria();
	}
	
	/**
	 * Realiza a consulta de totalização do estoque
	 * retornando preço de venda e custo totais do estoque
	 * @return
	 */
	
	public void carregarTotalEstoque() {
		
		try{
		 totaisEstoqueDTO = (TotaisEstoqueDTO)entityManager.createQuery(
					"select new br.com.gerenciadornet.dto.TotaisEstoqueDTO(sum(loteProduto.qtdEstoque), sum(loteProduto.precoCompraUnidade * loteProduto.qtdEstoque), sum(loteProduto.precoVendaUnidade * loteProduto.qtdEstoque) )" +
					"from LoteProduto loteProduto")
					.getSingleResult();
		}catch(Exception e){
			totaisEstoqueDTO = new TotaisEstoqueDTO(0 , 0.0d, 0.0d);
		}
	 }
	
	/**
	 * Realiza a consulta de totalização do estoque
	 * retornando preço de venda e custo totais do estoque
	 * agrupados por Marca
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void carregarTotalEstoquePorMarca() {
		
		try{
			totaisEstoqueDTOListMarca = (List<TotaisEstoqueDTO>)entityManager.createQuery(
					"select new br.com.gerenciadornet.dto.TotaisEstoqueDTO(sum(loteProduto.qtdEstoque), sum(loteProduto.precoCompraUnidade * loteProduto.qtdEstoque),"+
					"sum(loteProduto.precoVendaUnidade * loteProduto.qtdEstoque), marca.nomeMarca )" +
					"from LoteProduto loteProduto, Produto produto, Marca marca " +
					"where produto.codProduto = loteProduto.produto.codProduto and marca.codMarca = produto.marca.codMarca " +
					"group by produto.marca.codMarca order by produto.marca.nomeMarca")
					.getResultList();
		}catch(Exception e){
			totaisEstoqueDTOListMarca = new ArrayList<TotaisEstoqueDTO>();
		}
	 }
	
	public String graficoTotalEstoquePorMarca(){
		
		StringBuilder dados = new StringBuilder();
		
		
		dados.append("data.addColumn('string', 'Marca');");
		dados.append("data.addColumn('number', 'Valor');");
		dados.append("data.addRows("+ totaisEstoqueDTOListMarca.size() +");");
		
		int coluna = 0;
		
		Locale loc = new Locale("pt", "BR");
        Locale.setDefault(loc);
       
		
		for(TotaisEstoqueDTO totaisEstoque : totaisEstoqueDTOListMarca){
					        
			String total = new java.text.DecimalFormat("0.00").format(totaisEstoque.getTotalEstoquePrecoCusto());
			dados.append("data.setValue("+ coluna + ", 0,'" + totaisEstoque.getMarca() + "');");
			dados.append("data.setValue("+ coluna + ", 1, " + total + ");");
			coluna++;
		}
		
		return dados.toString();
	}
	
	@SuppressWarnings("unchecked")
	public void carregarTotalEstoquePorCategoria() {
		
		try{
			totaisEstoqueDTOListCategoria = (List<TotaisEstoqueDTO>)entityManager.createQuery(
					"select new br.com.gerenciadornet.dto.TotaisEstoqueDTO(sum(loteProduto.qtdEstoque), sum(loteProduto.precoCompraUnidade * loteProduto.qtdEstoque),"+
					"sum(loteProduto.precoVendaUnidade * loteProduto.qtdEstoque), categoria.nomeCategoria )" +
					"from LoteProduto loteProduto, Produto produto, Categoria categoria " +
					"where produto.codProduto = loteProduto.produto.codProduto and categoria.codCategoria = produto.categoria.codCategoria " +
					"group by produto.categoria.codCategoria order by produto.categoria.nomeCategoria")
					.getResultList();
		}catch(Exception e){
			totaisEstoqueDTOListCategoria = new ArrayList<TotaisEstoqueDTO>();
		}
	 }

	public TotaisEstoqueDTO getTotaisEstoqueDTO()
	{
		return totaisEstoqueDTO;
	}

	public void setTotaisEstoqueDTO(TotaisEstoqueDTO totaisEstoqueDTO)
	{
		this.totaisEstoqueDTO = totaisEstoqueDTO;
	}

	public List<TotaisEstoqueDTO> getTotaisEstoqueDTOListMarca()
	{
		return totaisEstoqueDTOListMarca;
	}

	public void setTotaisEstoqueDTOListMarca(List<TotaisEstoqueDTO> totaisEstoqueDTOListMarca)
	{
		this.totaisEstoqueDTOListMarca = totaisEstoqueDTOListMarca;
	}

	public List<TotaisEstoqueDTO> getTotaisEstoqueDTOListCategoria()
	{
		return totaisEstoqueDTOListCategoria;
	}

	public void setTotaisEstoqueDTOListCategoria(List<TotaisEstoqueDTO> totaisEstoqueDTOListCategoria)
	{
		this.totaisEstoqueDTOListCategoria = totaisEstoqueDTOListCategoria;
	}

}
