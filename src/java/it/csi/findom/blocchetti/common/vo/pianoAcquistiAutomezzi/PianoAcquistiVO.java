/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianoAcquistiAutomezzi;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PianoAcquistiVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	DettaglioAcquistiVO[] dettaglioAcquistiVOList;
	
	@MapTo(target = MapTarget.INHERIT)
	DettaglioVoceSpesaInterventoVO[] pianoAcquistiVOList;
	
	@MapTo(target = MapTarget.INHERIT)
	String totale;
	
	@MapTo(target=INHERIT)
	DettaglioAcquistiVO dettaglioAcquistiVO;

	public DettaglioAcquistiVO[] getDettaglioAcquistiVOList() {
		return dettaglioAcquistiVOList;
	}

	public void setDettaglioAcquistiVOList(
			DettaglioAcquistiVO[] dettaglioAcquistiVOList) {
		this.dettaglioAcquistiVOList = dettaglioAcquistiVOList;
	}

	public DettaglioVoceSpesaInterventoVO[] getPianoAcquistiVOList() {
		return pianoAcquistiVOList;
	}

	public void setPianoAcquistiVOList(
			DettaglioVoceSpesaInterventoVO[] pianoAcquistiVOList) {
		this.pianoAcquistiVOList = pianoAcquistiVOList;
	}

	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}

	public DettaglioAcquistiVO getDettaglioAcquistiVO() {
		return dettaglioAcquistiVO;
	}

	public void setDettaglioAcquistiVO(DettaglioAcquistiVO dettaglioAcquistiVO) {
		this.dettaglioAcquistiVO = dettaglioAcquistiVO;
	}
	
}
