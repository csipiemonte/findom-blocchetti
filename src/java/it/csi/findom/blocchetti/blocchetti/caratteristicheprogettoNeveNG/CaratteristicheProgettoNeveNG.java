/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG;

import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class CaratteristicheProgettoNeveNG extends Commonality {

	CaratteristicheProgettoNeveNGInput input = new CaratteristicheProgettoNeveNGInput();
	
	
	

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
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException 
	{
		FinCommonInfo info = (FinCommonInfo) info1;
		
		CaratteristicheProgettoNeveNGOutput output = new CaratteristicheProgettoNeveNGOutput();

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[CaratteristicheProgettoNeveNG::inject] CaratteristicheProgettoNeveNG  BEGIN");
		
		boolean isGrandiStazioni = false;
		
		/** Jira: 1410 
		 * utente ha già compilato il Tab degli indicatori, 
		 * torna sul tab: 'Informazioni sul progetto' e deseleziona la:
		 * - tipologia di intervento "CAT C..." poi salva.
		 * I dati contenuti nel Tab degli indicatori devono essere cancellati su XML
		 * tabella: findom_v_domande_nuova_gestione :
		 * 
		 * MSTAZ1: Micro stazioni private
		 * GSTAZ : Grandi stazioni
		 * MSTAZ2: Micro stazioni pubbliche
		 * 
		 * */
		String isSelectedIdTipoIntervento99 = "false";
		
		//// tipo di beneficiario tramite: flagPubblicoPrivato (1=impresa), (2=pubblico)
		int flagPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);
		logger.info("debug: flagPubblicoPrivato vale= " + flagPubblicoPrivato);
		
		String tipoBeneficiario = "";
		if( flagPubblicoPrivato == 1) {
			tipoBeneficiario = "Impresa";
		}
		logger.info("debug: tipoBeneficiario risulta: " + tipoBeneficiario);
		
		String codeTipoBeneficiario = "";
		codeTipoBeneficiario = DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) != null ? DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) : "";
		logger.info("debug: codeTipoBeneficiario vale= " + codeTipoBeneficiario);
		
		//// dichiarazioni
		List<TipologiaInterventoNeveVO> tipologiaInterventoList = new ArrayList<>();
		
		String esistonoDettagli = "true";
		String viewWarningSpese = "false";
		
		/** : Richiesta per tutti i bandi di sostituire label:
		 *  - Caratteristiche del progetto di investimento
		 *  con: Caratteristiche del progetto rif.: DT
		 **/
		String caratteristicheProgettoLabel = "Caratteristiche del progetto"; // MB2016_11_18 ini

		String idDomanda = info.getStatusInfo().getNumProposta() + "";
		logger.info("[Debug:]  idDomanda vale = " + idDomanda);
		
		String dataInvio = "";
		
		//// valorizzazione
		if (info.getCurrentPage() != null) 
		{
				if(input.caratteristicheProgettoCustomLabel != null && !input.caratteristicheProgettoCustomLabel.isEmpty()) {
					caratteristicheProgettoLabel = input.caratteristicheProgettoCustomLabel;
				}

				SessionCache.getInstance().set("vociDiSpesaConsList", null); // lista che viene messa in sessione in progetto-spese				
				
				// e che dipende dagli interventi selezionati in caratteristiche progetto;
				// la rimozione dalla sessione di vociDiSpesaList fatta qui forza il ricaricamento dei dati
				// quando si accede a progetto-spese
				
				if (info.getStatusInfo().getDataTrasmissione() != null) {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
				}
				
				if ("false".equals(input._progetto_caratteristiche_tipo_intervento))
				{
					
					if ("false".equals(input._caratteristicheProgettoNG_idTipoIntervento_99))
					{
						tipologiaInterventoList = CaratteristicheProgettoNeveNGDAO.getTipologiaInterventoList(idDomanda, dataInvio, logger); // lista-1 presa da tabelle DB
						logger.info("[Debug:]  tipologiaInterventoList vale = " + tipologiaInterventoList.size());
					}
					else
						{
							if( flagPubblicoPrivato == 1 && codeTipoBeneficiario.equalsIgnoreCase("GSTAZ"))
							{
								/** Jira: 1410: -- verifico se beneficiario risulta 'Grandi stazioni' e filtro anche per dettagli - inizio */
								tipologiaInterventoList = CaratteristicheProgettoNeveNGDAO.getTipologiaInterventoList3(idDomanda, dataInvio, logger); // lista-3 presa da tabelle DB
								isGrandiStazioni= true;
								logger.info("[Debug:]  tipologiaInterventoList vale = " + tipologiaInterventoList.size());
							
								logger.info("ciclo sulla lista di interventi per Grandi stazioni: ");
								
//								for (int i = 0; i < tipologiaInterventoList.size(); i++) {
//									// logger.info("tipo: " + i + ": " + tipologiaInterventoList.get(i).getIdTipoIntervento()+" idDettaglio: " + tipologiaInterventoList.get(i).getIdDettaglio());
//								}
							}
							else
							{
								/** Jira: 1410: -- verifico se beneficiario risulta 'micro stazioni' privato */
								tipologiaInterventoList = CaratteristicheProgettoNeveNGDAO.getTipologiaInterventoList(idDomanda, dataInvio, logger); // lista-1 presa da tabelle DB
								logger.info("[Debug:]  tipologiaInterventoList vale = " + tipologiaInterventoList.size());
							}
						}
				}// -/ fine if
				
				if (tipologiaInterventoList != null) 
				{
					int numDettagli = caricaDettaglioTipologia(tipologiaInterventoList, isGrandiStazioni, logger);
					logger.info("[Debug:]  numDettagli risulta = " + numDettagli);
					
					CaratteristicheProgettoNeveNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
					if (_caratteristicheProgetto != null) 
					{
						/** Jira: 1410: -- verifico se utente ha selezionato idTipoIntervento=99 - inizio */
						if ("true".equals(input._caratteristicheProgettoNG_idTipoIntervento_99)) // verifico variabile di cfg
						{
							TipologiaInterventoNeveVO[] tipologiaInterventoVO = _caratteristicheProgetto.getTipologiaInterventoList();
							
							for (int i = 0; i < tipologiaInterventoVO.length; i++) 
							{
								int idTipoInterventoSelezionato = Integer.parseInt(tipologiaInterventoVO[i].getIdTipoIntervento());
								logger.info("Intervento selezionato risulta: " + idTipoInterventoSelezionato);
								
								if( idTipoInterventoSelezionato == 99)
								{
									String selezione =  tipologiaInterventoVO[i].getChecked();
									if (selezione != null && selezione.equals("true")) {
										logger.info("idTipoInterventoSelezionato: " +idTipoInterventoSelezionato+ " risulta selezionato!");
										isSelectedIdTipoIntervento99= "true";
									}
								}
							}
						} 
						/** Jira: 1410: -- fine */
						
						// Leggo da xml
						List<TipologiaInterventoNeveVO> tipologiaInterventoSalvataList = new ArrayList<TipologiaInterventoNeveVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList())); // presa
						
						logger.info("[Debug:]  tipologiaInterventoSalvataList da xml ");
						
						if (tipologiaInterventoSalvataList != null) 
						{
							for (int i = 0; i < tipologiaInterventoList.size(); i++) 
							{
								TipologiaInterventoNeveVO curTipologia = tipologiaInterventoList.get(i);
								
								if (curTipologia != null) 
								{
									for (Object o : tipologiaInterventoSalvataList) 
									{
										TipologiaInterventoNeveVO curTipologiaSalvata = (TipologiaInterventoNeveVO) o;
										
										if (curTipologiaSalvata != null) 
										{
											if (curTipologia.getIdTipoIntervento().equals(curTipologiaSalvata.getIdTipoIntervento())) 
											{
												String checked = curTipologiaSalvata.getChecked();
												
												if (StringUtils.isBlank(checked)) 
												{
													checked = "false";
												}
												curTipologia.setChecked(checked);
												break;
											}
										}
									}
								}
							} // chiude for esterno
						}
					} // chiude test null _caratteristicheProgetto
					
					if (numDettagli == 0) {
						esistonoDettagli = "false";
					}

					// per ceccare di default la tipologia se ce ne fosse una sola
					int numTipologie = tipologiaInterventoList.size();
					if (numTipologie == 1) {
						TipologiaInterventoNeveVO unicaTipologia = tipologiaInterventoList.get(0);
						if (unicaTipologia != null) {
							unicaTipologia.setChecked("true");
						}
					} // chiude test su numTipologie == 1

				}

				viewWarningSpese = "false";
		}

		//// namespace
		output.tipologiaInterventoList = tipologiaInterventoList;
		output.esistonoDettagli = esistonoDettagli;
		output.viewWarningSpese = viewWarningSpese;
		output.caratteristicheProgettoLabel = caratteristicheProgettoLabel; // MB2016_11_18
		output.tipoBeneficiario = tipoBeneficiario; // RR2018_05_30 CR-1 Voucher
		output.isGrandiStazioni = isGrandiStazioni;
		
		/** Jira: 1410 
		 * se utente ha già compilato il Tab degli indicatori, 
		 * torna in Informazioni sul progetto e
		 * deseleziona la tipologia di intervento "CAT C..." e salva,
		 * i dati contenuti nel Tab degli indicatori devono essere cancellati su XML
		 * */
		output.isSelectedIdTipoIntervento99= isSelectedIdTipoIntervento99;

		logger.info("[CaratteristicheProgettoNG::inject]_caratteristicheProgetto S4_P1 END");
		return output;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1)
			throws CommonalityException {
		FinCommonInfo info = (FinCommonInfo) info1;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		
		Logger logger = Logger.getLogger(info.getLoggerName());

 		logger.info("[CaratteristicheProgettoNG::modelValidate]  BEGIN");
		

		String ERRMSG_NUM_TIPOLOGIE_SELEZIONATE = "- Indicare almeno una tipologia di intervento";
		if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia))
		{
			ERRMSG_NUM_TIPOLOGIE_SELEZIONATE = "- Indicare una sola tipologia di intervento";
		}
		
		String ERRMSG_NUM_DETTAGLI_SELEZIONATI = "";
		String ERRMSG_NUM_DETTAGLI_SELEZIONATI_NEVE = "- Indicare almeno un dettaglio intervento per la tipologia di intervento indicata";

		if ("true".equals(input._progetto_caratteristiche_uno_e_uno_solo_dettaglio))
		{
			if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia))
			{
				ERRMSG_NUM_DETTAGLI_SELEZIONATI = "- Specificare un solo dettaglio intervento per la tipologia di intervento indicata";
			}
			else {
				ERRMSG_NUM_DETTAGLI_SELEZIONATI = "- Specificare un solo dettaglio intervento per ciascuna tipologia di intervento indicata";
			}
		}
		else {
			if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia))
			{
				ERRMSG_NUM_DETTAGLI_SELEZIONATI = "- Indicare almeno un dettaglio intervento per la tipologia di intervento indicata";
			}
			else {
				
				// ERRMSG_NUM_DETTAGLI_SELEZIONATI = "- Indicare almeno un dettaglio intervento per ciascuna tipologia di intervento indicata";
			}
		}

		//// validazione panel Caratteristiche Progetto

		CaratteristicheProgettoNeveNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
		
		boolean isTipologiaSelected_99 	= false;
		boolean isTipologiaSelected 	= false;
		
		if (_caratteristicheProgetto != null) 
		{					
			//almeno una tipologia deve essere stata selezionata  
			List<TipologiaInterventoNeveVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoNeveVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList()));
			int numTipologieSelezionate = 0;	
			boolean errore = false;	   

			if(tipologiaInterventoList!=null)
			{	
				for(int j=0; j<tipologiaInterventoList.size();j++){
					TipologiaInterventoNeveVO curTipologia=tipologiaInterventoList.get(j); // idTipologiaIntervento= 99 - Cat. C

					if(curTipologia!=null){
						String checked = curTipologia.getChecked();
						if(!StringUtils.isBlank(checked) && checked.equals("true"))
						{
							
							if(curTipologia.getIdTipoIntervento().equals("99")){
								isTipologiaSelected_99= true;
								logger.info("tipologia selezionata 97: " + isTipologiaSelected);
							}
							
							if(curTipologia.getIdTipoIntervento().equals("97") || curTipologia.getIdTipoIntervento().equals("98") ){
								isTipologiaSelected= true;
							}
							numTipologieSelezionate += 1;
						}
					}
				}
				logger.info("[CaratteristicheProgettoNG::modelValidate]  verifica di almeno una tipologia selezionata; numTipologieSelezionate =  "+ numTipologieSelezionate);

				if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia))
				{
					if(numTipologieSelezionate==0 || numTipologieSelezionate>1){
						logger.warn("[CaratteristicheProgettoNG::modelValidate] nessuna o più di una tipologia intervento selezionata, ma il bando é configurato per accettare una e una sola tipologia selezionata ");			
						addMessage(newMessages,"_caratteristicheProgetto", ERRMSG_NUM_TIPOLOGIE_SELEZIONATE);
						errore = true;
					}
				}
				else 
				{
					if(numTipologieSelezionate==0 ){
						logger.warn("[CaratteristicheProgettoNG::modelValidate] nessuna tipologia intervento selezionata; il bando é configurato per accettare almeno una tipologa selezionata ");			
						addMessage(newMessages,"_caratteristicheProgetto", ERRMSG_NUM_TIPOLOGIE_SELEZIONATE);
						errore = true;
					}
				}

				if(!errore){
					for(int i=0; i<tipologiaInterventoList.size();i++){
						TipologiaInterventoNeveVO curTipologia= tipologiaInterventoList.get(i); 

						if(curTipologia!=null){
							String checked = (String) curTipologia.getChecked();
							if(!StringUtils.isBlank(checked) && checked.equals("true")){	

								// verifico che almeno un dettaglio (se c'e' un dettaglio) sia stato selezionato
								int numDettSelezionati = 0;

								TipologiaDettaglioInterventoNeveVO[] curDettaglioList = curTipologia.getDettaglioInterventoList();

								if(curDettaglioList!=null && curDettaglioList.length>0){
									for(int j=0; j<curDettaglioList.length;j++){
										TipologiaDettaglioInterventoNeveVO curDettaglio=curDettaglioList[j];					                  
										if(curDettaglio!=null){
											String checkedDett = curDettaglio.getChecked();
											if(!StringUtils.isBlank(checkedDett) && checkedDett.equals("true")){			                     
												numDettSelezionati +=1;
											}
										}			            
									}
								}else{
									numDettSelezionati = 1; //se non c'e' dettaglio considero ok la tipologia
								}
								if ("true".equals(input._progetto_caratteristiche_uno_e_uno_solo_dettaglio))
								{
									if(numDettSelezionati==0 || numDettSelezionati>1 ){
										addMessage(newMessages,"_caratteristicheProgetto", ERRMSG_NUM_DETTAGLI_SELEZIONATI);
										break;							        
									}
								} else {
									if(numDettSelezionati==0 && isTipologiaSelected){
										addMessage(newMessages,"_caratteristicheProgetto", ERRMSG_NUM_DETTAGLI_SELEZIONATI_NEVE);
										break;							        
									}
								}

							} else {
								// controllo se la selezione del flag é obbligatoria
								String flagObbligatorio = curTipologia.getFlagObbligatorio();				    	
								if(!StringUtils.isBlank(flagObbligatorio) && flagObbligatorio.equalsIgnoreCase("S")){
									addMessage(newMessages,"_caratteristicheProgetto", "- Selezionare obbligatoriamente la tipologia " + curTipologia.getDescrTipoIntervento() + "<BR/>");
									break;
								}
							}
						}//chiude test null su curTipologia			 
					}//chiude for su tipologiaInterventoList
				} //chiude test su errore
			}
						
			logger.info("[CaratteristicheProgettoNG::modelValidate()] fine validazioni definite da variabile di configurazione");
			
		} else{
			logger.warn("[CaratteristicheProgettoNG::modelValidate] _caratteristicheProgetto non presente o vuoto");
		}

		logger.info("[CaratteristicheProgettoNG::modelValidate] _caratteristicheProgetto S4_P1 END");
		return newMessages;
	}

	// associa gli eventuali dettagli a ciascuna tipologia intervento;
	// ritorna il numero di tipologie intervento che hanno almeno un dettaglio
	private int caricaDettaglioTipologia(List<TipologiaInterventoNeveVO> parTipologiaInterventoList, boolean isGrandiStazioni, Logger logger)
			throws NumberFormatException, CommonalityException 
	{
		int numDettagliValorizzati = 0;
		
		/**
		 *  se isGrandiStazioni = true, recuperare solo :
		 *  97	67: idDettaglio 
		 *  98  69: idDettaglio 
		 */
		
		if (parTipologiaInterventoList == null || parTipologiaInterventoList.isEmpty()) {
			return numDettagliValorizzati;
		}
		
		Integer integer_idTipoIntervento = 0;
		Integer integer_idDettaglio 	 = 0;
		
		for (int i = 0; i < parTipologiaInterventoList.size(); i++) 
		{
			TipologiaInterventoNeveVO curTipologia = parTipologiaInterventoList.get(i);
		
			if (curTipologia != null) 
			{
				String idTipoIntervento = curTipologia.getIdTipoIntervento(); // 97
				logger.info("idTipoIntervento: "+idTipoIntervento);
				
				String idDettaglio 	    = curTipologia.getIdDettaglio(); // 67
				logger.info("idDettaglio: "+idDettaglio);
				
				List<TipologiaDettaglioInterventoNeveVO> dettaglioList = new ArrayList<TipologiaDettaglioInterventoNeveVO>();
				
				// dati da tabelle DB (non xml)
				if( isGrandiStazioni )
				{
					integer_idTipoIntervento = Integer.parseInt(idTipoIntervento);
					integer_idDettaglio = Integer.parseInt(idDettaglio);
					dettaglioList = CaratteristicheProgettoNeveNGDAO.getDettaglioInterventoGrdStzList(integer_idTipoIntervento, integer_idDettaglio);
				}
				else{
					dettaglioList = CaratteristicheProgettoNeveNGDAO.getDettaglioInterventoList(new Integer(idTipoIntervento)); 
				}
				
				if (dettaglioList != null && !dettaglioList.isEmpty()) 
				{
					numDettagliValorizzati += 1;
					// in dettaglioList valorizzo checked in base al valore eventualmente presente nell'xml
					CaratteristicheProgettoNeveNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
					if (_caratteristicheProgetto != null) 
					{
						List<TipologiaInterventoNeveVO> tipologiaInterventoSalvataList = new ArrayList<TipologiaInterventoNeveVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList())); // presa xml

						if (tipologiaInterventoSalvataList != null && !tipologiaInterventoSalvataList.isEmpty() && tipologiaInterventoSalvataList.get(i) != null) 
						{
							TipologiaInterventoNeveVO curTipologiaSalvata = tipologiaInterventoSalvataList.get(i);
							
							TipologiaDettaglioInterventoNeveVO[] dettaglioSalvatoList = curTipologiaSalvata.getDettaglioInterventoList();
							
							if (dettaglioSalvatoList != null && dettaglioSalvatoList.length > 0) 
							{
								for (int j = 0; j < dettaglioList.size(); j++) 
								{
									TipologiaDettaglioInterventoNeveVO curDettaglio = dettaglioList.get(j);
									
									if (curDettaglio != null) 
									{
										for (TipologiaDettaglioInterventoNeveVO curDettaglioSalvato : dettaglioSalvatoList) 
										{
											if (curDettaglioSalvato != null) 
											{
												if (curDettaglio.getIdInterventoDettaglio().equals(curDettaglioSalvato.getIdInterventoDettaglio()) && curDettaglio.getIdDettIntervento().equals(curDettaglioSalvato.getIdDettIntervento())) 
												{
													String checked = curDettaglioSalvato.getChecked();
													
													if (StringUtils.isBlank(checked)) {
														checked = "false";
													}
													curDettaglio.setChecked(checked);
												}
											}
										}
									}
								}
							}
						}
					}

					curTipologia.setDettaglioInterventoList ( dettaglioList.toArray(new TipologiaDettaglioInterventoNeveVO[0]));
					int numDettagli = dettaglioList.size();

					curTipologia.setNumDettagli(numDettagli + "");
				}
			}
		}
		logger.info("[caricaDettaglioTipologia::numDettagliValorizzati]  = " + numDettagliValorizzati);
		return numDettagliValorizzati;
	}

}
