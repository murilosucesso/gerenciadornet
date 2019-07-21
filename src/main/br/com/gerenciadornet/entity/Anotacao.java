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
@Table(name = "anotacao")
public class Anotacao{

	private Integer codAnotacao;
	private Date dataAnotacao;
	private String descAnotacao;
	private Usuario usuario = new Usuario();
	private Cliente cliente = new Cliente();

	public Anotacao(){
		
	}
	
	public Anotacao(Integer codAnotacao, Date dataAnotacao,
			String descAnotacao, Usuario usuario, Cliente cliente) {
		super();
		this.codAnotacao = codAnotacao;
		this.dataAnotacao = dataAnotacao;
		this.descAnotacao = descAnotacao;
		this.usuario = usuario;
		this.cliente = cliente;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_anotacao", unique = true, nullable = false)
	public Integer getCodAnotacao() {
		return codAnotacao;
	}

	public void setCodAnotacao(Integer codAnotacao) {
		this.codAnotacao = codAnotacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_anotacao", nullable = false)
	@NotNull
	public Date getDataAnotacao() {
		return dataAnotacao;
	}

	public void setDataAnotacao(Date dataAnotacao) {
		this.dataAnotacao = dataAnotacao;
	}
	
	@Column(name = "desc_anotacao", nullable = false, length = 200)
	@NotNull
	@Length(max = 200)
	public String getDescAnotacao() {
		return descAnotacao;
	}

	public void setDescAnotacao(String descAnotacao) {
		this.descAnotacao = descAnotacao;
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
	@JoinColumn(name = "cod_cliente", nullable = false)
	@NotNull
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}

