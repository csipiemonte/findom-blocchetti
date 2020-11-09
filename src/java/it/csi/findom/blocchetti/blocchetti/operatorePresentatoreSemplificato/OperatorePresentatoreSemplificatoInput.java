/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.operatorePresentatoreSemplificato;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONTEXT;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class OperatorePresentatoreSemplificatoInput extends CommonalityInput {

	@MapTo(target = MODEL, name = "_operatorePresentatore")
	OperatorePresentatoreVo operatorePresentatore;

//	@MapTo(target = CONF, name = "operatorePresentatore_dipartimenti")
//	String operatorePresentatore_dipartimenti;
	
	@MapTo(target = CONF, name = "_operatorePresentatore_ateco")
	String _operatorePresentatore_ateco;
	
	@MapTo(target = CONF, name = "_operatorePresentatore_indirizzoPec")
	String _operatorePresentatore_indirizzoPec;
	
	@MapTo(target = CONF, name = "_operatorePresentatore_settore_attivita_economica")
	String _operatorePresentatore_settore_attivita_economica;
	
	/** : Jira: x sostegno imprese  - */
	@MapTo(target = CONF, name = "_operatorePresentatore_formaGiuridica_lavoratoreAutonomo")
	String _operatorePresentatore_formaGiuridica_lavoratoreAutonomo;
	
	/** : Jira: x sostegno imprese  - */
	@MapTo(target = CONF, name = "_operatorePresentatore_mex_alert")
	String _operatorePresentatore_mex_alert;
	
	@MapTo(target=CONTEXT,name="xformId")
	String xformId;

	@MapTo(target=CONTEXT,name="xformProg")
	Integer xformProg;

	@MapTo(target=CONTEXT,name="xformName")
	String xformName;
	
	/** Jira: 1974 - */
	@MapTo(target = CONF, name = "_operatorePresentatore_cir")
	String _operatorePresentatore_cir;
	
	/** Jira: 2005 - */
	@MapTo(target = CONF, name = "_operatorePresentatore_codiceAps")
	String _operatorePresentatore_codiceAps;
	
	/** Jira: 2010 - */
	@MapTo(target = CONF, name = "_operatorePresentatore_dataIscrLimMax")
	String _operatorePresentatore_dataIscrLimMax;
	
}
