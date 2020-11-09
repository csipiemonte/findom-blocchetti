/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB;

import it.csi.findom.blocchetti.blocchetti.cultVociEntrata.CultVociEntrataVO;
import it.csi.findom.blocchetti.blocchetti.cultVociEntrata.VoceEntrataItemVO;
import it.csi.findom.blocchetti.common.dao.PianoSpeseDAO;
import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.vo.cultPianospese.CultPianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.pianospese.ImportoPianoSpeseVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class CultFormaAgevolazioneB extends Commonality {
	
	CultFormaAgevolazioneBInput input = new CultFormaAgevolazioneBInput();
	/* ----------------------------------------- : Esegui controllo round importoErogabile - inizio - */
	boolean isDecimaliUguali = false;
	
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info1, List<CommonalityMessage> messages) throws CommonalityException {
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	 /**************************/
	/**       Inject          */
   /**************************/
	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> messages) throws CommonalityException {
		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[CultFormaAgevolazioneB::inject] _cultFormaAgevolazioneB BEGIN");
		CultFormaAgevolazioneBOutput output = new CultFormaAgevolazioneBOutput();
		
		/*** jira 2040 - */
		String viewInfoSaldoContabile = "true";
		
		try 
		{
			//// dichiarazioni 
	        String viewWarningAgevolazioni = "false";
	        
	        /**********************************************
	         * Jira: 1337 : 
	         * Visualizzare messaggio a video
	         * se non vi sono importi configurati in alcuna
	         * tabella predisposta su database.
	         * 
	         ******************************************** */
			String viewWarningImportiAgevolazioni = "false";
			boolean visualizzaMsg = false; // Visualizza msg : Contattare Numero Verde per Assistenza tecnica.
			
			int cntVociEntrata = 0;
			String entratePreviste = "false";
			
			String totaleEntrate = "0,00"; 
			String speseConnesseAttivita = "0,00";
	        String speseGeneraliEFunz = "0,00";        	
	        String speseGeneraliEFunzQP = "0,00";
	        String totaleSpeseEffettive = "0,00";
	        String differenzaEU = "0,00";        
	        String importoRichiesto = "0,00"; 
	        
	        /**  - gestione nuovo campo per sistema neve B1 */
	        String percentualeContributoErogabile = "0,00"; 
	        
	        String percQuotaParteSpeseGenFunz = "100,00";
			String percMassimaContributoErogabile = "100,00";
			String importoMinimoErogabile = "0,00";
			
			String importoMassimoErogabile = "0,00";
		    String importoErogabile = "0,00";
		    
		    String quotaParte = "0,00";
		    
		    /**  Sistema neve B1 :: verifico se bando prevede spese generali di funzionamento */
			String _spese_generali_funzionamento_previste = "false";
			String campiNascosti = "false";
			
		    //legge l'importo erogabile salvato sull'xml
			String campoNascostoTotaleEntrate = "false";
		    
		    BigDecimal importoRichiestoBD = new BigDecimal("0.00");
		
		    /** jira 2009 - */
		    BigDecimal contributoMaxUltimiTreAnni = new BigDecimal(0.00);
		    BigDecimal saldoContabilePrevistoBD = new BigDecimal(0.00);
		    BigDecimal importoErogabileBD= new BigDecimal(0.00);
		    String saldoContabilePrevisto = "0,00";
		    BigDecimal importoMassimoErogabileBD = new BigDecimal(0.00);
		    
		    /**
			 * valorizzazione
			 */
		    
			if (info.getCurrentPage() != null) 
			{	
				
				
				/** jira 2040 - eventuale visualizzazione del messaggio informativo a video - */
				try {			
					if(SessionCache.getInstance().get("hideInfoSaldoContabile")!=null){
						String hideInfoSaldoContabile = (String)SessionCache.getInstance().get("hideInfoSaldoContabile");
						if(!StringUtils.isBlank(hideInfoSaldoContabile) && hideInfoSaldoContabile.equals("true")){
							viewInfoSaldoContabile = "false";
							SessionCache.getInstance().set("hideInfoSaldoContabile", "");
						}
					}
				} catch (Exception e) {
					logger.error("[CultFormaAgevolazioneConSaldo::outputCustomField] Errore nella valorizzazione di viewInfoSaldoContabile ", e);
				}
				
				
				String idBando = info.getStatusInfo().getTemplateId()+"";
				logger.info("debug-83: idBando:: " + idBando);
				
				ParametriCalcoloBVO parametriCalcoloMap = null;
				
				String tmpPercMassimaContributoErogabile = "";
				String tmpImportoMinimoErogabile = "0,00";
				String tmpImportoMassimoErogabile = "";
				
				ImportoPianoSpeseVO importiParam = new ImportoPianoSpeseVO();
				Integer idDomanda = info.getStatusInfo().getNumProposta();
				logger.info("idDomanda: " + idDomanda);
				
				String tmpPercQuotaParteSpeseGenFunz = "";
				
				/** jira 2009 */
				String tmpImportoUltimiTreAnni = "";
					
				
				/**  Sistema neve B1 :: Verifico presenza variabile di configurazione bando das sportello */
				boolean isCultFrmAgvSportello 	= false;
				
				
				boolean isPrjSpQuotaInseribile 	= false;
				String campoNascostoQuotaParteInQuota = "false";
				
				String dataInvio = "";
				
				Integer idCategVoceSpesa = 2;
				
				Integer int_idBando = Integer.parseInt(idBando);
				logger.info("int_idBando vale: " + int_idBando);
				
				if (info.getStatusInfo().getDataTrasmissione() != null) {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
					logger.info("dataInvio vale: " + dataInvio);
				}
				
				// --------------------------------------------------------------------------  I step
				logger.info("I step: verifico stato spese_geneali_funzionamento_previste...");
				int cntNumeroVociSpesa = 0;
				
				cntNumeroVociSpesa = MetodiUtili.getTotVociSpesaSNB1(int_idBando, idCategVoceSpesa, dataInvio, logger);
				
				if(cntNumeroVociSpesa > 0){
					_spese_generali_funzionamento_previste = "true";
					
					logger.info("_spese_generali_funzionamento_previste vale: " + _spese_generali_funzionamento_previste);
				}else{
					logger.info("_spese_generali_funzionamento_previste vale: " + _spese_generali_funzionamento_previste);
					logger.info("... il recupero della percentuale in quota parte deve essere nascosto e valere (vuoto) ");
					campoNascostoQuotaParteInQuota = "true";
					logger.info("... campoNascostoQuotaParteInQuota nascosto ? " + campoNascostoQuotaParteInQuota);
					campiNascosti= "true";
					
				}
				
				
				// --------------------------------------------------------------------------  II step
				logger.info("IV step: verifico variabile presente di cfg bando: _progetto_spese_quota_inseribile...");

				if (("true").equals(((CultFormaAgevolazioneBInput)getInput())._progetto_spese_quota_inseribile)) {
				
					percQuotaParteSpeseGenFunz = "0,00";
					isPrjSpQuotaInseribile=true;
					logger.info(" bando configurato per visualizzare e recuperare dati percentuale in quota parte compilata da utente... " );

					CultPianoSpeseVO pianoSpeseMap = ((CultFormaAgevolazioneBInput)getInput())._pianoSpese;
					if(pianoSpeseMap!=null){
						quotaParte = pianoSpeseMap.getQuotaParte();
			        	if(!StringUtils.isBlank(quotaParte)){
							percQuotaParteSpeseGenFunz = quotaParte; // 0,00
						}
					}
				}else{
					logger.info(" bando non risulta configurato per visualizzare e recuperare dati di percentuale in quota parte ... " );
					logger.info(" Campo verra' nascosto e salvato in campo hidden su xml  ... " );
					percQuotaParteSpeseGenFunz = "";
					logger.info(" Campo percentuale-quota-parte nascosto e salvato in campo hidden su xml vale: " + percQuotaParteSpeseGenFunz );
				}
				
				// --------------------------------------------------------------------------  II step
				logger.info("II step: verifico stato variabile di cfg _cult_forma_agv_cfg_da_sportello...");
				logger.info(" il bando presenta variabile di configurazione: cult_forma_agv_cfg_da_sportello ?" );
				if (("true").equals(((CultFormaAgevolazioneBInput)getInput())._cult_forma_agv_cfg_da_sportello))
				{
					isCultFrmAgvSportello=true;
					importiParam = CultFormaAgevolazioneBDAO.getImportiPianoSpese(idDomanda, logger); // parametri di calcolo recuperati da funzione
					
					/** jira 2009 | 2144 - */
					if ("true".equals(input._cultFormaAgvB_cntb_max_ultimi_tre_anni))
					{
						// recupero cf beneficiario
						String codFiscaleBeneficiario = info.getStatusInfo().getCodFiscaleBeneficiario();
						logger.info(" codFiscaleBeneficiario risulta: " + codFiscaleBeneficiario );
						logger.debug("idBando="+idBando);
						
						// idSoggettoAbilitato
						String idSoggettoAbilitato = "";
						idSoggettoAbilitato = CultFormaAgevolazioneBDAO.getIdSoggettoAbilitato(codFiscaleBeneficiario, logger);
						logger.info("idSoggettoAbilitato="+idSoggettoAbilitato);
						
						if(idSoggettoAbilitato != null && !idSoggettoAbilitato.isEmpty()){
							
							Integer int_IdSoggettoAbilitato = Integer.parseInt(idSoggettoAbilitato);
							logger.info("... int_IdSoggettoAbilitato risulta formato Integer: "+int_IdSoggettoAbilitato);
							
							Integer int_IdBando = Integer.parseInt(idBando);
							logger.info("... int_IdBando risulta formato Integer: "+int_IdBando);
							
							OperatorePresentatoreVo operatorePresentatoreVO = input.operatorePresentatoreVO;
							boolean isPresentIdDipartimento = false;
							
							// recupera idDipartimento selezionato
							String idDipartimento = "";
							if (operatorePresentatoreVO != null) {
								
								if(StringUtils.isNotBlank(operatorePresentatoreVO.getIdDipartimento())){
									idDipartimento = operatorePresentatoreVO.getIdDipartimento();
									logger.info(" idDipartimento=" + idDipartimento );
									isPresentIdDipartimento = true;
								}else{
									logger.info(" idDipartimento risulta assente");
								}
							}
							
							List<CultFormaAgevolazioneBVO> importiList = new ArrayList<CultFormaAgevolazioneBVO>(); 
							importiList = CultFormaAgevolazioneBDAO.getImportiList(int_IdBando, int_IdSoggettoAbilitato, logger);

							logger.info("... numRecordsPerBeneficiario: " + importiList.size());
							if(importiList.size() ==1){
								tmpImportoUltimiTreAnni = getImportoPerBeneficiario(idSoggettoAbilitato, importiList, logger);
							} 
							else if(importiList.size()>1){
								// se importi multipli, cerco il contributo per: id_bando, codice_fiscale, id_sogg_abil, id_dipartimento
								tmpImportoUltimiTreAnni = getImportoByIdDipartimento( idSoggettoAbilitato, isPresentIdDipartimento, idDipartimento, importiList, logger);
							}
							else{
								// versione nessun record trovato
								// query std
								logger.info("!!! - Attenzione ... idDipartimento non trovato !!! - ( NON dovrebbe mai verificarsi) ... ");
								tmpImportoUltimiTreAnni = CultFormaAgevolazioneBDAO.getImportoContributoUltimoTriennio(int_IdBando, int_IdSoggettoAbilitato, logger);
								logger.info("debug: Situazione std, singolo  importo: " + tmpImportoUltimiTreAnni);
							}
							logger.info("debug: to continue...");
							
							if(tmpImportoUltimiTreAnni != null && !tmpImportoUltimiTreAnni.isEmpty()){

								if(tmpImportoUltimiTreAnni.indexOf(',')!=-1){
									tmpImportoUltimiTreAnni = tmpImportoUltimiTreAnni.replace(',','.');
									logger.info("... tmpImportoUltimiTreAnni: "+tmpImportoUltimiTreAnni);
						        }
								
								contributoMaxUltimiTreAnni = new BigDecimal(tmpImportoUltimiTreAnni);
								logger.info("... importoMaxCntrUltimiTreAnni: "+contributoMaxUltimiTreAnni);
							}
						}
					}
					/** jira 2009 - */
					
					
					// Se tutti e 3 gli importi sono assenti
					if(importiParam.getImportoMassimoErogabile() == null && importiParam.getImportoMinimoErogabile() == null && importiParam.getPercMaxContributoErogabile() == null){
						visualizzaMsg = true; // visualizza messaggio
					}else{
						
						/***************************************************
						 * :  dati-3
						 * *************************************************
						 * call function postgresql
						 * Dati da recuperare tramite funzione:
						 * 
						 * importoMassimoErogabile
						 * importoMinimoErogabile
						 * percMassimaContributoErogabile
						 * *************************************************/
						
						if (isPrjSpQuotaInseribile) 
						{
							if (parametriCalcoloMap.getPercQuotaParteSpeseGenFunz() != null) 
							{
								tmpPercQuotaParteSpeseGenFunz = parametriCalcoloMap.getPercQuotaParteSpeseGenFunz().replace(".", ",");
								logger.info("debug-93: tmpPercQuotaParteSpeseGenFunz:: " + tmpPercQuotaParteSpeseGenFunz);
							}
						}else{
							tmpPercQuotaParteSpeseGenFunz = percQuotaParteSpeseGenFunz;
							logger.info("debug-93: tmpPercQuotaParteSpeseGenFunz:: " + tmpPercQuotaParteSpeseGenFunz);
						}
						
						/* Percentuale massima contributo erogabile originale */
						tmpPercMassimaContributoErogabile = String.valueOf(importiParam.getPercMaxContributoErogabile()).replace(".",",");
						logger.info("debug-96: tmpPercMassimaContributoErogabile:: " + tmpPercMassimaContributoErogabile); // 70
						
						
						/* Importo minimo erogabile */
						tmpImportoMinimoErogabile = (String.valueOf(importiParam.getImportoMinimoErogabile())).replace(".",",");
						logger.info("debug-99: tmpImportoMinimoErogabile:: " + tmpImportoMinimoErogabile); // 2000,00
						
						
						/* Importo massimo erogabile */
						tmpImportoMassimoErogabile = (String.valueOf(importiParam.getImportoMassimoErogabile())).replace(".",",");
						logger.info("debug-104: tmpImportoMassimoErogabile:: " + tmpImportoMassimoErogabile); // 120000,00
					
						
						/** -------------------------------------- Gestione decimali ---------------------------------------- */
					
						percQuotaParteSpeseGenFunz = DecimalFormat.decimalFormat(tmpPercQuotaParteSpeseGenFunz,2);
						logger.info("debug-107: percQuotaParteSpeseGenFunz:: " + percQuotaParteSpeseGenFunz); // vuoto
						
						/* Percentuale massima contributo erogabile */
						percMassimaContributoErogabile = DecimalFormat.decimalFormat(tmpPercMassimaContributoErogabile,2);
						logger.info("debug-110: percMassimaContributoErogabile:: " + percMassimaContributoErogabile); // 70,00
						
						/* Importo minimo erogabile */
						importoMinimoErogabile = DecimalFormat.decimalFormat(tmpImportoMinimoErogabile,2);
						logger.info("debug-113: importoMinimoErogabile:: " + importoMinimoErogabile); // 2000,00
						
						/* Importo massimo erogabile */
						importoMassimoErogabile = DecimalFormat.decimalFormat(tmpImportoMassimoErogabile,2);	// 120000,00
						logger.info("debug-137: importoMassimoErogabile:: " + importoMassimoErogabile); //
					
					}
					
				} else {
					
					/**
					 * Gestione bandi-cultura vecchi...
					 * dati-5
					 */
						parametriCalcoloMap = CultFormaAgevolazioneBDAO.getParametriCalcolo(idBando); // recupero parametri calcolo da findom_t_bandi
						logger.info("parametriCalcoloMap:: " + parametriCalcoloMap.toString()); // 100,00
						
						// Se tutti e 3 gli importi sono assenti
						if(parametriCalcoloMap.getImportoMassimoErogabile() == null && parametriCalcoloMap.getImportoMinimoErogabile() == null && parametriCalcoloMap.getPercMassimaContributoErogabile() == null){
							visualizzaMsg = true; // Visualizza messaggio di assistenza Numero verde
						}
						
						tmpPercQuotaParteSpeseGenFunz = (parametriCalcoloMap.getPercQuotaParteSpeseGenFunz()).replace(".",",");
						logger.info("debug-93: tmpPercQuotaParteSpeseGenFunz:: " + tmpPercQuotaParteSpeseGenFunz); // 100,00
						
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
						
				}
				
				// se nessuno importo recuperato da database, visualizzare msg
				if (visualizzaMsg) {
					// a video per richiesta assistenza a Numero Verde.
					viewWarningImportiAgevolazioni = "true";
				}
					
				if ((((CultFormaAgevolazioneBInput)getInput())._importo_minimo_specifico!=null) && (!((CultFormaAgevolazioneBInput)getInput())._importo_minimo_specifico.isEmpty())) {
						
					DomandaNGVO _domanda = ((CultFormaAgevolazioneBInput)getInput())._domanda;
					String codiceTipologiaBeneficiario = _domanda.getCodiceTipologiaBeneficiario();
					
					if (codiceTipologiaBeneficiario.equalsIgnoreCase("BICI")) importoMinimoErogabile = "3000,00";
				}
							
				//calcolo il totale delle entrate e la differenza spese - entrate
			     
				BigDecimal totaleEntrateBD = new BigDecimal(0);
				BigDecimal totaleSpeseEffettiveBD = new BigDecimal(0);  
				
				
				
				//  - inizio: Sistema neve B1: verifico se sono presenti voci di entrata per bando corrente
				cntVociEntrata = MetodiUtili.getTotVociEntrataSNB1(int_idBando, dataInvio, logger);
				
				if(cntVociEntrata > 0)
				{
					entratePreviste = "true";
					logger.info("entratePreviste vale: " + entratePreviste);
					
					// Recupera dati dal tab omonimo:
					//totale entrate    
					CultVociEntrataVO vociEntrataMap = ((CultFormaAgevolazioneBInput)getInput())._vociEntrata;
					if(vociEntrataMap!=null){
					   //ottengo il totale  come somma degli importi nella tabella delle voci di entrata (il totale non era affidabile perche' non sempre aggiornato, adesso forse lo sarebbe)
						if(vociEntrataMap.getVociEntrataScelteList()!=null){		  
							
							List<VoceEntrataItemVO> vociEntrataScelteList = Arrays.asList(vociEntrataMap.getVociEntrataScelteList());
					   	   
						    for(int i=0; i<vociEntrataScelteList.size(); i++){	           
						    	VoceEntrataItemVO curVoce = vociEntrataScelteList.get(i); 
				                if(curVoce!=null){
				        	        String importo = curVoce.getImporto(); // 12078,00
				        	        logger.info("importo voci entrate vale: " + importo);
				        	        if(!StringUtils.isBlank(importo)){
				        		       totaleEntrateBD = totaleEntrateBD.add(new DecimalFormat().getBigDecimalFromString(curVoce.getImporto(), false));
				        		       logger.info("importo voci entrate in BigDecimal vale: " + totaleEntrateBD);
				        	        }
				                }//chiude test null su curVoce	          
					        }//chiude il for
					          		          
					          String totaleEntrateStr = totaleEntrateBD.toString(); // 100000,00
					          if(totaleEntrateStr.indexOf(".") != -1){
						         totaleEntrateStr = totaleEntrateStr.replace(".",",");
					          }	
					          totaleEntrate = DecimalFormat.decimalFormat(totaleEntrateStr,2);
					          logger.info("totaleEntrate in DecimalFormat vale: " + totaleEntrate);
				       }			   
					}
					
				} else {
					logger.info("entratePreviste vale: " + entratePreviste);
					campoNascostoTotaleEntrate = "true";
					logger.info("... il campo C. Totale delle entrate dovra' risultare nascosto: ? " + campoNascostoTotaleEntrate);
					totaleEntrate = "0,00";
					logger.info("... C. Totale delle entrate risulta : " + totaleEntrate);
				}
	            
				CultFormaAgevolazioneBVO _formaAgevolazioneB = ((CultFormaAgevolazioneBInput)getInput())._formaAgevolazioneB;
				
				if(_formaAgevolazioneB!=null){
				   importoRichiesto = DecimalFormat.decimalFormat(_formaAgevolazioneB.getImportoRichiesto(), 2); 
				   logger.info("importoRichiesto: "+ importoRichiesto+ " *** "); // 35000,00
				}
				
				CultPianoSpeseVO _pianoSpese = ((CultFormaAgevolazioneBInput)getInput())._pianoSpese; 
				if(_pianoSpese!=null){ 			    
				   speseConnesseAttivita = DecimalFormat.decimalFormat(_pianoSpese.getSpeseConnesseAttivita(),2); 	
				   logger.info("[CultFormaAgevolazioneB::inject] speseConnesseAttivita: "+ speseConnesseAttivita+ " *** "); // 710901,00
				   
				   if (_spese_generali_funzionamento_previste.equals("true")) 
				   {
					   speseGeneraliEFunz = DecimalFormat.decimalFormat(_pianoSpese.getSpeseGeneraliEFunz(), 2);
					   logger.info("[CultFormaAgevolazioneB::inject] speseGeneraliEFunz: " + speseGeneraliEFunz + " *** ");
					   
					   if(StringUtils.isBlank(speseGeneraliEFunz)){
					       speseGeneraliEFunz = "0,00";
					   }
				   }else{
					   speseGeneraliEFunz = "";
					   logger.info("[CultFormaAgevolazioneB::inject] speseGeneraliEFunz: " + speseGeneraliEFunz + " *** ");
					   logger.info(" il campo speseGeneraliEFunz deve essere nascosto ");
				   }
				   
				   if(StringUtils.isBlank(speseConnesseAttivita)){
				       speseConnesseAttivita = "0,00";
				   }
				}
					
				try{
					
					if (isPrjSpQuotaInseribile && _spese_generali_funzionamento_previste.equals("true")) 
					{
					   logger.info("calcoli: percQuotaParteSpeseGenFunz: "+ percQuotaParteSpeseGenFunz);
					   BigDecimal percQuotaParteBD = new BigDecimal(percQuotaParteSpeseGenFunz.replace(',', '.'));	
						
					   BigDecimal speseGeneraliEFunzBD = new BigDecimal(speseGeneraliEFunz.replace(',', '.'));		
						
					   BigDecimal speseGeneraliEFunzQPBD = speseGeneraliEFunzBD.multiply(percQuotaParteBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP); 
					   speseGeneraliEFunzQP = speseGeneraliEFunzQPBD.toString().replace('.', ','); 
					    
					   BigDecimal speseConnesseAttivitaBD = new BigDecimal(speseConnesseAttivita.replace(',', '.'));	
					   totaleSpeseEffettiveBD = speseConnesseAttivitaBD.add(speseGeneraliEFunzQPBD).setScale(2, RoundingMode.HALF_UP); 
					   totaleSpeseEffettive = totaleSpeseEffettiveBD.toString().replace('.', ',');
						   
					   totaleEntrateBD = new BigDecimal(totaleEntrate.replace(',', '.')); 
					   BigDecimal differenzaEUBD = speseConnesseAttivitaBD.add(speseGeneraliEFunzQPBD).subtract(totaleEntrateBD).setScale(2, RoundingMode.HALF_UP); 
					   logger.info("[CultFormaAgevolazioneB::inject] differenzaEUBD: "+ differenzaEUBD+ " *** ");
					   
					   differenzaEU = MetodiUtili.roundBDToString(differenzaEUBD.toString()); // 39001.00
					   logger.info("[CultFormaAgevolazioneB::inject] differenzaEU "+ differenzaEU+ " *** "); // 80001.00
					   
					   BigDecimal diffEUBD = new BigDecimal(differenzaEU); // 80001.00
					   logger.info("[CultFormaAgevolazioneB::inject] diffEUBD "+ diffEUBD);
					   differenzaEUBD = diffEUBD; // 180001.00
					   
					   BigDecimal percMassimaContributoErogabileBD = new BigDecimal(percMassimaContributoErogabile.replace(',', '.'));	// 50.00
					   
					   //calcolo l'importo erogabile
					   percMassimaContributoErogabileBD = new DecimalFormat().getBigDecimalFromString(percMassimaContributoErogabile, false);
					   BigDecimal tmpImportoErogabileCalcolatoBD = totaleSpeseEffettiveBD.multiply(percMassimaContributoErogabileBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
					   
					   /** : Eseguire arrotondamento - -------------------------------------------------------------- inizio -  */
					   String tmpImportoErogabileCalcolatoStr = tmpImportoErogabileCalcolatoBD.toString(); 				// 90000.46
					   BigDecimal importoErogabileCalcolatoBD = MetodiUtili.roundBD(tmpImportoErogabileCalcolatoStr);	// 90000.00
					   logger.info("debug: " + importoErogabileCalcolatoBD);
					   
					   BigDecimal minBD = new BigDecimal(0);
					   
					   if(!StringUtils.isBlank(importoMassimoErogabile))
					   { 
					       importoMassimoErogabileBD = new DecimalFormat().getBigDecimalFromString(importoMassimoErogabile, false);			   
					       
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
					   
					} else {
						
						/** calcoli condizioanti da spese_generali_funzionamento_previste a: false */
						logger.debug("calcoli condizioanti da spese_generali_funzionamento_previste a: false: ");
						BigDecimal percMassimaContributoErogabileBD = new BigDecimal(percMassimaContributoErogabile.replace(',', '.'));	// 50.00
						logger.info("percMassimaContributoErogabileBD risulta:  " + percMassimaContributoErogabileBD); // 70.00
						
						BigDecimal percQuotaParteBD = new BigDecimal(percQuotaParteSpeseGenFunz.replace("", "0.00"));	
						logger.info("percQuotaParteBD risultano:  " + percQuotaParteBD); // 0.00
						
					    BigDecimal speseGeneraliEFunzBD = new BigDecimal(speseGeneraliEFunz.replace("", "0.00"));		
					    logger.info("speseGeneraliEFunzBD risultano:  " + speseGeneraliEFunzBD); // 0.00
					    
					    BigDecimal speseGeneraliEFunzQPBD = speseGeneraliEFunzBD.multiply(percQuotaParteBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP); 
					    logger.info("speseGeneraliEFunzQPBD risultano:  " + speseGeneraliEFunzQPBD);
					    
					    speseGeneraliEFunzQP = speseGeneraliEFunzQPBD.toString().replace('.', ','); // 0.00
					    logger.info("speseGeneraliEFunzQP risultano:  " + speseGeneraliEFunzQP);
					    
					    BigDecimal speseConnesseAttivitaBD = new BigDecimal(speseConnesseAttivita.replace(',', '.'));	// 710901.00
					    logger.info("speseConnesseAttivitaBD risultano:  " + speseConnesseAttivitaBD);
					    
					    totaleSpeseEffettiveBD = speseConnesseAttivitaBD.add(speseGeneraliEFunzQPBD).setScale(2, RoundingMode.HALF_UP); 
					    logger.info("totaleSpeseEffettiveBD risultano:  " + totaleSpeseEffettiveBD);
					    
					    totaleSpeseEffettive = totaleSpeseEffettiveBD.toString().replace('.', ',');
					    logger.info("totaleSpeseEffettive risultano:  " + totaleSpeseEffettive); // 710901.00
					    
					    totaleEntrateBD = new BigDecimal(totaleEntrate.replace(',', '.')); 
					    logger.info("totaleEntrateBD: "+ totaleEntrateBD+ " *** "); // 455999.00
					    
					    BigDecimal differenzaEUBD = speseConnesseAttivitaBD.add(speseGeneraliEFunzQPBD).subtract(totaleEntrateBD).setScale(2, RoundingMode.HALF_UP); 
					    logger.info("differenzaEUBD: "+ differenzaEUBD+ " *** "); // 254902.00 
					   
					    differenzaEU = MetodiUtili.roundBDToString(differenzaEUBD.toString()); // deve risultare (A+B2) perche C=0 : 25001.00 ***
					    logger.info("[CultFormaAgevolazioneB::inject] differenzaEU "+ differenzaEU+ " *** "); // 254902.00
					   
					    BigDecimal diffEUBD = new BigDecimal(differenzaEU); 
					    logger.info("[CultFormaAgevolazioneB::inject] diffEUBD "+ diffEUBD); // 254902.00
					    differenzaEUBD = diffEUBD; 
					    logger.info("[differenzaEUBD versione bd "+ differenzaEUBD); // 254902.00
					    
					    //calcolo l'importo erogabile
						percMassimaContributoErogabileBD = new DecimalFormat().getBigDecimalFromString(percMassimaContributoErogabile, false);
						logger.info("[percMassimaContributoErogabileBD versione bd risulta: "+ percMassimaContributoErogabileBD); // 70.00
						
						//BigDecimal importoErogabileCalcolatoBD = differenzaEUBD.multiply(percMassimaContributoErogabileBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
						BigDecimal tmpImportoErogabileCalcolatoBD = totaleSpeseEffettiveBD.multiply(percMassimaContributoErogabileBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
						logger.info("[tmpImportoErogabileCalcolatoBD versione bd risulta: "+ tmpImportoErogabileCalcolatoBD);  // 710901.00 * 0.7 = 497630.70 ERRORE  ### ? 
						
						/** : Eseguire arrotondamento - -------------------------------------------------------------- inizio -  */
						String tmpImportoErogabileCalcolatoStr = tmpImportoErogabileCalcolatoBD.toString(); 	
						logger.info("tmpImportoErogabileCalcolatoStr versione stringa: " + tmpImportoErogabileCalcolatoStr); // 497630.70
						
						BigDecimal importoErogabileCalcolatoBD = MetodiUtili.roundBD(tmpImportoErogabileCalcolatoStr);	// 497630.70
						logger.info("importoErogabileCalcolatoBD: " + importoErogabileCalcolatoBD);
						
						importoErogabile = importoErogabileCalcolatoBD.toString().replace('.', ','); // 497631.00
						logger.info("importoErogabile risultano:  " + importoErogabile); // 497631,00
						logger.info("A. spese connesse all'attivita risultano:  " + speseConnesseAttivita);
						logger.info("B. spese genearali e di funzionamento risultano:  " + speseGeneraliEFunz); // hidden ""
						logger.info("B2. spese genearali e di funzionamento in quota parte risultano:  " + speseGeneraliEFunzQP); // hidden ""
						logger.info("Totale spese effettive risultano:  " + totaleSpeseEffettive); // 710901,00
						logger.info("C. Totale delle entrate risultano bigdecimal:  " + totaleEntrateBD); // 455999.00
						logger.info("C. Totale delle entrate risulta string:  " + totaleEntrate); // 455999,00
						
						
						/** jira 2009 - */
						if ("true".equals(input._cultFormaAgvB_cntb_max_ultimi_tre_anni))
						{
							// -------------------------------------------------------------- saldo contabile previsto
							logger.info("importoErogabile vale:  "+ importoErogabile);
							importoErogabileBD = calcolaImpErg1(speseConnesseAttivitaBD, percMassimaContributoErogabileBD, logger);
							logger.info("importoErogabileBD vale:  "+ importoErogabileBD);
							
							/**
							 *  calcolo importo minimo fra ::
							 *  - spese connesse alle attivita
							 *  - differenza fra spese connesse ad attivita e le entrate
							 *  - importo massimo erogabile previsto dal bando
							 *  - importo contributo-piu-elevato-ultimo-triennio
							 */
							BigDecimal importoMinBD = new BigDecimal(0);
							logger.info("... importoErogabileBD vale:  "		+importoErogabileBD); 	// 497630.70
							logger.info("... differenzaEUBD vale:  "			+differenzaEUBD);		// 254902.00
							
							if(importoMassimoErogabile!=null && !importoMassimoErogabile.isEmpty()){
								if(importoMassimoErogabile.indexOf(",") != -1){
									importoMassimoErogabile = importoMassimoErogabile.replace(",",".");
									logger.info("... importoMassimoErogabile vale:  "			+importoMassimoErogabile);
								}	
							}else{
								importoMassimoErogabile = "0";
							}
							
							importoMassimoErogabileBD = new BigDecimal(importoMassimoErogabile);
							logger.info("... importoMassimoErogabileBD vale:  "	+importoMassimoErogabileBD); // 120000.00
							logger.info("... contributoMaxUltimiTreAnni vale: " +contributoMaxUltimiTreAnni); // 35000.00
							
							
							
							
							logger.info("******************************************************************");
							logger.info("... calcola il minore: ...");
							logger.info("******************************************************************");
							importoMinBD = calcolaMinore(importoErogabileBD, differenzaEUBD, importoMassimoErogabileBD, contributoMaxUltimiTreAnni, logger);
							logger.info("... importoMinBD vale:  "+importoMinBD); // 35000.00
							
							importoErogabileBD = importoMinBD;
							logger.info("... importoErogabileBD vale:  "+importoErogabileBD); // 35000.00

							
							
							
							saldoContabilePrevistoBD = differenzaEUBD.subtract(importoErogabileBD).setScale(2); 
							saldoContabilePrevisto = saldoContabilePrevistoBD.toString().replace('.', ','); 
							logger.info(" saldoContabilePrevisto: " + saldoContabilePrevisto); // 219902,00
							
							
							
							if(saldoContabilePrevistoBD.compareTo(new BigDecimal(0)) == 0)
							{
								// importoRichiesto = importoErogabileBD.toString();	
								importoRichiesto = importoMinBD.toString();
							}else{
								importoRichiesto = "0";
							}
							
							logger.info("contributoMaxUltimiTreAnni risulta:  " + contributoMaxUltimiTreAnni.toString()); // 35000.00
							output.contributoMaxUltimiTreAnni=contributoMaxUltimiTreAnni.toString();
							
							logger.info("importoRichiesto risulta:  " + importoRichiesto.replace(".", ",")); // 35000,00
							output.setImportoRichiesto(importoRichiesto.replace(".", ","));
							
							logger.info("importoErogabileBD risulta:  " + importoErogabileBD.toString().replace(".", ",")); // 35000,00
							output.setImportoErogabile(importoErogabileBD.toString().replace(".", ","));
							
							logger.info("differenzaEU risulta:  " + differenzaEU.replace(".", ","));
							output.setDifferenzaEU(differenzaEU.replace(".", ","));
							
							logger.info("saldoContabilePrevisto risulta:  " + saldoContabilePrevisto); // 219902,00
							output.setSaldoContabilePrevisto(saldoContabilePrevisto);
						}
					}
				   
					/** calcolo pce: percentuale contributo erogabile */
					if(importoRichiesto!=null && !importoRichiesto.isEmpty()){
						if(importoRichiesto.indexOf(",") != -1){
							importoRichiesto = importoRichiesto.replace(",",".");
							logger.info("... importoRichiesto vale:  "			+importoRichiesto); // 35000.00
						}	
					}
					importoRichiestoBD = new BigDecimal(importoRichiesto);
					logger.info("importoRichiestoBD risulta valere:  " + importoRichiestoBD); // 35000.00
					
					
				}catch(Exception e){
	               speseGeneraliEFunzQP = "0,00";	
	               totaleSpeseEffettive	= "0,00";
	               differenzaEU	= "0,00";
	               importoErogabile	= "0,00";
	               importoRichiesto	= "0,00";
	               percentualeContributoErogabile = "0,00";
				}
					
				String importoErogabileXml = CultFormaAgevolazioneBDAO.getImportoErogabileFromXml(info.getStatusInfo().getNumProposta(), logger);// 35000,00
				logger.info("importoErogabileXml:  " + importoErogabileXml);
				
				/** jira 2009 - */
				if ("true".equals(input._cultFormaAgvB_cntb_max_ultimi_tre_anni))
				{
					String str_importoErogabile = importoErogabileBD.toString();
					logger.info("str_importoErogabile:  " + str_importoErogabile); // 35000.00
					
					if(str_importoErogabile!=null && !str_importoErogabile.isEmpty()){
						if(str_importoErogabile.indexOf(".") != -1){
							str_importoErogabile = str_importoErogabile.replace(".",",");
							logger.info("... str_importoErogabile vale:  "			+str_importoErogabile); // 35000,00
						}	
					}
					
					// custom
					if(!StringUtils.isBlank(importoErogabileXml) && !importoErogabileXml.equals(str_importoErogabile)){ 
						viewWarningAgevolazioni = "true";
					}
				}else{
					// std
					if(!StringUtils.isBlank(importoErogabileXml) && !importoErogabileXml.equals(importoErogabile)){ 
						viewWarningAgevolazioni = "true";
					}
				}
					
					if (("true").equals( ((CultFormaAgevolazioneBInput)getInput())._progetto_spese_quota_inseribile)) {
					String quotaParteXml = PianoSpeseDAO.getQuotaParte(info.getStatusInfo().getNumProposta()); // 7
					logger.info("quotaParteXml:  " + quotaParteXml);
					
					if(!StringUtils.isBlank(quotaParteXml) && !quotaParteXml.equals(percQuotaParteSpeseGenFunz)){ 
						viewWarningAgevolazioni = "true";
					}
				}
			}

			//// namespace			
			output.viewWarningImportiAgevolazioni=viewWarningImportiAgevolazioni; 	// false
			output.percMassimaContributoErogabile=percMassimaContributoErogabile;	//    70,00
			output.percQuotaParteSpeseGenFunz=percQuotaParteSpeseGenFunz;			//     0,00
			output.importoMassimoErogabile=importoMassimoErogabile; 				// 120000,00
			output.viewWarningAgevolazioni=viewWarningAgevolazioni; 				// false
			output.importoMinimoErogabile=importoMinimoErogabile; 					//   2000,00

			logger.info("A. spese connesse all'attivita risultano:  " + speseConnesseAttivita); // 710901,00
			output.speseConnesseAttivita=speseConnesseAttivita; 	
			
			/** se variabile a true, visualizza campi calcolati come da standard */
			if (_spese_generali_funzionamento_previste.equals("true")) 
			{
				logger.info("B. speseGeneraliEFunz risultano:  " + speseGeneraliEFunz);
				output.speseGeneraliEFunz=speseGeneraliEFunz;
				
				logger.info("B2. speseGeneraliEFunzQP risultano:  " + speseGeneraliEFunzQP);
				output.speseGeneraliEFunzQP = speseGeneraliEFunzQP;
				
				logger.info("Totale spese effettive risultano:  " + totaleSpeseEffettive);
				output.totaleSpeseEffettive=totaleSpeseEffettive;
				
				logger.info("C. Totale entrate risultano:  " + totaleEntrate);
				output.totaleEntrate=totaleEntrate;	
				
				logger.info("importoErogabile risulta:  " + importoErogabile);
				output.importoErogabile=importoErogabile;
				
				logger.info("D. Differenze risultano:  " + differenzaEU);
				output.differenzaEU=differenzaEU.replace(".", ","); 
				
			} else {
				
				/** altrimenti nascondi campi dopo averli calcolati */
				output.speseGeneraliEFunz=speseGeneraliEFunz.replace("0,00", "");
				logger.info("B. speseGeneraliEFunz risultano:  " + speseGeneraliEFunz.replace("0,00", ""));
				
				output.speseGeneraliEFunzQP = speseGeneraliEFunzQP.replace("0,00", "");
				logger.info("B2. speseGeneraliEFunzQP risultano: " + speseGeneraliEFunzQP.replace("0,00", ""));
				
				logger.info("importoErogabile risulta:  " + importoErogabile); // 497631,00
				output.importoErogabile=importoErogabile;
				
				// std
				output.totaleSpeseEffettive = totaleSpeseEffettive.replace(".", ",");
				logger.info("Totale spese effettive risultano:  " + totaleSpeseEffettive.replace(".", ","));
				
				
				if (campoNascostoTotaleEntrate.equals("true")) 
				{
					logger.info("C1. Totale entrate risulta:  " + totaleEntrate.replace(",", "."));
					totaleEntrate= totaleEntrate.replace(",", ".");
					
					output.differenzaEU=(differenzaEU.replace(".", ","));
					
				} else {
					output.totaleEntrate=totaleEntrate;
					logger.info("C2. Totale entrate risulta:  " + totaleEntrate); // 455999,00
					
					output.differenzaEU=(differenzaEU.replace(".", ",").replace("0,00", ""));
					logger.info("D2. Differenze risultano:  " + differenzaEU.replace(".", ",")); // 254902,00
					
				}
			
				output.campiNascosti=campiNascosti;
				output.campoNascostoTotaleEntrate=campoNascostoTotaleEntrate;
				
			}
			
			logger.info("importoRichiesto risulta:  " + importoRichiesto); // 35000.00
			output.importoRichiesto=importoRichiesto;	
			
			/**  : 
			 * 	nuovo campo per snb1 : (sistema neve b1)
			 * 	percentualeContributoErogabile = (importoRichiesto * 100)/totaleSpeseEffettive
			 * 
			 * */
			logger.info("percentualeContributoErogabile risulta:  " + percentualeContributoErogabile);
			output.percentualeContributoErogabile = percentualeContributoErogabile;	
			
			
			/** jira 2009 - */
			if ("true".equals(input._cultFormaAgvB_cntb_max_ultimi_tre_anni))
			{
				logger.info("contributoMaxUltimiTreAnni risulta:  " + contributoMaxUltimiTreAnni.toString()); // 35000.00
				output.contributoMaxUltimiTreAnni=contributoMaxUltimiTreAnni.toString();
				
				
				logger.info("importoRichiesto risulta:  " + importoRichiesto.replace(".", ",")); // 35000,00
				output.setImportoRichiesto(importoRichiesto.replace(".", ","));
				
				logger.info("importoErogabileBD risulta:  " + importoErogabileBD.toString().replace(".", ",")); // 35000,00
				output.setImportoErogabile(importoErogabileBD.toString().replace(".", ","));
				
				logger.info("differenzaEU risulta:  " + differenzaEU.replace(".", ","));
				output.setDifferenzaEU(differenzaEU.replace(".", ","));
				
				// calcola saldo contabile
				
				logger.info("saldoContabilePrevisto risulta:  " + saldoContabilePrevisto); // 219902,00
				output.setSaldoContabilePrevisto(saldoContabilePrevisto);
			}
			
			/** jira 2040 - */
			output.setViewInfoSaldoContabile(viewInfoSaldoContabile);
			
		}catch (NullPointerException npe) {
			logger.error("[CultFormaAgevolazioneB::inject] Error ", npe);
		}catch (Exception e) {
			logger.error("[CultFormaAgevolazioneB::inject] Error ", e);
			throw new CommonalityException(e);
		}finally {
			logger.info("[CultFormaAgevolazioneB::inject] END");
		}
		return output;
	}

	
	/** jira 2144 */
	public String getImportoByIdDipartimento(String idSoggettoAbilitato, boolean isPresentIdDipartimento, String idDipartimento, List<CultFormaAgevolazioneBVO> importiList, Logger logger) {
		
		String importoUltimiTreAnni = "";
		
		if(isPresentIdDipartimento){
			if(importiList!=null && !importiList.isEmpty()){
				for (int i = 0; i < importiList.size(); i++) {
					logger.info("idSoggett: " + importiList.get(i).getIdSoggetto());
					logger.info("idDipartimento: " + importiList.get(i).getIdDipartimento());
					logger.info("importo: " + importiList.get(i).getImporto());
					if(idSoggettoAbilitato.equalsIgnoreCase(importiList.get(i).getIdSoggetto())){
						if(idDipartimento.equals(importiList.get(i).getIdDipartimento())){
							importoUltimiTreAnni = importiList.get(i).getImporto();
							logger.info("tmpImportoUltimiTreAnni: ..." + importoUltimiTreAnni);
							break;
						}
					}
				} 
			}
		}
		return importoUltimiTreAnni;
	}

	/** jira 2144 */
	public String getImportoPerBeneficiario(String idSoggettoAbilitato, List<CultFormaAgevolazioneBVO> importiList, Logger logger) {
		
		String importoPerBeneficiario = "";
		
		if(importiList!=null && !importiList.isEmpty()){
			for (int i = 0; i < importiList.size(); i++) {
				logger.info("idSoggett: " + importiList.get(i).getIdSoggetto());
				logger.info("importo: " + importiList.get(i).getImporto());
				if(idSoggettoAbilitato.equalsIgnoreCase(importiList.get(i).getIdSoggetto())){
					importoPerBeneficiario = importiList.get(i).getImporto();
					logger.info("importoPerBeneficiario: ..." + importoPerBeneficiario);
					break;
				}
			} 
		}
		return importoPerBeneficiario;
	}
	

	private BigDecimal calcolaMinore(BigDecimal speseConnesseAttivitaBD, BigDecimal differenzaEUBD, BigDecimal importoMassimoErogabileBD, BigDecimal contributoMaxUltimiTreAnni, Logger logger) {
		
		BigDecimal minoreGrp1 = new BigDecimal(0.00);
		BigDecimal minoreGrp2 = new BigDecimal(0.00);
		BigDecimal minoreRis = new BigDecimal(0.00);
		
		minoreGrp1 = getBigDecimalMinore(speseConnesseAttivitaBD, differenzaEUBD);
		logger.info("minoreGrp1 risulta: "+minoreGrp1);
		
		minoreGrp2 = getBigDecimalMinore(importoMassimoErogabileBD, contributoMaxUltimiTreAnni);
		logger.info("minoreGrp2 risulta: "+minoreGrp2);
		
		minoreRis = getBigDecimalMinore(minoreGrp1, minoreGrp2);
		logger.info("minoreRis risulta: "+minoreRis);
		
		return minoreRis;
	}
	
	public static BigDecimal getBigDecimalMinore(BigDecimal a, BigDecimal b) {
	  return a.min(b);
	}


	private BigDecimal calcolaImpErg1(BigDecimal speseConnesseAttivitaBD, BigDecimal percMassimaContributoErogabileBD, Logger logger) {
		
		BigDecimal ris = new BigDecimal(0.00);
		
		ris = (speseConnesseAttivitaBD.multiply(percMassimaContributoErogabileBD).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
		logger.info("ris vale: " + ris);
		return ris;
	}


	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) arg0;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		logger.info("[CultFormaAgevolazioneB::modelValidate] _formaAgevolazioneB BEGIN");

		//// validazione panel Forma di finanziamento

		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo è obbligatorio";
		String ERRMSG_IMPORTO_FORMATO = "- il valore deve essere numerico, maggiore di zero e senza decimali";        
		String ERRMSG_IMPORTO_RICHIESTO_SUPERA_EROGABILE = "- l'importo richiesto non può essere superiore all'importo erogabile";
		String ERRMSG_IMPORTO_EROGABILE_INF_MINIMO_EROGABILE = "- non è stato raggiunto il contributo minimo erogabile previsto dal bando";  
		String ERRMSG_IMPORTO_RICHIESTO_INF_MINIMO_EROGABILE = "- l'importo richiesto deve essere almeno pari all'importo minimo erogabile previsto per il bando";

		String ERRMSG_IMPORTO_RICHIESTO_MAGGIORE_IMPORTO_EROGABILE = "- l'importo richiesto deve essere minore od uguale all'importo erogabile";
		
		/** jira 2009 -2*/
		String ERRMSG_SALDO_CONTABILE_NO_ZERO = " - verificare di aver compilato le pagine delle Entrate e delle Spese e che il saldo contabile sia 0; in caso non lo sia, agire sugli importi delle spese e/o delle entrate. Impossibile salvare.";
		
		CultFormaAgevolazioneBVO cultFormaAgevolazioneMap = input._formaAgevolazioneB; 			   
		if(cultFormaAgevolazioneMap!=null) {
				      
		    //il campo "importoRichiesto" e' obbligatorio e numerico positivo
		    String importoRichiesto = cultFormaAgevolazioneMap.getImportoRichiesto();
		    logger.info("importoRichiesto: " + importoRichiesto);
		    
		    String importoErogabile = cultFormaAgevolazioneMap.getImportoErogabile();	
		    logger.info("importoErogabile: " + importoErogabile);
		    
		    String importoMinimoErogabile = cultFormaAgevolazioneMap.getImportoMinimoErogabile();
		    logger.info("importoMinimoErogabile: " + importoMinimoErogabile);
		    
		    String importoMassimoErogabile = cultFormaAgevolazioneMap.getImportoMassimoErogabile();
		    logger.info("importoMassimoErogabile: " + importoMassimoErogabile);
		    
		    String differenza = cultFormaAgevolazioneMap.getDifferenza();
		    logger.info("differenza: " + differenza);
		    
		    BigDecimal importoErogabileBD = new BigDecimal(0);  // 10005.25           
			
			BigDecimal importoMinimoErogabileBD = new BigDecimal(0); 
			
	        if(!StringUtils.isBlank(importoMinimoErogabile)){     
				importoMinimoErogabile = importoMinimoErogabile.replace(',', '.');
				importoMinimoErogabileBD = new BigDecimal(importoMinimoErogabile);	
				logger.info("importoMinimoErogabileBD: " + importoMinimoErogabileBD);
			}  
	            
		    if(StringUtils.isNotBlank(importoErogabile) ) // 7922,33
		    {
		       if(!importoErogabile.matches("^\\d+(,\\d{1,2})?$") || Utils.isZero(importoErogabile))
		       {
		    	  logger.info("[CultFormaAgevolazioneB::modelValidate] - il valore deve essere numerico, maggiore di zero e senza decimali");			
		 	      addMessage(newMessages,"_formaAgevolazioneB_importoErogabile", ERRMSG_IMPORTO_FORMATO);
		 	   } else {
		 		   	/* ----------------------------------------- : Esegui controllo round importoErogabile - inizio - */ 	   		 
						importoErogabile = importoErogabile.replace(',', '.'); // 7922,33
						importoErogabileBD = MetodiUtili.roundBD(importoErogabile); // 7922.00
						importoErogabileBD = new BigDecimal(importoErogabileBD.toString());		
						logger.info("importoErogabileBD: " + importoErogabileBD);
					/* ----------------------------------------- : Esegui controllo round importoErogabile - fine - */
					if(importoErogabileBD.compareTo(importoMinimoErogabileBD)==-1){                 
						logger.info("[CultFormaAgevolazioneB::modelValidate] l'importo erogabile non può essere inferiore all'importoMinimoErogabile");
						addMessage(newMessages,"_formaAgevolazioneB_importoErogabile", ERRMSG_IMPORTO_EROGABILE_INF_MINIMO_EROGABILE); 
					}
					
					/**
					 *  - eseguo controlli su importo erogabile:
					 * 
					 * */
					
					/**************/
					/** check I  **/
					/**************/
					logger.info("importoErogabile pre-controllo-1 vale: " + importoErogabile);
					boolean isMaggioreImpMaxErogabile = MetodiUtili.isGreater(importoErogabile, importoMassimoErogabile, logger);
					
					if(isMaggioreImpMaxErogabile){
						logger.info("importo-erogabile > importo-max-erogabile");
						logger.info("importo-erogabile risultera essere = importo-max-erogabile");
						importoErogabile = importoMassimoErogabile;
					}else{
						logger.info("1° check superato: importoErogabile NON risulta > di importo-max-erogabile!");
					}
					
					
					/**************/
					/** check II **/
					/**************/
					boolean isMaggioreDifferenza = MetodiUtili.isGreater(importoErogabile, differenza, logger);
					
					if(isMaggioreDifferenza){
						logger.info("importo-erogabile > differenza");
						logger.info("importo-erogabile risultera essere pari al valore della  differenza");
						importoErogabile = differenza;
					}
					
					/***************/
					/** check III **/
					/***************/
					boolean isMinoreUgualeImpErogabile = MetodiUtili.isMinoreUguale(importoRichiesto, importoErogabile, logger);
					
					if(!isMinoreUgualeImpErogabile){
						logger.info("importoRichiesto NON risulta <= importoErogabile");
						logger.info("Errore in fase di salvataggio!");
						addMessage(newMessages,"_formaAgevolazioneB_importoErogabile", ERRMSG_IMPORTO_RICHIESTO_MAGGIORE_IMPORTO_EROGABILE); 
					}
					
					
		 	   }
		 	}  
		 	  
		    if(StringUtils.isNotBlank(importoRichiesto) )
		    {
	    	/* ----------------------------------------- : Esegui controllo validita dei decimali - inizio - */
		    	isDecimaliUguali= MetodiUtili.confrontaDecimaliUguali(importoRichiesto, logger);
		    	logger.info("isDecimaliUguali: " + isDecimaliUguali);
	    	/* ----------------------------------------- : Esegui controllo validita dei decimali -   fine - */
		    	
		    	
		 	  if(!importoRichiesto.matches("^\\d+(,\\d{1,2})?$") || Utils.isZero(importoRichiesto) || !isDecimaliUguali){
		 		 logger.info("[CultFormaAgevolazioneB::modelValidate] - il valore deve essere numerico, maggiore di zero e senza decimali ");			
		 	      addMessage(newMessages,"_formaAgevolazioneB_importoRichiesto", ERRMSG_IMPORTO_FORMATO);
		 	  } else {
				// importoRichiesto non deve superare l'importo erogabile
				
	            BigDecimal importoRichiestoBD = new BigDecimal(0);   
	            importoRichiesto = importoRichiesto.replace(',', '.');
	            importoRichiestoBD = new BigDecimal(importoRichiesto);
	            logger.info("importoRichiestoBD: " + importoRichiestoBD);
	                               
	            if(importoRichiestoBD.compareTo(importoErogabileBD)==1)
	            {                 
	            	logger.info("[CultFormaAgevolazioneB::modelValidate] l'importo richiesto non può essere superiore all'importoErogabile");
	            	addMessage(newMessages,"_formaAgevolazioneB_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_SUPERA_EROGABILE); 
	            	
	            } else {
	            	
	            	if(importoRichiestoBD.compareTo(importoMinimoErogabileBD)==-1) 
	            	{                 
	            		logger.info("[CultFormaAgevolazioneB::modelValidate] l'importo richiesto non può essere inferiore all'importoMinimoErogabile");
						addMessage(newMessages,"_formaAgevolazioneB_importoRichiesto", ERRMSG_IMPORTO_RICHIESTO_INF_MINIMO_EROGABILE); 
					} 
	            }	
		 	  }
		 	  
		    } else {
		    	logger.info("[CultFormaAgevolazioneB::modelValidate] _formaAgevolazioneB.importoRichiesto non presente o vuoto");		
				addMessage(newMessages,"_formaAgevolazioneB_importoRichiesto", ERRMSG_CAMPO_OBBLIGATORIO);
		    }
		    
		    /** jira 2009 - */
			if ("true".equals(input._cultFormaAgvB_cntb_max_ultimi_tre_anni))
			{
				//per poter salvare la pagina il campo 'saldo contabile previsto' deve essere uguale a 0,00
				String saldoContabilePrevisto = cultFormaAgevolazioneMap.getSaldoContabilePrevisto();
				if(StringUtils.isEmpty(saldoContabilePrevisto) || !saldoContabilePrevisto.equals("0,00") ){
					logger.info("[CultFormaAgevolazioneConSaldo::modelValidate] il saldo contabile previsto deve essere uguale a zero ");
					addMessage(newMessages,"_formaAgevolazione_saldoContabilePrevisto", ERRMSG_SALDO_CONTABILE_NO_ZERO);
				}
			}
				     
	     } else {
	    	 logger.info("[CultFormaAgevolazioneB::modelValidate] _formaAgevolazioneB non presente o vuoto");
	     }
		
		/** Jira 2040: - */
		// qualunque sia l'esito della validazione il messaggio informativo viene nascosto dal messaggio che esce dalla validazione */
		SessionCache.getInstance().set("hideInfoSaldoContabile","true");
	
		logger.info("[CultFormaAgevolazioneB::modelValidate] _formaAgevolazioneB END");
	
		return newMessages;
	}


	public boolean isDecimaliUguali() {
		return isDecimaliUguali;
	}

	public void setDecimaliUguali(boolean isDecimaliUguali) {
		this.isDecimaliUguali = isDecimaliUguali;
	}
}
