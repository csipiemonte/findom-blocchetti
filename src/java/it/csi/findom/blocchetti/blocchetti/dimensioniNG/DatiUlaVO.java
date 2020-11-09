/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DatiUlaVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String id;

	@MapTo(target=INHERIT)
	String descrizione;

	public String getId() {
		return id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
}
