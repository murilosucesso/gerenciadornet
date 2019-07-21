package br.com.gerenciadornet.dto;

public class TotaisEstoqueDTO{

	private double 	totalEstoquePrecoCusto;
	private double 	totalEstoquePrecoVenda;
	private long	qtdTotalEstoque;
	private String marca;
	
	public TotaisEstoqueDTO(){
		
	}
	
	public TotaisEstoqueDTO(double totalEstoquePrecoCusto, double totalEstoquePrecoVenda)
	{
		super();
		this.totalEstoquePrecoCusto = totalEstoquePrecoCusto;
		this.totalEstoquePrecoVenda = totalEstoquePrecoVenda;
	}
	
	public TotaisEstoqueDTO(double totalEstoquePrecoCusto, double totalEstoquePrecoVenda, String marca)
	{
		super();
		this.totalEstoquePrecoCusto = totalEstoquePrecoCusto;
		this.totalEstoquePrecoVenda = totalEstoquePrecoVenda;
		this.marca = marca;
	}
	 
	public TotaisEstoqueDTO(long qtdTotalEstoque, double totalEstoquePrecoCusto, double totalEstoquePrecoVenda)
	{
		super();
		this.qtdTotalEstoque = qtdTotalEstoque;
		this.totalEstoquePrecoCusto = totalEstoquePrecoCusto;
		this.totalEstoquePrecoVenda = totalEstoquePrecoVenda;
	}
	
	public TotaisEstoqueDTO(long qtdTotalEstoque, double totalEstoquePrecoCusto, double totalEstoquePrecoVenda, String marca)
	{
		super();
		this.qtdTotalEstoque = qtdTotalEstoque;
		this.totalEstoquePrecoCusto = totalEstoquePrecoCusto;
		this.totalEstoquePrecoVenda = totalEstoquePrecoVenda;
		this.marca = marca;
	}

	public double getTotalEstoquePrecoCusto()
	{
		return totalEstoquePrecoCusto;
	}
	public void setTotalEstoquePrecoCusto(double totalEstoquePrecoCusto)
	{
		this.totalEstoquePrecoCusto = totalEstoquePrecoCusto;
	}
	public double getTotalEstoquePrecoVenda()
	{
		return totalEstoquePrecoVenda;
	}
	public void setTotalEstoquePrecoVenda(double totalEstoquePrecoVenda)
	{
		this.totalEstoquePrecoVenda = totalEstoquePrecoVenda;
	}

	public String getMarca()
	{
		return marca;
	}

	public void setMarca(String marca)
	{
		this.marca = marca;
	}

	public long getQtdTotalEstoque() {
		return qtdTotalEstoque;
	}

	public void setQtdTotalEstoque(long qtdTotalEstoque) {
		this.qtdTotalEstoque = qtdTotalEstoque;
	}

	
	
	
}
