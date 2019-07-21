package br.com.gerenciadornet.session;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Historico;
import br.com.gerenciadornet.entity.Recebimento;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;
import br.com.gerenciadornet.util.LogUtil;

@SuppressWarnings("serial")
@Name("recebimentoHome")
@Scope(ScopeType.CONVERSATION)
public class RecebimentoHome extends EntityHome<Recebimento> {

    @In
    EntityManager entityManager;

    @In(create = true)
    VendaHome vendaHome;

    @In(create = true)
    Usuario user;

    @In
    FacesMessages facesMessages;

    boolean wired = false;

    private Integer currentRow;

    Recebimento recebimentoSelected;

    @Override
    protected Recebimento createInstance() {
	Recebimento pagamento = new Recebimento();
	return pagamento;
    }

    public void load() {
	if (isIdDefined()) {
	    wire();
	}
    }

    public void wire() {
	getInstance();
	wired = true;
    }

    public boolean isWired() {
	return true;
    }

    public Recebimento getDefinedInstance() {
	return isIdDefined() ? getInstance() : null;
    }

    public boolean novoRecebimento = false;

    /**
     * Cria uma nova instancia de recebimento para poder criar um novo
     * Recebimento.
     */
    public void novoRecebimento() {

	setInstance(new Recebimento());
	novoRecebimento = true;
    }

    @Override
    public String update() {
	    
	setInstance(recebimentoSelected);
	return super.update();
    }
    
    public String update2() {
	return super.update();
    }

    @Override
    public String persist() {
	
	Recebimento recebimento = getInstance();

	Venda venda = buscarVenda(recebimento.getVenda().getCodVenda());
	
	if(venda != null) {
	    recebimento.setVenda(venda);
	    return super.persist();
	} else {
	    return null;
	}
    }

    /**
     * 
     * @param codVenda
     * @return
     */
    private Venda buscarVenda(Integer codVenda) {

	StringBuilder query = new StringBuilder(
		"select venda from Venda venda where venda.codVenda = :codVenda ");

	Query createQuery = getEntityManager().createQuery(query.toString());
	createQuery.setParameter("codVenda", codVenda);

	try {
	    Venda venda = (Venda) createQuery.getSingleResult();
	    
	    int statusVenda = venda.getStatusVenda().getCodStatusVenda().intValue();
	    
	    if(statusVenda == Constantes.CANCELADA.intValue() ||
		    statusVenda == Constantes.ORCAMENTO.intValue() ) {
		
		addFacesMessage("Venda nao localizada. Status Inválido.",  FacesMessage.SEVERITY_ERROR);
		return null;
	    }
	    
	    
	    return venda;
	    
	} catch (NoResultException e) {
	    addFacesMessage("Venda nao localizada",  FacesMessage.SEVERITY_ERROR);
	    return null;
	}
    }

    /**
     * Exclui um recebimento caso a venda deste não tenha sido concluída. Grava
     * historico da exclusao.
     */
    @Override
    public String remove() {

	Venda venda = getInstance().getVenda();
	String retorno = null;

	if (user.getPerfil().getCodPerfil().intValue() == Constantes.ADMINISTRADOR.intValue()
		|| venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.CONCLUIDA.intValue()) {

	    String log = LogUtil.logHistorico("O recebimento valor: "
		    + getInstance().getValorAReceber() + " da VENDA COD: "
		    + venda.getCodVenda() + " foi excluido.");

	    Historico historico = new Historico(user, log, new Date(System.currentTimeMillis()));
	    entityManager.persist(historico);

	    retorno = super.remove();
	} else {
	    facesMessages.add("Somente o Administrador pode excluir o recebimento de uma venda CONCLUÍDA.",
		    FacesMessage.SEVERITY_INFO);
	}

	return retorno;
    }

    public void selectedRecebimento(Recebimento recebimento) {

	setRecebimentoSelected(recebimento);
	novoRecebimento = false;
    }

    /**
     * 
     * @param recebimento
     * @param indicadorConferencia
     */
    public void conferir(Recebimento recebimento, boolean conferir) {

	StringBuilder query = new StringBuilder(
		"update Recebimento r set r.inConferencia = :conferido ");

	if (recebimento.getDataBalancete() == null) {
	    query.append(", r.dataBalancete = :dataBalancete ");
	}

	if (recebimento.getValorPago() == null) {
	    query.append(", r.valorPago = :valorRecebido ");
	}

	query.append("WHERE r.codRecebimento = :codigoRecebimento ");

	Query createQuery = getEntityManager().createQuery(query.toString());

	// se passar false, nao ta conferido, entao confere.
	if (recebimento.getInConferencia() && conferir) {
	    createQuery.setParameter("conferido", true);
	} else {
	    createQuery.setParameter("conferido", false);
	}

	if (recebimento.getDataBalancete() == null) {
	    Date dataAtual = new Date(System.currentTimeMillis());
	    createQuery.setParameter("dataBalancete", dataAtual);
	    recebimento.setDataBalancete(dataAtual);
	}

	if (recebimento.getValorPago() == null) {
	    createQuery.setParameter("valorRecebido", recebimento.getValorAReceber());
	    recebimento.setValorPago(recebimento.getValorAReceber());
	}

	createQuery.setParameter("codigoRecebimento", recebimento.getCodRecebimento());

	createQuery.executeUpdate();

    }
    
    boolean inConferenciaTodos = false;
   
    /**
     * Método chamado pelo checkbox selecionar todos.
     * @param recebimentosList
     * @param conferir
     */
    public void conferirTodos(List<Recebimento> recebimentosList, boolean conferir) {

	for(Recebimento recebimento: recebimentosList) {
	    
	    // se passar false, nao ta conferido, entao confere.
	    if (inConferenciaTodos && conferir) {
		recebimento.setInConferencia(inConferenciaTodos);
		conferir(recebimento, true);
		
	    } else {
		recebimento.setInConferencia(inConferenciaTodos);
		conferir(recebimento, false);
		
	    }
	}
    }

    private Integer codVenda = null;
    
    /**
     * Chama o método vendaHome.andamentoFinalizarVenda() para 
     * realizar o andamento de finalizar uma venda.
     */
    public void finalizarVenda() {

	Venda venda = buscarVenda(codVenda);
	vendaHome.setInstance(venda);
	vendaHome.andamentoFinalizarVenda();

	//limpa o campo codVenda
	setCodVenda(null);

    }

    public Integer getCurrentRow() {
	return currentRow;
    }

    public void setCurrentRow(Integer currentRow) {
	this.currentRow = currentRow;
    }

    public boolean isNovoRecebimento() {
	return novoRecebimento;
    }

    public void setNovoRecebimento(boolean novoRecebimento) {
	this.novoRecebimento = novoRecebimento;
    }

    public Integer getCodVenda() {
        return codVenda;
    }

    public void setCodVenda(Integer novoCodVenda) {
        codVenda = novoCodVenda;
    }
    
    public Recebimento getRecebimentoSelected() {
	return recebimentoSelected;
    }

    public void setRecebimentoSelected(Recebimento recebimentoSelected) {
	this.recebimentoSelected = recebimentoSelected;
    }

    public void setProdutoCodProduto(Integer id) {
	setId(id);
    }

    public Integer getProdutoCodProduto() {
	return (Integer) getId();
    }

    public boolean isInConferenciaTodos() {
        return inConferenciaTodos;
    }

    public void setInConferenciaTodos(boolean novoInConferenciaTodos) {
        inConferenciaTodos = novoInConferenciaTodos;
    }
}
