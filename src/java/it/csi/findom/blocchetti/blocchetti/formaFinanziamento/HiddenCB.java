/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class HiddenCB extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String str_idCheckbox;
	
	
	public HiddenCB() {
		//  Auto-generated constructor stub
	}

	public HiddenCB(String str_idCheckbox) {
		this.str_idCheckbox = str_idCheckbox;
		
	}

	public String getStr_idCheckbox() {
		return str_idCheckbox;
	}

	public void setStr_idCheckbox(String str_idCheckbox) {
		this.str_idCheckbox = str_idCheckbox;
	}

	
}
