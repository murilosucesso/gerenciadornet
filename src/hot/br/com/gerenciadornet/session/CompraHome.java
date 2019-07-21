package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

import br.com.gerenciadornet.entity.Compra;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.LoteProduto;
import br.com.gerenciadornet.entity.Marca;
import br.com.gerenciadornet.entity.Pagamento;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.TipoDebito;
import br.com.gerenciadornet.entity.TipoPagamento;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.CalculosUtil;
import br.com.gerenciadornet.util.Constantes;
import br.com.gerenciadornet.util.Transacoes;

@Name("compraHome")
public class CompraHome extends EntityHome<Compra> {

	private static final long serialVersionUID = 1L;

	@In
	EntityManager entityManager;

	@In(create = true)
	Usuario user;
	
	@In(create = true)
	LoteProdutoHome loteProdutoHome;
	
	@In(create = true)
	ProdutoHome produtoHome;
	
	@In(create = true)
	PagamentoHome pagamentoHome;
	
	@In
	Identity identity;
	
	@In(create = true)
	MarcaList marcaList;
	
	@In(create = true)
	ProdutoList produtoList;
	
	@In(create = true)
	TipoDebitoList tipoDebitoList;
	
	@In(create = true)
	Transacoes transacoes;
	
	//Novo Lote Produto
	private LoteProduto novoLoteProduto = new LoteProduto(1, 0, 100f);	
	private Float percentLucro = new Float(0f);
	private String loteSelecionado;
	private String produtoLocalizadorDefault;

	//totais
	//private Float valorTotalCompra = new Float(0f);	
	//private Float valorPrecoVendaTotal = new Float(0f);
	//private Float lucroTotalCompra = new Float(0f);
	//private int qtdTotalProduto = 0;
	
	//selects na tela compraEdit
	private Marca marcaSelecionada = new Marca();	
	private List<Marca> marcas = new ArrayList<Marca>(0);
	private List<Produto> produtos = new ArrayList<Produto>(0);
	
	//Produto selecionado no suggetionBox
	private Produto produtoSelecionado = new Produto();
	
	public void setCompraCodCompra(Integer id) {
		setId(id);
	}

	public Integer getCompraCodCompra() {
		return (Integer) getId();
	}

	@Override
	protected Compra createInstance() {
		Compra compra = new Compra();
		compra.setDataCompra(new Date(System.currentTimeMillis()));
		return compra;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	private boolean jaCarregado = false;
	
	public void wire() {
		
		if(jaCarregado){
			return;
		}
		
		Compra compra = getInstance();
		
		if(compra.getCodCompra()!= null){
			listarMarcas();
		}
		
		if(compra.getLoteProdutos() != null && compra.getLoteProdutos().size() > 0 ){ 
			calcularSubtotaisProduto(compra.getLoteProdutos());
		}
			
		jaCarregado = true;
		
		if(!compraPodeSerAlterada()){
			addFacesMessage("Esta compra não pode ser alterada. Produtos já vendidos.");
		}
		
	}

	public boolean isWired() {
		if(getInstance().getLoteProdutos().size() > 0 ){
			return true;
		}
		return false;
	}

	public Compra getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<LoteProduto> getLoteProdutos() {
			
		return getInstance() == null ? null : new ArrayList<LoteProduto>(
				getInstance().getLoteProdutos());
	}
	
	public List<Pagamento> getPagamentos() {
		return getInstance() == null ? null : new ArrayList<Pagamento>(
				getInstance().getPagamentos());
	}
	
	public void addLoteProduto(){
		
		LoteProduto loteProduto = getNovoLoteProduto();

		if(loteProduto.getProduto() == null){
			addFacesMessage("Nenhum produto foi selecionado.", "");
			return;
		} else if(loteProduto.getQtdCompra() == 0){
			addFacesMessage("Quantidade invalida.", "");
			return;
		} else if(loteProduto.getPrecoCompraUnidade() == null){
			addFacesMessage("Preco compra invalido.", "");
			return;
		} else if(loteProduto.getPrecoVendaUnidade() == null){
			addFacesMessage("Preco venda invalido.", "");
			return;
		} else if(percentLucro < 0){ 
			addFacesMessage("Lucro invalido.", "");
			return;
		} 				
		
		loteProduto.setCompra(getDefinedInstance());
		loteProduto = new LoteProduto(loteProduto);
		getInstance().getLoteProdutos().add(loteProduto);
		calcularSubtotaisProduto(getInstance().getLoteProdutos());
		//setNovoLoteProduto(new LoteProduto(1, 0, 0f));
		atualizarPrecoNovoLote();
		calcularLucro();
		
	}
	
	/**
	 * Executado a partir do ModalPanel de editar localizador
	 * na tela CompraEdit. Atualizar localizar de um produto.
	 * @param produto
	 */
	public void persistProduto(){

		Produto produto = getNovoLoteProduto().getProduto();
		produtoHome.setInstance(produto);
		addFacesMessage("Produto atualizado com sucesso.", FacesMessage.SEVERITY_INFO);
		produtoHome.update();
	}
	
	public void calcularPrecoVenda(){
		
		LoteProduto loteProduto = getNovoLoteProduto();
		if( loteProduto.getPrecoCompraUnidade() == null || loteProduto.getPrecoCompraUnidade() == 0 ){
			loteProduto.setPrecoCompraUnidade(0.0f);
			if( getPercentLucro() == null ||  getPercentLucro() == 0){
				setPercentLucro(0.0f);
			}
			return;
		}
		if( getPercentLucro() == null ||  getPercentLucro() == 0){
			setPercentLucro(0.0f);
			return;
		}
		loteProduto.setPrecoVendaUnidade(loteProduto.getPrecoCompraUnidade() + 
				(loteProduto.getPrecoCompraUnidade() * getPercentLucro()/100));
		
	}
	
	/**
	 * Perc. Lucro = (Preco Venda - Preco Compra)* 100 / PrecoCompra
	 */
	public void calcularLucro(){
		LoteProduto loteProduto = getNovoLoteProduto();
		if( loteProduto.getPrecoVendaUnidade() == null || loteProduto.getPrecoVendaUnidade() == 0 ){
			loteProduto.setPrecoVendaUnidade(0.0f);
			return;
		}
		if( loteProduto.getPrecoCompraUnidade() == null || loteProduto.getPrecoCompraUnidade() == 0 ){
			loteProduto.setPrecoCompraUnidade(0.0f);
			return;
		}
		setPercentLucro((loteProduto.getPrecoVendaUnidade() - loteProduto.getPrecoCompraUnidade()) 
				* 100 / loteProduto.getPrecoCompraUnidade());
	}
	
	/**
	 * Remove um lote produto.
	 */
	public void removeLoteProduto(LoteProduto _loteProdutoComprado){

		Compra compra = getInstance();
		
		Set<LoteProduto> loteProdutoList = compra.getLoteProdutos();
		Set<LoteProduto> loteProdutoListAux = new HashSet<LoteProduto>();
				
		if(compraPodeSerAlterada()){
						
			for (LoteProduto loteProduto : loteProdutoList) {
					
				if (_loteProdutoComprado != loteProduto) {
					
					loteProdutoListAux.add(loteProduto);
					
				} else if(_loteProdutoComprado.getCodLoteProduto() != null && _loteProdutoComprado.getCodLoteProduto() > 0){
					
					entityManager.remove(_loteProdutoComprado);
					
					StringBuilder log = new StringBuilder();
					log.append("A compra " + compra.getCodCompra());
					log.append(" teve o lote produto: " + _loteProdutoComprado.getProduto().getNomeProduto());
					log.append(" qtd: " +_loteProdutoComprado.getQtdCompra() + " removido");
											
					Historico historico = new Historico(user, log.toString(), new Date(System.currentTimeMillis()));
					entityManager.persist(historico);
				}
			}
			compra.setLoteProdutos(loteProdutoListAux);
			calcularSubtotaisProduto(loteProdutoListAux);		
		}
	}
	
	@Override
	public String remove() {
		
		Compra compra = getInstance();
		
		Set<LoteProduto> loteProdutoList = compra.getLoteProdutos();
		
		for (LoteProduto loteProduto : loteProdutoList) {
			if(loteProduto.getProdutoVendidos() != null && loteProduto.getProdutoVendidos().size() > 0){
				
				addFacesMessage("Esta compra não pode ser excluída. O lote produto " + loteProduto.getProduto().getCodProdutoExterno() 
						+ " - " + loteProduto.getProduto().getNomeProduto() 
						+ " já foi vendido.");
				return null;
			}
		}
		
		for (LoteProduto loteProduto : loteProdutoList) {
			entityManager.remove(loteProduto);
		}
		
		String log = "A compra " + compra.getCodCompra() + " foi removida.";
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		removePagamentos();
	
		return super.remove();
	}
	
	/**
	 * Lista as marcas para preencher o select de acordo com o 
	 * fornecedor selecionado.
	 * Se mudar de fornecedor, zera os produtos adicionados.
	 */
	public void listarMarcas(){
		
		getInstance().getLoteProdutos().clear();
		
		String[] RESTRICTIONS = {
				"marca.fornecedor.codFornecedor = #{compraHome.instance.vendedor.fornecedor.codFornecedor}"};
		marcaList.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

		List<Marca> marcaListAux = marcaList.getResultList();
		
		if(marcaListAux.size() > 0 && getInstance().getVendedor() != null){
			setMarcas(marcaListAux);
			setMarcaSelecionada(marcaListAux.get(0));
			listarProdutos();
		} else {
			addFacesMessage("Marcas nao encontradas pra este FORNECEDOR.", "");
			setMarcas(new ArrayList<Marca>());
			setProdutos(new ArrayList<Produto>());
			novoLoteProduto = new LoteProduto(1, 0, 100f);
		}
		
	}
	
	/**
	 * Lista os produtos de um fornecedor.
	 */
	public void listarProdutosPorFornecedor(){
		
		Compra compra = getInstance();
		String[] RESTRICTIONS = {
				//"produto.marca.codMarca = #{compraHome.marcaSelecionada.codMarca}",
				"marca.fornecedor.codFornecedor = #{compraHome.instance.vendedor.fornecedor.codFornecedor}",
				"produto.inExclusao = #{produtoList.produto.inExclusao}"
				};
		produtoList.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		produtoList.setOrder("produto.nomeProduto");
		
		List<Produto> produtoListAux = produtoList.getResultList();
		Set<LoteProduto> loteProdutoListAux = new HashSet<LoteProduto>(); 

		//seta a lista de produtos para ser utilizado no suggestionBox
		setProdutos(produtoListAux);
		
		if(produtoListAux.size() > 0){
			for(Produto produto : produtoListAux){
				
				LoteProduto loteProduto = new LoteProduto(produto, compra, 0,
							produto.getPrecoCompraDefault(), produto.getPrecoVendaDefault(), 0);
				
				loteProduto.setLocalizador(produto.getLocalizadorDefault());
				loteProdutoListAux.add(loteProduto);
			}
		} else {
			addFacesMessage("Produtos nao encontrados pra este Fornecedor.", "");
			setProdutos(new ArrayList<Produto>());
		}
		compra.setLoteProdutos(loteProdutoListAux);
	}
		
	/**
	 * Lista os produtos para preencher o select de acordo com a 
	 * marca selecionada.
	 */
	public void listarProdutos(){
		
		String[] RESTRICTIONS = {
				"produto.marca.codMarca = #{compraHome.marcaSelecionada.codMarca}",
				"produto.inExclusao = #{produtoList.produto.inExclusao}"
				};
		produtoList.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

		List<Produto> produtoListAux = produtoList.getResultList();
		
		if(produtoListAux.size() > 0 && getMarcaSelecionada() != null){
		
			setProdutos(produtoListAux);
			
			Produto p = produtoListAux.get(0);
			getNovoLoteProduto().setProduto(p);
			getNovoLoteProduto().setPrecoCompraUnidade(p.getPrecoCompraDefault());
			getNovoLoteProduto().setPrecoVendaUnidade(p.getPrecoVendaDefault());
			getNovoLoteProduto().getProduto().setLocalizadorDefault(p.getLocalizadorDefault());
			getNovoLoteProduto().setQtdCompra(1);
			
			if(p.getPrecoCompraDefault() > 0 && p.getPrecoVendaDefault() > 0){
				calcularLucro();
			}
			
		} else {
			addFacesMessage("Produtos nao encontrados pra esta MARCA.", "");
			setProdutos(new ArrayList<Produto>());
		}
	}
	
	public void atualizarPrecoNovoLote(){
		LoteProduto lote = getNovoLoteProduto();
		lote.setPrecoCompraUnidade(getNovoLoteProduto().getProduto().getPrecoCompraDefault());
		lote.setPrecoVendaUnidade(getNovoLoteProduto().getProduto().getPrecoVendaDefault());
		lote.setQtdCompra(1);
		
		if(lote.getPrecoCompraUnidade() > 0 && lote.getPrecoVendaUnidade() > 0){
			calcularLucro();
		}
	}

	private void calcularSubtotaisProduto(Set<LoteProduto> loteProdutoList) {
		
		int qtdTotalProdutoAux = 0;
		Float lucroTotalCompraAux= new Float(0.0f);
		Float valorPrecoVendaTotalAux= new Float(0.0f);
		Float valorTotalCompraAux= new Float(0.0f);
		
		for (LoteProduto loteProduto : loteProdutoList) {
			qtdTotalProdutoAux += loteProduto.getQtdCompra();
			valorPrecoVendaTotalAux += loteProduto.getQtdCompra() * loteProduto.getPrecoVendaUnidade();
			lucroTotalCompraAux += loteProduto.getQtdCompra() * (loteProduto.getPrecoVendaUnidade() - loteProduto.getPrecoCompraUnidade());
			valorTotalCompraAux += loteProduto.getQtdCompra() * loteProduto.getPrecoCompraUnidade();
		}
		
		Compra compra = getInstance();
		
		compra.setQtdTotalProduto(qtdTotalProdutoAux);
		compra.setLucroTotalCompra(lucroTotalCompraAux);
		compra.setValorPrecoVendaTota(valorPrecoVendaTotalAux);		
		compra.setPrecoTotal(valorTotalCompraAux);
	}
	

	public void setCodStatusCompra(Integer codStatusCompra){
		Compra compra = getInstance();
		compra.setCodStatusCompra(codStatusCompra);
	}
	
	@Override
	public String persist(){
		
		Compra compra = getInstance();
		
		compra.setUsuario(user);
		//compra.setPrecoTotal(getValorTotalCompra());
		String retorno = super.persist();
		
		//Salva os lotes de uma compra
		persistLoteProdutos(compra.getLoteProdutos());
		
		//Salva os pagamentos de uma compra
		if(compra.getCodStatusCompra().intValue() != Constantes.COMPRA_EM_ANDAMENTO.intValue()){
			gravarPagamentos(compra);
		}

		return retorno;
	}
	

	@Override
	public String update() {
		
		if(!possuiAcesso(transacoes.getTransacaoCompraEditar())){
			return null;
		}
		
		Compra compra = getInstance();

		if(!compraPodeSerAlterada()){
			 return null;
		}
			
		Set<LoteProduto> loteProdutosAux = compra.getLoteProdutos();
		
		if(loteProdutosAux.size() > 0 ){
			
			//Se nenhum lote foi vendido, remove todos os lotes que existiam 
			//para incluí-los novamente pois nao sabemos quais foram alterados.
			removeLotesProdutos();
			persistLoteProdutos(loteProdutosAux);
			
		}
		
		calcularSubtotaisProduto(compra.getLoteProdutos());
		
		//compra.setPrecoTotal(getValorTotalCompra());
		
		//persistLoteProdutos(loteProdutosAux);
		
		gravaLog(compra);
		
		return super.update();
		
	}
	
	/**
	 * Persist os lotesProdutos de uma compra. Inserção e Edição
	 * 
	 * @param compra
	 */	
	public void persistLoteProdutos(Set<LoteProduto> lotesProdutos){
		
		Compra compra = getInstance();
		
		Set<LoteProduto> lotesProdutosAux = new HashSet<LoteProduto>(0);
		
		for (LoteProduto loteProduto : lotesProdutos) {				
			loteProduto = new LoteProduto(loteProduto);
			loteProduto.setCompra(compra);
			
			if(compra.getCodStatusCompra() == 0){
				loteProduto.setQtdEstoque(0);
			} else {					
				loteProduto.setQtdEstoque(loteProduto.getQtdCompra());
			}

			entityManager.persist(loteProduto);	
			lotesProdutosAux.add(loteProduto);
		}
		compra.setLoteProdutos(lotesProdutosAux);
	}
	
	/**
	 * Verifica se nenhum loteProduto foi vendido, senão foi, a compra
	 * pode ser alterada ainda.
	 * @return
	 */
	public boolean compraPodeSerAlterada(){
		
		Compra compra = getInstance();
		
		//utilizado para resolver erro de concorrencia ConcurrentModificationException.
		CopyOnWriteArraySet<LoteProduto> loteProdutoList = new CopyOnWriteArraySet<LoteProduto>(compra.getLoteProdutos());

		//No caso de edicao, verifica se algum lote jah foi vendido, se foi, a compra nao pode ser alterada.
		for (LoteProduto loteProduto : loteProdutoList) {
			if(loteProduto.getCodLoteProduto() != null && loteProduto.getProdutoVendidos().size() > 0 ){								
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Remove a lista de lotesProdutos.
	 */
	public void removeLotesProdutos() {
		Set<LoteProduto> loteProdutoList = getInstance().getLoteProdutos();
		
		for (LoteProduto loteProdutoAux : loteProdutoList) {
				loteProdutoHome.setInstance(loteProdutoAux);
				loteProdutoHome.remove();
		}
		getInstance().setLoteProdutos(null);
	}
	
	/**
	 * Salva os pagamentos de uma compra de acordo com o valor e tipo de pagamento.
	 * @param venda
	 */
	private void gravarPagamentos(Compra compra){
		
		TipoPagamento tipoPagamento = compra.getTipoPagamento();
		Set<Pagamento> pagamentoList = new TreeSet<Pagamento>();
		TipoDebito tipoDebito = entityManager.find(TipoDebito.class, 2);//compras diversas
				
		//calcular datas dos pagamentos
		List<Date> datasPagamentos = CalculosUtil.calcularDatasPagamento(tipoPagamento, new Date(System.currentTimeMillis()));
		
		//calcular valores dos pagamentos
		List<Float> valoresRecebimentos  = CalculosUtil.calcularParcelas(tipoPagamento, compra.getPrecoTotal());
		
		Fornecedor fornecedor = compra.getVendedor().getFornecedor();
		
		if(tipoPagamento.getNumVezes() == 0 || tipoPagamento.getNumVezes() == 1){
			
			Pagamento pagamento = new Pagamento();
			pagamento.setCompra(compra);
			pagamento.setDataVencimento(datasPagamentos.get(0));
			pagamento.setValor(valoresRecebimentos.get(0));
			pagamento.setTipoDebito(tipoDebito);//compras diversas
			pagamento.setFornecedor(fornecedor);
			pagamento.setUsuarioCadastramento(user);
			pagamento.setDataCadastramento( new Date(System.currentTimeMillis()));
			pagamento.setObservacao("Cadastro automático.");
			
			pagamentoList.add(pagamento);
			entityManager.persist(pagamento);
			
		} else {
			//Quando são vários pagamentos
			for(int i = 0; i < tipoPagamento.getNumVezes(); i++){
				
				Pagamento pagamento = new Pagamento();
				pagamento.setCompra(compra);
				pagamento.setDataVencimento(datasPagamentos.get(i));
				pagamento.setValor(valoresRecebimentos.get(i));
				pagamento.setTipoDebito(tipoDebito);
				pagamento.setFornecedor(fornecedor);
				pagamento.setUsuarioCadastramento(user);
				pagamento.setDataCadastramento( new Date(System.currentTimeMillis()));
				pagamento.setObservacao("Cadastro automático.");
				
				entityManager.persist(pagamento);
				pagamentoList.add(pagamento);
			}
		}
		compra.setPagamentos(pagamentoList);
	}
	
	
	/**
	 * Remove a lista de pagamentos.
	 */
	public void removePagamentos() {
		Set<Pagamento> pagamentoList = getInstance().getPagamentos();
		
		for (Pagamento pagamentoAux : pagamentoList) {
			pagamentoHome.setInstance(pagamentoAux);
			pagamentoHome.remove();
		}
		getInstance().setPagamentos(null);
	}
	
	
	private boolean possuiAcesso(String transacao){
		
		if(!identity.hasRole(transacao)){
			addFacesMessage("Usuário não possui acesso à transação: " + transacao, "");
			return false;
		}
		return true;
	}
	
	private void gravaLog(Compra compra){
		
		String log = "A compra " + compra.getCodCompra() + " foi alterada.";
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		//Salva os pagamentos de uma compra
		if(compra.getCodStatusCompra() != 0){
			removePagamentos();
			gravarPagamentos(compra);
		}
	}
	
	private List<Produto> produtoSuggestionList = new ArrayList<Produto>();
	

	/**
	 * Cria um TreeMap para ser utilizado pelas ombo Box with dynamic suggestions list. 
	 * @return
	 */
	public List<Produto> getClienteSuggestionList(){					
		return produtoSuggestionList;
	}
	
	public List<Produto> popularProdutoSuggestionList(){
		
		produtoSuggestionList = new ArrayList<Produto>();
		produtoSuggestionList = produtoList.getResultList();
					
		return produtoSuggestionList;
	}	

	public LoteProduto getNovoLoteProduto() {
		return novoLoteProduto;
	}

	public void setNovoLoteProduto(LoteProduto novoLoteProduto) {
		this.novoLoteProduto = novoLoteProduto;
	}

	public Float getPercentLucro() {
		return percentLucro;
	}

	public void setPercentLucro(Float percentLucro) {
		this.percentLucro = percentLucro;
	}

	public String getLoteSelecionado() {
		return loteSelecionado;
	}

	public void setCodProdutoSelecionado(String loteSelecionado) {
		this.loteSelecionado = loteSelecionado;
	}

	public List<Marca> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<Marca> marcas) {
		this.marcas = marcas;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Marca getMarcaSelecionada() {
		return marcaSelecionada;
	}

	public void setMarcaSelecionada(Marca marcaSelecionada) {
		this.marcaSelecionada = marcaSelecionada;
	}

	public void setLoteSelecionado(String loteSelecionado) {
		this.loteSelecionado = loteSelecionado;
	}

	public Produto getProdutoSelecionado()
	{
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado)
	{
		this.produtoSelecionado = produtoSelecionado;
	}

	public String getProdutoLocalizadorDefault() {
		return produtoLocalizadorDefault;
	}

	public void setProdutoLocalizadorDefault(String produtoLocalizadorDefault) {
		this.produtoLocalizadorDefault = produtoLocalizadorDefault;
	}
}
