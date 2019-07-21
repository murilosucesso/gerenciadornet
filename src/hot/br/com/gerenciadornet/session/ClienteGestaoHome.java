package br.com.gerenciadornet.session;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Constantes;
import br.com.gerenciadornet.util.Transacoes;

@SuppressWarnings("serial")
@Name("clienteGestaoHome")
@Scope(ScopeType.CONVERSATION)
public class ClienteGestaoHome extends EntityHome<Cliente> {

	@In
	EntityManager entityManager;

	@In
	Usuario user;

	@In(create = true)
	Transacoes transacoes;

	@In
	Identity identity;

	public void setClienteCodCliente(Integer id) {
		setId(id);
	}

	public Integer getClienteCodCliente() {
		return (Integer) getId();
	}

	@Override
	protected Cliente createInstance() {
		Cliente cliente = new Cliente();
		return cliente;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
	}

	public boolean isWired() {
		return true;
	}

	public Cliente getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * @param clienteList
	 * @param conferir
	 */
	public String excluirVendedoresResponsavel(List<Cliente> clienteList) {
		
		if (!identity.hasRole(transacoes.getTransacaoClienteGestao()) && user.getPerfil().getCodPerfil() != Constantes.ADMINISTRADOR) {
			addFacesMessage("Usuário não possui acesso à transação: " + transacoes.getTransacaoClienteExcluir(), "");
			return null;
		}

		boolean vendedoresRespExcluidos = false;

		for (Cliente cliente : clienteList) {

			this.setInstance(cliente);
			cliente.setUsuario(null);
			this.update();
			vendedoresRespExcluidos = true;
		}

		//Grava Histórico da transação
		if (vendedoresRespExcluidos) {
			String log = "Realizado a exclusao de vendedores responsaveis.";

			Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
			entityManager.persist(historico);
		}

		return null;
	}

}
