/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class Indicatori extends Commonality {

	  IndicatoriInput input = new IndicatoriInput();

	  @Override
	  public IndicatoriInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    
	    try {
	        IndicatoriOutput output = new IndicatoriOutput();
	        
			logger.info("Indicatori::inject] _indicatori BEGIN");
			
		//// dichiarazioni
			List<IndicatoreResultVO> tipoIndicatoreTmpList = new ArrayList<IndicatoreResultVO>();  //contiene gli indicatori estratti dal db non raggruppati per tipo indicatore 
			TipoIndicatoreVO[] tipoIndicatoreList = new TipoIndicatoreVO[0];  //contiene gli indicatori estratti dal db raggruppati per tipo indicatore, per cui e' una lista di 
													    //mappe ciascuna rappresentante un tipo indicatore; ogni mappa ha a sua volta una lista di mappe che  
													    //contiene gli indicatori che sono associati a quel tipo indicatore
				
		    String idBando = info.getStatusInfo().getTemplateId()+"";
		    logger.info("Indicatori::inject]  idBando="+ idBando);
			
		    String dataInvio = "";
		    if(info.getStatusInfo().getDataTrasmissione()!=null){
			   
               dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
			}	        
		          
		    tipoIndicatoreTmpList = IndicatoriDAO.getIndicatoriList(idBando, dataInvio, logger); //lista presa da tabelle DB, senza struttura
		    
		    // se bando richiede lista inversa : asd covid19
		    if ("true".equals(input._indicatori_reverse_descrizione_list)) 
		    {
		    	Collections.reverse(tipoIndicatoreTmpList);
		    }

		    tipoIndicatoreList = IndicatoriDAO.raggruppaIndicatoriPerTipoIndicatore(tipoIndicatoreTmpList, logger);  //lista strutturata 
		    
		    
		    /* debug per Jira: 1797 inizio */
		    logger.info("debug tipoIndicatoreTmpList for: ");
//		    for (int i = 0; i < tipoIndicatoreTmpList.size(); i++) {
//		    	logger.info("Indicatori::inject]  IdIndicatore="+ tipoIndicatoreTmpList.get(i).getIdIndicatore());
//		    	logger.info("Indicatori::inject]  CodIndicatore()="+ tipoIndicatoreTmpList.get(i).getCodIndicatore());
//		    	logger.info("Indicatori::inject]  DescrIndicatore()="+ tipoIndicatoreTmpList.get(i).getDescrIndicatore());
//		    	logger.info("Indicatori::inject]  UnitaMisuraIndicatore()="+ tipoIndicatoreTmpList.get(i).getUnitaMisuraIndicatore());
//		    	logger.info("Indicatori::inject]  TipoIndicatore()="+ tipoIndicatoreTmpList.get(i).getIdTipoIndicatore());
//		    	logger.info("Indicatori::inject]  LinkIndicatore()="+ tipoIndicatoreTmpList.get(i).getLinkIndicatore());
//		    	logger.info("Indicatori::inject]  DescrTipoIndicatore()="+ tipoIndicatoreTmpList.get(i).getDescrTipoIndicatore());
//		    	logger.info("Indicatori::inject]  FlagObbligatorio()="+ tipoIndicatoreTmpList.get(i).getFlagObbligatorio());
//		    	logger.info("Indicatori::inject]  FlagAlfa()="+ tipoIndicatoreTmpList.get(i).getFlagAlfa());
//		    	logger.info("Indicatori::inject]  ValoreIndicatore()="+ tipoIndicatoreTmpList.get(i).getValoreIndicatore());
//			}
		    logger.info("tipoIndicatoreList for: ");
		    for (int i = 0; i < tipoIndicatoreList.length; i++) {
		    	logger.info("Indicatori::inject]  debug: tipoIndicatoreList= "+ tipoIndicatoreList[i].toString());
			}
		    
		    //cerco nell'xml i valori da mettere in tipoIndicatoreList, e in particolare nelle liste di indicatori di ciascun tipo indicatore
		    if(tipoIndicatoreList!=null){
		    	IndicatoriVO _indicatori = (IndicatoriVO) input._indicatori; 			   
			    if(_indicatori!=null){ 
			        if(_indicatori.getTipoIndicatoreList()!=null){ 
				        List<TipoIndicatoreVO> tipoIndicatoreSalvatoList = (List<TipoIndicatoreVO>)Arrays.asList(_indicatori.getTipoIndicatoreList());  //presa da xml
				        for(int i=0; i<tipoIndicatoreList.length;i++){  //ciclo sui dati estratti dal DB, non dall'xml
			        	   TipoIndicatoreVO curTipoIndicatore=(TipoIndicatoreVO)tipoIndicatoreList[i]; 
			        	   if(curTipoIndicatore!=null){			             
			                   for(Object o : tipoIndicatoreSalvatoList){  //ciclo sui dati dell'xml
			                	   TipoIndicatoreVO curTipoIndicatoreSalvato = (TipoIndicatoreVO)o;
			                       if(curTipoIndicatoreSalvato!=null){
			                                        
			                          if(curTipoIndicatore.getIdTipoIndicatore().equals(curTipoIndicatoreSalvato.getIdTipoIndicatore())){                                        
                                         
                                          //proseguo ricerca nella lista degli indicatori
                                          List<IndicatoreItemVO> indicatoriList = (List<IndicatoreItemVO>)Arrays.asList(curTipoIndicatore.getIndicatoriList());
                                          List<IndicatoreItemVO> indicatoriSalvatiList = (List<IndicatoreItemVO>)Arrays.asList(curTipoIndicatoreSalvato.getIndicatoriList());
							              if(indicatoriList!=null && !indicatoriList.isEmpty() && indicatoriSalvatiList!=null && !indicatoriSalvatiList.isEmpty()){

								               for(int j=0; j<indicatoriList.size();j++){  //ciclo sul dettaglio preso dal DB (non dall'xml)                       
								            	   IndicatoreItemVO curIndicatore = (IndicatoreItemVO) indicatoriList.get(j);

									               if(curIndicatore!=null){
										              for(IndicatoreItemVO indSalvato : indicatoriSalvatiList){	//ciclo sul dettaglio preso dall'xml 								 
										            	  IndicatoreItemVO curIndicatoreSalvato = (IndicatoreItemVO)indSalvato;
											              if(curIndicatoreSalvato!=null){											 
												             if(curIndicatore.getIdIndicatore().equals(curIndicatoreSalvato.getIdIndicatore())){                                        
													            String valoreIndicatore = (String)curIndicatoreSalvato.getValoreIndicatore();
													            if(StringUtils.isBlank(valoreIndicatore)){
														           valoreIndicatore = "";
													            }	
													            logger.debug("Indicatori::inject] valorizzazione: idTipoIndicatore = " + (String) curTipoIndicatore.getIdTipoIndicatore() + " ; idIndicatore = " + (String) curIndicatore.getIdIndicatore() + " ; valore =  " + valoreIndicatore );										

													            curIndicatore.setValoreIndicatore(DecimalFormat.decimalFormat(valoreIndicatore,2));
												             }//chiude test uguaglianza su idIndicatore
												          }//chiude test null su curIndicatoreSalvato  
												      } //chiude for su indicatoriSalvatiList 
												   } //chiude test null su curIndicatore
											   } //chiude for su indicatoriList                           
											}//chiude if null-isEmpty su 2 liste di indicatori 
			                          
			                          }//chiude test uguaglianza su idTipoIndicatore di curTipoIndicatore e curTipoIndicatoreSalvato
			                       }//chiude test null su curTipoIndicatoreSalvato
			                   } //chiude for su tipoIndicatoreSalvatoList
			              }  //chiude test null su curTipoIndicatore
			           }//chiude for su tipoIndicatoreList
			        } //chiude test null su tipoIndicatoreSalvatoList			        
			    } //chiude test null su _indicatori
			  } //chiude test null su tipoIndicatoreList    	

					//// namespace
			output.setTipoIndicatoreList(tipoIndicatoreList);
						
	        return output;
	    }
	    finally {
			logger.info("Indicatori::inject] _indicatori END");
	    }
	  }

	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
			
		logger.info("Indicatori::modelValidate] _indicatori  BEGIN");

		try {
			
			String ERRMSG_ALMENO_UN_INDICATORE = "- valorizzare almeno un indicatore";
			String ERRMSG_VALORE_FORMATO_NUM = "- il valore deve essere numerico con al massimo 2 decimali";
			String ERRMSG_VALORE_OBBLIGATORIO = "- il campo è obbligatorio"; //MB2018_02_01
			String ERRMSG_VALORE_MAGGIORE_UGUALE_UNO = "- il valore digitato deve risultare maggiore od uguale ad 1";

			//// validazione panel Indicatori Progetto
			int numIndicatoriValorizzati = 0;
			int numIndicatoriObbligatori = 0;   //MB2018_02_01
					 
			IndicatoriVO _indicatori = (IndicatoriVO) input._indicatori;
			if (_indicatori != null) {					
			   //almeno un valore indicatore deve essere valorizzato   
			   if(_indicatori.getTipoIndicatoreList()!=null){
				   List<TipoIndicatoreVO> tipoIndicatoreList = (List<TipoIndicatoreVO>)Arrays.asList(_indicatori.getTipoIndicatoreList());		   		  		   

				   for(int i=0; i<tipoIndicatoreList.size();i++){
					   TipoIndicatoreVO curTipoIndicatore=(TipoIndicatoreVO)tipoIndicatoreList.get(i);						  
					   if(curTipoIndicatore!=null){					
						  
						   if(curTipoIndicatore.getIndicatoriList()!=null && curTipoIndicatore.getIndicatoriList().length>0){
							   List<IndicatoreItemVO> curIndicatoriList = (List<IndicatoreItemVO>)Arrays.asList(curTipoIndicatore.getIndicatoriList());
						 
							   for(int j=0; j<curIndicatoriList.size();j++){
								   IndicatoreItemVO curIndicatore=(IndicatoreItemVO)curIndicatoriList.get(j);	
								   logger.info("codIndicatore risulta: " + curIndicatore.getCodIndicatore());
								   if(curIndicatore!=null){
							           String valoreIndicatore = (String) curIndicatore.getValoreIndicatore();
							           logger.info("valoreIndicatore salvato corrente vale: " + valoreIndicatore);
							           
							           /** Jira: 1935 -inizio  */
						        	   if ("true".equals(input._indicatori_controllo_valore_indicatore)) {
							        		// eseguo controllo che valore dgt sia >=1
						        		   if(curIndicatore.getIdIndicatore()!= null && curIndicatore.getIdIndicatore().equals("68")){
						        			   if(valoreIndicatore!=null && !valoreIndicatore.isEmpty()){
						        				   if(valoreIndicatore.matches("^\\d+(,\\d{1,2})?$")){
						        					   // verifico se >=1
						        					   boolean isMaggioreUgualeUno = checkValoreIndicatore(valoreIndicatore, logger);
						        					   if(!isMaggioreUgualeUno){
						        						   String id = curTipoIndicatore.getIdTipoIndicatore()+"_"+curIndicatore.getIdIndicatore();
						        						   addMessage(newMessages,"_indicatori_valore_testo",id + ERRMSG_VALORE_MAGGIORE_UGUALE_UNO);
						        						   addMessage(newMessages,"_indicatori_valore_id", id);
						        						   logger.warn("Indicatori::modelValidate] indicatore valorizzato deve risultare un numero >=1 ");
						        					   }
						        				   }
						        			   }
						        		   }
							   		   }/** Jira: 1935 -fine  */
							        	   
							           //MB2018_02_01 ini
							           String flagObbligatorio = (String) curIndicatore.getFlagObbligatorio();  
							           logger.info("flagObbligatorio salvato corrente vale: " + flagObbligatorio);
							           
							           String flagAlfa = (String) curIndicatore.getFlagAlfa();  
							           logger.info("flagAlfa salvato corrente vale: " + flagAlfa);
							           
							           if(!StringUtils.isBlank(flagObbligatorio) && flagObbligatorio.equals("S")){
							               numIndicatoriObbligatori  += 1;
							               logger.debug("numIndicatoriObbligatori corrente obbligatorio risulta: " + numIndicatoriObbligatori);
							           }
							           //MB2018_02_01 fine
							           if(!StringUtils.isBlank(valoreIndicatore)){		
							        	   logger.info("valoreIndicatore corrente risulta: " + valoreIndicatore);							           
							               numIndicatoriValorizzati +=1;
							               logger.info("numIndicatoriValorizzati corrente risulta: " + numIndicatoriValorizzati);
							               //il valore deve essere numerico con al max 2 decimali
							               
							               /** Jira: 1797 */
							               if( input._indicatori_flag_alfa.equals("true"))
					             		   {
							            	   // bypassa controllo valore indicatore
							            	   if(!StringUtils.isBlank(flagAlfa) && flagAlfa.equalsIgnoreCase("true")){
							            		   logger.info("valoreIndicatore corrente bypassa controllo numerico-decimale: ");
							            		   
							            		   if(!valoreIndicatore.trim().matches("^[a-zA-Z0-9]+$"))
							 				 	  {
							            			  String id = curTipoIndicatore.getIdTipoIndicatore()+"_"+curIndicatore.getIdIndicatore();
							 				 		  logger.warn("Indicatori::modelValidate]  l'importo richiesto deve essere numerico con al max due decimali");			
							 				 		   addMessage(newMessages,"_indicatori_valore_testo", id + "Non sono validi caratteri speciali!"); //MB2018_02_01
							            			   addMessage(newMessages,"_indicatori_valore_id", id);
							 				 	  } else {
							 						//  aggiungo regola che importoRichiesto <= importoProposto (vedere dove prendere importoProposto, vedi inject.bsh)
							 				 	   }
							            	   }else{
							            		   if(!valoreIndicatore.matches("^\\d+(,\\d{1,2})?$")){
							            			   //chiave del record corrente
							            			   String id = curTipoIndicatore.getIdTipoIndicatore()+"_"+curIndicatore.getIdIndicatore();
							            			   logger.info("id tipo indicatore corrente: " + id);
							            			   //MB2018_02_01 addMessage(newMessages,"_indicatori_valore_testo", ERRMSG_VALORE_FORMATO_NUM);
							            			   addMessage(newMessages,"_indicatori_valore_testo", id + ERRMSG_VALORE_FORMATO_NUM); //MB2018_02_01
							            			   addMessage(newMessages,"_indicatori_valore_id", id);
							            			   logger.warn("Indicatori::modelValidate] il valore dell'indicatore deve essere numerico con al massimo 2 decimali ");
							            		   }
							            	   }
					             		   } 
							               else {
							            	   logger.info("valoreIndicatore corrente deve superare il controllo numerico-decimale: ");
						            		   
						            		   if(!valoreIndicatore.matches("^\\d+(,\\d{1,2})?$")){
						            			   //chiave del record corrente
						            			   String id = curTipoIndicatore.getIdTipoIndicatore()+"_"+curIndicatore.getIdIndicatore();
						            			   logger.info("id tipo indicatore corrente: " + id);
						            			   //MB2018_02_01 addMessage(newMessages,"_indicatori_valore_testo", ERRMSG_VALORE_FORMATO_NUM);
						            			   addMessage(newMessages,"_indicatori_valore_testo", id + ERRMSG_VALORE_FORMATO_NUM); //MB2018_02_01
						            			   addMessage(newMessages,"_indicatori_valore_id", id);
						            			   logger.warn("Indicatori::modelValidate] il valore dell'indicatore deve essere numerico con al massimo 2 decimali ");
						            			   
						            		   }
					             		   }
							               logger.info("inizio controllo su valore indicatore corrente essere decimale con virgola: ");
							           }
							           //MB2018_02_01 ini
							           else{
							        	   logger.info("Indicatori::modelValidate] verifica se indicatore obbligatorio ma non valorizzato ");
							             if(!StringUtils.isBlank(flagObbligatorio) && flagObbligatorio.equals("S")){
							                  //chiave del record corrente
						                     String id = curTipoIndicatore.getIdTipoIndicatore()+"_"+curIndicatore.getIdIndicatore();
							               
							                  addMessage(newMessages,"_indicatori_valore_testo",id + ERRMSG_VALORE_OBBLIGATORIO);
							                  addMessage(newMessages,"_indicatori_valore_id", id);
							                  logger.warn("Indicatori::modelValidate] indicatore obbligatorio ma non valorizzato ");
							             }
							           }
							           //MB2018_02_01 fine
							        }			            
							   }
						   }	
				     }//chiude test null su curTipoIndicatore			 
		          }//chiude for su tipoIndicatoreList	          
		          //MB2018_02_01 if(numIndicatoriValorizzati==0 ){
		          if(numIndicatoriValorizzati==0 && numIndicatoriObbligatori ==0){ //MB2018_02_01
					 addMessage(newMessages,"_indicatori", ERRMSG_ALMENO_UN_INDICATORE);					   							        
				  }
			   }
			   			
		} else{
			 logger.warn("Indicatori::modelValidate] _indicatori non presente o vuoto");
		}

		}
		catch(Exception ex) {
			logger.error("[Indicatori::modelValidate] ", ex);
		}
		finally {
			logger.info("Indicatori::modelValidate] _indicatori END");
		}

		return newMessages;

	  }
	  
	  /**
	   * Jira: 1935
	   * @param valoreIndicatore
	   * @param logger
	   * @return
	   */
	  private boolean checkValoreIndicatore(String valoreIndicatore, Logger logger) {
		
		  boolean errore = true;
		
		  String[] s = String.valueOf(valoreIndicatore).split("[,]");
			int valore = Integer.parseInt(s[0]);
			if( valore == 0){
				// segnalo errore con obbligo di digitare valore 1
				errore = false;
			}
		return errore;
	  }


	@Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }

}
