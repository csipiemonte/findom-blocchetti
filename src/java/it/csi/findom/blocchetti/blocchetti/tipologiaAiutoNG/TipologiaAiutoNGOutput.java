/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.tipologiaAiutoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGItemListVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class TipologiaAiutoNGOutput extends CommonalityOutput {

	@MapTo(target=NAMESPACE)
	List<TipologiaAiutoNGItemListVO> tipologiaAiutoList;
	
	@MapTo(target=NAMESPACE)
	String esistonoDettagli;

	public List<TipologiaAiutoNGItemListVO> getTipologiaAiutoList() {
		return tipologiaAiutoList;
	}

	public String getEsistonoDettagli() {
		return esistonoDettagli;
	}

	public void setTipologiaAiutoList(
			List<TipologiaAiutoNGItemListVO> tipologiaAiutoList) {
		this.tipologiaAiutoList = tipologiaAiutoList;
	}

	public void setEsistonoDettagli(String esistonoDettagli) {
		this.esistonoDettagli = esistonoDettagli;
	}
	
	
	
	
}
