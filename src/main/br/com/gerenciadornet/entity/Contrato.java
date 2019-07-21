package br.com.gerenciadornet.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@SuppressWarnings("serial")
//@Entity
//@Table(name = "contrato")
public class Contrato implements java.io.Serializable {

	/*private Integer codContrato;
	private String nome;
	private String descContrato;
	private Date dataCadastro;
	private Date dataInicio;
	private Date dataFim;

	private Usuario usuarioCadastro;
	
	private Set<Venda> vendas = new HashSet<Venda>();

	public Contrato() {
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_contrato", unique = true, nullable = false)
	public Integer getCodContrato() {
		return this.codContrato;
	}

	public void setCodContrato(Integer codContrato) {
		this.codContrato = codContrato;
	}

	@Column(name = "nome", length = 100)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "desc_contrato", length = 400)
	public String getDescContrato() {
		return descContrato;
	}

	public void setDescContrato(String descContrato) {
		this.descContrato = descContrato;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cadastro", nullable = false, length = 0)
	@NotNull
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contrato")
	public Set<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(Set<Venda> vendas) {
		this.vendas = vendas;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_usuario_cadastro", nullable = false)
	@NotNull
	public Usuario getUsuarioCadastro() {
		return this.usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}
*/}
