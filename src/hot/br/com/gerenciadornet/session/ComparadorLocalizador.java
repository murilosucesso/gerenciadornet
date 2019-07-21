package br.com.gerenciadornet.session;

import java.io.Serializable;
import java.util.Comparator;

import br.com.gerenciadornet.entity.ProdutoVendido;

/**
 * Comparador de produtos por localizador do produto
 *
 */
public class ComparadorLocalizador implements Comparator<ProdutoVendido>, Serializable
{

    private static final long serialVersionUID = 1L;

    public int compare(final ProdutoVendido objeto1, final ProdutoVendido objeto2)
    {
    	if(objeto1.getLoteProduto().getProduto().getLocalizadorDefault() == null &&
    			objeto2.getLoteProduto().getProduto().getLocalizadorDefault() == null){
    		return 0;
    		
    	} else if(objeto1.getLoteProduto().getProduto().getLocalizadorDefault() == null ){
    		return -1;
    		
    	} else if(objeto2.getLoteProduto().getProduto().getLocalizadorDefault() == null){
    		return 1;
    	}
        return objeto1.getLoteProduto().getProduto().getLocalizadorDefault().compareTo(objeto2.getLoteProduto().getProduto().getLocalizadorDefault());
    }
}