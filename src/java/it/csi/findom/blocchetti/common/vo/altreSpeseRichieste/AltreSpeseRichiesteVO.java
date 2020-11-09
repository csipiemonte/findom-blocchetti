/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.altreSpeseRichieste;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AltreSpeseRichiesteVO extends CommonalityVO{

	private static final long serialVersionUID = 1L;
	

	@MapTo(target=INHERIT)
	AltreSpeseRichiesteItemVO altreSpeseRichiesteItemVO;

	@MapTo(target=INHERIT)
	String daCancellare;


	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	public AltreSpeseRichiesteItemVO getAltreSpeseRichiesteItemVO() {
		return altreSpeseRichiesteItemVO;
	}

	public void setAltreSpeseRichiesteItemVO(
			AltreSpeseRichiesteItemVO altreSpeseRichiesteItemVO) {
		this.altreSpeseRichiesteItemVO = altreSpeseRichiesteItemVO;
	}


	
	
	
}
