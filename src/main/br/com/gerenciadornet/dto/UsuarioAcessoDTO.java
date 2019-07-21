package br.com.gerenciadornet.dto;

import br.com.gerenciadornet.entity.Acesso;
import br.com.gerenciadornet.entity.AcessoDefault;
import br.com.gerenciadornet.entity.Perfil;
import br.com.gerenciadornet.entity.Transacao;
import br.com.gerenciadornet.entity.Usuario;

/**
 * Utilizada para mostrar os dados de acesso de um usuário.
 * 
 * @author Murilo
 */
public class UsuarioAcessoDTO {

	private Transacao transacao;
	private Acesso acesso;
	private AcessoDefault acessoDefault;
	private Usuario usuario;
	private Perfil perfil;
	private boolean possuiAcesso = false;

	public UsuarioAcessoDTO() {
		super();
	}

	public UsuarioAcessoDTO(Transacao transacao) {
		super();
		this.transacao = transacao;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public boolean isPossuiAcesso() {
		return possuiAcesso;
	}

	public void setPossuiAcesso(boolean possuiAcesso) {
		this.possuiAcesso = possuiAcesso;
	}

	public Acesso getAcesso() {
		return acesso;
	}

	public void setAcesso(Acesso acesso) {
		this.acesso = acesso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public AcessoDefault getAcessoDefault() {
		return acessoDefault;
	}

	public void setAcessoDefault(AcessoDefault acessoDefault) {
		this.acessoDefault = acessoDefault;
	}
}
