/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.riferimenti;

import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.commonality.ControlCodFisc;
import it.csi.findom.blocchetti.commonality.ControlEmail;
import it.csi.findom.blocchetti.commonality.ControlPartitaIVA;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Riferimenti extends Commonality {

	RiferimentiInput input = new RiferimentiInput();

	@Override
	public RiferimentiInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[Riferimenti::inject] BEGIN");

		try {
			RiferimentiOutput ns = new RiferimentiOutput();
			
			
			if (input.riferimentiViewSezioneSocietaConsulenza!=null &&
					input.riferimentiViewSezioneSocietaConsulenza.equals("true"))
			{
				List<ProvinciaVO> provinciaList = new ArrayList<>();
				List<ComuneVO> comuneList = new ArrayList<>();
				
				if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
					
					// ottiene la lista delle province				
					provinciaList = LuoghiDAO.getProvinciaList(logger);

					// ottiene key provincia postato per ottenere elenco comuni di nascita sull'onchange
					String codiceProvincia = input.riferimentiSocietaProvincia;
					if (StringUtils.isNotBlank(codiceProvincia)) {
						comuneList = LuoghiDAO.getComuneList(codiceProvincia, logger);					
					} else {
						// passo necessario per riottenere la lista dei comuni se ha gia salvato almeno una volta un valore
						RiferimentiVO societaMap = input.riferimenti;	
						if (societaMap != null  && societaMap.societa !=null  && societaMap.societa.provincia !=null && !societaMap.societa.provincia.equals("")) {						
							String provincia = societaMap.societa.provincia;
							logger.info("[Riferimenti::inject] provincia:" + provincia);
							if (StringUtils.isNotBlank(provincia)) {
								comuneList = LuoghiDAO.getComuneList(provincia, logger);
							}
						}
					}
				}
				
				ns.provinciaList = provinciaList;
				ns.comuneList = comuneList;
				
				
			}
			
			logger.info("[Riferimenti::inject]  _Riferimenti  END");

			return ns;
		} catch (CommonalityException ex) {
			throw new CommonalityException(ex);
		} finally {
			logger.info("[Riferimenti::inject] END");
		}
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[Riferimenti::modelValidate] _riferimenti BEGIN");
		
		String ERRMSG_SEZIONI = "- compilare la sezione";
		//<#if _riferimenti_view_sezione_societa_consulenza?? >
		if (input.riferimentiViewSezioneSocietaConsulenza != null ) {
			if (input.riferimentiViewSezioneSocietaConsulenza.equals("true") ) {
				ERRMSG_SEZIONI = "- compilare almeno una sezione";
				logger.info("message: " + ERRMSG_SEZIONI);
			}	
		}
		
		String ERRMSG_CAMPO_OBBL = "- il campo é obbligatorio";
		String ERRMSG_CF_ALF = "- codice fiscale non valido<br /> - inserire solo caratteri alfanumerici";
		String ERRMSG_CF_NOGOOD = "- codice fiscale formalmente non corretto";
		String ERRMSG_CF_LUNG = "- lunghezza del codice fiscale non valida";
		String ERRMSG_COGN_ALFA = "- cognome non valido<br /> - inserire solo caratteri alfabetici";
		String ERRMSG_NOME_ALFA = "- nome non valido<br /> - inserire solo caratteri alfabetici";
		String ERRMSG_EMAIL_NOGOOD = "- indirizzo email formalmente non corretto";
		String ERRMSG_IVA_NOGOOD = "- partita IVA formalmente non corretta";
		String ERRMSG_SOCIETA = "- compilare i dati della società";
		String ERRMSG_CONSULENTE = "- compilare i dati del consulente";
		String ERRMSG_COMPILAZIONE_SEZIONE_OBBLIGATORIA = "- Compilare obbligatoriamente almeno la sezione: Persona dell'Ente/Impresa autorizzata ad intrattenere contatti";

		try {
					RiferimentiVO _riferimenti = input.riferimenti;
					logger.warn("[Riferimenti::modelValidate] _riferimenti="+_riferimenti);
					logger.info("[Riferimenti::modelValidate] _riferimenti="+_riferimenti);
	
					if(_riferimenti != null) {
	
						// personaImpresa
						PersonaImpresaVO personaImpresa = input.riferimenti.personaImpresa;
						logger.warn("[Riferimenti::modelValidate] personaImpresa="+personaImpresa);
						logger.info("[Riferimenti::modelValidate] personaImpresa="+personaImpresa);
						
						boolean personaImpresaCompilato = true;
						
						if(personaImpresa != null && !personaImpresa.isEmpty()) {
							
							String codiceFiscaleImp = (String) personaImpresa.getCodiceFiscale();
							logger.info("[Riferimenti::modelValidate] codiceFiscaleImp="+codiceFiscaleImp);
							
							String cognomeImp = (String) personaImpresa.getCognome();
							logger.info("[Riferimenti::modelValidate] cognomeImp="+cognomeImp);
							
							String nomeImp = (String) personaImpresa.getNome();
							logger.info("[Riferimenti::modelValidate] nomeImp="+nomeImp);
							
							String telefonoImp = (String) personaImpresa.getTelefono();
							logger.info("[Riferimenti::modelValidate] telefonoImp="+telefonoImp);
							
							String emailImp = (String) personaImpresa.getEmail();
							logger.info("[Riferimenti::modelValidate] emailImp="+emailImp);
							
							if(StringUtils.isBlank(codiceFiscaleImp) &&
								StringUtils.isBlank(cognomeImp) &&
								StringUtils.isBlank(nomeImp) &&
								StringUtils.isBlank(telefonoImp) &&
								StringUtils.isBlank(emailImp)){
								
								// tutti i campi vuoti
								personaImpresaCompilato = false;
	
							}else{
							
								// Codice Fiscale
								ControlCodFisc ctrlCF = new ControlCodFisc(codiceFiscaleImp, logger);
								
								if (StringUtils.isBlank(codiceFiscaleImp)) {
									addMessage(newMessages,"_riferimenti_personaImpresa_codiceFiscale", ERRMSG_CAMPO_OBBL);
									logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");
								} else {
									if (codiceFiscaleImp.length() == 16 || codiceFiscaleImp.length() == 11) {
										if (codiceFiscaleImp.length() == 16) {
											if (!StringUtils.isAlphanumeric(codiceFiscaleImp)) {
												addMessage(newMessages,"_riferimenti_personaImpresa_codiceFiscale", ERRMSG_CF_ALF);
												logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");	
											} else if(!ctrlCF.controllaCheckDigit()) {
												addMessage(newMessages,"_riferimenti_personaImpresa_codiceFiscale", ERRMSG_CF_NOGOOD);
												logger.warn("[Riferimenti::modelValidate] codiceFiscale formalmente non corretto");
											}
										} else {
											
											//controllo cod fiscale (partita iva)
									        if (!ControlPartitaIVA.controllaPartitaIVA(codiceFiscaleImp)){
			 			                    addMessage(newMessages,"_riferimenti_personaImpresa_codiceFiscale", ERRMSG_CF_NOGOOD);
											    logger.warn("[Riferimenti::modelValidate] codiceFiscale personaImpresa (partita iva) formalmente non corretto");
						                    }							
										}
										logger.debug("[Riferimenti::modelValidate] codiceFiscale:" + codiceFiscaleImp);
									}else{
										addMessage(newMessages,"_riferimenti_personaImpresa_codiceFiscale", ERRMSG_CF_LUNG);
										logger.warn("[Riferimenti::modelValidate] codiceFiscale o partita IVA di lunghezza non valida");
									}
								}
								
								// Cognome
								if (StringUtils.isBlank(cognomeImp)) {
									addMessage(newMessages,"_riferimenti_personaImpresa_cognome", ERRMSG_CAMPO_OBBL);
									logger.warn("[Riferimenti::modelValidate] cognome non valorizzato");
								} else {
									if (!StringUtils.isAlphaSpaceApostropheCommaDot(cognomeImp)) {
										addMessage(newMessages,"_riferimenti_personaImpresa_cognome", ERRMSG_COGN_ALFA);
										logger.warn("[Riferimenti::modelValidate] cognome:" + cognomeImp + " contiene caratteri non ammessi");
									} 
								}
								
								// Nome
								if (StringUtils.isBlank(nomeImp)) {
									addMessage(newMessages,"_riferimenti_personaImpresa_nome", ERRMSG_CAMPO_OBBL);
									logger.warn("[Riferimenti::modelValidate] nome non valorizzato");
								} else {
									if (!StringUtils.isAlphaSpaceApostropheCommaDot(nomeImp)) {
										addMessage(newMessages,"_riferimenti_personaImpresa_nome", ERRMSG_NOME_ALFA);
										logger.warn("[Riferimenti::modelValidate] nome:" + nomeImp + " contiene caratteri non ammessi");
									} 
								}
								
								// Telefono
								if (StringUtils.isBlank(telefonoImp)) {
									addMessage(newMessages,"_riferimenti_personaImpresa_telefono", ERRMSG_CAMPO_OBBL);
									logger.warn("[Riferimenti::modelValidate] telefono non valorizzato");
								}
								
								// Email
								if (StringUtils.isBlank(emailImp)) {
									addMessage(newMessages,"_riferimenti_personaImpresa_email", ERRMSG_CAMPO_OBBL);
									logger.warn("[Riferimenti::modelValidate] email non valorizzato");
								}else{
									 //MB2018_06_21 ini jira 914
//									if (emailImp.indexOf('@') == -1 || emailImp.startsWith("@") || emailImp.endsWith("@")){			
//										addMessage(newMessages,"_riferimenti_personaImpresa_email", ERRMSG_EMAIL_NOGOOD);
//										logger.warn("[Riferimenti::modelValidate]  indirizzo email formalmente non corretto");
//									}
									if(!ControlEmail.ctrlFormatoIndirizzoEmail(emailImp)){
										addMessage(newMessages,"_riferimenti_personaImpresa_email", ERRMSG_EMAIL_NOGOOD);						
										logger.warn("[Riferimenti::modelValidate]  indirizzo email formalmente non corretto");
									}else{				
										logger.debug("[Riferimenti::modelValidate]  indirizzo email:" + emailImp);
									}
									//MB2018_06_21 fine
									
								}
							}
						} else {
							personaImpresaCompilato = false;
							
							if ("true".equals(input._riferimenti_obbligo_compilazione) && !personaImpresaCompilato)
							{
								addMessage(newMessages,"_riferimenti_personaImpresa", ERRMSG_COMPILAZIONE_SEZIONE_OBBLIGATORIA);
								logger.info("[Riferimenti::modelValidate] Obbligatorio sezione da compilare!");
							}
						}
						
						// societa
						boolean societaCompilato = false;
						
						if (input.riferimentiViewSezioneSocietaConsulenza != null ) {
							if (input.riferimentiViewSezioneSocietaConsulenza.equals("true") ) {
								
	
						SocietaVO societa = input.riferimenti.societa;
						logger.warn("[Riferimenti::modelValidate] societa="+societa);
	
						if(societa != null && !societa.isEmpty())
							societaCompilato = true;
						
						// consulente
						ConsulenteVO consulente = input.riferimenti.consulente;
	
						logger.warn("[Riferimenti::modelValidate] consulente="+consulente);
						
						if((societa == null || societa.isEmpty()) &&  (consulente==null || consulente.isEmpty())) {
							societaCompilato = false;
						} else {
							if(societa != null && !societa.isEmpty() && (consulente==null || consulente.isEmpty())) {
								// Msg per inserimento dati consulente
								addMessage(newMessages,"_riferimenti", ERRMSG_CONSULENTE);
							} else {
								if((societa == null || societa.isEmpty()) && (consulente!=null && !consulente.isEmpty())) {
									// msg inserimento dati società
									addMessage(newMessages,"_riferimenti", ERRMSG_SOCIETA);
								} else {
									// società e consulenza NOT null
									String ragioneSociale = (String) societa.getRagioneSociale();
									String codiceFiscale = (String) societa.getCodiceFiscale();
									String partitaIVA = (String) societa.getPartitaIVA();
									String provincia = (String) societa.getProvincia();
									String comune = (String) societa.getComune();
									String cap = (String) societa.getCap();
									String indirizzo = (String) societa.getIndirizzo();
									String numeroCivico = (String) societa.getNumeroCivico();
									String telefonoSoc = (String) societa.getTelefono();
									String emailSoc = (String) societa.getEmail();
							
									String codiceFiscaleConsul = (String) consulente.getCodiceFiscale();
									String cognomeConsul = (String) consulente.getCognome();
									String nomeConsul =  (String) consulente.getNome();
									String telefonoConsul = (String) consulente.getTelefono();
									String emailConsul = (String) consulente.getEmail();
							
									if(StringUtils.isBlank(ragioneSociale) &&
										StringUtils.isBlank(codiceFiscale) &&
										StringUtils.isBlank(partitaIVA) &&
										StringUtils.isBlank(provincia) &&
										StringUtils.isBlank(comune) &&
										StringUtils.isBlank(cap) &&
										StringUtils.isBlank(indirizzo) &&
										StringUtils.isBlank(numeroCivico) &&
										StringUtils.isBlank(telefonoSoc) &&
										StringUtils.isBlank(emailSoc) && 
										StringUtils.isBlank(codiceFiscaleConsul) &&
										StringUtils.isBlank(cognomeConsul) &&
										StringUtils.isBlank(nomeConsul) &&
										StringUtils.isBlank(telefonoConsul) &&
										StringUtils.isBlank(emailConsul)) {				
																
										// tutti i campi vuoti
										societaCompilato = false;					
								
									} else {
								
										// codiceFiscale
										ControlCodFisc ctrlCF2 = new ControlCodFisc(codiceFiscale, logger);
								
										if (StringUtils.isBlank(codiceFiscale)) {
											addMessage(newMessages,"_riferimenti_societa_codiceFiscale", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");
										}else{
											if (codiceFiscale.length() == 16 || codiceFiscale.length() == 11) {
												if (codiceFiscale.length() == 16) {
													if (!StringUtils.isAlphanumeric(codiceFiscale)) {
														addMessage(newMessages,"_riferimenti_societa_codiceFiscale", ERRMSG_CF_ALF);
														logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");	
													} else if(!ctrlCF2.controllaCheckDigit()) {
														addMessage(newMessages,"_riferimenti_societa_codiceFiscale", ERRMSG_CF_NOGOOD);
														logger.warn("[Riferimenti::modelValidate] codiceFiscale formalmente non corretto");
													}
												} else {
										
													//controllo cod fiscale (partita iva)
									   			    if(!ControlPartitaIVA.controllaPartitaIVA(codiceFiscale)){
			 			                       			addMessage(newMessages,"_riferimenti_societa_codiceFiscale", ERRMSG_CF_NOGOOD);
											   			logger.warn("[Riferimenti::modelValidate] codiceFiscale societa (partita iva) formalmente non corretto");
						                    		}								
												}
												logger.debug("[Riferimenti::modelValidate] codiceFiscale:" + codiceFiscale);
											} else {
												addMessage(newMessages,"_riferimenti_societa_codiceFiscale", ERRMSG_CF_LUNG);
												logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");
											}
										}
								
										// partitaIVA
										if (StringUtils.isBlank(partitaIVA)) {
											addMessage(newMessages,"_riferimenti_societa_partitaIVA", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] partitaIVA non valorizzato");
										}else{
										   if (!ControlPartitaIVA.controllaPartitaIVA(partitaIVA)){
											  addMessage(newMessages,"_riferimenti_societa_partitaIVA", ERRMSG_IVA_NOGOOD);
											  logger.warn("[Riferimenti::modelValidate]  partita IVA formalmente non corretta");
										   }
										}
									
										// RagioneSociale
										if (StringUtils.isBlank(ragioneSociale)) {
											addMessage(newMessages,"_riferimenti_societa_ragioneSociale", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] ragioneSociale non valorizzato");
										}
								
										// Provincia
										if (StringUtils.isBlank(provincia)) {
											addMessage(newMessages,"_riferimenti_societa_provincia", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] provincia non valorizzato");
										}
								
										// Comune
										if (StringUtils.isBlank(comune)) {
											addMessage(newMessages,"_riferimenti_societa_comune", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] comune non valorizzato");
										}
								
										// CAP
										if (StringUtils.isBlank(cap)) {
											addMessage(newMessages,"_riferimenti_societa_cap", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] cap non valorizzato");
										}
								
										// Indirizzo
										if (StringUtils.isBlank(indirizzo)) {
											addMessage(newMessages,"_riferimenti_societa_indirizzo", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] indirizzo non valorizzato");
										}
								
										// Numero civico
										if (StringUtils.isBlank(numeroCivico)) {
											addMessage(newMessages,"_riferimenti_societa_numeroCivico", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] numeroCivico non valorizzato");
										}
								
										// Telefono
										if (StringUtils.isBlank(telefonoSoc)) {
											addMessage(newMessages,"_riferimenti_societa_telefono", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] telefono non valorizzato");
										}
								
										// email
										if (StringUtils.isBlank(emailSoc)) {
											addMessage(newMessages,"_riferimenti_societa_email", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] email non valorizzato");
										}else{											
											//MB2018_06_21 ini jira 914
//											if (emailSoc.indexOf('@') == -1 || emailSoc.startsWith("@") || emailSoc.endsWith("@")){			
//												addMessage(newMessages,"_riferimenti_societa_email", ERRMSG_EMAIL_NOGOOD);
//												logger.warn("[Riferimenti::modelValidate]  indirizzo email formalmente non corretto");
//									 		}
											if(!ControlEmail.ctrlFormatoIndirizzoEmail(emailSoc)){
												addMessage(newMessages,"_riferimenti_societa_email", ERRMSG_EMAIL_NOGOOD);						
												logger.warn("[Riferimenti::modelValidate]  indirizzo email formalmente non corretto");
											}else{				
												logger.debug("[Riferimenti::modelValidate]  indirizzo email:" + emailSoc);
											}
											//MB2018_06_21 fine											
										}
										
										// Codice Fiscale
										ControlCodFisc ctrlCF3 = new ControlCodFisc(codiceFiscaleConsul, logger);
								
										if (StringUtils.isBlank(codiceFiscaleConsul)) {
											addMessage(newMessages,"_riferimenti_consulente_codiceFiscale", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");
										} else {
											if (codiceFiscaleConsul.length() == 16 || codiceFiscaleConsul.length() == 11) {
												if (codiceFiscaleConsul.length() == 16) {
													if (!StringUtils.isAlphanumeric(codiceFiscaleConsul)) {
														addMessage(newMessages,"_riferimenti_consulente_codiceFiscale", ERRMSG_CF_ALF);
														logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");	
													} else if(!ctrlCF3.controllaCheckDigit()) {
														addMessage(newMessages,"_riferimenti_consulente_codiceFiscale", ERRMSG_CF_NOGOOD);
														logger.warn("[Riferimenti::modelValidate] codiceFiscale formalmente non corretto");
													}
												} else {
										  
													//controllo cod fiscale (partita iva)
											        if(!ControlPartitaIVA.controllaPartitaIVA(codiceFiscaleConsul)){
			 					                       addMessage(newMessages,"_riferimenti_consulente_codiceFiscale", ERRMSG_CF_NOGOOD);
													   logger.warn("[Riferimenti::modelValidate] codiceFiscale consulente (partita iva) formalmente non corretto");
						            		        }									
												}
												logger.debug("[Riferimenti::modelValidate] codiceFiscale:" + codiceFiscaleConsul);
											} else {
												addMessage(newMessages,"_riferimenti_consulente_codiceFiscale", ERRMSG_CF_LUNG);
												logger.warn("[Riferimenti::modelValidate] codiceFiscale non valorizzato");
											}
										}
								
										// Cognome
										if (StringUtils.isBlank(cognomeConsul)) {
											addMessage(newMessages,"_riferimenti_consulente_cognome", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] cognome non valorizzato");
										} else {
											if (!StringUtils.isAlphaSpaceApostropheCommaDot(cognomeConsul)) {
												addMessage(newMessages,"_riferimenti_consulente_cognome", ERRMSG_COGN_ALFA);
												logger.warn("[Riferimenti::modelValidate] cognome:" + cognomeConsul + " contiene caratteri non ammessi");
											}
										}
								
										// Nome
										if (StringUtils.isBlank(nomeConsul)) {
											addMessage(newMessages,"_riferimenti_consulente_nome", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] nome non valorizzato");
										} else {
											if (!StringUtils.isAlphaSpaceApostropheCommaDot(nomeConsul)) {
												addMessage(newMessages,"_riferimenti_consulente_nome", ERRMSG_NOME_ALFA);
												logger.warn("[Riferimenti::modelValidate] nome:" + nomeConsul + " contiene caratteri non ammessi");
											}
										}
								
										// Telefono
										if (StringUtils.isBlank(telefonoConsul)) {
											addMessage(newMessages,"_riferimenti_consulente_telefono", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] telefono non valorizzato");
										}
								
										// Email
										if (StringUtils.isBlank(emailConsul)) {
											addMessage(newMessages,"_riferimenti_consulente_email", ERRMSG_CAMPO_OBBL);
											logger.warn("[Riferimenti::modelValidate] email non valorizzato");
										}else{
											//MB2018_06_21 ini jira 914
//											if (emailConsul.indexOf('@') == -1 || emailConsul.startsWith("@") || emailConsul.endsWith("@")){			
//												addMessage(newMessages,"_riferimenti_consulente_email", ERRMSG_EMAIL_NOGOOD);
//												logger.warn("[Riferimenti::modelValidate]  indirizzo email formalmente non corretto");
//											}
											if(!ControlEmail.ctrlFormatoIndirizzoEmail(emailConsul)){
												addMessage(newMessages,"_riferimenti_consulente_email", ERRMSG_EMAIL_NOGOOD);						
												logger.warn("[Riferimenti::modelValidate]  indirizzo email formalmente non corretto");
											}else{				
												logger.debug("[Riferimenti::modelValidate]  indirizzo email:" + emailConsul);
											}
											//MB2018_06_21 fine
										}
									} 
								}//chiusura sezione società e consulente non null
							}
							
						}
						}
					}
						
						if(!personaImpresaCompilato && !societaCompilato){
							logger.warn("[Riferimenti::modelValidate] Nessuna sezione valorizzata.");
							addMessage(newMessages,"_riferimenti", ERRMSG_SEZIONI);
						}
					} else {
						logger.warn("[Riferimenti::modelValidate] Nessuna sezione valorizzata.");
						addMessage(newMessages,"_riferimenti", ERRMSG_SEZIONI);
					}
//				}
//			}
			
		} catch(Exception ex) {
			logger.error("[Riferimenti::modelValidate] ", ex);
		}
		finally {
			logger.info("[Riferimenti::modelValidate] _riferimenti END");
		}

		return newMessages;
	}

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info1, List<CommonalityMessage> inputMessages)
			throws CommonalityException {
		// 
		return null;
	}

	private CommonalityMessage findMessage(List<CommonalityMessage> messages, String field) {
		for (CommonalityMessage message : messages) {
			if (field.equals(message.getField()))
				return message;
		}
		return null;
	}

}
