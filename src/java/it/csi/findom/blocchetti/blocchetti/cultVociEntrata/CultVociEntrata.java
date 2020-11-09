/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultVociEntrata;

import it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazione.CultFormaAgevolazioneVO;
import it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB.CultFormaAgevolazioneBVO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
//----------------------------------------------------------- : Jira 1330 -  - inizio
// import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.util.NoDecimalFormat;
//----------------------------------------------------------- : Jira 1330 -  - fine
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

public class CultVociEntrata extends Commonality {

	CultVociEntrataInput input = new CultVociEntrataInput();
	// ---------------------------------------------------------- : Jira 1330 -  - inizio
	// DecimalFormat decimalFormatter = new DecimalFormat();
		NoDecimalFormat decimalFormatter = new NoDecimalFormat();
  		boolean isDecimaliUguali = false;
	
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info1, List<CommonalityMessage> messages)
			throws CommonalityException {
	
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> messages) throws CommonalityException {
		//  Auto-generated method stub
		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[CultVociEntrata::inject] _CultVociEntrata BEGIN");
		CultVociEntrataOutput output = new CultVociEntrataOutput();

		try {
			
			List<VoceEntrataItemVO> vociEntrataList = new ArrayList<VoceEntrataItemVO>();	        //lista contenente tutte le voci di entrata e usate per popolare la combo relativa 
			List<VoceEntrataItemVO> vociEntrataScelteList = new ArrayList<VoceEntrataItemVO>();	//le voci di entrata scelte dall'utente e mappate sulla tabella in basso nella pagina 
			List<VoceEntrataItemVO> vociEntrataRiepilogoList = new ArrayList<VoceEntrataItemVO>();	//le voci di entrata scelte dall'utente e rappgruppate/ordinate e mappate sul riepilogo in alto nella pagina
			String totaleGenerale = "0,00";	
			String viewWarningEntrateAgevolazioni = "";	 // se true visualizzo un messaggio di tipo Warning dopo il salvataggio	
			//String isReloadPage = "";
						
			//// valorizzazione		
			if (info.getCurrentPage() != null) {
				if (!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {			
					String idBando = info.getStatusInfo().getTemplateId()+"";
							
					vociEntrataList = CultVociEntrataDAO.getVociEntrataList(idBando, logger);
					
					CultVociEntrataVO vociEntrataMap = (CultVociEntrataVO) input._vociEntrata;
					if( vociEntrataMap != null && !vociEntrataMap.isEmpty() ) {
					    
						if(vociEntrataMap.getVociEntrataScelteList()!=null){
							
							vociEntrataScelteList = Arrays.asList(vociEntrataMap.getVociEntrataScelteList());		    
					    	
					    	for(int i=0; i<vociEntrataScelteList.size(); i++){
						      VoceEntrataItemVO curVoceEntrata = null;
							     curVoceEntrata = (VoceEntrataItemVO)vociEntrataScelteList.get(i);
							     
     					      if (curVoceEntrata!=null && curVoceEntrata.is__Deleted()) {
    
							      //faccio in modo da rimettere nell'html il campo marcato per essere cancellato
						          curVoceEntrata = new VoceEntrataItemVO();
						          curVoceEntrata.setDaCancellare("true");
//						          vociEntrataScelteList.remove(i);
//						          vociEntrataScelteList.add(i,curVoceEntrata);	
						          
						          vociEntrataScelteList.set(i, curVoceEntrata);
						          
							      continue;
						      }
						      
						      if(curVoceEntrata!=null){				         
						    	  String idVoceEntrata = (String) curVoceEntrata.getIdVoceEntrata();				         
						          curVoceEntrata.setDaCancellare("false");
						          
						          VoceEntrataItemVO curVoceEntrataDati = getVoceEntrataById(idVoceEntrata,vociEntrataList);
				        	
						          if(curVoceEntrataDati!=null){
						             String descrizione = (String) curVoceEntrataDati.getDescrizione();
						             String descrBreve = (String) curVoceEntrataDati.getDescrBreve();
						             String indicazioni = (String) curVoceEntrataDati.getIndicazioni();				                             
						             String flagDuplicabile = (String) curVoceEntrataDati.getFlagDuplicabile();
						             String flagEdit = (String) curVoceEntrataDati.getFlagEdit();
						             if(flagEdit==null || !flagEdit.equals("S")){
						                //il dettaglio non è editabile ed e' costituito dal valore del campo 'indicazioni'				               
						                curVoceEntrata.setDettaglio(indicazioni);
						                curVoceEntrata.setIndicazioni("");		  				            
						            
						             }else {
						               if(flagDuplicabile != null){				                 
						                 
						                 if(flagDuplicabile.equals("S")){
						                    curVoceEntrata.setIndicazioni(indicazioni + " (più righe ammesse)"); 		
						                 }else if(flagDuplicabile.equals("N")){				                   
						                    curVoceEntrata.setIndicazioni(indicazioni + " (una sola riga ammessa)"); 		
						                 } 
						              
						               }// chiude test null su flagDuplicabile				            
						               
						             }//chiude else test null o non "S" su flagEdit	
						         
						             curVoceEntrata.setDescrizione(descrizione);
						             curVoceEntrata.setDescrBreve(descrBreve);				            
						             curVoceEntrata.setFlagDuplicabile(flagDuplicabile);
						             curVoceEntrata.setFlagEdit(flagEdit);

						     //MB2017_03_16 curVoceEntrata.put("importo", decimalFormat(curVoceEntrata.getImporto(),2));
						            
						             // MB2017_03_20 ini      
						             String curImporto = curVoceEntrata.getImporto();
						             if (decimalFormatter.decimalValidate(curImporto, false, false)){
						                curImporto = decimalFormatter.decimalFillZero(curImporto,2);
						             }
						             curVoceEntrata.setImporto(curImporto); 				             
						             //MB2017_03_20 fine     			            
						            
						         }//chiude test null su curVoceEntrataDati   
						             
						      }//chiude test null su curVoceEntrata				
						   }//chiude for su vociEntrataScelteList			    
					    } //chiude test null su vociEntrataScelteList	
					    
					    //MB2017_05_02 ini
					    if(!StringUtils.isBlank(input.aggiornaRiepilogo) && ("S").equals(input.aggiornaRiepilogo)){			        
					        vociEntrataRiepilogoList = ordinaVociEntrataScelteList(vociEntrataScelteList, logger);
					        CultVociEntrataVO vociEntrataXML = (CultVociEntrataVO) input._vociEntrata;
					        vociEntrataRiepilogoList = impostaEliminazioneRecordRiepilogo(vociEntrataXML,vociEntrataRiepilogoList, logger);
					        
					    }else{	
					    	List<VoceEntrataItemVO> vociEntrataRiepilogoListXML = new ArrayList<VoceEntrataItemVO>();
					       
					    	if (vociEntrataMap.getVociEntrataRiepilogoList()!=null) {
					    		vociEntrataRiepilogoListXML = Arrays.asList(vociEntrataMap.getVociEntrataRiepilogoList());	
					    	}
					    	vociEntrataRiepilogoList = vociEntrataRiepilogoListXML;
		                   
					    }
					    //MB2017_05_02 fine
					    
					    totaleGenerale = calcolaTotaleGenerale(vociEntrataRiepilogoList);
					    
					    //MB2017_03_27 ini
					    if( SessionCache.getInstance().get("warningEntrateAgevolazioni")!=null ){
					       String warningEntrateAgevolazioni = (String)SessionCache.getInstance().get("warningEntrateAgevolazioni");
					       if(!StringUtils.isBlank(warningEntrateAgevolazioni) && warningEntrateAgevolazioni.equals("true")){
						        
					          viewWarningEntrateAgevolazioni = "true";
					          SessionCache.getInstance().set("warningEntrateAgevolazioni", null);  
					       }      
				        }
					    //MB2017_03_27 fine
					    
					    			    
					    
			        }//chiude test null su vociEntrataMap	        
			    }
			}  
				
			//// namespace
		    output.vociEntrataList=vociEntrataList;	
		    output.vociEntrataScelteList=vociEntrataScelteList;	
		    output.vociEntrataRiepilogoList=vociEntrataRiepilogoList;
		    output.totaleGenerale=totaleGenerale;
		    output.viewWarningEntrateAgevolazioni=viewWarningEntrateAgevolazioni;				
	     	       	
		}catch (Exception e) {
			logger.error("[CultVociEntrata::inject] Error ", e);
			throw new CommonalityException(e);
		}finally {
			logger.info("[CultVociEntrata::inject] END");
			
		}

		return output;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) arg0;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		logger.info("[CultVociEntrata::modelValidate] _cultVociEntrata BEGIN");

		String ERRMSG_CAMPO_OBBLIGATORIO_VOCI_E = "- il campo è obbligatorio";
		String ERRMSG_FORMATO_IMPORTO_VOCI_E = "- il valore deve essere numerico, maggiore di zero e senza decimali";
		String ERRMSG_VOCE_DUPLICATA = "- questa voce di entrata non puo' essere duplicata";
		String ERRMSG_ALMENO_UNA_VOCE_ENTRATA_OBBLIGATORIA = " - indicare almeno una voce di entrata";
		String ERRMSG_RIEPILOGO_NON_AGGIORNATO = " - aggiornare l'elenco prima di salvare";

		/* --------------------------------------------------------------------- fine */
		
		CultVociEntrataVO vociEntrata = (CultVociEntrataVO) input._vociEntrata;
		logger.info("[CultVociEntrata::modelValidate]vociEntrata:" + vociEntrata);
		boolean trovatoVoce = false;
		int numDettagli = 0;
		
//		if (vociEntrata != null && !vociEntrata.isEmpty()) {   
		if (vociEntrata != null) 
		{   
			int numVociEntrataValide = 0; //numero di record non cancellati nella tabella; serve per il controllo che ci sia almeno una voce di entrata
			boolean trovatoEr = false;
			
			List<VoceEntrataItemVO> vociEntrataScelteList = new ArrayList<VoceEntrataItemVO>();
			if (vociEntrata.getVociEntrataScelteList() != null) {
				vociEntrataScelteList = Arrays.asList(vociEntrata.getVociEntrataScelteList());
			}
			
			if (vociEntrataScelteList.size() > 0) 
			{
				logger.info("[CultVociEntrata::modelValidate]vociEntrataScelteList:" + vociEntrataScelteList);
			    
			    //controllo che non ci siano duplicazioni di voci con flag_duplicabile = "N"		
				Outer:
				for (int j = 0, size = vociEntrataScelteList.size(); j < size; j++) 
				{
				    try
				    {
				    	VoceEntrataItemVO voce = (VoceEntrataItemVO) vociEntrataScelteList.get(j);
				    	
						if (voce != null && !voce.is__Deleted()) 
						{
						   String flagDuplicabile = voce.getFlagDuplicabile();
						   
						   if(!StringUtils.isEmpty(flagDuplicabile) && flagDuplicabile.equals("N"))
						   {
						       String idVoceEntrata = voce.getIdVoceEntrata();
						       //cerco altre occorrenze dell'idVoceEntrata corrente; alla prima occorrenza interrompo il controllo e segnalo errore
						      
						       for(int k=j+1; k<vociEntrataScelteList.size(); k++)
						       {
						    	   VoceEntrataItemVO curInnerVoce = vociEntrataScelteList.get(k);
						    	   
				                   if(curInnerVoce!=null && !curInnerVoce.is__Deleted())
				                   {
				                      String curInnerIdVoceEntrata = (String) curInnerVoce.getIdVoceEntrata();
				                      
				                      if(!StringUtils.isEmpty(curInnerIdVoceEntrata) && curInnerIdVoceEntrata.equals(idVoceEntrata))
				                      {
				                         addMessage(newMessages,"_vociEntrata_voce_duplicata_testo", ERRMSG_VOCE_DUPLICATA);
										 addMessage(newMessages, "_vociEntrata_voce_id", j+"U");
										 logger.warn("[CultVociEntrata::modelValidate]_vociEntrata voce con flag_duplicabile = 'N' duplicata ");
										 trovatoEr = true;
										 break Outer;   //esco dai due cicli, tornando alla label 
				                      
				                      } //chiude test null su curInnerIdVoceEntrata	      
				                   } //chiude test null su curInnerVoce
				               }//chiude il for annidato
						   }//chiude test su flagDuplicabile
						}//chiude test null su voce
						
					}catch(ClassCastException e){
						// se sono qui e' perche' c'era un elemento marcato DELETED, quindi lo devo ignorare
						logger.warn("[CultVociEntrata::modelValidate]probabile elemento DELETED");
						continue;
					}
				}//chiude il for esterno per controllo duplicazioni di voci con flag_duplicabile = "N"
							
				//altri controlli	
				int cntVociEntrate = 0;
				for (int i = 0, n = vociEntrataScelteList.size(); i < n; i++) 
				{
					try
					{
						VoceEntrataItemVO voce = (VoceEntrataItemVO) vociEntrataScelteList.get(i);
						logger.info("[CultVociEntrata::modelValidate]voce:" + voce);

						if (voce != null && !voce.is__Deleted()) 
						{									
							trovatoVoce=true;
							cntVociEntrate++;
							//controllo valorizzazione voce di entrata
							String idVoceEntrata = voce.getIdVoceEntrata();

							if (StringUtils.isBlank(idVoceEntrata) || idVoceEntrata.equals("-1")) {
								addMessage(newMessages, "_vociEntrata_id_testo", ERRMSG_CAMPO_OBBLIGATORIO_VOCI_E);
								addMessage(newMessages, "_vociEntrata_voce_id", i+"V");
								logger.warn("[CultVociEntrata::modelValidate]_vociEntrata id nullo");
								trovatoEr = true;						
							}
								
							//controllo valorizzazione dettaglio (se digitabile)						
							String flagEdit = voce.getFlagEdit();
							String dettaglio = voce.getDettaglio();
							if (!StringUtils.isBlank(flagEdit) && flagEdit.equals("S") && StringUtils.isBlank(dettaglio)) {
								addMessage(newMessages, "_vociEntrata_dettaglio_testo", ERRMSG_CAMPO_OBBLIGATORIO_VOCI_E);
								addMessage(newMessages, "_vociEntrata_voce_id", i+"D");
								logger.warn("[CultVociEntrata::modelValidate]_vociEntrata dettaglio nullo");
								trovatoEr = true;						
							}						
																
							// controllo valorizzazione importo
							String importo = voce.getImporto();		// Jira 1330 : gst decimali				
							
							if (StringUtils.isBlank(importo)) {
								addMessage(newMessages, "_vociEntrata_importo_testo", ERRMSG_CAMPO_OBBLIGATORIO_VOCI_E);
								addMessage(newMessages, "_vociEntrata_voce_id", i+"I");
								logger.warn("[CultVociEntrata::modelValidate]_vociEntrata importo nullo");
								trovatoEr = true;
							
							}else{
								
								/* ------------------------------------------------------------ : Esegui controllo validita dei decimali - inizio - */
							      if(!importo.isEmpty() && importo!=null)
							      {
							    	  isDecimaliUguali= MetodiUtili.confrontaDecimaliUguali(importo, logger);
							    	  logger.info("test: importo voci entrata vale: " + isDecimaliUguali);
							      }
						      /* ------------------------------------------------------------  : Esegui controllo validita dei decimali - fine - */
								// controllo sia un numerico con max due decimali									
								if(!importo.matches("(([1-9]\\d*)|0)(,\\d{1,2})?$") || importo.equals("0,00") || importo.equals("0") || !isDecimaliUguali){							                    
									addMessage(newMessages, "_vociEntrata_importo_testo", ERRMSG_FORMATO_IMPORTO_VOCI_E);
									addMessage(newMessages, "_vociEntrata_voce_id", i+"I");
									logger.warn("[CultVociEntrata::modelValidate]_vociEntrata importo formato non corretto");
									
									trovatoEr = true;
								}
							}
						}
						// numVociEntrataValide++;
						numVociEntrataValide = numVociEntrataValide-cntVociEntrate; // -1
						logger.info("numVociEntrataValide: " + numVociEntrataValide);
					}catch(ClassCastException e){
						// se sono qui e' perche' c'era un elemento marcato DELETED, quindi lo devo ignorare
						logger.warn("[CultVociEntrata::modelValidate]probabile elemento DELETED");
						continue;
					}
				} //chiude il for relativo agli altri controlli
				
				if(trovatoEr){
				   // metto questo elemento nei messaggi per segnalare che ho trovato almeno un errore					
				   logger.warn("[CultVociEntrata::modelValidate]e' stato trovato almeno un errore durante la validazione delle voci di entrata");
				   addMessage(newMessages, "_vociEntrata_testo", "xxxxxxxxx");
				}
			} 	
			
			if(numVociEntrataValide==0 && numDettagli == 0)
			{		
				// if(numDettagli == 0){	
				//ci deve essere almeno una voce di entrata
				addMessage(newMessages, "_vociEntrata_generale", ERRMSG_ALMENO_UNA_VOCE_ENTRATA_OBBLIGATORIA);
				logger.info("[CultVociEntrata::modelValidate]non e' stata indicata alcuna voce di entrata");
				trovatoEr = true;
			}
			
			//MB2017_03_28 ini
			//verifico se il riepilogo (la tabella sopra) e' aggiornato rispetto al dettaglio
			if(!trovatoEr)
			{
			   //se sono qua so che le righe del dettaglio sono corrette (non mancano dati, gli importi sono validi, non sono presenti duplicazioni non permesse).
			   //Pertanto ciascuna voce di entrata presente nel dettaglio deve essere presente nel riepilogo con gli stessi importi e viceversa; 
			   //in caso contrario segnalo che si deve aggiornare il riepilogo prima di salvare
			   
			   List<VoceEntrataItemVO> vociEntrataRiepilogoList = new ArrayList<VoceEntrataItemVO>();
			   if(vociEntrata.getVociEntrataRiepilogoList()!=null){
			      vociEntrataRiepilogoList = Arrays.asList(vociEntrata.getVociEntrataRiepilogoList());
			   }
			   
			   List<VoceEntrataItemVO> vociEntrataRiepilogoAuxList = new ArrayList<VoceEntrataItemVO>();
			   
			   //ciclo sul riepilogo e lo uso per popolare una lista ausiliaria che usero' poi nel controllo seguente
			   for (int t = 0; t < vociEntrataRiepilogoList.size(); t++) 
			   {
			      try
			      {
			    	  VoceEntrataItemVO curVoceRiepilogo = (VoceEntrataItemVO) vociEntrataRiepilogoList.get(t);
					
			    	  if(curVoceRiepilogo!=null && !curVoceRiepilogo.is__Deleted() && curVoceRiepilogo.getTipoRecord().equals("dettaglio"))
			    	  {
						  numDettagli++;
						  VoceEntrataItemVO auxMap = new VoceEntrataItemVO();
					     auxMap.setIdVoceEntrata(curVoceRiepilogo.getIdVoceEntrata());
					     auxMap.setDettaglio(curVoceRiepilogo.getDettaglio());
					     auxMap.setImporto(curVoceRiepilogo.getImporto());
					     auxMap.setTrovato("N");  //marcatore che dice se il record e' gia' stato considerato
					  
					     vociEntrataRiepilogoAuxList.add(auxMap);
					  
					  }//chiude if null su curVoceRiepilogo
			     
			      }catch(ClassCastException e){
					// se sono qui e' perche' c'era un elemento marcato DELETED, quindi lo devo ignorare
					continue;
				  }//chiude il catch
			   }//chiude il ciclo su riepilogoList
			   
			   // trovatoVoce = false;
			   for (int i = 0, n = vociEntrataScelteList.size(); i < n; i++) 
			   {
					try
					{
						VoceEntrataItemVO curVoceDettaglio = (VoceEntrataItemVO) vociEntrataScelteList.get(i);
						trovatoVoce = false;
						
			            if (curVoceDettaglio!=null && !curVoceDettaglio.is__Deleted())
			            {	  
			        	   String curIdVoceDettaglio = curVoceDettaglio.getIdVoceEntrata();
			        	   String curDettaglioDettaglio = curVoceDettaglio.getDettaglio();
			        	   String curImportoDettaglio = curVoceDettaglio.getImporto();

		                   //ciclo sul riepilogo per cercare una occorrenza corrispondente
			               for (int z = 0; z < vociEntrataRiepilogoAuxList.size(); z++) 
			               {
			            	   VoceEntrataItemVO curVoceRiepilogo = (VoceEntrataItemVO) vociEntrataRiepilogoAuxList.get(z);
			                   String curIdVoceRiepilogo = curVoceRiepilogo.getIdVoceEntrata();
			        	       String curDettaglioRiepilogo = curVoceRiepilogo.getDettaglio();
			        	       String curImportoRiepilogo = curVoceRiepilogo.getImporto();
			        	       String curTrovatoRiepilogo = curVoceRiepilogo.getTrovato();
			                   
			                   if(curIdVoceDettaglio.equals(curIdVoceRiepilogo) && 
			                      curDettaglioDettaglio.equals(curDettaglioRiepilogo) &&
			                      curImportoDettaglio.equals(curImportoRiepilogo) &&
			                      curTrovatoRiepilogo.equals("N"))
			                   {
			                        curVoceRiepilogo.setTrovato("S");
			                        trovatoVoce = true;
			                        break;
			                   
			                   }//chiude l'if che verifica uguaglianza tra voce Dettaglio e voce Riepilogo
			               }//chiude il ciclo annidato
			               
				            if(!trovatoVoce)
				            {
				                addMessage(newMessages, "_vociEntrata_aggiornamentoRichiesto", ERRMSG_RIEPILOGO_NON_AGGIORNATO);
				                logger.warn("[CultVociEntrata::modelValidate]non e' stato aggiornato il riepilogo prima del salvataggio");
				                trovatoEr = true;	      
				                break;  //ho gia' trovato un errore, per cui non proseguo ed esco dal for
				            }
			            }//chiude if null su curVoceDettaglio

			        }catch(ClassCastException e){
						// se sono qui e' perche' c'era un elemento marcato DELETED, quindi lo devo ignorare
						continue;
					}//chiude il catch
			   }//chiude il for su vociEntrataScelteList
			   
			   if(!trovatoVoce && numDettagli == 0 || numDettagli == 0)
			   {
				   	addMessage(newMessages, "row-433 _vociEntrata_generale", ERRMSG_ALMENO_UNA_VOCE_ENTRATA_OBBLIGATORIA);
	                logger.info("[CultVociEntrata::modelValidate]" + ERRMSG_ALMENO_UNA_VOCE_ENTRATA_OBBLIGATORIA);
	                trovatoEr = true;	      
	            }
			   
			   //se non ci sono stati errori, verifico che tutti i record di vociEntrataRiepilogoAuxList siano  siano "S" e quindi abbiano i corrispondenti record del dettaglio
			   if(!trovatoEr)
			   {
			      for (int y = 0; y < vociEntrataRiepilogoAuxList.size(); y++) 
			      {
			    	  VoceEntrataItemVO curVoceRiepilogo = (VoceEntrataItemVO) vociEntrataRiepilogoAuxList.get(y);  
			    
				     if(curVoceRiepilogo.getTrovato().equals("N")){
					    addMessage(newMessages,"_vociEntrata_aggiornamentoRichiesto", ERRMSG_RIEPILOGO_NON_AGGIORNATO);
			            logger.warn("[CultVociEntrata::modelValidate]non e' stato aggiornato il riepilogo prima del salvataggio");
			            trovatoEr = true;	    
			            break; 
				     } 
			      }	  
			   }
			    
			}//chiude if su trovatoEr
			//MB2017_03_28 fine
			
			
			//MB2017_03_27 ini
			if(!trovatoEr)
			{
			   //controllo per capire se visualizzare il warning che avvisa di verificare la pagina delle agevolazioni
			
			   //essendo nel model validation rules non ho ancora salvato il totale nell'xml del DB e quindi leggo  
			   //il totale precedentemente salvato sull'xml del DB con query xpath
			   String totaleEntrateXml = CultVociEntrataDAO.getTotaleEntrate(info.getStatusInfo().getNumProposta(), logger); 
			
			   CultVociEntrataVO vociEntrataMap = input._vociEntrata;
			   
			   if ("true".equals(input._cultPianoSpese_procedure_tot_spese))
	            {
					// fuori std
					CultFormaAgevolazioneBVO _formaAgevolazioneBVO = input._formaAgevolazioneB;	
					
					if(_formaAgevolazioneBVO !=null)
	            	{
	            		String totaleEntrateInAgevolazioneXml = (String) _formaAgevolazioneBVO.getTotaleEntrate();
	            		
	            		if(StringUtils.isBlank(totaleEntrateInAgevolazioneXml)){
	            			totaleEntrateInAgevolazioneXml= "0,00";
	            		}		          
	            		
	            		if(totaleEntrateInAgevolazioneXml.indexOf(",") == -1){		          
	            			totaleEntrateInAgevolazioneXml = totaleEntrateInAgevolazioneXml + ",00";
	            		} 
	            		
	            		if (vociEntrataMap != null && !vociEntrataMap.isEmpty()) 
	            		{ 
	            			String totaleEntrateAttuale = (String) vociEntrataMap.getTotale();
	            			
	            			if(StringUtils.isBlank(totaleEntrateXml)){
	            				totaleEntrateXml= "0,00";
	            			}		          
	            			
	            			if(StringUtils.isBlank(totaleEntrateAttuale)){
	            				totaleEntrateAttuale= "0,00";
	            			}		         
	            			
	            			if(totaleEntrateXml.indexOf(",") == -1){		          
	            				totaleEntrateXml = totaleEntrateXml + ",00";
	            			} 
	            			
	            			if(totaleEntrateAttuale.indexOf(",") == -1){		          
	            				totaleEntrateAttuale = totaleEntrateAttuale + ",00";
	            			}
	            			
	            			boolean trovatoDiff = false;	
	            			
	            			if(!totaleEntrateXml.equals(totaleEntrateAttuale)){
	            				//confronto il totale entrate corrente con quello salvato sull'xml
	            				SessionCache.getInstance().set("warningEntrateAgevolazioni","true");	//verra' tolto dalla sessione nell'inject	
	            				trovatoDiff = true;     
	            			}			
	            			
	            			if(!trovatoDiff){
	            				// se il controllo precedente e' stato superato verifico se il totale entrate attuale differisce dal totale entrate salvato in _formaAgevolazione 
	            				if(!totaleEntrateAttuale.equals(totaleEntrateInAgevolazioneXml)){
	            					SessionCache.getInstance().set("warningEntrateAgevolazioni","true");	//verra' tolto dalla sessione nell'inject	
	            				}
	            			}	
	            		}
	            	}
					
	            }else{
	            	// std
	            	CultFormaAgevolazioneVO formaAgevolazioneMap = input._formaAgevolazione;
	            	
	            	if(formaAgevolazioneMap !=null)
	            	{
	            		String totaleEntrateInAgevolazioneXml = (String) formaAgevolazioneMap.getTotaleEntrate();
	            		
	            		if(StringUtils.isBlank(totaleEntrateInAgevolazioneXml)){
	            			totaleEntrateInAgevolazioneXml= "0,00";
	            		}		          
	            		
	            		if(totaleEntrateInAgevolazioneXml.indexOf(",") == -1){		          
	            			totaleEntrateInAgevolazioneXml = totaleEntrateInAgevolazioneXml + ",00";
	            		} 
	            		
	            		if (vociEntrataMap != null && !vociEntrataMap.isEmpty()) 
	            		{ 
	            			String totaleEntrateAttuale = (String) vociEntrataMap.getTotale();
	            			
	            			if(StringUtils.isBlank(totaleEntrateXml)){
	            				totaleEntrateXml= "0,00";
	            			}		          
	            			
	            			if(StringUtils.isBlank(totaleEntrateAttuale)){
	            				totaleEntrateAttuale= "0,00";
	            			}		         
	            			
	            			if(totaleEntrateXml.indexOf(",") == -1){		          
	            				totaleEntrateXml = totaleEntrateXml + ",00";
	            			} 
	            			
	            			if(totaleEntrateAttuale.indexOf(",") == -1){		          
	            				totaleEntrateAttuale = totaleEntrateAttuale + ",00";
	            			}
	            			
	            			boolean trovatoDiff = false;	
	            			
	            			if(!totaleEntrateXml.equals(totaleEntrateAttuale)){
	            				//confronto il totale entrate corrente con quello salvato sull'xml
	            				SessionCache.getInstance().set("warningEntrateAgevolazioni","true");	//verra' tolto dalla sessione nell'inject	
	            				trovatoDiff = true;     
	            			}			
	            			
	            			if(!trovatoDiff){
	            				// se il controllo precedente e' stato superato verifico se il totale entrate attuale differisce dal totale entrate salvato in _formaAgevolazione 
	            				if(!totaleEntrateAttuale.equals(totaleEntrateInAgevolazioneXml)){
	            					SessionCache.getInstance().set("warningEntrateAgevolazioni","true");	//verra' tolto dalla sessione nell'inject	
	            				}
	            			}	
	            		}
	            	}
	            }
			   
			}
			//MB2017_03_27 fine
			
		} else {
			logger.warn("[CultVociEntrata::modelValidate]_vociEntrata non presente o vuoto");
		}
	
		logger.info("[CultVociEntrata::modelValidate] _cultVociEntrata END");
	
		return newMessages;
	}
	

	private String calcolaTotaleGenerale(List<VoceEntrataItemVO> vociList){
		BigDecimal totGenerale = new BigDecimal(0);
		
		if(vociList ==null){
			return "0,00";
		}

		VoceEntrataItemVO curVoce = null;
		
		for(int i=0; i<vociList.size(); i++)
		{	 
	        try
	        {	           
	          curVoce = (VoceEntrataItemVO) vociList.get(i);  
	        }catch(ClassCastException e){
			   //nel caso di un elemento cancellato il cast a Map fallisce perche' e' una String
			   continue;
			}
	        
	        if(curVoce!=null)
	        {
	        	String tipoRecord = curVoce.getTipoRecord();
	        	if(!StringUtils.isBlank(tipoRecord) && tipoRecord.equals("radice")){
	        		totGenerale = totGenerale.add(decimalFormatter.getBigDecimalFromString(curVoce.getImporto(), false)); 
	        	}//chiude test su tipoRecord
	        	
	        }//chiude test null su curVoce	          
		}//chiude il for
		return decimalFormatter.getStringFromBigDecimal(totGenerale, 2);  
	}

	
	//cerca in vociEntrataListPar l'elemento (una mappa) avente idVoceEntrata uguale al parametro idVoceEntrataPar
	//e lo ritorna al chiamante
	private VoceEntrataItemVO getVoceEntrataById(String idVoceEntrataPar, List<VoceEntrataItemVO> vociEntrataListPar)
	{	   
	   if(vociEntrataListPar!=null && !vociEntrataListPar.isEmpty() && !StringUtils.isEmpty(idVoceEntrataPar))
	   {	   
		  for(int i=0; i<vociEntrataListPar.size(); i++)
		  {
			  VoceEntrataItemVO curVoce = vociEntrataListPar.get(i);

			  if(curVoce!=null)
			  {
		         if (curVoce.getIdVoceEntrata()!=null)
		         {
		            String curIdVoceEntrata = (String) curVoce.getIdVoceEntrata();
		         
		            if(curIdVoceEntrata.equals(idVoceEntrataPar)){
		               return curVoce;	
		            }		         
		         }		      
		      }
		  }
	   }
	   return null;
	}
	
	//raggruppa per voci di entrata le diverse voci inserite dall'utente, le ordina e a ciascuna voce individuata fa seguire, nelle lista 
	//restituita, una serie di record che condividono con il primo l'id voce di entrata e dal primo si differenziano per dettaglio associato
	//la valorizzazione messa a fattor comune ha un importo ottenuto dalla somma degli importi dei suoi dettagli 	  
	private List<VoceEntrataItemVO> ordinaVociEntrataScelteList(List<VoceEntrataItemVO> vociEntrataScelteListPar, Logger logger) 
	{		
		List<VoceEntrataItemVO> retList = new ArrayList<VoceEntrataItemVO>();  //lista ritornata dal metodo
		List<VoceEntrataItemVO> tmpList = new ArrayList<VoceEntrataItemVO>();  //lista di utilita'
		List<VoceEntrataItemVO> dettaglioList =  new ArrayList<VoceEntrataItemVO>(); //lista di utilita' 
	  
	   if(vociEntrataScelteListPar!=null && !vociEntrataScelteListPar.isEmpty())
	   {       
	       //clono la lista di partenza
	       for(int i=0; i<vociEntrataScelteListPar.size(); i++)
	       {	           
	    	   VoceEntrataItemVO curVoce = (VoceEntrataItemVO) vociEntrataScelteListPar.get(i); 
	          //Map curVoce = null;
           
	    	   try{
				curVoce = (VoceEntrataItemVO) vociEntrataScelteListPar.get(i);
			   } catch(ClassCastException e){			      
				  //nel caso di un elemento cancellato il cast a Map fallisce perche' e' una String
				  continue;
			  }	
	    	   
              //Considero solo le righe completamente valorizzate 
	          if(curVoce!=null && StringUtils.isNotBlank(curVoce.getIdVoceEntrata()) && 
	                               StringUtils.isNotBlank(curVoce.getDettaglio())&& 
	                               StringUtils.isNotBlank(curVoce.getImporto()) && 
	                               decimalFormatter.decimalValidate(curVoce.getImporto(), false, false) ){    
	        	  
	        	  VoceEntrataItemVO newMap = new VoceEntrataItemVO();
	              newMap.setIdVoceEntrata(curVoce.getIdVoceEntrata());
	              newMap.setDescrizione(curVoce.getDescrizione());
	              newMap.setDescrBreve(curVoce.getDescrBreve());
	              newMap.setFlagDuplicabile(curVoce.getFlagDuplicabile());
	              newMap.setFlagEdit(curVoce.getFlagEdit());
	              newMap.setDettaglio(curVoce.getDettaglio());  
	              
//MB2017_03_20 newMap.put("importo", decimalFormat(curVoce.getImporto(),2));              
	              newMap.setImporto(curVoce.getImporto());  
	              
	              newMap.setElaborato("N"); //marcatura preliminare di tutte le voci di entrata come 'non elaborate'
	              tmpList.add(newMap);
	           }           
	       }
	       
	       //comparator per ordinamento lista ottenuta
	       Comparator vociEntrataComparator = new Comparator() {
             public int compare(Object o1, Object o2) {
            	 VoceEntrataItemVO voce1 = (VoceEntrataItemVO) o1;
            	 VoceEntrataItemVO voce2 = (VoceEntrataItemVO) o2;
        
                String descrBreve1 = (String) voce1.getDescrBreve();
                String descrBreve2 = (String) voce2.getDescrBreve();         
        
                return descrBreve1.compareTo(descrBreve2);
             }
           };
           
           //ordinamento lista clone        
	       Collections.sort( tmpList, vociEntrataComparator);
	       
	       logger.info("[CultVociEntrata::inject]  tmpList ordinata vale :" + tmpList);
	       
	       //ciclo sulla lista clone (tmpList, ordinata) per costruire una nuova lista in cui tutte le occorrenze 
	       //di una certa voce di entrata sono consecutive e prefissate da una ulteriore 
	       //occorrenza che fa da 'titolo' e il cui importo e' la somma degli importi delle voci aventi uguale idVoceEntrata
	       for(int j=0; j<tmpList.size(); j++)
	       {  
	    	   dettaglioList = new ArrayList<VoceEntrataItemVO>();
	    	   VoceEntrataItemVO curOuterVoce = (VoceEntrataItemVO) tmpList.get(j); 	           
	       
	    	   if (curOuterVoce!=null)
	    	   { 
	               String elaboratoOuter = curOuterVoce.getElaborato();	                
	           
	               if(elaboratoOuter.equals("N"))
	               {  
	            	   //non ancora elaborato
	            	   curOuterVoce.setElaborato("S");  //marco il record come elaborato 
	                   
	            	   if(!StringUtils.isEmpty(curOuterVoce.getIdVoceEntrata()))
	            	   {	                       
	                       
	        //MB2017_03_28 BigDecimal totaleVoce = getImporto(curOuterVoce.getImporto());  
	                       BigDecimal totaleVoce = decimalFormatter.getBigDecimalFromString(curOuterVoce.getImporto(), false); 
	                       
	                       //la voce di entrata corrente diventa, nel riepilogo, una voce radice, avente quindi le seguenti caratteristiche:
	                       //1) tipoRecord = 1 (indentata quindi a sinistra)
	                       //2) importo uguale alla somma degli importi di tutte le voci di entrata con il suo id (compresa se stessa) 
	                       //3) seguita nell'ordinamento da un'altra occorrenza di se stessa avente il proprio importo e tipoRecord = 2 (quindi indentata a destra)
	                       // e poi seguita dalle eventuali altre voci di entrata aventi il suo stesso id_voce_entrata 
	                       VoceEntrataItemVO radiceMap = new VoceEntrataItemVO();
	                       radiceMap.setIdVoceEntrata(curOuterVoce.getIdVoceEntrata());
	                       radiceMap.setDescrizione( curOuterVoce.getDescrizione());
	                       radiceMap.setDescrBreve( curOuterVoce.getDescrBreve());
	                       radiceMap.setFlagDuplicabile( curOuterVoce.getFlagDuplicabile());
	                       radiceMap.setFlagEdit( curOuterVoce.getFlagEdit());	              
	                       radiceMap.setDettaglio(curOuterVoce.getDettaglio());
	                       radiceMap.setTipoRecord("radice");
	                       //l'importo della radice e' la somma degli importi dei suoi dettagli
	                       
	                       //costruzione del primo dettaglio della voce di entrata corrente, con dati uguali a quelli della voce radice 
	                       VoceEntrataItemVO primoDettaglio = new VoceEntrataItemVO();
	                       primoDettaglio.setIdVoceEntrata(curOuterVoce.getIdVoceEntrata());
	                       primoDettaglio.setDescrizione( curOuterVoce.getDescrizione());
	                       primoDettaglio.setDescrBreve( curOuterVoce.getDescrBreve());
	                       primoDettaglio.setFlagDuplicabile( curOuterVoce.getFlagDuplicabile());
	                       primoDettaglio.setFlagEdit( curOuterVoce.getFlagEdit());	              
	                       primoDettaglio.setDettaglio(curOuterVoce.getDettaglio());	                      
	                       
	        //MB2017_03_20 primoDettaglio.put("importo",curOuterVoce.getImporto()); 	                       
	                       primoDettaglio.setImporto(decimalFormatter.decimalFillZero(curOuterVoce.getImporto(),2)); //MB2017_03_20
	                       
	                       primoDettaglio.setTipoRecord("dettaglio");
	                       
	                       dettaglioList.add(primoDettaglio);	                    
	                   
	                       //ciclo in cui si cercano le eventuali altre occorrenze della voce di entrata corrente 
	                       for(int k=j+1; k<tmpList.size(); k++){	                        
	                       	  
	                    	   VoceEntrataItemVO curInnerVoce = (VoceEntrataItemVO) tmpList.get(k);  
	                           if (curInnerVoce!=null)
	                           {
	                              String elaboratoInner = curInnerVoce.getElaborato();	                             
	                           
	                              if(elaboratoInner.equals("N") )
	                              {	                            	 
	                                  if (!StringUtils.isEmpty(curInnerVoce.getIdVoceEntrata()))
	                                  {	                                     
	                                      if(curInnerVoce.getIdVoceEntrata().equals(curOuterVoce.getIdVoceEntrata()))
	                                      {
	                                    	  
	                                    	  curInnerVoce.setElaborato("S");  //marco il record come elaborato	                                         
	                                          
	                                    	  VoceEntrataItemVO curDettaglio = new VoceEntrataItemVO();	                                        
	                                          curDettaglio.setIdVoceEntrata(curInnerVoce.getIdVoceEntrata());
	                                          curDettaglio.setDescrizione( curInnerVoce.getDescrizione());
	                                          curDettaglio.setDescrBreve( curInnerVoce.getDescrBreve());
	                                          curDettaglio.setFlagDuplicabile( curInnerVoce.getFlagDuplicabile());
	                                          curDettaglio.setFlagEdit( curInnerVoce.getFlagEdit());	
	                                          curDettaglio.setDettaglio(curInnerVoce.getDettaglio());
	                                          
	                          //MB2017_03_20  curDettaglio.put("importo(curInnerVoce.getImporto());
	                                          curDettaglio.setImporto(decimalFormatter.decimalFillZero(curInnerVoce.getImporto(),2));
	                                          	                                         
	                                          curDettaglio.setTipoRecord("dettaglio");
	                                          
	                                          dettaglioList.add(curDettaglio);
	                                         
	                           //MB2017_03_20 BigDecimal importoInnerVoce = getImporto(curInnerVoce.getImporto());	                                           
	                                          BigDecimal importoInnerVoce = decimalFormatter.getBigDecimalFromString(curInnerVoce.getImporto(), false);//MB2017_03_20
	                                          
	                                          //sommo l'importo del dettaglio corrente a quanto totalizzato fin'ora per la voce entrata corrente
	                                          totaleVoce = totaleVoce.add(importoInnerVoce);	                                         	                                        
	                                     }
	                               
	                                } //chiude test su idVoceEntrata di curInnerVoce                                   
	                            
	                              }//chiude test su elaboratoInner
	                           }//chiude test null su curInnerVoce
	                       
	                       } //chiude il for annidato	                     
	                      /*MB2017_03_20 ini 
					       String totaleVoceEntrataStr = totaleVoce.toString();
					       if(totaleVoceEntrataStr.indexOf(".") != -1){
					          totaleVoceEntrataStr = totaleVoceEntrataStr.replace(".",",");
					       }					     
					       radiceMap.put("importo", decimalFormat(totaleVoceEntrataStr,2));
					       MB2017_03_20 fine */
					       
					       radiceMap.setImporto(decimalFormatter.getStringFromBigDecimal(totaleVoce, 2));
					     
					       //ora che il totale è calcolato aggiungo la mappa radice e poi tutti i suoi dettagli temporaneamente messi in dettaglioList
					       retList.add(radiceMap);					       
					       for(int x=0; x < dettaglioList.size(); x++){
					    	   retList.add((VoceEntrataItemVO) dettaglioList.get(x));
					       }
	                          
	                   }//chiude test su curOuterIdVoceEntrata
	                   
	               }//chiude test null su elaboratoOuter	         
	           } //chiude test null su curOuterVoce     
	           
	       }//chiude il for su retList
	   }//chiude il test null su vociEntrataScelteListPar	  
	  
	   logger.info("[CultVociEntrata::inject] ordinaVociEntrataScelteList ritorna la lista seguente " + retList);	   
	   
	   return retList;
	}//chiude il metodo
	
	//gestione della cancellazione voci di entrata riepilogo 
	private List<VoceEntrataItemVO> impostaEliminazioneRecordRiepilogo(CultVociEntrataVO vociEntrataXML, List<VoceEntrataItemVO> vociEntrataRiepilogoList, Logger logger){
	   //numero di elementi nel riepilogo voci di entrata in questo momento (appena aggiornato) 
	   int numElementiVociEntrataAttuali = 0;
	   //numero di elementi nel riepilogo valorizzazione presente sull'XML (ultimo salvato)
	   int numElementiVociEntrataXml = 0;
	   if(vociEntrataRiepilogoList!=null && !vociEntrataRiepilogoList.isEmpty()){
		  numElementiVociEntrataAttuali = vociEntrataRiepilogoList.size();
	   }
	   logger.debug("[CultVociEntrata::inject] impostaEliminazioneRecordRiepilogo(),  numElementiVociEntrataAttuali = " + numElementiVociEntrataAttuali  );
		
	   if(vociEntrataXML!=null && vociEntrataXML.getVociEntrataRiepilogoList()!=null){
			   
		   List<VoceEntrataItemVO> vociEntrataRiepilogoListXML = Arrays.asList(vociEntrataXML.getVociEntrataRiepilogoList()); 
		   logger.debug("[CultVociEntrata::inject]vociEntrataRiepilogoListXML = " + vociEntrataRiepilogoListXML);
		   numElementiVociEntrataXml = vociEntrataRiepilogoListXML.size();
	       logger.debug("[CultVociEntrata::inject]impostaEliminazioneRecordRiepilogo(), numElementiVociEntrataXml = " + numElementiVociEntrataXml  );
			       
	       if(numElementiVociEntrataXml>0){
	    	   if(numElementiVociEntrataXml > numElementiVociEntrataAttuali){
	    		   int numElementiInPiuSuXml = numElementiVociEntrataXml-numElementiVociEntrataAttuali;
			            
	    		   for(int k = 0; k<numElementiInPiuSuXml; k++){
	    			   VoceEntrataItemVO mapDeleted = new VoceEntrataItemVO();
	    			   mapDeleted.setTipoRecord("del"); //il valore 'del' viene interpretato nel template come da valorizzare DELETED per cancellazione
	    			   vociEntrataRiepilogoList.add(mapDeleted);
	    		   }				               
	    	   }//chiude confronto num elementi tra le due liste
	       }//chiude test numElementiVociEntrataXml>0
	   }//chiude test null su vociEntrataXML e vociEntrataRiepilogoListXML
	
	   return vociEntrataRiepilogoList;
	} //chiude metodo impostaEliminazioneRecordRiepilogo

}
