/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.estremiBancari;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EstremiBancariInput extends CommonalityInput {

	@MapTo(target=MODEL,name="_estremiBancari")
	EstremiBancariVO _estremiBancari;
	  
	@MapTo(target=CONF,name="_estremiBancari_duplicazioneIban")
	String _estremiBancari_duplicazioneIban;
	
	@MapTo(target=CONF,name="_estremiBancari_bicObbligatorio")
	String _estremiBancari_bicObbligatorio;
	
	/** jira: 2018 - */
	@MapTo(target=CONF,name="_estremiBancari_intestatario_denominazione")
	String _estremiBancari_intestatario_denominazione;
	  
	@MapTo(target = MODEL, name = "_operatorePresentatore")
	OperatorePresentatoreVo operatorePresentatore;
}
