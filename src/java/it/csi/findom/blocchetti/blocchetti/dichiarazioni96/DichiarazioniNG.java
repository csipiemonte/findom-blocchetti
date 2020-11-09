/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioni96;

import it.csi.findom.blocchetti.common.vo.agevolazioni.AgevolazioneItemVO;
import it.csi.findom.blocchetti.common.vo.agevolazioni.AgevolazioniVO;
import it.csi.findom.blocchetti.common.vo.sede.SediVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;

public class DichiarazioniNG extends Commonality {

	DichiarazioniNGInput input = new DichiarazioniNGInput();
	
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo infol, List<CommonalityMessage> arg1) throws CommonalityException {
		// <#-- note di dipendenze da altre commonalities: nessuna -->

		FinCommonInfo info = (FinCommonInfo) infol;
		DichiarazioniNGOutput output = new DichiarazioniNGOutput();
		
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[Dichiarazioni::inject] Dichiarazioni  BEGIN");
		
		
		//// dichiarazioni
		List<AgevolazioniVO> agevolazioniInseriteList  = new ArrayList();
		String sedeAttiva = "false";
		String sedeNoPiemonte = "false";
		String ratingLegalita = "false"; // se diventa true visualizzo la dichiarazione "Rating di Legalita"
//		String poloAssociato = "false"; // se resta false visualizzo la dichiarazione "Impegno ad associarmi ad un Polo" esempio: S3_P2 di PoliC
	
		//// valorizzazione
		if (info.getCurrentPage()!= null) {
			
				// carico le agevolazioni inserite in XML
				DichiarazioniNGVO _dichiarazioni = input.dichiarazioni;
				if(_dichiarazioni != null) {
					agevolazioniInseriteList = _dichiarazioni.agevolazioniList!=null?Arrays.asList(_dichiarazioni.agevolazioniList):null;
					
					if(agevolazioniInseriteList!=null && !agevolazioniInseriteList.isEmpty()){
					   for(int x=0; x < agevolazioniInseriteList.size(); x++){
					      AgevolazioniVO curAgevolazione = null;
					        curAgevolazione = agevolazioniInseriteList.get(x);
					        
					        if (curAgevolazione!=null && curAgevolazione.is__Deleted()) {
				             //faccio in modo da rimettere nell'html il campo marcato per essere cancellato
				             curAgevolazione = new AgevolazioniVO();
				             curAgevolazione.setDaCancellare("true");
				             agevolazioniInseriteList.set(x,curAgevolazione);
				             continue;
				         }
				       }
				    }
				}
				
				// verifico il valore del check "Dichiaro che la sede intervento non e' ancora attiva...."  nel TAB "sedi" (S4_P2)
				SediVO _sedi = input.sedi;
				logger.info("[Dichiarazioni::inject] Dichiarazioni _sedi="+_sedi);

				if(_sedi != null) {
					String flagSedeAtt = _sedi.getFlagSedeAttiva();
					logger.info("[Dichiarazioni::inject] Dichiarazioni flagSedeAtt=["+flagSedeAtt+"]");
					if("checked".equals(flagSedeAtt)){
						sedeAttiva = "true";
					}
					
					String flagSedeNoPiemonte = (String)_sedi.getFlagSedeNoPiemonte();
					logger.info("[Dichiarazioni::inject] Dichiarazioni flagSedeNoPiemonte=["+flagSedeNoPiemonte+"]");
					if("checked".equals(flagSedeNoPiemonte)){
						sedeNoPiemonte = "true";
					}
				}
		}
		
		logger.info("[Dichiarazioni::inject] Dichiarazioni sedeAttiva="+sedeAttiva);
		logger.info("[Dichiarazioni::inject] Dichiarazioni ratingLegalita="+ratingLegalita);
		
		//// namespace

		if (agevolazioniInseriteList != null)
			output.agevolazioniInseriteList=agevolazioniInseriteList.toArray(new AgevolazioniVO[0]);
		output.ratingLegalita=ratingLegalita;
		output.sedeAttiva=sedeAttiva;
		output.sedeNoPiemonte=sedeNoPiemonte;

		logger.info("[Dichiarazioni::inject] Dichiarazioni _dichiarazioni S3_P2 END");
		return output;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1)
			throws CommonalityException {
		FinCommonInfo info = (FinCommonInfo) info1;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());

		logger.info("[DichiarazioniNG::modelValidate]  BEGIN");

		DateValidator validator = DateValidator.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();

		String ERRMSG_CAMPO_OBBLIGATORIO_LR = "- il campo é obbligatorio";

		String ERRMSG_CAMPO_OBBLIGATORIO_DICH = "- dato obbligatorio";
		String ERRMSG_DICH_VOCE = "- selezionare almeno una voce";
		String ERRMSG_DICH_DESCR = "- dato mancante, tutta la descrizione é obbligatoria";

				DichiarazioniNGVO _dichiarazioni = input.dichiarazioni;
				boolean error = false;
				if (_dichiarazioni != null ) 
				{
					logger.info("[DichiarazioniNG::modelValidate] _dichiarazioni:" + _dichiarazioni);

					// controllo che tutte le descrizioni della lista "Accesso agevolazioni" siano valorizzate
					List<AgevolazioniVO> agevolazioniList = _dichiarazioni.agevolazioniList!=null?Arrays.asList(_dichiarazioni.agevolazioniList):null;
					if (agevolazioniList != null) {
						//logger.debug("[DichiarazioniNG::modelValidate] agevolazioniList:" + agevolazioniList);
						boolean errore_descr = false;
						for (int i = 0, n = agevolazioniList.size(); i < n; i++) {
							try{
								AgevolazioniVO agevolazioniMap = agevolazioniList.get(i);
								//logger.debug("[DichiarazioniNG::modelValidate] agevolazioniMap:" + agevolazioniMap);
								if (agevolazioniMap != null) {
									
									AgevolazioneItemVO agevolMap = agevolazioniMap.getAgevolazione();
									// logger.info("[DichiarazioniNG::modelValidate] agevolMap:" + agevolMap);
									if(null == agevolMap) 
										break;
									
									String descrizione = agevolMap.getDescrizione();
									logger.info("[DichiarazioniNG::modelValidate] descrizione:" + descrizione);
									
									// String descrizione = agevolMap.getDescrizione()!=null ? agevolMap.getDescrizione() : "";
									if (StringUtils.isBlank(descrizione)) {
										errore_descr = true;
										logger.info("[DichiarazioniNG::modelValidate] descrizione non valorizzata nell'elemento ["+i+"]");
										break;
									}
								}
							}catch(NullPointerException npe){
								logger.info("[DichiarazioniNG::modelValidate] probabile elemento null");
								continue;
							}catch(ClassCastException e){
								// se sono qui e' perche' c'era un elemento marcato DELETED, quindi lo devo ignorare
								logger.warn("[DichiarazioniNG::modelValidate] probabile elemento DELETED");
								continue;
							}
						}
						if(errore_descr){ // trovata almeno una descrizione non valorizzata
							addMessage(newMessages,"_dichiarazioni_agevolazioniList_agevolazione_descrizione", ERRMSG_DICH_DESCR);
						}
					} else {
						// addMessage(newMessages,"_dichiarazioni_agevolazioniList_agevolazione_descrizione", ERRMSG_DICH_DESCR);
						logger.info("Campo descrizione non richieste come campo obbligatorio in Bando MPMI!");
					}
					

					//MB2015_10_01 ini 
					// Validazione trattamento dati personali
					String trattamentoDatiPersonali = _dichiarazioni.trattamentoDatiPersonali;
					if(StringUtils.isBlank(trattamentoDatiPersonali)|| !trattamentoDatiPersonali.equals("si")){			
					    logger.warn("[DichiarazioniNG::modelValidate] _dichiarazioni.trattamentoDatiPersonali non presente o vuoto");
					    addMessage(newMessages,"_dichiarazioni_trattamentoDatiPersonali", ERRMSG_CAMPO_OBBLIGATORIO_DICH);
					}
					
					// Validazione regole di compilazione
					String regoleCompilazione = _dichiarazioni.regoleCompilazione;
					if(StringUtils.isBlank(regoleCompilazione)|| !regoleCompilazione.equals("si")){			
					    logger.warn("[DichiarazioniNG::modelValidate] _dichiarazioni.regoleCompilazione non presente o vuoto");
					    addMessage(newMessages,"_dichiarazioni_regoleCompilazione", ERRMSG_CAMPO_OBBLIGATORIO_DICH);
					}
					
					// Validazione presa visione
					String presaVisione = _dichiarazioni.presaVisione;
					if(StringUtils.isBlank(presaVisione)|| !presaVisione.equals("si")){			
					    logger.warn("[DichiarazioniNG::modelValidate] _dichiarazioni.presaVisione non presente o vuoto");
					    addMessage(newMessages,"_dichiarazioni_presaVisione", ERRMSG_CAMPO_OBBLIGATORIO_DICH);
					}
					//MB2015_10_01 fine

				} else {
					logger.warn("[DichiarazioniNG::modelValidate] _dichiarazioni non presente o vuoto");
				}

		logger.info("[DichiarazioniNG::modelValidate] _dichiarazioni S3_P2 END");
		return newMessages;
	}

}
