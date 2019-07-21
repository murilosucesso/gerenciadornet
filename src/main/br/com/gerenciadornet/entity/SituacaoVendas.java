package br.com.gerenciadornet.entity;

public class SituacaoVendas{
	
	//SITUAÇÕES QUE PODEM SER ASSUMIDAS PELO FINANCEIRO DO CLIENTE
	public static final String NENHUMA_VENDA 		= "Sem venda p/ cliente";
	public static final String EM_DIA 				= "Em dia";
	public static final String ABERTO_30 			= "Abertas até 30 Dias";
	public static final String ABERTO_60 			= "Abertas até 60 Dias";
	public static final String ABERTO_90 			= "Abertas até 90 Dias";
	public static final String ABERTO_Mais_90 		= "Abertas MAIS 91 Dias";
	
	public String getPRIMEIRA_COMPRA()
	{
		return NENHUMA_VENDA;
	}
	public String getEM_DIA()
	{
		return EM_DIA;
	}
	public String getABERTO_30()
	{
		return ABERTO_30;
	}
	public String getABERTO_60()
	{
		return ABERTO_60;
	}
	public String getABERTO_90()
	{
		return ABERTO_90;
	}
	public String getABERTO_Mais_90()
	{
		return ABERTO_Mais_90;
	}
	
	
	

}
