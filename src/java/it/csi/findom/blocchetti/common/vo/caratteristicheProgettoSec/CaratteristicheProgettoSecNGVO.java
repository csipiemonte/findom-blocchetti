/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.caratteristicheProgettoSec;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CaratteristicheProgettoSecNGVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;
	@MapTo(target=MapTarget.INHERIT)
	TipologiaSettoreVO[] tipologiaBandoList;
	
	public TipologiaSettoreVO[] getTipologiaInterventoList() {
		return tipologiaBandoList;
	}

	public void setTipologiaInterventoList(
			TipologiaSettoreVO[] tipologiaBandoList) {
		this.tipologiaBandoList = tipologiaBandoList;
	}
	
	
	
}
