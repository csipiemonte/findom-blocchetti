/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.dichiarazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RichiestaContributiAltreStruttureVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	RichiestaContributoAltreStruttureItemVO richiestaContributoAltreStrutture;

	@MapTo(target=INHERIT)
	String daCancellare;

	

	public RichiestaContributoAltreStruttureItemVO getRichiestaContributoAltreStrutture() {
		return richiestaContributoAltreStrutture;
	}

	public void setRichiestaContributoAltreStrutture(
			RichiestaContributoAltreStruttureItemVO richiestaContributoAltreStrutture) {
		this.richiestaContributoAltreStrutture = richiestaContributoAltreStrutture;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}
	
	
}
