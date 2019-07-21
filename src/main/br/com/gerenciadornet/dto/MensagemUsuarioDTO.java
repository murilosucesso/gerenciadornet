package br.com.gerenciadornet.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.gerenciadornet.entity.Usuario;

public class MensagemUsuarioDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private Integer 	codMensagem;
    private String 	nomeUsuarioRemetente;
    private Usuario 	usuarioRemetente;
    private String 	assunto;
    private String 	assuntoResumido;
    private String 	mensagem;
    private Date 		dataEnvio;
    private Boolean 	inLeitura;
    
    public MensagemUsuarioDTO() {
	
    }
    
    /**
     * Utilizado na listagem de mensagens
     * @param novoCodMensagem
     * @param novoNomeUsuarioRemetente
     * @param novoAssunto
     * @param novoMensagem
     * @param novoDataEnvio
     * @param novoInLeitura
     */
    public MensagemUsuarioDTO(Integer novoCodMensagem,
	    String novoNomeUsuarioRemetente, String novoAssunto, String novoMensagem,
	    Date novoDataEnvio, Boolean novoInLeitura) {
	
	super();
	codMensagem = novoCodMensagem;
	nomeUsuarioRemetente = novoNomeUsuarioRemetente;
	assunto = novoAssunto;
	mensagem = novoMensagem;
	dataEnvio = novoDataEnvio;
	inLeitura = novoInLeitura;
    }
    
    /**
     * Utilizado na criação de uma nova mensagem.
     * @param novoCodMensagem
     * @param usuarioRemetente
     * @param novoAssunto
     * @param novoMensagem
     * @param novoDataEnvio
     * @param novoInLeitura
     */
    public MensagemUsuarioDTO(Integer novoCodMensagem,
	    Usuario novoUsuarioRemetente, String novoAssunto, String novoMensagem,
	    Date novoDataEnvio, Boolean novoInLeitura) {
	
	super();
	codMensagem = novoCodMensagem;
	usuarioRemetente = novoUsuarioRemetente;
	assunto = novoAssunto;
	mensagem = novoMensagem;
	dataEnvio = novoDataEnvio;
	inLeitura = novoInLeitura;
    }

    public Integer getCodMensagem() {
	return codMensagem;
    }

    public void setCodMensagem(Integer novoCodMensagem) {
	codMensagem = novoCodMensagem;
    }

    public String getNomeUsuarioRemetente() {
	return nomeUsuarioRemetente;
    }

    public void setNomeUsuarioRemetente(String novoNomeUsuarioRemetente) {
	nomeUsuarioRemetente = novoNomeUsuarioRemetente;
    }

    public String getAssunto() {
	return assunto;
    }

    public void setAssunto(String novoAssunto) {
	assunto = novoAssunto;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String novoMensagem) {
        mensagem = novoMensagem;
    }

    public Date getDataEnvio() {
	return dataEnvio;
    }

    public void setDataEnvio(Date novoDataEnvio) {
	dataEnvio = novoDataEnvio;
    }

    public Boolean getInLeitura() {
	return inLeitura;
    }

    public void setInLeitura(Boolean novoInLeitura) {
	inLeitura = novoInLeitura;
    }

    public Usuario getUsuarioRemetente() {
        return usuarioRemetente;
    }

    public void setUsuarioRemetente(Usuario novoUsuarioRemetente) {
        usuarioRemetente = novoUsuarioRemetente;
    }

	public String getAssuntoResumido() {
		return assuntoResumido;
	}

	public void setAssuntoResumido(String assuntoResumido) {
		this.assuntoResumido = assuntoResumido;
	}

}
