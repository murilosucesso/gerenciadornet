package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO;
import br.com.gerenciadornet.dto.RelatorioGraficoRecebPagDTO;
import br.com.gerenciadornet.dto.RelatorioGraficoVendaDTO;
import br.com.gerenciadornet.util.Constantes;

@Scope(ScopeType.CONVERSATION)
@Name("relatorioFechamentoMensalList")
public class RelatorioFechamentoMensalList {

	@In
	EntityManager entityManager;

	private Integer anoPesquisado = new GregorianCalendar().get(Calendar.YEAR);
	private Integer mesPesquisado = new GregorianCalendar().get(Calendar.MONTH);

	private Date dataInicioPesquisa = new Date(System.currentTimeMillis());
	private Date dataFimPesquisa = new Date(System.currentTimeMillis());
	
	private StringBuilder respTipoDebitoPagPie	= new StringBuilder("['Tipo Debito', 'Valor']");
	private StringBuilder respVendaProdCategoriaGraf = new StringBuilder("['Categoria', 'Vendas', 'Lucro']");

	private boolean mostrarResultados = false;

	public void executarConsultas() {

		GregorianCalendar dataInicioPesquisa = new GregorianCalendar(anoPesquisado, mesPesquisado - 1, 1);
		Date dataInicio = new Date(dataInicioPesquisa.getTimeInMillis());
		GregorianCalendar dataFimPesquisa = new GregorianCalendar(anoPesquisado, mesPesquisado - 1, getLastDayOfMonth(dataInicio));
		Date dataFim = new Date(dataFimPesquisa.getTimeInMillis());
				
		consultarTotalVendido();
		totalProdutosVendidos(dataInicio, dataFim);
		totalServicoVendidos(dataInicio, dataFim);
		consultarPagamentosRealizados(dataInicio, dataFim);
		consultarRecebimentos(dataInicio, dataFim);
		montarGraficoPagamentoXTipoPag(dataInicio, dataFim);
		totalProdutosVendidosPorCategoria(dataInicio, dataFim);

	}

	private RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTO;

	public void consultarTotalVendido() {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(vwClienteVendas.mes, vwClienteVendas.ano, sum(vwClienteVendas.valorTotalMes), sum(vwClienteVendas.lucroTotalMes)) ");
		sql.append("FROM VwClienteVendas vwClienteVendas where mes = :mesPesquisado and ano = :anoPesquisado ");
		sql.append("group by vwClienteVendas.mes, vwClienteVendas.ano");

		try{
			relatorioFechamentoMensalDTO = (RelatorioFechamentoMensalDTO) entityManager
				.createQuery(sql.toString()).setParameter("mesPesquisado", mesPesquisado)
				.setParameter("anoPesquisado", anoPesquisado).getSingleResult();
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTO = new RelatorioFechamentoMensalDTO();
			relatorioFechamentoMensalDTO.setMes(mesPesquisado);
			relatorioFechamentoMensalDTO.setAno(anoPesquisado);
		}

	}

	// TABELA DE PRODUTOS VENDIDOS
	// DETALHAR QUANTO FOI VENDIDO, QUANTO LUCROU, QUAL A QTD VENDIDA E QUANTO
	// EM COMISSÃO FOI PAGO

	RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTOProd;

	/**
	 * Consulta quanto foi vendido em produtos e quanto deu de lucro.
	 */
	public void totalProdutosVendidos(Date dataInicio, Date dataFim) {

		boolean indicadorOrcamento = false;
		
		StringBuilder sql = new StringBuilder();

		sql.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql.append("sum(pv.qtd * lp.precoVendaUnidade), ");//precoVendaSemDesconto
		sql.append("sum((pv.qtd * pv.precoVenda * (100 - v.descontoTotal)/100)  - pv.desconto ), ");//precoVendaComDesconto
		sql.append("sum(pv.qtd * lp.precoVendaUnidade) - sum((pv.qtd * pv.precoVenda * (100 - v.descontoTotal)/100)  - pv.desconto ), ");//valorTotalDescontos = precoVendaSemDesconto - precoVendaComDesconto
		sql.append("sum(pv.qtd * lp.precoCompraUnidade), ");//valorTotalPrecoCompra
		sql.append("sum(prod.valorComissao * pv.qtd), ");//valorTotalComissoesSemDesconto
		//sql.append("sum((prod.valorComissao * pv.qtd + ((pv.precoVenda * (100 - v.descontoTotal)/100 ) - lp.precoVendaUnidade)/2 * pv.qtd)), ");//valorTotalComissoesComDesconto
		sql.append("sum(prod.valorComissao * pv.qtd), ");//valorTotalComissoesComDesconto
		sql.append("sum((pv.qtd * pv.precoVenda * (100 - v.descontoTotal)/100)  - pv.desconto ) - sum(pv.qtd * lp.precoCompraUnidade)) "); //valorTotalLucroBruto - lucro sem descontar comissoes e taxas

		sql.append("from Venda as v ");

		sql.append("inner join v.produtoVendidos as pv ");
		sql.append("inner join pv.loteProduto as lp ");
		sql.append("inner join lp.produto as prod ");

		sql.append("where v.statusVenda.codStatusVenda not in( "
				+ Constantes.CANCELADA + ", " + Constantes.ORCAMENTO + ", "
				+ Constantes.PEDIDO + "," + Constantes.PEDIDO_AUTORIZADO + ") ");

		sql.append("and v.inOrcamento = :indicadorOrcamento ");
		sql.append("and v.dataInicioVenda >= :dataInicio ");
		sql.append("and v.dataInicioVenda <= :dataFim ");
		
		try{
			
			relatorioFechamentoMensalDTOProd = (RelatorioFechamentoMensalDTO) entityManager
				.createQuery(sql.toString())
				.setParameter("indicadorOrcamento", indicadorOrcamento)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getSingleResult();
		
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTOProd = new RelatorioFechamentoMensalDTO();
		}
	}
			
	/**
	 * Consulta quanto foi vendido em produtos e quanto deu de lucro.
	 */
	@SuppressWarnings("unchecked")
	public void totalProdutosVendidosPorCategoria(Date dataInicio, Date dataFim) {

		boolean indicadorOrcamento = false;
		
		StringBuilder sql = new StringBuilder();

		sql.append("select new br.com.gerenciadornet.dto.RelatorioGraficoVendaDTO(");		
		sql.append("cat.nomeCategoria, ");//precoVendaComDesconto
		sql.append("sum((pv.qtd * pv.precoVenda * (100 - v.descontoTotal)/100)  - pv.desconto ), ");//precoVendaComDesconto
		sql.append("sum((pv.qtd * pv.precoVenda * (100 - v.descontoTotal)/100)  - pv.desconto ) - sum(pv.qtd * lp.precoCompraUnidade)) "); //valorTotalLucroBruto - lucro sem descontar comissoes e taxas

		sql.append("from Venda as v ");

		sql.append("inner join v.produtoVendidos as pv ");
		sql.append("inner join pv.loteProduto as lp ");
		sql.append("inner join lp.produto as prod ");
		sql.append("inner join prod.categoria as cat ");
		
		sql.append("where v.statusVenda.codStatusVenda not in( "
				+ Constantes.CANCELADA + ", " + Constantes.ORCAMENTO + ", "
				+ Constantes.PEDIDO + "," + Constantes.PEDIDO_AUTORIZADO + ") ");

		sql.append("and v.inOrcamento = :indicadorOrcamento ");
		sql.append("and v.dataInicioVenda >= :dataInicio ");
		sql.append("and v.dataInicioVenda <= :dataFim ");
		
		sql.append("group by cat.codCategoria order by cat.nomeCategoria ");
		
		try{
			
			List <RelatorioGraficoVendaDTO> produtosPorCategoriaList = new ArrayList<RelatorioGraficoVendaDTO>();
			
			produtosPorCategoriaList = (List <RelatorioGraficoVendaDTO>) entityManager
				.createQuery(sql.toString())
				.setParameter("indicadorOrcamento", indicadorOrcamento)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		
			// Montando resposta para gráfico de Vendas por Categoria
			for (RelatorioGraficoVendaDTO dto : produtosPorCategoriaList) {		
				respVendaProdCategoriaGraf.append(",['" + dto.getNome() + "', " + Math.round(dto.getValorVenda()) + ", " + Math.round(dto.getValorLucro()) + "]");			
			}
			System.out.println("\n\n === totalProdutosVendidosPorCategoria =====");
			System.out.println(respVendaProdCategoriaGraf.toString());
						
		}catch(NoResultException ex){
			System.out.println("\n---- Erro ao gerar codigo pra gráfico totalProdutosVendidosPorCategoria -----\n");
		}
	}
	
	RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTOSevico;
	
	public void totalServicoVendidos(Date dataInicio, Date dataFim) {
		
		boolean indicadorOrcamento = false;
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql.append("sum(s.precoCusto), sum(s.precoServico), sum(sr.preco), ");
		sql.append("sum(s.precoCusto + ((sr.preco * (100 - v.descontoTotal)/100) - s.precoServico )/2), ");//comissao
		//lucro = valor cobrado do serviço realizado com desconto total - comissão
		sql.append("sum((sr.preco  * (100 - v.descontoTotal)/100)  - (s.precoCusto + ((sr.preco * (100 - v.descontoTotal)/100) - s.precoServico )/2))) ");

		sql.append("from Venda as v ");
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
		sql.append("order by v.codVenda ");
		
		try{
			relatorioFechamentoMensalDTOSevico = (RelatorioFechamentoMensalDTO)entityManager
				.createQuery(sql.toString())
				.setParameter("indicadorOrcamento", indicadorOrcamento)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)// testar se pega o dia por causa da hora
				.getSingleResult();
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTOSevico = new RelatorioFechamentoMensalDTO();
		}
	}
	
	RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTOPagamento = new RelatorioFechamentoMensalDTO();
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */
	public void consultarPagamentosRealizados(Date dataInicio, Date dataFim){
		
		//------------------------- totalPagamentosAgendados - TOTAL AGENDADO NO MÊS ----------
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql.append("SUM(p.valor)) ");
		sql.append("from Pagamento as p ");
		sql.append("where  ");
		sql.append("p.dataVencimento >= :dataInicio ");
		sql.append("and p.dataVencimento <= :dataFim ");
		
		try{
			relatorioFechamentoMensalDTOPagamento = (RelatorioFechamentoMensalDTO)entityManager
					.createQuery(sql.toString())
					.setParameter("dataInicio", dataInicio)
					.setParameter("dataFim", dataFim)
					.getSingleResult();	
			
			if(relatorioFechamentoMensalDTOPagamento.getValor() == null){
				relatorioFechamentoMensalDTOPagamento.setValor(0d);
			}
			
			relatorioFechamentoMensalDTOPagamento.setTotalPagamentosAgendados(relatorioFechamentoMensalDTOPagamento.getValor());
			
		} catch(NoResultException ex){			
			relatorioFechamentoMensalDTOPagamento.setTotalPagamentosAgendados(0.0d);
		}
		
		
		//------------------------- totalPagamentosPendentes - TOTAL AGENDADO NO MÊS PORÉM NÃO PAGO
		
		RelatorioFechamentoMensalDTO dtoPagamento;
		
		StringBuilder sql1 = new StringBuilder();
		
		sql1.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql1.append("SUM(p.valor)) ");
		sql1.append("from Pagamento as p ");
		sql1.append("where (p.dataBalancete IS NULL or p.dataBalancete > :dataFim ) ");//NÃO PAGO OU NÃO PAGO EM ATRASO NO PRÓXIMO MÊS
		sql1.append("and p.dataVencimento >= :dataInicio ");//AGENDADO NO MES
		sql1.append("and p.dataVencimento <= :dataFim ");
		
		try{
			dtoPagamento = (RelatorioFechamentoMensalDTO)entityManager
				.createQuery(sql1.toString())
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getSingleResult();	
		
			if(dtoPagamento.getValor() == null){
				dtoPagamento.setValor(0d);
			}
			
			relatorioFechamentoMensalDTOPagamento.setTotalPagamentosPendentes(dtoPagamento.getValor());
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTOPagamento.setTotalPagamentosPendentes(0.0d);
			
		}
		
		//-------------------------TotalPago - TOTAL PAGO NO MES, INDEPENDENTE DA DATA DE VENCIMENTO
		RelatorioFechamentoMensalDTO dtoPagamento2;
		StringBuilder sql3 = new StringBuilder();
		
		sql3.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql3.append("SUM(p.valor)) ");
		sql3.append("from Pagamento as p ");
		sql3.append("where p.dataBalancete IS NOT NULL ");
		sql3.append("and p.dataBalancete >= :dataInicio ");
		sql3.append("and p.dataBalancete <= :dataFim ");
		
		try{
			dtoPagamento2 = (RelatorioFechamentoMensalDTO)entityManager
					.createQuery(sql3.toString())
					.setParameter("dataInicio", dataInicio)
					.setParameter("dataFim", dataFim)
					.getSingleResult();	
			
			if(dtoPagamento2.getValor() == null){
				dtoPagamento2.setValor(0d);
			}
			
			relatorioFechamentoMensalDTOPagamento.setTotalPago(dtoPagamento2.getValor());
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTOPagamento.setTotalPago(0.0d);
		}
		
		//-------------------------TotalPagoAgendado - TOTAL PAGO NO MES, QUE ESTAVA AGENDADO
		RelatorioFechamentoMensalDTO dtoPagamento3;
		StringBuilder sql4 = new StringBuilder();
		
		sql4.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql4.append("SUM(p.valor)) ");
		sql4.append("from Pagamento as p ");
		sql4.append("where ");
		sql4.append("p.dataVencimento >= :dataInicio ");
		sql4.append("and p.dataVencimento <= :dataFim ");
		sql4.append("and p.dataBalancete >= :dataInicio ");
		sql4.append("and p.dataBalancete <= :dataFim ");
		
		try{
			dtoPagamento3 = (RelatorioFechamentoMensalDTO)entityManager
					.createQuery(sql4.toString())
					.setParameter("dataInicio", dataInicio)
					.setParameter("dataFim", dataFim)
					.getSingleResult();	
			
			if(dtoPagamento3.getValor() == null){
				dtoPagamento3.setValor(0d);
			}
			
			relatorioFechamentoMensalDTOPagamento.setTotalPagoAgendado(dtoPagamento3.getValor());
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTOPagamento.setTotalPagoAgendado(0.0d);
		}
		
		//-------------------------Total Pago Não Agendado - TOTAL PAGO NO MES, INDEPENDENTE DA DATA DE VENCIMENTO
		relatorioFechamentoMensalDTOPagamento.setTotalPagoNaoAgendado( 
				relatorioFechamentoMensalDTOPagamento.getTotalPago() -
				relatorioFechamentoMensalDTOPagamento.getTotalPagoAgendado()
				);
	}
	
	
	RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTORecebimento = new RelatorioFechamentoMensalDTO();
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */
	public void consultarRecebimentos(Date dataInicio, Date dataFim){
		
		//-------------------------- TOTAL AGENDADO A RECEBER -------
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql.append("SUM(r.valorAReceber)) ");
		sql.append("from Recebimento r ");
		sql.append("where ");
		sql.append("r.dataAgendamentoRecebimento >= :dataInicio ");
		sql.append("and r.dataAgendamentoRecebimento <= :dataFim ");
		
		try{
			RelatorioFechamentoMensalDTO r = (RelatorioFechamentoMensalDTO)entityManager
					.createQuery(sql.toString())
					.setParameter("dataInicio", dataInicio)
					.setParameter("dataFim", dataFim)
					.getSingleResult();	
			
			if(r.getValor() == null){
				r.setValor(0d);
			}
			
			relatorioFechamentoMensalDTORecebimento.setTotalAgendadoAReceber(r.getValor());
			
		} catch(NoResultException ex){
			relatorioFechamentoMensalDTORecebimento.setTotalAgendadoAReceber(0.0d);
		}
		
		
		//-------------------------- TOTAL RECEBIDO - INDEPENDENTE DA DATA DO AGENDAMENTO DO RECEBIMENTO
		StringBuilder sql3 = new StringBuilder();
		
		sql3.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql3.append("SUM(r.valorPago)) ");
		sql3.append("from Recebimento r ");
		sql3.append("where ");
		sql3.append("r.dataBalancete >= :dataInicio ");
		sql3.append("and r.dataBalancete <= :dataFim ");
		
		try{
			RelatorioFechamentoMensalDTO r2 = (RelatorioFechamentoMensalDTO)entityManager
					.createQuery(sql3.toString())
					.setParameter("dataInicio", dataInicio)
					.setParameter("dataFim", dataFim)
					.getSingleResult();	
			
			if(r2.getValor() == null){
				r2.setValor(0d);
			}
			
			relatorioFechamentoMensalDTORecebimento.setTotalRecebido(r2.getValor());
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTORecebimento.setTotalRecebido(0.0d);
		}
		

		//-------------------------- TOTAL RECEBIDO DO VALOR TOTAL AGENDADO
		StringBuilder sql2 = new StringBuilder();
		
		sql2.append("select new br.com.gerenciadornet.dto.RelatorioFechamentoMensalDTO(");
		sql2.append("SUM(r.valorPago)) ");
		sql2.append("from Recebimento r ");
		sql2.append("where ");
		sql2.append("r.dataBalancete >= :dataInicio ");
		sql2.append("and r.dataBalancete <= :dataFim ");
		sql2.append("and r.dataAgendamentoRecebimento >= :dataInicio ");
		sql2.append("and r.dataAgendamentoRecebimento <= :dataFim ");
		
		try{
			RelatorioFechamentoMensalDTO r1 = (RelatorioFechamentoMensalDTO)entityManager
					.createQuery(sql2.toString())
					.setParameter("dataInicio", dataInicio)
					.setParameter("dataFim", dataFim)
					.getSingleResult();	
			
			if(r1.getValor() == null){
				r1.setValor(0d);
			}
			
			relatorioFechamentoMensalDTORecebimento.setTotalRecebidoAgendado(r1.getValor());
			
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTORecebimento.setTotalRecebidoAgendado(0.0d);
		}
		
		
		//-------------------------- TOTAL RECEBIDO NÃO AGENDADO
		relatorioFechamentoMensalDTORecebimento.setTotalRecebidoNaoAgendado(
				relatorioFechamentoMensalDTORecebimento.getTotalRecebido() - 
				relatorioFechamentoMensalDTORecebimento.getTotalRecebidoAgendado()); 
		
		//-------------------------- TOTAL A RECEBER - PENDENTE
				relatorioFechamentoMensalDTORecebimento.setTotalPendenteRecebimento(
						relatorioFechamentoMensalDTORecebimento.getTotalAgendadoAReceber() - 
						relatorioFechamentoMensalDTORecebimento.getTotalRecebidoAgendado()); 
		
	}
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */
	@SuppressWarnings("unchecked")
	public void montarGraficoPagamentoXTipoPag(Date dataInicio, Date dataFim){
		
		respTipoDebitoPagPie = new StringBuilder();
		
		StringBuilder sqlGraphTipoDebitoPagPie = new StringBuilder();
		sqlGraphTipoDebitoPagPie.append("SELECT new br.com.gerenciadornet.dto.RelatorioGraficoRecebPagDTO(p.tipoDebito.descTipoDebito , sum(p.valor)) ");
		sqlGraphTipoDebitoPagPie.append("FROM Pagamento p where p.dataBalancete >= :dataInicial and p.dataBalancete <= :dataFinal ");
		sqlGraphTipoDebitoPagPie.append("group by p.tipoDebito.descTipoDebito order by p.tipoDebito.descTipoDebito");
		
		List <RelatorioGraficoRecebPagDTO> tipoDebitoList 			= new ArrayList<RelatorioGraficoRecebPagDTO>();
		
		try{
			tipoDebitoList = (List<RelatorioGraficoRecebPagDTO>)entityManager
					.createQuery(sqlGraphTipoDebitoPagPie.toString())
					.setParameter("dataInicial", dataInicio)
					.setParameter("dataFinal", dataFim)
					.getResultList();
			
			// Montando resposta para gráfico de Pagamento por Tipo de Débito		
			for (RelatorioGraficoRecebPagDTO pagamentoPorTipoDebitoDTO : tipoDebitoList) {		
				respTipoDebitoPagPie.append(",['" + pagamentoPorTipoDebitoDTO.getNome() + "', " + Math.round(pagamentoPorTipoDebitoDTO.getValorAPagar()) + "]");			
			}
		
		}catch(NoResultException ex){
			relatorioFechamentoMensalDTOSevico = new RelatorioFechamentoMensalDTO();
		}
		
	}

	public List<SelectItem> getListaAnos() {

		Integer anoInicial = new GregorianCalendar().get(Calendar.YEAR);
		List<SelectItem> anos = new ArrayList<SelectItem>();

		for (int i = anoInicial; i > 2010; i--) {
			anos.add(new SelectItem(i, "" + i));
		}

		return anos;
	}

	public List<SelectItem> getListaMeses() {

		List<SelectItem> meses = new ArrayList<SelectItem>();

		meses.add(new SelectItem(1, "Janeiro"));
		meses.add(new SelectItem(2, "Fevereiro"));
		meses.add(new SelectItem(3, "Março"));
		meses.add(new SelectItem(4, "Abril"));
		meses.add(new SelectItem(5, "Maio"));
		meses.add(new SelectItem(6, "Junho"));
		meses.add(new SelectItem(7, "Julho"));
		meses.add(new SelectItem(8, "Agosto"));
		meses.add(new SelectItem(9, "Setembro"));
		meses.add(new SelectItem(10, "Outubro"));
		meses.add(new SelectItem(11, "Novembro"));
		meses.add(new SelectItem(12, "Dezembro"));

		return meses;
	}

	public int getLastDayOfMonth(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public Date getDataInicioPesquisa() {
		return dataInicioPesquisa;
	}

	public void setDataInicioPesquisa(Date dataInicioPesquisa) {
		this.dataInicioPesquisa = dataInicioPesquisa;
	}

	public Date getDataFimPesquisa() {
		return dataFimPesquisa;
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

	public RelatorioFechamentoMensalDTO getRelatorioFechamentoMensalDTO() {
		return relatorioFechamentoMensalDTO;
	}

	public void setRelatorioFechamentoMensalDTO(
			RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTO) {
		this.relatorioFechamentoMensalDTO = relatorioFechamentoMensalDTO;
	}

	public Integer getAnoPesquisado() {
		return anoPesquisado;
	}

	public void setAnoPesquisado(Integer anoPesquisado) {
		this.anoPesquisado = anoPesquisado;
	}

	public Integer getMesPesquisado() {
		return mesPesquisado;
	}

	public void setMesPesquisado(Integer mesPesquisado) {
		this.mesPesquisado = mesPesquisado;
	}

	public RelatorioFechamentoMensalDTO getRelatorioFechamentoMensalDTOProd() {
		return relatorioFechamentoMensalDTOProd;
	}

	public void setRelatorioFechamentoMensalDTOProd(
			RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTOProd) {
		this.relatorioFechamentoMensalDTOProd = relatorioFechamentoMensalDTOProd;
	}

	public RelatorioFechamentoMensalDTO getRelatorioFechamentoMensalDTOSevico() {
		return relatorioFechamentoMensalDTOSevico;
	}

	public void setRelatorioFechamentoMensalDTOSevico(
			RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTOSevico) {
		this.relatorioFechamentoMensalDTOSevico = relatorioFechamentoMensalDTOSevico;
	}

	public RelatorioFechamentoMensalDTO getRelatorioFechamentoMensalDTOPagamento() {
		return relatorioFechamentoMensalDTOPagamento;
	}

	public void setRelatorioFechamentoMensalDTOPagamento(
			RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTOPagamento) {
		this.relatorioFechamentoMensalDTOPagamento = relatorioFechamentoMensalDTOPagamento;
	}

	public RelatorioFechamentoMensalDTO getRelatorioFechamentoMensalDTORecebimento() {
		return relatorioFechamentoMensalDTORecebimento;
	}

	public void setRelatorioFechamentoMensalDTORecebimento(
			RelatorioFechamentoMensalDTO relatorioFechamentoMensalDTORecebimento) {
		this.relatorioFechamentoMensalDTORecebimento = relatorioFechamentoMensalDTORecebimento;
	}

	public StringBuilder getRespTipoDebitoPagPie() {
		return respTipoDebitoPagPie;
	}

	public void setRespTipoDebitoPagPie(StringBuilder respTipoDebitoPagPie) {
		this.respTipoDebitoPagPie = respTipoDebitoPagPie;
	}

	public StringBuilder getRespVendaProdCategoriaGraf() {
		return respVendaProdCategoriaGraf;
	}

	public void setRespVendaProdCategoriaGraf(
			StringBuilder respVendaProdCategoriaGraf) {
		this.respVendaProdCategoriaGraf = respVendaProdCategoriaGraf;
	}
}
