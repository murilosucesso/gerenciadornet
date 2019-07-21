package br.com.gerenciadornet.session;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import br.com.gerenciadornet.entity.Compra;
import br.com.gerenciadornet.entity.Fornecedor;
import br.com.gerenciadornet.entity.Pagamento;
import br.com.gerenciadornet.entity.TipoPagamento;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.util.CalculosUtil;

@SuppressWarnings("serial")
@Name("pagamentoHome")
@Scope(ScopeType.CONVERSATION)
public class PagamentoHome extends EntityHome<Pagamento> {

	@In
	EntityManager entityManager;

	@In(create = true)
	TipoPagamentoList tipoPagamentoList;

	@In(create = true)
	FornecedorList fornecedorList;

	List<TipoPagamento> tipoPagamentos;
	List<Fornecedor> fornecedores;

	boolean wired = false;

	private Integer currentRow;

	Pagamento pagamentoSelected;

	private Integer codigoCompra;

	@In
	Usuario user;

	TipoPagamento tipoPagamento = new TipoPagamento();

	@Override
	protected Pagamento createInstance() {
		Pagamento pagamento = new Pagamento();
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
		if (this.instance.getFornecedor() == null
				|| this.instance.getValor() == null
				|| this.instance.getDataVencimento() == null) {
			return false;
		}
		return true;
	}

	public Pagamento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public boolean novoPagamento = false;

	public boolean listasCarregadas = false;

	/**
	 * Carregar as listas que serao utilizacadas nos combos para nao ficar
	 * recarregando em toda execucao.
	 */
	public void carregarListas() {

		if (!listasCarregadas) {
			tipoPagamentos = tipoPagamentoList.getResultList2();
			fornecedores = fornecedorList.getResultList();
			listasCarregadas = true;
		}
	}

	/**
	 * Cria uma nova instancia de pagamento para poder criar um novo Pagamento.
	 */
	public void novoPagamento() {

		setInstance(new Pagamento());
		novoPagamento = true;
	}

	@Override
	public String update() {

		// caso seja inserido codigo da compra, valida.
		if (codigoCompra != null) {
			
			Compra compra = new Compra();
			compra = validarCompra(codigoCompra);

			if (compra == null) {
				return null;
			}

			pagamentoSelected.setCompra(compra);
			codigoCompra = null;
		}
		
		pagamentoSelected.setUsuarioCadastramento(user);
		setInstance(pagamentoSelected);

		return super.update();
	}

	@Override
	public String persist() {
		
		Pagamento pagamento = getInstance();
		Compra compra = new Compra();

		// caso seja inserido codigo da compra, valida.
		if (codigoCompra != null) {

			compra = validarCompra(codigoCompra);

			if (compra == null) {
				return null;
			}

			pagamento.setCompra(compra);
			codigoCompra = null;
		}

		// Se não tiver tipo de pagamento - grava um único pagamento
		if (tipoPagamento == null || tipoPagamento.getCodTipoPagamento() == 0) {

			pagamento.setDataCadastramento(new Date(System.currentTimeMillis()));
			pagamento.setUsuarioCadastramento(user);
			return super.persist();

			// Grava os vários pagamentos de acordo com o tipo de pagamento.
		} else {
			if (compra.getCodCompra() == null) {
				gravarPagamentos(pagamento, null);
			} else {
				gravarPagamentos(pagamento, compra);
			}
			addFacesMessage("Pagamentos gravados com sucesso!", "");
			tipoPagamento = new TipoPagamento();
			return "persisted";
		}
	}

	/**
	 * Grava vários pagamentos baseado no campo tipoPagamento.
	 * 
	 * @param venda
	 * @param observacao
	 */
	private void gravarPagamentos(Pagamento pagamento, Compra compra) {

		// calcular datas de cada parcela do pagamento
		List<Date> datasVencimentos = CalculosUtil.calcularDatasPagamento(
				tipoPagamento, new Date(System.currentTimeMillis()));

		// calcular valores de cada parcela do pagamento
		List<Float> valoresPagamentos = CalculosUtil.calcularParcelas(
				tipoPagamento, pagamento.getValor());

		if (tipoPagamento.getNumVezes() == 0
				|| tipoPagamento.getNumVezes() == 1) {

			Pagamento pagamentoAux = new Pagamento(compra,
					pagamento.getDataVencimento(),
					pagamento.getDataBalancete(), pagamento.getFornecedor(),
					false, pagamento.getTipoDebito(), valoresPagamentos.get(0),
					pagamento.getObservacao());

			pagamentoAux.setDataCadastramento(new Date(System.currentTimeMillis()));
			pagamentoAux.setUsuarioCadastramento(user);
			pagamentoAux.setFontePagadora(pagamento.getFontePagadora());
			setInstance(pagamentoAux);

			super.persist();

		} else {
			for (int i = 0; i < tipoPagamento.getNumVezes(); i++) {

				Pagamento pagamentoAux = new Pagamento(compra,
						datasVencimentos.get(i), pagamento.getDataBalancete(),
						pagamento.getFornecedor(), false,
						pagamento.getTipoDebito(), valoresPagamentos.get(i),
						pagamento.getObservacao());

				pagamentoAux.setDataCadastramento(new Date(System.currentTimeMillis()));
				pagamentoAux.setUsuarioCadastramento(user);
				pagamentoAux.setFontePagadora(pagamento.getFontePagadora());
				setInstance(pagamentoAux);

				super.persist();
			}
		}
	}

	/**
	 * Recebe um código de compra e verifica se ele é válido, caso seja retorna
	 * a compra.
	 * 
	 * @return
	 */
	private Compra validarCompra(Integer codigoCompra) {

		StringBuilder query = new StringBuilder(
				"select compra from Compra compra where compra.codCompra = :codCompra ");

		Query createQuery = getEntityManager().createQuery(query.toString());
		createQuery.setParameter("codCompra", codigoCompra);

		try {
			Compra compra = (Compra) createQuery.getSingleResult();
			return compra;

		} catch (NoResultException e) {
			addFacesMessage("Compra nao localizada", "");
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String persistUpdate() {

		if (this.novoPagamento) {
			return persist();
		} else {
			return update();
		}
	}

	/**
	 * Informa o pagamento selecionado
	 * @param pagamento
	 */
	public void selectedPagamento(Pagamento pagamento) {
		
		setPagamentoSelected(pagamento);
		
		if(pagamento.getCompra() != null){
			codigoCompra = pagamento.getCompra().getCodCompra();
		}
		novoPagamento = false;
	}

	/**
	 * 
	 * @param pagamento
	 * @param indicadorConferencia
	 */
	public void conferir(Pagamento pagamento) {

		StringBuilder query = new StringBuilder(
				"update Pagamento r set r.inConferencia = :conferido ");

		if (pagamento.getDataBalancete() == null) {
			query.append(", r.dataBalancete = :dataBalancete ");
		}

		query.append("WHERE r.codPagamento = :codigoPagamento ");

		Query createQuery = getEntityManager().createQuery(query.toString());

		if (pagamento.getInConferencia()) {
			createQuery.setParameter("conferido", true);
		} else {
			createQuery.setParameter("conferido", false);
		}

		if (pagamento.getDataBalancete() == null) {
			createQuery.setParameter("dataBalancete",
					pagamento.getDataVencimento());
			pagamento.setDataBalancete(pagamento.getDataVencimento());
		}

		createQuery
				.setParameter("codigoPagamento", pagamento.getCodPagamento());

		createQuery.executeUpdate();
	}

	public Pagamento getPagamentoSelected() {
		return pagamentoSelected;
	}

	public void setPagamentoSelected(Pagamento pagamentoSelected) {
		this.pagamentoSelected = pagamentoSelected;
	}

	public Integer getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(Integer currentRow) {
		this.currentRow = currentRow;
	}

	public boolean isNovoPagamento() {
		return novoPagamento;
	}

	public void setNovoPagamento(boolean novoPagamento) {
		this.novoPagamento = novoPagamento;
	}

	public Integer getCodigoCompra() {
		return codigoCompra;
	}

	public void setCodigoCompra(Integer codigoCompra) {
		this.codigoCompra = codigoCompra;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento novoTipoPagamento) {
		this.tipoPagamento = novoTipoPagamento;
	}

	public List<TipoPagamento> getTipoPagamentos() {
		return tipoPagamentos;
	}

	public void setTipoPagamentos(List<TipoPagamento> novoTipoPagamentos) {
		tipoPagamentos = novoTipoPagamentos;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> novoFornecedores) {
		fornecedores = novoFornecedores;
	}

}
