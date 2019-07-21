package br.com.gerenciadornet.nfe.txt;

import java.io.Serializable;

/**
 * E|xNome|indIEDest|IE|ISUF|IM|email|
 * 		[seleção entre E02 ou E03 ou E03a]{
 * 			E02|CNPJ|
 * 			[ou]
 * 			E03|CPF|
 * 			[ou]
 * 			E03a|idEstrangeiro|
 * 		}
 *     E05|xLgr|nro|xCpl|xBairro|cMun|xMun|UF|CEP|cPais|xPais|fone|
 * [0 ou 1]{
 * 
 * @author Murilo
 *
 */
public class Nfe_IdentificacaoDestinario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String EXnome;
	private String EindIEDest;
	private String EIE;
	private String EISUFl;
	private String EIM;
	private String Eemail;
	
	private String E02CNPJ;
	private String E03CPF;
	private String E03aidEstrangeiro;
	
	private String E5XLgr;
	private String E5nro;
	private String E5xCpl;
	private String E5xBairro;
	private String E5cMun;
	private String E5xMun;
	private String E5UF;
	private String E5CEP;
	private String E5cPais;
	private String E5xPais;
	private String E5fone;
	
	public Nfe_IdentificacaoDestinario() {

		inicializa();

	}

	private void inicializa() {
		
		EXnome 		= "";
		EindIEDest 	= "";
		EIE 			= "";
		EISUFl 		= "";
		EIM 			= "";
		Eemail 		= "";
		
		E02CNPJ 	= "";
		E03CPF 		= "";
		E03aidEstrangeiro = "";
		
		E5XLgr 		= "";
		E5nro 		= "";
		E5xCpl 		= "";
		E5xBairro 	= "";
		E5cMun 		= "";
		E5UF 			= "";
		E5CEP 		= "";
		E5cPais 		= "";
		E5xPais 		= "";
		E5fone 		= "";
	}

	public String getEXnome() {
		return EXnome;
	}

	public void setEXnome(String eXnome) {
		EXnome = eXnome;
	}

	public String getEindIEDest() {
		return EindIEDest;
	}

	public void setEindIEDest(String eindIEDest) {
		EindIEDest = eindIEDest;
	}

	public String getEIE() {
		return EIE;
	}

	public void setEIE(String eIE) {
		EIE = eIE;
	}

	public String getEISUFl() {
		return EISUFl;
	}

	public void setEISUFl(String eISUFl) {
		EISUFl = eISUFl;
	}

	public String getEIM() {
		return EIM;
	}

	public void setEIM(String eIM) {
		EIM = eIM;
	}

	public String getEemail() {
		return Eemail;
	}

	public void setEemail(String eemail) {
		Eemail = eemail;
	}

	public String getE02CNPJ() {
		return E02CNPJ;
	}

	public void setE02CNPJj(String e02cnpj) {
		E02CNPJ = e02cnpj;
	}

	public String getE03CPF() {
		return E03CPF;
	}

	public void setE03CPF(String e03cpf) {
		E03CPF = e03cpf;
	}

	public String getE03aidEstrangeiro() {
		return E03aidEstrangeiro;
	}

	public void setE03aidEstrangeiro(String e03aidEstrangeiro) {
		E03aidEstrangeiro = e03aidEstrangeiro;
	}

	public String getE5XLgr() {
		return E5XLgr;
	}

	public void setE5XLgr(String e5xLgr) {
		E5XLgr = e5xLgr;
	}

	public String getE5nro() {
		return E5nro;
	}

	public void setE5nro(String e5nro) {
		E5nro = e5nro;
	}

	public String getE5xCpl() {
		return E5xCpl;
	}

	public void setE5xCpl(String e5xCpl) {
		E5xCpl = e5xCpl;
	}

	public String getE5xBairro() {
		return E5xBairro;
	}

	public void setE5xBairro(String e5xBairro) {
		E5xBairro = e5xBairro;
	}

	public String getE5cMun() {
		return E5cMun;
	}

	public void setE5cMun(String e5cMun) {
		E5cMun = e5cMun;
	}

	public String getE5UF() {
		return E5UF;
	}

	public void setE5UF(String e5uf) {
		E5UF = e5uf;
	}

	public String getE5CEP() {
		return E5CEP;
	}

	public void setE5CEP(String e5cep) {
		E5CEP = e5cep;
	}

	public String getE5cPais() {
		return E5cPais;
	}

	public void setE5cPais(String e5cPais) {
		E5cPais = e5cPais;
	}

	public String getE5xPais() {
		return E5xPais;
	}

	public void setE5xPais(String e5xPais) {
		E5xPais = e5xPais;
	}

	public String getE5fone() {
		return E5fone;
	}

	public void setE5fone(String e5fone) {
		E5fone = e5fone;
	}

	public String getE5xMun() {
		return E5xMun;
	}

	public void setE5xMun(String e5xMun) {
		E5xMun = e5xMun;
	}
}
