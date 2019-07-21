package br.com.gerenciadornet.dto;

public class RelatorioGraficoVendasDTO {

	private Integer mes;
	private Integer ano;
	private double total;//Valor Total Vendas
	private double lucro;//Lucro Total sobre Vendas
	private Integer quantidade;//Quantidade de Vendas
	private Integer codigoGrupo;//Grupo ou Carteira
	private String grupo;//Grupo ou Carteira
	
	public RelatorioGraficoVendasDTO(Integer mes, Integer ano, double total, double lucro) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.total = total;
		this.lucro = lucro;
	}
	public RelatorioGraficoVendasDTO(Integer mes, Integer ano, String total, String lucro) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.total = new Float(total);
		this.lucro = new Float(lucro);
	}
	
	public RelatorioGraficoVendasDTO(Integer mes, Integer ano, float total, float lucro) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.total = new Float(total);
		this.lucro = new Float(lucro);
	}
	
	public RelatorioGraficoVendasDTO(Integer mes, Integer ano, double total, double lucro, long quantidade, Integer codigoGrupo, String grupo) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.total = new Float(total);
		this.lucro = new Float(lucro);
		this.quantidade = (int)quantidade;
		this.codigoGrupo = codigoGrupo;
		this.grupo = grupo;
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getLucro() {
		return lucro;
	}

	public void setLucro(double lucro) {
		this.lucro = lucro;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public Integer getCodigoGrupo() {
		return codigoGrupo;
	}
	public void setCodigoGrupo(Integer codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}
	

}
