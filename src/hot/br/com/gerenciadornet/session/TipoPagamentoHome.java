package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import br.com.gerenciadornet.util.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@SuppressWarnings("serial")
@Name("tipoPagamentoHome") 
public class TipoPagamentoHome extends EntityHome<TipoPagamento> {

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setTipoPagamentoCodTipoPagamento(Integer id) {
		setId(id);
	}

	public Integer getTipoPagamentoCodTipoPagamento() {
		return (Integer) getId();
	}

	@Override
	protected TipoPagamento createInstance() {
		TipoPagamento tipoPagamento = new TipoPagamento();
		return tipoPagamento;
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

	public TipoPagamento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Venda> getVendas() {
		return getInstance() == null ? null : new ArrayList<Venda>(
				getInstance().getVendas());
	}

	public List<Compra> getCompras() {
		return getInstance() == null ? null : new ArrayList<Compra>(
				getInstance().getCompras());
	}		 
	/**
	 * Exclui um cliente que nao tem vendas associadas.<br/>
	 * Desativa um cliente que tem vendas associadas.<br/>
	 * Nao deixa excluir o cliente default Venda de Balcao 
	 * com codigo igual a 1.<br/>
	 * Grava historico do cliente excluido / desativado.
	 */
	@Override
	public String remove() {			
		
		TipoPagamento tipoPagamento = 	getInstance();
		String log = "";
		
		if(tipoPagamento.getCodTipoPagamento() == 1){
			addFacesMessage("O Tipo Pagamento " + tipoPagamento.getDescTipoPagamento().toUpperCase() + " nao pode ser exluido.", "");			 
			return null;
		}
				
		if(tipoPagamento.getVendas().size() == 0 && tipoPagamento.getCompras().size() == 0){
			super.remove();
			addFacesMessage("O tipoPagamento " + tipoPagamento.getDescTipoPagamento().toUpperCase() + " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("TipoPagamento", tipoPagamento.getCodTipoPagamento(), tipoPagamento.getDescTipoPagamento().toUpperCase());
			
		} else {		
			tipoPagamento.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("TipoPagamento", tipoPagamento.getCodTipoPagamento(), tipoPagamento.getDescTipoPagamento().toUpperCase());
			addFacesMessage("O tipoPagamento " + tipoPagamento.getDescTipoPagamento().toUpperCase() + " foi desabilitado com sucesso.", "");
		}
				
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
		
	}
	
	@Override
	public String update() {
		
		TipoPagamento tipoPagamento = 	getInstance();

		tipoPagamento.setDescTipoPagamento(tipoPagamento.getDescTipoPagamento());
		
		if(tipoPagamento.getCodTipoPagamento() == 1){
			addFacesMessage("Este Tipo Pagamento nao pode ser alterado.", "");			 
			return null;
		}
		
		String log = LogUtil.logHistorico(
				"O Tipo de Pagamento " + tipoPagamento.getCodTipoPagamento()  
				+ " - " + tipoPagamento.getDescTipoPagamento() 
				+ " teve seus dados atualizados.");
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
	@Override
	public String persist() {

		TipoPagamento tipoPagamento = 	getInstance();
		//Converte a descTipoPagemento todo para MAÍSCULO
		tipoPagamento.setDescTipoPagamento(tipoPagamento.getDescTipoPagamento());
		return super.persist();
	}
}
