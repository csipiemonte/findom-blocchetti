/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import java.io.Serializable;

/**
 * classe che contiene le informazioni necessarie ad individuare e invocare via reflection un metodo (nome metodo e parametri)
 * @author mauro.bottero
 *
 */
public class MethodCall implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	//obbligatorio quanto si crea un oggetto MethodCall
	private String methodName; 
	
	//opzionale; serve da contenitore per un certo numero di parametri String, stabilito dalla firma del metodo
	private String[] args;

	//opzionale; serve per contenere parametri di qualunque tipo; da usare nei casi piu' complessi per i quali non sono sufficienti arg e/o varargs; 
	//viene mappato sul penultimo parametro del metodo nel caso ci sia varargs valorizzato, 
	//sull'ultimo parametro del metodo in caso contrario. Il parametro del metodo deve essere un Object, e il metodo deve sapere 
	//come recuperare da tale object le informazioni che gli servono, coerentemente con la valorizzazione fatta nella creazione dell'oggetto MethodCall
	private Object objArg;

	//opzionale; serve per metodi che come ultimo parametro hanno un numero arbitrario (varargs) di coppie di valori String vincolati tra loro 
	//(1 valore vincolato ad 1 e 1 solo altro valore); es. un certo idPremialita e' selezionabile solo se e' selezionato un certo idTipoIntervento
	private String[] varargs;
	
	public MethodCall() {
		super();		
	}
	
	public MethodCall(String methodName) {
		super();
		this.methodName = methodName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	public String[] getArgs() {
		return args;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}	
	public void setArgs(String[] args) {
		this.args = args;
	}
	public String[] getVarargs() {
		return varargs;
	}
	public void setVarargs(String[] varargs) {
		this.varargs = varargs;
	}
	public Object getObjArg() {
		return objArg;
	}
	public void setObjArg(Object objArg) {
		this.objArg = objArg;
	}
		
}
