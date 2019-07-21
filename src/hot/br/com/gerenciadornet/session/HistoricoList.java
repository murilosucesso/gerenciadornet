package br.com.gerenciadornet.session;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.util.Constantes;

@SuppressWarnings("serial")
@Name("historicoList")
@Scope(ScopeType.CONVERSATION)
public class HistoricoList extends EntityQuery<Historico> {

	private static final String EJBQL = "select historico from Historico historico"
	    	+ " left join fetch historico.usuario";

	private static final String[] RESTRICTIONS = { 
		"historico.usuario.codUsuario = #{historicoList.historico.usuario.codUsuario}",
		"lower(historico.descHistorico) like lower(concat('%', concat(#{historicoList.historico.descHistorico},'%')))",
		"historico.dataHistorico >= #{historicoList.dataInicial}",
		"historico.dataHistorico <= #{historicoList.dataFinal}",
		"historico.usuario.codUsuario <> #{historicoList.codAdministrador}"		
		};
	
	//codigo default de usuario.
	private Integer codAdministrador = new Integer(Constantes.CODIGO_ADMINISTRADOR);
	
	//historico do dia
	private Date dataInicial = new Date();
	private Date dataFinal = new Date();
	List<Historico> historicoList;
	
	//Utilizados para exibicao de resultado da pesquisa
	private boolean mostrarResultados = false;
	private boolean pesquisaVazia;
	private int qtdResultados = 0;
	
	private Historico historico = new Historico();

	public HistoricoList() {		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("historico.dataHistorico");
		setOrderDirection("desc");
		setMaxResults(1000);
	}
	
	public void listarHistorico() {
		
	    setDataFinal(new Date(dataFinal.getTime() + 86400000l));
	    List<Historico> historicos = super.getResultList();
	    qtdResultados = historicos.size();
        
            if(historicos == null || qtdResultados == 0){
            	setPesquisaVazia(true);
            } else {
            	setPesquisaVazia(false);
            }
    		
    	    setHistoricoList(historicos);
    	}
		
	public Historico getHistorico() {
		return historico;
	}
	
	public Integer getCodAdministrador() {
		return codAdministrador;
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

	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public boolean isPesquisaVazia() {
		return pesquisaVazia;
	}

	public void setPesquisaVazia(boolean pesquisaVazia) {
		this.pesquisaVazia = pesquisaVazia;
	}

	public int getQtdResultados() {
		return qtdResultados;
	}

	public void setQtdResultados(int qtdResultados) {
		this.qtdResultados = qtdResultados;
	}

	public List<Historico> getHistoricoList() {
	    return historicoList;
	}

	public void setHistoricoList(List<Historico> novoHistoricoList) {
	    historicoList = novoHistoricoList;
	}

}
