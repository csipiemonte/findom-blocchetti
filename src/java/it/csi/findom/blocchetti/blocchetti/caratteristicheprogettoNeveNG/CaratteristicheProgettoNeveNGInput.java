/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.blocchetti.dimensioniNG.DimensioniImpresaVO;
import it.csi.findom.blocchetti.blocchetti.formaFinContrNeve.FormaFinContrNeveVO;
import it.csi.findom.blocchetti.common.vo.cultPianospese.CultPianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CaratteristicheProgettoNeveNGInput extends CommonalityInput {

	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNeveNGVO _caratteristicheProgetto;
	
	@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
	PianoSpeseVO pianoSpese;
	
	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_una_e_una_sola_tipologia;

	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_uno_e_uno_solo_dettaglio;

	@MapTo(target=MapTarget.CONF)
	String caratteristicheProgettoCustomLabel;
	
	// --------------------------------------- Jira: 1361:  - inizio
	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_tipo_intervento;

	@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
	CultPianoSpeseVO cultPianoSpese;

	@MapTo(target=MODEL,name="_formaFinanziamento")
	FormaFinContrNeveVO formaFinanziamentoVO;
	
	@MapTo(target=MODEL,name="_dimensioni")
	DimensioniImpresaVO dimensioniImpresaVO;

	@MapTo(target=MapTarget.CONF)		
	public String validationMethodsCaratteristicheProgettoNG;
	
	// --------------------------------------- Jira: 1410:  - inizio
	@MapTo(target=MapTarget.CONF)
	String _caratteristicheProgettoNG_idTipoIntervento_99;
	
}
