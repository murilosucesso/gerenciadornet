package br.com.gerenciadornet.entity;

// Generated 09/02/2010 16:40:06 by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Scope;

/**
 * Usuario generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "usuario")
@Scope(ScopeType.SESSION)
public class Usuario implements java.io.Serializable {

	private Integer codUsuario;
	private String loginUsuario;
	private String nomeUsuario;
	private String senha;
	private String cpf;
	private String numRg;
	private Date dataNasc;
	private String email;
	private String dadosBancarios;
	private Boolean inVendedorAtivo;
	private boolean inExclusao;
	private Perfil perfil = new Perfil();
	private Set<Endereco> enderecos = new HashSet<Endereco>(0);
	private Set<Venda> vendas = new HashSet<Venda>(0);
	private Set<Compra> compras = new HashSet<Compra>(0);
	private Set<Venda> vendas_1 = new HashSet<Venda>(0);
	private Set<Acesso> acessos = new HashSet<Acesso>(0);
	private Set<Historico> historicos = new HashSet<Historico>(0);
	private Set<Cliente> clientes = new HashSet<Cliente>(0);
	private Set<Anotacao> anotacoes = new HashSet<Anotacao>(0); 
	
	public Usuario() {
	}

	public Usuario(boolean inExclusao) {
		this.inExclusao = inExclusao;
	}
	
	public Usuario(Integer codUsuario, String nomeUsuario) {
		this.codUsuario = codUsuario;
		this.nomeUsuario = nomeUsuario;
	}
	
	public Usuario(String loginUsuario, String senha, Perfil perfil) {
		this.loginUsuario = loginUsuario;
		this.senha = senha;
		this.perfil = perfil;
	}

	public Usuario(String loginUsuario, String senha,
			String cpf, String numRg, Date dataNasc, Perfil perfil,Set<Endereco> enderecos,
			Set<Venda> vendas, Set<Compra> compras, Set<Venda> vendas_1,
			Set<Historico> historicos) {
		this.loginUsuario = loginUsuario;
		this.senha = senha;
		this.cpf = cpf;
		this.numRg = numRg;
		this.dataNasc = dataNasc;
		this.perfil = perfil;
		this.enderecos = enderecos;
		this.vendas = vendas;
		this.compras = compras;
		this.vendas_1 = vendas_1;
		this.historicos = historicos;
	}

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_usuario", unique = true, nullable = false)	
	public Integer getCodUsuario() {
		return this.codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}
	
	@Column(name = "nome_usuario", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	@Column(name = "login_usuario", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	@Pattern(regex="^\\w*$", message="Nome invalido. Nao digite caracteres especiais.")
	public String getLoginUsuario() {
		return this.loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	@Column(name = "senha", nullable = false, length = 20)
	@NotNull
	@Length(max = 20)
	//@UserPassword//(hash = "md5")
	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "cpf", length = 14)
	@Length(max = 14)
	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(name = "num_rg", length = 20)
	@Length(max = 20)
	public String getNumRg() {
		return this.numRg;
	}

	public void setNumRg(String numRg) {
		this.numRg = numRg;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_nasc", length = 0)
	public Date getDataNasc() {
		return this.dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}
	
	@Column(name = "email", length = 100)
	@Length(max = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "dados_bancarios", length = 100)
	@Length(max = 100)
	public String getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(String dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}
	
	@Column(name = "in_exclusao", nullable = false)
	public boolean isInExclusao() {
		return this.inExclusao;
	}

	public void setInExclusao(boolean inExclusao) {
		this.inExclusao = inExclusao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_perfil")
	//@UserRoles
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	@Column(name = "in_vendedor_ativo")
	public Boolean getInVendedorAtivo() {
		return inVendedorAtivo;
	}

	public void setInVendedorAtivo(Boolean inVendedorAtivo) {
		this.inVendedorAtivo = inVendedorAtivo;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Endereco> getEnderecos() {
		return this.enderecos;
	}

	public void setEnderecos(Set<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Venda> getVendas() {
		return this.vendas;
	}

	public void setVendas(Set<Venda> vendas) {
		this.vendas = vendas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Venda> getVendas_1() {
		return this.vendas_1;
	}

	public void setVendas_1(Set<Venda> vendas_1) {
		this.vendas_1 = vendas_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Historico> getHistoricos() {
		return this.historicos;
	}

	public void setHistoricos(Set<Historico> historicos) {
		this.historicos = historicos;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Acesso> getAcessos() {
		return acessos;
	}

	public void setAcessos(Set<Acesso> acessos) {
		this.acessos = acessos;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Anotacao> getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(Set<Anotacao> anotacoes) {
		this.anotacoes = anotacoes;
	}


	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codUsuario == null) ? 0 : codUsuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codUsuario == null) {
			if (other.codUsuario != null)
				return false;
		} else if (!codUsuario.equals(other.codUsuario))
			return false;
		return true;
	}*/
	
}
