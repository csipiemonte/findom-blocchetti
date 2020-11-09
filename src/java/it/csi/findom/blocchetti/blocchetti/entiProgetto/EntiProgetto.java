/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.entiProgetto;

import it.csi.findom.blocchetti.common.dao.EnteDAO;
import it.csi.findom.blocchetti.common.dao.ParametriDAO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.ente.EnteLocaleVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class EntiProgetto extends Commonality {

	  EntiProgettoInput input = new EntiProgettoInput();

	  @Override
	  public EntiProgettoInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[EntiProgetto::inject] BEGIN");
	    	
        EntiProgettoOutput output = new EntiProgettoOutput();
		
		//// dichiarazioni			

		//List<EnteLocaleVO> entiList = new ArrayList<EnteLocaleVO>();		
		
		int totPopolazione = 0;	
		int totPopolazioneGruppo = 0;
		
		String codiceTipologiaBeneficiario = "";
		DomandaNGVO _domanda = input._domanda;
		codiceTipologiaBeneficiario = _domanda.getCodiceTipologiaBeneficiario();

		//MB2018_11_14 ini
		String clausola = "";
        if(StringUtils.isNotBlank(input._entiProgetto_parametro_caricamento_comuni)){
           int idParametro = Integer.parseInt(input._entiProgetto_parametro_caricamento_comuni);
		   clausola = ParametriDAO.getValoreParametroById(idParametro, logger);
        }
		//MB2018_11_14 fine		
		
        //MB2018_11_14 List<EnteLocaleVO> entiList = EnteDAO.getComuniPiemonteList();
        List<EnteLocaleVO> entiList = EnteDAO.getComuniPiemonteList(clausola,logger);//MB2018_11_14
		
		List<EnteProgettoItemVO> entiProgettoList = new ArrayList<EnteProgettoItemVO>();
		// carico lista comuni inserite in XML
		EntiProgettoVO entiProgetto = input._entiProgetto;
		if( entiProgetto != null && !entiProgetto.isEmpty() && entiProgetto.getEntiProgettoList()!= null && entiProgetto.getEntiProgettoList().length>0) {
		
			entiProgettoList = Arrays.asList(entiProgetto.getEntiProgettoList());			
			logger.info("[EntiProgetto::inject] entiProgettoList " + entiProgettoList);
			
			EnteLocaleVO datiEnte = null;
			String codIstatEnteSelezionato = "";
			if (input.codIstatEnteSelezionato != null){
				codIstatEnteSelezionato = input.codIstatEnteSelezionato;
				datiEnte = getDatiEnte(entiList, codIstatEnteSelezionato, logger);
				
				/** debug: ciclo sulla lista recuperata */
				logger.info("debug: - datiEnte: " + datiEnte);
			} 
						
			// nella lista potrebbero esserci degli elementi DELETED
			// rimappo questi elementi in oggetti Map altrimenti Freemarker si rompe
			 if(entiProgettoList!=null && !entiProgettoList.isEmpty()){
				for(int i=0; i<entiProgettoList.size(); i++){
					EnteProgettoItemVO curEnte = null;
						curEnte = (EnteProgettoItemVO)entiProgettoList.get(i);
						// if (curEnte==null && !curEnte.is__Deleted()) {
						if (curEnte != null && !curEnte.is__Deleted()) {
						logger.debug("[EntiProgetto::inject] curEnte: " + curEnte);
						
						if (codIstatEnteSelezionato!=null && !codIstatEnteSelezionato.equals("")){
							logger.debug("[EntiProgetto::inject]  codIstatEnteSelezionato: " + codIstatEnteSelezionato);
							if (curEnte.getCodiceEnte().equals(codIstatEnteSelezionato)){
								curEnte.setPopolazione(datiEnte.getPopolazione());
								curEnte.setClassificazione(datiEnte.getClassificazione());
								curEnte.setTipoEnte(datiEnte.getTipoente());
							}
						} 						
						
						logger.debug("[EntiProgetto::inject] letto ente valido:" + curEnte);
					} else {
						logger.info("[EntiProgetto::inject]  trovata comune DELETED");
						//faccio in modo da rimettere nell'html il campo marcato per essere cancellato
						curEnte = new EnteProgettoItemVO();
						curEnte.setDaCancellare("true");
						entiProgettoList.set(i,curEnte);
						continue;
					}							
					if (curEnte!=null){
						String popolazione = curEnte.getPopolazione();
						String partecipazione = curEnte.getPartecipazioneProgetto();
						
						if (!StringUtils.isBlank(popolazione))
							popolazione = popolazione.replace(".", "");
												
						try { 							
      						int tot = Integer.parseInt(popolazione);
      						if (!StringUtils.isBlank(partecipazione) && "checked".equalsIgnoreCase(partecipazione)){
      							totPopolazione += tot;
      						} 
      						totPopolazioneGruppo += tot;
      						
    					} catch(NumberFormatException e) { 
        					continue; 
    					} 
					}	 
				}
			 }
		} else {
			
			// se la lista é vuota e lo stereotipo é PRV o CME o COM
			// prepopoliamo la lista con i dati della provincia/città metropolitana comune
			if(StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "COM") || StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "PRV") || StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "CME")){
				String codFiscaleEnteCollegato = info.getStatusInfo().getCodFiscaleBeneficiario();
				logger.info("[EntiProgetto::inject] codFiscaleEnteCollegato " + codFiscaleEnteCollegato);
				//List provList = getProvincia(codFiscaleProvCollegata);
				entiList = EnteDAO.getEnteLocale(codFiscaleEnteCollegato);
								
				// ATTENZIONE: verrà fatto un controllo a monte 
				// per utente loggato e tipologia beneficiario scelta
				// per cui comuniList conterrà sicuramente l'elemento che ci interessa
				EnteLocaleVO enteMap = entiList.get(0);
				
				EnteProgettoItemVO ente = new EnteProgettoItemVO();
				
				logger.info("Debug: [row:140] enteMap.getCodice() " + enteMap.getCodice());
				ente.setCodiceEnte(enteMap.getCodice());

				ente.setPopolazione(enteMap.getPopolazione());
				ente.setClassificazione(enteMap.getClassificazione());
				ente.setPartecipazioneProgetto("checked");
				ente.setDescrizioneEnte(enteMap.getDescrizione());
				ente.setTipoEnte(enteMap.getTipoente());
				entiProgettoList.add(ente);
				
			}			
		}
		logger.info("[EntiProgetto::inject] totalePopolazione " + totPopolazione);

		//// namespace		
		output.setEntiList(entiList);
		output.setEntiProgettoList(entiProgettoList);
		output.setTotalePopolazione(totPopolazione+"");
		output.setTotalePopolazioneGruppo(totPopolazioneGruppo+"");
		output.setCodiceTipologiaBeneficiario(codiceTipologiaBeneficiario);
		logger.info("[EntiProgetto::inject] _entiProgetto END");
	        
        return output;
	  }

	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[EntiProgetto::modelValidate] _entiProgetto  BEGIN");
			
		//// validazione panel Enti progetto
	
		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo é obbligatorio";
		String ERRMSG_NRO_ENTI_PARTECIPANTI = "Almeno due Enti devono partecipare al progetto";
		String ERRMSG_RTE_PARTECIPANTI = "Tutti gli enti indicati devono partecipare al progetto";
		String ERRMSG_RTE_UTENTE = "L'ente per cui si sta inserendo la domanda deve essere compreso fra i partecipanti al progetto";
		String ERRMSG_TABELLA_OBB = "Compilare la sezione Dati riepilogativi";
		String ERRMSG_ENTE_DUPLICATO = "questo ente è già presente in tabella";
	
		try {

				boolean trovatoUtente = false;
				boolean trovatoEr = false;
				
				DomandaNGVO _domanda = input._domanda;
				String codiceTipologiaBeneficiario = _domanda.getCodiceTipologiaBeneficiario();
				
				EntiProgettoVO entiProgetto = input._entiProgetto;
				if (entiProgetto != null && entiProgetto.getEntiProgettoList()!=null && entiProgetto.getEntiProgettoList().length>0) {
				
					logger.info("[EntiProgetto::modelValidate] entiProgetto: " + entiProgetto);
				
					List<EnteProgettoItemVO> entiProgettoList = Arrays.asList(entiProgetto.getEntiProgettoList());
							
					if (entiProgettoList != null && !entiProgettoList.isEmpty()) {		
									
						String codiceFiscaleBeneficiario = info.getStatusInfo().getCodFiscaleBeneficiario();
						String codIstatUtente = EnteDAO.getCodIstat(codiceFiscaleBeneficiario);
						
						int nroPartecipanti = 0;
					    ArrayList<String> codIstatVisti = new ArrayList<>();	
						for (int i=0; i<entiProgettoList.size(); i++) {
						
							EnteProgettoItemVO itemEnte = (EnteProgettoItemVO)entiProgettoList.get(i);
							logger.debug("[EntiProgetto::modelValidate] itemEnte:" + itemEnte);
	
							if (itemEnte != null && !itemEnte.isEmpty()) {
								
								// controllo valorizzazione codiceEnte
								String codiceEnte = itemEnte.getCodiceEnte();
								if(StringUtils.isBlank(codiceEnte)) {
									addMessage(newMessages,"_entiProgetto_valore_testo_codiceEnte", ERRMSG_CAMPO_OBBLIGATORIO);
									addMessage(newMessages,"_entiProgetto_valore_codiceEnte", i+"");
									logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_codiceEnte nullo");
									trovatoEr = true;
								} else {
									if (codiceEnte.equalsIgnoreCase(codIstatUtente)){
										trovatoUtente = true;
									}
									if(!codIstatVisti.contains(codiceEnte)){
									     codIstatVisti.add(codiceEnte);
									}else{
										addMessage(newMessages,"_entiProgetto_valore_testo_codiceEnte", ERRMSG_ENTE_DUPLICATO);
										addMessage(newMessages,"_entiProgetto_valore_codiceEnte", i+"");
										logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_codiceEnte duplicato");
										trovatoEr = true;										
									}
								}
								
								String partecipazioneProgetto = itemEnte.getPartecipazioneProgetto();						
								if(!StringUtils.isBlank(partecipazioneProgetto) && partecipazioneProgetto.equalsIgnoreCase("checked")) { 
									//MB2018_11_14 ini controllo i nuovi campi
									// controllo valorizzazione pianoComunale
									String pianoComunale = itemEnte.getPianoComunale();
									if(StringUtils.isBlank(pianoComunale)) {
										addMessage(newMessages,"_entiProgetto_valore_testo_pianoComunale", ERRMSG_CAMPO_OBBLIGATORIO);
										addMessage(newMessages,"_entiProgetto_valore_pianoComunale", i+"");
										logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_pianoComunale nullo");
										trovatoEr = true;
									} 
									// controllo valorizzazione pianoComunale
									String paes = itemEnte.getPaes();
									if(StringUtils.isBlank(paes)) {
										addMessage(newMessages,"_entiProgetto_valore_testo_paes", ERRMSG_CAMPO_OBBLIGATORIO);
										addMessage(newMessages,"_entiProgetto_valore_paes", i+"");
										logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_paes nullo");
										trovatoEr = true;
									} 
									//MB2018_11_14 fine
//MB2018_11_14 ini commento controlli sui campi che non ci sono piu'								
									// controllo valorizzazione certificazioneAmbientale
//									String certificazioneAmbientale = itemEnte.getCertificazioneAmbientale();
//									if(StringUtils.isBlank(certificazioneAmbientale)) {
//										addMessage(newMessages,"_entiProgetto_valore_testo_certificazioneAmbientale", ERRMSG_CAMPO_OBBLIGATORIO);
//										addMessage(newMessages,"_entiProgetto_valore_certificazioneAmbientale", i+"");
//										logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_certificazioneAmbientale nullo");
//										trovatoEr = true;
//									} else {
//										
//										// se certificazioneAmbientale vale SI deve essere valorizzato anche certificazioneAmbientaleText
//										if(certificazioneAmbientale.equalsIgnoreCase("SI")) {
//									
//											String certificazioneAmbientaleText = itemEnte.getCertificazioneAmbientaleText();
//											if(StringUtils.isBlank(certificazioneAmbientaleText)) {
//												addMessage(newMessages,"_entiProgetto_valore_testo_certificazioneAmbientaleText", ERRMSG_CAMPO_OBBLIGATORIO);
//												addMessage(newMessages,"_entiProgetto_valore_certificazioneAmbientaleText", i+"");
//												logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_certificazioneAmbientaleText nullo");
//												trovatoEr = true;
//											} 
//										}
//									}
								
									// controllo valorizzazione pattoSindaci
//									String pattoSindaci = itemEnte.getPattoSindaci();
//									if(StringUtils.isBlank(pattoSindaci)) {
//										addMessage(newMessages,"_entiProgetto_valore_testo_pattoSindaci", ERRMSG_CAMPO_OBBLIGATORIO);
//										addMessage(newMessages,"_entiProgetto_valore_pattoSindaci", i+"");
//										logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_pattoSindaci nullo");
//										trovatoEr = true;
//									} 
								
									// controllo valorizzazione richiestaAgevolazioni
//									String richiestaAgevolazioni = itemEnte.getRichiestaAgevolazioni();
//									if(StringUtils.isBlank(richiestaAgevolazioni)) {
//										addMessage(newMessages,"_entiProgetto_valore_testo_richiestaAgevolazioni", ERRMSG_CAMPO_OBBLIGATORIO);
//										addMessage(newMessages,"_entiProgetto_valore_richiestaAgevolazioni", i+"");
//										logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_richiestaAgevolazioni nullo");
//										trovatoEr = true;
//									} else {
//									
//										// se richiestaAgevolazioni vale SI deve essere valorizzato anche richiestaAgevolazioniText
//										if(richiestaAgevolazioni.equalsIgnoreCase("SI")) {
//									
//											String richiestaAgevolazioniText = itemEnte.getRichiestaAgevolazioniText();
//											if(StringUtils.isBlank(richiestaAgevolazioniText)) {
//												addMessage(newMessages,"_entiProgetto_valore_testo_richiestaAgevolazioniText", ERRMSG_CAMPO_OBBLIGATORIO);
//												addMessage(newMessages,"_entiProgetto_valore_richiestaAgevolazioniText", i+"");
//												logger.warn("[EntiProgetto::modelValidate] _entiProgetto_entiProgettoList[]_richiestaAgevolazioniText nullo");
//												trovatoEr = true;
//											} 
//										}
//									}
//MB2018_11_14 fine commento controlli sui campi che non ci sono piu'
									nroPartecipanti++;
				
								}
								
							} // chiude if (itemEnte != null && !itemEnte.isEmpty()) 
						} // chiude for (int i=0; i<entiProgettoList.size(); i++)
						
						// per l'unione dei comuni e per i raggruppamenti dobbiamo controllare 
						// che ci siano almeno 2 partecipanti
						if(StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "UCO") || StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "RTE")){
							
							if (nroPartecipanti<2){			
								addMessage(newMessages,"_entiProgetto", ERRMSG_NRO_ENTI_PARTECIPANTI);
								trovatoEr = true;
							} else {
								// per RTE tutti i record devono avere la selezione partecipazioneProgetto
								if (StringUtils.equalsIgnoreCase(codiceTipologiaBeneficiario, "RTE")){
									
									int nroEnti = entiProgettoList.size();
									
									if (nroPartecipanti!=nroEnti){
										addMessage(newMessages,"_entiProgetto", ERRMSG_RTE_PARTECIPANTI);
										trovatoEr = true;
									} else {
										// controlliamo che il beneficiario sia presente fra i partecipanti al raggruppamento
										if (!trovatoUtente){
											addMessage(newMessages,"_entiProgetto", ERRMSG_RTE_UTENTE);
											trovatoEr = true;
										}
									}
								}
							}
						} 
					} else { // entiProgettoList == null  o vuoto
						logger.warn("[EntiProgetto::modelValidate] _entiProgettoList non presente o vuoto");
						addMessage(newMessages,"_entiProgetto", ERRMSG_TABELLA_OBB);
						trovatoEr = true;
					} 
					
				} else {
					logger.warn("[EntiProgetto::modelValidate] _entiProgetto non presente o vuoto");
					addMessage(newMessages,"_entiProgetto", ERRMSG_TABELLA_OBB);
					trovatoEr = true;
				}
		  
		} catch (Exception e) {
			logger.error("[EntiProgetto::modelValidate] Generic ERROR", e);
		}
		finally {
			logger.info("EntiProgetto::modelValidate] _entiProgetto S2_P6 END");
		}
	
		return newMessages;

	  }
	  
	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }

	  private EnteLocaleVO getDatiEnte(List<EnteLocaleVO> entiList, String codIstatEnte, Logger logger) {
		  EnteLocaleVO ente = new EnteLocaleVO();
		  logger.info("EntiProgetto::inject] getDatiEnte(codIstatEnte:" + codIstatEnte + ")");
			
			if (codIstatEnte==null || codIstatEnte.equals("")){
				ente.setPopolazione("");
				ente.setClassificazione("");
				ente.setDescrizione("");
				ente.setTipoente("");
				return ente;
			} 
			for (EnteLocaleVO com : entiList){
				if(StringUtils.equals(codIstatEnte, com.getCodice())){
					ente.setCodice(com.getCodice());
					ente.setDescrizione(com.getDescrizione());
					if (com.getPopolazione()!=null)
						ente.setPopolazione(com.getPopolazione());
					else
						ente.setPopolazione("");
					ente.setClassificazione(com.getClassificazione());	
					ente.setTipoente(com.getTipoente());			
					break;
				}
			}
			logger.info("EntiProgetto::inject] getDatiEnte(ente:" + ente + ")");
			return ente;
		}
}
