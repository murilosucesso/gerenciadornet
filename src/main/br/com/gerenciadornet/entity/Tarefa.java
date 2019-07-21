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
@Table(name = "tarefa")
public class Tarefa{

	
	private Integer codTarefa;
	private Integer codTipoTarefa;
	private Date 	dataInclusaoAgendamento;//Data de realização do agendamento
	private Date 	dataRealizacaoTarefa;//Data futura, data de realização Tarefa
	private String 	descTarefa;
	private boolean inConclusao = false;//0 - Não Concluído, 1 - Concluído
	private Usuario usuario = new Usuario();
	private Cliente cliente = new Cliente();
	private Atendimento atendimento = new Atendimento();

		
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_tarefa", unique = true, nullable = false)
	public Integer getCodTarefa() {
		return codTarefa;
	}

	public void setCodTarefa(Integer codTarefa) {
		this.codTarefa = codTarefa;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inclusao_agendamento", nullable = false)
	@NotNull
	public Date getDataInclusaoAgendamento() {
		return dataInclusaoAgendamento;
	}

	public void setDataInclusaoAgendamento(Date dataInclusaoAgendamento) {
		this.dataInclusaoAgendamento = dataInclusaoAgendamento;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_realizacao_tarefa	", nullable = false)
	@NotNull
	public Date getDataRealizacaoTarefa() {
		return dataRealizacaoTarefa;
	}

	public void setDataRealizacaoTarefa(Date dataRealizacaoTarefa) {
		this.dataRealizacaoTarefa = dataRealizacaoTarefa;
	}
	
	@Column(name = "desc_tarefa", nullable = false, length = 200)
	@NotNull
	@Length(max = 200)
	public String getDescTarefa() {
		return descTarefa;
	}

	public void setDescTarefa(String descTarefa) {
		this.descTarefa = descTarefa;
	}

	@Column(name = "cod_tipo_tarefa", nullable = false)	
	public Integer getCodTipoTarefa() {
		return codTipoTarefa;
	}

	public void setCodTipoTarefa(Integer codTipoTarefa) {
		this.codTipoTarefa = codTipoTarefa;
	}

	@Column(name = "in_conclusao", nullable = false)	
	public Boolean isInConclusao() {
		return inConclusao;
	}

	public void setInConclusao(Boolean inConclusao) {
		this.inConclusao = inConclusao;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_atendimento", nullable = false)
	@NotNull
	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}
	
}


