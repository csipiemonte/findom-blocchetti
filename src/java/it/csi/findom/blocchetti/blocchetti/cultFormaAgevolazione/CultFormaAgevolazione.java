/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazione;

import it.csi.findom.blocchetti.blocchetti.cultVociEntrata.CultVociEntrataVO;
import it.csi.findom.blocchetti.blocchetti.cultVociEntrata.VoceEntrataItemVO;
import it.csi.findom.blocchetti.common.dao.PianoSpeseDAO;
import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.vo.cultPianospese.CultPianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.pianospese.ImportoPianoSpeseVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CultFormaAgevolazione extends Commonality {

	CultFormaAgevolazioneInput input = new CultFormaAgevolazioneInput();
	/* ----------------------------------------- : Esegui controllo round importoErogabile - inizio - */
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
		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[CultFormaAgevolazione::inject] _cultFormaAgevolazione BEGIN");
		CultFormaAgevolazioneOutput output = new CultFormaAgevolazioneOutput();

		try 
		{
			//// dichiarazioni 
	        String viewWarningAgevolazioni = "false";
	        
	        /**********************************************
	         * Jira: 1337 : 
	         * Visualizzare messaggio a video
	         * se non vi sono importi configurati in alcuna
	         * tabella predisposta su database.
	         ******************************************** */
			String viewWarningImportiAgevolazioni = "false";
			boolean visualizzaMsg = false; // Visualizza msg : Contattare Numero Verde per Assistenza tecnica.
			
			String totaleEntrate = "0,00"; 
			String speseConnesseAttivita = "0,00";
	        String speseGeneraliEFunz = "0,00";        	
	        String speseGeneraliEFunzQP = "0,00";
	        String totaleSpeseEffettive = "0,00";
	        String differenzaEU = "0,00";        
	        String importoRichiesto = "0,00"; 
	        
	        String percQuotaParteSpeseGenFunz = "100,00";
			String percMassimaContributoErogabile = "100,00";
			String importoMinimoErogabile = "0,00";
			
			String importoMassimoErogabile = "0,00";
		    String importoErogabile = "0,00";
		    
		    String quotaParte = "0,00";
		    	    
		    //legge l'importo erogabile salvato sull'xml
		
			//// valorizzazione
		    
			if (info.getCurrentPage() != null) 
			{	
				
				String idBando = info.getStatusInfo().getTemplateId()+"";
				logger.info("debug-83: idBando:: " + idBando);
				
			/***************************************************************************************************
			 *  : Esiste gia DAO per parametri calcolo... Questa ha priorita sulla tabella della Jira 1337:
			 *  Se questo record non restituisce dati, 
			 *  occorre recuperare quelli della tabella Jira: 1337
			 *  Se anche questa non restituisce dati, occorre visulaizzare un messaggio all'utente:
			 *  - msg.: Contattare Numero Verde Assistenza tecnica  
			 *  
			 *  Campi interessati:
			 *  - importo massimo ergabile
			 *  - importo minimo erogabile
			 *  - percentuale max contributo erogabile
			 ****************************************************************************************************/
			// se bando vecchio - differenzaEUBD
			ParametriCalcoloVO parametriCalcoloMap = null;
			
			String tmpPercMassimaContributoErogabile = "";
			String tmpImportoMinimoErogabile = "0,00";
			String tmpImportoMassimoErogabile = "";
			
			ImportoPianoSpeseVO importiParam = new ImportoPianoSpeseVO();
			Integer idDomanda = info.getStatusInfo().getNumProposta();
			logger.info("idDomanda: " + idDomanda);
			
			parametriCalcoloMap = CultFormaAgevolazioneDAO.getParametriCalcolo(idBando); // bando vecchio
			
			String tmpPercQuotaParteSpeseGenFunz = "";
			/**
			 * Gestione bandi-cultura vecchi...
			 */
			if (("false").equals(((CultFormaAgevolazioneInput)getInput())._cult_forma_agv_cfg_da_sportello))
			{
				// Se tutti e 3 gli importi sono assenti
				if(parametriCalcoloMap.getImportoMassimoErogabile() == null && parametriCalcoloMap.getImportoMinimoErogabile() == null && parametriCalcoloMap.getPercMassimaContributoErogabile() == null){
					visualizzaMsg = true; // Visualizza messaggio di assistenza Numero verde
				}
				
				tmpPercQuotaParteSpeseGenFunz = (parametriCalcoloMap.getPercQuotaParteSpeseGenFunz()).replace(".",",");
				logger.debug("debug-93: tmpPercQuotaParteSpeseGenFunz:: " + tmpPercQuotaParteSpeseGenFunz); // 100,00
				
				/* Percentuale massima contributo erogabile originale */
				tmpPercMassimaContributoErogabile = (parametriCalcoloMap.getPercMassimaContributoErogabile()).replace(".",",");
				logger.info("debug-96: tmpPercMassimaContributoErogabile:: " + tmpPercMassimaContributoErogabile); // 50,00
				
				/* Importo minimo erogabile originale */
				// String tmpImportoMinimoErogabile = "0,00";
				if(parametriCalcoloMap.getImportoMinimoErogabile()!= null){
					tmpImportoMinimoErogabile = (parametriCalcoloMap.getImportoMinimoErogabile()).replace(".",",");
					logger.debug("debug-99: tmpImportoMinimoErogabile:: " + tmpImportoMinimoErogabile); // 5000,00
				}
				
				/* Importo massimo erogabile originale */
				if(parametriCalcoloMap.getImportoMassimoErogabile()!= null){
					tmpImportoMassimoErogabile = (parametriCalcoloMap.getImportoMassimoErogabile()).replace(".",",");
					logger.info("debug-104: tmpImportoMassimoErogabile:: " + tmpImportoMassimoErogabile); // 30000,00
				}
			
				/** -------------------------------------- Gestione decimali ---------------------------------------- */
			
				percQuotaParteSpeseGenFunz = DecimalFormat.decimalFormat(tmpPercQuotaParteSpeseGenFunz,2);
				logger.info("debug-107: percQuotaParteSpeseGenFunz:: " + percQuotaParteSpeseGenFunz); // 100,00
				
				/* Percentuale massima contributo erogabile originale */
				percMassimaContributoErogabile = DecimalFormat.decimalFormat(tmpPercMassimaContributoErogabile,2);
				logger.info("debug-110: percMassimaContributoErogabile:: " + percMassimaContributoErogabile); // 50,00
				
				/* Importo minimo erogabile originale */
				importoMinimoErogabile = DecimalFormat.decimalFormat(tmpImportoMinimoErogabile,2);
				logger.info("debug-113: importoMinimoErogabile:: " + importoMinimoErogabile); // 5000,00
				
				/* Importo massimo erogabile originale */
				importoMassimoErogabile = DecimalFormat.decimalFormat(tmpImportoMassimoErogabile,2);	// 30000,00
				logger.debug("debug-137: importoMassimoErogabile:: " + importoMassimoErogabile); //
				
			}else{
				// se bando nuovo 
				importiParam = (ImportoPianoSpeseVO) CultFormaAgevolazioneDAO.getImportiPianoSpese(idDomanda, logger); // bando nuovo
				
				// Se tutti e 3 gli importi sono assenti
				if(importiParam.getImportoMassimoErogabile() == null && importiParam.getImportoMinimoErogabile() == null && importiParam.getPercMaxContributoErogabile() == null){
					visualizzaMsg = true; // visualizza messaggio
				}
				
				/***************************************************
				 * : Jira 1337: call function postgresql
				 * Dati da recuperare tramite idDomanda su database:
				 * 
				 * importoMassimoErogabile
				 * importoMinimoErogabile
				 * percMassimaContributoErogabile
				 * *************************************************/
				logger.info("1: Budget disponibile: " + String.valueOf(importiParam.getBudgetDisponibile()));
				logger.info("2  Importo massimo erogabile: " + String.valueOf(importiParam.getImportoMassimoErogabile()));
				logger.info("3: Importo minimo erogabile: " + String.valueOf(importiParam.getImportoMinimoErogabile()));
				logger.info("4: Perc max contributo erogabile: " + String.valueOf(importiParam.getPercMaxContributoErogabile()));
				logger.info("5: Totale spese massimo: " + String.valueOf(importiParam.getTotaleSpeseMassimo()));
				logger.info("6: Totale spese minimo: " + String.valueOf(importiParam.getTotaleSpeseMinimo()));
				
				if (parametriCalcoloMap.getPercQuotaParteSpeseGenFunz() != null){
					tmpPercQuotaParteSpeseGenFunz= parametriCalcoloMap.getPercQuotaParteSpeseGenFunz().replace(".",",");
					logger.info("debug-93: tmpPercQuotaParteSpeseGenFunz:: " + tmpPercQuotaParteSpeseGenFunz); // 100,00
				}
				
				/* Percentuale massima contributo erogabile originale */
				tmpPercMassimaContributoErogabile = String.valueOf(importiParam.getPercMaxContributoErogabile()).replace(".",",");
				logger.info("debug-96: tmpPercMassimaContributoErogabile:: " + tmpPercMassimaContributoErogabile);
				
				/* Importo minimo erogabile */
				tmpImportoMinimoErogabile = (String.valueOf(importiParam.getImportoMinimoErogabile())).replace(".",",");
				logger.info("debug-99: tmpImportoMinimoErogabile:: " + tmpImportoMinimoErogabile); // 5000,00
				
				/* Importo massimo erogabile */
				tmpImportoMassimoErogabile = (String.valueOf(importiParam.getImportoMassimoErogabile())).replace(".",",");
				logger.info("debug-104: tmpImportoMassimoErogabile:: " + tmpImportoMassimoErogabile); // 30000,00
			
				/** -------------------------------------- Gestione decimali ---------------------------------------- */
			
				percQuotaParteSpeseGenFunz = DecimalFormat.decimalFormat(tmpPercQuotaParteSpeseGenFunz,2);
				logger.info("debug-107: percQuotaParteSpeseGenFunz:: " + percQuotaParteSpeseGenFunz); // 100,00
				
				/* Percentuale massima contributo erogabile */
				percMassimaContributoErogabile = DecimalFormat.decimalFormat(tmpPercMassimaContributoErogabile,2);
				logger.info("debug-110: percMassimaContributoErogabile:: " + percMassimaContributoErogabile); // 50,00
				
				/* Importo minimo erogabile */
				importoMinimoErogabile = DecimalFormat.decimalFormat(tmpImportoMinimoErogabile,2);
				logger.info("debug-113: importoMinimoErogabile:: " + importoMinimoErogabile); // 5000,00
				
				/* Importo massimo erogabile */
				importoMassimoErogabile = DecimalFormat.decimalFormat(tmpImportoMassimoErogabile,2);	// 30000,00
				logger.info("debug-137: importoMassimoErogabile:: " + importoMassimoErogabile); //
			
			}
				
			// se nessuno importo recuperato da database, visualizzare msg
			if (visualizzaMsg) {
				// a video per richiesta assistenza a Numero Verde.
				viewWarningImportiAgevolazioni = "true";
			}
				
				//MB prova if (("true").equals(input._progetto_spese_quota_inseribile)) {
				if (("true").equals(((CultFormaAgevolazioneInput)getInput())._progetto_spese_quota_inseribile)) {//MBprova
				
					percQuotaParteSpeseGenFunz = "0,00";
					//MBprova CultPianoSpeseVO pianoSpeseMap = (CultPianoSpeseVO)input._pianoSpese;
					CultPianoSpeseVO pianoSpeseMap = (CultPianoSpeseVO)((CultFormaAgevolazioneInput)getInput())._pianoSpese;//MBprova
					if(pianoSpeseMap!=null){
						quotaParte = pianoSpeseMap.getQuotaParte();
			        	if(!StringUtils.isBlank(quotaParte)){
							percQuotaParteSpeseGenFunz = quotaParte; // 0,00
						}
					}
				}
				
				//MBprova if ((input._importo_minimo_specifico!=null) && (!input._importo_minimo_specifico.isEmpty())) {
				if ((((CultFormaAgevolazioneInput)getInput())._importo_minimo_specifico!=null) && (!((CultFormaAgevolazioneInput)getInput())._importo_minimo_specifico.isEmpty())) {
						
					//MBprova DomandaNGVO _domanda = (DomandaNGVO)input._domanda;
					DomandaNGVO _domanda = (DomandaNGVO)((CultFormaAgevolazioneInput)getInput())._domanda;//MBprova 
					String codiceTipologiaBeneficiario = _domanda.getCodiceTipologiaBeneficiario();
					
					if (codiceTipologiaBeneficiario.equalsIgnoreCase("BICI")) importoMinimoErogabile = "3000,00";
				}
						
				//calcolo il totale delle entrate e la differenza spese - entrate
			      
				BigDecimal totaleEntrateBD = new BigDecimal(0);
					    
				//totale entrate    
				//MB prova CultVociEntrataVO vociEntrataMap = (CultVociEntrataVO)input._vociEntrata;
				CultVociEntrataVO vociEntrataMap = (CultVociEntrataVO)((CultFormaAgevolazioneInput)getInput())._vociEntrata;//MBprova
				if(vociEntrataMap!=null){
				   //ottengo il totale  come somma degli importi nella tabella delle voci di entrata (il totale non era affidabile perche' non sempre aggiornato, adesso forse lo sarebbe)
					if(vociEntrataMap.getVociEntrataScelteList()!=null){		  
						
						List<VoceEntrataItemVO> vociEntrataScelteList = Arrays.asList(vociEntrataMap.getVociEntrataScelteList());
				   	   
					    for(int i=0; i<vociEntrataScelteList.size(); i++){	           
					    	VoceEntrataItemVO curVoce = (VoceEntrataItemVO) vociEntrataScelteList.get(i); 
			                if(curVoce!=null){
			        	        String importo = curVoce.getImporto(); // 12078,00
			        	        if(!StringUtils.isBlank(importo)){
			        		       totaleEntrateBD = totaleEntrateBD.add(new DecimalFormat().getBigDecimalFromString(curVoce.getImporto(), false));
			        	        }
			                }//chiude test null su curVoce	          
				        }//chiude il for
				          		          
				          String totaleEntrateStr = totaleEntrateBD.toString(); // 100000,00
				          if(totaleEntrateStr.indexOf(".") != -1){
					         totaleEntrateStr = totaleEntrateStr.replace(".",",");
				          }	
				          totaleEntrate = DecimalFormat.decimalFormat(totaleEntrateStr,2);
			       }			   
				}
	            
	            //MB prova CultFormaAgevolazioneVO _formaAgevolazione = (CultFormaAgevolazioneVO) input._formaAgevolazione; 			   
				CultFormaAgevolazioneVO _formaAgevolazione = (CultFormaAgevolazioneVO) ((CultFormaAgevolazioneInput)getInput())._formaAgevolazione; //MBprova
				
				if(_formaAgevolazione!=null){
				   importoRichiesto = DecimalFormat.decimalFormat((String)_formaAgevolazione.getImportoRichiesto(), 2); // 30000,00
	            } 
				
				//MB prova CultPianoSpeseVO _pianoSpese = (CultPianoSpeseVO) input._pianoSpese; 
				CultPianoSpeseVO _pianoSpese = (CultPianoSpeseVO) ((CultFormaAgevolazioneInput)getInput())._pianoSpese; //MBprova 
				if(_pianoSpese!=null){ 			    
				   speseConnesseAttivita = DecimalFormat.decimalFormat(_pianoSpese.getSpeseConnesseAttivita(),2); 	// 123097,00
				   speseGeneraliEFunz = DecimalFormat.decimalFormat(_pianoSpese.getSpeseGeneraliEFunz(),2);			//  10150,00
				   if(StringUtils.isBlank(speseConnesseAttivita)){
				       speseConnesseAttivita = "0,00";
				   }
				   if(StringUtils.isBlank(speseGeneraliEFunz)){
				       speseGeneraliEFunz = "0,00";
				   }
				}
				
				try{
				   BigDecimal percQuotaParteBD = new BigDecimal(percQuotaParteSpeseGenFunz.replace(',', '.'));	//    75 %
				   BigDecimal speseGeneraliEFunzBD = new BigDecimal(speseGeneraliEFunz.replace(',', '.'));		// 10150.00
					   	
				   BigDecimal speseGeneraliEFunzQPBD = speseGeneraliEFunzBD.multiply(percQuotaParteBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP); // 7612.50
				   speseGeneraliEFunzQP = speseGeneraliEFunzQPBD.toString().replace('.', ','); // 0,91
				   
				   BigDecimal speseConnesseAttivitaBD = new BigDecimal(speseConnesseAttivita.replace(',', '.'));	// 123097.00
				   BigDecimal totaleSpeseEffettiveBD = speseConnesseAttivitaBD.add(speseGeneraliEFunzQPBD).setScale(2, RoundingMode.HALF_UP); // 130709.50
				   totaleSpeseEffettive = totaleSpeseEffettiveBD.toString().replace('.', ',');
				   
				   totaleEntrateBD = new BigDecimal(totaleEntrate.replace(',', '.')); // 91709.00
				   BigDecimal differenzaEUBD = speseConnesseAttivitaBD.add(speseGeneraliEFunzQPBD).subtract(totaleEntrateBD).setScale(2, RoundingMode.HALF_UP); // 39000.50 ***
				   logger.info("[CultFormaAgevolazione::inject] differenzaEUBD: "+ differenzaEUBD+ " *** ");
				   
				   differenzaEU = MetodiUtili.roundBDToString(differenzaEUBD.toString()); // 39001.00
				   logger.info("[CultFormaAgevolazione::inject] differenzaEU "+ differenzaEU+ " *** "); // 80001.00
				   
				   BigDecimal diffEUBD = new BigDecimal(differenzaEU); // 80001.00
				   logger.debug("[CultFormaAgevolazione::inject] diffEUBD "+ diffEUBD);
				   differenzaEUBD = diffEUBD; // 180001.00
				   
				   BigDecimal percMassimaContributoErogabileBD = new BigDecimal(percMassimaContributoErogabile.replace(',', '.'));	// 50.00		   
				   //calcolo l'importo erogabile
				   percMassimaContributoErogabileBD = new DecimalFormat().getBigDecimalFromString(percMassimaContributoErogabile, false);
				   //BigDecimal importoErogabileCalcolatoBD = differenzaEUBD.multiply(percMassimaContributoErogabileBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
				   BigDecimal tmpImportoErogabileCalcolatoBD = totaleSpeseEffettiveBD.multiply(percMassimaContributoErogabileBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
				   
				   /** : Eseguire arrotondamento - -------------------------------------------------------------- inizio -  */
				   String tmpImportoErogabileCalcolatoStr = tmpImportoErogabileCalcolatoBD.toString(); 				// 90000.46
				   BigDecimal importoErogabileCalcolatoBD = MetodiUtili.roundBD(tmpImportoErogabileCalcolatoStr);	// 90000.00
				   logger.debug("debug: " + importoErogabileCalcolatoBD);
				   
				   BigDecimal minBD = new BigDecimal(0);
				   if(!StringUtils.isBlank(importoMassimoErogabile)){ // 30000,00
				       BigDecimal importoMassimoErogabileBD = new DecimalFormat().getBigDecimalFromString(importoMassimoErogabile, false);			   
				       
				       if(importoMassimoErogabileBD.compareTo(differenzaEUBD)==1){ // 30000.00 == 20000.00
				       		minBD = differenzaEUBD;			          
				       }else{			          
				            minBD = importoMassimoErogabileBD; // 30000.00
				       }
				       
				       if(minBD.compareTo(importoErogabileCalcolatoBD)>=0){
				          importoErogabile = importoErogabileCalcolatoBD.toString().replace('.', ',');
				       }else{			          
				          importoErogabile = minBD.toString().replace('.', ','); // 30000,00
				       }			       			 
				       	  
				   }else{
				      importoErogabile = importoErogabileCalcolatoBD.toString().replace('.', ',');
				   }			   
				}catch(Exception e){
	               speseGeneraliEFunzQP = "0,00";	
	               totaleSpeseEffettive	= "0,00";
	               differenzaEU	= "0,00";
	               importoErogabile	= "0,00";
	               importoRichiesto	= "0,00";
				}
				
				String importoErogabileXml = CultFormaAgevolazioneDAO.getImportoErogabileFromXml(info.getStatusInfo().getNumProposta(), logger);// null
			
					if(!StringUtils.isBlank(importoErogabileXml) && !importoErogabileXml.equals(importoErogabile)){ 
						viewWarningAgevolazioni = "true";
					}
					
					//MBprova if (("true").equals(input._progetto_spese_quota_inseribile)) {
					if (("true").equals( ((CultFormaAgevolazioneInput)getInput())._progetto_spese_quota_inseribile)) {//MBprova
					String quotaParteXml = PianoSpeseDAO.getQuotaParte(info.getStatusInfo().getNumProposta()); // 7
					
					if(!StringUtils.isBlank(quotaParteXml) && !quotaParteXml.equals(percQuotaParteSpeseGenFunz)){ 
						viewWarningAgevolazioni = "true";
					}
				}
			}

			//// namespace			
			output.viewWarningAgevolazioni=viewWarningAgevolazioni; // false
			
			/** Jira: 1337 ---  inizio */
			output.viewWarningImportiAgevolazioni=viewWarningImportiAgevolazioni; // false
			
			output.percQuotaParteSpeseGenFunz=percQuotaParteSpeseGenFunz;	// 0,00
			output.percMassimaContributoErogabile=percMassimaContributoErogabile;	// 50,00
			
			output.importoMinimoErogabile=importoMinimoErogabile; 	// 5000,00
			
			output.importoMassimoErogabile=importoMassimoErogabile; // 	30000,00
			output.speseConnesseAttivita=speseConnesseAttivita; 	//      0,00
			output.speseGeneraliEFunz=speseGeneraliEFunz;			// 	    0,00 
			output.speseGeneraliEFunzQP=speseGeneraliEFunzQP;				// 		0,00
			output.totaleSpeseEffettive=totaleSpeseEffettive;				//      0,00
			output.totaleEntrate=totaleEntrate;								// 100000,00
			output.differenzaEU=differenzaEU.toString().replace(".", ",");       // 0,00
			output.importoErogabile=importoErogabile;							//  0,00
			output.importoRichiesto=importoRichiesto;							//  0,00
			
		}catch (NullPointerException npe) {
			logger.error("[CultFormaAgevolazione::inject] Error ", npe);
		}catch (Exception e) {
			logger.error("[CultFormaAgevolazione::inject] Error ", e);
			throw new CommonalityException(e);
		}finally {
			logger.info("[CultFormaAgevolazione::inject] END");
		}
		return output;
	}
	

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) arg0;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		logger.info("[CultFormaAgevolazione::modelValidate] _formaAgevolazione BEGIN");

		//// validazione panel Forma di finanziamento

		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo è obbligatorio";
		String ERRMSG_IMPORTO_FORMATO = "- il valore deve essere numerico, maggiore di zero e senza decimali";        
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_EROGABILE = "- l'importo richiesto non può essere superiore all'importo erogabile";
		String ERRMSG_IMPORTO_EROGABILE_INF_MINIMO_EROGABILE = "- non è stato raggiunto il contributo minimo erogabile previsto dal bando";  
		String ERRMSG_IMPORTO_RICHIESTO_INF_MINIMO_EROGABILE = "- l'importo richiesto deve essere almeno pari all'importo minimo erogabile previsto per il bando";

		CultFormaAgevolazioneVO cultFormaAgevolazioneMap = input._formaAgevolazione; 			   
//		if(cultFormaAgevolazioneMap!=null && !cultFormaAgevolazioneMap.isEmpty()) {
		if(cultFormaAgevolazioneMap!=null) {
				      
		    //il campo "importoRichiesto" e' obbligatorio e numerico positivo
		    String importoRichiesto = (String)cultFormaAgevolazioneMap.getImportoRichiesto(); // 30000,00
		    String importoErogabile = (String)cultFormaAgevolazioneMap.getImportoErogabile();	
		    
		    
		    String importoMinimoErogabile = (String)cultFormaAgevolazioneMap.getImportoMinimoErogabile();
		    
		    BigDecimal importoErogabileBD = new BigDecimal(0);  // 10005.25           
			
			BigDecimal importoMinimoErogabileBD = new BigDecimal(0); 
			
	        if(!StringUtils.isBlank(importoMinimoErogabile)){     
				importoMinimoErogabile = importoMinimoErogabile.replace(',', '.');
				importoMinimoErogabileBD = new BigDecimal(importoMinimoErogabile);	
			}  
	            
		    if(StringUtils.isNotBlank(importoErogabile) ) // 7922,33
		    {
		       if(!importoErogabile.matches("^\\d+(,\\d{1,2})?$") || Utils.isZero(importoErogabile))
		       {
		    	  logger.info("[CultFormaAgevolazione::modelValidate] - il valore deve essere numerico, maggiore di zero e senza decimali");			
		 	      addMessage(newMessages,"_formaAgevolazione_importoErogabile", ERRMSG_IMPORTO_FORMATO);
		 	   } else {
		 		   	/* ----------------------------------------- : Esegui controllo round importoErogabile - inizio - */ 	   		 
						importoErogabile = importoErogabile.replace(',', '.'); // 7922,33
						importoErogabileBD = MetodiUtili.roundBD(importoErogabile); // 7922.00
						importoErogabileBD = new BigDecimal(importoErogabileBD.toString());		
						
					if(importoErogabileBD.compareTo(importoMinimoErogabileBD)==-1){                 
						logger.info("[CultFormaAgevolazione::modelValidate] l'importo erogabile non può essere inferiore all'importoMinimoErogabile");
						addMessage(newMessages,"_formaAgevolazione_importoErogabile", ERRMSG_IMPORTO_EROGABILE_INF_MINIMO_EROGABILE); 
					}
		 	   }
		 	}  
		 	  
		    if(StringUtils.isNotBlank(importoRichiesto) )
		    {
	    	/* ----------------------------------------- : Esegui controllo validita dei decimali - inizio - */
		    	isDecimaliUguali= MetodiUtili.confrontaDecimaliUguali(importoRichiesto, logger);
		    	
		    	
		 	  if(!importoRichiesto.matches("^\\d+(,\\d{1,2})?$") || Utils.isZero(importoRichiesto) || !isDecimaliUguali){
		 		 logger.info("[CultFormaAgevolazione::modelValidate] - il valore deve essere numerico, maggiore di zero e senza decimali ");			
		 	      addMessage(newMessages,"_formaAgevolazione_importoRichiesto", ERRMSG_IMPORTO_FORMATO);
		 	  } else {
				// importoRichiesto non deve superare l'importo erogabile
				
	            BigDecimal importoRichiestoBD = new BigDecimal(0);   
	            importoRichiesto = importoRichiesto.replace(',', '.');
	            importoRichiestoBD = new BigDecimal(importoRichiesto);
	                               
	            if(importoRichiestoBD.compareTo(importoErogabileBD)==1){                 
	            	logger.info("[CultFormaAgevolazione::modelValidate] l'importo richiesto non può essere superiore all'importoErogabile");
	              addMessage(newMessages,"_formaAgevolazione_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_EROGABILE); 
	            } else {
	            	if(importoRichiestoBD.compareTo(importoMinimoErogabileBD)==-1){                 
	            		logger.info("[CultFormaAgevolazione::modelValidate] l'importo richiesto non può essere inferiore all'importoMinimoErogabile");
						addMessage(newMessages,"_formaAgevolazione_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_INF_MINIMO_EROGABILE); 
					} 
	            }	
		 	  }
		    }else{
		    	logger.info("[CultFormaAgevolazione::modelValidate] _formaAgevolazione.importoRichiesto non presente o vuoto");		
				addMessage(newMessages,"_formaAgevolazione_importoRichiesto", ERRMSG_CAMPO_OBBLIGATORIO);
		    }
				     
	     }else{
	    	 logger.info("[CultFormaAgevolazione::modelValidate] _formaAgevolazione non presente o vuoto");
	     }
	
		logger.info("[CultFormaAgevolazione::modelValidate] _formaAgevolazione END");
	
		return newMessages;
	}
}
