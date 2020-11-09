/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PianoSpeseProgettiComuniVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	SpeseProgettiComuniVO[] speseList;

	public SpeseProgettiComuniVO[] getSpeseList() {
		return speseList;
	}

	public void setSpeseList(SpeseProgettiComuniVO[] speseList) {
		this.speseList = speseList;
	}
	
	
}
