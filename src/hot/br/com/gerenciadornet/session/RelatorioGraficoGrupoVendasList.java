package br.com.gerenciadornet.session;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import br.com.gerenciadornet.dto.RelatorioGraficoVendasDTO;
import br.com.gerenciadornet.entity.Grupo;

@Name("relatorioGraficoGrupoVendasList")
public class RelatorioGraficoGrupoVendasList{

	private static final long serialVersionUID = 1L;
		
	@In
	EntityManager entityManager;
	
	@In(create=true)
	GrupoList grupoList;

	// Filtros de pesquisa
	
	private Integer anoAtual					= new GregorianCalendar().get(Calendar.YEAR);
	private Integer anoAnterior					= new GregorianCalendar().get(Calendar.YEAR) - 1;
	private StringBuilder respostaColumnChart	= new StringBuilder();
	private StringBuilder respostaPieChart 		= new StringBuilder();
	
	private StringBuilder respostaColumnChart2	= new StringBuilder();
	private StringBuilder respostaPieChart2		= new StringBuilder();
	
	private int numeroGrupos 					= 0;
	
	/**
	 * Metodo chamado para carregar os dados que irao compor os gráficos.
	 * Chamado de RelatorioGraficoGrupoVendas.page.xml 
	 * 
	 * SQL Original do método executar.
	 * 
	 * SELECT vwClienteVendas.ano, vwClienteVendas.mes, vwClienteVendas.codigoGrupo, vwClienteVendas.grupo,
	 * count(vwClienteVendas.cod_cliente) as totalVendas,
	 * sum(vwClienteVendas.valor_total_mes) as valorTotalMes,
	 * sum(vwClienteVendas.lucro_total_mes) as lucroTotalMes
	 * FROM vw_cliente_vendas vwClienteVendas
	 * where vwClienteVendas.ano = 2013 or vwClienteVendas.ano = 2012
	 * group by vwClienteVendas.codigoGrupo, vwClienteVendas.mes, vwClienteVendas.ano
	 * order by vwClienteVendas.ano desc, vwClienteVendas.mes;
	 */
	@SuppressWarnings("unchecked")
	public void executar(){
		
		String [] meses = new String[]{"Mes", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
		
		StringBuilder sql = new StringBuilder();
		
		try{
			
		sql.append("SELECT new br.com.gerenciadornet.dto.RelatorioGraficoVendasDTO(");
		sql.append("vwClienteVendas.mes, ");//mes
		sql.append("vwClienteVendas.ano, ");//ano
		sql.append("sum(vwClienteVendas.valorTotalMes), ");//valorTotalMes
		sql.append("sum(vwClienteVendas.lucroTotalMes), ");//lucroTotalMes
		sql.append("count(vwClienteVendas.codigoCliente), ");//totalVendas
		sql.append("vwClienteVendas.codigoGrupo, ");//codigoGrupo
		sql.append("vwClienteVendas.grupo) ");//Grupo
		sql.append("FROM VwClienteVendas vwClienteVendas WHERE ano = :anoAtual or ano = :anoAnterior ");
		sql.append("group by vwClienteVendas.codigoGrupo, vwClienteVendas.mes, vwClienteVendas.ano ");
		sql.append("order by vwClienteVendas.ano desc, vwClienteVendas.mes");
		
		List <RelatorioGraficoVendasDTO> rel = new ArrayList<RelatorioGraficoVendasDTO>();
		
		rel = (List<RelatorioGraficoVendasDTO>)entityManager
				.createQuery(sql.toString())
				.setParameter("anoAtual", anoAtual)
				.setParameter("anoAnterior", anoAnterior)
				.getResultList();
			
		List<Grupo> listaGrupos = grupoList.getResultList();
			
		//Array com 12 meses por N Grupos + 1 (Os clientes que não pertencem a nenhum Grupo(Carteira))
		String [][] anoAtualArray 		= new String[13][listaGrupos.size() + 1];
		String [][] anoAnteriorArray 	= new String[13][listaGrupos.size() + 1];
		
		//Percorre a lista de grupos e monta um Array
		Grupo[] grupos = new Grupo[listaGrupos.size() + 1];
		Grupo grupo = new Grupo(0, "Sem Grupo(Carteira)");
		grupos[0] = grupo;
		
		int cont = 1;
		for(Grupo g :listaGrupos){
			grupos[cont]=g;
			cont++;
		}
		
		numeroGrupos = grupos.length;
		
		//Zera todas as casas para iniciar
		for(int i = 0; i <= 12 ; i++ ){
			for(int j = 0; j < grupos.length; j++){
				anoAtualArray[i][j] 	= "0";
				anoAnteriorArray[i][j]	= "0";
			}
		}
		
		//Array para descobrir a possição no array a partir do código do grupo.
		Map<Integer, Integer> grupoPossicaoArray = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < grupos.length ; i++){
			//System.out.println(" | " + grupos[i].getCodGrupo() + "|" + grupos[i].getNomeGrupo() + "|");
			grupoPossicaoArray.put(grupos[i].getCodGrupo(), i);
		}
		
		//Preenche a primeira linha do array com os nomes dos Grupos
		for(int j = 0; j < grupos.length; j++){
			anoAtualArray[0][j] 	= grupos[j].getNomeGrupo();
			anoAnteriorArray[0][j] 	= grupos[j].getNomeGrupo();
		}
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		//Percorrer o resultado da pesquisa preenchendo o array
		for (RelatorioGraficoVendasDTO relatorioDTO : rel) {
			
			if(relatorioDTO.getAno().intValue() == anoAtual.intValue()){
				anoAtualArray[relatorioDTO.getMes()][ grupoPossicaoArray.get(relatorioDTO.getCodigoGrupo()) ] = df.format((relatorioDTO.getTotal())).replace(",", ".");
			} else {
				anoAnteriorArray[relatorioDTO.getMes()][ grupoPossicaoArray.get(relatorioDTO.getCodigoGrupo()) ] = df.format((relatorioDTO.getTotal())).replace(",", ".");
			}
		}
		
		//Array total Anual
		float[] totaisAnualGrupo 	= new float[grupos.length];
		float[] totaisAnteriorGrupo = new float[grupos.length]; 
		
		
		//Percorrendo o array final para montar a String com valores para o gráfico de colunas
		for(int i = 0; i <= 12 ; i++ ){
			
			respostaColumnChart.append("['" + meses[i] + "'");
			respostaColumnChart2.append("['" + meses[i] + "'");
			
			for(int j = 0; j < grupos.length; j++){
				
				if( i == 0 ){
					
					respostaColumnChart.append(",'"+ anoAtualArray[i][j] + "'");
					respostaColumnChart2.append(",'"+ anoAnteriorArray[i][j] + "'");
					
				} else {
					//Add de valores no ano atual
					respostaColumnChart.append(" ,"+ anoAtualArray[i][j]);
					totaisAnualGrupo[j] += Float.parseFloat(anoAtualArray[i][j]);
					
					//Add de valores no ano anterior
					respostaColumnChart2.append(" ,"+ anoAnteriorArray[i][j]);
					totaisAnteriorGrupo[j] += Float.parseFloat(anoAnteriorArray[i][j]);
				}
				
				//se for o último tem que fechar o colchetes
				if(j == grupos.length -1 ){
					respostaColumnChart.append("],\n");
					respostaColumnChart2.append("],\n");
				}
			}
		}
		
		//----------------- Montar dados Grafico Pizza --------------------------
		respostaPieChart.append("['Grupo(Carteira)' , 'Total']\n");
		respostaPieChart2.append("['Grupo(Carteira)' , 'Total']\n");
		
		for(int k = 0; k < grupos.length ; k++){
			respostaPieChart.append(", ['"+ grupos[k].getNomeGrupo()+ "', " + totaisAnualGrupo[k] + " ]\n");
			respostaPieChart2.append(", ['"+ grupos[k].getNomeGrupo()+ "', " + totaisAnteriorGrupo[k] + " ]\n");
		}
		
		
		//remove a última 'virgula' do respostaColumnChart.
		respostaColumnChart  = respostaColumnChart.replace(respostaColumnChart.lastIndexOf(","), respostaColumnChart.lastIndexOf(",") + 1, "");
		respostaColumnChart2  = respostaColumnChart2.replace(respostaColumnChart2.lastIndexOf(","), respostaColumnChart2.lastIndexOf(",") + 1, "");
			
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
			System.out.println("Causa:" + e.getCause());
			e.printStackTrace();
		}
		
		/*System.out.println("\n\n ------------------------- RESPOSTA GRAFICO COLUNAS 1-------------------------------------");
		System.out.println(respostaColumnChart);
		System.out.println("\n\n ------------------------- RESPOSTA GRAFICO PIZZA 1-------------------------------------");
		System.out.println(respostaPieChart);
		
		System.out.println("\n\n ------------------------- RESPOSTA GRAFICO COLUNAS 2-------------------------------------");
		System.out.println(respostaColumnChart2);
		System.out.println("\n\n ------------------------- RESPOSTA GRAFICO PIZZA 2-------------------------------------");
		System.out.println(respostaPieChart2);*/
		
	}

	public  String getRespostaColumnChart() {
		return respostaColumnChart.toString();
	}

	public void setRespostaColumnChart(StringBuilder respostaColumnChart) {
		this.respostaColumnChart = respostaColumnChart;
	}

	public StringBuilder getrespostaPieChart() {
		return respostaPieChart;
	}

	public void setrespostaPieChart(StringBuilder respostaPieChart) {
		this.respostaPieChart = respostaPieChart;
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

	public int getNumeroGrupos() {
		return numeroGrupos;
	}

	public StringBuilder getRespostaColumnChart2() {
		return respostaColumnChart2;
	}

	public void setRespostaColumnChart2(StringBuilder respostaColumnChart2) {
		this.respostaColumnChart2 = respostaColumnChart2;
	}

	public StringBuilder getRespostaPieChart2() {
		return respostaPieChart2;
	}

	public void setRespostaPieChart2(StringBuilder respostaPieChart2) {
		this.respostaPieChart2 = respostaPieChart2;
	}
}
