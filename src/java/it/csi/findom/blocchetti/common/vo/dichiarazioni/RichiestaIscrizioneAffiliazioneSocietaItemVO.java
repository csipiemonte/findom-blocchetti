/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.dichiarazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RichiestaIscrizioneAffiliazioneSocietaItemVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=INHERIT)
	String denominazioneAffiliato;
	
	@MapTo(target=INHERIT)
	String dataAffiliazione;

	public String getDenominazioneAffiliato() {
		return denominazioneAffiliato;
	}

	public void setDenominazioneAffiliato(String denominazioneAffiliato) {
		this.denominazioneAffiliato = denominazioneAffiliato;
	}

	public String getDataAffiliazione() {
		return dataAffiliazione;
	}

	public void setDataAffiliazione(String dataAffiliazione) {
		this.dataAffiliazione = dataAffiliazione;
	}
	

	

	
	
}
