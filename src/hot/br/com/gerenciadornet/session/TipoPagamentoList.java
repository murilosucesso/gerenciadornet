package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("tipoPagamentoList")
public class TipoPagamentoList extends EntityQuery<TipoPagamento> {

	private static final String EJBQL = "select tipoPagamento from TipoPagamento tipoPagamento";

	private static final String[] RESTRICTIONS = { 
		"lower(tipoPagamento.descTipoPagamento) like lower( concat('%', concat(#{tipoPagamentoList.tipoPagamento.descTipoPagamento},'%')))", 
		"tipoPagamento.inExclusao = #{tipoPagamentoList.tipoPagamento.inExclusao}",
		};
	
	private static final String[] RESTRICTIONS2 = { 
		"lower(tipoPagamento.descTipoPagamento) like lower( concat('%', concat(#{tipoPagamentoList.tipoPagamento.descTipoPagamento},'%')))", 
		"tipoPagamento.inExclusao = #{tipoPagamentoList.tipoPagamento.inExclusao}",
		"tipoPagamento.inGN = #{tipoPagamentoList.canalGN} "
		};

	private TipoPagamento tipoPagamento = new TipoPagamento();
	private boolean canalGN = true;
	
	public TipoPagamentoList() {
		tipoPagamento.setInExclusao(false);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("tipoPagamento.descTipoPagamento");
	}
	
	/**
	 * Retorna apenas os tipos de pagamentos utilizados no GN
	 * @return
	 */
	public List<TipoPagamento> getResultList2() {
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		return super.getResultList();
	}
	
	/**
	 * Cria um TreeMap para ser utilizado pelas combo Box with dynamic suggestions list. 
	 * @return
	 */
	public TreeMap<Integer, String> getTipoPagamentoSuggestionList(){
		
		TreeMap<Integer, String> tipoPagamentoSugestionList = new TreeMap<Integer, String>();
		
		List<TipoPagamento> tipoPagamentoList = getResultList();
		for(TipoPagamento tipoPagamento :tipoPagamentoList){
			tipoPagamentoSugestionList.put(tipoPagamento.getCodTipoPagamento(), tipoPagamento.getDescTipoPagamento());
		}
		
		return tipoPagamentoSugestionList;
	}
	
	/**
	 * Botao Limpar.
	 * Limpar todos os campos depois de setados
	 * na conversation.
	 */
	public void limpar() {
		tipoPagamento.setDescTipoPagamento(null);
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}


	public boolean isCanalGN() {
		return canalGN;
	}
}
