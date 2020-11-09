/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura;

import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.findom.blocchetti.blocchetti.entiProgetto.EnteProgettoItemVO;
import it.csi.findom.blocchetti.blocchetti.estremiBancari.EstremiBancariVO;
import it.csi.findom.blocchetti.blocchetti.entiProgetto.EntiProgettoVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiItemVO;
import it.csi.findom.blocchetti.common.vo.allegati.DocumentoVO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;
import it.csi.findom.blocchetti.commonality.ControlPartitaIVA;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.progetti.FinDirettiva;
import it.csi.findom.blocchetti.common.dao.EnteDAO;
import it.csi.findom.blocchetti.util.StringUtils;
import freemarker.core.Environment.Namespace;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.List;


public class Direttiva extends FinDirettiva {
  
  private DirettivaInput input = new DirettivaInput();
  
  public DirettivaInput getInput() {
    return input;
  }

  public Direttiva(TreeMap<String, Object> context, Namespace namespace, TreeMap<String, Object> model) {
    super(context, namespace, model);
  }

  public List<CommonalityMessage> globalValidate(CommonInfo info1) // richiamata direttamente dall'aggregatore
  {
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
			
		logger.info("[Direttiva::globalValidate] domanda MPMI BEGIN");
		String logprefix = "[Direttiva::globalValidate] ";
	
		////////////////////////////////////////////////////////////////////////////////////////////
		// messaggi visualizzati
		String ERR_S1 = "Anagrafica Ente/Impresa: E' necessario compilare la cartella 'Anagrafica Ente/Impresa'";
		String ERR_S1_P1 = "Anagrafica Ente/Impresa / Beneficiario: E' necessario verificare dati compilati nella sezione 'Beneficiario' e risalvare la pagina";
		String ERR_S1_P1_INTESTATARIO_DENOMINAZIONE = "Anagrafica Ente/Impresa / Beneficiario: Dati incongruenti. E' necessario tornare nella pagina di Estremi bancari e salvare.";
		String ERR_S1_P3 = "Anagrafica Ente/Impresa / Legale Rappresentante: E' necessario compilare la sezione 'Legale Rappresentante'";
		String ERR_S1_P4 = "Anagrafica Ente/Impresa / Sede Legale: E' necessario compilare la sezione 'Sede Legale'";
		String ERR_S1_P5 = "Anagrafica Ente/Impresa / Estremi Bancari: E' necessario compilare la sezione 'Estremi Bancari'";
		String ERR_S3 = "Allegati e Dichiarazioni : E' necessario compilare la cartella 'Dichiarazioni'";
		String ERR_S3_P2 = "Allegati e Dichiarazioni / Dichiarazioni: E' necessario compilare la sezione 'Dichiarazioni'";
		String ERR_S3_P3 = "Allegati e Dichiarazioni / Upload: E' necessario compilare la sezione 'Upload'";
		String ERR_S4 = "Progetto : E' necessario compilare la cartella 'Progetto'";
		String ERR_S4_P1 = "Progetto / Informazioni sul progetto: E' necessario compilare la sezione 'Informazioni sul progetto'";
		String ERR_S4_P4 = "Progetto / Tipologia di aiuto: E' necessario compilare la sezione 'Tipologia di aiuto'";
		
		// String ERR_S4_P1_DATI_ASSENTI = "Progetto / Informazioni sul progetto: E' necessario tornare nella pagina Informazioni sul progetto e salvare.";
		////////////////////////////////////////////////////////////////////////////////////////////
		// inizializzazioni generali
		
		// contatori errori e liste messaggi

		// messaggi di errore bloccanti
		int ccErr = 0;
		List<String> errorList = new ArrayList<String>();

		// messaggi di errore NON bloccanti
		int ccWarn = 0;
		List<String> warnList = new ArrayList<String>();

		// dichiarazione e inizializzazione variabili delle regole
		
		// inizializzazioni generali
		
		// regole di validazione generali
		
		// Il sistema verifica che siano state compilate tutte le pagine della sezione Anagrafica  - BLOCCANTE
		logger.info( logprefix + "regole Sezione Anagrafica :: Sezioni compilate-complete BEGIN");
		
		// REGOLA V1 : Il sistema verifica che sia stata compilata la maschera [Beneficiario] - BLOCCANTE 
		// tab Anagrafica/Beneficiario
		boolean isBeneficiarioCompiled 	= false;
		boolean isDenominazioneUguale	= true;
		
		if (input._operatorePresentatore != null) {
			
			OperatorePresentatoreVo _operatorePresentatore = input._operatorePresentatore;
			EstremiBancariVO _estremiBancari = input._estremiBancari;
			
			String denominazioneBeneficiario = "";
			String intestatarioCC = "";

			// eseguo controlli dati salvati su xml
			boolean erroriMVR = false;
			
			if (_operatorePresentatore != null ) {
				logger.info(logprefix + "_operatorePresentatore:" + _operatorePresentatore);

				// Denominazione o ragione sociale
				denominazioneBeneficiario = (String) _operatorePresentatore.getDenominazione();
				logger.info(logprefix + "denominazioneBeneficiario: " + denominazioneBeneficiario);
				if (StringUtils.isBlank(denominazioneBeneficiario)) {
					erroriMVR = true;
				}

				// forma giuridica
				String idFormaGiuridica = (String) _operatorePresentatore.getIdFormaGiuridica();
				logger.info(logprefix + " idFormaGiuridica: " + idFormaGiuridica);
				if (StringUtils.isBlank(idFormaGiuridica)) {
					erroriMVR = true;
				} 
				
				// descrizione forma giuridica
				String descrizioneFormaGiuridica = (String) _operatorePresentatore.getDescrizioneFormaGiuridica();
				logger.info(logprefix + " descrizioneFormaGiuridica: " + descrizioneFormaGiuridica);
				if (StringUtils.isBlank(descrizioneFormaGiuridica)) {
					erroriMVR = true;
				}

				// partita iva
				String partitaIva = (String) _operatorePresentatore.getPartitaIva();
				logger.info(logprefix + " partitaIva: " + partitaIva);
				if (StringUtils.isBlank(partitaIva)) {
					erroriMVR = true;
				} else {			
					if (!ControlPartitaIVA.controllaPartitaIVA(partitaIva)){
						erroriMVR = true;
					}else{				
						logger.info(logprefix + "partita IVA:" + partitaIva);
					}
				}
				
			} else {
				logger.info(logprefix + "_operatorePresentatore non presente o vuoto");
				erroriMVR = true;
			}
			
			
			if (_estremiBancari != null) {
				
				intestatarioCC = (String) _estremiBancari.getIntestatarioCC();
				logger.info("[EstremiBancari::globalValidate] intestatarioCC risulta:" + intestatarioCC);
				
				// eseguo verifica intestatario in estremi bancari 
				if(!intestatarioCC.equalsIgnoreCase(denominazioneBeneficiario)){
					isDenominazioneUguale=false;
					logger.info(logprefix + " Denominazione in (Beneficiario) diversa da Intestatario in (Estremi bancari) "+isDenominazioneUguale);
				}
				
			}else{
				logger.info("[EstremiBancari::modelValidate] estremi bancari non ancora compilato:");
			}
			
			if(!erroriMVR){
				isBeneficiarioCompiled = true;
				logger.info( logprefix + "tab 'Anagrafica/Beneficiario' compilato");
			}else{
				isBeneficiarioCompiled = false;
				
			}
		}
		
		boolean isLegaleRappresentanteCompiled = false;
		boolean isSoggettoDelegatoRequired = false; //usato anche nel controllo degli allegati
		
		if (input._legaleRappresentante != null) {
		    LegaleRappresentanteVO legaleRappr = input._legaleRappresentante;
		    String presenzaSoggettoDelegato = (String) legaleRappr.getPresenzaSoggettoDelegato();
		    if(presenzaSoggettoDelegato!=null && presenzaSoggettoDelegato.equals("si")){
		       isSoggettoDelegatoRequired = true;
		    }
			isLegaleRappresentanteCompiled = true;	
			logger.info( logprefix + "tab 'Anagrafica/Legale Rappresentante' compilato");
		}
		
		// REGOLA V3 : Il sistema verifica che sia stata compilata la maschera [Sede Legale] - BLOCCANTE 
		// tab Anagrafica/Sede Legale
		boolean isSedeLegaleCompiled = false;
		if (input._sedeLegale != null) {
			isSedeLegaleCompiled = true;	
			logger.info( logprefix + "tab 'Anagrafica/Sede Legale' compilato");
		}
		
		// REGOLA V4 : Il sistema verifica che sia stata compilata la maschera [Estremi Bancari] - BLOCCANTE 
		// tab Anagrafica/Estremi Bancari
		boolean isEstremiBancariCompiled = false;
		if (input._estremiBancari != null) {
			isEstremiBancariCompiled = true;	
			logger.info( logprefix + "tab 'Anagrafica/Estremi Bancari' compilato");
		} 
		
		boolean isAgevolazioneRichiestaCompiled = false; 
		if (input._tipologiaAiuto!= null) {
			isAgevolazioneRichiestaCompiled = true;
			logger.info( logprefix + "tab 'Progetto/Agevolazione richiesta' compilato");
		}
		
//		if (!isBeneficiarioCompiled && !isSedeLegaleCompiled && !isLegaleRappresentanteCompiled && !isEstremiBancariCompiled && !isRiferimentiCompiled && !isAgevolazioneRichiestaCompiled) {
		if (!isBeneficiarioCompiled && !isSedeLegaleCompiled && !isLegaleRappresentanteCompiled && !isEstremiBancariCompiled && !isAgevolazioneRichiestaCompiled) {
			errorList.add(ERR_S1);
			ccErr++;	
			logger.info( logprefix + "cartella 'Anagrafica' non compilata");
		} else {
			if(!isBeneficiarioCompiled){
				errorList.add(ERR_S1_P1);
				ccErr++;		
				logger.info( logprefix + "tab 'Anagrafica/Beneficiario' non compilato");
			}
			if(!isDenominazioneUguale)
			{
				errorList.add(ERR_S1_P1_INTESTATARIO_DENOMINAZIONE);
				ccErr++;		
				logger.info( logprefix + "tab 'Anagrafica/Beneficiario' Dati incongruenti. E' necessario tornare nella pagina di Estremi bancari e salvare.");
			}
			if(!isLegaleRappresentanteCompiled){
				errorList.add(ERR_S1_P3);
				ccErr++;		
				logger.info( logprefix + "tab 'Anagrafica/Legale Rappresentante' non compilato");
			}
			if(!isSedeLegaleCompiled){
				errorList.add(ERR_S1_P4);
				ccErr++;		
				logger.info( logprefix + "tab 'Anagrafica/Sede Legale' non compilato");
			}
			if(!isEstremiBancariCompiled){
				errorList.add(ERR_S1_P5);
				ccErr++;		
				logger.info( logprefix + "tab 'Anagrafica/Estremi Bancari' non compilato");
			}
			if (isBeneficiarioCompiled && isSedeLegaleCompiled && isEstremiBancariCompiled) {		
				logger.info( logprefix + "STATO SEZIONE 'Anagrafica' compilata");
			}
		}
		
		logger.info( logprefix + "regole Sezione Anagrafica :: Sezioni compilate-complete END");
		
		
		// Il sistema verifica che siano state compilate tutte le pagine della sezione "Dati Impresa"  - BLOCCANTE
		logger.info( logprefix + "regole Sezione Dati Impresa :: Sezioni compilate-complete BEGIN");
		
		// REGOLA V6: Il sistema verifica che sia stata compilata la maschera [Profilo dell'impresa] - BLOCCANTE
			
		DomandaNGVO _domanda = input._domanda;
		String stereotipoDomanda = _domanda == null ? "" : (String) _domanda.getStereotipoDomanda();
		logger.info("[DEBUG] " + logprefix + "stereotipo domanda " + stereotipoDomanda);		
		
		if(!StringUtils.isBlank(stereotipoDomanda) && !stereotipoDomanda.equals("OP")){
			logger.info("[DEBUG] " + logprefix + "tab 'Informazioni sull''Ente' ignorato");
			logger.info("[DEBUG] " + logprefix + "tab 'Struttura organizzativa e capacit&#224; finanziaria' ignorato");
		} else {
			if (input._entiProgetto != null) {
				logger.info( logprefix + "tab 'Dati Impresa/Informazioni sull''Ente' compilato");
			} else {
				logger.info("[DEBUG] " + logprefix + "tab 'Informazioni sull''Ente' non compilato");
			}
		
		}
		
		// Il sistema verifica che siano state compilate tutte le pagine della sezione "Progetto" - BLOCCANTE
		logger.info( logprefix + "regole Sezione Progetto :: Sezioni compilate-complete BEGIN");
		
		// REGOLA V9: Il sistema verifica che sia stata compilata correttamente la maschera [Informazioni sul progetto] - BLOCCANTE
		// tab "Informazioni sul progetto"
		boolean isInformazioniProgettoCompiled = false;
		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO  = input._caratteristicheProgetto;
		
		String idDomanda = info.getStatusInfo().getNumProposta() + "";
		logger.info("[Debug:]  idDomanda vale = " + idDomanda);
		
		Integer intIdDomanda = Integer.parseInt(idDomanda);
		logger.info("intIdDomanda: " + intIdDomanda);
		
		if (caratteristicheProgettoNGVO != null) {
			isInformazioniProgettoCompiled = true;	
			logger.info( logprefix + "tab 'Progetto/Informazioni sul progetto' compilato");
		}
		
		boolean trovatoErroreInfoEnte = false;
		boolean trovatoUtente = false;
		if (isBeneficiarioCompiled){
			EntiProgettoVO entiProgetto = input._entiProgetto;
			if (entiProgetto != null) {
				
				logger.info( logprefix + " entiProgetto: " + entiProgetto);
				
				EnteProgettoItemVO[] entiProgettoList = entiProgetto.getEntiProgettoList();
							
				if (entiProgettoList != null && entiProgettoList.length>0) {		
									
					OperatorePresentatoreVo operatorePresentatore = input._operatorePresentatore;
					String codiceFiscaleBeneficiario = (String)operatorePresentatore.getCodiceFiscale();
					String codIstat = "";
					try {
						codIstat = EnteDAO.getCodIstat(codiceFiscaleBeneficiario);
					} catch (CommonalityException ex) { 
						logger.error( logprefix + " exception="+ex.toString());
					} 
					
					int nroPartecipanti = 0;
						
					for (int i=0; i<entiProgettoList.length; i++) {
						
						EnteProgettoItemVO itemEnte = entiProgettoList[i];
						logger.info( logprefix + " itemEnte:" + itemEnte);
		
						if (itemEnte != null) {
								
							// controllo valorizzazione codiceEnte
							String codiceEnte = itemEnte.getCodiceEnte();
							if(StringUtils.isBlank(codiceEnte)) {
								logger.info( logprefix + "_entiProgetto_entiProgettoList[]_codiceEnte nullo");
								trovatoErroreInfoEnte = true;
							} else {
								if (codiceEnte.equalsIgnoreCase(codIstat)){
									trovatoUtente = true;
								}
							}
								
							String partecipazioneProgetto = itemEnte.getPartecipazioneProgetto();						
							if(!StringUtils.isBlank(partecipazioneProgetto) && partecipazioneProgetto.equalsIgnoreCase("checked")) { 
								
								// controllo valorizzazione certificazioneAmbientale
								String certificazioneAmbientale = itemEnte.getCertificazioneAmbientale();
								if(StringUtils.isBlank(certificazioneAmbientale)) {
									logger.info( logprefix + "_entiProgetto_entiProgettoList[]_certificazioneAmbientale nullo");
									trovatoErroreInfoEnte = true;
								} else {
										
									// se certificazioneAmbientale vale SI deve essere valorizzato anche certificazioneAmbientaleText
									if(certificazioneAmbientale.equalsIgnoreCase("SI")) {
									
										String certificazioneAmbientaleText = itemEnte.getCertificazioneAmbientaleText();
										if(StringUtils.isBlank(certificazioneAmbientaleText)) {
											logger.info( logprefix + " _entiProgetto_entiProgettoList[]_certificazioneAmbientaleText nullo");
											trovatoErroreInfoEnte = true;
										} 
									}
								}
								
								// controllo valorizzazione pattoSindaci
								String pattoSindaci = itemEnte.getPattoSindaci();
								if(StringUtils.isBlank(pattoSindaci)) {
									logger.info( logprefix + " _entiProgetto_entiProgettoList[]_pattoSindaci nullo");
									trovatoErroreInfoEnte = true;
								} 
								
								// controllo valorizzazione richiestaAgevolazioni
								String richiestaAgevolazioni = itemEnte.getRichiestaAgevolazioni();
								if(StringUtils.isBlank(richiestaAgevolazioni)) {
									logger.info( logprefix + " _entiProgetto_entiProgettoList[]_richiestaAgevolazioni nullo");
									trovatoErroreInfoEnte = true;
								} else {
									
									// se richiestaAgevolazioni vale SI deve essere valorizzato anche richiestaAgevolazioniText
									if(richiestaAgevolazioni.equalsIgnoreCase("SI")) {
									
										String richiestaAgevolazioniText = itemEnte.getRichiestaAgevolazioniText();
										if(StringUtils.isBlank(richiestaAgevolazioniText)) {
											logger.info( logprefix + "_entiProgetto_entiProgettoList[]_richiestaAgevolazioniText nullo");
											trovatoErroreInfoEnte = true;
										} 
									}
								}
								nroPartecipanti++;
							}
								
						} // chiude if (itemEnte != null && !itemEnte.isEmpty()) 
					} // chiude for (int i=0; i<entiProgettoList.size(); i++)
						
					// per l'unione dei comuni e per i raggruppamenti dobbiamo controllare 
					// che ci siano almeno 2 partecipanti
					String codiceTipologiaBeneficiario = "";
					codiceTipologiaBeneficiario = _domanda.getCodiceTipologiaBeneficiario();
					
					if(StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "UCO") || StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "RTE")){
							
						if (nroPartecipanti<2){
							logger.info( logprefix + " nroPartecipanti minore di 2");							
							trovatoErroreInfoEnte = true;
						} else {
							// per RTE tutti i record devono avere la selezione partecipazioneProgetto
							if (StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "RTE")){
									
								int nroEnti = entiProgettoList.length;
									
								if (nroPartecipanti!=nroEnti){		
									logger.info( logprefix + " nroPartecipanti non coincide con numero enti");							
									trovatoErroreInfoEnte = true;
								} else {
									// controlliamo che il beneficiario sia presente fra i partecipanti al raggruppamento
									if (!trovatoUtente){	
										logger.info( logprefix + " utente non partecipante al progetto");								
										trovatoErroreInfoEnte = true;
									}
								}
							}
						}
					}	 
				} else { // entiProgettoList == null  o vuoto
					logger.info( logprefix + "_entiProgettoList non presente o vuoto");			
					trovatoErroreInfoEnte = true;
				} 
				
					
			} // chiude if (entiProgetto != null && !entiProgetto.isEmpty()) 
		} // chiude if (isInfoEnteCompiled && isBeneficiarioCompiled)
		
		 /***********/
		/**   S4   */
	   /***********/
		if (!isInformazioniProgettoCompiled && !isAgevolazioneRichiestaCompiled) {
				errorList.add(ERR_S4);
				ccErr++;	
				logger.info( logprefix + "cartella 'Progetto' non compilata");
		} 
		else {
			if(!isInformazioniProgettoCompiled){
				errorList.add(ERR_S4_P1);
				ccErr++;
				logger.info( logprefix + "tab 'Progetto/Informazioni sul progetto' non compilato");
			}
			if(!isAgevolazioneRichiestaCompiled){
				errorList.add(ERR_S4_P4);
				ccErr++;		
				logger.info( logprefix + "tab 'Progetto/Agevolazione richiesta' non compilato");
			}
			
			if (isInformazioniProgettoCompiled && isAgevolazioneRichiestaCompiled) {
				logger.info( logprefix + "STATO SEZIONE 'Progetto' compilata");
			}
		}
		logger.info( logprefix + "regole Sezione Progetto :: Sezioni compilate-complete END");
		
		
		// Il sistema verifica che siano state compilate tutte le pagine della sezione "Dichiarazioni e allegati" - BLOCCANTE
		logger.info( logprefix + "regole Sezione Dichiarazioni :: Sezioni compilate-complete BEGIN");
		
		// REGOLA V14: "Il sistema verifica che sia stata compilata correttamente la maschera [Dichiarazioni] - BLOCCANTE 
		// tab "Upload"
		boolean isDichiarazioniCompiled = false;
		if (input._dichiarazioni!= null) {
			isDichiarazioniCompiled = true;
			logger.info( logprefix + "tab 'Dichiarazioni e allegati/Dichiarazioni' compilato");
		}else{
			logger.info( logprefix + "tab 'Dichiarazioni e allegati/Dichiarazioni' Non compilato");
		}
		
		// ------------------------- TODO: controllo presnza allegati per beneficiario - 2r - inizio
		DomandaNGVO domandaNGVO = input._domanda;	
		String flagPubblicoPrivato = "1"; // privato
		if (domandaNGVO!=null){
			flagPubblicoPrivato = (String)domandaNGVO.getCodTipologiaUtente();
		}

		Integer idBando = info.getStatusInfo().getTemplateId();
		logger.info(logprefix + "idBando: demo Bando Cultura: " + idBando);

		Integer intDomanda = info.getStatusInfo().getNumProposta();
		logger.info(logprefix + "idDomanda: demo Bando Cultura: " + idDomanda);

		Integer idSportello = info.getStatusInfo().getNumSportello();
		logger.info(logprefix + "idSportello: demo Bando Cultura: " + idSportello);
				
		logger.info("Verifico documenti obbligatori supplementari uplodati: \n");
		boolean isAllegatiSupplementariPresenti = false;
		int numeroAllegatiSupplementari = 0;
		int cntDcmtSupplCaricati = 0;

		List<TipologiaAllegatoVO> documentiObbligatoriSupplementariList = new ArrayList<>();
		Map<String,String> idAllegatiMancantiMap = new HashMap<String,String>();
		int idTipologiaBeneficiario = -1;
		String descrizioneTipologiaUtente = "";

		numeroAllegatiSupplementari = MetodiUtili.isPresenteAllegatoSupplementare(idSportello, intDomanda, logger);

		if (domandaNGVO!=null) {
			if (numeroAllegatiSupplementari > 0) {
				isAllegatiSupplementariPresenti = true;
				logger.info("test: Sono presenti: "
						+ numeroAllegatiSupplementari
						+ " allegati supplementari!!!");

				idTipologiaBeneficiario = MetodiUtili.getTipologiaBeneficiario(intDomanda, logger);
				logger.info("test: tipologiaBeneficiario: "
						+ idTipologiaBeneficiario);

				// recupero descrizione tipologia beneficiario:
				// descrizioneTipologiaUtente = _domanda.getDescrTipologiaUtente();
				descrizioneTipologiaUtente = domandaNGVO
						.getDescrTipologiaUtente();
				logger.info(" descrizione tipologia utente risulta: "
						+ descrizioneTipologiaUtente);

				if (idSportello != null && idTipologiaBeneficiario > 0) {
					// recupero la lista di documenti supplementari
					documentiObbligatoriSupplementariList = MetodiUtili
							.getElencoDocumentiObbligatoriSupplementari(
									idSportello, idTipologiaBeneficiario,
									logger);

					for (int i = 0; i < documentiObbligatoriSupplementariList
							.size(); i++) {
						idAllegatiMancantiMap.put(Integer
								.toString(documentiObbligatoriSupplementariList
										.get(i).getIdallegato()), "-");
					}
				}

				// recupero la lista di documenti standard
				AllegatiVO _allegati = input._allegati;

				if (_allegati != null) {
					if (_allegati.getAllegatiList() != null) {
						AllegatiItemVO[] allegatiList = _allegati.getAllegatiList();

						for (int i = 0; i < allegatiList.length; i++) {
							AllegatiItemVO docMap = allegatiList[i];

							if (docMap != null) {
								DocumentoVO documento = docMap.getDocumento();
								logger.info("[DEBUG] " + logprefix
										+ " documento=" + documento);
								if (documento != null) {
									String idTipologia = (String) documento
											.getIdTipologia();
									logger.info("[DEBUG] " + logprefix
											+ " idTipologia=" + idTipologia);
									for (int j = 0; j < documentiObbligatoriSupplementariList
											.size(); j++) {
										String idAllegatoSuppl = Integer
												.toString(documentiObbligatoriSupplementariList
														.get(j).getIdallegato());
										logger.info("[DEBUG] " + logprefix
												+ " idAllegatoSuppl= "
												+ idAllegatoSuppl);
										String descrizioneAllegatoSuppl = documentiObbligatoriSupplementariList
												.get(j).getDescrizione();
										logger.info("[DEBUG] " + logprefix
												+ " descrizioneAllegatoSuppl= "
												+ descrizioneAllegatoSuppl);

										if (idTipologia.equals(idAllegatoSuppl)) {
											idAllegatiMancantiMap.put(
													idAllegatoSuppl, "x");
											cntDcmtSupplCaricati++;
										}
									}
								}
							}
						}
					} else {
						logger.info("[DEBUG] " + logprefix
								+ " Nessun allegato supplementare presente!");
					}
				}
			} else {
				logger.info("test: NON sono presenti allegati supplementari per idBando: "
						+ idBando
						+ " idDomanda: "
						+ idDomanda
						+ " ed idSportello: " + idSportello);
				//documentiObbligatoriList = DocumentazioneNGDAO.getTipologiaAllegatoList(info.getStatusInfo().getTemplateId(), null, null, true, logger);
			}
		}
		// eseguo controllo definitivo
		String descrizioneMancante = "";
		String ERRMSG_DOC_SUPPLEMENTARI = "";

		if( cntDcmtSupplCaricati != numeroAllegatiSupplementari)
		{
			  for (Map.Entry<String, String> entry : idAllegatiMancantiMap.entrySet()) 
			  {
				  logger.info("Key : " + entry.getKey() + " Value : " + entry.getValue());
				  if(entry.getValue().equals("-"))
				  {
					  logger.info("Key : " + entry.getKey() + " mancante!");
					  for (int i = 0; i < documentiObbligatoriSupplementariList.size(); i++) 
					  {
						  Integer idAllegatoSuppl = documentiObbligatoriSupplementariList.get(i).getIdallegato();
						  logger.info( logprefix + "idAllegatoSuppl vale: " + idAllegatoSuppl);
						if(entry.getKey().equals(Integer.toString(idAllegatoSuppl)))
						{
							descrizioneMancante = documentiObbligatoriSupplementariList.get(i).getDescrizione();
							ERRMSG_DOC_SUPPLEMENTARI = "Allegati e Dichiarazioni / Allegati: Per il beneficiario "+descrizioneTipologiaUtente+" é obbligatorio allegare "+  descrizioneMancante;
							errorList.add(ERRMSG_DOC_SUPPLEMENTARI);
							logger.info( logprefix + ERRMSG_DOC_SUPPLEMENTARI +" risulta necessario allegare '" + descrizioneMancante+"'");
							ccErr++;
						}
					 }
				  }
				}
		}else{
			 logger.info("Tutti gli allegati sono stati caricati!");
		}
		// ------------------------- TODO: presnza allegati per beneficiario - 2r - fine
		
		
		/** verifica allegato 343 non piu richiesto */
		AllegatiVO _allegati = input._allegati;
//		String ERRMSG_DOC_343NONPRES = "Allegati e Dichiarazioni / Allegati alla domanda: Non è stato inserito il 'Documento di identità del richiedente (allegare copia fronte e retro del documento)'";
//		boolean founded = false;
//		if (_allegati != null && !_allegati.isEmpty()) 
//		{
//			//cerco nella lista degli allegati l'allegato con id = 137  
//			List<AllegatiItemVO> allegatiList = Arrays.asList(_allegati.getAllegatiList());			 
//			logger.info("[DEBUG] " + logprefix + " allegatiList="+allegatiList);
//		
//			if(allegatiList!=null)
//			{		    
//				for(int i=0; i<allegatiList.size();i++)
//				{
//					AllegatiItemVO allegato = (AllegatiItemVO)allegatiList.get(i);				
//				
//					if(allegato!=null)
//					{
//						DocumentoVO documento = allegato.getDocumento();
//						String idallegato = (String) documento.getIdTipologia();
//						logger.info("[DEBUG] " + logprefix + " idallegato="+idallegato);
//					
//						if(StringUtils.equals(idallegato, "343"))
//						{
//							founded = true;
//							break;
//						}
//					}
//				}
//			}
//		}
//		
//		if(!founded)
//		{
//			errorList.add(ERRMSG_DOC_343NONPRES);
//			ccErr++;
//		}
//		else {
//			logger.info("[INFO] " + logprefix + "Controllo presenza del Documento 343 'Per Enti privati/Soggetti Privati è obbligatorio allegare il 'Documento di identità del richiedente (allegare copia fronte e retro del documento)'");
//		}
		/** fine verifica allegato 343 */
		
		//REGOLA Vxxxx : : Il sistema verifica che siano stati inseriti tutti gli allegati obbligatori
		String ERRMSG_ALLEGATI_OBBL = "Allegati e Dichiarazioni / Allegati: Non sono stati inseriti tutti gli allegati obbligatori previsti";
		
		if(_allegati!=null) 
		{
			String elencoIdAllegatiObbligatori = _allegati.getElencoIdAllegatiObbligatori();
			boolean isAllegatoObblPresente = false;
			
			logger.info("[DEBUG] " + logprefix + " elencoIdAllegatiObbligatori="+elencoIdAllegatiObbligatori); //elencoIdAllegatiObbligatori=#2#3#6#>
			
			StringTokenizer st = new StringTokenizer(elencoIdAllegatiObbligatori,",");  
		    while (st.hasMoreTokens()) {  
		     logger.info("[DEBUG] " + logprefix + st.nextToken()); 
		     isAllegatoObblPresente=true;
		    }
		    
			//logger.info("[DEBUG] " + logprefix + " _allegati="+_allegati);
			
			Map<String,String> idTipologieSalvateMap = new HashMap<String,String>();
			
			if(_allegati.getAllegatiList()!=null)
			{
				List<AllegatiItemVO> allegatiList = Arrays.asList(_allegati.getAllegatiList());			 
			
				for(int i=0; i<allegatiList.size();i++)
				{
					AllegatiItemVO docMap = (AllegatiItemVO)allegatiList.get(i);
					//logger.info("[DEBUG] " + logprefix + " docMap="+docMap);
					
					if(docMap!=null)
					{
						DocumentoVO documento = docMap.getDocumento();
						logger.info("[DEBUG] " + logprefix + " documento="+documento);
					
						if(documento!=null)
						{
							String idTipologia = (String) documento.getIdTipologia();
							logger.info("[DEBUG] " + logprefix + " idTipologia="+idTipologia);
							
							idTipologieSalvateMap.put(idTipologia,"x");
						}
					}
				}
				
				if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals(""))
				{
					// elimino primo carattere ','		
					String strTmp = elencoIdAllegatiObbligatori.substring(1);
					String[] arrayIdTipolObbl = strTmp.split(",");
					int counter = 0;
				
					for (int i = 0; i < arrayIdTipolObbl.length; i++) 
					{
						if(idTipologieSalvateMap.containsKey(arrayIdTipolObbl[i]))
						{
							logger.info("[DEBUG] " + logprefix + "arrayIdTipolObbl["+i+"]="+arrayIdTipolObbl[i] + "INNN");
							counter ++; // trovato un doc obbligatorio
						}
					}
				
					if(counter!=arrayIdTipolObbl.length)
					{
						// se i doc contati sono diversi da quelli attesi allora ERRORE
						errorList.add(ERRMSG_ALLEGATI_OBBL);
						ccErr++;
					}
					else {
						logger.info("[INFO] " + logprefix + "Regola Vxxxx OK");
					}
				}
				logger.info("[DEBUG] " + logprefix + " idTipologieSalvateMap="+idTipologieSalvateMap);
			}
			else{
				if(isAllegatoObblPresente)
				{
				  errorList.add(ERRMSG_ALLEGATI_OBBL);
				  ccErr++;
				}
			}
		}
		
		
		boolean isUploadCompiled = false;
		if (input._allegati!= null) {
			
			isUploadCompiled = true;
			
			_allegati = input._allegati;
			String elencoIdAllegatiObbligatori = _allegati.getElencoIdAllegatiObbligatori();
			logger.info("elencoIdAllegatiObbligatori " + elencoIdAllegatiObbligatori);
			if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")){
				AllegatiItemVO[] allegatiList = _allegati.getAllegatiList();			
				if(allegatiList==null || (allegatiList!=null && allegatiList.length==0)){
					isUploadCompiled = false;
				}
			}
			
			logger.info( logprefix + "tab 'Documentazione/Upload' compilato");
		 } else {
			logger.info( logprefix + "tab 'Documentazione/Upload' non compilato");
		 }
		
		if (!isDichiarazioniCompiled && !isUploadCompiled) {
			errorList.add(ERR_S3);
			ccErr++;
			logger.info( logprefix + "cartella 'Dichiarazioni e allegati' non compilata");
		}else{
			if(!isDichiarazioniCompiled){
				errorList.add(ERR_S3_P2);
				ccErr++;		
				logger.info( logprefix + "tab 'Dichiarazioni e allegati/Dichiarazioni' non compilato");
			}
			if (isDichiarazioniCompiled) {		
				logger.info( logprefix + "STATO SEZIONE 'Dichiarazioni e allegati' compilata");
			}
			if(!isUploadCompiled){
				errorList.add(ERR_S3_P3);
				ccErr++;		
				logger.info( logprefix + "tab 'Dichiarazioni e allegati/Upload' non compilato");
			}
		}
		
		logger.info( logprefix + "regole Sezione Dichiarazioni e allegati :: Sezioni compilate-complete END");
		
		// regole di validazione generali
		
		// registrazione di tutti gli errori rilevati
		
		// BLOCCANTI
		if (ccErr > 0) {
			for (int i = 0, n = errorList.size(); i < n; i++) {
				addMessage(newMessages,"AGGR_messageValidationError", (String) errorList.get(i));
			}
		}
		// NON BLOCCANTI
		if (ccWarn > 0) {
			for (int i = 0, n = warnList.size(); i < n; i++) {
				addMessage(newMessages,"AGGR_messageValidationWarn", (String) warnList.get(i));
			}
		}
		// messaggi informativi
		if (ccErr > 0) {
			addMessage(newMessages,"AGGR_messageValidationInfo", (ccErr > 1 ? ccErr + " errori BLOCCANTI" : "un errore BLOCCANTE"));
		}
		if (ccWarn > 0) {
			addMessage(newMessages,"AGGR_messageValidationInfo", (ccWarn > 1 ? ccWarn + " errori NON bloccanti" : "un errore NON bloccante"));
		}
		
		// registrazione di tutti gli errori rilevati
		
		logger.info("[Direttiva::globalValidate] Bonus Piemonte Turismo END");

	    return newMessages;
  }

  
  @Override
  public void initConfigurations() {
    
    super.initConfigurations();
    
    /** var cfg */
    configuration.put("msgDocumentiOpzionaliPerBando", "true");
    
    /** recupero importo contributo by cf, risalgo all'idSoggetto e recupero importo contributo per il bonus */
    configuration.put("_caratteristicheProgettoNG_imp_contr_by_sogg_abilitati", "true");
    
    /** sede legale solo in Piemonte */
    configuration.put("_sedeLegale_solo_province_piemonte", "true");
    
    /** var di cfg usata anche per bando Aps: */
	configuration.put("_estremiBancari_intestatario_denominazione", "true");
    
    /** eseguire controllo che sia compilato almeno 1 campo: email or pec */
    configuration.put("_sedeLegale_emailOrPecObbligatorio", "true");
    
    /** bic campo non obbligatorio */
    configuration.put("_estremiBancari_bicObbligatorio", "true");
    
    /** no dipartimenti */
    configuration.put("operatorePresentatore_dipartimenti", "false");
    
    /** no pec */
    configuration.put("_operatorePresentatore_indirizzoPec", "true");
    
    /** no costituzione impresa */
    configuration.put("_costituzioneImpresa_costituzione_in_corso", "false");
    configuration.put("_costituzioneImpresa_iscrizione_in_corso", "false");
    configuration.put("_costituzioneImpresa_data_solo_primo_salvataggio", "false");
    
    /** no soggetto delegato */
    configuration.put("_legaleRappresentante_presenza_soggetto_delegato", "false");
    
    /** no select forma giuridica */
    configuration.put("_operatorePresentatore_formaGiuridica_lavoratoreAutonomo", "false");
    
    /** non settore ateco */
    configuration.put("_operatorePresentatore_ateco", "false");
    
	configuration.put("_sede_legale_visualizza_nota_precompilazione", "true");
	configuration.put("_dettaglioSede_max_una_sede_intervento", "true");
	
	/** var cfg : numero e relativa data di Iscrizione al Registro APS */
    configuration.put("_operatorePresentatore_codiceAps", "false");
    
    configuration.put("_operatorePresentatore_dataIscrLimMax", "false");
	
  }

  protected void initializeData() {

  }

  public void commandValidate() {
  }

}
