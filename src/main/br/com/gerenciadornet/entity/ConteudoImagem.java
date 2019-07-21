/* 
 * ConteudoImagem.java
 * 
 * Data de criação: 10/11/2010
 * 
 * CORPORATIVO - MTE. 
 *
 */
package br.com.gerenciadornet.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Classe responsável por representar o conteúdo em bytes da imagem cadastrada
 * devido a especificação JPA não garantir que a implementação @Basic(Lazy) pode
 * ou não ser executada. Criando uma associação One-to-One pode-se garantir o
 * Lazy.
 * 
 * @author Osvaldeir
 * @since 10/11/2010
 * 
 */
@Entity
@Table(name="conteudo_imagem")
public class ConteudoImagem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private byte[] bytes;
	
	private ImagemProduto imagemProduto;
	
	/**
	 * Retorna o(a) id.
	 * 
	 * @return Integer
	 */
	@Id
	@Column(name="cod_conteudo_imagem")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	/**
	 * Atribui o(a) id.
	 *
	 * @param id Integer
	 */
	
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Retorna o(a) bytes.
	 * 
	 * @return byte[]
	 */
	@Lob
	@Column(name="bytes", nullable=false, length=1310720)
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * Atribui o(a) bytes.
	 *
	 * @param bytes byte[]
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * Retorna o(a) imagemProduto.
	 * 
	 * @return ImagemProduto
	 */
	@OneToOne
	@JoinColumn(name="cod_imagem_produto")
	public ImagemProduto getImagemProduto() {
		return imagemProduto;
	}

	/**
	 * Atribui o(a) imagemProduto.
	 *
	 * @param imagemProduto ImagemProduto
	 */
	public void setImagemProduto(ImagemProduto imagemProduto) {
		this.imagemProduto = imagemProduto;
	}

}
