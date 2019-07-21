package br.com.gerenciadornet.session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import br.com.gerenciadornet.dto.RelatorioGraficoRecebPagDTO;

@Name("relatorioGraficoRecebXPagaList")
public class RelatorioGraficoRecebXPagaList{
	
	
	/* PAREI AQUI.. IMPLEMENTAR NOVO GRÁFICA MONSTRANDO OS GASTOS POR TIPO DE DÉBITO
	 * 
	 * SELECT sum(p.valor)as valor, td.desc_tipo_debito FROM pagamento p, tipo_debito td
	where
	td.cod_tipo_debito = p.cod_tipo_debito and
	p.data_balancete >= "2016-10-03" and p.data_balancete <= "2019-10-03" group by td.cod_tipo_debito;*/

	@In
	EntityManager entityManager;

	private Date dataInicial;
		
	//Por default, mostra o que ira vencer no proximo mes.
	private Date dataFinal;
	
	private StringBuilder respContasRecPag		= new StringBuilder("['Data', 'A Pagar', 'A Receber']");
	private StringBuilder respContasRecPagBar	= new StringBuilder("['Data', 'A Pagar', 'A Receber']");
	private StringBuilder respContasRecPagPie	= new StringBuilder("['A Pagar', 'A Receber']");
	private StringBuilder respTipoDebitoPagPie	= new StringBuilder("['Tipo Debito', 'Valor']");
	
	/**
	 * Metodo chamado para carregar os dados que irao compor os gráficos.
	 * Chamado de RelatorioGraficoRecPagList.page.xml 
	 */
	@SuppressWarnings("unchecked")
	public void executar(){

		if(dataInicial == null){
			dataInicial = new Date(System.currentTimeMillis());
		}
		
		if(dataFinal == null){
			dataFinal = new Date(System.currentTimeMillis() + (30 * 86400000l));
		}
		
		StringBuilder sqlContasAPagar = new StringBuilder();
		sqlContasAPagar.append("SELECT new br.com.gerenciadornet.dto.RelatorioGraficoRecebPagDTO(pagamento.dataVencimento, sum(pagamento.valor)) ");
		sqlContasAPagar.append("FROM Pagamento pagamento where pagamento.dataVencimento >= :dataInicial and pagamento.dataVencimento <= :dataFinal ");
		sqlContasAPagar.append("and pagamento.inConferencia = :inConferencia ");
		sqlContasAPagar.append("group by pagamento.dataVencimento order by pagamento.dataVencimento");
						
		StringBuilder sqlContasAReceber = new StringBuilder();
		sqlContasAReceber.append("SELECT new br.com.gerenciadornet.dto.RelatorioGraficoRecebPagDTO(sum(recebimento.valorAReceber), recebimento.dataAgendamentoRecebimento) ");
		sqlContasAReceber.append("FROM Recebimento recebimento where recebimento.dataAgendamentoRecebimento >= :dataInicial and recebimento.dataAgendamentoRecebimento <= :dataFinal ");
		sqlContasAReceber.append("and recebimento.inConferencia = :inConferencia ");
		sqlContasAReceber.append("group by recebimento.dataAgendamentoRecebimento order by recebimento.dataAgendamentoRecebimento");
		
		StringBuilder sqlGraphTipoDebitoPagPie = new StringBuilder();
		sqlGraphTipoDebitoPagPie.append("SELECT new br.com.gerenciadornet.dto.RelatorioGraficoRecebPagDTO(p.tipoDebito.descTipoDebito , sum(p.valor)) ");
		sqlGraphTipoDebitoPagPie.append("FROM Pagamento p where p.dataBalancete >= :dataInicial and p.dataBalancete <= :dataFinal ");
		sqlGraphTipoDebitoPagPie.append("group by p.tipoDebito.descTipoDebito order by p.tipoDebito.descTipoDebito");
		
		
		List <RelatorioGraficoRecebPagDTO> contasAPagarList 		= new ArrayList<RelatorioGraficoRecebPagDTO>();
		List <RelatorioGraficoRecebPagDTO> contasAReceberList 		= new ArrayList<RelatorioGraficoRecebPagDTO>();
		List <RelatorioGraficoRecebPagDTO> tipoDebitoList 			= new ArrayList<RelatorioGraficoRecebPagDTO>();
		
		double totalAPagar   = 0;
		double totalAReceber = 0;
		
		try {
			contasAPagarList = (List<RelatorioGraficoRecebPagDTO>)entityManager
					.createQuery(sqlContasAPagar.toString())
					.setParameter("dataInicial", dataInicial)
					.setParameter("dataFinal", dataFinal)
					.setParameter("inConferencia", false)
					.getResultList();
			
			contasAReceberList = (List<RelatorioGraficoRecebPagDTO>)entityManager
					.createQuery(sqlContasAReceber.toString())
					.setParameter("dataInicial", dataInicial)
					.setParameter("dataFinal", dataFinal)
					.setParameter("inConferencia", false)
					.getResultList();
			
			tipoDebitoList = (List<RelatorioGraficoRecebPagDTO>)entityManager
					.createQuery(sqlGraphTipoDebitoPagPie.toString())
					.setParameter("dataInicial", dataInicial)
					.setParameter("dataFinal", dataFinal)
					.getResultList();
			
			
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
			System.out.println("Causa:" + e.getCause());
			e.printStackTrace();
		}
		
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		
		RelatorioGraficoRecebPagDTO[] contasAReceberAux = new RelatorioGraficoRecebPagDTO[contasAReceberList.size()];
		contasAReceberAux = contasAReceberList.toArray(contasAReceberAux);
		 
		int i = 0;
		boolean respVazia = true;
		
		for (RelatorioGraficoRecebPagDTO contasAPagarDTO : contasAPagarList) {
		
			double totalRecebimentos = 0d;
			respVazia = false;

			for( ; i < contasAReceberAux.length; i++){
				
				if(contasAReceberAux[i].getData().compareTo(contasAPagarDTO.getData()) < 0){
					totalRecebimentos += contasAReceberAux[i].getValorAReceber();
					
				} else  if(contasAReceberAux[i].getData().compareTo(contasAPagarDTO.getData()) == 0){
					totalRecebimentos += contasAReceberAux[i].getValorAReceber();
					i++;
					break;
					
				} else {
					break;
				}
				
			}
			respContasRecPag.append(",['" + spf.format(contasAPagarDTO.getData()) + "', " + Math.round(contasAPagarDTO.getValorAPagar()) + ", " +  Math.round(totalRecebimentos) + "]");
		}
		
		if(respVazia){
			double totalRecebimentos = 0d;
			
			if(contasAReceberAux.length > 0){
				for(int j = 0; j < contasAReceberAux.length; j++){
					totalRecebimentos += contasAReceberAux[j].getValorAReceber();						
					respContasRecPag.append(",['" + spf.format(contasAReceberAux[j].getData()) + "', " + "0" + ", " +  totalRecebimentos + "]");
				}
			} else {
				respContasRecPag.append(",['" + spf.format(dataFinal) + "', " + "0" + ", " +  totalRecebimentos + "]");
			}
		}
		
		respVazia = true;
		
		//Lista de Meses que contem valores no ano anterios, deve ser a mesma lista do ano atual
		Map<Date, RelatorioGraficoRecebPagDTO> resultadoList = new HashMap<Date, RelatorioGraficoRecebPagDTO>();
		
		for (RelatorioGraficoRecebPagDTO contasAPagarDTO : contasAPagarList) {
			resultadoList.put(contasAPagarDTO.getData(), contasAPagarDTO);
			totalAPagar += contasAPagarDTO.getValorAPagar();
		}
		
		for (RelatorioGraficoRecebPagDTO contasAReceberDTO : contasAReceberList) {
			
			totalAReceber += contasAReceberDTO.getValorAReceber();
			
			if(resultadoList.containsKey(contasAReceberDTO.getData())){
				double valorAPagar = resultadoList.get(contasAReceberDTO.getData()).getValorAPagar();
				respContasRecPagBar.append(",['" + spf.format(contasAReceberDTO.getData()) + "', " + Math.round(valorAPagar) + ", " +  Math.round(contasAReceberDTO.getValorAReceber()) + "]");
			} else{
				respContasRecPagBar.append(",['" + spf.format(contasAReceberDTO.getData()) + "', " + 0 + ", " +  Math.round(contasAReceberDTO.getValorAReceber()) + "]");
			}
			respVazia = false;
		}
		
		if(respVazia){
			respContasRecPagBar.append(",['" + spf.format(dataFinal) + "', " + 0 + ", " +  "0" + "]");
		}
		
		respContasRecPagPie.append(",[' Total a pagar' , " +  Math.round(totalAPagar) + "]");
		respContasRecPagPie.append(",[' Total a receber' , " +  Math.round(totalAReceber) + "]");
		
		
		// Montando resposta para gráfico de Pagamento por Tipo de Débito		
		for (RelatorioGraficoRecebPagDTO pagamentoPorTipoDebitoDTO : tipoDebitoList) {		
			respTipoDebitoPagPie.append(",['" + pagamentoPorTipoDebitoDTO.getNome() + "', " + Math.round(pagamentoPorTipoDebitoDTO.getValorAPagar()) + "]");			
		}
		
	}
	
	/**
	 * Chama o método executar alterando a dataFinal de 30 para
	 * 7 dias.
	 */
	public void executarHome(){
		 dataFinal = new Date(System.currentTimeMillis() + (7 * 86400000l));
		 executar();
	}

	public  String getrespContasRecPag() {
		return respContasRecPag.toString();
	}

	public void setrespContasRecPag(StringBuilder respContasRecPag) {
		this.respContasRecPag = respContasRecPag;
	}

	public StringBuilder getRespContasRecPagBar() {
		return respContasRecPagBar;
	}

	public void setRespContasRecPagBar(StringBuilder respContasRecPagBar) {
		this.respContasRecPagBar = respContasRecPagBar;
	}

	public StringBuilder getRespContasRecPagPie() {
		return respContasRecPagPie;
	}

	public void setRespContasRecPagPie(StringBuilder respContasRecPagPie) {
		this.respContasRecPagPie = respContasRecPagPie;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public StringBuilder getRespTipoDebitoPagPie() {
		return respTipoDebitoPagPie;
	}

	public void setRespTipoDebitoPagPie(StringBuilder respTipoDebitoPagPie) {
		this.respTipoDebitoPagPie = respTipoDebitoPagPie;
	}
}
