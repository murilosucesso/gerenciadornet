package br.com.gerenciadornet.nfe.txt;

import java.util.List;

public class ServicosNotaFiscal {

	Double snfVlr;
	Double snfBc;
	Double snfAliquota;
	List<Servicos> snfListaservico;
	Double snfTributacao;
	Double SnfIssqn;
	Cidades cidades;

	public Double getSnfVlr() {
		return snfVlr;
	}

	public void setSnfVlr(Double snfVlr) {
		this.snfVlr = snfVlr;
	}

	public Double getSnfBc() {
		return snfBc;
	}

	public void setSnfBc(Double snfBc) {
		this.snfBc = snfBc;
	}

	public Double getSnfAliquota() {
		return snfAliquota;
	}

	public void setSnfAliquota(Double snfAliquota) {
		this.snfAliquota = snfAliquota;
	}

	public List<Servicos> getSnfListaservico() {
		return snfListaservico;
	}

	public void setSnfListaservico(List<Servicos> snfListaservico) {
		this.snfListaservico = snfListaservico;
	}

	public Double getSnfTributacao() {
		return snfTributacao;
	}

	public void setSnfTributacao(double snfTributacao) {
		this.snfTributacao = snfTributacao;
	}

	public Double getSnfIssqn() {
		return SnfIssqn;
	}

	public void setSnfIssqn(Double snfIssqn) {
		SnfIssqn = snfIssqn;
	}

	public void setSnfTributacao(Double snfTributacao) {
		this.snfTributacao = snfTributacao;
	}

	public Cidades getCidades() {
		return cidades;
	}

	public void setCidades(Cidades cidades) {
		this.cidades = cidades;
	}
}
