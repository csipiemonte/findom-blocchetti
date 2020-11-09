/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.azienda;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AziendaAAEPVO extends CommonalityVO {

	@MapTo(target = INHERIT, name = "capitaleSociale")
	String capitaleSociale;
	@MapTo(target = INHERIT, name = "capitaleSottoscritto")
	String capitaleSottoscritto;
	@MapTo(target = INHERIT, name = "capitaleVersato")
	String capitaleVersato;
	
	public String getCapitaleSociale() {
		return capitaleSociale;
	}
	public String getCapitaleSottoscritto() {
		return capitaleSottoscritto;
	}
	public String getCapitaleVersato() {
		return capitaleVersato;
	}
	public void setCapitaleSociale(String capitaleSociale) {
		this.capitaleSociale = capitaleSociale;
	}
	public void setCapitaleSottoscritto(String capitaleSottoscritto) {
		this.capitaleSottoscritto = capitaleSottoscritto;
	}
	public void setCapitaleVersato(String capitaleVersato) {
		this.capitaleVersato = capitaleVersato;
	}

	
	
}
