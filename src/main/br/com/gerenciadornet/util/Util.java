package br.com.gerenciadornet.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Date;

public class Util {
	
	/**
	 * Formata um nome para a sequencia de caracter
	 * Maiúscula / Minúscula. Ex: Murilo M. De Oliveira.
	 * @param nome
	 * @return
	 */
	public static String formataNome(String nome){
		
		if(nome == null){
			return "";
		}
		
		String[] partesNome = nome.split(" ");
		String nomeFormatado = "";
		
		for (int i = 0; i < partesNome.length; i++) {
			
			if(partesNome[i] != null && partesNome[i].length() > 1){
				nomeFormatado += partesNome[i].substring(0, 1).toUpperCase() + partesNome[i].substring(1).toLowerCase();
			} else if(partesNome[i] != null && partesNome[i].length() == 1){
				nomeFormatado += partesNome[i].substring(0, 1).toUpperCase();
			}
			
			if(i < partesNome.length + 1){
				nomeFormatado +=" ";
			}
		}
		
		return nomeFormatado;
	}
	
	 /** 
     * Calcula a diferença de duas datas em dias 
     * <br> 
     * <b>Importante:</b> Quando realiza a diferença em dias entre duas datas, este método considera as horas restantes e as converte em fração de dias. 
     * @param dataInicial 
     * @param dataFinal 
     * @return quantidade de dias existentes entre a dataInicial e dataFinal. 
     */  
    public static double diferencaEmDias(Date dataInicial, Date dataFinal){  
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        double diferencaEmDias = (diferenca /1000) / 60 / 60 /24; //resultado é diferença entre as datas em dias  
        long horasRestantes = (diferenca /1000) / 60 / 60 %24; //calcula as horas restantes  
        result = diferencaEmDias + (horasRestantes /24d); //transforma as horas restantes em fração de dias  
      
        return result;  
    }
    
    //Função para criar hash da senha informada  
    public static String md5(String senha){  
        String sen = "";  
        MessageDigest md = null;  
        try {  
            md = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
        sen = hash.toString(16);              
        return sen;  
    }  
    
    public static String removerCaracteresEspeciais(String string) {
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        return string;
    }
    
    public static String removerMascara(String texto) {
    		if(texto == null)
    			return "";
        return texto.replaceAll("\\D", "");
    }
    
    public static void main(String[] args) {
    	
		System.out.println(removerMascara(null));
	}
}
