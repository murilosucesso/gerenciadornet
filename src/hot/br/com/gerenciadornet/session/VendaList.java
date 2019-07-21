package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("vendaList")
public class VendaList extends EntityQuery<Venda> {

	private static final String EJBQL = "select venda from Venda venda"
			+ " left join fetch venda.tipoPagamento"
			+ " left join fetch venda.statusVenda"
			+ " left join fetch venda.usuario"
			+ " left join fetch venda.cliente";

	private static String[] RESTRICTIONS = { "venda.codVenda = #{vendaList.venda.codVenda}", };

	private static String[] RESTRICTIONS2 = {
			"lower(venda.numNotaFiscal) like lower(#{vendaList.venda.numNotaFiscal})",
			"lower(venda.cliente.nome) like lower(concat('%', concat(#{vendaList.venda.cliente.nome},'%')))",
			"venda.cliente.codCliente = #{vendaList.venda.cliente.codCliente}",
			"venda.cliente.usuario.codUsuario = #{vendaList.vendedorResponsavel.codUsuario}",//Vendedor Responsavel
			"venda.usuario.codUsuario = #{vendaList.venda.usuario.codUsuario}",
			"venda.dataInicioVenda >= #{vendaList.dataInicioVenda}",
			"venda.dataInicioVenda <= #{vendaList.dataFimVenda}",
			"venda.inOrcamento = #{vendaList.venda.inOrcamento}",
			"venda.statusVenda.codStatusVenda = #{vendaList.statusVenda}",
			"venda.statusVenda.codStatusVenda = #{vendaList.statusVendaDefault}",
			"venda.usuarioCadastro.codUsuario = #{vendaList.usuarioCadastro.codUsuario}",
			"venda.tipoPagamento.codTipoPagamento = #{vendaList.codTipoPagamento}",//Tipo Pagamento
			"venda.canal = #{vendaList.venda.canal}" };
	
	private static String[] RESTRICTIONS3 = { 
			"venda.statusVenda.codStatusVenda = #{vendaList.statusVenda}",
			"venda.statusVenda.codStatusVenda = #{vendaList.statusVendaConfirmadaFinanceiro}",
			"venda.statusVenda.codStatusVenda = #{vendaList.statusVendaSeparandoMaterial}",
			"venda.statusVenda.codStatusVenda = #{vendaList.statusVendaEmFaseEntrega}",
			"venda.usuario.codUsuario = #{vendaList.venda.usuario.codUsuario}"
	};
	
	/**
	 * mostra como default as vendas de hoje
	 */
	private Venda venda = new Venda(new Date(), new Date(), null);

	private Date dataInicioVenda = new Date();
	private Date dataFimVenda = new Date();
	private Integer statusVenda;
	
	//Utilizado para montar a lista de ultimas vendas para o Estoque
	private Integer statusVendaConfirmadaFinanceiro;
	private Integer statusVendaSeparandoMaterial ;
	private Integer statusVendaEmFaseEntrega;
	//----------------------------------------------------------
	
	private Integer statusVendaCancela = Constantes.CANCELADA;
	private Integer statusVendaDefault;
	private boolean mostrarResultados 	= false;
	private Usuario usuarioCadastro   	= new Usuario();
	private Usuario vendedorResponsavel = new Usuario();
	private Integer codTipoPagamento;
	private Float 	valorTotal 		  	= new Float(0);
	private Integer inOrcamento 		= 2;
	
	public Integer getInOrcamento() {
		return inOrcamento;
	}

	public void setInOrcamento(Integer inOrcamento) {
		this.inOrcamento = inOrcamento;
	}

	@In(scope = ScopeType.SESSION, required = false)
	Usuario user;

	@In
	Constantes constantes;

	public VendaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
	}

	private boolean pesquisaVazia;
	private int qtdResultados = 0;

	/**
	 * Altera a pesquisa caso seja pesq. por códigoVenda, nao pode ficar dentro
	 * do resultList senao recarrega, executa a pesq. varias vezes, sem
	 * necessidade.
	 */
	public void setRestriction() {

		if (this.venda.getCodVenda() == null) {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		} else {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		}
	}

	@Override
	public List<Venda> getResultList() {

		// Caso seja vendedor, so consulta suas proprias vendas.
		if (user.getPerfil().getCodPerfil().intValue() == constantes
				.getVendedor().intValue()) {
			venda.setUsuario(user);
		}
		
		//Logica para setar pesquisa por todos, vendas ou orcamentos
		if (getInOrcamento() == 0) {
			venda.setInOrcamento(false);	
		}else if (getInOrcamento() == 1) {
			venda.setInOrcamento(true);
		}else { 		
			venda.setInOrcamento(null);
		}

		List<Venda> vendas = super.getResultList();

		qtdResultados = vendas.size();

		if (vendas == null || qtdResultados == 0) {
			setPesquisaVazia(true);
		} else {
			setPesquisaVazia(false);
		}	
		
		//Totalizar as vendas pesquisadas
		Float valorTotalAux = new Float(0);
		
		for (Venda venda : vendas) {								
			if(venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.CANCELADA.intValue()){
				valorTotalAux += venda.getValorTotalVenda();
			}
		}
		setValorTotal(valorTotalAux);
		
		return vendas;
	}
	
	//Utilizado para exibir a lista das últimas vendas na tela inicial
	 List<Venda> ultimasVendas = new ArrayList<Venda>();
	 boolean consultaJaRealizada = false; // utilizado para evitar múltiplas chamdas do mesmo método.
	
	 StringBuilder respostaGraficoEstoqueHome = new StringBuilder();
	/**
	 * Método utilizado na página inicial para montar a tabela
	 * das últimas 40 vendas.
	 * @return
	 */
	public  List<Venda> listarUltimasVendas() {
		
		dataInicioVenda = null;
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS3));
		setOrderColumn("venda.codVenda");
		setOrderDirection("desc");
		
		//Vai realizar a consulta das últimas vendas de acordo com o perfil e o status da venda.
		int codPerfil = user.getPerfil().getCodPerfil();
		
		//Se perfil for Financeiro -  Vê todas a vendas com status que depende de seus andamentos
		if(codPerfil == constantes.getFinanceiro()){
			
			setRestrictionLogicOperator("or");
			setStatusVenda(Constantes.ABERTA);
			setStatusVendaEmFaseEntrega(Constantes.EM_FASE_ENTREGA);
			
			//Se perfil for Estoque - Vê todas a vendas com status que depende de seus andamentos
		} else if(codPerfil == constantes.getEstoque()){
			
			setRestrictionLogicOperator("or");
			setStatusVendaConfirmadaFinanceiro(Constantes.CONFIRMADA_FINANCEIRO);
			setStatusVendaSeparandoMaterial(Constantes.SEPARANDO_MATERIAL);
			setStatusVendaEmFaseEntrega(Constantes.EM_FASE_ENTREGA);
			
			//Se perfil for Vendedor - Vê todas as suas últimas vendas
		} else if(codPerfil == constantes.getVendedor()){
			
			venda.getUsuario().setCodUsuario(user.getCodUsuario());
		}
		
		//Trazer todas as vendas para o perfil estoque para utilizar
		//resultado da consulta no gráfico
		if(codPerfil != constantes.getEstoque()){
			setMaxResults(40);//5 Pagina de rolagem na pagina inicial.
		}
		//Se perfil for Admin/Supervisor/Gerente - Vê as últimas vendas global. Não precisa de filtro
		
		
		if(consultaJaRealizada){
			return ultimasVendas;
		}
			
		ultimasVendas = super.getResultList();
		consultaJaRealizada =  true;
				
		
		if (codPerfil == constantes.getEstoque()) {

			int contConfirmadaFinanceiro = 0;
			int contSeparandoMaterial		= 0;
			int contEmFaseEntrega			= 0;
			
			for (Venda venda : ultimasVendas) {

				if (venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.CONFIRMADA_FINANCEIRO.intValue()) {
					
					contConfirmadaFinanceiro++;
					
				} else if (venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.SEPARANDO_MATERIAL.intValue()) {
					
					contSeparandoMaterial++;
					
				} else if (venda.getStatusVenda().getCodStatusVenda().intValue() == Constantes.EM_FASE_ENTREGA.intValue()) {
					contEmFaseEntrega++;
				}
			}
			
			//Monta os dados para serem utilizados no gráfico da tela inicial do estoque
			respostaGraficoEstoqueHome.append("['Label', 'Value'],");
			respostaGraficoEstoqueHome.append("['Liberada', " +  contConfirmadaFinanceiro + "],");
			respostaGraficoEstoqueHome.append("['Separando', " +  contSeparandoMaterial + "],");
			respostaGraficoEstoqueHome.append("['Entregando', " +  contEmFaseEntrega + "]");
		}
		
		return ultimasVendas;
		
	}
	
	/**
	 * Utilizado para montar a tela home do perfil estoque.
	 * Mostra todas as vendas que estão em Edição.
	 * @return
	 */
	public  List<Venda> listarVendasEmAndamento() {
		
		setStatusVenda(Constantes.EM_EDICAO);
		
		return getResultList();
		
	}
	
	/**
	 * Pega a data da venda e adiciona um dia na pesquisa final para a data fim
	 * ser incluida na pesquisa. Por default mostra as vendas da abertas da
	 * ultima semana.
	 * 
	 * @return
	 */
	public Date getDataFimVenda() {
		if (venda.getDataFimVenda() != null) {
			return new Date(venda.getDataFimVenda().getTime() + 86400000l);
		}
		return dataFimVenda;
	}

	public Date getDataInicioVenda() {
		if (venda.getDataInicioVenda() != null) {
			return new Date(venda.getDataInicioVenda().getTime());
		}
		return dataInicioVenda;
	}

	public Venda getVenda() {
		return venda;
	}

	public Integer getStatusVendaCancela() {
		return statusVendaCancela;
	}

	public Integer getStatusVendaDefault() {
		return statusVendaDefault;
	}

	public void setStatusVendaDefault(Integer statusVendaDefault) {
		this.statusVendaDefault = statusVendaDefault;
	}

	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public boolean isPesquisaVazia() {
		return pesquisaVazia;
	}

	public int getQtdResultados() {
		return qtdResultados;
	}

	public void setPesquisaVazia(boolean pesquisaVazia) {
		this.pesquisaVazia = pesquisaVazia;
	}

	public void setQtdResultados(int qtdResultados) {
		this.qtdResultados = qtdResultados;
	}

	public Integer getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(Integer novoStatusVenda) {
		statusVenda = novoStatusVenda;
	}
	public List<Venda> getUltimasVendas() {
		return ultimasVendas;
	}

	public void setUltimaVendas(List<Venda> ultimasVendas) {
		this.ultimasVendas = ultimasVendas;
	}

	public Integer getStatusVendaConfirmadaFinanceiro() {
		return statusVendaConfirmadaFinanceiro;
	}

	public Integer getStatusVendaSeparandoMaterial() {
		return statusVendaSeparandoMaterial;
	}

	public Integer getStatusVendaEmFaseEntrega() {
		return statusVendaEmFaseEntrega;
	}

	public void setStatusVendaConfirmadaFinanceiro(
			Integer statusVendaConfirmadaFinanceiro) {
		this.statusVendaConfirmadaFinanceiro = statusVendaConfirmadaFinanceiro;
	}

	public void setStatusVendaSeparandoMaterial(Integer statusVendaSeparandoMaterial) {
		this.statusVendaSeparandoMaterial = statusVendaSeparandoMaterial;
	}

	public void setStatusVendaEmFaseEntrega(Integer statusVendaEmFaseEntrega) {
		this.statusVendaEmFaseEntrega = statusVendaEmFaseEntrega;
	}

	public String getRespostaGraficoEstoqueHome() {
		return respostaGraficoEstoqueHome.toString();
	}

	public void setRespostaGraficoEstoqueHome(
			StringBuilder respostaGraficoEstoqueHome) {
		this.respostaGraficoEstoqueHome = respostaGraficoEstoqueHome;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Integer getCodTipoPagamento() {
		return codTipoPagamento;
	}

	public void setCodTipoPagamento(Integer codTipoPagamento) {
		this.codTipoPagamento = codTipoPagamento;
	}

	public Float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Usuario getVendedorResponsavel() {
		return vendedorResponsavel;
	}

	public void setVendedorResponsavel(Usuario vendedorResponsavel) {
		this.vendedorResponsavel = vendedorResponsavel;
	}
}
