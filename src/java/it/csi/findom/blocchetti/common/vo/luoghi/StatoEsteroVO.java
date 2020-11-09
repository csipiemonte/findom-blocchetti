/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.luoghi;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class StatoEsteroVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore default
	 */
	public StatoEsteroVO() {
	}

	/**
	 * Costruttore con parametri
	 */
	public StatoEsteroVO(String codice, String descrizione, String sigla) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.sigla = sigla;
	}

	@MapTo(target = INHERIT, name = "codice")
	String codice;

	@MapTo(target = INHERIT, name = "descrizione")
	String descrizione;

	@MapTo(target = INHERIT, name = "sigla")
	String sigla;

	
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public String toString() {
		return "StatoEsteroVO [codice=" + codice + ", descrizione=" + descrizione + ", sigla=" + sigla + "]";
	}

}
