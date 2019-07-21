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
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "atendimento")
public class Atendimento{

	
	
	private Integer codAtendimento;
	private Integer codTipoAtendimento;
	private Date 	dataAtendimento;
	private String 	descAtendimento;
	private Integer statusAtendimento;
	private boolean inObtencaoExito = true;
	private Usuario usuario = new Usuario();
	private Cliente cliente = new Cliente();
	
	//transient
	private String 	nomeCliente;
	private String 	telefoneCliente;
	private String 	nomeFuncionario;

	public Atendimento() {
		super();		
	}
	
	public Atendimento(Integer codAtendimento, Integer codTipoAtendimento,
			Date dataAtendimento, String descAtendimento,
			Integer statusAtendimento, boolean inObtencaoExito,
			String nomeCliente, String 	telefoneCliente, String nomeFuncionario) {
		super();
		this.codAtendimento = codAtendimento;
		this.codTipoAtendimento = codTipoAtendimento;
		this.dataAtendimento = dataAtendimento;
		this.descAtendimento = descAtendimento;
		this.statusAtendimento = statusAtendimento;
		this.inObtencaoExito = inObtencaoExito;
		this.nomeCliente = nomeCliente;
		this.telefoneCliente = telefoneCliente; 
		this.nomeFuncionario = nomeFuncionario;
	}
	
	public Atendimento(Integer codAtendimento, Integer codTipoAtendimento,
			Date dataAtendimento, String descAtendimento,
			Integer statusAtendimento, boolean inObtencaoExito,
			Usuario usuario, Cliente cliente) {
		super();
		this.codAtendimento = codAtendimento;
		this.codTipoAtendimento = codTipoAtendimento;
		this.dataAtendimento = dataAtendimento;
		this.descAtendimento = descAtendimento;
		this.statusAtendimento = statusAtendimento;
		this.inObtencaoExito = inObtencaoExito;
		this.usuario = usuario;
		this.cliente = cliente;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_atendimento", unique = true, nullable = false)
	public Integer getCodAtendimento() {
		return codAtendimento;
	}

	public void setCodAtendimento(Integer codAtendimento) {
		this.codAtendimento = codAtendimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atendimento", nullable = false)
	@NotNull
	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}
	
	@Column(name = "desc_atendimento")
	public String getDescAtendimento() {
		return descAtendimento;
	}

	public void setDescAtendimento(String descAtendimento) {
		this.descAtendimento = descAtendimento;
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

	
	@Column(name = "cod_tipo_atendimento", nullable = false)	
	public Integer getCodTipoAtendimento() {
		return codTipoAtendimento;
	}

	public void setCodTipoAtendimento(Integer codTipoAtendimento) {
		this.codTipoAtendimento = codTipoAtendimento;
	}

	@Column(name = "cod_status_atendimento", nullable = false)	
	public Integer getStatusAtendimento() {
		return statusAtendimento;
	}

	public void setStatusAtendimento(Integer statusAtendimento) {
		this.statusAtendimento = statusAtendimento;
	}

	@Column(name = "in_obtencao_exito", nullable = false)	
	public boolean isInObtencaoExito() {
		return inObtencaoExito;
	}

	public void setInObtencaoExito(boolean inObtencaoExito) {
		this.inObtencaoExito = inObtencaoExito;
	}

	@Transient
	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	@Transient
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	@Transient
	public String getTelefoneCliente() {
		return telefoneCliente;
	}

	public void setTelefoneCliente(String telefoneCliente) {
		this.telefoneCliente = telefoneCliente;
	}

}


