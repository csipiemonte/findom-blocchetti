/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioniSA;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DichiarazioniSAVO extends CommonalityVO{

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	DichiarazioniVoceVO[] listaVoci;

	public DichiarazioniVoceVO[] getListaVoci() {
		return listaVoci;
	}

	public void setListaVoci(DichiarazioniVoceVO[] listaVoci) {
		this.listaVoci = listaVoci;
	}
}
