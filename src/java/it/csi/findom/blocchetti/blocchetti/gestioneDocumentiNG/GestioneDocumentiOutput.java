/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.gestioneDocumentiNG;

import it.csi.findom.blocchetti.common.vo.allegati.AllegatiItemVO;
import it.csi.findom.blocchetti.common.vo.documentazione.DocumentazioneItemVO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class GestioneDocumentiOutput extends CommonalityOutput {
	
	@MapTo(target=MapTarget.NAMESPACE, name="errorsIndexMap")
	ErrorsIndexVO errorsIndexVO;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String urlContextSportello;

	@MapTo(target=MapTarget.NAMESPACE)
	String stringaIdTipolDifferibili;

	@MapTo(target=MapTarget.NAMESPACE)
	String operIndexOk;

	@MapTo(target=MapTarget.NAMESPACE)
	String errorOnElement;

	@MapTo(target=MapTarget.NAMESPACE)
	AllegatiItemVO[] allegatiList;

	@MapTo(target=MapTarget.NAMESPACE)
	TipologiaAllegatoVO[] documentiObblMancantiList;

	@MapTo(target=MapTarget.NAMESPACE)
	DocumentazioneItemVO[] documentiList;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String msg_allegati_opzionali_per_bando;

	public String getUrlContextSportello() {
		return urlContextSportello;
	}

	public void setUrlContextSportello(String urlContextSportello) {
		this.urlContextSportello = urlContextSportello;
	}

	public String getMsg_allegati_opzionali_per_bando() {
		return msg_allegati_opzionali_per_bando;
	}

	public void setMsg_allegati_opzionali_per_bando(
			String msg_allegati_opzionali_per_bando) {
		this.msg_allegati_opzionali_per_bando = msg_allegati_opzionali_per_bando;
	}
}
