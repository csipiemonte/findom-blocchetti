/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DatiImpresaVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String anno;

	@MapTo(target=INHERIT)
	String ula;

	@MapTo(target=INHERIT)
	String fatturato;

	@MapTo(target=INHERIT)
	String totBilancio;

	public String getAnno() {
		return anno;
	}

	public String getUla() {
		return ula;
	}

	public String getFatturato() {
		return fatturato;
	}

	public String getTotBilancio() {
		return totBilancio;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public void setUla(String ula) {
		this.ula = ula;
	}

	public void setFatturato(String fatturato) {
		this.fatturato = fatturato;
	}

	public void setTotBilancio(String totBilancio) {
		this.totBilancio = totBilancio;
	}

}
