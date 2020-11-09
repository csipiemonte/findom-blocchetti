/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.pianospese.DettaglioCostiVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import freemarker.core.Environment.Namespace;

/**
 * metodi static di validazione invocati via reflection se presenti in variabile di configurazione
 * @author mauro.bottero
 *
 */
public class FormaFinanziamentoValidationMethods implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public FormaFinanziamentoValidationMethods(TreeMap<String, Object> context, Namespace namespace, TreeMap<String, Object> model) {
		super();
	}
	
	/**
	 *  controlla che non sia stato superato il contributo massimo ammesso in forma finanziamento; il limite massimo varia in base all'intervento selezionato.
	 *  Se esistono interventi che non prevedono un limite massimo, questi non devono essere passati nel parametro 'vincoli'
	 * Il metodo assume che l'importo richiesto digitato dall'utente sia un numero ben formato; se non e' cosi' il metodo non fa nulla
	 * @param input  l'oggetto FormaFinanziamentoInput associato al blocchetto formaFinanziamento 
	 * @param logger
	 * @param vincoli un array di String rappresentanti, in una generica posizione 'i-1', un idTipoIntervento e alla posizione 'i' 
	 *                il massimo contributo ammesso per quell'idTipoIntervento
	 * @return
	 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaMaxContributoIntervento(FormaFinanziamentoInput input, Logger logger, String... vincoli){
		logger.info("[FormaFinanziamentoValidationMethods::ctrlCoerenzaMaxContributoIntervento] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		//popolo una Map con i vincoli passati: ogni elemento ha come chiave idTipoIntervento e come valore il max contributo ammesso corrispondente
		Map<String, String> maxContributoInterventi = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			maxContributoInterventi.put(vincoli[i], vincoli[i+1]);			
		}

		String ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED = "- l'importo inserito supera il contributo massimo previsto per la tipologia intervento selezionata nella pagina 'Informazioni sul progetto'";		
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;
		FormaFinanziamentoVO formaFinanziamentoVO = input._formaFinanziamento;	
		String totImportoRichiesto = formaFinanziamentoVO.getImportoRichiesto();
		BigDecimal totImportoRichiestoBD;
		try {
			totImportoRichiestoBD = new BigDecimal(totImportoRichiesto.replace(',', '.'));
		} catch (Exception e) {
			return result; // se l'importo non e' convertibile in BigDecimal si assume che altri controlli abbiano gia' segnalato l'errore e il metodo termina 
		}	
		
		if (caratteristicheProgettoNGVO!=null && caratteristicheProgettoNGVO.getTipologiaInterventoList()!= null){
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(caratteristicheProgettoNGVO.getTipologiaInterventoList()));
			for(int i=0; i<tipologiaInterventoList.size();i++){
				TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 
				if(curTipologia!=null){
					String checked = (String) curTipologia.getChecked();
					if(!StringUtils.isBlank(checked) && checked.equals("true")){							
						String curIdTipoIntervento = curTipologia.getIdTipoIntervento();
						if(!StringUtils.isBlank(curIdTipoIntervento) &&	maxContributoInterventi.containsKey(curIdTipoIntervento)){
							String maxContributoPerCurIdTipoIntervento = maxContributoInterventi.get(curIdTipoIntervento);
							BigDecimal maxContributoPerCurIdTipoInterventoBD = new BigDecimal(maxContributoPerCurIdTipoIntervento.replace(',', '.'));
							
							 if (totImportoRichiestoBD.compareTo(maxContributoPerCurIdTipoInterventoBD)>0){								
								
								if(result==null){
									result = new ArrayList<>();
								}
								SegnalazioneErrore segnalazione = new SegnalazioneErrore();
								
								segnalazione.setCampoErrore("_formaFinanziamento");
								segnalazione.setMsgErrore(ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED);	
								result.add(segnalazione);
								break;
							}								
						}
					}	 
				}
			}
		}
		logger.info("[FormaFinanziamentoValidationMethods::ctrlCoerenzaMaxContributoIntervento] END");
		return result;
	}
	
	
	/**
	 * : Jira: 1696 - 
	 **/
	public static ArrayList<SegnalazioneErrore> ctrlContributoFinanziamento(FormaFinanziamentoInput input, Logger logger, String idInterventoRichiesto)
	{
		logger.info("[FormaFinanziamentoValidationMethods::ctrlContributoFinanziamento] BEGIN");

		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || StringUtils.isBlank(idInterventoRichiesto)) {
			return result;
		}
		
		String ERRMSG_SELEZIONE_AGEVOLAZIONE_NON_AMMESSA = "- La forma di agevolazione 'Finanziamento a tasso agevolato' deve essere selezionata obbligatoriamente.";				

		String idInterventoObbligatorio = idInterventoRichiesto;
		logger.info("idInterventoObbligatorio: " + idInterventoObbligatorio);
		
		Map<String, String> finSelezionatiMap = new HashMap<>();
		
		FormaFinanziamentoVO _formaFinanziamento = input._formaFinanziamento;
		
		if(_formaFinanziamento != null) {
			List<TipoFormaFinanziamentoVO> formaFinanziamentoSalvataList = Arrays.asList(_formaFinanziamento.getFormaFinanziamentoList());
			
			if(formaFinanziamentoSalvataList!=null) {
				for(int i=0; i<formaFinanziamentoSalvataList.size(); i++){
					TipoFormaFinanziamentoVO curFormaFinanziamento = (TipoFormaFinanziamentoVO)formaFinanziamentoSalvataList.get(i);
					
					if(curFormaFinanziamento != null) {
						 logger.info("[FormaFinanziamentoValidationMethods::ctrlContributoFinanziamento] ciclo sui dati dell'xml: ");
						 
						 if(curFormaFinanziamento != null){
							String checked = curFormaFinanziamento.getChecked();
								
							if(checked.equals("true")) {
								String interventoSelezionato  =  curFormaFinanziamento.getCodFormaFinanziamento();
								logger.info("interventoSelezionato: " + interventoSelezionato);
								
								finSelezionatiMap.put(interventoSelezionato, curFormaFinanziamento.getDescrFormaFinanziamento());
							} // fine controllo checked
						 }
					}
				}// fine for
			}// fine if forme finanziamento selezionate
	    }
		
		if ( idInterventoObbligatorio != null) {
			// Eseguo verifica presenza finanziamento richiesto se selezionato		
			if (finSelezionatiMap.containsKey(idInterventoObbligatorio)) {

				logger.info("isFinanziamentoRichiesto: puoi salvare!");

			} else {
				logger.info("[FormaFinanziamentoValidationMethods::ctrlContributoIntervento] la tipologia di finanziamento richiesta per poter salvare non e' stata selezionata. ");

				if (result == null) {
					result = new ArrayList<>();
				}

				SegnalazioneErrore segnalazione = new SegnalazioneErrore();
				segnalazione.setCampoErrore("_formaFinanziamento");
				segnalazione.setMsgErrore(ERRMSG_SELEZIONE_AGEVOLAZIONE_NON_AMMESSA);
				result.add(segnalazione);
			}
		}
		
		logger.info("[FormaFinanziamentoValidationMethods::ctrlContributoFinanziamento] END");
		return result;
	}
	
	/** Jira: 1671 -  */
	public static ArrayList<SegnalazioneErrore> ctrlLimiteMaxImpRichiesto(FormaFinanziamentoInput input, Logger logger, String... vincoli)
	{
		logger.info("[FormaFinanziamentoValidationMethods::ctrlLimiteMaxImpRichiesto] BEGIN");
		
		String idFormaFinanziamento = "0";
		String importoRichiesto = "0";
		String percPrevista = "0"; // 100%
		String limiteMax = "0"; // 500000
		String limiteMin = "0";
		
		boolean isLimiteImporto = false;
		
		ArrayList<SegnalazioneErrore> result = null;		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		//popolo una Map con i vincoli passati: ogni elemento ha come chiave idTipoIntervento e come valore il max contributo ammesso corrispondente
		Map<String, String> maxContributoInterventi = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			maxContributoInterventi.put(vincoli[i], vincoli[i+1]);			
		}
		
		
		// debug:
		logger.info("[FormaFinanziamentoValidationMethods::ctrlLimiteMaxImpRichiesto] debug: ");
		logger.info(maxContributoInterventi);
		
		String ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED = "- Importo inserito supera il contributo massimo previsto per la forma di agevolazione selezionata.";	
		
		// CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;
		FormaFinanziamentoVO formaFinanziamentoVO = input._formaFinanziamento;	
		
		
		if(formaFinanziamentoVO!=null) 
		{
			List<TipoFormaFinanziamentoVO> formaFinanziamentoList = (List<TipoFormaFinanziamentoVO>)Arrays.asList(formaFinanziamentoVO.getFormaFinanziamentoList());

			if(formaFinanziamentoList!=null)
			{ 
				for(int i=0; i<formaFinanziamentoList.size();i++)
				{		    	   
					TipoFormaFinanziamentoVO curFormaFin=(TipoFormaFinanziamentoVO)formaFinanziamentoList.get(i);
					
					if(curFormaFin!=null)
					{
						String checked = (String) curFormaFin.getChecked();
						idFormaFinanziamento = (String) curFormaFin.getIdFormaFinanziamento();
						logger.info("FormaFinanziamento::modelValidate] idFormaFinanziamento: " + idFormaFinanziamento); // 19
						
						if(!StringUtils.isBlank(checked) && checked.equals("true"))
						{
							if(!StringUtils.isBlank(idFormaFinanziamento) && maxContributoInterventi.containsKey(idFormaFinanziamento))
							{
								importoRichiesto = (String) curFormaFin.getImportoFormaAgevolazione();						
								logger.info("FormaFinanziamento::modelValidate] importoRichiesto: " + importoRichiesto); // 510000
								
								if(importoRichiesto.matches("^\\d+(,\\d{1,2})?$"))
								{
									percPrevista = curFormaFin.getPercPrevista().toString();
									logger.info("FormaFinanziamento::modelValidate] percPrevista: " + percPrevista); // 100.00
									
									if(!StringUtils.isBlank(percPrevista))
									{
										BigDecimal percPrevistaBD = new BigDecimal(0); // 70
										percPrevista = percPrevista.replace(',', '.');
										percPrevistaBD = new BigDecimal(percPrevista);	
										logger.info("FormaFinanziamento::modelValidate] percPrevistaBD: " + percPrevistaBD);
										
										BigDecimal importoRichiestoBD = new BigDecimal(0);	  
										importoRichiesto = importoRichiesto.replace(',', '.');
										importoRichiestoBD = new BigDecimal(importoRichiesto);	
										logger.info("FormaFinanziamento::modelValidate] importoRichiestoBD: " + importoRichiestoBD);
										
										for (Map.Entry<String, String> entry : maxContributoInterventi.entrySet()) {
								            if (idFormaFinanziamento.equals(entry.getKey())) {
								            	limiteMax = entry.getValue();
								            }
								        }
										logger.info("limiteMax : " + limiteMax);
								        
										isLimiteImporto = MetodiUtili.isLimitiMinMaxCorretti( importoRichiesto, percPrevista, limiteMax, limiteMin, logger);
										logger.info("isLimiteImporto: " + isLimiteImporto);
								        
										if (!isLimiteImporto)
										{								
											if(result==null)
											{
												result = new ArrayList<>();
											}
											
											SegnalazioneErrore segnalazione = new SegnalazioneErrore();
											
											segnalazione.setCampoErrore("_formaFinanziamento");
											segnalazione.setMsgErrore(ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED);	
											result.add(segnalazione);
											break;
										}		
										
										
									} // fine test percPrevista
								} // fine test importoRichiesto
							}
								
						} // fine test checked
					} //fine test null su curFormaFin
				 } //fine for su formaFinanziamentoList
			   }
		    }
			logger.info("[FormaFinanziamentoValidationMethods::ctrlLimiteMaxImpRichiesto] END");
		
		return result;
	} // fine Jira: 1671

	
	/** Jira: 1855 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaContribRichSommaCatg(FormaFinanziamentoInput input, Logger logger, String... vincoli)
	{
		logger.info("[FormaFinanziamentoValidationMethods::ctrlCoerenzaContribRichSommaCatg] BEGIN");
	
		ArrayList<SegnalazioneErrore> result = null;		
		
		if(input==null || vincoli == null || vincoli.length==0) {
			return result;
		}
		
		Map<String, String> vincoliVoceSpesaMap = new HashMap<>();
		for (int i = 0; i < vincoli.length; i= i+2) {
			vincoliVoceSpesaMap.put(vincoli[i], vincoli[i+1]);			
		}
		
		String ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED = "- l'importo inserito supera il contributo massimo previsto <br/> per la tipologia intervento selezionata nella pagina 'Informazioni sul progetto'";		
		FormaFinanziamentoVO formaFinanziamentoVO = input._formaFinanziamento;	
		String totImportoRichiesto = formaFinanziamentoVO.getImportoRichiesto();
		BigDecimal totImportoRichiestoBD;
		try {
			totImportoRichiestoBD = new BigDecimal(totImportoRichiesto.replace(',', '.'));
		} catch (Exception e) {
			return result;
		}	
		
		PianoSpeseVO _pianoSpese = input._pianoSpese;
		List<DettaglioCostiVO> dettaglioCostiInseritiList = new ArrayList<DettaglioCostiVO>();
		
		String idVoceSpesa = "";
		String strImportoProposto = "";
		String descrizioneVoceSpesa = "";
		
		BigDecimal newTotale = new BigDecimal(0);
		if(_pianoSpese !=null && _pianoSpese.getDettaglioCostiList() != null)
		{
			dettaglioCostiInseritiList = (List<DettaglioCostiVO>)Arrays.asList(_pianoSpese.getDettaglioCostiList());
			
			if(dettaglioCostiInseritiList!=null && !dettaglioCostiInseritiList.isEmpty())
			{ 	
				for(int x=0; x<dettaglioCostiInseritiList.size(); x++)
				{
				    DettaglioCostiVO curDettaglioCosto = null;
			        curDettaglioCosto = (DettaglioCostiVO)dettaglioCostiInseritiList.get(x);
			        
			        if(curDettaglioCosto!=null)
			        {
			        	idVoceSpesa= curDettaglioCosto.getIdVoceSpesa();
			        	logger.info("idVoceSpesa: " +idVoceSpesa);
			        	
			        	descrizioneVoceSpesa = curDettaglioCosto.getDescrVoceSpesa();
			        	logger.info("descrizioneVoceSpesa: " +descrizioneVoceSpesa);
			        	
			        	// ciclo su mappa
			        	for (Map.Entry<String, String> entry : vincoliVoceSpesaMap.entrySet()) 
			        	{
			        		logger.info("key : " + entry.getKey() + " value : " + entry.getValue());
			        		
			        		if( idVoceSpesa != null && !StringUtils.isBlank(idVoceSpesa) && idVoceSpesa.equals(entry.getValue()))
			        		{
		        				String descrVoceSpesaSubstring = "";
		        				if(descrizioneVoceSpesa != null && !StringUtils.isBlank(descrizioneVoceSpesa))
		        				{
		        					descrVoceSpesaSubstring = descrizioneVoceSpesa.substring(0,3);
		        				}
		        				logger.info("descrVoceSpesaSubstring : " + descrVoceSpesaSubstring);
		        				
		        				if( descrVoceSpesaSubstring != null && !StringUtils.isBlank(descrizioneVoceSpesa) && descrVoceSpesaSubstring.equals(entry.getKey())){
		        					
		        					if(StringUtils.isBlank(curDettaglioCosto.getImportoProposto())){
		        						strImportoProposto = "0";					
		        					}else{
		        						strImportoProposto = (String) curDettaglioCosto.getImportoProposto();
		        					}
		        					
		        					if(strImportoProposto.indexOf(',') != -1){
		        						strImportoProposto = strImportoProposto.replace(',', '.');
		        					}
		        					logger.info("importo proposto string : " +strImportoProposto);
		        					
		        					BigDecimal importoPropostoBD = new BigDecimal(strImportoProposto);	
		        					logger.info("importoPropostoBD: " +importoPropostoBD);
		        					
		        					if(!StringUtils.isBlank(strImportoProposto) && !strImportoProposto.equals("0")){
		        						
		        						if(strImportoProposto.indexOf(',') != -1){
		        							strImportoProposto = strImportoProposto.replace(',', '.');
		        						}	 
		        						
		        						newTotale = newTotale.add(importoPropostoBD);
		        						logger.info("newTotaleBD: " +newTotale);
		        						break;
		        					}
		        				}
			        		}
			        	}
						logger.info("newTotaleBD: " +newTotale);
			        }
				}//chiude for su dettaglioCostiInseritiList
				
				int ris = totImportoRichiestoBD.compareTo(newTotale);
				logger.info("[FormaFinanziamentoValidationMethods::ctrlCoerenzaContribRichSommaCatg] ris: " + ris);
				
				if (ris >0){								
					
					if(result==null){
						result = new ArrayList<>();
					}
					SegnalazioneErrore segnalazione = new SegnalazioneErrore();
					segnalazione.setCampoErrore("_formaFinanziamento");
					segnalazione.setMsgErrore(ERRMSG_CAMPO_CONTRIBUTO_MAX_EXCEDED + " essere di €" + newTotale);	
					result.add(segnalazione);
				}
			}
		}
							 
		logger.info("[FormaFinanziamentoValidationMethods::ctrlCoerenzaContribRichSommaCatg] END");
		return result;
	}
	
}
