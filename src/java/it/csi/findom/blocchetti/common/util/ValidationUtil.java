/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * MB2018_12_04
 * @author mauro.bottero
 *
 */
public class ValidationUtil { 
	
	/**
	 * questo metodo si puo' usare per tutti i bandi e per tutti i blocchetti e serve per eseguire ulteriori validazioni per un blocchetto, 
	 * oltre a quelle del modelValidate(). Le validazioni supplementari sono implementate in metodi definiti in una apposita classe del blocchetto
	 * e i cui nomi (e parametri attuali) si configurano tramite variabili di configurazione (al piu' una per blocchetto).
	 * L'esecuzione dei metodi aggiuntivi avviene tramite reflection e non impatta in alcun modo i bandi che non necessitano di chiamate a tali metodi.
	 * Ciascuno di questi metodi deve avere sempre come primo parametro la classe CommonalityInput specifica del blocchetto 
	 * e come secondo parametro il Logger;
	 * e poi puo' avere il numero che serve di parametri String, un parametro Object che puo' contenere qualsiasi struttura dati, 
	 * ed eventuali String varargs finali.
	 * 
	 * In questo modo e' possibile aggiungere validazioni al modeValidate di blocchetti condivisi solo per certi bandi senza aggiungere 
	 * delle if su nuove variabili di configurazione nel modelValidate stesso e quindi si elimina il rischio 
	 * di regressioni per tutti gli altri bandi
	 * 
	 * @param validationMethodsClass e' una classe specifica di un certo blocchetto che contiene l'implementazione dei metodi da eseguire
	 * 
	 * @param input E' la classe Input di un blocchetto, un oggetto quindi che contiene tutti i dati necessari per eseguire i metodi 
	 * di validazione. Se contiene un campo "validationMethods...", questo deve essere public e mappato su omonime var 
	 * di configurazione, vuote in FinDirettiva e popolate in Direttiva se il bando necessita di validazioni aggiuntive. 
	 * Nelle classi Direttiva dei vari bandi possono essere sovrascritte con stringhe json 
	 * contenenti l'elenco dei metodi (ciascuno con i relativi parametri attuali) che si vogliono chiamare nella validazione dei blocchetti.
	 * Se la classe Input del blocchetto non ha un campo "validationMethods..." o tale variabile vale "", come e' nel caso dei bandi che non hanno bisogno
	 * di validazioni aggiuntive rispetto alle validazioni standard del modelValidate(), questo metodo non fa nulla, 
	 * anche se richiamato in un blocchetto che il bando usa
	 * 
	 * @param logger
	 * @return una lista di SegnalazioneErrore su cui il chiamante cicla per visualizzare a video gli eventuali errori 
	 */
	public static ArrayList<SegnalazioneErrore> validate(Class<?> validationMethodsClass, CommonalityInput input, Logger logger){  
		
		String prf = "[ValidationUtil::validate] ";
		logger.info(prf + "BEGIN");
		
		ArrayList<SegnalazioneErrore> result = null;
		if(validationMethodsClass == null || input == null){
			return result;
		}
		
		String validationMethodsConfig = getValidationMethodsConfig(input, logger);
		logger.info(prf + "validationMethodsConfig: " + validationMethodsConfig);
		
		if(StringUtils.isBlank(validationMethodsConfig)){
			//la classe input del blocchetto non contiene un attributo mappato su una var di configurazione di nome "validationMethods..." 
			//oppure lo contiene, ma il suo valore e' ""
			return result;
		}
		
		//ricavo dalla var di configurazione ("validationMethods...") l'eventuale lista di metodi da chiamare 		
		try {
			ArrayList<MethodCall> methodCallList = null;
			methodCallList = JsonUtil.toObject(validationMethodsConfig);
			for (int j = 0; j < methodCallList.size(); j++) {
				logger.info(prf + "No: " + (j+1) + ") debug:: methodCallList: " + methodCallList.get(j).getMethodName()); // ctrlCoerenzaPremialitaIntervento
			}
			
			if(methodCallList==null || methodCallList.isEmpty()){
				return result;
			}
			result = new ArrayList<SegnalazioneErrore>();	
			logger.info(prf + "result: " + result);
			
			for (MethodCall methodCall : methodCallList) {	

				//preparo gli argomenti da passare al metodo rappresentato da methodCall
				String methodNameConfig = methodCall.getMethodName();
				logger.info(prf + "methodNameConfig: " + methodNameConfig); // ctrlCoerenzaInterventoMaxContributo, ctrlCoerenzaInterventoDimImpresa, ctrlCoerenzaInterventoRuolo
				
				//Array di Object da passare all'invoke come secondo parametro; contiene sempre in posizione 0 il parametro input di un sottotipo di CommonalityInput 
				//e in posizione 1 il Logger; nelle posizioni successive seguono gli eventuali parametri args, objArg e varargs del 
				//MethodCall corrente (in questo ordine e tutti opzionali)
				Object[] args = getArgsConfig(methodCall,input, logger); // 122,120

				for (Method curMethod : validationMethodsClass.getDeclaredMethods()) 
				{
					logger.info(prf + "curMethod: " + curMethod.getName() + " vs " + methodNameConfig);
					
					if(curMethod!=null && curMethod.getName().equalsIgnoreCase(methodNameConfig)){						
						ArrayList<SegnalazioneErrore> segnalazioneErroreList = (ArrayList<SegnalazioneErrore>) curMethod.invoke(null, args);
						logger.info(prf + "segnalazioneErroreList: " + segnalazioneErroreList);
						
						if(segnalazioneErroreList!=null && !segnalazioneErroreList.isEmpty()){
							result.addAll(segnalazioneErroreList);
						}
						break; //il metodo e' stato trovato ed eseguito, si puo' interrompere il ciclo interno e passare all'eventuale successivo metodo configurato 
					}
				}	
			}
		} catch (IOException e) {
			logger.warn(prf + " si e' verificata una IOException ", e);
		} catch (SecurityException e) {
			logger.warn(prf + " si e' verificata una SecurityException ", e);
		} catch (IllegalAccessException e) {
			logger.warn(prf + " si e' verificata una IllegalAccessException ", e);
		} catch (IllegalArgumentException e) {
			logger.warn(prf + " si e' verificata una IllegalArgumentException ", e);
		} catch (InvocationTargetException e) {
			logger.warn(prf + " si e' verificata una InvocationTargetException ", e);
		}
		logger.info(prf + "END");
		return result;
		
	}
	
	// costruisce e ritorna l'array di Object da passare all'invoke
	private static Object[] getArgsConfig(MethodCall methodCall,CommonalityInput input, Logger logger){
		Object[] args = null; 
		String[] argsConfig = methodCall.getArgs();
		String[] varargsConfig = methodCall.getVarargs(); 
		Object objArgConfig = methodCall.getObjArg();
		int numElementiParametro = 2;  //due parametri fissi sempre presenti

		if(argsConfig!=null && argsConfig.length>0){
			numElementiParametro += argsConfig.length;
		}				
		if (objArgConfig!=null){
			numElementiParametro++; //il parametro Object occupa l'ultima o la penultima posizione nell'array di Object passato all'invoke
		}
		if(varargsConfig!=null && varargsConfig.length>0){
			numElementiParametro++;  //se ci sono vararg sono sempre in ultima posizione (un array di String) nell'array di Object passato all'invoke
		}
		//contiene l'oggetto input, il Logger e gli eventuali argomenti definiti nell'oggetto MethodCall che si trova in formato json su var di configurazione
		args = new Object[numElementiParametro];  				
		args[0] = input;
		args[1] = logger;

		
		if(argsConfig!=null && argsConfig.length>0){			
			for(int i = 0; i < argsConfig.length; i++) {
				args[i+2] = argsConfig[i];
			}
		}
		
		int argConfigLength = 0; //per stabilire l'indice dove mettere l'eventuale objArgConfig
		if(argsConfig!=null && argsConfig.length>0){
			argConfigLength = argsConfig.length;
		}
		
		if (objArgConfig!=null){			
			args[2+argConfigLength] = objArgConfig;
		}
		
		if(varargsConfig!=null && varargsConfig.length>0){
			args[numElementiParametro-1] = varargsConfig;
		}
		
		return args;
		
	}

	//estrae via reflection dall'oggetto input il valore dell'attributo "validationMethods...", se esiste
	private static String getValidationMethodsConfig(CommonalityInput input, Logger logger) {
		String validationMethodsConfig= "";
		try {
			Class<? extends CommonalityInput> inputClass = input.getClass();
			Field[] fields = inputClass.getDeclaredFields();
			if (fields!=null && fields.length>0){
				for (Field field : fields) 
				{
					logger.info("[ValidationUtil::getValidationMethodsConfig] field.getName() vale: " + field.getName()); // validationMethodsAbstractProgettoNG
					
					if(field.getName().startsWith("validationMethods")){
						//recupera il valore dell'attributo, che e' quello di default da Findirettiva o quello specifico del bando da Direttiva
						logger.info("[ValidationUtil::getValidationMethodsConfig] name:[" + field.getName() + "] input:[" + input + "]");
						
						validationMethodsConfig = (String) field.get(input); 
						logger.info("[ValidationUtil::getValidationMethodsConfig] validationMethodsConfig = [" + validationMethodsConfig+"]");
						break;
					}
				}
			}
		} catch (Exception e1) {
			logger.info("[ValidationUtil::getValidationMethodsConfig] si e' verificata una eccezione recuperando il valore dell'attributo 'validationMethods... dalla classe Input\n ", e1);
		}
		return validationMethodsConfig;
	}
	
}
