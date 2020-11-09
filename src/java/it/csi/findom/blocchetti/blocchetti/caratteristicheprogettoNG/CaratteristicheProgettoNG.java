/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNG;

import it.csi.findom.blocchetti.blocchetti.schedaProgetto.CriterioVO;
import it.csi.findom.blocchetti.blocchetti.schedaProgetto.SchedaProgettoVO;
import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.common.dao.RegoleDAO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.util.ValidationUtil;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaDettaglioInterventoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.cultPianospese.CultPianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.cultPianospese.DettaglioVoceSpesaInterventoCulturaVO;
import it.csi.findom.blocchetti.common.vo.pianospese.DettaglioCostiVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CaratteristicheProgettoNG extends Commonality {

	CaratteristicheProgettoNGInput input = new CaratteristicheProgettoNGInput();

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
		CaratteristicheProgettoNGOutput output = new CaratteristicheProgettoNGOutput();
		String prf = "[CaratteristicheProgettoNG::inject] ";
		
		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info(prf + " BEGIN");
		
		//// tipo di beneficiario tramite: flagPubblicoPrivato (1=impresa), (2=pubblico)
		int flagPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);
		logger.info(prf + " flagPubblicoPrivato vale= " + flagPubblicoPrivato);
		
		String idDettIntervento = "";
		String esistonoDettagli = "true";
		String viewWarningSpese = "false";
		
		/** : Richiesta per tutti i bandi di sostituire label:
		 *  - Caratteristiche del progetto di investimento
		 *  con: Caratteristiche del progetto rif.: DT
		 **/
		String caratteristicheProgettoLabel = "Caratteristiche del progetto";
		String tipoBeneficiario = "";
		
		String importoContributo ="";
		
		Integer idSportello = info.getStatusInfo().getNumSportello();
		logger.info(prf + "idSportello vale= " + idSportello); // 80

		String codeTipoBeneficiario = "";
		codeTipoBeneficiario = DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) != null ? DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) : "";
		logger.info(prf + "codeTipoBeneficiario: "	+ codeTipoBeneficiario);
		
		if( flagPubblicoPrivato == 1) {
			tipoBeneficiario = "Impresa";
		}

		//// dichiarazioni
		List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<>();

		//// valorizzazione
		if (info.getCurrentPage() != null) {
			if(input.caratteristicheProgettoCustomLabel != null && !input.caratteristicheProgettoCustomLabel.isEmpty()) {
			 caratteristicheProgettoLabel = input.caratteristicheProgettoCustomLabel;
			 logger.info(prf + " caratteristicheProgettoLabel vale = " + caratteristicheProgettoLabel);
			}

			SessionCache.getInstance().set("vociDiSpesaConsList", null); // lista che viene messa in sessione in progetto-spese				
				
				// e che dipende dagli interventi selezionati in caratteristiche progetto;
				// la rimozione dalla sessione di vociDiSpesaList fatta qui forza il
				// ricaricamento dei dati
				// quando si accede a progetto-spese

				String idDomanda = info.getStatusInfo().getNumProposta() + "";
				logger.info(prf + " idDomanda vale = " + idDomanda);
				
				String dataInvio = "";
				if (info.getStatusInfo().getDataTrasmissione() != null) {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
				}
				
				/*** Bonus Piemonte covid2019 recupero codice ateco ed importo da cf collegato inizio */
				String idBando = info.getStatusInfo().getTemplateId()+"";
				logger.info(prf + "  idBando: " + idBando);
				
            	if( "true".equals(input._caratteristicheProgettoNG_imp_contr_covid))
            	{
            		logger.info(prf + " importo contributo per bonus Piemonte covid19 inizio");
            		
            		// codice fiscale del beneficiario
            		FinStatusInfo statusInfo = info.getStatusInfo();
            		
            		String cfBeneficiario = statusInfo.getCodFiscaleBeneficiario();
            		logger.info(prf + "cfBeneficiario: " + cfBeneficiario);
            		
            		importoContributo = CaratteristicheProgettoNGDAO.getImportoContributoCovid(cfBeneficiario, logger);
            		logger.info(prf + "importoContributo: " + importoContributo);
            		
            		logger.info(prf + " importo contributo per bonus Piemonte covid19 fine");
            	}
            	/*** test Bonus Piemonte covid2019 recupero codice ateco ed importo da cf collegato fine */
            	
				/*** Jira: 1977 Bonus Piemonte turismo -  inizio */
            	if( "true".equals(input._caratteristicheProgettoNG_imp_contr_cir))
            	{
            		logger.debug(prf + "importo contributo per bonus Piemonte turismo inizio");
            		logger.info(prf + "idDomanda vale: " + idDomanda);
            		
            		// codice regionale salvato su xml nella sezione: Beneficiario 
            		String codeRegionale = "";
            		Integer intIdDomanda = Integer.parseInt(idDomanda);
            		logger.info(prf + "intIdDomanda: " + intIdDomanda);
            		
            		codeRegionale = CaratteristicheProgettoNGDAO.getCodeRegionaleByIdDomanda(intIdDomanda, logger);
            		logger.info(prf + "codeRegionale: " + codeRegionale);
            		
            		// recupero importo contributo  in base al codice regionale:
            		codeRegionale = codeRegionale.toUpperCase();
            		logger.info(prf + "codeRegionale: " + codeRegionale);
            		
            		importoContributo = CaratteristicheProgettoNGDAO.getImportoContributoByCIR(codeRegionale, logger);
            		logger.info(prf + "importoContributo: " + importoContributo);
            		
            		logger.info(prf + "importo contributo per bonus Piemonte turismo fine");
            	}
				/*** Jira: 1977 Bonus Piemonte turismo - fine */
            	
            	
            	
            	/** Jira:  - Bando bonus cultura - inizio */
				if( "true".equals(input._caratteristicheProgettoNG_imp_contr_by_sogg_abilitati))
		    	{
					// codice fiscale del beneficiario
            		FinStatusInfo statusInfo = info.getStatusInfo();
            		
            		String cfBenefBonusCult = statusInfo.getCodFiscaleBeneficiario();
            		logger.info("cfBenefBonusCult: " + cfBenefBonusCult);
            		
            		Integer intIdBando = Integer.parseInt(idBando);
            		logger.info("cf beneficiario bonus cultura 2020: "+intIdBando);
            		
            		importoContributo = CaratteristicheProgettoNGDAO.getImportoBonusCultura(intIdBando, cfBenefBonusCult, logger);
            		logger.info("importoContributo bonus cultura 2020 risulta: " + importoContributo);
            		
            		logger.info("[CaratteristicheProgettoNG::inject]  importo contributo per bonus cultira 2020 covid19 fine");
		    	}
				
				
				
				/** Jira: 1521 -: Bando-VoucherIR Tipologie intervento condizionate dal beneficiario - inizio */
				if ("true".equals(input._progetto_caratteristiche_tipo_intervento_beneficiario_custom)){
						tipologiaInterventoList = CaratteristicheProgettoNGDAO.getTipologiaInterventoListCustom(idDomanda, dataInvio, codeTipoBeneficiario, logger);
						logger.info(prf + "  tipologiaInterventoList(0)  = " + tipologiaInterventoList );
				}
				else{
					
					if ("false".equals(input._progetto_caratteristiche_tipo_intervento))
					{
						// "true" solo per bando "Promozione del libro", "false" per tutti gli altri
//						tipologiaInterventoList = CaratteristicheProgettoNGDAO.getTipologiaInterventoList(idDomanda, dataInvio, logger); // lista-1 presa da tabelle DB
						logger.debug(prf + "  tipologiaInterventoList(1)  = " + tipologiaInterventoList );

						tipologiaInterventoList = CaratteristicheProgettoNGDAO.getTipologiaInterventoListNew(idDomanda, idSportello, dataInvio, logger); 
						
						// bando 93 : Spettacolo_2020 ha abilitazione...
						// TODO PK in alcuni casi scatta il controllo sull'abilitazione del soggetto a vedere la tipologia di intervento
						// o modifico la query getTipologiaInterventoListNew e ne faccio una nuova
						// o faccio quella e poi, per ogni tipologia trovata verifico se il soggetto a' abilitato...
						
						if(RegoleDAO.isAssociataRegolaAlBando("SOGGETTI_ABILITATI", idBando, logger)){ // se devo controllare le abilitazioni 
							
							
							List<Integer> listaTipolInterventoAbilitatePerBeneficiario = 
								CaratteristicheProgettoNGDAO.getListaTipolInterventoAbilitatePerBeneficiario(idBando, 
										info.getStatusInfo().getCodFiscaleBeneficiario(), 
										input.operatorePresentatore.getIdDipartimento(), logger);
						
							logger.debug(prf + " listaTipolInterventoAbilitatePerBeneficiario = " +listaTipolInterventoAbilitatePerBeneficiario);
									
							if(listaTipolInterventoAbilitatePerBeneficiario!=null 
									&& listaTipolInterventoAbilitatePerBeneficiario.size()==1
									&& listaTipolInterventoAbilitatePerBeneficiario.get(0)!=null){
								// se l'utente ha una tipologia di intervento associata, setto SoggettoAbilitato("true");
								for (TipologiaInterventoVO tipologiaInterventoVO : tipologiaInterventoList) {
									
									if (listaTipolInterventoAbilitatePerBeneficiario.contains(Integer.parseInt(tipologiaInterventoVO.getIdTipoIntervento()))){
										tipologiaInterventoVO.setSoggettoAbilitato("true");
										logger.info(prf + " IdTipoIntervento = " + tipologiaInterventoVO.getIdTipoIntervento() + ", abilitata = TRUE");
									}else{
										tipologiaInterventoVO.setSoggettoAbilitato("false");
										logger.info(prf + " IdTipoIntervento = " + tipologiaInterventoVO.getIdTipoIntervento() + ", abilitata = FALSE");
									}
								}
							}else{
								// l'utente non ha una tipologia di intervento associata, lascio tutte le tipologie con SoggettoAbilitato(null);
								logger.info(prf + " l'utente non ha una tipologia di intervento associata o ne ha troppe");
							}
						}
						
//						tipologiaInterventoList = caricaListaTipologieIntervento(idDomanda, idSportello, dataInvio, logger);
					}
					else
					{
						if( flagPubblicoPrivato == 2)
						{
							tipologiaInterventoList = CaratteristicheProgettoNGDAO.getTipologiaInterventoList2(idDomanda, dataInvio, logger); // lista-2 presa da tabelle DB
							logger.debug(prf + "tipologiaInterventoList(2)  = " + tipologiaInterventoList);
						}else{
							tipologiaInterventoList = CaratteristicheProgettoNGDAO.getTipologiaInterventoList(idDomanda, dataInvio, logger); // lista-1 presa da tabelle DB
							logger.info(prf + "tipologiaInterventoList(3)  = " + tipologiaInterventoList);
						}
					}
				}
				
				
				
				if (tipologiaInterventoList != null) {

					logger.info(prf + "tipologiaInterventoList size = " + tipologiaInterventoList.size());
					
					int numDettagli = caricaDettaglioTipologia(tipologiaInterventoList, logger);
					logger.info(prf + " numDettagli vale = " + numDettagli);
					
					CaratteristicheProgettoNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
					if (_caratteristicheProgetto != null) 
					{
						List<TipologiaInterventoVO> tipologiaInterventoSalvataList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList())); // presa
						// da xml
						
						if (tipologiaInterventoSalvataList != null) 
						{
							for (int i = 0; i < tipologiaInterventoList.size(); i++) 
							{
								TipologiaInterventoVO curTipologia = tipologiaInterventoList.get(i);
								
								if (curTipologia != null) 
								{
									for (TipologiaInterventoVO o : tipologiaInterventoSalvataList){
										TipologiaInterventoVO curTipologiaSalvata = o;
										
										if (curTipologiaSalvata != null){
											if (curTipologia.getIdTipoIntervento().equals(curTipologiaSalvata.getIdTipoIntervento())){
												String checked = curTipologiaSalvata.getChecked();
												logger.info(prf + " curTipologia  da xml vale =  " + curTipologiaSalvata.getIdTipoIntervento() );
												
												if (StringUtils.isBlank(checked)){
													checked = "false";
												}
												curTipologia.setChecked(checked);
												break;
											}}}}}}
					} // chiude test null _caratteristicheProgetto
					if (numDettagli == 0) {
						esistonoDettagli = "false";
					}

					// per ceccare di default la tipologia se ce ne fosse una sola
					int numTipologie = tipologiaInterventoList.size();
					if (numTipologie == 1) {
						TipologiaInterventoVO unicaTipologia = tipologiaInterventoList.get(0);
						if (unicaTipologia != null) {
							unicaTipologia.setChecked("true");
						}
					} // chiude test su numTipologie == 1

				}// chiude test null tipologiaInterventoList
				else{
					logger.info(prf + "Non ci sono tipologie di intervento, controlla il database!");
				}

				// verifico che non ci siano spese senza Caratteristiche collegate
				viewWarningSpese = "false";

				/* Controllo se esistono dei "Dettagli costi" associate a tipologieIntervento unchecked
					se ne trovo attivo il viewWarningSpese */
				PianoSpeseVO pianoSpeseMap = input.pianoSpese;

				if (pianoSpeseMap != null && !pianoSpeseMap.isEmpty()) {
					DettaglioCostiVO[] dettaglioCostiList = pianoSpeseMap.getDettaglioCostiList();
					if (dettaglioCostiList != null && dettaglioCostiList.length > 0) {
						logger.info(prf + "dettaglioCostiList.size="+ dettaglioCostiList.length);

						// metto tipologiaInterventoList con checked==true in una mappa
						Map<String, String> tipolIntervCheckedMap = new HashMap();
						for (int i = 0; i < tipologiaInterventoList.size(); i++) {
							TipologiaInterventoVO tipolIntMap = tipologiaInterventoList.get(i);

							String codTipoIntervento = tipolIntMap.getCodTipoIntervento(); // ERRORE
							logger.info(prf + " codTipoIntervento vale: " + codTipoIntervento);
							String chek = tipolIntMap.getChecked();
							
							String idTipoIntervento = tipolIntMap.getIdTipoIntervento();
							
							if ("true".equals(chek)) {
								TipologiaDettaglioInterventoVO[] dettaglioInterventoList = tipolIntMap.getDettaglioInterventoList();
								
								if (dettaglioInterventoList != null && dettaglioInterventoList.length > 0) 
								{
									for (int j = 0; j < dettaglioInterventoList.length; j++) 
									{
										TipologiaDettaglioInterventoVO interventoMap = dettaglioInterventoList[j];
										
										String chekInt = interventoMap.getChecked();
										idDettIntervento = interventoMap.getIdDettIntervento();
										
										if ("true".equals(chekInt)) {
											tipolIntervCheckedMap.put(idTipoIntervento + "-" + idDettIntervento, "checked");
										}
									}
								} else {
									tipolIntervCheckedMap.put(idTipoIntervento + "-", "checked");
								}
							}
						}
						logger.info(prf + "tipolIntervCheckedMap = " + tipolIntervCheckedMap);

						// ciclo su tutti i dettagliCosti estratti dall'XML
						// per ognuno controllo che esista la corrispondente tipologiaIntervento e che sia checked
						for (int i = 0; i < dettaglioCostiList.length; i++) {
							DettaglioCostiVO detCostoMap = dettaglioCostiList[i];
							if (detCostoMap != null) {
								// 3-4 , 2-2 == in dettaglioInterventoList (idTipoIntervento-idDettIntervento)
								String id = detCostoMap.getIdentificativo(); 
								if (tipolIntervCheckedMap.containsKey(id)) {
									logger.info(prf + "id=" + id + " INNN OK");
								} else {
									// trovato un dettaglioCosto non associato a nessuna tipologiaIntervento checked
									logger.info(prf + "id=" + id + " OUTT KO");
									viewWarningSpese = "true";
									break;
								}
							}
						}

					} else {						
						logger.info(prf + "dettaglioCostiList NULL or Empty");
					}
				}
				//MB2018_06_04 ini per i bandi che usano il blocchetto cultPianoSpese la lista dettaglioCostiList non esiste, per cui si deve fare un controllo 
				// diverso per verificare se esistono incongruenze con piano spese; 
				//entrambi gli input input.cultPianoSpese e input.pianoSpese sono mappati sullo stesso nodo dell'xml e sono entrambi valorizzati in caso di 
				//nodo spese valorizzato. Ho verificato che se le spese sono originate dal blocchetto cultPianoSpese il dettaglio costo non e' valorizzato in entrambe 
				//le variabili; se le spese sono originate dal blocchetto pianoSpese il dettaglio costo e' valorizzato in entrambe 
				//le variabili; per cui il controllo seguente serve solo nel primo caso (spese originate dal blocchetto cultPianoSpese) che non riesce ad
				//individuare eventuali incongruenze perche' le cerca in dettaglioCosti che non e' valorizzato
				if(viewWarningSpese.equalsIgnoreCase("false")){//se fosse gia' true significa che ci sono incongruenze (e quindi e' inutile cercare ulteriormente) e che siamo nel caso di pianoSpese
					CultPianoSpeseVO cultPianoSpeseVO = input.cultPianoSpese;
					if(cultPianoSpeseVO!= null && !cultPianoSpeseVO.isEmpty()){
						if(!verificaCoerenzaCultSpese(cultPianoSpeseVO,tipologiaInterventoList, logger))	{
							viewWarningSpese = "true";
						}
					}
				}
		}
		
		for (TipologiaInterventoVO tip : tipologiaInterventoList) {
			logger.debug(prf + tip.toString());
		}
		
		//// namespace
		output.tipologiaInterventoList = tipologiaInterventoList;
		output.esistonoDettagli = esistonoDettagli;
		output.viewWarningSpese = viewWarningSpese;
		output.caratteristicheProgettoLabel = caratteristicheProgettoLabel; // MB2016_11_18
		output.tipoBeneficiario = tipoBeneficiario; // RR2018_05_30 CR-1 Voucher
		
		/*** Bonus Piemonte covid2019 */
		if( "true".equals(input._caratteristicheProgettoNG_imp_contr_covid))
    	{
			output.importoContributo = importoContributo;
    	}
		
		/*** Jira: 1977 Bonus Piemonte turismo - */
		if( "true".equals(input._caratteristicheProgettoNG_imp_contr_cir))
    	{
			output.importoContributo = importoContributo;
    	}
		
		/*** Jira:  @gf Bonus cultura 2020 - */
		if( "true".equals(input._caratteristicheProgettoNG_imp_contr_by_sogg_abilitati))
    	{
			output.importoContributo = importoContributo;
    	}
		
		logger.info(prf + "END");
		return output;
	}


	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1)
			throws CommonalityException {
		FinCommonInfo info = (FinCommonInfo) info1;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		String prf = "[CaratteristicheProgettoNG::modelValidate] ";
		logger.info(prf + " BEGIN");
		
		String ERRMSG_NUM_TIPOLOGIE_SELEZIONATE = "- Indicare almeno una tipologia di intervento";
		if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia))
		{
			ERRMSG_NUM_TIPOLOGIE_SELEZIONATE = "- Indicare una sola tipologia di intervento";
		}
		
		String ERRMSG_NUM_DETTAGLI_SELEZIONATI = "";

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
			if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia)){
				ERRMSG_NUM_DETTAGLI_SELEZIONATI = "- Indicare almeno un dettaglio intervento per la tipologia di intervento indicata";
			}
			else {
				// : da verificare : ERRMSG_NUM_DETTAGLI_SELEZIONATI = "- Indicare almeno un dettaglio intervento per ciascuna tipologia di intervento indicata";
			}
		}

		//// validazione panel Caratteristiche Progetto

		CaratteristicheProgettoNGVO _caratteristicheProgetto = input._caratteristicheProgetto;

		if (_caratteristicheProgetto != null) 
		{		
			//almeno una tipologia deve essere stata selezionata  
			List<TipologiaInterventoVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList()));
			int numTipologieSelezionate = 0;
			String tipologieIntervDeselezionate = null; // qui segno le tipologie non selezionate
			boolean errore = false;	   

			if(tipologiaInterventoList!=null){
			
				for(int j=0; j<tipologiaInterventoList.size();j++){
					TipologiaInterventoVO curTipologia=tipologiaInterventoList.get(j); // idTipologiaIntervento= 99 - Cat. C

					if(curTipologia!=null){
						String checked = curTipologia.getChecked();
						if(!StringUtils.isBlank(checked) && checked.equals("true")){
							numTipologieSelezionate += 1;
						}
						if(!StringUtils.isBlank(checked) && checked.equals("false")){
							tipologieIntervDeselezionate = tipologieIntervDeselezionate + "-"+curTipologia.getIdTipoIntervento();
						}
					}
				}
				if(StringUtils.isNotBlank(tipologieIntervDeselezionate))
					tipologieIntervDeselezionate += "-";
				
				logger.info(prf + " verifica di almeno una tipologia selezionata; numTipologieSelezionate =  "+ numTipologieSelezionate);

				if ("true".equals(input._progetto_caratteristiche_una_e_una_sola_tipologia)){
					
					if(numTipologieSelezionate==0 || numTipologieSelezionate>1){
						logger.warn(prf + " nessuna o più di una tipologia intervento selezionata, ma il bando é configurato per accettare una e una sola tipologia selezionata ");			
						addMessage(newMessages,"_caratteristicheProgetto", ERRMSG_NUM_TIPOLOGIE_SELEZIONATE);
						errore = true;
					}
				} else {
					if(numTipologieSelezionate==0 ){
						logger.warn(prf + " nessuna tipologia intervento selezionata; il bando é configurato per accettare almeno una tipologa selezionata ");			
						addMessage(newMessages,"_caratteristicheProgetto", ERRMSG_NUM_TIPOLOGIE_SELEZIONATE);
						errore = true;
					}
				}

				if(!errore){
				
					for(int i=0; i<tipologiaInterventoList.size();i++){
						TipologiaInterventoVO curTipologia= tipologiaInterventoList.get(i); 

						if(curTipologia!=null){
							String checked = (String) curTipologia.getChecked();
							if(!StringUtils.isBlank(checked) && checked.equals("true")){	

								// verifico che almeno un dettaglio (se c'e' un dettaglio) sia stato selezionato
								int numDettSelezionati = 0;

								TipologiaDettaglioInterventoVO[] curDettaglioList = curTipologia.getDettaglioInterventoList();

								if(curDettaglioList!=null && curDettaglioList.length>0){
									for(int j=0; j<curDettaglioList.length;j++){
										TipologiaDettaglioInterventoVO curDettaglio=curDettaglioList[j];					                  
										if(curDettaglio!=null){
											String checkedDett = curDettaglio.getChecked();
											if(!StringUtils.isBlank(checkedDett) && checkedDett.equals("true")){
												logger.info(prf + " IdDettIntervento= " + curDettaglio.getIdDettIntervento());
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
									if(numDettSelezionati==0){
										addMessage(newMessages,"_caratteristicheProgetto", "- Indicare almeno un dettaglio.");
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
						
			//me generico che tramite reflection chiama eventuali ulteriori metodi di validazione, 
			//i cui nomi e argomenti sono contenuti nella var di configurazione validationMethodsCaratteristicheProgettoNG
			logger.info(prf + " inizio validazioni definite da variabile di configurazione");
			ArrayList<SegnalazioneErrore> segnalazioneList = ValidationUtil.validate(CaratteristicheProgettoNGValidationMethods.class, input, logger);
			if(segnalazioneList!=null && !segnalazioneList.isEmpty()){
				for (SegnalazioneErrore segnalazioneErrore : segnalazioneList) {
					addMessage(newMessages, segnalazioneErrore.getCampoErrore(), MetodiUtili.prefixErrMsg(newMessages,segnalazioneErrore));			
				}
			}
			logger.debug(prf + " fine validazioni definite da variabile di configurazione");

			
			// JIRA FINDOM 1813 : Scheda progetto - Controlli nel tab
			// se cambiano le tipologie verifico se nella sezione "Scheda Corso" sono stati valorizzati dei criteri
			// inerenti le tipologie modificate, se SI allora non consento di salvare
			
			// 1) Controllo se sono state deselezionate delle tipologie intervento
			logger.info(prf + " tipologieIntervDeselezionate="+tipologieIntervDeselezionate);
			if(StringUtils.isNotBlank(tipologieIntervDeselezionate)){
				
				// 2) Controllo se il bando prevede la sezione "Scheda Progetto"
				boolean flagSchedaProgetto = CaratteristicheProgettoNGDAO.getFlagSchedaProgetto(info.getStatusInfo().getTemplateId(), logger);
				logger.debug(prf + " flagSchedaProgetto="+flagSchedaProgetto);
				
				if(flagSchedaProgetto){
					
					// 3) Controllo se il bando prevede dei criteri compilabili in base alla tipologia intervento selezionata
					 List<Integer> listaTipologIntervAssociate = CaratteristicheProgettoNGDAO.getTipologieInterventoAssociateBando(info.getStatusInfo().getTemplateId(), logger);
				
					 if(listaTipologIntervAssociate!=null){
						 
						// 4) Controllo se sono stati compilati nella sezione "Scheda Progetto" dell'xml uno o piu criteri associati 
						// alle tipologie intervento deselezionate. 
						SchedaProgettoVO schedaProgetto = input.schedaProgettoVO;
						if(schedaProgetto!=null){
							
							HashMap<Integer, String> mappaCritErr = new HashMap<Integer, String>();
							
							CriterioVO[] listaCriteri = schedaProgetto.getCriteriList();
							for (CriterioVO criterioVO : listaCriteri) {
								
								 logger.debug(prf + " idCriterio = "+criterioVO.getIdCriterio() +
										 ", idTipologiaIntervento="+criterioVO.getIdTipolIntervento());
										
								 if(criterioVO.getIdTipolIntervento()!=null && tipologieIntervDeselezionate.contains("-"+criterioVO.getIdTipolIntervento()+"-")){
									 // trovata tipologiaIntervento deselezionata presente in Scheda Corso
									 logger.debug(prf + " trovata tipologiaIntervento deselezionata  presente in Scheda Corso");
									 mappaCritErr.put(criterioVO.getIdTipolIntervento(), criterioVO.getDescrBreveCriterio());
								 }
							}
							
							String msgError = " ";
							String msgErrorEnd = " ";
							if(!mappaCritErr.isEmpty()){
								if(mappaCritErr.size()==1){
									msgError = "Per poter eliminare la tipologia di intervento evidenziata, cancellare nella sezione Scheda Progetto tutte le informazioni relative al criterio ";
									msgErrorEnd = " ad essa associato";
								}else{
									msgError = "Per poter eliminare le tipologie di intervento evidenziate, cancellare nella sezione Scheda Progetto tutte le informazioni relative ai criteri ";
									msgErrorEnd = " ad esse associati";
								}
								for (Integer id1 : mappaCritErr.keySet()) {
									msgError = msgError + "\""+mappaCritErr.get(id1)+"\", ";
									addMessage(newMessages,"_carattPrgTipolInterv", id1+"D");
								}
								logger.debug(prf + " msgError="+msgError);
								// elimino virgola finale
								msgError = msgError.substring(0, msgError.lastIndexOf(","));
								
								msgError = msgError + msgErrorEnd;
								logger.debug(prf + " msgError="+msgError);
								
								addMessage(newMessages,"_caratteristicheProgetto", msgError);
								addMessage(newMessages,"_carattPrgTipolIntervMsg", "Impossibile deselezionare questa tipologia di intervento");
							}
						}
					 }else{
						 logger.debug(prf + " listaTipologIntervAssociate = NULL");
					 }
				}
			}
		// FINE JIRA FINDOM 1813 : Scheda progetto - Controlli nel tab
		
		} else{
			logger.info(prf + " _caratteristicheProgetto non presente o vuoto");
		}
		
		logger.info(prf + " END");
		return newMessages;
	}

	// associa gli eventuali dettagli a ciascuna tipologia intervento;
	// ritorna il numero di tipologie intervento che hanno almeno un dettaglio
	private int caricaDettaglioTipologia(List<TipologiaInterventoVO> parTipologiaInterventoList, Logger logger)
			throws NumberFormatException, CommonalityException {
		int numDettagliValorizzati = 0;

		if (parTipologiaInterventoList == null || parTipologiaInterventoList.isEmpty()) {
			return numDettagliValorizzati;
		}

		for (int i = 0; i < parTipologiaInterventoList.size(); i++) {
			TipologiaInterventoVO curTipologia = parTipologiaInterventoList.get(i);
			if (curTipologia != null) {
				String idTipoIntervento = curTipologia.getIdTipoIntervento();

				List<TipologiaDettaglioInterventoVO> dettaglioList = CaratteristicheProgettoNGDAO.getDettaglioInterventoList(new Integer(idTipoIntervento)); // dati da tabelle DB (non xml)
				if (dettaglioList != null && !dettaglioList.isEmpty()) 
				{
					numDettagliValorizzati += 1;
					// in dettaglioList valorizzo checked in base al valore eventualmente presente
					// nell'xml
					CaratteristicheProgettoNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
					if (_caratteristicheProgetto != null) 
					{
						
						List<TipologiaInterventoVO> tipologiaInterventoSalvataList = new ArrayList<TipologiaInterventoVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList())); // presa xml
						if (tipologiaInterventoSalvataList != null && !tipologiaInterventoSalvataList.isEmpty()
								&& tipologiaInterventoSalvataList.get(i) != null) 
						{

							TipologiaInterventoVO curTipologiaSalvata = tipologiaInterventoSalvataList.get(i);
							
							TipologiaDettaglioInterventoVO[] dettaglioSalvatoList = curTipologiaSalvata.getDettaglioInterventoList();
							if (dettaglioSalvatoList != null && dettaglioSalvatoList.length > 0) 
							{
								for (int j = 0; j < dettaglioList.size(); j++) 
								{
									TipologiaDettaglioInterventoVO curDettaglio = dettaglioList.get(j);
									
									
									if (curDettaglio != null) 
									{
										for (TipologiaDettaglioInterventoVO curDettaglioSalvato : dettaglioSalvatoList) 
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

					curTipologia.setDettaglioInterventoList ( dettaglioList.toArray(new TipologiaDettaglioInterventoVO[0]));
					int numDettagli = dettaglioList.size();

					curTipologia.setNumDettagli(numDettagli + "");

				}
			}
		}
		logger.info("[caricaDettaglioTipologia::numDettagliValorizzati]  = " + numDettagliValorizzati);
		return numDettagliValorizzati;
	}
	
	//MB2018_06_04 ini
	private boolean verificaCoerenzaCultSpese(CultPianoSpeseVO cultPianoSpeseVO,List<TipologiaInterventoVO> tipologiaInterventoList, Logger logger)	throws CommonalityException{		
		logger.info("[CaratteristicheProgettoNG::verificaCoerenzaCultSpese] BEGIN ");
		boolean esito = true;
		if (cultPianoSpeseVO!=null && cultPianoSpeseVO.getPianoSpeseList()!=null && cultPianoSpeseVO.getPianoSpeseList().length>0 && tipologiaInterventoList!=null && !tipologiaInterventoList.isEmpty()) {
			// metto i record di tipologiaInterventoList con checked==true in una mappa
			Map<String, String> tipolIntervCheckedMap = new HashMap<String, String>();
			for (int i = 0; i < tipologiaInterventoList.size(); i++) {
				TipologiaInterventoVO tipologiaInterventoVO = tipologiaInterventoList.get(i);
				String checked = tipologiaInterventoVO.getChecked();
				String idTipoIntervento = tipologiaInterventoVO.getIdTipoIntervento();

				if (checked!=null && "true".equals(checked)) {
					TipologiaDettaglioInterventoVO[] dettaglioInterventoList = tipologiaInterventoVO.getDettaglioInterventoList();
					if (dettaglioInterventoList != null && dettaglioInterventoList.length > 0) {
						for (int j = 0; j < dettaglioInterventoList.length; j++) {
							TipologiaDettaglioInterventoVO dettaglioInterventoVO = dettaglioInterventoList[j];
							String checkedDett = dettaglioInterventoVO.getChecked();
							String idDettIntervento = dettaglioInterventoVO.getIdDettIntervento();
							if (checkedDett!=null && "true".equals(checkedDett)) {
								tipolIntervCheckedMap.put(idTipoIntervento + "-" + idDettIntervento,"checked");
							}
						}
					} else {
						tipolIntervCheckedMap.put(idTipoIntervento + "-", "checked");
					}
				}
			}
			// ciclo su tutti i record di piano spese; per ognuno controllo che la corrispondente tipologiaIntervento ed eventuale dettaglio sia presente e ceccato in caratteristiche progetto
			List<DettaglioVoceSpesaInterventoCulturaVO> pianoSpeseList = Arrays.asList(cultPianoSpeseVO.getPianoSpeseList());
			for (int i = 0; i < pianoSpeseList.size(); i++) {
				DettaglioVoceSpesaInterventoCulturaVO curVoceSpesa = pianoSpeseList.get(i);
				if (curVoceSpesa != null) {
					String curTipoRecord = curVoceSpesa.getTipoRecord();
					if(curTipoRecord != null && (curTipoRecord.equals("1") || curTipoRecord.equals("2"))){
						String curIdTipoIntervento = curVoceSpesa.getIdTipoIntervento(); 
						String curIdDettIntervento = curVoceSpesa.getIdDettIntervento() == null ? "" : curVoceSpesa.getIdDettIntervento();
						String identificativo = curIdTipoIntervento + "-" + curIdDettIntervento;
						if (!tipolIntervCheckedMap.containsKey(identificativo)) {
							esito=false;
							logger.info("[CaratteristicheProgettoNG::verificaCoerenzaCultSpese] record di pianoSpese con identificativo" + identificativo + " non presente in cartatteristicheProgetto ");
							break;
						}
					}
				}
			}
		}
		logger.info("[CaratteristicheProgettoNG::verificaCoerenzaCultSpese] END ");
		return esito; 
	}
	//MB2018_06_04 fine

}
