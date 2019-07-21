/**
 * 
 */
package br.com.gerenciadornet.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Representa a imagen do produto a ser exibida.
 * 
 * @author osvaldeir
 * 
 */
@Entity
@Table(name = "imagem_produto")
public class ImagemProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Produto produto;

	private Integer ordemApresentacao;

	private String nome;

	private String extensao;

	private Long tamanho;

	private String mime;

	private ConteudoImagem conteudoImagem;

	@Column(name = "nome_imagem", nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
		if (nome != null) {

			int extDot = nome.lastIndexOf('.');
			if (extDot > 0) {
				String extension = nome.substring(extDot + 1);
				if ("bmp".equals(extension)) {
					mime = "image/bmp";
				} else if ("jpg".equals(extension)) {
					mime = "image/jpeg";
				} else if ("gif".equals(extension)) {
					mime = "image/gif";
				} else if ("png".equals(extension)) {
					mime = "image/png";
				} else {
					mime = "image/unknown";
				}
			}
		}
	}

	@Column(name = "extensao_imagem", nullable = false)
	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	@Id
	@Column(name = "cod_imagem_produto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		if (id != null && id == 0)
			setId(null);
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_produto")
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	/**
	 * Indica a ordem a qual as imagens devem ser apresentadas ao Usuário.
	 * 
	 * @return
	 */
	@Column(name = "ordem_apresentacao")
	public Integer getOrdemApresentacao() {
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}
	

	@Column(name = "tamanho", nullable = false)
	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}


	/**
	 * Retorna o(a) conteudoImagem.
	 * 
	 * @return ConteudoImagem
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "imagemProduto")
	@JoinColumn(name = "cod_conteudo_imagem")
	public ConteudoImagem getConteudoImagem() {
		return conteudoImagem;
	}

	/**
	 * Atribui o(a) conteudoImagem.
	 * 
	 * @param conteudoImagem
	 *            ConteudoImagem
	 */
	public void setConteudoImagem(ConteudoImagem conteudoImagem) {
		if (conteudoImagem != null) {
			conteudoImagem.setImagemProduto(this);
		}
		this.conteudoImagem = conteudoImagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((extensao == null) ? 0 : extensao.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tamanho == null) ? 0 : tamanho.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImagemProduto other = (ImagemProduto) obj;
		if (extensao == null) {
			if (other.extensao != null)
				return false;
		} else if (!extensao.equals(other.extensao))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (tamanho == null) {
			if (other.tamanho != null)
				return false;
		} else if (!tamanho.equals(other.tamanho))
			return false;
		return true;
	}

	@PostLoad
	public void postLoad() {
		this.setConteudoImagem(null);
	}

	/**
	 * Retorna o(a) mime.
	 * 
	 * @return String
	 */
	@Transient
	public String getMime() {
		return mime;
	}
}
