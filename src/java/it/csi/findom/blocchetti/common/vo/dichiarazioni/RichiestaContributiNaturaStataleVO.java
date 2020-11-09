/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.dichiarazioni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RichiestaContributiNaturaStataleVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	RichiestaContributoNaturaStataleItemVO richiestaContributoNaturaStatale;

	@MapTo(target=INHERIT)
	String daCancellare;	

	
	public RichiestaContributoNaturaStataleItemVO getRichiestaContributoNaturaStatale() {
		return richiestaContributoNaturaStatale;
	}

	public void setRichiestaContributoNaturaStatale(
			RichiestaContributoNaturaStataleItemVO richiestaContributoNaturaStatale) {
		this.richiestaContributoNaturaStatale = richiestaContributoNaturaStatale;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}
	
	
}
