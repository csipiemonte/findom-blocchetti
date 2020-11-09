/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.gestioneDocumentiNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class ErrorsIndexVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;


	@MapTo(target=MapTarget.INHERIT, name="_allegati_read")
	String allegati_read;


	@MapTo(target=MapTarget.INHERIT, name="_allegati")
	String allegati;

	@MapTo(target=MapTarget.INHERIT, name="_allegati_delete")
	String allegati_delete;
	
}				
