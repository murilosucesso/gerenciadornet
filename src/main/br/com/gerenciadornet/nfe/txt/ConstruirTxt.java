package br.com.gerenciadornet.nfe.txt;

import NFEletronica.Nfe_Confins;
import NFEletronica.Nfe_ConfinsSt;
import NFEletronica.Nfe_Dados;
import NFEletronica.Nfe_DadosCobranca;
import NFEletronica.Nfe_DetalhamentoArmamentos;
import NFEletronica.Nfe_DetalhamentoCombustiveis;
import NFEletronica.Nfe_DetalhamentoMedicamentos;
import NFEletronica.Nfe_DetalhamentoVeiculos;
import NFEletronica.Nfe_Icms;
import NFEletronica.Nfe_IdentificacaoEmitente;
import NFEletronica.Nfe_IdentificacaoEntrega;
import NFEletronica.Nfe_IdentificacaoRetirada;
import NFEletronica.Nfe_ImpostoImportacao;
import NFEletronica.Nfe_ImpostoProdIndustria;
import NFEletronica.Nfe_InformacoesAdicionais;
import NFEletronica.Nfe_InformacoesCompras;
import NFEletronica.Nfe_InformacoesExterior;
import NFEletronica.Nfe_InformacoesRegistroCana;
import NFEletronica.Nfe_InformacoesTransporte;
import NFEletronica.Nfe_Issqn;
import NFEletronica.Nfe_Pis;
import NFEletronica.Nfe_PisSt;
import NFEletronica.Nfe_ProdutosServicos;

/**
 * Esta classe é a Classe ConstruirTxt descompilada do pacote NFEletronica
 * e corrigida para adicionar um "|" ao final de todas as linhas 
 * @author Murilo
 *
 */
public class ConstruirTxt {

    // Referenced classes of package NFEletronica:
    // Nfe_Dados, Nfe_Identificacao, Nfe_IdentificacaoEmitente, Nfe_IdentificacaoDestinario,
    // Nfe_IdentificacaoRetirada, Nfe_IdentificacaoEntrega, Nfe_ProdutosServicos, Nfe_DetalhamentoVeiculos,
    // Nfe_DetalhamentoMedicamentos, Nfe_DetalhamentoArmamentos, Nfe_DetalhamentoCombustiveis, Nfe_Icms,
    // Nfe_ImpostoProdIndustria, Nfe_ImpostoImportacao, Nfe_Pis, Nfe_PisSt,
    // Nfe_Confins, Nfe_ConfinsSt, Nfe_Issqn, Nfe_ValoresTotais,
    // Nfe_InformacoesTransporte, Nfe_DadosCobranca, Nfe_InformacoesAdicionais, Nfe_InformacoesExterior,
    // Nfe_InformacoesCompras, Nfe_InformacoesRegistroCana
     
	public ConstruirTxt() {
	}
     
    public String NfeDados(Nfe_Dados a){
    	
    	String linha = (new StringBuilder()).append("A|").append(a.getA02Versao()).append("|").append(a.getA03Id()).append("|").append(System.getProperty("line.separator")).toString();
    	return linha;
    }
     
     /**
      *  
      *  B|cUF|cNF|natOp|indPag|mod|serie|nNF|dhEmi|dhSaiEnt |tpNF|idDest|cMunFG|tpImp| tpEmis|cDV|tpAmb|finNFe|indFinal|indPres|procEmi|verProc|dhCont|xJust| - VERSÃO 3.1
      * @param b
      * @return
      */
    public String NfeIdentificacao(Nfe_Identificacao b){
    	
	    StringBuilder linha =  new StringBuilder();
	    linha.append("B|");
	    linha.append(b.getBcUF() 	+ "|");
	    linha.append(b.getBcNF() 	+ "|");
	    linha.append(b.getBnatOp() 	+ "|");
	    //linha.append(b.getBindPag() 	+ "|");
	    linha.append(b.getBmod() 	+ "|");
	    linha.append(b.getBserie() 	+ "|");
	    linha.append(b.getBnNF()	+ "|");
	    linha.append(b.getBdhEmi()	+ "|");
	    linha.append(b.getBdhSaiEnt()+ "|");
	    linha.append(b.getBtpNF()	+ "|");
	    linha.append(b.getBidDest()	+ "|");
	    linha.append(b.getBcMunFG()	+ "|");
	    linha.append(b.getBtpImp()	+ "|");
	    linha.append(b.getBtpEmis()	+ "|");
	    linha.append(b.getBcDV()	+ "|");
	    linha.append(b.getBtpAmb()	+ "|");
	    linha.append(b.getBfinNFe()	+ "|");
	    linha.append(b.getBindFinal()+ "|");
	    linha.append(b.getBindPres()+ "|");
	    linha.append(b.getBprocEmi()+ "|");
	    linha.append(b.getBverProc()+ "|");
	    linha.append(b.getBdhCont()	+ "|");
	    linha.append(b.getBxJust()	+ "|");
	    linha.append(System.getProperty("line.separator")).toString();
	    
	    if(b.getBA02refNFe() != null && !b.getBA02refNFe().isEmpty()){
	    	
	    	 linha.append("BA02|");
	    	 linha.append(b.getBA02refNFe()		+ "|");
	    	 linha.append(System.getProperty("line.separator"));
	    
	    } else if(b.getBA03cUF() != null && !b.getBA03cUF().isEmpty()){
	    	
	    	 linha.append("BA03|");
	    	 linha.append(b.getBA03cUF()	+ "|");
	    	 linha.append(b.getBA03AAMM()	+ "|");
	    	 linha.append(b.getBA03CNPJ()	+ "|");
	    	 linha.append(b.getBA03mod()	+ "|");
	    	 linha.append(b.getBA03serie()	+ "|");
	    	 linha.append(b.getBA03nNF()	+ "|");
	    	 linha.append(System.getProperty("line.separator"));
	    	
	    } else if(b.getBA10cUF() != null && !b.getBA10cUF().isEmpty()){
	    	
	    	 linha.append("BA10|");
	    	 linha.append(b.getBA10cUF()	+ "|");
	    	 linha.append(b.getBA10AAMM()	+ "|");
	    	 linha.append(b.getBA10IE()		+ "|");
	    	 linha.append(b.getBA10mod()	+ "|");
	    	 linha.append(b.getBA10serie()	+ "|");
	    	 linha.append(b.getBA10nNF()	+ "|");
	    	 linha.append(b.getBA10refCTe()	+ "|");
	    	 linha.append(System.getProperty("line.separator"));
	    	 
	    	 if(b.getBA13CNPJ() != null && !b.getBA13CNPJ().isEmpty()){
	    		 
	    		 linha.append("BA13|");
	    		 linha.append(b.getBA13CNPJ()		+ "|");
	    		 linha.append(System.getProperty("line.separator"));
	    	 
	    	 }else  if(b.getBA13CPF() != null && !b.getBA13CPF().isEmpty()){
	    		 
	    		 linha.append("BA13|");
	    		 linha.append(b.getBA13CPF()		+ "|");
	    		 linha.append(System.getProperty("line.separator"));
	    	 }
	    	 
	    } else if (b.getBA20mod() != null && !b.getBA20mod().isEmpty() ){
	    	
	    	 linha.append("BA20|");
    		 linha.append(b.getBA20mod()		+ "|");
    		 linha.append(b.getBA20nECF()		+ "|");
    		 linha.append(b.getBA20nCOO()		+ "|");
    		 linha.append(System.getProperty("line.separator"));
	    	
	    }
	    	
	    	
	    	
	    
	    return linha.toString();
    }
     
    public String NfeIdentificacaoEmitente(Nfe_IdentificacaoEmitente c){
    	
	    String linha = (new StringBuilder()).append("C|").append(c.getC03Xnome()).append("|").append(c.getC04Xfant()).append("|").append(c.getC17Ie()).append("|").append(c.getC18Iest()).append("|").append(c.getC19Im()).append("|").append(c.getC20Cnae()).append("|").append(c.getC21Crt()).append("|").append(System.getProperty("line.separator")).toString();
	   
	    if(c.getC02Cnpj() != null && !c.getC02Cnpj().isEmpty())
	    	linha = (new StringBuilder()).append(linha).append("C02|").append(c.getC02Cnpj()).append("|").append(System.getProperty("line.separator")).toString();
	    else
	    	linha = (new StringBuilder()).append(linha).append("C02a|").append(c.getC02aCpf()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    linha = (new StringBuilder()).append(linha).append("C05|").append(c.getC06Xlgr()).append("|").append(c.getC07Nro()).append("|").append(c.getC08Xcpl()).append("|").append(c.getC09Bairro()).append("|").append(c.getC10Cmun()).append("|").append(c.getC11Xmun()).append("|").append(c.getC12Uf()).append("|").append(c.getC13Cep()).append("|").append(c.getC14Cpais()).append("|").append(c.getC15Xpais()).append("|").append(c.getC16Fone()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    return linha;
    }
     
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
     */
    public String NfeIdentificacaoDestinario(Nfe_IdentificacaoDestinario e){
    	
    	StringBuilder linha =  new StringBuilder();
  	    linha.append("E|");
  	    linha.append(e.getEXnome()			+ "|");
  	    linha.append(e.getEindIEDest()		+ "|");
  	    linha.append(e.getEIE()					+ "|");
  	    linha.append(e.getEISUFl()				+ "|");
  	    linha.append(e.getEIM()				+ "|");
  	    linha.append(e.getEemail()				+ "|");
  	    linha.append(System.getProperty("line.separator"));
  	    
  	    if(e.getE02CNPJ() != null && !e.getE02CNPJ().isEmpty()){
  	    
  	    	linha.append("E02|");
  	  	    linha.append(e.getE02CNPJ()			+ "|");
  	  	    linha.append(System.getProperty("line.separator"));
  	  	    
  	    } else  if(e.getE03CPF() != null && !e.getE03CPF().isEmpty()){
  	    	
  	    	linha.append("E03|");
  	  	    linha.append(e.getE03CPF()			+ "|");
  	  	    linha.append(System.getProperty("line.separator"));
  	  	    
  	    } else  if(e.getE03aidEstrangeiro() != null && !e.getE03aidEstrangeiro().isEmpty()){
  	    	
  	    	linha.append("E03a|");
  	  	    linha.append(e.getE03aidEstrangeiro()			+ "|");
  	  	    linha.append(System.getProperty("line.separator"));
  	    }
  	    
  	    linha.append("E05|");
  	    linha.append(e.getE5XLgr()	+ "|");
  	    linha.append(e.getE5nro()		+ "|");
  	    linha.append(e.getE5xCpl()		+ "|");
  	    linha.append(e.getE5xBairro()	+ "|");
  	    linha.append(e.getE5cMun()	+ "|");
  	    linha.append(e.getE5xMun()	+ "|");
  	    linha.append(e.getE5UF()		+ "|");
  	    linha.append(e.getE5CEP()		+ "|");
  	    linha.append(e.getE5cPais()	+ "|");
  	    linha.append(e.getE5xPais()	+ "|");
  		linha.append(e.getE5fone()	+ "|");
  		linha.append(System.getProperty("line.separator"));
    	
    	return linha.toString();
    }
     
    public String NfeIdentificacaoRetirada(Nfe_IdentificacaoRetirada f){
    
    	String linha = (new StringBuilder()).append("F|").append(f.getF03Xlgr()).append("|").append(f.getF04Nro()).append("|").append(f.getF05Xcpl()).append("|").append(f.getF06Xbairro()).append("|").append(f.getF07Cmun()).append("|").append(f.getF08Xmun()).append("|").append(f.getF09Uf()).append(System.getProperty("line.separator")).toString();
    	
    	if(f.getF02Cnpj() != null && !f.getF02Cnpj().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("F02|").append(f.getF02Cnpj()).append(System.getProperty("line.separator")).toString();
    	else
    		linha = (new StringBuilder()).append(linha).append("F02a|").append(f.getF02aCpf()).append(System.getProperty("line.separator")).toString();
    	
    	return linha;
    }
     
    public String NfeIdentificacaoEntrega(Nfe_IdentificacaoEntrega g){
    
    	String linha = (new StringBuilder()).append("G|").append(g.getG03Xlgr()).append("|").append(g.getG04Nro()).append("|").append(g.getG05Xcpl()).append("|").append(g.getG06Xbairro()).append("|").append(g.getG07Cmun()).append("|").append(g.getG08Xmun()).append("|").append(g.getG09Uf()).append(System.getProperty("line.separator")).toString();
    	
    	if(g.getG02Cnpj() != null && !g.getG02Cnpj().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("G02|").append(g.getG02Cnpj()).append(System.getProperty("line.separator")).toString();
    	else
    		linha = (new StringBuilder()).append(linha).append("G02a|").append(g.getG02aCpf()).append(System.getProperty("line.separator")).toString();
    
    	return linha;
    }
     
    public String NfeProdutosServicos(Nfe_ProdutosServicos i){
    	
    	String linha = "";
    	
    	if(i.getH02Nitem() != null && !i.getH02Nitem().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("H|").append(i.getH02Nitem()).append("|").append(i.getH03InfAdProd()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	if(i.getI02Cprod() != null && !i.getI02Cprod().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("I|").append(i.getI02Cprod()).append("|").append(i.getI03Cean()).append("|").append(i.getI04Xprod()).append("|").append(i.getI05Ncm()).append("|").append(i.getI06Extipi()).append("|").append(i.getI08Cfop()).append("|").append(i.getI09Ucom()).append("|").append(i.getI10Qcom()).append("|").append(i.getI10aVuncom()).append("|").append(i.getI11Vprod()).append("|").append(i.getI12Ceantrib()).append("|").append(i.getI13Utrib()).append("|").append(i.getI14Qtrib()).append("|").append(i.getI14aVuntrib()).append("|").append(i.getI15Vfrete()).append("|").append(i.getI16Vseg()).append("|").append(i.getI17Vdesc()).append("|").append(i.getI17aVoutro()).append("|").append(i.getI17bIndtot()).append("|").append(i.getI30Xped()).append("|").append(i.getI31Nitemped()).append("|").append("|").append("|").append("|").append("|").append(System.getProperty("line.separator")).toString();
    
    	if(i.getI19Ndi() != null && !i.getI19Ndi().isEmpty()){
    		linha = (new StringBuilder()).append(linha).append("I18|").append(i.getI19Ndi()).append("|").append(i.getI20Ddi()).append("|").append(i.getI21Xlocdesemb()).append("|").append(i.getI22Ufdesemb()).append("|").append(i.getI23Ddesemb()).append("|").append(i.getI24Cexportador()).append("|").append(System.getProperty("line.separator")).toString();
    		
    		if(i.getI26Nadicao() != null && !i.getI26Nadicao().isEmpty())
    			linha = (new StringBuilder()).append(linha).append("I25|").append(i.getI26Nadicao()).append("|").append(i.getI27Nseqadic()).append("|").append(i.getI28Cfabricante()).append("|").append(i.getI29Vdescdi()).append("|").append(System.getProperty("line.separator")).toString();
    	}
    	return linha;
    }
     
    public String NfeDetalhamentoVeiculos(Nfe_DetalhamentoVeiculos j){
    	
    	String linha = (new StringBuilder()).append("J|").append(j.getJ02Tpop()).append("|").append(j.getJ03Chassi()).append("|").append(j.getJ04Ccor()).append("|").append(j.getJ05Xcor()).append("|").append(j.getJ06Pot()).append("|").append(j.getJ07Cilin()).append("|").append(j.getJ08Pesol()).append("|").append(j.getJ09Pesob()).append("|").append(j.getJ10Nserie()).append("|").append(j.getJ11Tpcomb()).append("|").append(j.getJ12Nmotor()).append("|").append(j.getJ13Cmt()).append("|").append(j.getJ14Dist()).append("|").append(j.getJ16Anomod()).append("|").append(j.getJ17Anofab()).append("|").append(j.getJ18Tppint()).append("|").append(j.getJ19Tpveic()).append("|").append(j.getJ20Espveic()).append("|").append(j.getJ21Vin()).append("|").append(j.getJ22Condveic()).append("|").append(j.getJ23Cmod()).append("|").append(j.getJ24Ccordenatran()).append("|").append(j.getJ25Lota()).append("|").append(j.getJ26Tprest()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	return linha;
    }
     
    public String NfeDetalhamentoMedicamentos(Nfe_DetalhamentoMedicamentos k){
    	
    	String linha = (new StringBuilder()).append("K|").append(k.getK02Nlote()).append("|").append(k.getK03Qlote()).append("|").append(k.getK04Dfab()).append("|").append(k.getK05Dval()).append("|").append(k.getK06Vpmc()).append("|").append(System.getProperty("line.separator")).toString();
    	return linha;
    }
     
    public String NfeDetalhamentoArmamentos(Nfe_DetalhamentoArmamentos l){
    	String linha = (new StringBuilder()).append("L|").append(l.getL02Tparma()).append("|").append(l.getL03Nserie()).append("|").append(l.getL04Ncano()).append("|").append(l.getL05Descr()).append("|").append(System.getProperty("line.separator")).toString();
    	return linha;
    }
     
    public String NfeDetalhamentoCombustiveis(Nfe_DetalhamentoCombustiveis l1){
    	
    	String linha = (new StringBuilder()).append("L101|").append(l1.getL102Cprodanp()).append("|").append(l1.getL103Codig()).append("|").append(l1.getL104Qtemp()).append("|").append(l1.getL120Ufcons()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	if(l1.getL106Qbcprod() != null && !l1.getL106Qbcprod().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("L105|").append(l1.getL106Qbcprod()).append("|").append(l1.getL107Valiqprod()).append("|").append(l1.getL108Vcide()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	return linha;
    	
    }
     
    public String NfeIcms(Nfe_Icms n){
    	
    	String linha = (new StringBuilder()).append("M").append("|").append("|").append(System.getProperty("line.separator")).toString();
    	linha = (new StringBuilder()).append(linha).append("N").append("|").append(System.getProperty("line.separator")).toString();
    	
    	if(n.getN12Cst().equals("00"))
    		linha = (new StringBuilder()).append(linha).append("N02|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(System.getProperty("line.separator")).toString();
    	else if(n.getN12Cst().equals("10"))
    		linha = (new StringBuilder()).append(linha).append("N03|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	else if(n.getN12Cst().equals("20"))
    		linha = (new StringBuilder()).append(linha).append("N04|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN14Predbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	else if(n.getN12Cst().equals("30"))
	    linha = (new StringBuilder()).append(linha).append("N05|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(System.getProperty("line.separator")).toString();
	    
    	else  if(n.getN12Cst().equals("40") || n.getN12Cst().equals("41") || n.getN12Cst().equals("50"))
    		linha = (new StringBuilder()).append(linha).append("N06|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN17Vicms()).append("|").append(n.getN28Motdesicms()).append(System.getProperty("line.separator")).toString();
	    else if(n.getN12Cst().equals("51"))
	    	linha = (new StringBuilder()).append(linha).append("N07|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN14Predbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else  if(n.getN12Cst().equals("60"))
	    	linha = (new StringBuilder()).append(linha).append("N08|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN23Vicmsst()).append(System.getProperty("line.separator")).toString();
	    
	    else  if(n.getN12Cst().equals("70"))
	    	linha = (new StringBuilder()).append(linha).append("N09|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN14Predbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else  if(n.getN12Cst().equals("90"))
	    	linha = (new StringBuilder()).append(linha).append("N10|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN14Predbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else  if(n.getN12Cst().toUpperCase().equals("10A") || n.getN12Cst().toUpperCase().equals("90A"))
	    	linha = (new StringBuilder()).append(linha).append("N10a|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN13Modbc()).append("|").append(n.getN14Predbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(n.getN25Pbcop()).append("|").append(n.getN24Ufst()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else  if(n.getN12Cst().toUpperCase().equals("41B"))
	    	linha = (new StringBuilder()).append(linha).append("N10b|").append(n.getN11Orig()).append("|").append(n.getN12Cst()).append("|").append(n.getN26Vbcstret()).append("|").append(n.getN27Vicmsstret()).append("|").append(n.getN31Vbcstdest()).append("|").append(n.getN32Vicmsstdest()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else  if(n.getN12ACSosn().equals("101"))
	    	linha = (new StringBuilder()).append(linha).append("N10c|").append(n.getN11Orig()).append("|").append(n.getN12ACSosn()).append("|").append(n.getN29Pcredsn()).append("|").append(n.getN30Vcredicmssn()).append(System.getProperty("line.separator")).toString();
	    
	    else if(n.getN12ACSosn().equals("102") || n.getN12ACSosn().equals("103") || n.getN12ACSosn().equals("300") || n.getN12ACSosn().equals("400"))
	    	linha = (new StringBuilder()).append(linha).append("N10d|").append(n.getN11Orig()).append("|").append(n.getN12ACSosn()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else if(n.getN12ACSosn().equals("201"))
	    	linha = (new StringBuilder()).append(linha).append("N10e|").append(n.getN11Orig()).append("|").append(n.getN12ACSosn()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(n.getN29Pcredsn()).append("|").append(n.getN30Vcredicmssn()).append(System.getProperty("line.separator")).toString();
	    
	    else if(n.getN12ACSosn().equals("202") || n.getN12ACSosn().equals("203"))
	    	linha = (new StringBuilder()).append(linha).append("N10f|").append(n.getN11Orig()).append("|").append(n.getN12ACSosn()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append(System.getProperty("line.separator")).toString();
	    
	    else if(n.getN12ACSosn().equals("500"))
	    	linha = (new StringBuilder()).append(linha).append("N10g|").append(n.getN11Orig()).append("|").append(n.getN12ACSosn()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN26Vbcstret()).append("|").append(n.getN27Vicmsstret()).append(System.getProperty("line.separator")).toString();
	    
	    else if(n.getN12ACSosn().equals("900"))
	    	linha = (new StringBuilder()).append(linha).append("N10h|").append(n.getN11Orig()).append("|").append(n.getN12ACSosn()).append("|").append(n.getN13Modbc()).append("|").append(n.getN15Vbc()).append("|").append(n.getN14Predbc()).append("|").append(n.getN16Picms()).append("|").append(n.getN17Vicms()).append("|").append(n.getN18Modbcst()).append("|").append(n.getN19Pmvast()).append("|").append(n.getN20Predbcst()).append("|").append(n.getN21Vbcst()).append("|").append(n.getN22Picmsst()).append("|").append(n.getN23Vicmsst()).append("|").append(n.getN29Pcredsn()).append("|").append(n.getN30Vcredicmssn()).append(System.getProperty("line.separator")).toString();
	    
    	return linha;
    }
     
    public String NfeImpostoProdIndustria(Nfe_ImpostoProdIndustria o) {
    
    	String linha = (new StringBuilder()).append("O|").append(o.getO02Clenq()).append("|").append(o.getO03Cnpjprod()).append("|").append(o.getO04Cselo()).append("|").append(o.getO05Qselo()).append("|").append(o.getO06Cenq()).append(System.getProperty("line.separator")).toString();
	    
    	if(o.getO09Cst().equals("00") || o.getO09Cst().equals("49") || o.getO09Cst().equals("50") || o.getO09Cst().equals("99")){
    		linha = (new StringBuilder()).append(linha).append("O07|").append(o.getO09Cst()).append("|").append(o.getO14Vipi()).append(System.getProperty("line.separator")).toString();
	    
    		if(o.getO10Vbc() != null && !o.getO10Vbc().isEmpty())
    			linha = (new StringBuilder()).append(linha).append("O10|").append(o.getO10Vbc()).append("|").append(o.getO14Vipi()).append(System.getProperty("line.separator")).toString();
	    
    		else if(o.getO11Qunid() != null && !o.getO11Qunid().isEmpty())
    			linha = (new StringBuilder()).append(linha).append("O11|").append(o.getO11Qunid()).append("|").append(o.getO12Vunid()).append(System.getProperty("line.separator")).toString();
    		
	    } else if(o.getO09Cst().equals("01") || o.getO09Cst().equals("02") || o.getO09Cst().equals("03") || o.getO09Cst().equals("04") || o.getO09Cst().equals("51") || o.getO09Cst().equals("52") || o.getO09Cst().equals("53") || o.getO09Cst().equals("54") || o.getO09Cst().equals("55"))
	    	linha = (new StringBuilder()).append(linha).append("O08|").append(o.getO09Cst()).append(System.getProperty("line.separator")).toString();
	    
    	return linha;
    }
     
    public String NfeImpostoImportacao(Nfe_ImpostoImportacao p){
    	
	    String linha = (new StringBuilder()).append("P|").append(p.getP02Vbc()).append("|").append(p.getP03Vdespadu()).append("|").append(p.getP04Vii()).append("|").append(p.getP05Viof()).append("|").append(System.getProperty("line.separator")).toString();
	    return linha;
	    }
	     
    public String NfePis(Nfe_Pis q){
    	
	    String linha = (new StringBuilder()).append("Q").append("|").append(System.getProperty("line.separator")).toString();
	    
	    if(q.getQ06Cst().equals("01") || q.getQ06Cst().equals("02"))
	    	linha = (new StringBuilder()).append(linha).append("Q02|").append(q.getQ06Cst()).append("|").append(q.getQ07Vbc()).append("|").append(q.getQ08Ppis()).append("|").append(q.getQ09Vpis()).append(System.getProperty("line.separator")).toString();
	    
	    else if(q.getQ06Cst().equals("03"))
	    	linha = (new StringBuilder()).append(linha).append("Q03|").append(q.getQ06Cst()).append("|").append(q.getQ10Qbcprod()).append("|").append(q.getQ11Valiqprod()).append("|").append(q.getQ09Vpis()).append(System.getProperty("line.separator")).toString();
	    
	    else if(q.getQ06Cst().equals("04") || q.getQ06Cst().equals("06") || q.getQ06Cst().equals("07") || q.getQ06Cst().equals("08") || q.getQ06Cst().equals("09"))
	    	linha = (new StringBuilder()).append(linha).append("Q04|").append(q.getQ06Cst()).append("|").append(System.getProperty("line.separator")).toString();
	    
	    else if(q.getQ06Cst().equals("99")){
	    	linha = (new StringBuilder()).append(linha).append("Q05|").append(q.getQ06Cst()).append("|").append(q.getQ09Vpis()).append("|").append(System.getProperty("line.separator")).toString();
	    
		    if(q.getQ07Vbc() != null && !q.getQ07Vbc().isEmpty())
		    	linha = (new StringBuilder()).append(linha).append("Q07|").append(q.getQ07Vbc()).append("|").append(q.getQ08Ppis()).append("|").append(System.getProperty("line.separator")).toString();
		    
		    else if(q.getQ10Qbcprod() != null && !q.getQ10Qbcprod().isEmpty())
		    	linha = (new StringBuilder()).append(linha).append("Q10|").append(q.getQ10Qbcprod()).append("|").append(q.getQ11Valiqprod()).append("|").append(System.getProperty("line.separator")).toString();
	    }
	    
	    return linha;
    }
     
    public String NfePisSt(Nfe_PisSt r){
    	
    	String linha = (new StringBuilder()).append("R|").append(r.getR06Vpis()).append(System.getProperty("line.separator")).toString();
    	
    	if(r.getR02Vbc() != null && !r.getR02Vbc().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("R02|").append(r.getR02Vbc()).append("|").append(r.getR03Ppis()).append("|").append(System.getProperty("line.separator")).toString();
    	else if(r.getR04Qbcprod() != null && !r.getR04Qbcprod().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("R03|").append(r.getR04Qbcprod()).append("|").append(r.getR05Valiqprod()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	return linha;
    }
     
    public String NfeConfins(Nfe_Confins s){
    	
    	String linha = (new StringBuilder()).append("S").append("|").append(System.getProperty("line.separator")).toString();
    	
    	if(s.getS06Cst().equals("01") || s.getS06Cst().equals("02"))
    		linha = (new StringBuilder()).append(linha).append("S02|").append(s.getS06Cst()).append("|").append(s.getS07Vbc()).append("|").append(s.getS08Pconfins()).append("|").append(s.getS11Vconfins()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	else if(s.getS06Cst().equals("03"))
    		linha = (new StringBuilder()).append(linha).append("S03|").append(s.getS06Cst()).append("|").append(s.getS09Qbcprod()).append("|").append(s.getS10Valiqprod()).append("|").append(s.getS11Vconfins()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	else if(s.getS06Cst().equals("04") || s.getS06Cst().equals("06") || s.getS06Cst().equals("07") || s.getS06Cst().equals("08") || s.getS06Cst().equals("09"))
    		linha = (new StringBuilder()).append(linha).append("S04|").append(s.getS06Cst()).append("|").append(System.getProperty("line.separator")).toString();
    	
    	else if(s.getS06Cst().equals("99")){
    		linha = (new StringBuilder()).append(linha).append("S05|").append(s.getS06Cst()).append("|").append(s.getS11Vconfins()).append("|").append(System.getProperty("line.separator")).toString();
    		
    		if(s.getS07Vbc() != null && !s.getS07Vbc().isEmpty())
    			linha = (new StringBuilder()).append(linha).append("S07|").append(s.getS07Vbc()).append("|").append(s.getS08Pconfins()).append("|").append(System.getProperty("line.separator")).toString();
    		
    		else if(s.getS07Vbc() != null && !s.getS07Vbc().isEmpty())
    			linha = (new StringBuilder()).append(linha).append("S09||").append(s.getS09Qbcprod()).append("|").append(s.getS10Valiqprod()).append("|").append(System.getProperty("line.separator")).toString();
    	}
    	return linha;
    }
     
    public String NfeConfinsSt(Nfe_ConfinsSt t){
	    String linha = (new StringBuilder()).append("T|").append(t.getT06Vconfins()).append(System.getProperty("line.separator")).toString();
	    
	    if(t.getT02Vbc() != null && !t.getT02Vbc().isEmpty())
	    	linha = (new StringBuilder()).append(linha).append("T02|").append(t.getT02Vbc()).append("|").append(t.getT03Pconfins()).append(System.getProperty("line.separator")).toString();
	    
	    else if(t.getT04Qbcprod() != null && !t.getT04Qbcprod().isEmpty())
	    	linha = (new StringBuilder()).append(linha).append("T03|").append(t.getT04Qbcprod()).append("|").append(t.getT05Valiqprod()).append(System.getProperty("line.separator")).toString();
	    
	    return linha;
    }
     
    public String NfeIssqn(Nfe_Issqn u){
    	
    	String linha = (new StringBuilder()).append("M").append("|").append(System.getProperty("line.separator")).toString();
    	linha = (new StringBuilder()).append(linha).append("U|").append(u.getU02Vbc()).append("|").append(u.getU03Valiq()).append("|").append(u.getU04Vissqn()).append("|").append(u.getU05Cmunfg()).append("|").append(u.getU06Clistserv()).append("|").append(u.getU07Csittrib()).append(System.getProperty("line.separator")).toString();
    	
    	return linha;
    }
     
    /**
     *  W|
     *  	W02|vBC|vICMS|vICMSDeson|vBCST|vST|vProd|vFrete|vSeg|vDesc|vII|vIPI|vPIS|vCOFINS|vOutro|vNF|vTotTrib|
     *  	[0 ou 1]{
     *  		W17|vServ|vBC|vISS|vPIS|vCOFINS|dCompet|vDeducao|vOutro|vDescIncond|vDescCond|vISSRet|cRegTrib|
     *  	}
     *  
     *  	W23|vRetPIS|vRetCOFINS|vRetCSLL|vBCIRRF|vIRRF|vBCRetPrev|vRetPrev|
     * @param w
     * @return
     */
    public String NfeValoresTotais(Nfe_ValoresTotais w){
    	
    	StringBuilder linha =  new StringBuilder();
    	
  	    linha.append("W|");
  	    linha.append(System.getProperty("line.separator"));
  	    
		if (w.getW02Vbc() != null && !w.getW02Vbc().isEmpty()) {

			linha.append("W02|");
			linha.append(w.getW02Vbc() 				+ "|");
			linha.append(w.getW02VICMS() 			+ "|");
			linha.append(w.getW02VICMSDeson() 	    + "|");
			linha.append(w.getW02VBCST() 			+ "|");
			linha.append(w.getW02Vst() 				+ "|");
			linha.append("0.00"				 		+ "|");//PARA A VERSÃO 4.00
			linha.append("0.00"				 		+ "|");//PARA A VERSÃO 4.00
			linha.append("0.00"				 		+ "|");//PARA A VERSÃO 4.00
			linha.append(w.getW02Vprod() 			+ "|");
			linha.append(w.getW02Vfrete() 			+ "|");
			linha.append(w.getW02Vseg() 			+ "|");
			linha.append(w.getW02Vdesc() 			+ "|");
			linha.append(w.getW02VII() 				+ "|");
			linha.append(w.getW02VIPI() 			+ "|");
			linha.append(w.getW02VPIS() 			+ "|");
			linha.append(w.getW02VCOFINS() 			+ "|");
			linha.append(w.getW02VOutro() 			+ "|");
			linha.append("0.00"				 		+ "|");//PARA A VERSÃO 4.00
			linha.append(w.getW02VNF() 				+ "|");
			linha.append(w.getW02VTotTrib() 		+ "|");
			
			
			linha.append(System.getProperty("line.separator"));
		}
		
		//ADICIONADO PARA VERSÃO 4.00 - inicio
		linha.append("W04c|");
		linha.append("0.00" 				+ "|");
		linha.append(System.getProperty("line.separator"));
		
		linha.append("W04e|");
		linha.append("0.00" 				+ "|");
		linha.append(System.getProperty("line.separator"));
		
		linha.append("W04g|");
		linha.append("0.00" 				+ "|");
		linha.append(System.getProperty("line.separator"));
		//ADICIONADO PARA VERSÃO 4.00 - fim
		
		
		
		
		if(w.getW17Vserv() != null && !w.getW17Vserv().isEmpty() 
				|| w.getW17VBC() != null && !w.getW17VBC().isEmpty() 
				|| w.getW17VISS() != null && !w.getW17VISS().isEmpty() 
				|| w.getW17VPIS() != null && !w.getW17VPIS().isEmpty() 
				|| w.getW17VCONFINS() != null && !w.getW17VCONFINS().isEmpty()){
			
			linha.append("W17|");
			linha.append(w.getW17Vserv() 			+ "|");
			linha.append(w.getW17VBC() 				+ "|");
			linha.append(w.getW17VISS() 			+ "|");
			linha.append(w.getW17VPIS() 			+ "|");
			linha.append(w.getW17VCONFINS() 	+ "|");
			linha.append(w.getW17DCompet() 		+ "|");
			linha.append(w.getW17VDeducao() 		+ "|");
			linha.append(w.getW17VOutro() 			+ "|");
			linha.append(w.getW17VDescIncond() + "|");
			linha.append(w.getW17VDescCond() 	+ "|");
			linha.append(w.getW17VISSRet() 		+ "|");
			linha.append(w.getW17CRegTrib() 		+ "|");
			linha.append(System.getProperty("line.separator"));
		}
    	
		if(w.getW23VRetPIS() != null 				&& !w.getW23VRetPIS().isEmpty() 
				|| w.getW23VRetCOFINS() != null 	&& !w.getW23VRetCOFINS().isEmpty() 
				|| w.getW23VRetCSLL() != null 		&& !w.getW23VRetCSLL().isEmpty() 
				|| w.getW23VBCIRRF() != null 		&& !w.getW23VBCIRRF().isEmpty() 
				|| w.getW23VIRRF() != null 			&& !w.getW23VIRRF().isEmpty() 
				|| w.getW23VBCRetPrev() != null	&& !w.getW23VBCRetPrev().isEmpty() 
				|| w.getW23VRetPrev() != null 		&& !w.getW23VRetPrev().isEmpty()){
			
			linha.append("W23|");
			linha.append(w.getW23VRetPIS() 			+ "|");
			linha.append(w.getW23VRetCOFINS()		+ "|");
			linha.append(w.getW23VRetCSLL()			+ "|");
			linha.append(w.getW23VBCIRRF() 			+ "|");
			linha.append(w.getW23VIRRF()	 			+ "|");
			linha.append(w.getW23VBCRetPrev()		+ "|");
			linha.append(w.getW23VRetPrev() 			+ "|");
			linha.append(System.getProperty("line.separator"));
			
		}
	   
	    return linha.toString();
    }
     
    public String NfeInformacoesTransporte(Nfe_InformacoesTransporte x){
    	
    	String linha = (new StringBuilder()).append("X|").append(x.getX02Modfrete()).append("|").append(System.getProperty("line.separator")).toString();
    	if(x.getX04Cnpj() != null && !x.getX04Cnpj().isEmpty() || x.getX05Cpf() != null && !x.getX05Cpf().isEmpty() || x.getX07Ie() != null && !x.getX07Ie().isEmpty() || x.getX08Xender() != null && !x.getX08Xender().isEmpty() || x.getX09Xmun() != null && !x.getX09Xmun().isEmpty() || x.getX10Uf() != null && !x.getX10Uf().isEmpty())
    	{
    		linha = (new StringBuilder()).append(linha).append("X03|").append(x.getX06Xnome()).append("|").append(x.getX07Ie()).append("|").append(x.getX08Xender()).append("|").append(x.getX10Uf()).append("|").append(x.getX09Xmun()).append(System.getProperty("line.separator")).toString();
		    if(x.getX04Cnpj() != null && !x.getX04Cnpj().isEmpty())
		    linha = (new StringBuilder()).append(linha).append("X04|").append(x.getX04Cnpj()).append(System.getProperty("line.separator")).toString();
		    else
		    if(x.getX05Cpf() != null && !x.getX05Cpf().isEmpty())
		    linha = (new StringBuilder()).append(linha).append("X05|").append(x.getX05Cpf()).append(System.getProperty("line.separator")).toString();
    	}
    	
	    if(x.getX12Vserv() != null && !x.getX12Vserv().isEmpty())
	    linha = (new StringBuilder()).append(linha).append("X11|").append(x.getX12Vserv()).append("|").append(x.getX13Vbcret()).append("|").append(x.getX14Picmsret()).append("|").append(x.getX15Vicmsret()).append("|").append(x.getX16Cfop()).append("|").append(x.getX17Cmunfg()).append(System.getProperty("line.separator")).toString();
	    
	    if(x.getX19Placa() != null && !x.getX19Placa().isEmpty())
	    linha = (new StringBuilder()).append(linha).append("X18|").append(x.getX19Placa()).append("|").append(x.getX20Uf()).append("|").append(x.getX21Rntc()).append(System.getProperty("line.separator")).toString();
	    
	    if(x.getX23Placa() != null && !x.getX23Placa().isEmpty())
	    linha = (new StringBuilder()).append(linha).append("X22|").append(x.getX23Placa()).append("|").append(x.getX24Uf()).append("|").append(x.getX25Rntc()).append(System.getProperty("line.separator")).toString();
	    
	    if(x.getX27Qvol() != null && !x.getX27Qvol().isEmpty() || x.getX28Esp() != null && !x.getX28Esp().isEmpty() || x.getX29Marca() != null && !x.getX29Marca().isEmpty() || x.getX30Nvol() != null && !x.getX30Nvol().isEmpty() || x.getX31Pesol() != null && !x.getX31Pesol().isEmpty() || x.getX32Pesob() != null && !x.getX32Pesob().isEmpty())
	    {
	    	linha = (new StringBuilder()).append(linha).append("X26|").append(x.getX27Qvol()).append("|").append(x.getX28Esp()).append("|").append(x.getX29Marca()).append("|").append(x.getX30Nvol()).append("|").append(x.getX31Pesol()).append("|").append(x.getX32Pesob()).append(System.getProperty("line.separator")).toString();
	    
	    	if(x.getX34Nlacre() != null && !x.getX34Nlacre().isEmpty())
	    		linha = (new StringBuilder()).append(linha).append("X33|").append(x.getX34Nlacre()).append(System.getProperty("line.separator")).toString();
	    }
	    return linha;
    }
     
    public String NfeDadosCobranca(Nfe_DadosCobranca y){
    	
    	StringBuilder linha = (new StringBuilder()).append("YA|").append(System.getProperty("line.separator"));
    	
    	linha.append("YA01|");
		linha.append("0" + "|");
		linha.append("01" + "|");
		linha.append(y.getY10Vdup() + "|");
    	
    	//if(y.getY03Nfat() != null && !y.getY03Nfat().isEmpty() || y.getY04Vorig() != null && !y.getY04Vorig().isEmpty() || y.getY05Vdesc() != null && !y.getY05Vdesc().isEmpty() || y.getY06Vliq() != null && !y.getY06Vliq().isEmpty())
    		//linha = (new StringBuilder()).append(linha).append("Y02|").append(y.getY03Nfat()).append("|").append(y.getY04Vorig()).append("|").append(y.getY05Vdesc()).append("|").append(y.getY06Vliq()).append(System.getProperty("line.separator")).toString();
    	
    	//if(y.getY08Ndup() != null && !y.getY08Ndup().isEmpty() || y.getY09Dvenc() != null && !y.getY09Dvenc().isEmpty() || y.getY10Vdup() != null && !y.getY10Vdup().isEmpty())
    		//linha = (new StringBuilder()).append(linha).append("Y07|").append(y.getY08Ndup()).append("|").append(y.getY09Dvenc()).append("|").append(y.getY10Vdup()).append(System.getProperty("line.separator")).toString();
    	return linha.toString();
    }
    
     
    public String NfeInformacoesAdicionais(Nfe_InformacoesAdicionais z){
    	
	    String linha = (new StringBuilder()).append("Z").append("|").append(z.getZ02Infadfisco()).append("|").append(z.getZ03Infcpl()).append(System.getProperty("line.separator")).toString();
	    
	    if(z.getZ05Xcampo() != null && !z.getZ05Xcampo().isEmpty())
	    	linha = (new StringBuilder()).append(linha).append("Z04|").append(z.getZ05Xcampo()).append("|").append(z.getZ06Xtexto()).append(System.getProperty("line.separator")).toString();
	    
	    if(z.getZ08Xcampo() != null && !z.getZ08Xcampo().isEmpty())
	    	linha = (new StringBuilder()).append(linha).append("Z07|").append(z.getZ08Xcampo()).append("|").append(z.getZ09Xtexto()).append(System.getProperty("line.separator")).toString();
	    
	    if(z.getZ11Nproc() != null && !z.getZ11Nproc().isEmpty())
	    	linha = (new StringBuilder()).append(linha).append("Z10|").append(z.getZ11Nproc()).append("|").append(z.getZ12Indproc()).append(System.getProperty("line.separator")).toString();
	    
	    return linha;
    }
     
    public String NfeInformacoesExterior(Nfe_InformacoesExterior za){
    	String linha = (new StringBuilder()).append("ZA").append(za.getZa02Ufembarq()).append("|").append(za.getZa03Xlocembarq()).append(System.getProperty("line.separator")).toString();
    	return linha;
    }
     
    public String NfeInformacoesCompras(Nfe_InformacoesCompras zb){
    	
    	String linha = (new StringBuilder()).append("ZB|").append(zb.getZb02XNemp()).append("|").append(zb.getZb03Xped()).append("|").append(zb.getZb04Xcont()).append(System.getProperty("line.separator")).toString();
    	return linha;
    }
     
    public String NfeInformacoesRegistroCana(Nfe_InformacoesRegistroCana zc){
    	
    	String linha = (new StringBuilder()).append("ZC01|").append(zc.getZc03Safra()).append("|").append(zc.getZc02Ref()).append("|").append(zc.getZc07Qtomes()).append("|").append(zc.getZc08Qtotant()).append("|").append(zc.getZc09Qtotger()).append("|").append(zc.getZc13Vfor()).append("|").append(zc.getZc14Vtotded()).append("|").append(zc.getZc15Vliqfor()).append(System.getProperty("line.separator")).toString();
    	
    	if(zc.getZc05Dia() != null && !zc.getZc05Dia().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("ZC04|").append(zc.getZc05Dia()).append("|").append(zc.getZc06Qtde()).append(System.getProperty("line.separator")).toString();
    	
    	if(zc.getZc11Xded() != null && !zc.getZc11Xded().isEmpty())
    		linha = (new StringBuilder()).append(linha).append("ZC10|").append(zc.getZc11Xded()).append("|").append(zc.getZc12Vded()).append(System.getProperty("line.separator")).toString();
    	
    	return linha;
    }
}
