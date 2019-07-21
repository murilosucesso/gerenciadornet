package br.com.gerenciadornet.nfe.txt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import NFEletronica.Nfe_Confins;
import NFEletronica.Nfe_Dados;
import NFEletronica.Nfe_DadosCobranca;
import NFEletronica.Nfe_Icms;
import NFEletronica.Nfe_IdentificacaoEmitente;
import NFEletronica.Nfe_InformacoesAdicionais;
import NFEletronica.Nfe_InformacoesTransporte;
import NFEletronica.Nfe_Issqn;
import NFEletronica.Nfe_Pis;
import NFEletronica.Nfe_ProdutosServicos;
import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Empresa;
import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.LoteProduto;
import br.com.gerenciadornet.entity.Marca;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.ProdutoVendido;
import br.com.gerenciadornet.entity.Venda;

public class GeradorNfeFachada {
	
	
	/**
	 * Método para testes de geração de  
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("GeradorNfeFachada.main() = " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date( System.currentTimeMillis() )  ) );
		
		GeradorNfeFachada fachada = new GeradorNfeFachada();
		Venda v = new Venda();
		v.setCodVenda(356);

		Empresa empresa = new Empresa();
		
		Cliente c = new Cliente();
		c.setCodCliente(123);
		c.setNome("Murilo M Oliveira");
		c.setCpfCnpj("935.801.771-68");//replace(".", "")
		c.setTelefone("(061) 9855-30405");
		c.setRgIe("1978297");
		//False - PF, True - PJ
		c.setTipoPessoa(true);
		
		Endereco e = new Endereco();
		e.setEndereco("QNG 04 Casa ");
		e.setBairro("Taguatinga Norte");
		e.setNumero("03");
		e.setCidade("Brasilia");
		e.setUf("DF");
		e.setCep("72.130-040");
		
		Set <Endereco>enderecos = new HashSet<Endereco>();
		enderecos.add(e);
		
		c.setEnderecos(enderecos);
		
		v.setCliente(c);
		v.setNumNotaFiscal("12345");
		
		Set <ProdutoVendido> produtosVendidos = new HashSet<ProdutoVendido>();
		
		Produto produto = new Produto();
		produto.setNomeProduto("Maquina de Solda a ponto");
		produto.setNcm("99999999");
		produto.setCodProdutoExterno("77425");
		produto.setCodProduto(774256);
		produto.setMarca(new Marca("Kernit", false));
		
		LoteProduto lote = new LoteProduto();
		lote.setProduto(produto);
		
		ProdutoVendido pv = new ProdutoVendido();
		pv.setPrecoVenda(489.00f);
		pv.setDesconto(0.00f);

		pv.setLoteProduto(lote);
		
		produtosVendidos.add(pv);
		
		v.setProdutoVendidos(produtosVendidos);
		v.setValorTotalVenda(489.00f);
		
		String nota = fachada.emitirNFe(empresa, v);
		
		try{
			String nomeArquivo = "NFe - Venda " + v.getCodVenda() + "-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date( System.currentTimeMillis() )  ) + ".txt";		
			//FileWriter writer = new FileWriter("C:/Users/f7292219/Desktop/" + nomeArquivo);
			//writer.write(fachada.cabecalho);
			//writer.write(fachada.corpo);
			
			//writer.write(fachada.corpo);
			//writer.close(); 
			
			System.out.println(nomeArquivo);
			System.out.println(nota);
			
			} catch (Exception ex) {
				System.out.println("Erro no fileWriter...zz");
			}
		
	}
	
	Date dataAtual = new Date(System.currentTimeMillis());
	
	/**
	 * Gera um arquivo .txt para ser importado pelo programa
	 * de emissão de nota fiscal eletrônica SP com base nos
	 * dados de uma venda.
	 * 
	 * @param venda
	 */
	public String emitirNFe(Empresa empresa, Venda venda){
		
		NotaFiscal nf = new NotaFiscal();
		
		nf.setNfTipoDocumento(new Float(1));//0-Entrada 1-Saída
		
		nf.setNfFormaPagto(new Float(0));//0-Pagamento a vista, 1-Pagamento a prazo, 2-Outros
		 	
		nf.setNfModalidadefrete(new Double(1));//0-Por Conta do emitente, 1-PC Destin/Emitente, 2-PC Terceiros,9-Sem Frete
		
		GeradorNfeFachada fachada = new GeradorNfeFachada();
		fachada.gerarCabecalho();
		fachada.gerarCorpo(nf, empresa, venda);
		
		return fachada.cabecalho + fachada.corpo;
	}
	
	
	String cabecalho = "";
	String corpo = "";
	
    private void gerarCabecalho(){  
        cabecalho = "NOTAFISCAL|"  
                   +"1"  //Informar o número de notas emitidas ??
                   +System.getProperty ("line.separator");// Não sei pra que serve
    }  
    
   /*    
    private Nfe_Icms gerarTributacao(Tributacao tri, String origem, double vlrBcSt, double icmsSt){  
        
        Nfe_Icms n = new Nfe_Icms();  
  
        if (tri.getTriCodigo()==500){  
  
            n.setN11Orig(origem);  
            n.setN12ACSosn(String.valueOf(tri.getTriCodigo()));  
            n.setN26Vbcstret(formatarDouble(vlrBcSt).replace(",", "."));  
            n.setN27Vicmsstret(formatarDouble(icmsSt).replace(",", "."));  
  
        }else if (tri.getTriCodigo()==300 || tri.getTriCodigo()==400){  
  
            n.setN11Orig(origem);  
            n.setN12ACSosn(String.valueOf(tri.getTriCodigo()));  
        }  
  
        return n;  
    }  */
    
    private void gerarCorpo(NotaFiscal nf, Empresa empresa, Venda venda){  
    	
        Nfe_Dados a = new Nfe_Dados();  
        Nfe_Identificacao b = new Nfe_Identificacao();  
        Nfe_IdentificacaoEmitente c = new Nfe_IdentificacaoEmitente();  
        Nfe_IdentificacaoDestinario e = new Nfe_IdentificacaoDestinario();  
        Nfe_ProdutosServicos i = new Nfe_ProdutosServicos();  
        Nfe_Icms n_nfeIcms = new Nfe_Icms();  //Grupo N
        Nfe_Pis q = new Nfe_Pis();  
        Nfe_Confins s = new Nfe_Confins();  
        Nfe_Issqn u = new Nfe_Issqn();  
        Nfe_ValoresTotais w = new Nfe_ValoresTotais();  
        Nfe_InformacoesTransporte x = new Nfe_InformacoesTransporte();  
        //Nfe_DadosCobranca y = new Nfe_DadosCobranca();
        Nfe_InformacoesAdicionais z = new Nfe_InformacoesAdicionais();  
  
  
        ConstruirTxt construirTxt = new ConstruirTxt();  
  
        //GRUPO A  
        //A|versao|Id|pk_nItem|
        a.setA02Versao("4.00");  
        a.setA03Id("NFe");  
        corpo += construirTxt.NfeDados(a);  
  
        //GRUPO B - Identificação da Nota Fiscal eletrônica 
        //B|cUF|cNF|natOp|indPag|mod|serie|nNF|dhEmi|dhSaiEnt|tpNF|idDest|cMunFG|tpImp|tpEmis|cDV|tpAmb|finNFe|indFinal|indPres|procEmi|verProc|dhCont|xJust| - VERSÃO 3.1
        b.setBcUF("53");  
        b.setBnatOp("Venda de Mercadoria"); 
        b.setBindPag("2");//0 - Pagamento à vista;  1 - Pagamento a prazo;  2 - Outros.
        b.setBmod("55");  //Código do Modelo do Documento Fiscal - Utilizar o código 55 identificação NF-e.
        b.setBserie("1");
        
        if(venda.getNumNotaFiscal() != null && !venda.getNumNotaFiscal().trim().equals("")){
        	
        	b.setBnNF(venda.getNumNotaFiscal());//Numero do documento fiscal informado na venda.
        	
        } else {
        	b.setBnNF("1");//Numero do documento fiscal default, vai ter q ser informado no programa do SEFAZ  
        }
        
       // if (nf.getNfDtemissao()!= null)  
        b.setBdhEmi(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(dataAtual));  
        
        //if (nf.getNfDtsaida()!= null)  
        b.setBdhSaiEnt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(dataAtual));  
        
        b.setBtpNF("1");//Tipo de Operação( 0-entrada / 1-saída )  
        b.setBidDest("1");//1 - Operação Interna; 2 - Interestadual; 3 - Exterior.
        b.setBcMunFG("5300108"); //Código do Município DF
        b.setBtpImp("0"); //Formato de Impressão do DANFE - 0-Sem geração de DANFE; 1-DANFE normal, Retrato;  2-DANFE normal, Paisagem; 3-DANFE Simplificado;
        b.setBtpEmis("1"); //Tipo de Emissão da NF-e, 1 – Normal – emissão normal
        b.setBcDV("0");//Informar o DV da Chave de Acesso da NF-e. (Não sei pq é 9)
        b.setBtpAmb("1"); // Identificação do Ambiente, 1-Produção/ 2-Homologação
        b.setBfinNFe("1"); //Finalidade de emissão da NFe, 1- NF-e normal/ 2-NF-e complementar / 3 – NF-e de ajuste
        b.setBindFinal("1");//Indica operação com consumidor final ( 0 - Não; 1 - Consumidor Final.)
        b.setBindPres("0");//Indicador de presença do comprador  no estabelecimento comercial no  momento da operação ( 0 - Não se aplica;   1 - Operação presencial;  2 - Não presencial, internet;   3 - Não presencial, teleatendimento;  9 - Não presencial, outros. )
        b.setBprocEmi("3");//Processo de emissão da NF-e, 3- emissão NF-e pelo contribuinte com aplicativo fornecido pelo Fisco.  
        b.setBverProc("4.01_b018");//Versão do Processo de emissão da NF-e
        
        corpo += construirTxt.NfeIdentificacao(b);  
  
  
        //GRUPO C  - DADOS DO EMITENTE DA NOTA - EMPRESA
        
        //c.setC02Cnpj("02417059000105");  
        c.setC02Cnpj(removeCharEpecial(empresa.getCnpj()));
        
        //c.setC17Ie("0756387700196");           
        c.setC17Ie(removeCharEpecial(empresa.getInscricaoEstadual()));
        
        //c.setC03Xnome("DF COM DE PROD MED E ODON LTDA");  
        c.setC03Xnome(empresa.getRazaoSocial());
        
        //c.setC04Xfant("DENTAL DF");
        c.setC04Xfant(empresa.getNomeFantasia());
        
        //c.setC06Xlgr("ST SCIA QUADRA 13 CONJ 04 LT");  
        c.setC06Xlgr(empresa.getEndereco());
        
        c.setC07Nro(empresa.getEnderecoNumero());  
        
        c.setC09Bairro(empresa.getEnderecoBairro());  
        c.setC10Cmun(empresa.getEnderecoCodMunicipio());  
        
        //c.setC11Xmun("Brasilia");  
        c.setC11Xmun(empresa.getCidade());
        
        //c.setC12Uf("DF");  
        c.setC12Uf(empresa.getEstado());
        
        c.setC13Cep(removeCharEpecial(empresa.getCep()));
        
        c.setC14Cpais("1058");  
        c.setC15Xpais("BRASIL");  

        //c.setC16Fone("08007271420");
        c.setC16Fone(removeCharEpecial(empresa.getTelefone()));
        
        c.setC21Crt("1");//SIMPLES NACIONAL - REGIME TRIBUTÁRIO
        
        corpo += construirTxt.NfeIdentificacaoEmitente(c);
  
        //GRUPO E - DADOS DO CLIENTE
        Cliente cliente = venda.getCliente();
        
        if(cliente != null){
	        e.setEXnome(cliente.getNome());
	        
	        //true - PJ, false - PF
	        if(venda.getCliente().getTipoPessoa()){
	        	 e.setE02CNPJj(removeCharEpecial(cliente.getCpfCnpj()));  
	        	 
	        	 if(cliente.getRgIe() != null || !cliente.getRgIe().equals("")){
	        		 	e.setEIE(removeCharEpecial(cliente.getRgIe()));
		         		e.setEindIEDest("1");// 1 - Contribuinte ICMS (informar a IE do destinatário);
	        	 } else {
	        		 e.setEindIEDest("2");// 2 - Contribuinte isento de Inscrição no cadastro de Contribuintes do ICMS;
	        	 }
	        	 
	        }else{  
	            e.setE03CPF(removeCharEpecial(cliente.getCpfCnpj()));  
	            e.setEindIEDest("2");// 2 - Contribuinte isento de Inscrição no cadastro de Contribuintes do ICMS;
	        }
        }

        //e.setEemail(cliente.getEmail()); Não é obrigatório
        
        Set<Endereco> endList= cliente.getEnderecos();
        
        String uf= "";
        
		for (Endereco endereco : endList) {

			uf = endereco.getUf();
			e.setE5XLgr(endereco.getEndereco());
			e.setE5nro(String.valueOf(endereco.getNumero()));
			e.setE5xBairro(endereco.getBairro());
			e.setE5CEP(removeCharEpecial(endereco.getCep()));
			break;// para pegar somente o primeiro endereço
		}
		
        //Esta fixo pois nao temos cadastro de municipio no sistema, ainda.
        e.setE5cMun("5300108");//Codigo do Municipio
        e.setE5xMun("Brasilia");//Nome Municipio
        e.setE5UF("DF");//e.setE12Uf(endereco.getUf());//UF
        
        e.setE5cPais("1058");  
        e.setE5xPais("BRASIL");
        
       
       e.setE5fone(removeCharEpecial(cliente.getTelefone())); //retirar mascara telefone
       
        
        corpo += construirTxt.NfeIdentificacaoDestinario(e);  
  
        int nItem = 1;  
  
        double totIcmsSt = 0;  
        //double totBcSt = 0;  
        double totDesc = 0;  
       // double totIcms = 0;  
        //double totServ = 0;  
        boolean contemInfoAdd = false;
        
        Set<ProdutoVendido> prodVendidosList = venda.getProdutoVendidos();
        
        int numItem = 1;
        
        for (ProdutoVendido produtoVendido : prodVendidosList){ 
        	
        	LoteProduto loteProduto = produtoVendido.getLoteProduto();
        	Produto produto = loteProduto.getProduto();
        	
        	//Só para nao dar erro.
        	ProdutosNotaFiscal pnf = new ProdutosNotaFiscal();
        	pnf.setPnfOrigem("XXX");
        	
        	//GRUPO H  - Número de itens (Produtos) na venda
        	if(prodVendidosList != null){
        		i.setH02Nitem("" + numItem++);  
        	}
            //GRUPO I  
        	//I|CProd  |CEAN|XProd|NCM|EXTIPI|CFOP|UCom|QCom|VUnCom|VProd|CEANTrib|UTrib|QTrib|VUnTrib|VFrete|VSeg|VDesc|vOutro|
        	//indTot|xPed|nItemPed|I|CProd|CEAN|XProd|NCM|EXTIPI|CFOP|UCom|QCom|VUnCom|VProd|CEANTrib|UTrib|QTrib|VUnTrib|VFrete|VSeg|VDesc|vOutro|
        	//indTot|xPed|nItemPed|
        	
        	
            double desconto = 0;  
            
            if(produto.getCodProdutoExterno() != null && !produto.getCodProdutoExterno().trim().equals("")){
            	i.setI02Cprod("" + produto.getCodProdutoExterno());
            } else {
            	i.setI02Cprod("" + produto.getCodProduto());
            }
            
            String dataValidade = "Indet.";//Indeterminada
            if(loteProduto.getDataValidade() != null) {
        	dataValidade = ""+ loteProduto.getDataValidade(); 
            }
            
            String lote = "Indet.";//Indeterminada
            if(loteProduto.getIdentificacaoLote() != null && !"".equals(loteProduto.getIdentificacaoLote().trim())) {
        	lote = loteProduto.getIdentificacaoLote();
            }
            
            i.setI04Xprod(
        	    produto.getNomeProduto() + " " 
        	    + produto.getMarca().getNomeMarca() 
        	    + " Lt. " + lote  
        	    + " Valid. " + dataValidade);
            
            if(produto.getNcm() == null || produto.getNcm().trim().equals("")){ //Temporário até os NCM dos produtos serem preenchidos
            	i.setI05Ncm("99999999");
            } else {
            	i.setI05Ncm(produto.getNcm());
            }
            
            //Se for para fora do DF é 6102
            if(uf != null && !uf.equalsIgnoreCase("DF")) {
            	i.setI08Cfop("6102");//Código Fiscal de Operações e Prestações 
            } else {
            	i.setI08Cfop("5102");//Código Fiscal de Operações e Prestações 
            }
            
            i.setI09Ucom(produto.getUnidade());//Campo unidade na tabela produto
            i.setI10Qcom(formatarDouble(produtoVendido.getQtd(),4).replace(",", "."));
            
            double precoVendaUnitario = produtoVendido.getPrecoVenda();
            
            //Faz o cálculo do valor unitario do produto. O desconto é total, por isso tenho que dividir pela quantidade.
            if(produtoVendido.getDesconto() != null && produtoVendido.getDesconto() > 0){
            	precoVendaUnitario = precoVendaUnitario - (produtoVendido.getDesconto() / produtoVendido.getQtd());
            }
            
            //Faz o cálculo do desconto total em porcentagem sobre o valor unitário do produto
            if(venda.getDescontoTotal() != null && venda.getDescontoTotal() > 0.0f){
            	precoVendaUnitario = precoVendaUnitario - (precoVendaUnitario * venda.getDescontoTotal() / 100);
            }
            
            //Utilizado para resolver problemas de casas decimais a mais que duas.
            precoVendaUnitario = new BigDecimal(precoVendaUnitario).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            
            i.setI10aVuncom(formatarDouble(precoVendaUnitario, 10).replace(",", "."));//Campo V.Unit - Valor Unitario de Comercializacao vUnCom
            i.setI11Vprod(formatarDouble((produtoVendido.getQtd() * precoVendaUnitario)).replace(",", "."));//Campo V.Total - Valor Total Bruto dos Produtos
            i.setI13Utrib(produto.getUnidade());//Campo Unid. - Unidade Tributável
            i.setI14Qtrib(formatarDouble(produtoVendido.getQtd(),4).replace(",", ".")); //Quantidade Tributável
            i.setI14aVuntrib(formatarDouble(precoVendaUnitario,10).replace(",", "."));//Valor Unitário de tributação
            i.setI17bIndtot("1");//Indica que o valor do item nao deve ser somado ao valor total da nota.
            
           /* 
            if (nf.getNfDescontosProdutos() != null) {
            	desconto = nf.getNfDescontosProdutos();  
            }
            
            i.setI02Cprod(String.valueOf(prod.getProCodigo()));  
            i.setI03Cean(prod.getProEan().replace("-", ""));  
            i.setI04Xprod(prod.getProDescricao());  
            i.setI05Ncm(prod.getProNbm());  
            i.setI08Cfop(nf.getNfCfop().toString());  
            i.setI09Ucom(prod.getProUnidcomercial());  
            i.setI10Qcom(formatarDouble(pnf.getPnfQtde()).replace(",", ".")+"00");  
            i.setI10aVuncom(formatarDouble(pnf.getPnfVlr()).replace(",", ".")+"00");  
            i.setI11Vprod(formatarDouble((pnf.getPnfQtde() * pnf.getPnfVlr() )).replace(",", "."));  
            i.setI12Ceantrib(prod.getProEan().replace("-", ""));  
            i.setI13Utrib(prod.getProUnidcomercial());  
            i.setI14Qtrib(formatarDouble(pnf.getPnfQtde()).replace(",", ".")+"00");  
            i.setI14aVuntrib(formatarDouble(pnf.getPnfVlr()).replace(",", ".")+"00");  
            i.setI17Vdesc(formatarDouble(desconto).replace(",", ".")+"00");  
            i.setI17bIndtot("1");  */
  
            corpo += construirTxt.NfeProdutosServicos(i);  
  
            
            //GRUPO N  
            //double bcSt = 0;  
            //double icmsSt = 0;  
            int icms = 0;
           
            n_nfeIcms.setN12ACSosn("400");//SIMPLES NACIONAL
             
            //Se for para fora do DF o icms é 12%
            if(uf != null && !uf.equalsIgnoreCase("DF")) {
            	icms = 12;
            } 
  
            //N02|Orig|CST|ModBC|VBC|PICMS|VICMS|
            
            //Setando dados Dental
            //tributacao = pnf.getTributacao();
            
            
            n_nfeIcms.setN11Orig("0");//Origem da Mercadoria: 0 - Nacional, 1 – Estrangeira – Importação direta; 2 – Estrangeira – Adquirida no mercado interno.
            n_nfeIcms.setN13Modbc("3");//BC do ICMS - 0 - Margem Valor Agregado (%); 1 - Pauta (Valor); 2 - Preço Tabelado Máx. (valor); 3 - valor da operação.
            n_nfeIcms.setN15Vbc(formatarDouble(produtoVendido.getQtd() * precoVendaUnitario).replace(",", "."));//Valor da BC do ICMS
            n_nfeIcms.setN16Picms(formatarDouble(icms).replace(",", "."));//Alíquota do imposto 17%, e 12% para Anestésico
            n_nfeIcms.setN17Vicms(formatarDouble(produtoVendido.getQtd() * precoVendaUnitario * icms/100).replace(",", "."));//Valor total ICMS
         
            //soma o valor dos ICMS para totalizar
            //totIcms += (produtoVendido.getQtd() * precoVendaUnitario) * icms/100;  
            
            //n = gerarTributacao(tributacao, pnf.getPnfOrigem().toString(), bcSt, icmsSt);  
  
            corpo += construirTxt.NfeIcms(n_nfeIcms);  
  
            //totBcSt += bcSt;  
            //totIcmsSt += icmsSt; 
            //totProd += pnf.getPnfQtde() * pnf.getPnfVlr();
            //totProd = produtoVendido.getQtd() * precoVendaUnitario;
            
            totDesc = desconto;  
  
  
            //GRUPO Q  
            
            //Q04|CST|
            
            q.setQ06Cst("06");
            /*q.setQ06Cst("99");  
            q.setQ07Vbc("0.00");  
            q.setQ08Ppis("0.00");  
            q.setQ10Qbcprod("0.00");  
            q.setQ11Valiqprod("0.00");  
            q.setQ09Vpis("0.00");  */
  
            corpo += construirTxt.NfePis(q);  
  
            //LINHA S  
            
            s.setS06Cst("06");  
           /* s.setS07Vbc("0.00");  
            s.setS08Pconfins("0.00");  
            s.setS09Qbcprod("0.00");  
            s.setS10Valiqprod("0.00");  
            s.setS11Vconfins("0.00");  */
  
  
            corpo += construirTxt.NfeConfins(s);  
        }  
        //totProd += totIcmsSt;  
  
                       
        	//A princípio não emitirá nota de serviço.
        	List<Object> list = new ArrayList<Object>();
        	for (Object obj : list){  
                ServicosNotaFiscal snf = (ServicosNotaFiscal)obj;  
             
                //Servicos ser = (Servicos) sessao.get(Servicos.class, snf.getServicos().getSerCodigo());  
                //Servicos ser = null;	
  
                //GRUPO H  
  
  
                i.setH02Nitem(String.valueOf(nItem++));  
  
  
                //GRUPO I  
  
                double desconto = 0;  
  
                if (nf.getNfDescontosServicos()!=null)  
                    desconto = nf.getNfDescontosProdutos();  
  
                //i.setI02Cprod(String.valueOf(ser.getSerCodigo()));  
                //i.setI04Xprod(ser.getSerDescricao());  
                i.setI05Ncm("99");  
                i.setI08Cfop(nf.getNfCfop().toString());  
                i.setI09Ucom("SERVIC");  
                i.setI10Qcom("1.0000");  
                i.setI10aVuncom(formatarDouble(snf.getSnfVlr(),4).replace(",", "."));  
                i.setI11Vprod(formatarDouble(snf.getSnfVlr()).replace(",", "."));  
                i.setI13Utrib("SERVIC");  
                i.setI14Qtrib("1.0000");  
                i.setI14aVuntrib(formatarDouble(snf.getSnfVlr(),4).replace(",", "."));  
                i.setI17bIndtot("1");  
                  
                corpo += construirTxt.NfeProdutosServicos(i);  
  
  
                //GRUPO U  
  
                double bc = 0;  
                double aliquota = 0;  
                double iss = 0;  
  
                if (snf.getSnfBc()!=null){  
                    bc = snf.getSnfBc();  
                }  
  
                if (snf.getSnfAliquota()!=null){  
                    aliquota = snf.getSnfAliquota();  
                }  
  
                if (snf.getSnfIssqn()!=null){  
                    iss = snf.getSnfIssqn();  
                }  
  
                u.setU02Vbc(formatarDouble(bc).replace(",", "."));  
                u.setU03Valiq(formatarDouble(aliquota).replace(",", "."));  
                u.setU04Vissqn(formatarDouble(iss).replace(",", "."));  
                u.setU05Cmunfg(String.valueOf(snf.getCidades().getCidCodigo()));  
                u.setU06Clistserv(snf.getSnfListaservico().toString());  
                u.setU07Csittrib(snf.getSnfTributacao().toString());  
  
                corpo += construirTxt.NfeIssqn(u);  
                  
                //totIss += iss;  
                //totBcServico += bc;  
               // totServ += snf.getSnfVlr();  
                totDesc = desconto;  
  
                //GRUPO Q  
  
  
               /* q.setQ06Cst("99");  
                q.setQ07Vbc("0.00");  
                q.setQ08Ppis("0.00");  
                q.setQ10Qbcprod("0.00");  
                q.setQ11Valiqprod("0.00");  
                q.setQ09Vpis("0.00");  */
  
                corpo += construirTxt.NfePis(q);  
  
                //LINHA S  
  
  
                s.setS06Cst("99");  
                s.setS07Vbc("0.00");  
                s.setS08Pconfins("0.00");  
                s.setS09Qbcprod("0.00");  
                s.setS10Valiqprod("0.00");  
                s.setS11Vconfins("0.00");  
  
  
                corpo += construirTxt.NfeConfins(s);  
            }  
  
  
        //GRUPO W - TOTAIS  
        //W02|vBC|vICMS|                  vBCST|vST|vProd|vFrete|vSeg|vDesc|vII|vIPI|vPIS|vCOFINS|vOutro|vNF|              versão 2.0
        //W02|vBC|vICMS|vICMSDeson|vBCST|vST|vProd|vFrete|vSeg|vDesc|vII|vIPI|vPIS|vCOFINS|vOutro|vNF|vTotTrib|  versão 3.1
         String zero = formatarDouble(0).replace(",", ".");
        	
        //w.setW03Vbc(formatarDouble(venda.getValorTotalVenda()).replace(",", "."));//Base de Cálculo do ICMS  
        w.setW02Vbc(formatarDouble(0).replace(",", "."));//Base de Cálculo do ICMS - SIMPLES NACIONAL
        
		w.setW02VICMS(zero);// Valor Total do ICMS
		w.setW02VICMSDeson(zero);
		w.setW02VBCST(zero);//Base de Cálculo do ICMS ST
		w.setW02Vst(formatarDouble(totIcmsSt).replace(",", "."));//Valor Total do ICMS ST
		w.setW02Vprod(formatarDouble(venda.getValorTotalVenda()).replace(",", "."));//Valor Total dos produtos e serviços
		w.setW02Vfrete(zero);
		w.setW02Vseg(zero);
		w.setW02Vdesc(formatarDouble(totDesc).replace(",", "."));//Valor Total do Desconto
		w.setW02VII(zero);
		w.setW02VIPI(zero);
		w.setW02VPIS(zero);
		w.setW02VCOFINS(zero);
		w.setW02VOutro(	zero);
		w.setW02VNF(formatarDouble(venda.getValorTotalVenda()).replace(",","."));//Valor Total da NF-e
		w.setW02VTotTrib(zero);//ERA "" E AGORA É "0.00" - VERSÃO 4.00
		
		//Totais par serviço...
		/*if (totIss > 0) {
			w.setW18Vserv(formatarDouble(totServ).replace(",", "."));
			w.setW19Vbc(formatarDouble(totBcServico).replace(",", "."));
			w.setW20Viss(formatarDouble(totIss).replace(",", "."));
			w.setW21Vpis("0.00");
			w.setW22Vconfins("0.00");
		}*/
  
        /*w.setW03Vbc(formatarDouble(totBc).replace(",", "."));  
        w.setW04Vicms(formatarDouble(totIcms).replace(",", "."));  
        w.setW05Vbcst(formatarDouble(totBcSt).replace(",", "."));  
        w.setW06Vst(formatarDouble(totIcmsSt).replace(",", "."));  
        w.setW07Vprod(formatarDouble(totProd).replace(",", "."));  
        w.setW10Vdesc(formatarDouble(totDesc).replace(",", "."));  
        w.setW16Vnf(formatarDouble(totProd+totServ-totDesc).replace(",", "."));  
  
        if (totIss>0){  
            w.setW18Vserv(formatarDouble(totServ).replace(",", "."));  
            w.setW19Vbc(formatarDouble(totBcServico).replace(",", "."));  
            w.setW20Viss(formatarDouble(totIss).replace(",", "."));  
            w.setW21Vpis("0.00");  
            w.setW22Vconfins("0.00");  
        }  */
  
        corpo += construirTxt.NfeValoresTotais(w);  
  
        //GRUPO X  -- TRANSPORTE
  
        x.setX02Modfrete("4");//ALTERADO DE 9 PRA 4 - VERSAO 4.00 
        /*x.setX02Modfrete(nf.getNfModalidadefrete().toString());  
          
        if (nf.getNfTransTipopessoa()=='F'){  
            x.setX05Cpf(nf.getNfTransDocumento());  
        }else{  
            x.setX04Cnpj(nf.getNfTransDocumento());  
        }  
        x.setX06Xnome(nf.getNfTransportador());  
  
        String endereco = "";  
        if (nf.getNfTransEndereco()!=null && !nf.getNfTransEndereco().isEmpty()){  
            endereco = String.valueOf(nf.getNfTransEndereco());  
              
            if (nf.getNfTransNumero()!=null){  
                endereco += ", "+String.valueOf(nf.getNfTransNumero());  
            }  
            if (nf.getNfTransBairro()!=null && !nf.getNfTransBairro().isEmpty()){  
                endereco += ", "+nf.getNfTransBairro();  
            }  
        }  
        String cidade = "";  
        String estado = "";  
        if (nf.getCidadesByCidTransporte()!=null){  
            cidade = String.valueOf(nf.getCidadesByCidTransporte().getCidDescricao());  
            estado = nf.getCidadesByCidTransporte().getEstados().getEstSigla();  
        }  
  
        x.setX08Xender(endereco);  
        x.setX09Xmun(cidade);  
        x.setX10Uf(estado);  */
  
        corpo += construirTxt.NfeInformacoesTransporte(x);  

        Nfe_DadosCobranca y = new Nfe_DadosCobranca();
        y.setY10Vdup(formatarDouble(venda.getValorTotalVenda()).replace(",","."));// dados de cobrança
  
        corpo += construirTxt.NfeDadosCobranca(y);
  
        //GRUPO Z  
  
        //z.setZ03Infcpl("DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL,NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.");
        if(contemInfoAdd){
        	z.setZ03Infcpl("DECRETO Nº 33.820/12 CADERNO I (ROMANO) ITEM 53 CONVENIO ICMS 126/10.");
        	corpo += construirTxt.NfeInformacoesAdicionais(z);
        }
  
    }  
    
    private String formatarDouble(double d){
    	return formatarDouble(d, 0);
    }
    
    private String removeCharEpecial(String x){
    	
    	if(x == null){
    		return "";
    	}
    	return x.replace(".", "").replace("-", "").replace("/", "").replace("(", "").replace(")", "").replace(" ", "");
    }
    
    /**
     * Formatar double com 2 casas decimais.
     * @param d
     * @return
     */
    private String formatarDouble(double d, int numCasasDecimais){
    	
    	DecimalFormat df = new DecimalFormat("0.00");
    	String numFormatado = "" + df.format(d);
    	
    	for(int i=0; i<numCasasDecimais - 2 ; i++){
    		numFormatado += "0";
    	}
    	
    	return  numFormatado;
    }
}
