package br.com.gerenciadornet.session;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Recebimento;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;

@SuppressWarnings("serial")
@Name("recebimentoList")
@Scope(ScopeType.CONVERSATION)
public class RecebimentoList extends EntityQuery<Recebimento> {
	
	/*
	 * tipos de recebimentos:
	 * 
	 * 1 - TODOS: Data de vencimento ou data balancente dentro do periodo informado
	 * 2 - A RECEBER: Data de vencimento dentro do periodo informado E data balancente = NULL
	 * 3 - RECEBIDOS: Data de balancente dentro do periodo informado 
	 * 
	 *  Venda (Data Venda)
	 *  1 - TODAS: independente da data da venda. Não filtra
	 *  2 - Abertas no período
	 */

	private static final String EJBQL = "select recebimento from Recebimento recebimento"
			+ " left join fetch recebimento.venda"
			+ " left join fetch recebimento.venda.tipoPagamento"
			+ " left join fetch recebimento.venda.statusVenda"
			+ " left join fetch recebimento.venda.cliente"
			+ " left join fetch recebimento.venda.usuario";

	private static String[] RESTRICTIONS = {
			"recebimento.venda.cliente.codCliente = #{recebimentoList.recebimento.venda.cliente.codCliente}",
			"recebimento.venda.usuario.codUsuario = #{recebimentoList.recebimento.venda.usuario.codUsuario}",
			"lower(recebimento.venda.tipoPagamento.descTipoPagamento) like lower(concat('%', concat(#{recebimentoList.tipoPagamento},'%')))",
			"recebimento.numDocumento  like lower(concat('%', concat( #{recebimentoList.recebimento.numDocumento},'%')))",
			"recebimento.valorAReceber = #{recebimentoList.recebimento.valorAReceber}",
			"recebimento.venda.statusVenda.codStatusVenda = #{recebimentoList.codigoStatusVenda}",
			
			"recebimento.inConferencia = #{recebimentoList.indicadorConferencia}",

			"recebimento.venda.dataInicioVenda >= #{recebimentoList.dataInicioVenda}",
			"recebimento.venda.dataInicioVenda <= #{recebimentoList.dataFimVenda}",

			"recebimento.dataAgendamentoRecebimento >= #{recebimentoList.dataInicioRecebimento}",
			"recebimento.dataAgendamentoRecebimento <= #{recebimentoList.dataFimRecebimento}",

			"recebimento.dataBalancete >= #{recebimentoList.dataInicioBalancete}",
			"recebimento.dataBalancete <= #{recebimentoList.dataFimBalancete}", };

	private static String[] RESTRICTIONS2 = { "recebimento.venda.codVenda = #{recebimentoList.recebimento.venda.codVenda}" };

	private static String[] RESTRICTIONS3 = {
			"recebimento.venda.cliente.codCliente = #{recebimentoList.recebimento.venda.cliente.codCliente}",
			"recebimento.venda.usuario.codUsuario = #{recebimentoList.recebimento.venda.usuario.codUsuario}",
			"lower(recebimento.venda.tipoPagamento.descTipoPagamento) like lower(concat('%', concat(#{recebimentoList.tipoPagamento},'%')))",
			"recebimento.numDocumento  like lower(concat('%', concat( #{recebimentoList.recebimento.numDocumento},'%')))",
			"recebimento.valorAReceber = #{recebimentoList.recebimento.valorAReceber}",

			"recebimento.venda.statusVenda.codStatusVenda <> #{recebimentoList.codigoStatusVendaConcluida}",
			
			"recebimento.inConferencia = #{recebimentoList.indicadorConferencia}",

			"recebimento.venda.dataInicioVenda >= #{recebimentoList.dataInicioVenda}",
			"recebimento.venda.dataInicioVenda <= #{recebimentoList.dataFimVenda}",

			"recebimento.dataAgendamentoRecebimento >= #{recebimentoList.dataInicioRecebimento}",
			"recebimento.dataAgendamentoRecebimento <= #{recebimentoList.dataFimRecebimento}",

			"recebimento.dataBalancete >= #{recebimentoList.dataInicioBalancete}",
			"recebimento.dataBalancete <= #{recebimentoList.dataFimBalancete}", };

	private Recebimento recebimento = new Recebimento();

	private Boolean indicadorConferencia;

	private String tipoPagamento = null;

	private Date dataAtual = new Date();
	private Date dataInicio = new Date();
	private Date dataFim = new Date();

	private Date dataInicioVenda;
	private Date dataFimVenda;

	private Date dataInicioRecebimento;
	private Date dataFimRecebimento;

	private Date dataInicioBalancete;
	private Date dataFimBalancete;

	private boolean mostrarResultados = false;
	private Integer numeroLinhasDT = 50;

	private Float valorTotalAReceber = new Float(0);
	private Float valorTotalRecebido = new Float(0);
	private Float valorTotalMulta = new Float(0);

	private Integer codigoStatusVenda = 0;
	private Integer codigoStatusVendaConcluida = Constantes.CONCLUIDA;

	/**
	 * 1 - Pesquisa por Data Balancete 2 - Pesquisa por Data Venda 3 - Pesquisa
	 * por Data Recebimento
	 */
	private Integer tipoData = 3;

	public RecebimentoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("recebimento.venda.cliente.nome, recebimento.venda.dataInicioVenda");
	}

	List<Recebimento> recebimentosList;

	Integer qtdResultados = 0;

	/**
	 * Substitui o metodo getResultList()
	 */
	public void listarRecebimentos() {

		if (this.recebimento.getVenda().getCodVenda() != null) {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		} else {
			// Tem que colocar esta linha para ele sempre realizar a consulta
			// novamente qndo um recebimento for excluído. Senão dá o erro de
			// "removing a detached instance" no caso de remover
			// o mesmo objeto duas vezes.
			if (codigoStatusVenda != null && codigoStatusVenda == 88) {
				setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS3));
			} else {
				setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
			}
		}
		
		// limpar campos data. Para caso de nova pesquisa alterando o tipo da
		// data.
		setDataInicioBalancete(null);
		setDataFimBalancete(null);
		setDataInicioVenda(null);
		setDataFimVenda(null);
		setDataInicioRecebimento(null);
		setDataFimRecebimento(null);

		if (getTipoData() == 1) {
			// Nao precisa adicionar + 86400000l pq não é timestamp
			setDataInicioBalancete(getDataInicio());
			setDataFimBalancete(new Date(getDataFim().getTime()));

		} else if (getTipoData() == 2) {
			setDataInicioVenda(getDataInicio());
			setDataFimVenda(new Date(getDataFim().getTime() + 86400000l));

		} else if (getTipoData() == 3) {
			// Nao precisa adicionar + 86400000l pq não é timestamp
			setDataInicioRecebimento(getDataInicio());
			setDataFimRecebimento(new Date(getDataFim().getTime()));
		}

		List<Recebimento> recebimentos = super.getResultList();

		Float valorTotalAReceberAux = new Float(0);
		Float valorTotalRecebidoAux = new Float(0);
		Float valorTotalMultaAux = new Float(0);

		for (Recebimento pag : recebimentos) {

			if (pag.getValorAReceber() != null) {
				valorTotalAReceberAux += pag.getValorAReceber();
			}
			if (pag.getValorPago() != null) {
				valorTotalRecebidoAux += pag.getValorPago();
			}
			if (pag.getValorMulta() != null) {
				valorTotalMultaAux += pag.getValorMulta();
			}
		}
		setValorTotalAReceber(valorTotalAReceberAux);
		setValorTotalRecebido(valorTotalRecebidoAux);
		setValorTotalMulta(valorTotalMultaAux);

		recebimentosList = recebimentos;
		qtdResultados = recebimentos.size();
	}

	public List<Venda> vendasFinalizadasDia;
	Integer qtdResultFinalizadas = 0;

	private String query = new String(
			"select venda from Venda venda where "
					+ "venda.statusVenda.codStatusVenda <> :cancelada "
					+ "and venda.statusVenda.codStatusVenda <> :orcamento "
					+ "and venda.dataFimVenda >= :dataFimVenda "
					+ "and venda.dataFimVenda <= :dataFimVenda2 order by venda.dataFimVenda desc ");

	/**
	 * Lista todas as vendas finalizadas no dia. Metodo utilizado pela popup
	 * finalizar venda.
	 * 
	 * @param codVenda
	 * @return
	 */
	public void listarVendasFinalizadasDia() {

		Query createQuery = getEntityManager().createQuery(query);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			createQuery.setParameter("cancelada", Constantes.CANCELADA);
			createQuery.setParameter("orcamento", Constantes.ORCAMENTO);
			createQuery.setParameter("dataFimVenda",
					sdf.parse(sdf.format(new Date())));
			createQuery.setParameter("dataFimVenda2", sdf.parse(sdf
					.format(new Date(new Date().getTime() + 86400000l))));

		} catch (Exception e) {
			e.printStackTrace();
		}

		@SuppressWarnings("unchecked")
		List<Venda> vendas = (List<Venda>) createQuery.getResultList();
		vendasFinalizadasDia = vendas;
		qtdResultFinalizadas = vendasFinalizadasDia.size();

	}

	public Recebimento getRecebimento() {
		return recebimento;
	}

	public Date getDataInicioVenda() {
		return dataInicioVenda;
	}

	public void setDataInicioVenda(Date dataInicioVenda) {
		this.dataInicioVenda = dataInicioVenda;
	}

	public Date getDataFimVenda() {
		return dataFimVenda;
	}

	public void setDataFimVenda(Date dataFimVenda) {
		this.dataFimVenda = dataFimVenda;
	}

	public Date getDataInicioRecebimento() {
		return dataInicioRecebimento;
	}

	public void setDataInicioRecebimento(Date dataInicioRecebimento) {
		this.dataInicioRecebimento = dataInicioRecebimento;
	}

	public Date getDataFimRecebimento() {
		return dataFimRecebimento;
	}

	public void setDataFimRecebimento(Date dataFimRecebimento) {
		this.dataFimRecebimento = dataFimRecebimento;
	}

	public Date getDataInicioBalancete() {
		return dataInicioBalancete;
	}

	public void setDataInicioBalancete(Date dataInicioBalancete) {
		this.dataInicioBalancete = dataInicioBalancete;
	}

	public Date getDataFimBalancete() {
		return dataFimBalancete;
	}

	public void setDataFimBalancete(Date dataFimBalancete) {
		this.dataFimBalancete = dataFimBalancete;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getTipoData() {
		return tipoData;
	}

	public void setTipoData(Integer tipoData) {
		this.tipoData = tipoData;
	}

	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public Float getValorTotalAReceber() {
		return valorTotalAReceber;
	}

	public void setValorTotalAReceber(Float valorTotalAReceber) {
		this.valorTotalAReceber = valorTotalAReceber;
	}

	public Float getValorTotalRecebido() {
		return valorTotalRecebido;
	}

	public void setValorTotalRecebido(Float valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Float getValorTotalMulta() {
		return valorTotalMulta;
	}

	public void setValorTotalMulta(Float valorTotalMulta) {
		this.valorTotalMulta = valorTotalMulta;
	}

	public Boolean getIndicadorConferencia() {
		return indicadorConferencia;
	}

	public void setIndicadorConferencia(Boolean indicadorConferencia) {
		this.indicadorConferencia = indicadorConferencia;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}

	public List<Recebimento> getRecebimentosList() {
		return recebimentosList;
	}

	public Integer getQtdResultados() {
		return qtdResultados;
	}

	public List<Venda> getVendasFinalizadasDia() {
		return vendasFinalizadasDia;
	}

	public Integer getQtdResultFinalizadas() {
		return qtdResultFinalizadas;
	}
	
	public Integer getCodigoStatusVenda() {
		return codigoStatusVenda;
	}

	public void setCodigoStatusVenda(Integer codigoStatusVenda) {
		this.codigoStatusVenda = codigoStatusVenda;
	}

	public Integer getCodigoStatusVendaConcluida() {
		return codigoStatusVendaConcluida;
	}

}
