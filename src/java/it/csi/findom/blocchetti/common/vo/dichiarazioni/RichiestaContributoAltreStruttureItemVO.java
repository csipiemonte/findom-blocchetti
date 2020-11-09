/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.dichiarazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RichiestaContributoAltreStruttureItemVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String denominazioneDirezione;

	@MapTo(target=INHERIT)
	String denominazioneSettore;
	
	@MapTo(target=INHERIT)
	String normativa;

	public String getDenominazioneDirezione() {
		return denominazioneDirezione;
	}

	public String getDenominazioneSettore() {
		return denominazioneSettore;
	}

	public String getNormativa() {
		return normativa;
	}

	public void setDenominazioneDirezione(String denominazioneDirezione) {
		this.denominazioneDirezione = denominazioneDirezione;
	}

	public void setDenominazioneSettore(String denominazioneSettore) {
		this.denominazioneSettore = denominazioneSettore;
	}

	public void setNormativa(String normativa) {
		this.normativa = normativa;
	}



}
