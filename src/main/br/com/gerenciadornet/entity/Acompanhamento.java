package br.com.gerenciadornet.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;


@Entity
@Table(name = "acompanhamento")
public class Acompanhamento{

	private Integer codAcompanhamento;
	private Date dataAcompanhamento;
	private String observacao;
	private Usuario usuario = new Usuario();
	private StatusVenda statusVenda = new StatusVenda();
	private Venda venda = new Venda();

	public Acompanhamento(){
		
	}
	
	public Acompanhamento(Date dataAcompanhamento, String observacao,
			Usuario usuario, StatusVenda statusVenda, Venda venda) {
		super();
		this.dataAcompanhamento = dataAcompanhamento;
		this.observacao = observacao;
		this.usuario = usuario;
		this.statusVenda = statusVenda;
		this.venda = venda;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_acompanhamento", unique = true, nullable = false)
	public Integer getCodAcompanhamento() {
		return codAcompanhamento;
	}

	public void setCodAcompanhamento(Integer codAcompanhamento) {
		this.codAcompanhamento = codAcompanhamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_acompanhamento", nullable = false, length = 0)
	@NotNull
	public Date getDataAcompanhamento() {
		return dataAcompanhamento;
	}

	public void setDataAcompanhamento(Date dataAcompanhamento) {
		this.dataAcompanhamento = dataAcompanhamento;
	}

	@Column(name = "observacao", length = 100)
	@Length(max = 100)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_usuario", nullable = false)
	@NotNull
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_status_venda", nullable = false)
	@NotNull
	public StatusVenda getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(StatusVenda statusVenda) {
		this.statusVenda = statusVenda;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_venda", nullable = false)
	@NotNull
	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
}

