/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaAiutoNGVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "tipologiaAiutoList")
	TipologiaAiutoNGItemListVO[] tipologiaAiutoList;

	public TipologiaAiutoNGItemListVO[] getTipologiaAiutoList() {
		return tipologiaAiutoList;
	}

	public void setTipologiaAiutoList(TipologiaAiutoNGItemListVO[] tipologiaAiutoList) {
		this.tipologiaAiutoList = tipologiaAiutoList;
	}
	
}
