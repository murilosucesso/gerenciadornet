package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.Constantes;

@SuppressWarnings("serial")
@Name("usuarioList")
public class UsuarioList extends EntityQuery<Usuario> {
	private static final String EJBQL = "select usuario from Usuario usuario";

	private static final String[] RESTRICTIONS = {
			"lower(usuario.loginUsuario) like lower( concat('%', concat(#{usuarioList.usuario.loginUsuario},'%')))",
			"lower(usuario.nomeUsuario) like lower( concat('%',concat(#{usuarioList.usuario.nomeUsuario},'%')))",
			"usuario.perfil.codPerfil = #{usuarioList.usuario.perfil.codPerfil}",
			"lower(usuario.cpf) like lower(concat(#{usuarioList.usuario.cpf},'%'))",			
			"lower(usuario.numRg) like lower(concat(#{usuarioList.usuario.numRg},'%'))", 
			"usuario.codUsuario <> #{usuarioList.codAdministrador}",
			"usuario.inVendedorAtivo = #{usuarioList.usuario.inVendedorAtivo}",
			"usuario.inExclusao = #{usuarioList.usuario.inExclusao} "
			};

	private Usuario usuario = new Usuario();
	//codigo default de usuario.
	private Integer codAdministrador = new Integer(Constantes.CODIGO_ADMINISTRADOR);
	
	public UsuarioList() {			
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("usuario.nomeUsuario");
	}
	
	List<Usuario> usuariosList = new ArrayList<Usuario>();
	boolean consultaJaRealizada = false; // utilizado para evitar múltiplas chamdas do mesmo método.
	
	@Override
	public List<Usuario> getResultList() {
		
		if(consultaJaRealizada){
			return usuariosList;
		}

		usuariosList = super.getResultList();
		consultaJaRealizada = true;
		
		return usuariosList;
	}
	
	/**
	 * Retorna apenas os usuários ativos
	 * @return
	 */
	public List<Usuario> getResultListAtivos() {
		
		usuario.setInExclusao(false);
		
		if(consultaJaRealizada){
			return usuariosList;
		}

		usuariosList = super.getResultList();
		
		return usuariosList;
	}
	
	/**
	 * Retorna apenas os usuários ativos
	 * @return
	 */
	public List<Usuario> getResultListVendedoresAtivos() {
		
		usuario.setInExclusao(false);
		usuario.setInVendedorAtivo(true);
		
		if(consultaJaRealizada){
			return usuariosList;
		}

		usuariosList = super.getResultList();
		
		return usuariosList;
	}
	
	/**
	 * Metodo utilizado para preencher combos de Usuarios.
	 * @return List<Usuario>
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> getResultListCombo(){
				
		StringBuilder sql = new StringBuilder();
		sql.append("select new br.com.gerenciadornet.entity.Usuario(u.codUsuario, u.nomeUsuario) ");
		sql.append("from Usuario u ");
		sql.append("where u.inExclusao = :inExclusao ");
		sql.append("order by u.nomeUsuario");
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("inExclusao", false);
		
		return (List<Usuario>)query.getResultList();
	}
	
	public Usuario getUsuario() {	
		
		return usuario;
	}

	public Integer getCodAdministrador() {
		return codAdministrador;
	}
}
