package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Compra;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.LoteProduto;
import br.com.gerenciadornet.entity.Produto;
import br.com.gerenciadornet.entity.ProdutoVendido;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("loteProdutoHome")
public class LoteProdutoHome extends EntityHome<LoteProduto> {

	@In(create = true)
	ProdutoHome produtoHome;
	
	@In(create = true)
	CompraHome compraHome;

	@In
	EntityManager entityManager;
	
	@In 
	Usuario user;
	
	public void setLoteProdutoCodLoteProduto(Integer id) {
		setId(id);
	}

	public Integer getLoteProdutoCodLoteProduto() {
		return (Integer) getId();
	}

	@Override
	protected LoteProduto createInstance() {
		LoteProduto loteProduto = new LoteProduto();
		return loteProduto;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Produto produto = produtoHome.getDefinedInstance();
		if (produto != null) {
			getInstance().setProduto(produto);
		}
		Compra compra = compraHome.getDefinedInstance();
		if (compra != null) {
			getInstance().setCompra(compra);
		}
	}

	public boolean isWired() {
		if (getInstance().getProduto() == null)
			return false;
		if (getInstance().getCompra() == null)
			return false;
		return true;
	}

	public LoteProduto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<ProdutoVendido> getProdutoVendidos() {
		return getInstance() == null ? null : new ArrayList<ProdutoVendido>(
				getInstance().getProdutoVendidos());
	}
	
	
	@Override
	public String update() {
		
		LoteProduto lote = getInstance();
		String nomeProduto = lote.getProduto().getNomeProduto();
		
		if(nomeProduto.length() > 10){
			nomeProduto.substring(0, 9);
		}
		
		return super.update();
	}
	
	public String balancete(){
		
		LoteProduto lote = getInstance();
		
		Produto produto = lote.getProduto();
		
		if(produto.getNomeProduto().length() > 10){
		    produto.getNomeProduto().substring(0, 9);
		}

		String log = LogUtil.logHistorico("BALANCETE: O Lote Produto: " + lote.getIdentificacaoLote() +  " - Produto: "
				+ produto.getCodProdutoExterno() + " - " + produto.getNomeProduto() 
				+ " Codigo de Barras: " + produto.getCodigoBarrasProduto()
				+ " teve a QTD atualizada para " + lote.getQtdEstoque() 
				+ ". ID_LOTE=" + lote.getCodLoteProduto());
		
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);		
		
		return super.update();
	}
}
