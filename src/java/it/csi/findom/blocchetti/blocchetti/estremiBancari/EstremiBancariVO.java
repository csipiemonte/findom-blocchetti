/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.estremiBancari;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EstremiBancariVO extends CommonalityVO {
			 
	private static final long serialVersionUID = 1L;

@MapTo(target = INHERIT, name = "iban")
  String iban;

  @MapTo(target = INHERIT, name = "bic")
  String bic;
	  
  @MapTo(target = INHERIT, name = "intestatarioCC")
  String intestatarioCC;

	public String getIban() {
		return iban;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public String getBic() {
		return bic;
	}
	
	public void setBic(String bic) {
		this.bic = bic;
	}
	
	public String getIntestatarioCC() {
		return intestatarioCC;
	}
	
	public void setIntestatarioCC(String intestatarioCC) {
		this.intestatarioCC = intestatarioCC;
	}
}
