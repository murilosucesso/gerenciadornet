function hideInTroca() {
	if (document.getElementById('venda:inOrcamentoField:inOrcamento:0').checked == true) {
		document.getElementById('divInTroca').style.visibility = "visible";
	} else {
		document.getElementById('venda:inTrocaField:inTroca:0').checked = true;
		document.getElementById('divInTroca').style.visibility = "hidden";
	}
}

function focarPesquisa() {
	document.getElementById('venda:pesquisar:pesquisarGeral').value = "";
	document.getElementById('venda:pesquisar:pesquisarGeral').focus();

}

function habilitarPesquisaCliente() {
	if (document.getElementById('venda:pesquisarCliente:pesquisaGeralCliente').value.length > 2) {
		document.getElementById('venda:pesquisarClienteButton').disabled = false;
	} else {
		document.getElementById('venda:pesquisarClienteButton').disabled = "disabled";
	}
}

function habilitarPesquisaProduto() {
	if (document.getElementById('venda:pesquisar:pesquisarGeral').value.length > 2) {
		document.getElementById('venda:pesquisarProdutos').disabled = false;
	} else {
		document.getElementById('venda:pesquisarProdutos').disabled = "disabled";
	}
}

function inicializacao() {
	habilitarPesquisaCliente();
	habilitarPesquisaProduto();
	document.getElementById('venda:pesquisarCliente:pesquisaGeralCliente')
			.focus();
}