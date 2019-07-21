package br.com.gerenciadornet.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "mensagem_usuario")
public class MensagemUsuario {

    private MensagemUsuarioId id;
    private Mensagem mensagem;
    private Usuario usuario;
    private Boolean inLeitura = false;
    private Boolean inExclusao = false;
    private Date dataLeituraMensagem;

    @EmbeddedId
    @NotNull
    public MensagemUsuarioId getId() {
	return id;
    }

    public void setId(MensagemUsuarioId novoId) {
	id = novoId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_mensagem", nullable = false, insertable = false, updatable = false)
    @NotNull
    public Mensagem getMensagem() {
	return mensagem;
    }

    public void setMensagem(Mensagem novoMensagem) {
	mensagem = novoMensagem;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_usuario", nullable = false, insertable = false, updatable = false)
    @NotNull
    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario novoUsuario) {
	usuario = novoUsuario;
    }

    @Column(name = "in_leitura")
    public Boolean getInLeitura() {
	return inLeitura;
    }

    public void setInLeitura(Boolean novoInLeitura) {
	inLeitura = novoInLeitura;
    }

    @Column(name = "in_exclusao")
    public Boolean getInExclusao() {
	return inExclusao;
    }

    public void setInExclusao(Boolean novoInExclusao) {
	inExclusao = novoInExclusao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_leitura_mensagem")
    public Date getDataLeituraMensagem() {
	return dataLeituraMensagem;
    }

    public void setDataLeituraMensagem(Date novoDataLeituraMensagem) {
	dataLeituraMensagem = novoDataLeituraMensagem;
    }

}
