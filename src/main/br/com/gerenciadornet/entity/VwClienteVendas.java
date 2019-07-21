package br.com.gerenciadornet.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "vw_cliente_vendas")
public class VwClienteVendas {

	/**
	 * Essa classe representa um View no BD. Codigo de criacao da view abaixo.
	 * 
	 * create or replace view vw_cliente_vendas as 
	 * select
	 * IFNULL(v.data_inicio_venda, c.dt_cadastro) as cod_vw_cliente_vendas,
	 * c.cod_cliente, 
	 * c.nome, 
	 * c.cod_usuario as responsavel, 
	 * IFNULL(g.cod_grupo, 0) as codigoGrupo,
	 * IFNULL(g.nome_grupo, 'Sem Grupo(Carteira)') as grupo, 
	 * count(v.cod_venda) as numero_vendas,
	 * IFNULL(DATE_FORMAT(v.data_inicio_venda, '%m'),0) as mes,
	 * IFNULL(DATE_FORMAT(v.data_inicio_venda, '%Y'),0) as ano,
	 * truncate(IFNULL(sum(v.valor_total_venda),0), 2) as valor_total_mes,
	 * truncate(IFNULL(sum(v.lucro_total_venda),0),2) as lucro_total_mes from
	 * cliente c left outer join venda v on c.cod_cliente = v.cod_cliente left
	 * outer join grupo g on c.cod_grupo = g.cod_grupo where v.in_orcamento = 0 and v.in_troca = 0
	 * and v.cod_status_venda <> 3 group by DATE_FORMAT(v.data_inicio_venda,'%M%Y') , 
	 * c.cod_cliente order by c.cod_cliente,
	 * DATE_FORMAT(v.data_inicio_venda, '%Y'), DATE_FORMAT(v.data_inicio_venda, '%m');
	 * 
	 */
	
	private Date 	codVwClienteVendas;
	private Integer codigoCliente;
	private String 	nome;
	private Integer responsavel; //codigoResponsavel
	private Integer codigoGrupo;
	private String 	grupo;//nome do grupo(carteira)
	private long 	numeroVendas;
	private Integer mes;
	private Integer ano;
	private float 	valorTotalMes;
	private float 	lucroTotalMes;

	public VwClienteVendas() {

	}

	public VwClienteVendas(Date codVwClienteVendas, Integer codigoCliente, String nome,
			long numeroVendas, Integer mes, Integer ano, float valorTotalMes,
			float lucroTotalMes) {
		super();
		this.codVwClienteVendas = codVwClienteVendas;
		this.codigoCliente = codigoCliente;
		this.nome = nome;
		this.numeroVendas = numeroVendas;
		this.mes = mes;
		this.ano = ano;
		this.valorTotalMes = valorTotalMes;
		this.lucroTotalMes = lucroTotalMes;
	}

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cod_vw_cliente_vendas")
	public Date getCodVwClienteVendas() {
		return codVwClienteVendas;
	}
	
	public void setCodVwClienteVendas(Date codVwClienteVendas) {
		this.codVwClienteVendas = codVwClienteVendas;
	}
		
	@Column(name = "cod_cliente")
	public Integer getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	@Column(name = "nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "responsavel")
	public Integer getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Integer responsavel) {
		this.responsavel = responsavel;
	}
	
	@Column(name = "numero_vendas")
	public long getNumeroVendas() {
		return numeroVendas;
	}

	public void setNumeroVendas(long numeroVendas) {
		this.numeroVendas = numeroVendas;
	}
	
	@Column(name = "mes")
	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
	@Column(name = "ano")
	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@Column(name = "valor_total_mes", precision = 12, scale = 0)
	public float getValorTotalMes() {
		return valorTotalMes;
	}

	public void setValorTotalMes(float valorTotalMes) {
		this.valorTotalMes = valorTotalMes;
	}

	@Column(name = "lucro_total_mes", precision = 12, scale = 0)
	public float getLucroTotalMes() {
		return lucroTotalMes;
	}

	public void setLucroTotalMes(float lucroTotalMes) {
		this.lucroTotalMes = lucroTotalMes;
	}

	@Column(name = "grupo")
	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public Integer getCodigoGrupo() {
		return codigoGrupo;
	}

	public void setCodigoGrupo(Integer codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}
}
