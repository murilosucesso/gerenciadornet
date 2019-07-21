package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Categoria;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("categoriaHome")
public class CategoriaHome extends EntityHome<Categoria> {

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setCategoriaCodCategoria(Integer id) {
		setId(id);
	}
	
	public Integer getCategoriaCodCategoria() {
		return (Integer) getId();
	}

	@Override
	protected Categoria createInstance() {
		Categoria categoria = new Categoria();
		return categoria;
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

	public Categoria getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Produto> getProdutos() {
		return getInstance() == null ? null : new ArrayList<Produto>(
				getInstance().getProdutos());
	}

	@Override
	public String remove() {		
		
		Categoria categoria = getInstance();
		String log = "";
		
		if(categoria.getProdutos().size() == 0){
			super.remove();
			addFacesMessage("A categoria " + categoria.getNomeCategoria().toUpperCase() + " foi excluida com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Categoria", categoria.getCodCategoria(), categoria.getNomeCategoria() .toUpperCase());
			
		} else {		
			categoria.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Categoria", categoria.getCodCategoria(), categoria.getNomeCategoria().toUpperCase());
			
			StringBuilder message = new StringBuilder("A categoria ");
			message.append(categoria.getNomeCategoria().toUpperCase());
			message.append(" foi desabilitada com sucesso.");
						
			addFacesMessage(message.toString(), "");
		}
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
	}
	
	@Override
	public String persist() {
		
		Categoria categoria = getInstance();
		categoria.setNomeCategoria(categoria.getNomeCategoria().toUpperCase());
		
		return super.persist();
	}
	
	@Override
	public String update() {
		
		Categoria categoria = getInstance();
		categoria.setNomeCategoria(categoria.getNomeCategoria().toUpperCase());
		
		return super.update();
	}
	
}
