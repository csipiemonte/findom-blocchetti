/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.legalerappresentante;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class LRVo extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	String codiceFiscale;

	@MapTo(target = INHERIT)
	String cognome;

	@MapTo(target = INHERIT)
	String nome;

	@MapTo(target = INHERIT)
	String idAzienda;

	@MapTo(target = INHERIT)
	String idFonteDato;

	@MapTo(target = INHERIT)
	String idPersona;

}
