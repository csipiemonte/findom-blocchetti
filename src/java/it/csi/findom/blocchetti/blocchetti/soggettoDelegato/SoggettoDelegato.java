/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.soggettoDelegato;

import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.dao.TipoDocRiconoscimentoDAO;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.SiglaProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.common.vo.tipodocriconoscimento.TipoDocRiconoscimentoVO;
import it.csi.findom.blocchetti.commonality.ControlCodFisc;
import it.csi.findom.blocchetti.commonality.ControlPartitaIVA;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;

public class SoggettoDelegato extends Commonality {

	SoggettoDelegatoInput input = new SoggettoDelegatoInput();

	@Override
	public SoggettoDelegatoInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[SoggettoDelegato::inject] BEGIN");
		try {
			SoggettoDelegatoOutput ns = new SoggettoDelegatoOutput();
			
			List<StatoEsteroVO> statoEsteroList = new ArrayList<StatoEsteroVO>();
			List<ProvinciaVO> provinciaList = new ArrayList<ProvinciaVO>();
			List<ComuneVO> comuneNascitaList = new ArrayList<ComuneVO>();
			List<ComuneVO> comuneResidenzaList = new ArrayList<ComuneVO>();			

			List<TipoDocRiconoscimentoVO> tipoDocRiconoscimentoList = new ArrayList<TipoDocRiconoscimentoVO>();
			
			String soggettoDelegato_siglaProvinciaNascita = "";	
			String soggettoDelegato_siglaProvinciaResidenza = "";		
			
				
			//// valorizzazione
				
		   if (info.getCurrentPage() != null) {
//			    if (info.getCurrentPage().contains("S1_P6") && !info.getFormState().equals("IN") && !info.getFormState().equals("CO")) {
		    	if (!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
			    		
				    //lista degli stati esteri e delle province
	                statoEsteroList = LuoghiDAO.getStatoEsteroList(logger); // 233
		
	                tipoDocRiconoscimentoList = TipoDocRiconoscimentoDAO.getTipoDocRiconoscimentoList(logger);		// 3    			    
				    provinciaList = LuoghiDAO.getProvinciaList(logger); // 113
		
				   //  VERIFICARE Map _soggettoDelegatoMap = (Map) model.get("_soggettoDelegato");			   
				    // SoggettoDelegatoVO _soggettoDelegatoMap = input.soggettoDelegatoModel;	// ***	
				    SoggettoDelegatoVO _soggettoDelegatoMap = input._soggettoDelegato;

		   
				    //se soggetto delegato nullo, significa che sull'xml non e' mai stato salvato e quindi lo inizializzo
				    if(_soggettoDelegatoMap==null){                   
						if(_soggettoDelegatoMap==null){					 
						    _soggettoDelegatoMap = new SoggettoDelegatoVO();   
						    //_soggettoDelegatoMap = input._soggettoDelegato;
					    }
				   }else{
					    //la provincia di nascita presente eventualmente in request ha la precedenza su quella presente nell'xml
					    //perche' sull'onchange delle combo provincia nascita devo tener conto del valore appena scelto
					    //  VERIFICARE String provinciaNascita = ServletActionContext.getRequest().getParameter("_soggettoDelegato.provinciaNascita");
					    String provinciaNascita = input.soggettoDelegatoProvinciaNascitaRequest;

					    if (StringUtils.isBlank(provinciaNascita)){
					        // se non ho una provincia in request, considero quella dell'xml					   	
					        provinciaNascita = (String) _soggettoDelegatoMap.getProvinciaNascita();		
			        
				        }			    
						
						if (StringUtils.isNotBlank(provinciaNascita)) {					   
						   comuneNascitaList = LuoghiDAO.getComuneList(provinciaNascita, logger);
						
						   // ottiene la sigla della provincia di nascita
						   List<SiglaProvinciaVO> siglaProvinciaList = LuoghiDAO.getSiglaProvinciaList(provinciaNascita, logger);

						   if (siglaProvinciaList != null && !siglaProvinciaList.isEmpty()) {
							   soggettoDelegato_siglaProvinciaNascita = (String) ((SiglaProvinciaVO) siglaProvinciaList.get(0)).getSigla();

							   logger.info("[SoggettoDelegato::inject] soggettoDelegato_siglaProvinciaNascita:" + soggettoDelegato_siglaProvinciaNascita);
						   }				   
	                    }                    
	                    
	                    //la provincia di residenza presente eventualmente in request ha la precedenza su quella presente nell'xml
					    //perche' sull'onchange delle combo provincia residenza devo tener conto del valore appena scelto
					    //  VERIFICARE String provinciaResidenza = ServletActionContext.getRequest().getParameter("_soggettoDelegato.provinciaResidenza");
					    String provinciaResidenza = input.soggettoDelegatoProvinciaResidenzaRequest;

					    if (StringUtils.isBlank(provinciaResidenza)){
					        // se non ho una provincia in request, considero quella dell'xml					    	
					        provinciaResidenza = (String) _soggettoDelegatoMap.getProvinciaResidenza();				       
					    }			    
						
						if (StringUtils.isNotBlank(provinciaResidenza)) {					   
						   comuneResidenzaList = LuoghiDAO.getComuneList(provinciaResidenza, logger);
						
						   // ottiene la sigla della provincia di residenza
						   List<SiglaProvinciaVO> siglaProvinciaResidenzaList = LuoghiDAO.getSiglaProvinciaList(provinciaResidenza, logger);
						   if (siglaProvinciaResidenzaList != null && !siglaProvinciaResidenzaList.isEmpty()) {
							   soggettoDelegato_siglaProvinciaResidenza = (String) ((SiglaProvinciaVO) siglaProvinciaResidenzaList.get(0)).getSigla();
		
							   logger.info("[SoggettoDelegato::inject] soggettoDelegato_siglaProvinciaResidenza:" + soggettoDelegato_siglaProvinciaResidenza);
						   }
	                    }
				   }
			  }
	        }
			//// namespace
	 			
			ns.setStatoEsteroList(statoEsteroList);
			ns.setProvinciaList(provinciaList);
			ns.setComuneNascitaList(comuneNascitaList);
			ns.setSoggettoDelegato_siglaProvinciaNascita(soggettoDelegato_siglaProvinciaNascita);
			ns.setSoggettoDelegato_siglaProvinciaResidenza(soggettoDelegato_siglaProvinciaResidenza);
			ns.setComuneResidenzaList(comuneResidenzaList);
			ns.setTipoDocRiconoscimentoList(tipoDocRiconoscimentoList);
			
			logger.info("[SoggettoDelegato::inject] _soggettoDelegato END");
			
			return ns;
			
		} catch (Exception ex) {
			throw new CommonalityException(ex);
		} finally {
			logger.info("[SoggettoDelegato::inject] END");
		}
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {

		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[EstremiBancari::modelValidate] _soggettoDelegato  BEGIN");
			
		DateValidator validator = DateValidator.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		
		String ERRMSG_CAMPO_OBBLIGATORIO_SD = "- il campo é obbligatorio";
		
		String ERRMSG_CF_NOTALPHANUM_SD = "- Il Codice Fiscale non é valido. Inserire solo caratteri alfanumerici";
		String ERRMSG_SD_NOTCORRECT_CF = "- Il Codice Fiscale é formalmente non corretto";
		String ERRMSG_SD_WRONGLENGTH_CF = "- La lunghezza del Codice Fiscale non é valida";
		
		String ERRMSG_SD_NOTALPHANUM_CGN = "- il campo cognome non é valido. Inserire solo caratteri alfabetici";
		String ERRMSG_SD_NOTALPHANUM_NM = "- il campo nome non é valido. Inserire solo caratteri alfabetici";
		
		String ERRMSG_SD_NOTSELSTATO_LN = "- il campo Luogo di Nascita é obbligatorio. Selezionare o Italia o Stato Estero";
		String ERRMSG_SD_NOTSELSTATO_LR = "- il campo Luogo di Residenza é obbligatorio. Selezionare o Italia o Stato Estero";
		String ERRMSG_SD_PROVINC = "- il campo Provincia é obbligatorio se valorizzata l'opzione stato Italia";
		String ERRMSG_SD_COMUNE = "- il campo Comune é obbligatorio se valorizzata l'opzione stato Italia";
		String ERRMSG_SD_ESTERO = "- il campo Stato Estero é obbligatorio se valorizzata l'opzione corrispondente";
		
		String ERRMSG_SD_DATA_FORMAT = "- Il formato della data non é valido";
		String ERRMSG_SD_DATA_SUCC = "- data nascita successiva alla data odierna";
		
		String ERRMSG_SD_DATARILDOC = "- La data di rilascio documento é formalmente non corretta";
		String ERRMSG_SD_DATARILDOC_SUCC = "- La data di rilascio documento é successiva alla data odierna";
		
		//// validazione panel Soggetto delegato
		try {
			
				// :  -> SoggettoDelegatoVO _soggettoDelegato = (SoggettoDelegatoVO) input.soggettoDelegatoModel;
			SoggettoDelegatoVO _soggettoDelegato = (SoggettoDelegatoVO) input._soggettoDelegato;
				if (_soggettoDelegato != null) {
					logger.info("[SoggettoDelegato::modelValidate] _soggettoDelegato:" + _soggettoDelegato);
		
					// Codice Fiscale
					String codiceFiscale = (String) _soggettoDelegato.getCodiceFiscale();
					ControlCodFisc ctrlCF = new ControlCodFisc(codiceFiscale, logger);
					
					if (StringUtils.isBlank(codiceFiscale)) {
						addMessage(newMessages,"_soggettoDelegato_codiceFiscale", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] codiceFiscale non valorizzato");
					} else {
						if (codiceFiscale.length() == 16 || codiceFiscale.length() == 11) {
							if (codiceFiscale.length() == 16) {
								if (!StringUtils.isAlphanumeric(codiceFiscale)) {
									addMessage(newMessages,"_soggettoDelegato_codiceFiscale", ERRMSG_CF_NOTALPHANUM_SD);
									logger.warn("[SoggettoDelegato::modelValidate] codiceFiscale non valorizzato");	
								}else if(!ctrlCF.controllaCheckDigit()) {
								    addMessage(newMessages,"_soggettoDelegato_codiceFiscale", ERRMSG_SD_NOTCORRECT_CF);
									logger.warn("[SoggettoDelegato::modelValidate] codiceFiscale formalmente non corretto");
								}
							} else {					    
								//controllo cod fiscale (partita iva)
								if (!ControlPartitaIVA.controllaPartitaIVA(codiceFiscale)){
		 			                addMessage(newMessages,"_soggettoDelegato_codiceFiscale", ERRMSG_SD_NOTCORRECT_CF);
								    logger.warn("[SoggettoDelegato::modelValidate] codiceFiscale legale rappresentante (partita iva) formalmente non corretto");
					            }									
							}
							logger.info("[SoggettoDelegato::modelValidate] codiceFiscale:" + codiceFiscale);
						} else {
							addMessage(newMessages,"_soggettoDelegato_codiceFiscale", ERRMSG_SD_WRONGLENGTH_CF);
							logger.warn("[SoggettoDelegato::modelValidate] lunghezza del codiceFiscale non valida");
						}
					}
		
					// Cognome
					String cognome = (String) _soggettoDelegato.getCognome();
					if (StringUtils.isBlank(cognome)) {
						addMessage(newMessages,"_soggettoDelegato_cognome", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] cognome non valorizzato");
					} else {
						if (!StringUtils.isAlphaSpaceApostropheCommaDot(cognome)) {
							addMessage(newMessages,"_soggettoDelegato_cognome", ERRMSG_SD_NOTALPHANUM_CGN);
							logger.warn("[SoggettoDelegato::modelValidate] cognome:" + cognome + " contiene caratteri non ammessi");
						} else {
							logger.info("[SoggettoDelegato::modelValidate] cognome:" + cognome);
						}
					}
		
					// Genere
					if (input._soggettoDelegato_genere.equals("true")) {
						String genere = (String) _soggettoDelegato.getGenere();
						if (StringUtils.isBlank(genere)) {
							addMessage(newMessages,"_soggettoDelegato_genere", ERRMSG_CAMPO_OBBLIGATORIO_SD);
							logger.warn("[SoggettoDelegato::modelValidate] genere non valorizzato");
						} else {
							logger.info("[SoggettoDelegato::modelValidate] genere:" + genere);
						}
					}
		
					// Nome
					String nome = (String) _soggettoDelegato.getNome();
					if (StringUtils.isBlank(nome)) {
						addMessage(newMessages,"_soggettoDelegato_nome", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] nome non valorizzato");
					} else {
						if (!StringUtils.isAlphaSpaceApostropheCommaDot(nome)) {
							addMessage(newMessages,"_soggettoDelegato_nome", ERRMSG_SD_NOTALPHANUM_NM);
							logger.warn("[SoggettoDelegato::modelValidate] nome:" + nome + " contiene caratteri non ammessi");
						} else {
							logger.info("[SoggettoDelegato::modelValidate] nome:" + nome);	
						}
					}
		
					// radio Luogo di nascita
					String luogoNascita = (String) _soggettoDelegato.getLuogoNascita();
					if (StringUtils.isBlank(luogoNascita)) {
						addMessage(newMessages,"_soggettoDelegato_noLuogoNascita", ERRMSG_SD_NOTSELSTATO_LN);
						logger.warn("[SoggettoDelegato::modelValidate] luogoNascita non valorizzato");
					} else {
						logger.info("[SoggettoDelegato::modelValidate] luogoNascita:" + luogoNascita);
						if (luogoNascita.equals("Italia")) {
							// Provincia
							String provinciaNascita = (String) _soggettoDelegato.getProvinciaNascita();
							if (StringUtils.isBlank(provinciaNascita)) {
								addMessage(newMessages,"_soggettoDelegato_luogoNascita", ERRMSG_SD_PROVINC);
								logger.warn("[SoggettoDelegato::modelValidate] provinciaNascita non valorizzato");
							} else {
								logger.info("[SoggettoDelegato::modelValidate] provinciaNascita:" + provinciaNascita);
							}
		
							// Comune
							String comuneNascita = (String) _soggettoDelegato.getComuneNascita();
							if (StringUtils.isBlank(comuneNascita)) {
								addMessage(newMessages,"_soggettoDelegato_luogoNascita", ERRMSG_SD_COMUNE);
								logger.warn("[SoggettoDelegato::modelValidate] comuneNascita non valorizzato");
							} else {
								logger.info("[SoggettoDelegato::modelValidate] comuneNascita:" + comuneNascita);
							}
						}
		
						if (luogoNascita.equals("Estero")) {
							// Stato Estero
							String statoEsteroNascita = (String) _soggettoDelegato.getStatoEsteroNascita();
							if (StringUtils.isBlank(statoEsteroNascita)) {
								addMessage(newMessages,"_soggettoDelegato_luogoNascita", ERRMSG_SD_ESTERO);
								logger.warn("[SoggettoDelegato::modelValidate] statoEsteroNascita non valorizzato");
							} else {
								logger.info("[SoggettoDelegato::modelValidate] statoEsteroNascita:" + statoEsteroNascita);
							}
						}
					}
		
					// Data di nascita
					String dataNascita = (String) _soggettoDelegato.getDataNascita();
					if (StringUtils.isBlank(dataNascita)) {
						addMessage(newMessages,"_soggettoDelegato_dataNascita", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] dataNascita non valorizzato");
					} else {
						logger.info("[SoggettoDelegato::modelValidate] dataNascita:" + dataNascita);
						if (!validator.isValid(dataNascita, "dd/MM/yyyy") || !(dataNascita.matches("\\d{2}/\\d{2}/\\d{4}")) ) {
							logger.warn("[SoggettoDelegato::modelValidate] dataNascita formato data non valido:" + dataNascita);
							addMessage(newMessages,"_soggettoDelegato_dataNascita", ERRMSG_SD_DATA_FORMAT);
						} else {
							Date dataNascitaParse = sdf.parse(dataNascita, new ParsePosition(0));
							if (dataNascitaParse.after(today)) {
								logger.warn("[SoggettoDelegato::modelValidate] dataNascita posteriore alla data odierna:" + dataNascita);
								addMessage(newMessages,"_soggettoDelegato_dataNascita", ERRMSG_SD_DATA_SUCC);
							} 
						}
					}
		
					//tipo doc riconoscimento
					String idTipoDocRiconoscimento = (String) _soggettoDelegato.getDocumento().getIdTipoDocRiconoscimento();
					if (StringUtils.isBlank(idTipoDocRiconoscimento)) {
						addMessage(newMessages,"_soggettoDelegato_idTipoDocRiconoscimento", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] idTipoDocRiconoscimento non valorizzato");
					} else {
						logger.info("[SoggettoDelegato::modelValidate] idTipoDocRiconoscimento:" + idTipoDocRiconoscimento);
					}
		
					//numero doc riconoscimento
					String numDocRiconoscimento = (String) _soggettoDelegato.getDocumento().getNumDocumentoRiconoscimento();
					if (StringUtils.isBlank(numDocRiconoscimento)) {
						addMessage(newMessages,"_soggettoDelegato_numDocumentoRiconoscimento", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] numDocRiconoscimento non valorizzato");
					} else {
						logger.info("[SoggettoDelegato::modelValidate] numDocRiconoscimento:" + numDocRiconoscimento);
					}
		
					//ente che ha rilasciato il doc riconoscimento
					String rilasciatoDa = (String) _soggettoDelegato.getDocumento().getDocRilasciatoDa();
					if (StringUtils.isBlank(rilasciatoDa)) {
						addMessage(newMessages,"_soggettoDelegato_docRilasciatoDa", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] doc riconoscimento rilasciatoDa non valorizzato");
					} else {
						logger.info("[SoggettoDelegato::modelValidate] doc riconoscimento rilasciatoDa:" + rilasciatoDa);
					}
		
					//Data rilascio doc riconoscimento
					String dataRilascioDoc = (String) _soggettoDelegato.getDocumento().getDataRilascioDoc();
					if (StringUtils.isBlank(dataRilascioDoc)) {
						addMessage(newMessages,"_soggettoDelegato_dataRilascioDoc", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						logger.warn("[SoggettoDelegato::modelValidate] data rilascio doc non valorizzato");
					}else{
						if (!validator.isValid(dataRilascioDoc, "dd/MM/yyyy") || !(dataRilascioDoc.matches("\\d{2}/\\d{2}/\\d{4}")) ) {			
				            addMessage(newMessages,"_soggettoDelegato_dataRilascioDoc", ERRMSG_SD_DATARILDOC);
						    logger.warn("[SoggettoDelegato::modelValidate]  data di rilascio documento formalmente non corretta");
					    }else{				
						    logger.info("[SoggettoDelegato::modelValidate]  data di rilascio documento:" + dataRilascioDoc);
							Date dataRilascioDocParse = sdf.parse(dataRilascioDoc, new ParsePosition(0));
							if (dataRilascioDocParse.after(today)) {
								logger.warn("[SoggettoDelegato::modelValidate] dataRilascioDoc posteriore alla data odierna:" + dataNascita);
								addMessage(newMessages,"_soggettoDelegato_dataRilascioDoc", ERRMSG_SD_DATARILDOC_SUCC);
							} 
						}
					}
					if (input._soggettoDelegato_presenza_residenza.equals("true")) {
						// radio Luogo residenza
						String luogoResidenza = (String) _soggettoDelegato.getLuogoResidenza();
						if (StringUtils.isBlank(luogoResidenza)) {
							addMessage(newMessages,"_soggettoDelegato_noLuogoResidenza", ERRMSG_SD_NOTSELSTATO_LR);
							logger.warn("[SoggettoDelegato::modelValidate] luogoResidenza non valorizzato ");
						} else {
							logger.info("[SoggettoDelegato::modelValidate] luogoResidenza:" + luogoResidenza);
							if (luogoResidenza.equals("Italia")) {
								// Provincia
								String provinciaResidenza = (String) _soggettoDelegato.getProvinciaResidenza();
								if (StringUtils.isBlank(provinciaResidenza)) {
									addMessage(newMessages,"_soggettoDelegato_luogoResidenza", ERRMSG_SD_PROVINC);
									logger.warn("[SoggettoDelegato::modelValidate] provinciaResidenza non valorizzato");
								} else {
									logger.info("[SoggettoDelegato::modelValidate] provinciaResidenza:" + provinciaResidenza);
								}
			
								// Comune
								String comuneResidenza = (String) _soggettoDelegato.getComuneResidenza();
								if (StringUtils.isBlank(comuneResidenza)) {
									addMessage(newMessages,"_soggettoDelegato_luogoResidenza", ERRMSG_SD_COMUNE);
									logger.warn("[SoggettoDelegato::modelValidate] comuneResidenza non valorizzato");
								} else {
									logger.info("[SoggettoDelegato::modelValidate] comuneResidenza:" + comuneResidenza);
								}
							}
			
							if (luogoResidenza.equals("Estero")) {
								// Stato Estero
								String statoEsteroResidenza = (String) _soggettoDelegato.getStatoEsteroResidenza();
								if (StringUtils.isBlank(statoEsteroResidenza)) {
									addMessage(newMessages,"_soggettoDelegato_luogoResidenza", ERRMSG_SD_ESTERO);
									logger.warn("[SoggettoDelegato::modelValidate] statoEsteroResidenza non valorizzato");
								} else {
									logger.info("[SoggettoDelegato::modelValidate] statoEsteroResidenza:" + statoEsteroResidenza);
								}
			
								// Citta estera
								String cittaEstera = (String) _soggettoDelegato.getCittaEsteraResidenza();
								if (StringUtils.isBlank(cittaEstera)) {
									addMessage(newMessages,"_soggettoDelegato_cittaEsteraResidenza", ERRMSG_CAMPO_OBBLIGATORIO_SD);
									logger.warn("[SoggettoDelegato::modelValidate] cittaEstera non valorizzato");
								} else {
									logger.info("[SoggettoDelegato::modelValidate] cittaEstera:" + cittaEstera);
								}
							}
						}	
						// Indirizzo
						String indirizzo = (String) _soggettoDelegato.getIndirizzo();
						if (StringUtils.isBlank(indirizzo)) {
						   addMessage(newMessages,"_soggettoDelegato_indirizzo", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						   logger.warn("[SoggettoDelegato::modelValidate] indirizzo non valorizzato");
						} else {
						  logger.info("[SoggettoDelegato::modelValidate] indirizzo:" + indirizzo);
						}
						
						// numero civico
						String numCivico = (String) _soggettoDelegato.getNumCivico();
						if (StringUtils.isBlank(numCivico)) {
						   addMessage(newMessages,"_soggettoDelegato_numcivico", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						   logger.warn("[SoggettoDelegato::modelValidate] numCivico non valorizzato");
						} else {
						  logger.info("[SoggettoDelegato::modelValidate] numCivico:" + numCivico);
						}
						
						// cap
						String cap = (String) _soggettoDelegato.getCap();
						if (StringUtils.isBlank(cap)) {
						   addMessage(newMessages,"_soggettoDelegato_cap", ERRMSG_CAMPO_OBBLIGATORIO_SD);
						   logger.warn("[SoggettoDelegato::modelValidate] cap non valorizzato");
						} else {
						  logger.info("[SoggettoDelegato::modelValidate] cap:" + cap);
						}
					}
				} else {
					logger.warn("[SoggettoDelegato::modelValidate] _soggettoDelegato non presente o vuoto");
				}
		} catch(Exception ex) {
			logger.error("[SoggettoDelegato::modelValidate] ", ex);
		}
		finally {
			logger.info("[SoggettoDelegato::modelValidate] _soggettoDelegato END");
		}
		   
		return newMessages;
	}

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
		// nothing to validate
		return null;
	}

}
