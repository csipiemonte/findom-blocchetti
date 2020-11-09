/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultVociEntrata;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CultVociEntrataVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String totale;
	
	@MapTo(target=INHERIT)
	VoceEntrataItemVO[] vociEntrataScelteList;

	@MapTo(target=INHERIT)
	VoceEntrataItemVO[] vociEntrataRiepilogoList;
	
	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}

	public VoceEntrataItemVO[] getVociEntrataScelteList() {
		return vociEntrataScelteList;
	}

	public void setVociEntrataScelteList(VoceEntrataItemVO[] vociEntrataScelteList) {
		this.vociEntrataScelteList = vociEntrataScelteList;
	}

	public VoceEntrataItemVO[] getVociEntrataRiepilogoList() {
		return vociEntrataRiepilogoList;
	}

	public void setVociEntrataRiepilogoList(
			VoceEntrataItemVO[] vociEntrataRiepilogoList) {
		this.vociEntrataRiepilogoList = vociEntrataRiepilogoList;
	}

	
}
