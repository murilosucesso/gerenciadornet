//Formata cpf ou cnpj no mesmo campo
function mascaraTexto(evento) {

	var campo, valor, i, tam, caracter, v;

	if (document.all) {
		campo = evento.srcElement;
	} else {
		campo = evento.target;
	}

	valor = campo.value;
	v = valor;

	if (valor.length < 15) {
		// mascara = "999.999.999-99";
		v = v.replace(/\D/g, "");
		v = v.replace(/(\d{3})(\d)/, "$1.$2");
		v = v.replace(/(\d{3})(\d)/, "$1.$2");
		v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
		campo.value = v;
	} else {

		// mascara = "99.999.999/9999-99";
		v = v.replace(/\D/g, "");
		v = v.replace(/^(\d{2})(\d)/, "$1.$2");
		v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
		v = v.replace(/\.(\d{3})(\d)/, ".$1/$2");
		v = v.replace(/(\d{4})(\d)/, "$1-$2");
		campo.value = v;

	}
}