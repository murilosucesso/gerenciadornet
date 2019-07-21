package br.com.gerenciadornet.session;

import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Constantes;

@Name("consultaPedidosList")
public class ConsultaPedidosList {
	
	@In
	EntityManager entityManager;	
	
	@Out(scope=ScopeType.SESSION)
	boolean novoPedido = false;
	
	@Out(scope=ScopeType.SESSION)
	boolean novoOrcamento = false;
	
	/**
	 * Verifica se existem novos pedidos ao de venda pelo 
	 * sistema /Vendas.
	 */
	public void getNovosPedidos() {

		@SuppressWarnings("unchecked")
		List<Integer> pedidoList = (List<Integer>)entityManager
				.createQuery("select venda.codVenda from Venda venda where venda.statusVenda.codStatusVenda = :statusPedido or venda.statusVenda.codStatusVenda = :statusPedidoAutorizado")
				.setParameter("statusPedido", Constantes.PEDIDO)
				.setParameter("statusPedidoAutorizado", Constantes.PEDIDO_AUTORIZADO)
				.getResultList();
		
		if(pedidoList != null && pedidoList.size() > 0){
			novoPedido = true;
		} 
	}
	
	/**
	 * Verifica se existem orçamentos em aberto para o usuário.
	 */
	public void getOrcamentosAbertos(Usuario usuario) {

		@SuppressWarnings("unchecked")
		List<Integer> orcamentoList = (List<Integer>)entityManager
				.createQuery("select venda.codVenda from Venda venda where venda.statusVenda.codStatusVenda = :statusPedido and venda.usuario.codUsuario = :codUsuario")
				.setParameter("statusPedido", Constantes.ORCAMENTO)
				.setParameter("codUsuario", usuario.getCodUsuario())
				.getResultList();
		
		if(orcamentoList != null && orcamentoList.size() > 0){
			novoOrcamento = true;
		} 
	}
}
