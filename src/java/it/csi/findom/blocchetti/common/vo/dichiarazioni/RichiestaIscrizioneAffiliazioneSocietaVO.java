/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.dichiarazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RichiestaIscrizioneAffiliazioneSocietaVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	RichiestaIscrizioneAffiliazioneSocietaItemVO richiestaIscrizioneAffiliazione;

	@MapTo(target=INHERIT)
	String daCancellare;	

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	public RichiestaIscrizioneAffiliazioneSocietaItemVO getRichiestaIscrizioneAffiliazione() {
		return richiestaIscrizioneAffiliazione;
	}

	public void setRichiestaIscrizioneAffiliazioneSocieta(
			RichiestaIscrizioneAffiliazioneSocietaItemVO richiestaIscrizioneAffiliazione) {
		this.richiestaIscrizioneAffiliazione = richiestaIscrizioneAffiliazione;
	}
	
	
}
