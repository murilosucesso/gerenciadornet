package br.com.gerenciadornet.session;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.FontePagadora;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Pagamento;
import br.com.gerenciadornet.entity.TipoDebito;

@SuppressWarnings("serial")
@Name("pagamentoList")
@Scope(ScopeType.CONVERSATION)
public class PagamentoList extends EntityQuery<Pagamento> {

	private static final String EJBQL = "select pagamento from Pagamento pagamento"
			 + " left join fetch pagamento.usuarioCadastramento"
			 + " left join fetch pagamento.tipoDebito";

	private static String[] RESTRICTIONS = {
			"pagamento.fornecedor.codFornecedor = #{pagamentoList.pagamento.fornecedor.codFornecedor}",
			"pagamento.valor = #{pagamentoList.pagamento.valor}",

			"pagamento.compra.dataCompra >= #{pagamentoList.dataInicioCompra}",
			"pagamento.compra.dataCompra <= #{pagamentoList.dataFimCompra}",

			"pagamento.dataVencimento >= #{pagamentoList.dataInicioVencimento}",
			"pagamento.dataVencimento <= #{pagamentoList.dataFimVencimento}",

			"pagamento.dataBalancete >= #{pagamentoList.dataInicioBalancete}",
			"pagamento.dataBalancete <= #{pagamentoList.dataFimBalancete}",

			"pagamento.fornecedor.inContaFixa = #{pagamentoList.pagamento.fornecedor.inContaFixa}",
			
			"pagamento.inConferencia = #{pagamentoList.pagamento.inConferencia}",
			
			"lower(pagamento.observacao) like lower(concat('%', concat(#{pagamentoList.pagamento.observacao},'%')))",

			//"pagamento.tipoPagamento.codTipoPagamento = #{pagamentoList.tipoPagamento}", // Ainda não vinculado com tipoPagamento
			"pagamento.tipoDebito.codTipoDebito = #{pagamentoList.tipoDebito.codTipoDebito}",
			"pagamento.fontePagadora.codFontePagadora = #{pagamentoList.fontePagadora.codFontePagadora}",

	};

	private static String[] RESTRICTIONS2 = { "pagamento.compra.codCompra = #{pagamentoList.codigoCompra}" };

	private Pagamento pagamento = new Pagamento();

	private Date dataAtual = new Date();

	// Como default mostra a data atual
	private Date dataInicio = new Date(System.currentTimeMillis());

	// Como default mostra a data atual
	private Date dataFim = new Date(System.currentTimeMillis());

	private Date dataInicioCompra;
	private Date dataFimCompra;

	private Date dataInicioVencimento;
	private Date dataFimVencimento;

	private Date dataInicioBalancete;
	private Date dataFimBalancete;
	
	private Integer codigoCompra;

	private Integer inContaFixaAux = 2;
	
	private Integer inConferencia = 2;

	private TipoDebito tipoDebito = null;
	
	private FontePagadora fontePagadora = null;
	
	private Integer tipoPagamento;

	private boolean mostrarResultados = false;
	private Integer numeroLinhasDT = 20;

	private Float valorTotal = new Float(0);

	List<Pagamento> pagamentosList;
	Integer qtdResultados = 0;

	/**
	 * 1 - Pesquisa por Data Balancete 2 - Pesquisa por Data Compra 3 - Pesquisa
	 * por Data Pagamento
	 */
	private Integer tipoData = 1;

	public PagamentoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("pagamento.dataVencimento");		
	}

	/**
	 * 
	 */
	public void listarPagamentos() {
		
		//Caso não tenha selecionado nenhum Fornecedor, instancia um para evitar NullPointerException
		if(pagamento.getFornecedor() == null){
			Fornecedor fornecedor = new Fornecedor();
			pagamento.setFornecedor(fornecedor);
		}

		if (codigoCompra == null) {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		} else {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		}
		
		// limpar campos data. Para caso de nova pesquisa alterando o tipo da
		// data.
		setdataInicioVencimento(null);
		setdataFimVencimento(null); 
		setDataInicioCompra(null);
		setDataFimCompra(null);
		setDataInicioBalancete(null);
		setDataFimBalancete(null);

		if (getTipoData() == 1) {
			setdataInicioVencimento(getDataInicio());
			setdataFimVencimento(getDataFim());

		} else if (getTipoData() == 2) {
			setDataInicioCompra(getDataInicio());
			setDataFimCompra(new Date(getDataFim().getTime() + 86400000l));

		} else if (getTipoData() == 3) {
			setDataInicioBalancete(getDataInicio());
			setDataFimBalancete(getDataFim());
		}
		
		//Logica para setar InContaFixa
		if (getInContaFixaAux() == 0) {
			pagamento.getFornecedor().setInContaFixa(false);
			
		}else if (getInContaFixaAux() == 1) {
			pagamento.getFornecedor().setInContaFixa(true);
			
		}else { 		
			pagamento.getFornecedor().setInContaFixa(null);
		}

		//Logica para setar inConferencia
		if (getInConferencia() == 0) {
			pagamento.setInConferencia(false);
			
		}else if (getInConferencia() == 1) {
			pagamento.setInConferencia(true);
			
		} else {
			pagamento.setInConferencia(null);
		}

		List<Pagamento> pagamentos = super.getResultList();

		Float valorTotalVencimentoAux = new Float(0);

		for (Pagamento pag : pagamentos) {

			if (pag.getValor() != null) {
				valorTotalVencimentoAux += pag.getValor();
			}
			
		}
		setValorTotal(valorTotalVencimentoAux);

		qtdResultados = pagamentos.size();
		setPagamentosList(pagamentos);
		
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public Date getDataInicioCompra() {
		return dataInicioCompra;
	}

	public void setDataInicioCompra(Date dataInicioCompra) {
		this.dataInicioCompra = dataInicioCompra;
	}

	public Date getDataFimCompra() {
		return dataFimCompra;
	}

	public void setDataFimCompra(Date dataFimCompra) {
		this.dataFimCompra = dataFimCompra;
	}

	public Date getdataInicioVencimento() {
		return dataInicioVencimento;
	}

	public void setdataInicioVencimento(Date dataInicioVencimento) {
		this.dataInicioVencimento = dataInicioVencimento;
	}

	public Date getdataFimVencimento() {
		return dataFimVencimento;
	}

	public void setdataFimVencimento(Date dataFimVencimento) {
		this.dataFimVencimento = dataFimVencimento;
	}

	public Date getDataInicioBalancete() {
		return dataInicioBalancete;
	}

	public void setDataInicioBalancete(Date dataInicioBalancete) {
		this.dataInicioBalancete = dataInicioBalancete;
	}

	public Date getDataFimBalancete() {
		return dataFimBalancete;
	}

	public void setDataFimBalancete(Date dataFimBalancete) {
		this.dataFimBalancete = dataFimBalancete;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getTipoData() {
		return tipoData;
	}

	public void setTipoData(Integer tipoData) {
		this.tipoData = tipoData;
	}

	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public Float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public Date getDataAtual() {
		return dataAtual;
	}

	public Integer getCodigoCompra() {
		return codigoCompra;
	}

	public void setCodigoCompra(Integer codigoCompra) {
		this.codigoCompra = codigoCompra;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer novoNumeroLinhasDT) {
		numeroLinhasDT = novoNumeroLinhasDT;
	}
	
	public List<Pagamento> getPagamentosList() {
		return pagamentosList;
	}

	public void setPagamentosList(List<Pagamento> novoPagamentosList) {
		pagamentosList = novoPagamentosList;
	}

	public Integer getQtdResultados() {
		return qtdResultados;
	}
	
	public TipoDebito getTipoDebito() {
		return tipoDebito;
	}

	public void setTipoDebito(TipoDebito tipoDebito) {
		this.tipoDebito = tipoDebito;
	}

	public Integer getInConferencia() {
		return inConferencia;
	}

	public void setInConferencia(Integer inConferencia) {
		this.inConferencia = inConferencia;
	}

	public Integer getInContaFixaAux() {
		return inContaFixaAux;
	}

	public void setInContaFixaAux(Integer inContaFixaAux) {
		this.inContaFixaAux = inContaFixaAux;
	}

	public Integer getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(Integer tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}

	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

}
