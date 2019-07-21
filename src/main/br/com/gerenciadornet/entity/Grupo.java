package br.com.gerenciadornet.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "grupo")
public class Grupo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codGrupo;
	private String nomeGrupo;
	private String descGrupo;
	private Set<Cliente> clientes = new HashSet<Cliente>(0);
	
	public Grupo() {
	}
	
	

	public Grupo(Integer codGrupo, String nomeGrupo) {
		super();
		this.codGrupo = codGrupo;
		this.nomeGrupo = nomeGrupo;
	}


	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_grupo", unique = true, nullable = false)	
	public Integer getCodGrupo() {
		return codGrupo;
	}

	public void setCodGrupo(Integer codGrupo) {
		this.codGrupo = codGrupo;
	}

	@Column(name = "nome_grupo", nullable = false, length = 20)
	public String getNomeGrupo() {
		return nomeGrupo;
	}

	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}

	@Column(name = "desc_grupo", length = 100)
	public String getDescGrupo() {
		return descGrupo;
	}

	public void setDescGrupo(String descGrupo) {
		this.descGrupo = descGrupo;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "grupo")
	public Set<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}
}
