package br.com.gerenciadornet.session;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.RelatorioDetalhadoFuncionarioDTO;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Pagamento;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;

@Scope(ScopeType.CONVERSATION)
@Name("relatorioDetalhadoFuncionarioList")
public class RelatorioDetalhadoFuncionarioList extends EntityQuery<Venda> {

	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select new br.com.gerenciadornet.entity.Venda(venda.codVenda, tp.descTipoPagamento, c.nome, "
			+ "u.nomeUsuario, uc.nomeUsuario, venda.dataInicioVenda, venda.dataFimVenda, sv.descStatusVenda,"
			+ "venda.valorTotalVenda, venda.lucroTotalVenda ) "
			+ "from Venda venda " 
			+ "left join venda.cliente c "
			+ "left join venda.usuario u "
			+ "left join venda.usuarioCadastro uc "
			+ "left join venda.tipoPagamento tp "
			+ "left join venda.statusVenda sv";
	
	private static String[] RESTRICTIONS = {
			"venda.cliente.codCliente = #{relatorioDetalhadoFuncionarioList.clienteFuncionario}",
			"venda.dataInicioVenda >= #{relatorioDetalhadoFuncionarioList.dataInicioPesquisa}",
			"venda.dataInicioVenda <= #{relatorioDetalhadoFuncionarioList.dataFimPesquisa}",
			"venda.statusVenda.codStatusVenda <> #{relatorioDetalhadoFuncionarioList.statusVendaCancela}",
			"venda.statusVenda.codStatusVenda <> #{relatorioDetalhadoFuncionarioList.statusVendaOrcamento}",
			"venda.statusVenda.codStatusVenda <> #{relatorioDetalhadoFuncionarioList.statusVendaPedido}",
			"venda.statusVenda.codStatusVenda <> #{relatorioDetalhadoFuncionarioList.statusVendaPedidoAutorizado}"
			};
			
	
	@In(scope = ScopeType.SESSION)
	Usuario user;
	
	@In
	Constantes constantes;
	
	private Usuario 	funcionario					= new Usuario();
	private Fornecedor 	fornecedor					= new Fornecedor();
	private Integer 	clienteFuncionario;
	private Date 		dataInicioPesquisa 			= new Date(System.currentTimeMillis());
	private Date 		dataFimPesquisa 			= new Date(System.currentTimeMillis());
	private boolean 	mostrarResultados			= false;
	
	private Float 		valorTotalComissaoProdutosVendidosDefault	= new Float(0);
	private Float 		valorTotalComissaoProdutosVendidosEfetivo	= new Float(0);
	private Float 		valorTotalLucroProdutosVendidos				= new Float(0);
	
	private Float 		valorTotalComissaoServicosVendidosDefault	= new Float(0);
	private Float 		valorTotalComissaoServicosVendidosEfetivo	= new Float(0);
	private Float 		valorTotalLucroServicosVendidos				= new Float(0);
	private Float 		valorTotalConsumoFuncionario				= new Float(0);
	
	
	private Float 		valorTotalServicosVendidos	= new Float(0);
	private Integer 	numeroTotalProdutosVendidos = 0;
	private Integer 	numeroTotalServicosVendidos = 0;
	
	private String 		nomeFuncionario;
	
	private boolean 	resultadoPesquisaVendas = false;
	
	private boolean 	executarConsulta = false;
	
	private Integer statusVendaCancela 		= Constantes.CANCELADA;
	private Integer statusVendaOrcamento	= Constantes.ORCAMENTO;
	private Integer statusVendaPedido		= Constantes.PEDIDO;
	private Integer statusVendaPedidoAutorizado	= Constantes.PEDIDO_AUTORIZADO;
	
	
	List<RelatorioDetalhadoFuncionarioDTO> listaProdutosVendidos;
	List<RelatorioDetalhadoFuncionarioDTO> listaServicosVendidos;
	List<Venda> 						   listaVendasConsumo;
	
	Integer qtdResultados = 0;
	Integer qtdTotal = 0;
	
	@In
	EntityManager entityManager;

	@In(create = true)
	ProdutoList produtoList;

	public RelatorioDetalhadoFuncionarioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	
	public void executarConsultas(){
		
		if (user.getPerfil().getCodPerfil().intValue() == constantes.getVendedor().intValue()) {
			setFuncionario(user);
		}
		
		listarProdutosVendidos();
		listarServicoVendidos();
		listarVendasConsumo();
		listarPagamentosFuncionario();
		
	}

	/**
	 * Lista todos os produtos vendidos por um usuário
	 * em um determinado período.
	 */
	@SuppressWarnings("unchecked")
	public void listarProdutosVendidos() {
				
		boolean indicadorOrcamento = false;
		float vlComissaoExcluida = 0;
		qtdTotal = 0;
		valorTotalComissaoProdutosVendidosDefault = 0f;
		valorTotalComissaoProdutosVendidosEfetivo = 0f;
		numeroTotalProdutosVendidos = 0;
		valorTotalLucroProdutosVendidos = 0f;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.gerenciadornet.dto.RelatorioDetalhadoFuncionarioDTO(");
		sql.append("v.codVenda, v.dataInicioVenda, usu.nomeUsuario,");	
		sql.append("cli.codCliente, cli.nome, pv.precoVenda * (100 - v.descontoTotal)/100, pv.qtd, pv.desconto, ");//Preço de venda menos desconto 
		sql.append("lp.precoCompraUnidade, lp.precoVendaUnidade, ");
		
		sql.append("marca.nomeMarca,");
		sql.append("prod.nomeProduto, ");
		sql.append("prod.valorComissao * pv.qtd, ");//comissaoDefault
		//Cálculo da comissão:  Valor da comissão x quantidade + ( preço de venda - % de desconto dado - valor da unidade / 2 , pois a e for mais ou menos racha no meio
		sql.append("(prod.valorComissao * pv.qtd + ((pv.precoVenda * (100 - v.descontoTotal)/100 ) - lp.precoVendaUnidade)/2 * pv.qtd), ");//comissao efetiva
		sql.append("v.valorTotalVenda, v.descontoTotal, ");
		sql.append("(lp.precoVendaUnidade *  pv.qtd) - (lp.precoCompraUnidade * pv.qtd) - ( prod.valorComissao * pv.qtd ) ) ");

		sql.append("from Venda as v ");
		
		sql.append("inner join v.usuario as usu ");
		sql.append("inner join v.cliente as cli ");
		sql.append("inner join v.produtoVendidos as pv ");
		sql.append("inner join pv.loteProduto as lp ");
		sql.append("inner join lp.produto as prod ");
		sql.append("inner join prod.marca as marca ");
		
		sql.append("where v.statusVenda.codStatusVenda not in( "
			+ Constantes.CANCELADA + ", " 
			+ Constantes.ORCAMENTO + ", " 
			+ Constantes.PEDIDO + "," 
			+ Constantes.PEDIDO_AUTORIZADO + ") " );
		
		sql.append("and prod.valorComissao > :vlComissaoExcluida ");//não mostrar produtos que tem comissão = 0
		sql.append("and v.inOrcamento = :indicadorOrcamento ");
		sql.append("and v.dataInicioVenda >= :dataInicio ");
		sql.append("and v.dataInicioVenda <= :dataFim ");
		sql.append("and v.usuario.codUsuario = :codUsuario ");
		sql.append("order by v.codVenda ");
		 
		listaProdutosVendidos = (List<RelatorioDetalhadoFuncionarioDTO>)entityManager
				.createQuery(sql.toString())
				.setParameter("vlComissaoExcluida", vlComissaoExcluida)
				.setParameter("indicadorOrcamento", indicadorOrcamento)
				.setParameter("dataInicio", getDataInicioPesquisa())
				.setParameter("dataFim", getDataFimPesquisa())// testar se pega o dia por causa da hora
				.setParameter("codUsuario", funcionario.getCodUsuario())
				.getResultList();
		
		for (RelatorioDetalhadoFuncionarioDTO produtosVendidos : listaProdutosVendidos) {
			
			numeroTotalProdutosVendidos += produtosVendidos.getQuantidade();
			valorTotalLucroProdutosVendidos += produtosVendidos.getLucro();
			valorTotalComissaoProdutosVendidosDefault += produtosVendidos.getComissaoDefault();
			valorTotalComissaoProdutosVendidosEfetivo += produtosVendidos.getComissaoEfetiva();
			
			nomeFuncionario = produtosVendidos.getNomeVendedor();
			if(produtosVendidos.getComissaoDefault().floatValue() != produtosVendidos.getComissaoEfetiva().floatValue()){
				produtosVendidos.setAlteracaoComissao(true);
			}
		}
		
		qtdResultados = listaProdutosVendidos.size();
		
		//return listaProdutosVendidos;
	}
	
	/**
	 * Lista todos os serviço vendidos por um usuário
	 * em um determinado período.
	 */
	@SuppressWarnings("unchecked")
	public void listarServicoVendidos() {
		
		boolean indicadorOrcamento = false;
		qtdTotal = 0;
		valorTotalComissaoServicosVendidosDefault	= 0f;
		valorTotalComissaoServicosVendidosEfetivo	= 0f;
		valorTotalLucroServicosVendidos				= 0f;
		
		numeroTotalServicosVendidos = 0;
		valorTotalLucroServicosVendidos = 0f;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.gerenciadornet.dto.RelatorioDetalhadoFuncionarioDTO(");
		sql.append("v.codVenda, v.dataInicioVenda, usu.nomeUsuario,");	
		sql.append("cli.codCliente, cli.nome,  ");
		sql.append("s.descServico, s.precoCusto, s.precoServico, sr.preco, ");
		
		//comissao
		//sql.append("(s.precoCusto + (sr.preco - s.precoServico)/2) * (100 - v.descontoTotal)/100, ");
		sql.append("s.precoCusto + ((sr.preco * (100 - v.descontoTotal)/100) - s.precoServico )/2, ");
		
		//lucro = Preço Realizado do serviço - comissão - desconto
		//sql.append("sr.preco - ((s.precoCusto + (sr.preco - s.precoServico)/2) * (100 - v.descontoTotal)/100) * (100 - v.descontoTotal)/100 , ");
		//lucro = valor cobrado do serviço realizado com desconto total - comissão
		sql.append("(sr.preco  * (100 - v.descontoTotal)/100)  - (s.precoCusto + ((sr.preco * (100 - v.descontoTotal)/100) - s.precoServico )/2), ");
		
		sql.append("v.valorTotalVenda, v.descontoTotal) ");

		sql.append("from Venda as v ");
		sql.append("inner join v.usuario as usu ");
		sql.append("inner join v.cliente as cli ");
		sql.append("inner join v.servicoRealizados as sr ");
		sql.append("inner join sr.servico as s ");
		
		sql.append("where v.statusVenda.codStatusVenda not in( "
			+ Constantes.CANCELADA + ", " 
			+ Constantes.ORCAMENTO + ", " 
			+ Constantes.PEDIDO + "," 
			+ Constantes.PEDIDO_AUTORIZADO + ") " );
		
		sql.append("and v.inOrcamento = :indicadorOrcamento ");
		sql.append("and v.dataInicioVenda >= :dataInicio ");
		sql.append("and v.dataInicioVenda <= :dataFim ");
		sql.append("and v.usuario.codUsuario = :codUsuario ");
		sql.append("order by v.codVenda ");
		
		
		listaServicosVendidos = (List<RelatorioDetalhadoFuncionarioDTO>)entityManager
				.createQuery(sql.toString())
				.setParameter("indicadorOrcamento", indicadorOrcamento)
				.setParameter("dataInicio", getDataInicioPesquisa())
				.setParameter("dataFim", getDataFimPesquisa())// testar se pega o dia por causa da hora
				.setParameter("codUsuario", funcionario.getCodUsuario())
				.getResultList();
		
		for (RelatorioDetalhadoFuncionarioDTO servicosVendidos : listaServicosVendidos) {
			
			valorTotalLucroServicosVendidos += servicosVendidos.getLucro();
			valorTotalComissaoServicosVendidosDefault += servicosVendidos.getComissaoDefault();
			valorTotalComissaoServicosVendidosEfetivo += servicosVendidos.getComissaoEfetiva();
			
			if(servicosVendidos.getComissaoDefault().floatValue() != servicosVendidos.getComissaoEfetiva().floatValue()){
				servicosVendidos.setAlteracaoComissao(true);
			}
		}
		
		numeroTotalServicosVendidos = listaServicosVendidos.size();
		
	}
	
	/**
	 *Lista as vendas aonde o funcionário está como cliente
	 *no período informado. 
	 * @return
	 */
	public void listarVendasConsumo() {
		
		super.setEntityManager(entityManager);
		
		listaVendasConsumo = super.getResultList();

		//Totalizar as vendas pesquisadas
		valorTotalConsumoFuncionario = new Float(0);
		
		for (Venda venda : listaVendasConsumo) {	
			setResultadoPesquisaVendas(true);
			valorTotalConsumoFuncionario += venda.getValorTotalVenda();
		}
		
	}
	
	@In(create=true)
	private PagamentoList pagamentoList;
	List<Pagamento> pagamentosList;
	
	/**
	 * Lista os pagamentos feitos a um funcionário durante 
	 * o período informado.
	 */
	public void listarPagamentosFuncionario(){
		
		Pagamento pagamento = new Pagamento();
		pagamento.setFornecedor(fornecedor);
				
		pagamentoList.setPagamento(pagamento);
		pagamentoList.setDataInicio(getDataInicioPesquisa());
		pagamentoList.setDataFim(getDataFimPesquisa());
		pagamentoList.setInConferencia(2);
		pagamentoList.setTipoData(1);
		 
		pagamentoList.listarPagamentos();
		pagamentosList = pagamentoList.getPagamentosList();
						
	}
	
	public Date getDataInicioPesquisa() {
		if (dataInicioPesquisa != null) {
			return new Date(dataInicioPesquisa.getTime());
		}
		return dataInicioPesquisa;
	}

	/**
	 * adiciona um dia na pesquisa final para a data fim ser incluida na
	 * pesquisa
	 */
	public Date getDataFimPesquisa() {
		if (dataFimPesquisa != null) {

			return new Date(dataFimPesquisa.getTime() + 86400000l);
		}
		return dataFimPesquisa;
	}


	public void setDataInicioPesquisa(Date dataInicioPesquisa) {
		this.dataInicioPesquisa = dataInicioPesquisa;
	}


	public void setDataFimPesquisa(Date dataFimPesquisa) {
		this.dataFimPesquisa = dataFimPesquisa;
	}


	public boolean isMostrarResultados() {
		return mostrarResultados;
	}


	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}


	public Usuario getFuncionario() {
		return funcionario;
	}


	public void setFuncionario(Usuario funcionario) {
		this.funcionario = funcionario;
	}


	public List<RelatorioDetalhadoFuncionarioDTO> getListaProdutosVendidos() {
		return listaProdutosVendidos;
	}


	public void setListaProdutosVendidos(List<RelatorioDetalhadoFuncionarioDTO> listaProdutosVendidos) {
		this.listaProdutosVendidos = listaProdutosVendidos;
	}


	public List<RelatorioDetalhadoFuncionarioDTO> getListaServicosVendidos() {
		return listaServicosVendidos;
	}


	public void setListaServicosVendidos(List<RelatorioDetalhadoFuncionarioDTO> listaServicosVendidos) {
		this.listaServicosVendidos = listaServicosVendidos;
	}


	public Integer getQtdResultados() {
		return qtdResultados;
	}


	public void setQtdResultados(Integer qtdResultados) {
		this.qtdResultados = qtdResultados;
	}


	public Integer getQtdTotal() {
		return qtdTotal;
	}


	public void setQtdTotal(Integer qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

	public Float getValorTotalServicosVendidos() {
		return valorTotalServicosVendidos;
	}


	public void setValorTotalServicosVendidos(Float valorTotalServicosVendidos) {
		this.valorTotalServicosVendidos = valorTotalServicosVendidos;
	}


	public Integer getnumeroTotalProdutosVendidos() {
		return numeroTotalProdutosVendidos;
	}


	public void setnumeroTotalProdutosVendidos(Integer numeroTotalProdutosVendidos) {
		this.numeroTotalProdutosVendidos = numeroTotalProdutosVendidos;
	}


	public Float getValorTotalComissaoProdutosVendidosDefault() {
		return valorTotalComissaoProdutosVendidosDefault;
	}


	public void setValorTotalComissaoProdutosVendidosDefault(Float valorTotalComissaoProdutosVendidosDefault) {
		this.valorTotalComissaoProdutosVendidosDefault = valorTotalComissaoProdutosVendidosDefault;
	}


	public Float getValorTotalComissaoProdutosVendidosEfetivo() {
		return valorTotalComissaoProdutosVendidosEfetivo;
	}


	public void setValorTotalComissaoProdutosVendidosEfetivo(Float valorTotalComissaoProdutosVendidosEfetivo) {
		this.valorTotalComissaoProdutosVendidosEfetivo = valorTotalComissaoProdutosVendidosEfetivo;
	}


	public Float getValorTotalLucroProdutosVendidos() {
		return valorTotalLucroProdutosVendidos;
	}


	public void setValorTotalLucroProdutosVendidos(Float valorTotalLucroProdutosVendidos) {
		this.valorTotalLucroProdutosVendidos = valorTotalLucroProdutosVendidos;
	}


	public Float getValorTotalComissaoServicosVendidosDefault() {
		return valorTotalComissaoServicosVendidosDefault;
	}


	public void setValorTotalComissaoServicosVendidosDefault(Float valorTotalComissaoServicosVendidosDefault) {
		this.valorTotalComissaoServicosVendidosDefault = valorTotalComissaoServicosVendidosDefault;
	}


	public Float getValorTotalComissaoServicosVendidosEfetivo() {
		return valorTotalComissaoServicosVendidosEfetivo;
	}


	public void setValorTotalComissaoServicosVendidosEfetivo(Float valorTotalComissaoServicosVendidosEfetivo) {
		this.valorTotalComissaoServicosVendidosEfetivo = valorTotalComissaoServicosVendidosEfetivo;
	}


	public Float getValorTotalLucroServicosVendidos() {
		return valorTotalLucroServicosVendidos;
	}


	public void setValorTotalLucroServicosVendidos(Float valorTotalLucroServicosVendidos) {
		this.valorTotalLucroServicosVendidos = valorTotalLucroServicosVendidos;
	}


	public Integer getNumeroTotalServicosVendidos() {
		return numeroTotalServicosVendidos;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public Float getValorTotalConsumoFuncionario() {
		return valorTotalConsumoFuncionario;
	}

	public void setValorTotalConsumoFuncionario(Float valorTotalConsumoFuncionario) {
		this.valorTotalConsumoFuncionario = valorTotalConsumoFuncionario;
	}

	public boolean isResultadoPesquisaVendas() {
		return resultadoPesquisaVendas;
	}

	public void setResultadoPesquisaVendas(boolean resultadoPesquisaVendas) {
		this.resultadoPesquisaVendas = resultadoPesquisaVendas;
	}

	public Integer getClienteFuncionario() {
		return clienteFuncionario;
	}

	public void setClienteFuncionario(Integer clienteFuncionario) {
		this.clienteFuncionario = clienteFuncionario;
	}


	public boolean isExecutarConsulta() {
		return executarConsulta;
	}


	public void setExecutarConsulta(boolean executarConsulta) {
		this.executarConsulta = executarConsulta;
	}


	public List<Venda> getListaVendasConsumo() {
		return listaVendasConsumo;
	}


	public void setListaVendasConsumo(List<Venda> listaVendasConsumo) {
		this.listaVendasConsumo = listaVendasConsumo;
	}


	public Integer getStatusVendaCancela() {
		return statusVendaCancela;
	}


	public void setStatusVendaCancela(Integer statusVendaCancela) {
		this.statusVendaCancela = statusVendaCancela;
	}


	public Integer getStatusVendaOrcamento() {
		return statusVendaOrcamento;
	}


	public void setStatusVendaOrcamento(Integer statusVendaOrcamento) {
		this.statusVendaOrcamento = statusVendaOrcamento;
	}


	public Integer getStatusVendaPedido() {
		return statusVendaPedido;
	}


	public void setStatusVendaPedido(Integer statusVendaPedido) {
		this.statusVendaPedido = statusVendaPedido;
	}


	public Integer getStatusVendaPedidoAutorizado() {
		return statusVendaPedidoAutorizado;
	}


	public void setStatusVendaPedidoAutorizado(Integer statusVendaPedidoAutorizado) {
		this.statusVendaPedidoAutorizado = statusVendaPedidoAutorizado;
	}


	public Fornecedor getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}


	public List<Pagamento> getPagamentosList() {
		return pagamentosList;
	}


	public void setPagamentosList(List<Pagamento> pagamentosList) {
		this.pagamentosList = pagamentosList;
	}




}
