/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNG;

import it.csi.findom.blocchetti.blocchetti.dimensioniNG.DimensioniImpresaVO;
import it.csi.findom.blocchetti.blocchetti.formaFinanziamento.FormaFinanziamentoVO;
import it.csi.findom.blocchetti.common.dao.CaratteristicheProgettoDAO;
import it.csi.findom.blocchetti.common.dao.DimensioniDAO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaDettaglioInterventoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * metodi static di validazione invocati via reflection se presenti in variabile di configurazione
 * @author mauro.bottero
 *
 */
public class CaratteristicheProgettoNGValidationMethods implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	/**
	 * nel caso in cui la pagina delle forme di agevolazione sia gia' stata compilata, controlla che l'importo richiesto, salvato in quella pagina, non superi 
	 * il massimo contributo ammesso dall'intervento selezionato; il metodo serve per i bandi in cui il massimo contributo ammissibile 
	 * dipende dalla tipologia intervento scelta (nel caso in cui alcune tipologie intervento non prevedono dei massimi contributi, quelle tipologie 
	 * devono essere passate nell'attributo 'vincoli') 
	 * Nel caso in cui non ci siano interventi selezionati il metodo non fa nulla
	 * @param input
	 * @param logger
	 * @param vincoli un array di stringhe che rappresentano alternativamente degli idTipoIntervento seguiti alla posizione successiva dal max contributo 
	 *                richiedibile corrispondente; il numero di coppie <idTipoIntervento, max contributo> puo' essere qualsiasi
	 * @return
	 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoMaxContributo(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli){
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoMaxContributo] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		//popolo una Map con i vincoli passati: ogni elemento ha come chiave idTipoIntervento e come valore il max contributo ammesso corrispondente
		Map<String, String> maxContributoInterventi = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			maxContributoInterventi.put(vincoli[i], vincoli[i+1]);			
		}

		String ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED = "- per la tipologia intervento selezionata il massimo contributo che è possibile richiedere è inferiore a quello indicato nella pagina 'Agevolazione richiesta' ";		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		FormaFinanziamentoVO formaFinanziamentoVO = input.formaFinanziamentoVO;	
		
		if(formaFinanziamentoVO==null){
			return result;
		}
		
		String totImportoRichiesto = formaFinanziamentoVO.getImportoRichiesto();
		BigDecimal totImportoRichiestoBD;
		try {
			totImportoRichiestoBD = new BigDecimal(totImportoRichiesto.replace(',', '.'));
		} catch (Exception e) {
			return result; // non dovrebbe succedere visto che la pagina forma agevolazione è salvata, comunque prevedo il caso: se l'importo non e' convertibile in BigDecimal non serve proseguire 
		}
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null){
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
			
			for(int i=0; i<tipologiaInterventoList.size();i++)
			{
				TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
			
				if(curTipologia!=null)
				{
					String checked = (String) curTipologia.getChecked();
				
					if(!StringUtils.isBlank(checked) && checked.equals("true"))
					{							
						String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
					
						if(!StringUtils.isBlank(curIdTipoIntervento) &&	maxContributoInterventi.containsKey(curIdTipoIntervento))
						{
							String maxContributoPerCurIdTipoIntervento = maxContributoInterventi.get(curIdTipoIntervento);
							BigDecimal maxContributoPerCurIdTipoInterventoBD = new BigDecimal(maxContributoPerCurIdTipoIntervento.replace(',', '.'));
							
							 if (totImportoRichiestoBD.compareTo(maxContributoPerCurIdTipoInterventoBD)>0){								
								
								if(result==null){
									result = new ArrayList<>();
								}
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								
								segnalazione.setCampoErrore("_caratteristicheProgetto");
								segnalazione.setMsgErrore(ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED);	
								result.add(segnalazione);
								break;
							}								
						}
					}	 
				}
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoMaxContributo]  END");
		return result;
	}

	/**
	 * Controlla che se le dimensioni sono valorizzate, gli interventi selezionati siano compatibili, secondo le regole del bando, con la dimensione di impresa specificata;
	 * il metodo funziona nei casi in cui ci siano una o piu' dimensioni di impresa ciascuna delle quali è incompatibile con una e una sola tipologia di intervento.
	 * Gli idDimensione-descrizione definiti su findom_d_dimensione_impresa sono:
	 * 1-Micro
	 * 2-Piccola
	 * 3-Media
	 * 4-Grande
	 * 
	 * @param input
	 * @param logger
	 * @param vincoli un array di stringhe che rappresentano alternativamente degli idTipoIntervento seguiti alla posizione successiva dall'id di una dimensione
	 * 					di impresa che NON permette, se specificata nella pag delle Dimensioni, di scegliere quel tipo intervento; il numero di coppie 
	 * 					<idTipoIntervento, id dimensione di impresa non compatibile> puo' essere qualsiasi
	 * @return
	 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoDimImpresa(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli){
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoDimImpresa] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		//popolo una Map con i vincoli passati: ogni elemento ha come chiave idTipoIntervento e come valore l'id dimensione impresa NON compatibile con l'intervento
		Map<String, String> interventiDimensioniNonCompatibili = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			interventiDimensioniNonCompatibili.put(vincoli[i], vincoli[i+1]);			
		}
		String ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_DIMENSIONE = "- la tipologia intervento selezionata non è compatibile con la dimensione di Impresa '%s'";		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		DimensioniImpresaVO dimensioniImpresaVO = input.dimensioniImpresaVO;
				
		if(dimensioniImpresaVO==null){
			return result;
		}
		
		String idDimensioneImpresaDomanda = dimensioniImpresaVO.getIdDimensioneImpresa();
	
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null){
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
			for(int i=0; i<tipologiaInterventoList.size();i++){
				TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
				if(curTipologia!=null){
					String checked = (String) curTipologia.getChecked();
					if(!StringUtils.isBlank(checked) && checked.equals("true")){							
						String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
						if(!StringUtils.isBlank(curIdTipoIntervento) &&	interventiDimensioniNonCompatibili.containsKey(curIdTipoIntervento)){
							String idDimensioneNonCompatibile  = interventiDimensioniNonCompatibili.get(curIdTipoIntervento);
							
							
							 if (StringUtils.isNotBlank(idDimensioneNonCompatibile) && StringUtils.isNotBlank(idDimensioneImpresaDomanda)
									&& idDimensioneNonCompatibile.equals(idDimensioneImpresaDomanda)){								
								
								String descrizioneDimensione = "";
								try {
									descrizioneDimensione = DimensioniDAO.getDescrByIdDimensione(idDimensioneImpresaDomanda, logger);
								} catch (CommonalityException e) {
									logger.error("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoDimImpresa]  si e' verificata una eccezione leggendo la descrizione della dimensione impresa \n" +e.getMessage());
								}
								if(result==null){
									result = new ArrayList<>();
								}
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								String msg = String.format(ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_DIMENSIONE,descrizioneDimensione);
								segnalazione.setCampoErrore("_caratteristicheProgetto");
								segnalazione.setMsgErrore(msg);	
								result.add(segnalazione);
								break;
							}								
						}
					}	 
				}
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoDimImpresa]  END");
		return result;
	}
	
	/**
	 * il metodo serve per un caso molto specifico: quello in cui esistano per il bando due tipologie intervento e 
	 * una di queste non sia selezionabile singolarmente e l'altra invece lo sia; 
	 * segnala errorre se c'è una sola tipologa intervento selezionata e questa e' quella non selezionabile singolarmente  
	 * @param input
	 * @param logger
	 * @param idInterventoNoSelezSIngola
	 * @param idInterventoSelSingolaAmmessa (serve per il messaggio di errore che deve contenere anche la descrizione di questa tipologia
	 * @return
	 */
	public static ArrayList<SegnalazioneErrore> ctrlInterventoNonSelezSingolarmente(CaratteristicheProgettoNGInput input, Logger logger, 
			        String idInterventoNoSelezSIngola, String idInterventoSelSingolaAmmessa){
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlInterventoNonSelezSingolarmente] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || StringUtils.isBlank(idInterventoNoSelezSIngola) || StringUtils.isBlank(idInterventoSelSingolaAmmessa)) {
			return result;
		}
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
	
		ArrayList<String> interventiSelezionatiList = new ArrayList<>();
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null){
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
			for(int i=0; i<tipologiaInterventoList.size();i++){
				TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
				if(curTipologia!=null){
					String checked = (String) curTipologia.getChecked();
					if(!StringUtils.isBlank(checked) && checked.equals("true")){							
						String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
						interventiSelezionatiList.add(curIdTipoIntervento);
					}
				}
			}
			if(!interventiSelezionatiList.isEmpty() && interventiSelezionatiList.size()==1){
			   if(interventiSelezionatiList.contains(idInterventoNoSelezSIngola)){
				   //errore; devo recuperare dal db le descrizioni dei due interventi per costruire il messaggio di errore
				   String descrInterventoNoSelezSingola = "";
				   String descrInterventoSelSingolaAmmessa = "";
					try {
						descrInterventoNoSelezSingola = CaratteristicheProgettoDAO.getDescrTipolInterventoById(idInterventoNoSelezSIngola, logger);
						descrInterventoSelSingolaAmmessa = CaratteristicheProgettoDAO.getDescrTipolInterventoById(idInterventoSelSingolaAmmessa, logger); 
					} catch (CommonalityException e1) {
						logger.error("[CaratteristicheProgettoNGValidationMethods::ctrlInterventoNonSelezSingolarmente] si e' verificata una eccezione leggendo dal DB le descrizioni degl interventi \n" + e1);
					} 
				    if(result==null){
						result = new ArrayList<>();
					}
					SegnalazioneErrore segnalazione = new SegnalazioneErrore();
					
//					String msg = "E' possibile selezionare solo SVILUPPO SPERIMENTALE ma non solo RICERCA INDUSTRIALE";
					String ERRMSG_INTERVENTO_NON_SELEZIONABILE_SINGOLARMENTE = "E' possibile selezionare solo '%s' ma non solo '%s'";
					
					String msg = String.format(ERRMSG_INTERVENTO_NON_SELEZIONABILE_SINGOLARMENTE,descrInterventoSelSingolaAmmessa,descrInterventoNoSelezSingola );
					segnalazione.setCampoErrore("_caratteristicheProgetto");
					segnalazione.setMsgErrore(msg);	
					result.add(segnalazione);							
			   }				   
			}			
		}						 
		
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlInterventoNonSelezSingolarmente]  END");
		return result;
	}
	
	
	/** Jira: CR5 Condomini - */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoDettaglio(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli)
	{
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoDettaglio] BEGIN");
	
		ArrayList<SegnalazioneErrore> result = null;		
		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		String idInterventoObbligatorio = "";
		String idDettaglioObbligatorio = "";
		
		Map<String, String> dettagliInterventiCompatibili = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			dettagliInterventiCompatibili.put(vincoli[i], vincoli[i+1]);
			
			idInterventoObbligatorio = vincoli[i];
			logger.info("idInterventoObbligatorio: " + idInterventoObbligatorio);
			
			idDettaglioObbligatorio = vincoli[i+1];
			logger.info("idDettaglioObbligatorio: " + idDettaglioObbligatorio);
		}
		
	   	List<String> dettagliSelezionaliList = new ArrayList<String>();
		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null)
		{
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));

			if(tipologiaInterventoList!=null){	
				for(int i=0; i<tipologiaInterventoList.size();i++){
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 

					if(curTipologia!=null){
						String checked = (String) curTipologia.getChecked();
						logger.info("dbg1: check vale: " + checked);
						
						if(!StringUtils.isBlank(checked) && checked.equals("true")){	
							TipologiaDettaglioInterventoVO[] curDettaglioList = curTipologia.getDettaglioInterventoList();

							if(curDettaglioList != null && curDettaglioList.length > 0){
								for(int j=0; j<curDettaglioList.length; j++){
									TipologiaDettaglioInterventoVO curDettaglio=curDettaglioList[j];					                  
								
									if(curDettaglio!=null){
										String checkedDett = curDettaglio.getChecked();
										logger.info("dbg2: checkedDett vale: " + checkedDett);
										
										if(!StringUtils.isBlank(checkedDett) && checkedDett.equals("true")){
											logger.info("dbg3: " + curDettaglio.getIdDettIntervento());
											dettagliSelezionaliList.add(curDettaglio.getIdDettIntervento());
										}
									}			            
								}
								
							} else {
								logger.info("nessun dettaglio selezionato!");
							}
							
							// se il dettaglio selezionato non contiene il vincolo richiesto, restituisce msg errore
							if(!dettagliSelezionaliList.contains(idDettaglioObbligatorio))
							{
								// logger.info("dbg4a: puoi salvare!");
								logger.info("dbg4b: Non puoi salvare!");
								
								if( result == null ){
									result = new ArrayList<>();
								}
								
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								String descrDettaglioSelSingolaAmmessa = "";
								
								try {
									descrDettaglioSelSingolaAmmessa = CaratteristicheProgettoDAO.getDescrDettaglioById(idDettaglioObbligatorio, logger);
									
								} catch (CommonalityException e2) {
									logger.error("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoDimImpresa] si e' verificata una eccezione leggendo dal DB le descrizioni dei dettagli interventi \n" + e2);
								} 
								
								String ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_DETTAGLIO_OBBLIGATORIO = "- E' obbligatorio selezionare " + descrDettaglioSelSingolaAmmessa;
								String msg = String.format(ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_DETTAGLIO_OBBLIGATORIO, descrDettaglioSelSingolaAmmessa);
								segnalazione.setCampoErrore("_caratteristicheProgetto");
								segnalazione.setMsgErrore(msg);	
								result.add(segnalazione);
								break;
							} 
						} 
					}//chiude test null su curTipologia			 
				}//chiude for su tipologiaInterventoList
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoDimImpresa]  END");
		return result;
	}
	
	
	/** Jira: 1808 FSE-Misura-3 - */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaBeneficiarioDettaglio(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli)
	{
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaBeneficiarioDettaglio] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		String idTipologiaBeneficiario = "";
		String idDettaglioObbligatorio = "";
		boolean isEqual = false;
		
		Map<String, String> dettagliInterventiCompatibili = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			dettagliInterventiCompatibili.put(vincoli[i], vincoli[i+1]);
			
			idTipologiaBeneficiario = vincoli[i];
			logger.info("idTipologiaBeneficiario: " + idTipologiaBeneficiario);
			
			idDettaglioObbligatorio = vincoli[i+1];
			logger.info("idDettaglioObbligatorio: " + idDettaglioObbligatorio);
		}
		
	   	List<String> dettagliSelezionaliList = new ArrayList<String>();
		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		DomandaNGVO domandaNGVO = input._domanda;
		
		Map<String, String> dettagliBeneficiarioCompatibili = new HashMap<>();
		String codTipoBeneficiario = null;
		
		if ( domandaNGVO != null ) {
			codTipoBeneficiario = (String)domandaNGVO.getCodiceTipologiaBeneficiario();
			logger.info("codTipoBeneficiario: " + codTipoBeneficiario);
		}
		
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null)
		{
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));

			if(tipologiaInterventoList!=null){	
				for(int i=0; i<tipologiaInterventoList.size();i++){
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 

					if(curTipologia!=null){
						String checked = (String) curTipologia.getChecked();
						logger.info("dbg1: check vale: " + checked);
						
						if(!StringUtils.isBlank(checked) && checked.equals("true")){	
							TipologiaDettaglioInterventoVO[] curDettaglioList = curTipologia.getDettaglioInterventoList();
							
							if(curDettaglioList != null && curDettaglioList.length > 0){
								for(int j=0; j<curDettaglioList.length; j++){
									TipologiaDettaglioInterventoVO curDettaglio=curDettaglioList[j];					                  
									
									if(curDettaglio != null )
									{
										String checkedDett = curDettaglio.getChecked();
										logger.info("dbg2: checkedDett vale: " + checkedDett);
										
										if(!StringUtils.isBlank(checkedDett) && checkedDett.equals("true"))
										{
											logger.info("dbg3: " + curDettaglio.getIdDettIntervento());
											dettagliBeneficiarioCompatibili.put(codTipoBeneficiario, curDettaglio.getIdDettIntervento());
										}
									}			            
								}
								
							} else {
								logger.info("nessun dettaglio selezionato!");
							}
							
							isEqual = MetodiUtili.compare(dettagliInterventiCompatibili, dettagliBeneficiarioCompatibili, logger);
							logger.info("isEqual ? " + isEqual);
							
							if(!isEqual)
							{
								logger.info("dbg4b: Non puoi salvare!");
								
								if( result == null ){
									result = new ArrayList<>();
								}
								
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								String descrDettaglioSelSingolaAmmessa = "";
								
								try {
									String idDettObbByBeneficiario = "";
									for (Entry<String, String> entry : dettagliInterventiCompatibili.entrySet()) {
										if(codTipoBeneficiario.equals(entry.getKey())){
											idDettObbByBeneficiario = entry.getValue();
										}
									}
									
									if(idDettObbByBeneficiario!=null){
										descrDettaglioSelSingolaAmmessa = CaratteristicheProgettoDAO.getDescrDettaglioById(idDettObbByBeneficiario, logger);
									}else{
										logger.info("errore!");
									}
									
								} catch (CommonalityException e2) {
									logger.error("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaBeneficiarioDettaglio] si e' verificata una eccezione leggendo dal DB le descrizioni dei dettagli interventi \n" + e2);
								} 
								
								String ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_DETTAGLIO_OBBLIGATORIO = "- E' obbligatorio selezionare " + descrDettaglioSelSingolaAmmessa;
								String msg = String.format(ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_DETTAGLIO_OBBLIGATORIO, descrDettaglioSelSingolaAmmessa);
								segnalazione.setCampoErrore("_caratteristicheProgetto");
								segnalazione.setMsgErrore(msg);	
								result.add(segnalazione);
								break;
							} 
						} 
					}//chiude test null su curTipologia			 
				}//chiude for su tipologiaInterventoList
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaBeneficiarioDettaglio]  END");
		return result;
	}
	
	/** 
	 * Jira: 1844 
	 * ctrlCoerenzaStereotipoIntervento
	 **/
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaStereotipoIntervento(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli)
	{
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaStereotipoIntervento] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		String idTipologiaBeneficiarioObbligatorio = "";
		String idInterventoErrato = "";
		boolean isError = false;
		
		Map<String, String> dettagliInterventiCompatibili = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			dettagliInterventiCompatibili.put(vincoli[i], vincoli[i+1]);
			
			idTipologiaBeneficiarioObbligatorio = vincoli[i];
			logger.info("idTipologiaBeneficiarioObbligatorio: " + idTipologiaBeneficiarioObbligatorio);
			
			idInterventoErrato = vincoli[i+1];
			logger.info("idInterventoErrato: " + idInterventoErrato);
		}
		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		DomandaNGVO domandaNGVO = input._domanda;
		
		String codTipoBeneficiario = null;
		
		if ( domandaNGVO != null ) {
			codTipoBeneficiario = (String)domandaNGVO.getCodiceTipologiaBeneficiario();
			logger.info("codTipoBeneficiario: " + codTipoBeneficiario);
		}
		
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null)
		{
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));

			if(tipologiaInterventoList!=null)
			{	
				for(int i=0; i<tipologiaInterventoList.size();i++)
				{
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
					logger.info("dbg: curTipologia vale: " + curTipologia.getIdTipoIntervento());

					if(curTipologia!=null)
					{
						String checked = (String) curTipologia.getChecked();
						logger.info("dbg: check vale: " + checked);
						
						if(!StringUtils.isBlank(checked) && checked.equals("true"))
						{	
							String idTipoIntervento = curTipologia.getIdTipoIntervento();
							logger.info("dbg: idTipoIntervento vale: " + idTipoIntervento);
							
							if(idTipoIntervento != null && idTipoIntervento.equals(idInterventoErrato)) {
								if(codTipoBeneficiario != null && codTipoBeneficiario.equalsIgnoreCase(idTipologiaBeneficiarioObbligatorio)) 
								{
									isError = true;
								} 
								else {
									logger.info("puoi salvare!");
								}
							}
							
							logger.info("isError ? " + isError);
							
							if(isError)
							{
								logger.info("dbg: Non puoi salvare!");
								
								if( result == null ) {
									result = new ArrayList<>();
								}
								
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								String descrDettaglioSelSingolaAmmessa = "";
								
								String idIntErratiByBeneficiario = "";
								for (Entry<String, String> entry : dettagliInterventiCompatibili.entrySet()) {
									if(codTipoBeneficiario.equals(entry.getKey())){
										idIntErratiByBeneficiario = entry.getValue();
									}
								}
								
								if(idIntErratiByBeneficiario!=null){
									descrDettaglioSelSingolaAmmessa = "La tipologia di intervento selezionata &#232; disponibile solo per Micro Impresa, Piccola Impresa e Media Impresa";
								}else{
									logger.info("errore!");
								} 
								
								String ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_INTERVENTO_OBBLIGATORIO = descrDettaglioSelSingolaAmmessa;
								String msg = String.format(ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_INTERVENTO_OBBLIGATORIO, descrDettaglioSelSingolaAmmessa);
								segnalazione.setCampoErrore("_caratteristicheProgetto");
								segnalazione.setMsgErrore(msg);	
								result.add(segnalazione);
								break;
							} 
						} 
					}//chiude test null su curTipologia			 
				}//chiude for su tipologiaInterventoList
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaStereotipoIntervento]  END");
		return result;
	}
	
	
	/** 
	 * Jira:  Asd -
	 * ctrlCoerenzaInterventoAssociazione
	 **/
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoAssociazione(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli)
	{
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoAssociazione] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		String idTipoIntervento = "";
		boolean isError = false;
		
		Map<String, String> interventiCompatibiliMap = new HashMap<>();
		List<String> idInterventoObblg = new ArrayList<String>();
		
		for (int i = 0; i < vincoli.length; i= i+2) {
			
			interventiCompatibiliMap.put(vincoli[i], vincoli[i+1]);
			logger.info("vincoli[i]: "+vincoli[i] + " vincoli[i+1]: "+vincoli[i+1]);
			
			logger.info("Beneficiario obbligatorio: "+vincoli[i].substring(0,4)); 
			
			idInterventoObblg.add(vincoli[i+1]);
			logger.info("idTipologiaInterventoObblg: " + vincoli[i+1]);
		}
		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		DomandaNGVO domandaNGVO = input._domanda;
		
		String codTipoBeneficiario = null;
		
		if ( domandaNGVO != null ) {
			codTipoBeneficiario = (String)domandaNGVO.getCodiceTipologiaBeneficiario();
			logger.info("codTipoBeneficiario: " + codTipoBeneficiario);
		}
		
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null) {
			
			List<TipologiaInterventoVO> tipologiaInterventoList = 
					new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));

			if(tipologiaInterventoList!=null) {	
				for(int i=0; i<tipologiaInterventoList.size();i++) {
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
					logger.info("dbg: curTipologia vale: " + curTipologia.getIdTipoIntervento());

					if(curTipologia!=null) {
						String checked = (String) curTipologia.getChecked();
						logger.info("dbg: check vale: " + checked);
						
						if(!StringUtils.isBlank(checked) && checked.equals("true")) {	
							idTipoIntervento = curTipologia.getIdTipoIntervento();
							logger.info("dbg: idTipoIntervento vale: " + idTipoIntervento);
							
							if(idTipoIntervento != null && !idTipoIntervento.isEmpty()) {
								
								for (Map.Entry<String, String> entry : interventiCompatibiliMap.entrySet()) {
									logger.info("Beneficiario : " + entry.getKey().substring(0,4) + " idInterventoObbligatorio : " + entry.getValue());

									if(codTipoBeneficiario.equals(entry.getKey().substring(0,4))) {
										if(interventiCompatibiliMap.containsValue(idTipoIntervento)) {
											isError=false;
											break;
								        }else{
								        	isError=true;
								        	break;
								        }
									}
								}// fine for error
							}
						}
					}
				}// fine for interventi selezionati
				
				logger.info("isError ? " + isError);
				
				if(isError)
				{
					logger.info("dbg: Non puoi salvare!");
					
					if( result == null ) {
						result = new ArrayList<>();
					}
					
					SegnalazioneErrore segnalazione = new SegnalazioneErrore();
					String descrDettaglioSelSingolaAmmessa1 = "La tipologia di intervento selezionata &#232; disponibile solo per:<b> ";
					
					String idIntErratiByBeneficiario = "";
					for (Entry<String, String> entry : interventiCompatibiliMap.entrySet()) {
						if(codTipoBeneficiario.equals(entry.getKey().substring(0,4))){
							idIntErratiByBeneficiario = entry.getValue();
						}
					}
					
					if(idIntErratiByBeneficiario!=null){
						try {
						
							String descrDettaglioSelSingolaAmmessa2 = CaratteristicheProgettoDAO.getDescrTipolInterventoById(idTipoIntervento, logger);
							String ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_INTERVENTO_OBBLIGATORIO = descrDettaglioSelSingolaAmmessa1.concat(descrDettaglioSelSingolaAmmessa2)+"</b>";
							String msg = String.format(ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_INTERVENTO_OBBLIGATORIO);
							segnalazione.setCampoErrore("_caratteristicheProgetto");
							segnalazione.setMsgErrore(msg);	
							result.add(segnalazione);

						} catch (CommonalityException e) {
							e.printStackTrace();
						}
					}else{
						logger.info("errore!");
					} 
				}
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoAssociazione]  END");
		return result;
	}
	
	/** 
	 * Jira:  Asd -
	 * ctrlCoerenzaInterventoFederazione
	 **/
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaInterventoFederazione(CaratteristicheProgettoNGInput input, Logger logger, String... vincoli)
	{
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoFederazione] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		String idTipoIntervento = "";
		boolean isError = false;
		
		Map<String, String> interventiCompatibiliMap = new HashMap<>();
		List<String> idInterventoObblg = new ArrayList<String>();
		
		for (int i = 0; i < vincoli.length; i= i+2) {
			
			interventiCompatibiliMap.put(vincoli[i], vincoli[i+1]);
			logger.info("vincoli[i]: "+vincoli[i] + " vincoli[i+1]: "+vincoli[i+1]);
			
			logger.info("Beneficiario obbligatorio: "+vincoli[i]); 
			
			idInterventoObblg.add(vincoli[i+1]);
			logger.info("idTipologiaInterventoObblg: " + vincoli[i+1]);
		}
		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input._caratteristicheProgetto;
		DomandaNGVO domandaNGVO = input._domanda;
		
		String codTipoBeneficiario = null;
		
		if ( domandaNGVO != null ) {
			codTipoBeneficiario = (String)domandaNGVO.getCodiceTipologiaBeneficiario();
			logger.info("codTipoBeneficiario: " + codTipoBeneficiario);
		}
		
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null) {
			
			List<TipologiaInterventoVO> tipologiaInterventoList = 
					new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));

			if(tipologiaInterventoList!=null) {	
				for(int i=0; i<tipologiaInterventoList.size();i++) {
					TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
					logger.info("dbg: curTipologia vale: " + curTipologia.getIdTipoIntervento());

					if(curTipologia!=null) {
						String checked = (String) curTipologia.getChecked();
						logger.info("dbg: check vale: " + checked);
						
						if(!StringUtils.isBlank(checked) && checked.equals("true")) {	
							idTipoIntervento = curTipologia.getIdTipoIntervento();
							logger.info("dbg: idTipoIntervento vale: " + idTipoIntervento);
							
							if(idTipoIntervento != null && !idTipoIntervento.isEmpty()) {
								
								for (Map.Entry<String, String> entry : interventiCompatibiliMap.entrySet()) {
									logger.info("Beneficiario : " + entry.getKey() + " idInterventoObbligatorio : " + entry.getValue());

									if(codTipoBeneficiario.equals(entry.getKey())) {
										if(interventiCompatibiliMap.containsValue(idTipoIntervento)) {
											isError=false;
											break;
								        }else{
								        	isError=true;
								        	break;
								        }
									}
								}
							}
						}
					}
				}
				
				logger.info("isError ? " + isError);
				
				if(isError)
				{
					logger.info("dbg: Non puoi salvare!");
					
					if( result == null ) {
						result = new ArrayList<>();
					}
					
					SegnalazioneErrore segnalazione = new SegnalazioneErrore();
					String descrDettaglioSelSingolaAmmessa1 = "La tipologia di intervento selezionata &#232; disponibile solo per:<b> ";
					
					String idIntErratiByBeneficiario = "";
					for (Entry<String, String> entry : interventiCompatibiliMap.entrySet()) {
						if(codTipoBeneficiario.equals(entry.getKey())){
							idIntErratiByBeneficiario = entry.getValue();
						}
					}
					
					if(idIntErratiByBeneficiario!=null){
						try {
						
							String descrDettaglioSelSingolaAmmessa2 = CaratteristicheProgettoDAO.getDescrTipolInterventoById(idTipoIntervento, logger);
							String ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_INTERVENTO_OBBLIGATORIO = descrDettaglioSelSingolaAmmessa1.concat(descrDettaglioSelSingolaAmmessa2)+"</b>";
							String msg = String.format(ERRMSG_INTERVENTO_NON_COMPATIBILE_CON_INTERVENTO_OBBLIGATORIO);
							segnalazione.setCampoErrore("_caratteristicheProgetto");
							segnalazione.setMsgErrore(msg);	
							result.add(segnalazione);

						} catch (CommonalityException e) {
							e.printStackTrace();
						}
					}else{
						logger.info("errore!");
					} 
				}
			}
		}
		logger.info("[CaratteristicheProgettoNGValidationMethods::ctrlCoerenzaInterventoFederazione]  END");
		return result;
	}
}
