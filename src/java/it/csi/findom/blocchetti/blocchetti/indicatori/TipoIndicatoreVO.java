/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class TipoIndicatoreVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String idTipoIndicatore;
	
	@MapTo(target=INHERIT)
	String descrTipoIndicatore;

	@MapTo(target=INHERIT)
	IndicatoreItemVO[] indicatoriList;

	public String getIdTipoIndicatore() {
		return idTipoIndicatore;
	}

	public void setIdTipoIndicatore(String idTipoIndicatore) {
		this.idTipoIndicatore = idTipoIndicatore;
	}

	public String getDescrTipoIndicatore() {
		return descrTipoIndicatore;
	}

	public void setDescrTipoIndicatore(String descrTipoIndicatore) {
		this.descrTipoIndicatore = descrTipoIndicatore;
	}

	public IndicatoreItemVO[] getIndicatoriList() {
		return indicatoriList;
	}

	public void setIndicatoriList(IndicatoreItemVO[] indicatoriList) {
		this.indicatoriList = indicatoriList;
	}

	@Override
	public String toString() {
		return "TipoIndicatoreVO [idTipoIndicatore=" + idTipoIndicatore
				+ ", descrTipoIndicatore=" + descrTipoIndicatore
				+ ", indicatoriList=" + Arrays.toString(indicatoriList) + "]";
	}
	
}
