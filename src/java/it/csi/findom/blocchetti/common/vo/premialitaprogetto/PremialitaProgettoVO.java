/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.premialitaprogetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PremialitaProgettoVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	PremialitaProgettoItemVO[] premialitaList;
	
	@MapTo(target=INHERIT)
	String flagPremialitaNonRichiesta;
	
	public PremialitaProgettoItemVO[] getPremialitaList() {
		return premialitaList;
	}

	public void setPremialitaList(PremialitaProgettoItemVO[] premialitaList) {
		this.premialitaList = premialitaList;
	}

	public String getFlagPremialitaNonRichiesta() {
		return flagPremialitaNonRichiesta;
	}

	public void setFlagPremialitaNonRichiesta(String flagPremialitaNonRichiesta) {
		this.flagPremialitaNonRichiesta = flagPremialitaNonRichiesta;
	}

}
