package br.com.gerenciadornet.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@SuppressWarnings("serial")
@Embeddable
public class MensagemUsuarioId implements java.io.Serializable {

	private int codMensagem;
	private int codUsuario;

	public MensagemUsuarioId() {
	}

	public MensagemUsuarioId(int codMensagem, int codUsuario) {
		this.codMensagem = codMensagem;
		this.codUsuario = codUsuario;
	}

	@Column(name = "cod_mensagem", nullable = false, updatable=false)
	public int getCodMensagem() {
		return this.codMensagem;
	}

	public void setCodMensagem(int codMensagem) {
		this.codMensagem = codMensagem;
	}

	@Column(name = "cod_usuario", nullable = false, updatable=false)
	public int getCodUsuario() {
		return this.codUsuario;
	}

	public void setCodUsuario(int codUsuario) {
		this.codUsuario = codUsuario;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MensagemUsuarioId))
			return false;
		MensagemUsuarioId castOther = (MensagemUsuarioId) other;

		return (this.getCodMensagem() == castOther.getCodMensagem())
				&& (this.getCodUsuario() == castOther.getCodUsuario());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodMensagem();
		result = 37 * result + this.getCodUsuario();
		return result;
	}

}
