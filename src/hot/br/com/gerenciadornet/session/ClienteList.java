package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Grupo;
import br.com.gerenciadornet.entity.SituacaoVendas;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Constantes;
import br.com.gerenciadornet.util.Util;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("clienteList")
public class ClienteList extends EntityQuery<Cliente> {

	private static final String EJBQL = "select cliente from Cliente cliente";

	private static String[] RESTRICTIONS = {
        "cliente.codCliente = #{clienteList.cliente.codCliente}", 
        };
	
	private static final String[] RESTRICTIONS2 = {
			"lower(cliente.nome) like lower(concat('%', concat(#{clienteList.cliente.nome},'%')))",
			"lower(cliente.cpfCnpj) like lower( concat('%', concat(#{clienteList.cliente.cpfCnpj},'%')))",
			"cliente.especialidade = #{clienteList.cliente.especialidade}", 
			"cliente.telefone = #{clienteList.cliente.telefone}", 
			"cliente.entidade.codEntidade = #{clienteList.cliente.entidade.codEntidade}",
			"lower(cliente.email) like lower( concat('%',concat(#{clienteList.cliente.email},'%')))",
			"lower(cliente.observacao) like lower( concat('%',concat(#{clienteList.cliente.observacao},'%')))", 
			"cliente.usuario.codUsuario = #{clienteList.cliente.usuario.codUsuario}",
			"cliente.grupo.codGrupo = #{clienteList.cliente.grupo.codGrupo}",
			"cliente.inAdimplente = #{clienteList.situacaoFinanceira} ",
			"cliente.inExclusao = #{clienteList.cliente.inExclusao} ",
			"lower(cliente.nome) like lower(concat('%', concat(#{clienteList.pesquisaGeral},'%')))",
			"lower(cliente.nome) like lower(concat('%', concat(#{clienteList.cliente.nome},'%')))",
			"cliente.telefone = #{pesquisaGeral}", 
			};
	
	private static final String[] RESTRICTIONS3 = {
		"lower(cliente.nome) like lower(concat('%', concat(#{clienteList.pesquisaGeral},'%')))",
		"cliente.telefone  like concat('%', concat(#{clienteList.pesquisaGeral},'%'))",
		"cast(cliente.codCliente as string) like #{clienteList.pesquisaGeral}", 
		};

	private Cliente cliente = new Cliente("", false);

	private boolean mostrarResultados = false;
	
	private Boolean situacaoFinanceira;
	
	private Integer numeroLinhasDT 	= 15;
	
	private String pesquisaGeral;
	
	public boolean isMostrarResultados()
	{
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados)
	{
		this.mostrarResultados = mostrarResultados;
	}

	/**
	 * Altera a pesquisa caso seja pesq. por códigoVenda, nao pode ficar
	 * dentro do resultList senao recarrega, executa a pesq. varias vezes,
	 * sem necessidade.
	 */
	public void setRestriction(){
		
	    if(this.cliente.getCodCliente() == null){
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		
		if(this.cliente.getTelefone() != null && "(  )    -    ".trim().equals(this.cliente.getTelefone().trim())){
		    this.cliente.setTelefone(null);
		}
	    }else{
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn(null);
	    }
	}
	
	public ClienteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		setOrderColumn("cliente.nome");
	}
	
	List<Cliente> clienteList;
	
	/**
	 * Metodo utilizado para preencher combos de clientes.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Cliente> getResultListCombo(){
		
		//Para evitar múltiplas consultas
		if(clienteList != null){
			return clienteList; 
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.entity.Cliente(c.codCliente, c.nome) ");
		sql.append("from Cliente c ");
		sql.append("where c.inExclusao = :inExclusao ");
		sql.append("order by c.nome");
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("inExclusao", false);
		
		clienteList = (List<Cliente>)query.getResultList();
		return clienteList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> getResultListComboFull(){
		
		//Para evitar múltiplas consultas
		if(clienteList != null){
			return clienteList; 
		} 
		
		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.entity.Cliente( ");
		sql.append("c.codCliente, c.nome, c.grupo.nomeGrupo, c.inAdimplente, c.diaCobrancaPagamento) ");
		sql.append("from Cliente c left outer join c.grupo ");
		sql.append("where c.inExclusao = :inExclusao ");
		sql.append("order by c.nome");
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("inExclusao", false);
		
		clienteList = (List<Cliente>)query.getResultList();
		return clienteList;
	}
	
	public List<Cliente> getResultList2(){
				
		List<Cliente> clientes = super.getResultList();

		StringBuilder sql = new StringBuilder(
				"select min(venda.dataInicioVenda) from Venda venda WHERE " +
				"cod_cliente = :cod " +
				"and cod_status_venda <> :situacao1 " +
				"and cod_status_venda <> :situacao2 " +
				"and cod_status_venda <> :situacao3 "
				);
		
		for (Cliente cliente : clientes){
			
			Query query = getEntityManager().createQuery(sql.toString());
			query.setParameter("cod", cliente.getCodCliente());
			query.setParameter("situacao1", Constantes.CONCLUIDA);
			query.setParameter("situacao2", Constantes.CANCELADA);
			query.setParameter("situacao3", Constantes.ORCAMENTO);
			
			Date dataVenda = (Date)query.getSingleResult();
			
			if(dataVenda != null){
				Date dataAtual = new Date(System.currentTimeMillis());
				
				double diasEmAberto = Util.diferencaEmDias(dataVenda, dataAtual);
				 
				if(diasEmAberto <= 30){
					cliente.setSituacaoFinanceira(SituacaoVendas.ABERTO_30);
					
				} else if (diasEmAberto <= 60){
					cliente.setSituacaoFinanceira(SituacaoVendas.ABERTO_60);
					
				} else if (diasEmAberto <= 90){
					cliente.setSituacaoFinanceira(SituacaoVendas.ABERTO_90);
					
				} else {
					cliente.setSituacaoFinanceira(SituacaoVendas.ABERTO_Mais_90);
				}
			} else {
				if(cliente.getVendas() == null || cliente.getVendas().size() == 0){
					cliente.setSituacaoFinanceira(SituacaoVendas.NENHUMA_VENDA);
				}else{
					cliente.setSituacaoFinanceira(SituacaoVendas.EM_DIA);
				}
					
			}
		}
		return clientes;
	}
	
	/**
	 * Método utilizado para mostar a data da última compra do cliente
	 * na popup de detalhes do cliente na tela de venda.
	 * 
	 * @param codCliente
	 * @return
	 */
	public Date getDataUltimaVenda(Integer codCliente){
		
		if(codCliente == null || codCliente == 0){
			return null;
		}
		
		Date dataUltimaVenda = null;
		
		StringBuilder sql = new StringBuilder(
				"select max(venda.dataInicioVenda) from Venda venda WHERE " +
				"cod_cliente = :codCliente " +
				"and cod_status_venda <> :situacao1 " +
				"and cod_status_venda <> :situacao2 " +
				"and cod_status_venda <> :situacao3 " +
				"and cod_status_venda <> :situacao4 "
				);

		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("codCliente", codCliente);
		query.setParameter("situacao1", Constantes.CANCELADA);
		query.setParameter("situacao2", Constantes.ORCAMENTO);
		query.setParameter("situacao3", Constantes.PEDIDO);
		query.setParameter("situacao4", Constantes.PEDIDO_AUTORIZADO);
		
		try {
			dataUltimaVenda = (Date)query.getSingleResult();
				
		} catch (NoResultException e) {
			dataUltimaVenda = null;
		}
		
		return dataUltimaVenda;
	}
	
	
	@In 
	Usuario user;
	boolean consultaJaRealizada = false; // utilizado para evitar múltiplas chamdas do mesmo método.
	List<Cliente> clientesInadimplentesList = new ArrayList<Cliente>();
	/**
	 * Retorna a lista de clientes inadimplentes para serem exibidos
	 * na tela inicial do vendedor.
	 * @return
	 */
	public  List<Cliente> listarClientesInadiplentes(){

		//false para inadimplentes
		setSituacaoFinanceira(false);
		cliente.setUsuario(user);
		
		if(consultaJaRealizada){
			return clientesInadimplentesList;
		}
			
		clientesInadimplentesList = super.getResultList();
		consultaJaRealizada =  true;
				
		return clientesInadimplentesList;
	} 
	
	@Override
	public List<Cliente> getResultList() {
		if(pesquisaGeral != null){
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS3));
			setRestrictionLogicOperator("or");
		}
		return super.getResultList();
	}
	
	/**
	 * Utilizado para pesquisa geral de clientes em Nova Venda
	 * @return
	 */
	public List<Cliente> getResultListPopUp() {
		if(pesquisaGeral != null){
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS3));
			setRestrictionLogicOperator("or");
			return super.getResultList();
		}
		return null;
	}
		
	/**
	 * Botao Limpar.
	 * Limpar todos os campos depois de setados
	 * na conversation.
	 */
	public void limpar() {
		this.cliente.setCodCliente(null);
		this.cliente.setCpfCnpj(null);
		this.cliente.setNome(null);
		this.cliente.setEmail(null);
		this.cliente.setObservacao(null);
	    this.cliente.setTelefone(null);
	    this.cliente.setUsuario(new Usuario());//vendedor
	    this.cliente.setSituacaoFinanceira(null);
	    this.cliente.setGrupo(new Grupo());
	    setMostrarResultados(false);// para não listar todos assim que limpar
	}


	public Cliente getCliente() {
		return cliente;
	}

	public Boolean getSituacaoFinanceira() {
		return situacaoFinanceira;
	}

	public void setSituacaoFinanceira(Boolean situacaoFinanceira) {
		this.situacaoFinanceira = situacaoFinanceira;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}	
	
	public String getPesquisaGeral()
	{
		return pesquisaGeral;
	}

	public void setPesquisaGeral(String pesquisaGeral)
	{
		this.pesquisaGeral = pesquisaGeral;
	}
}
