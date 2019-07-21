package br.com.gerenciadornet.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "recebimento")
public class Recebimento implements Comparable<Recebimento>, Serializable{
	
	private static final long	serialVersionUID	= 4051011774703766646L;
	private Integer codRecebimento;
	private Venda 	venda = new Venda();
	private Date 	dataAgendamentoRecebimento;
	private Date 	dataBalancete;
	private Float 	valorAReceber;
	private Float 	valorPago;
	private Float 	valorMulta;
	private String 	mumDocumento;
	private Boolean pagoEmDia = true;
	private Boolean inConferencia = false;
	
	public Recebimento()
	{
		super();
	}
	
	public Recebimento(Venda venda, Date dataAgendamentoRecebimento, Float valorAReceber)
	{
		super();
		this.venda = venda;
		this.dataAgendamentoRecebimento = dataAgendamentoRecebimento;
		this.valorAReceber = valorAReceber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_Recebimento", unique = true, nullable = false)
	public Integer getCodRecebimento()
	{
		return codRecebimento;
	}
	
	public void setCodRecebimento(Integer codRecebimento)
	{
		this.codRecebimento = codRecebimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_venda", nullable = false)
	@NotNull
	public Venda getVenda() {
		return this.venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_agendamento_Recebimento", nullable = false, length = 0)
	@NotNull
	public Date getDataAgendamentoRecebimento()
	{
		return dataAgendamentoRecebimento;
	}
	
	public void setDataAgendamentoRecebimento(Date dataRecebimentoAgendamento)
	{
		this.dataAgendamentoRecebimento = dataRecebimentoAgendamento;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_balancete", length = 0)
	public Date getDataBalancete()
	{
		return dataBalancete;
	}
	
	public void setDataBalancete(Date dataBalancete)
	{
		this.dataBalancete = dataBalancete;
	}
	
	@Column(name = "valor_a_receber", precision = 12, scale = 0)
	public Float getValorAReceber()
	{
		return valorAReceber;
	}
	
	public void setValorAReceber(Float valorAReceber)
	{
		this.valorAReceber = valorAReceber;
	}
	
	@Column(name = "valor_pago", precision = 12, scale = 0)
	public Float getValorPago()
	{
		return valorPago;
	}
	
	public void setValorPago(Float valorPago)
	{
		this.valorPago = valorPago;
	}
	
	@Column(name = "valor_multa", precision = 12, scale = 0)
	public Float getValorMulta()
	{
		return valorMulta;
	}
	
	public void setValorMulta(Float valorMulta)
	{
		this.valorMulta = valorMulta;
	}
	
	@Column(name = "num_documento", length = 40)
	@Length(max = 40)
	public String getNumDocumento()
	{
		return mumDocumento;
	}
	
	public void setNumDocumento(String mumDocumento)
	{
		this.mumDocumento = mumDocumento;
	}
	
	@Column(name = "pago_em_dia")
	public Boolean getPagoEmDia()
	{
		return pagoEmDia;
	}
	
	public void setPagoEmDia(Boolean pagoEmDia)
	{
		this.pagoEmDia = pagoEmDia;
	}
	
	@Column(name = "in_conferencia")
	public Boolean getInConferencia()
	{
		return inConferencia;
	}
	
	public void setInConferencia(Boolean inConferencia)
	{
		this.inConferencia = inConferencia;
	}
	
	 public int compareTo(final Recebimento objeto1)
     {
         return this.getDataAgendamentoRecebimento().after(objeto1.getDataAgendamentoRecebimento()) ? 1 : 0;
     }
}
