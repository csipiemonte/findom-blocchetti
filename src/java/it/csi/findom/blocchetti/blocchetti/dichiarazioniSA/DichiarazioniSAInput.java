/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioniSA;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DichiarazioniSAInput extends CommonalityInput {
	
	 @MapTo(target=MODEL,name = "_dichiarazioniSA")
	 DichiarazioniSAVO dichiarazioniSA;

}
