/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.accessoForbidden;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AccessoForbiddenOutput extends CommonalityOutput {

	@MapTo(target=NAMESPACE)
	boolean accessoForbidden;

	public boolean isAccessoForbidden() {
		return accessoForbidden;
	}

	public void setAccessoForbidden(boolean accessoForbidden) {
		this.accessoForbidden = accessoForbidden;
	}
	
}
