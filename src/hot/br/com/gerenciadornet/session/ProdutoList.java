package br.com.gerenciadornet.session;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.ProdutoDTO;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Produto;

@SuppressWarnings("serial")
@Name("produtoList")
public class ProdutoList extends EntityQuery<Produto> {

	private static final String EJBQL = "select produto from Produto produto"
	    + " left join fetch produto.marca"
	    + " left join fetch produto.categoria"
	    ;

	@In
	EntityManager entityManager;
	
	private static final String[] RESTRICTIONS = {
			"lower(produto.nomeProduto) like lower ( concat('%', concat(#{produtoList.produto.nomeProduto},'%')))",
			"lower(produto.codProdutoExterno) like lower ( concat('%', concat(#{produtoList.produto.codProdutoExterno}, '%')))",
			"lower(produto.descProduto) like lower (concat('%', concat(#{produtoList.produto.descProduto},'%')))",
			"lower(produto.ncm) like lower (concat('%', concat(#{produtoList.produto.ncm},'%')))",
			"produto.codigoBarrasProduto = #{produtoList.produto.codigoBarrasProduto}",
			"produto.marca.fornecedor.codFornecedor = #{produtoList.fornecedor.codFornecedor}",
			"produto.marca.codMarca = #{produtoList.produto.marca.codMarca}", 						
			"produto.inExclusao = #{produtoList.produto.inExclusao}",
			};

	private Produto produto = new Produto();
	
	private boolean mostrarResultados = false; 
	
	private Fornecedor fornecedor = new Fornecedor();
	
	private Integer numeroLinhasDT 	= 20;
	
	public ProdutoList() {
		produto.setInExclusao(false);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("produto.nomeProduto");
		
	}

	public Produto getProduto() {
		return produto;
	}
	
	/**
	 * Lista todos os produtos de uma marca para montar o select do filtro.
	 * 
	 * @return
	 */
	public List<ProdutoDTO> listarProdutos(Integer codMarca) {
		
		if(codMarca == 0 || codMarca == null ){
			return null;
		}

		boolean inExclusao = false;

		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.dto.ProdutoDTO(");
		sql.append("p.codProduto, p.nomeProduto, p.codigoBarrasProduto, m.nomeMarca) ");
		sql.append("from Produto p, Marca m ");
		sql.append("where p.marca.codMarca = m.codMarca and p.inExclusao =  :inExclusao ");
		sql.append("and p.marca.codMarca = :codMarca ");
		sql.append("order by p.nomeProduto");

		@SuppressWarnings("unchecked")
		List<ProdutoDTO> produtoList = (List<ProdutoDTO>) entityManager
				.createQuery(sql.toString())
				.setParameter("inExclusao", inExclusao)
				.setParameter("codMarca", codMarca).getResultList();

		return produtoList;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public boolean getMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public Integer getNumeroLinhasDT() {
	    return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer novoNumeroLinhasDT) {
	    numeroLinhasDT = novoNumeroLinhasDT;
	}
		
}
