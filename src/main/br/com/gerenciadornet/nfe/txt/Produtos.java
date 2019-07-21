package br.com.gerenciadornet.nfe.txt;

public class Produtos {

	int proCodigo;
    String proDescricao;  
    String proNbm;  
    String proUnidcomercial;  
    String proEan;//.replace("-", ""));  
    
	public int getProCodigo() {
		return proCodigo;
	}
	public void setProCodigo(int proCodigo) {
		this.proCodigo = proCodigo;
	}
	public String getProDescricao() {
		return proDescricao;
	}
	public void setProDescricao(String proDescricao) {
		this.proDescricao = proDescricao;
	}
	public String getProNbm() {
		return proNbm;
	}
	public void setProNbm(String proNbm) {
		this.proNbm = proNbm;
	}
	public String getProUnidcomercial() {
		return proUnidcomercial;
	}
	public void setProUnidcomercial(String proUnidcomercial) {
		this.proUnidcomercial = proUnidcomercial;
	}
	public String getProEan() {
		return proEan;
	}
	public void setProEan(String proEan) {
		this.proEan = proEan;
	}
}
