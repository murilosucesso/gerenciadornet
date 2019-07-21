package br.com.gerenciadornet.entity;

// Generated 09/02/2010 16:40:06 by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Fornecedor generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "fornecedor")
public class Fornecedor implements java.io.Serializable {

	private Integer codFornecedor;
	private String nomeFantasia;
	private String razaoSocial;
	private String descFornecedor;
	private String cpjCnpj;
	private String inscricaoEstadual;
	private String fax;
	private String observacao;
	private Boolean inExclusao;
	private Boolean inContaFixa = false;
	private Boolean inAfeFuncionamento;
	private Set<Vendedor> vendedors = new HashSet<Vendedor>(0);
	private Set<Endereco> enderecos = new HashSet<Endereco>(0);
	private Set<Marca> marcas = new HashSet<Marca>(0);

	public Fornecedor() {
	}

	public Fornecedor(String nomeFantasia, Boolean inExclusao) {
		this.nomeFantasia = nomeFantasia;
		this.inExclusao = inExclusao;
	}

	public Fornecedor(String nomeFantasia, String razaoSocial,
			String descFornecedor, String cpjCnpj, String inscricaoEstadual,
			String fax, String observacao, Boolean inExclusao,
			Set<Vendedor> vendedors, Set<Endereco> enderecos) {
		this.nomeFantasia = nomeFantasia;
		this.razaoSocial = razaoSocial;
		this.descFornecedor = descFornecedor;
		this.cpjCnpj = cpjCnpj;
		this.inscricaoEstadual = inscricaoEstadual;
		this.fax = fax;
		this.observacao = observacao;
		this.inExclusao = inExclusao;
		this.vendedors = vendedors;
		this.enderecos = enderecos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_fornecedor", unique = true, nullable = false)
	public Integer getCodFornecedor() {
		return this.codFornecedor;
	}

	public void setCodFornecedor(Integer codFornecedor) {
		this.codFornecedor = codFornecedor;
	}

	@Column(name = "nome_fantasia", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNomeFantasia() {
		return this.nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	@Column(name = "razao_social", length = 100)
	@Length(max = 100)
	public String getRazaoSocial() {
		return this.razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@Column(name = "desc_fornecedor", length = 100)
	@Length(max = 100)
	public String getDescFornecedor() {
		return this.descFornecedor;
	}

	public void setDescFornecedor(String descFornecedor) {
		this.descFornecedor = descFornecedor;
	}

	@Column(name = "cpj_cnpj", length = 20)
	@Length(max = 20)
	public String getCpjCnpj() {
		return this.cpjCnpj;
	}

	public void setCpjCnpj(String cpjCnpj) {
		this.cpjCnpj = cpjCnpj;
	}

	@Column(name = "inscricao_estadual", length = 20)
	@Length(max = 20)
	public String getInscricaoEstadual() {
		return this.inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	@Column(name = "fax", length = 20)
	@Length(max = 20)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "observacao", length = 200)
	@Length(max = 200)
	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "in_exclusao", nullable = false)
	public Boolean getInExclusao() {
		return this.inExclusao;
	}

	public void setInExclusao(Boolean inExclusao) {
		this.inExclusao = inExclusao;
	}

	@Column(name = "in_afe_funcionamento", nullable = false)
	public Boolean getInAfeFuncionamento() {
	    return inAfeFuncionamento;
	}

	public void setInAfeFuncionamento(Boolean novoInAfeFuncionamento) {
	    inAfeFuncionamento = novoInAfeFuncionamento;
	}
	
	@Column(name = "in_conta_fixa", nullable = true)
	public Boolean getInContaFixa() {
	    return inContaFixa;
	}
	
	public void setInContaFixa(Boolean inContaFixa) {
	    this.inContaFixa = inContaFixa;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fornecedor")
	public Set<Vendedor> getVendedors() {
		return this.vendedors;
	}

	public void setVendedors(Set<Vendedor> vendedors) {
		this.vendedors = vendedors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fornecedor")
	public Set<Endereco> getEnderecos() {
		return this.enderecos;
	}

	public void setEnderecos(Set<Endereco> enderecos) {
		this.enderecos = enderecos;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fornecedor")
	public Set<Marca> getMarcas() {
		return this.marcas;
	}

	public void setMarcas(Set<Marca> marcas) {
		this.marcas = marcas;
	}
}