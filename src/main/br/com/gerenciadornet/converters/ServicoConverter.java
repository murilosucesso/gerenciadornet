package br.com.gerenciadornet.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;

import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import br.com.gerenciadornet.entity.Servico;

@Name("servicoConverter")
@BypassInterceptors
@Converter(forClass = Servico.class)
public class ServicoConverter implements javax.faces.convert.Converter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
/*
		if (value != null) {
			Servico servico = new Servico();
			servico.setCodServico(Integer.valueOf(value));
			return servico;
		}*/
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent component,
			Object obj) {
		/*if (obj != null && obj instanceof Servico) {
			return ((Servico) obj).getDescServico().toString();
		}*/
		return null;
	}
}