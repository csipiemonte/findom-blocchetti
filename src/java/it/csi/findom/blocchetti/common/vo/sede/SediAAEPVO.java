/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.sede;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SediAAEPVO extends CommonalityVO {

	@MapTo(target = INHERIT)
	SedeAAEPItemVO[] sediListAAEP;

	public SedeAAEPItemVO[] getSediListAAEP() {
		return sediListAAEP;
	}

	public void setSediListAAEP(SedeAAEPItemVO[] sediList) {
		this.sediListAAEP = sediList;
	}
}
