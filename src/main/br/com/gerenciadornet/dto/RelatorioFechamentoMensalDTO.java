package br.com.gerenciadornet.dto;

public class RelatorioFechamentoMensalDTO {

	private Integer mes;
	private Integer ano;

	private Integer qtdVendasRealizadas = 0;
	private Double lucroProdutosVendidos = 0.0d;;

	// TABELA TOTAIS VENDAS
	
	private Double valorTotalRealizado = 0.0d;;
	private Double lucroTotalRealizado = 0.0d;;
	
	// TABELA PRODUTOS
	private Double precoVendaSemDesconto = 0.0d;
	private Double precoVendaComDesconto = 0.0d;
	private Double qtdProdutosVendidos = 0.0d;
	private Double valorTotalDescontos = 0.0d;
	private Double valorTotalPrecoCompra = 0.0d;
	private Double valorTotalComissoesSemDesconto = 0.0d;
	private Double valorTotalComissoesComDesconto = 0.0d;
	private Double valorTotalLucroBruto = 0.0d;
	
	// TABELA SERVIÇOS
	private Double totalPrecoCustoServicos = 0.0d;//preco de custo
	private Double totalPrecoServicos = 0.0d;//preco do servico
	private Double totalPrecoRealizadoServicos = 0.0d;//preco cobrado
	private Double totalComissaoServicosRealizados = 0.0d;//comissao
	private Double totalLucroServicosRealizados = 0.0d;//Total do servico - comissao
	
	// TABELA PAGAMENTOS
	private Double totalPagamentosAgendados = 0.0d;
	private Double totalPagamentosPendentes = 0.0d;
	private Double totalPago = 0.0d;
	private Double totalPagoAgendado = 0.0d;
	private Double totalPagoNaoAgendado = 0.0d;
	
	// TABELA RECEBIMENTOS
	private Double totalAgendadoAReceber = 0.0d; // pesquisa pela data de agendamento do recebimento
	private Double totalRecebido = 0.0d; //pesquisa pela data Balancete
	private Double totalRecebidoNaoAgendado = 0.0d; //pesquisa pela data Balancete dentro e data de agendamento do recebimento fora do período informado 
	private Double totalRecebidoAgendado = 0.0d; // pesquisa pela data de agendamento do recebimento e data Balancete
	private Double totalPendenteRecebimento = 0.0d; // valor pendente de recebimento no mês. (Total Agendador a Receber - Total Agendado Recebido)
	
	//TRANSIENTE
	private Double valor = 0.0d;
		
	
	public RelatorioFechamentoMensalDTO() {
		super();
	}
	
	/**
	 * Construtor utilizado em listarServiçosVendidos
	 */
	public RelatorioFechamentoMensalDTO(Double valor) {
		super();
		this.valor = valor;
	}

	/**
	 * Construtor utilizado em listarServiçosVendidos
	 */
	public RelatorioFechamentoMensalDTO(Double totalPrecoCustoServicos,
			Double totalPrecoServicos, Double totalPrecoRealizadoServicos,
			Double totalComissaoServicosRealizados,
			Double totalLucroServicosRealizados) {
		super();
		this.totalPrecoCustoServicos = totalPrecoCustoServicos;
		this.totalPrecoServicos = totalPrecoServicos;
		this.totalPrecoRealizadoServicos = totalPrecoRealizadoServicos;
		this.totalComissaoServicosRealizados = totalComissaoServicosRealizados;
		this.totalLucroServicosRealizados = totalLucroServicosRealizados;
	}

	
	/**
	 * Construtor utilizado em listarProdutosVendidos
	 */
	public RelatorioFechamentoMensalDTO(Double precoVendaSemDesconto,
			Double precoVendaComDesconto, 
			Double valorTotalDescontos,
			Double valorTotalPrecoCompra,
			Double valorTotalComissoesSemDesconto,
			Double valorTotalComissoesComDesconto,
			Double valorTotalLucroBruto
			) {
		 
		super();
		this.precoVendaSemDesconto = precoVendaSemDesconto;
		this.precoVendaComDesconto = precoVendaComDesconto;
		this.valorTotalDescontos = valorTotalDescontos;
		this.valorTotalPrecoCompra = valorTotalPrecoCompra;
		this.valorTotalComissoesSemDesconto = valorTotalComissoesSemDesconto;
		this.valorTotalComissoesComDesconto = valorTotalComissoesComDesconto;
		this.valorTotalLucroBruto = valorTotalLucroBruto;
	}

	/**
	 * Construtor utilizado em consultarTotalVendido
	 * 
	 * @param mes
	 * @param ano
	 * @param valorTotalRealizado
	 * @param lucroTotalRealizado
	 */
	public RelatorioFechamentoMensalDTO(Integer mes, Integer ano,
			Double valorTotalRealizado, Double lucroTotalRealizado) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.valorTotalRealizado = valorTotalRealizado;
		this.lucroTotalRealizado = lucroTotalRealizado;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Double getQtdProdutosVendidos() {
		return qtdProdutosVendidos;
	}

	public void setQtdProdutosVendidos(Double qtdProdutosVendidos) {
		this.qtdProdutosVendidos = qtdProdutosVendidos;
	}

	public int getQtdVendasRealizadas() {
		return qtdVendasRealizadas;
	}

	public void setQtdVendasRealizadas(int qtdVendasRealizadas) {
		this.qtdVendasRealizadas = qtdVendasRealizadas;
	}
	
	public Double getLucroProdutosVendidos() {
		return lucroProdutosVendidos;
	}

	public void setLucroProdutosVendidos(Double lucroProdutosVendidos) {
		this.lucroProdutosVendidos = lucroProdutosVendidos;
	}

	public Double getValorTotalRealizado() {
		return valorTotalRealizado;
	}

	public void setValorTotalRealizado(Double valorTotalRealizado) {
		this.valorTotalRealizado = valorTotalRealizado;
	}

	public Double getLucroTotalRealizado() {
		return lucroTotalRealizado;
	}

	public void setLucroTotalRealizado(Double lucroTotalRealizado) {
		this.lucroTotalRealizado = lucroTotalRealizado;
	}

	public Double getPrecoVendaSemDesconto() {
		return precoVendaSemDesconto;
	}

	public void setPrecoVendaSemDesconto(Double precoVendaSemDesconto) {
		this.precoVendaSemDesconto = precoVendaSemDesconto;
	}

	public Double getPrecoVendaComDesconto() {
		return precoVendaComDesconto;
	}

	public void setPrecoVendaComDesconto(Double precoVendaComDesconto) {
		this.precoVendaComDesconto = precoVendaComDesconto;
	}

	public Double getValorTotalDescontos() {
		return valorTotalDescontos;
	}

	public void setValorTotalDescontos(Double valorTotalDescontos) {
		this.valorTotalDescontos = valorTotalDescontos;
	}

	public Double getValorTotalPrecoCompra() {
		return valorTotalPrecoCompra;
	}

	public void setValorTotalPrecoCompra(Double valorTotalPrecoCompra) {
		this.valorTotalPrecoCompra = valorTotalPrecoCompra;
	}

	public Double getValorTotalComissoesSemDesconto() {
		return valorTotalComissoesSemDesconto;
	}

	public void setValorTotalComissoesSemDesconto(
			Double valorTotalComissoesSemDesconto) {
		this.valorTotalComissoesSemDesconto = valorTotalComissoesSemDesconto;
	}

	public Double getValorTotalComissoesComDesconto() {
		return valorTotalComissoesComDesconto;
	}

	public void setValorTotalComissoesComDesconto(
			Double valorTotalComissoesComDesconto) {
		this.valorTotalComissoesComDesconto = valorTotalComissoesComDesconto;
	}

	public Double getValorTotalLucroBruto() {
		return valorTotalLucroBruto;
	}

	public void setValorTotalLucroBruto(Double valorTotalLucroBruto) {
		this.valorTotalLucroBruto = valorTotalLucroBruto;
	}

	public Double getTotalPrecoCustoServicos() {
		return totalPrecoCustoServicos;
	}

	public void setTotalPrecoCustoServicos(Double totalPrecoCustoServicos) {
		this.totalPrecoCustoServicos = totalPrecoCustoServicos;
	}

	public Double getTotalPrecoServicos() {
		return totalPrecoServicos;
	}

	public void setTotalPrecoServicos(Double totalPrecoServicos) {
		this.totalPrecoServicos = totalPrecoServicos;
	}

	public Double getTotalPrecoRealizadoServicos() {
		return totalPrecoRealizadoServicos;
	}

	public void setTotalPrecoRealizadoServicos(Double totalPrecoRealizadoServicos) {
		this.totalPrecoRealizadoServicos = totalPrecoRealizadoServicos;
	}

	public Double getTotalComissaoServicosRealizados() {
		return totalComissaoServicosRealizados;
	}

	public void setTotalComissaoServicosRealizados(
			Double totalComissaoServicosRealizados) {
		this.totalComissaoServicosRealizados = totalComissaoServicosRealizados;
	}

	public Double getTotalLucroServicosRealizados() {
		return totalLucroServicosRealizados;
	}

	public void setTotalLucroServicosRealizados(Double totalLucroServicosRealizados) {
		this.totalLucroServicosRealizados = totalLucroServicosRealizados;
	}

	
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getTotalAgendadoAReceber() {
		return totalAgendadoAReceber;
	}

	public void setTotalAgendadoAReceber(Double totalAgendadoAReceber) {
		this.totalAgendadoAReceber = totalAgendadoAReceber;
	}

	public Double getTotalRecebidoAgendado() {
		return totalRecebidoAgendado;
	}

	public void setTotalRecebidoAgendado(Double totalRecebidoAgendado) {
		this.totalRecebidoAgendado = totalRecebidoAgendado;
	}

	public Double getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(Double totalPago) {
		this.totalPago = totalPago;
	}

	public Double getTotalRecebido() {
		return totalRecebido;
	}

	public void setTotalRecebido(Double totalRecebido) {
		this.totalRecebido = totalRecebido;
	}

	public Double getTotalRecebidoNaoAgendado() {
		return totalRecebidoNaoAgendado;
	}

	public void setTotalRecebidoNaoAgendado(Double totalRecebidoNaoAgendado) {
		this.totalRecebidoNaoAgendado = totalRecebidoNaoAgendado;
	}

	public Double getTotalPendenteRecebimento() {
		return totalPendenteRecebimento;
	}

	public void setTotalPendenteRecebimento(Double totalPendenteRecebimento) {
		this.totalPendenteRecebimento = totalPendenteRecebimento;
	}

	public Double getTotalPagamentosAgendados() {
		return totalPagamentosAgendados;
	}

	public void setTotalPagamentosAgendados(Double totalPagamentosAgendados) {
		this.totalPagamentosAgendados = totalPagamentosAgendados;
	}

	public Double getTotalPagoAgendado() {
		return totalPagoAgendado;
	}

	public void setTotalPagoAgendado(Double totalPagoAgendado) {
		this.totalPagoAgendado = totalPagoAgendado;
	}

	public Double getTotalPagoNaoAgendado() {
		return totalPagoNaoAgendado;
	}

	public void setTotalPagoNaoAgendado(Double totalPagoNaoAgendado) {
		this.totalPagoNaoAgendado = totalPagoNaoAgendado;
	}

	public Double getTotalPagamentosPendentes() {
		return totalPagamentosPendentes;
	}

	public void setTotalPagamentosPendentes(Double totalPagamentosPendentes) {
		this.totalPagamentosPendentes = totalPagamentosPendentes;
	}

}
