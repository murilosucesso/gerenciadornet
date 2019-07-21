package br.com.gerenciadornet.dto;

import java.util.Date;

public class RelatorioDetalhadoFuncionarioDTO {

	private Integer codVenda;
	private Date dataVenda;
	private String nomeVendedor;
	private Integer codCliente;
	private String nomeCliente;
	private String marca;
	private String nomeProdutoServico;
	private Float precoCustoProdutoServico;
	private Float precoVendaProdutoServico;
	private Float precoVendidoProdutoServico;
	private Integer quantidade;
	private Float descontoUnitario;
	private Float percDescontoTotalVenda;
	private Float valorTotalVenda;
	private Float lucro;
	private Float comissaoDefault;//valor default da comissão
	private Float comissaoEfetiva; //Valor que foi pago pela comissão 
	private boolean alteracaoComissao = false;
			
	/**
	 * Construtor de produtos
	 */
	public RelatorioDetalhadoFuncionarioDTO(Integer codVenda, Date dataVenda, String nomeVendedor, 
			Integer codCliente, String nomeCliente,
			Float precoVendidoProdutoServico, Integer quantidade, Float descontoUnitario,
			Float precoCustoProdutoServico, Float precoVendaProdutoServico,
			String marca,
			String nomeProdutoServico, Float comissaoDefault, Float comissaoEfetiva,
			Float valorTotalVenda, Float percDescontoTotalVenda,
			Float lucro
			) {

		super();
		this.codVenda = codVenda;
		this.dataVenda = dataVenda;
		this.nomeVendedor = nomeVendedor;
		this.codCliente = codCliente;
		this.nomeCliente = nomeCliente;
		this.precoVendidoProdutoServico = precoVendidoProdutoServico;
		this.quantidade = quantidade;
		this.descontoUnitario = descontoUnitario;
		this.precoCustoProdutoServico = precoCustoProdutoServico;
		this.precoVendaProdutoServico = precoVendaProdutoServico;
		this.marca = marca;
		this.nomeProdutoServico = nomeProdutoServico;
		this.comissaoDefault = comissaoDefault;
		this.comissaoEfetiva = comissaoEfetiva;
		this.valorTotalVenda = valorTotalVenda;
		this.percDescontoTotalVenda = percDescontoTotalVenda;
		this.lucro = lucro;
	}

	/**
	 * 	Construtor de serviços
	 * @param codVenda
	 * @param dataVenda
	 * @param nomeVendedor
	 * @param codCliente
	 * @param nomeCliente
	 * @param nomeProdutoServico
	 * @param precoCustoProdutoServico
	 * @param precoVendaProdutoServico
	 * @param precoVendidoProdutoServico
	 * @param comissaoEfetiva
	 * @param lucro
	 * @param valorTotalVenda
	 * @param percDescontoTotalVenda
	 */
	public RelatorioDetalhadoFuncionarioDTO(Integer codVenda, Date dataVenda, String nomeVendedor, 
			Integer codCliente, String nomeCliente,
			String nomeProdutoServico,
			Float precoCustoProdutoServico, Float precoVendaProdutoServico, Float precoVendidoProdutoServico,
			Float comissaoEfetiva, Float lucro,
			Float valorTotalVenda, Float percDescontoTotalVenda
			) {

		super();
		this.codVenda = codVenda;
		this.dataVenda = dataVenda;
		this.nomeVendedor = nomeVendedor;
		this.nomeProdutoServico = nomeProdutoServico;
		this.codCliente = codCliente;
		this.nomeCliente = nomeCliente;
		this.precoCustoProdutoServico = precoCustoProdutoServico;
		this.precoVendaProdutoServico = precoVendaProdutoServico;
		this.precoVendidoProdutoServico = precoVendidoProdutoServico;
		this.comissaoDefault = precoCustoProdutoServico;
		this.comissaoEfetiva = comissaoEfetiva;
		this.lucro = lucro;
		this.valorTotalVenda = valorTotalVenda;
		this.percDescontoTotalVenda = percDescontoTotalVenda;
	}

	
	
	public RelatorioDetalhadoFuncionarioDTO() {

	}

	public Integer getCodVenda() {
		return codVenda;
	}

	public void setCodVenda(Integer codVenda) {
		this.codVenda = codVenda;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public String getNomeVendedor() {
		return nomeVendedor;
	}

	public void setNomeVendedor(String nomeVendedor) {
		this.nomeVendedor = nomeVendedor;
	}

	public Integer getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNomeProdutoServico() {
		return nomeProdutoServico;
	}

	public void setNomeProdutoServico(String nomeProdutoServico) {
		this.nomeProdutoServico = nomeProdutoServico;
	}

	public Float getPrecoCustoProdutoServico() {
		return precoCustoProdutoServico;
	}

	public void setPrecoCustoProdutoServico(Float precoCustoProdutoServico) {
		this.precoCustoProdutoServico = precoCustoProdutoServico;
	}

	public Float getPrecoVendaProdutoServico() {
		return precoVendaProdutoServico;
	}

	public void setPrecoVendaProdutoServico(Float precoVendaProdutoServico) {
		this.precoVendaProdutoServico = precoVendaProdutoServico;
	}

	public Float getPrecoVendidoProdutoServico() {
		return precoVendidoProdutoServico;
	}

	public void setPrecoVendidoProdutoServico(Float precoVendidoProdutoServico) {
		this.precoVendidoProdutoServico = precoVendidoProdutoServico;
	}

	public Float getPercDescontoTotalVenda() {
		return percDescontoTotalVenda;
	}

	public void setPercDescontoTotalVenda(Float percDescontoTotalVenda) {
		this.percDescontoTotalVenda = percDescontoTotalVenda;
	}

	public Float getValorTotalVenda() {
		return valorTotalVenda;
	}

	public void setValorTotalVenda(Float valorTotalVenda) {
		this.valorTotalVenda = valorTotalVenda;
	}

	public Float getLucro() {
		return lucro;
	}

	public void setLucro(Float lucro) {
		this.lucro = lucro;
	}

	public Float getComissaoDefault() {
		return comissaoDefault;
	}

	public void setComissaoDefault(Float comissaoDefault) {
		this.comissaoDefault = comissaoDefault;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Float getDescontoUnitario() {
		return descontoUnitario;
	}

	public void setDescontoUnitario(Float descontoUnitario) {
		this.descontoUnitario = descontoUnitario;
	}

	public Float getComissaoEfetiva() {
		return comissaoEfetiva;
	}

	public void setComissaoEfetiva(Float comissaoEfetiva) {
		this.comissaoEfetiva = comissaoEfetiva;
	}

	public boolean isAlteracaoComissao() {
		return alteracaoComissao;
	}

	public void setAlteracaoComissao(boolean alteracaoComissao) {
		this.alteracaoComissao = alteracaoComissao;
	}

}