/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.abstractprogettoNG;

import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.commonality.Constants;
import it.csi.findom.blocchetti.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;

/**
 * metodi static di validazione invocati via reflection se presenti in variabile di configurazione
 * @author mauro.bottero
 *
 */
public class AbstractProgettoNGValidationMethods implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * controlla che se la tipologia intervento avente id idTipologiaIntervento e' selezionata, allora il ruolo scelto sia idRuolo
	 * @param input
	 * @param idTipologiaIntervento
	 * @param idRuolo id del ruolo che deve essere selezionato se e' stata selezionata la tipologia intervento avente idTipologiaIntervento
	 * @return 
	 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoRuolo(AbstractProgettoNGInput input, Logger logger, String idTipologiaIntervento, String idRuolo){
		logger.debug("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoRuolo] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;
		
		logger.info("idTipologiaIntervento: " + idTipologiaIntervento);
		logger.info("idRuolo: " + idRuolo); // C, CP
		
		if(input==null || StringUtils.isBlank(idTipologiaIntervento) || StringUtils.isBlank(idRuolo)) { // 89 || CP
			return result;
		}
		String descrRuolo = Constants.ABSTRACT_RUOLI.get(idRuolo); // idRuolo: CP
		logger.info("test idRuolo: " + descrRuolo); // Capofila
		
		String ERRMSG_RUOLO_NON_CORRETTO = "- per la tipologia intervento selezionata il ruolo deve essere '"+descrRuolo+"'";		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;
		AbstractProgettoVO abstractProgettoVO = input.abstractProgetto;			

		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null && abstractProgettoVO!=null){
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
			for(int i=0; i<tipologiaInterventoList.size();i++){
				TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
				if(curTipologia!=null)
				{
					logger.info("test curTipologia: " + curTipologia.getCodTipoIntervento()+"-"+curTipologia.getDescrTipoIntervento());
					
					String curChecked = curTipologia.getChecked();
					logger.info("test curChecked: " + curChecked);
					
					if(!StringUtils.isBlank(curChecked) && curChecked.equals("true")){							
						String curIdTipologia = curTipologia.getIdTipoIntervento();
						logger.info("test curIdTipologia: " + curIdTipologia); // 89
						if(!StringUtils.isBlank(curIdTipologia) && curIdTipologia.equals(idTipologiaIntervento)){
							String ruolo = abstractProgettoVO.getRuolo();
							logger.info("ruolo: " + ruolo); // P or C
							if(StringUtils.isNotBlank(ruolo) && !ruolo.equalsIgnoreCase(idRuolo)){ // P != CP
								if(result==null){
									result = new ArrayList<>();
								}
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								
								segnalazione.setCampoErrore("_abstractProgetto_ruolo");
								segnalazione.setMsgErrore(ERRMSG_RUOLO_NON_CORRETTO);
								logger.info("ERRMSG_RUOLO_NON_CORRETTO: " + ERRMSG_RUOLO_NON_CORRETTO);
								result.add(segnalazione);
								logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoRuolo] l'idTipologia selezionato: " +idTipologiaIntervento +" richiede che il ruolo sia: "+ idRuolo );
							}								
						}
						break; //la tipologia di interesse ormai è stata verificata, per cui interrompo l'iterazione
					}	 
				}
			}
		}
		logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoRuolo] END");
		return result;
	}
	
	/**
	 * 
	 * @param input l'oggetto AbstractProgettoNGInput
	 * @param logger
	 * @param vincoli un array di stringhe che rappresentano alternativamente degli idTipoIntervento seguiti alla posizione successiva dalla max durata progetto ammessa 
	 * 						se quella tipologia è selezionata; il numero di coppie <idTipoIntervento, max durata> puo' essere qualsiasi
	 * @return
	 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoDurata(AbstractProgettoNGInput input, Logger logger, String... vincoli) {																		
		logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDurata] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		//popolo una Map con i vincoli passati: ogni elemento ha come chiave idTipoIntervento e come valore la max durata progetto corrispondente
		Map<String, String> maxDurateInterventi = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			maxDurateInterventi.put(vincoli[i], vincoli[i+1]);			
		}
		
		// ctrlCoerenzaInterventoDurata
		String ERRMSG_CAMPO_DURATAMAX_EXCEDED = "- il valore inserito supera la durata massima prevista per la tipologia intervento selezionata";		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;
		AbstractProgettoVO abstractProgettoVO = input.abstractProgetto;	

		if (caratteristicheProgettoNGVO!=null && abstractProgettoVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null){
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
			for(int i=0; i<tipologiaInterventoList.size();i++){
				TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
				if(curTipologia!=null){
					String checked = curTipologia.getChecked();
					if(!StringUtils.isBlank(checked) && checked.equals("true")){							
						String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
						if(!StringUtils.isBlank(curIdTipoIntervento) &&	maxDurateInterventi.containsKey(curIdTipoIntervento)){
							String durataPrevista = abstractProgettoVO.getDurataPrevista();
						
							if(!StringUtils.isBlank(durataPrevista) && (Integer.parseInt(durataPrevista) > Integer.parseInt( maxDurateInterventi.get(curIdTipoIntervento)))){
								if(result==null){
									result = new ArrayList<>();
								}
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								
								segnalazione.setCampoErrore("_abstractProgetto_durataPrevista");
								segnalazione.setMsgErrore(ERRMSG_CAMPO_DURATAMAX_EXCEDED);	
								result.add(segnalazione);
								break;
							}								
						}
					}	 
				}
			}
		}
		logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDurata] END");
		return result;
	}
	
	
	
	
	/** Jira: 1854  */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoBeneficiario(AbstractProgettoNGInput input, Logger logger, String... vincoli) 
	{	
		logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoBeneficiario] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		//popolo una Map con i vincoli passati: ogni elemento ha come chiave idTipoIntervento e come valore la max durata progetto corrispondente
		Map<String, String> vincoliObbligatoriMap = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			logger.info("vincolo: " +i+": "+ vincoli[i]);
			logger.info("vincolo: " +i+": "+ vincoli[i+1]);
			vincoliObbligatoriMap.put(vincoli[i], vincoli[i+1]);			
		}
		
			CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;
			AbstractProgettoVO abstractProgettoVO = input.abstractProgetto;	
			DomandaNGVO domandaNGVO = input._domanda;
			
			String codTipoBeneficiario = null;
			String descrizioneTipologiaUtente = "";
			if ( domandaNGVO != null ) {
				codTipoBeneficiario = domandaNGVO.getCodiceTipologiaBeneficiario();
				logger.info("codTipoBeneficiario: " + codTipoBeneficiario);
				
				descrizioneTipologiaUtente = domandaNGVO.getDescrTipologiaUtente();
				logger.info("descrizioneTipologiaUtente: " + descrizioneTipologiaUtente);
			}
			
			String ERRMSG_CAMPO_COERENZA_BENEFICIARIO_INTERVENTO = "- Per il beneficiario corrente non risulta previsto la tipologia intervento selezionata";
			if(descrizioneTipologiaUtente!=null && descrizioneTipologiaUtente.length()>0){
				ERRMSG_CAMPO_COERENZA_BENEFICIARIO_INTERVENTO = "- Per il beneficiario " +descrizioneTipologiaUtente+ " non risulta prevista la tipologia intervento selezionata";
			}
			
			boolean isVincoloCorretto = false;
			
			if (caratteristicheProgettoNGVO!=null && abstractProgettoVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null)
			{
				List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
				
				for(int i=0; i<tipologiaInterventoList.size();i++)
				{
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
					logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoBeneficiario] curTipologia: " + curTipologia.getIdTipoIntervento());
					
					if(curTipologia!=null)
					{
						String checked = curTipologia.getChecked();
						logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoBeneficiario] checked: " + checked);
						
						if(!StringUtils.isBlank(checked) && checked.equals("true"))
						{							
							String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
							logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoBeneficiario] curIdTipoIntervento: " + curIdTipoIntervento);
							
							if(!StringUtils.isBlank(curIdTipoIntervento) )
							{
								// ciclo sulla mappa per value 
								for (Map.Entry<String, String> entry : vincoliObbligatoriMap.entrySet()) {
									
									if(!StringUtils.isBlank(codTipoBeneficiario) && curIdTipoIntervento.equals(entry.getKey().substring(3)))
									{
										if(codTipoBeneficiario.equals(entry.getValue()))
										{
											logger.info("intervento: " + entry.getKey().substring(3) + " operatore: " + entry.getValue() + " *** Corretto ***");
											isVincoloCorretto = true;
											break;
										}
										
									} else {
										logger.info("intervento: " + entry.getKey().substring(3) + " operatore: " + entry.getValue() + "*** NON hai superato la validazione ***");
									}
								}
								
								if(!isVincoloCorretto)
								{
									if(result==null){
										result = new ArrayList<>();
									}
									SegnalazioneErrore segnalazione = new SegnalazioneErrore();
									
									segnalazione.setCampoErrore("_caratteristicheProgetto");
									segnalazione.setMsgErrore(ERRMSG_CAMPO_COERENZA_BENEFICIARIO_INTERVENTO);	
									result.add(segnalazione);
									break;
								}else{
									logger.info(" puoi salvare !");
								}
							}	 
						}
					}
				}
			}
			logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoBeneficiario] END");
			return result;
		}
	
	
	
	  /** jira 2111 : ctrlCoerenzaInterventoDataInizioProgetto
	   *  validazione singolo campo data inizio progetto
	   *  in base alla tipologia intervento selezionata.
	   *  	-- data inizio progetto selezionata deve risultare >= (01/10/2019) se tipo intervento == 1 
	   *  	-- data inizio progetto selezionata deve risultare >= (01/02/2020) se tipo intervento <> 1 
	   **/
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoDataInizioProgetto(AbstractProgettoNGInput input, Logger logger, String... vincoli) 
	{	
		logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDataInizioProgetto] BEGIN");
		
		DateValidator validator = DateValidator.getInstance();
		
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		String vincoloData1= "";
		String vincoloData2= "";
		String vincoloTipoIntervento= "";

		for (int i = 0; i < vincoli.length; i= i+2) {
			logger.info("vincolo: " +i+": "+ vincoli[i]);
			logger.info("vincolo: " +i+": "+ vincoli[i+1]);
			if(i==0){
				vincoloTipoIntervento = vincoli[i].substring(0,3);
				vincoloData1 = vincoli[i+1];
			}
			if(i==2){
				vincoloTipoIntervento = vincoli[i].substring(0,3);
				vincoloData2 = vincoli[i+1];
			}
		}
		
		boolean err_01 = false; // per intervento == 1
		boolean err_02 = false; // per intervento <> da 1
		
		String dtInizioPrjSelezionata = null;
		String ERRMSG_DATA_INVALIDA = "- La data selezionata non risulta valida!";
		String ERRMSG_INTERVENTO_UNO = "";
		String ERRMSG_INTERVENTO_DIVERSO_DA_UNO = "";
		String descrTipoIntervento = "";
		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;
		AbstractProgettoVO abstractProgettoVO = input.abstractProgetto;	
		
		if (abstractProgettoVO != null ) {
			dtInizioPrjSelezionata 	= abstractProgettoVO.getDataInizioProgetto();
			logger.info("dtInizioPrjSelezionata: " + dtInizioPrjSelezionata);
		}
		
		// valido data: ( dtInizioPrjSelezionata ) nel formato e nel calendario 
		if (MetodiUtili.isDataValida(dtInizioPrjSelezionata, logger)) 
		{
			if (caratteristicheProgettoNGVO != null && abstractProgettoVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList() != null) {
				// elenco tipologie intervento
				List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
				
				for(int i=0; i<tipologiaInterventoList.size();i++) {
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
					logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDataInizioProgetto] curTipologia: " + curTipologia.getIdTipoIntervento());
					
					String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
					logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDataInizioProgetto] curIdTipoIntervento: " + curIdTipoIntervento);
					
					if(curTipologia!=null ) 
					{
						if(!StringUtils.isBlank(curIdTipoIntervento) && curIdTipoIntervento.equals(vincoloTipoIntervento)){
							
							String checked = curTipologia.getChecked();
							logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDataInizioProgetto] checked: " + checked);
							
							if(!StringUtils.isBlank(checked) && checked.equals("true")) {
								// tipo intervento 1 
								if(!MetodiUtili.isDataRangeValida(dtInizioPrjSelezionata, vincoloData1, logger)){
									err_01 = true;
								}
							}else{
								// tipo intervento <> 1 
								if(!MetodiUtili.isDataRangeValida(dtInizioPrjSelezionata, vincoloData2, logger)){
									err_02 = true;
								}
							}
						}
					}
									
					if(err_01) {
						result = new ArrayList<>();
						SegnalazioneErrore segnalazione = new SegnalazioneErrore();
						segnalazione.setCampoErrore("_abstractProgetto_dataInizioProgetto");
						descrTipoIntervento = curTipologia.getDescrTipoIntervento();
						ERRMSG_INTERVENTO_UNO = "- Per la tipologia intervento selezionata la data inizio progetto deve essere successiva al "+vincoloData1;
						segnalazione.setMsgErrore(ERRMSG_INTERVENTO_UNO);
						result.add(segnalazione);
						break;
					} 
					
					if(err_02) {
						result = new ArrayList<>();
						SegnalazioneErrore segnalazione = new SegnalazioneErrore();
						segnalazione.setCampoErrore("_abstractProgetto_dataInizioProgetto");
						descrTipoIntervento = curTipologia.getDescrTipoIntervento();
						ERRMSG_INTERVENTO_DIVERSO_DA_UNO = "- Per le tipologie intervento selezionate la data inizio progetto deve essere successiva al "+vincoloData2;
						segnalazione.setMsgErrore(ERRMSG_INTERVENTO_DIVERSO_DA_UNO);	
						result.add(segnalazione);
						break;
					} 
					break;
				}
			}// fine for
		} 
		else{
			
			if(result==null){
				result = new ArrayList<>();
			}
			
			SegnalazioneErrore segnalazione = new SegnalazioneErrore();
			segnalazione.setCampoErrore("_abstractProgetto_dataInizioProgetto");
			segnalazione.setMsgErrore(ERRMSG_DATA_INVALIDA);	
			result.add(segnalazione);
		}
		logger.info("[AbstractProgettoNGValidationMethods::ctrlCoerenzaInterventoDataInizioProgetto] END");
		return result;
	}
	
}
