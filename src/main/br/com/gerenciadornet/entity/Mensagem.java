package br.com.gerenciadornet.entity;

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
@Table(name = "mensagem")
public class Mensagem implements java.io.Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer 	codMensagem;
    private String 	assunto;
    private String 	mensagem;
    private Date 	dataEnvio;

    /**
     * Usuário remetente
     */
    private Usuario 	usuario = new Usuario();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_mensagem", unique = true, nullable = false)
    public Integer getCodMensagem() {
        return codMensagem;
    }

    public void setCodMensagem(Integer novoCodMensagem) {
        codMensagem = novoCodMensagem;
    }

    @Column(name = "assunto", length = 100)
    @Length(max = 100)
    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String novoAssunto) {
        assunto = novoAssunto;
    }

    @Column(name = "mensagem", length = 400)
    @Length(max = 400)
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String novoMensagem) {
        mensagem = novoMensagem;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_envio", nullable = false)
    @NotNull
    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date novoDataEnvio) {
        dataEnvio = novoDataEnvio;
    }

    @ManyToOne(fetch = FetchType.LAZY)//testar com Eager
    @JoinColumn(name = "cod_usuario", nullable = false)
    @NotNull
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario novoUsuario) {
        usuario = novoUsuario;
    }

}
