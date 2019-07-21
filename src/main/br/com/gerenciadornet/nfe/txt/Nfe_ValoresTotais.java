package br.com.gerenciadornet.nfe.txt;

import java.io.Serializable;

/**
 *  W|
 *  	W02|vBC|vICMS|vICMSDeson|vBCST|vST|vProd|vFrete|vSeg|vDesc|vII|vIPI|vPIS|vCOFINS|vOutro|vNF|vTotTrib|
 *  	[0 ou 1]{
 *  		W17|vServ|vBC|vISS|vPIS|vCOFINS|dCompet|vDeducao|vOutro|vDescIncond|vDescCond|vISSRet|cRegTrib|
 *  	}
 *  
 *  	W23|vRetPIS|vRetCOFINS|vRetCSLL|vBCIRRF|vIRRF|vBCRetPrev|vRetPrev|
 *  
 * @author Murilo
 *
 */
public class Nfe_ValoresTotais implements Serializable {

	private static final long serialVersionUID = 1L;

	//W02|vBC|vICMS|vICMSDeson|vBCST|vST|vProd|vFrete|vSeg|vDesc|vII|vIPI|vPIS|vCOFINS|vOutro|vNF|vTotTrib|
	
	private String w02Vbc;
	private String w02VICMS;
	private String w02VICMSDeson;
	private String w02VBCST;
	private String w02Vst;
	private String w02Vprod;
	private String w02Vfrete;
	private String w02Vseg;
	private String w02Vdesc;
	private String w02VII;
	private String w02VIPI;
	private String w02VPIS;
	private String w02VCOFINS;
	private String w02VOutro;
	private String w02VNF;
	private String w02VTotTrib;
	
	private String w17Vserv;
	private String w17VBC;
	private String w17VISS;
	private String w17VPIS;
	private String w17VCONFINS;
	private String w17DCompet;
	private String w17VDeducao;
	private String w17VOutro;
	private String w17VDescIncond;
	private String w17VDescCond;
	private String w17VISSRet;
	private String w17CRegTrib;
	
	private String w23VRetPIS;
	private String w23VRetCOFINS;
	private String w23VRetCSLL;
	private String w23VBCIRRF;
	private String w23VIRRF;
	private String w23VBCRetPrev;
	private String w23VRetPrev;
	
	
	public Nfe_ValoresTotais() {
		inicializa();
	}

	

	private void inicializa() {
		
		w02Vbc = "";
		w02VICMS = "";
		w02VICMSDeson = "";
		w02VBCST = "";
		w02Vst = "";
		w02Vprod = "";
		w02Vfrete = "";
		w02Vseg = "";
		w02Vdesc = "";
		w02VII = "";
		w02VIPI = "";
		w02VPIS = "";
		w02VCOFINS = "";
		w02VOutro = "";
		w02VNF = "";
		w02VTotTrib = "";
		
		w17Vserv = "";
		w17VBC = "";
		w17VISS = "";
		w17VPIS = "";
		w17VCONFINS = "";
		w17DCompet = "";
		w17VDeducao = "";
		w17VOutro = "";
		w17VDescIncond = "";
		w17VDescCond = "";
		w17VISSRet = "";
		w17CRegTrib = "";
		
		w23VRetPIS = "";
		w23VRetCOFINS = "";
		w23VRetCSLL = "";
		w23VBCIRRF = "";
		w23VIRRF = "";
		w23VBCRetPrev = "";
		w23VRetPrev = "";
		
	}

	public String getW02Vbc() {
		return w02Vbc;
	}

	public void setW02Vbc(String w02Vbc) {
		this.w02Vbc = w02Vbc;
	}

	public String getW02VICMS() {
		return w02VICMS;
	}

	public void setW02VICMS(String w02vicms) {
		w02VICMS = w02vicms;
	}

	public String getW02VICMSDeson() {
		return w02VICMSDeson;
	}

	public void setW02VICMSDeson(String w02vicmsDeson) {
		w02VICMSDeson = w02vicmsDeson;
	}

	public String getW02VBCST() {
		return w02VBCST;
	}

	public void setW02VBCST(String w02vbcst) {
		w02VBCST = w02vbcst;
	}

	public String getW02Vst() {
		return w02Vst;
	}

	public void setW02Vst(String w02Vst) {
		this.w02Vst = w02Vst;
	}

	public String getW02Vprod() {
		return w02Vprod;
	}

	public void setW02Vprod(String w02Vprod) {
		this.w02Vprod = w02Vprod;
	}

	public String getW02Vfrete() {
		return w02Vfrete;
	}

	public void setW02Vfrete(String w02Vfrete) {
		this.w02Vfrete = w02Vfrete;
	}

	public String getW02Vseg() {
		return w02Vseg;
	}

	public void setW02Vseg(String w02Vseg) {
		this.w02Vseg = w02Vseg;
	}

	public String getW02Vdesc() {
		return w02Vdesc;
	}

	public void setW02Vdesc(String w02Vdesc) {
		this.w02Vdesc = w02Vdesc;
	}

	public String getW02VII() {
		return w02VII;
	}

	public void setW02VII(String w02vii) {
		w02VII = w02vii;
	}

	public String getW02VIPI() {
		return w02VIPI;
	}

	public void setW02VIPI(String w02vipi) {
		w02VIPI = w02vipi;
	}

	public String getW02VPIS() {
		return w02VPIS;
	}

	public void setW02VPIS(String w02vpis) {
		w02VPIS = w02vpis;
	}

	public String getW02VCOFINS() {
		return w02VCOFINS;
	}

	public void setW02VCOFINS(String w02vcofins) {
		w02VCOFINS = w02vcofins;
	}

	public String getW02VOutro() {
		return w02VOutro;
	}

	public void setW02VOutro(String w02vOutro) {
		w02VOutro = w02vOutro;
	}

	public String getW02VNF() {
		return w02VNF;
	}

	public void setW02VNF(String w02vnf) {
		w02VNF = w02vnf;
	}

	public String getW02VTotTrib() {
		return w02VTotTrib;
	}

	public void setW02VTotTrib(String w02vTotTrib) {
		w02VTotTrib = w02vTotTrib;
	}

	public String getW17Vserv() {
		return w17Vserv;
	}

	public void setW17Vserv(String w17Vserv) {
		this.w17Vserv = w17Vserv;
	}

	public String getW17VBC() {
		return w17VBC;
	}

	public void setW17VBC(String w17vbc) {
		w17VBC = w17vbc;
	}

	public String getW17VISS() {
		return w17VISS;
	}

	public void setW17VISS(String w17viss) {
		w17VISS = w17viss;
	}

	public String getW17VPIS() {
		return w17VPIS;
	}

	public void setW17VPIS(String w17vpis) {
		w17VPIS = w17vpis;
	}

	public String getW17VCONFINS() {
		return w17VCONFINS;
	}

	public void setW17VCONFINS(String w17vconfins) {
		w17VCONFINS = w17vconfins;
	}

	public String getW17DCompet() {
		return w17DCompet;
	}

	public void setW17DCompet(String w17dCompet) {
		w17DCompet = w17dCompet;
	}

	public String getW17VDeducao() {
		return w17VDeducao;
	}

	public void setW17VDeducao(String w17vDeducao) {
		w17VDeducao = w17vDeducao;
	}

	public String getW17VOutro() {
		return w17VOutro;
	}

	public void setW17VOutro(String w17vOutro) {
		w17VOutro = w17vOutro;
	}

	public String getW17VDescIncond() {
		return w17VDescIncond;
	}

	public void setW17VDescIncond(String w17vDescIncond) {
		w17VDescIncond = w17vDescIncond;
	}

	public String getW17VDescCond() {
		return w17VDescCond;
	}

	public void setW17VDescCond(String w17vDescCond) {
		w17VDescCond = w17vDescCond;
	}

	public String getW17VISSRet() {
		return w17VISSRet;
	}

	public void setW17VISSRet(String w17vissRet) {
		w17VISSRet = w17vissRet;
	}

	public String getW17CRegTrib() {
		return w17CRegTrib;
	}

	public void setW17CRegTrib(String w17cRegTrib) {
		w17CRegTrib = w17cRegTrib;
	}

	public String getW23VRetPIS() {
		return w23VRetPIS;
	}

	public void setW23VRetPIS(String w23vRetPIS) {
		w23VRetPIS = w23vRetPIS;
	}

	public String getW23VRetCOFINS() {
		return w23VRetCOFINS;
	}

	public void setW23VRetCOFINS(String w23vRetCOFINS) {
		w23VRetCOFINS = w23vRetCOFINS;
	}

	public String getW23VRetCSLL() {
		return w23VRetCSLL;
	}

	public void setW23VRetCSLL(String w23vRetCSLL) {
		w23VRetCSLL = w23vRetCSLL;
	}

	public String getW23VBCIRRF() {
		return w23VBCIRRF;
	}

	public void setW23VBCIRRF(String w23vbcirrf) {
		w23VBCIRRF = w23vbcirrf;
	}

	public String getW23VIRRF() {
		return w23VIRRF;
	}

	public void setW23VIRRF(String w23virrf) {
		w23VIRRF = w23virrf;
	}

	public String getW23VBCRetPrev() {
		return w23VBCRetPrev;
	}

	public void setW23VBCRetPrev(String w23vbcRetPrev) {
		w23VBCRetPrev = w23vbcRetPrev;
	}

	public String getW23VRetPrev() {
		return w23VRetPrev;
	}

	public void setW23VRetPrev(String w23vRetPrev) {
		w23VRetPrev = w23vRetPrev;
	}

}
