package br.com.gerenciadornet.session;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.gerenciadornet.dto.RelatorioGraficoVendasDTO;

@Name("relatorioGraficoVendasList")
@Scope(ScopeType.CONVERSATION)
public class RelatorioGraficoVendasList{

	private static final long serialVersionUID = 1L;
		
	@In
	EntityManager entityManager;

	// Filtros de pesquisa
	
	private Integer anoAtual			= new GregorianCalendar().get(Calendar.YEAR);
	private Integer anoAnterior			= new GregorianCalendar().get(Calendar.YEAR) - 1;
	private StringBuilder respostaColumnChart	= new StringBuilder("['Mês', 'Total', 'Lucro']");
	private StringBuilder respostaAreaChart 	= new StringBuilder("['Mês', '" + anoAnterior + "', '" + anoAtual + "']");
	private StringBuilder respostaDataTable		= new StringBuilder();
	private StringBuilder respostaDataTable2	= new StringBuilder();
	
	private Integer anoPesquisado;

	/**
	 * Metodo chamado para carregar os dados que irao compor os gráficos.
	 * Chamado de RElatorioGraficoVendas.page.xml 
	 * 
	 * SQL Original do método executar.
	 * 
	 * SELECT vwClienteVendas.mes, vwClienteVendas.ano, sum(vwClienteVendas.valor_total_mes), 
	 * sum(vwClienteVendas.lucro_total_mes)
	 * FROM vw_cliente_vendas vwClienteVendas where ano = 2012 or ano = 2011
	 * group by vwClienteVendas.mes, vwClienteVendas.ano order by vwClienteVendas.ano desc, vwClienteVendas.mes;
	 */
	@SuppressWarnings("unchecked")
	public void executar(){
		
		String [] meses = new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT new br.com.gerenciadornet.dto.RelatorioGraficoVendasDTO(vwClienteVendas.mes, vwClienteVendas.ano, sum(vwClienteVendas.valorTotalMes), sum(vwClienteVendas.lucroTotalMes)) ");
		sql.append("FROM VwClienteVendas vwClienteVendas where ano = :anoAtual or ano = :anoAnterior ");
		sql.append("group by vwClienteVendas.mes, vwClienteVendas.ano order by vwClienteVendas.ano desc, vwClienteVendas.mes");
		
		List <RelatorioGraficoVendasDTO> rel = new ArrayList<RelatorioGraficoVendasDTO>();
		
		if(anoPesquisado == null){
			anoAtual 	= new GregorianCalendar().get(Calendar.YEAR);
			anoAnterior 	= new GregorianCalendar().get(Calendar.YEAR) - 1;
		} else {
			anoAtual 	= anoPesquisado ;
			anoAnterior 	= anoPesquisado - 1;
			respostaAreaChart = new StringBuilder("['Mês', '" + anoAnterior + "', '" + anoAtual + "']");
		}
		
		try {
			rel = (List<RelatorioGraficoVendasDTO>)entityManager
					.createQuery(sql.toString())
					.setParameter("anoAtual", anoAtual)
					.setParameter("anoAnterior", anoAnterior)
					.getResultList();
			
		} catch (Exception e) {
			/*System.out.println("Error:" + e.getMessage());
			System.out.println("Causa:" + e.getCause());
			e.printStackTrace();*/
		}
		
		//Lista de Meses que contem valores no ano atual
		Map<Integer, RelatorioGraficoVendasDTO> listaMesesAnoAtual 	= new HashMap<Integer, RelatorioGraficoVendasDTO>();
		
		//Lista de Meses que contem valores no ano anterios, deve ser a mesma lista do ano atual
		Map<Integer, RelatorioGraficoVendasDTO> listaMesesAnoAnterior = new HashMap<Integer, RelatorioGraficoVendasDTO>();
		
		for (RelatorioGraficoVendasDTO relatorioDTO : rel) {

			//adiciona o total à lista
			if(relatorioDTO.getAno().intValue() == anoAtual.intValue()){
				listaMesesAnoAtual.put(relatorioDTO.getMes(), relatorioDTO);
			}
			
			//adiciona à lista somente os meses que existem no ano atual
			if(relatorioDTO.getAno().intValue() == anoAnterior.intValue()){
				if(listaMesesAnoAtual.containsKey(relatorioDTO.getMes()))
					listaMesesAnoAnterior.put(relatorioDTO.getMes(), relatorioDTO);
			}
			
		}
		
		//DecimalFormat df = new DecimalFormat("#.00");
		DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols (new Locale ("pt", "BR")));  
		df.setMinimumFractionDigits(2);   
		df.setParseBigDecimal (true); 
		
		//Percorrendo os doze meses que podem existir.
		for(int i = 0; i < 12 ; i++ ){

			if(listaMesesAnoAtual.containsKey(i + 1)){
				
				RelatorioGraficoVendasDTO mesAnoAnterior = listaMesesAnoAnterior.get(i + 1);
				if(mesAnoAnterior == null){
					mesAnoAnterior = new RelatorioGraficoVendasDTO(i + 1, anoAnterior, 0, 0);
				}
				
				respostaColumnChart.append(",['" + meses[i] + "', " + Math.round(listaMesesAnoAtual.get(i + 1).getTotal()) + ", " 
						+  Math.round(listaMesesAnoAtual.get(i + 1).getLucro()) + "]");
				
				respostaAreaChart.append(",['" + meses[i] + "', " + Math.round(mesAnoAnterior.getTotal()) + ", " 
						+  Math.round(listaMesesAnoAtual.get(i + 1).getTotal()) + "]");
				
				//Calculo do percentual de lucro sobre o total
				String percentual = df.format(listaMesesAnoAtual.get(i + 1).getLucro() * 100 / listaMesesAnoAtual.get(i + 1).getTotal());
				
				respostaDataTable.append(
						"['" + meses[i] + "', 'R$ " + df.format(new Double(listaMesesAnoAtual.get(i + 1).getTotal())) 
						 + "','R$  " +  df.format(new Double(listaMesesAnoAtual.get(i + 1).getLucro())) + "', '" + percentual + "%'],");
				
				
				String percentual2 = df.format(
						(listaMesesAnoAtual.get(i + 1).getTotal() - mesAnoAnterior.getTotal())
						/mesAnoAnterior.getTotal() * 100);
				
				respostaDataTable2.append(
						"['" + meses[i] + "', 'R$ " + df.format(new Double(mesAnoAnterior.getTotal())) 
						 + "','R$  " +  df.format(new Double(listaMesesAnoAtual.get(i + 1).getTotal())) + "', '" + percentual2 + "%'],");
			} 
		}
		
		//remove a última 'virgula' do respostaDataTable.
		try{
		    	if(!respostaColumnChart.toString().contains("],[")) {
		    	    respostaColumnChart.append(",['', 0, 0]");
		    	}
		    	
		    	if(!respostaAreaChart.toString().contains("],[")) {
		    	    respostaAreaChart.append(",['', 0, 0]");
		    	}
		    
		    	if(respostaDataTable.toString().contains(",")) {
		    	    respostaDataTable  = respostaDataTable.replace(respostaDataTable.lastIndexOf(","), respostaDataTable.lastIndexOf(",") + 1, "");
		    	}else{
		    	    respostaDataTable = new StringBuilder("['Jan', 'R$ 0,00','R$  0,00', '0,00%']");
		    	}
		    	
		    	if(respostaDataTable2.toString().contains(",")) {
		    	    respostaDataTable2 = respostaDataTable2.replace(respostaDataTable2.lastIndexOf(","), respostaDataTable2.lastIndexOf(",") + 1, "");
		    	} else {
		    	    respostaDataTable2 = new StringBuilder("['Jan', 'R$ 0,00','R$  0,00', '0,00%']");
		    	}
			
			
		} catch (Exception e) {
		    /*System.out.println("Message: " + e.getMessage());
		    System.out.println("Causa: " + e.getCause());
		    e.printStackTrace();*/
		}
		
	}

        public List<SelectItem> getListaAnos() {
            
            Integer anoInicial = new GregorianCalendar().get(Calendar.YEAR);
            List<SelectItem> anos = new ArrayList<SelectItem>();
        
            for (int i = anoInicial; i > 2010; i--) {
        	anos.add(new SelectItem(i, ""+ i));
            }
        
            return anos;
        }
	
	public  String getRespostaColumnChart() {
		return respostaColumnChart.toString();
	}

	public void setRespostaColumnChart(StringBuilder respostaColumnChart) {
		this.respostaColumnChart = respostaColumnChart;
	}

	public StringBuilder getRespostaAreaChart() {
		return respostaAreaChart;
	}

	public void setRespostaAreaChart(StringBuilder respostaAreaChart) {
		this.respostaAreaChart = respostaAreaChart;
	}

	public StringBuilder getRespostaDataTable() {
		return respostaDataTable;
	}

	public void setRespostaDataTable(StringBuilder respostaDataTable) {
		this.respostaDataTable = respostaDataTable;
	}

	public Integer getAnoAtual() {
		return anoAtual;
	}

	public void setAnoAtual(Integer anoAtual) {
		this.anoAtual = anoAtual;
	}

	public Integer getAnoAnterior() {
		return anoAnterior;
	}

	public void setAnoAnterior(Integer anoAnterior) {
		this.anoAnterior = anoAnterior;
	}

	public StringBuilder getRespostaDataTable2() {
		return respostaDataTable2;
	}

	public void setRespostaDataTable2(StringBuilder respostaDataTable2) {
		this.respostaDataTable2 = respostaDataTable2;
	}

	public Integer getAnoPesquisado() {
		return anoPesquisado;
	}

	public void setAnoPesquisado(Integer anoPesquisado) {
		this.anoPesquisado = anoPesquisado;
	}
	
}
