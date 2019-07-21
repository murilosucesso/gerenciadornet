package br.com.gerenciadornet.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

@Name("transacoes")
@Startup
@Scope(ScopeType.APPLICATION)
public class Transacoes {
	
	//---- TRANSACOES BASICA - CRUD - INICIO ------
	public final static String TRANSACAO_USUARIO_CONSULTAR 			= "GN001";
	public final static String TRANSACAO_USUARIO_INSERIR 			= "GN002";
	public final static String TRANSACAO_USUARIO_EDITAR 			= "GN003";	
	public final static String TRANSACAO_USUARIO_EXCLUIR 			= "GN004";
	
	public final static String TRANSACAO_CLIENTE_CONSULTAR 			= "GN005";
	public final static String TRANSACAO_CLIENTE_INSERIR 			= "GN006";
	public final static String TRANSACAO_CLIENTE_EDITAR 			= "GN007";	
	public final static String TRANSACAO_CLIENTE_EXCLUIR 			= "GN008";
	
	public final static String TRANSACAO_FORNECEDOR_CONSULTAR 		= "GN009";
	public final static String TRANSACAO_FORNECEDOR_INSERIR 		= "GN010";
	public final static String TRANSACAO_FORNECEDOR_EDITAR 			= "GN011";	
	public final static String TRANSACAO_FORNECEDOR_EXCLUIR 		= "GN012";
	
	public final static String TRANSACAO_VENDEDOR_CONSULTAR 		= "GN012";
	public final static String TRANSACAO_VENDEDOR_INSERIR 			= "GN014";
	public final static String TRANSACAO_VENDEDOR_EDITAR 			= "GN015";	
	public final static String TRANSACAO_VENDEDOR_EXCLUIR 			= "GN016";
	
	public final static String TRANSACAO_PRODUTO_CONSULTAR 				= "GN017";
	public final static String TRANSACAO_PRODUTO_INSERIR 				= "GN018";
	public final static String TRANSACAO_PRODUTO_EDITAR 				= "GN019";	
	public final static String TRANSACAO_PRODUTO_EXCLUIR 				= "GN020";
	
	public final static String TRANSACAO_TIPO_PAGAMENTO_CONSULTAR 		= "GN021";
	public final static String TRANSACAO_TIPO_PAGAMENTO_INSERIR 		= "GN022";
	public final static String TRANSACAO_TIPO_PAGAMENTO_EDITAR 			= "GN023";	
	public final static String TRANSACAO_TIPO_PAGAMENTO_EXCLUIR 		= "GN024";
	
	public final static String TRANSACAO_SERVICO_CONSULTAR 				= "GN025";
	public final static String TRANSACAO_SERVICO_INSERIR 				= "GN026";
	public final static String TRANSACAO_SERVICO_EDITAR 				= "GN027";	
	public final static String TRANSACAO_SERVICO_EXCLUIR 				= "GN028";
	
	public final static String TRANSACAO_VENDA_CONSULTAR 				= "GN029";
	public final static String TRANSACAO_VENDA_INSERIR 					= "GN030";
	public final static String TRANSACAO_VENDA_EDITAR 					= "GN031";	
	public final static String TRANSACAO_VENDA_EXCLUIR 					= "GN032";	

	public final static String TRANSACAO_COMPRA_CONSULTAR 				= "GN033";
	public final static String TRANSACAO_COMPRA_INSERIR 				= "GN034";
	public final static String TRANSACAO_COMPRA_EDITAR 					= "GN035";	
	public final static String TRANSACAO_COMPRA_EXCLUIR 				= "GN036";
	
	public final static String TRANSACAO_ACESSO_CONSULTAR 				= "GN037";
	public final static String TRANSACAO_ACESSO_EDITAR 					= "GN038";
	
	public final static String TRANSACAO_ESTOQUE_CONSULTAR 				= "GN039";
	public final static String TRANSACAO_ESTOQUE_VENCIMENTO 			= "GN040";
	public final static String TRANSACAO_ESTOQUE_VALORES 				= "GN041";
	
	public final static String TRANSACAO_HISTORICO_USUARIO_CONSULTAR	= "GN042";	
	public final static String TRANSACAO_RELATORIOS 					= "GN043";
	
	public final static String TRANSACAO_CATEGORIA_CONSULTAR 		= "GN044";
	public final static String TRANSACAO_CATEGORIA_INSERIR 			= "GN045";
	public final static String TRANSACAO_CATEGORIA_EDITAR 			= "GN046";	
	public final static String TRANSACAO_CATEGORIA_EXCLUIR 			= "GN047";
	
	public final static String TRANSACAO_MARCA_CONSULTAR 				= "GN048";
	public final static String TRANSACAO_MARCA_INSERIR 					= "GN049";
	public final static String TRANSACAO_MARCA_EDITAR 					= "GN050";	
	public final static String TRANSACAO_MARCA_EXCLUIR 					= "GN051";
	
	public final static String TRANSACAO_ACESSO_PERFIL 					= "GN052";
	
	public final static String TRANSACAO_VENDA_FINALIZAR 				= "GN053";
	
	public final static String TRANSACAO_CONFIRMAR_PAGAMENTO 					= "GN054";
	public final static String TRANSACAO_CONFIRMAR_ENTREGA 						= "GN055";
	
	public final static String TRANSACAO_ESTOQUE_BALANCETE 						= "GN056";
	
	public final static String TRANSACAO_ALTERAR_VENDEDOR_RESP					= "GN057";
	public final static String TRANSACAO_ALTERAR_SITUACAO_FINANCEIRA			= "GN058";
	
	public final static String TRANSACAO_GERENCIAR_RECEBIMENTOS					= "GN059";
	public final static String TRANSACAO_GERENCIAR_RECEBIMENTOS_CONFERIR 		= "GN060";
	
	public final static String TRANSACAO_GERENCIAR_PAGAMENTOS					= "GN061";
	public final static String TRANSACAO_GERENCIAR_PAGAMENTOS_CONFERIR 			= "GN062";
	
	public final static String TRANSACAO_PRODUTO_ATUALIZAR_TODOS_LOTES 			= "GN063";
	
	public final static String TRANSACAO_NFE_GERAR_ARQUIVO						= "GN064";
	
	public final static String TRANSACAO_LIBERAR_EDICAO_VENDA					= "GN065";
	public final static String TRANSACAO_LIBERAR_SEPARACAO_MERCADORIA			= "GN066";
	public final static String TRANSACAO_CLIENTE_EXCLUIR_ANOTACAO				= "GN067";
	
	public final static String TRANSACAO_NFE_CANCELAR_EMISSAO					= "GN068";
	
	public final static String TRANSACAO_ATENDIMENTO_CONSULTAR					= "GN069";
	public final static String TRANSACAO_ATENDIMENTO_INSERIR					= "GN070";
	public final static String TRANSACAO_ATENDIMENTO_EDITAR						= "GN071";
	public final static String TRANSACAO_ATENDIMENTO_EXCLUIR					= "GN072";
	
	public final static String TRANSACAO_LIBERAR_VENDA_NAO_AUTORIZADA			= "GN073";
	
	public final static String TRANSACAO_CLIENTE_GESTAO							= "GN074";
	
	public final static String TRANSACAO_RELATORIO_REALIZACOES_FUNCIONARIO		= "GN075";
	
	public final static String TRANSACAO_ADMIN_TIPO_DEBITO				 		= "GN076";
	
	public final static String TRANSACAO_RELATORIO_VENDAS						= "GN077";	
	public final static String TRANSACAO_RELATORIO_PARA_O_CLIENTE				= "GN078";
	public final static String TRANSACAO_RELATORIO_PERIODICIDADE_CLIENTE		= "GN079";
	public final static String TRANSACAO_RELATORIO_VENDAS_X_ANO					= "GN080";
	public final static String TRANSACAO_RELATORIO_VENDAS_X_GRUPO				= "GN081";
	public final static String TRANSACAO_RELATORIO_CONTAS_X_RECEBIMENTO			= "GN082";
	public final static String TRANSACAO_RELATORIO_VENDAS_X_PRODUTO				= "GN083";
	public final static String TRANSACAO_RELATORIO_VENDAS_X_INSTITUICAO			= "GN084";
	public final static String TRANSACAO_RELATORIO_FECHAMENTO_MENSAL			= "GN085";
	
	public final static String TRANSACAO_ADMIN_FONTE_PAGADORA					= "GN086";
	
	
	//TODOS TEM ACESSO
	//public final static String TRANSACAO_USUARIO_ATULIZAR_MEUS_DADOS = "GN042";
	
	public  String getTransacaoUsuarioConsultar() {
		return TRANSACAO_USUARIO_CONSULTAR;
	}
	public  String getTransacaoUsuarioInserir() {
		return TRANSACAO_USUARIO_INSERIR;
	}
	public  String getTransacaoUsuarioEditar() {
		return TRANSACAO_USUARIO_EDITAR;
	}
	public  String getTransacaoUsuarioExcluir() {
		return TRANSACAO_USUARIO_EXCLUIR;
	}
	public  String getTransacaoClienteConsultar() {
		return TRANSACAO_CLIENTE_CONSULTAR;
	}
	public  String getTransacaoClienteInserir() {
		return TRANSACAO_CLIENTE_INSERIR;
	}
	public  String getTransacaoClienteEditar() {
		return TRANSACAO_CLIENTE_EDITAR;
	}
	public  String getTransacaoClienteExcluir() {
		return TRANSACAO_CLIENTE_EXCLUIR;
	}
	public  String getTransacaoFornecedorConsultar() {
		return TRANSACAO_FORNECEDOR_CONSULTAR;
	}
	public  String getTransacaoFornecedorInserir() {
		return TRANSACAO_FORNECEDOR_INSERIR;
	}
	public  String getTransacaoFornecedorEditar() {
		return TRANSACAO_FORNECEDOR_EDITAR;
	}
	public  String getTransacaoFornecedorExcluir() {
		return TRANSACAO_FORNECEDOR_EXCLUIR;
	}
	public  String getTransacaoVendedorConsultar() {
		return TRANSACAO_VENDEDOR_CONSULTAR;
	}
	public  String getTransacaoVendedorInserir() {
		return TRANSACAO_VENDEDOR_INSERIR;
	}
	public  String getTransacaoVendedorEditar() {
		return TRANSACAO_VENDEDOR_EDITAR;
	}
	public  String getTransacaoVendedorExcluir() {
		return TRANSACAO_VENDEDOR_EXCLUIR;
	}
	public  String getTransacaoProdutoConsultar() {
		return TRANSACAO_PRODUTO_CONSULTAR;
	}
	public  String getTransacaoProdutoInserir() {
		return TRANSACAO_PRODUTO_INSERIR;
	}
	public  String getTransacaoProdutoEditar() {
		return TRANSACAO_PRODUTO_EDITAR;
	}
	public  String getTransacaoProdutoExcluir() {
		return TRANSACAO_PRODUTO_EXCLUIR;
	}
	public  String getTransacaoTipoPagamentoConsultar() {
		return TRANSACAO_TIPO_PAGAMENTO_CONSULTAR;
	}
	public  String getTransacaoTipoPagamentoInserir() {
		return TRANSACAO_TIPO_PAGAMENTO_INSERIR;
	}
	public  String getTransacaoTipoPagamentoEditar() {
		return TRANSACAO_TIPO_PAGAMENTO_EDITAR;
	}
	public  String getTransacaoTipoPagamentoExcluir() {
		return TRANSACAO_TIPO_PAGAMENTO_EXCLUIR;
	}
	public  String getTransacaoServicoConsultar() {
		return TRANSACAO_SERVICO_CONSULTAR;
	}
	public  String getTransacaoServicoInserir() {
		return TRANSACAO_SERVICO_INSERIR;
	}
	public  String getTransacaoServicoEditar() {
		return TRANSACAO_SERVICO_EDITAR;
	}
	public  String getTransacaoServicoExcluir() {
		return TRANSACAO_SERVICO_EXCLUIR;
	}
	public  String getTransacaoVendaConsultar() {
		return TRANSACAO_VENDA_CONSULTAR;
	}
	public  String getTransacaoVendaInserir() {
		return TRANSACAO_VENDA_INSERIR;
	}
	public  String getTransacaoVendaEditar() {
		return TRANSACAO_VENDA_EDITAR;
	}
	public  String getTransacaoVendaExcluir() {
		return TRANSACAO_VENDA_EXCLUIR;
	}
	public  String getTransacaoCompraConsultar() {
		return TRANSACAO_COMPRA_CONSULTAR;
	}
	public  String getTransacaoCompraInserir() {
		return TRANSACAO_COMPRA_INSERIR;
	}
	public  String getTransacaoCompraEditar() {
		return TRANSACAO_COMPRA_EDITAR;
	}
	public  String getTransacaoCompraExcluir() {
		return TRANSACAO_COMPRA_EXCLUIR;
	}
	public  String getTransacaoAcessoConsultar() {
		return TRANSACAO_ACESSO_CONSULTAR;
	}
	public  String getTransacaoAcessoEditar() {
		return TRANSACAO_ACESSO_EDITAR;
	}
	public  String getTransacaoEstoqueConsultar() {
		return TRANSACAO_ESTOQUE_CONSULTAR;
	}
	public  String getTransacaoEstoqueVencimento() {
		return TRANSACAO_ESTOQUE_VENCIMENTO;
	}
	public  String getTransacaoEstoqueValores() {
		return TRANSACAO_ESTOQUE_VALORES;
	}
	public  String getTransacaoHistoricoUsuarioConsultar() {
		return TRANSACAO_HISTORICO_USUARIO_CONSULTAR;
	}
	public  String getTransacaoRelatorios() {
		return TRANSACAO_RELATORIOS;
	}
	public String getTransacaoCategoriaConsultar() {
		return TRANSACAO_CATEGORIA_CONSULTAR;
	}
	public String getTransacaoCategoriaInserir() {
		return TRANSACAO_CATEGORIA_INSERIR;
	}
	public String getTransacaoCategoriaEditar() {
		return TRANSACAO_CATEGORIA_EDITAR;
	}
	public String getTransacaoCategoriaExcluir() {
		return TRANSACAO_CATEGORIA_EXCLUIR;
	}
	public String getTransacaoMarcaConsultar() {
		return TRANSACAO_MARCA_CONSULTAR;
	}
	public  String getTransacaoMarcaInserir() {
		return TRANSACAO_MARCA_INSERIR;
	}
	public String getTransacaoMarcaEditar() {
		return TRANSACAO_MARCA_EDITAR;
	}
	public String getTransacaoMarcaExcluir() {
		return TRANSACAO_MARCA_EXCLUIR;
	}
	public String getTransacaoAcessoPerfil() {
		return TRANSACAO_ACESSO_PERFIL;
	}
	public String getTransacaoVendaFinalizar() {
		return TRANSACAO_VENDA_FINALIZAR;
	}
	public String getTransacaoConfirmarPagamento() {
		return TRANSACAO_CONFIRMAR_PAGAMENTO;
	}
	public String getTransacaoConfirmarEntrega() {
		return TRANSACAO_CONFIRMAR_ENTREGA;
	}
	
	public String getTransacaoEstoqueBalancete() {
		return TRANSACAO_ESTOQUE_BALANCETE;
	}
	public String getTransacaoAlterarVendedorResp()
	{
		return TRANSACAO_ALTERAR_VENDEDOR_RESP;
	}
	public String getTransacaoAlterarSituacaoFinanceira()
	{
		return TRANSACAO_ALTERAR_SITUACAO_FINANCEIRA;
	}
	
	public String getTransacaoGerenciarRecebimentos()
	{
		return TRANSACAO_GERENCIAR_RECEBIMENTOS;
	}
	public String getTransacaoGerenciarRecebimentosConferir()
	{
		return TRANSACAO_GERENCIAR_RECEBIMENTOS_CONFERIR;
	}
	
	public String getTransacaoGerenciarPagamentos() {
		return TRANSACAO_GERENCIAR_PAGAMENTOS;
	}
	
	public String getTransacaoGerenciarPagamentosConferir() {
		return TRANSACAO_GERENCIAR_PAGAMENTOS_CONFERIR;
	}
	
	public String getTransacaoProdutoAtualizarTodosLotes() {
		return TRANSACAO_PRODUTO_ATUALIZAR_TODOS_LOTES;
	}
	
	public String getTransacaoNfeGerarArquivo() {
		return TRANSACAO_NFE_GERAR_ARQUIVO;
	}
	
	public String getTransacaoLiberarEdicaoVenda() {
		return TRANSACAO_LIBERAR_EDICAO_VENDA;
	}
	
	public String getTransacaoLiberarSeparacaoMercadoria() {
	    return TRANSACAO_LIBERAR_SEPARACAO_MERCADORIA;
	}
	public String getTransacaoClienteExcluirAnotacao() {
	    return TRANSACAO_CLIENTE_EXCLUIR_ANOTACAO;
	}
	
	public String getTransacaoNfeCancelarEmissao() {
		return TRANSACAO_NFE_CANCELAR_EMISSAO;
	}
	
	public String getTransacaoAtendimentoConsultar() {
		return TRANSACAO_ATENDIMENTO_CONSULTAR;
	}
	
	public String getTransacaoAtendimentoInserir() {
		return TRANSACAO_ATENDIMENTO_INSERIR;
	}
	
	public String getTransacaoAtendimentoEditar() {
		return TRANSACAO_ATENDIMENTO_EDITAR;
	}
	
	public String getTransacaoAtendimentoExcluir() {
		return TRANSACAO_ATENDIMENTO_EXCLUIR;
	}

	public String getTransacaoLiberarVendaNaoAutorizada() {
		return TRANSACAO_LIBERAR_VENDA_NAO_AUTORIZADA;
	}
	
	public String getTransacaoClienteGestao() {
		return TRANSACAO_CLIENTE_GESTAO;
	}
	
	public String getTransacaoRelatorioRealizacoesFuncionario() {
		return TRANSACAO_RELATORIO_REALIZACOES_FUNCIONARIO;
	}
	
	public String getTransacaoAdminTipoDebito() {
		return TRANSACAO_ADMIN_TIPO_DEBITO;
	}
	
	public String getTransacaoAdminFontePagadora() {
		return TRANSACAO_ADMIN_FONTE_PAGADORA;
	}
	
	public String getTransacaoRelatorioVendas() {
		return TRANSACAO_RELATORIO_VENDAS;
	}
	
	public String getTransacaoRelatorioParaOCliente() {
		return TRANSACAO_RELATORIO_PARA_O_CLIENTE;
	}
	
	public String getTransacaoRelatorioPeriodicidadeCliente() {
		return TRANSACAO_RELATORIO_PERIODICIDADE_CLIENTE;
	}
	
	public String getTransacaoRelatorioVendasXAno() {
		return TRANSACAO_RELATORIO_VENDAS_X_ANO;
	}
	
	public String getTransacaoRelatorioVendasXGrupo() {
		return TRANSACAO_RELATORIO_VENDAS_X_GRUPO;
	}
	
	public String getTransacaoRelatorioContasXRecebimento() {
		return TRANSACAO_RELATORIO_CONTAS_X_RECEBIMENTO;
	}
	
	public String getTransacaoRelatorioVendasXProduto() {
		return TRANSACAO_RELATORIO_VENDAS_X_PRODUTO;
	}
	
	public String getTransacaoRelatorioVendasXInstituicao() {
		return TRANSACAO_RELATORIO_VENDAS_X_INSTITUICAO;
	}
	
	public String getTransacaoRelatorioFechamentoMensal() {
		return TRANSACAO_RELATORIO_FECHAMENTO_MENSAL;
	}
	
	/**
	 * Lista todas as transações e as transforma
	 * em roles, retornado um List<String> contento
	 * o nome de cada role.
	 * @return
	 */
	public static List<String> getTodasTransacoes(){
		
		List<String> allRoles = new ArrayList<String>();
		
		try{
			Method[] metodos = Transacoes.class.getDeclaredMethods();
			Object[] argumentos = new Object[0]; 
		
			for (Method metodo: metodos) {
				String nomeMetodo = metodo.getName();
				if(!"getTodasTransacoes".equals(nomeMetodo)){
					allRoles.add((String) metodo.invoke(new Transacoes(), argumentos));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return allRoles;		
	}
	
}
