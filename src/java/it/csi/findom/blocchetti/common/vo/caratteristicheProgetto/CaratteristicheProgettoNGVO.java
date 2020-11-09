/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.caratteristicheProgetto;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CaratteristicheProgettoNGVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;
	@MapTo(target=MapTarget.INHERIT)
	TipologiaInterventoVO[] tipologiaInterventoList;
	
	public TipologiaInterventoVO[] getTipologiaInterventoList() {
		return tipologiaInterventoList;
	}

	public void setTipologiaInterventoList(
			TipologiaInterventoVO[] tipologiaInterventoList) {
		this.tipologiaInterventoList = tipologiaInterventoList;
	}
	
	
	
}
