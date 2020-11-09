/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioni96;

import it.csi.findom.blocchetti.common.vo.agevolazioni.AgevolazioniVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DichiarazioniNGOutput extends CommonalityOutput {
	
	
	@MapTo(target=MapTarget.NAMESPACE)
	AgevolazioniVO[] agevolazioniInseriteList;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String ratingLegalita;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String sedeAttiva;


	@MapTo(target=MapTarget.NAMESPACE)
	String sedeNoPiemonte;
}
