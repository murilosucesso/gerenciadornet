/**
 * 
 */
package br.com.gerenciadornet.session;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.ConteudoImagem;
import br.com.gerenciadornet.entity.ImagemProduto;

/**
 * @author osvaldeir
 * 
 */
@Name("imagemProdutoHome")
public class ImagemProdutoHome extends EntityHome<ImagemProduto> {

	private static final long serialVersionUID = 8404520770599563819L;

	public void removerImagem(ImagemProduto imagemProduto) {
		StringBuilder query = new StringBuilder(
				"delete from ImagemProduto im WHERE im.id = ?");
		Query createQuery = getEntityManager().createQuery(query.toString());
		createQuery.setParameter(1, imagemProduto.getId());
		createQuery.executeUpdate();
	}
	
	public ConteudoImagem buscarConteudoImagem(ImagemProduto imagemProduto){
		StringBuilder query = new StringBuilder(
		"SELECT im.conteudoImagem FROM ImagemProduto im WHERE im = ? ");
		Query createQuery = getEntityManager().createQuery(query.toString());
		createQuery.setParameter(1, imagemProduto);
		return (ConteudoImagem) createQuery.getSingleResult();
	}

}
