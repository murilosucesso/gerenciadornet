package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.dto.MensagemUsuarioDTO;
import br.com.gerenciadornet.entity.Mensagem;
import br.com.gerenciadornet.entity.MensagemUsuario;
import br.com.gerenciadornet.entity.MensagemUsuarioId;
import br.com.gerenciadornet.entity.Usuario;

@SuppressWarnings("serial")
@Name("mensagemUsuarioHome")
public class MensagemUsuarioHome extends EntityHome<MensagemUsuarioDTO> {

	@In
	EntityManager entityManager;

	@In
	Usuario user;
	
	@In
	FacesMessages facesMessages;

	Mensagem novaMensagem = new Mensagem();
	List<Usuario> usuariosDetino = new ArrayList<Usuario>();
	MensagemUsuarioDTO mensagem = new MensagemUsuarioDTO();// mensagem utilizada
															// na leitura
	Usuario usuarioDetino = new Usuario();// para teste unitario.

	public void setMarcaCodMarca(Integer id) {
		setId(id);
	}

	public Integer getMarcaCodMarca() {
		return (Integer) getId();
	}

	@Override
	protected MensagemUsuarioDTO createInstance() {
		MensagemUsuarioDTO mensagemUsuarioDTO = new MensagemUsuarioDTO();
		return mensagemUsuarioDTO;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public MensagemUsuarioDTO getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Mensagem getNovaMensagem() {
		return novaMensagem;
	}

	public void setNovaMensagem(Mensagem novoNovaMensagem) {
		novaMensagem = novoNovaMensagem;
	}

	public MensagemUsuarioDTO getMensagem() {
		return mensagem;
	}

	public void setMensagem(MensagemUsuarioDTO novoMensagem) {
		mensagem = novoMensagem;
	}

	/**
	 * Utilizado para limpar o objeto novaMensagem.
	 */
	public void criarNovaMensagem() {
		this.novaMensagem = new Mensagem();
	}

	/**
	 * Método utilizado para marcar uma mensagem como lida.
	 * 
	 * @param mensagem
	 */
	public void lerMensagem(MensagemUsuarioDTO mensagem) {

		// seta a mensagem na variável que será exibida na popup
		// includeMEnsagemLerPopup
		setMensagem(mensagem);

		if (!mensagem.getInLeitura()) {

			mensagem.setInLeitura(true);
			// 1 para leitura = true
			entityManager
					.createNativeQuery(
							"update mensagem_usuario set in_leitura = ? "
									+ ", data_leitura_mensagem = ? where cod_usuario = ? and cod_mensagem = ?")
					.setParameter(1, mensagem.getInLeitura())
					.setParameter(2, new Date(System.currentTimeMillis()))
					.setParameter(3, user.getCodUsuario())
					.setParameter(4, mensagem.getCodMensagem()).executeUpdate();
		}
	}
	

	public void salvarMensagem() {

		// Remetente
		novaMensagem.setUsuario(user);
		novaMensagem.setDataEnvio(new Date(System.currentTimeMillis()));
		
		if(novaMensagem.getMensagem() != null && novaMensagem.getMensagem().length() > 400){
			facesMessages.add("A mensagem excedeu o tamanho máximo de 400 caracteres.", FacesMessage.SEVERITY_ERROR);
			return; 
		}
		
		entityManager.persist(novaMensagem);

		MensagemUsuario mensagemUsuario = new MensagemUsuario();

		MensagemUsuarioId id = new MensagemUsuarioId();
		id.setCodMensagem(novaMensagem.getCodMensagem());
		id.setCodUsuario(usuarioDetino.getCodUsuario());

		mensagemUsuario.setId(id);
		mensagemUsuario.setUsuario(usuarioDetino);// Destinatário
		mensagemUsuario.setInExclusao(false);
		mensagemUsuario.setInLeitura(false);

		mensagemUsuario.setMensagem(novaMensagem);
		
		entityManager.persist(mensagemUsuario);
		
		facesMessages.add("Mensagem enviada com sucesso.", FacesMessage.SEVERITY_INFO);
		novaMensagem = new Mensagem(); 
		usuarioDetino = new Usuario();
		
	}

	public Usuario getUsuarioDetino() {
		return usuarioDetino;
	}

	public void setUsuarioDetino(Usuario novoUsuarioDetino) {
		usuarioDetino = novoUsuarioDetino;
	}

}
