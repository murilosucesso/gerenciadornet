package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.entity.Anotacao;
import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Endereco;
import br.com.gerenciadornet.entity.Grupo;
import br.com.gerenciadornet.entity.Recebimento;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.Venda;
import br.com.gerenciadornet.util.Constantes;

@Scope(ScopeType.CONVERSATION)
@SuppressWarnings("serial")
@Name("relatorioList")
public class RelatorioList extends EntityQuery<Venda> {

	private static final String EJBQL = "select new br.com.gerenciadornet.entity.Venda(venda.codVenda, tp.descTipoPagamento, c.nome, "
			+ "u.nomeUsuario, uc.nomeUsuario, venda.dataInicioVenda, venda.dataFimVenda, sv.codStatusVenda, sv.descStatusVenda,"
			+ "venda.valorTotalVenda, venda.lucroTotalVenda ) "
			+ "from Venda venda " 
			+ "left join venda.cliente c "
			+ "left join venda.usuario u "
			+ "left join venda.usuarioCadastro uc "
			+ "left join venda.tipoPagamento tp "
			+ "left join venda.statusVenda sv";

	//Utilizado por RelatorioList (Relatorio de Vendas)
	private static String[] RESTRICTIONS = {
			"venda.codVenda = #{vendaList.venda.codVenda}",//Codigo da Vendas  
			"venda.cliente.codCliente = #{relatorioList.codCliente}",//Cliente
			"venda.tipoPagamento.codTipoPagamento = #{relatorioList.codTipoPagamento}",//Tipo Pagamento
			"venda.usuarioCadastro.codUsuario = #{relatorioList.usuarioResponsavel.codUsuario}",//Vendedor Que Realizou o Cadastro
			"venda.usuario.codUsuario = #{relatorioList.venda.usuario.codUsuario}",//Vendedor
			"venda.cliente.usuario.codUsuario = #{relatorioList.vendedorResponsavel.codUsuario}",//Vendedor Responsavel pelo Cliente
			"venda.cliente.grupo.codGrupo = #{relatorioList.grupoCarteira.codGrupo}",//Grupo(Carteira)
			"venda.dataInicioVenda >= #{relatorioList.dataInicioVenda}",
			"venda.dataInicioVenda <= #{relatorioList.dataFimVenda}",
			"venda.inOrcamento = #{relatorioList.inOrcamento}",	// inOrcamento = false (Venda)
			"venda.statusVenda.codStatusVenda = #{relatorioList.statusVenda}",// Status da Venda
			"venda.canal = #{relatorioList.venda.canal}" //Sistema de Origem
			};

	//Utilizado por RelatorioClienteList para pesquisa de vendas em aberto 
	private static String[] RESTRICTIONS2 = {
		"venda.cliente.codCliente = #{relatorioList.codCliente}",
		"venda.dataInicioVenda >= #{relatorioList.dataInicioVenda}",
		"venda.dataInicioVenda <= #{relatorioList.dataFimVenda}",
		"venda.statusVenda.codStatusVenda <> #{relatorioList.statusVendaCancela}",
		"venda.statusVenda.codStatusVenda <> #{relatorioList.statusVendaconcluida}",
		"venda.statusVenda.codStatusVenda <> #{relatorioList.statusVendaOrcamento}",
		"venda.inOrcamento <> #{relatorioList.indicadorOrcamento}"
	};
	
	
	//Utilizado para pesquisar vendas por data de fim da venda
	private static String[] RESTRICTIONS3 = {
		"venda.codVenda = #{vendaList.venda.codVenda}",  
		"venda.cliente.codCliente = #{relatorioList.codCliente}",
		"venda.cliente.usuario.codUsuario = #{relatorioList.usuarioResponsavel.codUsuario}",	
		"venda.usuario.codUsuario = #{relatorioList.venda.usuario.codUsuario}",
		"venda.dataFimVenda >= #{relatorioList.dataInicioVenda}",
		"venda.dataFimVenda <= #{relatorioList.dataFimVenda}",
		"venda.inOrcamento = #{relatorioList.inOrcamento}",	
		"venda.statusVenda.codStatusVenda = #{relatorioList.statusVenda}",
		"venda.canal = #{relatorioList.venda.canal}" 
		};
	
	//Filtros
	private Integer codCliente;
	private Integer codUsuario;
	private Integer codUsuarioResponsavel;
	
	//mostra como default as vendas do dia
	private Date 	dataInicioVenda 		= new Date(System.currentTimeMillis() - (0 * 86400000l));	
	private Date 	dataFimVenda 			= new Date(System.currentTimeMillis());	
	private Boolean inOrcamento 			= false;
	private Boolean resultadoIsNull 		= true;	
	private Float 	lucroTotal 				= new Float(0);
	private Float 	valorTotal 				= new Float(0);
	private Float 	valorComissaoVendaTotal = new Float(0);
	private Float 	valorComissaoLucroTotal	= new Float(0);
	private Float 	percentualComissao		= new Float(0);
	private Float 	valorTotalAreceber		= new Float(0);
	private Integer statusVendaCancela 		= Constantes.CANCELADA;
	private Integer codTipoPagamento;
	
	//status de vendas diferente de abertas ou em andamento.. RelatorioClienteList.xhtml
	private Integer statusVendaconcluida	= Constantes.CONCLUIDA;
	private Integer statusVendaOrcamento	= Constantes.ORCAMENTO;
	private boolean	indicadorOrcamento		= true;
	
	private Integer statusVenda;
	private Integer tipoRelatorio			= 0;
	private Integer numeroLinhasDT 			= 50;
	private boolean mostrarResultados 		= false;
	private Integer tipoData				= 1;//Tipo Data = 1 - Data Abertura da Venda , 2 = Data Conclusão da Venda
	private Grupo grupoCarteira				= new Grupo();
	private Usuario usuarioResponsavel 		= new Usuario();
	private Usuario vendedorResponsavel   = new Usuario();
	
	
	@In(scope=ScopeType.SESSION, required=false)
	Usuario user;
	
	@In
	Constantes constantes;
	
	@In
	EntityManager entityManager;
	
	private Venda venda = new Venda( 
			new Date(System.currentTimeMillis()), 
			new Date(System.currentTimeMillis()),
			null);
	
	public RelatorioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("venda.dataInicioVenda");
	}

	/**
	 * TODO Otimizar esta consulta escrevendo manualmente o SQL.
	 */
	@Override
	public List<Venda> getResultList() {

		if(user.getPerfil().getCodPerfil().intValue() ==  constantes.getVendedor().intValue()){
			setUsuarioResponsavel(user);
		}
		//88 = Em Aberto(Não Concluídas) - Em RelatorioClienteList.xhtml
		if(statusVenda != null && statusVenda == 88){
            setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS2));
        }
		
		//Data de Conclusão da venda
		if(tipoData == 2){
            setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS3));
        }
		
		List<Venda> vendas = super.getResultList();

		//TipoRelatorio = 1 : Resumido de Vendas
		if(tipoRelatorio.intValue() ==  1){
			return vendas;
			
		}else{
				
			List<Venda> vendasAux 				= new ArrayList<Venda>();
			Float valorTotalAux 				= new Float(0);
			Float lucroTotalAux 				= new Float(0);
			Float valorComissaoVendaTotalAux 	= new Float(0);
			Float valorComissaoLucroTotalAux 	= new Float(0);
			
			for (Venda venda : vendas) {								
				Float valorTotalVendaAux 	= new Float(0);
				Float lucroTotalVenda 		= new Float(0);
				
				valorTotalVendaAux = venda.getValorTotalVenda();
				lucroTotalVenda = venda.getLucroTotalVenda();
				
				//Se a venda não estiver cancelada, adiciona ao total o valor da venda, o lucro e a comissão
				if(venda.getStatusVenda().getCodStatusVenda().intValue() != Constantes.CANCELADA.intValue()){
					valorTotalAux += valorTotalVendaAux;
					lucroTotalAux += lucroTotalVenda;			
					venda.setValorComissaoVenda(valorTotalVendaAux * percentualComissao /100);
					venda.setValorComissaoLucro(lucroTotalVenda * percentualComissao /100);
					valorComissaoVendaTotalAux += venda.getValorComissaoVenda();
					valorComissaoLucroTotalAux += venda.getValorComissaoLucro();
				}
				vendasAux.add(venda);
			}
			setLucroTotal(lucroTotalAux);
			setValorTotal(valorTotalAux);
			setValorComissaoVendaTotal(valorComissaoVendaTotalAux);
			setValorComissaoLucroTotal(valorComissaoLucroTotalAux);
			
			if(vendasAux.size() > 0){
				setResultadoIsNull(false);
			} else {
				vendasAux = null;
			}
	
			return vendasAux;
		}
	}
	
	/**
	 * Lista todos os recebimentos que estão em ABERTO de um cliente
	 * @return
	 */
	public List<Recebimento> listarRecebimentosCliente() {

		StringBuilder sql = new StringBuilder();
		sql.append("select recebimento from Recebimento recebimento, Venda venda ");
		sql.append("where recebimento.venda.codVenda = venda.codVenda ");
		sql.append("and venda.cliente.codCliente = :codCliente ");
		sql.append("and recebimento.dataBalancete is null ");
		sql.append("order by venda.dataInicioVenda");
		
		@SuppressWarnings("unchecked")
		List<Recebimento> recebimentoList = (List<Recebimento>)entityManager.createQuery(sql.toString())
				.setParameter("codCliente", codCliente)
				.getResultList();
		
		valorTotalAreceber = new Float(0);
		
		for(Recebimento recebimento : recebimentoList){
			valorTotalAreceber += recebimento.getValorAReceber();
		}
		
		return recebimentoList;
	}
	
	/**
	 * Busca o cliente pelo código para seus dados
	 * serem mostrados em RelatorioClienteList.
	 */
	public void buscarCliente(){
		
		StringBuilder sql = new StringBuilder();
		sql.append("select cliente from Cliente cliente ");
		sql.append("where cliente.codCliente = :codCliente ");
		
		Cliente cliente = (Cliente)entityManager.createQuery(sql.toString())
				.setParameter("codCliente", codCliente)
				.getSingleResult();
		
		venda.setCliente(cliente);	
	}
	
	public List<Anotacao> getAnotacoes() {
		return  venda.getCliente() == null ? null : new ArrayList<Anotacao>(
				venda.getCliente().getAnotacoes());
	}
		
	public Venda getVenda() {
		return venda;
	}

	public Boolean getInOrcamento() {
		return inOrcamento;
	}
	
	/**
	 * adiciona um dia na pesquisa final para a data fim ser incluida na pesquisa
	 */
	public Date getDataFimVenda(){		
		if(venda.getDataFimVenda() != null){
			
			return new Date(venda.getDataFimVenda().getTime() + 86400000l);
		}
		return dataFimVenda;		
	}

	public Date getDataInicioVenda(){		
		if(venda.getDataInicioVenda() != null){
			return new Date(venda.getDataInicioVenda().getTime());
		}
		return dataInicioVenda;		
	}
	
	public List<Endereco> getEnderecosCliente() {
		return venda.getCliente() == null ? null : new ArrayList<Endereco>(
				venda.getCliente().getEnderecos());
	}

	public Float getLucroTotal() {
		return lucroTotal;
	}

	public Float getValorTotal() {
		return valorTotal;
	}

	public void setLucroTotal(Float lucroTotal) {
		this.lucroTotal = lucroTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}


	public Boolean getResultadoIsNull() {
		return resultadoIsNull;
	}

	public void setResultadoIsNull(Boolean resultadoIsNull) {
		this.resultadoIsNull = resultadoIsNull;
	}

	public Integer getStatusVendaCancela() {
		return statusVendaCancela;
	}

	public Integer getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(Integer statusVenda) {
		this.statusVenda = statusVenda;
	}

	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}

	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}

	/**Evita que a consulta seja executada ao carregar a página inicial*/
	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public Usuario getUsuarioResponsavel()
	{
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Usuario usuarioResponsavel)
	{
		this.usuarioResponsavel = usuarioResponsavel;
	}

	/**
	 * O tipo de relatório foi feito para otimizar a pesquisa. 
	 * Pois para calcular o lucro é muito oneroso.
	 * */
	public Integer getTipoRelatorio()
	{
		return tipoRelatorio;
	}

	public void setTipoRelatorio(Integer tipoRelatorio)
	{
		this.tipoRelatorio = tipoRelatorio;
	}

	public Float getPercentualComissao()
	{
		return percentualComissao;
	}

	public void setPercentualComissao(Float percentualComissao)
	{
		this.percentualComissao = percentualComissao;
	}

	public Float getValorComissaoVendaTotal()
	{
		return valorComissaoVendaTotal;
	}

	public void setValorComissaoVendaTotal(Float valorComissaoVendaTotal)
	{
		this.valorComissaoVendaTotal = valorComissaoVendaTotal;
	}

	public Float getValorComissaoLucroTotal()
	{
		return valorComissaoLucroTotal;
	}

	public void setValorComissaoLucroTotal(Float valorComissaoLucroTotal)
	{
		this.valorComissaoLucroTotal = valorComissaoLucroTotal;
	}

	public Integer getStatusVendaconcluida()
	{
		return statusVendaconcluida;
	}

	public Integer getStatusVendaOrcamento()
	{
		return statusVendaOrcamento;
	}

	public boolean isindicadorOrcamento() {
		return indicadorOrcamento;
	}

	public void setindicadorOrcamento(boolean indicadorOrcamento) {
		this.indicadorOrcamento = indicadorOrcamento;
	}

	public Integer getTipoData() {
		return tipoData;
	}

	public void setTipoData(Integer tipoData) {
		this.tipoData = tipoData;
	}

	public Float getValorTotalAreceber() {
		return valorTotalAreceber;
	}

	public void setValorTotalAreceber(Float valorTotalAreceber) {
		this.valorTotalAreceber = valorTotalAreceber;
	}

	public Grupo getGrupoCarteira() {
		return grupoCarteira;
	}

	public void setGrupoCarteira(Grupo grupoCarteira) {
		this.grupoCarteira = grupoCarteira;
	}

	public Integer getCodTipoPagamento() {
		return codTipoPagamento;
	}

	public void setCodTipoPagamento(Integer codTipoPagamento) {
		this.codTipoPagamento = codTipoPagamento;
	}

	public Integer getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public Integer getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Integer getCodUsuarioResponsavel() {
		return codUsuarioResponsavel;
	}

	public void setCodUsuarioResponsavel(Integer codUsuarioResponsavel) {
		this.codUsuarioResponsavel = codUsuarioResponsavel;
	}

	public Usuario getVendedorResponsavel() {
		return vendedorResponsavel;
	}

	public void setVendedorResponsavel(Usuario vendedorResponsavel) {
		this.vendedorResponsavel = vendedorResponsavel;
	}

}
