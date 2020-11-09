/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S4;

import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.blocchetti.formaFinanziamento.FormaFinanziamentoVO;
import it.csi.findom.blocchetti.common.vo.premialitaprogetto.PremialitaProgettoVO;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;
import it.csi.findom.blocchetti.blocchetti.indicatori.IndicatoriVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.sede.SediVO;

public class SectionInput extends SectionPageInput {

	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO caratteristicheProgetto;

	@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
	PianoSpeseVO _pianoSpese;

	@MapTo(target=MapTarget.MODEL ,name="_formaFinanziamento")
	FormaFinanziamentoVO _formaFinanziamento;

	@MapTo(target=MapTarget.MODEL ,name="_tipologiaAiuto")
	TipologiaAiutoNGVO _tipologiaAiuto;

	@MapTo(target = MODEL, name = "_premialitaProgetto")
	PremialitaProgettoVO _premialitaProgetto;

	@MapTo(target = MODEL, name = "_sedi")
	SediVO _sedi;

	@MapTo(target=MapTarget.MODEL ,name="_indicatori")
	IndicatoriVO _indicatori;
}
