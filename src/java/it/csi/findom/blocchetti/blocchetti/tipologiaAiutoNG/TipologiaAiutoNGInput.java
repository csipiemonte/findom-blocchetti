/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.tipologiaAiutoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaAiutoNGInput extends CommonalityInput {

	@MapTo(target=MODEL, name="_tipologiaAiuto")
	TipologiaAiutoNGVO _tipologiaAiuto;

	@MapTo(target=CONF, name="_progetto_agevolazione_nessun_dettaglio")
	String _progetto_agevolazione_nessun_dettaglio;

	@MapTo(target=CONF, name="_progetto_agevolazione_uno_e_uno_solo_tipo_aiuto")
	String _progetto_agevolazione_uno_e_uno_solo_tipo_aiuto;
	@MapTo(target=CONF, name="_progetto_agevolazione_uno_e_uno_solo_dettaglio")
	String _progetto_agevolazione_uno_e_uno_solo_dettaglio;

	@MapTo(target=MapTarget.CONF)		
	public String validationMethodsTipologiaAiutoNG;
	
	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO caratteristicheProgettoNGVO;
}
