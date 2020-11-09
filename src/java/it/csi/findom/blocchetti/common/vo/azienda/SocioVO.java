/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.azienda;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SocioVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "denominazione")
	String denominazione;
	
	@MapTo(target = INHERIT, name = "codiceFiscale")
	String codiceFiscale;
	
	/** 2R :: inizio Jira 1592 */
	@MapTo(target = INHERIT, name = "codiceTipoSocio")
	String codiceTipoSocio;

	@MapTo(target = INHERIT, name = "descrTipologiaSocio")
	String descrTipologiaSocio;
	/** 2R :: fine Jira 1592 */
	
	
	@MapTo(target = INHERIT, name = "quota")
	String quota;
	
	

	/* Get | Set */
	public String getDenominazione() {
		return denominazione;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public String getCodiceTipoSocio() {
		return codiceTipoSocio;
	}

	public void setCodiceTipoSocio(String codiceTipoSocio) {
		this.codiceTipoSocio = codiceTipoSocio;
	}

	public String getDescrTipologiaSocio() {
		return descrTipologiaSocio;
	}

	public void setDescrTipologiaSocio(String descrTipologiaSocio) {
		this.descrTipologiaSocio = descrTipologiaSocio;
	}
	
}
