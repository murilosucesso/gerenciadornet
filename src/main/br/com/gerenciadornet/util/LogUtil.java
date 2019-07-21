package br.com.gerenciadornet.util;

import br.com.gerenciadornet.entity.Acesso;
import br.com.gerenciadornet.entity.AcessoDefault;
import br.com.gerenciadornet.entity.Usuario;

public class LogUtil {
	
	
	private final static int TAMANHO_MAX_LOG = 200;
	/**
	 * Cria o log que sera salvo no historico quando uma
	 * entidade for excluida.
	 * @param Entidade
	 * @param cod
	 * @param nome
	 * @return
	 */
	public static String logHistoricoDelete(String Entidade, Integer cod, String nome){
		
		StringBuilder log = new StringBuilder();
		log.append("A entidade: " + Entidade + " codigo: " + cod + " , nome: " + nome + " foi excluida.");
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
	/**
	 * Cria o log que sera salvo no historico qndo uma
	 * entidade for desativada.
	 * @param Entidade
	 * @param cod
	 * @param nome
	 * @return
	 */
	public static String logHistoricoDesativada(String Entidade, Integer cod, String nome){
		
		StringBuilder log = new StringBuilder();
		log.append("A entidade: " + Entidade + " codigo: " + cod + " , nome: " + nome + " foi desativada.");

		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
		
	/**
	 * Cria log com a mensagem passada. 
	 * @param mensagem
	 * @return
	 */
	public static String logHistorico(String mensagem){
				
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(mensagem.length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = mensagem.length();
		
		return mensagem.substring(0, tamanhoMaxLog);
	}

	/**
	 * Cria o log que ser� salvo no historico qndo uma
	 * entidade for criada.
	 * @param Entidade
	 * @param nome
	 * @param user
	 * @return
	 */
	public static String logHistoricoUsuarioInsert(Usuario usuario){
		
		StringBuilder log = new StringBuilder();
		log.append("Usuario ");		
		log.append("nome " + usuario.getNomeUsuario().toUpperCase() );
		log.append(", login " + usuario.getLoginUsuario().toUpperCase() );
		log.append(", perfil " + usuario.getPerfil().getNomePerfil().toUpperCase() );		
		log.append(" incluido");
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
	/**
	 * Cria o log que sera salvo no historico qndo uma
	 * entidade for criada.
	 * @param Entidade
	 * @param nome
	 * @param user
	 * @return
	 */
	public static String logHistoricoUsuarioUpdate(Usuario usuario){
		
		StringBuilder log = new StringBuilder();
		log.append("Usuario ");		
		log.append(" cod " + usuario.getCodUsuario());
		log.append(", nome " + usuario.getNomeUsuario().toUpperCase() );
		log.append(", login " + usuario.getLoginUsuario().toUpperCase() );
		log.append(", perfil " + usuario.getPerfil().getNomePerfil().toUpperCase() );		
		log.append(" atualizado");
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
	/**
	 * Cria o log que sera salvo no historico qndo um
	 * acesso de um usuario for removido.
	 * @param Entidade
	 * @param nome
	 * @param user
	 * @return
	 */
	public static String logHistoricoAcessoRemove(Acesso acesso){
		
		StringBuilder log = new StringBuilder();
		log.append("O ACESSO a transacao ");
		log.append(acesso.getTransacao().getSiglaTransacao());
		log.append(" - ");
		log.append(acesso.getTransacao().getNome());
		log.append(" foi REMOVIDO do usuario " );
		log.append(acesso.getUsuario().getCodUsuario());
		log.append(" - " );
		log.append(acesso.getUsuario().getNomeUsuario());
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
	/**
	 * Cria o log que sera salvo no historico qndo um
	 * acesso de um usuario for removido.
	 * @param Entidade
	 * @param nome
	 * @param user
	 * @return
	 */
	public static String logHistoricoAcessoPersit(Acesso acesso){
		
		StringBuilder log = new StringBuilder();
		log.append("O ACESSO a transacao ");
		log.append(acesso.getTransacao().getSiglaTransacao());
		log.append(" - ");
		log.append(acesso.getTransacao().getNome());
		log.append(" foi CONCEDIDO ao usuario " );
		log.append(acesso.getUsuario().getCodUsuario());
		log.append(" - " );
		log.append(acesso.getUsuario().getNomeUsuario());
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
	/**
	 * Cria o log que sera salvo no historico quando um
	 * acesso for cadastrado a um perfil por default.
	 * @param Entidade
	 * @param nome
	 * @param user
	 * @return
	 */
	public static String logHistoricoAcessoDefaultRemove(AcessoDefault acesso){
		
		StringBuilder log = new StringBuilder();
		log.append("O ACESSO DEFUALT a transacao ");
		log.append(acesso.getTransacao().getSiglaTransacao());
		log.append(" - ");
		log.append(acesso.getTransacao().getNome());
		log.append(" foi REMOVIDO do perfil " );
		log.append(acesso.getPerfil().getCodPerfil());
		log.append(" - " );
		log.append(acesso.getPerfil().getNomePerfil());
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
	
	/**
	 * Cria o log que sera salvo no historico qndo um
	 * acesso de um usuario for removido.
	 * @param Entidade
	 * @param nome
	 * @param user
	 * @return
	 */
	public static String logHistoricoAcessoDefaultPersit(AcessoDefault acesso){
		
		StringBuilder log = new StringBuilder();
		log.append("O ACESSO DEFAULT a transacao ");
		log.append(acesso.getTransacao().getSiglaTransacao());
		log.append(" - ");
		log.append(acesso.getTransacao().getNome());
		log.append(" foi CONCEDIDO ao pefil" );
		log.append(acesso.getPerfil().getCodPerfil());
		log.append(" - " );
		log.append(acesso.getPerfil().getNomePerfil());
		
		int tamanhoMaxLog = TAMANHO_MAX_LOG;
		
		if(log.toString().length() < TAMANHO_MAX_LOG ) 
			tamanhoMaxLog = log.toString().length();
		
		return log.toString().substring(0, tamanhoMaxLog);
	}
}
