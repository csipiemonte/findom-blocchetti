/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.ws.aaep;

import it.csi.aaep.aaeporch.business.AttivitaEconomica;
import it.csi.aaep.aaeporch.business.OrchestratoreIntf;
import it.csi.aaep.aaeporch.business.Sede;
import it.csi.aaep.aaeporch.business.TipologiaFonte;
import it.csi.findom.blocchetti.blocchetti.bilancio.BilancioAziendaAAEPVO;
import it.csi.findom.blocchetti.blocchetti.bilancio.DettaglioBilancioAziendaAAEPVO;
import it.csi.findom.blocchetti.common.vo.aaep.AttivitaEconomicaVO;
import it.csi.findom.blocchetti.common.vo.aaep.ContattiVO;
import it.csi.findom.blocchetti.common.vo.aaep.PersonaINFOCVO;
import it.csi.findom.blocchetti.common.vo.aaep.SedeVO;
import it.csi.findom.blocchetti.common.vo.aaep.UbicazioneVO;
import it.csi.findom.blocchetti.commonality.Endpoints;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.melograno.aggregatore.freemarker.FreemarkerBshTools;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

public class AaepWs {
	  private static final AaepWs instance = new AaepWs();

	  private OrchestratoreIntf service = null;
//	  
	  private AaepWs() {
	  }
	  
	  public static AaepWs getInstance() {
	    return instance;
	  }
	  
	  private OrchestratoreIntf getService(Logger logger) throws Exception {
	    if(service==null) {
	      try {
	        String endpoint= Endpoints.getInstance().getProperty("endpointOrchestratoreService");
	        
	        URL aaepOrchUrl = this.getClass().getResource("/wsdl/orchestratoreService-2.0.0.wsdl");
	        String targetNamespace = Endpoints.getInstance().getProperty("nameSpaceWSOrchestratoreService"); 
	        
			String wsdlService = "OrchestratoreImplService";
			QName serviceName = new QName(targetNamespace,wsdlService); 
			
	        Service servAAEPOrchCxfService = Service.create(aaepOrchUrl, serviceName);
				
	        service = servAAEPOrchCxfService.getPort(OrchestratoreIntf.class);
			
			BindingProvider bp = (BindingProvider)service;
			Map<String, Object> map  = bp.getRequestContext();
			map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpoint);
	        
	      }
	      finally {
	      }
	    }
	    return service;
	  }

	  //  : rivedere metodo sopra
	  public PersonaINFOCVO getDatiPersonaFromAAEPOld(String ricerca, Logger logger) throws CommonalityException {
		    logger.info("[AaepWs::getDatiPersonaFromAAEPOld] (ricerca="+ricerca+")");
			String[] par = ricerca.split("/");
			String idAzienda = par[0];
			String idFonteDato = par[1];
			String idPersona = par[2];
			
			logger.debug("[[AaepWS::getDatiPersonaFromAAEPOld idAzienda=" + idAzienda);
			logger.debug("[[AaepWS::getDatiPersonaFromAAEPOld idFonteDato=" + idFonteDato);
			logger.debug("[[AaepWS::getDatiPersonaFromAAEPOld idPersona=" + idPersona);
			
			LinkedHashMap params = new LinkedHashMap();
			params.put("persona.idAzienda", idAzienda);
			params.put("persona.idFonteDato", idFonteDato);
			params.put("persona.idPersona", idPersona);

			Map persVO;
			try {
				persVO = (Map)  new FreemarkerBshTools().readMapfromWS(Endpoints.getInstance().getProperty("endpointOrchestratoreService"),  "return", "getDettaglioPersonaInfoc", Endpoints.getInstance().getProperty("nameSpaceWSOrchestratoreService"), "return", params);
			} catch (Exception e) {
				logger.error("[AaepWs::getDatiPersonaFromAAEPOld] Error during WS", e);
				throw new CommonalityException(e.getMessage(), e);
			}
			
			
			PersonaINFOCVO persona = new PersonaINFOCVO();
			persona.setCodiceFiscale((String)persVO.get("codiceFiscale"));
			persona.setCognome((String)persVO.get("cognome"));
			persona.setDescrTipoPersona((String)persVO.get("descrTipoPersona"));
			persona.setIdAzienda((String)persVO.get("idAzienda"));
			persona.setIdFonteDato((String)persVO.get("idFonteDato"));
			persona.setIdPersona((String)persVO.get("idPersona"));
			persona.setNome((String)persVO.get("nome"));
			persona.setTipoPersona((String)persVO.get("tipoPersona"));

			//  Lista cariche non viene tornato correttamente e comunque non é usato.
//			List<CaricaVO> lista = null;
//			if (persVO.get("listaCariche") != null) {
//
//				lista = new ArrayList<CaricaVO>();
//				for (Iterator<Map<String, String>> itr = ((List<Map<String, String>>) persVO.get("listaCariche")).iterator(); itr.hasNext();) {
//					Map<String, String> caricaVO =  itr.next();
//
//					CaricaVO carica = new CaricaVO();
//					carica.setCodCarica(caricaVO.get("codCarica"));
//					carica.setCodDurataCarica(caricaVO.get("codDurataCarica"));
//					carica.setCodFiscaleAzienda(caricaVO.get("codFiscaleAzienda"));
//					carica.setCodFiscalePersona(caricaVO.get("codFiscalePersona"));
//					carica.setDataFineCarica(caricaVO.get("dataFineCarica"));
//					carica.setDataInizioCarica(caricaVO.get("dataInizioCarica"));
//					carica.setDataPresentazCarica(caricaVO.get("dataPresentazCarica"));
//					carica.setDescrAzienda(caricaVO.get("descrAzienda"));
//					carica.setDescrCarica(caricaVO.get("descrCarica"));
//					carica.setDescrDurataCarica(caricaVO.get("descrDurataCarica"));
//					carica.setFlagRappresentanteLegale(caricaVO.get("flagRappresentanteLegale"));
//					carica.setIdAzienda(caricaVO.get("idAzienda"));
//					carica.setIdFonteDato(caricaVO.get("idFonteDato"));
//					carica.setNumAnniEsercCarica(caricaVO.get("numAnniEsercCarica"));
//					carica.setProgrCarica(caricaVO.get("progrCarica"));
//					carica.setProgrPersona(caricaVO.get("progrPersona"));
//					lista.add(carica);
//				}
//			}
//			persona.setListaCariche(lista);
			persona.setCapResidenza((String)persVO.get("capResidenza"));
            persona.setCodToponimoResid((String)persVO.get("codToponimoResid"));
            persona.setDataNascita((String)persVO.get("dataNascita"));
            persona.setDescrComuneNascita((String)persVO.get("descrComuneNascita"));
            persona.setDescrComuneRes((String)persVO.get("descrComuneRes"));
            persona.setDescrIndicatoriPotereF((String)persVO.get("descrIndicatoriPotereF"));
            persona.setDescrToponimoResid((String)persVO.get("descrToponimoResid"));
            persona.setFlagElettore((String)persVO.get("flagElettore"));
            persona.setIndicPoteriFirma((String)persVO.get("indicPoteriFirma"));
            persona.setNumCivicoResid((String)persVO.get("numCivicoResid"));
            persona.setPercentPartecip((String)persVO.get("percentPartecip"));
            persona.setProgrOrdineVisura((String)persVO.get("progrOrdineVisura"));
            persona.setProgrUnitaLocale((String)persVO.get("progrUnitaLocale"));
            persona.setQuotaPartecipaz((String)persVO.get("quotaPartecipaz"));
            persona.setQuotaPartecipazEuro((String)persVO.get("quotaPartecipazEuro"));
            persona.setSesso((String)persVO.get("sesso"));
            persona.setSiglaProvNascita((String)persVO.get("siglaProvNascita"));
            persona.setSiglaProvResidenza((String)persVO.get("siglaProvResidenza"));
            persona.setViaResidenza((String)persVO.get("viaResidenza"));
            persona.setCodComuneNascita((String)persVO.get("codComuneNascita"));

            persona.setCodStatoRes((String)persVO.get("codStatoRes"));
            
            persona.setCodComuneRes((String)persVO.get("codComuneRes"));
            persona.setCodCittadinanza((String)persVO.get("codCittadinanza"));
            persona.setCodStatoNascita((String)persVO.get("codStatoNascita"));
            persona.setDescrCittadinanza((String)persVO.get("descrCittadinanza"));
            persona.setDescrStatoNascita((String)persVO.get("descrStatoNascita"));
            
			SessionCache.getInstance().set("mappaDatiConsLRAAEP", persona);

			return persona;
		}

	

	  /*
	   * Mancano delle informazioni nel dto dello stub, ma presenti nel xml di ritorno ( mentre mancano comunque anche nel wsdl)
	   */
//	  public PersonaINFOCVO getDatiPersonaFromAAEP(String ricerca, Logger logger) throws CommonalityException {
//		    logger.info("[AaepWs::getDatiPersonaFromAAEP] (ricerca="+ricerca+")");
//			String[] par = ricerca.split("/");
//			String idAzienda = par[0];
//			String idFonteDato = par[1];
//			String idPersona = par[2];
//			
//			logger.debug("[[AaepWS::getDatiPersonaFromAAEP idAzienda=" + idAzienda);
//			logger.debug("[[AaepWS::getDatiPersonaFromAAEP idFonteDato=" + idFonteDato);
//			logger.debug("[[AaepWS::getDatiPersonaFromAAEP idPersona=" + idPersona);
//			
//			Persona personaParam = new Persona();
//			personaParam.setIdAzienda(idAzienda);
//			personaParam.setIdFonteDato(idFonteDato);
//			personaParam.setIdPersona(idPersona);
//			
//			Persona persVO;
//			try {
//				persVO = getService(logger).getDettaglioPersonaInfoc(null, personaParam);
//			} catch (Exception e) {
//				logger.error("[AaepWs::getDatiPersonaFromAAEP] Error during WS", e);
//				throw new CommonalityException(e.getMessage(), e);
//			}
//			
//
//			PersonaINFOCVO persona = new PersonaINFOCVO();
//			persona.setCodiceFiscale(persVO.get("codiceFiscale"));
//			persona.setCognome(persVO.get("cognome"));
//			persona.setDescrTipoPersona(persVO.get("descrTipoPersona"));
//			persona.setIdAzienda(persVO.get("idAzienda"));
//			persona.setIdFonteDato(persVO.get("idFonteDato"));
//			persona.setIdPersona(persVO.get("idPersona"));
//			List<CaricaVO> lista = null;
//			if (persVO.get("listaCariche") != null) {
//
//				lista = new ArrayList<CaricaVO>();
//				for (Iterator<Carica> itr = persVO.get("listaCariche").iterator(); itr.hasNext();) {
//					Carica caricaVO =  itr.next();
//
//					CaricaVO carica = new CaricaVO();
//					carica.setCodCarica(caricaVO.getCodCarica());
//					carica.setCodDurataCarica(caricaVO.getCodDurataCarica());
//					carica.setCodFiscaleAzienda(caricaVO.getCodFiscaleAzienda());
//					carica.setCodFiscalePersona(caricaVO.getCodFiscalePersona());
//					carica.setDataFineCarica(caricaVO.getDataFineCarica());
//					carica.setDataInizioCarica(caricaVO.getDataInizioCarica());
//					carica.setDataPresentazCarica(caricaVO.getDataPresentazCarica());
//					carica.setDescrAzienda(caricaVO.getDescrAzienda());
//					carica.setDescrCarica(caricaVO.getDescrCarica());
//					carica.setDescrDurataCarica(caricaVO.getDescrDurataCarica());
//					carica.setFlagRappresentanteLegale(caricaVO.getFlagRappresentanteLegale());
//					carica.setIdAzienda(caricaVO.getIdAzienda());
//					carica.setIdFonteDato(caricaVO.getIdFonteDato());
//					carica.setNumAnniEsercCarica(caricaVO.getNumAnniEsercCarica());
//					carica.setProgrCarica(caricaVO.getProgrCarica());
//					carica.setProgrPersona(caricaVO.getProgrPersona());
//					lista.add(carica);
//				}
//			}
//			persona.setListaCariche(lista);
//			persona.setNome(persVO.get("Nome());
//			persona.setTipoPersona(persVO.get("TipoPersona());
//		//  mancano nello stub delle property
//			SessionCache.getInstance().set("mappaDatiLRAAEP", persona);
//
//			return persona;
//		}


	  //  : rivedere metodo sopra
	  
	@SuppressWarnings("unchecked")
	public BilancioAziendaAAEPVO getBilancioAziendaFromAAEP(String idAzienda, Logger logger) throws CommonalityException {			
			
			logger.debug("[AaepWS::getBilancioAziendaFromAAEP] idAzienda=" + idAzienda);
			
			LinkedHashMap params = new LinkedHashMap();
			params.put("idAzienda", idAzienda);

			List<Map<String, String>> bilancioVO;
			try {
				bilancioVO = (List<Map<String, String>>) new FreemarkerBshTools().readListfromWS(Endpoints.getInstance().getProperty("endpointOrchestratoreService"),  "lista", "dettaglioBilancio", "getBilancioAzienda", Endpoints.getInstance().getProperty("nameSpaceWSOrchestratoreService"), "return", params);
				//lisDettBil = (List) tools.readListfromWS(endpointAAEP, "lista", "dettaglioBilancio", "getBilancioAzienda", nameSpaceWSAAEP, "return", params);

			} catch (Exception e) {
				logger.error("[AaepWs::getBilancioAziendaFromAAEP] Error during WS", e);
				throw new CommonalityException(e.getMessage(), e);
			}
			
			BilancioAziendaAAEPVO bilancio = new BilancioAziendaAAEPVO();

//			bilancio.setCodiceFiscale((String)bilancioVO.get("codiceFiscale"));
//			bilancio.setCapitaleSociale((String)bilancioVO.get("capitaleSociale"));
//			bilancio.setDataAggiornamento((String)bilancioVO.get("dataAggiornamento"));
//			bilancio.setIscrizioneAlboCOOP((String)bilancioVO.get("iscrizioneAlboCOOP"));
//			bilancio.setDenominazione((String)bilancioVO.get("denominazione"));
//			bilancio.setNumIscrizioneCCIAA((String)bilancioVO.get("numIscrizioneCCIAA"));
//			bilancio.setNumIscrizioneRegistroImprese((String)bilancioVO.get("numIscrizioneRegistroImprese"));
//			bilancio.setNumREA((String)bilancioVO.get("numREA"));
//			bilancio.setPartitaIVA((String)bilancioVO.get("partitaIVA"));
//			bilancio.setUbicazione((String)bilancioVO.get("ubicazione"));

			List<DettaglioBilancioAziendaAAEPVO> lista = null;
			
			if ((bilancioVO != null)&&(!bilancioVO.isEmpty())) {
				
				lista = new ArrayList<DettaglioBilancioAziendaAAEPVO>();
				
				for (Map<String, String> dettaglioVO:bilancioVO) { 	
										
					DettaglioBilancioAziendaAAEPVO dettaglio = new DettaglioBilancioAziendaAAEPVO();
					dettaglio.setAnnoAttuale(dettaglioVO.get("annoAttuale"));
					dettaglio.setAnnoPrecedente(dettaglioVO.get("annoPrecedente"));
					dettaglio.setLivelloIndentazione(dettaglioVO.get("livelloIndentazione"));
					dettaglio.setTipoRigo(dettaglioVO.get("tipoRigo"));
					dettaglio.setVoce(dettaglioVO.get("voce"));
	
					lista.add(dettaglio);
				}
			}
			bilancio.setDettaglioBilancio(lista);
			
			SessionCache.getInstance().set("mappaDatiConsLRAAEP", bilancio);

			return bilancio;
		}

	
	// @SuppressWarnings("unchecked")
	public SedeVO getDettaglioSedeFromAAEP(String fonte, String idAzienda, String idSede, Logger logger) throws CommonalityException {			
			
			logger.debug("[AaepWS::getBilancioAziendaFromAAEP] idAzienda=" + idAzienda);
			
			Sede sedeIn = new Sede();
			sedeIn.setIdAzienda(idAzienda);
			sedeIn.setIdSede(idSede);
						
			Sede mappa = null;
			SedeVO sede = null;
			try {
				mappa = getService(logger).getDettaglioSede(null, TipologiaFonte.fromValue(fonte), sedeIn);
			} catch (Exception e) {
				logger.error("[AaepWs::getDettaglioSedeFromAAEP] Error during WS", e);
				throw new CommonalityException(e.getMessage(), e);
			}
			
	
			sede = new SedeVO();
				// popolo un oggetto
			sede.setIdSede(mappa.getIdSede()); //  : per noi e' un progressivo... per AAEP ???
			UbicazioneVO ubicazioneVO = new UbicazioneVO();
			ubicazioneVO.setSiglaProvincia(mappa.getUbicazione().getSiglaProvincia());
			ubicazioneVO.setCodISTATComune(mappa.getUbicazione().getCodISTATComune());
			ubicazioneVO.setDescrComune(mappa.getUbicazione().getDescrComune());

			ubicazioneVO.setIndirizzo(mappa.getUbicazione().getIndirizzo());
			ubicazioneVO.setToponimo(mappa.getUbicazione().getToponimo());
			ubicazioneVO.setNumeroCivico(mappa.getUbicazione().getNumeroCivico());
			ubicazioneVO.setCap(mappa.getUbicazione().getCap());

			sede.setUbicazione(ubicazioneVO);
			
			ContattiVO contattiVO = new ContattiVO();
			contattiVO.setTelefono(mappa.getContatti().getTelefono());
			
			sede.setContatti(contattiVO);
			
			if (mappa.getAteco()!=null && !mappa.getAteco().isEmpty())
			{
				List<AttivitaEconomicaVO> atecoList = new ArrayList<>();
				
				for (AttivitaEconomica attivitaEconomica : mappa.getAteco()) {
					AttivitaEconomicaVO att = new AttivitaEconomicaVO();
					att.setAnnoDiRiferimento(attivitaEconomica.getAnnoDiRiferimento());
					att.setCodiceATECO(attivitaEconomica.getCodiceATECO());
					att.setCodImportanzaAA(attivitaEconomica.getCodImportanzaAA());
					att.setCodImportanzaRI(attivitaEconomica.getCodImportanzaRI());
					att.setDataCessazione(attivitaEconomica.getDataCessazione());
					att.setDataInizio(attivitaEconomica.getDataInizio());
					att.setDescrImportanzaAA(attivitaEconomica.getDescrImportanzaAA());
					att.setDescrImportanzaRI(attivitaEconomica.getDescrImportanzaRI());
					att.setDescrizione(attivitaEconomica.getDescrizione());
					
					atecoList.add(att);
				}
				
				
				sede.setAteco(atecoList);
			}
			
			return sede;
		}
	
	
	

	
}
