package br.com.gerenciadornet.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.nfe.xml.TEnderEmi;
import br.com.gerenciadornet.nfe.xml.TEndereco;
import br.com.gerenciadornet.nfe.xml.TNFe;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.II;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Det.Prod;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Emit;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Total;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Transp.Transporta;
import br.com.gerenciadornet.nfe.xml.TNFe.InfNFe.Transp.Vol;
import br.com.gerenciadornet.nfe.xml.TUf;
import br.com.gerenciadornet.nfe.xml.TUfEmi;
import br.com.gerenciadornet.nfe.xml.TVeiculo;


public class NFeHome
{

	//Local aonde serao salvos os xml de NFe
	//private final String repositorioNFe = "C:/GerenciadorNetNFe/";
	
	private JAXBContext getJAXBContext() throws JAXBException {
		return JAXBContext.newInstance("br.com.gerenciadornet.nfe");
	}
	
	/**
	 * Cria um xml de NFe em C:\GerenciadorNetNFe
	 * @param nfe
	 * @throws JAXBException
	 */
	public void criarNfeXML(Venda venda) throws JAXBException, IOException{
		
		TNFe nfe = new TNFe();
		InfNFe infNFe = new InfNFe();
		
		Emit emit = new Emit();
		emit.setCNPJ("000000000001");
		
		infNFe.setEmit(emit);
		
		nfe.setInfNFe(infNFe);
		//nfe.getInfNFe().getInfAdic().setInfAdFisco("ALterado por xxx.. murilo");
		
		SimpleDateFormat formater = new SimpleDateFormat("ddMMyyyy");
		String data = formater.format( new Date());
		
		String nomeArquivoNfe = data.toString() + venda.getCodVenda() + "Nfe.xml";
		
		Marshaller marshaller = getJAXBContext().createMarshaller();
//		File notaFile = new File("C:/kdi/workspace/gerenciadornet/resources/nfe.xml");
		
		File notaFile = new File(nomeArquivoNfe);
		FileOutputStream fos = new FileOutputStream(notaFile);
		
		marshaller.marshal(nfe, fos);
		
		System.out.println("okkk...");
	}
	
	/**
	 * Ler um xml de Nfe
	 * @param notaFile
	 * @return
	 * @throws JAXBException
	 */
	public TNFe lerNFeXml(File notaFile) throws JAXBException{

		Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
		TNFe nfe = unmarshaller.unmarshal(new StreamSource(notaFile), TNFe.class).getValue();
		return nfe;
	}
	
	/*public static void main(String[] args)
	{
		try
		{
			Venda v = new Venda();
			v.setCodVenda(42);
			
			NFeHome home = new NFeHome();
			home.criarNfeXML(v);
			//System.out.println("\n\n nfe.getInfNFe().getInfAdic() = " + nfe.getInfNFe().getInfAdic().getInfAdFisco());
			
			//nfe.getInfNFe().getInfAdic().setInfAdFisco("NOTA ALTERADA!");

			
		}
		catch (Exception e)
		{

			System.out.println("\n\n error: " + e.getCause() + "\n Message: " + e.getMessage());
			e.printStackTrace();

		}
	}*/
	
	//------------------------------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) {

		try {
			File file = new File("c:/GerenciadorNetNFe/nfe.xml");
			FileOutputStream fos = new FileOutputStream(file);
			
			JAXBContext context = new NFeHome().getJAXBContext();
			 
			Marshaller marshaller = context.createMarshaller();
			TNFe nfe = new TNFe();
			nfe.setInfNFe( createInfNFe() );
			
			//TNFe nfe = new ObjectFactory().createTNFe();
			
			//<TNFe> element = 
			marshaller.marshal(nfe, fos);
			
			// fechar arquivo
			fos.close();
			
		} catch (Exception nfe) {
			nfe.printStackTrace();
		}
	}
	
	public static TNFe.InfNFe createInfNFe() {
		TNFe.InfNFe result = new TNFe.InfNFe();
		result.setIde( createIdentificacao() );
		result.setEmit( createEmitente() );
		result.setDest( createDestinatario() );
		result.setVersao("2.00");
		result.setId("NFe11100805679642000182550010000830740000830748");
		result.setTotal( createTotal() );
		result.setTransp( createTransportadora() );
		result.setInfAdic( createInformacoesAdicionais() );
		
		result.getDet().add( createDetalheProduto1() );
		
		return result;
	}
	
	public static TNFe.InfNFe.Det createDetalheProduto1() {
		TNFe.InfNFe.Det result = new TNFe.InfNFe.Det();
		result.setNItem("1");
		result.setProd(new Prod());
		result.getProd().setCProd("1");
		result.getProd().setCEAN("7897569700037");
		result.getProd().setXProd("GALAO DE 20 LT");
		result.getProd().setNCM("22021000");
		result.getProd().setCFOP("5949");
		result.getProd().setUCom("GL");
		result.getProd().setQCom("68.0000");
		result.getProd().setVUnCom("1.6500");
		result.getProd().setVProd("112.20");
		result.getProd().setCEANTrib("7897569700037");
		result.getProd().setUTrib("GL");
		result.getProd().setQTrib("68.0000");
		result.getProd().setVUnTrib("1.6500");
		result.getProd().setVFrete("0.01");
		result.getProd().setVDesc("0.01");
		result.getProd().setVOutro("0.01");
		result.getProd().setIndTot("1"); // 0 ou 1		
		result.setImposto(new Imposto());
		
		result.getImposto().setICMS(new ICMS());
		result.getImposto().getICMS().setICMS10(new ICMS10());
		result.getImposto().getICMS().getICMS10().setOrig("0");
		result.getImposto().getICMS().getICMS10().setCST("10");
		result.getImposto().getICMS().getICMS10().setModBC("1");
		result.getImposto().getICMS().getICMS10().setVBC("112.20");
		result.getImposto().getICMS().getICMS10().setPICMS("17.00");
		result.getImposto().getICMS().getICMS10().setVICMS("19.07");
		result.getImposto().getICMS().getICMS10().setModBCST("5");
		result.getImposto().getICMS().getICMS10().setVBCST("0.00");
		result.getImposto().getICMS().getICMS10().setPICMSST("17.00");
		result.getImposto().getICMS().getICMS10().setVICMSST("0.00");
		
		result.getImposto().setII(new II());
		result.getImposto().getII().setVBC("0.00");
		result.getImposto().getII().setVDespAdu("0.00");
		result.getImposto().getII().setVII("0.00");
		result.getImposto().getII().setVIOF("0.00");
		
		result.getImposto().setPIS(new PIS());
		result.getImposto().getPIS().setPISAliq(new PISAliq());
		result.getImposto().getPIS().getPISAliq().setCST("01");
		result.getImposto().getPIS().getPISAliq().setPPIS("0.00");
		result.getImposto().getPIS().getPISAliq().setVBC("0.00");
		result.getImposto().getPIS().getPISAliq().setVPIS("0.00");
		
		result.getImposto().setCOFINS(new COFINS());
		result.getImposto().getCOFINS().setCOFINSAliq(new COFINSAliq());
		result.getImposto().getCOFINS().getCOFINSAliq().setCST("01");
		result.getImposto().getCOFINS().getCOFINSAliq().setPCOFINS("0.00");
		result.getImposto().getCOFINS().getCOFINSAliq().setVBC("0.00");
		result.getImposto().getCOFINS().getCOFINSAliq().setVCOFINS("0.00");
		
//		result.getImposto().setPISST(new PISST());
//		result.getImposto().getPISST().setPPIS("0.01");
//		result.getImposto().getPISST().setQBCProd("0.01");
//		result.getImposto().getPISST().setVAliqProd("0.000");
//		result.getImposto().getPISST().setVBC("0.01");
//		result.getImposto().getPISST().setVPIS("0.01");
		
		return result;
	}
	
	public static TNFe.InfNFe.Ide createIdentificacao() {
		TNFe.InfNFe.Ide result = new TNFe.InfNFe.Ide();
		result.setCUF("11");
		result.setCNF("00083074");
		result.setNatOp("OUTRA SAIDA DE MERCADOU PRESTSERVICO NAO ESPEC");
		result.setIndPag("0");
		result.setMod("55");
		result.setSerie("1");
		result.setNNF("83074");
		result.setDEmi("2010-08-26");
		result.setDSaiEnt("2010-08-26");
		result.setTpNF("1");
		result.setCMunFG("1100189");
		result.setTpImp("1");
		result.setTpEmis("1");
		result.setCDV("8");
		result.setTpAmb("2"); // homologacao *** CUIDADO ***
		result.setFinNFe("1");
		result.setProcEmi("0");
		result.setVerProc("2.0");		
		return result;
	}
	
	public static TNFe.InfNFe.Emit createEmitente() {
		TNFe.InfNFe.Emit result = new TNFe.InfNFe.Emit();
		result.setXNome("NOME DO ESTABELECIMENTO");
		result.setXFant("NOME DO ESTABELECIMENTO");
		result.setCNPJ("05679642000182");
		result.setIE("00000000153931");
		result.setEnderEmit(new TEnderEmi()); 
		result.getEnderEmit().setXLgr("LINHA 36 GLEBA TATU LOTE 07");
		result.getEnderEmit().setNro("000");
		result.getEnderEmit().setXBairro("ZONA RURAL");
		result.getEnderEmit().setCMun("1100189");
		result.getEnderEmit().setXMun("PIMENTA BUENO");
		result.getEnderEmit().setUF(TUfEmi.RO);
		result.getEnderEmit().setCEP("76970000");
		result.getEnderEmit().setCPais("1058");
		result.getEnderEmit().setXPais("BRASIL");
		result.getEnderEmit().setFone("6934510200");
		result.setIEST("00000000153931");
		result.setCRT("3");
		return result;
	}
	
	public static TNFe.InfNFe.Dest createDestinatario() {
		TNFe.InfNFe.Dest result = new TNFe.InfNFe.Dest();
		result.setXNome("SO FRANGO BK");
		result.setXNome("NOME DO ESTABELECIMENTO");
		result.setCNPJ("05679642000182");
		result.setIE("00000000153931");
		result.setEnderDest(new TEndereco()); 
		result.getEnderDest().setXLgr("LINHA 36 GLEBA TATU LOTE 07");
		result.getEnderDest().setNro("000");
		result.getEnderDest().setXBairro("ZONA RURAL");
		result.getEnderDest().setCMun("1100189");
		result.getEnderDest().setXMun("PIMENTA BUENO");
		result.getEnderDest().setUF(TUf.RO);
		result.getEnderDest().setCEP("76970000");
		result.getEnderDest().setCPais("1058");
		result.getEnderDest().setXPais("BRASIL");
		result.getEnderDest().setFone("6934510200");		
		return result;
	}
	
	public static Total createTotal() {
		Total result = new Total();
		result.setICMSTot( createICMSTot() );
		return result;
	}
	
	public static TNFe.InfNFe.Total.ICMSTot createICMSTot() {
		TNFe.InfNFe.Total.ICMSTot result = new TNFe.InfNFe.Total.ICMSTot();
		result.setVBC("100.00");
		result.setVICMS("17.00");
		result.setVProd("100.00");
		result.setVBCST("0.00");
		result.setVST("0.00");
		result.setVNF("100.00");
		result.setVFrete("0.00");
		result.setVSeg("0.00");
		result.setVDesc("0.00");
		result.setVII("0.00");
		result.setVIPI("0.00");
		result.setVPIS("0.00");
		result.setVCOFINS("0.00");
		result.setVOutro("0.00");
		
		
		return result;
	}
	
	public static TNFe.InfNFe.Transp createTransportadora() {
		TNFe.InfNFe.Transp result = new TNFe.InfNFe.Transp();
		result.setModFrete("1");
		result.setTransporta(new Transporta());
		result.getTransporta().setCPF("33693900949");
		result.getTransporta().setXNome("MIGUEL FERREIRA NETTO");
		result.getTransporta().setXEnder("AV PLACIDO DE CASTRO 2377");
		result.getTransporta().setXMun("JARU");
		result.getTransporta().setUF(TUf.RO);
		result.setVeicTransp(new TVeiculo());
		result.getVeicTransp().setPlaca("NBE3011");
		result.getVeicTransp().setUF(TUf.RO);
		Vol vol = new Vol();
		vol.setEsp("LITRO/TROCAS");
		vol.setMarca("LINDAGUA");
		vol.setPesoB("1428");
		vol.setQVol("68");
		result.getVol().add(vol);
		return result;
	}
	
	public static TNFe.InfNFe.InfAdic createInformacoesAdicionais() {
		TNFe.InfNFe.InfAdic result = new TNFe.InfNFe.InfAdic();
		result.setInfCpl("NOME FANTASIA");
		return result;
	}

}
