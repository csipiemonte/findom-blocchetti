/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class ParametriCalcoloBVO  extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String importoMassimoErogabile;		    
	
	@MapTo(target=INHERIT)
	String importoMinimoErogabile;
    
	@MapTo(target=INHERIT)
	String percQuotaParteSpeseGenFunz;

	@MapTo(target=INHERIT)
	String percMassimaContributoErogabile;

	/**
	 * Get | Set
	 * @return
	 */
	public String getImportoMassimoErogabile() {
		return importoMassimoErogabile;
	}

	public String getImportoMinimoErogabile() {
		return importoMinimoErogabile;
	}

	public String getPercQuotaParteSpeseGenFunz() {
		return percQuotaParteSpeseGenFunz;
	}

	public String getPercMassimaContributoErogabile() {
		return percMassimaContributoErogabile;
	}

	public void setImportoMassimoErogabile(String importoMassimoErogabile) {
		this.importoMassimoErogabile = importoMassimoErogabile;
	}

	public void setImportoMinimoErogabile(String importoMinimoErogabile) {
		this.importoMinimoErogabile = importoMinimoErogabile;
	}

	public void setPercQuotaParteSpeseGenFunz(String percQuotaParteSpeseGenFunz) {
		this.percQuotaParteSpeseGenFunz = percQuotaParteSpeseGenFunz;
	}

	public void setPercMassimaContributoErogabile(
			String percMassimaContributoErogabile) {
		this.percMassimaContributoErogabile = percMassimaContributoErogabile;
	}

	@Override
	public String toString() {
		return "ParametriCalcoloBVO [importoMassimoErogabile="
				+ importoMassimoErogabile + ", importoMinimoErogabile="
				+ importoMinimoErogabile + ", percQuotaParteSpeseGenFunz="
				+ percQuotaParteSpeseGenFunz
				+ ", percMassimaContributoErogabile="
				+ percMassimaContributoErogabile + "]";
	}
	
}
