/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import it.csi.findom.blocchetti.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class DecimalFormat {
		


	// decimalFormat_function.bsh
		// Funzione che data una stringa cerca di formattarla come decimale con numDecimali cifre dopo la virgola
	public static String decimalFormat(String valore, int numDecimali){
		  
		  if(valore==null ||valore.trim().equals("")){
		     return "";
		  }
		  if(valore.startsWith(",") ||valore.endsWith(",")){
			  return valore;
		  }
		  valore = valore.replace(".", "");
		  
		  String negativo = "false";
		  String valoreConDecimali = valore.trim();
		  if(valoreConDecimali.startsWith("-")){
			  valoreConDecimali = valoreConDecimali.substring(1);
		     negativo = "true";
		  }
		  
		  String parteIntera = valoreConDecimali;
	      String parteDecimale = "";
	      
	      if(valoreConDecimali.indexOf(",") != -1){        	  
	    	  parteIntera = valoreConDecimali.substring(0,valoreConDecimali.indexOf(","));
	    	  parteDecimale = valoreConDecimali.substring(valoreConDecimali.indexOf(",")+1);
	      }
	     
	      if( parteIntera.matches("\\d+") && (parteDecimale.equals("") || parteDecimale.matches("\\d+"))){
	    	  //elimino zeri inutili a sinistra
	    	  while(parteIntera.length()>0 && parteIntera.charAt(0) == '0'){
	    		  parteIntera = parteIntera.substring(1);
	    	  } 
	    	  if (parteIntera.equals("")){
	    		  parteIntera = "0";
	    	  }       	  
	          //completo con 0 a destra fino a raggiungere un numero di cifre pari a numDecimali dopo la virgola
	    	  while (parteDecimale.length() < numDecimali) {
	    		  parteDecimale += "0";
	    	  }
	    	  if(numDecimali>0)
	    		  valoreConDecimali = parteIntera+","+parteDecimale;
	          if(negativo.equals("true")){
	        	  valoreConDecimali = "-" + valoreConDecimali;
	          }	              
	      }         
	      return valoreConDecimali;		
	}
	
	//ritorna true se la stringa in input rappresenta correttamente un numero decimale con al piu' due cifre decimali.
	//Il parametro ammessiNegativi indica se sono ammessi valori negativi oppure no
	//Il parametro ammessoZero stabilisce se il valore 0  deve considerarsi un numero decimale valido oppure no
	public boolean decimalValidate(String inputVal, boolean ammessiNegativi, boolean ammessoZero){
	   boolean valido = true;
	   String regexpr = "";
	   if(ammessiNegativi){
	      regexpr = "^-?(([1-9]\\d*)|0)(,\\d{1,2})?$";
	   }else {
	      regexpr = "(([1-9]\\d*)|0)(,\\d{1,2})?$";
	   }
	   if(inputVal !=null && StringUtils.isBlank(inputVal) && (inputVal.length()>0)){
	     valido = false;
	   }
	   	  			
	   if(StringUtils.isEmpty(inputVal) || (!inputVal.matches(regexpr))){
	     valido = false;
	   }
	   if(valido && (!ammessoZero)){
	      
	      inputVal = inputVal.replace(",", ".");	   
	      BigDecimal valore = new BigDecimal(inputVal);
	      if(valore.compareTo(new BigDecimal(0))==0){
	         valido = false;
	      }
	   
	   }
	   return valido;
	}
	
	//getBigDecimalFromString serve se si ha una stringa e si vuole il BigDecimal corrispondente, o un BigDecimal di valore 0 
	//se la stringa é nulla o vuota o non convertibile in numero.
	//Il parametro ammessiNegativi indica se sono ammessi valori negativi oppure no	
	public BigDecimal getBigDecimalFromString(String inputVal, boolean ammessiNegativi){
	
	  BigDecimal outVal = new BigDecimal(0);	   			
	  if(decimalValidate(inputVal, ammessiNegativi, true)){
	    inputVal = inputVal.replace(",",".");   
	    outVal = new BigDecimal(inputVal);
	  }else{
		return outVal;	  
	  }
	  return outVal;
	}
	
	//getStringFromBigDecimal serve se si ha un BigDecimal e si vuole la corrispondente stringa formattata in modo da essere visualizzabile. 
	//applica anche un arrotondamento; se il BigDecimal in input é null ritorna "0"
	public String getStringFromBigDecimal(BigDecimal inputVal, int numDec){
	   String retVal = "0";
	   if(inputVal!=null){
	      BigDecimal bd = inputVal.setScale(numDec, RoundingMode.HALF_UP);
	      retVal = bd.toString().replace('.', ',');
	   }
	   return retVal; 
	}
	
	//decimalFillZero serve se si ha un valore decimale gia' stato validato con decimalValidate() e lo si vuole formattare in modo che sia visualizzabile 
	//con il numero voluto di cifre decimali.
	//La formattazione consiste nell'aggiunta della virgola se non c'e' e di tanti zeri a destra fino a raggiungere numDecimali cifre decimali.
	//Al momento poiche' si assume che sia gia' stato validato con decimalValidate() il valore in input non ha piu' di 2 decimali.
	//Se si trattano piu' o meno di due decimali si deve modificare decimalValidate() in modo da rendere parametrico il numero di decimali
	//e eventualmente chiamare decimalFillZero con numDecimali diverso da 2		
	public String decimalFillZero(String inputVal, int numDecimali){
	   String valore = inputVal;  		  
	   if(valore!=null && !valore.equals("")){	      		        
		  String parteIntera = valore;
	      String parteDecimale = "";
	      if(valore.indexOf(",") == -1){
	       	 valore = valore+","; 
	      }
	      parteIntera = valore.substring(0,valore.indexOf(",")); 
	      parteDecimale = valore.substring(valore.indexOf(",")+1);
	        	  
	      if(parteDecimale.length() < numDecimali){
	         while (parteDecimale.length() < numDecimali) {
	     	    parteDecimale += "0";
	         }
	         valore = parteIntera+","+parteDecimale;	          
	      }		  
	   }//chiude test null iniziale
		   
	   return valore; 
	}
	
	//assume che inputVal sia una stringa corrispondente ad un numero decimale valido, 
	//con gli eventuali decimali separati da , dalla parte intera.
	//Ritorna la stringa passata in input con i separatori delle migliaia e sempre due decimali separati da , dalla parte intera	
	public String decimalGroup(String inputVal){
		String retVal = "";	
		if(StringUtils.isNotBlank(inputVal)){
			inputVal = inputVal.replace(',', '.');
			BigDecimal valoreBD = new BigDecimal(inputVal);		
			retVal = String.format(Locale.ITALY, "%,.2f", valoreBD);	//la , sta per separatore delle migliaia e .2 sta per il numero di cifre dopo la virgola	
		}
		return retVal; 
	}

}
