/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.dichiarazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RichiestaContributoNaturaStataleItemVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	@MapTo(target=INHERIT)
	String denominazioneStruttura;
	@MapTo(target=INHERIT)
	String denominazioneProgramma;
	
	public String getDenominazioneStruttura() {
		return denominazioneStruttura;
	}
	public String getDenominazioneProgramma() {
		return denominazioneProgramma;
	}
	public void setDenominazioneStruttura(String denominazioneStruttura) {
		this.denominazioneStruttura = denominazioneStruttura;
	}
	public void setDenominazioneProgramma(String denominazioneProgramma) {
		this.denominazioneProgramma = denominazioneProgramma;
	}
	
	
}
