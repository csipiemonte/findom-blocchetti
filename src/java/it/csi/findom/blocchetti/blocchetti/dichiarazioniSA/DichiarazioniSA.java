/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioniSA;

import it.csi.findom.blocchetti.common.dao.DichiarazioniSADAO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

//
//Dichiarazioni SemiAutomoatiche
//
// Blocchetto che legge le impostazioni da DB e visualizza le dichiarazioni
//
// Clone del blocchetto "dichiarazioni" di ERPDOM fatto da Ronco
//
public class DichiarazioniSA extends Commonality {


	DichiarazioniSAInput input = new DichiarazioniSAInput();
	
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0,
			List<CommonalityMessage> arg1) throws CommonalityException {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;
		DichiarazioniSAOutput output = new DichiarazioniSAOutput();
		
		Logger logger = Logger.getLogger(info.getLoggerName());
		String prf = "[DichiarazioniSA::inject] ";
		logger.info(prf + " BEGIN");
		
		DichiarazioniSAVO _dichiarazioni = input.dichiarazioniSA;
		
		// leggo le configurazioni sul DB
		List<ElementoDichiarazioniVO> elencoConfDichiarazioni = DichiarazioniSADAO.getConfDichiarazioni(info.getStatusInfo().getNumSportello(), logger);
		
		LinkedHashMap<String,String> dettaglioDichiarazioniInputMap = new LinkedHashMap<String,String>();
		// Carica solo l'elenco dei campi input 
		for (ElementoDichiarazioniVO elemento:elencoConfDichiarazioni) {
			if (elemento.getOutputNameField() != null) {
				dettaglioDichiarazioniInputMap.put(elemento.getOutputNameField(), elemento.getDefaultValueField());
			}
		}
		logger.debug(prf + " dettaglioDichiarazioniInputMap="+dettaglioDichiarazioniInputMap);
		
		
		if (_dichiarazioni == null) {
			
			// Inizializza il blocchetto con i dati dei campi input con i valori di default
			
			logger.debug(prf + " inizializzo blocchetto con i dati dei campi input con i valori di default");
			
			_dichiarazioni = new DichiarazioniSAVO();
			_dichiarazioni.listaVoci = new DichiarazioniVoceVO[dettaglioDichiarazioniInputMap.size()];
			
			int i = 0;
			Iterator<Entry<String,String>> itr = dettaglioDichiarazioniInputMap.entrySet().iterator();
			while (itr.hasNext()) {
			  Entry<String,String> entry = itr.next();
			  DichiarazioniVoceVO elem = new DichiarazioniVoceVO();
			  
			  elem.setVoce(entry.getKey());
			  elem.setValore(entry.getValue());
			  
			  _dichiarazioni.listaVoci[i++] = elem;
			}
			i--;
			logger.debug(prf + " caricate "+i+" voci");
		}

		List<ElementoDichiarazioniVO> listaDichiarazioni = new ArrayList();
		
		// carica i nodi padre
		for (ElementoDichiarazioniVO elemento : elencoConfDichiarazioni) {
			if (elemento.getIdPadre()==null) {
				listaDichiarazioni.add(elemento);
			}
		}
		logger.debug(prf + " caricata lista di nodi padre, size= "+listaDichiarazioni.size());
		
		// scorro i nodi padre caricati e carico i nodi figli di primo livello
		for (ElementoDichiarazioniVO elemElenco : elencoConfDichiarazioni) {
			if (elemElenco.getIdPadre()!=null) {				
				for (ElementoDichiarazioniVO elemLiv1 : listaDichiarazioni) {
					if (elemLiv1.getId().equals(elemElenco.getIdPadre())) {
						// trovato figlio
						if (elemLiv1.getLista()!=null){
							elemLiv1.getLista().add(elemElenco);
						} else {
							List<ElementoDichiarazioniVO> listaElem1 = new ArrayList<ElementoDichiarazioniVO>();
							listaElem1.add(elemElenco);
							elemLiv1.setLista(listaElem1);
						}
					}
				}
			}
		}
		logger.debug(prf + " caricata lista di nodi di primo livello");
		
		// carica secondo livello
		for (ElementoDichiarazioniVO elemElenco : elencoConfDichiarazioni) {
			if (elemElenco.getIdPadre()!=null) {				
				for (ElementoDichiarazioniVO elemLiv1 : listaDichiarazioni) {
					if (elemLiv1.getLista()!=null){
						for (ElementoDichiarazioniVO elemLiv2 : elemLiv1.getLista()) {
							if (elemLiv2.getId().equals(elemElenco.getIdPadre())) {
								// trovato figlio
								if (elemLiv2.getLista()!=null){
									elemLiv2.getLista().add(elemElenco);
								} else {
									List<ElementoDichiarazioniVO> listaElem2 = new ArrayList<ElementoDichiarazioniVO>();
									listaElem2.add(elemElenco);
									elemLiv2.setLista(listaElem2);
								}
							}
						}
					}
				}
			}
		}
		logger.debug(prf + " caricata lista di nodi di secondo livello");
/*
		// carica terzo livello
		for (ElementoDichiarazioniVO elemElenco : elencoConfDichiarazioni) {
			if (elemElenco.getIdPadre()!=null) {				
				for (ElementoDichiarazioniVO elemLiv1 : listaDichiarazioni) {
					if (elemLiv1.getLista()!=null){
						for (ElementoDichiarazioniVO elemLiv2 : elemLiv1.getLista()) {
							if (elemLiv2.getLista()!=null){
								for (ElementoDichiarazioniVO elemLiv3 : elemLiv2.getLista()) {
									if (elemLiv3.getId().equals(elemElenco.getIdPadre())) {
										// trovato figlio
										if (elemLiv3.getLista()!=null){
											elemLiv3.getLista().add(elemElenco);
										} else {
											List<ElementoDichiarazioniVO> listaElem3 = new ArrayList<ElementoDichiarazioniVO>();
											listaElem3.add(elemElenco);
											elemLiv3.setLista(listaElem3);
										}
									}
								}
							}
						}
					}
					
				}
			}
		}
		logger.debug(prf + " caricata lista di nodi di terzo livello");
		
		// carica quarto livello
		for (ElementoDichiarazioniVO elemElenco : elencoConfDichiarazioni) {
			if (elemElenco.getIdPadre()!=null) {				
				for (ElementoDichiarazioniVO elemLiv1 : listaDichiarazioni) {
					if (elemLiv1.getLista()!=null){
						for (ElementoDichiarazioniVO elemLiv2 : elemLiv1.getLista()) {
							if (elemLiv2.getLista()!=null){
								for (ElementoDichiarazioniVO elemLiv3 : elemLiv2.getLista()) {
									if (elemLiv3.getLista()!=null){
										for (ElementoDichiarazioniVO elemLiv4 : elemLiv3.getLista()) {
											if (elemLiv4.getId().equals(elemElenco.getIdPadre())) {
												// trovato figlio
												if (elemLiv4.getLista()!=null){
													elemLiv4.getLista().add(elemElenco);
												} else {
													List<ElementoDichiarazioniVO> listaElem4 = new ArrayList<ElementoDichiarazioniVO>();
													listaElem4.add(elemElenco);
													elemLiv4.setLista(listaElem4);
												}
											}
										}
									}
								}
							}
						}
					}
					
				}
			}
		}
		logger.debug(prf + " caricata lista di nodi di quarto livello");
*/
		logger.debug(prf + " listaDichiarazioni="+ ( (listaDichiarazioni!=null) ? "" + listaDichiarazioni.size() : "null" ));
		
//		output.dichiarazioniList = listaDichiarazioni;
		output.setDichiarazioniList(listaDichiarazioni);
		
		new ArrayList<ElementoDichiarazioniVO>();
		output.dettaglioDichiarazioniInputMap = dettaglioDichiarazioniInputMap;
		
		logger.info(prf + " END");
		return output;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {

		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
		
		Logger logger = Logger.getLogger(info.getLoggerName());
		String prf = "[DichiarazioniSA::modelValidate] ";
		logger.debug(prf + "BEGIN");
		
		String ERRMSG_CAMPO_OBBLIGATORIO_DICH = "- dato obbligatorio";

		List<DichiarazioniVoceVO> listaDichiarazioniInserite = null;
		DichiarazioniSAVO _dichiarazioni = input.dichiarazioniSA;
		if (_dichiarazioni != null && _dichiarazioni.getListaVoci()!= null) {
			listaDichiarazioniInserite = (List<DichiarazioniVoceVO>)Arrays.asList(_dichiarazioni.getListaVoci());
		}
			
		List<ElementoDichiarazioniVO> elencoConfDichiarazioni = DichiarazioniSADAO.getConfDichiarazioni(info.getStatusInfo().getNumSportello(), logger);
		logger.debug(prf + "caricata elencoConfDichiarazioni, size="+elencoConfDichiarazioni.size());
		
		// Verifica solo l'elenco delle checkbox obbligatorie 
		int i = 0;
		
		for (ElementoDichiarazioniVO elemento:elencoConfDichiarazioni) {
			if (elemento.getOutputNameField() != null) {
				if ("checkbox".equals(elemento.getTypeField())) {
					if (elemento.getMandatory()) {
						boolean mandatory = true;
						if ((elemento.getDependsOn() != null)&&(elemento.getDependsOnValue() != null)) {
							// verifico se nella dichiarazione il campo di dipendenza e' settato con valore che implica obbligatorieta
						
							for (DichiarazioniVoceVO voce : listaDichiarazioniInserite) {
								if (elemento.getDependsOn().equals(voce.getVoce())) {
									if (!elemento.getDependsOnValue().equals(voce.getValore())) {
										// il valore non implica obbligatorieta 
										mandatory = false;
										break; 
									}
								}
							}
						}
						
						if (mandatory) {
							boolean trovato = false;
							for (DichiarazioniVoceVO voce : listaDichiarazioniInserite) {
								if (elemento.getValueSelectedField().equals(voce.getValore()) &&
									elemento.getOutputNameField().equals(voce.getVoce())) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								addMessage(newMessages,"_dichiarazione_checkbox_testo", ERRMSG_CAMPO_OBBLIGATORIO_DICH);
								addMessage(newMessages,"_dichiarazione_checkbox_id",  i+"C");
								logger.debug(prf+" checkbox " + elemento.getOutputNameField() + " non valorizzato ");
							}
						}
					}				
				}
				i++;
			}
		}
		logger.debug(prf+" verificati campi checkbox ");
		

		// Verifica l'elenco dei radio obbligatori
		for (ElementoDichiarazioniVO elemento:elencoConfDichiarazioni) {
			
			//logger.debug(prf+" elemento.getNameField()="+elemento.getNameField()+" ,elemento.getTypeField="+elemento.getTypeField());
			
			if (elemento.getNameField() != null) {
				
// se trovo un elemento di tipo radio_group cerco i suoi figli radio(quelli che hanno stesso nameField)
// Se mandatory=TRUE, verifico se almeno un figlio e' "selected"

				if ("radio_group".equals(elemento.getTypeField())) {
					
					logger.debug(prf+" radio elemento="+elemento.toString());
					
					if (elemento.getMandatory()) {
						logger.debug(prf+" radio Mandatory");
						boolean mandatory = true;
						if ((elemento.getDependsOn() != null)&&(elemento.getDependsOnValue() != null)) {
							// verifico se nella dichiarazione il campo di dipendenza e' settato con valore che implica obbligatorieta'
							
							logger.debug(prf+" radio DependsOn");
							for (DichiarazioniVoceVO voce : listaDichiarazioniInserite) {
								if (elemento.getDependsOn().equals(voce.getVoce())) {
									if (!elemento.getDependsOnValue().equals(voce.getValore())) {
										// il valore non implica obbligatorieta' 
										mandatory = false;
										break; 
									}
									
								}
							}
						}
						logger.debug(prf+" radio mandatory="+mandatory);
						if (mandatory) {
							// cerca i radio del gruppo
							boolean trovato = false;
							for (ElementoDichiarazioniVO elem:elencoConfDichiarazioni) {
								if ("radio".equals(elem.getTypeField())) {
									
									logger.debug(prf+" radio elem="+elem.toString());
									
									if (elemento.getNameField().equals(elem.getNameField())) {
										// radio appartenente al gruppo
										
										// verifico se nel blocchetto e' "selected"
										for (DichiarazioniVoceVO voce : listaDichiarazioniInserite) {
											if (elem.getOutputNameField().equals(voce.getVoce()) && 
												elem.getValueSelectedField().equals(voce.getValore())) {
												logger.debug(prf+" TRUE"); 
												trovato = true;
												break;
											}
										}
										if (trovato) break;
									}	
								}
							}
							if (!trovato) {
								// cerca tutti i radio del gruppo se almeno uno ha valueselected
								addMessage(newMessages,"_dichiarazione_radio_group_testo", ERRMSG_CAMPO_OBBLIGATORIO_DICH);
								addMessage(newMessages,"_dichiarazione_radio_group_id", elemento.getNameField());
								logger.debug(prf+" radio " + elemento.getNameField() + " non valorizzato ");
							}
						}
					}
				}
			}
		}
		logger.debug(prf+" verificati campi radio ");
		
		logger.info(prf +" END");
		return newMessages;
	}

}
