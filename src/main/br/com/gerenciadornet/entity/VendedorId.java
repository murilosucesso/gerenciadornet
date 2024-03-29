package br.com.gerenciadornet.entity;

// Generated 09/02/2010 16:40:06 by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

/**
 * VendedorId generated by hbm2java
 */
@SuppressWarnings("serial")
@Embeddable
public class VendedorId implements java.io.Serializable {

	private int codVendedor;
	private int codFornecedor;

	public VendedorId() {
	}

	public VendedorId(int codVendedor, int codFornecedor) {
		this.codVendedor = codVendedor;
		this.codFornecedor = codFornecedor;
	}

	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cod_vendedor", nullable = false)
	public int getCodVendedor() {
		return this.codVendedor;
	}

	public void setCodVendedor(int codVendedor) {
		this.codVendedor = codVendedor;
	}

	@Column(name = "cod_fornecedor", nullable = false)
	public int getCodFornecedor() {
		return this.codFornecedor;
	}

	public void setCodFornecedor(int codFornecedor) {
		this.codFornecedor = codFornecedor;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VendedorId))
			return false;
		VendedorId castOther = (VendedorId) other;

		return (this.getCodVendedor() == castOther.getCodVendedor())
				&& (this.getCodFornecedor() == castOther.getCodFornecedor());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodVendedor();
		result = 37 * result + this.getCodFornecedor();
		return result;
	}

}
