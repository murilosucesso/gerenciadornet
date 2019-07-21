package br.com.gerenciadornet.dto;

/**
 * Utilizado para montar uma lista de produtos com nome da Marca. Pois se for
 * pegar o nome da marca de pois é realizado uma consulta para cada marca,
 * sobrecarregando o server.
 */
public class ProdutoDTO {

    private Integer codProduto;
    private String nomeProduto;
    private String codigoBarrasProduto;
    private String nomeMarca;

    public ProdutoDTO() {
    }
    
    public ProdutoDTO(Integer novoCodProduto, String novoNomeProduto,
	    String novoCodigoBarrasProduto, String novoNomeMarca) {
	super();
	codProduto 		= novoCodProduto;
	nomeProduto 		= novoNomeProduto;
	codigoBarrasProduto 	= novoCodigoBarrasProduto;
	nomeMarca 		= novoNomeMarca;
    }

    public Integer getCodProduto() {
	return codProduto;
    }

    public void setCodProduto(Integer novoCodProduto) {
	codProduto = novoCodProduto;
    }

    public String getNomeProduto() {
	return nomeProduto;
    }

    public void setNomeProduto(String novoNomeProduto) {
	nomeProduto = novoNomeProduto;
    }

    public String getCodigoBarrasProduto() {
	return codigoBarrasProduto;
    }

    public void setCodigoBarrasProduto(String novoCodigoBarrasProduto) {
	codigoBarrasProduto = novoCodigoBarrasProduto;
    }

    public String getNomeMarca() {
	return nomeMarca;
    }

    public void setNomeMarca(String novoNomeMarca) {
	nomeMarca = novoNomeMarca;
    }

}
