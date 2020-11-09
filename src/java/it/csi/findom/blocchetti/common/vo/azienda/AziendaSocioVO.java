/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.azienda;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AziendaSocioVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT, name = "socio")
	SocioVO socio;
	
	@MapTo(target = INHERIT, name = "daCancellare")
	String daCancellare;

	public SocioVO getSocio() {
		return socio;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setSocio(SocioVO socio) {
		this.socio = socio;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	@Override
	public String toString() {
		return "AziendaSocioVO [socio=" + socio + ", daCancellare="
				+ daCancellare + "]";
	}
	
	
	
}
