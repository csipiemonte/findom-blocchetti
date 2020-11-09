/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.cultPianospese;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CultPianoSpeseVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	DettaglioCostiCulturaVO[] dettaglioCostiList;
	
	@MapTo(target = MapTarget.INHERIT)
	DettaglioVoceSpesaInterventoCulturaVO[] pianoSpeseList;
	
	@MapTo(target = MapTarget.INHERIT)
	String totale;

	@MapTo(target = MapTarget.INHERIT)
	String quotaParte;
	
	@MapTo(target = MapTarget.INHERIT)
	String speseConnesseAttivita;
	
	@MapTo(target = MapTarget.INHERIT)
	String speseGeneraliEFunz;
		
	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}

	public DettaglioCostiCulturaVO[] getDettaglioCostiList() {
		return dettaglioCostiList;
	}

	public void setDettaglioCostiList(DettaglioCostiCulturaVO[] dettaglioCostiList) {
		this.dettaglioCostiList = dettaglioCostiList;
	}

	public DettaglioVoceSpesaInterventoCulturaVO[] getPianoSpeseList() {
		return pianoSpeseList;
	}

	public void setPianoSpeseList(
			DettaglioVoceSpesaInterventoCulturaVO[] pianoSpeseList) {
		this.pianoSpeseList = pianoSpeseList;
	}

	public String getQuotaParte() {
		return quotaParte;
	}

	public void setQuotaParte(String quotaParte) {
		this.quotaParte = quotaParte;
	}

	public String getSpeseConnesseAttivita() {
		return speseConnesseAttivita;
	}

	public String getSpeseGeneraliEFunz() {
		return speseGeneraliEFunz;
	}

	public void setSpeseConnesseAttivita(String speseConnesseAttivita) {
		this.speseConnesseAttivita = speseConnesseAttivita;
	}

	public void setSpeseGeneraliEFunz(String speseGeneraliEFunz) {
		this.speseGeneraliEFunz = speseGeneraliEFunz;
	}

}
