/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioniSA;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DichiarazioniVoceVO extends CommonalityVO{

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String voce;

	@MapTo(target=INHERIT)
	String valore;

	public String getVoce() {
		return voce;
	}

	public String getValore() {
		return valore;
	}

	public void setVoce(String voce) {
		this.voce = voce;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}
}
