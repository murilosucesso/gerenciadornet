package br.com.gerenciadornet.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "pagamento")
public class Pagamento implements Comparable<Pagamento>, Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codPagamento;
	private Compra compra;
	private Date dataBalancete;
	private Date dataVencimento;
	private Boolean inConferencia = false;
	private String observacao;
	private Fornecedor fornecedor = new Fornecedor();
	private TipoDebito tipoDebito = new TipoDebito();
	private FontePagadora fontePagadora = new FontePagadora();
	private Float valor;
	
	private Date dataCadastramento;// Data que foi cadastrado
	private Usuario usuarioCadastramento = new Usuario();// Usuário que cadastrou

	public Pagamento() {
		super();
	}
	
	public Pagamento(Compra novoCompra, Date novoDataVencimento, Date novaDataBalancete, Fornecedor novoFornecedor,
		Boolean novoInConferencia, TipoDebito novoTipoDebito,
		Float novoValor, String novaObservacao) {
	    
	    super();
	    compra = novoCompra;
	    dataBalancete = novaDataBalancete;
	    dataVencimento = novoDataVencimento;
	    fornecedor = novoFornecedor;
	    inConferencia = novoInConferencia;
	    tipoDebito = novoTipoDebito;
	    valor = novoValor;
	    observacao = novaObservacao;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_pagamento", unique = true, nullable = false)
	public Integer getCodPagamento() {
		return codPagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_compra")
	public Compra getCompra() {
		return this.compra;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_balancete", length = 0)
	public Date getDataBalancete() {
		return dataBalancete;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento", nullable = false, length = 0)
	@NotNull
	public Date getDataVencimento() {
		return dataVencimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_fornecedor", nullable = false)
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	@Column(name = "in_conferencia")
	public Boolean getInConferencia() {
		return inConferencia;
	}
	
	@Column(name = "observacao")
	public String getObservacao() {
		return observacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_tipo_debito", nullable = false)
	public TipoDebito getTipoDebito() {
		return tipoDebito;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_fonte_pagadora", nullable = true)
	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}

	@Column(name = "valor", precision = 12, scale = 0)
	public Float getValor() {
		return valor;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_usuario", nullable = true)
	public Usuario getUsuarioCadastramento() {
		return usuarioCadastramento;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cadastramento")
	public Date getDataCadastramento() {
		return dataCadastramento;
	}

	public void setCodPagamento(Integer codPagamento) {
		this.codPagamento = codPagamento;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public void setDataBalancete(Date dataBalancete) {
		this.dataBalancete = dataBalancete;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setInConferencia(Boolean inConferencia) {
		this.inConferencia = inConferencia;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setTipoDebito(TipoDebito tipoDebito) {
		this.tipoDebito = tipoDebito;
	}
	
	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
	
	public int compareTo(final Pagamento objeto1) {
		return this.getDataVencimento().after(objeto1.getDataVencimento()) ? 1
				: 0;
	}

	public void setDataCadastramento(Date dataCadastramento) {
		this.dataCadastramento = dataCadastramento;
	}

	public void setUsuarioCadastramento(Usuario usuarioCadastramento) {
		this.usuarioCadastramento = usuarioCadastramento;
	}

}
