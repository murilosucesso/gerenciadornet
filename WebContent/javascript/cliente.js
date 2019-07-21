function hideCampos() {
	if (document.getElementById('cliente:tipoPessoaField:tipoPessoa:1').checked == true) {
		document.getElementById('sexo').style.display = 'none';
		document.getElementById('divDataNascimento').style.display = 'none';
		// document.getElementById('estudanteSelectDiv').style.display = 'none';
		document.getElementById('labelNome').innerHTML = 'Razão Social';
		document.getElementById('labelNomeFantasiaApelido').innerHTML = 'Nome Fantasia';
		document.getElementById('labelRgIE').innerHTML = 'I.E';
		document.getElementById('labelCpfCnpj').innerHTML = 'C.N.P.J';
	} else {
		document.getElementById('sexo').style.display = 'block';
		document.getElementById('divDataNascimento').style.display = 'block';
		// document.getElementById('estudanteSelectDiv').style.display =
		// 'block';
		document.getElementById('labelNome').innerHTML = 'Nome';
		document.getElementById('labelNomeFantasiaApelido').innerHTML = 'Apelido';
		document.getElementById('labelRgIE').innerHTML = 'R.G';
		document.getElementById('labelCpfCnpj').innerHTML = 'C.P.F';
	}
}

function hideCamposEstudante() {

	if (document.getElementById('cliente:estudandeField:estudante:1').checked == true) {
		document.getElementById('estudanteDiv').style.display = 'block';
	} else {
		document.getElementById('estudanteDiv').style.display = 'none';
	}
}