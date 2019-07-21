package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.ProdutoDTO;
import br.com.gerenciadornet.dto.RelatorioProdutoListDTO;
import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Constantes;

@Name("relatorioProdutoList")
@Scope(ScopeType.PAGE)
public class RelatorioProdutoList extends EntityQuery<RelatorioProdutoListDTO> {

	private static final long serialVersionUID = 1L;

	// Campos do filtro de pesquisa
	private String codigoProdutoExterno;
	private String nomeProdutoPesquisado;
	private Integer codigoCompra;
	private Integer codMarca;
	private ProdutoDTO produto 			= new ProdutoDTO();
	private Cliente cliente 			= new Cliente();
	private Date dataInicioPesquisa 	= new Date(System.currentTimeMillis());
	private Date dataFimPesquisa 		= new Date(System.currentTimeMillis());
	private Integer numeroLinhasDT 		= 50;
	private boolean mostrarResultados	= false;
	private Float valorTotalProdutosVendidos	= new Float(0);
	
	private Usuario vendedor = new Usuario();
	private Usuario vendedorResponsavel = new Usuario();
	
	private String orderByAscDesc = "";
	

	Integer qtdResultados = 0;
	Integer qtdTotal = 0;

	private List<Produto> produtos = new ArrayList<Produto>(0);

	@In
	EntityManager entityManager;

	@In(create = true)
	ProdutoList produtoList;

	private static final String EJBQL = "select venda from Venda venda";

	public RelatorioProdutoList() {
		setEjbql(EJBQL);
	}

	List<RelatorioProdutoListDTO> listaLotesVendidos;
	
	
	/**
	 * Utilizado inicialmente para listar os produtos vendidos
	 * da lista de produtos vencidos.
	 * @param codProduto
	 */
	public void listarProdutosVendidos(Integer codProduto) {
		
		produto.setCodProduto(codProduto);
		
		//Seta a data inicial pra 10 Anos atrás
		setDataInicioPesquisa(new Date(System.currentTimeMillis() - (10 * 365 * 86400000l)));
		setDataFimPesquisa(new Date(System.currentTimeMillis()));
		orderByAscDesc = " desc";
		 listarProdutosVendidos();
	}
	
	/**
	 * Seta o nome do produto que foi pesquisado na lista de produtos
	 * vencidos. 
	 * 
	 * @param nome
	 */
	public void gravaNomeProdutoPesquisado(String nome){
		
		setNomeProdutoPesquisado(nome);
		
	}

	/**
	 * Lista os produtos vendidos de acordo com o filtro selecionado.
	 */
	public void listarProdutosVendidos() {

		Integer statusVendaCancela = Constantes.CANCELADA;
		Integer statusVendaOrcamento = Constantes.ORCAMENTO;
		
		boolean indicadorOrcamento = true;
		qtdTotal = 0;
		valorTotalProdutosVendidos = 0f;
		
		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.dto.RelatorioProdutoListDTO(p.nomeProduto, ");
		sql.append("v.codVenda, cp.codCompra, lp.qtdCompra, lp.qtdEstoque,  c.codCliente,  c.nome, c.telefone,  lp.identificacaoLote, lp.dataValidade, u.nomeUsuario, ");
		sql.append("v.dataInicioVenda, pv.qtd, pv.desconto, pv.precoVenda, v.descontoTotal, v.valorTotalVenda) ");
		sql.append("from Venda v, Usuario u, ProdutoVendido pv, LoteProduto lp, Produto p, Cliente c, StatusVenda sv, Compra cp ");
		sql.append("where v.usuario.codUsuario = u.codUsuario ");
		sql.append("and v.codVenda = pv.venda.codVenda ");
		sql.append("and v.statusVenda.codStatusVenda = sv.codStatusVenda ");
		sql.append("and lp.compra.codCompra = cp.codCompra ");
		sql.append("and pv.loteProduto.codLoteProduto = lp.codLoteProduto ");
		sql.append("and p.codProduto = lp.produto.codProduto ");
		sql.append("and v.cliente.codCliente = c.codCliente ");

		if (getCodigoProdutoExterno() != null && !"".equals(getCodigoProdutoExterno().trim())) {
			sql.append("and lower(p.codProdutoExterno) like lower ( concat('%', concat(#{relatorioProdutoList.codigoProdutoExterno},'%')))");
			
		} else if (produto.getCodProduto() != null ){
			sql.append("and p.codProduto = #{relatorioProdutoList.produto.getCodProduto()} ");
		}

		if (getCodigoCompra() != null && !"".equals(getCodigoCompra().toString().trim())) {
			sql.append("and cp.codCompra = #{relatorioProdutoList.codigoCompra} ");
		}

		if (cliente != null && cliente.getCodCliente() != null) {
			sql.append("and v.cliente.codCliente = #{relatorioProdutoList.cliente.codCliente} ");
		}
		
		if(getCodMarca() != null ){
			sql.append("and p.marca.codMarca = #{relatorioProdutoList.getCodMarca()} ");
		}

		// Para pesquisar vendas que nao forma canceladas, nem orcamentos
		sql.append("and v.statusVenda.codStatusVenda <> :statusVendaCancela ");
		sql.append("and v.statusVenda.codStatusVenda <> :statusVendaOrcamento ");
		sql.append("and v.inOrcamento <> :indicadorOrcamento ");

		sql.append("and v.dataInicioVenda >= :dataInicio ");
		sql.append("and v.dataInicioVenda <= :dataFim ");
		sql.append("order by v.codVenda" + orderByAscDesc);

		@SuppressWarnings("unchecked")
		List<RelatorioProdutoListDTO> relatProdList = (List<RelatorioProdutoListDTO>) entityManager
				.createQuery(sql.toString())
				.setParameter("statusVendaCancela", statusVendaCancela)
				.setParameter("statusVendaOrcamento", statusVendaOrcamento)
				.setParameter("indicadorOrcamento", indicadorOrcamento)
				.setParameter("dataInicio", getDataInicioPesquisa())
				.setParameter("dataFim", getDataFimPesquisa()).getResultList();


		for (RelatorioProdutoListDTO vendas : relatProdList) {
			
			//Calcula o valor total dos produtos vendidos com o desconto da venda.
			Float valorComDescontoPercentual = vendas.getValorVendaProduto() - (vendas.getValorVendaProduto() *  vendas.getPercDescontoTotalVenda()/100);
			
			//Subtrai o valor total do produtos vendidos com os descontos unitários
			Float valorTotalProdAux =  (vendas.getQtdVendida() * valorComDescontoPercentual) - vendas.getDescontoUnitario();

			vendas.setValorProdutosVendidos( valorTotalProdAux );
			
			valorTotalProdutosVendidos += vendas.getValorProdutosVendidos(); 
					
			qtdTotal += vendas.getQtdVendida();
		}

		qtdResultados = relatProdList.size();

		listaLotesVendidos = relatProdList;
	}

	public Date getDataInicioPesquisa() {
		if (dataInicioPesquisa != null) {
			return new Date(dataInicioPesquisa.getTime());
		}
		return dataInicioPesquisa;
	}

	/**
	 * adiciona um dia na pesquisa final para a data fim ser incluida na
	 * pesquisa
	 */
	public Date getDataFimPesquisa() {
		if (dataFimPesquisa != null) {

			return new Date(dataFimPesquisa.getTime() + 86400000l);
		}
		return dataFimPesquisa;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}

	/** Evita que a consulta seja executada ao carregar a página inicial */
	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public ProdutoDTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoDTO novoProduto) {
		produto = novoProduto;
	}

	public void setDataInicioPesquisa(Date novoDataInicioPesquisa) {
		dataInicioPesquisa = novoDataInicioPesquisa;
	}

	public void setDataFimPesquisa(Date novoDataFimPesquisa) {
		dataFimPesquisa = novoDataFimPesquisa;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> novoProdutos) {
		produtos = novoProdutos;
	}

	public ProdutoList getProdutoList() {
		return produtoList;
	}

	public void setProdutoList(ProdutoList novoProdutoList) {
		produtoList = novoProdutoList;
	}

	public Integer getQtdResultados() {
		return qtdResultados;
	}

	public String getCodigoProdutoExterno() {
		return codigoProdutoExterno;
	}

	public void setCodigoProdutoExterno(String novoCodigoProdutoExterno) {
		codigoProdutoExterno = novoCodigoProdutoExterno;
	}

	public Integer getQtdTotal() {
		return qtdTotal;
	}

	public List<RelatorioProdutoListDTO> getListaLotesVendidos() {
		return listaLotesVendidos;
	}

	public Integer getCodigoCompra() {
		return codigoCompra;
	}

	public void setCodigoCompra(Integer novoCodigoCompra) {
		codigoCompra = novoCodigoCompra;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente novoCliente) {
		cliente = novoCliente;
	}

	public Integer getCodMarca() {
		return codMarca;
	}

	public void setCodMarca(Integer codMarca) {
		this.codMarca = codMarca;
	}


	public String getNomeProdutoPesquisado() {
		return nomeProdutoPesquisado;
	}


	public void setNomeProdutoPesquisado(String nomeProdutoPesquisado) {
		this.nomeProdutoPesquisado = nomeProdutoPesquisado;
	}

	public Float getValorTotalProdutosVendidos() {
		return valorTotalProdutosVendidos;
	}

	public void setValorTotalProdutosVendidos(Float valorTotalProdutosVendidos) {
		this.valorTotalProdutosVendidos = valorTotalProdutosVendidos;
	}
	
	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	public Usuario getVendedorResponsavel() {
		return vendedorResponsavel;
	}

	public void setVendedorResponsavel(Usuario vendedorResponsavel) {
		this.vendedorResponsavel = vendedorResponsavel;
	}
}
