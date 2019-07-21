package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("fornecedorList")
public class FornecedorList extends EntityQuery<Fornecedor> {

	private static final String EJBQL = "select fornecedor from Fornecedor fornecedor";

	private static final String[] RESTRICTIONS = {
			"lower(fornecedor.nomeFantasia) like lower(concat(#{fornecedorList.fornecedor.nomeFantasia},'%'))",
			"lower(fornecedor.razaoSocial) like lower(concat(#{fornecedorList.fornecedor.razaoSocial},'%'))",
			"lower(fornecedor.descFornecedor) like lower(concat(#{fornecedorList.fornecedor.descFornecedor},'%'))",
			"lower(fornecedor.cpjCnpj) like lower(concat(#{fornecedorList.fornecedor.cpjCnpj},'%'))",
			"lower(fornecedor.inscricaoEstadual) like lower(concat(#{fornecedorList.fornecedor.inscricaoEstadual},'%'))",
			"lower(fornecedor.fax) like lower(concat(#{fornecedorList.fornecedor.fax},'%'))",
			"lower(fornecedor.observacao) like lower(concat(#{fornecedorList.fornecedor.observacao},'%'))", 
			"fornecedor.inExclusao = #{fornecedorList.fornecedor.inExclusao}",
			};

	private Fornecedor fornecedor = new Fornecedor("", null);

	public FornecedorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));	
		setOrderColumn("fornecedor.nomeFantasia");
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}
}
