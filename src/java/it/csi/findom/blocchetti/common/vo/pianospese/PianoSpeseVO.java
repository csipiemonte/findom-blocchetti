/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianospese;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PianoSpeseVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	DettaglioCostiVO[] dettaglioCostiList;
	
	@MapTo(target = MapTarget.INHERIT)
	DettaglioVoceSpesaInterventoVO[] pianoSpeseList;
	
	@MapTo(target = MapTarget.INHERIT)
	String totale;
	
	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}

	public DettaglioCostiVO[] getDettaglioCostiList() {
		return dettaglioCostiList;
	}

	public void setDettaglioCostiList(DettaglioCostiVO[] dettaglioCostiList) {
		this.dettaglioCostiList = dettaglioCostiList;
	}

	public DettaglioVoceSpesaInterventoVO[] getPianoSpeseList() {
		return pianoSpeseList;
	}

	public void setPianoSpeseList(
			DettaglioVoceSpesaInterventoVO[] pianoSpeseList) {
		this.pianoSpeseList = pianoSpeseList;
	}
	
	

}
