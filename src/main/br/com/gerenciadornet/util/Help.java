package br.com.gerenciadornet.util;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("help")
@Scope(ScopeType.APPLICATION)
public class Help {

	String teste = "teste help .x.x..x.x ";
	public String vendaEdit() {

		StringBuilder help = new StringBuilder();
		help.append(" -- Ajuda, Dicas e Procedimentos para realizacao da Venda/Orcamento --&lt;br&gt;");
		help.append("Usuario: Pessoa (Funcionario) responsavel pela realizacao da venda / orcamento.<br/>");
		help.append("Cliente: Selecione o nome do cliente proprietario da venda / orcamento.<br/>");
		help.append("Nota fical: Informe o numero da nota fiscal emitada para a venda.<br/>");
		help.append("Para carregar a lista de clientes, clique na imagem ao lado da comboBox clientes.<br/>");
		help.append("Tipo Pagamento: Forma como a venda sera paga.<br/><br/>");

		help.append("Servico: Lista de servicos que poderao ser contratados pelo cliente.<br/>");
		help.append("Para adicionar um servico a sua venda:<br/>");
		help.append("1 - Selecione o servico na comboBox,<br/>");
		help.append("2 - atualize o Valor Cobrado(R$)caso necessario,<br/>");
		help.append("3 - clique no botao Add+<br/>");
		help.append("repeita este processo enquanto necessario.<br/><br/>");

		help.append("Produto: Produtos comprados pelo pelo cliente. <br/>");
		help.append("Para adicionar um produto a sua venda:<br/>");
		help.append("1 - Clique no botao Buscar<br/>");
		help.append("2 - selecione o lote de produto desejado<br/>");
		help.append("3 - atualize o preco caso necessario<br/>");
		help.append("4 - informe a quantidade de produtos vendidos<br/>");
		help.append("5 - informe o desconto, caso houver<br/>");
		help.append("6 - clique no botao Add+<br/>");
		help.append("repeita este processo enquanto necessario.<br/>");
		help.append("Desconto total: Corresponde ao valor do desc. em porcentagem a ser aplicado na venda.<br/>");
		help.append("Observacao: Preencha este campo caso haja alguma informacao extra sobre a venda.<br/>");
		help.append("Venda/Orcamento: Informe se eh uma venda(debita do estoque) ou um orcamento(Nao debita do estoque).<br/>");
		help.append("Finalizar Venda: Informe se a venda foi ou nao finalizada. As nao finalizadas sao vendas pendentes.<br/>");
		help.append("Valor Pago: Campo nao obrigatorio, informe caso haja necessidade de se realizar o calculo do troco.<br/>");
		help.append("Clique no botao Salvar pra finalizar ou no botao voltar para cancelar a venda / orcamento<br/><br/>");
		help.append("Nao podera ser criada uma venda / orcamento sem servico(s) ou produto(s) adicionado(s).<br/>");

		return help.toString();
	}
	public String getTeste() {
		return teste;
	}
	public void setTeste(String teste) {
		this.teste = teste;
	}
}
