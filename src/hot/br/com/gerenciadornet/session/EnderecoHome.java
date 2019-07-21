package br.com.gerenciadornet.session;

import java.util.Date;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("enderecoHome")
public class EnderecoHome extends EntityHome<Endereco> {

	@In(create = true)
	ClienteHome clienteHome;
	
	@In(create = true)
	FornecedorHome fornecedorHome;
	
	@In(create = true)
	UsuarioHome usuarioHome;

	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	public void setEnderecoCodEndereco(Integer id) {
		setId(id);
	}

	public Integer getEnderecoCodEndereco() {
		return (Integer) getId();
	}

	@Override
	protected Endereco createInstance() {
		Endereco endereco = new Endereco();
		return endereco;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	} 

	public void wire() {
		getInstance();
		/*Cliente cliente = clienteHome.getDefinedInstance();
		if (cliente != null) {
			getInstance().setCliente(cliente);
		}
		Fornecedor fornecedor = fornecedorHome.getDefinedInstance();
		if (fornecedor != null) {
			getInstance().setFornecedor(fornecedor);
		}
		Usuario usuario = usuarioHome.getDefinedInstance();
		if (usuario != null) {
			getInstance().setUsuario(usuario);
		}*/     
	}  
 
	public boolean isWired() {		
		/*if (getInstance().getUsuario() == null)
			return false;*/
		return true;
	}
  
	public Endereco getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	  
	@Override
	public String persist() {
		
		Endereco endereco = getInstance();		
		Cliente cliente = clienteHome.getInstance();
		Usuario usuario = usuarioHome.getInstance();
		Fornecedor fornecedor = fornecedorHome.getInstance();
		String destino = null;
		
		if(cliente.getCodCliente() != null){
			endereco.setCliente(cliente);
			destino = "/ClienteEdit.xhtml";
		}
		if(usuario.getLoginUsuario() != null){
			endereco.setUsuario(usuario);	
			destino = "/UsuarioEdit.xhtml";
		}
		if(fornecedor.getNomeFantasia() != null){
			endereco.setFornecedor(fornecedor);	
			destino = "/FornecedorEdit.xhtml";
		}		

		super.persist();
		clearInstance();
		
		return destino;
	}
	
	@Override
	public String remove() {
		
		Endereco endereco = getDefinedInstance();
		
		String log = LogUtil.logHistoricoDelete("Endereco", endereco.getCodEndereco(), 
				endereco.getEndereco().toUpperCase() +  endereco.getNumero().toUpperCase());
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		entityManager.remove(endereco);
		
		
		return "removed";
	}
}
