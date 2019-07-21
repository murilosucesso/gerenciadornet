package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@SuppressWarnings("serial")
@Name("enderecoList")
public class EnderecoList extends EntityQuery<Endereco> {

	private static final String EJBQL = "select endereco from Endereco endereco";

	private static final String[] RESTRICTIONS = {
			"lower(endereco.endereco) like lower(concat(#{enderecoList.endereco.endereco},'%'))",
			"lower(endereco.numero) like lower(concat(#{enderecoList.endereco.numero},'%'))",
			"lower(endereco.complemento) like lower(concat(#{enderecoList.endereco.complemento},'%'))",
			"lower(endereco.bairro) like lower(concat(#{enderecoList.endereco.bairro},'%'))",
			"lower(endereco.cidade) like lower(concat(#{enderecoList.endereco.cidade},'%'))",
			"lower(endereco.uf) like lower(concat(#{enderecoList.endereco.uf},'%'))",
			"lower(endereco.telefone1) like lower(concat(#{enderecoList.endereco.telefone1},'%'))",
			"lower(endereco.tefefone2) like lower(concat(#{enderecoList.endereco.tefefone2},'%'))", };

	private Endereco endereco = new Endereco();

	public EnderecoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Endereco getEndereco() {
		return endereco;
	}
}
