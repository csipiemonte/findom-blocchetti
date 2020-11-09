/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.tipologiaAiutoNG;

import it.csi.findom.blocchetti.common.dao.TipologiaAiutoDAO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.util.ValidationUtil;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.DettaglioAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGVO;
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
import java.util.List;

import org.apache.log4j.Logger;

public class TipologiaAiutoNG extends Commonality {

	  TipologiaAiutoNGInput input = new TipologiaAiutoNGInput();

	  @Override
	  public TipologiaAiutoNGInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[TipologiaAiutoNG::inject] BEGIN");
	    
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	    try {
	        TipologiaAiutoNGOutput output = new TipologiaAiutoNGOutput();      
			
			//// dichiarazioni

			List<TipologiaAiutoNGItemListVO> tipologiaAiutoList = new ArrayList<TipologiaAiutoNGItemListVO>();
			String esistonoDettagli = "true";
			
			//// valorizzazione
			if (info.getCurrentPage() != null) {		   
				if (info.getCurrentPage().contains("S4_P4") ) {		//&& !formState.equals("IN")	
				    String idBando = info.getStatusInfo().getTemplateId()+"";	
				    logger.info("idBando: " + idBando);
				    
				    String dataInvio = "";
				    if(info.getStatusInfo().getDataTrasmissione()!=null){
	                   dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
	                   logger.info("dataInvio: " + dataInvio);
				    }
			         
				    tipologiaAiutoList = TipologiaAiutoNGDAO.getTipologiaAiutoList(idBando, dataInvio, logger); //lista presa da tabelle DB
				    
				    if(tipologiaAiutoList !=null){
				      //a ciascuna tipologia aiuto associo il rispettivo (eventuale) dettaglio
				      //MB2016_04_11int numTipologieConDettaglio = caricaDettaglioTipologiaAiuto(tipologiaAiutoList);	
				      int numTipologieConDettaglio = caricaDettaglioTipologiaAiuto(tipologiaAiutoList,dataInvio, idBando, logger);	//MB2016_04_11	      
				      logger.info("numTipologieConDettaglio: " + numTipologieConDettaglio);
				      
				      TipologiaAiutoNGVO _tipologiaAiutoMap = (TipologiaAiutoNGVO) input._tipologiaAiuto; 			   
				      if(_tipologiaAiutoMap!=null){	
				    	  if(_tipologiaAiutoMap.getTipologiaAiutoList()!=null){  
				    		  List<TipologiaAiutoNGItemListVO> tipologiaAiutoSalvataList = Arrays.asList(_tipologiaAiutoMap.getTipologiaAiutoList());  //presa da xml
				    		  for(int i=0; i<tipologiaAiutoList.size();i++){
				               TipologiaAiutoNGItemListVO curTipologia=(TipologiaAiutoNGItemListVO)tipologiaAiutoList.get(i); 
				               if(curTipologia!=null){			             
				                  for(TipologiaAiutoNGItemListVO o : tipologiaAiutoSalvataList){
				                	  TipologiaAiutoNGItemListVO curTipologiaSalvata = (TipologiaAiutoNGItemListVO)o;
				                     if(curTipologiaSalvata!=null){
				                                        
				                        if(curTipologia.getIdTipoAiuto().equals(curTipologiaSalvata.getIdTipoAiuto())){
				                        	logger.info("curTipologiaID: " + curTipologia.getIdTipoAiuto());
				                        	logger.info("curTipologiaSalvataID: " + curTipologiaSalvata.getIdTipoAiuto());
				                        	
				                       	    String checked = curTipologiaSalvata.getChecked();
				                       	    logger.info("checked: " + curTipologiaSalvata.getChecked());
				                       	    
				                       	    if(StringUtils.isBlank(checked)){
				                       	        checked = "false";
				                       	        logger.info("checked: " + checked);
				                       	    }	                         
				                       	    
				                            curTipologia.setChecked(checked);
				                            logger.info("checked: " + checked);
				                            break;		                          
				                        }
				                     }//test null su curTipologiaSalvata
				                  }//for interno
				               }//test null su curTipologia
				    		  }//for esterno
				    	  }			        
				      }
				      if(numTipologieConDettaglio==0){
				          esistonoDettagli="false";
				      } 
				      
				      //MB2017_12_01 jira 661 ini 
				      //A questo punto la lista delle tipologie e' stata completata con gli eventuali dettagli e lo stato dei check box e' stato
				      //allineato a quanto eventualmente già salvato sull'xml.
				      //Considero di nuovo la lista; se siamo nel caso di 1 unica tipologia e questa ha 0 o 1 dettagli,imposto a "true" il check della tipologia
				      //e dell'eventuale dettaglio (se e' gia' avvenuto il salvataggio della pagina si tratta di un settaggio di un valore gia' presente)
				      if(tipologiaAiutoList.size() == 1){
				         if(numTipologieConDettaglio<=1 ){			         
				            for(int i=0; i<tipologiaAiutoList.size();i++){
				               TipologiaAiutoNGItemListVO curTipologia=tipologiaAiutoList.get(i); 
				               if(curTipologia!=null){
				            	   logger.info("**********************************************************************************************************");
				            	   logger.info("CodTipoAiuto: " + curTipologia.getCodTipoAiuto() + "\nDescrTipoAiuto: " + curTipologia.getDescrTipoAiuto() + "\nIdTipoAiuto " + curTipologia.getIdTipoAiuto() + "\nchecked: " +curTipologia.getChecked());
				            	   logger.info("**********************************************************************************************************");
				            	   curTipologia.setChecked("true");			                  
				                  if(curTipologia.getDettaglioAiutoList()!=null){
				                	 List<DettaglioAiutoNGItemListVO> dettaglioAiutoList = (List<DettaglioAiutoNGItemListVO>)Arrays.asList(curTipologia.getDettaglioAiutoList());
								  	 for(int j=0; j<dettaglioAiutoList.size();j++){								      
										 DettaglioAiutoNGItemListVO curDettaglio=dettaglioAiutoList.get(j); 
										   logger.info("\n**********************************************************************************************************");
						            	   logger.info("\nIdDettAiuto: " + curDettaglio.getIdDettAiuto() + "\nDescrDettAiuto: " + curDettaglio.getDescrDettAiuto() + "\nChecked: " + curDettaglio.getChecked());
						            	   logger.info("\n**********************************************************************************************************\n\n");
				                         if(curDettaglio!=null && curDettaglio.getChecked().equals("true")){
				                            curDettaglio.setChecked("true");
									     }else{
									    	 curDettaglio.setChecked("false"); 
									     }
									 }//for su dettaglioAiutoList			                  
				                  }//test null su dettaglioAiutoList			            
				               }//test null su curTipologia
				            }//for esterno			         
				         }
				      }
				      //MB2017_12_01 jira 661 fine
				      
				    }//chiude test null di tipologiaAiutoList			    		    
				}			
			}

			//// namespace
			output.setTipologiaAiutoList(tipologiaAiutoList);
			output.setEsistonoDettagli(esistonoDettagli); 
		
	        return output;
	    }
	    finally {
	      logger.info("[TipologiaAiutoNG::inject] END");
	    }
	  }
	  
	  
	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  Logger logger = Logger.getLogger(info1.getLoggerName());	    		
		  logger.info("[TipologiaAiutoNG::modelValidate] _tipologiaAiutoNG  BEGIN");
		  
		  FinCommonInfo info = (FinCommonInfo) info1;
		  List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
		  
		  String ERRMSG_NUM_TIPO_AIUTO_SELEZ = "";
		  if( input._progetto_agevolazione_uno_e_uno_solo_tipo_aiuto.equals("true")){
			  ERRMSG_NUM_TIPO_AIUTO_SELEZ = "- Selezionare una sola tipologia di aiuto";
		  }else{
			  ERRMSG_NUM_TIPO_AIUTO_SELEZ = "- Selezionare almeno una tipologia di aiuto";
		  }
		  
		  String ERRMSG_NUM_DETTAGLI_AIUTO_SELEZ = "";
		  if (input._progetto_agevolazione_uno_e_uno_solo_dettaglio.equals("true")){
			  ERRMSG_NUM_DETTAGLI_AIUTO_SELEZ = "- Selezionare un solo dettaglio per ciascuna tipologia di aiuto indicata";
		  } else{
			  ERRMSG_NUM_DETTAGLI_AIUTO_SELEZ = "- Selezionare almeno un dettaglio per ciascuna tipologia di aiuto indicata";
		  }
		  
		  String ERRMSG_TIPO_AIUTO_OBBLIGATORIA = "- E' obbligatorio selezionare la tipologia di aiuto ";
		  try {			  
			  if (input._tipologiaAiuto != null) {
				  if(input._tipologiaAiuto.getTipologiaAiutoList()!=null && input._tipologiaAiuto.getTipologiaAiutoList().length>0 ){
					  List<TipologiaAiutoNGItemListVO> tipologiaAiutoList = Arrays.asList(input._tipologiaAiuto.getTipologiaAiutoList());
					  int numTipologieSelezionate = 0;
					  boolean erroreSegnalato = false;
					  if(tipologiaAiutoList!=null){
						  for(int i=0; i<tipologiaAiutoList.size();i++){
							  TipologiaAiutoNGItemListVO curTipologia=(TipologiaAiutoNGItemListVO)tipologiaAiutoList.get(i);						
							  if(curTipologia!=null){
								  String checked = (String) curTipologia.getChecked();
								  if(!StringUtils.isBlank(checked) && checked.equals("true")){
									  numTipologieSelezionate += 1;

									  // verifico quanti dettagli (se ce ne sono) sono stati selezionati
									  int numDettSelezionati = 0;
								  if(curTipologia.getDettaglioAiutoList()!=null){
									  List<DettaglioAiutoNGItemListVO> curDettaglioList = Arrays.asList(curTipologia.getDettaglioAiutoList());
									  if(curDettaglioList!=null && !curDettaglioList.isEmpty()){
										  for(int j=0; j<curDettaglioList.size();j++){
											  DettaglioAiutoNGItemListVO curDettaglio=(DettaglioAiutoNGItemListVO)curDettaglioList.get(j);					                  
											  if(curDettaglio!=null){
												  String checkedDett = (String) curDettaglio.getChecked();
												  if(!StringUtils.isBlank(checkedDett) && checkedDett.equals("true")){			                     
													  numDettSelezionati +=1;
												  }
											  }			            
										  }
									  }
								  }else{
										  numDettSelezionati = 1; //uniformo il caso di assenza di dettaglio con quello di un dettaglio selezionato
									  }
									  if (input._progetto_agevolazione_uno_e_uno_solo_dettaglio.equals("true")){
										  if(numDettSelezionati==0 || numDettSelezionati>1 ){											 
											  addMessage(newMessages,"_tipologiaAiuto", ERRMSG_NUM_DETTAGLI_AIUTO_SELEZ);
											  break;							        
										  }
									  }else{
										  if(numDettSelezionati==0){
											  addMessage(newMessages,"_tipologiaAiuto", ERRMSG_NUM_DETTAGLI_AIUTO_SELEZ);
											  break;							        
										  }
									  }
								  } else {
									  // verifico l'obbligatorietà della tipologia di aiuto in base a flag_obbligatorio presente sul DB
									  String idTipoAiuto = curTipologia.getIdTipoAiuto();
									  String descrTipoAiuto = curTipologia.getDescrTipoAiuto();
									  String idBando = info.getStatusInfo().getTemplateId() + "";

									  String flagObbligatorio = TipologiaAiutoDAO.getFlagObbligatorio(idBando, idTipoAiuto, logger);
									  if (!StringUtils.isBlank(flagObbligatorio) && (flagObbligatorio.equalsIgnoreCase("S"))){
										  logger.info("[TipologiaAiutoNG::modelValidate] tipologia aiuto obbligatoria ma non selezionata ");
										  addMessage(newMessages,"_tipologiaAiuto", ERRMSG_TIPO_AIUTO_OBBLIGATORIA + descrTipoAiuto + "<BR/>");
										  erroreSegnalato = true;
									  }
								  }
							  } //chiude test null su curTipologia			    			 
						  }//chiude for su tipologiaAiutoList
					  }
					  logger.info("[TipologiaAiutoNG::modelValidate] verifica del numero di tipologie selezionate in base alla configurazione del bando; numTipologieSelezionate =  "+ numTipologieSelezionate);
					  if(!erroreSegnalato){  //per evitare di segnalare più errori relativi alle tipologie aiuto
						  if(input._progetto_agevolazione_uno_e_uno_solo_tipo_aiuto.equals("true")){					         		 
							  if(numTipologieSelezionate==0 || numTipologieSelezionate>1){
								  logger.info("[TipologiaAiutoNG::modelValidate] nessuna o più di una tipologia aiuto selezionata, ma il bando è configurato per accettare una e una sola tipologia selezionata ");
								  addMessage(newMessages,"_tipologiaAiuto", ERRMSG_NUM_TIPO_AIUTO_SELEZ);
							  }else{
								  if(numTipologieSelezionate==0 ){
									  logger.info("[TipologiaAiutoNG::modelValidate] nessuna tipologia di aiuto selezionata, ma il bando è configurato per accettare almeno una tipologia selezionata ");
									  addMessage(newMessages,"_tipologiaAiuto", ERRMSG_NUM_TIPO_AIUTO_SELEZ);
								  }
							  }
						  }
					  }
					  
					//metodo generico che tramite reflection chiama eventuali ulteriori metodi di validazione, 
					//i cui nomi e argomenti sono contenuti nella var di configurazione validationMethodsCaratteristicheProgettoNG
					logger.info("[TipologiaAiutoNG::modelValidate()] inizio validazioni definite da variabile di configurazione");
					ArrayList<SegnalazioneErrore> segnalazioneList = ValidationUtil.validate(TipologiaAiutoNGValidationMethods.class, input, logger);
					if(segnalazioneList!=null && !segnalazioneList.isEmpty()){
						for (SegnalazioneErrore segnalazioneErrore : segnalazioneList) {
							addMessage(newMessages, segnalazioneErrore.getCampoErrore(), MetodiUtili.prefixErrMsg(newMessages,segnalazioneErrore));			
						}
					}
					logger.info("[TipologiaAiutoNG::modelValidate()] fine validazioni definite da variabile di configurazione");
					  
					  
					  
				  } else{
					  logger.info("[TipologiaAiutoNG::modelValidate] _tipologiaAiuto non presente o vuoto");
				  }
			  }
		  }catch(Exception ex) {
			  logger.error("[TipologiaAiutoNG::modelValidate] ", ex);
		  }
		  finally {
			  logger.info("TipologiaAiutoNG::modelValidate] _tipologiaAiutoNG END");
		  }
		  return newMessages;
	  } 

	  
	  /**
	   * commandValidate
	   */
	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages)  {
	    // nothing to validate
	    return null;
	  }
	  
	   		//associa gli eventuali dettagli a ciascuna tipologia di aiuto;
		//ritorna il numero di tipologie di aiuto che hanno almeno un dettaglio 
		//MB2016_04_11 int caricaDettaglioTipologiaAiuto(List parTipologiaAiutoList){	 
	  private int caricaDettaglioTipologiaAiuto(List<TipologiaAiutoNGItemListVO> parTipologiaAiutoList, String dataInvio, String idBando, Logger logger) throws CommonalityException { //MB2016_04_11
		
			int numDettagliValorizzati = 0;  
			
			logger.info("numDettagliValorizzati: " + numDettagliValorizzati);
			
			if(parTipologiaAiutoList==null || parTipologiaAiutoList.isEmpty()){
				return numDettagliValorizzati;
			}		 

			for(int i=0; i<parTipologiaAiutoList.size();i++){			   		           
				TipologiaAiutoNGItemListVO curTipologia=(TipologiaAiutoNGItemListVO)parTipologiaAiutoList.get(i);

				if(curTipologia!=null){
					String idTipoAiuto = (String) curTipologia.getIdTipoAiuto();
					logger.info("\nidTipoAiuto: " + idTipoAiuto );
					
					List<DettaglioAiutoNGItemListVO> dettaglioList = null;
					//MB2016_04_11 List dettaglioList = getDettaglioTipolAiutoList(idTipoAiuto);  //dati da tabelle DB (non sono presi dall'xml)
					
					try {
						dettaglioList = TipologiaAiutoNGDAO.getDettaglioTipolAiutoList(idTipoAiuto, dataInvio, idBando, logger);       //MB2016_04_11 dati da tabelle DB (non sono presi dall'xml) 
					} catch (Exception ex) {
						throw new CommonalityException(ex);
					}
					
					if (dettaglioList != null && !dettaglioList.isEmpty()){				     
						 numDettagliValorizzati+=1;
						 
						 //in dettaglioList valorizzo checked in base al valore eventualmente presente nell'xml
						 TipologiaAiutoNGVO _tipologiaAiutoMap = input._tipologiaAiuto;
						 if(_tipologiaAiutoMap!=null){

							 List<TipologiaAiutoNGItemListVO> tipologiaAiutoSalvataList = (List<TipologiaAiutoNGItemListVO>)Arrays.asList( _tipologiaAiutoMap.getTipologiaAiutoList());  //presa da xml 
							 if(tipologiaAiutoSalvataList!=null && !tipologiaAiutoSalvataList.isEmpty() && tipologiaAiutoSalvataList.get(i)!=null){
							 
								 TipologiaAiutoNGItemListVO curTipologiaSalvata=(TipologiaAiutoNGItemListVO)tipologiaAiutoSalvataList.get(i);
								 List<DettaglioAiutoNGItemListVO> dettaglioSalvatoList = (List<DettaglioAiutoNGItemListVO>)Arrays.asList(curTipologiaSalvata.getDettaglioAiutoList());
								 if(dettaglioSalvatoList!=null && !dettaglioSalvatoList.isEmpty()){

									 for(int j=0; j<dettaglioList.size();j++){  //ciclo sul dettaglio preso dal DB (non dall'xml)    
										 
										 DettaglioAiutoNGItemListVO curDettaglio = (DettaglioAiutoNGItemListVO) dettaglioList.get(j);

										 if(curDettaglio!=null){
											 for(DettaglioAiutoNGItemListVO o : dettaglioSalvatoList){	//ciclo sul dettaglio preso dall'xml 								 
												 DettaglioAiutoNGItemListVO curDettaglioSalvato = (DettaglioAiutoNGItemListVO)o;
												 if(curDettaglioSalvato!=null){
												 
													 if(curDettaglio.getIdTipoAiutoDett().equals(curDettaglioSalvato.getIdTipoAiutoDett()) && curDettaglio.getIdDettAiuto().equals(curDettaglioSalvato.getIdDettAiuto()))
													 {                                        
															String checked = (String)curDettaglioSalvato.getChecked();
															logger.info("[TipologiaAiutoNG::caricaDettaglioTipologiaAiuto] checked su xml vale  = " + checked + " per il dettaglio con id dettaglio =  " + curDettaglioSalvato.getIdDettAiuto() + " relativo alla tipologia : " + curDettaglioSalvato.getIdTipoAiutoDett()  );										
															
															if(StringUtils.isBlank(checked)){
																 checked = "false";
															}
															
															if(curDettaglioSalvato.getChecked().equals("true")){
																curDettaglio.setChecked(checked);
															}
															else{
																 checked = "false";
															}
													  }
													}                              
												 }                           
											 }
										 }
									 
										 
									 }
								 }
							 }										 
						 DettaglioAiutoNGItemListVO[] dettaglioArray = new DettaglioAiutoNGItemListVO[dettaglioList.size()];
						 int ind=0;
						 for (DettaglioAiutoNGItemListVO dett : dettaglioList) {
							 DettaglioAiutoNGItemListVO dettArr = new DettaglioAiutoNGItemListVO();
							 dettArr.setChecked(dett.getChecked());
							 dettArr.setIdDettAiuto(dett.getIdDettAiuto());
							 dettArr.setIdTipoAiutoDett(dett.getIdTipoAiutoDett());
							 dettArr.setDescrDettAiuto(dett.getDescrDettAiuto());
							 dettArr.setCodiceDettAiuto(dett.getCodiceDettAiuto());
							 dettaglioArray[ind++] = dettArr;
						 }
						 curTipologia.setDettaglioAiutoList(dettaglioArray);
						 int numDettagli = dettaglioList.size();
						 curTipologia.setNumDettagli(numDettagli+"");
						} 		      
					}	   
				}
			
			logger.info("[TipologiaAiutoNG::caricaDettaglioTipologiaAiuto] numDettagliValorizzati = " + numDettagliValorizzati); 
				return numDettagliValorizzati;
			}
}
