/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.allegati;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class AllegatiVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	
	
    @MapTo(target=MODEL,name="_allegati")
    AllegatiVO allegati;

	@MapTo(target = INHERIT)
	AllegatiItemVO[] allegatiList;

	@MapTo(target = INHERIT)
	String elencoIdAllegatiObbligatori;
	
	public boolean isEmpty() {
		return allegatiList == null;
	}

	public AllegatiItemVO[] getAllegatiList() {
		return allegatiList;
	}

	public void setAllegatiList(AllegatiItemVO[] allegatiList) {
		this.allegatiList = allegatiList;
	}

  @Override
  public String toString() {
    return "AllegatiVO [allegatiList=" + Arrays.toString(allegatiList) + "]";
  }

public String getElencoIdAllegatiObbligatori() {
	return elencoIdAllegatiObbligatori;
}

public void setElencoIdAllegatiObbligatori(String elencoIdAllegatiObbligatori) {
	this.elencoIdAllegatiObbligatori = elencoIdAllegatiObbligatori;
}

public AllegatiVO getAllegati() {
	return allegati;
}

public void setAllegati(AllegatiVO allegati) {
	this.allegati = allegati;
}

}
