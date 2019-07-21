package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Scope(ScopeType.CONVERSATION)
@Name("compraList")
public class CompraList extends EntityQuery<Compra> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select compra from Compra compra";

	private static String[] RESTRICTIONS = {
        "compra.codCompra = #{compraList.compra.codCompra}", 
        };
	
	private static final String[] RESTRICTIONS2 = {			
			"compra.vendedor.fornecedor.codFornecedor = #{compraList.compra.vendedor.fornecedor.codFornecedor}",
			"compra.numeroNotaFiscal = #{compraList.compra.numeroNotaFiscal}", 
			"compra.dataCompra >= #{compraList.dataInicial}",
			"compra.dataCompra <= #{compraList.dataFinal}",
			};

	private Compra compra = new Compra();
	
	private boolean mostrarResultados = false;
	private boolean pesquisaVazia;
	private int qtdResultados = 0;
	private Float precoCustoTotal = new Float(0);

	public CompraList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		setOrderColumn("compra.codCompra");
		setOrderDirection("desc");
		//setMaxResults(25);
	}

	/**
	 * Altera a pesquisa caso seja pesq. por códigoVenda, nao pode ficar
	 * dentro do resultList senao recarrega, executa a pesq. varias vezes,
	 * sem necessidade.
	 */
	public void setRestriction() {

		if (this.compra.getCodCompra() == null) {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
		} else {
			setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		}
	}

	@Override
    public List<Compra> getResultList() {
		
		List<Compra> compras = super.getResultList();
	        
        qtdResultados = compras.size();
        
        if(compras == null || qtdResultados == 0){
        	setPesquisaVazia(true);
        	return compras;
        } else {
        	setPesquisaVazia(false);
        }
                
        qtdResultados = compras.size();
	    
        Float precoCustoTotalAux = new Float(0);
		
		for (Compra compraAux : compras) {								
			precoCustoTotalAux += compraAux.getPrecoTotal();		
		}
		
		setPrecoCustoTotal(precoCustoTotalAux);
		
		return compras;
	}

	public Compra getCompra() {
		return compra;
	}
	
	private Date dataInicial = new Date();
			
	private Date dataFinal = new Date();
	
	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		if(dataFinal != null){			
			return new Date(dataFinal.getTime() + 86400000l);
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}
		
	public boolean isPesquisaVazia() {
		return pesquisaVazia;
	}
	
	public int getQtdResultados() {
		return qtdResultados;
	}
	
	public void setPesquisaVazia(boolean pesquisaVazia) {
		this.pesquisaVazia = pesquisaVazia;
	}

	public void setQtdResultados(int qtdResultados) {
		this.qtdResultados = qtdResultados;
	}

	public Float getPrecoCustoTotal() {
		return precoCustoTotal;
	}

	public void setPrecoCustoTotal(Float precoCustoTotal) {
		this.precoCustoTotal = precoCustoTotal;
	}
}
