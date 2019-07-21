package br.com.gerenciadornet.dto;


public class RelatorioGraficoVendaDTO {

	double valorVenda;
	double valorLucro;
	String nome;

	public RelatorioGraficoVendaDTO(String nome, double valorVenda, double valorLucro
			) {
		super();
		this.valorVenda = valorVenda;
		this.valorLucro = valorLucro;
		this.nome = nome;
	}

	public RelatorioGraficoVendaDTO() {

	}

	public double getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(double valorVenda) {
		this.valorVenda = valorVenda;
	}

	public double getValorLucro() {
		return valorLucro;
	}

	public void setValorLucro(double valorLucro) {
		this.valorLucro = valorLucro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
