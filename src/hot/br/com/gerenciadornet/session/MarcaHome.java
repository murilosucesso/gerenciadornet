package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Marca;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("marcaHome")
public class MarcaHome extends EntityHome<Marca> {

	@In(create = true)
	ProdutoHome produtoHome;
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setMarcaCodMarca(Integer id) {
		setId(id);
	}

	public Integer getMarcaCodMarca() {
		return (Integer) getId();
	}

	@Override
	protected Marca createInstance() {
		Marca marca = new Marca();
		return marca;
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

	public Marca getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Produto> getProdutos() {
		return getInstance() == null ? null : new ArrayList<Produto>(
				getInstance().getProdutos());
	}
	
	/**
	 * Exclui uma MARCA caso esta nao possua relacionamentos.
	 * Ou desativa uma MARCA e seus PRODUTOS.
	 * Grava log de historico de exclus�o / desativa��o.
	 */
	@Override
	public String remove() {		
		
		Marca marca = 	getInstance();
		String log = "";
		
		if(marca.getProdutos().size() == 0){
			super.remove();
			addFacesMessage("A Marca " + marca.getNomeMarca().toUpperCase() + " foi excluida com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Marca", marca.getCodMarca() , marca.getNomeMarca().toUpperCase());
			
		} else {		
			marca.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Marca", marca.getCodMarca(), marca.getNomeMarca().toUpperCase());
			
			StringBuilder message = new StringBuilder("A marca ");
			message.append(marca.getNomeMarca() .toUpperCase());
			message.append(" foi desabilitada com sucesso.");
			
			Set<Produto> produtos = marca.getProdutos();
			
			for (Produto produto: produtos) {
				produto.setInExclusao(true);
				produtoHome.setInstance(produto);
				produtoHome.update();
				message.append("O produto " + produto.getNomeProduto() + " foi desativado.");
				
				String logVendedor = LogUtil.logHistoricoDesativada("Marca / Produto", produto.getCodProduto(), produto.getNomeProduto());
				
				Historico historico = new Historico(user, logVendedor, new Date(System.currentTimeMillis()));
				entityManager.persist(historico);
			}
			
			
			addFacesMessage(message.toString(), "");
		}
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
	}
	
	@Override
	public String update() {
		
		Marca marca = 	getInstance();
		marca.setNomeMarca(marca.getNomeMarca().toUpperCase());
		
		String log = LogUtil.logHistorico(
				"A marca codigo " + marca.getCodMarca() 
				+ " - " + marca.getNomeMarca()  
				+ " teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
	@Override
	public String persist() {
		Marca marca = 	getInstance();
		marca.setNomeMarca(marca.getNomeMarca().toUpperCase());
		return super.persist();
	}

}
