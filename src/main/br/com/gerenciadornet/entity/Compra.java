package br.com.gerenciadornet.entity;

// Generated 09/02/2010 16:40:06 by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Compra generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "compra")
public class Compra implements java.io.Serializable {

	private Integer codCompra;
	private Vendedor vendedor = new Vendedor();
	private TipoPagamento tipoPagamento;
	private Usuario usuario;
	private float precoTotal;
	private String obsCompra;
	private String numeroNotaFiscal;
	private Date dataCompra;
	private Integer codStatusCompra;
	private Set<LoteProduto> loteProdutos = new HashSet<LoteProduto>(0);
	private Set<Pagamento> pagamentos 		= new HashSet<Pagamento>();
	
	private transient Float valorPrecoVendaTotal = new Float(0f);
	private transient Float lucroTotalCompra = new Float(0f);
	private transient int qtdTotalProduto = 0;
	
	public Compra() {
	}

	public Compra(Vendedor vendedor, TipoPagamento tipoPagamento,
			Usuario usuario, float precoTotal, Date dataCompra) {
		this.vendedor = vendedor;
		this.tipoPagamento = tipoPagamento;
		this.usuario = usuario;
		this.precoTotal = precoTotal;
		this.dataCompra = dataCompra;
	}

	public Compra(Vendedor vendedor, TipoPagamento tipoPagamento,
			Usuario usuario, float precoTotal, String obsCompra,
			String numeroNotaFiscal, Date dataCompra,
			Set<LoteProduto> loteProdutos) {
		this.vendedor = vendedor;
		this.tipoPagamento = tipoPagamento;
		this.usuario = usuario;
		this.precoTotal = precoTotal;
		this.obsCompra = obsCompra;
		this.numeroNotaFiscal = numeroNotaFiscal;
		this.dataCompra = dataCompra;
		this.loteProdutos = loteProdutos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_compra", unique = true, nullable = false)
	public Integer getCodCompra() {
		return this.codCompra;
	}

	public void setCodCompra(Integer codCompra) {
		this.codCompra = codCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "cod_vendedor", referencedColumnName = "cod_vendedor", nullable = false),
			@JoinColumn(name = "cod_fornecedor", referencedColumnName = "cod_fornecedor", nullable = false) })
	@NotNull
	public Vendedor getVendedor() {
		return this.vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_tipo_pagamento", nullable = false)
	@NotNull
	public TipoPagamento getTipoPagamento() {
		return this.tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_usuario", nullable = false)
	@NotNull
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "preco_total", nullable = false, precision = 12, scale = 0)
	public float getPrecoTotal() {
		return this.precoTotal;
	}

	public void setPrecoTotal(float precoTotal) {
		this.precoTotal = precoTotal;
	}

	@Column(name = "obs_compra", length = 100)
	@Length(max = 100)
	public String getObsCompra() {
		return this.obsCompra;
	}

	public void setObsCompra(String obsCompra) {
		this.obsCompra = obsCompra;
	}

	@Column(name = "numero_nota_fiscal", length = 45)
	@Length(max = 45)
	public String getNumeroNotaFiscal() {
		return this.numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_compra", nullable = false, length = 0)
	@NotNull
	public Date getDataCompra() {
		return this.dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "compra")
	public Set<LoteProduto> getLoteProdutos() {
		return this.loteProdutos;
	}

	public void setLoteProdutos(Set<LoteProduto> loteProdutos) {
		this.loteProdutos = loteProdutos;
	}

	@Column(name = "cod_status_compra", length = 1)
	public Integer getCodStatusCompra() {
		return codStatusCompra;
	}

	public void setCodStatusCompra(Integer codStatusCompra) {
		this.codStatusCompra = codStatusCompra;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "compra")
	public Set<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(Set<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	
	@Transient
	public int getQtdTotalProduto() {
		return qtdTotalProduto;
	}

	public void setQtdTotalProduto(int qtdTotalProduto) {
		this.qtdTotalProduto = qtdTotalProduto;
	}

	@Transient
	public Float getValorPrecoVendaTotal() {
		return valorPrecoVendaTotal;
	}

	public void setValorPrecoVendaTota(Float valorPrecoVendaTotal) {
		this.valorPrecoVendaTotal = valorPrecoVendaTotal;
	}

	@Transient
	public Float getLucroTotalCompra() {
		return lucroTotalCompra;
	}

	public void setLucroTotalCompra(Float lucroTotalCompra) {
		this.lucroTotalCompra = lucroTotalCompra;
	}

}
