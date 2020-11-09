/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class IndicatoriOutput extends CommonalityOutput {
		
	@MapTo(target=NAMESPACE)
	TipoIndicatoreVO[] tipoIndicatoreList;

	public TipoIndicatoreVO[] getTipoIndicatoreList() {
		return tipoIndicatoreList;
	}
	public void setTipoIndicatoreList(TipoIndicatoreVO[] tipoIndicatoreList) {
		this.tipoIndicatoreList = tipoIndicatoreList;
	}

	
}
