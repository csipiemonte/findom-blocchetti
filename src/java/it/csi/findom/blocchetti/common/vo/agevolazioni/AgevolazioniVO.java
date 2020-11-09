/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.agevolazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AgevolazioniVO extends CommonalityVO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	AgevolazioneItemVO agevolazione;

	@MapTo(target=INHERIT)
	String daCancellare;

	public AgevolazioneItemVO getAgevolazione() {
		return agevolazione;
	}

	public void setAgevolazione(AgevolazioneItemVO agevolazione) {
		this.agevolazione = agevolazione;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}
	
	
}
