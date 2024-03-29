package br.com.gerenciadornet.entity;

// Generated 09/02/2010 16:40:06 by Hibernate Tools 3.2.5.Beta

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/** 
 * Empresa generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "empresa")
public class Empresa implements java.io.Serializable {

	//dados da empresa
	private Integer codEmpresa;
	private String nomeFantasia;
	private String razaoSocial;
	private boolean inMatriz;
	private boolean inEmpresaAtiva;
	private boolean inContaAtrasada;
	private String cnpj;
	private String inscricaoEstadual;
	private String codRegimeTributario;
	
	//campos de endereco
	private String endereco;
	private String enderecoNumero;
	private String enderecoBairro;

	private String enderecoCodMunicipio;
	private String cep;
	private String cidade;
	private String estado;
	private String telefone;

	//parametros de sistema
	private int qtdUsuarios;
	
	//Indica como deve ser o fluxo das vendas. Resumido ou completo
	private Integer inFluxo;

	public Empresa() {
	}

	public Empresa(int qtdUsuarios, boolean inEmpresaAtiva) {
		this.qtdUsuarios = qtdUsuarios;
		this.inEmpresaAtiva = inEmpresaAtiva;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_empresa", unique = true, nullable = false)
	public Integer getCodEmpresa() {
		return this.codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	@Column(name = "qtd_usuarios", nullable = false)
	public int getQtdUsuarios() {
		return this.qtdUsuarios;
	}

	public void setQtdUsuarios(int qtdUsuarios) {
		this.qtdUsuarios = qtdUsuarios;
	}

	@Column(name = "in_empresa_ativa", nullable = false)
	public boolean isInEmpresaAtiva() {
		return this.inEmpresaAtiva;
	}

	public void setInEmpresaAtiva(boolean inEmpresaAtiva) {
		this.inEmpresaAtiva = inEmpresaAtiva;
	}
	
	@Column(name = "nome_fantasia", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNomeFantasia() {
		return this.nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	@Column(name = "razao_social", length = 100)
	@Length(max = 100)
	public String getRazaoSocial() {
		return this.razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@Column(name = "endereco", length = 100)
	@Length(max = 100)
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Column(name = "cep", length = 10)
	@Length(max = 10)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Column(name = "telefone", length = 20)
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Column(name = "cnpj", length = 13)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Column(name = "inscricao_estadual", length = 10)
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	@Column(name = "in_matriz")
	public boolean isInMatriz() {
		return inMatriz;
	}

	
	public void setInMatriz(boolean inMatriz) {
		this.inMatriz = inMatriz;
	}

	@Column(name = "cidade", length = 100)
	@Length(max = 100)
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@Column(name = "estado", length = 2)
	@Length(max = 2)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "cod_regime_tributario", length = 1)
	public String getCodRegimeTributario() {
		return codRegimeTributario;
	}

	public void setCodRegimeTributario(String codRegimeTributario) {
		this.codRegimeTributario = codRegimeTributario;
	}
	
	@Column(name = "endereco_numero")
	public String getEnderecoNumero() {
		return enderecoNumero;
	}

	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}
	
	@Column(name = "endereco_cod_municipio")
	public String getEnderecoCodMunicipio() {
		return enderecoCodMunicipio;
	}

	public void setEnderecoCodMunicipio(String enderecoCodMunicipio) {
		this.enderecoCodMunicipio = enderecoCodMunicipio;
	}
		
	@Column(name = "endereco_bairro")
	public String getEnderecoBairro() {
		return enderecoBairro;
	}

	public void setEnderecoBairro(String enderecoBairro) {
		this.enderecoBairro = enderecoBairro;
	}

	@Column(name = "in_conta_atrasada", nullable = false)
	public boolean isInContaAtrasada() {
		return inContaAtrasada;
	}

	public void setInContaAtrasada(boolean inContaAtrasada) {
		this.inContaAtrasada = inContaAtrasada;
	}

	@Column(name = "in_fluxo")
	public Integer getInFluxo() {
		return inFluxo;
	}

	public void setInFluxo(Integer inFluxo) {
		this.inFluxo = inFluxo;
	}

}

