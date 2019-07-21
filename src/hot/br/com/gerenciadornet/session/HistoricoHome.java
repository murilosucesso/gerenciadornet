package br.com.gerenciadornet.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Historico;

@SuppressWarnings("serial")
@Name("historicoHome")
public class HistoricoHome extends EntityHome<Historico> {

	/*@In(create = true)
	UsuarioHome usuarioHome;*/

	public void setHistoricoCodHistorico(Integer id) {
		setId(id);
	}

	public Integer getHistoricoCodHistorico() {
		return (Integer) getId();
	}

	@Override
	protected Historico createInstance() {
		Historico historico = new Historico();
		return historico;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		/*Usuario usuario = usuarioHome.getDefinedInstance();
		if (usuario != null) {
			getInstance().setUsuario(usuario);
		}*/
	}

	public boolean isWired() {
		if (getInstance().getUsuario() == null)
			return false;
		return true;
	}

	public Historico getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
