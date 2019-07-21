package br.com.gerenciadornet.session;

import java.io.Serializable;
import java.util.Comparator;

import br.com.gerenciadornet.entity.ProdutoVendido;

/**
 * Comparador de produtos por nome do produto
 *
 */
public class ComparadorProdutos implements Comparator<ProdutoVendido>, Serializable
{

    private static final long serialVersionUID = 1L;

    public int compare(final ProdutoVendido objeto1, final ProdutoVendido objeto2)
    {
        return objeto1.getLoteProduto().getProduto().getNomeProduto().compareTo(objeto2.getLoteProduto().getProduto().getNomeProduto());
    }
}
