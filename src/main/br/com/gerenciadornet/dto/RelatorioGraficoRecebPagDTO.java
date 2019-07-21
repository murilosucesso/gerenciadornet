package br.com.gerenciadornet.dto;

import java.util.Date;

public class RelatorioGraficoRecebPagDTO {

	Date data;
	double valorAPagar;
	double valorAReceber;
	String nome;

	public RelatorioGraficoRecebPagDTO() {

	}
	
	public RelatorioGraficoRecebPagDTO(Date data, double valorAPagar, double valorAReceber) {
		super();
		this.data = data;
		this.valorAPagar = valorAPagar;
		this.valorAReceber = valorAReceber;
	}
	
	public RelatorioGraficoRecebPagDTO(Date data, double valorAPagar) {
		super();
		this.data = data;
		this.valorAPagar = valorAPagar;
	}
	
	public RelatorioGraficoRecebPagDTO(double valorAReceber, Date data) {
		super();
		this.data = data;
		this.valorAReceber = valorAReceber;
	}

	public RelatorioGraficoRecebPagDTO(String nome, Double valorAPagar) {
		super();
		this.nome = nome;
		this.valorAPagar = valorAPagar;
	}

	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(double valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

	public double getValorAReceber() {
		return valorAReceber;
	}

	public void setValorAReceber(double valorAReceber) {
		this.valorAReceber = valorAReceber;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
