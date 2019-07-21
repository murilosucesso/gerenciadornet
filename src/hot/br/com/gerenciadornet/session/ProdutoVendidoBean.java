package br.com.gerenciadornet.session;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import br.com.gerenciadornet.entity.LoteProduto;

@Name("produtoVendidoBean")
@Scope(ScopeType.SESSION)
public class ProdutoVendidoBean {
	
	@DataModel
	List<LoteProduto> lotesProduto;
	/*@DataModelSelection("lotesProduto")
	LoteProduto selectedLoteProduto;*/
	
	LoteProdutoList loteProdutoPopUpList = new LoteProdutoList();
	
	public void testandoLink(LoteProduto loteProduto){
		
		System.out.println("\n\n --->>> ok, chamou com escopo SESSION ------");
		System.out.println("\n--->>> selected selectedLoteProduto = " + loteProduto.getIdentificacaoLote());
	}

	public ProdutoVendidoBean(){
		
	}
	public List<LoteProduto> getLotesProduto(){
		List<LoteProduto>lotesProdutoAux = loteProdutoPopUpList.getResultList();
		System.out.println("\n\n --->>> listaLotes ------ == " + lotesProdutoAux);
		System.out.println("\n\n --->>> listaLotes size------ == " + lotesProdutoAux.size());
		return lotesProdutoAux;
	}
}
