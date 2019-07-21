package br.com.gerenciadornet.entity;

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


@Entity
@Table(name = "entidade")
public class Entidade implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codEntidade;
	private String nome;
	private Set<Cliente> clientes = new HashSet<Cliente>(0);
	
	public Entidade() {
	}

	public Entidade(Integer codEntidade, String nome) {
		super();
		this.codEntidade 	= codEntidade;
		this.nome 			= nome;
	}


	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_entidade", unique = true, nullable = false)	
	public Integer getCodEntidade() {
		return codEntidade;
	}

	public void setCodEntidade(Integer codEntidade) {
		this.codEntidade = codEntidade;
	}

	@Column(name = "nome", nullable = false, length = 100)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidade")
	public Set<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}
}
