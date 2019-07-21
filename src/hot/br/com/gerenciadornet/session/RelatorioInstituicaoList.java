package br.com.gerenciadornet.session;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.RelatorioInstituicaoListDTO;
import br.com.gerenciadornet.entity.Entidade;
import br.com.gerenciadornet.entity.Grupo;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;

@SuppressWarnings("serial")
@Name("relatorioInstituicaoList")
@Scope(ScopeType.PAGE)
public class RelatorioInstituicaoList extends EntityQuery<RelatorioInstituicaoListDTO> {

	private static final String EJBQL = "select * from venda";
	
	//private static final String EJBQL = "select new br.com.gerenciadornet.dto.RelatorioInstituicaoListDTO( venda.cliente.nome, venda.cliente.telefone, sum(venda.valorTotalVenda), count(venda.codVenda)) from Venda venda";

	/*private static String[] RESTRICTIONS = {
			"venda.cliente.entidade.codEntidade = #{relatorioInstituicaoList.entidade.codEntidade}",//Entidade (Ex: Univ. UCB)
			"venda.inOrcamento = #{relatorioInstituicaoList.inOrcamento}",	// inOrcamento = false (Venda)
			"venda.statusVenda.codStatusVenda <> #{relatorioInstituicaoList.statusVendaCancela}",//vendas não canceladas
			"venda.dataInicioVenda >= #{relatorioInstituicaoList.dataInicioVenda}",
			"venda.dataInicioVenda <= #{relatorioInstituicaoList.dataFimVenda}"
			
			};

	*/
	private Date 		dataInicioVenda 			= new Date(System.currentTimeMillis());	
	private Date 		dataFimVenda 				= new Date(System.currentTimeMillis());	
	private Boolean		inOrcamento 				= false;
	private Boolean 	resultadoIsNull 			= true;	
	private Integer		statusVendaCancela 			= Constantes.CANCELADA;
	private Integer 	statusVendaOrcamento		= Constantes.ORCAMENTO;
	private Integer 	numeroLinhasDT 				= 15;
	private boolean 	mostrarResultados 			= false;
	private Entidade 	entidade					= new Entidade();
	private float 		valorTotal;
	private int 		qtdTotal;
	private Boolean		msnListaVazia				= true;
	private Grupo		grupoCarteira				= new Grupo();
	
	private StringBuilder respostaScatterChart;
	
	@In(scope=ScopeType.SESSION, required=false)
	Usuario user;
	
	@In
	Constantes constantes;
	
	@In
	EntityManager entityManager;
			
	private Venda venda = new Venda(new Date(System.currentTimeMillis()), 
			new Date(System.currentTimeMillis()), null);
	
	public RelatorioInstituicaoList() {
		setEjbql(EJBQL);
		//setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setGroupBy("venda.cliente.codCliente");
		//setOrderColumn("venda.cliente.nome");
	}
	
	
	DecimalFormat df = new DecimalFormat("###.##");
	
	public List<RelatorioInstituicaoListDTO> listaRelatorioInstituicaoListDTO;
	
	public void listarVendasInstituicao() {
		
		respostaScatterChart	= new StringBuilder();

		Integer statusVendaCancela = Constantes.CANCELADA;
		Integer statusVendaOrcamento = Constantes.ORCAMENTO;

		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.dto.RelatorioInstituicaoListDTO( c.nome, c.telefone, sum(v.valorTotalVenda), count(v.codVenda)) ");
		sql.append("from Venda v, Cliente c where v.cliente.codCliente = c.codCliente ");
		
		// Para pesquisar vendas que nao forma canceladas, nem orcamentos
		sql.append("and v.statusVenda.codStatusVenda <> :statusVendaCancela ");
		sql.append("and v.statusVenda.codStatusVenda <> :statusVendaOrcamento ");
		sql.append("and v.inOrcamento = :indicadorOrcamento ");
		
		if(entidade != null && entidade.getCodEntidade() != null){
			sql.append("and c.entidade.codEntidade = :entidade ");
		}
		if(grupoCarteira != null && grupoCarteira.getCodGrupo() != null){
			sql.append("and c.grupo.codGrupo = :grupo ");
		}
		
		sql.append("and v.dataInicioVenda >= :dataInicio ");
		sql.append("and v.dataInicioVenda <= :dataFim ");
		sql.append("group by c.codCliente ");
		sql.append("order by c.nome");
		
		Query query = entityManager.createQuery(sql.toString());
		
		query.setParameter("statusVendaCancela", statusVendaCancela);
		query.setParameter("statusVendaOrcamento", statusVendaOrcamento);
		query.setParameter("indicadorOrcamento", inOrcamento);
		
		if(entidade != null && entidade.getCodEntidade() != null){
			query.setParameter("entidade",entidade.getCodEntidade());
		}
		if(grupoCarteira != null && grupoCarteira.getCodGrupo() != null){
			query.setParameter("grupo", grupoCarteira.getCodGrupo());
		}
		query.setParameter("dataInicio", getDataInicioVenda());
		query.setParameter("dataFim", getDataFimVenda());;
				
		@SuppressWarnings("unchecked")
		List<RelatorioInstituicaoListDTO> relatIntList = (List<RelatorioInstituicaoListDTO>) query.getResultList();

		valorTotal 	= 0;
		qtdTotal 	= 0;
		
		for (RelatorioInstituicaoListDTO dto : relatIntList) {
			valorTotal += dto.getValorVendasTotal();
			qtdTotal += dto.getQtdVendasTotal();
			
			//monta a string utilizada no gráfico
			respostaScatterChart.append("[" + dto.getQtdVendasTotal() +  " , " + df.format( dto.getValorVendasTotal() ).replace(",", ".") + " , '" + "R$ " + df.format( dto.getValorVendasTotal() ) + " - " + dto.getNomeCliente() +  "'],\n");
			
		}

		//remove a última vírgula da string.
		if(respostaScatterChart.indexOf(",") > 0){
			respostaScatterChart = respostaScatterChart.deleteCharAt(respostaScatterChart.length() - 1);
		}
		
		if(relatIntList.size() == 0 ){
			msnListaVazia = true;
		} else {
			msnListaVazia = false;
		}

		listaRelatorioInstituicaoListDTO = relatIntList;
	}
	
	
	public Venda getVenda() {
		return venda;
	}

	public Boolean getInOrcamento() {
		return inOrcamento;
	}
	
	@Override
	public List<RelatorioInstituicaoListDTO> getResultList() {
		return super.getResultList();
	}
	
	/**
	 * adiciona um dia na pesquisa final para a data fim ser incluida na pesquisa
	 */
	public Date getDataFimVenda(){		
		if(venda.getDataFimVenda() != null){
			
			return new Date(venda.getDataFimVenda().getTime() + 86400000l);
		}
		return dataFimVenda;		
	}

	public Date getDataInicioVenda(){		
		return dataInicioVenda;		
	}


	public Boolean getResultadoIsNull() {
		return resultadoIsNull;
	}


	public void setResultadoIsNull(Boolean resultadoIsNull) {
		this.resultadoIsNull = resultadoIsNull;
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


	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}


	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}


	public boolean isMostrarResultados() {
		return mostrarResultados;
	}


	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}


	public Entidade getEntidade() {
		return entidade;
	}


	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}


	public Usuario getUser() {
		return user;
	}


	public void setUser(Usuario user) {
		this.user = user;
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	public void setDataInicioVenda(Date dataInicioVenda) {
		this.dataInicioVenda = dataInicioVenda;
	}


	public void setDataFimVenda(Date dataFimVenda) {
		this.dataFimVenda = dataFimVenda;
	}


	public void setInOrcamento(Boolean inOrcamento) {
		this.inOrcamento = inOrcamento;
	}


	public void setVenda(Venda venda) {
		this.venda = venda;
	}


	public List<RelatorioInstituicaoListDTO> getListaRelatorioInstituicaoListDTO() {
		return listaRelatorioInstituicaoListDTO;
	}


	public void setListaRelatorioInstituicaoListDTO(
			List<RelatorioInstituicaoListDTO> listaRelatorioInstituicaoListDTO) {
		this.listaRelatorioInstituicaoListDTO = listaRelatorioInstituicaoListDTO;
	}


	public float getValorTotal() {
		return valorTotal;
	}


	public int getQtdTotal() {
		return qtdTotal;
	}


	public Boolean getMsnListaVazia() {
		return msnListaVazia;
	}


	public StringBuilder getRespostaScatterChart() {
		return respostaScatterChart;
	}


	public Grupo getGrupoCarteira() {
		return grupoCarteira;
	}


	public void setGrupoCarteira(Grupo grupoCarteira) {
		this.grupoCarteira = grupoCarteira;
	}
	
}
