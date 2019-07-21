package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.LoteProduto;

@SuppressWarnings("serial")
@Name("loteProdutoVencimentoList")
public class LoteProdutoVencimentoList extends EntityQuery<LoteProduto> {

	private static final String EJBQL = "select loteProduto from LoteProduto loteProduto"
	    + " left join fetch loteProduto.produto"
	    + " left join fetch loteProduto.produto.marca";

	private static final String[] RESTRICTIONS = { 
	    	"lower(loteProduto.produto.marca.nomeMarca) like lower ( concat('%', concat(#{loteProdutoVencimentoList.nomeMarca},'%')))",
		"loteProduto.dataValidade >= #{loteProdutoVencimentoList.dataInicial}",
		"loteProduto.dataValidade <= #{loteProdutoVencimentoList.dataFinal}",		 
		"loteProduto.qtdEstoque > #{loteProdutoVencimentoList.qtdMinima}",
	}; 
	
	private LoteProduto loteProduto = new LoteProduto();
	
	private String nomeMarca;
	
	private Date dataAtual =  new Date(System.currentTimeMillis());
	
	//Por default, mostra o que venceu ate um mes.
	private Date dataInicial = new Date(System.currentTimeMillis() - (30 * 86400000l));
		
	//Por default, mostra o que ira vencer no proximo mes.
	private Date dataFinal = new Date(System.currentTimeMillis() + (30 * 86400000l));

	public LoteProdutoVencimentoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("loteProduto.dataValidade");			
	}
	
	Float valorTotal = new Float(0);
	int quantidadeTotal;
	@Override
	public List<LoteProduto> getResultList() {

		Float valorTotalAux = new Float(0);
		int quantidadeAux = 0;

		List<LoteProduto> loteProduto = super.getResultList();
		
		for (LoteProduto loteProdutoAux : loteProduto) {								
				valorTotalAux += loteProdutoAux.getPrecoVendaUnidade();
				quantidadeAux += loteProdutoAux.getQtdEstoque();
		}
		setValorTotal(valorTotalAux);
		setQuantidadeTotal(quantidadeAux);
		return loteProduto;
		
	}
	
	//Utilizado para exibir a lista dos produtos que estão vencendo na tela inicial
	List<LoteProduto> produtosList = new ArrayList<LoteProduto>();
	boolean consultaJaRealizada = false; // utilizado para evitar múltiplas chamdas do mesmo método.
		 
	/**
	 * Retorna a lista de produtos que irão vencer no próximos 30 dias.
	 * Utilizado na página inicial.
	 * @return
	 */
	public  List<LoteProduto> listarProdutosVencendo(){
		
		setDataInicial(dataAtual);
		
		if(consultaJaRealizada){
			return produtosList;
		}
			
		produtosList = super.getResultList();
		consultaJaRealizada =  true;
				
		return produtosList;
	}
	
	public LoteProduto getLoteProduto() {
		return loteProduto;
	}
	
	private int qtdMinima = 0;
	
	public int getQtdMinima(){
		return qtdMinima;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public String getNomeMarca() {
	    return nomeMarca;
	}

	public void setNomeMarca(String novoNomeMarca) {
	    nomeMarca = novoNomeMarca;
	}

	public Float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(int quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}
	
}
