package br.com.gerenciadornet.util;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

@Name("constantes")
@Startup
@Scope(ScopeType.APPLICATION)
public class Constantes {

	public static final int CODIGO_ADMINISTRADOR = 1;
	
	//TIPO DE PESSOA
	public static final boolean TIPO_PESSOA_PF = false;
	public static final boolean TIPO_PESSOA_PJ = true;

	//---- STATUS_VENDA  ------
	public final static Integer ABERTA 					= 1;//Nova Venda
	public final static Integer CONCLUIDA 				= 2;//Status final da venda
	public final static Integer CANCELADA 				= 3;//Venda Cancelada
	public final static Integer ORCAMENTO 				= 4;//Orçamento, não é venda ainda.
	public final static Integer CONFIRMADA_FINANCEIRO 	= 5;//Depois da venda aberta o financeiro libera.
	public final static Integer CONFIRMADA_ESTOQUE 		= 6;//ENTREGA LIBERADA
	public final static Integer PEDIDO					= 7;//PEDIDO FEITO PELO CLIENTE MAS SEM CONFIRMAÇÃO DE PAGAMENTO - UTILIZADO NO ECOMMERCE/GNb2b
	public final static Integer PEDIDO_AUTORIZADO 		= 8;//PEDIDO COM CONFIRMAÇÃO DE PAGAMENTO
	public final static Integer EM_EDICAO	 			= 9;//Icone do cafezinho.. Venda ainda não foi aberta.
	public final static Integer SEPARANDO_MATERIAL		= 10;//Iniciado o processo de separação do material
	public final static Integer EM_FASE_ENTREGA			= 11;//EM FASE DE ENTREGA DO MATERIAL
	
	
	//---- STATUS_COMPRA  ------
	public final static Integer COMPRA_EM_ANDAMENTO 	= 0;//Nova Venda
	public final static Integer COMPRA_CONCLUIDA 		= 1;//Status final da venda
		
		
	//CANAL - SISTEMA
	public final static Integer CANAL_GN	 			= 0;//(0 - GN, 1 - GN B2B, 2 GN ECOM)
	public final static Integer CANAL_GN_B2B 			= 1;//(0 - GN, 1 - GN B2B, 2 GN ECOM)
	public final static Integer CANAL_ECOMMERCE		    = 2;//(0 - GN, 1 - GN B2B, 2 GN ECOM)
	
	//PERFIL
	public final static Integer ADMINISTRADOR 	= 1;
	public final static Integer GERENTE 		= 2;
	public final static Integer VENDEDOR 		= 3;
	public final static Integer FINANCEIRO 		= 4;
	public final static Integer ESTOQUE 		= 5;
	public final static Integer SUPERVISOR 		= 6;
	
	//FLUXO
	public final static Integer IN_FLUXO_RESUMIDO = 0;
	public final static Integer IN_FLUXO_COMPLETO = 1;
		
	public int getCodigoAdministrador() {
		return CODIGO_ADMINISTRADOR;
	}
	public boolean isTipoPessoaPf() {
		return TIPO_PESSOA_PF;
	}
	public boolean isTipoPessoaPj() {
		return TIPO_PESSOA_PJ;
	}
	public Integer getEmEdicao() {
		return EM_EDICAO;
	}
	public Integer getAberta() {
		return ABERTA;
	}
	public Integer getConcluida() {
		return CONCLUIDA;
	}
	public Integer getCancelada() {
		return CANCELADA;
	}
	public Integer getOrcamento() {
		return ORCAMENTO;
	}
	public Integer getConfirmadaFinanceiro() {
		return CONFIRMADA_FINANCEIRO;
	}
	public Integer getConfirmadaEstoque() {
		return CONFIRMADA_ESTOQUE;
	}
	public Integer getSeparandoMaterial() {
	    return SEPARANDO_MATERIAL;
	}
	public Integer getEmFaseEntrega() {
	    return EM_FASE_ENTREGA;
	}
	public Integer getAdministrador() {
		return ADMINISTRADOR;
	}
	public Integer getGerente() {
		return GERENTE;
	}
	public Integer getVendedor() {
		return VENDEDOR;
	}
	public Integer getFinanceiro() {
		return FINANCEIRO;
	}
	public Integer getEstoque() {
		return ESTOQUE;
	}
	public Integer getSupervisor() {
		return SUPERVISOR;
	}
	public Integer getPedido() {
		return PEDIDO;
	}
	public Integer getPedidoAutorizado() {
		return PEDIDO_AUTORIZADO;
	}
	public Integer getCanalGn() {
		return CANAL_GN;
	}
	public Integer getCanalGnB2b() {
		return CANAL_GN_B2B;
	}
	public Integer getCanalEcommerce() {
		return CANAL_ECOMMERCE;
	}
	public Integer getCompraEmAndamento() {
		return COMPRA_EM_ANDAMENTO;
	}
	public Integer getCompraConcluida() {
		return COMPRA_CONCLUIDA;
	}
	public Integer getInFluxoResumido() {
		return IN_FLUXO_RESUMIDO;
	}
	public Integer getInFluxoCompleto() {
		return IN_FLUXO_COMPLETO;
	}
}
