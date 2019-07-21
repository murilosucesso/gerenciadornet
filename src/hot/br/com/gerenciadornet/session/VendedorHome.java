package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Compra;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Vendedor;
import br.com.gerenciadornet.entity.VendedorId;
import br.com.gerenciadornet.util.LogUtil;
import br.com.gerenciadornet.util.Util;

@SuppressWarnings("serial")
@Name("vendedorHome")
public class VendedorHome extends EntityHome<Vendedor> {

	@In(create = true)
	FornecedorHome fornecedorHome;
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
		
	public void setVendedorId(VendedorId id) {
		setId(id);
	}

	public VendedorId getVendedorId() {
		return (VendedorId) getId();
	}

	public VendedorHome() {
		setVendedorId(new VendedorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVendedorId().getCodVendedor() == 0)
			return false;
		if (getVendedorId().getCodFornecedor() == 0)
			return false;
		return true;
	}

	@Override
	protected Vendedor createInstance() {
		Vendedor vendedor = new Vendedor();	
		vendedor.setId(new VendedorId());
		return vendedor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Fornecedor fornecedor = fornecedorHome.getDefinedInstance();
		if (fornecedor != null) {
			getInstance().setFornecedor(fornecedor);
		}
	}

	public boolean isWired() {
		if (getInstance().getFornecedor() == null)
			return false;
		return true;
	}

	public Vendedor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Compra> getCompras() {
		return getInstance() == null ? null : new ArrayList<Compra>(
				getInstance().getCompras());
	}
	
	@Override
	public String persist() {
		
		Vendedor vendedor = getInstance();	
		vendedor.setNomeVendedor(Util.formataNome(vendedor.getNomeVendedor()));
		vendedor.setInExclusao(false);
		
		VendedorId id = new VendedorId();
		id.setCodFornecedor(vendedor.getFornecedor().getCodFornecedor());
		vendedor.setId(id);
		
		return super.persist();
	}
	
	@Override
	public String remove() {
		
		Vendedor vendedor = getDefinedInstance();
		
		Fornecedor fornecedor = fornecedorHome.getInstance();
		fornecedor.getVendedors().remove(vendedor);
		
		String log = "";
				
		if(vendedor.getCompras().size() == 0){
			super.remove();
			addFacesMessage("O vendedor " + vendedor.getNomeVendedor().toUpperCase() + " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Vendedor", vendedor.getId().getCodVendedor(), vendedor.getNomeVendedor().toUpperCase());
			
		} else {		
			vendedor.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Vendedor", vendedor.getId().getCodVendedor(), vendedor.getNomeVendedor().toUpperCase());
			addFacesMessage("O cliente " + vendedor.getNomeVendedor().toUpperCase() + " foi desabilitado com sucesso.", "");
			
		}

		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
	}

	@Override
	public String update() {	
		
	       //Vendedor vendedor = getDefinedInstance(); alteração dia 14/10/2014
		Vendedor vendedor = getInstance();
		vendedor.setNomeVendedor(Util.formataNome(vendedor.getNomeVendedor()));

		String log = LogUtil.logHistorico(
				"O vendedor codigo " + vendedor.getId().getCodVendedor()  
				+ " - " + vendedor.getNomeVendedor() 
				+ ", do fornecedor " + vendedor.getFornecedor().getNomeFantasia()
				+ " teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
}
