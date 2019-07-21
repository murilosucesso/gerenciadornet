package br.com.gerenciadornet.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import br.com.gerenciadornet.entity.Venda;

@Entity
@Table(name = "status_venda")
public class StatusVenda {
	
	private Integer codStatusVenda;
	
	private String descStatusVenda;
	
	private boolean inExclusao;
	
	private Set<Venda> vendas = new HashSet<Venda>(0);
	
	public StatusVenda() {
		super();
	}
	public StatusVenda(Integer codStatusVenda) {
		super();
		this.codStatusVenda = codStatusVenda;
	}

	public StatusVenda(Integer codStatusVenda, String descStatusVenda) {
		super();
		this.codStatusVenda = codStatusVenda;
		this.descStatusVenda = descStatusVenda;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_status_venda", unique = true, nullable = false)
	public Integer getCodStatusVenda() {
		return codStatusVenda;
	}

	public void setCodStatusVenda(Integer codStatusVenda) {
		this.codStatusVenda = codStatusVenda;
	}

	@Column(name = "desc_status_venda", nullable = false, length = 40)
	@NotNull
	@Length(max = 40)
	public String getDescStatusVenda() {
		return descStatusVenda;
	}

	public void setDescStatusVenda(String descStatusVenda) {
		this.descStatusVenda = descStatusVenda;
	}

	@Column(name = "in_exclusao", nullable = false)
	public boolean isInExclusao() {
		return inExclusao;
	}

	public void setInExclusao(boolean inExclusao) {
		this.inExclusao = inExclusao;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
	public Set<Venda> getVendas() {
		return this.vendas;
	}
	
	public void setVendas(Set<Venda> vendas) {
		this.vendas = vendas;
	}
	
}
