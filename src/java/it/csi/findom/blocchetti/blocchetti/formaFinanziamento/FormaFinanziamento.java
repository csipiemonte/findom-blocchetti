/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import it.csi.findom.blocchetti.blocchetti.descrizioneFiera.DescrizioneFieraVO;
import it.csi.findom.blocchetti.blocchetti.descrizioneFieraSecEd.DescrizioneFieraSecEdVO;
import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.dao.PianoSpeseDAO;
import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.util.ValidationUtil;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


public class FormaFinanziamento extends Commonality {

	  FormaFinanziamentoInput input = new FormaFinanziamentoInput();

	  @Override
	  public FormaFinanziamentoInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;
	    
	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[FormaFinanziamento::inject] BEGIN");

	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    
	    /** innometro */
	    String contributoContoInteresse = "";
	    String impContrContoInteresse 	= "0";
	    String totaleSpeseAttuale 		= "0";
	    boolean isEqualZero = false;
	    BigDecimal valoreMinimoBD = new BigDecimal(0);
	    
	    try {
	        FormaFinanziamentoOutput output = new FormaFinanziamentoOutput();      
			String cb1 = "false";
			String cb21 = "false";
			
			// dichiarazioni

			List<TipoFormaFinanziamentoVO> formaFinanziamentoList = new ArrayList<TipoFormaFinanziamentoVO>();
			String importoProposto = "0";  // preso da piano spese
			String importoRichiesto = "0";  // digitato da utente o in sola lettura e somma importi delle singole forme di agevolazione
			
			String idBando = info.getStatusInfo().getTemplateId()+"";
		    logger.info("[FormaFinanziamento::inject] idBando: " + idBando);
		    
		    String dataInvio = "";
		    if(info.getStatusInfo().getDataTrasmissione()!=null){
               dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
               logger.info("[FormaFinanziamento::inject] dataInvio: " + dataInvio);
		    }
			
			String msgWarning = "";	
			
			/** Jira: 1646 */
			String[] nascondereIDCheckbox = null;
			// List<String> hd = new ArrayList<String>();
			List<HiddenCB> hd = new ArrayList<HiddenCB>();
			
			//// valorizzazione
			if (info.getCurrentPage() != null) {
			  
				PianoSpeseVO _pianoSpese = input._pianoSpese;
				if(_pianoSpese!=null){ 
				    importoProposto = DecimalFormat.decimalFormat(_pianoSpese.getTotale(),2);
				    logger.info("[FormaFinanziamento::inject] importoProposto: " + importoProposto); // 35000,00 - Pitef(1)
				}
				else{
					 msgWarning = msgWarning + "Attenzione! piano spese non risulta compilato."; 
					logger.debug("[FormaFinanziamento::inject] piano spese non compilato: ");
				}
				
				if (!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) 
				{
				    
				    formaFinanziamentoList = FormaFinanziamentoDAO.getFormeFinanziamentoList(idBando, dataInvio, logger); //lista presa da tabelle DB 
				    
				    
				/** Jira: 1646 - 
			     *  - verifico campi eventuali da nascondere in tab: formaFinanziamento 
			     **/
		         if( input._forma_finanziamento_is_checkbox_nascoste != null 
		        	  && !input._forma_finanziamento_is_checkbox_nascoste.isEmpty()
		        	  && input._forma_finanziamento_is_checkbox_nascoste.equals("true"))
		  		  {
		        	  
		        	  nascondereIDCheckbox = input._forma_finanziamento_id_checkbox_nascoste.split(",");
		        	  
		        	  if (formaFinanziamentoList!=null && !formaFinanziamentoList.isEmpty()) 
		        	  {
						// verifico se hanno id in comune
		        		for (int i = 0; i < formaFinanziamentoList.size(); i++) 
		        		{
		        			for (int j = 0; j < nascondereIDCheckbox.length; j++) {
		        				if(formaFinanziamentoList.get(i).getIdFormaFinanziamento().equals(nascondereIDCheckbox[j])){
		        					HiddenCB curhcb = new HiddenCB();
		        					curhcb.setStr_idCheckbox(nascondereIDCheckbox[j]);
		        					hd.add(curhcb);
		        				}
						}}}
		  		  }
			          
				    if(formaFinanziamentoList!=null && !formaFinanziamentoList.isEmpty()){
				       FormaFinanziamentoVO _formaFinanziamento = input._formaFinanziamento; 	
				       
				       if(_formaFinanziamento!=null){
				    	   
				    	   /** Innometro */
				    	   if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
	          			   {
				    		   importoRichiesto = (String)_formaFinanziamento.getImportoRichiesto();
				    		   logger.info("[FormaFinanziamento::inject] importoRichiesto: " + importoRichiesto);
		          		   }
				    	   else{
				    		   importoRichiesto = DecimalFormat.decimalFormat((String)_formaFinanziamento.getImportoRichiesto(), 2);
				    		   logger.info("[FormaFinanziamento::inject] importoRichiesto: " + importoRichiesto);
		          		   }
				          
				          //se importo richiesto supera totale spese viene visualizzato un warning
				          BigDecimal totaleSpeseBD = new BigDecimal(0);
	                      BigDecimal importoRichiestoBD = new BigDecimal(0);
	   
	                      if(!StringUtils.isBlank(importoProposto)){ 
	                         String importoPropostoTmp = importoProposto;     
		                     importoPropostoTmp = importoPropostoTmp.replace(',', '.');
		                     
		                     totaleSpeseBD = new BigDecimal(importoPropostoTmp);	
		                     logger.info("[FormaFinanziamento::inject] totaleSpeseBD: " + totaleSpeseBD);
	                      }   
	   
	                      if(!StringUtils.isBlank(importoRichiesto)){ 
	                         String importoRichiestoTmp = importoRichiesto;      
	                         importoRichiestoTmp = importoRichiestoTmp.replace(',', '.');
		                     importoRichiestoBD = new BigDecimal(importoRichiestoTmp);
		                     logger.info("[FormaFinanziamento::inject] importoRichiestoBD: " + importoRichiestoBD);
	                      }             
	              
	                      /**
	                       * : Jira: 1671:  Bando Condomini - bypass
	                       * L'importo totale puo superare il totale delle spese.
	                       * Non si richiede la verifica...
	                       * Esiste invece il controllo sui limiti min e max delle singole voci di agevolazione richiesta.
	                       **/
	                      // se totaleSpeseBD > 0 && importoRichiestoBD > totaleSpeseBD
	                      if( input._progetto_forme_finanziamento_imp_min_max_by_bando.equals("true")){
	                    	  logger.info("[FormaFinanziamento::inject] bypassa il controllo sulla spesa totale. ");
	             		  } 
		                      else if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
		          			  {
		                    	  /** : Innometro Cr2:  ... - */
		                    	  logger.info("[FormaFinanziamento::inject] gestione spesa totale differente dallo std per Innometro. "); 
			          		  }
		                      else {
		             			 
		             			 if((totaleSpeseBD.compareTo(new BigDecimal(0)) ==1)  
		             					 && importoRichiestoBD.compareTo(totaleSpeseBD)==1) {                 
		             				 msgWarning = msgWarning + "Attenzione! L'importo richiesto è superiore al totale delle spese."; 
		             				 logger.info("[FormaFinanziamento::inject] msgWarning: " + msgWarning);
		             			 } 			           
		             		  }
				          
				          List<TipoFormaFinanziamentoVO> formaFinanziamentoSalvataList = Arrays.asList(_formaFinanziamento.getFormaFinanziamentoList());  //presa da xml
				          
				          if(formaFinanziamentoSalvataList!=null)
				          { 			          
				             for(int i=0; i<formaFinanziamentoList.size();i++)
				             {  //ciclo sui dati estratti dal DB, non dall'xml
				            	
				            	TipoFormaFinanziamentoVO curFormaFinanziamento = (TipoFormaFinanziamentoVO)formaFinanziamentoList.get(i); 
				                 logger.info("i vale: " + i);
				                 logger.info("curFormaFinanziamento id vale: " + curFormaFinanziamento.getIdFormaFinanziamento());
				                 logger.debug("curFormaFinanziamento ---: ");
				            	if(curFormaFinanziamento!=null){
				            		
				            		/** : Innometro Cr2:  ... - */
				            		if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
				            		{
	                              		for(TipoFormaFinanziamentoVO o : formaFinanziamentoSalvataList){
	                              		   //ciclo sui dati dell'xml
					                	   logger.info("[FormaFinanziamento::inject] ciclo sui dati dell'xml: ");
					                	   TipoFormaFinanziamentoVO curFormaFinanziamentoSalvata = (TipoFormaFinanziamentoVO)o;
						                	  
							                 if(curFormaFinanziamentoSalvata!=null){
						                         if(curFormaFinanziamento.getIdFormaFinanziamento().equals(curFormaFinanziamentoSalvata.getIdFormaFinanziamento())){
							                       	    
						                        	 String checked = curFormaFinanziamentoSalvata.getChecked();//stato del checkbox
							                       	    
						                       	    	if(StringUtils.isBlank(checked)) checked = "false";
						                       	    				                       	                    
							                            curFormaFinanziamento.setChecked(checked);
							                            
							                            if ((input._progetto_agevolrichiesta_importo_unico!=null) 
							                            		&& input._progetto_agevolrichiesta_importo_unico.equals("false")){
							                            			String importoFormaAgevolazione = "0";
							                            	
						                              		if(curFormaFinanziamentoSalvata.getIdFormaFinanziamento().equals("1")){
						                              			// per id1
						                              			if(checked.equals("true")) cb1="true";
						                              			logger.info("[FormaFinanziamento::inject] cb1: " + cb1);
						                              			importoFormaAgevolazione = (String) curFormaFinanziamentoSalvata.getImportoFormaAgevolazione();
							                    				logger.info("[FormaFinanziamento::inject] importoFormaAgevolazione: " + importoFormaAgevolazione);
							                    				
							                    				if(importoFormaAgevolazione!=null && !importoFormaAgevolazione.isEmpty()){
							                    					isEqualZero = verificaStringaZero(importoFormaAgevolazione, logger);
							                    					
							                    					if(!isEqualZero){
							                    						// curFormaFinanziamento.setImportoFormaAgevolazione(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );
							                    						curFormaFinanziamento.setImportoFormaAgevolazione(importoFormaAgevolazione);
							                    					}else{
							                    						importoFormaAgevolazione = "0";
							                    						// curFormaFinanziamento.setImportoFormaAgevolazione(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );
							                    						curFormaFinanziamento.setImportoFormaAgevolazione(importoFormaAgevolazione);
							                    					}
							                    					break;
							                    					}
							                    				}
						                              		
						                              		else{
						                              			
						                              			if(checked.equals("true")) {
						                              				cb21="true";
						                              			}
						                              			logger.info("[FormaFinanziamento::inject] cb21: " + cb21);
						                              			importoFormaAgevolazione = (String) curFormaFinanziamentoSalvata.getContributoContoInteresse();
							                    				logger.info("[FormaFinanziamento::inject] importoFormaAgevolazione: " + importoFormaAgevolazione);
							                    				
							                    				if(importoFormaAgevolazione!=null && !importoFormaAgevolazione.isEmpty()){

							                    					// verifica se zero:
							                    					isEqualZero = verificaStringaZero(importoFormaAgevolazione, logger);
							                    					if(!isEqualZero){
							                    						curFormaFinanziamento.setContributoContoInteresse(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );
							                    					}else{
							                    						importoFormaAgevolazione = "";
							                    						curFormaFinanziamento.setContributoContoInteresse(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );
							                    					}
							                    					break;
							                    					
							                    				}
							                    				else{
							                    					
							                    					importoFormaAgevolazione = "";
							                    					// curFormaFinanziamento.setImportoFormaAgevolazione(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );
							                    					curFormaFinanziamento.setImportoFormaAgevolazione(importoFormaAgevolazione);
							                    					if(checked.equals("false")){
							                    						contributoContoInteresse = "";
							                    						// curFormaFinanziamento.setImportoFormaAgevolazione(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );
							                    						curFormaFinanziamento.setImportoFormaAgevolazione(importoFormaAgevolazione);
							                    						// curFormaFinanziamento.setContributoContoInteresse(DecimalFormat.decimalFormat(contributoContoInteresse, 2) );
							                    						curFormaFinanziamento.setContributoContoInteresse(contributoContoInteresse);
							                    					}
							                    				}
						                              		}}}}}
	                              		/** : Innometro Cr2:  ... - */
	                    			} 
	                              	else 
	                    			{
	                    					// std: //ciclo sui dati dell'xml
						                   for(TipoFormaFinanziamentoVO o : formaFinanziamentoSalvataList)
						                   { 
						                	   logger.info("[FormaFinanziamento::inject] ciclo sui dati dell'xml: ");
						                	   TipoFormaFinanziamentoVO curFormaFinanziamentoSalvata = (TipoFormaFinanziamentoVO)o;
						                	     logger.info("i vale: " + i);
						                	     logger.info("curFormaFinanziamento id vale: " + curFormaFinanziamento.getIdFormaFinanziamento());
								                 logger.info("curFormaFinanziamentoSalvata id vale: " + curFormaFinanziamentoSalvata.getIdFormaFinanziamento());
							                	  if(curFormaFinanziamentoSalvata!=null)
							                	  {
							                		     logger.info("curFormaFinanziamento id vale: " + curFormaFinanziamento.getIdFormaFinanziamento());
							                		     logger.info("curFormaFinanziamentoSalvata id vale: " + curFormaFinanziamentoSalvata.getIdFormaFinanziamento());
							                		     
								                         if(curFormaFinanziamento.getIdFormaFinanziamento().equals(curFormaFinanziamentoSalvata.getIdFormaFinanziamento()))
								                         {
									                       	    String checked = curFormaFinanziamentoSalvata.getChecked();//stato del checkbox
									                       	    if(StringUtils.isBlank(checked)) {
									                       	       checked = "false";
									                       	    }			                       	                    
									                            curFormaFinanziamento.setChecked(checked);
									                            
									                            if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
									                            	String importoFormaAgevolazione = "";
								                    				// std
								                    				importoFormaAgevolazione = (String) curFormaFinanziamentoSalvata.getImportoFormaAgevolazione();
								                    				logger.info("[FormaFinanziamento::inject] importoFormaAgevolazione: " + importoFormaAgevolazione);
								                    				curFormaFinanziamento.setImportoFormaAgevolazione(DecimalFormat.decimalFormat(importoFormaAgevolazione, 2) );			                      
									                            }
								                         }}}}}}
			          	}	
				          
				       //jira FINDOM-147 inizio; seleziono il check box della forma di finanziamento se è l'unico record in tabella		        
				       }else{
				            //sono nel caso di _formaFinanziamento nullo sull'xml, quindi la forma di finanziamento non e' mai stata salvata 			       
				            //se nel model la forma di finanziamento è già stata salvata non entro mai in questo ramo 
				            //(il salvataggio ha comportato che almeno una forma di finanziamento sia stata salvata)
				    	   int numFF = 0;
				    	   numFF = formaFinanziamentoList.size();
				    	   logger.info("forme numero: " + numFF);
				    	   
				            if((formaFinanziamentoList.size() == 1)  
				            		&& (formaFinanziamentoList.get(0) != null)){
				            			//sono nel caso di una unica forma di finanziamento associata al bando	
				            			logger.info("[FormaFinanziamento::inject] sono nel caso di una unica forma di finanziamento associata al bando");
				            			TipoFormaFinanziamentoVO unicaFormaFinanziamento = (TipoFormaFinanziamentoVO) formaFinanziamentoList.get(0); 
				            			unicaFormaFinanziamento.setChecked("true");
				            }else{
				            	logger.info("[FormaFinanziamento::inject] sono nel caso di errore... da sistemare ...");
				            }
				       }
				       //jira FINDOM-147 fine	
				       
		        /** Jira: 1671 Gestione cessione del credito - inizio */
	          	if( input._forma_finanziamento_cessione_credito.equals("true")) 
				{
	          		String cessioneCreditoChecked  = null;
	          		String cessioneCreditoImporto  = null;
	          		
	          		if(_formaFinanziamento!=null){
	          			cessioneCreditoChecked = _formaFinanziamento.getCessioneCreditoChecked();
	          			cessioneCreditoImporto = _formaFinanziamento.getCessioneCreditoImporto();
	          		} else {
	          			cessioneCreditoChecked = "no";
	          			cessioneCreditoImporto = "0";
	          			logger.info("cessioneCreditoImporto risulta: " + cessioneCreditoImporto);
	          		}
	          		
	          		// portare in namespace
	          		output.cessioneCreditoChecked = cessioneCreditoChecked;
	          		output.cessioneCreditoImporto = cessioneCreditoImporto;
				}
	          /** Jira: 1671 Gestione cessione del credito - fine */
			          	
			          	
          	/** : Innometro Cr2:  ... - */
          	if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
			{
          		 logger.info("[FormaFinanziamento::inject] ...");

          		 String importo_richiesto 		 = "0";
				 String percPrevista 	  		 = "0"; 
				 String limiteMaxPrevisto 		 = "0"; 
				 String limiteMinPrevisto 		 = "0";
				 String idFormaFinanziamento 	 = "";
				 String viewWarningAgevolazioni = "false";
				 
				 BigDecimal totSpeseBD = new BigDecimal(0);
				 BigDecimal percPrevistaBD = new BigDecimal(0);
				 BigDecimal percCalcolataBD = new BigDecimal(0);
				 BigDecimal limiteMaxPrevistoBD = new BigDecimal(0);
				 
				 FormaFinanziamentoVO formaFinanziamentoVO = input._formaFinanziamento;
				 
				 String importoRichiestoID1 = "0";
				 logger.info("FormaFinanziamento::inject] importoRichiestoID1: " + importoRichiestoID1); // 19
				 
				 if(formaFinanziamentoVO!=null) 
				 {
						List<TipoFormaFinanziamentoVO> frmFinanzList = (List<TipoFormaFinanziamentoVO>)Arrays.asList(formaFinanziamentoVO.getFormaFinanziamentoList());

						if(frmFinanzList!=null) { 
							for(int i=0; i<frmFinanzList.size();i++) 
							{		    	   
								TipoFormaFinanziamentoVO curFormaFin=(TipoFormaFinanziamentoVO)frmFinanzList.get(i);
								
								if(curFormaFin!=null) {
									String checked = (String) curFormaFin.getChecked();
									idFormaFinanziamento = (String) curFormaFin.getIdFormaFinanziamento();
									logger.info("FormaFinanziamento::inject] idFormaFinanziamento: " + idFormaFinanziamento); // 19
									
										if(idFormaFinanziamento.equals("1"))
										{
											if(curFormaFin.equals("true")) 
												curFormaFin.setChecked(checked);

											// validazione id1 fondo perduto
											importoRichiestoID1 = (String) curFormaFin.getImportoFormaAgevolazione();					
											logger.info("FormaFinanziamento::inject] id1: importoRichiestoID1: " + importoRichiestoID1); // 510000
											
											if(importoRichiestoID1.length()==0){
												importoRichiestoID1="0";
											}
											
											// recuperare totaleSpese per campo A
											String importoSogliaMinima = "";
											importoSogliaMinima = (String) curFormaFin.getImportoFormaAgevolazione().replace(",", ".");
											logger.info("FormaFinanziamento::inject] importoSogliaMinima: " + importoSogliaMinima);
											
											if(StringUtils.isBlank(importoSogliaMinima)) {
												importoSogliaMinima="0";
											}
											
											// recuperare limiteSogliaMin per campo C
											valoreMinimoBD = new BigDecimal(importoSogliaMinima);
											logger.info("FormaFinanziamento::inject] valoreMinimoBD: " + valoreMinimoBD);
											
											if(importoRichiestoID1!=null){
												logger.info("FormaFinanziamento::inject] id1: importoRichiestoID1: " + importoRichiestoID1); // 510000
												
												if(importoRichiestoID1.matches("^\\d+(.\\d{1,2})?$")) {
													
													/* percentuale prevista */
													percPrevista = curFormaFin.getPercPrevista().toString();
													logger.info("FormaFinanziamento::inject] percPrevista: " + percPrevista); // 100.00
													
													if(!StringUtils.isBlank(percPrevista)) {
														
														percPrevista = percPrevista.replace(',', '.');
														importoRichiestoID1 = importoRichiestoID1.replace(',', '.');
														logger.info("importoRichiestoID1: " + importoRichiestoID1);
														
														 /* limite max da db */
														 limiteMaxPrevisto = limiteMaxPrevistoDB(idBando,idFormaFinanziamento,logger);
														 logger.info("limiteMaxPrevisto: " + limiteMaxPrevisto);
														 
														 if(limiteMaxPrevisto == null){
															 limiteMaxPrevisto = "0";
														 }
														 logger.info("limiteMaxPrevisto: " + limiteMaxPrevisto);
														 
														 /* limite min da db */
														 limiteMinPrevisto = limiteMinPrevistoDB(idBando,idFormaFinanziamento,logger);
														 logger.info("limiteMinPrevisto: " + limiteMinPrevisto);
														 
														 if(limiteMinPrevisto == null){
															 limiteMinPrevisto = "0";
														 }
														 logger.info("limiteMinPrevisto: " + limiteMinPrevisto);
													 	
														if(_pianoSpese!=null)
														{ 
															 String totaleSpeseXml = "";
															 
															 try 
														    {
														       totaleSpeseXml = PianoSpeseDAO.getTotaleContributoRichiesto(info.getStatusInfo().getNumProposta());  
														       logger.info("[FormaFinanziamento::inject] totaleSpeseXml: " + totaleSpeseXml);
														    } 
															 catch (Exception ex) {
														       logger.error("[PianoSpese::modelValidate] getTotaleSpese() " + ex.getMessage());
														    }
														    	  
													    	  totaleSpeseAttuale = (String) _pianoSpese.getTotale();
													    	  logger.info("[FormaFinanziamento::inject] totaleSpeseAttuale: " + totaleSpeseAttuale);
														}
													
													    String importoSpeseTemp = "";
													    
													    importoSpeseTemp = (String) curFormaFin.getImportoFormaAgevolazione();
													    logger.info("importo ultimo salvato: " + importoSpeseTemp);
													    
													    if(StringUtils.isBlank(importoSpeseTemp)) importoSpeseTemp="0";
													    if(StringUtils.isBlank(totaleSpeseAttuale)) totaleSpeseAttuale="0";

													    totSpeseBD = new BigDecimal(totaleSpeseAttuale.replace(",", "."));
													    logger.info("totSpeseBD: " + totSpeseBD); 
													    
													    if (totSpeseBD.compareTo(new BigDecimal("0.00")) == 0) {
													    	msgWarning = msgWarning + "Attenzione! piano spese non risulta compilato."; 
								            				logger.info("[FormaFinanziamento::inject] msgWarning: " + msgWarning);
													    }
													    
													    percPrevistaBD = new BigDecimal(percPrevista.replace(",", "."));
													    percCalcolataBD = percentualeCalcolataBD(totSpeseBD, percPrevistaBD, logger);
													    logger.info("percCalcolata: " + percCalcolataBD);
													    
													    limiteMaxPrevistoBD = new BigDecimal(limiteMaxPrevisto.replace(",", "."));
													    logger.info("limiteMaxPrevistoBD: " + limiteMaxPrevistoBD);
													    
													    // calcola il minimo tra 2 valori: percCalcolataBD e limiteMaxPrevisto da DB
													    valoreMinimoBD = MetodiUtili.minimoTraDueBD(percCalcolataBD, limiteMaxPrevistoBD, logger);
														
														logger.info("[FormaFinanziamento::inject] idFormaFinanziamento: " 		+ idFormaFinanziamento); 
													    logger.info("[FormaFinanziamento::inject] importoProposto: A " 			+ totaleSpeseAttuale); 	    // A 
													    logger.info("[FormaFinanziamento::inject] contributoContoInteresse: B " + contributoContoInteresse);   // B 
													    logger.info("[FormaFinanziamento::inject] impSogliaLimiteInferiore: C " + valoreMinimoBD.toString()); // C
													    logger.info("[FormaFinanziamento::inject] impContrContoInteresse: D " 	+ impContrContoInteresse);   // D
														
														output.contributoTotaleRichiesto=totaleSpeseAttuale;						  
														output.importoSogliaLimiteInferiore=valoreMinimoBD.toString();
														// output.contributoContoInteresse=contributoContoInteresse;
														output.setViewWarningAgevolazioni(viewWarningAgevolazioni); 
														output.setCb1(cb1);
														output.setCb21(cb21);
														
													} // fine test percPrevista
												} // fine test importoRichiesto
											}
											else{
												importo_richiesto="0";
												logger.info("[FormaFinanziamento::inject] importo_richiesto:  " 	+ importo_richiesto);
											}
										}
										else
											if(idFormaFinanziamento.equals("21"))
											{
												if(!StringUtils.isBlank(checked) && checked.equals("true")) 
												{
													if(curFormaFin.equals("true")) curFormaFin.setChecked(checked);
													
													if(_pianoSpese!=null)
													{ 
														String totaleSpeseXml = "";
														try 
														{
															totaleSpeseXml = PianoSpeseDAO.getTotaleContributoRichiesto(info.getStatusInfo().getNumProposta());  
															logger.info("[FormaFinanziamento::inject] totaleSpeseXml: " + totaleSpeseXml);
														} 
														catch (Exception ex) {
															logger.error("[PianoSpese::modelValidate] getTotaleSpese() " + ex.getMessage());
														}
														
														totaleSpeseAttuale = (String) _pianoSpese.getTotale();
														logger.info("[FormaFinanziamento::inject] totaleSpeseAttuale: " + totaleSpeseAttuale);
														
														if(StringUtils.isBlank(totaleSpeseXml)) totaleSpeseXml= "0,00";
														if(StringUtils.isBlank(totaleSpeseAttuale)) totaleSpeseAttuale= "0,00";
														
														if(totaleSpeseXml.indexOf(",") == -1) totaleSpeseXml = totaleSpeseXml + ",00";
														if(totaleSpeseAttuale.indexOf(",") == -1) totaleSpeseAttuale = totaleSpeseAttuale + ",00";
														
														// se sono diversi da ultimo salvataggio setta mostra messaggio... warming
														if(!totaleSpeseXml.equals(totaleSpeseAttuale)) viewWarningAgevolazioni = "true";     
														
														List<TipoFormaFinanziamentoVO> tempFPList = new ArrayList<TipoFormaFinanziamentoVO>();
														
														tempFPList = FormaFinanziamentoDAO.getFormeFinanziamentoConImpMinMaxList(idBando, dataInvio, logger); //lista presa da tabelle DB 
														
														String idFormaFinFondoPerduto = "0";
														String percPrevFondoPerduto = "";
														
														if(tempFPList!=null && !tempFPList.isEmpty())
														{
															for (int j = 0; j < tempFPList.size(); j++) 
															{
																logger.info("id: " + tempFPList.get(j).getIdFormaFinanziamento());
																logger.info("percentuale prevista: " + tempFPList.get(j).getPercPrevista());
																
																if(tempFPList.get(j).getIdFormaFinanziamento().equals("1")){
																	idFormaFinFondoPerduto = tempFPList.get(j).getIdFormaFinanziamento();
																	percPrevFondoPerduto = tempFPList.get(j).getPercPrevista();
																	limiteMaxPrevisto = tempFPList.get(j).getImportoMaxErogabile();
																}}
														}
														
														totSpeseBD = new BigDecimal(totaleSpeseAttuale.replace(",", "."));
														logger.info("totSpeseBD: " + totSpeseBD); 
														
														percPrevistaBD = new BigDecimal(percPrevFondoPerduto.replace(",", "."));
														percCalcolataBD = percentualeCalcolataBD(totSpeseBD, percPrevistaBD, logger);
														logger.info("percCalcolata: " + percCalcolataBD); // 20000.00
														
														limiteMaxPrevistoBD = new BigDecimal(limiteMaxPrevisto.replace(",", "."));
														logger.info("limiteMaxPrevistoBD: " + limiteMaxPrevistoBD);
														
														// calcola il minimo tra 2 valori: percCalcolataBD e limiteMaxPrevisto da DB
														valoreMinimoBD = MetodiUtili.minimoTraDueBD(percCalcolataBD, limiteMaxPrevistoBD, logger);
														
														/** elaborazione contContoInteresse inizio */
														contributoContoInteresse = curFormaFin.getContributoContoInteresse();
														logger.info("[FormaFinanziamento::inject] contributoContoInteresse: B " 	+ contributoContoInteresse);
														
														if(impContrContoInteresse.length()==0) impContrContoInteresse = "";
														
														output.contributoContoInteresse=contributoContoInteresse; // contr conto interesse B 8900
														output.setViewWarningAgevolazioni(viewWarningAgevolazioni); 
														/** elaborazione contContoInteresse fine */
														
														output.contributoTotaleRichiesto=totaleSpeseAttuale;			  // A 
														output.importoSogliaLimiteInferiore=valoreMinimoBD.toString();	// C
														output.importoContributoContoInteresse=impContrContoInteresse; // D
														output.setViewWarningAgevolazioni(viewWarningAgevolazioni); 
														output.setCb1(cb1);
														output.setCb21(cb21);
													}}}}}} 
					    }
				 else{
				    	// se son qui entro la prima volta e non ci sono dati salvati di finanziamento
					 	logger.info("[FormaFinanziamento::inject] non ci sono dati salvati di finanziamento... ");

					 	String idFormaFinFondoPerduto = "0";
					 	String percPrevFondoPerduto = "";
					 	
				    	if(_pianoSpese!=null)
				    	{ 
				    		totaleSpeseAttuale = (String) _pianoSpese.getTotale();
				    		logger.info("totaleSpeseAttuale: " + totaleSpeseAttuale);
				    		
				    		if(StringUtils.isBlank(totaleSpeseAttuale)){
				 			  totaleSpeseAttuale= "0,00";
				    		}
				    		
				    		contributoContoInteresse = "";
				    		List<TipoFormaFinanziamentoVO> tempCCIList = new ArrayList<TipoFormaFinanziamentoVO>();
				    		
				    		tempCCIList = FormaFinanziamentoDAO.getFormeFinanziamentoConImpMinMaxList(idBando, dataInvio, logger); //lista presa da tabelle DB 
						    
						    if(tempCCIList!=null && !tempCCIList.isEmpty()) {
						    	for (int i = 0; i < tempCCIList.size(); i++) {

						    		if(tempCCIList.get(i).getIdFormaFinanziamento().equals("1")){
						    			idFormaFinFondoPerduto = tempCCIList.get(i).getIdFormaFinanziamento();
						    			logger.info("idFormaFinFondoPerduto: " + idFormaFinFondoPerduto);
						    			percPrevFondoPerduto = tempCCIList.get(i).getPercPrevista();
						    			limiteMaxPrevisto = tempCCIList.get(i).getImportoMaxErogabile();
					    		}}
						    }
						    
						    totSpeseBD = new BigDecimal(totaleSpeseAttuale.replace(",", "."));
						    logger.info("totSpeseBD: " + totSpeseBD); 
						    
						    percPrevistaBD = new BigDecimal(percPrevFondoPerduto.replace(",", "."));
						    percCalcolataBD = percentualeCalcolataBD(totSpeseBD, percPrevistaBD, logger);
						    logger.info("percCalcolata: " + percCalcolataBD); // 20000.00
						    
						    limiteMaxPrevistoBD = new BigDecimal(limiteMaxPrevisto.replace(",", "."));
						    logger.info("limiteMaxPrevistoBD: " + limiteMaxPrevistoBD);
						    
						    // calcola il minimo tra 2 valori: percCalcolataBD e limiteMaxPrevisto da DB
						    valoreMinimoBD = MetodiUtili.minimoTraDueBD(percCalcolataBD, limiteMaxPrevistoBD, logger);
						    logger.info("valoreMinimoBD: " + valoreMinimoBD);
						    
							output.contributoTotaleRichiesto=totaleSpeseAttuale;			  // A 
							output.contributoContoInteresse=contributoContoInteresse; 		 // B
							output.importoSogliaLimiteInferiore=valoreMinimoBD.toString();	// C
							output.importoContributoContoInteresse=impContrContoInteresse; // D
							output.setViewWarningAgevolazioni(viewWarningAgevolazioni); 
							output.setCb1(cb1);
							output.setCb21(cb21);
						}
				    	else
				    	{
				    		/** passo se non ci sono spese compilate!*/
							logger.info("[FormaFinanziamento::inject] piano spese vuoto...: "); 
							if(StringUtils.isBlank(totaleSpeseAttuale)){
				 			  totaleSpeseAttuale= "0,00";
				    		}
							logger.info("[FormaFinanziamento::inject] totaleSpeseAttuale: "+totaleSpeseAttuale); 
							logger.info("[FormaFinanziamento::inject] contributoContoInteresse: "+contributoContoInteresse); 
							logger.info("[FormaFinanziamento::inject] valoreMinimoBD: "+valoreMinimoBD); 
							logger.info("[FormaFinanziamento::inject] impContrContoInteresse: "+impContrContoInteresse); 
							output.contributoTotaleRichiesto=totaleSpeseAttuale;			  // A 
							output.contributoContoInteresse=contributoContoInteresse; 		 // B
							output.importoSogliaLimiteInferiore=valoreMinimoBD.toString();	// C
							output.importoContributoContoInteresse=impContrContoInteresse; // D
            				output.setCb1(cb1);
							output.setCb21(cb21);
						}
				    }
				 logger.debug("fine inject innometro: ");
			}
			     } //chiude test null su formaFinanziamentoList 
			}
			else if( info.getFormState().equals("IN") || info.getFormState().equals("CO") || info.getFormState().equals("NV") ){
				
				// SOLO PER R/O
				FormaFinanziamentoVO _formaFinanziamento = input._formaFinanziamento;
				
				if(_formaFinanziamento != null ){
					formaFinanziamentoList = Arrays.asList(_formaFinanziamento.getFormaFinanziamentoList());
					importoRichiesto = _formaFinanziamento.getImportoRichiesto() != null ? _formaFinanziamento.getImportoRichiesto() : "";
				}else{
					formaFinanziamentoList = null;
				}
			}
		}
			//// namespace
			
			output.setFormaFinanziamentoList(formaFinanziamentoList); // 7777,00 importoFormaAgevolazione
			output.setImportoProposto(importoProposto);   
			output.setImportoRichiesto(importoRichiesto); 
			output.setMsgWarning(msgWarning);  
			
			/** : Innometro Cr2:  e  ... - */
          	if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
			{
          		output.setCb1(cb1);
          		output.setCb21(cb21);
          		
          		if(StringUtils.isBlank(impContrContoInteresse)){
          			impContrContoInteresse= "0";
	    		}
          		output.importoContributoContoInteresse=impContrContoInteresse;
          		
          		if(StringUtils.isBlank(contributoContoInteresse)){
          			contributoContoInteresse= "";
	    		}
          		output.setImportoProposto(contributoContoInteresse);
          		
          		if(StringUtils.isBlank(importoProposto)){
          			importoProposto= "0";
	    		}
          		output.setImportoProposto(importoProposto);  
          		
          		if(StringUtils.isBlank(importoRichiesto)){
          			importoRichiesto= "0";
	    		}
          		output.setImportoRichiesto(importoRichiesto);
			}
			
			if(input._forma_finanziamento_is_checkbox_nascoste.equals("true"))
			{
				output.hd = hd;
			}
		
	        return output;
	    }
	    finally {
	      logger.debug("[FormaFinanziamento::inject] END");
	    }
	  }

	  
	private boolean verificaStringaZero(String importo, Logger logger) {
		
		BigDecimal importoDaVerificare;
		logger.info("importo stringa vale: " + importo);
		
		if(importo!=null && !importo.isEmpty() && importo.matches("^\\d+(.\\d{1,2})?$")){
			if(importo.contains(",")) importo = importo.replace(",", ".");
			
			importoDaVerificare = new BigDecimal(importo);
			logger.info("stringa vale: " + importoDaVerificare);
			if (importoDaVerificare.compareTo(BigDecimal.ZERO) == 0){
				return true;
			}
		}
		return false;
	}


	/** : Innometro Cr2:  e  ... - */
	private BigDecimal percentualeCalcolataBD(BigDecimal totSpeseBD, BigDecimal percPrevistaBD, Logger logger) {
		BigDecimal ris = new BigDecimal(0);
		ris = totSpeseBD.multiply(percPrevistaBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
		return ris;
	}

	@SuppressWarnings("unused")
	@Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  

		FinCommonInfo info = (FinCommonInfo) info1;
		
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[FormaFinanziamento::modelValidate] _formaFinanziamento  BEGIN");
			
		String idBando = info.getStatusInfo().getTemplateId()+"";
	    logger.info("[FormaFinanziamento::modelValidate] idBando: " + idBando);
	    
	    BigDecimal importoMin = new BigDecimal(0.00);
	    BigDecimal importoMax = new BigDecimal(0.00);
	    
		String ERRMSG_NUM_FORME_FIN_SELEZ = "Indicare almeno una forma di agevolazione";
		if ((input._progetto_agevolazione_una_e_una_sola_forma_fin!=null) && (input._progetto_agevolazione_una_e_una_sola_forma_fin.equals("true"))) {
			ERRMSG_NUM_FORME_FIN_SELEZ = "Indicare una sola forma di agevolazione";
		}
		
		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo è obbligatorio";
		String ERRMSG_IMPORTO_RICHIESTO_FORMATO = "- il valore deve essere numerico positivo con al massimo 2 decimali";        
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE = "- l'importo richiesto non può essere superiore al totale delle spese"; 
		String ERRMSG_IMPORTO_RICHIESTO_PERC = "- il valore non può superare la percentuale prevista sul totale delle spese"; 
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_EU = "- l'importo richiesto non può essere superiore il limite massimo per Europa: € 5000"; 
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_FEU = "- l'importo richiesto non può essere superiore il limite massimo per Fuori-Europa: € 7000"; 
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE = "- il contributo richiesto supera l'80% delle spese";
		
		/** Jira: 1671 - */
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_LIMITE_PREVISTO = "- l'importo inserito per ";
		String ERRMSG_IMPORTO_LIMITE_MIN_PREVISTO = " risulta inferiore all'importo minimo erogabile previsto dal Bando di €: ";
		String ERRMSG_IMPORTO_LIMITE_MAX_PREVISTO = " risulta superiore all'importo massimo erogabile previsto dal Bando di €: ";
		
		/** :
		 * Se supera l'80% delle spese:
		 * - Il contributo richiesto supera l'80% delle spese
		 * 
		 * Se supera il massimo importo erogabile:
		 * - il contributo richiesto non può superare € 3.200.000 
		 */
		
		/**
		 * Messaggi di errore di Cinema:
		 * ERRMSG_IMPORTO_RICHIESTO_SUPERA_IMPORTO_MAX
		 * ERRMSG_IMPORTO_RICHIESTO_INFERIORE_IMPORTO_MIN
		 */
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_IMPORTO_MAX = "- l'importo richiesto non può essere superiore al limite massimo di: € 200000.00"; 
		String ERRMSG_IMPORTO_RICHIESTO_INFERIORE_IMPORTO_MIN_DINAMICO = "- l'importo richiesto non può essere inferiore al limite minimo di: € ";
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE_DINAMICO = "- l'importo richiesto non può essere superiore al " ;
		
		/** Msg per importo richiesto > 80% del totale delle spese */
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE_TOT_SPESE = "- E' necessario verificare l'importo del contributo richiesto. Questo non può superare l'80% delle spese."; 
		
		//MB2018_11_15 String ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE = "- il contributo richiesto non può superare € 3.200.000";
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE = "- il contributo richiesto non può superare € %s";
		
		boolean erroreS4P4 = false; 
		boolean isSuperaTotaleSpesa = false;

		 FormaFinanziamentoVO _formaFinanziamento = input._formaFinanziamento; 			   
		 
	     if(_formaFinanziamento!=null) 
	     {
		        //MB2016_05_02 ini
		    	 if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("true")) 
		    	 {
				    //se il bando lo prevede, il campo "importoRichiesto" deve essere obbligatorio e numerico
				    String impRich = (String)_formaFinanziamento.getImportoRichiesto();
				    logger.info("[FormaFinanziamento::modelValidate] impRich: " + impRich);
				    
				    if(StringUtils.isNotBlank(impRich) )
				    {				
				 	  if(!impRich.matches("^\\d+(,\\d{1,2})?$"))
				 	  {
				 		  logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto deve essere numerico con al max due decimali");			
				 	      addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_FORMATO);
				 	  } else {
						//  aggiungo regola che importoRichiesto <= importoProposto (vedere dove prendere importoProposto, vedi inject.bsh)
				 	   }
				    }else{
						logger.info("FormaFinanziamento::modelValidate] _formaFinanziamento.importoRichiesto non presente o vuoto");		
						addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_CAMPO_OBBLIGATORIO);
				    }
		    	 } 
		        //MB2016_05_02 ini
				     
		     	//il numero delle forme di finanziamento selezionabili dipende dalla configurazione del bando: forme-finzto-salvate nella pagina ***
		    	List<TipoFormaFinanziamentoVO> formaFinanziamentoList = (List<TipoFormaFinanziamentoVO>)Arrays.asList(_formaFinanziamento.getFormaFinanziamentoList());
			    int numFormeFinSelezionate = 0;	
			    logger.info(numFormeFinSelezionate);

			    if(formaFinanziamentoList!=null)
			    { 
			    	for(int i=0; i<formaFinanziamentoList.size();i++)
			    	{			       
			    	   TipoFormaFinanziamentoVO curFormaFin=(TipoFormaFinanziamentoVO)formaFinanziamentoList.get(i);
					   
			    	   if(curFormaFin!=null){
						  String checked = (String) curFormaFin.getChecked();
						  if(!StringUtils.isBlank(checked) && checked.equals("true")){
							 numFormeFinSelezionate += 1;
						  }
					   } //chiude test null su curFormaFin			    			 
				    }//chiude for su formaFinanziamentoList
			    } //chiude test null su formaFinanziamentoList
			    logger.info("FormaFinanziamento::modelValidate]  verifica del numero di forme di finanziamento selezionate in base alla configurazione del bando; numFormeFinSelezionate =  "+ numFormeFinSelezionate);
	
			    if ((input._progetto_agevolazione_una_e_una_sola_forma_fin!=null) && (input._progetto_agevolazione_una_e_una_sola_forma_fin.equals("true"))) {
				    
			    	if(numFormeFinSelezionate==0 || numFormeFinSelezionate>1){
					   logger.info("FormaFinanziamento::modelValidate] nessuna o più di una forma di finanziamento selezionata, ma il bando è configurato per accettare una e una sola forma di finanziamento selezionata ");			
					   addMessage(newMessages,"_formaFinanziamento", ERRMSG_NUM_FORME_FIN_SELEZ);
				    }
			    } else {
				    
			    	if(numFormeFinSelezionate==0 ){
					   logger.warn("FormaFinanziamento::modelValidate] nessuna forma di finanziamento selezionata, ma il bando è configurato per accettare almeno una forma di finanziamento selezionata ");			
					   addMessage(newMessages,"_formaFinanziamento", ERRMSG_NUM_FORME_FIN_SELEZ);
				    }
			    }
					    
			    PianoSpeseVO _pianoSpese = input._pianoSpese;
			    
		    	 if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
				    //controllo formati importi
				    if(formaFinanziamentoList!=null)
				    { 
				    	for(int i=0; i<formaFinanziamentoList.size();i++)
				    	{		    	   
				    		TipoFormaFinanziamentoVO curFormaFin=(TipoFormaFinanziamentoVO)formaFinanziamentoList.get(i);
				    		
				    		if(curFormaFin!=null)
				    		{
							  String checked = (String) curFormaFin.getChecked();
							  String idFormaAgevolazione = (String) curFormaFin.getIdFormaFinanziamento();
							  logger.info("FormaFinanziamento::modelValidate] idFormaAgevolazione: " + idFormaAgevolazione); // 19
							  
							  // controllo che il check sia stato effettivamente selezionato
							  // se da configurazione il flag risulta essere obbligatorio (Jira 503)
							  String flagObbligatorio = (String)curFormaFin.getFlagObbligatorio();
							  logger.debug("FormaFinanziamento::modelValidate] flagObbligatorio: " + flagObbligatorio);
							  
							  if ((StringUtils.isBlank(checked) || !checked.equals("true")) && (!StringUtils.isBlank(flagObbligatorio) && "S".equalsIgnoreCase(flagObbligatorio)))
							  {
							  	addMessage(newMessages,"_formaFinanziamento_valore_testo_checkFormaFinanziamento", ERRMSG_CAMPO_OBBLIGATORIO);
							    addMessage(newMessages,"_formaFinanziamento_valore_checkFormaFinanziamento", idFormaAgevolazione);
								logger.warn("FormaFinanziamento::modelValidate]  la selezione del check è obbligatoria ");
								erroreS4P4=true;
							  } 
							  
							  if(!StringUtils.isBlank(checked) && checked.equals("true"))
							  {
								 //controllo le righe checked (quelle non checked non dovrebbero avere l'importo valorizzato perchè il decheck pulisce il campo) 
								  String importoFormaAgevolazione = "";
								  if(curFormaFin.getImportoFormaAgevolazione()==null || curFormaFin.getImportoFormaAgevolazione().length()==0){
									  // importoFormaAgevolazione = "0";
									  addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_FORMATO);
									  logger.warn("FormaFinanziamento::modelValidate]  l'importo della forma di agevolazione deve essere valorizzato e deve essere numerico con max 2 cifre decimali");
									  erroreS4P4=true;
								  }else{
									  importoFormaAgevolazione = (String) curFormaFin.getImportoFormaAgevolazione().replace(".", ",");
								  }
								 
								 logger.info("FormaFinanziamento::modelValidate] importoFormaAgevolazione: " + importoFormaAgevolazione); // 510000
								/** Innometro */
					     		if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
								{
					     			// Per Innometro vi sono piu record, e a video restituisce piu msg per lo stesso errore
					     			//break;
								}else{
									
									if(StringUtils.isBlank(importoFormaAgevolazione) || (!importoFormaAgevolazione.matches("^\\d+(,\\d{1,2})?$")))
									{
										addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_FORMATO);
										addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaAgevolazione+"");
										logger.warn("FormaFinanziamento::modelValidate]  l'importo della forma di agevolazione deve essere valorizzato e deve essere numerico con max 2 cifre decimali");
										erroreS4P4=true;
									} else {
										// controlliamo che, se per la forma di finanziamento è stata prevista una percentuale,
										// l'importo della forma di agevolazione non superi il totale delle spese * la percentuale
										
										String percPrevista = curFormaFin.getPercPrevista().toString();
										logger.info("FormaFinanziamento::modelValidate] percPrevista: " + percPrevista); // 100.00
										
										// : Nel caso di sessione in cache, per evitare che bypassi il controllo
										if(_pianoSpese == null)
										{
											// : Invio un messaggio di errore a video e blocco il salvataggio
											addMessage(newMessages,"_formaFinanziamento", ERRMSG_CAMPO_OBBLIGATORIO);
											logger.info("FormaFinanziamento::modelValidate] !ATTENZIONE! Manca piano spese: ");
										}
										
										if(!StringUtils.isBlank(percPrevista) && _pianoSpese!=null)
										{
											BigDecimal percPrevistaBD = new BigDecimal(0); // 70
											percPrevista = percPrevista.replace(',', '.');
											percPrevistaBD = new BigDecimal(percPrevista);	
											logger.info("FormaFinanziamento::modelValidate] percPrevistaBD: " + percPrevistaBD);
											
											BigDecimal importoFormaAgevolazioneBD = new BigDecimal(0);	  
											importoFormaAgevolazione = importoFormaAgevolazione.replace(',', '.');
											importoFormaAgevolazioneBD = new BigDecimal(importoFormaAgevolazione);	
											logger.info("FormaFinanziamento::modelValidate] importoFormaAgevolazioneBD: " + importoFormaAgevolazioneBD);
											
											String totaleSpese = (String)_pianoSpese.getTotale();
											logger.info("FormaFinanziamento::modelValidate] totaleSpese: " + totaleSpese);
											
											BigDecimal totaleSpeseBD = new BigDecimal(-1);
											if(!StringUtils.isBlank(totaleSpese)){     
												totaleSpese = totaleSpese.replace(',', '.');
												totaleSpeseBD = new BigDecimal(totaleSpese);
												logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
											}  
											
											BigDecimal percCalcolataBD = totaleSpeseBD.multiply(percPrevistaBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
											logger.info("FormaFinanziamento::modelValidate] percCalcolataBD: " + percCalcolataBD);
											
											if(importoFormaAgevolazioneBD.compareTo(totaleSpeseBD)==1){
												addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
												addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaAgevolazione + ""); 
												logger.info("FormaFinanziamento::modelValidate]  " +ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
												isSuperaTotaleSpesa = true;
											}
											
											if(importoFormaAgevolazioneBD.compareTo(percCalcolataBD)==1 && !isSuperaTotaleSpesa){
												addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_PERC);
												addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaAgevolazione + ""); 
												logger.info("FormaFinanziamento::modelValidate] l'importo della forma di agevolazione "
														+ "	non deve superare il " + percPrevista + " del totale delle spese");
												erroreS4P4=true;
											}    
										}
									}
								}
							  }
						   } //chiude test null su curFormaFin
						   if(erroreS4P4){					      
								break;  //mi fermo alle segnalazioni di errore della prima riga che ha errori
						   }
				    	}//chiude for su formaFinanziamentoList
				    }//chiude test null su formaFinanziamentoList
		    	 }
		    	 
			    //l'Importo dell'agevolazione pubblica richiesta' (campo digitato dall'utente o somma importi delle singole forme di agevolazione a seconda 
			    //della configurazione del bando) non deve superare il totale delle spese
					    
				if(_pianoSpese!=null)
				{
				    String totaleSpese = (String)_pianoSpese.getTotale();
				    logger.info("FormaFinanziamento::modelValidate] totaleSpese: " + totaleSpese);
				    
				    String importoRichiesto = (String)_formaFinanziamento.getImportoRichiesto();
				    logger.info("FormaFinanziamento::modelValidate] importoRichiesto: " + importoRichiesto);
							    
					 // : Jira 903 - solo per Voucher - inizio
					 // recuperare e controllare statoEstero se Europa <=5000 oppure FEU: <=7000
					if( input._progetto_agevolrichiesta_importo_europeo.equals("true"))
					{
						DescrizioneFieraVO _descrizioneFiera = input.descrizioneFiera;
							 
						logger.info("Debug: inizializzo descrizioneFiera ");
					
						String statoEstero = "";
						String siglaContinente = "";
						
						boolean isEuropa = false;
						
						// create 2 BigDecimal objects
					    BigDecimal strEU, strFEU;
					    strEU = new BigDecimal("5000.00");
					    strFEU = new BigDecimal("7000.00");
					    
					    BigDecimal totaleSpeseBD = new BigDecimal(0);
			            logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
			            
			            BigDecimal importoRichiestoBD = new BigDecimal(0);
			            logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
			   
			            if(!StringUtils.isBlank(totaleSpese)) {     
				              totaleSpese = totaleSpese.replace(',', '.');
				              totaleSpeseBD = new BigDecimal(totaleSpese);	
				              logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
			            }   
				   
			            if(!StringUtils.isBlank(importoRichiesto)){     
			               
			               importoRichiesto = importoRichiesto.replace(',', '.');
			               importoRichiestoBD = new BigDecimal(importoRichiesto);
			               logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
			            }  
							    
						try {
							if( _descrizioneFiera != null && !_descrizioneFiera.isEmpty())
							{
								statoEstero = _descrizioneFiera.getStatoEstero();
								logger.info("Debug-df3: statoEstero risulta " + statoEstero);
								
								if (statoEstero != null && !statoEstero.isEmpty()) 
					     		{
									siglaContinente = LuoghiDAO.getSiglaContinenteByCodStato(statoEstero);
									
							     	if(siglaContinente.equalsIgnoreCase("EU"))
							     	{
							     		logger.warn("[DescrizioneFiera::modelValidate] siglaContinente risulta: " + siglaContinente);
							     		isEuropa = true;
							     		logger.info("debug: " + isEuropa);
							     		
							     		if(importoRichiestoBD.compareTo(totaleSpeseBD)==1)
							     		{                 
					                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al totale delle spese");
					                		
					                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
					                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
					                		}                
					                	}
							     		
							     		if(importoRichiestoBD.compareTo(strEU)==1)
							     		{                 
					                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al limite massimo di € 5000.00");
					                		
					                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
					                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_EU); 
					                		}                
					                	}
							     	}
							     	else if(siglaContinente.equalsIgnoreCase("FEU"))
							     	{
							     		logger.warn("[DescrizioneFiera::modelValidate] siglaContinente risulta: " + siglaContinente);
							     		isEuropa = false;
							     		
							     		if(importoRichiestoBD.compareTo(totaleSpeseBD)==1)
							     		{                 
					                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al totale delle spese");
					                		
					                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
					                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
					                		}                
					                	}
							     		
							     		if(importoRichiestoBD.compareTo(strFEU)==1)
							     		{                 
					                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al limite massimo di € 7000.00");
					                		
					                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
					                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_FEU);
					                			logger.warn("FormaFinanziamento::modelValidate] "+ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_FEU);
					                		}                 
					                	}
							     	}else{
							     		logger.warn("FormaFinanziamento::modelValidate]  Nessun messaggio!");
							     	}
								}
							}else{
								logger.info("Debug: _descrizioneFiera risulta vuoto");
							}
						} catch (CommonalityException e) {
							e.printStackTrace();
						}
					}
					// : Jira 903 - solo per Voucher - fine
					
					
				/***********************************
				 * Recupero importo-max-erogabile
				 ***********************************/
				// : Jira ?!? - solo per Cinema - inizio
				// agevolazione min >= 30.000.00 e max <= 200.000.00
				if( input._progetto_agevolrichiesta_importo_attivita_produttive.equals("true"))
				{
					logger.info("Debug: inizio verifica importo per bando cinema ");
						    
				    BigDecimal totaleSpeseBD = new BigDecimal(0);
			        logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
			        
			        BigDecimal importoRichiestoBD = new BigDecimal(0);
			        logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
			   
			        if(!StringUtils.isBlank(totaleSpese))
			        {     
			          totaleSpese = totaleSpese.replace(',', '.');
			          totaleSpeseBD = new BigDecimal(totaleSpese);	
			          logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
			        }   
			   
			        if(!StringUtils.isBlank(importoRichiesto))
			        {     
			           importoRichiesto = importoRichiesto.replace(',', '.');
			           importoRichiestoBD = new BigDecimal(importoRichiesto);
			           logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
			        }  
			    	
			        // Verifica se importo_richiesto risulta > spesa totale
			        int ris1 = importoRichiestoBD.compareTo(totaleSpeseBD);
					if(ris1==1)
					{  
						logger.info("importoRichiestoBD: " + importoRichiestoBD + " > totaleSpesa che vale: " + totaleSpeseBD);
			    		logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto risulta essere maggiore al totale delle spese");
			    		
			    		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) 
			    		{
			    			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
			    			logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
			    			
			    		} else {  
			    			addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
			    			logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
			    		}  
					} else {
						logger.info("Debug: [ ok ] ... controllo IR minore della spesa totale - puoi salvare! ");
					}	
			    		idBando = info.getStatusInfo().getTemplateId()+"";
					    logger.info("[FormaFinanziamento::inject] idBando: " + idBando);
					    
						try 
						{
			            	/* Recupero importo-max-erogabile */
							String str_importo_max = FormaFinanziamentoDAO.getImportoMassimoErogabile(idBando, logger);
							importoMax = new BigDecimal(str_importo_max);
							logger.info("Debug query importoMax risulta: " + importoMax);
							
							/* Recupero importo-min-erogabile */
							String str_importo_min = FormaFinanziamentoDAO.getImportoMinimoErogabile(idBando, logger);
							importoMin = new BigDecimal(str_importo_min);
							logger.info("Debug query importoMin risulta: " + importoMin);
							
							int ris2 = importoRichiestoBD.compareTo(importoMax);
							if(ris2==1)
							{              
								logger.info("importoRichiestoBD: " + importoRichiestoBD + " > importoMax che vale: " + importoMax);
			            		logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto non deve risultare superiore all'importo massimo di € 200000.00");
			            		
			            		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) 
			            		{
			            			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_IMPORTO_MAX);
			            			logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_IMPORTO_MAX);
			            		}  
							} else {
								logger.info("Debug: [ ok ] ... controllo IR minore dell'importo max erogabile di [€ 200000.00] - puoi salvare! ");
							}	
							
			        		int ris3 = importoRichiestoBD.compareTo(importoMin);
							logger.info("ris3 vale: " + ris3);
							
							if(ris3==-1)
							{
								logger.info("importoRichiestoBD: " + importoRichiestoBD + " < importoMin che vale: " + importoMin);
			            		logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto non deve risultare inferiore all'importo minimo di € 30000.00");
			            		
			            		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
			            			
			            			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_INFERIORE_IMPORTO_MIN_DINAMICO);
			            			logger.info(ERRMSG_IMPORTO_RICHIESTO_INFERIORE_IMPORTO_MIN_DINAMICO);
			            		}  
			            		
			            	} else {
								logger.info("Debug: [ ok ] ... controllo IR maggiore dell'importo min erogabile di [€ 30000.00] - puoi salvare! ");
							}
							
						}catch (CommonalityException e) {
							e.printStackTrace();
						}
				}// fine controlli IR: importo richiesto
						
				/**
				 * Jira per importo richiedibile <= <variabilePercentuale> del totale delle spese
				 * Esempio: Bando Unesco
				 * 
				 */
				if( input._progetto_agevolrichiesta_max_importo_perc_var.equals("true"))
				{
					logger.info("Debug: inizio verifica importo per bando unesco ");
					boolean errore_1 = false;
					boolean errore_2 = false;
					
				    BigDecimal totaleSpeseBD = new BigDecimal(0);
		            logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
		            
		            BigDecimal importoRichiestoBD = new BigDecimal(0);
		            logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
		   
		            if(!StringUtils.isBlank(totaleSpese))
		            {     
		              totaleSpese = totaleSpese.replace(',', '.');
		              totaleSpeseBD = new BigDecimal(totaleSpese);	
		              logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
		            }   
		   
		            if(!StringUtils.isBlank(importoRichiesto))
		            {     
		               importoRichiesto = importoRichiesto.replace(',', '.');
		               importoRichiestoBD = new BigDecimal(importoRichiesto);
		               logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
		            }  
		            
		            // Verifica se importo_richiesto risulta > 80% della spesa totale 
		            idBando = info.getStatusInfo().getTemplateId()+"";
				    logger.info("[FormaFinanziamento::inject] idBando: " + idBando);
				    
		            logger.info("debug: Recupero % nel db: ");
					String percentualeDB = MetodiUtili.recuperaPercByIdBando(idBando, logger);
					logger.info("FormaFinanziamento::modelValidate] percentualeDB: " + percentualeDB);
					
					// converto % percentuale String to BigDecimal
					BigDecimal percentuale = new BigDecimal(percentualeDB);
					logger.info("debug percentuale: " + percentuale);
					
					// calcolo
					BigDecimal maxImportoPerc = new BigDecimal(0);
					maxImportoPerc = MetodiUtili.calcoloMaxImporto(totaleSpeseBD, percentuale);
					
					logger.info("FormaFinanziamento::modelValidate] maxImportoPerc: " + maxImportoPerc);
		    
				int ris1 = importoRichiestoBD.compareTo(totaleSpeseBD);
				if(ris1==1)
				{  
					logger.info("importoRichiestoBD: " + importoRichiestoBD + " > totaleSpesa che vale: " + totaleSpeseBD);
					logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto risulta essere maggiore al totale delle spese");
					
					if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
						errore_1 = true;
						addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE+"\n"); 
						logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
						
					} else {  
						addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
						logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
					} 
					
				} else {
						logger.info("Debug: [ ok ] ... controllo IR minore o uguale alla spesa totale - puoi salvare! ");
					}	
		
					logger.info("Debug query importoMax risulta: " + maxImportoPerc);
					
					int ris2 = importoRichiestoBD.compareTo(maxImportoPerc);
					
					if(ris2==1)
					{              
						logger.info("importoRichiestoBD: " + importoRichiestoBD + " > importoMax che vale: " + maxImportoPerc);
						logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto non deve risultare superiore all' 80% del totale delle spese!");
						
						if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
							errore_2 = true;
							
							if(errore_1)
							{
								// addMessage(newMessages,"_formaFinanziamento", "\n"+ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE_TOT_SPESE+"\n");
								logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE_TOT_SPESE);
								
							} else {
								addMessage(newMessages,"_formaFinanziamento", "\n"+ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE_TOT_SPESE+"\n");
								logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE_TOT_SPESE);
							}
						}  
						
					} else {
						logger.info("Debug: [ ok ] ... " + ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE_TOT_SPESE+ " puoi salvare!");
					}
				 }// fine controlli IR: importo richiesto <= 80%
						
						
				/** ASR */
				// agevolazione  max <= 3200000.00
				// if( input._forma_fin_importo_max_erogabile.equals("true") && (erroreS4P4 == false))
				if( input._forma_fin_importo_max_erogabile.equals("true") && !isSuperaTotaleSpesa)
				{
					logger.info("Debug: inizio verifica importo per bando asr ");
					
				    BigDecimal totaleSpeseBD = new BigDecimal(0);
		            logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
		            
		            BigDecimal importoRichiestoBD = new BigDecimal(0);
		            logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
		   
		            if(!StringUtils.isBlank(totaleSpese))
		            {     
		              totaleSpese = totaleSpese.replace(',', '.');
		              totaleSpeseBD = new BigDecimal(totaleSpese);	
		              logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
		            }   
		   
		            if(!StringUtils.isBlank(importoRichiesto))
		            {     
		               importoRichiesto = importoRichiesto.replace(',', '.');
		               importoRichiestoBD = new BigDecimal(importoRichiesto);
		               logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
		            }  
		            
		            // Verifica se importo_richiesto risulta > spesa totale
		            int ris1 = importoRichiestoBD.compareTo(totaleSpeseBD);
		            logger.info("ris1: " + ris1);
		            
					if(ris1==1)
					{  
						logger.info("importoRichiestoBD: " + importoRichiestoBD + " > totaleSpesa che vale: " + totaleSpeseBD);
		        		logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto risulta essere maggiore al totale delle spese");
		        		
		        		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) 
		        		{
		        			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
		        			logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
		        			
		        		} else {  
		        			addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
		        			logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
		        		}
		        		
					} else {
						logger.info("Debug: [ ok ] ... controllo IR minore della spesa totale - puoi salvare! ");
					}	
		    		
					idBando = info.getStatusInfo().getTemplateId()+"";
				    logger.info("[FormaFinanziamento::inject] idBando: " + idBando);
				    
					logger.info("debug: Recupero % nel db: ");
					String percentualeDB = MetodiUtili.recuperaPercByIdBando(idBando, logger);
					logger.info("FormaFinanziamento::modelValidate] percentualeDB: " + percentualeDB);
					
					BigDecimal percentuale = new BigDecimal(0);
					if(percentualeDB != null)
					{
						logger.info("debug: converto % percentuale String to BigDecimal: ");
						percentuale = new BigDecimal(percentualeDB);
						logger.info("debug percentuale: " + percentuale);
						
					} else {
						percentuale = new BigDecimal(80.00);
						logger.info("debug percentuale: " + percentuale);
					}
						
					// calcolo
					BigDecimal maxImportoPerc = new BigDecimal(0);
					maxImportoPerc = MetodiUtili.calcoloMaxImporto(totaleSpeseBD, percentuale);
					logger.info("debug maxImportoPerc: " + maxImportoPerc);
		
					// Max importo erogabile dal bando
					String max_imp_erogabileBD = MetodiUtili.recuperaImpMaxErogabileByIdBando(idBando, logger);
					logger.info("debug max_imp_erogabileBD: " + max_imp_erogabileBD);
					
					BigDecimal maxImpErogabileBD = new BigDecimal(0);
					max_imp_erogabileBD = max_imp_erogabileBD.replace(',', '.');
					maxImpErogabileBD = new BigDecimal(max_imp_erogabileBD);
					logger.info("debug maxImpErogabileBD: " + maxImpErogabileBD);
					
					int ris2 = importoRichiestoBD.compareTo(maxImportoPerc);
					logger.info("debug maxImpErogabileBD: " + ris2);
					
					if(ris2==1 && !erroreS4P4)
					{  
						logger.info("importoRichiestoBD: " + importoRichiestoBD + " > importo max erogabile che vale: " + maxImportoPerc);
						logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto risulta essere maggiore all' importo max erogabile");
						
						if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false") && !isSuperaTotaleSpesa) 
						{
							addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE); 
							logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE);
							
						} else {  
							addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE);
							logger.info("Importo richiesto supera la % di importo massimo richiedibile!");
						} 
						
					} else {
						logger.info("Debug: [ ok ] ... controllo IR minore della spesa totale - puoi salvare! ");
					}
					
					int ris3 = importoRichiestoBD.compareTo(maxImpErogabileBD);
					logger.info("debug maxImpErogabileBD: " + ris3);
					
					if(ris3==1)
					{  
						logger.info("importoRichiestoBD: " + importoRichiestoBD + " > importo max erogabile che vale: " + maxImpErogabileBD);
						logger.info("FormaFinanziamento::modelValidate]  l'importo richiesto risulta essere maggiore dell' importo max erogabile");
						
						if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) 
						{
							//MB2018_11_15 ini
							String msg = String.format(ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE,max_imp_erogabileBD.replace('.', ','));
							addMessage(newMessages,"_formaFinanziamento", msg);
							//MB2018_11_15 fine
							logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE);
							
						} else {  
							addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_PERCENTUALE);
							logger.info("Importo richiesto supera la % di importo massimo richiedibile!");
						}  
						
					} else {
						logger.info("Debug: [ ok ] ... controllo IR minore dell' importo max erogabile - puoi salvare! ");
					}
				}// fine controlli IR: importo richiesto
				/** end ASR 2018 */
				
				
				// : Jira 1418 - solo per VoucherFiere II edizione - inizio
				// recuperare e controllare statoEstero se Europa <=5000 oppure FEU: <=7000
				if( input._progetto_agevolrichiesta_importo_europeo_voucher_sec_ed.equals("true"))
				{
					DescrizioneFieraSecEdVO _descrizioneFieraSecEd = input._descrizioneFieraSecEd;
						 
					logger.info("Debug: inizializzo descrizioneFiera ");
					
					String statoEsteroVoucherSecED = "";
					String siglaContinenteVoucherSecEd = "";
					
					boolean isEuropaVoucherSecEd = false;
					boolean errore1 = false;
					
					// create 2 BigDecimal objects
				    BigDecimal strVoucherSecEdEU, strVoucherSecEdFEU;
				    strVoucherSecEdEU = new BigDecimal("5000.00");
				    strVoucherSecEdFEU = new BigDecimal("7000.00");
				    
				    BigDecimal totaleSpeseVoucherSecEdBD = new BigDecimal(0);
		            logger.info("FormaFinanziamento::modelValidate] totaleSpeseVoucherSecEdBD: " + totaleSpeseVoucherSecEdBD);
		           
		            BigDecimal importoRichiestoVoucherSecEdBD = new BigDecimal(0);
		            logger.info("FormaFinanziamento::modelValidate] importoRichiestoVoucherSecEdBD: " + importoRichiestoVoucherSecEdBD);
		  
		           if(!StringUtils.isBlank(totaleSpese))
		           {     
		              totaleSpese = totaleSpese.replace(',', '.');
		              totaleSpeseVoucherSecEdBD = new BigDecimal(totaleSpese);	
		              logger.info("FormaFinanziamento::modelValidate] totaleSpeseVoucherSecEdBD: " + totaleSpeseVoucherSecEdBD);
		           }   
			   
		           if(!StringUtils.isBlank(importoRichiesto))
		           {     
		              importoRichiesto = importoRichiesto.replace(',', '.');
		              importoRichiestoVoucherSecEdBD = new BigDecimal(importoRichiesto);
		              logger.info("FormaFinanziamento::modelValidate] importoRichiestoVoucherSecEdBD: " + importoRichiestoVoucherSecEdBD);
		           }  
						    
					try {
						if( _descrizioneFieraSecEd != null && !_descrizioneFieraSecEd.isEmpty())
						{ 
							statoEsteroVoucherSecED = _descrizioneFieraSecEd.getStatoEstero();
							logger.info("Debug-df3: statoEsteroVoucherSecED risulta " + statoEsteroVoucherSecED);
							
							if (statoEsteroVoucherSecED != null && !statoEsteroVoucherSecED.isEmpty()) 
				     		{
								siglaContinenteVoucherSecEd = LuoghiDAO.getSiglaContinenteByCodStato(statoEsteroVoucherSecED);
								
						     	if(siglaContinenteVoucherSecEd.equalsIgnoreCase("EU"))
						     	{
						     		logger.warn("[DescrizioneFiera::modelValidate] siglaContinenteVoucherSecEd risulta: " + siglaContinenteVoucherSecEd);
						     		isEuropaVoucherSecEd = true;
						     		logger.warn("FormaFinanziamento::modelValidate]  isEuropaVoucherSecEd :" + isEuropaVoucherSecEd);
						     		
						     		if(importoRichiestoVoucherSecEdBD.compareTo(totaleSpeseVoucherSecEdBD)==1)
						     		{                 
				                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al totale delle spese");
				                		
				                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) 
				                		{
				                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
				                		}                
				                	}
						     		
						     		if(importoRichiestoVoucherSecEdBD.compareTo(strVoucherSecEdEU)==1) 
						     		{                 
				                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al limite massimo di € 5000.00");
				                		
				                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) {
				                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_EU); 
				                		}                
				                	}
						     	}
						     	else if(siglaContinenteVoucherSecEd.equalsIgnoreCase("FEU"))
						     	{
						     		logger.warn("[DescrizioneFieraSecEd::modelValidate] siglaContinenteVoucherSecEd risulta: " + siglaContinenteVoucherSecEd);
						     		isEuropaVoucherSecEd = false;
						     		
						     		if(importoRichiestoVoucherSecEdBD.compareTo(totaleSpeseVoucherSecEdBD)==1)
						     		{                 
				                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al totale delle spese");
				                		
				                		if ((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false")) 
				                		{
				                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
				                			errore1=true;
				                		}                
				                	}
						     		
						     		if(importoRichiestoVoucherSecEdBD.compareTo(strVoucherSecEdFEU)==1)
						     		{                 
				                		logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non può essere superiore al limite massimo di € 7000.00");
				                		
				                		if (((input._progetto_agevolrichiesta_importo_unico!=null) && input._progetto_agevolrichiesta_importo_unico.equals("false") ) && !errore1) 
				                		{
				                			addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_FEU);
				                			logger.warn("FormaFinanziamento::modelValidate] "+ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE_FEU);
				                		}                 
				                	}
						     		
						     	} else {
						     		logger.warn("FormaFinanziamento::modelValidate]  Nessun messaggio!");
						     	}
							}
							
						} else {
							logger.info("Debug: _descrizioneFiera risulta vuoto");
						}
						
					} catch (CommonalityException e) {
						e.printStackTrace();
					}
				}
				// : Jira 1418 - solo per VoucherFiere II edizione - fine
					
					
				/** ------------------------------------------------ : Jira: 1537:  
				 * 	Bando Voucher IR 
				 * 	Emp.Contributo 
				 * 	Unesco_20 
				 *  Amianto
				 *  Editoria2020
				 **/
				if( input._progetto_forme_finanziamento_custom.equals("true")) // presente var-cfg ?
				{
					logger.info("Debug: inizio verifica importo per bando voucherIR e/o Unesco ***");
					
					/* Recupero importi */
					logger.debug("Debug: Recuperare importo minimo da db ");
					
					String str_importo_min = null;
					String str_importo_max = null;
					String str_percentualeDB = null;
					
					/**
					 * Eseguo controllo come segue:
					 * 
					 * importo richiesto >= importo_minimo_erogabile  							( 1 or 0)
					 * importo richiesto <= importo_massimo_erogabile 							(-1 or 0)
					 * importo richiesto <= (perc_contributo_massimo_erogabile * totale_spese) 	(-1 or 0)
					 */
					boolean isImportoRicMinoreImpMaxErogabile 	= false;
					boolean isImportoRicMaggioreImpMinErogabile = false;
					boolean isPercContributoMassimoErogabile 	= false;
					
					BigDecimal totaleSpeseBD 		= new BigDecimal(0);
					BigDecimal maxImportoPerc 		= new BigDecimal(0);
					BigDecimal importoRichiestoBD 	= new BigDecimal(0);
					BigDecimal bd_percentuale 		= new BigDecimal(0);
					
					int check1 = 01;
					int check2 = 02;
					int check3 = 03;
					
		            if(!StringUtils.isBlank(totaleSpese))
		            {     
			          totaleSpese = totaleSpese.replace(',', '.');
		              totaleSpeseBD = new BigDecimal(totaleSpese);	
		              logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
		            }
		            
		            if(!StringUtils.isBlank(importoRichiesto))
		            {     
		               importoRichiesto = importoRichiesto.replace(',', '.');
		               importoRichiestoBD = new BigDecimal(importoRichiesto);
		               logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
		            }
		            
					try {
						
						int check0 = 0;
						check0 = verificaImportoRichiesto(importoRichiestoBD, totaleSpeseBD, logger); // -1
						
						if (check0 == -1 || check0 == 0) 
						{
							// importo-min da db
							str_importo_min = FormaFinanziamentoDAO.getImportoMinimoErogabile(idBando, logger);
							logger.info("[FormaFinanziamento::modelValidate] str_importoMin risulta: "	+ str_importo_min);
							
							if (str_importo_min != null) 
							{
								importoMin = new BigDecimal(str_importo_min);
								logger.info("[FormaFinanziamento::modelValidate] bd_importoMin risulta: " + importoMin);
		
								// check1: importo richiesto >= importo_minimo_erogabile: (0 or 1) ok
								check1 = verificaImportoRichiesto(importoRichiestoBD, importoMin, logger); // -1 errore
		
								if (check1 == 1 || check1 == 0) 
								{
									logger.info("(importoRichiesto) " + importoRichiestoBD + " < (importoMin): " + importoMin + " : " + isImportoRicMinoreImpMaxErogabile);
									isImportoRicMaggioreImpMinErogabile = true;
		
								} else {
									logger.info(ERRMSG_IMPORTO_RICHIESTO_INFERIORE_IMPORTO_MIN_DINAMICO + ": " + str_importo_min);
								}
								// fine verifica: importo richiesto >= importo_minimo_erogabile ****
		
							} else {
								logger.info(" Nessun controllo, manca importoMin su database!");
							}
							
							// importo-max da db
							str_importo_max = FormaFinanziamentoDAO.getImportoMassimoErogabile(idBando, logger);
							logger.info("[FormaFinanziamento::modelValidate] str_importo_max risulta: " + str_importo_max);
							
							if (str_importo_max != null) 
							{
								str_importo_max = str_importo_max.replace(",", ".");
								logger.info("[FormaFinanziamento::modelValidate] str_importo_max risulta: " + str_importo_max);

								importoMax = new BigDecimal(str_importo_max);
								logger.info("[FormaFinanziamento::modelValidate] importoMax risulta: " + importoMax);
		
								// Esegui verifica: importo richiesto >= importo_massimo_erogabile * inizio ***
								check2 = verificaImportoRichiesto(importoMax, importoRichiestoBD, logger); // -1 ( a < b )
		
								if (check2 == 1 || check2 == 0) 
								{
									logger.info("(importoRichiesto) " + importoRichiestoBD + " < (importoMax): " + importoMax + " : " + isImportoRicMaggioreImpMinErogabile);
									isImportoRicMinoreImpMaxErogabile = true;
									
								} else {
									logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE_DINAMICO + " " + importoMax);
								}
								// Esegui verifica: importo richiesto >= importo_massimo_erogabile * fine ***
		
							} else {
								logger.debug(" Nessun controllo, manca importoMax su database!");
							}
							
							// percentuale da db
							str_percentualeDB = MetodiUtili.recuperaPercByIdBando(idBando, logger);
							logger.info("[FormaFinanziamento::modelValidate] str_percentualeDB: " + str_percentualeDB);
							
							if (str_percentualeDB != null) 
							{
								bd_percentuale = new BigDecimal(str_percentualeDB);
								logger.info("[FormaFinanziamento::modelValidate] bd_percentuale: " + bd_percentuale);
		
								// calcolo percentuale max-importo
								maxImportoPerc = MetodiUtili.calcoloMaxImporto(totaleSpeseBD, bd_percentuale);
								logger.info("debug: maxImportoPerc: " + maxImportoPerc);
		
								// Esegui verifica: importo richiesto <= (perc_contributo_massimo_erogabile * totale_spese) * inizio ***
								check3 = verificaImportoRichiesto(importoRichiestoBD, maxImportoPerc, logger);
		
								if (check3 == -1 || check3 == 0) 
								{
									logger.info("(importoRichiesto) " + importoRichiestoBD + " < (perc_contributo_massimo_erogabile): " + str_percentualeDB + " : " + isPercContributoMassimoErogabile);
									isPercContributoMassimoErogabile = true;
		
								} else {
									logger.info(ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE_DINAMICO + " " + maxImportoPerc);
								}
								// Esegui verifica: importo richiesto <= (perc_contributo_massimo_erogabile * totale_spese) *   fine ***
		
							} else {
								logger.info(" Nessun controllo, manca percentualeDB su database!");
							}
							
							// Gst errori
							if (!isImportoRicMaggioreImpMinErogabile && str_importo_min != null) 
							{
								addMessage(newMessages, "_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_INFERIORE_IMPORTO_MIN_DINAMICO + " " + importoMin);
		
							} else if (!isImportoRicMinoreImpMaxErogabile && str_importo_max != null) {
								addMessage(newMessages, "_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE_DINAMICO + " " + importoMax);
		
							} else if (!isPercContributoMassimoErogabile && str_percentualeDB != null) {
								addMessage(newMessages, "_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_MAX_IMPORTO_EROGABILE_DINAMICO + " " + str_percentualeDB+ "% prevista sul totale delle spese pari ad € "+ maxImportoPerc);
							} else {
								logger.info("Nessun errore!");
							}
						
						} else {
							logger.debug("Errore, importo richiesto supera le spese!!");
							addMessage(newMessages, "_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
						}
						 
					} catch (CommonalityException e1) {
						e1.printStackTrace();
					}
					
					logger.info("Debug: [ end check voucherIR ] ");
				}// Jira: 1537:  Bando Voucher IR - fine
				
				
			/** ---------------------------------------------------------------------------- Jira 1671 -  */
			if( input._progetto_forme_finanziamento_imp_min_max_by_bando.equals("true"))
			{
				 logger.info("Calcolo agevolazione garanzia o contributo condomini");
				
				 String idFormaFinanziamento = "";
				 
				 logger.info("\n\n********************************************************************* importo richiesto ");
				 String importo_richiesto = "0";
				 String percPrevista = "0"; 
				 String limiteMaxPrevisto = "0"; 
				 String limiteMinPrevisto = "0";

				 String cessioneCreditoChecked = "";
				 String cessioneCreditoImporto = "";
				 
				 boolean isLimiteImportoMin = false;
				 boolean isLimiteImportoMax = false;
				 boolean isTotaleSpeseCorretto = false;
				 
				 FormaFinanziamentoVO formaFinanziamentoVO = input._formaFinanziamento;
				 
				 if(formaFinanziamentoVO!=null) {
						List<TipoFormaFinanziamentoVO> frmFinanzList = (List<TipoFormaFinanziamentoVO>)Arrays.asList(formaFinanziamentoVO.getFormaFinanziamentoList());

						if(frmFinanzList!=null) { 
							
							for(int i=0; i<frmFinanzList.size();i++) {		    	   
								TipoFormaFinanziamentoVO curFormaFin=(TipoFormaFinanziamentoVO)frmFinanzList.get(i);
								
								if(curFormaFin!=null) {
									String checked = (String) curFormaFin.getChecked();
									idFormaFinanziamento = (String) curFormaFin.getIdFormaFinanziamento();
									logger.info("FormaFinanziamento::modelValidate] idFormaFinanziamento: " + idFormaFinanziamento); // 19
									
									if(!StringUtils.isBlank(checked) && checked.equals("true")) {
										importo_richiesto = (String) curFormaFin.getImportoFormaAgevolazione();						
										logger.info("FormaFinanziamento::modelValidate] importo_richiesto: " + importo_richiesto); // 510000
										
										if(importo_richiesto.matches("^\\d+(,\\d{1,2})?$")) {
											percPrevista = curFormaFin.getPercPrevista().toString();
											logger.info("FormaFinanziamento::modelValidate] percPrevista: " + percPrevista); // 100.00
											
											if(!StringUtils.isBlank(percPrevista)) {
												percPrevista = percPrevista.replace(',', '.');
												importo_richiesto = importo_richiesto.replace(',', '.');
												
												 /* limite max */
												 limiteMaxPrevisto = limiteMaxPrevistoDB(idBando,
														idFormaFinanziamento,
														logger);
												 logger.info("limiteMaxPrevisto: " + limiteMaxPrevisto);
												 
												 if(limiteMaxPrevisto == null){
													 limiteMaxPrevisto = "0";
												 }
												 logger.info("limiteMaxPrevisto: " + limiteMaxPrevisto);
												 
												 /* limite min */
												 limiteMinPrevisto = limiteMinPrevistoDB(idBando,
														idFormaFinanziamento,
														logger);
												 logger.info("limiteMinPrevisto: " + limiteMinPrevisto);
												 
												 if(limiteMinPrevisto == null){
													 limiteMinPrevisto = "0";
												 }
												 logger.info("limiteMinPrevisto: " + limiteMinPrevisto);
												
												isLimiteImportoMin = MetodiUtili.isLimiteMinCorretto( importo_richiesto, limiteMinPrevisto, logger);
												logger.info("isLimiteImportoMin: " + isLimiteImportoMin);
												
												if (!isLimiteImportoMin) {								
													addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_SUPERA_LIMITE_PREVISTO + frmFinanzList.get(i).getDescrFormaFinanziamento() + ERRMSG_IMPORTO_LIMITE_MIN_PREVISTO + limiteMinPrevisto ); 
													addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
													break;
												}		

												isLimiteImportoMax = MetodiUtili.isLimiteMaxCorretto( importo_richiesto, limiteMaxPrevisto, logger);
												logger.info("isLimiteImportoMax: " + isLimiteImportoMax);
												
												if (!isLimiteImportoMax) {								
													addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_SUPERA_LIMITE_PREVISTO + frmFinanzList.get(i).getDescrFormaFinanziamento() + ERRMSG_IMPORTO_LIMITE_MAX_PREVISTO + limiteMaxPrevisto); 
													addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
													break;
												}
												
												/** Gestione cessione del credito - inizio */
												if( input._forma_finanziamento_cessione_credito.equals("true")) 
												{
													cessioneCreditoChecked = formaFinanziamentoVO.getCessioneCreditoChecked();
													logger.info("debug checkBox cessione importo risulta selezionata ?" + cessioneCreditoChecked);
													
													if(!StringUtils.isBlank(cessioneCreditoChecked) && cessioneCreditoChecked.equals("si")) {
														cessioneCreditoImporto = formaFinanziamentoVO.getCessioneCreditoImporto();
														logger.info("debug cessione credito importo risulta essere di: " + cessioneCreditoImporto);
														
														logger.info("debug validazione importo con cessione credito selezionato...");
														
														if(!StringUtils.isBlank(cessioneCreditoImporto) && cessioneCreditoImporto.matches("^\\d+(,\\d{1,2})?$")) {
															if(idFormaFinanziamento.equals("19")) {
																if( cessioneCreditoChecked != null && cessioneCreditoChecked.equals("si") ) {
																	logger.info("selezionato importo cessione di credito...");

																	if(cessioneCreditoImporto != null) {
																		// verifica se importoCessioneCredito
																		isTotaleSpeseCorretto = MetodiUtili.checkTotaleSpese( totaleSpese, cessioneCreditoImporto, importo_richiesto, logger);
																		if(isTotaleSpeseCorretto) {
																			logger.info("OK ... risulta corretto : ");
																		} else {
																			logger.info("ATTENZIONE ... Non corretto... ");
																			addMessage(newMessages, "_formaFinanziamento_cessioneCredito", "L\'importo della cessione del credito e della garanzia richiesta supera il totale delle spese");
																			break;
																		}
																		
																	} else {
																		logger.info("errore: manca importo cessione credito...");
																	}
																}
																
															} else {
																logger.info("prosegui, ifFormaFinanziamento diverso da Garanzia contributo...");
															}
														}
														else if(!cessioneCreditoImporto.matches("^\\d+(,\\d{1,2})?$")) {
															logger.info("ATTENZIONE ... 'ERRORE' - ckb selezionata ma campo vuoto... ");
															addMessage(newMessages, "_formaFinanziamento_cessioneCredito", "L\'importo cessione del credito non risulta nel formato corretto.");
															break;
														}
														else if(cessioneCreditoImporto.matches ("[a-zA-Z]+\\.?")) {
															logger.info("ATTENZIONE ... 'ERRORE' - ckb selezionata ma campo vuoto... ");
															addMessage(newMessages, "_formaFinanziamento_cessioneCredito", "L\'importo cessione del credito non deve contenere caratteri alfanumerici.");
															break;
														}
														else{
															logger.info("ATTENZIONE ... 'ERRORE' - ckb selezionata ma campo vuoto... ");
															addMessage(newMessages, "_formaFinanziamento_cessioneCredito", "L\'importo della cessione del credito se selezionato, risulta obbligatorio.");
															break;
														}

													} else {
														logger.info("- ckb NON risulta selezionata!!! ");
													}
													
												} // fine gestione del credito
												else{
													logger.info("Bando configurato senza gestione cessione credito !");
												}
											}
										}
										else{
											// gestione campo vuoto
											if(idFormaFinanziamento.equals("19")){
												logger.info("ATTENZIONE ... 'ERRORE' - ckb selezionata ma campo vuoto... ");
												addMessage(newMessages,"_formaFinanziamento_valore_testo", "L\'importo della garanzia richiesta se selezionato, risulta obbligatorio."); 
												addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
												break;
											}else
												if(idFormaFinanziamento.equals("20")){
												logger.info("ATTENZIONE ... 'ERRORE' - ckb selezionata ma campo vuoto... ");
												addMessage(newMessages,"_formaFinanziamento_valore_testo", "L\'importo del Contributo richiesto se selezionato, risulta obbligatorio."); 
												addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
												break;
											}
										}
									}
								}
							}
						}
					}
				 logger.debug("fine ciclo dati recuperati in forma di fin.to condomini by idBando: 79  ");
				 
			 }
			/** Jira: 1671 - fine */
			
			
			/** : Innometro Cr2:  e  ... - */
          	if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
			{
          		logger.info("[FormaFinanziamento::modelValidate] ...");
          		logger.info("\n\n********************************************************************* dati ");

           		 String idFormaFinanziamento 	= "";
				 String importo_richiesto 		= "0";
				 String percPrevista 	  		= "0"; 
				 String limiteMaxPrevisto 		= "0"; 
				 String limiteMinPrevisto 		= "0";
				 String totSpese 				= "0";
				 
				 boolean isLimiteImportoMin = false;
				 boolean isLimiteImportoMax = false;
				 
				 BigDecimal totSpeseBD = new BigDecimal(0);
				 
				 FormaFinanziamentoVO formaFinanziamentoVO = input._formaFinanziamento;
				 if(formaFinanziamentoVO!=null) 
				 {
					List<TipoFormaFinanziamentoVO> frmFinanzList = (List<TipoFormaFinanziamentoVO>)Arrays.asList(formaFinanziamentoVO.getFormaFinanziamentoList());// dati da validare
					logger.debug("[FormaFinanziamento::modelValidate] ...");
					if(frmFinanzList!=null) { 
						
						for(int i=0; i<frmFinanzList.size();i++){		    	   
							
							TipoFormaFinanziamentoVO curFormaFin=(TipoFormaFinanziamentoVO)frmFinanzList.get(i);
							
							if(curFormaFin!=null) {
								String checked = (String) curFormaFin.getChecked();
								idFormaFinanziamento = (String) curFormaFin.getIdFormaFinanziamento();
								logger.info("FormaFinanziamento::modelValidate] idFormaFinanziamento: " + idFormaFinanziamento); // 19
								
								if(!StringUtils.isBlank(checked) 
										&& checked.equals("true")) 
								{
									if(idFormaFinanziamento != null 
											&& idFormaFinanziamento.equals("21")){
												importo_richiesto = (String) curFormaFin.getContributoContoInteresse();	
												logger.info("FormaFinanziamento::modelValidate] importo_richiesto: " + importo_richiesto); // ?
												if(StringUtils.isBlank(importo_richiesto))
											    {
													importo_richiesto="";
											    }
												else 
													if (!importo_richiesto.matches("^\\d+(,\\d{1,2})?$")){
														addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_FORMATO ); 
														addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
														break;
												}
												
												else{
											    	if(importo_richiesto.contains(",")){
											    		importo_richiesto = importo_richiesto.replace(",", ".");
											    		logger.info("FormaFinanziamento::modelValidate] importo_richiesto: " + importo_richiesto);
											    	}
											    }
												logger.info("FormaFinanziamento::modelValidate] id1: importo_richiesto: " + importo_richiesto); // 510000
									}else{
										importo_richiesto = (String) curFormaFin.getImportoFormaAgevolazione();						
										logger.info("FormaFinanziamento::modelValidate] id1: importo_richiesto: " + importo_richiesto); // 510000
									}
									
									if(importo_richiesto!=null 
											&& !importo_richiesto.equals("0")){
										if(importo_richiesto.matches("^\\d+(.\\d{1,2})?$")){
											
											/* percentuale prevista */
											percPrevista = curFormaFin.getPercPrevista().toString();
											logger.info("FormaFinanziamento::modelValidate] percPrevista: " + percPrevista); // 100.00
											
											if(!StringUtils.isBlank(percPrevista)) {
												percPrevista = percPrevista.replace(',', '.');
												logger.debug("percPrevista str: " + percPrevista);
												
												importo_richiesto = importo_richiesto.replace(',', '.');
												logger.info("importo_richiesto str: " + importo_richiesto);
												
												 /* limite max da db */
												 limiteMaxPrevisto = limiteMaxPrevistoDB(idBando,idFormaFinanziamento,logger);
												 
												 if(limiteMaxPrevisto == null) limiteMaxPrevisto = "0";
												 logger.info("limiteMaxPrevisto: " + limiteMaxPrevisto);
												 
												 /* limite min da db */
												 limiteMinPrevisto = limiteMinPrevistoDB(idBando,idFormaFinanziamento,logger);
												 logger.debug("limiteMinPrevisto da db: " + limiteMinPrevisto);
												 
												 if(limiteMinPrevisto == null) limiteMinPrevisto = "0";
												 logger.info("limiteMinPrevisto: " + limiteMinPrevisto);
												
												if(_pianoSpese!=null){ 
													totSpese = DecimalFormat.decimalFormat(_pianoSpese.getTotale(),2);
												    logger.info("[FormaFinanziamento::modelValidate] totSpese: " + totSpese); // 100000,00 - inno
												}
												
												if(totSpese.equals("0")){
													totSpeseBD = new BigDecimal(0);
												}else{
													if(totSpese.contains(",")){
														totSpeseBD = new BigDecimal(totSpese.replace(",", "."));
													}
												}
												logger.info("totSpeseBD: " + totSpeseBD);
												
												if(idFormaFinanziamento.equals("21"))
												{
													logger.info("limiteMinPrevisto: " + limiteMinPrevisto);
													logger.info("limiteMaxPrevisto: "  + limiteMaxPrevisto);
													
													// String contributoContoInteresse = (String) curFormaFin.getContributoContoInteresse(),2);
													String contributoContoInteresse = (String) importo_richiesto;
													logger.info("contributoContoInteresse: " + contributoContoInteresse);
													
													if(StringUtils.isNotBlank(contributoContoInteresse))
												    {				
												 	  if(!contributoContoInteresse.matches("^\\d+(.\\d{1,2})?$"))
												 	  {
												 		  logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto deve essere numerico con al max due decimali");			
												 	      addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_FORMATO);
												 	      addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
												 	      break;
												 	  }
												 	  else if(contributoContoInteresse.equals("0") || contributoContoInteresse.equals("0,00"))
												 	  {
												 		  logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto deve essere numerico con al max due decimali");			
												 		    addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_LIMITE_MIN_PREVISTO + limiteMinPrevisto ); 
															addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
												 	      break;
												 	  }
												 	  else{
												 		 if(contributoContoInteresse.contains(",")){
															contributoContoInteresse = contributoContoInteresse.replace(',', '.');
														 }
												 	  }
												    }else{
														logger.info("FormaFinanziamento::modelValidate] _formaFinanziamento.importoRichiesto non presente o vuoto ma non obbligatorio!");		
												    }
													
													// verifica se importo >= del limite max consentito da specifiche bando
													logger.info("contributoContoInteresse " + contributoContoInteresse+" <= "+limiteMaxPrevisto+" ?");
													isLimiteImportoMin = verificaLimMinUguale(limiteMaxPrevisto, contributoContoInteresse, logger);
													logger.info("isLimiteImportoMin: " + isLimiteImportoMin); 
													
													// verifica se importo <= del limite min consentito da specifiche bando
													logger.info("contributoContoInteresse " + contributoContoInteresse+" >= "+limiteMinPrevisto+" ?");
													isLimiteImportoMax =  verificaLimMaxUguale(limiteMinPrevisto, contributoContoInteresse, logger);
													logger.info("isLimiteImportoMax: " + isLimiteImportoMax);
													
													if (!isLimiteImportoMin) {	
														addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_SUPERA_LIMITE_PREVISTO + frmFinanzList.get(i).getDescrFormaFinanziamento() + ERRMSG_IMPORTO_LIMITE_MAX_PREVISTO + limiteMaxPrevisto ); 
														addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
														break;
													}		
													
													if (!isLimiteImportoMax) {								
														addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_SUPERA_LIMITE_PREVISTO + frmFinanzList.get(i).getDescrFormaFinanziamento() + ERRMSG_IMPORTO_LIMITE_MIN_PREVISTO + limiteMinPrevisto); 
														addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
														break;
													}}} // fine test percPrevista
										} // fine test importoRichiesto
										else{
											logger.debug("errore 2");
											addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_IMPORTO_RICHIESTO_FORMATO ); 
											addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
											break;
										}
									}
									else{
										logger.info("importo contributo conto  interesse non valorizzato...");
										addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_CAMPO_OBBLIGATORIO); 
										addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
										break;
									}
								}
							}
						}
							// : forse non serve da cancellare post test utente...
						   if(_pianoSpese != null)
					     	{
					     		String pianoSpese = _pianoSpese.getTotale();
					     		if(pianoSpese.contains(",")){
					     			pianoSpese = pianoSpese.replace(',', '.');
					     			logger.debug("pianoSpese risulta: " + pianoSpese);
								 }
					     		BigDecimal pianoSpeseTmpBD = new BigDecimal(pianoSpese);
					     		// verifico se '0'
					     		if (pianoSpeseTmpBD.compareTo(BigDecimal.ZERO) == 0){
					     			
						     		// verifico numero forme fin.to selezionate
									if(numFormeFinSelezionate==0){
									   logger.info("FormaFinanziamento::modelValidate] nessuna o più di una forma di finanziamento selezionata, "
									   		+ "ma il bando è configurato per accettare una e una sola forma di finanziamento selezionata ");
									}  
					     		}
					     		if(numFormeFinSelezionate==0){
								   logger.info("FormaFinanziamento::modelValidate] nessuna o più di una forma di finanziamento selezionata, "
								   		+ "ma il bando è configurato per accettare una e una sola forma di finanziamento selezionata ");
								}
					     	}
					 }
				   }
			 else{
				 addMessage(newMessages,"_formaFinanziamento_valore_testo", ERRMSG_CAMPO_OBBLIGATORIO); 
				 addMessage(newMessages,"_formaFinanziamento_valore_idFormaFinanziamento", idFormaFinanziamento+"");
			 }
  				logger.info("fine elaborazione modelValidate dati Innometro");
			/** : Innometro Cr2:  e  ... - */
		}
          	
			/***********************
			 * Verifica stdandard  *	
			 **********************/
          	
          	 /** : Innometro Cr2:  e  ... - */
			 if( input._progetto_forme_finanziamento_imp_min_max_by_bando.equals("false")){
				if( input._progetto_forme_finanziamento_custom.equals("false"))	{	
					if( input._progetto_agevolrichiesta_importo_attivita_produttive.equals("false")){
						if( input._progetto_agevolrichiesta_importo_attivita_produttive.equals("false")){
							if( input._progetto_agevolrichiesta_importo_europeo.equals("false")){
								if( input._progetto_agevolrichiesta_max_importo_perc_var.equals("false")){
									if( input._forma_fin_importo_max_erogabile.equals("false")){
										if( input._progetto_agevolrichiesta_importo_europeo_voucher_sec_ed.equals("false"))
										{
											BigDecimal totaleSpeseBD = new BigDecimal(0);
											logger.debug("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
											
											BigDecimal importoRichiestoBD = new BigDecimal(0);
											logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
											
											if(!StringUtils.isBlank(totaleSpese))
											{     
												totaleSpese = totaleSpese.replace(',', '.');
												totaleSpeseBD = new BigDecimal(totaleSpese);	
												logger.info("FormaFinanziamento::modelValidate] totaleSpeseBD: " + totaleSpeseBD);
											}   
										
											if(!StringUtils.isBlank(importoRichiesto)){     
												importoRichiesto = importoRichiesto.replace(',', '.');
												importoRichiestoBD = new BigDecimal(importoRichiesto);
												logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
											}  
										
											if(importoRichiestoBD.compareTo(totaleSpeseBD)==1){                 
												logger.warn("FormaFinanziamento::modelValidate]  l'importo richiesto non deve essere superiore al totale delle spese");
												
												if ((input._progetto_agevolrichiesta_importo_unico!=null) 
														&& input._progetto_agevolrichiesta_importo_unico.equals("false")) {
													
													addMessage(newMessages,"_formaFinanziamento", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE); 
												} 
												else {  
													addMessage(newMessages,"_formaFinanziamento_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_SPESE);
												}                 
										}}}}}}}}}
		}
		/** : Innometro Cr2:  e  ... - */
	     if( input._formaFinanziamento_ImpMinMaxPercByDb.equals("true")) 
		{
	    	if(_pianoSpese == null)
	     	{
	     		// : Invio un messaggio di errore a video e blocco il salvataggio
	     		addMessage(newMessages,"_formaFinanziamento_totaleSpese", ERRMSG_CAMPO_OBBLIGATORIO);
	     		logger.info("FormaFinanziamento::modelValidate] !ATTENZIONE! Manca piano spese: ");
	     	}else{
	     		logger.info(" da gestire ?");
	     		
	     	}
	    	
		}
			
			//metodo generico che tramite reflection chiama eventuali ulteriori metodi di validazione, 
			//i cui nomi e argomenti sono contenuti nella var di configurazione validationMethodsFormaFinanziamento
			logger.debug("[FormaFinanziamento::modelValidate()] inizio validazioni definite da variabile di configurazione");
			ArrayList<SegnalazioneErrore> segnalazioneList = ValidationUtil.validate(FormaFinanziamentoValidationMethods.class, input, logger);
			
			if(segnalazioneList!=null && !segnalazioneList.isEmpty()){
				for (SegnalazioneErrore segnalazioneErrore : segnalazioneList) {
					addMessage(newMessages, segnalazioneErrore.getCampoErrore(), MetodiUtili.prefixErrMsg(newMessages,segnalazioneErrore));			
				}
			}
			logger.info("[FormaFinanziamento::modelValidate()] fine validazioni definite da variabile di configurazione");
	 }
     else{
	    logger.info("FormaFinanziamento::modelValidate] _formaFinanziamento non presente o vuoto");
     }
		logger.info("FormaFinanziamento::modelValidate] errore "+ erroreS4P4);	
		logger.debug("FormaFinanziamento::modelValidate] _formaFinanziamento END");
		return newMessages;
	  }

	
	/**
	* @param limiteMinPrevisto
	 * @param contributoContoInteresse
	 * @param logger
	 * @return
	 */
	private boolean verificaLimMaxUguale(String limiteMinPrevisto,
			String contributoContoInteresse, Logger logger) {
		return MetodiUtili.isLimiteMaggioreUguale( contributoContoInteresse, limiteMinPrevisto, logger);
	}

	/**
	 * 
	 * @param limiteMaxPrevisto
	 * @param contributoContoInteresse
	 * @param logger
	 * @return
	 */
	private boolean verificaLimMinUguale(String limiteMaxPrevisto,
			String contributoContoInteresse, Logger logger) {
		return MetodiUtili.isLimiteMinoreUguale( contributoContoInteresse, limiteMaxPrevisto, logger);
	}

	/**
	 * 
	 * @param idBando
	 * @param idFormaFinanziamento
	 * @param logger
	 * @return
	 */
	private String limiteMinPrevistoDB(String idBando,
			String idFormaFinanziamento, Logger logger) {
		return MetodiUtili.recuperoImportoMinErogabileByIdBando(idBando, idFormaFinanziamento, logger);
	}

	/**
	 * 
	 * @param idBando
	 * @param idFormaFinanziamento
	 * @param logger
	 * @return
	 */
	private String limiteMaxPrevistoDB(String idBando,
			String idFormaFinanziamento, Logger logger) {
		return MetodiUtili.recuperoImportoMaxErogabileByIdBando(idBando, idFormaFinanziamento, logger);
	}
	  

	/**
	 * Prima verifica richiesta:
	 * 	verifico se:
	 * 	- importo richiesto >= importo_minimo_erogabile
	 * 
	 * @param impRichBD
	 * @param impMin
	 * @param logger
	 * @return
	 */
	private int verificaImportoRichiesto(BigDecimal impRichBD, BigDecimal importo, Logger logger) {
		
		logger.info("Sono nel metodo: verificaImportoRichiesto ... ");
		int risultato = 0;
		
		if (impRichBD.compareTo(importo) == 0) { 
			logger.info(impRichBD + " e " + importo + " sono uguali."); 
            risultato = 0;
        } 
        else if (impRichBD.compareTo(importo) == 1) { 
        	logger.info(impRichBD + " é maggiore di " + importo + "."); 
            risultato = 1;
        } 
        else { 
        	logger.info(impRichBD + " é minore di " + importo + "."); 
            risultato = -1;
        } 
		return risultato;
	}

	@Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }
}
