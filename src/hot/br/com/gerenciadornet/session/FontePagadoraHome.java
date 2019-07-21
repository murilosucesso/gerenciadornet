package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.FontePagadora;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("fontePagadoraHome") 
public class FontePagadoraHome extends EntityHome<FontePagadora> {

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setFontePagadoraCodFontePagadora(Integer id) {
		setId(id);
	}

	public Integer getFontePagadoraCodFontePagadora() {
		return (Integer) getId();
	}

	@Override
	protected FontePagadora createInstance() {
		FontePagadora fontePagadora = new FontePagadora();
		return fontePagadora;
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

	public FontePagadora getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	/**
	 * Exclui um FontePagadora que nao tem pagamentos associados.<br/>
	 * Desativa um FontePagadora que tem pagamentos associados.<br/>
	 * Nao deixa excluir o cliente default Venda de Balcao 
	 * com codigo igual a 1.<br/>
	 * Grava historico do cliente excluido / desativado.
	 */
	@Override
	public String remove() {			
		
		FontePagadora fontePagadora = 	getInstance();
		String log = "";
		
		if(fontePagadora.getCodFontePagadora() == 1 ){//PAGAMENTO_FUNCIONARIO E COMPRAS DIVERSAS
			addFacesMessage("A Fonte Pagadora " + fontePagadora.getDescFontePagadora().toUpperCase() + " nao pode ser exluida.", "");			 
			return null;
		}
						
		if(fontePagadora.getPagamentos().size() == 0){
			super.remove();
			addFacesMessage("A Fonte Pagadora " + fontePagadora.getDescFontePagadora().toUpperCase() + " foi excluida com sucesso.", "");
			log = LogUtil.logHistoricoDelete("FontePagadora", fontePagadora.getCodFontePagadora(), fontePagadora.getDescFontePagadora().toUpperCase());
			
		} else {		
			fontePagadora.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("FontePagadora", fontePagadora.getCodFontePagadora(), fontePagadora.getDescFontePagadora().toUpperCase());
			addFacesMessage("O Fonte Pagadora " + fontePagadora.getDescFontePagadora().toUpperCase() + " foi desabilitada com sucesso.", "");
		}
				
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
		
	}
	
	@Override
	public String update() {
		
		FontePagadora fontePagadora = 	getInstance();

		fontePagadora.setDescFontePagadora(fontePagadora.getDescFontePagadora());
				
		String log = LogUtil.logHistorico(
				"A Fonte Pagadora " + fontePagadora.getCodFontePagadora()  
				+ " - " + fontePagadora.getDescFontePagadora() 
				+ " teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
}
