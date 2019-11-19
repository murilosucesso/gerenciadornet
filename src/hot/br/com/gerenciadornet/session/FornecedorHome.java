package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Vendedor;
import br.com.gerenciadornet.entity.VendedorId;
import br.com.gerenciadornet.util.LogUtil;
import br.com.gerenciadornet.util.Util;

@SuppressWarnings("serial")
@Name("fornecedorHome")
public class FornecedorHome extends EntityHome<Fornecedor> {

	@In(create = true)
	EnderecoHome enderecoHome;
	
	@In(create = true)
	VendedorHome vendedorHome;
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setFornecedorCodFornecedor(Integer id) {
		setId(id);
	}

	public Integer getFornecedorCodFornecedor() {
		return (Integer) getId();
	}

	@Override
	protected Fornecedor createInstance() {
		
	    Fornecedor fornecedor = new Fornecedor();
	    fornecedor.setInExclusao(false);
	    fornecedor.setInContaFixa(false);
	    fornecedor.setInAfeFuncionamento(true);
		
	    return fornecedor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Fornecedor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Vendedor> getVendedors() {
		return getInstance() == null ? null : new ArrayList<Vendedor>(
				getInstance().getVendedors());
	}

	public List<Endereco> getEnderecos() {
		return getInstance() == null ? null : new ArrayList<Endereco>(
				getInstance().getEnderecos());
	}

	public String removeEndereco(){
		
		String destino = enderecoHome.remove();
		Endereco endereco = enderecoHome.getDefinedInstance();
		Fornecedor fornecedor = getInstance();
		fornecedor.getEnderecos().remove(endereco);
		
		return destino;
	}
	
	/**
	 * Exclui um vendedor e grava historico.
	 * @return
	 */
	public String removeVendedor(){
		
		Vendedor vendedor = vendedorHome.getInstance();
		Fornecedor fornecedor = getInstance();
		fornecedor.getVendedors().remove(vendedor);
		String destino = vendedorHome.remove();
		
		//Seta um novo id para excluir varios vendedores sem 
		//pegar o vendedor excluido primeiro.
		vendedorHome.setId(new VendedorId());
		
		return destino;
	}
		
	/**
	 * Exclui um fornecedor caso este nao possua relacionamentos.
	 * Ou desativa um fornecedor e seus vendedores.
	 * Grava log de historico de exclusao / desativacao.
	 */
	@Override
	public String remove() {		
		
		Fornecedor fornecedor = 	getInstance();
		String log = "";
		
		/*if(fornecedor.getVendedors().size() == 0 && fornecedor.getMarcas().size() == 0){
			super.remove();
			addFacesMessage("O fornecedor " + fornecedor.getNomeFantasia().toUpperCase() + " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Fornecedor", fornecedor.getCodFornecedor(), fornecedor.getNomeFantasia().toUpperCase());
			
		} else {		*/
			fornecedor.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Fornecedor", fornecedor.getCodFornecedor(), fornecedor.getNomeFantasia().toUpperCase());
			
			StringBuilder message = new StringBuilder("O fornecedor ");
			message.append(fornecedor.getNomeFantasia().toUpperCase());
			message.append(" foi desabilitado com sucesso.");
			
			Set<Vendedor> vendedores = fornecedor.getVendedors();
			
			for (Vendedor vendedor : vendedores) {
				vendedor.setInExclusao(true);
				vendedorHome.setInstance(vendedor);
				vendedorHome.update();
				message.append("O vendedor " + vendedor.getNomeVendedor() + " foi desativado.");
				
				String logVendedor = LogUtil.logHistoricoDesativada("Fornecedor / Vendedor", vendedor.getId().getCodVendedor(), vendedor.getNomeVendedor());
				
				Historico historico = new Historico(user, logVendedor, new Date(System.currentTimeMillis()));
				entityManager.persist(historico);
			}
			
			
			addFacesMessage(message.toString(), "");
		//}
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
	}
	
	public String ativar() {
	 
	    Fornecedor fornecedor = getInstance();
	    fornecedor.setInExclusao(new Boolean(false));	    
	    addFacesMessage("O fornecedor " + fornecedor.getNomeFantasia().toUpperCase() + " foi ativado com sucesso.", "");
	    return super.update();
	}
	
	@Override
	public String update() {
		
		Fornecedor fornecedor = getInstance();
		fornecedor.setNomeFantasia(Util.formataNome(fornecedor.getNomeFantasia()));
		fornecedor.setRazaoSocial(Util.formataNome(fornecedor.getRazaoSocial()));
		
		String log = LogUtil.logHistorico("O forncedor codigo " + fornecedor.getCodFornecedor() 
				+ " - " + fornecedor.getNomeFantasia() +
			" teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
	@Override
	public String persist() {
		
		Fornecedor fornecedor = getInstance();

		Query query = entityManager.createQuery("select fornecedor from Fornecedor fornecedor where lower(fornecedor.nomeFantasia) = :fornecedor");
		query.setParameter("fornecedor", fornecedor.getNomeFantasia().toLowerCase());
		
		if(query.getResultList().size() > 0){
			addFacesMessage("Fornecedor com este Nome Fantasia já cadastrado.", "");
			return null;
		}
		
		fornecedor.setNomeFantasia(Util.formataNome(fornecedor.getNomeFantasia()));
		fornecedor.setRazaoSocial(Util.formataNome(fornecedor.getRazaoSocial()));
		
		return super.persist();
	}
}
