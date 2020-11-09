/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.riferimenti;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RiferimentiInput extends CommonalityInput {

	@MapTo(target = CONF, name = "_riferimenti_view_sezione_societa_consulenza")
	String riferimentiViewSezioneSocietaConsulenza;
	
	/**  : Implementato controllo custom per compilazione obbligatoria in Bando: Empowerment Finanziamento */
	@MapTo(target = CONF, name = "_riferimenti_obbligo_compilazione")
	String _riferimenti_obbligo_compilazione;
	
	@MapTo(target = REQUEST, name = "_riferimenti.societa.provincia")
	String riferimentiSocietaProvincia;
	
	@MapTo(target = MODEL, name = "_riferimenti")
	RiferimentiVO riferimenti;
}
