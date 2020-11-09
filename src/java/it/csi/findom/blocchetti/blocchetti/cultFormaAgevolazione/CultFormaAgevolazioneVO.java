/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CultFormaAgevolazioneVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String importoRichiesto;
	
	@MapTo(target=INHERIT)
	String importoErogabile;		    
	
	@MapTo(target=INHERIT)
	String importoMinimoErogabile;
    
	@MapTo(target=INHERIT)
	String speseGeneraliFunz;

	@MapTo(target=INHERIT)
	String speseConnesseAttivita;
	
	@MapTo(target=INHERIT)
	String totaleEntrate;
	
	@MapTo(target=INHERIT)
	String percQuotaParteSpeseGenFunz; 

	
	public String getImportoRichiesto() {
		return importoRichiesto;
	}

	public String getImportoErogabile() {
		return importoErogabile;
	}

	public String getImportoMinimoErogabile() {
		return importoMinimoErogabile;
	}

	public void setImportoRichiesto(String importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public void setImportoErogabile(String importoErogabile) {
		this.importoErogabile = importoErogabile;
	}

	public void setImportoMinimoErogabile(String importoMinimoErogabile) {
		this.importoMinimoErogabile = importoMinimoErogabile;
	}

	public String getSpeseGeneraliFunz() {
		return speseGeneraliFunz;
	}

	public String getSpeseConnesseAttivita() {
		return speseConnesseAttivita;
	}

	public void setSpeseGeneraliFunz(String speseGeneraliFunz) {
		this.speseGeneraliFunz = speseGeneraliFunz;
	}

	public void setSpeseConnesseAttivita(String speseConnesseAttivita) {
		this.speseConnesseAttivita = speseConnesseAttivita;
	}

	public String getTotaleEntrate() {
		return totaleEntrate;
	}

	public void setTotaleEntrate(String totaleEntrate) {
		this.totaleEntrate = totaleEntrate;
	}

	public String getPercQuotaParteSpeseGenFunz() {
		return percQuotaParteSpeseGenFunz;
	}

	public void setPercQuotaParteSpeseGenFunz(String percQuotaParteSpeseGenFunz) {
		this.percQuotaParteSpeseGenFunz = percQuotaParteSpeseGenFunz;
	}

	
}
