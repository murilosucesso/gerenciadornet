package br.com.gerenciadornet.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.gerenciadornet.entity.TipoPagamento;

public class CalculosUtil{
	
	/**
	 * Recebe a data atual(java.util.Date) e retorna um List<java.util.Date>
	 * com as datas de pagamentos de acordo com o tipo de pagamento.
	 * @param tipoPagamento
	 * @param dataAtual
	 * @return
	 */
	public static List<Date> calcularDatasPagamento(TipoPagamento tipoPagamento,Date dataAtual){
		
		List<Date> datasFuturas = new ArrayList<Date>();
		//valor de um dia em milessegundos
		long umDia = 86400000L;
		long diasCarencia = tipoPagamento.getDiasCarencia() * umDia;
		Integer intervaloDias = tipoPagamento.getIntervaloDias();
		
		if(intervaloDias == null){
			intervaloDias = 0;
		}
		
		if(tipoPagamento.getNumVezes() == null 
				|| tipoPagamento.getNumVezes().intValue() == 0 
				|| tipoPagamento.getNumVezes().intValue() == 1){
			
			Date dataFutura = new Date();
			if(isMesMaior(dataAtual)){
				if(tipoPagamento.getNumVezes() > 1){
					dataFutura = new Date(dataAtual.getTime() + diasCarencia + intervaloDias * umDia + umDia);
				} else {
					if(diasCarencia >= 30){
						dataFutura = new Date(dataAtual.getTime() + diasCarencia + umDia);
					} else {
						dataFutura = new Date(dataAtual.getTime() + diasCarencia);
					}
				}
			} else {
				dataFutura = new Date(dataAtual.getTime() + diasCarencia + intervaloDias * umDia);
			}
			
			datasFuturas.add(dataFutura);
			
		} else {
			Long dataAtualAux = dataAtual.getTime();
			for(int i = 0; i < tipoPagamento.getNumVezes(); i++){
				if(i == 0){
					dataAtualAux += diasCarencia;
				} else { 
					if(isMesMaior(new Date(dataAtualAux))){
						dataAtualAux = dataAtualAux + intervaloDias * umDia + umDia;
					} else {
						dataAtualAux = dataAtualAux + intervaloDias * umDia;
					}
				}
				Date dataFutura = new Date(dataAtualAux);
				datasFuturas.add(dataFutura);
				
				//subtrai o dia adicionado
				if(isMesMaior(new Date(dataAtualAux))){
					//dataAtualAux -= umDia;
				}
			}
		}
		
		return datasFuturas;
	}
	
	/**
	 * Se o próximo mês for de 31 dias retorna true
	 * else retorna false;
	 * @param data
	 * @return
	 */
	private static boolean isMesMaior(Date data){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		int mes = calendar.get(Calendar.MONTH) + 1;
		
		if(mes == 1 || mes == 3  || mes == 5  || mes == 7  || mes == 8  || mes == 10  || mes == 12)
			return true;
				
		return false;
	}
	
	
	/**
	 * Recebe um tipo de pagamento e um valor, calcula o valor de da parcela
	 * e retorna um List<Float> de valores com todas as parcelas.
	 * 
	 * @param tipoPagamento
	 * @param valor
	 * @return
	 */
	public static List<Float> calcularParcelas(TipoPagamento tipoPagamento, Float valor){
		
		List<Float> valoresParcela = new ArrayList<Float>();
		
		if(tipoPagamento.getNumVezes() == null || tipoPagamento.getNumVezes() == 0
				 || tipoPagamento.getNumVezes() == 1){
			valoresParcela.add(valor);
		} else {
			
			Float valorParcela = arrendondarFloat(valor / tipoPagamento.getNumVezes());
			Float valorTotalParcelas = 0f;
			
			for(int i = 0; i < tipoPagamento.getNumVezes(); i++){
				valoresParcela.add(valorParcela);
				valorTotalParcelas += valorParcela;
			}
			
			//Faz o ajusto que passam ou faltam centavos e altera o 
			//valor da primeira parcela
			if(valorTotalParcelas > valor){
				Float diferenca = valorTotalParcelas - valor;
				valoresParcela.set(0, valorParcela - diferenca);
				
			} else if(valorTotalParcelas < valor){
				Float diferenca = valor - valorTotalParcelas;
				valoresParcela.set(0, valorParcela + diferenca);
			}
		}
		return valoresParcela;
	}
	
	/**
	 * Formata um float para duas casa decimais e se
	 * passar 0.51 arredonda pra cima
	 * @param numero
	 * @return
	 */
	public static Float arrendondarFloat(float numero){
		
		BigDecimal aNumber = new BigDecimal(numero);  
		aNumber = aNumber.setScale(2, BigDecimal.ROUND_HALF_EVEN);      
		return aNumber.floatValue();
	}
		
	
	public static void main(String[] args)
	{
		System.out.println("\n\n ============== formatandox =======================");
		TipoPagamento tipoPagamento = new TipoPagamento();
		tipoPagamento.setIntervaloDias(0);
		tipoPagamento.setDiasCarencia(0);
		tipoPagamento.setNumVezes(1);
		
		System.out.println(" parcela = " + calcularDatasPagamento(tipoPagamento, new Date(System.currentTimeMillis())));
		System.out.println(" parcela = " + calcularParcelas(tipoPagamento, 768.26f));
		
		System.out.println("float =  " + new BigDecimal(1.590).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
		
		//isMesMaior(new Date(System.currentTimeMillis()));
		//System.out.println(""+ arrendondarFloat(new Float("9.66667")));
		//System.out.println("\n\n ============== formatando =======================");
	}

}

