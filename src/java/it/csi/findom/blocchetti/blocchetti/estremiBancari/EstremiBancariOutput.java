/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.estremiBancari;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EstremiBancariOutput extends CommonalityOutput {

	@MapTo(target=NAMESPACE)
	Integer maxLengthIBAN;	
	
	@MapTo(target=NAMESPACE)
	Integer maxLengthBIC;
	
	/** jira 2018 - */
	@MapTo(target=NAMESPACE)
	String intestatarioCC;
	
	public Integer getMaxLengthIBAN() {
		return maxLengthIBAN;
	}

	public void setMaxLengthIBAN(Integer maxLengthIBAN) {
		this.maxLengthIBAN = maxLengthIBAN;
	}

	public Integer getMaxLengthBIC() {
		return maxLengthBIC;
	}

	public void setMaxLengthBIC(Integer maxLengthBIC) {
		this.maxLengthBIC = maxLengthBIC;
	}

	public String getIntestatarioCC() {
		return intestatarioCC;
	}

	public void setIntestatarioCC(String intestatarioCC) {
		this.intestatarioCC = intestatarioCC;
	}
}
