/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RisorseUmaneVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String categoria;
	
	@MapTo(target=INHERIT)
	String numUomini;

	@MapTo(target=INHERIT)
	String numDonne;

	@MapTo(target=INHERIT)
	String totale;

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNumUomini() {
		return numUomini;
	}

	public String getNumDonne() {
		return numDonne;
	}

	public String getTotale() {
		return totale;
	}

	public void setNumUomini(String numUomini) {
		this.numUomini = numUomini;
	}

	public void setNumDonne(String numDonne) {
		this.numDonne = numDonne;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}
	

}
