package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Util;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("clienteGestaoList")
public class ClienteGestaoList extends EntityQuery<Cliente> {

	private static final String EJBQL = "select cliente from Cliente cliente";
	
	private static final String[] RESTRICTIONS = {
			"cliente.entidade.codEntidade = #{clienteGestaoList.cliente.entidade.codEntidade}",
			"cliente.usuario.codUsuario = #{clienteGestaoList.cliente.usuario.codUsuario}",
			"cliente.grupo.codGrupo = #{clienteGestaoList.cliente.grupo.codGrupo}",
			"cliente.inExclusao = #{clienteGestaoList.cliente.inExclusao} "
			};
	
	
	private Cliente cliente = new Cliente("", false);

	private boolean mostrarResultados = false;
	
	@In 
	Usuario user;
	
	boolean consultaJaRealizada = false; // utilizado para evitar múltiplas chamdas do mesmo método.
	
	private Integer numeroLinhasDT 	= 15;
	
	private Date dataInicioVenda = new Date(System.currentTimeMillis());
	
	public boolean isMostrarResultados()
	{
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados)
	{
		this.mostrarResultados = mostrarResultados;
	}
	
	public ClienteGestaoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("cliente.nome");
	}
	
	List<Cliente> clientesList = new ArrayList<Cliente>();
	
	/**
	 * Retorna uma lista de clientes de acordo com os parâmetros passados.
	 */
	public List<Cliente> getResultList(){
		
		if(consultaJaRealizada){
			return clientesList;
		}
		
		clientesList = listarClientesPorData();
		
		Date dataAtual = new Date(System.currentTimeMillis());
		
		for(Cliente cliente : clientesList){
			Date dataUltimaVenda = cliente.getDtUltimaVenda();
			
			if(dataUltimaVenda == null){
				cliente.setObservacao("Sem Vendas");
			} else {
				cliente.setObservacao((int)Util.diferencaEmDias(cliente.getDtUltimaVenda(), dataAtual) + "");
			}
		}
		
		consultaJaRealizada = true;
		
		return clientesList;
	}
	
	/**
	 * Lista todos os clientes que não possuem venda a partir de uma
	 * determinada data.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Cliente> listarClientesPorData(){
		
		StringBuilder sql = new StringBuilder();
		
		//seleciona os clientes que não estão no subselect, ou seja, os que não compraram
		sql.append(" select new br.com.gerenciadornet.entity.Cliente( ");
				
		sql.append( "c.codCliente, c.nome, c.telefone, c.email, c.dtCadastro, ");
		sql.append( "MAX(v.dataInicioVenda), c.usuario.nomeUsuario) ");
		sql.append( "FROM Cliente c, Venda v, Usuario u ");
		sql.append( "WHERE c.usuario.codUsuario = u.codUsuario ");
		sql.append( "AND v.cliente.codCliente = c.codCliente ");
		
		sql.append( "AND c.inAdimplente = :inAdimplente ");
		sql.append( "AND c.inExclusao = :inExclusao ");
		
		sql.append( "AND v.dataInicioVenda <=  :dataInicioVenda ");
		sql.append( "AND c.codCliente NOT IN ( ");
		
		//Seleciona os clientes que compraram a partir da data informada 
		sql.append( "SELECT c.codCliente FROM Cliente c, Venda v ");
		sql.append( "WHERE v.cliente.codCliente = c.codCliente ");
		sql.append( "AND v.dataInicioVenda > :dataInicioVenda1 ");
		sql.append( "and c.inAdimplente = :inAdimplente1 ");
		sql.append( "AND c.inExclusao = :inExclusao1 ) ");
		
		
		if(cliente.getUsuario() != null){
			sql.append( "AND c.usuario.codUsuario =  :codUsuarioResponsavel ");
		}
		
		if(cliente.getEntidade() != null){
			sql.append( "AND c.entidade.codEntidade =  :codEntidade ");
		}
		
		if(cliente.getGrupo() != null){
			sql.append( "AND c.grupo.codGrupo =  :codGrupo ");
		}
		
		sql.append( "group by c.codCliente order by c.nome");
				
	
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("inAdimplente", true);
		query.setParameter("inAdimplente", true);
		query.setParameter("inExclusao", false);
		query.setParameter("dataInicioVenda", getDataInicioVenda());
		query.setParameter("dataInicioVenda1", getDataInicioVenda());
		query.setParameter("inAdimplente1", true);
		query.setParameter("inExclusao1", false);
		
		if(cliente.getUsuario() != null){
			query.setParameter("codUsuarioResponsavel", cliente.getUsuario().getCodUsuario());
		}
		
		if(cliente.getEntidade() != null){
			query.setParameter("codEntidade", cliente.getEntidade().getCodEntidade());
		}
		
		if(cliente.getGrupo() != null){
			query.setParameter("codGrupo", cliente.getGrupo().getCodGrupo());
		}
				
		return query.getResultList();
		
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}

	public Date getDataInicioVenda() {
		return dataInicioVenda;
	}

	public void setDataInicioVenda(Date dataInicioVenda) {
		this.dataInicioVenda = dataInicioVenda;
	}

	public boolean isConsultaJaRealizada() {
		return consultaJaRealizada;
	}

	public void setConsultaJaRealizada(boolean consultaJaRealizada) {
		this.consultaJaRealizada = consultaJaRealizada;
	}	
}
