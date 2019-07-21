package br.com.gerenciadornet.dto;

import java.util.Date;

public class RelatorioProdutoListDTO {

	private String nomeProduto;
	private Integer codVenda;
	private Integer codCompra;
	private Integer qtdCompra;
	private Integer qtdEstoque;
	private Integer codCliente;
	private String nomeCliente;
	private String telefoneCliente;
	private String identificacaoLote;
	private Date dataValidade;
	private String nomeVendedor;
	private Date dataInicioVenda;
	private Integer qtdVendida;
	private Float descontoUnitario;
	private Float valorVendaProduto;
	private Float percDescontoTotalVenda;
	private Float valorTotalVenda;
	private Float valorProdutosVendidos;

	public RelatorioProdutoListDTO() {

	}

	public RelatorioProdutoListDTO(String novoNomeProduto, Integer novoCodVenda, Integer novoCodCompra,
			Integer novoQtdCompra, Integer novoQtdEstoque,
			Integer novoCodigoCliente, String novoNomeCliente, String novoTelefoneCliente, 
			String novoIdentificacaoLote,
			Date novoDataValidade, String novoNomeVendedor,
			Date novoDataInicioVenda, Integer novoQtdVendida,
			Float novoDescontoUnitario, Float novoValorVendaProduto,
			Float novoPercDescontoTotalVenda, Float novoValorTotalVenda) {
		super();
		nomeProduto = novoNomeProduto;
		codVenda = novoCodVenda;
		codCompra = novoCodCompra;
		qtdCompra = novoQtdCompra;
		qtdEstoque = novoQtdEstoque;
		codCliente = novoCodigoCliente;
		nomeCliente = novoNomeCliente;
		telefoneCliente = novoTelefoneCliente;
		identificacaoLote = novoIdentificacaoLote;
		dataValidade = novoDataValidade;
		nomeVendedor = novoNomeVendedor;
		dataInicioVenda = novoDataInicioVenda;
		qtdVendida = novoQtdVendida;
		descontoUnitario = novoDescontoUnitario;
		valorVendaProduto = novoValorVendaProduto;
		percDescontoTotalVenda = novoPercDescontoTotalVenda;
		valorTotalVenda = novoValorTotalVenda;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public Integer getCodVenda() {
		return codVenda;
	}

	public void setCodVenda(Integer novoCodVenda) {
		codVenda = novoCodVenda;
	}

	public Integer getCodCompra() {
		return codCompra;
	}

	public void setCodCompra(Integer novoCodCompra) {
		codCompra = novoCodCompra;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String novoNomeCliente) {
		nomeCliente = novoNomeCliente;
	}

	public String getNomeVendedor() {
		return nomeVendedor;
	}

	public void setNomeVendedor(String novoNomeVendedor) {
		nomeVendedor = novoNomeVendedor;
	}

	public Date getDataInicioVenda() {
		return dataInicioVenda;
	}

	public void setDataInicioVenda(Date novoDataInicioVenda) {
		dataInicioVenda = novoDataInicioVenda;
	}

	public Integer getQtdVendida() {
		return qtdVendida;
	}

	public void setQtdVendida(Integer novoQtdVendida) {
		qtdVendida = novoQtdVendida;
	}

	public Float getDescontoUnitario() {
		return descontoUnitario;
	}

	public void setDescontoUnitario(Float novoDescontoUnitario) {
		descontoUnitario = novoDescontoUnitario;
	}

	public Float getValorVendaProduto() {
		return valorVendaProduto;
	}

	public void setValorVendaProduto(Float novoValorVendaProduto) {
		valorVendaProduto = novoValorVendaProduto;
	}

	public Float getPercDescontoTotalVenda() {
		return percDescontoTotalVenda;
	}

	public void setPercDescontoTotalVenda(Float novoPercDescontoTotalVenda) {
		percDescontoTotalVenda = novoPercDescontoTotalVenda;
	}

	public Float getValorTotalVenda() {
		return valorTotalVenda;
	}

	public void setValorTotalVenda(Float novoValorTotalVenda) {
		valorTotalVenda = novoValorTotalVenda;
	}

	public String getIdentificacaoLote() {
		return identificacaoLote;
	}

	public void setIdentificacaoLote(String novoIdentificacaoLote) {
		identificacaoLote = novoIdentificacaoLote;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date novoDataValidade) {
		dataValidade = novoDataValidade;
	}

	public Integer getQtdCompra() {
		return qtdCompra;
	}

	public void setQtdCompra(Integer novoQtdCompra) {
		qtdCompra = novoQtdCompra;
	}

	public Integer getQtdEstoque() {
		return qtdEstoque;
	}

	public void setQtdEstoque(Integer novoQtdEstoque) {
		qtdEstoque = novoQtdEstoque;
	}

	public Integer getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public String getTelefoneCliente() {
		return telefoneCliente;
	}

	public void setTelefoneCliente(String telefoneCliente) {
		this.telefoneCliente = telefoneCliente;
	}

	public Float getValorProdutosVendidos() {
		return valorProdutosVendidos;
	}

	public void setValorProdutosVendidos(Float valorProdutosVendidos) {
		this.valorProdutosVendidos = valorProdutosVendidos;
	}
}
