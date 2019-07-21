package br.com.gerenciadornet.dto;

import br.com.gerenciadornet.entity.VwClienteVendas;

public class RelatorioClienteVendasDTO {

	private Integer codigoCliente;
	private String nome;
	private float valorTotalAno;
	private float lucroTotalAno;
	
	//Representa dos 12 meses do ano
	private VwClienteVendas[] vwclienteVendas = new VwClienteVendas[12];

	public Integer getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getValorTotalAno() {
		return valorTotalAno;
	}

	public void setValorTotalAno(float valorTotalAno) {
		this.valorTotalAno = valorTotalAno;
	}

	public float getLucroTotalAno() {
		return lucroTotalAno;
	}

	public void setLucroTotalAno(float lucroTotalAno) {
		this.lucroTotalAno = lucroTotalAno;
	}

	public RelatorioClienteVendasDTO() {

	}

	public VwClienteVendas[] getVwclienteVendas() {
		return vwclienteVendas;
	}

	public void setVwclienteVendas(VwClienteVendas[] vwclienteVendas) {
		this.vwclienteVendas = vwclienteVendas;
	}

}
