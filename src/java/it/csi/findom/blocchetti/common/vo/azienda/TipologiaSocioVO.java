/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.azienda;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaSocioVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore default
	 */
	public TipologiaSocioVO() {
	}

	/**
	 * Costruttore con parametri
	 */
	public TipologiaSocioVO(String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
	}

	@MapTo(target = INHERIT, name = "codice")
	String codice;

	@MapTo(target = INHERIT, name = "descrizione")
	String descrizione;


	
	/* Get | Set */
	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		return "TipologiaSocioVO [codice=" + codice + ", descrizione=" + descrizione + "]";
	}

}
