/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinContrNeve;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.CaratteristicheProgettoNeveNGVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class FormaFinContrNeveInput extends CommonalityInput {
	
    // @MapTo(target=MODEL,name="_formaAgevolazione")
	@MapTo(target=MapTarget.MODEL ,name="_formaAgevolazione")
    FormaFinContrNeveVO _formaAgevolazione;

    
    @MapTo(target=MODEL,name="_domanda")
	DomandaNGVO _domanda;

    
    @MapTo(target=CONF,name="_progetto_spese_quota_inseribile")
	String _progetto_spese_quota_inseribile;
	
	@MapTo(target=CONF)
	String _importo_minimo_specifico;
	
	
	/**
	 * : Jira: 1337 -  
	 */
	@MapTo(target=CONF)
	public String _cult_forma_agv_cfg_da_sportello;
	
	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNeveNGVO _caratteristicheProgetto;
	
	@MapTo(target=MapTarget.CONF)
	String _progetto_caratteristiche_tipo_intervento;
	
	@MapTo(target=MapTarget.CONF)
	String _caratteristicheProgettoNG_idTipoIntervento_99;
	
	@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
	PianoSpeseVO _pianoSpese;

	public FormaFinContrNeveVO get_formaAgevolazione() {
		return _formaAgevolazione;
	}

	public void set_formaAgevolazione(FormaFinContrNeveVO _formaAgevolazione) {
		this._formaAgevolazione = _formaAgevolazione;
	}

	public DomandaNGVO get_domanda() {
		return _domanda;
	}

	public void set_domanda(DomandaNGVO _domanda) {
		this._domanda = _domanda;
	}


	public String get_progetto_spese_quota_inseribile() {
		return _progetto_spese_quota_inseribile;
	}

	public void set_progetto_spese_quota_inseribile(String _progetto_spese_quota_inseribile) {
		this._progetto_spese_quota_inseribile = _progetto_spese_quota_inseribile;
	}

	public String get_importo_minimo_specifico() {
		return _importo_minimo_specifico;
	}

	public void set_importo_minimo_specifico(String _importo_minimo_specifico) {
		this._importo_minimo_specifico = _importo_minimo_specifico;
	}

	public String get_cult_forma_agv_cfg_da_sportello() {
		return _cult_forma_agv_cfg_da_sportello;
	}

	public void set_cult_forma_agv_cfg_da_sportello(String _cult_forma_agv_cfg_da_sportello) {
		this._cult_forma_agv_cfg_da_sportello = _cult_forma_agv_cfg_da_sportello;
	}
	

	public CaratteristicheProgettoNeveNGVO get_caratteristicheProgetto() {
		return _caratteristicheProgetto;
	}

	public void set_caratteristicheProgetto(CaratteristicheProgettoNeveNGVO _caratteristicheProgetto) {
		this._caratteristicheProgetto = _caratteristicheProgetto;
	}

	
	public String get_progetto_caratteristiche_tipo_intervento() {
		return _progetto_caratteristiche_tipo_intervento;
	}


	public PianoSpeseVO get_pianoSpese() {
		return _pianoSpese;
	}

	public void set_pianoSpese(PianoSpeseVO _pianoSpese) {
		this._pianoSpese = _pianoSpese;
	}

	public void set_progetto_caratteristiche_tipo_intervento(String _progetto_caratteristiche_tipo_intervento) {
		this._progetto_caratteristiche_tipo_intervento = _progetto_caratteristiche_tipo_intervento;
	}

	public String get_caratteristicheProgettoNG_idTipoIntervento_99() {
		return _caratteristicheProgettoNG_idTipoIntervento_99;
	}

	public void set_caratteristicheProgettoNG_idTipoIntervento_99(
			String _caratteristicheProgettoNG_idTipoIntervento_99) {
		this._caratteristicheProgettoNG_idTipoIntervento_99 = _caratteristicheProgettoNG_idTipoIntervento_99;
	}

	public PianoSpeseVO getPianoSpese() {
		return _pianoSpese;
	}

	public void setPianoSpese(PianoSpeseVO pianoSpese) {
		this._pianoSpese = pianoSpese;
	}

}
