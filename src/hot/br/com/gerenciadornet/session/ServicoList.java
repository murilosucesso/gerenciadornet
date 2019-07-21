package br.com.gerenciadornet.session;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Servico;

@SuppressWarnings("serial")
@Name("servicoList")
public class ServicoList extends EntityQuery<Servico> {

	private static final String EJBQL = "select servico from Servico servico";

	private static final String[] RESTRICTIONS = { 
		"lower(servico.descServico) like lower( concat('%',concat(#{servicoList.servico.descServico},'%')))", 
		"servico.inExclusao = #{servicoList.servico.inExclusao}",
		};

	private Servico servico = new Servico();

	public ServicoList() {
		servico.setInExclusao(false);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("servico.descServico");
	}

	/**
	 * Cria um TreeMap para ser utilizado pelas combo Box with dynamic
	 * suggestions list.
	 * 
	 * @return
	 */
	public List<Servico> getServicoSuggestionList() {

		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		List<Servico> servicoList = getResultList();
		List<Servico> servicoList2 = new ArrayList<Servico>();

		for (Servico servico : servicoList) {

			servico.setDescServico(
					servico.getDescServico() + " - "
					+ numberFormat.format(servico.getPrecoServico()));
			
			servicoList2.add(servico);
		}
		return servicoList2;
	}

	public Servico getServico() {
		return servico;
	}
}
