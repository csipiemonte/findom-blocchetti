/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.rottamazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class ItemRottamazioneVO extends CommonalityVO {
	  
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	RottamazioneVO rottamazioneVO;
	
	@MapTo(target = INHERIT)
	String daCancellare;

	public RottamazioneVO getRottamazioneVO() {
		return rottamazioneVO;
	}

	public void setRottamazioneVO(RottamazioneVO rottamazioneVO) {
		this.rottamazioneVO = rottamazioneVO;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}	
}
