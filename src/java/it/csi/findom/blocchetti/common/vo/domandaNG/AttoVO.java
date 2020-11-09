/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AttoVO extends CommonalityVO {
	@MapTo(target = INHERIT, name = "numAtto")
	String numAtto;

	@MapTo(target = INHERIT, name = "dataAtto")
	String dataAtto;


	public String getNumAtto() {
		return numAtto;
	}

	public void setNumAtto(String numAtto) {
		this.numAtto = numAtto;
	}

	public String getDataAtto() {
		return dataAtto;
	}

	public void setDataAtto(String dataAtto) {
		this.dataAtto = dataAtto;
	}

}
