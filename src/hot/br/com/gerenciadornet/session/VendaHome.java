package br.com.gerenciadornet.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

import br.com.gerenciadornet.entity.Acompanhamento;
import br.com.gerenciadornet.entity.Anotacao;
import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Empresa;
import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.LoteProduto;
import br.com.gerenciadornet.entity.ProdutoPedido;
import br.com.gerenciadornet.entity.ProdutoVendido;
import br.com.gerenciadornet.entity.ProdutoVendidoId;
import br.com.gerenciadornet.entity.Recebimento;
import br.com.gerenciadornet.entity.Servico;
import br.com.gerenciadornet.entity.ServicoRealizado;
import br.com.gerenciadornet.entity.StatusVenda;
import br.com.gerenciadornet.entity.TipoPagamento;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.nfe.txt.GeradorNfeFachada;
import br.com.gerenciadornet.util.CalculosUtil;
import br.com.gerenciadornet.util.Constantes;
import br.com.gerenciadornet.util.LogUtil;
import br.com.gerenciadornet.util.Transacoes;

@SuppressWarnings("serial")
@Scope(ScopeType.CONVERSATION)
@Name("vendaHome")
public class VendaHome extends EntityHome<Venda> {

	@In(create = true)
	ClienteList clienteList;
	
	@In(create = true)
	ClienteHome clienteHome;
	
	@In(create = true)
	LoteProdutoHome loteProdutoHome;
	
	@In(create = true)
	ProdutoVendidoHome produtoVendidoHome;
	
	@In(create = true)
	RecebimentoHome recebimentoHome;
	
	@In(create = true)
	Transacoes transacoes;
	
	@In	
	EntityManager entityManager;
	
	@In 
	Usuario user;
	
	@In
	Identity identity;
	
	@In(scope=ScopeType.SESSION)
	Empresa empresa;
	
	@In
	FacesMessages facesMessages;
	
	private int tabIndex = 1;
	
	
	//Cliente
	private Integer codCliente;
	
	//servico
	private Servico servicoRealizado;
	private Float valorServicoRealizado;
	private Float subTotalvalorServico;
	private Float subTotalvalorServicoCobrado 	= new Float(0f);	
	private Float subTotalLucroServico 		= new Float(0f);
	private int codServicoSelecionado;
	
	//produto
	private ProdutoVendido loteProdutoVendidoSelected = new ProdutoVendido(new LoteProduto(1));
	private int subTotalProdutoQtd;
	private Float subTotalProdutoValorUnit 	= new Float(0f);;
	private Float subTotalProdutoDesconto  	= new Float(0f);;
	private Float subTotalProduto 		= new Float(0f);
	private Float subTotalLucroProduto 	= new Float(0f);
	private int codProdutoSelecionado;
	private int qtdProdutoVendido 		= 1;
	
	//ProdutoPedido
	private int   subTotalProdutoPedidoQtd	    	= 0;
	private Float subTotalProdutoPedidoValorUnit 	= new Float(0f);
	private Float subTotalProdutoPedidoDesconto  	= new Float(0f);
	private Float subTotalProdutoPedido	    	= new Float(0f);
	
	//Venda
	private Float valorTotalVenda 			= new Float(0f);
	private Float valorTotalVendaComDesconto	= new Float(0f);
	private Float valorDescontoTotalVenda 		= new Float(0f);
	private Float trocoVenda 			= new Float(0f);
	private Float valorPago 			= new Float(0f);	
	boolean finalizarVenda 				= false;
	
	boolean exibirProdutosPedidos			= false;
	
	//Campos para confiramação da Entrega do Estoque
	private String codBarrasValidacao;
	private List<ProdutoVendido> produtoVendidosConfirmacao = new ArrayList<ProdutoVendido>();
	private int subTotalProdutosConfirmadosQtd;
	boolean produtosValidados	 		= false;

	//TODO FALTA IMPLEMENTAR DESCONTO EM REAIS, NO TOTAL DA VENDA
	//TEM Q ADICIONAR UM CAMPO PARA ARMAZER O VALOR OU CONVERTER EM % E SALVAR..
	private Float descTotalReais = new Float(0f);
	
	private Date dataAtual = new Date();	
	
	private LoteProduto loteProdutoSelecionado = new LoteProduto();	

	@Override
	protected Venda createInstance() {
		
		Venda venda = new Venda();
		
		if(user.getPerfil().getCodPerfil().intValue() == Constantes.VENDEDOR.intValue()){
			venda.setUsuario(user);
		}
		
		return venda;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}		
	}
	
	public void setarVenda(Venda venda){
		setInstance(venda);
	}

	private boolean jaCarregado = false;
	private Integer ultimaVendaCarregada = 0;
	
	public void wire() {
						
		Venda venda = getInstance();
		
		if(jaCarregado && ultimaVendaCarregada != null && ultimaVendaCarregada.intValue() == venda.getCodVenda().intValue())
			return;
					
		//if(venda.getProdutoVendidos() != null && venda.getProdutoVendidos().size() > 0){
			calcularSubtotaisProduto(venda.getProdutoVendidos());
		//} 
		
		//if(venda.getProdutoPedidos() != null && venda.getProdutoPedidos().size() > 0){
			calcularSubtotaisProdutoPedido(venda.getProdutoPedidos());
		//}
		
		//if(venda.getServicoRealizados() != null){
			calcularSubtotalServico(venda.getServicoRealizados());
		//}		
		
		//Para o cliente vir selecionado na combo
		this.codCliente = venda.getCliente().getCodCliente();
		
		jaCarregado = true;
		ultimaVendaCarregada = venda.getCodVenda();
	} 

	public boolean isWired() {
		Venda venda = getInstance();
		if(venda.getServicoRealizados().size() == 0 && venda.getProdutoVendidos().size() ==0){
			return false;						
		}
		return true;
	}

	public Venda getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	

	public List<ProdutoVendido> getProdutoVendidosOrdenados() {
		
		if( getInstance() == null){
			return null;
		}
		
		ArrayList<ProdutoVendido> prod = new ArrayList<ProdutoVendido>(getInstance().getProdutoVendidos());
		Collections.sort(prod, new ComparadorProdutos());
		return prod;
		
	}
	
	public List<ProdutoVendido> getProdutoVendidosOrdenadosLocalizador() {
		
		if( getInstance() == null){
			return null;
		}
		
		ArrayList<ProdutoVendido> prod = new ArrayList<ProdutoVendido>(getInstance().getProdutoVendidos());
		Collections.sort(prod, new ComparadorLocalizador());
		return prod;
		
	}
	
	/**
	 * Comparador de produtos por nome do produto
	 *
	 */
	public static class ComparadorProdutos implements Comparator<ProdutoVendido>, Serializable
    {

        private static final long serialVersionUID = 1L;

        public int compare(final ProdutoVendido objeto1, final ProdutoVendido objeto2)
        {
            return objeto1.getLoteProduto().getProduto().getNomeProduto().compareTo(objeto2.getLoteProduto().getProduto().getNomeProduto());
        }
    }
	
	/**
	 * Comparador de produtos por localizador do produto
	 *
	 */
	public static class ComparadorLocalizador implements Comparator<ProdutoVendido>, Serializable
    {

        private static final long serialVersionUID = 1L;

        public int compare(final ProdutoVendido objeto1, final ProdutoVendido objeto2)
        {
        	if(objeto1.getLoteProduto().getProduto().getLocalizadorDefault() == null &&
        			objeto2.getLoteProduto().getProduto().getLocalizadorDefault() == null){
        		return 0;
        		
        	} else if(objeto1.getLoteProduto().getProduto().getLocalizadorDefault() == null ){
        		return -1;
        		
        	} else if(objeto2.getLoteProduto().getProduto().getLocalizadorDefault() == null){
        		return 1;
        	}
            return objeto1.getLoteProduto().getProduto().getLocalizadorDefault().compareTo(objeto2.getLoteProduto().getProduto().getLocalizadorDefault());
        }
    }
	
	/**
	 * Adiciona um servico selecionado na comboBox ah lista de servicos
	 * realizados.
	 */
	public void addServico() {

		if (getServicoRealizado() == null) {
			addFacesMessage("Nenhum servico foi selecionado.", "");		
			return;
		} else {
			if (getValorServicoRealizado() == null) {
					setValorServicoRealizado(0.0f);	
			}
			
			ServicoRealizado servicoRealizado = new ServicoRealizado();
			servicoRealizado.setServico(getServicoRealizado());
			servicoRealizado.setPreco(getValorServicoRealizado());
			servicoRealizado.setVenda(getInstance());
			
			Set<ServicoRealizado> servicoRealizadoList = getInstance().getServicoRealizados();

			if (servicoRealizadoList.size() > 0) {

				boolean addServico = true;

				for (ServicoRealizado servicoRealizadoAux : servicoRealizadoList) {
					if (servicoRealizadoAux.getServico().getCodServico() == servicoRealizado
							.getServico().getCodServico()) {
						addServico = false;
						addFacesMessage("Servico ja adicionado.");
					}
				}
				if (addServico) {
					servicoRealizadoList.add(servicoRealizado);
				}

			} else {
				servicoRealizadoList.add(servicoRealizado);
			}
			
			getInstance().setServicoRealizados(servicoRealizadoList);			
			calcularSubtotalServico(servicoRealizadoList);		
		}
	}

	/**
	 * Adiciona o valor do servico selecionado na comboBox ao campo Valor
	 * Cobrado, via requisicao Ajax.
	 */
	public void addValorServico() {

		Servico servico = getServicoRealizado();
		if (servico != null)
			setValorServicoRealizado(servico.getPrecoServico());
		else
			setValorServicoRealizado(0.0f);
	}

	/**
	 * Remove um servico da lista de servicosRealizados.
	 */
	public void removeServico() {
		Set<ServicoRealizado> servicoRealizadoList = getInstance().getServicoRealizados();
		Set<ServicoRealizado> servicoRealizadoListAux = new HashSet<ServicoRealizado>();
		
		
		for (ServicoRealizado servicoRealizado : servicoRealizadoList) {
			
			// se nao for o servico excluido, eh readicionado na nova lista.
			if ( servicoRealizado.getServico().getCodServico() != getCodServicoSelecionado() ) {
				servicoRealizadoListAux.add(servicoRealizado);
			
			//se for o servico excluido
			} else {
				if(servicoRealizado.getId() != null){
					
					Venda venda = getInstance();
					entityManager.remove(servicoRealizado);
					
					Servico servico = servicoRealizado.getServico();
					String log = LogUtil.logHistorico("O servico codigo " + servico.getCodServico() 
							+  "-" + servico.getDescServico() 
							+" R$ " + servico.getPrecoServico() 
							+ " foi excluido da venda " + venda.getCodVenda());
									
					Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
					entityManager.persist(historico);					
					}
			}
		}
		getInstance().setServicoRealizados(servicoRealizadoListAux);
		calcularSubtotalServico(servicoRealizadoListAux);
	}

	/**
	 * Calcula o valor do subTotalServico 
	 * @param servicoRealizadoList
	 */
	private void calcularSubtotalServico(Set<ServicoRealizado> servicoRealizadoList) {
		
		Float subTotalValorServico = new Float(0.0f);
		Float subTotalValorServicoCobrado = new Float(0.0f);
		Float subTotalLucroServicoCobrado = new Float(0.0f);

		for (ServicoRealizado servicoRealizado2 : servicoRealizadoList) {
			Servico servico = servicoRealizado2.getServico();
			subTotalValorServico += servico.getPrecoServico();
			subTotalValorServicoCobrado += servicoRealizado2.getPreco();
			subTotalLucroServicoCobrado += servicoRealizado2.getPreco() - servico.getPrecoCusto();
		}

		setSubTotalvalorServicoRealizado(subTotalValorServico);
		setSubTotalvalorServicoCobrado(subTotalValorServicoCobrado);
		setSubTotalLucroServico(subTotalLucroServicoCobrado);
		calcularTotalVenda();
	}

	
	
	/**
	 * Adiciona um produto a lista de produtos adicionados.
	 */
	public void addProduto() {
		
		ProdutoVendido produtoVendidoSelecionado = getLoteProdutoVendidoSelected(); 
		
		if (produtoVendidoSelecionado.getLoteProduto().getCodLoteProduto() == null || produtoVendidoSelecionado.getPrecoVenda() == null) {
			produtoVendidoSelecionado.setPrecoVenda(0.0f);
			addFacesMessage("Nenhum produto foi selecionado ou Preco invalido.", "");		
			return;			
		} 
		if (produtoVendidoSelecionado.getQtd() == 0) {
			addFacesMessage("Produto nao adicionado. Quantidade invalida.", "");		
			return;			
		} 
		if (produtoVendidoSelecionado.getDesconto() == null) {
			produtoVendidoSelecionado.setDesconto(new Float(0f));	
		} 
		
		Float descMaxPerc = produtoVendidoSelecionado.getLoteProduto().getPrcDescontoMaximo();
		Float precoVendaPadrao = produtoVendidoSelecionado.getLoteProduto().getPrecoVendaUnidade();
		Float precoMin = precoVendaPadrao - (precoVendaPadrao * descMaxPerc / 100);
			
		
		if (produtoVendidoSelecionado.getQtd() * produtoVendidoSelecionado.getPrecoVenda() - produtoVendidoSelecionado.getDesconto() < produtoVendidoSelecionado.getQtd() * precoMin) {
			addFacesMessage("Produto nao adicionado. Desconto maior que o permitido. Valor do Produto: R$ " 
					+ precoVendaPadrao 
					+ ". Preco Minimo permitido: R$ " 
					+ precoMin, "");		
			return;			
		} 
		
		ProdutoVendido produtoVendido = new ProdutoVendido();
		produtoVendido = produtoVendidoSelecionado;
		
		//ALTERAÇÃO PARA DOM CARECONE
		produtoVendido.setDesconto(produtoVendido.getDesconto() * produtoVendido.getQtd());
		//produtoVendido.setDesconto(produtoVendido.getDesconto());
		produtoVendido.setVenda(getDefinedInstance());
		
		Set<ProdutoVendido> produtoVendidoList = getInstance().getProdutoVendidos();
		
		boolean loteJaAdicionado = false;
		//verifica se o lote jah foi adicionado
		for(ProdutoVendido produtoVendidoAux: produtoVendidoList){
			if(produtoVendidoAux.getLoteProduto().getCodLoteProduto() == produtoVendido.getLoteProduto().getCodLoteProduto()){
				produtoVendidoAux.setQtd(produtoVendidoAux.getQtd() + produtoVendido.getQtd());
				produtoVendidoAux.setDesconto(produtoVendidoAux.getDesconto() + produtoVendido.getDesconto());
				loteJaAdicionado = true;
				//addFacesMessage("Lote ja adicionado. Exclua e inclua novamente com a nova quantidade.");
				//return;
			}
		}
		
		if(!loteJaAdicionado){
			produtoVendidoList.add(produtoVendido);
		}
		
		getInstance().setProdutoVendidos(produtoVendidoList);
		
		//para limpar os campos do produto selecionado anteriormente
		loteProdutoVendidoSelected = new ProdutoVendido(new LoteProduto(1));
		
		//Quando for poder add mais de um lote na mesma venda, add linha abaixo.
		//setLoteProdutoVendidoSelected(new ProdutoVendido());
		
		calcularSubtotaisProduto(produtoVendidoList);
		
	}
	
	/**
	 * Remove um produto da lista de produtos adicionados.
	 * Se nao for o produto excluido, eh readicionado na nova lista.
	 * Se for edicao, remove o produto, atualiza o estoque.
	 * 
	 */
	public void removeProduto() {
		
		Venda venda = getInstance();
		
		Set<ProdutoVendido> produtoVendidoList = venda.getProdutoVendidos();
		Set<ProdutoVendido> produtoVendidoListAux = new HashSet<ProdutoVendido>();

		for (ProdutoVendido produtoVendido : produtoVendidoList) {
						
			if ( produtoVendido.getLoteProduto().getCodLoteProduto()!= getCodProdutoSelecionado()) {
				produtoVendidoListAux.add(produtoVendido);
			} else {				
				if(venda.getCodVenda() != null && produtoVendido.getId() != null){

					//Se for venda, remove o produto e adiciona ao estoque novamente
					if(!venda.getInOrcamento()){
						addEstoque(produtoVendido);
						entityManager.remove(produtoVendido);
						super.update();
						
						String log = 
						    " EditarVenda: O Lote Produto: " + produtoVendido.getLoteProduto().getCodLoteProduto()
						    + " Qtd vendida: " + produtoVendido.getQtd()
						    + " foi excluído da venda " + venda.getCodVenda()
						    ;
						Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
						entityManager.persist(historico);
						
					//se for orcamento, apenas remove 
					} else {
						entityManager.remove(produtoVendido);
					}
				}
			}			
		}
		venda.setProdutoVendidos(produtoVendidoListAux);
		calcularSubtotaisProduto(produtoVendidoListAux);
	}
	
	/**
	 * Realiza o calculo dos subtotais de Quantidade, Valor Unitario, Desconto,
	 * e Subtotal Produto.
	 * @param produtoVendidoList
	 */
	private void calcularSubtotaisProduto(Set<ProdutoVendido> produtoVendidoList) {
		
		int subTotalProdutoQtdAux 			= 0;
		Float subTotalProdutoValorUnitAux	= new Float(0.0f);
		Float subTotalProdutoDescontoAux 	= new Float(0.0f);
		Float subTotalProdutoAux			= new Float(0.0f);
		Float subTotalLucroProdutoAux		= new Float(0.0f);
		
		for (ProdutoVendido produtoVendido : produtoVendidoList) {
			subTotalProdutoQtdAux 		+= produtoVendido.getQtd();
			subTotalProdutoValorUnitAux += produtoVendido.getQtd() * produtoVendido.getPrecoVenda();
			subTotalProdutoDescontoAux 	+= produtoVendido.getDesconto();
			subTotalProdutoAux 			+= produtoVendido.getQtd() * produtoVendido.getPrecoVenda() - produtoVendido.getDesconto();	
			subTotalLucroProdutoAux 	+= (produtoVendido.getQtd() * produtoVendido.getPrecoVenda() - produtoVendido.getDesconto()) - produtoVendido.getQtd() * produtoVendido.getLoteProduto().getPrecoCompraUnidade();
		}
		setSubTotalProdutoQtd(subTotalProdutoQtdAux);
		setSubTotalProdutoValorUnit(subTotalProdutoValorUnitAux);
		setSubTotalProdutoDesconto(subTotalProdutoDescontoAux);
		setSubTotalProduto(subTotalProdutoAux);
		setSubTotalLucroProduto(subTotalLucroProdutoAux);
		calcularTotalVenda();
	}
	
	/**
	 * Realiza o calculo dos subtotais de Quantidade, Valor Unitario, Desconto,
	 * e Subtotal Produto.
	 * @param produtoVendidoList
	 */
	private void calcularSubtotaisProdutoPedido(Set<ProdutoPedido> produtoPedidoList) {
		
		int subTotalProdutoQtdAux = 0;
		Float subTotalProdutoValorUnitAux	= new Float(0.0f);
		Float subTotalProdutoDescontoAux 	= new Float(0.0f);
		Float subTotalProdutoAux		= new Float(0.0f);
		
		for (ProdutoPedido produtoPedido : produtoPedidoList) {
			subTotalProdutoQtdAux += produtoPedido.getQtd();
			subTotalProdutoValorUnitAux += produtoPedido.getQtd() * produtoPedido.getPrecoVenda();
			subTotalProdutoDescontoAux += produtoPedido.getDesconto();
			subTotalProdutoAux += produtoPedido.getQtd() * produtoPedido.getPrecoVenda() - produtoPedido.getDesconto();			
		}
		
		setSubTotalProdutoPedidoQtd(subTotalProdutoQtdAux);
		setSubTotalProdutoPedidoValorUnit(subTotalProdutoValorUnitAux);
		setSubTotalProdutoPedidoDesconto(subTotalProdutoDescontoAux);
		setSubTotalProdutoPedido(subTotalProdutoAux);
		
	}
	
	/**
	 * Quando clicar na pesquisa de loteProdutoPopUp no botao select,
	 * adiciona um LoteProdutoVendido ah tela de venda. 
	 */
	public void selectedLoteProduto(LoteProduto loteProduto) {

		ProdutoVendido produtoVendidoSelecionado = new ProdutoVendido();
		produtoVendidoSelecionado.setLoteProduto(loteProduto);
		produtoVendidoSelecionado.setPrecoVenda(loteProduto.getPrecoVendaUnidade());
		produtoVendidoSelecionado.setQtd(1);
		produtoVendidoSelecionado.setDesconto(0f);
		
		loteProdutoVendidoSelected = produtoVendidoSelecionado;		
	}
	
	/**
	 * Calcula o valor total da Venda, desconto Total, e desconto.
	 */
	public void calcularTotalVenda(){
		
		Venda venda 			= getInstance();		
		
		//Alteação Dom carecone 
		//Float totalGeral 		= subTotalvalorServicoCobrado + subTotalProduto;
		Float totalGeral 		= subTotalvalorServicoCobrado + subTotalProdutoValorUnit;
		
		
		//Float totalGeral 		= subTotalvalorServicoCobrado + subTotalProdutoValorUnit;
		//Float descontoTotalVenda 	= (totalGeral * venda.getDescontoTotal() / 100) + getSubTotalProdutoDesconto();
		Float descontoTotalVenda 	= totalGeral * venda.getDescontoTotal() / 100;
		
		setValorDescontoTotalVenda(descontoTotalVenda + subTotalProdutoDesconto);
		
		Float lucroTotalGeral 		= subTotalLucroProduto + subTotalLucroServico - descontoTotalVenda;
		
		setValorTotalVenda(totalGeral);
		setValorTotalVendaComDesconto(totalGeral - descontoTotalVenda - subTotalProdutoDesconto);
		
		//Persist o valor total da venda e o lucro total da venda.
		//Arredondando para duas casas decimais.
		venda.setValorTotalVenda( Math.round((getValorTotalVendaComDesconto())*100)/100.0f);
		venda.setLucroTotalVenda(Math.round(lucroTotalGeral*100)/100.0f);
		
		setTrocoVenda(valorPago - valorTotalVendaComDesconto);		
	}
	
	
	public boolean descontoValido = false;
	public boolean adimplente     = true;
	public boolean vendaAprovada  = false;
	
	/**
	 * Verifica se o desconto dado para o cliente da venda eh
	 * igual ou inferior ao cadastrado para este cliente.
	 * E se o cliente está adimplente.
	 * 
	 * Caso o desconto seja valido returna true.
	 * Desconto eh valido automaticamente para ADMINISTRADOR e SUPERVISOR
	 * @return
	 */ 
	public void validarVenda(){
		
		
		/**
		 * Alteração DomCarecone
		 */
		//Se for administrador ou supervisor nao precisa validar desconto nem cliente
		if(user.getPerfil().getCodPerfil().intValue() == Constantes.ADMINISTRADOR.intValue() ||				
				user.getPerfil().getCodPerfil().intValue() == Constantes.ESTOQUE.intValue()){
			setDescontoValido(true);
			vendaAprovada = true;
			adimplente = true;
			return;
		}
		
		Venda venda = getInstance(); 
		
		//Verifica se o cliente esta inadimplente. Se o usuario for != ADMINISTRADOR & SUPERVISOR pede autorizacao
		if(venda.getCliente() != null && !getInstance().getCliente().isInAdimplente() ){
			adimplente = false;
			return;
		} else {
			adimplente = true;
		}
		
		//---- Validacao do desconto dado na venda
		/**
		 * Alteração DomCarecone
		 */
		Float descontoMax = 100.0f;
		setDescontoValido(false);
		
		float totalSemDesconto = getSubTotalProdutoDesconto() + getSubTotalProduto();
		float descontoTotal = getSubTotalProdutoDesconto() + getValorDescontoTotalVenda();
		float percentualDescRealizado = 0;
		
		if(totalSemDesconto > 0){
			percentualDescRealizado = descontoTotal * 100/totalSemDesconto;
		}
		
		if( percentualDescRealizado <= descontoMax){
			setDescontoValido(true);
		} 
		
		if(descontoValido && adimplente){
			vendaAprovada = true;
		} else {
			vendaAprovada = false;
		}
	}
	
	
	private boolean vendaAutorizada = false;
	private String funcAutorizador;
	private String senhaAutorizador;
	
	/**
	 * Realiza e grava a autorizacao de uma venda
	 * com desconto superior ao pre determinado.
	 * So quem autoriza eh administrador
	 */
	public String realizarVendaAutorizada(){
		
		try {
			Usuario admin = (Usuario) entityManager
					.createQuery("from Usuario usu where usu.loginUsuario = :login " +
							"and usu.senha = :password and usu.inExclusao = :inExclusao")
							
					.setParameter("login", funcAutorizador)
					.setParameter("password", senhaAutorizador)
					.setParameter("inExclusao", false).getSingleResult();
			
			if(admin.getPerfil().getCodPerfil().intValue() == Constantes.ADMINISTRADOR.intValue() ||					
					possuiAcesso(transacoes.getTransacaoLiberarVendaNaoAutorizada())){
				
				vendaAutorizada = true;
				
				if(this.isManaged()){
					return update();
				}
				return persist();
			} else {
				addFacesMessage("Usuario nao possui acesso GN073 para autorizar esta venda.", "");
				return null;
			}
			
		} catch (NoResultException e) {
			addFacesMessage("Usuario ou senha invalido.", "");
			return null;
		}
		
	}
	
	StatusVenda statusInicial = new StatusVenda(Constantes.ABERTA);

	/**
	 * Sala uma venda inicial com Status "ABERTA".
	 * 
	 * @return
	 */
    public String saveUpdate() {
    
		statusInicial = new StatusVenda(Constantes.ABERTA);

		if (empresa.getInFluxo().intValue() == Constantes.IN_FLUXO_RESUMIDO) {
			statusInicial = new StatusVenda(Constantes.CONFIRMADA_ESTOQUE);
		} 

		if (this.isManaged()) {
			return update();
		}
		return persist();
    }
	
	/**
	 * Salva uma venda inicial com Status "Em Edição".
	 * Método chamado pela tela AutorizacaoPopUp.xhtml
	 * 
	 * @return
	 */
	public String saveUpdateEditavel(){
	    
	    statusInicial =  new StatusVenda(Constantes.EM_EDICAO);
	    
	    if(this.isManaged()){
	    	return update();
	    } 
	    return persist();
	}
	 
	
	/**
	 * Realiza a inclusao de uma nova venda.
	 */
	@Override
	public String persist(){
		
		if(!possuiAcesso(transacoes.getTransacaoVendaInserir())){
			return null;
		}
		
		Venda venda = getInstance();
		
		if(!validarQtdEstoque(venda)) {
			return null;
		}
		
		/**
		 * Alteração DomCarecone
		 */
		//venda.setUsuario(user);
		if(venda.getUsuario() == null){
		    addFacesMessage("Selecione o Funionario/Vendedor.");
		    return null;
		}
		
		venda.setUsuarioCadastro(user);
		venda.setDataInicioVenda(new Date(System.currentTimeMillis()));
		venda.setCanal(0);
		venda.setInNfeEmitida(false);
		
		//NOVO
		if(this.getCodCliente() == null){
			Cliente cliente = new Cliente(1, "Venda Balcao");
			venda.setCliente(cliente);			
		} else {
			detalharCliente();
		}
		
		//Eh uma venda e eh para ser finalizada 
		if(finalizarVenda && !venda.getInOrcamento()){
			venda.setUsuarioFinalizador(user);
			venda.setStatusVenda(new StatusVenda(Constantes.CONCLUIDA));
			venda.setDataFimVenda(venda.getDataInicioVenda());
			
		//Eh um orcamento.
		} else if(venda.getInOrcamento()){			
			venda.setStatusVenda(new StatusVenda(Constantes.ORCAMENTO));
		//Eh uma venda Aberta
		} else {
			venda.setStatusVenda(statusInicial);
		}
		
		entityManager.persist(venda);			
		persistServicosRealizados(venda);
		persistProdutosVendidos(venda);	
		
		if(!venda.getInOrcamento()){
		    
		    	if(Constantes.EM_EDICAO.equals(statusInicial.getCodStatusVenda())) {
		    	    //Gera o acompanhamento da venda
		    	    gravarAcompanhamento(venda, "VENDA_EM_EDICAO");
		    	} else {
		    	    //Gera o acompanhamento da venda
		    	    gravarAcompanhamento(venda, "VENDA_ABERTA");
		    	    
		    	    gravarLogVenda("INSERT", venda);		    	    		    	   
		    	}
		    	
			//Gera os pagamentos conforme o tipo de pagamento da venda.
			gravarRecebimentos(venda);
		} else {
			gravarAcompanhamento(venda, "ORÇAMENTO");
		}
		
		if(vendaAutorizada){
			String log = LogUtil.logHistorico("A venda " + venda.getCodVenda() + " foi autorizada por " + funcAutorizador +".");
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
		}
				 
		return super.persist();
	}
	
	/**
	 * Volta uma venda Finalizada pra o status CONFIRMADA_ESTOQUE.
	 * 
	 */
	public String desfazerVendaFinalizada(){
		
		if(user.getPerfil().getCodPerfil().intValue() != Constantes.ADMINISTRADOR.intValue()){
		    addFacesMessage("Somente Administrador pode desafazer uma venda finalizada.");
		    return null;
		}
		
		Venda venda = getInstance();
		
		if(venda.getInOrcamento()){
		    addFacesMessage("Orcamento nao pode ser desfeito.");
		    return null;
		}
		
		
		//Gera o acompanhamento da venda
		gravarAcompanhamento(venda, "VENDA_EXTORNADA");
		
		venda.setUsuarioFinalizador(null);
		venda.setStatusVenda(new StatusVenda(Constantes.CONFIRMADA_ESTOQUE));
		venda.setDataFimVenda(null);
		addFacesMessage("A VENDA codigo " + venda.getCodVenda() + " foi EXTORNADA com sucesso.");
		
		super.update();
		
		return "confirmada";
	}
	
	/**
	 * Altera o status para SEPARANDO_MATERIAL, de uma venda que estah com 
	 * status = CONFIRMADA_FINANCEIRO.
	 */
	public String andamentoIniciarSeparacaoMaterial(){
		
	    if(!possuiAcesso(transacoes.getTransacaoConfirmarEntrega())){
		return null;
	    }
		
	    Venda venda = getDefinedInstance();
	    venda.setStatusVenda(new StatusVenda(Constantes.SEPARANDO_MATERIAL));

	    String resposta = super.update();
		 
	    if("updated".equalsIgnoreCase(resposta)) {
		//Gera o acompanhamento da venda
		gravarAcompanhamento(venda, "SEPARANDO MATERIAL");
		addFacesMessage("O material da venda codigo " + venda.getCodVenda() + " esta sendo separado.");
		bloquearEdicaoVenda();
		
	    }else {
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: A venda não pode ter seu STATUS atualizado, tente novamente mais tarde.", "");
		FacesContext.getCurrentInstance().addMessage("err", fm);
		resposta = "error";
	    }
		
	    return resposta;
	}
	
	/**
	 * Altera o status para CONFIRMADA_FINANCEIRO, de uma venda que esta com 
	 * status = ABERTA.
	 */
	public String andamentoLiberarVenda(){
		
		if(!possuiAcesso(transacoes.getTransacaoConfirmarPagamento())){
			return null;
		}
		
		Venda venda = getDefinedInstance();
		venda.setStatusVenda(new StatusVenda(Constantes.CONFIRMADA_FINANCEIRO));

		String resposta  = super.update();

		if("updated".equalsIgnoreCase(resposta)) {
        		
		    //Gera o acompanhamento da venda
		    gravarAcompanhamento(venda, "ENTREGA LIBERADA");
		    addFacesMessage("Venda liberada para entrega com sucesso. Codigo Venda: " + venda.getCodVenda() + ".");
		    
		} else {
		    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: A venda não pode ter seu STATUS atualizado, tente novamente mais tarde.", "");
		    FacesContext.getCurrentInstance().addMessage("err", fm);
		    resposta = "error";
		}
		bloquearEdicaoVenda();
		
		return resposta;
	}
	
	/**
	 * Altera o status para CONFIRMADA_FINANCEIRO, de uma venda que esta com 
	 * status = ABERTA.
	 */
	public String andamentoValidarSeparacao(){
		
		if(!possuiAcesso(transacoes.getTransacaoConfirmarEntrega())){
			return null;
		}
		
		Venda venda = getDefinedInstance();
		venda.setStatusVenda(new StatusVenda(Constantes.EM_FASE_ENTREGA));

		String resposta  = super.update();

		if("updated".equalsIgnoreCase(resposta)) {
        		
		    //Gera o acompanhamento da venda
		    gravarAcompanhamento(venda, "EM FASE DE ENTREGA");
		    addFacesMessage("Venda separada/validada com sucesso. Codigo Venda: " + venda.getCodVenda() + ".");
		    
		} else {
		    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: A venda não pode ter seu STATUS atualizado, tente novamente mais tarde.", "");
		    FacesContext.getCurrentInstance().addMessage("err", fm);
		    resposta = "error";
		}
		bloquearEdicaoVenda();
		
		return resposta;
	}
	
	/**
	 * Permite que uma venda tenha o seu passo de Confirmada_Estoque sem a verificação do
	 * código de barras dos produtos.
	 * 
	 * ACESSO: TRANSACAO_LIBERAR_SEPARACAO_MERCADORIA	= "GN066"
	 * 
	 * @return
	 */
	public String andamentoValidarSeparacaoSemVerificacao(){
	    
	    if(!possuiAcesso(transacoes.getTransacaoLiberarSeparacaoMercadoria())){
		return null;
	    }
	    Venda venda = getDefinedInstance();
	    String retorno = andamentoValidarSeparacao();
	    	
	    if("confirmada".equalsIgnoreCase(retorno)) {
		String log = LogUtil.logHistorico("A venda " + venda.getCodVenda() + " foi liberada do estoque sem a verificacao do codigo de barras dos produtos.");
    	    	Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
    	    	entityManager.persist(historico);
	    }
	    return retorno;
	}
	
	/**
	 * Altera o status para CONFIRMADA_FINANCEIRO, de uma venda que esta com 
	 * status = ABERTA.
	 */
	public String confirmarEntrega(){
		
		if(!possuiAcesso(transacoes.getTransacaoConfirmarPagamento())){
			return null;
		}
		
		Venda venda = getDefinedInstance();
		venda.setStatusVenda(new StatusVenda(Constantes.CONFIRMADA_ESTOQUE));

		String resposta  = super.update();

		if("updated".equalsIgnoreCase(resposta)) {
        		
		    //Gera o acompanhamento da venda
		    gravarAcompanhamento(venda, "ENTREGA CONFIRMADA");
		    addFacesMessage("Venda entregue com sucesso. Codigo Venda: " + venda.getCodVenda() + ".");
		    
		} else {
		    FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: A venda não pode ter seu STATUS atualizado, tente novamente mais tarde.", "");
		    FacesContext.getCurrentInstance().addMessage("err", fm);
		    resposta = "error";
		}
		bloquearEdicaoVenda();
		
		return resposta;
	}
	
	/**
	 * Finaliza uma venda que estah com 
	 * status = Aberta. Verifica se a qtd do orcamento
	 * ainda existe em estoque e realiza o debito do estoque.
	 */
	public String andamentoFinalizarVenda(){
		
		if(!possuiAcesso(transacoes.getTransacaoVendaFinalizar())){
			addFacesMessage("Usuário não possui acesso na transação Venda Finalizar - " + transacoes.getTransacaoVendaFinalizar());
			return null;
		}
		
		//Venda venda = getDefinedInstance(); Alterado para finalizar a venda a partir da tela de Gerenciar Recebimentos
		Venda venda = getInstance();
		
		if(venda.getInOrcamento()){
		    addFacesMessage("Orcamento nao pode ser finalizado.");
		    return null;
		}
		
		if(venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.CONFIRMADA_ESTOQUE.intValue()){
		    addFacesMessage("A venda " + venda.getCodVenda() + " não pode ser finalizada pois não esta no Status CONFIRMADA_ESTOQUE.");
		    return null;
		}
		
		//valida o valor total da venda com a soma dos recebimentos
		if(!validarTotalRecebimentos()) {
			addFacesMessage("A soma dos \'Valores a Receber\' dos recebimentos referentes a venda não conferem com o \'Valor Total da Venda\'.");
			return null;
		}
		
		//Gera o acompanhamento da venda
		gravarAcompanhamento(venda, "VENDA FINALIZADA");
		
		venda.setUsuarioFinalizador(user);
		venda.setStatusVenda(new StatusVenda(Constantes.CONCLUIDA,"Concluída"));
		venda.setDataFimVenda(new Date(System.currentTimeMillis()));
		addFacesMessage("A VENDA codigo " + venda.getCodVenda() + " foi finalizada com sucesso.");
		
		bloquearEdicaoVenda();
		realizarRecebimentos();
		
		return "finalizada";
	}
	
	/**
	 * Finaliza TODAS as vendas de uma resultado de consulta do resultlist.
	 */            
	public String andamentoFinalizarTodasVendas(List<Venda> vendaList){
		
		if(!possuiAcesso(transacoes.getTransacaoVendaFinalizar())){
			addFacesMessage("Usuário não possui acesso na transação Venda Finalizar - " + transacoes.getTransacaoVendaFinalizar());
			return null;
		}
		
		StringBuilder vendasNaoFinalizadas = new StringBuilder();
		boolean possuiVendasNaoFinalizadas = false;
		
		for(Venda venda : vendaList){
		
			if(venda.getInOrcamento()){
			  continue; //passa para próxima venda
			}
		
			if(venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.CONFIRMADA_ESTOQUE.intValue()){
				continue;
			}
		
			//valida o valor total da venda com a soma dos recebimentos
			if(!validarTotalRecebimentos()) {
				vendasNaoFinalizadas.append(venda.getCodVenda() + ", ");
				possuiVendasNaoFinalizadas = true;
				continue;
			}
		
			//Gera o acompanhamento da venda
			gravarAcompanhamento(venda, "VENDA FINALIZADA");
			
			venda.setUsuarioFinalizador(user);
			venda.setStatusVenda(new StatusVenda(Constantes.CONCLUIDA,"Concluída"));
			venda.setDataFimVenda(new Date(System.currentTimeMillis()));
						
			setInstance(venda);
			super.update();
			
			//Limpa todas as mensagens, assim mostrará somente um mensagem de update
			//que será do update abaixo.
			facesMessages.clearGlobalMessages();
			
			bloquearEdicaoVenda();
			realizarRecebimentos();
		}
		
		if(possuiVendasNaoFinalizadas){
			addFacesMessage("As vendas " + vendasNaoFinalizadas + " não foram finalizadas pois os valores de recebimentos divergem com o valor da venda.");
		}
		
		return "finalizada";
	}
	
	/**
	 * TODO À IMPLEMENTAR........
	 * 
	 * Finaliza TODAS as vendas de uma resultado de consulta do recebimentoList.
	 */
	public String andamentoFinalizarVendasPorRecebimento(List<Recebimento> recebimentoList){
		
		//Valida o acesso do usuário
		if(!possuiAcesso(transacoes.getTransacaoVendaFinalizar())){
			addFacesMessage("Usuário não possui acesso na transação Venda Finalizar - " + transacoes.getTransacaoVendaFinalizar());
			return null;
		}
			
		StringBuilder vendasNaoFinalizadas = new StringBuilder();
		boolean possuiVendasNaoFinalizadas = false;
		
		//Array com a lista de Vendas
		ArrayList<Integer> ultimosCodigosVenda = new ArrayList<Integer>();
		
		//Verifica se todos os recebimentos possuem data de balancete e valor pago.
		for(Recebimento recebimento : recebimentoList){
			
			if(recebimento.getDataBalancete() == null || recebimento.getValorPago() == null ){
				
				vendasNaoFinalizadas.append("O recebimento da venda "
						+ recebimento.getVenda().getCodVenda()
						+ " não está com a Data de Balancete ou o Valor Recebido preenchidos.");
				
				addFacesMessage(vendasNaoFinalizadas + "");
				return null;
			}
		}
		
		
		for(Recebimento recebimento : recebimentoList){
			
			Venda venda = recebimento.getVenda();
			setInstance(venda);
			
			//Se a venda estiver concluída não faz nada 
			if(venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.CONCLUIDA.intValue() ){
				continue;
			}
			
			if(!ultimosCodigosVenda.contains(venda.getCodVenda())){
				
				ultimosCodigosVenda.add(venda.getCodVenda());
				
				//Validando o status da venda
				if(venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.CONFIRMADA_ESTOQUE.intValue() ){
					
					vendasNaoFinalizadas.append("A venda "
							+ venda.getCodVenda()
							+ " não foi finalizada pois não está no status de Confirmada Estoque.");
					possuiVendasNaoFinalizadas = true;
					
				} else if(validarTotalRecebimentos()) {
					
					gravarAcompanhamento(venda, "VENDA FINALIZADA");
					
					venda.setUsuarioFinalizador(user);
					venda.setStatusVenda(new StatusVenda(Constantes.CONCLUIDA,"Concluída"));
					venda.setDataFimVenda(new Date(System.currentTimeMillis()));
					venda.setInTrava(true);
					
					super.update();
					
				} else {
					vendasNaoFinalizadas.append(venda.getCodVenda() + ", ");
				
					vendasNaoFinalizadas.append("As vendas "
							+ vendasNaoFinalizadas.replace(vendasNaoFinalizadas.lastIndexOf(","), vendasNaoFinalizadas.lastIndexOf(","), "")  
							+ " não foram finalizadas pois os valores de recebimentos divergem com o valor da venda.");
					
					possuiVendasNaoFinalizadas = true;
				}
				
			}
		}
		
		if(possuiVendasNaoFinalizadas){
			addFacesMessage(vendasNaoFinalizadas + "");
		}
		
		return "finalizada";
	}
	
	/**
	 * Finaliza uma venda que estah com 
	 * status = Aberta. Verifica se a qtd do orcamento
	 * ainda existe em estoque e realiza o debito do estoque.
	 */
	public String andamentoFinalizarVenda(Venda venda){
		
		setInstance(venda);
		return andamentoFinalizarVenda();
		
	}
	
	//Css utilizado para pintar o footer do dataTable de conferência de produtos.
	String cssFooterConfProduto = "center";
	/**
	 * Metodo utilizado na tela VendaEditConfirmaEntregaEstoque.xhml para comparar
	 * o código de barras do produto que esta sendo separado com o produto vendido.
	 */
	public void validarProdutos() {
	    
	    Venda venda = getDefinedInstance();
	    Set<ProdutoVendido> produtosVendidos = venda.getProdutoVendidos();
	    
	    boolean produtoConfirmado 	= false;
	    boolean qtdUltrapassada 	= false;
	   // boolean loteAdicionado 	= false;
	    
	    //Percorre a lista de produtosVendidos para verificar se o código de barras é válido
	    for (ProdutoVendido produtoVendido : produtosVendidos) {
		
		LoteProduto loteProdutoAux = produtoVendido.getLoteProduto();
		
		if(loteProdutoAux.getProduto().getCodigoBarrasProduto().equalsIgnoreCase(codBarrasValidacao)) {
		    
		    produtoConfirmado = true;
		    
		    if(produtoVendido.getQtd() > produtoVendido.getQtdConfirmada()) {	
			
			produtoVendido.setQtdConfirmada(produtoVendido.getQtdConfirmada() + 1);
	    		subTotalProdutosConfirmadosQtd++;
	    		cssFooterConfProduto = "footerOk";
	    		qtdUltrapassada = false;
	    		
	    		if(produtoVendido.getQtd() == produtoVendido.getQtdConfirmada()) {
	    		    produtoVendido.setQtdTotalSeparada(true);
	    		}
	    		break;
	    		
		    } else {
			qtdUltrapassada = true;
		    }
		   
		}
		
	    }
	    
	    //Se a qtd de itens ultrapassou a vendida, dá erro.
	    if(qtdUltrapassada) {
		addFacesMessage("A quantidade adicionada não pode ser maior que a quantidade vendida.");
	    	cssFooterConfProduto = "footerBad";
	    	return;
	    }
	    	
	    if(!produtoConfirmado) {
		addFacesMessage("Produto inválido! Código de barras não localizado.");
		cssFooterConfProduto = "footerBad";
		return;
	    }
	    if(subTotalProdutoQtd == subTotalProdutosConfirmadosQtd) {
		produtosValidados = true;
		cssFooterConfProduto = "footerOkFinish";
	    } 
	    
	    codBarrasValidacao = null;
	}
	
	/**
	 * Gera um acompanhamento da venda. Para saber quando e por quem foram dados os andamentos.
	 * @param venda
	 * @param observacao
	 */
	private void gravarAcompanhamento(Venda venda, String observacao){
		
		Acompanhamento acompanhamento = new Acompanhamento(new Date(System.currentTimeMillis()), observacao, user, venda.getStatusVenda(), venda);
		entityManager.persist(acompanhamento);
		venda.getAcompanhamentos().add(acompanhamento);
	}
	
	/**
	 * Salva os recebimentos de uma venda de acordo com o valor e tipo de pagamento.
	 * @param venda
	 */
	private void gravarRecebimentos(Venda venda){
		
		TipoPagamento tipoPagamento = venda.getTipoPagamento();
		Set<Recebimento> recebimentosList = new TreeSet<Recebimento>();
		
		Date dataPrimeiraParcela  = venda.getDataCobrancaVenda();
		
		if(dataPrimeiraParcela == null){
			dataPrimeiraParcela = new Date(System.currentTimeMillis());
		}
		
		//calcular datas dos recebimentos
		List<Date> datasRecebimentos = CalculosUtil.calcularDatasPagamento(tipoPagamento, dataPrimeiraParcela);
		
		//calcular valores dos recebimentos
		List<Float> valoresRecebimentos  = CalculosUtil.calcularParcelas(tipoPagamento, venda.getValorTotalVenda());
		
		if(tipoPagamento.getNumVezes() == 0 || tipoPagamento.getNumVezes() == 1){
			
			Recebimento recebimento = new Recebimento(venda, datasRecebimentos.get(0), valoresRecebimentos.get(0));
			recebimentosList.add(recebimento);
			entityManager.persist(recebimento);
			
		} else {
			for(int i = 0; i < tipoPagamento.getNumVezes(); i++){
				Recebimento recebimento = new Recebimento(venda, datasRecebimentos.get(i), valoresRecebimentos.get(i));
				entityManager.persist(recebimento);
				recebimentosList.add(recebimento);
			}
		}
		venda.setRecebimentos(recebimentosList);
	}
	
	/**
	 * Método utilizado para atualizar apenas os valores dos recebimentos.
	 * É utilizado quando uma venda teve uma exclusão de produtos na separação de material. Ou seja, alguns
	 * produtos foram excluídos e o valor tem que ser atualizado
	 * @param venda
	 */
	private void updateValoresRecebimentos(Venda venda){
		
		TipoPagamento tipoPagamento = venda.getTipoPagamento();
		Set<Recebimento> recebimentosList = venda.getRecebimentos();
		int numeroParcela = 0;
		
		//calcular valores dos recebimentos
		List<Float> valoresRecebimentos  = CalculosUtil.calcularParcelas(tipoPagamento, venda.getValorTotalVenda());
		
		//Se tiver o mesmo número de parcelas e recebimentos gravados
		if(valoresRecebimentos.size() == recebimentosList.size()){
			for (Recebimento recebimento : recebimentosList) {
					recebimento.setValorAReceber(valoresRecebimentos.get(numeroParcela));
					recebimentoHome.setInstance(recebimento);
					recebimentoHome.update();
					numeroParcela++;
			}
		} else {
			removeRecebimento();
			gravarRecebimentos(venda);
		}
				
	}
	
	
	
	/**
	 * Remove um servico da lista de servicosRealizados.
	 */
	public void removeRecebimento() {
		Set<Recebimento> recebimentoList = getInstance().getRecebimentos();
		
		for (Recebimento recebimentoAux : recebimentoList) {
			recebimentoHome.setInstance(recebimentoAux);
			recebimentoHome.remove();
		}
		getInstance().setRecebimentos(null);
	}
	
	/**
	 * Atualiza uma venda.
	 */
	@Override
	public String update() {
		
		Venda venda = getInstance();
		int statusVenda = venda.getStatusVenda().getCodStatusVenda().intValue();
		
		if(!possuiAcesso(transacoes.getTransacaoVendaEditar())){
			facesMessages.add("Usuario nao possui acesso para realizar Edicao de Venda. Transação " 
					+ transacoes.getTransacaoVendaEditar(), FacesMessage.SEVERITY_INFO);
			return null;
		}
		
		if(this.getCodCliente() == null){
			Cliente cliente = new Cliente(1, "Venda Balcao");
			venda.setCliente(cliente);			
		} else {
			detalharCliente();
		}
		
		persistServicosRealizados(venda);
		persistProdutosVendidos(venda);	
				
		//ser for um orcamento que virou venda
		if(venda.getInOrcamento() == false && 
				(venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.ORCAMENTO.intValue()
						|| venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.PEDIDO.intValue() 
						|| venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.PEDIDO_AUTORIZADO.intValue())){
			
			venda.setDataInicioVenda(new Date(System.currentTimeMillis()));			
			
			if(finalizarVenda){
				venda.setUsuarioFinalizador(user);
				venda.setStatusVenda(new StatusVenda(Constantes.CONCLUIDA));
				venda.setDataFimVenda(new Date(System.currentTimeMillis()));
			} else {
				if( venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.PEDIDO.intValue() 
						&& venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.PEDIDO_AUTORIZADO.intValue()){
					venda.setUsuario(user);
				}
				//Alteração fluxo da venda. Já vai pra Confirmada pagamento. Dom Carecone
				//venda.setStatusVenda(new StatusVenda(Constantes.ABERTA));statusInicial
				venda.setStatusVenda(new StatusVenda(statusInicial.getCodStatusVenda()));
				gravarAcompanhamento(venda, "VENDA_ABERTA");
			}
			
		} else if(!venda.getInOrcamento()){
			removeRecebimento();
		}
		
		//Se a venda estava em edição e foi dado o comando de salvar, altera o status pra ABERTA
		if(statusVenda == Constantes.EM_EDICAO.intValue() && 
				(statusInicial.getCodStatusVenda().intValue() == Constantes.CONFIRMADA_ESTOQUE.intValue() ||
						statusInicial.getCodStatusVenda().intValue() == Constantes.ABERTA.intValue())) {
			
		    venda.setDataInicioVenda(new Date(System.currentTimeMillis()));
		    venda.setStatusVenda(new StatusVenda(statusInicial.getCodStatusVenda()));
		    gravarAcompanhamento(venda, "VENDA_ABERTA");
		    
		}
		
		if(statusVenda != Constantes.ABERTA.intValue()  
				&& statusVenda != Constantes.EM_EDICAO.intValue() 
				&& statusVenda != Constantes.PEDIDO.intValue() 
				&& statusVenda != Constantes.PEDIDO_AUTORIZADO.intValue()
				&& statusVenda != Constantes.ORCAMENTO.intValue() 
				&& statusVenda != Constantes.CONFIRMADA_FINANCEIRO.intValue()){
		    
			bloquearEdicaoVenda();
		}
		
		String update = super.update();
		
		//se for venda atualiza os recebimentos, excluindo e incluindo novamente.
		if(!venda.getInOrcamento() && statusVenda != Constantes.CANCELADA.intValue() 
				&& statusVenda != Constantes.ORCAMENTO.intValue() && statusVenda != Constantes.PEDIDO.intValue() 
				&& statusVenda != Constantes.PEDIDO_AUTORIZADO.intValue() ){
			
			gravarRecebimentos(venda);
		}
		
		facesMessages.clear();
		facesMessages.add("Atualização realizada com sucesso.", FacesMessage.SEVERITY_INFO);
		
		gravarLogVenda("UPDATE", venda);
		
		return update;
	}
	
	private void gravarLogVenda(String tipoLog, Venda venda){
		
		 String log = LogUtil.logHistorico(tipoLog + " Venda: " + venda.getCodVenda() + 
	    		" Cliente: " + venda.getCliente().getCodCliente() + 
	    		" Vendedor: " + venda.getUsuario().getCodUsuario() +
	    		" Usu. Cadast.: " + venda.getUsuarioCadastro().getCodUsuario() + 
	    		" Orçamento: " + venda.getInOrcamento() +
	    		" Valor Total: " + venda.getValorTotalVenda() +
	    		" Lucro Total: " + venda.getLucroTotalVenda() 
	    		);
	    
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
	}
	
	/**
	 * Realiza um orcamento, ou seja, pega um orcamento e transforma em venda.
	 * @return
	 */
	public String realizarVenda(){

		Venda venda = getInstance();
		//Validar disponibilidade de produtos em estoque.
		if(!validarQtdEstoque(venda)){
			return null;
		}
		venda.setInOrcamento(false);
		
		//para não perder o cliente no update //DENTALDF TESTAR TODO
		this.codCliente = venda.getCliente().getCodCliente();
				
		gravarRecebimentos(venda);
		
		return update();  

	}
	
	/**
	 * Realiza um pedido, ou seja, pega um pedido e redireciona para 
	 * a página responsável por o transformar em venda.
	 * @return
	 */
	public String realizarPedido(){

		Venda venda = getInstance();
		venda.setInOrcamento(false);
		
		//para não perder o cliente no update //DENTALDF TESTAR TODO
		this.codCliente = venda.getCliente().getCodCliente();
		
		return "realizarPedido";  

	}
	
	/**
	 * Sempre que for realizar um orcamento ou venda, 
	 * validar se os produtos ainda estao disponiveis
	 * em estoque.
	 * @param venda
	 * @return
	 */
	private boolean validarQtdEstoque(Venda venda){
		
		Set<ProdutoVendido> produtosVenda  = venda.getProdutoVendidos();
		boolean inValidacao = true;
		
		for(ProdutoVendido produtoVendido : produtosVenda){
		
			LoteProduto lp = produtoVendido.getLoteProduto();
			
			entityManager.refresh(lp);
			
			//Refaz a consulta no BD pra recarregar o loteProduto
			LoteProduto lpReloaded = (LoteProduto) entityManager
				.createQuery("from LoteProduto where codLoteProduto = :codLoteProduto")
				.setParameter("codLoteProduto", lp.getCodLoteProduto()).getSingleResult();
			
			if( produtoVendido.getQtd() > lpReloaded.getQtdEstoque()){
				facesMessages.add("O produto \'" + lp.getProduto().getNomeProduto() 
						+ "\' não possui a quantidade disponvível em estoque. Quantidade em estoque: " 
						+ lp.getQtdEstoque(), FacesMessage.SEVERITY_INFO);
				inValidacao = false;
			}
		}
		return inValidacao;
	}
	
	
	/**
	 * Persite uma lista de servicos realizados.
	 * No caso de edicao remove todos os servicos e realiza
	 * o persist novamente.
	 * @param venda
	 */
	private void persistServicosRealizados(Venda venda){
		
		if(venda.getServicoRealizados().size() > 0 ){
			//utilizado para resolver erro de concorrencia ConcurrentModificationException.			
			CopyOnWriteArraySet<ServicoRealizado> servicoRealizadoList = new CopyOnWriteArraySet<ServicoRealizado>(venda.getServicoRealizados());
			
			//ERROR: No caso de nova venda, depois já editar dá o seguinte error: org.hibernate.NonUniqueObjectException
			//No caso de edicao, remove e adiciona os servicos novamente.
			for (ServicoRealizado servicoRealizado : servicoRealizadoList) {
				if(servicoRealizado.getId() != null){
					entityManager.remove(servicoRealizado);	
				}
			}
			for (ServicoRealizado servicoRealizado : servicoRealizadoList) {	
				servicoRealizado = new ServicoRealizado(venda, servicoRealizado.getServico(), servicoRealizado.getPreco());
				entityManager.persist (servicoRealizado);	
			}
			
		}
	}
	
	/**
	 * Persiste uma lista de lotes de produtos vendidos.
	 * @param venda
	 */
	private void persistProdutosVendidos(Venda venda){
		if(venda.getProdutoVendidos().size() > 0 ){

			CopyOnWriteArraySet<ProdutoVendido> produtoVendidoList = new CopyOnWriteArraySet<ProdutoVendido>(venda.getProdutoVendidos());
			
			//No caso de edicao, remove tudo e inclui novamente.
			for (ProdutoVendido produtoVendido : produtoVendidoList) {
				if(produtoVendido.getId() != null){
					produtoVendidoHome.setInstance(produtoVendido);
					produtoVendidoHome.remove();
					
					//Se nao for orcamento.. adiciona ao estoque..
					if(!venda.getInOrcamento() && venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.ORCAMENTO.intValue()){
						addEstoque(produtoVendido);
					}						
				}
			}
			
			for (ProdutoVendido produtoVendido : produtoVendidoList) {
				
				LoteProduto loteProduto = produtoVendido.getLoteProduto();	
				
				ProdutoVendidoId id = new ProdutoVendidoId(loteProduto.getCodLoteProduto(), venda.getCodVenda());
										
				produtoVendido = new ProdutoVendido(id, venda, loteProduto, produtoVendido.getQtd(),
						produtoVendido.getDesconto(), produtoVendido.getPrecoVenda());
				
				entityManager.persist(produtoVendido);
				
				if(!venda.getInOrcamento()){
					//debitar do estoque(LoteProduto) se for venda.
					int qtdEstoque = loteProduto.getQtdEstoque();
					qtdEstoque -= produtoVendido.getQtd();
					
					if(qtdEstoque < 0){
						addFacesMessage("Nao e possivel vender uma quantidade maior que o estoque.");
						return;
					} else {
						loteProduto.setQtdEstoque(qtdEstoque);
						entityManager.persist(loteProduto);						
					}					
				}
			}
		}
	}
		
	
	/**
	 * Altera o status de um venda para Cancelada.
	 */
	@Override
	public String remove() {
		
		if(!possuiAcesso(transacoes.getTransacaoVendaExcluir())){
			return null;
		}
		
		String log = "";
		
		Venda venda = getInstance();
		
		entityManager.refresh(venda);
		
		//Verifica se a venda já não tinha sido concluída. Para evitar erro de concorrencia.
		if(venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.CANCELADA.intValue()) {
			addFacesMessage("A venda " + venda.getCodVenda() + " ja estava cancelada. Atualize sua pesquisa.", "");
			return null;
		}
		
		venda.setDataFimVenda(new Date(System.currentTimeMillis()));
		venda.setUsuarioFinalizador(user);
		venda.setStatusVenda(new StatusVenda(Constantes.CANCELADA));
		
		bloquearEdicaoVenda();
		
		super.update();
		
		if(!venda.getInOrcamento()){
			for(ProdutoVendido produtoVendido: venda.getProdutoVendidos()){			
				addEstoque(produtoVendido);
			}
			
			removeRecebimento();
			
			addFacesMessage("A venda " + venda.getCodVenda() + " foi cancelada com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Venda", venda.getCodVenda(), venda.getCliente().getNome().toUpperCase() 
					+ " R$" + venda.getValorTotalVenda());
							
			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
			
			gravarAcompanhamento(venda, "VENDA CANCELADA");
		}
		return "removed";
	}
	
	private boolean possuiAcesso(String transacao){
		
		if(!identity.hasRole(transacao)){
			addFacesMessage("Usuario nao possui acesso para realizar a transacao: " + transacao, "");
			return false;
		}
		return true;
	}
	
	/**
	 * Remove o produto que ja foi adicionado a uma venda e volta ele
	 * para o estoque, atualizando novamente sua qtd no estoque.
	 * @param produtoVendido
	 */
	private void addEstoque(ProdutoVendido produtoVendido){
		
		int qtdVendida = produtoVendido.getQtd();
		int codLoteProduto = produtoVendido.getLoteProduto().getCodLoteProduto();
		//int qtdEstoque = produtoVendido.getLoteProduto().getQtdEstoque();
		
		//Refaz a consulta no BD pra recarregar a qtd disponível em estoque.
		Integer qtdAtualizada = (Integer) entityManager
			.createNativeQuery("select qtd_estoque from lote_produto where cod_lote_produto = ?")
			.setParameter(1, codLoteProduto )
			.getSingleResult();
		
		
		LoteProduto loteProduto = produtoVendido.getLoteProduto();
		loteProduto.setQtdEstoque(qtdAtualizada + qtdVendida);
		
		loteProdutoHome.setInstance(loteProduto);
		loteProdutoHome.update();
		
	}
			
	/**
	 * Este metodo recebe um status e verifica se o usuario pode dar andamento
	 * a venda de acordo com o status atual em que ela se encontra e de
	 * acordo com seu poder de acesso a transacao correspondente. 
	 * 
	 * @param statusVenda
	 * @return
	 */
	public boolean confirmarAndamentoVenda(StatusVenda statusVenda){
		
	    //O FINANCEIRO PODE CONFIRMAR O PAGAMENTO DE UMA VENDA ABERTA
	    if(statusVenda.getCodStatusVenda().intValue() == Constantes.ABERTA.intValue() 
		    && identity.hasRole(transacoes.getTransacaoConfirmarPagamento())){			
		return true;
	    }
	    //O ESTOQUISTA PODE INICIAR O PROCESSO DE SEPARAÇÃO DE MERCADORIA.
	    if(statusVenda.getCodStatusVenda().intValue() == Constantes.CONFIRMADA_FINANCEIRO.intValue() 
		    && identity.hasRole(transacoes.getTransacaoConfirmarEntrega())){			
		return true;
	    }
	    //O ESTOQUISTA VALIDA O MATERIAL SEPARADO VALIDANDO SEU CODIGO DE BARRAS.
	    if(statusVenda.getCodStatusVenda().intValue() == Constantes.SEPARANDO_MATERIAL.intValue() 
		    && identity.hasRole(transacoes.getTransacaoConfirmarEntrega()) && produtosValidados){			
		return true;
	    }
	    //O ESTOQUISTA CONFIRMA A ENTREGA DA MERCADORIA.
	    if(statusVenda.getCodStatusVenda().intValue() == Constantes.EM_FASE_ENTREGA.intValue() 
		    && identity.hasRole(transacoes.getTransacaoConfirmarEntrega())){			
		return true;
	    }
	    //O FINANCEIRO PODE FINALIZAR A VENDA DEPOIS QUE A MERCADORIA FOI ENTREGUE
	    if(statusVenda.getCodStatusVenda().intValue() == Constantes.CONFIRMADA_ESTOQUE.intValue()
		    && identity.hasRole(transacoes.getTransacaoVendaFinalizar())){			
		return true;
	    }
		
	    return false;
	}
	
	/**
	 * Este metodo eh utilizado pela pagina VendaEditConfirmaEntregaEstoque.xhml 
	 * pra verificar se os produtos ja foram validados. Se nao vier nenhum produto
	 * ou seja, se a venda possui apenas servico nao precisa validar.
	 * produtosValidados = true;
	 * 
	 * @param statusVenda
	 * @return
	 */
	public boolean confirmarAndamentoVendaEstoque(StatusVenda statusVenda) {
	    
	    Venda venda = getDefinedInstance();
	    Set<ProdutoVendido> produtosVendidos = venda.getProdutoVendidos();
	    
	    if(produtosVendidos == null || produtosVendidos.size() == 0) {
	    	produtosValidados = true;
	    }
	    
	    // Valida o total da venda e atualiza o valor caso a venda teve alteração
	    if(!validarTotalRecebimentos()){
	    	updateValoresRecebimentos(venda);
	    }
	    
	    return confirmarAndamentoVenda(statusVenda);
	    
	}
	
	/**
	 * Exibe ou oculta a lista de Produtos Pedidos na 
	 * tela VendaEdit.xhtml 
	 */
	public void exibirOcultarProdutosPedidos(){
		
		if(exibirProdutosPedidos){
			exibirProdutosPedidos = false;
		} else {
			exibirProdutosPedidos = true;
		}
	}
	
	@In
	FacesContext facesContext;
	
	/**
	 * Método que cria o arquivo a ser importado
	 * para geração de nota fiscal eletronica.
	 */
	public void gerarArquivoNFe(){
		
		Venda venda = getDefinedInstance();
		
		if(!possuiAcesso(transacoes.getTransacaoNfeGerarArquivo())){
			return ;
		}

		//Cod usuario 1 = Venda Balcao
		if(venda.getCliente().getCodCliente() == 1){
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "O arquivo não pode ser gerado. Cliente não selecionado.", "");
			FacesContext.getCurrentInstance().addMessage("err", fm);
			return;
		}
		
		if(venda.getNumNotaFiscal()== null || venda.getNumNotaFiscal().trim().equals("")){
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção. Número da nota fiscal não informado, isso pode acarretar erros na importação.", "");
			FacesContext.getCurrentInstance().addMessage("err", fm);
		}
		
		
		if(!venda.getInNfeEmitida()){
			venda.setInNfeEmitida(true);
			super.update();
		}
		
		GeradorNfeFachada gNfeFachada = new GeradorNfeFachada();
		String nfe = gNfeFachada.emitirNFe(empresa, venda);
		
		 try {  
	            ByteArrayOutputStream output = new ByteArrayOutputStream();  
	            output.write(nfe.getBytes());  
	            
	            String nomeArquivo = "NFe-Venda-" + getInstance().getCodVenda() + "-" + new SimpleDateFormat("dd-MM-yyyy").format(dataAtual) + ".txt";	
	            
	            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();  
	            response.setContentType("text/plain");  
	            response.setHeader("Content-Disposition", "attachment;filename=" + nomeArquivo +";");  
	            response.setContentLength(output.toByteArray().length);  

	            ServletOutputStream servletOutputStream;  
	            servletOutputStream = response.getOutputStream();  
	            servletOutputStream.write(output.toByteArray(), 0, output.toByteArray().length);  
	            servletOutputStream.flush();  
	            servletOutputStream.close();  
	            FacesContext.getCurrentInstance().responseComplete();  
	            
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        
			
		
	}
	
	/**
	 * Altera o atributo in_trava de 1(travada) para 0(destravada)
	 * podendo assim a venda ser alterada.  
	 */
	public void liberarEdicaoVenda(){
		
		if(!possuiAcesso(transacoes.getTransacaoLiberarEdicaoVenda())){
			return ;
		}
		
		Venda venda = getDefinedInstance();
		
		//Grava histórico de quem liberou a EDIÇÃO
		String log = LogUtil.logHistorico("A venda " + venda.getCodVenda() + " foi liberada para edição.");
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		venda.setInTrava(false);
		addFacesMessage("Venda liberada para Edição.");
		super.update();
	}
	
	
	/**
	 * Altera o atributo in_nfe_emitida de 1(Emita) para 0(Não Emitida).  
	 */
	public void cancelarEmissaoNfe(){
		
		if(!possuiAcesso(transacoes.getTransacaoLiberarEdicaoVenda())){
			addFacesMessage("Usuário não possui acesso para cancelar a emissão da NFE.");
			return ;
		}
		
		Venda venda = getDefinedInstance();
		
		//Grava histórico de quem liberou a EDIÇÃO
		String log = LogUtil.logHistorico("A Nfe da venda " + venda.getCodVenda() + " teve sua emissão cancelada.");
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		
		entityManager.persist(historico);
		
		venda.setInNfeEmitida(false);
		addFacesMessage("Nfe cancelada com sucesso.");
		super.update();
	}
	
	/**
	 * Método chamada para bloquear a edicao da venda
	 */
	private void bloquearEdicaoVenda(){
		
		//Venda venda = getDefinedInstance();
	    Venda venda = getInstance();
	    venda.setInTrava(true);
		
	}
	
	/**
	 * Método que atualiza a data_balancete e o valor pago de todos os 
	 * recebimentos de uma venda assim que ela for concluída.
	 */
	private void realizarRecebimentos(){
		
		Set<Recebimento> recebimentoList = getInstance().getRecebimentos();
		
		for (Recebimento recebimentoAux : recebimentoList) {
			recebimentoAux.setDataBalancete(new Date(System.currentTimeMillis()));
			recebimentoAux.setValorPago(recebimentoAux.getValorAReceber());
			recebimentoHome.setInstance(recebimentoAux);
			recebimentoHome.update();
		}
	}
	
	/**
	 * Verifica se a soma dos recebimentos é igual ao valor total da venda.
	 * @return
	 */
	private boolean validarTotalRecebimentos(){
		
		Venda venda = getInstance();
		Set<Recebimento> recebimentoList = venda.getRecebimentos();
		boolean recebimentosValidos = false;
		float totalRecebimentos = 0;
		
		for (Recebimento recebimentoAux : recebimentoList) {
			totalRecebimentos += recebimentoAux.getValorAReceber(); 
		}

		//Cálculo utilizado para arrendondar para 2 casas decimais  
		if( Math.round(totalRecebimentos*100)/100.0d == Math.round(venda.getValorTotalVenda()*100)/100.0d) {
			recebimentosValidos = true;
		}
		return recebimentosValidos;
	}
	
	/**
	 * Atualiza os dados do cliente selecionado pelo mesmo
	 * cliente com todos os dados dele.
	 * @return
	 */
	public void detalharCliente(){
		
		Venda venda = getInstance();
		
		if(this.getCodCliente() == null){
			Cliente cliente = new Cliente(1, "Venda Balcao");
			venda.setCliente(cliente);
			
		} else {
			StringBuilder sql = new StringBuilder();
			sql.append("from Cliente c ");
			sql.append("where c.codCliente = :codCliente ");
			
			Cliente cliente = (Cliente)getEntityManager().createQuery(sql.toString())
					.setParameter("codCliente", this.getCodCliente()).getSingleResult();
			
			//Substitui o cliente sem detalhes por ele mesmo com todos os dados
			venda.setCliente(cliente);
		}
	}
	
	/**
	 * método utilizado quando se cadastra um cliente durante
	 * o processo de nova venda.
	 */
	public void persistClientePopUp(){
		
		Cliente cliente = clienteHome.getInstance();
		
		//Seta null no cliente para não dar o erro
		//object references an unsaved transient instance 
		cliente.setEntidade(null);
		
		clienteHome.persist();
		
		cliente = clienteHome.getInstance();
				
		getInstance().setCliente(cliente);
		setCodCliente(cliente.getCodCliente());
		clienteHome.setInstance(new Cliente());
		
	}
	
	/**
	 * Realiza um reload do objeto from DataBase.
	 */
	public void cancelar(){
		entityManager.refresh(this.instance);
	}
	
	public Servico getServicoRealizado() {
		return servicoRealizado;
	}

	public void setServicoRealizado(Servico servicoRealizado) {
		this.servicoRealizado = servicoRealizado;
	}

	public Float getValorServicoRealizado() {
		return valorServicoRealizado;
	}

	public void setValorServicoRealizado(Float valorServicoRealizado) {
		this.valorServicoRealizado = valorServicoRealizado;
	}

	public int getCodProdutoSelecionado() {
		return codProdutoSelecionado;
	}

	public void setCodProdutoSelecionado(int codProdutoSelecionado) {
		this.codProdutoSelecionado = codProdutoSelecionado;
	}

	public Float getSubTotalvalorServicoRealizado() {
		return subTotalvalorServico;
	}

	public void setSubTotalvalorServicoRealizado(
			Float subTotalvalorServicoRealizado) {
		subTotalvalorServico = subTotalvalorServicoRealizado;
	}

	public Float getSubTotalvalorServicoCobrado() {
		return subTotalvalorServicoCobrado;
	}

	public void setSubTotalvalorServicoCobrado(Float subTotalvalorServicoCobrado) {
		this.subTotalvalorServicoCobrado = subTotalvalorServicoCobrado;
	}

	public int getQtdProdutoVendido() {
		return qtdProdutoVendido;
	}

	public void setQtdProdutoVendido(int qtdProdutoVendido) {
		this.qtdProdutoVendido = qtdProdutoVendido;
	}

	public Float getValorPago() {
		return valorPago;
	}

	public void setValorPago(Float valorPago) {
		this.valorPago = valorPago;
	}

	public int getCodServicoSelecionado() {
		return codServicoSelecionado;
	}

	public void setCodServicoSelecionado(int codServicoSelecionado) {
		this.codServicoSelecionado = codServicoSelecionado;
	}

	public ProdutoVendido getLoteProdutoVendidoSelected() {
		return loteProdutoVendidoSelected;
	}

	public void setLoteProdutoVendidoSelected(
			ProdutoVendido loteProdutoVendidoSelected) {
		this.loteProdutoVendidoSelected = loteProdutoVendidoSelected;
	}

	public int getSubTotalProdutoQtd() {
		return subTotalProdutoQtd;
	}

	public void setSubTotalProdutoQtd(int subTotalProdutoQtd) {
		this.subTotalProdutoQtd = subTotalProdutoQtd;
	}

	public Float getSubTotalProdutoValorUnit() {
		return subTotalProdutoValorUnit;
	}

	public void setSubTotalProdutoValorUnit(Float subTotalProdutoValorUnit) {
		this.subTotalProdutoValorUnit = subTotalProdutoValorUnit;
	}

	public Float getSubTotalProdutoDesconto() {
		return subTotalProdutoDesconto;
	}

	public void setSubTotalProdutoDesconto(Float subTotalProdutoDesconto) {
		this.subTotalProdutoDesconto = subTotalProdutoDesconto;
	}

	public Float getSubTotalProduto() {
		return subTotalProduto;
	}

	public void setSubTotalProduto(Float subTotalProduto) {
		this.subTotalProduto = subTotalProduto;
	}

	public Float getValorTotalVenda() {
		return valorTotalVenda;
	}

	public void setValorTotalVenda(Float valorTotalVenda) {
		this.valorTotalVenda = valorTotalVenda;
	}

	public Float getValorDescontoTotalVenda() {
		return valorDescontoTotalVenda;
	}

	public void setValorDescontoTotalVenda(Float valorDescontoTotalVenda) {
		this.valorDescontoTotalVenda = valorDescontoTotalVenda;
	}

	public Float getValorTotalVendaComDesconto() {
		return valorTotalVendaComDesconto;
	}

	public void setValorTotalVendaComDesconto(Float valorTotalVendaComDesconto) {
		this.valorTotalVendaComDesconto = valorTotalVendaComDesconto;
	}

	public Float getTrocoVenda() {
		return trocoVenda;
	}

	public void setTrocoVenda(Float trocoVenda) {
		this.trocoVenda = trocoVenda;
	}

	public LoteProduto getLoteProdutoSelecionado() {
		return loteProdutoSelecionado;
	}

	public void setLoteProdutoSelecionado(LoteProduto loteProdutoSelecionado) {
		this.loteProdutoSelecionado = loteProdutoSelecionado;
	}

	public boolean getFinalizarVenda() {
		return finalizarVenda;
	}

	public void setFinalizarVenda(boolean finalizarVenda) {
		this.finalizarVenda = finalizarVenda;
	}

	public void setVendaCodVenda(Integer id) {
		setId(id);
	}

	public Integer getVendaCodVenda() {
		return (Integer) getId();
	}

	public Date getDataAtual() {
		return dataAtual;
	}
	
	public Float getDescTotalReais()
	{
		return descTotalReais;
	}

	public void setDescTotalReais(Float descTotalReais)
	{
		this.descTotalReais = descTotalReais;
	}

	public int getTabIndex()
	{
		return tabIndex++;
	}

	public String getFuncAutorizador()
	{
		return funcAutorizador;
	}

	public void setFuncAutorizador(String funcAutorizador)
	{
		this.funcAutorizador = funcAutorizador;
	}

	public String getSenhaAutorizador()
	{
		return senhaAutorizador;
	}

	public void setSenhaAutorizador(String senhaAutorizador)
	{
		this.senhaAutorizador = senhaAutorizador;
	}

	public boolean isDescontoValido()
	{
		return descontoValido;
	}
	
	public boolean isAdimplente()
	{
		return adimplente;
	}

	public void setDescontoValido(boolean descontoValido)
	{
		this.descontoValido = descontoValido;
	}

	public Float getSubTotalLucroServico() {
		return subTotalLucroServico;
	}

	public void setSubTotalLucroServico(Float subTotalLucroServico) {
		this.subTotalLucroServico = subTotalLucroServico;
	}

	public Float getSubTotalLucroProduto() {
		return subTotalLucroProduto;
	}

	public void setSubTotalLucroProduto(Float subTotalLucroProduto) {
		this.subTotalLucroProduto = subTotalLucroProduto;
	}

	public boolean isVendaAprovada() {
		return vendaAprovada;
	}

	public int getSubTotalProdutoPedidoQtd() {
		return subTotalProdutoPedidoQtd;
	}

	public void setSubTotalProdutoPedidoQtd(int subTotalProdutoPedidoQtd) {
		this.subTotalProdutoPedidoQtd = subTotalProdutoPedidoQtd;
	}

	public Float getSubTotalProdutoPedidoValorUnit() {
		return subTotalProdutoPedidoValorUnit;
	}

	public void setSubTotalProdutoPedidoValorUnit(
			Float subTotalProdutoPedidoValorUnit) {
		this.subTotalProdutoPedidoValorUnit = subTotalProdutoPedidoValorUnit;
	}

	public Float getSubTotalProdutoPedidoDesconto() {
		return subTotalProdutoPedidoDesconto;
	}

	public void setSubTotalProdutoPedidoDesconto(Float subTotalProdutoPedidoDesconto) {
		this.subTotalProdutoPedidoDesconto = subTotalProdutoPedidoDesconto;
	}

	public Float getSubTotalProdutoPedido() {
		return subTotalProdutoPedido;
	}

	public void setSubTotalProdutoPedido(Float subTotalProdutoPedido) {
		this.subTotalProdutoPedido = subTotalProdutoPedido;
	}

	public boolean isExibirProdutosPedidos() {
		return exibirProdutosPedidos;
	}

	public void setExibirProdutosPedidos(boolean exibirProdutosPedidos) {
		this.exibirProdutosPedidos = exibirProdutosPedidos;
	}

	public List<ServicoRealizado> getServicoRealizados() {	
		return getInstance() == null ? null : new ArrayList<ServicoRealizado>(
				getInstance().getServicoRealizados());
	}

	public List<ProdutoVendido> getProdutoVendidos() {
		return getInstance() == null ? null : new ArrayList<ProdutoVendido>(
				getInstance().getProdutoVendidos());
	}
	
	public List<ProdutoPedido> getProdutoPedidos() {
		return getInstance() == null ? null : new ArrayList<ProdutoPedido>(
				getInstance().getProdutoPedidos());
	}
	
	public List<Endereco> getEnderecosCliente() {
	    
	    if( getInstance() == null) {
		return null;
	    } else if(getInstance().getCliente() == null) {
		return null;
	    } else {
		return new ArrayList<Endereco>(getInstance().getCliente().getEnderecos());
	    }
	}
		
	public List<Acompanhamento> getAcompanhamentos() {
		return getInstance() == null ? null : new ArrayList<Acompanhamento>(
				getInstance().getAcompanhamentos());
	}
	
	public List<Recebimento> getRecebimentos() {
		return getInstance() == null ? null : new ArrayList<Recebimento>(
				getInstance().getRecebimentos());
	}
	
	public List<Anotacao> getAnotacoes() {
	    if( getInstance() == null) {
		return null;
	    } else if(getInstance().getCliente() == null) {
		return null;
	    } else { 
		return new ArrayList<Anotacao>(getInstance().getCliente().getAnotacoes());
	    }
	}

	public String getCodBarrasValidacao() {
	    return codBarrasValidacao;
	}

	public void setCodBarrasValidacao(String novoCodBarrasValidacao) {
	    codBarrasValidacao = novoCodBarrasValidacao;
	}

	public List<ProdutoVendido> getProdutoVendidosConfirmacao() {
	    return produtoVendidosConfirmacao;
	}

	public void setProdutoVendidosConfirmacao(
		List<ProdutoVendido> novoProdutoVendidosConfirmacao) {
	    produtoVendidosConfirmacao = novoProdutoVendidosConfirmacao;
	}
	
	public int getSubTotalProdutosConfirmadosQtd() {
	    return subTotalProdutosConfirmadosQtd;
	}

	public boolean isProdutosValidados() {
	    return produtosValidados;
	}

	public String getCssFooterConfProduto() {
	    return cssFooterConfProduto;
	}

	public Integer getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}
}
