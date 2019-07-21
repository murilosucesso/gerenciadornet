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
@Table(name = "tipo_debito")
@PrimaryKeyJoinColumn(name = "cod_tipo_debito")
public class TipoDebito implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer codTipoDebito;
	private String descTipoDebito;
	private boolean 	inExclusao;
	
	private Set<Pagamento> pagamento = new HashSet<Pagamento>(0);

	public TipoDebito() {
	}
	
	public TipoDebito(Integer codTipoDebito) {
		this.codTipoDebito = codTipoDebito;
	}

	public TipoDebito(Integer codTipoDebito, String descTipoDebito) {
		this.codTipoDebito = codTipoDebito;
		this.descTipoDebito = descTipoDebito;
	}

	public TipoDebito(Integer codTipoDebito, String descTipoDebito,
			Set<Pagamento> pagamento) {
		this.codTipoDebito = codTipoDebito;
		this.descTipoDebito = descTipoDebito;
		this.pagamento = pagamento;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_tipo_debito", unique = true, nullable = false)
	public Integer getCodTipoDebito() {
		return this.codTipoDebito;
	}

	public void setCodTipoDebito(Integer codTipoDebito) {
		this.codTipoDebito = codTipoDebito;
	}

	@Column(name = "desc_tipo_debito", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	public String getDescTipoDebito() {
		return this.descTipoDebito;
	}

	public void setDescTipoDebito(String descTipoDebito) {
		this.descTipoDebito = descTipoDebito;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDebito")
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

