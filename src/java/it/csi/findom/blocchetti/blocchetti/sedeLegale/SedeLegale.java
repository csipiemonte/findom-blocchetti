/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.sedeLegale;

import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.util.TrasformaClassiAAEP2VO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.aaep.SedeVO;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.SiglaProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.commonality.ControlEmail;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.findom.findomwebnew.dto.aaep.Sede;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.SedeLegaleDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.VistaUltimaDomandaDto;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.melograno.aggregatore.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class SedeLegale extends Commonality {

	SedeLegaleInput input = new SedeLegaleInput();

	@Override
	public SedeLegaleInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[SedeLegale::inject] BEGIN");
		
		String visNotaPrecompilazioneSedeAAEP = "false";
		String visNotaPrecompilazioneSedeUltimaDomanda = "false";
		

		/** Jira: 1842 - */
		String svuotaCampiResidenzaSL = "false";
		boolean isSiglaProvResidenzaSL1 = false;
		boolean isSiglaProvResidenzaSL2 = false;
		
		try {
			SedeLegaleOutput ns = new SedeLegaleOutput();
			
			List<StatoEsteroVO> statoEsteroSLList = new ArrayList<StatoEsteroVO>();
			List<ProvinciaVO> provinciaSLList = new ArrayList<ProvinciaVO>();
			List<ComuneVO> comuneSLList = new ArrayList<ComuneVO>();
			
			String _sedeLegale_siglaProvincia = "";
			
			SedeLegaleVO sedeLegMap = new SedeLegaleVO(); // dati da visulaizzare nel form
			// fr - 08/01/2019 - serve a stabilire se sono presenti i dati su AAEP
			boolean datiSuAAEP = false;
		
			//// valorizzazione
//			ImpresaVO enteImpresa = (ImpresaVO)SessionCache.getInstance().get("enteImpresa");
			ImpresaVO enteImpresa = (ImpresaVO)TrasformaClassiAAEP2VO.impresa2ImpresaVO((Impresa)SessionCache.getInstance().get("enteImpresa"));

			String operazione = "INS";
			Integer idDomanda = info.getStatusInfo().getNumProposta();
			String sedelegaleDaDomanda = SedeLegaleDAO.getNodoSedeLegale(idDomanda);
			if (sedelegaleDaDomanda != null) operazione = "MOD";	
			
			logger.info("[SedeLegale::inject] operazione=" + operazione);
			
			SedeVO sedeLegaleAziCorrente = null;
			
			String LgRppNonSelezionato = "false";
			
			if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
			
				sedeLegaleAziCorrente = (SedeVO)TrasformaClassiAAEP2VO.sede2SedeVO((Sede)SessionCache.getInstance().get("sedeLegale"));
									
				// Jira: 1807 -FSE 
				if (input._sedeLegale_solo_province_piemonte.equals("true"))
				{
					provinciaSLList =  LuoghiDAO.getProvinciaList(input._sedi_extra_Piemonte, logger);
					
				}else{

					// lista degli stati esteri e delle province
					statoEsteroSLList = LuoghiDAO.getStatoEsteroList(logger);
					provinciaSLList = LuoghiDAO.getProvinciaList(logger);
				}
			    
				SedeLegaleVO _sedeLegaleMap = input.sedeLegaleModel;			   
				
				if(_sedeLegaleMap == null ) {
				
					logger.info("[SedeLegale::inject] _sedeLegaleMap NULL");
					visNotaPrecompilazioneSedeUltimaDomanda = (informazioniUltimaDomandaSedeLegalePresenti(logger) ? "true" : "false");

					//se sede legale nulla, significa che sull'xml non e' mai stato salvata e quindi uso i dati presi da AAEP			
					    	
					if( enteImpresa != null){

						if( sedeLegaleAziCorrente != null){
							
							if( StringUtils.isNotEmpty(sedeLegaleAziCorrente.getIdSede())){

								datiSuAAEP = true;	
								visNotaPrecompilazioneSedeAAEP = "true";
							}
							if(StringUtils.equals("Italia", sedeLegaleAziCorrente.getUbicazione().getNomeNazione())){
								sedeLegMap.setStato("Italia"); // "Italia" o "Estero"
								sedeLegMap.setStatoEstero("");
								sedeLegMap.setStatoEsteroDescrizione(""); 
								sedeLegMap.setCittaEstera("");
							}else{
								sedeLegMap.setStato("Estero"); // "Italia" o "Estero"
								String codSE = LuoghiDAO.getCodiceStatoEsteroByDescrizione(sedeLegaleAziCorrente.getUbicazione().getNomeNazione(), logger);
								sedeLegMap.setStatoEstero(codSE); // Es: 506 
								sedeLegMap.setStatoEsteroDescrizione(sedeLegaleAziCorrente.getUbicazione().getNomeNazione()); // non deve contemplare il caso Italia
								sedeLegMap.setCittaEstera(sedeLegaleAziCorrente.getUbicazione().getDescrComune());
							}
							
							
							// inizializzo
							sedeLegMap.setProvincia("");
							sedeLegMap.setProvinciaDescrizione("");
							for(int i=0; i<provinciaSLList.size();i++){
								// non faccio una query apposita ma ciclo sull'elenco delle province
								ProvinciaVO provincMap = (ProvinciaVO)provinciaSLList.get(i);
								if(provincMap != null && StringUtils.equals(provincMap.getSigla(), sedeLegaleAziCorrente.getUbicazione().getSiglaProvincia())){
									sedeLegMap.setProvincia(provincMap.getCodice());
									sedeLegMap.setProvinciaDescrizione(provincMap.getDescrizione()); 
									logger.info("[SedeLegale::inject] trovata sigla:"+provincMap.getSigla() + " codice="+provincMap.getCodice());

									// carico la lista dei comuni per la provincia data
									comuneSLList = LuoghiDAO.getComuneList(provincMap.getCodice(), logger);
									break;
								}
							}
							sedeLegMap.setProvinciaSigla(sedeLegaleAziCorrente.getUbicazione().getSiglaProvincia());						
							sedeLegMap.setComune(sedeLegaleAziCorrente.getUbicazione().getCodISTATComune());
							sedeLegMap.setComuneDescrizione(sedeLegaleAziCorrente.getUbicazione().getDescrComune());
							
							String indirzz = "";
							if(StringUtils.isBlank(sedeLegaleAziCorrente.getUbicazione().getToponimo())){
								// In AAEP il toponimo non c'e' se la residenza e' in uno Stato estero.
								indirzz = sedeLegaleAziCorrente.getUbicazione().getIndirizzo();
							}else{
								indirzz = sedeLegaleAziCorrente.getUbicazione().getToponimo() + " " +  sedeLegaleAziCorrente.getUbicazione().getIndirizzo();
							}
							logger.info("[SedeLegale::inject] indirzz="+indirzz );
							sedeLegMap.setIndirizzo(indirzz);
							sedeLegMap.setNumCivico(sedeLegaleAziCorrente.getUbicazione().getNumeroCivico());
							sedeLegMap.setCap(sedeLegaleAziCorrente.getUbicazione().getCap());

							sedeLegMap.setTelefono(sedeLegaleAziCorrente.getContatti().getTelefono());
							sedeLegMap.setPec(enteImpresa.getPostaElettronicaCertificata()); 
							
							// spostato dopo aggiornamento dati ultima domanda
							// _sedeLegale_siglaProvincia = sedeLegMap.getProvinciaSigla();
						}
					}
					
					// fr 08/01/2019 BEGIN - aggiorna con informazioni inserite nell'ultima domanda
					String siglaProvinciaPreAggiornamento = sedeLegMap.getProvinciaSigla();
					
					sedeLegMap = aggiornaConInformazioniUltimaDomandaSedeLegale(sedeLegMap, datiSuAAEP, logger);
					
					if (sedeLegMap.getProvinciaSigla() != null) {
						_sedeLegale_siglaProvincia = sedeLegMap.getProvinciaSigla();
						
						// verifico se la provincia assegnata è diversa da quella presente su AAEP
						if (!StringUtils.equals(siglaProvinciaPreAggiornamento, sedeLegMap.getProvinciaSigla())) {
							for(int i=0; i<provinciaSLList.size();i++){
								// non faccio una query apposita ma ciclo sull'elenco delle province
								ProvinciaVO provincMap = (ProvinciaVO)provinciaSLList.get(i);
								if(provincMap != null && StringUtils.equals(provincMap.getSigla(), sedeLegMap.getProvinciaSigla())){
									logger.info("[SedeLegale::inject] trovata sigla:"+provincMap.getSigla() + " codice="+provincMap.getCodice());

									// ricarico la lista dei comuni per la provincia data
									comuneSLList = LuoghiDAO.getComuneList(provincMap.getCodice(), logger);
									break;
								}
							}
						}
					}
					// fr 08/01/2019 END - aggiorna con informazioni inserite nell'ultima domanda
					
					

				} else {
					logger.info("[SedeLegale::inject] _sedeLegaleMap NOT NULL");
					// utilizzo i dati presenti su XML

					if (input._sedeLegale_solo_province_piemonte.equals("true"))
					{
						sedeLegMap.setStato("Italia");
						// sedeLegMap.setStatoEstero(_sedeLegaleMap.getStatoEstero());
						// sedeLegMap.setStatoEsteroDescrizione(_sedeLegaleMap.getStatoEsteroDescrizione());
						// sedeLegMap.setCittaEstera(_sedeLegaleMap.getCittaEstera());
						sedeLegMap.setProvincia(_sedeLegaleMap.getProvincia());
						sedeLegMap.setProvinciaDescrizione(_sedeLegaleMap.getProvinciaDescrizione());
						sedeLegMap.setProvinciaSigla(_sedeLegaleMap.getProvinciaSigla());
						sedeLegMap.setComune(_sedeLegaleMap.getComune());
						sedeLegMap.setComuneDescrizione(_sedeLegaleMap.getComuneDescrizione());
						sedeLegMap.setIndirizzo(_sedeLegaleMap.getIndirizzo());
						sedeLegMap.setNumCivico(_sedeLegaleMap.getNumCivico());
						sedeLegMap.setCap(_sedeLegaleMap.getCap().replaceAll("\\s", ""));
						sedeLegMap.setTelefono(_sedeLegaleMap.getTelefono());
						sedeLegMap.setPec(_sedeLegaleMap.getPec());
						sedeLegMap.setEmail(_sedeLegaleMap.getEmail());
					
					} else {
						sedeLegMap.setStato(_sedeLegaleMap.getStato());
						sedeLegMap.setStatoEstero(_sedeLegaleMap.getStatoEstero());
						sedeLegMap.setStatoEsteroDescrizione(_sedeLegaleMap.getStatoEsteroDescrizione());
						sedeLegMap.setCittaEstera(_sedeLegaleMap.getCittaEstera());
						sedeLegMap.setProvincia(_sedeLegaleMap.getProvincia());
						sedeLegMap.setProvinciaDescrizione(_sedeLegaleMap.getProvinciaDescrizione());
						sedeLegMap.setProvinciaSigla(_sedeLegaleMap.getProvinciaSigla());
						sedeLegMap.setComune(_sedeLegaleMap.getComune());
						sedeLegMap.setComuneDescrizione(_sedeLegaleMap.getComuneDescrizione());
						sedeLegMap.setIndirizzo(_sedeLegaleMap.getIndirizzo());
						sedeLegMap.setNumCivico(_sedeLegaleMap.getNumCivico());
						sedeLegMap.setCap(_sedeLegaleMap.getCap().replaceAll("\\s", ""));
						sedeLegMap.setTelefono(_sedeLegaleMap.getTelefono());
						sedeLegMap.setPec(_sedeLegaleMap.getPec());
						sedeLegMap.setEmail(_sedeLegaleMap.getEmail());
					}
					
					if (input.enteImpresaSLAltriRecapiti.equals("true")) {
						sedeLegMap.setPersonaRifSL(_sedeLegaleMap.getPersonaRifSL());
						sedeLegMap.setCellulare(_sedeLegaleMap.getCellulare());
					}
					//la provincia della sede legale presente eventualmente in request ha la precedenza su quella presente nell'xml
					//perche' sull'onchange delle combo provincia sede legale devo tener conto del valore appena scelto
					
					String provinciaSL = input.sedeLegale_provincia;
					
					if (StringUtils.isBlank(provinciaSL)){
						// se non ho una provincia in request, considero quella dell'xml					    	
						provinciaSL = (String) _sedeLegaleMap.getProvincia();				       
					}			    

					if (StringUtils.isNotBlank(provinciaSL)) {			   
					   comuneSLList = LuoghiDAO.getComuneList(provinciaSL, logger);

					   // ottiene la sigla della provincia di residenza
					   List<SiglaProvinciaVO> siglaProvinciaSLList = LuoghiDAO.getSiglaProvinciaList(provinciaSL, logger);
					   // logger.info("SedeLegale::modelValidate] siglaProvinciaResidenzaList:" + siglaProvinciaResidenzaList);
					   if (siglaProvinciaSLList != null && !siglaProvinciaSLList.isEmpty()) {
						   _sedeLegale_siglaProvincia = (String) ((SiglaProvinciaVO) siglaProvinciaSLList.get(0)).getSigla();	
						   logger.info("[SedeLegale::inject] _sedeLegale_siglaProvincia:" + _sedeLegale_siglaProvincia);
					   }
					} else {
						// altrimenti risettiamo il valore dell'xml
						_sedeLegale_siglaProvincia = sedeLegMap.getProvinciaSigla();
					}
				}
				
				
				LegaleRappresentanteVO _legaleRappresentante = input._legaleRappresentante;
				if(_legaleRappresentante != null && _legaleRappresentante.getPresenzaSoggettoDelegato() != null ){
					logger.info("Sezione LR inizializzata!");
				}else{
					logger.info("Sezione non compilata né inizializzata!");
					LgRppNonSelezionato = "true";
				}
								
			}

			//// namespace

			ns.setSedeLegale_siglaProvincia(_sedeLegale_siglaProvincia);

			/** Jira: 1842 - */
			if ("true".equals(input._sedeLegale_solo_province_piemonte)) 
			{
				if(sedeLegMap != null) {
					
					isSiglaProvResidenzaSL1 = checkProvinciaInPiemonte(sedeLegMap.getProvinciaSigla(), provinciaSLList, logger);
					isSiglaProvResidenzaSL2 = checkProvinciaInPiemonte(_sedeLegale_siglaProvincia, provinciaSLList, logger);
					if( sedeLegMap == null || (!isSiglaProvResidenzaSL1 && !isSiglaProvResidenzaSL2)) {
						comuneSLList.clear();
						svuotaCampiResidenzaSL = "true";
						logger.info("svotaCampiResidenzaSL risulta: " + svuotaCampiResidenzaSL); 
					}
				}
			}
			
			// Jira: 1807 -FSE 
			if ("true".equals(input._sedeLegale_solo_province_piemonte)) 
			{
				ns.setProvinciaSLList(provinciaSLList);
				ns.setComuneSLList(comuneSLList);
				ns.svuotaCampiResidenzaSL = svuotaCampiResidenzaSL;
				
			}else{

				ns.setStatoEsteroSLList(statoEsteroSLList);
				ns.setProvinciaSLList(provinciaSLList);
				ns.setComuneSLList(comuneSLList);
			}
			
			
			ns.setSedeLeg(sedeLegMap);
			ns.setSedeLegaleAziCorrente(sedeLegaleAziCorrente);
			ns.setEnteImpresa(enteImpresa);
			ns.setVisNotaPrecompilazioneSedeAAEP(visNotaPrecompilazioneSedeAAEP);
			ns.setVisNotaPrecompilazioneSedeUltimaDomanda(visNotaPrecompilazioneSedeUltimaDomanda);
			
			logger.info("[SedeLegale::inject] _sedeLegale END");
			
			return ns;
		} catch (Exception ex) {
			throw new CommonalityException(ex);
		} finally {
			logger.info("[SedeLegale::inject] END");
		}
	}

	private boolean checkProvinciaInPiemonte(String provinciaSigla,	List<ProvinciaVO> provinciaSLList, Logger logger) {
		boolean ris = false;
		
		Map<Object, Object> sigleProvPiemontesiMap = provinciaSLList.stream().collect( Collectors.toMap(ProvinciaVO::getCodice, ProvinciaVO::getSigla));
		String siglaProvRes = provinciaSigla;
		logger.info("siglaProvRes :" + siglaProvRes);
		
		if(siglaProvRes != null && !siglaProvRes.isEmpty()) {
			ris = sigleProvPiemontesiMap.values().stream().anyMatch(siglaProv -> siglaProv.equals(siglaProvRes));
			logger.info(ris);
		}
		logger.info("ris :" + ris);
		return ris;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo infol, List<CommonalityMessage> inputMessages) {
		//		<#-- note di dipendenze da altre commonalities: nessuna -->
		//// validazione panel Sede Legale

		FinCommonInfo info = (FinCommonInfo) infol;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();

		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[SedeLegale::modelValidate] _riferimenti BEGIN");

		String ERRMSG_CAMPO_OBBLIGATORIO_SL = "- il campo é obbligatorio";
		String ERRMSG_SL_PEC_NOTCORRECT= "- L'indirizzo PEC é formalmente non corretto";
		String ERRMSG_SL_EMAIL_NOTCORRECT= "- L'indirizzo EMAIL é formalmente non corretto";
		String ERRMSG_ALMENO_UNO_TEL_CELL_SL = "- é obbligatorio indicare almeno uno tra il telefono e il cellulare";
		String ERRMSG_ALMENO_UNO_EMAIL_PEC_SL = "- Compilare almeno uno tra mail e PEC";
		
		try {
			SedeLegaleVO _sedeLegale = input.sedeLegaleModel;
			if (_sedeLegale != null) {
				logger.info("SedeLegale::modelValidate] _sedeLegale:" + _sedeLegale);
		
				// radio stato italia/estero
				String stato = _sedeLegale.stato;
				logger.info("SedeLegale::modelValidate] stato:" + stato);
				
				if (input._sedeLegale_solo_province_piemonte.equals("true"))
				{
					stato = "Italia";
					logger.info("SedeLegale::modelValidate] stato:" + stato);
					
					if (stato.equals("Italia")) {
						// Provincia
						String provinciaSL = (String) _sedeLegale.getProvincia();
						if (StringUtils.isBlank(provinciaSL)) {
							addMessage(newMessages,"_sedeLegale_stato_provincia", "- il campo Provincia é obbligatorio.");
							logger.warn("SedeLegale::modelValidate] provincia sede legale non valorizzato");
						} else {
							logger.info("SedeLegale::modelValidate] provinciaSL:" + provinciaSL);
						}
		
						// Comune
						String comuneSL = (String) _sedeLegale.getComune();
						if (StringUtils.isBlank(comuneSL)) {
							addMessage(newMessages,"_sedeLegale_stato_comune", "- il campo Comune é obbligatorio.");
							logger.warn("SedeLegale::modelValidate] comune sede legale non valorizzato");
						} else {
							logger.info("SedeLegale::modelValidate] comuneSL:" + comuneSL);
						}
					}

				} else {
						
						if (StringUtils.isBlank(stato)) {
							addMessage(newMessages,"_sedeLegale_noStato", "- il campo é obbligatorio<br /> - selezionare o Italia o Stato Estero");
							logger.warn("SedeLegale::modelValidate] stato non valorizzato");
						} else {
							logger.info("SedeLegale::modelValidate] stato:" + stato);
							if (stato.equals("Italia")) {
								// Provincia
								String provinciaSL = (String) _sedeLegale.getProvincia();
								if (StringUtils.isBlank(provinciaSL)) {
									addMessage(newMessages,"_sedeLegale_stato", "- il campo Provincia é obbligatorio se valorizzata l'opzione stato Italia");
									logger.warn("SedeLegale::modelValidate] provincia sede legale non valorizzato");
								} else {
									logger.info("SedeLegale::modelValidate] provinciaSL:" + provinciaSL);
								}
				
								// Comune
								String comuneSL = (String) _sedeLegale.getComune();
								if (StringUtils.isBlank(comuneSL)) {
									addMessage(newMessages,"_sedeLegale_stato", "- il campo Comune é obbligatorio se valorizzata l'opzione stato Italia");
									logger.warn("SedeLegale::modelValidate] comune sede legale non valorizzato");
								} else {
									logger.info("SedeLegale::modelValidate] comuneSL:" + comuneSL);
								}
							}
				
							if (stato.equals("Estero")) {
								// Stato Estero
								String statoEsteroSL = (String) _sedeLegale.getStatoEstero();
								if (StringUtils.isBlank(statoEsteroSL)) {
									addMessage(newMessages,"_sedeLegale_stato", "- il campo Stato Estero é obbligatorio se valorizzata l'opzione corrispondente");
									logger.warn("SedeLegale::modelValidate] stato estero sede legale non valorizzato");
								} else {
									logger.info("SedeLegale::modelValidate] statoEsteroSL:" + statoEsteroSL);
								}
				
								// Cittaa' estera
								String cittaEstera = (String) _sedeLegale.getCittaEstera();
								if (StringUtils.isBlank(cittaEstera)) {
									addMessage(newMessages,"_sedeLegale_cittaEstera", ERRMSG_CAMPO_OBBLIGATORIO_SL);
									logger.warn("SedeLegale::modelValidate] cittaEstera non valorizzato");
								} else {
									logger.info("SedeLegale::modelValidate] cittaEstera:" + cittaEstera);
								}
							}
						}

				}
				
		
				// Indirizzo
				String indirizzo = (String) _sedeLegale.getIndirizzo();
				if (StringUtils.isBlank(indirizzo)) {
				   addMessage(newMessages,"_sedeLegale_indirizzo", ERRMSG_CAMPO_OBBLIGATORIO_SL);
				   logger.warn("SedeLegale::modelValidate] indirizzo non valorizzato");
				} else {
				  logger.info("SedeLegale::modelValidate] indirizzo:" + indirizzo);
				}
		
				// numero civico
				String numCivico = (String) _sedeLegale.getNumCivico();
				if (StringUtils.isBlank(numCivico)) {
				   addMessage(newMessages,"_sedeLegale_numcivico", ERRMSG_CAMPO_OBBLIGATORIO_SL);
				   logger.warn("SedeLegale::modelValidate] numCivico non valorizzato");
				} else {
				  logger.info("SedeLegale::modelValidate] numCivico:" + numCivico);
				}
		
				// cap
				String cap = (String) _sedeLegale.getCap().replaceAll("\\s", "");
				if (StringUtils.isBlank(cap)) {
				   addMessage(newMessages,"_sedeLegale_cap", ERRMSG_CAMPO_OBBLIGATORIO_SL);
				   logger.warn("SedeLegale::modelValidate] cap non valorizzato");
				} else {
				  logger.info("SedeLegale::modelValidate] cap:" + cap);
				}
		
		    if (input.enteImpresaSLAltriRecapiti.equals("false")){
	
		    	// telefono
				String telefonoSL = (String) _sedeLegale.getTelefono();
				if (StringUtils.isBlank(telefonoSL)) {
				   addMessage(newMessages,"_sedeLegale_telefono", ERRMSG_CAMPO_OBBLIGATORIO_SL);
				   logger.warn("SedeLegale::modelValidate] telefono non valorizzato");
				} else {
				  logger.info("SedeLegale::modelValidate] telefono:" + telefonoSL);
				}
		
				
				if (input._sedeLegale_emailOrPecObbligatorio.equals("true"))
				{
					boolean isPecOrEmailCompiled = false;
					
					// verifico compilazione pec
					if (input._ente_impresa_SL_altri_recapiti_senza_pec.equals("false"))
					{
						String indirizzoPec = (String) _sedeLegale.getPec();
						
						if (StringUtils.isBlank(indirizzoPec) && !isBeneficiarioAziendaEstera(info)) {
							logger.warn("SedeLegale::modelValidate] indirizzo almeno uno pec o email valorizzato");
						}else{
							if(StringUtils.isNotBlank(indirizzoPec) && !ControlEmail.ctrlFormatoIndirizzoEmail(indirizzoPec)){
								addMessage(newMessages,"_sedeLegale_pec", ERRMSG_SL_PEC_NOTCORRECT);						
								logger.warn("[SedeLegale::modelValidate]  indirizzo PEC formalmente non corretto");
							}else{				
								logger.info("[SedeLegale::modelValidate]  indirizzo PEC:" + indirizzoPec);
								isPecOrEmailCompiled=true;
							}
						}
					}else{
						logger.info("[SedeLegale::modelValidate]  indirizzo PEC non presente da configurazione bando...");
					}
					
					//email
					String emailSL = (String) _sedeLegale.getEmail();
					
					if (!StringUtils.isBlank(emailSL)) {
						if(!ControlEmail.ctrlFormatoIndirizzoEmail(emailSL)){
							addMessage(newMessages,"_sedeLegale_email", ERRMSG_SL_EMAIL_NOTCORRECT);
							
							logger.warn("[SedeLegale::modelValidate]   indirizzo EMAIL formalmente non corretto");
						}else{				
							logger.info("[SedeLegale::modelValidate]   indirizzo EMAIL:" + emailSL);
							isPecOrEmailCompiled=true;
						}
					}
					
					// verifico compilazione email or pec
					if(!isPecOrEmailCompiled){
						// errore nessun campo dei 2 compilato 
						addMessage(newMessages,"_sedeLegale_pec", ERRMSG_ALMENO_UNO_EMAIL_PEC_SL);
						addMessage(newMessages,"_sedeLegale_email", ERRMSG_ALMENO_UNO_EMAIL_PEC_SL);
					}
					
				}else{
					
					//indirizzo pec
					// campo non obbligatorio se beneficiario e' un'azienda estera
					if (input._ente_impresa_SL_altri_recapiti_senza_pec.equals("false"))
					{
						String indirizzoPec = (String) _sedeLegale.getPec();
						
						if (StringUtils.isBlank(indirizzoPec) && !isBeneficiarioAziendaEstera(info)) {
							addMessage(newMessages,"_sedeLegale_pec", ERRMSG_CAMPO_OBBLIGATORIO_SL);
							logger.warn("SedeLegale::modelValidate] indirizzo PEC non valorizzato");
						}else{
							if(StringUtils.isNotBlank(indirizzoPec) && !ControlEmail.ctrlFormatoIndirizzoEmail(indirizzoPec)){
								addMessage(newMessages,"_sedeLegale_pec", ERRMSG_SL_PEC_NOTCORRECT);						
								logger.warn("[SedeLegale::modelValidate]  indirizzo PEC formalmente non corretto");
							}else{				
								logger.info("[SedeLegale::modelValidate]  indirizzo PEC:" + indirizzoPec);
							}
						}
					}else{
						logger.info("[SedeLegale::modelValidate]  indirizzo PEC non presente da configurazione bando...");
					}
					
					//email
					String emailSL = (String) _sedeLegale.getEmail();
					if (!StringUtils.isBlank(emailSL)) {
						if(!ControlEmail.ctrlFormatoIndirizzoEmail(emailSL)){
							addMessage(newMessages,"_sedeLegale_email", ERRMSG_SL_EMAIL_NOTCORRECT);
							
							logger.warn("[SedeLegale::modelValidate]   indirizzo EMAIL formalmente non corretto");
						}else{				
							logger.info("[SedeLegale::modelValidate]   indirizzo EMAIL:" + emailSL);
						}
					}
				}
			  	
		    } else {
			  //persona riferimento obbligatoria
			  String personaRifSL = (String) _sedeLegale.getPersonaRifSL();
			  if (StringUtils.isBlank(personaRifSL)) {
				 addMessage(newMessages,"_sedeLegale_persona_rif", ERRMSG_CAMPO_OBBLIGATORIO_SL);
				 logger.warn("SedeLegale::modelValidate] persona riferimento non valorizzato");
			  } else {
				 logger.info("SedeLegale::modelValidate] persona riferimento:" + personaRifSL);
			  }
			  
			  String telefonoSL = (String) _sedeLegale.getTelefono();
			  String cellulareSL = (String) _sedeLegale.getCellulare();
			  
			  if (StringUtils.isBlank(telefonoSL) && StringUtils.isBlank(cellulareSL)) {
			     addMessage(newMessages,"_sedeLegale_telefono", ERRMSG_ALMENO_UNO_TEL_CELL_SL);
			     addMessage(newMessages,"_sedeLegale_cellulare", ERRMSG_ALMENO_UNO_TEL_CELL_SL);
				 logger.warn("SedeLegale::modelValidate] telefono e cellulare entrambi non valorizzati");
			  } else {
				 logger.info("SedeLegale::modelValidate] telefono : " + telefonoSL + "cellulare : " + cellulareSL);
			  }
			  
			  String emailSL = (String) _sedeLegale.getEmail();
			  
			  //MB2018_06_21 ini jira 914
//			  if (StringUtils.isBlank(emailSL)) {
//				 addMessage(newMessages,"_sedeLegale_email", ERRMSG_CAMPO_OBBLIGATORIO_SL);
//				 logger.warn("SedeLegale::modelValidate] email non valorizzato");
//			  } else {
//				 logger.info("SedeLegale::modelValidate] email:" + emailSL);
			  //			  }
			  if(!ControlEmail.ctrlFormatoIndirizzoEmail(emailSL)){
				  addMessage(newMessages,"_sedeLegale_email", ERRMSG_SL_EMAIL_NOTCORRECT);					
				  logger.warn("[SedeLegale::modelValidate] indirizzo EMAIL altri recapiti formalmente non corretto");
			  }else{				
				  logger.info("[SedeLegale::modelValidate] indirizzo EMAIL altri recapiti :" + emailSL);
			  }
			  //MB2018_06_21 fine
		
		    }					
		
			} else {
				logger.warn("SedeLegale::modelValidate] _sedeLegale non presente o vuoto");
			}
			
		} catch(Exception ex) {
			logger.error("[SedeLegale::modelValidate] ", ex);
		}
		finally {
			logger.info("SedeLegale::modelValidate] _sedeLegale END");
		}
		
		
		
		return newMessages;
	}

	/**
	 * Restituisce TRUE se il beneficiario loggato non e' italiano
	 * ( ossia se shell_t_soggetti.sigla_nazione != null AND !='000' =
	 * @param info
	 * @return
	 */
	private boolean isBeneficiarioAziendaEstera(FinCommonInfo info) {
		boolean ret = false;
		String sn = info.getStatusInfo().getSiglaNazione();
		if (StringUtils.isNotBlank(sn) && !StringUtils.equals(sn,"000")){
			ret = true;
		}
		return ret;
	}

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
		// nothing to validate
		return null;
	}
	
	private SedeLegaleVO aggiornaConInformazioniUltimaDomandaSedeLegale(SedeLegaleVO sedeLegMap, boolean datiSuAAEP, Logger logger) {
		// utilizzo i dati dell'ultima domanda inserita
		String prf = "[SedeLegale::aggiornaConInformazioniUltimaDomandaSedeLegale] ";
		
		VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		logger.debug(prf + "datiUltimoBeneficiario in sessione="+datiUltimoBeneficiario);
		
		if (datiUltimoBeneficiario != null && datiUltimoBeneficiario.getSedeLegale()!=null) {
			
			logger.debug(prf + "datiUltimoBeneficiario in sessione, operatorePresentatore.cf="+datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale());
			SedeLegaleDto ultimaSedeLegale = datiUltimoBeneficiario.getSedeLegale();
			
			sedeLegMap.setPec(Utils.isEmpty(sedeLegMap.getPec()) ? ultimaSedeLegale.getPec() : sedeLegMap.getPec());
			
			if (datiSuAAEP) {
				
				if (Utils.isEmpty(sedeLegMap.getStato())) {
					// manca la sede legale
					sedeLegMap = aggiornaInformazioniSedeLegale(sedeLegMap, ultimaSedeLegale);
				} else {
					if (StringUtils.equals(ultimaSedeLegale.getStato(), sedeLegMap.getStato())) {

						if(StringUtils.equals("Italia", sedeLegMap.getStato())){
							// Italia
							if (StringUtils.equals(ultimaSedeLegale.getStato(), sedeLegMap.getStato())) {
								if (Utils.isEmpty(sedeLegMap.getComune()) || Utils.isEmpty(sedeLegMap.getProvincia())) {
									// manca il comune/provincia
									sedeLegMap = aggiornaInformazioniComune(sedeLegMap, ultimaSedeLegale);							
								} else {
									if ((Utils.isEmpty(sedeLegMap.getIndirizzo()) || Utils.isEmpty(sedeLegMap.getCap()) 
											|| Utils.isEmpty(sedeLegMap.getNumCivico())) &&
										sedeLegMap.getComune().equals(ultimaSedeLegale.getComune())) {
										// manca l'indirizzo ma è lo stesso comune di aaep
										sedeLegMap = aggiornaInformazioniIndirizzo(sedeLegMap, ultimaSedeLegale);
									}							
								}
							}
						} else {
							// Estero
							sedeLegMap = aggiornaInformazioniEstero(sedeLegMap, ultimaSedeLegale);						
						}
					}
				}
			} else {

				sedeLegMap = aggiornaInformazioniSedeLegale(sedeLegMap, ultimaSedeLegale);
						
			}
			
		}
		return sedeLegMap;
	}

	private SedeLegaleVO aggiornaInformazioniSedeLegale(SedeLegaleVO sedeLegMap, SedeLegaleDto ultimaSedeLegale) {
		// utilizzo i dati dell'ultima domanda inserita

		sedeLegMap.setStato(ultimaSedeLegale.getStato());		
		if(StringUtils.equals("Italia", ultimaSedeLegale.getStato())){
			sedeLegMap = aggiornaInformazioniComune(sedeLegMap, ultimaSedeLegale);
		} else {
			sedeLegMap = aggiornaInformazioniEstero(sedeLegMap, ultimaSedeLegale);
		}	
		return sedeLegMap;
	}
	
	private SedeLegaleVO aggiornaInformazioniComune(SedeLegaleVO sedeLegMap, SedeLegaleDto ultimaSedeLegale) {
		// utilizzo i dati dell'ultima domanda inserita

		sedeLegMap.setComune(ultimaSedeLegale.getComune());
		sedeLegMap.setComuneDescrizione(ultimaSedeLegale.getComuneDescrizione());			
		sedeLegMap.setProvincia(ultimaSedeLegale.getProvincia());
		sedeLegMap.setProvinciaDescrizione(ultimaSedeLegale.getProvinciaDescrizione());
		sedeLegMap.setProvinciaSigla(ultimaSedeLegale.getProvinciaSigla());					
		sedeLegMap.setStatoEstero("");
		sedeLegMap.setStatoEsteroDescrizione(""); 
		sedeLegMap.setCittaEstera("");				
		sedeLegMap = aggiornaInformazioniIndirizzo(sedeLegMap, ultimaSedeLegale);

		return sedeLegMap;
	}

	private SedeLegaleVO aggiornaInformazioniEstero(SedeLegaleVO sedeLegMap, SedeLegaleDto ultimaSedeLegale) {
		// utilizzo i dati dell'ultima domanda inserita

		sedeLegMap.setCittaEstera(ultimaSedeLegale.getCittaEstera());
		sedeLegMap.setStatoEstero(ultimaSedeLegale.getStatoEstero());
		sedeLegMap.setStatoEsteroDescrizione(ultimaSedeLegale.getStatoEsteroDescrizione());					
		sedeLegMap.setComune("");
		sedeLegMap.setComuneDescrizione("");			
		sedeLegMap.setProvincia("");
		sedeLegMap.setProvinciaDescrizione("");
		sedeLegMap.setProvinciaSigla("");	
		
		sedeLegMap = aggiornaInformazioniIndirizzo(sedeLegMap, ultimaSedeLegale);
		
		return sedeLegMap;
	}

	private SedeLegaleVO aggiornaInformazioniIndirizzo(SedeLegaleVO sedeLegMap, SedeLegaleDto ultimaSedeLegale) {
		// utilizzo i dati dell'ultima domanda inserita

		sedeLegMap.setIndirizzo(ultimaSedeLegale.getIndirizzo());
		sedeLegMap.setNumCivico(ultimaSedeLegale.getNumCivico());
		sedeLegMap.setCap(ultimaSedeLegale.getCap());
								
		return sedeLegMap;
	}
	
	private boolean informazioniUltimaDomandaSedeLegalePresenti(Logger logger) {
		// utilizzo i dati dell'ultima domanda inserita
		
		String prf = "[SedeLegale::informazioniUltimaDomandaSedeLegalePresenti] ";
		
		VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		logger.debug(prf + "datiUltimoBeneficiario in sessione="+datiUltimoBeneficiario);
		
		if (datiUltimoBeneficiario != null && 
			datiUltimoBeneficiario.getOperatorePresentatore()!=null &&
			datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale()!=null) {
			
			logger.debug(prf + "datiUltimoBeneficiario OperatorePresentatore.CF="+datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale());
			return true;
		}
		return false;
	}

}
