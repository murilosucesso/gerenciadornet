package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Atendimento;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("atendimentoList")
public class AtendimentoList extends EntityQuery<Atendimento> {
	
	private static final String EJBQL = "select  new br.com.gerenciadornet.entity.Atendimento("
			+ " atendimento.codAtendimento,"
			+ " atendimento.codTipoAtendimento,"
			+ " atendimento.dataAtendimento,"
			+ " atendimento.descAtendimento,"
			+ " atendimento.statusAtendimento,"
			+ " atendimento.inObtencaoExito,"
			+ " atendimento.cliente.nome,"
			+ " atendimento.cliente.telefone,"
			+ " atendimento.usuario.nomeUsuario"			
			+ ") from Atendimento atendimento"
			+ " left join atendimento.usuario"
			+ " left join atendimento.cliente";
	
	private static final String[] RESTRICTIONS = {
		"atendimento.codAtendimento = #{atendimentoList.atendimento.codAtendimento}",
		};

	
	private static final String[] RESTRICTIONS2 = {
			"atendimento.cliente.codCliente 	= #{atendimentoList.atendimento.cliente.codCliente}",
			"atendimento.cliente.cpfCnpj 		= #{atendimentoList.atendimento.cliente.cpfCnpj}",
			"atendimento.cliente.telefone		= #{atendimentoList.atendimento.cliente.telefone}", 
			"lower(atendimento.cliente.nome) like lower(concat('%', concat(#{atendimentoList.atendimento.cliente.nome},'%')))",
			"atendimento.codTipoAtendimento 	= #{atendimentoList.atendimento.codTipoAtendimento}", 
			"atendimento.usuario.codUsuario 	= #{atendimentoList.atendimento.usuario.codUsuario}",
			"atendimento.inObtencaoExito 		= #{atendimentoList.obtencaoExito} ",
			"atendimento.dataAtendimento 		>=#{atendimentoList.dataInicio}",
			"atendimento.dataAtendimento 		< #{atendimentoList.dataFim}"
			};
	

	private Atendimento atendimento = new Atendimento();
	
	List<Atendimento> atendimentosInadimplentesList = new ArrayList<Atendimento>();
	
	private Date dataInicio = new Date();
	private Date dataFim = new Date();
	private boolean mostrarResultados = false;
	private Integer numeroLinhasDT 	= 15;
	private Boolean obtencaoExito = null;
	

	/**
	 * Altera a pesquisa caso seja pesq. por código Atendimento, nao pode ficar
	 * dentro do resultList senao recarrega, executa a pesq. varias vezes,
	 * sem necessidade.
	 */
	public void setRestriction(){
		
	    if(this.atendimento.getCodAtendimento() == null){
	    	setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		
			if(this.atendimento.getCliente().getTelefone() != null && "(  )    -    ".trim().equals(this.atendimento.getCliente().getTelefone().trim())){
			    this.atendimento.getCliente().setTelefone(null);
			}
			
	    }else{
	    	setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	    	setOrderColumn(null);
	    }
	}
	
	@Override
	public List<Atendimento> getResultList() {
						
		return super.getResultList();
	}
	
	/**
	 * Botao Limpar.
	 * Limpar o campo telefone pois nao apagava com o reset
	 * por se tratar de easyfaces.
	 */
	public void limpar() {
	    this.atendimento.getCliente().setTelefone(null);
	}
	
	public boolean isMostrarResultados()
	{
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados)
	{
		this.mostrarResultados = mostrarResultados;
	}

	
	public AtendimentoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		setOrderColumn("atendimento.codAtendimento");
	}
		
	
	public Atendimento getAtendimento() {
		return atendimento;
	}
	
	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim != null) {
			return new Date(dataFim.getTime() + 86400000l);
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Boolean getObtencaoExito() {
		return obtencaoExito;
	}

	public void setObtencaoExito(Boolean obtencaoExito) {
		this.obtencaoExito = obtencaoExito;
	}
}
