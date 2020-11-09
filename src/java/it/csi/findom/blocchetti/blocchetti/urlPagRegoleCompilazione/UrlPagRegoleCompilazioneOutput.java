/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.urlPagRegoleCompilazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class UrlPagRegoleCompilazioneOutput extends CommonalityOutput {

	@MapTo(target = NAMESPACE, name = "urlPagRegoleCompilazione")
	String urlPagRegoleCompilazione;

	public String getUrlPagRegoleCompilazione() {
		return urlPagRegoleCompilazione;
	}

	public void setUrlPagRegoleCompilazione(String urlPagRegoleCompilazione) {
		this.urlPagRegoleCompilazione = urlPagRegoleCompilazione;
	}
	
}
