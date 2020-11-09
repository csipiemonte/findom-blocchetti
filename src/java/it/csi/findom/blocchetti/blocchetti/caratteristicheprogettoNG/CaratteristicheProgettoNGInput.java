/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.blocchetti.dimensioniNG.DimensioniImpresaVO;
import it.csi.findom.blocchetti.blocchetti.formaFinanziamento.FormaFinanziamentoVO;
import it.csi.findom.blocchetti.blocchetti.schedaProgetto.SchedaProgettoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.cultPianospese.CultPianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CaratteristicheProgettoNGInput extends CommonalityInput {

	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO _caratteristicheProgetto;
	
	@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
	PianoSpeseVO pianoSpese;
	
	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_una_e_una_sola_tipologia;

	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_uno_e_uno_solo_dettaglio;

	@MapTo(target=MapTarget.CONF)
	String caratteristicheProgettoCustomLabel;
	
	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_tipo_intervento;

	@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
	CultPianoSpeseVO cultPianoSpese;

	@MapTo(target=MODEL,name="_formaFinanziamento")
	FormaFinanziamentoVO formaFinanziamentoVO;
	
	@MapTo(target=MODEL, name="_dimensioni")
	DimensioniImpresaVO dimensioniImpresaVO;
	
	//  FSE-M3 -
    @MapTo(target=MODEL,name="_domanda")
    DomandaNGVO _domanda;
	
	@MapTo(target=MapTarget.CONF)		
	public String validationMethodsCaratteristicheProgettoNG;
	
	@MapTo(target=MapTarget.CONF)
	String _caratteristicheProgettoNG_idTipoIntervento_99;
	
	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_tipo_intervento_beneficiario_custom;
	
	// --------------------------------------- bonus-piemonte-covid19 -
	@MapTo(target=MapTarget.CONF)
	String _caratteristicheProgettoNG_imp_contr_covid;
	
	// --------------------------------------- bonus-piemonte-turismo -
	@MapTo(target=MapTarget.CONF)
	String _caratteristicheProgettoNG_imp_contr_cir;
	
	// --------------------------------------- bonus-cultura 2020 -
	@MapTo(target=MapTarget.CONF)
	String _caratteristicheProgettoNG_imp_contr_by_sogg_abilitati;
	
	@MapTo(target=MapTarget.MODEL,name="_criteri")
	SchedaProgettoVO schedaProgettoVO;
	
	@MapTo(target = MODEL, name = "_operatorePresentatore")
	OperatorePresentatoreVo operatorePresentatore;
}
