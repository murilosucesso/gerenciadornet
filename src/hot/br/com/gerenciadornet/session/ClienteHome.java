package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

import br.com.gerenciadornet.entity.Anotacao;
import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;
import br.com.gerenciadornet.util.LogUtil;
import br.com.gerenciadornet.util.Transacoes;
import br.com.gerenciadornet.util.Util;

@SuppressWarnings("serial")
@Name("clienteHome")
@Scope(ScopeType.CONVERSATION)
public class ClienteHome extends EntityHome<Cliente> {
	
	
	@In(create = true)
	EnderecoHome enderecoHome;
	
	@In(create = true)
	AnotacaoHome anotacaoHome;
	
	@In
	EntityManager entityManager;
	
	@In
	Usuario user;
	
	@In
	Identity identity;
	
	@In(create = true)
	Transacoes transacoes;
		
	private double valorTotalVendasEmAberto;
	
	//Vinculado ao popup de novo endereço
	Endereco novoEnderecoEntity = new Endereco();
	
	//Boolean para informar se é novo ou apenas alteração
	public boolean novoEndereco = false;
	
	/*//lista de enderecos adicionados
	List<Endereco> novosEnderecosList;*/
			
	public void setClienteCodCliente(Integer id) {
		setId(id);
	}

	public Integer getClienteCodCliente() {
		return (Integer) getId();
	}

	@Override
	protected Cliente createInstance() {
		Cliente cliente = new Cliente();
		return cliente;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	} 

	public void wire() {
		//Cliente cliente = getInstance();
		/*if(cliente.getTipoPessoa() == Constantes.TIPO_PESSOA_PF){
			setCpf(cliente.getCpfCnpj());
		} else {
			setCnpj(cliente.getCpfCnpj());
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public Cliente getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	/**
	 * Lista todas as vendas de um cliente que estão em aberto.
	 * Utilizado na Cliente.xhtml
	 * 
	 * @return
	 */
	public List<Venda> getVendasEmAberto() {
		
		StringBuilder sql = new StringBuilder(
				"select venda from Venda venda WHERE " +
				"cod_cliente = :cod " +
				"and cod_status_venda <> :situacao1 " +
				"and cod_status_venda <> :situacao2 " +
				"and cod_status_venda <> :situacao3 " +
				"and in_orcamento = 0" 
				);
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("cod", getInstance().getCodCliente());
		query.setParameter("situacao1", Constantes.CONCLUIDA);
		query.setParameter("situacao2", Constantes.CANCELADA);
		query.setParameter("situacao3", Constantes.ORCAMENTO);
		
		@SuppressWarnings("unchecked")
		List<Venda> vendas = query.getResultList();
		List<Venda> vendasAux = new ArrayList<Venda>();
		Float valorTotalAux = new Float(0);
		Date dataAtual = new Date(System.currentTimeMillis());
		
		for (Venda venda : vendas) {				
			
			valorTotalAux += venda.getValorTotalVenda();
			vendasAux.add(venda);
			
			//Calcula a quantidade de dias que uma venda está aberta.
			venda.setSituacaoFinanceira(((int)Util.diferencaEmDias(venda.getDataInicioVenda(), dataAtual)) + " dia(s) em aberto");
		}
		setValorTotalVendasEmAberto(valorTotalAux);
		return vendasAux;
		
	}
	
	private boolean reativar = false;
	
	/**
	 * Retorna com um cliente que estava excluído.
	 * @return
	 */
	public String reativarCliente(){
	
		Cliente cliente = getInstance();
		cliente.setInExclusao(false);
		reativar = true;
		
		return this.persist();
	}
	
	@Override
	public String persist() {
		
		Cliente cliente = getInstance();
		
		//TODO igualar com o Dental DF...
		if(cliente.getCpfCnpj() != "" && !this.isCpfCnpjValido(cliente)){
			return null;
		}
		
		cliente.setNome( Util.formataNome(cliente.getNome()));
		
		if(cliente.getGrupo() != null && cliente.getGrupo().getCodGrupo() == null) {
		    cliente.setGrupo(null);
		}
		
		if(cliente.getUsuario() != null && cliente.getUsuario().getCodUsuario() == null) {
		    cliente.setUsuario(null);
		}
		
		if(cliente.getSenha() != null && cliente.getSenha().length() > 0){
			cliente.setSenha(Util.md5(cliente.getSenha()));
		}
		
		cliente.setDtCadastro(new Date(System.currentTimeMillis()));
		
		if(!cliente.isInEstudante()){
			limparCamposEstudante(cliente);
		}	
		
		entityManager.persist(cliente);
		
		return "persisted";
		
	}
	
	/**
     * Cria uma nova instancia de endereco para poder criar um novo novoEndereco.
     */
    public void novoEndereco() {
    	
    	novoEnderecoEntity = new Endereco();    	
    	novoEndereco = true;
    }

    /**
	 * Adiciona um endereço ao Set de endereços.
	 * Tem que setar no endereço o cliente para o  CascadeType.ALL
	 * funcionar na hora de inserir o cliente e ele adicionar automaticamente
	 * os endereços da lista.
	 */
	public void adicionarEndereco(){
		
		Cliente cliente = getInstance();
		novoEnderecoEntity.setCliente(cliente);
		cliente.getEnderecos().add(novoEnderecoEntity);
		 novoEndereco() ;
	}
	
		
	/**
	 * VAlida um cpf ou cpnj e verifica se já está cadastrado na base.
	 * @param cliente
	 * @return
	 */
	private boolean isCpfCnpjValido(Cliente cliente){
		
		if(cliente.isInEstudante()){
			return true;
		}
		
		if(cliente.getTipoPessoa() ==  Constantes.TIPO_PESSOA_PF){
			
			if(cliente.getCpfCnpj() == null || cliente.getCpfCnpj().trim().equals("")){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campo CPF obrigatório", "");
		        FacesContext.getCurrentInstance().addMessage("err", fm);
		        return false;
			} else {
				if(isOutroClienteCadastrado(cliente.getCpfCnpj())){
					FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Já existe um cliente cadastrado para o CPF " +cliente.getCpfCnpj() , "");
					FacesContext.getCurrentInstance().addMessage("err", fm);
					return false;
				}
			} 
			//cliente.setCpfCnpj(getCpf());
		} else {
			if(cliente.getCpfCnpj() == null || cliente.getCpfCnpj().trim().equals("")){
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campo CNPJ obrigatório", "");
		        FacesContext.getCurrentInstance().addMessage("err", fm);
				return false;
			}else {
				if(isOutroClienteCadastrado(cliente.getCpfCnpj())){
					FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Já existe um cliente cadastrado para o CNPJ " + cliente.getCpfCnpj(), "");
			        FacesContext.getCurrentInstance().addMessage("err", fm);
			        return false;
				}
			}
			//cliente.setCpfCnpj(getCnpj());
		}
		return true;
	}
	
	private void limparCamposEstudante(Cliente cliente){
		cliente.setNomeResponsavel(null);
		cliente.setMatriculaEstudante(null);
		cliente.setGrauCurso(null);
	}
	
	
	/**
	 * Se já existir outro cliente cadastrado para o cpfCnpj
	 * retorna true, caso contrário false.
	 * @param cpfCnpj
	 * @return
	 */
	private boolean isOutroClienteCadastrado(String cpfCnpj){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select cliente from Cliente cliente WHERE ");
		sql.append("cpf_cnpj = :cod  ");
		
		//Verifica se existe algum cliente cadastrado com id diferente
		//pois a consulta só por cpf/cnpj retorna o próprio cliente.
		if(reativar){
			sql.append("and cod_cliente <> :codCliente ");
		}
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("cod", cpfCnpj);
		
		if(reativar){
			query.setParameter("codCliente", getInstance().getCodCliente());
		}
		
		@SuppressWarnings("unchecked")
		List<Cliente> clienteList = query.getResultList();
		
		//Se fez a consulta e achou alguém.. pode ser o mesmo ou 
		//outro que já existe. Senão é pq é novo cliente.
		if(clienteList != null && clienteList.size() >0 ){
			for(Cliente clienteAux : clienteList){
				
				if(clienteAux.getCodCliente() == getInstance().getCodCliente()){
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Exclui um endereco e grava o historico.
	 * Recebe o encereco como parametro para poder excluir
	 * os que ainda não estão em BD. E recebe o enderecoCodEndereco
	 * para excluir os que já estão em BD.
	 * @return
	 */
	public String removeEndereco(Endereco endereco){
		
		enderecoHome.setInstance(endereco);
		
		if( endereco.getCodEndereco() != 0 ){
			enderecoHome.remove();
		} 
		
		Cliente cliente = getInstance();
		cliente.getEnderecos().remove(endereco);
		
		return "removed";
	}
	
	/**
	 * Exclui um cliente que nao tem vendas associadas.<br/>
	 * Desativa um cliente que tem vendas associadas.<br/>
	 * Nao deixa excluir o cliente default Venda de Balcao 
	 * com codigo igual a 1.<br/>
	 * Grava historico do cliente excluido / desativado.
	 */
	@Override
	public String remove() {			
		
		if(!identity.hasRole(transacoes.getTransacaoClienteExcluir())){
			addFacesMessage("Usuário não possui acesso à transação: " + transacoes.getTransacaoClienteExcluir(), "");
			return null;
		}
		
		Cliente cliente = 	getInstance();
		String log = "";
		
		if(cliente.getCodCliente() == 1){
			addFacesMessage("O cliente " + cliente.getNome().toUpperCase() + " nao pode ser exluido.", "");			 
			return null;
		}
		
		if(cliente.getVendas().size() == 0){
			super.remove();
			addFacesMessage("O cliente " + cliente.getNome().toUpperCase() + " foi excluido com sucesso.", "");
			log = LogUtil.logHistoricoDelete("Cliente", cliente.getCodCliente(), cliente.getNome().toUpperCase());
			
		} else {		
			cliente.setInExclusao(true);
			update();
			log = LogUtil.logHistoricoDesativada("Cliente", cliente.getCodCliente(), cliente.getNome().toUpperCase());
			addFacesMessage("O cliente " + cliente.getNome().toUpperCase() + " foi desabilitado com sucesso.", "");
		}
				
		Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return "removed";
		
	}
	
	@Override
	public String update() {
		
		if(!identity.hasRole(transacoes.getTransacaoClienteEditar())){
			addFacesMessage("Usuário não possui acesso à transação: " + transacoes.getTransacaoClienteEditar(), "");
			return null;
		}
		
		Cliente cliente = 	getInstance();
		
		if(cliente.getCodCliente() == 1){
			addFacesMessage("Este cliente nao pode ser alterado.", "");			 
			return null;
		}

		//TODO igualar com o Dental DF...
		if(cliente.getCpfCnpj() != "" && !this.isCpfCnpjValido(cliente)){
			return null;
		}
		
		cliente.setNome( Util.formataNome(cliente.getNome()));
		
		String situacaoFinanceira = "Adimplente";
		
		if(!cliente.isInAdimplente()){
			situacaoFinanceira = "Inadimplente";
		}
		
		StringBuilder logHistorico = new StringBuilder();
		logHistorico.append("O Cliente codigo ");
		logHistorico.append(cliente.getCodCliente()); 
		logHistorico.append(" - ");
		logHistorico.append(cliente.getNome());
		logHistorico.append(" teve seus dados atualizados. Situação Financeira: ");
		logHistorico.append(situacaoFinanceira);
			 
		if(cliente.getSenha() != null && cliente.getSenha().length() > 0){
			cliente.setSenha(Util.md5(cliente.getSenha()));
			logHistorico.append(". Senha alterada");
		}
				
		Historico historico = new Historico(user, logHistorico.toString(), new Date(System.currentTimeMillis()));
		entityManager.persist(historico);
		
		return super.update();
	}
	
	/**
	 * Exclui uma anotação de um cliente
	 * @return
	 */
	public void removeAnotacao(Anotacao anotacao){
		
		Cliente cliente = getInstance();
		cliente.getAnotacoes().remove(anotacao);
		anotacaoHome.setInstance(anotacao);
		anotacaoHome.remove();
	}
	
	Anotacao novaAnotacao = new Anotacao();
	
	public void persistAnotacao(){
		
		Cliente cliente = getInstance();
		novaAnotacao.setCliente(cliente);
		novaAnotacao.setUsuario(user);
		novaAnotacao.setDataAnotacao(new Date(System.currentTimeMillis()));
		anotacaoHome.setInstance(novaAnotacao);
		String retorno = anotacaoHome.persist();
		
		if("persisted".equals(retorno)){
			cliente.getAnotacoes().add(novaAnotacao);
		}
		
		novaAnotacao = new Anotacao();
	}
	
	public String getSexoDesc() {
		if("M".equalsIgnoreCase(getInstance().getSexo())){
			return "Masculino";
		} 
		return "Feminino";
	}
	
	public List<Endereco> getEnderecos() {
		return getInstance() == null ? null : new ArrayList<Endereco>(
				getInstance().getEnderecos());
	}

	public List<Venda> getVendas() {
		return getInstance() == null ? null : new ArrayList<Venda>(
				getInstance().getVendas());
	}
	
	public List<Anotacao> getAnotacoes() {
		return getInstance() == null ? null : new ArrayList<Anotacao>(
				getInstance().getAnotacoes());
	}
	
	public double getValorTotalVendasEmAberto()
	{
		return valorTotalVendasEmAberto;
	}

	public void setValorTotalVendasEmAberto(double valorTotalVendasEmAberto)
	{
		this.valorTotalVendasEmAberto = valorTotalVendasEmAberto;
	}

	public Anotacao getNovaAnotacao() {
		return novaAnotacao;
	}

	public void setNovaAnotacao(Anotacao novaAnotacao) {
		this.novaAnotacao = novaAnotacao;
	}

	public Endereco getNovoEnderecoEntity() {
		return novoEnderecoEntity;
	}

	public void setNovoEnderecoEntity(Endereco novoEnderecoEntity) {
		this.novoEnderecoEntity = novoEnderecoEntity;
	}

	public boolean isNovoEndereco() {
		return novoEndereco;
	}

	public void setNovoEndereco(boolean novoEndereco) {
		this.novoEndereco = novoEndereco;
	}
	
}
