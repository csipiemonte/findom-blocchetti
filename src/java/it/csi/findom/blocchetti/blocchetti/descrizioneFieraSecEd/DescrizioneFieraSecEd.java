/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFieraSecEd;

import it.csi.findom.blocchetti.blocchetti.abstractprogettoNG.AbstractProgettoNGDAO;
import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaDettaglioInterventoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DescrizioneFieraSecEd extends Commonality{
	
	DescrizioneFieraSecEdInput input = new DescrizioneFieraSecEdInput();

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0,
			List<CommonalityMessage> arg1) throws CommonalityException {
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[DescrizioneFieraSecEd::inject] BEGIN");
		
		logger.info("[DescrizioneFieraSecEd::inject]  CommonalityOutput ");
		
		String descrizioneStato = "";
		String nomeFiera = "";
		String statoEstero = "";
		
		try 
		{
			DescrizioneFieraSecEdOutput ns = new DescrizioneFieraSecEdOutput();
			logger.info("[DescrizioneFieraSecEd::inject]  inizializzo ns ");
			
			List<StatoEsteroVO> statoEsteroSLList = new ArrayList<StatoEsteroVO>();
			
			DescrizioneFieraSecEdVO _descrizioneFieraSecEd = input.descrizioneFieraSecEd;
			logger.info("[DescrizioneFieraSecEd::inject]  inizializzo descrizioneFieraSecEd ");
			
			if( _descrizioneFieraSecEd != null && !_descrizioneFieraSecEd.isEmpty()){
				
				descrizioneStato = _descrizioneFieraSecEd.getStatoEsteroDescrizione();
				logger.info("[DescrizioneFieraSecEd::inject]  descrizioneStato risulta " + descrizioneStato);
				
				nomeFiera = _descrizioneFieraSecEd.getNomeFiera();
				logger.info("[DescrizioneFieraSecEd::inject]  nomeFiera risulta " + nomeFiera);
				
				statoEstero = _descrizioneFieraSecEd.getStatoEstero();
				_descrizioneFieraSecEd.setStatoEstero(statoEstero);
				logger.info("[DescrizioneFieraSecEd::inject]  statoEstero risulta " + statoEstero);
				
			}else{
				logger.info("[DescrizioneFieraSecEd::inject]  _descrizioneFieraSecEd risulta vuoto");
			}
			
			
			String operazione = "INS";
			Integer idDomanda = info.getStatusInfo().getNumProposta();
			logger.info("[DescrizioneFieraSecEd::inject]  idDomanda: " + idDomanda);
			
			String descrizioneFieraSecEdDaDomanda = DescrizioneFieraSecEdDAO.getNododescrizioneFieraSecEd(idDomanda);
			logger.info("[DescrizioneFieraSecEd::inject]  descrizioneFieraSecEdDaDomanda: " + descrizioneFieraSecEdDaDomanda);
			
			if(descrizioneFieraSecEdDaDomanda != null){
				logger.info("[DescrizioneFieraSecEd::inject]  descrizioneFieraSecEdDaDomanda != null ");
				logger.info("[DescrizioneFieraSecEd::inject]  operazione MOD ");
				String descrizione = descrizioneFieraSecEdDaDomanda;
				logger.info("[DescrizioneFieraSecEd::inject] descrizione" + descrizione);
				operazione = "MOD";
			}
			
			
			
			logger.info("[DescrizioneFieraSecEd::inject] operazione" + operazione);
			
			if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
				
				// se bando non standard
				statoEsteroSLList  = LuoghiDAO.getStatoEsteroListSenzaItalia(logger);
				
				int dimElencoStatoEstero = statoEsteroSLList.size();
				logger.info("dimElencoStatoEstero risulta di dimensioni:" + dimElencoStatoEstero);
				
			}
			
			ns.setStatoEsteroSLList(statoEsteroSLList);
			ns.setStatoEstero(statoEstero);
			
			logger.info("[DescrizioneFieraSecEd::inject] _decrizioneFiera END");
			return ns;
		}
		catch (Exception ex) {
			throw new CommonalityException(ex);
		} 
		finally {
			logger.info("[DescrizioneFieraSecEd::inject] END");
		}
	}

	/**
	 * Model Validate
	 */
	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		
		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		logger.info("[DescrizioneFieraSecEd::modelValidate]  BEGIN");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		/**
		 * COSTANTI per msg di errore
		 */
		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo risulta obbligatorio";
		String ERRMSG_CAMPO_NAZIONE_FEU = "- la Nazione selezionata risulta fuori Europa!";
		String ERRMSG_CAMPO_NAZIONE_IEU = "- la Nazione selezionata risulta in Europa!";
		String ERRMSG_DATA_NON_VALIDA = "- data non risulta valida!";
		String ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO = "La data inizio progetto deve risultare inferiore o uguale alla data fine progetto.";
		String ERRMSG_DATA_INIZIO_PROGETTO_INFERIORE_DTINPRJDB = "- Sono ammissibili progetti con decorrenza dal ";
		String ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO = "- Sono ammissibili progetti con inizio entro: ";
		String ERRMSG_DATA_FINE_MINORE_DATA_INIZIO= "- La data di fine progetto deve risultare maggiore od uguale alla data inizio progetto. ";
		String ERRMSG_DATA_FINE_PROGETTO_SUPERIORE = "- Sono ammissibili progetti con termine entro: ";
		
		String idBando = info.getStatusInfo().getTemplateId()+"";
		logger.info("[DescrizioneFieraSecEd::modelValidate]  idBando: " + idBando);
		
		DescrizioneFieraSecEdVO _descrizioneFieraSecEd = input.descrizioneFieraSecEd;
		
		String siglaContinente = "";
		/**
		 * Inizio con la validazione campi, 
		 * presenti in template.xhtml
		 */
		if( _descrizioneFieraSecEd != null)
		{
			
			//controllo singoli campi
			logger.info("[DescrizioneFieraSecEd::modelValidate] [descrizioneFieraSecEd.java] controllo singoli campi: ");
			
			/**
			 * Nome della fiera:
			 * - recuperato da Oggetto: descrizioneFieraSecEdVO
			 */
			String nomeFiera =  _descrizioneFieraSecEd.getNomeFiera();
			logger.info("[DescrizioneFieraSecEd::modelValidate] [descrizioneFieraSecEd.java] nomeFiera risulta: " + nomeFiera);
			
            if (StringUtils.isBlank(nomeFiera)) {
				addMessage(newMessages,"_descrizioneFieraSecEd_nomeFiera", ERRMSG_CAMPO_OBBLIGATORIO);
				logger.warn("[DescrizioneFieraSecEd::modelValidate] nomeFiera non valorizzato");
			}
            
            /****************************************************************************************
			 * Validazione campi:
			 * - dataInizioProgetto
			 * - dataFineProgetto
			 * variabile configurazione: _descrizioneFieraSecEd_data_inizio_fine_progetto_seconda_ed
			 ****************************************************************************************/
			if ("true".equals(input._descrizioneFieraSecEd_data_inizio_fine_progetto_seconda_ed))
			{
				// ------------- check presenza data campi input a video:
				boolean isPresenteDataInizioProgetto = false;
				boolean isPresenteDataFineProgetto	 = false;
				
				// --------------- check formato data: dd/MM/yyyy
				boolean isDateInizioProgettoFeValida = false;
				boolean isDateFineProgettoFeValida = false;
				
				// ----------------- Recupero data inizio e fine da frontend type: String
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				String dataInizioProgetto 	= (String) _descrizioneFieraSecEd.getDataInizioProgetto();
				logger.info("[DescrizioneFieraSecEd::modelValidate]dataInizioProgettoFE: " + dataInizioProgetto); // 10/04/2019
				
				String dataFineProgetto 	= (String) _descrizioneFieraSecEd.getDataFineProgetto();
				logger.info("[DescrizioneFieraSecEd::modelValidate]dataFineProgettoFE: " + dataFineProgetto); 	// 07/04/2019
				
				if( MetodiUtili.isDateValid( dataInizioProgetto ) ){
					isPresenteDataInizioProgetto = true;
					logger.info("[DescrizioneFieraSecEd::modelValidate]Data inizio progetto selezionata a video risulta presente e nel formato corretto: " + dataInizioProgetto);
				}else{
					addMessage(newMessages,"_descrizioneFieraSecEd_dataInizioProgetto", ERRMSG_DATA_NON_VALIDA);
					logger.info("[DescrizioneFieraSecEd::modelValidate] campo data inizio progetto non valorizzato");
				}
				
				if( MetodiUtili.isDateValid(dataFineProgetto) ){
					isPresenteDataFineProgetto = true;
					logger.info("[DescrizioneFieraSecEd::modelValidate]Data inizio progetto selezionata a video risulta presente e nel formato corretto: " + dataFineProgetto);
				}else{
					logger.debug("[DescrizioneFieraSecEd::modelValidate] Data inizio progetto selezionata a video NON risulta presente!" + isPresenteDataFineProgetto);
					addMessage(newMessages,"_descrizioneFieraSecEd_dataFineProgetto", ERRMSG_DATA_NON_VALIDA);
					logger.info("[DescrizioneFieraSecEd::modelValidate] campo data fine progetto non valorizzato");
				}
				// ---------- Validazione presenza data nei campi a video: fine
				
				// Se entrambi i campi input a video risultano presenti
				if( isPresenteDataInizioProgetto && isPresenteDataFineProgetto )
				{
					// ------------ Recupero data inizio e fine da database type: String
					List<String> dateList = AbstractProgettoNGDAO.getDateInizioMaxFineProgettoList(idBando, logger);
					
					String dataInizioProgettoDAO 	= "";
					String dataMaxInizioProgettoDAO = "";		
					String dataFineProgettoDAO 		= "";
					boolean isPresenteDataInizioProgettoDAO = false;
					boolean isPresentDataMaxInizioPrjDAO = false;
					boolean isPresenteDataFineProgettoDAO = false;
					
					boolean isDateDbMancanti = true;
					
					if (dateList == null || dateList.size() <= 0){
						logger.info("[DescrizioneFieraSecEd::modelValidate]lista date vuota... ");
					}else{
						for(int i=0; i<dateList.size(); i++)
						{
							if (i==0) 
							{
								if (dateList.get(0) != null) {
									dataInizioProgettoDAO = dateList.get(0);
									isPresenteDataInizioProgettoDAO = true;
									logger.info("[DescrizioneFieraSecEd::modelValidate]dataInizioProgettoDAO: " + dataInizioProgettoDAO);
								}
								
								if (dateList.get(1) != null) {
									dataMaxInizioProgettoDAO = dateList.get(1);
									isPresentDataMaxInizioPrjDAO= true;
									logger.info("[DescrizioneFieraSecEd::modelValidate]dataMaxInizioProgettoDAO: " + dataMaxInizioProgettoDAO);
								}
								
								if (dateList.get(2) != null) {
									dataFineProgettoDAO = dateList.get(2);
									isPresenteDataFineProgettoDAO= true;
									logger.info("[DescrizioneFieraSecEd::modelValidate]dataFineProgettoDAO: " + dataFineProgettoDAO);
								}
								break;
							}
						}
					}
					
					// ----------- eseguo validazione data: inizio
					if( isPresenteDataInizioProgettoDAO && isPresenteDataFineProgettoDAO && isPresentDataMaxInizioPrjDAO){
						isDateDbMancanti= false;
					}
					
					if( MetodiUtili.validate(dataInizioProgetto) ){ // 10/04/2019
						isDateInizioProgettoFeValida = true;
						logger.info("[DescrizioneFieraSecEd::modelValidate] isDateInizioProgettoFeValida: dataInizioProgetto= " + dataInizioProgetto +" - "+ isDateInizioProgettoFeValida);
					}else{
						addMessage(newMessages, "_descrizioneFieraSecEd_dataInizioProgetto", ERRMSG_DATA_NON_VALIDA);
						logger.info("[DescrizioneFieraSecEd::modelValidate]  dataInizioProgetto non valorizzato");
					}
					
					if( MetodiUtili.validate(dataFineProgetto) ) // 07/04/2019
					{
						isDateFineProgettoFeValida = true;
						logger.info("[DescrizioneFieraSecEd::modelValidate] isDateFineProgettoFeValida: dataFineProgetto= " + dataFineProgetto +" - "+ isDateFineProgettoFeValida);
					}else{
						addMessage(newMessages, "_descrizioneFieraSecEd_dataFineProgetto", ERRMSG_DATA_NON_VALIDA);
						logger.info("[DescrizioneFieraSecEd::modelValidate]  dataInizioProgetto non valorizzato");
					}
					
					// ---------- se date risultano valide - inizio
					if( isDateInizioProgettoFeValida && isDateFineProgettoFeValida )
					{
						// ---- eseguo conversione StringToDate: inizio
						try {
							
							boolean code01 = false; // data inizio a fe > data fine fe ?!? errore
							
							Date dateInizioProgetto = null;
							if (dataInizioProgetto != null) {
								dateInizioProgetto = sdf.parse(dataInizioProgetto);
								logger.info("[DescrizioneFieraSecEd::modelValidate]dateInizioProgetto: " + sdf.format(dateInizioProgetto));
							}
							
							Date dateFineProgetto = null;
							if (dataFineProgetto != null) {
								dateFineProgetto = sdf.parse(dataFineProgetto);
								logger.info("[DescrizioneFieraSecEd::modelValidate]dateFineProgetto: " + sdf.format(dateFineProgetto));
							}
							
							/*******************************************************************
							 *  1 controllo: 
							 *  Verifico che data inizio progettoFe sia <= data fine progettoFe:
							 *  ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO
							 */
							logger.info("[DescrizioneFieraSecEd::modelValidate] 1 controllo: dataInizioProgettoFe: "+dateInizioProgetto+" <= dataFineProgettoFe: "+dateFineProgetto);
							int ret1 = MetodiUtili.confrontaDate(dateInizioProgetto, dateFineProgetto);
							logger.info("ret1 vale: " + ret1);
							
							if (ret1 > 0) {
								code01= true;
								addMessage(newMessages,"_descrizioneFieraSecEd_dataInizioProgetto",ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO	+ ": " + sdf.format(dateFineProgetto) + " ");
								logger.info("[DescrizioneFieraSecEd::modelValidate] data inizio progetto deve risultare minore od uguale alla data di fine progetto! " );
							}
							
							if (ret1 <= 0) {
								logger.info("[DescrizioneFieraSecEd::modelValidate] dData inizio progettoFE <= data fine progettoFE : corretta!");
								logger.info("[DescrizioneFieraSecEd::modelValidate] ddataInizioProgettoFe: "+dateInizioProgetto+" e dataFineProgettoFe: "+dateFineProgetto);
							}
							// se le date a fe sono presenti e valide
							if( !code01 )
							{
								// eseguo confronti con date recuperate dal database
								if(!isDateDbMancanti){
									
								Date dateInizioProgettoDAO = null; 
								if (isPresenteDataInizioProgettoDAO) {
									if (dataInizioProgettoDAO != null) {
										dateInizioProgettoDAO	= sdf.parse(dataInizioProgettoDAO);
										logger.info("[DescrizioneFieraSecEd::modelValidate] ddateInizioProgettoDAO: " + sdf.format(dateInizioProgettoDAO));
									}
								}
								else{
									logger.info("[DescrizioneFieraSecEd::modelValidate] ddataInizioProgettoDAO: assente in database! - Nessun controllo!");
								}
							
								Date dateFineProgettoDAO = null;
								if (isPresenteDataFineProgettoDAO) {
									if (dataFineProgettoDAO != null) {
										dateFineProgettoDAO = sdf.parse(dataFineProgettoDAO);
										logger.info("[DescrizioneFieraSecEd::modelValidate] ddateFineProgettoDAO: " + sdf.format(dateFineProgettoDAO));
									}
								}
								else{
									logger.info("[DescrizioneFieraSecEd::modelValidate] ddataFineProgettoDAO: assente in database! - Nessun controllo!");
								}
							
								Date dateMaxInizioProgettoDAO = null;		
								if (isPresentDataMaxInizioPrjDAO) {
		
									if (dataMaxInizioProgettoDAO != null) {
										dateMaxInizioProgettoDAO = sdf
												.parse(dataMaxInizioProgettoDAO);
										logger.info("dateMaxInizioProgettoDAO: "
												+ sdf.format(dateMaxInizioProgettoDAO));
									}
								}else{
									logger.info("[DescrizioneFieraSecEd::modelValidate] ddateMaxInizioProgettoDAO: assente in database! - Nessun controllo!");
								}
							
								logger.debug("[DescrizioneFieraSecEd::modelValidate] dInizio controlli ...");
							
								// ************** inizio controlli ***
								// value = 0, data-1 e data-2 risultano uguali
								// value < 0, data-1 risulta < data-2
								// value > 0, data-1 risulta > data-2
								// code01: dataInizioProgetto < dataFineProgetto
								
								/*******************************************
								 *  2 controllo:
								 *  dateInizioProgettoFe >= dateInizioProgettoDAO
								 *  ERRMSG_DATA_INIZIO_PROGETTO_INFERIORE_DTINPRJDB
								 */
								logger.info("[DescrizioneFieraSecEd::modelValidate] d 2 controllo: dataInizioProgettoFe: "+dateInizioProgetto+" >= dateInizioProgettoDAO: "+dateInizioProgettoDAO);
								int ret2 = MetodiUtili.confrontaDate(dateInizioProgetto, dateInizioProgettoDAO);
								logger.info("[DescrizioneFieraSecEd::modelValidate] dret2 vale: " + ret2);
								
								if (ret2 >= 0) {
									logger.info("[DescrizioneFieraSecEd::modelValidate] dData inizio progetto >= data inizio progetto database: corretto!");
									logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateInizioProgettoDAO));
								}
								else if (ret2 < 0 ) {
									logger.info("[DescrizioneFieraSecEd::modelValidate] dData inizio progetto < data inizio progetto database: NON corretto!");
									addMessage(newMessages,"_descrizioneFieraSecEd_dataInizioProgetto",ERRMSG_DATA_INIZIO_PROGETTO_INFERIORE_DTINPRJDB	+ ": " + sdf.format(dateInizioProgettoDAO) + " ");
									logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateInizioProgettoDAO));
								}
								
								/*******************************************
								 *  3 controllo:
								 *  dateInizioProgettoFe <= dateMaxInizioProgettoDAO
								 *  ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO
								 */
								logger.info("[DescrizioneFieraSecEd::modelValidate] 3 controllo: dataInizioProgettoFe <= dateMaxInizioProgettoDAO");
								if (isPresentDataMaxInizioPrjDAO) 
								{
									int ret3 = MetodiUtili.confrontaDate(dateInizioProgetto, dateMaxInizioProgettoDAO);
									logger.info("[DescrizioneFieraSecEd::modelValidate] dret3 vale: " + ret3);
									
									if (ret3 > 0 && !code01) 
									{
										logger.info("[DescrizioneFieraSecEd::modelValidate] Data inizio progetto > data max inizio progetto database: NON corretto!");
										addMessage(
												newMessages,
												"_descrizioneFieraSecEd_dataInizioProgetto",
												ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO
														+ ": "
														+ sdf.format(dateMaxInizioProgettoDAO)
														+ " ");
										logger.info(sdf.format(dateInizioProgetto)
												+ " : "
												+ sdf.format(dateMaxInizioProgettoDAO));
									}
									
									if (ret3 <= 0) {
										logger.info("[DescrizioneFieraSecEd::modelValidate] Data inizio progetto <= data max-inizio progetto database: corretto!");
										logger.info(sdf.format(dateInizioProgetto)
												+ " : "
												+ sdf.format(dateMaxInizioProgettoDAO));
									}
								}else{
									logger.info("[DescrizioneFieraSecEd::modelValidate] Data Max inizio progetto assente su database, bypassa il controllo!");
								}
								
								/*******************************************
								 *  4 controllo:
								 *  dateInizioProgettoFe <= dateFineProgettoDAO
								 *  ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO
								 */
								logger.info("[DescrizioneFieraSecEd::modelValidate]  4 controllo: dataInizioProgettoFe <= dateFineProgettoDAO");
								int ret4 = MetodiUtili.confrontaDate(dateInizioProgetto, dateFineProgettoDAO);
								logger.info("[DescrizioneFieraSecEd::modelValidate] ret4 vale: " + ret4);
								
								
								if (ret4 <= 0) {
									logger.info("[DescrizioneFieraSecEd::modelValidate]  La data inizio progetto deve risultare inferiore o uguale alla data fine progetto. NON corretto!!!");
									logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateFineProgettoDAO));
								}else{
									logger.info("[DescrizioneFieraSecEd::modelValidate]  Controllo 4 NON corretto!");
									addMessage(newMessages,"_descrizioneFieraSecEd_dataInizioProgetto", ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO	+ ": " + sdf.format(dateFineProgettoDAO) + " ");
									logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateFineProgettoDAO));
								}
								
								
								/*******************************************
								 *  5 controllo:
								 *  data fine prjFe >= data inizio prj db
								 *  ERRMSG_DATA_FINE_MINORE_DATA_INIZIO
								 */
								logger.info("[DescrizioneFieraSecEd::modelValidate] 5 controllo: dataFineProgettoFe >= dateInizioProgetto");
								int ret5 = MetodiUtili.confrontaDate(dateFineProgetto, dateInizioProgetto);
								logger.info("[DescrizioneFieraSecEd::modelValidate]  ret5 vale: " + ret5); 
								
								if (ret5 >=0 ) {
									logger.info("[DescrizioneFieraSecEd::modelValidate] Data fine progetto >= data fine progetto: corretto!");
									logger.info(sdf.format(dateFineProgetto)+" : "+ sdf.format(dateInizioProgetto));
								}
								
								if (ret5 < 0 ) {
									logger.info("[DescrizioneFieraSecEd::modelValidate] Controllo 5 NON superato! - La data di fine progetto deve risultare maggiore od uguale alla data inizio progetto. ");
									addMessage(newMessages,"_descrizioneFieraSecEd_dataFineProgetto", ERRMSG_DATA_FINE_MINORE_DATA_INIZIO	+ ": " + sdf.format(dateInizioProgetto) + " ");
									logger.info(sdf.format(dateFineProgetto)+" : "+ sdf.format(dateInizioProgetto));
								}
								
								
								/*******************************************
								 *  6 controllo:
								 *  data fine prjFe <= dateFineProgettoDAO
								 *  ERRMSG_DATA_FINE_PROGETTO_SUPERIORE
								 */
								logger.info("[DescrizioneFieraSecEd::modelValidate] 6 controllo: dataFineProgettoFe >= dateFineProgettoDAO ");
								int ret6 = MetodiUtili.confrontaDate(dateFineProgetto, dateFineProgettoDAO);
								logger.info("[DescrizioneFieraSecEd::modelValidate] ret6 vale: " + ret6);
								
								if (ret6 <=0 ) {
									logger.info("[DescrizioneFieraSecEd::modelValidate] Data fine progetto >= data fine progetto: corretto!");
									logger.info(sdf.format(dateFineProgetto) +" : "+ sdf.format(dateFineProgettoDAO));
								}
								
								if (ret6 >0 ) { 
									logger.info("[DescrizioneFieraSecEd::modelValidate] Controllo 6 NON superato!");
									addMessage(newMessages,"_descrizioneFieraSecEd_dataFineProgetto", ERRMSG_DATA_FINE_PROGETTO_SUPERIORE	+ ": " + sdf.format(dateFineProgettoDAO) + " ");
									logger.info(sdf.format(dateFineProgetto) +" : "+ sdf.format(dateFineProgettoDAO));
								}
								
								logger.info("[DescrizioneFieraSecEd::modelValidate] Fine controlli date!");
								}
						
							}	
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
					}else{
						logger.info("[DescrizioneFieraSecEd::modelValidate] Possibilita presenza errori! Date digitate non valide !!!");
					} 
					
				}else{
					logger.info("[DescrizioneFieraSecEd::modelValidate]Campi obbligatori: ");
				}
			}// fine controllo date inizio e fine progetto se _descrizioneFieraSecEd_data_inizio_fine_progetto == true
            
         // Jira 1417 Voucher: recupero nodo caratteristicheProgetto
             Map<String,String> tipolIntervCheckedMap = new HashMap<String,String>();
             
             String idTipoIntervento = "";
             String idDettIntervento = "";
             String idDettaglioInterventoSelezionato = "";
             Boolean isDettaglioInterventoEuropa = false;
             String descrDettIntervento = "";
             
             logger.info("[DescrizioneFieraSecEd::modelValidate] recupero il nodo caratteristicheProgetto: ");
     		CaratteristicheProgettoNGVO caratteristicheProgettoMap = input.caratteristicheProgetto; 
     		
     		if(caratteristicheProgettoMap!=null)
     		{
     			logger.info("[DescrizioneFieraSecEd::modelValidate] se il nodo caratteristicheProgetto non risulta vuoto: ");
     			logger.info("[DescrizioneFieraSecEd::modelValidate] recupero le tipologie di intervento: ");
     			TipologiaInterventoVO[] tipologiaInterventoList = caratteristicheProgettoMap.getTipologiaInterventoList();  //presa da xml
     				
     			
     			if(tipologiaInterventoList != null && tipologiaInterventoList.length>0)
     			{
     				logger.info("[DescrizioneFieraSecEd::modelValidate] se le tipologie di intervento sono presenti, le ciclo: ");
     				for(int i=0; i<tipologiaInterventoList.length;i++)
     				{
     					logger.info("[DescrizioneFieraSecEd::modelValidate] definisco una Mappa di tipologie di intervento: ");
     					TipologiaInterventoVO tipolIntMap = tipologiaInterventoList[i];
     					
     					String codTipoIntervento = (String)tipolIntMap.getCodTipoIntervento();
     					logger.info("[DescrizioneFieraSecEd::modelValidate] recupero codice tipo intervento: " + codTipoIntervento);
     					
     					String chek = (String)tipolIntMap.getChecked();	
     					logger.info("[DescrizioneFieraSecEd::modelValidate] verifico la check: " + chek);
     					
     					logger.debug("[DescrizioneFieraSecEd::modelValidate]codTipoIntervento = "+codTipoIntervento+" chek="+chek);
     					
     					idTipoIntervento = (String)tipolIntMap.getIdTipoIntervento();
     					logger.info("[DescrizioneFieraSecEd::modelValidate] recupero la idTipoIntervento: " + idTipoIntervento);
     					
     					if("true".equals(chek))
     					{
     						logger.info("[DescrizioneFieraSecEd::modelValidate] se la check risulta a true: ");
     						
     						TipologiaDettaglioInterventoVO[] dettaglioInterventoList = tipolIntMap.getDettaglioInterventoList();
     						logger.info("[DescrizioneFieraSecEd::modelValidate] recupero la lista di dettaglio intervento: ");
     						
     						if(dettaglioInterventoList!=null && dettaglioInterventoList.length>0)
     						{
     							logger.info("[DescrizioneFieraSecEd::modelValidate] se la lista di dettaglio intervento risulta piena: si, vi ciclo ");
     							
     							for(int j=0; j<dettaglioInterventoList.length;j++)
     							{
     								TipologiaDettaglioInterventoVO interventoMap = dettaglioInterventoList[j];
     								logger.info("[DescrizioneFieraSecEd::modelValidate] definisco una Mappa con elenco dettaglio interventiList ");
     								
     								String chekInt = (String)interventoMap.getChecked();
     								logger.info("[DescrizioneFieraSecEd::modelValidate] recupero la check: " + chekInt);
     								
     								descrDettIntervento = (String) interventoMap.getDescrDettIntervento();
     								logger.info("[DescrizioneFieraSecEd::modelValidate] recupero la descrDettIntervento: " + descrDettIntervento);
     								
     								idDettIntervento = (String)interventoMap.getIdDettIntervento();	
     								logger.info("[DescrizioneFieraSecEd::modelValidate] recupero la idDettIntervento: " + idDettIntervento);
     								
     								logger.debug("[DescrizioneFieraSecEd::modelValidate]idDettIntervento = "+idDettIntervento+" chekInt="+chekInt);
     								
     								
     								if("true".equals(chekInt)){
     									logger.info("[DescrizioneFieraSecEd::modelValidate] se la check risulta a true: " + chekInt);
     									logger.info("[DescrizioneFieraSecEd::modelValidate] la inserisco nella mappa: tipolIntervCheckedMap");
     									tipolIntervCheckedMap.put(idTipoIntervento+"-"+idDettIntervento,"0");
     									idDettaglioInterventoSelezionato = idDettIntervento;
     									logger.info("[DescrizioneFieraSecEd::modelValidate] memorizzo idDettaglioInterventoSelezionato: " + idDettaglioInterventoSelezionato);
     									
     									descrDettIntervento = (String) interventoMap.getDescrDettIntervento();
         								logger.info("[DescrizioneFieraSecEd::modelValidate] recupero la descrDettIntervento: " + descrDettIntervento);
         								if (descrDettIntervento.equalsIgnoreCase("Europa")) {
											isDettaglioInterventoEuropa = true;
										}
     								}
     							}
     						}else{
     							tipolIntervCheckedMap.put(idTipoIntervento+"-","0");
     						}
     					}
     				}
     			} else {		
     				logger.debug( "[DescrizioneFieraSecEd::modelValidate]tipologiaInterventoList NULL or Empty");
     			}
     		} else {
     			// se siamo qui, e' gia' stato segnalato l'errore alla REGOLA V9	
     			logger.debug( "[DescrizioneFieraSecEd::modelValidate]caratteristicheProgettoMap NULL");
     		}
     		
            String statoEstero =  _descrizioneFieraSecEd.getStatoEstero(); // Es.: 201
            logger.info("[DescrizioneFieraSecEd::modelValidate] statoEstero risulta: " + statoEstero);
            
     		if (statoEstero != null && !statoEstero.isEmpty()) 
     		{
				siglaContinente = LuoghiDAO.getSiglaContinenteByCodStato(statoEstero);
			}
     		
	     	logger.info("[DescrizioneFieraSecEd::modelValidate]siglaContinente recuperata da db risulta: " + siglaContinente);
	     		
	     	boolean isEuropa = false;
	     	if(siglaContinente.equalsIgnoreCase("EU")){
	     		logger.warn("[DescrizioneFieraSecEd::modelValidate] siglaContinente risulta: " + siglaContinente);
	     		isEuropa = true;
	     	}else{
	     		logger.info("[DescrizioneFieraSecEd::modelValidate]NON risulta una Nazione EUROPEA: ");
	     	}
	     	
	     	/**
	     	 * isEuropa = true ( Selezionato Nazione presente in Europa)
	     	 * isDettaglioInterventoEuropa = true ( Selezionato checkbox = 71 : Europa )
	     	 */
	     	// se 71 e null, errore
	     	if(idDettaglioInterventoSelezionato.equalsIgnoreCase("71") && !isEuropa && (statoEstero != null && !statoEstero.isEmpty()) )
	     	{
	     		addMessage(newMessages,"_descrizioneFieraSecEd_statoEstero", ERRMSG_CAMPO_NAZIONE_FEU);
				logger.warn("[DescrizioneFieraSecEd::modelValidate] statoEstero risulta fuori Europa");
	     	}
	     	
	     	// se 72 e EU : Errore
	     	if(idDettaglioInterventoSelezionato.equalsIgnoreCase("72") && isEuropa && statoEstero != null && !statoEstero.isEmpty())
	     	{
	     		addMessage(newMessages,"_descrizioneFieraSecEd_statoEstero", ERRMSG_CAMPO_NAZIONE_IEU);
				logger.warn("[DescrizioneFieraSecEd::modelValidate] statoEstero in Europa");
	     	}
     		
     		if (StringUtils.isBlank(statoEstero)) {
				addMessage(newMessages,"_descrizioneFieraSecEd_statoEstero", ERRMSG_CAMPO_OBBLIGATORIO);
				logger.warn("[DescrizioneFieraSecEd::modelValidate] statoEstero non valorizzato");
				logger.info("[DescrizioneFieraSecEd::modelValidate] statoEstero risulta mancante. ");
			}
			else {				
				logger.info("[DescrizioneFieraSecEd::modelValidate] statoEstero: " + statoEstero);
				logger.info("[DescrizioneFieraSecEd::modelValidate] statoEstero risulta presente. ");
			}
     		
		// Jira 1417 Voucher
			
		}else {
			logger.error("[DescrizioneFieraSecEd::modelValidate] descrizioneFieraSecEd non presente o vuoto");	
		}
		
		return newMessages;
	} 
}
