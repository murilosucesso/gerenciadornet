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

@SuppressWarnings("serial")
@Entity
@Table(name = "perfil")
public class Perfil implements java.io.Serializable {

	private Integer codPerfil;
	private String nomePerfil;
	private Set<AcessoDefault> acessosDefault = new HashSet<AcessoDefault>(0);

	private Set<Usuario> usuarios = new HashSet<Usuario>(0);

	public Perfil() {
	}

	public Perfil(Integer codPerfil, String nomePerfil) {
		this.codPerfil = codPerfil;
		this.nomePerfil = nomePerfil;
	}

	public Perfil(Integer codPerfil, String nomePerfil,
			Set<Usuario> usuarios) {
		this.codPerfil = codPerfil;
		this.nomePerfil = nomePerfil;
		this.usuarios = usuarios;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_perfil", unique = true, nullable = false)
	public Integer getCodPerfil() {
		return this.codPerfil;
	}

	public void setCodPerfil(Integer codPerfil) {
		this.codPerfil = codPerfil;
	}

	@Column(name = "nome_perfil", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	//@RoleName
	public String getNomePerfil() {
		return this.nomePerfil;
	}

	public void setNomePerfil(String nomePerfil) {
		this.nomePerfil = nomePerfil;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfil")
	public Set<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfil")
	public Set<AcessoDefault> getAcessosDefault() {
		return acessosDefault;
	}

	public void setAcessosDefault(Set<AcessoDefault> acessosDefault) {
		this.acessosDefault = acessosDefault;
	}
	
	

}
