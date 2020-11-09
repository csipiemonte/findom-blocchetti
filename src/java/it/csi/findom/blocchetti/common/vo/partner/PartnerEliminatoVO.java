/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PartnerEliminatoVO extends PartnerItemVO {
	
	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
