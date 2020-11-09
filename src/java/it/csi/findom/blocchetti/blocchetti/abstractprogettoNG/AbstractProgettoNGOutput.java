/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.abstractprogettoNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AbstractProgettoNGOutput extends CommonalityOutput {

	@MapTo(target=MapTarget.NAMESPACE)
	String stereotipoDomanda;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String titoloProgettoLabel;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String sintesiProgettoLabel;

	@MapTo(target=MapTarget.NAMESPACE)
	String abstractProgettoLabel;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String titoloProgettoNeve;
	
	/** : Jira 1589 */
	@MapTo(target=MapTarget.NAMESPACE)
	String importoComplessivoBP;
	
	/** : Jira 1579 */
	@MapTo(target=MapTarget.NAMESPACE)
	String studiInCollaborazione;
	
	/** : Jira 1883 */
	@MapTo(target=MapTarget.NAMESPACE)
	String mqCoperture;

}
