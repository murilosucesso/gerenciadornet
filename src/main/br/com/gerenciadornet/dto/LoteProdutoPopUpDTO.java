package br.com.gerenciadornet.dto;

import br.com.gerenciadornet.entity.LoteProduto;
import br.com.gerenciadornet.entity.Produto;

public class LoteProdutoPopUpDTO {
	
	Produto produto = new Produto();
	LoteProduto loteProduto = new LoteProduto();
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public LoteProduto getLoteProduto() {
		return loteProduto;
	}
	public void setLoteProduto(LoteProduto loteProduto) {
		this.loteProduto = loteProduto;
	}
	
}
