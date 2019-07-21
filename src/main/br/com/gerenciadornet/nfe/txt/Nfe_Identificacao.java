package br.com.gerenciadornet.nfe.txt;

import java.io.Serializable;

/**
 * Versão 3.1.0 - (24/11/2014) Página 7 de 13 
 * A |versao|Id|pk_nItem| 
 * B|cUF|cNF|natOp|indPag|mod|serie|nNF|dhEmi|dhSaiEnt|tpNF|idDest|cMunFG|tpImp|tpEmis|cDV|tp Amb|finNFe|i ndFinal|indPres|procEmi|verProc|dhCont|xJust| 
 * 		[0 a N] { 
 * 			[seleção entre BA02 OU BA03 OU BA10 OU BA20 ]{ 
 * 				BA02 |refNFe| 
 * 					[ou] 
 * 				BA03 |cUF|AAMM|CNPJ|mod|serie|nNF| 
 *	 				[ou]
 * 				BA10 |cUF|AAMM|IE|mod|serie|nNF|refCTe| 
 * 				[seleção entre BA13 ou BA14]{ 
 * 						BA13|CNPJ| 
 * 							[ou] 
 * 						BA14 |CPF| 
 * 				} 
 * 				[ou] 
 * 				BA20 |mod|nECF|nCOO| 
 * 			} 
 * }
 * 
 * 
 *  //GRUPO B - Identificação da Nota Fiscal eletrônica 
 *  //B|cUF|cNF|NatOp|intPag|mod|serie|nNF|dEmi  |dSaiEnt |hSaiEnt|tpNF          |cMunFG|TpImp|TpEmis|cDV|tpAmb|finNFe                       |procEmi|VerProc|dhCont|xJust|   - VERSÃO 2.0
 *  //B|cUF|cNF|natOp|indPag|mod|serie|nNF|dhEmi|dhSaiEnt           |tpNF|idDest|cMunFG|tpImp| tpEmis|cDV|tpAmb|finNFe|indFinal|indPres|procEmi|verProc|dhCont|xJust| - VERSÃO 3.1
 */
public class Nfe_Identificacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private String BcUF;
	private String BcNF;
	private String BnatOp;
	private String BindPag;
	private String Bmod;
	private String Bserie;
	private String BnNF;
	private String BdhEmi;
	private String BdhSaiEnt;
	private String BtpNF;
	private String BidDest;
	private String BcMunFG;
	private String BtpImp;
	private String BtpEmis;
	private String BcDV;
	private String BtpAmb;
	private String BfinNFe;
	private String BindFinal;
	private String BindPres;
	private String BprocEmi;
	private String BverProc;
	private String BdhCont;
	private String BxJust;
	
	private String BA02refNFe;
	private String BA03cUF;
	private String BA03AAMM;
	private String BA03CNPJ;
	private String BA03mod;
	private String BA03serie;
	private String BA03nNF;
	
	private String BA10cUF;
	private String BA10AAMM;
	private String BA10IE;
	private String BA10mod;
	private String BA10serie;
	private String BA10nNF;
	private String BA10refCTe;
	private String BA13CNPJ;
	private String BA13CPF;
	
	private String BA20mod;
	private String BA20nECF;
	private String BA20nCOO;


	public Nfe_Identificacao() {
		inicializa();
	}

	private void inicializa() {
		
		BcUF = "";
		BcNF = "";
		BnatOp = "";
		BindPag = "";
		Bmod = "";
		Bserie = "";
		BnNF = "";
		BdhEmi = "";
		BdhSaiEnt = "";
		BtpNF = "";
		BidDest = "";
		BcMunFG = "";
		BtpImp = "";
		BtpEmis = "";
		BcDV = "";
		BtpAmb = "";
		BfinNFe = "";
		BindFinal = "";
		BindPres = "";
		BprocEmi = "";
		BverProc = "";
		BdhCont = "";
		BxJust = "";

		BA02refNFe = "";
		BA03cUF = "";
		BA03AAMM = "";
		BA03CNPJ = "";
		BA03mod = "";
		BA03serie = "";
		BA03nNF = "";
		
		BA10cUF = "";
		BA10AAMM = "";
		BA10IE = "";
		BA10mod = "";
		BA10serie = "";
		BA10nNF = "";
		BA10refCTe = "";
		BA13CNPJ = "";
		BA13CPF = "";
		
		BA20mod = "";
		BA20nECF = "";
		BA20nCOO = "";
		
	}

	public String getBcUF() {
		return BcUF;
	}

	public void setBcUF(String bcUF) {
		BcUF = bcUF;
	}

	public String getBcNF() {
		return BcNF;
	}

	public void setBcNF(String bcNF) {
		BcNF = bcNF;
	}

	public String getBnatOp() {
		return BnatOp;
	}

	public void setBnatOp(String bnatOp) {
		BnatOp = bnatOp;
	}

	public String getBindPag() {
		return BindPag;
	}

	public void setBindPag(String bindPag) {
		BindPag = bindPag;
	}

	public String getBmod() {
		return Bmod;
	}

	public void setBmod(String bmod) {
		Bmod = bmod;
	}

	public String getBserie() {
		return Bserie;
	}

	public void setBserie(String bserie) {
		Bserie = bserie;
	}

	public String getBnNF() {
		return BnNF;
	}

	public void setBnNF(String bnNF) {
		BnNF = bnNF;
	}

	public String getBdhEmi() {
		return BdhEmi;
	}

	public void setBdhEmi(String bdhEmi) {
		BdhEmi = bdhEmi;
	}

	public String getBdhSaiEnt() {
		return BdhSaiEnt;
	}

	public void setBdhSaiEnt(String bdhSaiEnt) {
		BdhSaiEnt = bdhSaiEnt;
	}

	public String getBtpNF() {
		return BtpNF;
	}

	public void setBtpNF(String btpNF) {
		BtpNF = btpNF;
	}

	public String getBidDest() {
		return BidDest;
	}

	public void setBidDest(String bidDest) {
		BidDest = bidDest;
	}

	public String getBcMunFG() {
		return BcMunFG;
	}

	public void setBcMunFG(String bcMunFG) {
		BcMunFG = bcMunFG;
	}

	public String getBtpImp() {
		return BtpImp;
	}

	public void setBtpImp(String btpImp) {
		BtpImp = btpImp;
	}

	public String getBtpEmis() {
		return BtpEmis;
	}

	public void setBtpEmis(String btpEmis) {
		BtpEmis = btpEmis;
	}

	public String getBcDV() {
		return BcDV;
	}

	public void setBcDV(String bcDV) {
		BcDV = bcDV;
	}

	public String getBtpAmb() {
		return BtpAmb;
	}

	public void setBtpAmb(String btpAmb) {
		BtpAmb = btpAmb;
	}

	public String getBfinNFe() {
		return BfinNFe;
	}

	public void setBfinNFe(String bfinNFe) {
		BfinNFe = bfinNFe;
	}

	public String getBindFinal() {
		return BindFinal;
	}

	public void setBindFinal(String bindFinal) {
		BindFinal = bindFinal;
	}

	public String getBindPres() {
		return BindPres;
	}

	public void setBindPres(String bindPres) {
		BindPres = bindPres;
	}

	public String getBprocEmi() {
		return BprocEmi;
	}

	public void setBprocEmi(String bprocEmi) {
		BprocEmi = bprocEmi;
	}

	public String getBverProc() {
		return BverProc;
	}

	public void setBverProc(String bverProc) {
		BverProc = bverProc;
	}

	public String getBdhCont() {
		return BdhCont;
	}

	public void setBdhCont(String bdhCont) {
		BdhCont = bdhCont;
	}

	public String getBxJust() {
		return BxJust;
	}

	public void setBxJust(String bxJust) {
		BxJust = bxJust;
	}

	public String getBA02refNFe() {
		return BA02refNFe;
	}

	public void setBA02refNFe(String bA02refNFe) {
		BA02refNFe = bA02refNFe;
	}

	public String getBA03cUF() {
		return BA03cUF;
	}

	public void setBA03cUF(String bA03cUF) {
		BA03cUF = bA03cUF;
	}

	public String getBA03AAMM() {
		return BA03AAMM;
	}

	public void setBA03AAMM(String bA03AAMM) {
		BA03AAMM = bA03AAMM;
	}

	public String getBA03CNPJ() {
		return BA03CNPJ;
	}

	public void setBA03CNPJ(String bA03CNPJ) {
		BA03CNPJ = bA03CNPJ;
	}

	public String getBA03mod() {
		return BA03mod;
	}

	public void setBA03mod(String bA03mod) {
		BA03mod = bA03mod;
	}

	public String getBA03serie() {
		return BA03serie;
	}

	public void setBA03serie(String bA03serie) {
		BA03serie = bA03serie;
	}

	public String getBA03nNF() {
		return BA03nNF;
	}

	public void setBA03nNF(String bA03nNF) {
		BA03nNF = bA03nNF;
	}

	public String getBA10cUF() {
		return BA10cUF;
	}

	public void setBA10cuf(String bA10cUF) {
		BA10cUF = bA10cUF;
	}

	public String getBA10AAMM() {
		return BA10AAMM;
	}

	public void setBA10AAMM(String bA10AAMM) {
		BA10AAMM = bA10AAMM;
	}

	public String getBA10IE() {
		return BA10IE;
	}

	public void setBA10IE(String bA10IE) {
		BA10IE = bA10IE;
	}

	public String getBA10mod() {
		return BA10mod;
	}

	public void setBA10mod(String bA10mod) {
		BA10mod = bA10mod;
	}

	public String getBA10serie() {
		return BA10serie;
	}

	public void setBA10serie(String bA10serie) {
		BA10serie = bA10serie;
	}

	public String getBA10nNF() {
		return BA10nNF;
	}

	public void setBA10nNF(String bA10nNF) {
		BA10nNF = bA10nNF;
	}

	public String getBA10refCTe() {
		return BA10refCTe;
	}

	public void setBA10refCTe(String bA10refCTe) {
		BA10refCTe = bA10refCTe;
	}

	public String getBA13CNPJ() {
		return BA13CNPJ;
	}

	public void setBA13CNPJ(String bA13CNPJ) {
		BA13CNPJ = bA13CNPJ;
	}

	public String getBA13CPF() {
		return BA13CPF;
	}

	public void setBA13CPF(String bA13CPF) {
		BA13CPF = bA13CPF;
	}

	public String getBA20mod() {
		return BA20mod;
	}

	public void setBA20mod(String bA20mod) {
		BA20mod = bA20mod;
	}

	public String getBA20nECF() {
		return BA20nECF;
	}

	public void setBA20nECF(String bA20nECF) {
		BA20nECF = bA20nECF;
	}

	public String getBA20nCOO() {
		return BA20nCOO;
	}

	public void setBA20nCOO(String bA20nCOO) {
		BA20nCOO = bA20nCOO;
	}

}
