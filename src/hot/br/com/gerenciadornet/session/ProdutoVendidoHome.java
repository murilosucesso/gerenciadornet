package br.com.gerenciadornet.session;

import br.com.gerenciadornet.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@SuppressWarnings("serial")
@Name("produtoVendidoHome")
public class ProdutoVendidoHome extends EntityHome<ProdutoVendido> {

	@In(create = true)
	VendaHome vendaHome;
	@In(create = true)
	LoteProdutoHome loteProdutoHome;

	public void setProdutoVendidoId(ProdutoVendidoId id) {
		setId(id);
	}

	public ProdutoVendidoId getProdutoVendidoId() {
		return (ProdutoVendidoId) getId();
	}

	public ProdutoVendidoHome() {
		setProdutoVendidoId(new ProdutoVendidoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getProdutoVendidoId().getCodLoteProduto() == 0)
			return false;
		if (getProdutoVendidoId().getCodVenda() == 0)
			return false;
		return true;
	}

	@Override
	protected ProdutoVendido createInstance() {
		ProdutoVendido produtoVendido = new ProdutoVendido();
		produtoVendido.setId(new ProdutoVendidoId());
		return produtoVendido;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Venda venda = vendaHome.getDefinedInstance();
		if (venda != null) {
			getInstance().setVenda(venda);
		}
		LoteProduto loteProduto = loteProdutoHome.getDefinedInstance();
		if (loteProduto != null) {
			getInstance().setLoteProduto(loteProduto);
		}
	}

	public boolean isWired() {
		if (getInstance().getVenda() == null)
			return false;
		if (getInstance().getLoteProduto() == null)
			return false;
		return true;
	}

	public ProdutoVendido getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
