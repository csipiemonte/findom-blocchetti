/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import it.csi.findom.blocchetti.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil implements Serializable {	
	private static final long serialVersionUID = 1L;

	/**
	 *  converte un ArrayList<MethodCall> in una stringa json che viene ritornata al chiamante
	 * @param mcl
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toJson(ArrayList<MethodCall> mcl) throws JsonProcessingException{
		String jsonRes = "";
		if(mcl==null || mcl.isEmpty()){
			return jsonRes;
		}
		ObjectMapper mapper = new ObjectMapper();		
		jsonRes = mapper.writeValueAsString(mcl);
		return jsonRes;
	}
	
	/**
	 * converte la stringa json in input in un oggetto ArrayList<MethodCall> che viene ritornato al chiamante
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<MethodCall> toObject(String json) throws IOException{
		if(StringUtils.isBlank(json)){
			return null;
		}
		ArrayList<MethodCall> mclres = new  ArrayList<MethodCall>();
		ObjectMapper mapper = new ObjectMapper();
		mclres = mapper.readValue(json,new TypeReference<ArrayList<MethodCall>>() { });//senza TypeReference otterrei un ArrayList<LinkedHashMap> 			
		return mclres; 
	}
	
//	public static void main(String[] args){
//		ValidationUtil vu = new ValidationUtil();
//		
//		MethodCall mc1 = new MethodCall();
//		mc1.setMethodName("ctrlInterventoDurata");
//		String[] actualPar1 = new String[] {"9", "12"};
//		mc1.setArgs(actualPar1);
//		
//		MethodCall mc2 = new MethodCall();
//		mc2.setMethodName("ctrlInterventoDurata");
//		String[] actualPar2 = new String[] {"10", "18"};
//		mc2.setArgs(actualPar2);
//		
//		ArrayList<MethodCall> methodCallListDef = new ArrayList<>();
//		methodCallListDef.add(mc1);
//		methodCallListDef.add(mc2);		
//		
//		try {
//			//da ArrayList<MethodCall> a stringa json
//			String methodCallListJson = vu.toJson(methodCallListDef);
//			System.out.println(methodCallListJson);
//			//[{"methodName":"ctrlInterventoDurata","args":["9","12"]},{"methodName":"ctrlInterventoDurata","args":["10","18"]}]
//			
//			//da stringa json a ArrayList<MethodCall> 
//			ArrayList<MethodCall> methodCallListObj = vu.toObject(methodCallListJson);
//			 
//			methodCallListObj.toString();
//		} catch (JsonProcessingException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
