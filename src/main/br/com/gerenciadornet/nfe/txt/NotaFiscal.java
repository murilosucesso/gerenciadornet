package br.com.gerenciadornet.nfe.txt;

import java.util.Date;

public class NotaFiscal {

	String nfNatureza;
	Float nfTipoDocumento;
	Float nfFormaPagto;
	int nfCodigo;
	Date nfDtemissao;
	Date NfDtsaida;
	String nfNomecliente;
	char nfTipopessoa;
	String nfDocumento;
	Float nfDescontosProdutos;
	Float nfDescontosServicos;
	Double nfModalidadefrete;

	char nfTransTipopessoa;
	String nfTransDocumento;
	String nfTransportador;
	String nfTransEndereco;
	Double nfTransNumero;
	Cidades CidadesByCidTransporte;
	String nfTransBairro;

	String nfEndereco;
	String nfNumero;
	String nfBairro;
	char nfIsento;
	String nfIe;
	Float nfCfop;

	public String getNfNatureza() {
		return nfNatureza;
	}

	public void setNfNatureza(String nfNatureza) {
		this.nfNatureza = nfNatureza;
	}

	public Float getNfTipoDocumento() {
		return nfTipoDocumento;
	}

	public void setNfTipoDocumento(Float nfTipoDocumento) {
		this.nfTipoDocumento = nfTipoDocumento;
	}

	public Float getNfFormaPagto() {
		return nfFormaPagto;
	}

	public void setNfFormaPagto(Float nfFormaPagto) {
		this.nfFormaPagto = nfFormaPagto;
	}

	public int getNfCodigo() {
		return nfCodigo;
	}

	public void setNfCodigo(int nfCodigo) {
		this.nfCodigo = nfCodigo;
	}

	public Date getNfDtemissao() {
		return nfDtemissao;
	}

	public void setNfDtemissao(Date nfDtemissao) {
		this.nfDtemissao = nfDtemissao;
	}

	public Date getNfDtsaida() {
		return NfDtsaida;
	}

	public void setNfDtsaida(Date nfDtsaida) {
		NfDtsaida = nfDtsaida;
	}

	public String getNfNomecliente() {
		return nfNomecliente;
	}

	public void setNfNomecliente(String nfNomecliente) {
		this.nfNomecliente = nfNomecliente;
	}

	public char getNfTipopessoa() {
		return nfTipopessoa;
	}

	public void setNfTipopessoa(char nfTipopessoa) {
		this.nfTipopessoa = nfTipopessoa;
	}

	public String getNfDocumento() {
		return nfDocumento;
	}

	public void setNfDocumento(String nfDocumento) {
		this.nfDocumento = nfDocumento;
	}

	public String getNfEndereco() {
		return nfEndereco;
	}

	public void setNfEndereco(String nfEndereco) {
		this.nfEndereco = nfEndereco;
	}

	public String getNfNumero() {
		return nfNumero;
	}

	public void setNfNumero(String nfNumero) {
		this.nfNumero = nfNumero;
	}

	public String getNfBairro() {
		return nfBairro;
	}

	public void setNfBairro(String nfBairro) {
		this.nfBairro = nfBairro;
	}

	public char getNfIsento() {
		return nfIsento;
	}

	public void setNfIsento(char nfIsento) {
		this.nfIsento = nfIsento;
	}

	public String getNfIe() {
		return nfIe;
	}

	public void setNfIe(String nfIe) {
		this.nfIe = nfIe;
	}

	public Float getNfDescontosProdutos() {
		return nfDescontosProdutos;
	}

	public void setNfDescontosProdutos(Float nfDescontosProdutos) {
		this.nfDescontosProdutos = nfDescontosProdutos;
	}

	public Float getNfCfop() {
		return nfCfop;
	}

	public void setNfCfop(Float nfCfop) {
		this.nfCfop = nfCfop;
	}

	public Float getNfDescontosServicos() {
		return nfDescontosServicos;
	}

	public void setNfDescontosServicos(Float nfDescontosServicos) {
		this.nfDescontosServicos = nfDescontosServicos;
	}

	public Double getNfModalidadefrete() {
		return nfModalidadefrete;
	}

	public void setNfModalidadefrete(Double nfModalidadefrete) {
		this.nfModalidadefrete = nfModalidadefrete;
	}

	public char getNfTransTipopessoa() {
		return nfTransTipopessoa;
	}

	public void setNfTransTipopessoa(char nfTransTipopessoa) {
		this.nfTransTipopessoa = nfTransTipopessoa;
	}

	public String getNfTransDocumento() {
		return nfTransDocumento;
	}

	public void setNfTransDocumento(String nfTransDocumento) {
		this.nfTransDocumento = nfTransDocumento;
	}

	public String getNfTransportador() {
		return nfTransportador;
	}

	public void setNfTransportador(String nfTransportador) {
		this.nfTransportador = nfTransportador;
	}

	public String getNfTransEndereco() {
		return nfTransEndereco;
	}

	public void setNfTransEndereco(String nfTransEndereco) {
		this.nfTransEndereco = nfTransEndereco;
	}

	public Double getNfTransNumero() {
		return nfTransNumero;
	}

	public void setNfTransNumero(Double nfTransNumero) {
		this.nfTransNumero = nfTransNumero;
	}

	public Cidades getCidadesByCidTransporte() {
		return CidadesByCidTransporte;
	}

	public void setCidadesByCidTransporte(Cidades cidadesByCidTransporte) {
		CidadesByCidTransporte = cidadesByCidTransporte;
	}

	public String getNfTransBairro() {
		return nfTransBairro;
	}

	public void setNfTransBairro(String nfTransBairro) {
		this.nfTransBairro = nfTransBairro;
	}
}
