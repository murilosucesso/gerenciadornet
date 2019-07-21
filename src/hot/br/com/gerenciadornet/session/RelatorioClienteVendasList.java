package br.com.gerenciadornet.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.com.gerenciadornet.dto.RelatorioClienteVendasDTO;
import br.com.gerenciadornet.entity.Cliente;
import br.com.gerenciadornet.entity.Usuario;
import br.com.gerenciadornet.entity.VwClienteVendas;

@Name("relatorioClienteVendasList")
public class RelatorioClienteVendasList extends EntityQuery<VwClienteVendas>{

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select vwClienteVendas from VwClienteVendas vwClienteVendas";
	
	private static final String[] RESTRICTIONS = {
				"vwClienteVendas.codigoCliente =  #{relatorioClienteVendasList.cliente.codCliente}",
				"vwClienteVendas.ano =  #{relatorioClienteVendasList.ano}",
				"vwClienteVendas.responsavel =  #{relatorioClienteVendasList.usuario.codUsuario}",
			};
	
	@In
	EntityManager entityManager;

	// Filtros de pesquisa
	private Cliente cliente 				= new Cliente();
	private Usuario usuario 				= new Usuario();
	private Integer ano						= new GregorianCalendar().get(Calendar.YEAR);
	private Integer numeroLinhasDT 			= 15;
	private boolean mostrarResultados 		= false;
	
	List<RelatorioClienteVendasDTO> relatorioClienteVendasList;

	public RelatorioClienteVendasList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	public List<RelatorioClienteVendasDTO> getResultList2() {
		
		HashMap<Integer,RelatorioClienteVendasDTO> relatorioClienteVendasDTOHM = new HashMap<Integer, RelatorioClienteVendasDTO>();
		List<VwClienteVendas> vwClienteVendasList = (List<VwClienteVendas>) super.getResultList();
		
		int codigoClienteAux = 0;
		
		//Monta os List<RelatorioClienteVendasDTO>, que sao as linhas da tabela
		for (VwClienteVendas clienteVendas : vwClienteVendasList) {
			
			if(codigoClienteAux != clienteVendas.getCodigoCliente().intValue()){
				
				RelatorioClienteVendasDTO dadosClienteVenda = new RelatorioClienteVendasDTO();
				dadosClienteVenda.setCodigoCliente(clienteVendas.getCodigoCliente());
				dadosClienteVenda.setNome(clienteVendas.getNome());
				
				//Cria objetos em branco para preencher cada elemento do array
				VwClienteVendas[] clientesAux = dadosClienteVenda.getVwclienteVendas();
				for(int i = 0; clientesAux.length > i; i++) {
					clientesAux[i] = new VwClienteVendas();
				}
				dadosClienteVenda.setVwclienteVendas(clientesAux);
				
				codigoClienteAux = clienteVendas.getCodigoCliente();
				relatorioClienteVendasDTOHM.put(codigoClienteAux, dadosClienteVenda);
			}
		}
		
		//Monta o array das linhas
		for (VwClienteVendas clienteVendas : vwClienteVendasList) {

			RelatorioClienteVendasDTO relatorioClienteVendasDTO = relatorioClienteVendasDTOHM.get(clienteVendas.getCodigoCliente().intValue());
			VwClienteVendas[] vwclienteVendas = relatorioClienteVendasDTO.getVwclienteVendas();
			vwclienteVendas[clienteVendas.getMes() - 1] = clienteVendas;
		}
		
		List<RelatorioClienteVendasDTO> resultadoList = new ArrayList<RelatorioClienteVendasDTO>();
		
		//Totaliza todos os meses e monta List Final
		for(RelatorioClienteVendasDTO relatorioClienteVendasDTO : relatorioClienteVendasDTOHM.values()){

			VwClienteVendas[] vwclienteVendas =  relatorioClienteVendasDTO.getVwclienteVendas();
			
			float valorTotalAno = 0;
			float lucroTotalAno = 0;
			
			for(int i = 0; vwclienteVendas.length > i; i++) {
				valorTotalAno += vwclienteVendas[i].getValorTotalMes();
				lucroTotalAno += vwclienteVendas[i].getLucroTotalMes();
			}
			relatorioClienteVendasDTO.setValorTotalAno(valorTotalAno);
			relatorioClienteVendasDTO.setLucroTotalAno(lucroTotalAno);
			resultadoList.add(relatorioClienteVendasDTO);
		}
		
		return resultadoList;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Integer getAno() {
		return ano;
	}


	public void setAno(Integer ano) {
		this.ano = ano;
	}


	public Integer getNumeroLinhasDT() {
		return numeroLinhasDT;
	}


	public void setNumeroLinhasDT(Integer numeroLinhasDT) {
		this.numeroLinhasDT = numeroLinhasDT;
	}


	public boolean isMostrarResultados() {
		return mostrarResultados;
	}


	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}


	public List<RelatorioClienteVendasDTO> getRelatorioClienteVendasList() {
		return relatorioClienteVendasList;
	}


	public void setRelatorioClienteVendasList(
			List<RelatorioClienteVendasDTO> relatorioClienteVendasList) {
		this.relatorioClienteVendasList = relatorioClienteVendasList;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	

}
