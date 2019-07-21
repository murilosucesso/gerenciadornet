package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.TipoDebito;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("tipoDebitoHome") 
public class TipoDebitoHome extends EntityHome<TipoDebito> {

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setTipoDebitoCodTipoDebito(Integer id) {
		setId(id);
	}

	public Integer getTipoDebitoCodTipoDebito() {
		return (Integer) getId();
	}

	@Override
	protected TipoDebito createInstance() {
		TipoDebito tipoDebito = new TipoDebito();
		return tipoDebito;
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

	public TipoDebito getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	/**
	 * Exclui um TipoDebito que nao tem pagamentos associados.<br/>
	 * Desativa um TipoDebito que tem pagamentos associados.<br/>
	 * Nao deixa excluir o cliente default Venda de Balcao 
	 * com codigo igual a 1.<br/>
	 * Grava historico do cliente excluido / desativado.
	 */
	@Override
	public String remove() {			
		
		TipoDebito tipoDebito = 	getInstance();
		String log = "";
		
		if(tipoDebito.getCodTipoDebito() == 1 || tipoDebito.getCodTipoDebito() == 2 ){//PAGAMENTO_FUNCIONARIO E COMPRAS DIVERSAS
			addFacesMessage("O Tipo Debito " + tipoDebito.getDescTipoDebito().toUpperCase() + " nao pode ser exluido.", "");			 
			return null;
		}
				
		if(tipoDebito.getPagamentos().size() == 0){
			super.remove();
			addFacesMessage("O Tipo Debito " + tipoDebito.getDescTipoDebito().toUpperCase() + " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("TipoDebito", tipoDebito.getCodTipoDebito(), tipoDebito.getDescTipoDebito().toUpperCase());
			
		} else {		
			tipoDebito.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("TipoDebito", tipoDebito.getCodTipoDebito(), tipoDebito.getDescTipoDebito().toUpperCase());
			addFacesMessage("O tipoDebito " + tipoDebito.getDescTipoDebito().toUpperCase() + " foi desabilitado com sucesso.", "");
		}
				
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
		
	}
	
	@Override
	public String update() {
		
		TipoDebito tipoDebito = 	getInstance();

		tipoDebito.setDescTipoDebito(tipoDebito.getDescTipoDebito());
		
		if(tipoDebito.getCodTipoDebito() == 1){
			addFacesMessage("Este Tipo Debito nao pode ser alterado.", "");			 
			return null;
		}
		
		String log = LogUtil.logHistorico(
				"O Tipo de Debito " + tipoDebito.getCodTipoDebito()  
				+ " - " + tipoDebito.getDescTipoDebito() 
				+ " teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
	@Override
	public String persist() {

		TipoDebito tipoDebito = 	getInstance();
		//Converte a descTipoPagemento todo para MAÍSCULO
		tipoDebito.setDescTipoDebito(tipoDebito.getDescTipoDebito());
		return super.persist();
	}
}
