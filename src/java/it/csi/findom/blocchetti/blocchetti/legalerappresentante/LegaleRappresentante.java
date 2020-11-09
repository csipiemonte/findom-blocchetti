/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.legalerappresentante;


import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.dao.TipoDocRiconoscimentoDAO;
import it.csi.findom.blocchetti.common.util.TrasformaClassiAAEP2VO;
import it.csi.findom.blocchetti.common.vo.aaep.CaricaVO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.aaep.PersonaINFOCVO;
import it.csi.findom.blocchetti.common.vo.aaep.PersonaVO;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteDocumentoVO;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.SiglaProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.common.vo.tipodocriconoscimento.TipoDocRiconoscimentoVO;
import it.csi.findom.blocchetti.common.ws.aaep.AaepWs;
import it.csi.findom.blocchetti.commonality.ControlCodFisc;
import it.csi.findom.blocchetti.commonality.ControlPartitaIVA;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.LegaleRappresentanteDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.VistaUltimaDomandaDto;
import it.csi.melograno.aggregatore.business.AggregatoreFactory;
import it.csi.melograno.aggregatore.business.DataModel;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.melograno.aggregatore.exception.AggregatoreException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;


public class LegaleRappresentante extends Commonality {

	LegaleRappresentanteInput input = new LegaleRappresentanteInput();

	@Override
	public LegaleRappresentanteInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[LegaleRappresentante::inject] BEGIN");

		try {
			LegaleRappresentanteOutput output = new LegaleRappresentanteOutput();

			// inizializzazione aggiunte
			List<ProvinciaVO> provinciaList = new ArrayList<ProvinciaVO>();
			
			/** Jira 1842 - */
			List<ProvinciaVO> provinciaResidenzaList = new ArrayList<ProvinciaVO>();
			
			boolean isProvPiemontese = false;
			boolean isSiglaProvResidenza = false;
			
			List<ComuneVO> comuneNascitaList = new ArrayList<ComuneVO>();
			List<ComuneVO> comuneResidenzaList = new ArrayList<ComuneVO>();
			List<StatoEsteroVO> statoEsteroList = new ArrayList<StatoEsteroVO>();
			List<TipoDocRiconoscimentoVO> tipoDocRiconoscimentoList = new ArrayList<TipoDocRiconoscimentoVO>();

			String legaleRappresentante_siglaProvinciaNascita = "";
			String legaleRappresentante_siglaProvinciaResidenza = "";
			String presenzaSD = "no";

			LegaleRappresentanteVO LRMap = new LegaleRappresentanteVO();

			PersonaINFOCVO legRappr = null; // dati provenienti da AAEP
			List<LRVo> listLRAAEP = new ArrayList<LRVo>(); // dati provenienti da AAEP

			String importLRFromAAEP = input.importLRFromAAEP;
			String newLRNotFromAAEP = input.newLRNotFromAAEP;
			String identificativoLRsuAAEP = input.identificativoLRsuAAEP; // formato idAzienda/idFonteDato/idPersona
			logger.info("[LegaleRappresentante::inject] importLRFromAAEP=" + importLRFromAAEP);  // null con cf: 00588010033 
			logger.info("[LegaleRappresentante::inject] newLRNotFromAAEP=" + newLRNotFromAAEP); // null con cf: 00588010033
			logger.info("[LegaleRappresentante::inject] identificativoLRsuAAEP=" + identificativoLRsuAAEP); // null con cf: 00588010033

//			ImpresaVO enteImpresa = (ImpresaVO) SessionCache.getInstance().get("enteImpresa");
			ImpresaVO enteImpresa = (ImpresaVO)TrasformaClassiAAEP2VO.impresa2ImpresaVO((Impresa)SessionCache.getInstance().get("enteImpresa"));

			String lrFromAAEP = "no";
			String presenzaLRAAEPSuDB = "no";
			String mostraMsgAAEP = "N";
			String mostraMsgSelLR = "N";
			String viewFromLR = "no"; // diventa si se voglio visualizzare il form dei dati del LR
			String mostraMsgDatiUltimaDomanda = "N"; // se 'S' visualizza il messaggio che i dati provengono dall'ultima domanda

			String visMsgLRdaUltimaDomanda = "false";
			String visMsgLRdaAAEP = "false";
			String svuotaCampiResidenza = "false";
			
			if (!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
				// lista degli stati esteri e delle province
				
				statoEsteroList = LuoghiDAO.getStatoEsteroList(logger);

				/** Jira: 1842 - */
				if ("true".equals(input._legalerappresentante_res_prov_piemonte)) 
				{
					// residenza solo in province piemontesi
					String sediExtraPiemonte = "false";
					provinciaResidenzaList = LuoghiDAO.getProvinciaList(sediExtraPiemonte, logger);
					logger.info("[LegaleRappresentante::inject] Recupero dal db solo Province del Piemonte= " + provinciaResidenzaList.size());
				}
				
				// lista province di nascita
				provinciaList = LuoghiDAO.getProvinciaList(logger); // 113

				tipoDocRiconoscimentoList = TipoDocRiconoscimentoDAO.getTipoDocRiconoscimentoList(logger);

				// carico la lista dei legali rappresentanti di AAEP
				listLRAAEP = getListaLRAAEP(enteImpresa, logger); // AAAAAA00A11B000J con cf: 00588010033
				
				if (listLRAAEP.isEmpty()) {
					// carico la lista dei legali rappresentanti dell'ultima domanda
					listLRAAEP = getListaLRUltimaDomanda(info.getStatusInfo(), logger); // AAAAAA00A11B000J con cf: 00588010033
					if (!listLRAAEP.isEmpty()) {
						visMsgLRdaUltimaDomanda = "true";
					}
				} else {
					visMsgLRdaAAEP = "true";
				}
				
				logger.info("[LegaleRappresentante::inject] listLRAAEP.size=" + listLRAAEP.size());
				
				viewFromLR = "no"; // diventa si se voglio visualizzare il form dei dati del LR

				// se non ci sono legali rappresentatni su AAEP o se li abbiamo esclusi per
				// codice fiscale mancante
				// dobbiamo mostrare il form di inserimento
				if (listLRAAEP == null || listLRAAEP.isEmpty()) {
					viewFromLR = "si";
				}

				// AA - inizio
				LegaleRappresentanteVO _legaleRappresentanteMap = input.legaleRappresentante;

				if (_legaleRappresentanteMap != null && !_legaleRappresentanteMap.isEmpty()) {
					String s = _legaleRappresentanteMap.getLrFromAAEP();
					logger.info("s vale: " + s);
					
					if ("si".equals(s))	lrFromAAEP = "si";
				}
				logger.info("[LegaleRappresentante::inject]  lrFromAAEP=" + lrFromAAEP); // no se da AAEP con CF: 00588010033

				String idDomanda = info.getStatusInfo().getNumProposta() + "";
				logger.info("[LegaleRappresentante::inject]  idDomanda=" + idDomanda); // 3851 - 3856 - 3857
				String flagDomanda = LegaleRappresentanteDao.getPresenzaLrAAEP(idDomanda);
				logger.info("[LegaleRappresentante::inject]  flagDomanda=" + flagDomanda); // null
				
				if (flagDomanda != null) {
					logger.info("flagDomanda: " + flagDomanda);
					presenzaLRAAEPSuDB = flagDomanda;
				}
				logger.info("[LegaleRappresentante::inject] presenzaLRAAEPSuDB=" + presenzaLRAAEPSuDB); // no LR da database con cf: 00588010033
				// AA - fine

				if (StringUtils.equals("si", importLRFromAAEP)) {

					// carico LR from AAEP
					logger.info("[LegaleRappresentante::inject]  CARICO LRF FROM AAEP");
					// ricavo i parametri per invocare il WS
					if (!StringUtils.isBlank(identificativoLRsuAAEP)) 
					{
						viewFromLR = "si";
						mostraMsgAAEP = "S";

						if (StringUtils.equals("//", identificativoLRsuAAEP)) {
							// carico i dati del LR dall'ultima domanda
							LRMap = inserisciInformazioniUltimaDomandaLegaleRappresentante(LRMap);
							mostraMsgDatiUltimaDomanda = "S";

						} else {
							legRappr = AaepWs.getInstance().getDatiPersonaFromAAEPOld(identificativoLRsuAAEP, logger);
						
							// valori ottenuti dal WS
							logger.info("CodiceFiscale: " + legRappr.getCodiceFiscale()); // BRSGNN42S05B883J
							LRMap.setCodiceFiscale(legRappr.getCodiceFiscale());
							
							logger.info("Cognome: " + legRappr.getCognome()); 	// BRUSTIA
							LRMap.setCognome(legRappr.getCognome()); 			
							
							logger.info("Nome: " + legRappr.getNome());			// GIOVANNI
							LRMap.setNome(legRappr.getNome());
							
							logger.info("Sesso: " + legRappr.getSesso());
							LRMap.setGenere(legRappr.getSesso()); // legRappr.get("sesso"));  : verificare : in analisi
																	// non si dice di prenderlo da AAEP
							// nascita
							if (!StringUtils.isBlank(legRappr.getSiglaProvNascita())) {
								logger.info("Italia");	
								LRMap.setLuogoNascita("Italia");
								
							} else {
								logger.info("Estero: ");
								LRMap.setLuogoNascita("Estero");
							}
	
							logger.info("[LegaleRappresentante::inject]  AAEP siglaProvNascita=" + legRappr.getSiglaProvNascita()); // AAEP siglaProvNascita=NO
							LRMap.setSiglaProvinciaNascita(legRappr.getSiglaProvNascita());
							
							// fr - 15/01/2019 - spostato fuori assegnazione dati LR
							// legaleRappresentante_siglaProvinciaNascita = legRappr.getSiglaProvNascita();
							logger.info("[LegaleRappresentante::inject]  SiglaProvNascita = " + legRappr.getSiglaProvNascita()); // SiglaProvNascita = NO
							ProvinciaVO mappaNs = getProvinciaMap(provinciaList, legRappr.getSiglaProvNascita());
							//
							logger.info("[LegaleRappresentante::inject]  Descrizione = " + mappaNs.getDescrizione()); // Descrizione = NOVARA
							LRMap.setProvinciaNascitaDescrizione(mappaNs.getDescrizione());
							
							logger.info("[LegaleRappresentante::inject]  Codice = " + mappaNs.getCodice()); // 003
							LRMap.setProvinciaNascita(mappaNs.getCodice());
							//
							// carico la lista dei comuni per la provincia data
							// fr - 15/01/2019 - spostato fuori assegnazione dati LR
							// comuneNascitaList = LuoghiDAO.getComuneList(mappaNs.getCodice(), logger);
							
							logger.info("[LegaleRappresentante::inject]  AAEP descrComuneNascita=" + legRappr.getDescrComuneNascita()); // CASALEGGIO NOVARA
							logger.info("[LegaleRappresentante::inject]  AAEP codComuneNascita=" + legRappr.getCodComuneNascita()); // 003039
							LRMap.setComuneNascitaDescrizione(legRappr.getDescrComuneNascita());
	
							// su AAEP codComuneNascita a volte non esiste, cerco di ottenerlo tramite la descrizione
							if (StringUtils.isBlank(legRappr.getCodComuneNascita())) {
								String codCom = "";
								if (!StringUtils.isBlank(LRMap.getProvinciaNascita())) {
									codCom = LuoghiDAO.getCodiceComuneByDescrizione(LRMap.getProvinciaNascita(), legRappr.getDescrComuneNascita(), logger);
									logger.info("[LegaleRappresentante::inject] AAEP codCom="+codCom);
									
								}
								LRMap.setComuneNascita(codCom);
							} else {
								LRMap.setComuneNascita(legRappr.getCodComuneNascita());
								logger.info("[LegaleRappresentante::inject] settaggio AAEP CodComuneNascita="+legRappr.getCodComuneNascita());
							}
	
							// su AAEP i seguenti campi a volte non esistono
							// codCittadinanza : GB
							// codStatoNascita : GB
							// descrCittadinanza : GRAN BRETAGNA
							// descrStatoNascita : GRAN BRETAGNA
							// descrComuneNascita : LONDRA (REGNO UNITO)
							LRMap.setStatoEsteroNascitaDescrizione(legRappr.getDescrStatoNascita());
							logger.info("[LegaleRappresentante::inject]  AAEP codStatoNascita="	+ legRappr.getCodStatoNascita()); // null
							StatoEsteroVO mapSE = getStatoEstero(statoEsteroList, legRappr.getCodStatoNascita());
							LRMap.setStatoEsteroNascita(mapSE.getCodice()); //  : codice diverso dal nostro in
																			// ext_d_stati_esteri
	
							LRMap.setDataNascita(legRappr.getDataNascita()); // 05/11/1942
							// le informazioni sul documento non esistono su AAEP
							LegaleRappresentanteDocumentoVO docMap = new LegaleRappresentanteDocumentoVO();
							
							// Map docMap = new HashMap();
							docMap.setDataRilascioDoc("");
							docMap.setDescrizioneTipoDocRiconoscimento("");
							docMap.setDocRilasciatoDa("");
							docMap.setIdTipoDocRiconoscimento("");
							docMap.setNumDocumentoRiconoscimento("");
							LRMap.setDocumento(docMap);
							//
							// residenza
							if (!StringUtils.isBlank(legRappr.getSiglaProvResidenza())) {
								LRMap.setLuogoResidenza("Italia");
							} else {
								if (!StringUtils.isBlank(legRappr.getDescrComuneRes())) {
									LRMap.setLuogoResidenza("Estero");
								}
							}
							//
							logger.info("[LegaleRappresentante::inject] Sigla prov residenza= "+ legRappr.getSiglaProvResidenza()); // NOVARA
							LRMap.setSiglaProvinciaResidenza(legRappr.getSiglaProvResidenza());
							// fr - 15/01/2019 - spostato fuori assegnazione dati LR
							// legaleRappresentante_siglaProvinciaResidenza = legRappr.getSiglaProvResidenza();
							// inizializzo i due valori e poi li estraggo dalla lista delle province
	
							ProvinciaVO mappaRs = getProvinciaMap(provinciaList, legRappr.getSiglaProvResidenza());
							logger.info("[LegaleRappresentante::inject] Prov residenza descrizione= "+ mappaRs.getDescrizione());
							LRMap.setProvinciaResidenzaDescrizione(mappaRs.getDescrizione());
							logger.info("[LegaleRappresentante::inject] Codice residenza= "+ mappaRs.getCodice());
							LRMap.setProvinciaResidenza(mappaRs.getCodice());
							//
							// carico la lista dei comuni per la provincia data
							// fr - 15/01/2019 - spostato fuori assegnazione dati LR
							// comuneResidenzaList = LuoghiDAO.getComuneList(mappaRs.getCodice(), logger);
							//
							LRMap.setComuneResidenzaDescrizione(legRappr.getDescrComuneRes());
							//
							// su AAEP codComuneNascita a volte non esiste, cerco di ottenerlo tramite la descrizione
							if (StringUtils.isBlank(legRappr.getCodComuneRes())) {
								String codCom = "";
								logger.info("[LegaleRappresentante::inject] DescrComuneRes= "+ legRappr.getDescrComuneRes()); // NOVARA
								if (!StringUtils.isBlank(legRappr.getDescrComuneRes())) {
	
									if (!StringUtils.isBlank(LRMap.getProvinciaResidenza())) {
										codCom = LuoghiDAO.getCodiceComuneByDescrizione(LRMap.getProvinciaResidenza(), legRappr.getDescrComuneRes(), logger);
										logger.info("[LegaleRappresentante::inject] AAEP codCom="+codCom); // 003106
									}
								}
								LRMap.setComuneResidenza(codCom);
							} else {
								LRMap.setComuneResidenza(legRappr.getCodComuneRes()); // 003106
							}
	
							StatoEsteroVO mapSER = getStatoEstero(statoEsteroList, legRappr.getCodStatoRes());
							logger.info("[LegaleRappresentante::inject] mapSER codice= "+mapSER.getCodice() + " Descrizione cod: " + mapSER.getDescrizione()); //
							LRMap.setStatoEsteroResidenza(mapSER.getCodice());
	
							logger.info("[LegaleRappresentante::inject]  Descrizione stato residenza="+legRappr.getDescrStatoRes()); // null
							LRMap.setStatoEsteroResidenzaDescrizione(legRappr.getDescrStatoRes());
							
							logger.info("[LegaleRappresentante::inject]  Descrizione comune residenza="+legRappr.getDescrComuneRes()); // NOVARA
							LRMap.setCittaEsteraResidenza(legRappr.getDescrComuneRes());
	
							logger.info("[LegaleRappresentante::inject]  Cap residenza="+legRappr.getCapResidenza()); // 28100
							LRMap.setCap(legRappr.getCapResidenza());
							
							String indirizzo = (StringUtils.isBlank(legRappr.getDescrToponimoResid())) ? ""
									: legRappr.getDescrToponimoResid() + " ";
							logger.info("[LegaleRappresentante::inject]  indirizzo= "+ indirizzo); //
							indirizzo += (StringUtils.isBlank(legRappr.getViaResidenza())) ? ""
									: legRappr.getViaResidenza(); // Corso
							LRMap.setIndirizzo(indirizzo);
							logger.info("[LegaleRappresentante::inject]  NumCivicoResid= "+ legRappr.getNumCivicoResid()); // 43
							LRMap.setNumCivico(legRappr.getNumCivicoResid());
	
							LRMap.setPresenzaSoggettoDelegato("");
							
							// fr - 09/01/2019 aggiorna i dati del legale rappresentante selezionato con i dati inseriti nell'ultima domanda inserita
							LRMap = aggiornaConInformazioniUltimaDomandaLegaleRappresentante(LRMap);

							
							// fr - 15/01/2019 - spostato qui dopo assegnazione dati LR
							legaleRappresentante_siglaProvinciaNascita = LRMap.getSiglaProvinciaNascita();
							logger.info("[LegaleRappresentante::inject] legaleRappresentante_siglaProvinciaNascita="+legaleRappresentante_siglaProvinciaNascita); //
							legaleRappresentante_siglaProvinciaResidenza = LRMap.getSiglaProvinciaResidenza();
							logger.info("[LegaleRappresentante::inject] legaleRappresentante_siglaProvinciaResidenza= " + legaleRappresentante_siglaProvinciaResidenza);
							comuneNascitaList = LuoghiDAO.getComuneList(LRMap.getProvinciaNascita(), logger);
							logger.info("[LegaleRappresentante::inject] comuneNascitaList= " + comuneNascitaList);
							comuneResidenzaList = LuoghiDAO.getComuneList(LRMap.getProvinciaResidenza(), logger);
							logger.info("[LegaleRappresentante::inject] comuneResidenzaList= " + comuneResidenzaList);
							

							
							// fr - 15/01/2019 - spostato qui dopo assegnazione dati LR
							legaleRappresentante_siglaProvinciaNascita = LRMap.getSiglaProvinciaNascita();
							legaleRappresentante_siglaProvinciaResidenza = LRMap.getSiglaProvinciaResidenza();
							comuneNascitaList = LuoghiDAO.getComuneList(LRMap.getProvinciaNascita(), logger);
							comuneResidenzaList = LuoghiDAO.getComuneList(LRMap.getProvinciaResidenza(), logger);
						}

						// fr - 15/01/2019 - spostato qui dopo assegnazione dati LR
						legaleRappresentante_siglaProvinciaNascita = LRMap.getSiglaProvinciaNascita();
						legaleRappresentante_siglaProvinciaResidenza = LRMap.getSiglaProvinciaResidenza();
						comuneNascitaList = LuoghiDAO.getComuneList(LRMap.getProvinciaNascita(), logger);
						comuneResidenzaList = LuoghiDAO.getComuneList(LRMap.getProvinciaResidenza(), logger);
						
					} else {
						mostraMsgSelLR = "S";
					}
				}

				if (StringUtils.equals("si", newLRNotFromAAEP)) {
					// creo nuovo LR
					logger.info("[LegaleRappresentante::inject] CREO NUOVO LR ");
					viewFromLR = "si";

					// svuoto LRMap se non gia' vuoto
					LRMap = new LegaleRappresentanteVO();
					_legaleRappresentanteMap = new LegaleRappresentanteVO();
				}

				// AA - inizio
				// Se la pagina viene ricaricata per un motivo diverso dal click su "Importa" e
				// "Inserisci nuovo"
				// (per esempio per la selezione sulla combo delle province)
				// é necessario ripristinare la map legRappr utile per popolare gli hidden del
				// legaleRappresentanteAAEP sul tenplate.
				// La riprendiamo dalla sessione
				if ((StringUtils.equals("no", importLRFromAAEP) || StringUtils.equals("", importLRFromAAEP))
						&& (StringUtils.equals("no", newLRNotFromAAEP) || StringUtils.equals("", newLRNotFromAAEP))
						&& "si".equals(lrFromAAEP)) {

					if (SessionCache.getInstance().get("mappaDatiConsLRAAEP") != null)
						legRappr = (PersonaINFOCVO) SessionCache.getInstance().get("mappaDatiConsLRAAEP");
				}
				// AA - fine
				if ((listLRAAEP == null || listLRAAEP.isEmpty()) && !StringUtils.equals("si", importLRFromAAEP)
						&& !StringUtils.equals("si", newLRNotFromAAEP)) {
					viewFromLR = "si";
					logger.info("[LegaleRappresentante::inject]   enteImpresa null e nessun parametro");
				}

				// utilizzo i dati del LR salvati sul nostro XML
				if (_legaleRappresentanteMap != null && _legaleRappresentanteMap.getCodiceFiscale() != null
						&& !StringUtils.equals("si", importLRFromAAEP)) {

					viewFromLR = "si";

					// la provincia di nascita presente eventualmente in request ha la precedenza su
					// quella presente nell'xml
					// perche' sull'onchange delle combo provincia nascita devo tener conto del
					// valore appena scelto
					String provinciaNascita = input.legaleRappresentanteDotProvinciaNascita;
					logger.info("[LegaleRappresentante::inject]  provinciaNascita:" + provinciaNascita); // 096
					if (StringUtils.isBlank(provinciaNascita)) {
						// se non ho una provincia in request, considero quella dell'xml
						provinciaNascita = _legaleRappresentanteMap.getProvinciaNascita();
					}

					if (StringUtils.isNotBlank(provinciaNascita)) {
						comuneNascitaList = LuoghiDAO.getComuneList(provinciaNascita, logger); // 86
						// ottiene la sigla della provincia di nascita
						List<SiglaProvinciaVO> siglaProvinciaList = LuoghiDAO.getSiglaProvinciaList(provinciaNascita,
								logger);
						if (siglaProvinciaList != null && siglaProvinciaList.size() > 0) {
							legaleRappresentante_siglaProvinciaNascita = siglaProvinciaList.get(0).getSigla();
							logger.info("[LegaleRappresentante::inject]  legaleRappresentante_siglaProvinciaNascita:"
									+ legaleRappresentante_siglaProvinciaNascita);
						}
					}

					// la provincia di residenza presente eventualmente in request ha la precedenza
					// su quella presente nell'xml
					// perche' sull'onchange delle combo provincia residenza devo tener conto del
					// valore appena scelto
					String provinciaResidenza = input.legaleRappresentanteDotprovinciaResidenza;
					logger.info("[LegaleRappresentante::inject]  provinciaResidenza:" + provinciaResidenza); // 006
					if (StringUtils.isBlank(provinciaResidenza)) {
						// se non ho una provincia in request, considero quella dell'xml
						provinciaResidenza = _legaleRappresentanteMap.getProvinciaResidenza();
					}

					if (StringUtils.isNotBlank(provinciaResidenza)) { // 006
						comuneResidenzaList = LuoghiDAO.getComuneList(provinciaResidenza, logger); // 192
						// ottiene la sigla della provincia di residenza
						List<SiglaProvinciaVO> siglaProvinciaResidenzaList = LuoghiDAO.getSiglaProvinciaList(provinciaResidenza, logger);
						// logger.info("[LegaleRappresentante::inject] siglaProvinciaResidenzaList:" +
						// siglaProvinciaResidenzaList);
						if (siglaProvinciaResidenzaList != null && siglaProvinciaResidenzaList.size() > 0) {
							legaleRappresentante_siglaProvinciaResidenza = siglaProvinciaResidenzaList.get(0)
									.getSigla();
							logger.info("[LegaleRappresentante::inject]  legaleRappresentante_siglaProvinciaResidenza:" + legaleRappresentante_siglaProvinciaResidenza);
						}
					}
					// questa variabile viene letta all'ingresso nella pagina xhtml e in base al suo
					// valore si imposta
					// il name e il value di un campo hidden che sul salvataggio viene postato con
					// l'effetto, in caso
					// di soggetto delegato impostato a 'no' nel radio, di cancellare dall'xml il
					// nodo '_soggettoDelegato'
					presenzaSD = _legaleRappresentanteMap.getPresenzaSoggettoDelegato();
					logger.info("[LegaleRappresentante::inject]  presenzaSD = " + presenzaSD);

					logger.info("[LegaleRappresentante::inject]  cap = " + _legaleRappresentanteMap.getCap());
					LRMap.setCap(_legaleRappresentanteMap.getCap());
					
					logger.info("[LegaleRappresentante::inject]  citta estera residenza = " + _legaleRappresentanteMap.getCittaEsteraResidenza());
					LRMap.setCittaEsteraResidenza(_legaleRappresentanteMap.getCittaEsteraResidenza());
					
					logger.info("[LegaleRappresentante::inject]  codice fiscale = " + _legaleRappresentanteMap.getCodiceFiscale());
					LRMap.setCodiceFiscale(_legaleRappresentanteMap.getCodiceFiscale());
					
					logger.info("[LegaleRappresentante::inject]  Cognome = " + _legaleRappresentanteMap.getCognome());
					LRMap.setCognome(_legaleRappresentanteMap.getCognome());
					
					logger.info("[LegaleRappresentante::inject]  Genere = " + _legaleRappresentanteMap.getGenere());
					LRMap.setGenere(_legaleRappresentanteMap.getGenere());

					logger.info("[LegaleRappresentante::inject]  ComuneNascita = " + _legaleRappresentanteMap.getComuneNascita());
					LRMap.setComuneNascita(_legaleRappresentanteMap.getComuneNascita());
					
					logger.info("[LegaleRappresentante::inject]  ComuneNascitaDescrizione = " + _legaleRappresentanteMap.getComuneNascitaDescrizione());
					LRMap.setComuneNascitaDescrizione(_legaleRappresentanteMap.getComuneNascitaDescrizione());
					
					logger.info("[LegaleRappresentante::inject]  ComuneResidenza = " + _legaleRappresentanteMap.getComuneResidenza());
					LRMap.setComuneResidenza(_legaleRappresentanteMap.getComuneResidenza());
					
					logger.info("[LegaleRappresentante::inject]  Comune residenza descrizione = " + _legaleRappresentanteMap.getComuneResidenzaDescrizione());
					LRMap.setComuneResidenzaDescrizione(_legaleRappresentanteMap.getComuneResidenzaDescrizione());
					
					logger.info("[LegaleRappresentante::inject]  Data Nascita = " + _legaleRappresentanteMap.getDataNascita());
					LRMap.setDataNascita(_legaleRappresentanteMap.getDataNascita());

					LegaleRappresentanteDocumentoVO docMap = new LegaleRappresentanteDocumentoVO();
					docMap.setDataRilascioDoc(_legaleRappresentanteMap.getDocumento().getDataRilascioDoc());
					docMap.setDescrizioneTipoDocRiconoscimento(
							_legaleRappresentanteMap.getDocumento().getDescrizioneTipoDocRiconoscimento());
					docMap.setDocRilasciatoDa(_legaleRappresentanteMap.getDocumento().getDocRilasciatoDa());
					docMap.setIdTipoDocRiconoscimento(
							_legaleRappresentanteMap.getDocumento().getIdTipoDocRiconoscimento());
					docMap.setNumDocumentoRiconoscimento(
							_legaleRappresentanteMap.getDocumento().getNumDocumentoRiconoscimento());
					LRMap.setDocumento(docMap);

					LRMap.setIndirizzo(_legaleRappresentanteMap.getIndirizzo());
					LRMap.setLuogoNascita(_legaleRappresentanteMap.getLuogoNascita());
					LRMap.setLuogoResidenza(_legaleRappresentanteMap.getLuogoResidenza());
					LRMap.setNome(_legaleRappresentanteMap.getNome());
					LRMap.setNumCivico(_legaleRappresentanteMap.getNumCivico());
					LRMap.setPresenzaSoggettoDelegato(_legaleRappresentanteMap.getPresenzaSoggettoDelegato());
					LRMap.setProvinciaNascita(_legaleRappresentanteMap.getProvinciaNascita());
					LRMap.setProvinciaNascitaDescrizione(_legaleRappresentanteMap.getProvinciaNascitaDescrizione());
					LRMap.setProvinciaResidenza(_legaleRappresentanteMap.getProvinciaResidenza());
					LRMap.setProvinciaResidenzaDescrizione(_legaleRappresentanteMap.getProvinciaResidenzaDescrizione());
					LRMap.setSiglaProvinciaNascita(_legaleRappresentanteMap.getSiglaProvinciaNascita());
					LRMap.setSiglaProvinciaResidenza(_legaleRappresentanteMap.getSiglaProvinciaResidenza());
					LRMap.setStatoEsteroNascita(_legaleRappresentanteMap.getStatoEsteroNascita());
					LRMap.setStatoEsteroNascitaDescrizione(_legaleRappresentanteMap.getStatoEsteroNascitaDescrizione());
					LRMap.setStatoEsteroResidenza(_legaleRappresentanteMap.getStatoEsteroResidenza());
					LRMap.setStatoEsteroResidenzaDescrizione(
							_legaleRappresentanteMap.getStatoEsteroResidenzaDescrizione());
				}

				// if (presenzaLRAAEPSuDB=="si" && lrFromAAEP=="no"){
				if ("no".equals(lrFromAAEP)) {

					FinStatusInfo statusInfo = info.getStatusInfo();

					String cfBeneficiario = statusInfo.getCodFiscaleBeneficiario(); // codice fiscale del beneficiario
					String templateCode = input.xformId; // (String)context.get("xformId"); //id bando
					Integer modelProg = input.xformProg; // progressivo domanda
					String formName = input.xformName;
					String statoProposta = statusInfo.getStatoProposta();
					logger.info("[LegaleRappresentante::inject]  *************************** templateCode = "
							+ templateCode + ", modelProg = " + modelProg + ", formName =  " + formName
							+ ", statoProposta = " + statoProposta);
					
					logger.info("cfBeneficiario: " + cfBeneficiario);
					logger.info("templateCode: " + templateCode);
					logger.info("modelProg: " + modelProg);
					logger.info("formName: " + formName);
					logger.info("statoProposta: " + statoProposta);
					
					DataModel mod = AggregatoreFactory.create().readModel(cfBeneficiario, templateCode, modelProg);
					TreeMap<String, String> mapSerializedModel = mod.getSerializedModel();
					
					mapSerializedModel.put("_legaleRappresentanteAAEP", "DELETED");
					logger.info(
							"[LegaleRappresentante::inject]  ######## MARCATURA DELETED EFFETTUATA ###############################################");

					mod.setSerializedModel(mapSerializedModel);
					logger.info("[LegaleRappresentante::inject]  ######## mapSerializedModel dopo marcatura  DELETED "
							+ mapSerializedModel);
					AggregatoreFactory.create().saveModel(cfBeneficiario, templateCode, modelProg, formName, mod, null,statoProposta);

					logger.info(
							"[LegaleRappresentante::inject]  ######## MODELLO SALVATO ###############################################");

					logger.info(
							"[LegaleRappresentante::inject]  ##########################################FINE###############################################");
					logger.info(
							"[LegaleRappresentante::inject]  ##########################################FINE###############################################");
					logger.info(
							"[LegaleRappresentante::inject]  ##########################################FINE###############################################");
				}

			}
			logger.info("[LegaleRappresentante::inject]  [INFO] LRMap=" + LRMap);

			//// namespace

			output.statoEsteroList = statoEsteroList;
			output.provinciaList = provinciaList; 			// 113
			output.comuneNascitaList = comuneNascitaList;	//  86
			output.legaleRappresentante_siglaProvinciaNascita = legaleRappresentante_siglaProvinciaNascita;
			output.legaleRappresentante_siglaProvinciaResidenza = legaleRappresentante_siglaProvinciaResidenza;
			output.comuneResidenzaList = comuneResidenzaList;
			output.tipoDocRiconoscimentoList = tipoDocRiconoscimentoList;
			output.presenzaSD = presenzaSD;

			output.listLRAAEP = listLRAAEP;
			
			/** :  LR - */
			output.legRapprAAEP = legRappr;
			
			/** Jira: 1842 - */
			if ("true".equals(input._legalerappresentante_res_prov_piemonte)) 
			{
				
				if(LRMap != null) {
					
					isProvPiemontese = checkProvinciaInPiemonte(LRMap.getSiglaProvinciaResidenza(), provinciaResidenzaList, logger);
					isSiglaProvResidenza = checkProvinciaInPiemonte(legaleRappresentante_siglaProvinciaResidenza, provinciaResidenzaList, logger);
					if( LRMap.getSiglaProvinciaResidenza() == null || (!isProvPiemontese && !isSiglaProvResidenza)) {
						svuotaCampiResidenza = "true";
						logger.info("svotaCampiResidenza risulta: " + svuotaCampiResidenza); 
						comuneResidenzaList.clear();
					}
				}
			}
			
			output.LRMap = LRMap;
			
			
			if (!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
				output.viewFromLR = viewFromLR;
			}
			// AA - inizio
			output.lrFromAAEP = lrFromAAEP;
			output.presenzaLRAAEPSuDB = presenzaLRAAEPSuDB;
			output.mostraMsgAAEP = mostraMsgAAEP;
			output.mostraMsgSelLR = mostraMsgSelLR;
			output.mostraMsgDatiUltimaDomanda = mostraMsgDatiUltimaDomanda;
			
			output.visMsgLRdaAAEP = visMsgLRdaAAEP;
			output.visMsgLRdaUltimaDomanda = visMsgLRdaUltimaDomanda;
			
			
			/** Jira 1842 - */
			if ("true".equals(input._legalerappresentante_res_prov_piemonte)) 
			{
				// residenza solo in province piemontesi
				output.provinciaResidenzaList = provinciaResidenzaList;
				output.svuotaCampiResidenza = svuotaCampiResidenza;
			}
			
			// AA - fine
			logger.info("[LegaleRappresentante::inject]  _legaleRappresentante END");
			
			return output;
		} catch (CommonalityException ex) {
			throw new CommonalityException(ex);
			
		} catch (AggregatoreException e) {
			logger.error("[LegaleRappresentante::inject] Error", e);
			e.getMessage();
			throw new CommonalityException(e);
		} catch (Throwable e) {
			logger.error("[LegaleRappresentante::inject] Error", e);
			e.getMessage();
			throw new CommonalityException(e);
		} finally {
			logger.info("[LegaleRappresentante::inject] END");
		}
	}


	/** Jira: 1842 - */
	private boolean checkProvinciaInPiemonte(String siglaProvinciaResidenza, List<ProvinciaVO> provinciaResidenzaList, Logger logger) {
		
		boolean ris = false;
		
		Map<Object, Object> sigleProvPiemontesiMap = provinciaResidenzaList.stream().collect( Collectors.toMap(ProvinciaVO::getCodice, ProvinciaVO::getSigla));
		String siglaProvRes = siglaProvinciaResidenza;
		logger.info("siglaProvRes :" + siglaProvRes);
		
		if(siglaProvRes != null && !siglaProvRes.isEmpty()) {
			ris = sigleProvPiemontesiMap.values().stream().anyMatch(siglaProv -> siglaProv.equals(siglaProvinciaResidenza));
			logger.info(ris);
		}
		logger.info("ris :" + ris);
		return ris;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		FinCommonInfo info = (FinCommonInfo) info1;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());

		logger.info("[LegaleRappresentante::modelValidate] _legaleRappresentante BEGIN");

		DateValidator validator = DateValidator.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();

		String ERRMSG_CAMPO_OBBLIGATORIO_LR = "- il campo é obbligatorio";
		String ERRMSG_NESSUN_CAMPO_COMPILATO_LR = "- importare un Legale Rappresentante o inserirne uno nuovo prima di salvare";

		String ERRMSG_CF_NOTALPHANUM_LR = "- Il Codice Fiscale non é valido. Inserire solo caratteri alfanumerici";
		String ERRMSG_LR_NOTCORRECT_CF = "- Il Codice Fiscale é formalmente non corretto";
		String ERRMSG_LR_WRONGLENGTH_CF = "- La lunghezza del Codice Fiscale non é valida";

		String ERRMSG_LR_NOTALPHANUM_CGN = "- il campo cognome non é valido. Inserire solo caratteri alfabetici";
		String ERRMSG_LR_NOTALPHANUM_NM = "- il campo nome non é valido. Inserire solo caratteri alfabetici";

		String ERRMSG_LR_NOTSELSTATO_LN = "- il campo Luogo di Nascita é obbligatorio. Selezionare o Italia o Stato Estero";
		String ERRMSG_LR_NOTSELSTATO_LR = "- il campo Luogo di Residenza é obbligatorio. Selezionare o Italia o Stato Estero";
		String ERRMSG_LR_PROVINC = "- il campo Provincia é obbligatorio se valorizzata l'opzione stato Italia";
		String ERRMSG_LR_COMUNE = "- il campo Comune é obbligatorio se valorizzata l'opzione stato Italia";
		String ERRMSG_LR_STESTERO = "- il campo Stato Estero é obbligatorio se valorizzata l'opzione corrispondente";

		String ERRMSG_LR_DATAN_FN = "- Il formato della data non é valido";
		String ERRMSG_LR_DATAN_SUCC = "- data nascita successiva alla data odierna";

		String ERRMSG_LR_DATARILDOC = "- La data di rilascio documento é formalmente non corretta";
		String ERRMSG_LR_DATARILDOC_SUCC = "- La data di rilascio documento é successiva alla data odierna";

		//// validazione panel Legale Rappresentante

		String identificativoLRsuAAEP = input.identificativoLRsuAAEP;
		logger.info("[LegaleRappresentante::modelValidate] identificativoLRsuAAEP: " + identificativoLRsuAAEP + "****");
		logger.info("[LegaleRappresentante::modelValidate] _legaleRappresentante_presenza_residenza:" + input._legaleRappresentante_presenza_residenza);
		
		LegaleRappresentanteVO _legaleRappresentante = input.legaleRappresentante;
		
		try {
		
		if (_legaleRappresentante != null ) {

			if (_legaleRappresentante.toMap().keySet().size() == 1) {
				// esiste una sola chiave
				if (_legaleRappresentante.getLrFromAAEP() != null && !_legaleRappresentante.getLrFromAAEP().isEmpty()) {

					addMessage(newMessages, "_legaleRappresentante_identificativoLRsuAAEP",
							ERRMSG_NESSUN_CAMPO_COMPILATO_LR);
					logger.info("[LegaleRappresentante::modelValidate] identificativoLRsuAAEP: nessun campo compilato");
					return newMessages;
				}
			}

			logger.info("[LegaleRappresentante::modelValidate] _legaleRappresentante:" + _legaleRappresentante.getPresenzaSoggettoDelegato());
			if(_legaleRappresentante.getPresenzaSoggettoDelegato() == null && _legaleRappresentante.getCodiceFiscale() == null){
				addMessage(newMessages, "_legaleRappresentante", "La selezione Legali rappresentanti risulta obbligatoria");
				logger.info("[LegaleRappresentante::modelValidate]  La selezione Legali rappresentanti risulta obbligatoria");
			}
			/** CR-3: mod_02 Verifica coerenza dati anagrafici con correttezza codice fiscale */
			String comuneNascitaDescrizione = "";
			String comuneNascita = "";
			String codiceComune  = "";

			// Codice Fiscale
			String codiceFiscale = _legaleRappresentante.getCodiceFiscale();
			logger.info("[LegaleRappresentante::modelValidate]  codiceFiscale rilasciato da utente: " + codiceFiscale);
			
			/** Verifica formalita codice fiscale */
			ControlCodFisc ctrlCF = new ControlCodFisc(codiceFiscale, logger);
			logger.info("[LegaleRappresentante::modelValidate]  codiceFiscale: " + codiceFiscale);
			
			if (StringUtils.isBlank(codiceFiscale)) {
				addMessage(newMessages, "_legaleRappresentante_codiceFiscale", ERRMSG_CAMPO_OBBLIGATORIO_LR);
				logger.info("[LegaleRappresentante::modelValidate]  codiceFiscale non valorizzato");
			} else {
				if (codiceFiscale.length() == 16 || codiceFiscale.length() == 11) {
					if (codiceFiscale.length() == 16) {
						if (!StringUtils.isAlphanumeric(codiceFiscale)) {
							addMessage(newMessages, "_legaleRappresentante_codiceFiscale", ERRMSG_CF_NOTALPHANUM_LR);
							logger.info("[LegaleRappresentante::modelValidate]  codiceFiscale non valorizzato");
						} else if (!ctrlCF.controllaCheckDigit()) {
							addMessage(newMessages, "_legaleRappresentante_codiceFiscale", ERRMSG_LR_NOTCORRECT_CF);
							logger.info(
									"[LegaleRappresentante::modelValidate]  codiceFiscale formalmente non corretto");
						}
					} else {
						// controllo cod fiscale (partita iva)
						if (!ControlPartitaIVA.controllaPartitaIVA(codiceFiscale)) {
							addMessage(newMessages, "_legaleRappresentante_codiceFiscale", ERRMSG_LR_NOTCORRECT_CF);
							logger.info(
									"[LegaleRappresentante::modelValidate]  codiceFiscale legale rappresentante (partita iva) formalmente non corretto");
						}
					}
					logger.info("[LegaleRappresentante::modelValidate] codiceFiscale:" + codiceFiscale);
				} else {
					addMessage(newMessages, "_legaleRappresentante_codiceFiscale", ERRMSG_LR_WRONGLENGTH_CF);
					logger.info("[LegaleRappresentante::modelValidate]  lunghezza del codiceFiscale non valida");
				}
			}

			// Cognome
			String cognome = (String) _legaleRappresentante.getCognome();
			if (StringUtils.isBlank(cognome)) {
				addMessage(newMessages, "_legaleRappresentante_cognome", ERRMSG_CAMPO_OBBLIGATORIO_LR);
				logger.info("[LegaleRappresentante::modelValidate]  cognome non valorizzato");
			} else {
				if (!Utils.isAlphaSpaceApostropheCommaDot(cognome)) {
					addMessage(newMessages, "_legaleRappresentante_cognome", ERRMSG_LR_NOTALPHANUM_CGN);
					logger.info("[LegaleRappresentante::modelValidate]  cognome:" + cognome
							+ " contiene caratteri non ammessi");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] cognome:" + cognome);
				}
			}

			/** CR-3:  mod_03 Verifica coerenza dati anagrafici con correttezza codice fiscale */
			String genere = "";
			// Genere
			if ("true".equals(input._legaleRappresentante_genere)) {
				genere = (String) _legaleRappresentante.getGenere();
				if (StringUtils.isBlank(genere)) {
					addMessage(newMessages, "_legaleRappresentante_genere", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  genere non valorizzato");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] genere:" + genere);
				}
			}

			// Nome
			String nome = (String) _legaleRappresentante.getNome();
			if (StringUtils.isBlank(nome)) {
				addMessage(newMessages, "_legaleRappresentante_nome", ERRMSG_CAMPO_OBBLIGATORIO_LR);
				logger.info("[LegaleRappresentante::modelValidate]  nome non valorizzato");
			} else {
				if (!Utils.isAlphaSpaceApostropheCommaDot(nome)) {
					addMessage(newMessages, "_legaleRappresentante_nome", ERRMSG_LR_NOTALPHANUM_NM);
					logger.info(
							"[LegaleRappresentante::modelValidate]  nome:" + nome + " contiene caratteri non ammessi");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] nome:" + nome);
				}
			}


			// radio Luogo di nascita
			String luogoNascita = _legaleRappresentante.getLuogoNascita();
			logger.info("[LegaleRappresentante::modelValidate]  luogoNascita: " + luogoNascita);
			
			if (StringUtils.isBlank(luogoNascita)) {
				addMessage(newMessages, "_legaleRappresentante_noLuogoNascita", ERRMSG_LR_NOTSELSTATO_LN);
				logger.info("[LegaleRappresentante::modelValidate]  luogoNascita non valorizzato");
			} else {
				logger.info("[LegaleRappresentante::modelValidate] luogoNascita:" + luogoNascita);
				if (luogoNascita.equals("Italia")) {
					// Provincia
					String provinciaNascita = _legaleRappresentante.getProvinciaNascita();
					if (StringUtils.isBlank(provinciaNascita)) {
						addMessage(newMessages, "_legaleRappresentante_luogoNascita", ERRMSG_LR_PROVINC);
						logger.info("[LegaleRappresentante::modelValidate]  provinciaNascita non valorizzato");
					} else {
						logger.info("[LegaleRappresentante::modelValidate] provinciaNascita:" + provinciaNascita);
						
						/** CR-3:  mod_05 Verifica coerenza dati anagrafici con correttezza codice fiscale */
						codiceComune = provinciaNascita;
						logger.info("[LegaleRappresentante::modelValidate] codiceComune:" + codiceComune);
					}


					// Comune
					comuneNascita = _legaleRappresentante.getComuneNascita();
					if (StringUtils.isBlank(comuneNascita)) {
						addMessage(newMessages, "_legaleRappresentante_luogoNascita", ERRMSG_LR_COMUNE);
						logger.info("[LegaleRappresentante::modelValidate]  comuneNascita non valorizzato");
					} else {
						logger.info("[LegaleRappresentante::modelValidate] comuneNascita:" + comuneNascita);
					}
					
					/** CR-3: mod_06 Verifica coerenza dati anagrafici con correttezza codice fiscale - inizio */
					// Descrizione comune di nascita 
					comuneNascitaDescrizione = _legaleRappresentante.getComuneNascitaDescrizione();
					if (StringUtils.isBlank(comuneNascitaDescrizione)) {
						addMessage(newMessages, "_legaleRappresentante_luogoNascita", ERRMSG_LR_COMUNE);
						logger.info("[LegaleRappresentante::modelValidate]  comuneNascitaDescrizione non valorizzato");
					} else {
						logger.info("[LegaleRappresentante::modelValidate] comuneNascitaDescrizione:" + comuneNascitaDescrizione);
					}
					/** CR-3: mod_06 Verifica coerenza dati anagrafici con correttezza codice fiscale - fine */
				}
				
				String statoEsteroNascita = _legaleRappresentante.getStatoEsteroNascita();
				if (luogoNascita.equals("Estero")) {
					// Stato Estero
					if (StringUtils.isBlank(statoEsteroNascita)) {
						addMessage(newMessages, "_legaleRappresentante_luogoNascita", ERRMSG_LR_STESTERO);
						logger.info("[LegaleRappresentante::modelValidate]  statoEsteroNascita non valorizzato");
					} else {
						logger.info("[LegaleRappresentante::modelValidate] statoEsteroNascita:" + statoEsteroNascita);
						codiceComune = statoEsteroNascita;
						comuneNascitaDescrizione = _legaleRappresentante.getStatoEsteroNascitaDescrizione();
						logger.info("[LegaleRappresentante::modelValidate]  comuneNascitaDescrizione: " + comuneNascitaDescrizione);
					}
				}
			}

			// Data di nascita
			String dataNascita = _legaleRappresentante.getDataNascita();
			
			// Data nascita 
			if (StringUtils.isBlank(dataNascita)) {
				addMessage(newMessages, "_legaleRappresentante_dataNascita", ERRMSG_CAMPO_OBBLIGATORIO_LR);
				logger.info("[LegaleRappresentante::modelValidate]  dataNascita non valorizzato");
				
			} else {
				logger.info("[LegaleRappresentante::modelValidate] dataNascita:" + dataNascita);
				
				if (!validator.isValid(dataNascita, "dd/MM/yyyy") || !(dataNascita.matches("\\d{2}/\\d{2}/\\d{4}"))) {
					logger.info("[LegaleRappresentante::modelValidate]  dataNascita formato data non valido:" + dataNascita);
					addMessage(newMessages, "_legaleRappresentante_dataNascita", ERRMSG_LR_DATAN_FN);
					
				} 
				else 
				{
					Date dataNascitaParse = sdf.parse(dataNascita, new ParsePosition(0));
					
					if (dataNascitaParse.after(today)) {
						logger.info("[LegaleRappresentante::modelValidate]  dataNascita posteriore alla data odierna:" + dataNascita);
						addMessage(newMessages, "_legaleRappresentante_dataNascita", ERRMSG_LR_DATAN_SUCC);
					}
				}
			}

			// -----------------------
			// tipo doc riconoscimento
			LegaleRappresentanteDocumentoVO documento = _legaleRappresentante.getDocumento();
			if (documento != null) 
			{
				String idTipoDocRiconoscimento = documento.getIdTipoDocRiconoscimento();
				
				if (StringUtils.isBlank(idTipoDocRiconoscimento)) {
					addMessage(newMessages, "_legaleRappresentante_idTipoDocRiconoscimento", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  idTipoDocRiconoscimento non valorizzato");
				} 
				else {
					logger.info("[LegaleRappresentante::modelValidate] idTipoDocRiconoscimento:" + idTipoDocRiconoscimento);
				}

				// -------------------------
				// numero doc riconoscimento
				String numDocRiconoscimento = documento.getNumDocumentoRiconoscimento();
				
				if (StringUtils.isBlank(numDocRiconoscimento)) {
					addMessage(newMessages, "_legaleRappresentante_numDocumentoRiconoscimento", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  numDocRiconoscimento non valorizzato");
				} 
				else {
					logger.info("[LegaleRappresentante::modelValidate] numDocRiconoscimento:" + numDocRiconoscimento);
				}

				// --------------------------------------------
				// ente che ha rilasciato il doc riconoscimento
				String rilasciatoDa = documento.getDocRilasciatoDa();
				
				if (StringUtils.isBlank(rilasciatoDa)) {
					addMessage(newMessages, "_legaleRappresentante_docRilasciatoDa", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  doc riconoscimento rilasciatoDa non valorizzato");
				} 
				else {
					logger.info("[LegaleRappresentante::modelValidate] doc riconoscimento rilasciatoDa:" + rilasciatoDa);
				}

				// Data rilascio doc riconoscimento
				String dataRilascioDoc = documento.getDataRilascioDoc();
				if (StringUtils.isBlank(dataRilascioDoc)) {
					addMessage(newMessages, "_legaleRappresentante_dataRilascioDoc", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  data rilascio doc non valorizzato");
				} 
				else 
				{
					if (!validator.isValid(dataRilascioDoc, "dd/MM/yyyy") || !(dataRilascioDoc.matches("\\d{2}/\\d{2}/\\d{4}"))) {
						addMessage(newMessages, "_legaleRappresentante_dataRilascioDoc", ERRMSG_LR_DATARILDOC);
						logger.info("[LegaleRappresentante::modelValidate]   data di rilascio documento formalmente non corretta");
					} 
					else 
					{
						logger.info("[LegaleRappresentante::modelValidate]  data di rilascio documento:" + dataRilascioDoc);
						
						Date dataRilascioDocParse = sdf.parse(dataRilascioDoc, new ParsePosition(0));
						
						if (dataRilascioDocParse.after(today)) {
							logger.info("[LegaleRappresentante::modelValidate]  dataRilascioDoc posteriore alla data odierna:" + dataNascita);
							addMessage(newMessages, "_legaleRappresentante_dataRilascioDoc", ERRMSG_LR_DATARILDOC_SUCC);
						}
					}
				}
			}
			
			if ("true".equals(input._legaleRappresentante_presenza_residenza)) {

				// ---------------------
				// radio Luogo residenza
				String luogoResidenza = _legaleRappresentante.getLuogoResidenza();
				if (StringUtils.isBlank(luogoResidenza)) {
					addMessage(newMessages, "_legaleRappresentante_noLuogoResidenza", ERRMSG_LR_NOTSELSTATO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  luogoResidenza non valorizzato ");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] luogoResidenza:" + luogoResidenza);
					if (luogoResidenza.equals("Italia")) {
						// -------------
						// Provincia
						String provinciaResidenza = _legaleRappresentante.getProvinciaResidenza();
						if (StringUtils.isBlank(provinciaResidenza)) {
							addMessage(newMessages, "_legaleRappresentante_luogoResidenza", ERRMSG_LR_PROVINC);
							logger.info("[LegaleRappresentante::modelValidate]  provinciaResidenza non valorizzato");
						} else {
							logger.info(
									"[LegaleRappresentante::modelValidate] provinciaResidenza:" + provinciaResidenza);
						}

						// -------------
						// Comune
						String comuneResidenza = _legaleRappresentante.getComuneResidenza();
						if (StringUtils.isBlank(comuneResidenza)) {
							addMessage(newMessages, "_legaleRappresentante_luogoResidenza", ERRMSG_LR_COMUNE);
							logger.info("[LegaleRappresentante::modelValidate]  comuneResidenza non valorizzato");
						} else {
							logger.info("[LegaleRappresentante::modelValidate] comuneResidenza:" + comuneResidenza);
						}
					}

					if (luogoResidenza.equals("Estero")) {
						// -------------
						// Stato Estero
						String statoEsteroResidenza = _legaleRappresentante.getStatoEsteroResidenza();
						if (StringUtils.isBlank(statoEsteroResidenza)) {
							addMessage(newMessages, "_legaleRappresentante_luogoResidenza", ERRMSG_LR_STESTERO);
							logger.info("[LegaleRappresentante::modelValidate]  statoEsteroResidenza non valorizzato");
						} else {
							logger.info("[LegaleRappresentante::modelValidate] statoEsteroResidenza:"
									+ statoEsteroResidenza);
						}

						// Citta estera
						String cittaEstera = _legaleRappresentante.getCittaEsteraResidenza();
						if (StringUtils.isBlank(cittaEstera)) {
							addMessage(newMessages, "_legaleRappresentante_cittaEsteraResidenza",
									ERRMSG_CAMPO_OBBLIGATORIO_LR);
							logger.info("[LegaleRappresentante::modelValidate]  cittaEstera non valorizzato");
						} else {
							logger.info("[LegaleRappresentante::modelValidate] cittaEstera:" + cittaEstera);
						}
					}
				}
				
				// -------------
				// Indirizzo
				String indirizzo = _legaleRappresentante.getIndirizzo();
				if (StringUtils.isBlank(indirizzo)) {
					addMessage(newMessages, "_legaleRappresentante_indirizzo", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  indirizzo non valorizzato");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] indirizzo:" + indirizzo);
				}

				// -------------
				// numero civico
				String numCivico = _legaleRappresentante.getNumCivico();
				if (StringUtils.isBlank(numCivico)) {
					addMessage(newMessages, "_legaleRappresentante_numcivico", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  numCivico non valorizzato");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] numCivico:" + numCivico);
				}

				// -------------
				// cap
				String cap = _legaleRappresentante.getCap();
				if (StringUtils.isBlank(cap)) {
					addMessage(newMessages, "_legaleRappresentante_cap", ERRMSG_CAMPO_OBBLIGATORIO_LR);
					logger.info("[LegaleRappresentante::modelValidate]  cap non valorizzato");
				} else {
					logger.info("[LegaleRappresentante::modelValidate] cap:" + cap);
				}

			}

		} else {
			logger.info("[LegaleRappresentante::modelValidate]  _legaleRappresentante non presente o vuoto");
		}
		} catch (Exception e) {
			logger.error("[LegaleRappresentante::modelValidate] Generic ERROR", e);
		}

		logger.info("[LegaleRappresentante::modelValidate] _legaleRappresentante END");
		return newMessages;
	}


	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
		// nothing to validate
		return null;
	}

	List<LRVo> getListaLRAAEP(ImpresaVO enteImpresa, Logger logger) {

		List<LRVo> listaLR = new ArrayList<LRVo>();

		if (enteImpresa != null) {

			logger.info(
					"[LegaleRappresentante::inject] enteImpresa.getListaPersone()=" + enteImpresa.getListaPersone());
			for (PersonaVO persona : enteImpresa.getListaPersone()) {

				logger.info("[LegaleRappresentante::inject] persona.cf=" + persona.getCognome() + " "
						+ persona.getNome() + " - [" + persona.getCodiceFiscale() + "]");
				for (CaricaVO carica : persona.getListaCariche()) {
					logger.info("[LegaleRappresentante::inject]  carica LR = " + carica.getFlagRappresentanteLegale());

					// escludo i legali rappresentanti senza codice fiscale (non funziona
					// l'ordinamento della combo
					// se il codice fiscale non é presente)
					if (!StringUtils.isBlank(persona.getCodiceFiscale())
							&& StringUtils.equals("S", carica.getFlagRappresentanteLegale())) {
						LRVo lrVo = new LRVo();
						lrVo.codiceFiscale = persona.getCodiceFiscale();
						lrVo.cognome = persona.getCognome();
						lrVo.nome = persona.getNome();
						lrVo.idAzienda = enteImpresa.getIdAzienda();
						lrVo.idFonteDato = persona.getIdFonteDato();
						lrVo.idPersona = persona.getIdPersona();
						listaLR.add(lrVo);
					}
				}
			}
			logger.info("[LegaleRappresentante::inject] listaLR:" + listaLR);
		}

		return listaLR;
	}
	
	List<LRVo> getListaLRUltimaDomanda(FinStatusInfo statInfo, Logger logger) {

		String prf = "[LegaleRappresentante::getListaLRUltimaDomanda] ";
		
		List<LRVo> listaLR = new ArrayList<LRVo>();
		
		// Se non ci sono LR su AAEP, carico quello dell'ultima domanda con id vuoti
		VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		logger.info(prf +" datiUltimoBeneficiario in sessione=" + datiUltimoBeneficiario);
		
//		if(datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale().equals(statInfo.getCodFiscaleBeneficiario())){
			
			if (datiUltimoBeneficiario != null && datiUltimoBeneficiario.getLegaleRappresentante()!=null) {
				
				logger.info(prf +" datiUltimoBeneficiario OperatorePresentatore.CF=" + datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale());
				
				LegaleRappresentanteDto ultimoLR = datiUltimoBeneficiario.getLegaleRappresentante();
				  
				LRVo lrVo = new LRVo();
				lrVo.codiceFiscale = ultimoLR.getCodiceFiscale();
				logger.info("CodiceFiscale: " + lrVo.codiceFiscale);
				lrVo.cognome = ultimoLR.getCognome();
				logger.info("Cognome: " + lrVo.cognome);
				lrVo.nome = ultimoLR.getNome();
				logger.info("Nome: " + lrVo.nome);
				lrVo.idAzienda = "";
				lrVo.idFonteDato = "";
				lrVo.idPersona = "";
				listaLR.add(lrVo);  
			}
//		}else {
//			logger.warn(prf + "datiUltimoBeneficiario presente in sessione non si riferiscono al beneficiario corrente");
//			logger.warn(prf + "datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale()="+datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale());
//			logger.warn(prf + "statInfo.getCodFiscaleBeneficiario()="+statInfo.getCodFiscaleBeneficiario());
//		}
		logger.info(prf +" listaLR:" + listaLR);

		return listaLR;
	}

	private ProvinciaVO getProvinciaMap(List<ProvinciaVO> lista, String sigla) {
		ProvinciaVO mappa = new ProvinciaVO();
		mappa.setDescrizione("");
		mappa.setCodice("");
		mappa.setSigla("");

		for (int i = 0; i < lista.size(); i++) {
			// non faccio una query apposita ma ciclo sull'elenco delle province
			ProvinciaVO provincMap = (ProvinciaVO) lista.get(i);
			if (provincMap != null && StringUtils.equals(provincMap.getSigla(), sigla)) {
				mappa.setDescrizione(provincMap.getDescrizione());
				mappa.setCodice(provincMap.getCodice());
				mappa.setSigla(provincMap.getSigla());
				break;
			}
		}
		return mappa;
	}

	private StatoEsteroVO getStatoEstero(List<StatoEsteroVO> lista, String sigla) {
		StatoEsteroVO mappa = new StatoEsteroVO();
		mappa.setCodice("");
		if (!StringUtils.isBlank(sigla)) {
			// ricavo il codice dello stato dal DB
			for (int i = 0; i < lista.size(); i++) {
				// non faccio una query apposita ma ciclo sull'elenco delle province
				StatoEsteroVO statoMap = lista.get(i);
				if (statoMap != null && StringUtils.equals(statoMap.getSigla(), sigla)) {
					mappa.setCodice(statoMap.getCodice());
					mappa.setDescrizione(statoMap.getDescrizione());
					mappa.setSigla(statoMap.getSigla());
					break;
				}
			}
		}
		return mappa;
	}

	  private LegaleRappresentanteVO inserisciInformazioniUltimaDomandaLegaleRappresentante(LegaleRappresentanteVO lrMap) {
			
		  VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		  if (datiUltimoBeneficiario != null && datiUltimoBeneficiario.getLegaleRappresentante()!=null) {
			  LegaleRappresentanteDto ultimoLR = datiUltimoBeneficiario.getLegaleRappresentante();
			  
			  lrMap.setCodiceFiscale(ultimoLR.getCodiceFiscale());
			  lrMap.setCognome(ultimoLR.getCognome());
			  lrMap.setNome(ultimoLR.getNome());
			  lrMap.setDataNascita(ultimoLR.getDataNascita());
			  lrMap.setGenere(ultimoLR.getGenere());
//			  lrMap.setLrFromAAEP(ultimoLR.getLrFromAAEP()); // ????
			  lrMap.setPresenzaSoggettoDelegato(ultimoLR.getPresenzaSoggettoDelegato()); // ???
			  
			  lrMap =  aggiornaInformazioniNascita(lrMap, ultimoLR);			   
			  lrMap =  aggiornaInformazioniResidenza(lrMap, ultimoLR);
			   
			  lrMap = inserisciDocumentoLegaleRappresentante(lrMap, ultimoLR);

		  }

		  return lrMap;
	  }
	  
	  private LegaleRappresentanteVO aggiornaConInformazioniUltimaDomandaLegaleRappresentante(LegaleRappresentanteVO lrMap) {
			
		  VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		  if (datiUltimoBeneficiario != null && datiUltimoBeneficiario.getLegaleRappresentante()!=null) {
			  LegaleRappresentanteDto ultimoLR = datiUltimoBeneficiario.getLegaleRappresentante();
		  
			  if (it.csi.melograno.aggregatore.util.Utils.isEmpty(ultimoLR.getCodiceFiscale())) {
				  return lrMap;
			  } else {
				  if (lrMap.getCodiceFiscale().equals(ultimoLR.getCodiceFiscale())) {
					  
					  lrMap.setCognome(StringUtils.isEmpty(lrMap.getCognome()) ? ultimoLR.getCognome() : lrMap.getCognome());
					  lrMap.setNome(StringUtils.isEmpty(lrMap.getNome()) ? ultimoLR.getNome() : lrMap.getNome());
					  lrMap.setDataNascita(StringUtils.isEmpty(lrMap.getDataNascita()) ? ultimoLR.getDataNascita() : lrMap.getDataNascita());
					  lrMap.setGenere(StringUtils.isEmpty(lrMap.getGenere()) ? ultimoLR.getGenere() : lrMap.getGenere());
//					  lrMap.setLrFromAAEP(StringUtils.isEmpty(lrMap.getLrFromAAEP()) ? ultimoLR.getLrFromAAEP() : lrMap.getLrFromAAEP());
					  lrMap.setPresenzaSoggettoDelegato(StringUtils.isEmpty(lrMap.getPresenzaSoggettoDelegato()) ? ultimoLR.getPresenzaSoggettoDelegato() : lrMap.getPresenzaSoggettoDelegato());

					  
						if (StringUtils.isEmpty(lrMap.getLuogoNascita())) {
							// manca la sede legale
							lrMap = aggiornaInformazioniNascita(lrMap, ultimoLR);
						} else {
							if (StringUtils.equals(ultimoLR.getLuogoNascita(), lrMap.getLuogoNascita())) {

								if(StringUtils.equals("Italia", lrMap.getLuogoNascita())){
									// Italia
									if (StringUtils.isEmpty(lrMap.getComuneNascita()) || StringUtils.isEmpty(lrMap.getProvinciaNascita())) {
										// manca il comune/provincia
										lrMap = aggiornaInformazioniComuneNascita(lrMap, ultimoLR);	
									} 
								} else {
									// Estero
									lrMap = aggiornaInformazioniNascitaEstero(lrMap, ultimoLR);						
								}
							}
						}
					  
						if (StringUtils.isEmpty(lrMap.getLuogoResidenza())) {
							// manca la sede legale
							lrMap = aggiornaInformazioniResidenza(lrMap, ultimoLR);
						} else {
							if (StringUtils.equals(ultimoLR.getLuogoResidenza(), lrMap.getLuogoResidenza())) {

								if(StringUtils.equals("Italia", lrMap.getLuogoResidenza())){
									// Italia
									if (StringUtils.isEmpty(lrMap.getComuneResidenza()) || StringUtils.isEmpty(lrMap.getProvinciaResidenza())) {
										// manca il comune/provincia
										lrMap = aggiornaInformazioniComuneResidenza(lrMap, ultimoLR);							
									} else {
										if ((StringUtils.isEmpty(lrMap.getIndirizzo()) || StringUtils.isEmpty(lrMap.getCap().trim()) 
												|| StringUtils.isEmpty(lrMap.getNumCivico())) &&
											lrMap.getComuneResidenza().equals(ultimoLR.getComuneResidenza())) {
											// manca l'indirizzo ma è lo stesso comune di aaep
											lrMap = aggiornaInformazioniIndirizzo(lrMap, ultimoLR);
										}							
									}
								} else {
									// Estero
									lrMap = aggiornaInformazioniResidenzaEstero(lrMap, ultimoLR);						
								}
							}
						}

					  lrMap = inserisciDocumentoLegaleRappresentante(lrMap, ultimoLR);
				  }
			  }
		  }

		  return lrMap;
	  }
	  
	  private LegaleRappresentanteVO inserisciDocumentoLegaleRappresentante(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			
	  	LegaleRappresentanteDocumentoVO docMap = new LegaleRappresentanteDocumentoVO();
		docMap.setDataRilascioDoc(ultimoLR.getDocumento().getDataRilascioDoc());
		docMap.setDescrizioneTipoDocRiconoscimento(ultimoLR.getDocumento().getDescrizioneTipoDocRiconoscimento());
		docMap.setDocRilasciatoDa(ultimoLR.getDocumento().getDocRilasciatoDa());
		docMap.setIdTipoDocRiconoscimento(ultimoLR.getDocumento().getIdTipoDocRiconoscimento());
		docMap.setNumDocumentoRiconoscimento(ultimoLR.getDocumento().getNumDocumentoRiconoscimento());
		lrMap.setDocumento(docMap);

		return lrMap;
	  }
	  
		private LegaleRappresentanteVO aggiornaInformazioniIndirizzo(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita
			lrMap.setIndirizzo(ultimoLR.getIndirizzo());
			lrMap.setNumCivico(ultimoLR.getNumCivico());
			lrMap.setCap(ultimoLR.getCap().trim());
									
			return lrMap;
		}
		private LegaleRappresentanteVO aggiornaInformazioniNascitaEstero(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita

			lrMap.setStatoEsteroNascita(ultimoLR.getStatoEsteroNascita());
			lrMap.setStatoEsteroNascitaDescrizione(ultimoLR.getStatoEsteroNascitaDescrizione());
			lrMap.setComuneNascita("");
			lrMap.setComuneNascitaDescrizione("");
			lrMap.setProvinciaNascita("");
			lrMap.setProvinciaNascitaDescrizione("");
			lrMap.setSiglaProvinciaNascita("");
			
			return lrMap;
		}
		private LegaleRappresentanteVO aggiornaInformazioniResidenzaEstero(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita

			lrMap.setStatoEsteroResidenza(ultimoLR.getStatoEsteroResidenza());
			lrMap.setStatoEsteroResidenzaDescrizione(ultimoLR.getStatoEsteroResidenzaDescrizione());
			lrMap.setCittaEsteraResidenza(ultimoLR.getCittaEsteraResidenza());
			lrMap.setComuneResidenza("");
			lrMap.setComuneResidenzaDescrizione("");
			lrMap.setProvinciaResidenza("");
			lrMap.setProvinciaResidenzaDescrizione("");
			lrMap.setSiglaProvinciaResidenza("");
			
			lrMap = aggiornaInformazioniIndirizzo(lrMap, ultimoLR);
			
			return lrMap;
		}
		private LegaleRappresentanteVO aggiornaInformazioniComuneNascita(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita

			lrMap.setComuneNascita(ultimoLR.getComuneNascita());
			lrMap.setComuneNascitaDescrizione(ultimoLR.getComuneNascitaDescrizione());
			lrMap.setProvinciaNascita(ultimoLR.getProvinciaNascita());
			lrMap.setProvinciaNascitaDescrizione(ultimoLR.getProvinciaNascitaDescrizione());
			lrMap.setSiglaProvinciaNascita(ultimoLR.getSiglaProvinciaNascita());
			lrMap.setStatoEsteroNascita("");
			lrMap.setStatoEsteroNascitaDescrizione("");
			 							
			return lrMap;
		}
		private LegaleRappresentanteVO aggiornaInformazioniComuneResidenza(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita

			lrMap.setComuneResidenza(ultimoLR.getComuneResidenza());
			lrMap.setComuneResidenzaDescrizione(ultimoLR.getComuneResidenzaDescrizione());
			lrMap.setProvinciaResidenza(ultimoLR.getProvinciaResidenza());
			lrMap.setProvinciaResidenzaDescrizione(ultimoLR.getProvinciaResidenzaDescrizione());
			lrMap.setSiglaProvinciaResidenza(ultimoLR.getSiglaProvinciaResidenza());
			lrMap.setStatoEsteroResidenza("");
			lrMap.setStatoEsteroResidenzaDescrizione("");
			lrMap.setCittaEsteraResidenza("");

			lrMap = aggiornaInformazioniIndirizzo(lrMap, ultimoLR);
			
			return lrMap;
		}
		
		private LegaleRappresentanteVO aggiornaInformazioniResidenza(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita

			lrMap.setLuogoResidenza(ultimoLR.getLuogoResidenza());		
			if(StringUtils.equals("Italia", ultimoLR.getLuogoResidenza())){
				lrMap = aggiornaInformazioniComuneResidenza(lrMap, ultimoLR);
			} else {
				lrMap = aggiornaInformazioniResidenzaEstero(lrMap, ultimoLR);
			}	
			return lrMap;
		}

		private LegaleRappresentanteVO aggiornaInformazioniNascita(LegaleRappresentanteVO lrMap, LegaleRappresentanteDto ultimoLR) {
			//  --- utilizzo i dati dell'ultima domanda inserita

			lrMap.setLuogoNascita(ultimoLR.getLuogoNascita());		
			if(StringUtils.equals("Italia", ultimoLR.getLuogoNascita())){
				lrMap = aggiornaInformazioniComuneNascita(lrMap, ultimoLR);
			} else {
				lrMap = aggiornaInformazioniNascitaEstero(lrMap, ultimoLR);
			}	
			return lrMap;
		}
}
