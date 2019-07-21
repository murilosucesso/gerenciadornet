package br.com.gerenciadornet.entity;

// Generated 14/07/2010 16:57:06 by Hibernate Tools 3.2.5.Beta

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.NotNull;

/**
 * Acesso generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "acesso")
public class Acesso implements java.io.Serializable {

	private Usuario usuario = new Usuario();

	private Transacao transacao = new Transacao();
	
	private AcessoId id;
	
	public Acesso() {
	}

	public Acesso(AcessoId id) {
		this.id = id;
	}
	
	public Acesso(Usuario usuario, Transacao transacao) {
		this.usuario = usuario;
		this.transacao = transacao;
	}


	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codUsuario", column = @Column(name = "cod_usuario", nullable = false)),
			@AttributeOverride(name = "codTransacao", column = @Column(name = "cod_transacao", nullable = false)) })
	@NotNull
	public AcessoId getId() {
		return this.id;
	}

	public void setId(AcessoId id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cod_usuario", nullable = false, insertable = false, updatable = false)
	@NotNull
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cod_transacao", nullable = false, insertable = false, updatable = false)
	@NotNull
	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

}