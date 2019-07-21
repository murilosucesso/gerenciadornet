package br.com.gerenciadornet.nfe.txt;


public class ProdutosNotaFiscal {

	Tributacao tributacao = new Tributacao();
	String proUnidcomercial;
	double pnfQtde;
	Double PnfVlr;
	Double pnfBcst;
	Double Icmsst;
	Double pPnfIcmsst;
	String pnfOrigem;

	Double pnfIcmsst;

	public String getProUnidcomercial() {
		return proUnidcomercial;
	}

	public void setProUnidcomercial(String proUnidcomercial) {
		this.proUnidcomercial = proUnidcomercial;
	}

	public Double getPnfQtde() {
		return pnfQtde;
	}

	public void setPnfQtde(Double pnfQtde) {
		this.pnfQtde = pnfQtde;
	}

	public Double getPnfVlr() {
		return PnfVlr;
	}

	public void setPnfVlr(double pnfVlr) {
		PnfVlr = pnfVlr;
	}

	public Tributacao getTributacao() {
		return tributacao;
	}

	public void setTributacao(Tributacao tributacao) {
		this.tributacao = tributacao;
	}

	public Double getPnfBcst() {
		return pnfBcst;
	}

	public void setPnfBcst(double pnfBcst) {
		this.pnfBcst = pnfBcst;
	}

	public Double getIcmsst() {
		return Icmsst;
	}

	public void setIcmsst(Double icmsst) {
		Icmsst = icmsst;
	}

	public void setPnfQtde(double pnfQtde) {
		this.pnfQtde = pnfQtde;
	}

	public void setPnfVlr(Double pnfVlr) {
		PnfVlr = pnfVlr;
	}

	public void setPnfBcst(Double pnfBcst) {
		this.pnfBcst = pnfBcst;
	}

	public Double getpPnfIcmsst() {
		return pPnfIcmsst;
	}

	public void setpPnfIcmsst(Double pPnfIcmsst) {
		this.pPnfIcmsst = pPnfIcmsst;
	}

	public String getPnfOrigem() {
		return pnfOrigem;
	}

	public void setPnfOrigem(String pnfOrigem) {
		this.pnfOrigem = pnfOrigem;
	}

	public Double getPnfIcmsst() {
		return pnfIcmsst;
	}

	public void setPnfIcmsst(Double pnfIcmsst) {
		this.pnfIcmsst = pnfIcmsst;
	}
}
