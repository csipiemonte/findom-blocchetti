/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class IndicatoriVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	@MapTo(target=INHERIT)
	TipoIndicatoreVO[] tipoIndicatoreList;

	public TipoIndicatoreVO[] getTipoIndicatoreList() {
		return tipoIndicatoreList;
	}

	public void setTipoIndicatoreList(TipoIndicatoreVO[] tipoIndicatoreList) {
		this.tipoIndicatoreList = tipoIndicatoreList;
	}
	
}
