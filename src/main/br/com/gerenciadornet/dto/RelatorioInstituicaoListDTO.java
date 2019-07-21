package br.com.gerenciadornet.dto;

public class RelatorioInstituicaoListDTO {
	
	private String nomeCliente;
	private String telefoneCliente;
	private double valorVendasTotal;
	private long qtdVendasTotal;

	public RelatorioInstituicaoListDTO(String nomeCliente,
			String telefoneCliente, double valorVendasTotal,
			long qtdVendasTotal) {
		super();
		this.nomeCliente = nomeCliente;
		this.telefoneCliente = telefoneCliente;
		this.valorVendasTotal = valorVendasTotal;
		this.qtdVendasTotal = qtdVendasTotal;
	}

	
	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getTelefoneCliente() {
		return telefoneCliente;
	}

	public void setTelefoneCliente(String telefoneCliente) {
		this.telefoneCliente = telefoneCliente;
	}

	public double getValorVendasTotal() {
		return valorVendasTotal;
	}

	public void setValorVendasTotal(double valorVendasTotal) {
		this.valorVendasTotal = valorVendasTotal;
	}

	public long getQtdVendasTotal() {
		return qtdVendasTotal;
	}

	public void setQtdVendasTotal(long qtdVendasTotal) {
		this.qtdVendasTotal = qtdVendasTotal;
	}
	

}
