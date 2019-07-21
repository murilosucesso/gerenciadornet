package br.com.gerenciadornet.entity;

//Generated 09/02/2010 16:40:06 by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;


@Entity
@Table(name = "fonte_pagadora")
@PrimaryKeyJoinColumn(name = "cod_fonte_pagadora")
public class FontePagadora implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer codFontePagadora;
	private String descFontePagadora;
	private boolean 	inExclusao;
	
	private Set<Pagamento> pagamento = new HashSet<Pagamento>(0);

	public FontePagadora() {
	}
	
	public FontePagadora(Integer codFontePagadora) {
		this.codFontePagadora = codFontePagadora;
	}

	public FontePagadora(Integer codFontePagadora, String descFontePagadora) {
		this.codFontePagadora = codFontePagadora;
		this.descFontePagadora = descFontePagadora;
	}

	public FontePagadora(Integer codFontePagadora, String descFontePagadora,
			Set<Pagamento> pagamento) {
		this.codFontePagadora = codFontePagadora;
		this.descFontePagadora = descFontePagadora;
		this.pagamento = pagamento;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_fonte_pagadora", unique = true, nullable = false)
	public Integer getCodFontePagadora() {
		return this.codFontePagadora;
	}

	public void setCodFontePagadora(Integer codFontePagadora) {
		this.codFontePagadora = codFontePagadora;
	}

	@Column(name = "desc_fonte_pagadora", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getDescFontePagadora() {
		return this.descFontePagadora;
	}

	public void setDescFontePagadora(String descFontePagadora) {
		this.descFontePagadora = descFontePagadora;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fontePagadora")
	public Set<Pagamento> getPagamentos() {
		return this.pagamento;
	}

	public void setPagamentos(Set<Pagamento> pagamento) {
		this.pagamento = pagamento;
	}

	@Column(name = "in_exclusao", nullable = false)
	public boolean isInExclusao() {
		return this.inExclusao;
	}

	public void setInExclusao(boolean inExclusao) {
		this.inExclusao = inExclusao;
	}
}

