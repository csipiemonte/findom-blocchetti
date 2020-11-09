/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.selezioneAteco;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SelezioneAtecoInput extends CommonalityInput {

	@MapTo(target = REQUEST, name = "_command")
	String command;
	
	@MapTo(target = MODEL, name = "_commandLabel")
	String commandLabelModel;
	
	@MapTo(target = REQUEST, name = "codiceAteco")
	String codiceAteco;

	@MapTo(target = REQUEST, name = "descrizioneAteco")
	String descrizioneAteco;
	
	@MapTo(target = REQUEST, name = "tmpOperatorePresentatore_idAteco2007")
	String tmpOperatorePresentatore_idAteco2007;

}
