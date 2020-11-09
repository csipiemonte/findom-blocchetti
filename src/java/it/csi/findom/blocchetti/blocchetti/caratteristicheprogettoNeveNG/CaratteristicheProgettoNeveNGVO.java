/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CaratteristicheProgettoNeveNGVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;
	@MapTo(target=MapTarget.INHERIT)
	TipologiaInterventoNeveVO[] tipologiaInterventoList;

	public TipologiaInterventoNeveVO[] getTipologiaInterventoList() {
		return tipologiaInterventoList;
	}

	public void setTipologiaInterventoList(
			TipologiaInterventoNeveVO[] tipologiaInterventoList) {
		this.tipologiaInterventoList = tipologiaInterventoList;
	}
	
}
