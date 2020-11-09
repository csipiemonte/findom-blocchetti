/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.operatorePresentatoreSemplificato;

import it.csi.findom.blocchetti.common.dao.AtecoDAO;
import it.csi.findom.blocchetti.common.dao.AttivitaDAO;
import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.common.dao.FormaGiuridicaDAO;
import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.dao.OperatorePresentatoreDAO;
import it.csi.findom.blocchetti.common.dao.SoggettoDAO;
import it.csi.findom.blocchetti.common.util.TrasformaClassiAAEP2VO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.aaep.SedeVO;
import it.csi.findom.blocchetti.common.vo.ateco.AtecoVo;
import it.csi.findom.blocchetti.common.vo.attivita.AttivitaVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiTipolBeneficiarioVo;
import it.csi.findom.blocchetti.common.vo.formaGiuridica.FormaGiuridicaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.soggetto.SoggettoVO;
import it.csi.findom.blocchetti.commonality.Constants;
import it.csi.findom.blocchetti.commonality.ControlEmail;
import it.csi.findom.blocchetti.commonality.ControlPartitaIVA;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.findom.findomwebnew.dto.aaep.Sede;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.OperatorePresentatoreDto;
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
import it.csi.melograno.aggregatore.util.Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;
	
public class OperatorePresentatoreSemplificato extends Commonality {
	
	OperatorePresentatoreSemplificatoInput input = new OperatorePresentatoreSemplificatoInput();

	@Override
	public OperatorePresentatoreSemplificatoInput getInput() throws CommonalityException {
		return input;
	}   
	
	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		String prf = "[OperatorePresentatoreSemplificato::inject] ";
	    logger.info(prf + " BEGIN");
	    
	    FinStatusInfo finStatusInfo = new FinStatusInfo();
	    
    	String visNotaPrecompilazioneAAEP = "false";
		String visNotaPrecompilazioneUltimaDomanda = "false";
		String beneficiarioEstero = "";//gestione beneficiario estero  
		String statoEsteroRO = "false";//gestione beneficiario estero  
        String unitaOrganizzativaRO = "false";
        
    	String visIpa = "false";  //false se il codice Ipa non deve essere visualizzato
    	
    	/** : Jira: x sostegno imprese  - */
		String codTipoBeneficiario = "";
		
	    try {			
		    OperatorePresentatoreSemplificatoOutput ns = new OperatorePresentatoreSemplificatoOutput();	
			SedeVO sedeLegaleAziCorrente = (SedeVO)TrasformaClassiAAEP2VO.sede2SedeVO((Sede)SessionCache.getInstance().get("sedeLegale"));

			if(sedeLegaleAziCorrente == null){
				sedeLegaleAziCorrente = new SedeVO();
			}

			logger.info(prf + " sedeLegaleAziCorrente=" + sedeLegaleAziCorrente);
			List <FormaGiuridicaVO> datiFormaGiuridicaList = new ArrayList<FormaGiuridicaVO>();
			
			List<AttivitaVO> settoreAttivitaEconomicaList = new ArrayList<AttivitaVO>();
			
			//gestione beneficiario estero inizio 			
			List<StatoEsteroVO> statoEsteroList = new ArrayList<>(); 
			String idStatoUltimoSalvato = getUltimoStatoSalvato(info);  //qui serve solo distinguere tra Italia e diverso da Italia 
			beneficiarioEstero = (StringUtils.isNotBlank(idStatoUltimoSalvato) && !idStatoUltimoSalvato.equals(Constants.ID_STATO_ITALIA)) ? "true" : "";			
			//gestione beneficiario estero fine 
			
			OperatorePresentatoreVo operPresent = new OperatorePresentatoreVo();

			// campi per salvare i dati originari provenienti da AAEP
			String codPrevalenteAtecoAAEP = "";
			String descrizioneAtecoAAEP = "";
			String idAtecoAAEP = "";
			
			// fr - 08/01/2019 - serve a stabilire se sono presenti i dati su AAEP
			boolean datiSuAAEP = false;	
			ImpresaVO enteImpresa = (ImpresaVO)TrasformaClassiAAEP2VO.impresa2ImpresaVO((Impresa)SessionCache.getInstance().get("enteImpresa"));

			// valorizzazione
			if(enteImpresa!=null){
				// enteImpresa in sessione arriva da AAEP
			    logger.info(prf + " enteImpresa idAzienda="+enteImpresa.getIdAzienda());	
				
			}else{
				logger.info(prf + " enteImpresa non in sessione");
				enteImpresa = new ImpresaVO();
			}

	        OperatorePresentatoreVo _operatorePresentatore = input.operatorePresentatore; // : cf e descrImpresaEnte presenti
	       	        
			if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
				
				//aggiornamento xml con id unita organizzativa (e'il caso in cui la uo sia stata presa da IPA e sia stata salvata, in quanto 
				//non ancora presente, sulle tabelle del db di findom relative alle unita organizzative, per cui l'id  e' noto solo ora 
				//e lo uso per completare il nodo operatore presentatore sull'xml
				if(StringUtils.isNotBlank((String) SessionCache.getInstance().get("idUnitaOrganizzativaIns"))){
					String idUnitaOrganizzativaIns = (String) SessionCache.getInstance().get("idUnitaOrganizzativaIns");
					SessionCache.getInstance().set("idUnitaOrganizzativaIns", null); 
					try {
						aggiornaIdUOInXml(info,logger, idUnitaOrganizzativaIns);
					} catch (AggregatoreException e) {
						logger.error("[Partner::inject] si e' verificato un errore AggregatoreException: ", e);
						throw new CommonalityException(e);
					} catch (Throwable e) {
						logger.error("[Partner::inject] si e' verificato un errore Throwable: ", e);
						throw new CommonalityException(e);
					}
				}
				
				//se operatore presentatore nullo, significa che sull'xml non e' mai stato salvato e quindi uso i dati presi da AAEP
				if(_operatorePresentatore == null ) {
					
					visNotaPrecompilazioneUltimaDomanda = (informazioniUltimaDomandaPresenti() ? "true" : "false");

					if( enteImpresa != null ){
				
						if (enteImpresa.getCodiceFiscale()!=null && StringUtils.isNotEmpty(enteImpresa.getCodiceFiscale())){
							datiSuAAEP = true;	
							visNotaPrecompilazioneAAEP = "true";
						}
						logger.info(prf + " enteImpresa NOT NULL");
				
						//operPresent.put("codiceFiscale",enteImpresa.getCodiceFiscale());
						operPresent.setCodiceFiscale(info.getStatusInfo().getCodFiscaleBeneficiario());
						logger.info("debug - info.getStatusInfo().getCodFiscaleBeneficiario vale: " + info.getStatusInfo().getCodFiscaleBeneficiario());
						
						operPresent.setDenominazione(enteImpresa.getRagioneSociale());
						logger.info("debug - enteImpresa.getRagioneSociale vale: " + enteImpresa.getRagioneSociale());
						// Rinnovo la sessione riferita alla denominazione Jira: 1195 - inizio
						info.getStatusInfo().setDescrImpresaEnte(enteImpresa.getRagioneSociale());
						finStatusInfo.setDescrImpresaEnte(info.getStatusInfo().getDescrImpresaEnte());
						// Rinnovo la sessione riferita alla denominazione Jira: 1195 - fine
						
						operPresent.setDescrizioneFormaGiuridica(enteImpresa.getDescrNaturaGiuridica());
						operPresent.setCodiceFormaGiuridica(enteImpresa.getCodNaturaGiuridica());
						operPresent.setPartitaIva(enteImpresa.getPartitaIva());
						operPresent.setIndirizzoPec(enteImpresa.getPostaElettronicaCertificata());
						operPresent.setIdFormaGiuridica(enteImpresa.getIdNaturaGiuridica()); //  da capire....
						
						// AA - INIZIO
						// se enteImpresa e' diverso da NULL ma non c'e' la ragione sociale
						// probabilmente il servizio di AAEP non ha risposto..
						// se il soggetto e' presente sulla shell_t_soggetti recuperiamo la ragione sociale
						// e la forma giuridica in modo che possano apparire a video
						if (StringUtils.isBlank(enteImpresa.getRagioneSociale())){					
							List<SoggettoVO> listaSoggetti = SoggettoDAO.getSoggettoByCodFiscale(info.getStatusInfo().getCodFiscaleBeneficiario());
							if (listaSoggetti!=null && !listaSoggetti.isEmpty()){

								SoggettoVO soggetto = (SoggettoVO)listaSoggetti.get(0);
								logger.info(prf + " soggetto="+soggetto.toString());

								if (soggetto!=null){

									operPresent.setDenominazione(soggetto.getDenominazione());
									// : Rinnovo la sessione riferita alla denominazione Jira: 1195
									info.getStatusInfo().setDescrImpresaEnte(soggetto.getDenominazione());

									operPresent.setIdFormaGiuridica(soggetto.getIdformagiuridica());
									// logger.info("soggetto.getIdformagiuridica= "+soggetto.getIdformagiuridica());									
								}}
						}
						// AA - FINE

						//  : questo codice e' nella forma senza punti. Es 6210 mentre noi abbiamo 62.1.0
						operPresent.setCodicePrevalenteAteco2007((sedeLegaleAziCorrente.getCodiceAteco2007()== null) ? "" : sedeLegaleAziCorrente.getCodiceAteco2007()); 
						operPresent.setDescrizioneAteco2007((sedeLegaleAziCorrente.getDescrizioneAteco2007()== null) ? "" : sedeLegaleAziCorrente.getDescrizioneAteco2007());
						operPresent.setIdAteco2007("");

						// AA - INIZIO
						// Recuperiamo dal db il codice Ateco da mostrare sulla videata
						// quello restituito da AAEP non ha i punti												
						AtecoVo atecoDBVo = AtecoDAO.getAtecoVoByCodiceNorm((sedeLegaleAziCorrente.getCodiceAteco2007()== null) ? "" : sedeLegaleAziCorrente.getCodiceAteco2007());

						operPresent.setCodicePrevalenteAteco2007(atecoDBVo.getCodice());
						// AA - FINE 

						codPrevalenteAtecoAAEP = (sedeLegaleAziCorrente.getCodiceAteco2007()== null) ? "" : sedeLegaleAziCorrente.getCodiceAteco2007();
						descrizioneAtecoAAEP = (sedeLegaleAziCorrente.getDescrizioneAteco2007()== null) ? "" : sedeLegaleAziCorrente.getDescrizioneAteco2007();
						idAtecoAAEP = "";

						// questi nodi non esistono su AAEP
						operPresent.setIdAttivitaEconomica("");
						operPresent.setCodiceAttivitaEconomica("");
						operPresent.setDescrizioneAttivitaEconomica("");
						
						/**Jira: 1974 - */
						if ("true".equals(input._operatorePresentatore_cir))
					    {
							operPresent.setCodiceRegionale("");
					    }
						
						/**Jira: 2005 - */
						if ("true".equals(input._operatorePresentatore_codiceAps))
					    {
							operPresent.setNumeroIscrizioneRegistroAPS("");
							operPresent.setDataIscrizioneRegistroAPS("");
					    }
						
						
					}
					//gestione beneficiario estero inizio 
					operPresent.setIdStato(info.getStatusInfo().getSiglaNazione());
					String descrStato = LuoghiDAO.getDescrStatoByCodStato(info.getStatusInfo().getSiglaNazione(), logger);
					operPresent.setDescrStato(Objects.toString(descrStato, ""));
					//gestione beneficiario estero fine 
					
					// fr 08/01/2019 - aggiorna con informazioni inserite nell'ultima domanda
					operPresent = aggiornaConInformazioniUltimaDomandaOperatorePresentatore(operPresent, datiSuAAEP, beneficiarioEstero,logger);				

				} else {
					logger.info(prf + " _operatorePresentatore NOT NULL");

					operPresent.setIdDipartimento(_operatorePresentatore.getIdDipartimento());
					logger.info("[OperatorePresentatore:: da xml] - _operatorePresentatore.getIdDipartimento vale: " + _operatorePresentatore.getIdDipartimento());
					
					//gestione beneficiario estero inizio 
					operPresent.setIdStato(_operatorePresentatore.getIdStato());
					operPresent.setDescrStato(_operatorePresentatore.getDescrStato());
					//gestione beneficiario estero fine 

					// travaso i dati da _operatorePresentatore a operPresent
					operPresent.setCodiceFiscale(_operatorePresentatore.getCodiceFiscale());
					logger.info("[OperatorePresentatore:: da xml] row: 184 - _operatorePresentatore.getCodiceFiscale vale: " + _operatorePresentatore.getCodiceFiscale());

					operPresent.setDenominazione(_operatorePresentatore.getDenominazione());
					// : Rinnovo la sessione riferita alla denominazione Jira: 1195
					info.getStatusInfo().setDescrImpresaEnte(_operatorePresentatore.getDenominazione());
					logger.info("[OperatorePresentatore:: da xml] row: 184 - _operatorePresentatore NOT NULL vale: " + _operatorePresentatore.getDenominazione());

					operPresent.setDescrizioneFormaGiuridica(_operatorePresentatore.getDescrizioneFormaGiuridica());
					logger.info("DescrizioneFormaGiuridica: " + _operatorePresentatore.getDescrizioneFormaGiuridica());

					operPresent.setIdFormaGiuridica(_operatorePresentatore.getIdFormaGiuridica());
					operPresent.setCodiceFormaGiuridica(_operatorePresentatore.getCodiceFormaGiuridica());
					operPresent.setPartitaIva(_operatorePresentatore.getPartitaIva());
					operPresent.setIndirizzoPec(_operatorePresentatore.getIndirizzoPec());
					operPresent.setCodicePrevalenteAteco2007(_operatorePresentatore.getCodicePrevalenteAteco2007());
					operPresent.setDescrizioneAteco2007(_operatorePresentatore.getDescrizioneAteco2007());
					operPresent.setIdAteco2007(_operatorePresentatore.getIdAteco2007());
					operPresent.setIdAttivitaEconomica(_operatorePresentatore.getIdAttivitaEconomica());
					operPresent.setCodiceAttivitaEconomica(_operatorePresentatore.getCodiceAttivitaEconomica());
					operPresent.setDescrizioneAttivitaEconomica(_operatorePresentatore.getDescrizioneAttivitaEconomica());
					
					operPresent.setIdDipartimento(_operatorePresentatore.getIdDipartimento());
					operPresent.setCodiceDipartimento(_operatorePresentatore.getCodiceDipartimento());
					operPresent.setDescrizioneDipartimento(_operatorePresentatore.getDescrizioneDipartimento());
					
					/**Jira: 1974 - */
					if ("true".equals(input._operatorePresentatore_cir))
				    {
						operPresent.setCodiceRegionale(_operatorePresentatore.getCodiceRegionale());
				    }
					
					/**Jira: 2005 - */
					if ("true".equals(input._operatorePresentatore_codiceAps))
				    {
						operPresent.setNumeroIscrizioneRegistroAPS(_operatorePresentatore.getNumeroIscrizioneRegistroAPS());
						operPresent.setDataIscrizioneRegistroAPS(_operatorePresentatore.getDataIscrizioneRegistroAPS());
				    }
					
					
					// carico i dati di AAEP che non sono presenti in enteImpresa
					codPrevalenteAtecoAAEP = (sedeLegaleAziCorrente.getCodiceAteco2007()== null) ? "" : sedeLegaleAziCorrente.getCodiceAteco2007();
					descrizioneAtecoAAEP = (sedeLegaleAziCorrente.getDescrizioneAteco2007()== null) ? "" : sedeLegaleAziCorrente.getDescrizioneAteco2007();
				    idAtecoAAEP = "";
				}

				//lista delle forme giuridiche			   
				Integer idSB = info.getStatusInfo().getIdSoggettoBeneficiario();
				logger.info(prf + " idSB="+idSB);

				/** : Jira: x sostegno imprese - */
				codTipoBeneficiario = "";
				
				if("true".equals(input._operatorePresentatore_formaGiuridica_lavoratoreAutonomo))
				{
					// versione custom
					String idDomanda = "";
				    if(info.getStatusInfo().getNumProposta()!=null){
				    	idDomanda = info.getStatusInfo().getNumProposta()+"";
				    	logger.info("idDomanda risulta: " + idDomanda);
				    	
				    } else {
				    	logger.info("ATTENZIONE!!! idDomanda risulta vuoto! ");
				    }
				    
				    DatiTipolBeneficiarioVo datiTipolBeneficiario = null;
				    datiTipolBeneficiario = DomandaNGDAO.getDatiTipolBeneficiario(idDomanda);
				    codTipoBeneficiario = datiTipolBeneficiario.getCodicetipologiabeneficiario();
					logger.info("Codicetipologiabeneficiario risulta: " + codTipoBeneficiario);
					
					if(datiTipolBeneficiario != null && codTipoBeneficiario.length()>0 ) 
					{
						// se LA: estrae solo Lavoratore autonomo
						if(codTipoBeneficiario.equals("LARI")){
							datiFormaGiuridicaList = FormaGiuridicaDAO.getDatiFormaGiuridicaSoloLA(logger);
							logger.info(prf + " datiFormaGiuridicaList size="+datiFormaGiuridicaList.size());
						}

						// altrimenti tutto ad eccetto di lavoratore autonomo
						if(codTipoBeneficiario.equals("MED_STP") || codTipoBeneficiario.equals("MI_STP") || codTipoBeneficiario.equals("PI_STP"))
						{
							datiFormaGiuridicaList = FormaGiuridicaDAO.getDatiFormaGiuridicaSenzaLA(logger);
							logger.info(prf + " datiFormaGiuridicaList size="+datiFormaGiuridicaList.size());
						}
					}
					
				} else {
					// versione std
					datiFormaGiuridicaList = FormaGiuridicaDAO.getDatiFormaGiuridica(logger);
					logger.info(prf + " datiFormaGiuridicaList size="+datiFormaGiuridicaList.size());
				}

				//lista delle attivita economiche
				settoreAttivitaEconomicaList = AttivitaDAO.getSettoreAttivitaEconomicaList();
				logger.info(prf + " settoreAttivitaEconomicaList= "+settoreAttivitaEconomicaList);

				//gestione beneficiario estero inizio 
				//lista degli stati esteri se all'accesso il beneficiario e' stato dichiarato estero (nella lista Italia non compare, così resta sempre estero)
				if(StringUtils.isNotBlank(beneficiarioEstero) && beneficiarioEstero.equals("true")){
					String idStatoXml = OperatorePresentatoreDAO.getIdStatoInXmlOperatatorePresentatore(info.getStatusInfo().getNumProposta(), logger);
					if(StringUtils.isNotBlank(idStatoXml)){
						statoEsteroRO="true";						
					}else{
						//la lista serve solo se lo stato è estero e se non è ancora presente nell'xml
						statoEsteroList = LuoghiDAO.getStatoEsteroListSenzaItalia(logger);
					}
				}
				//gestione beneficiario estero fine 
				
				String codiceUOXml = OperatorePresentatoreDAO.getCodUOInXmlOperatatorePresentatore(info.getStatusInfo().getNumProposta(), logger);
				if(StringUtils.isNotBlank(codiceUOXml)){
					unitaOrganizzativaRO="true";						
				}
			}
			
			if (input.operatorePresentatore != null) {
				logger.info(prf + " operPresent.idFormaGiuridica="+operPresent.getIdFormaGiuridica() );
			}

			if(StringUtils.isBlank(beneficiarioEstero) || ! beneficiarioEstero.equals("true")){
				int flagPP = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);

				if(flagPP == 2){  //visualizzo il codice Ipa solo se la tipologia di beneficiario ha flagPubblicoPrivato = 2 (pubblico)
					visIpa="RW"; //come default metto campo ipa modificabile dall'utente (in caso di pagina RO testa solo che visIpa abbia contenuto)
					//la logica seguente non serve per domande concluse o inviate 
					String codiceIpaXml = "";
					String codiceIpaSessione = "";		   
					if(_operatorePresentatore != null ) {
						codiceIpaXml = _operatorePresentatore.getCodiceIpa();	
						logger.info(prf + " debug: codiceIpaXml risulta: " + codiceIpaXml);
					}
					it.csi.findom.findomwebnew.dto.ipa.Ipa datiIpa = (it.csi.findom.findomwebnew.dto.ipa.Ipa)SessionCache.getInstance().get("datiIpa");
					logger.info(prf + " debug: datiIpa risulta: " + datiIpa);
					if(datiIpa!=null){
						codiceIpaSessione = datiIpa.getCodiceIPA();
						logger.info(prf + " debug: codiceIpaSessione risulta: " + codiceIpaSessione);
					}

					if(StringUtils.isBlank(codiceIpaXml) && StringUtils.isNotBlank(codiceIpaSessione)){

						// logger.info("debug: row-246 - codiceIpaSessione risulta: " + codiceIpaSessione);
						operPresent.setCodiceIpa(codiceIpaSessione);
						visIpa="RO"; //campo ipa in sola lettura		
						logger.info(prf + " debug: campo ipa in sola lettura");
					}else if(StringUtils.isBlank(codiceIpaXml) && StringUtils.isBlank(codiceIpaSessione))
					{ 
						logger.info(prf + " campo ipa settato ad empty");
						operPresent.setCodiceIpa("");
					}
					else if(StringUtils.isNotBlank(codiceIpaXml) && StringUtils.isNotBlank(codiceIpaSessione))
					{		       
						if(codiceIpaXml.equals(codiceIpaSessione))
						{		       
							logger.info(prf + " campo ipa risulta: " + codiceIpaSessione);
							operPresent.setCodiceIpa(codiceIpaSessione);
							visIpa="RO"; //campo ipa in sola lettura		         
						}else{
							logger.info(prf + " campo ipa risulta codiceIpaXml: " + codiceIpaXml);
							operPresent.setCodiceIpa(codiceIpaXml);		           		          
						}		   
					}else if(StringUtils.isNotBlank(codiceIpaXml) && StringUtils.isBlank(codiceIpaSessione)){
						logger.info(prf + " campo ipa risulta codiceIpaXml: " + codiceIpaXml);
						operPresent.setCodiceIpa(codiceIpaXml);		           		          
					}}
			}
			//// namespace

			ns.setDatiFormaGiuridicaList(datiFormaGiuridicaList);	
			
			if("true".equals(input._operatorePresentatore_formaGiuridica_lavoratoreAutonomo))
			{
				ns.codTipoBeneficiario = codTipoBeneficiario;
			}
			
			ns.setSettoreAttivitaEconomicaList(settoreAttivitaEconomicaList);
			ns.setEnteImpresa(enteImpresa);
			ns.setOperPresent(operPresent); // cf e denominazione presenti
			ns.setCodPrevalenteAtecoAAEP(codPrevalenteAtecoAAEP);
			ns.setDescrizioneAtecoAAEP(descrizioneAtecoAAEP);
			ns.setIdAtecoAAEP(idAtecoAAEP);
//			ns.setDipartimentiList(dipartimentiList);

			//gestione beneficiario estero inizio 
			ns.beneficiarioEstero = beneficiarioEstero;
			ns.statoEsteroRO = statoEsteroRO;
			if(StringUtils.isNotBlank(beneficiarioEstero) && beneficiarioEstero.equals("true")){
				if(statoEsteroRO.equals("false")){
				   ns.setStatoEsteroList(statoEsteroList);
				}
			}
			ns.unitaOrganizzativaRO=unitaOrganizzativaRO;
			//gestione beneficiario estero fine 
			ns.visNotaPrecompilazioneAAEP = visNotaPrecompilazioneAAEP;
			ns.visNotaPrecompilazioneUltimaDomanda = visNotaPrecompilazioneUltimaDomanda;
			
			ns.visIpa=visIpa;
			logger.info(prf + "visIpa risulta covisIpa: " + visIpa);
			
		    return ns;
		  }
		  catch(Exception ex) {
		    throw new CommonalityException(ex);
		  }
		  finally {
		    logger.info(prf + " END");
		  }
	}



	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo infol, List<CommonalityMessage> inputMessages) {
		
		//<#-- note di dipendenze da altre commonalities: nessuna -->
		FinCommonInfo info = (FinCommonInfo) infol;
		List<CommonalityMessage> newMessages = new ArrayList<>();

		Logger logger = Logger.getLogger(info.getLoggerName());
		String prf = "[OperatorePresentatoreSemplificato::modelValidate] ";
	    logger.info(prf + "BEGIN");	    
		
		//// validazione panel Beneficiario - commonality _operatorePresentatore

		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo &#233; obbligatorio";
		String ERRMSG_PIVA_NOTCORRECT= "- La partita IVA &#233; formalmente non corretta";
		String ERRMSG_PEC_NOTCORRECT= "- L'indirizzo PEC &#233; formalmente non corretto";
		String ERRMSG_CIR_NOTCORRECT= "- Codice inserito non valido. \nRispettare il seguente formato: <b>000000-AAA-00000</b>";
		String ERRMSG_CIR_NOTPRESENT= "- Il codice regionale specificato non risulta ammissibile per il bando selezionato";
		String ERRMSG_NUM_REG_APS_NOTCORRECT= "- Il nunmero registro APS non risulta nel formato richiesto: <b>Numerico, intero maggiore di zero</b>.";
		String ERRMSG_FORMATO_CAMPO_DATA = "- il formato della data non é valido";
		String ERRMSG_DATA_REG_APS_SUCC = "- La data indicata risulta essere successiva alla data odierna";
		String ERRMSG_DATA_REG_APS_SUCC_DATA_MAX = "- La data di iscrizione al Registro APS deve essere antecedente al <b>15/05/2020</b> ";

		boolean erroriMVR = false;
//		AnnotationConfigApplicationContext context = null;
		try {
			//per eseguire operazioni transazionali sul db (escluse operazioni su xml fatte da aggregatore)
//			context = new AnnotationConfigApplicationContext(AppConfig.class);	
//			BlocchettiTransactionManager blocchettiTransactionManager= context.getBean(BlocchettiTransactionManager.class);

			OperatorePresentatoreVo _operatorePresentatore = input.operatorePresentatore;

			//gestione beneficiario estero  
			String beneficiarioEstero = "";			
			String idStatoUltimoSalvato = getUltimoStatoSalvato(info);
			beneficiarioEstero = (StringUtils.isNotBlank(idStatoUltimoSalvato) && !idStatoUltimoSalvato.equals(Constants.ID_STATO_ITALIA)) ? "true" : "";
			
			if (_operatorePresentatore != null ) {
				logger.info(prf + "_operatorePresentatore:" + _operatorePresentatore);

				// Denominazione o ragione sociale
				String denominazione = (String) _operatorePresentatore.getDenominazione();
				logger.info(prf + "denominazione: " + denominazione);
				if (StringUtils.isBlank(denominazione)) {
					addMessage(newMessages,"_operatorePresentatore_denominazione", ERRMSG_CAMPO_OBBLIGATORIO);
					erroriMVR = true;
					logger.warn(prf + " denominazione non valorizzato");
				}

				//se beneficiario estero, controllo che sia valorizzata la combo stati esteri
				//gestione beneficiario estero inizio 
				if(beneficiarioEstero.equals("true")){
					String idStatoEstero = (String) _operatorePresentatore.getIdStato();
					logger.info(prf + " idStatoEstero: " + idStatoEstero);
					if (StringUtils.isBlank(idStatoEstero)) {
						addMessage(newMessages,"_operatorePresentatore_statoEstero", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.info(prf + " stato estero non valorizzato");
					} 
				}
				//gestione beneficiario estero fine 
				
				// forma giuridica
				String idFormaGiuridica = (String) _operatorePresentatore.getIdFormaGiuridica();
				logger.info(prf + " idFormaGiuridica: " + idFormaGiuridica);
				if (StringUtils.isBlank(idFormaGiuridica)) {
					addMessage(newMessages,"_operatorePresentatore_idFormaGiuridica", ERRMSG_CAMPO_OBBLIGATORIO);
					erroriMVR = true;
					logger.warn(prf + " forma giuridica non valorizzato");
				} 

				// partita iva
				if(!beneficiarioEstero.equals("true")){
					String partitaIva = (String) _operatorePresentatore.getPartitaIva();
					logger.info(prf + " partitaIva: " + partitaIva);
					if (StringUtils.isBlank(partitaIva)) {
						addMessage(newMessages,"_operatorePresentatore_partitaIva", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.warn(prf + " partita IVA non valorizzato");
					} else {			
						if (!ControlPartitaIVA.controllaPartitaIVA(partitaIva)){
							addMessage(newMessages,"_operatorePresentatore_partitaIva", ERRMSG_PIVA_NOTCORRECT);
							erroriMVR = true;
							logger.warn(prf + " partita IVA formalmente non corretta");
						}else{				
							logger.info(prf + "partita IVA:" + partitaIva);
						}
					}
				}

				/** Jira 1838 - */
				if ("false".equals(input._operatorePresentatore_indirizzoPec)) {
					
					String indirizzoPec = (String) _operatorePresentatore.getIndirizzoPec();
					logger.info(prf + " indirizzoPec: " + indirizzoPec);
					logger.info(prf + " info: " + info);
					if (StringUtils.isBlank(indirizzoPec) && !isBeneficiarioAziendaEstera(info)) {
						addMessage(newMessages,"_operatorePresentatore_indirizzoPec", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.warn(prf + "indirizzo PEC non valorizzato");
					}else{
						//MB2018_06_21 ini jira 914
						if(StringUtils.isNotBlank(indirizzoPec) && !ControlEmail.ctrlFormatoIndirizzoEmail(indirizzoPec)){
							addMessage(newMessages,"_operatorePresentatore_indirizzoPec", ERRMSG_PEC_NOTCORRECT);
							erroriMVR = true;
							logger.warn(prf + " indirizzo PEC formalmente non corretto");
						}else{				
							logger.info(prf + " indirizzo PEC:" + indirizzoPec);
						}
						//MB2018_06_21 fine
					}
				}
				

				//settore prevalente ATECO (SUGGEST)
				String idAteco2007 = (String) _operatorePresentatore.getIdAteco2007();
				logger.info(prf + " idAteco2007: " + idAteco2007);
				
				String codicePrevalenteAteco2007 = (String) _operatorePresentatore.getCodicePrevalenteAteco2007();
				logger.info(prf + " codicePrevalenteAteco2007: " + codicePrevalenteAteco2007);
				
				String descrizioneAteco2007 = (String) _operatorePresentatore.getDescrizioneAteco2007();
				logger.info(prf + " descrizioneAteco2007: " + descrizioneAteco2007);
	
				if ("true".equals(input._operatorePresentatore_ateco))
			    {
					if (StringUtils.isBlank(codicePrevalenteAteco2007)) {
						addMessage(newMessages,"_operatorePresentatore_codicePrevalenteAteco2007", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.warn(prf + "codicePrevalenteAteco2007 non valorizzato");
					}else{
						logger.info(prf + "codicePrevalenteAteco2007:" + codicePrevalenteAteco2007 );
					}
					
					if (StringUtils.isBlank(descrizioneAteco2007)) {
						addMessage(newMessages,"_operatorePresentatore_descrizioneAteco2007", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.warn(prf + "descrizioneAteco2007 non valorizzato");
					}else{
						logger.info(prf + "descrizioneAteco2007:" + descrizioneAteco2007 );
					}
					
					if ("true".equals(input._operatorePresentatore_settore_attivita_economica))
				    {
						
						//Settore attivita' economica	
						String idAttivitaEconomica = (String) _operatorePresentatore.getIdAttivitaEconomica();
						logger.info(prf + "idAttivitaEconomica: " + idAttivitaEconomica );
						if (StringUtils.isBlank(idAttivitaEconomica)) {
							addMessage(newMessages,"_operatorePresentatore_idAttivitaEconomica", ERRMSG_CAMPO_OBBLIGATORIO);
							erroriMVR = true;
							logger.warn(prf + " attivita economica non valorizzato");
						} else {	
							logger.info(prf + "attivita economica: " + _operatorePresentatore.getIdAttivitaEconomica()+" "+ _operatorePresentatore.getCodiceAttivitaEconomica()+" "+_operatorePresentatore.getDescrizioneAttivitaEconomica());
						}
				    }
			    }
				if(StringUtils.isBlank(beneficiarioEstero) || ! beneficiarioEstero.equals("true")){
					int flgPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);  
					logger.info(prf + "flgPubblicoPrivato: " + flgPubblicoPrivato );
					if (flgPubblicoPrivato==2){
						String codiceIpa = (String) _operatorePresentatore.getCodiceIpa();
						if (StringUtils.isBlank(codiceIpa)) {
							addMessage(newMessages,"_operatorePresentatore_codiceIpa", ERRMSG_CAMPO_OBBLIGATORIO);
							erroriMVR = true;
							logger.warn(prf + "codice ipa non valorizzato");
						} else {		
							logger.info(prf + "codice ipa: " + _operatorePresentatore.getCodiceIpa());
						}
					}
				}
				
				/** Jira: 1974 - inizio */
				String codiceRegionale = (String) _operatorePresentatore.getCodiceRegionale() !=null ? _operatorePresentatore.getCodiceRegionale() : "";
				logger.info(prf + "codiceRegionale: " + codiceRegionale);
				
				boolean isPresenteCRI = false;
				
				if ("true".equals(input._operatorePresentatore_cir))
				{
					String regex = "^[0-9]{6}+[-]?[a-zA-Z]{3}+[-]?[0-9]{5}$";
					Pattern pattern = Pattern.compile(regex);
					
					if (StringUtils.isBlank(codiceRegionale)) {
						addMessage(newMessages,"_operatorePresentatore_codiceRegionale", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.warn(prf + " codice Regionale non valorizzato");
						
					}else{
						
						Matcher matcher = pattern.matcher(codiceRegionale);
						if(!matcher.matches()){
							logger.info(codiceRegionale+ "codice inserito non valido. Rispettare il seguente formato: 000000-AAAA-00000");
							addMessage(newMessages,"_operatorePresentatore_codiceRegionale", ERRMSG_CIR_NOTCORRECT);
							erroriMVR = true;
							
						}else{
							logger.info(codiceRegionale+ "codice inserito valido.");
							
							// verifico se codiceRegionale dgt risulta presente a db
							codiceRegionale = codiceRegionale.toUpperCase();
							logger.info(prf + " codice Regionale risulta : "+codiceRegionale);
							
							isPresenteCRI = OperatorePresentatoreDAO.getIsCodRegionalePresente(codiceRegionale, logger);
							logger.info(prf + " codice Regionale risulta : "+isPresenteCRI);
							
							if(!isPresenteCRI){
								addMessage(newMessages,"_operatorePresentatore_codiceRegionale", ERRMSG_CIR_NOTPRESENT);
								erroriMVR = true;
							}}}
				}
				/** Jira: 1974 - fine */
				
				
				/** Jira 2005 - */
				if ("true".equals(input._operatorePresentatore_codiceAps)) 
				{
					
					/** Gestione numero registro Aps */
					Date today = new Date();
					
					String regex = "[0-9]+";
					Pattern pattern = Pattern.compile(regex);
					
					String numeroIscrizioneRegistroAPS = (String) _operatorePresentatore.getNumeroIscrizioneRegistroAPS();
					logger.info(prf + " numeroIscrizioneRegistroAPS: " + numeroIscrizioneRegistroAPS);
					
					if (StringUtils.isBlank(numeroIscrizioneRegistroAPS)) {
						addMessage(newMessages,"_operatorePresentatore_indirizzoPec", ERRMSG_CAMPO_OBBLIGATORIO);
						erroriMVR = true;
						logger.warn(prf + "numero iscrizione registro APS non valorizzato");
					
					} 
					else {
						Matcher matcher = pattern.matcher(numeroIscrizioneRegistroAPS);
						if(!matcher.matches()){
							logger.info(numeroIscrizioneRegistroAPS+ "codice inserito non valido. Rispettare il formato numerico intero richiesto.");
							addMessage(newMessages,"_operatorePresentatore_numero_iscrizione_registro_APS", ERRMSG_NUM_REG_APS_NOTCORRECT);
							erroriMVR = true;
							
						}else{
							logger.info(codiceRegionale+ "codice inserito valido.");
						}
					}
					
					/** Gestione data iscrizione registro Aps */
					String dataIscrizioneRegistroAPS = (String) _operatorePresentatore.getDataIscrizioneRegistroAPS();
					logger.info(prf + " data iscrizione al registro Aps risulta: " + dataIscrizioneRegistroAPS);

					// verifico se campo risulta compilato
					if (StringUtils.isBlank(dataIscrizioneRegistroAPS)) {
						addMessage(newMessages,"_operatorePresentatore_data_iscrizione_registro_APS", ERRMSG_CAMPO_OBBLIGATORIO);
						logger.info(prf + " data iscrizione al registro Aps non valorizzato");

					} else {
						
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						
						Date date_IscrizioneRegistroAPS = sdf.parse(dataIscrizioneRegistroAPS, new ParsePosition(0));
						
						if (!DateValidator.getInstance().isValid(dataIscrizioneRegistroAPS, "dd/MM/yyyy") || !(dataIscrizioneRegistroAPS.matches("\\d{2}/\\d{2}/\\d{4}")) ) {			
							addMessage(newMessages,"_operatorePresentatore_dataIscrizioneRegistroImprese", ERRMSG_FORMATO_CAMPO_DATA);
							logger.info(" data iscrizione al registro Aps formalmente non corretta");

						} else if (date_IscrizioneRegistroAPS.after(today)) {
							logger.info(prf + " - La data iscrizione al registro APS risulta essere successiva alla data odierna: " + date_IscrizioneRegistroAPS);
							addMessage(newMessages,"_operatorePresentatore_data_iscrizione_registro_APS", ERRMSG_DATA_REG_APS_SUCC);

						} 
						else {
							logger.info(prf +"data iscrizione al registro APS risulta: " + date_IscrizioneRegistroAPS);
						}
						
						/** Jira 2010 - */
						if ("true".equals(input._operatorePresentatore_dataIscrLimMax)) {
							Date dtIscrizioneLimiteMax = sdf.parse("14/05/2020");
							logger.info(prf + "dtIscrizioneLimiteMax risulta : "+dtIscrizioneLimiteMax);
							
							if (date_IscrizioneRegistroAPS.compareTo(dtIscrizioneLimiteMax) > 0) {
								addMessage(newMessages,"_operatorePresentatore_data_iscrizione_registro_APS", ERRMSG_DATA_REG_APS_SUCC_DATA_MAX);
								logger.info(prf + "dtIscrizioneLimiteMax risulta posteriore alla data limite max: "+dtIscrizioneLimiteMax);
					        }
						}
					} //  :: fine Jira: 2005
				}
				

			} else {
				logger.info(prf + "_operatorePresentatore non presente o vuoto");
				erroriMVR = true;
				addMessage(newMessages,"_operatorePresentatore", ERRMSG_CAMPO_OBBLIGATORIO);
			}
	
			logger.info(prf + " erroriMVR="+erroriMVR);
			if(!erroriMVR){
				
				String codiceFiscale = (String) _operatorePresentatore.getCodiceFiscale();
				logger.info(prf + " codiceFiscale= "+codiceFiscale);
				// Denominazione o ragione sociale
				String denominazione = (String) _operatorePresentatore.getDenominazione();
				logger.info(prf + " denominazione= "+denominazione);

				// forma giuridica
				String idFormaGiuridica = (String) _operatorePresentatore.getIdFormaGiuridica();	
				// Rinnovo la sessione riferita alla denominazione Jira: 1195
					SoggettoDAO.updateSoggettoByCodFiscale(codiceFiscale, denominazione, idFormaGiuridica);	
					info.getStatusInfo().setDescrImpresaEnte(denominazione);

				String denominazioneSession = info.getStatusInfo().getDescrImpresaEnte();
				logger.info(" test sessione: " + denominazioneSession);

				//gestione beneficiario estero 
				//se stato estero aggiorno shell_t_soggetti perche' potrebbe essere cambiato in un altro stato estero
				if(beneficiarioEstero.equals("true")){
					String codStato = (String) _operatorePresentatore.getIdStato();
					SoggettoDAO.updateNazioneSoggettoByCodFiscale(codiceFiscale, codStato, logger);
				}
			}
		}
		catch(Exception ex) {
			logger.error(prf + "", ex);
		}
		finally {
//			if(context!=null){
//				context.close();
//			}
			logger.info(prf + "END");
		}	
		return newMessages;
	}

	/**
	 * Restituisce TRUE se il beneficiario loggato non e' italiano
	 * ( ossia se shell_t_soggetti.sigla_nazione != null AND !='000' )
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

  private OperatorePresentatoreVo aggiornaConInformazioniUltimaDomandaOperatorePresentatore(OperatorePresentatoreVo operatorePresentatore, boolean datiSuAAEP, String beneficiarioEstero,Logger logger) {

	  String prf = "[OperatorePresentatore::aggiornaConInformazioniUltimaDomandaOperatorePresentatore] ";
	    logger.debug(prf + " BEGIN");
		VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		
		if (datiUltimoBeneficiario != null && datiUltimoBeneficiario.getOperatorePresentatore()!=null) {
			
			logger.debug(prf + " datiUltimoBeneficiario presente in sessione");
			
			if(datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale().equals(operatorePresentatore.getCodiceFiscale())){
			
				OperatorePresentatoreDto ultimoOperatorePresentatore = datiUltimoBeneficiario.getOperatorePresentatore();
				
				if (!Utils.isEmpty(ultimoOperatorePresentatore.getIdAttivitaEconomica())) {
					operatorePresentatore.setIdAttivitaEconomica(ultimoOperatorePresentatore.getIdAttivitaEconomica());
					operatorePresentatore.setCodiceAttivitaEconomica(ultimoOperatorePresentatore.getCodiceAttivitaEconomica());
					operatorePresentatore.setDescrizioneAttivitaEconomica(ultimoOperatorePresentatore.getDescrizioneAttivitaEconomica());
				}
				if (!Utils.isEmpty(ultimoOperatorePresentatore.getIdFormaGiuridica())) {
					operatorePresentatore.setIdFormaGiuridica(ultimoOperatorePresentatore.getIdFormaGiuridica());
					operatorePresentatore.setCodiceFormaGiuridica(ultimoOperatorePresentatore.getCodiceFormaGiuridica());
					operatorePresentatore.setDescrizioneFormaGiuridica(ultimoOperatorePresentatore.getDescrizioneFormaGiuridica());
				}
				
				operatorePresentatore.setPartitaIva(Utils.isEmpty(operatorePresentatore.getPartitaIva()) ? ultimoOperatorePresentatore.getPartitaIva() : operatorePresentatore.getPartitaIva());
				operatorePresentatore.setDenominazione(Utils.isEmpty(operatorePresentatore.getDenominazione()) ? ultimoOperatorePresentatore.getDenominazione() : operatorePresentatore.getDenominazione());
				
				operatorePresentatore.setIdAteco2007(Utils.isEmpty(operatorePresentatore.getIdAteco2007()) ? ultimoOperatorePresentatore.getIdAteco2007() : operatorePresentatore.getIdAteco2007());
				operatorePresentatore.setCodicePrevalenteAteco2007(Utils.isEmpty(operatorePresentatore.getCodicePrevalenteAteco2007()) ? ultimoOperatorePresentatore.getCodicePrevalenteAteco2007() : operatorePresentatore.getCodicePrevalenteAteco2007());
				operatorePresentatore.setDescrizioneAteco2007(Utils.isEmpty(operatorePresentatore.getDescrizioneAteco2007()) ? ultimoOperatorePresentatore.getDescrizioneAteco2007() : operatorePresentatore.getDescrizioneAteco2007());
	
				operatorePresentatore.setIndirizzoPec(Utils.isEmpty(operatorePresentatore.getIndirizzoPec()) ? ultimoOperatorePresentatore.getIndirizzoPec() : operatorePresentatore.getIndirizzoPec());
				if(StringUtils.isNotBlank(beneficiarioEstero) && beneficiarioEstero.equals("true")){
					//la proposta dello ststo presa dall'ultima domanda inviata viene fatto solo se siamo in un caso di stato estero;
					//altrimenti se lo stato è Italia sarebbe inutile, oltre al fatto che se non si trova il dato dall'ultima domanda inviata,
					//lo stato resterebbe non valorizzato in questa domanda, non essendo un campo visibile e valorizzabile dall'utente
					operatorePresentatore.setIdStato(ultimoOperatorePresentatore.getIdStato());//gestione beneficiario estero 
					operatorePresentatore.setDescrStato(ultimoOperatorePresentatore.getDescrStato());//gestione beneficiario estero 
				}
			} else {
				logger.warn(prf + "datiUltimoBeneficiario presente in sessione non si riferiscono al beneficiario corrente");
				logger.warn(prf + "datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale()="+datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale());
				logger.warn(prf + "operatorePresentatore.getCodiceFiscale()="+operatorePresentatore.getCodiceFiscale());
			}
		}else{
			logger.debug(prf + "datiUltimoBeneficiario in sessione nullo");
		}
		logger.debug(prf + " END");
		return operatorePresentatore;
	}
  	private boolean informazioniUltimaDomandaPresenti() {
		//  --- utilizzo i dati dell'ultima domanda inserita
		VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		if (datiUltimoBeneficiario != null && 
			datiUltimoBeneficiario.getOperatorePresentatore()!=null &&
			datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale()!=null) {
			return true;
		}
		return false;
	}
  	
  	private void aggiornaIdUOInXml(FinCommonInfo info, Logger logger, String idUnitaOrganizzativaIns) throws AggregatoreException, Throwable {
		FinStatusInfo statusInfo = info.getStatusInfo();
		String prf = "[OperatorePresentatore::aggiornaIdUOInXml] ";
		logger.debug(prf + " BEGIN");

		String cfBeneficiario = statusInfo.getCodFiscaleBeneficiario(); // codice fiscale del beneficiario
		String templateCode = input.xformId; // (String)context.get("xformId"); //id bando
		Integer modelProg = input.xformProg; // progressivo domanda
		String formName = input.xformName;
		String statoProposta = statusInfo.getStatoProposta();								

		logger.debug(prf + "cfBeneficiario: " + cfBeneficiario);
		logger.debug(prf + "templateCode: " + templateCode);
		logger.debug(prf + "modelProg: " + modelProg);
		logger.debug(prf + "formName: " + formName);
		logger.debug(prf + "statoProposta: " + statoProposta);

		DataModel mod = AggregatoreFactory.create().readModel(cfBeneficiario, templateCode, modelProg); 
		TreeMap<String, String> mapSerializedModel = mod.getSerializedModel();
		logger.debug(prf + " model serializzato: " + mapSerializedModel);

		mapSerializedModel.put("_operatorePresentatore.idDipartimento", idUnitaOrganizzativaIns);

		mod.setSerializedModel(mapSerializedModel);									
		AggregatoreFactory.create().saveModel(cfBeneficiario, templateCode, modelProg, formName, mod, null,statoProposta);
		logger.debug(prf + " END");
	}
  	//gestione beneficiario estero inizio 
   	//info.getStatusInfo().getSiglaNazione() restituisce idStato di operatore presentatore della domanda se presente; 
  	//oppure l'id stato di shell_t_soggetti se operatore presentatore della domanda non e' valorizzato
  	
  	private String getUltimoStatoSalvato(FinCommonInfo info) throws CommonalityException {		
  		String idStato = (info.getStatusInfo().getSiglaNazione() == null || StringUtils.isBlank(info.getStatusInfo().getSiglaNazione())) ? "" : info.getStatusInfo().getSiglaNazione();
  		return idStato;
  	}
  	//gestione beneficiario estero fine 

}
