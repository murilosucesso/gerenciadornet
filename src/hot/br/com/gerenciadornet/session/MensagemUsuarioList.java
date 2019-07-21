package br.com.gerenciadornet.session;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.MensagemUsuarioDTO;
import br.com.gerenciadornet.entity.Usuario;

@SuppressWarnings("serial")
@Name("mensagemUsuarioList")
public class MensagemUsuarioList extends EntityQuery<MensagemUsuarioDTO> {

	private MensagemUsuarioDTO mensagemUsuarioDTO = new MensagemUsuarioDTO();

	public MensagemUsuarioDTO getMensagem() {
		return mensagemUsuarioDTO;
	}

	@In
	Usuario user;

	@In
	EntityManager entityManager;

	private static final String EJBQL = "select usuario from Usuario usuario";

	public Usuario getUser() {
		return user;
	}

	public MensagemUsuarioList() {
		setEjbql(EJBQL);
	}

	private boolean listaAtualizada = false;
	
	private boolean mensagesnEnviadas;
	
	public List<MensagemUsuarioDTO> mensagensList;

	
	public List<MensagemUsuarioDTO> listarMensagensRecebidas() {
		return listarMensagens(false);
	}
	
	public List<MensagemUsuarioDTO> listarMensagensEnviadas() {
		return listarMensagens(true);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MensagemUsuarioDTO> listarMensagens(boolean mensagemEnviada) {

		if (listaAtualizada)
			return mensagensList;

		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.dto.MensagemUsuarioDTO( ");
		sql.append("m.codMensagem, u.nomeUsuario, m.assunto, m.mensagem, m.dataEnvio, mu.inLeitura) ");
		sql.append("from MensagemUsuario mu, Mensagem m, Usuario u ");
		sql.append("where mu.mensagem.codMensagem = m.codMensagem ");
		
		if(mensagemEnviada){
			sql.append("and mu.usuario.codUsuario = u.codUsuario ");
			sql.append("and m.usuario.codUsuario = #{mensagemUsuarioList.user.codUsuario} ");
		}else{
			sql.append("and m.usuario.codUsuario = u.codUsuario ");
			sql.append("and mu.usuario.codUsuario = #{mensagemUsuarioList.user.codUsuario} ");
			sql.append("and mu.inExclusao = #{mensagemUsuarioList.inExclusao} ");
		}

		sql.append("order by m.codMensagem desc ");
		
		if(mensagemEnviada){
			sql.append("limit 8");
		}

		mensagensList = (List<MensagemUsuarioDTO>) entityManager.createQuery(
				sql.toString()).getResultList();

		for (int i = 0; i < mensagensList.size(); i++) {
			
			String assunto = mensagensList.get(i).getAssunto();
			
			if (assunto != null && assunto.length() > 30){
				mensagensList.get(i).setAssuntoResumido(assunto.substring(0, 30) + "...");
			} else {
				mensagensList.get(i).setAssuntoResumido(assunto);
			}
			
			//Mostra como se todas já tivessem sido lidas
			if(mensagemEnviada){
				mensagensList.get(i).setInLeitura(true);
			}
		}
		
		listaAtualizada = true;
		setMensagesnEnviadas(mensagemEnviada);

		return mensagensList;
	}
	
	// Utilizado para trazer somente as mensagens não excluídas.
	private Boolean inExclusao = false;

	public Boolean getInExclusao() {
		return inExclusao;
	}

	public List<MensagemUsuarioDTO> getMensagensList() {
		return mensagensList;
	}

	public void setMensagensList(List<MensagemUsuarioDTO> novoMensagensList) {
		mensagensList = novoMensagensList;
	}

	public void excluirMensagem(ActionEvent event) {

		MensagemUsuarioDTO mensagem = (MensagemUsuarioDTO) event.getComponent().getAttributes().get("mensagem");
		entityManager
				.createNativeQuery(
						"update mensagem_usuario set in_exclusao = ? "
								+ "where cod_usuario = ? and cod_mensagem = ?")
				.setParameter(1, true).setParameter(2, user.getCodUsuario())
				.setParameter(3, mensagem.getCodMensagem()).executeUpdate();

		mensagensList.remove(mensagem);
	}

	public boolean isMensagesnEnviadas() {
		return mensagesnEnviadas;
	}

	public void setMensagesnEnviadas(boolean mensagesnEnviadas) {
		this.mensagesnEnviadas = mensagesnEnviadas;
	}
}
