package br.com.gerenciadornet.session;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Servico;
import br.com.gerenciadornet.entity.ServicoRealizado;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.entity.ServicoRealizado.ServicoRealizadoID;

@SuppressWarnings("serial")
@Name("servicoRealizadoHome")
public class ServicoRealizadoHome extends EntityHome<ServicoRealizado> {

	@In(create = true)
	VendaHome vendaHome;
	@In(create = true)
	ServicoHome servicoHome;

	/*public void setServicoRealizadoCodServico(Integer id) {
		setId(id);
	}

	public Integer getServicoRealizadoCodServico() {
		return (Integer) getId();
	}*/

	public void setServicoRealizadoId(ServicoRealizadoID id) {
		setId(id);
	}
	
	public ServicoRealizadoID getServicoRealizadoId() {
		return (ServicoRealizadoID) getId();
	}
	
	@Override
	protected ServicoRealizado createInstance() {
		ServicoRealizado servicoRealizado = new ServicoRealizado();
		return servicoRealizado;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Venda venda = vendaHome.getDefinedInstance();
		if (venda != null) {
			getInstance().setVenda(venda);
		}
		Servico servico = servicoHome.getDefinedInstance();
		if (servico != null) {
			getInstance().setServico(servico);
		}
	}

	public boolean isWired() {
		if (getInstance().getVenda() == null)
			return false;
		if (getInstance().getServico() == null)
			return false;
		return true;
	}

	public ServicoRealizado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
