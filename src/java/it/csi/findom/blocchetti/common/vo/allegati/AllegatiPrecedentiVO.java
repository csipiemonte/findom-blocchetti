/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.allegati;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class AllegatiPrecedentiVO extends CommonalityVO {


	private static final long serialVersionUID = 1L;
	@MapTo(target = INHERIT)
	AllegatiItemVO[] allegatiPrecedentiList;

	public boolean isEmpty() {
		return allegatiPrecedentiList == null;
	}

	public AllegatiItemVO[] getAllegatiPrecedentiList() {
		return allegatiPrecedentiList;
	}

	public void setAllegatiPrecedentiList(AllegatiItemVO[] allegatiPrecedentiList) {
		this.allegatiPrecedentiList = allegatiPrecedentiList;
	}

  @Override
  public String toString() {
    return "AllegatiPrecedentiVO [allegatiPrecedentiList=" + Arrays.toString(allegatiPrecedentiList) + "]";
  }


}
