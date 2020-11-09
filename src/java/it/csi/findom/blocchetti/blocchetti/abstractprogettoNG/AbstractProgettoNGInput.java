/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.abstractprogettoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AbstractProgettoNGInput extends CommonalityInput {

	@MapTo(target=MapTarget.CONF)
	String _abstract_acronimo;
	
	@MapTo(target=MapTarget.CONF)
	String _relazioni_assunzioni_progetto_ctrl;
	
	@MapTo(target=MapTarget.CONF)
	String _relazioni_assunzioni_progetto;
	
	@MapTo(target=MapTarget.CONF)
	String _abstract_durata_prevista;
	
	@MapTo(target=MapTarget.CONF)
	String _abstract_cup;
	
	@MapTo(target=MapTarget.CONF)
	String _abstract_campoNumericoOpzionale;
	
	@MapTo(target=MapTarget.MODEL, name="_abstractProgetto")
	AbstractProgettoVO abstractProgetto;
	
	@MapTo(target=MapTarget.CONF)
	String abstractProgettoCustomLabel;
	
	@MapTo(target=MapTarget.CONF)
	String titoloProgettoCustomLabel;
	
	@MapTo(target=MapTarget.CONF)
	String sintesiProgettoCustomLabel;
	
	@MapTo(target=MapTarget.CONF)
	String _abstract_ruolo;
	
	@MapTo(target=MapTarget.CONF)
	String _abstractProgetto_visualizza_solo_data_inizio_progetto;
	
	/**
	 * :: Jira: 1561
	 * Implementazione radio button 
	 * per la gestione della collaborazione 
	 * _abstract_rb_collaborazione
	 */
	@MapTo(target=MapTarget.CONF)
	String _abstract_rb_collaborazione;
	
	/**
	 * :: Jira 1335 
	 * _abstract_corealizzazione
	 **/
	@MapTo(target=MapTarget.CONF)
	String _abstract_corealizzazione;

	
	/**
	 * :: CR-1
	 * _abstract_titoloProgetto
	 */
	@MapTo(target=MapTarget.CONF)
	String _abstract_titoloProgetto;
	
	/**
	 * ::
	 * Nuovi campi:
	 * - dataInizioProgetto
	 * - dataFineProgetto
	 * _abstractProgetto_data_inizio_fine_progetto
	 */
	@MapTo(target=MapTarget.CONF)
	String _abstractProgetto_data_inizio_fine_progetto;
	
	
	/**
	 * ::
	 * Nuovo campo: variabile di configurazione
	 * (Iporto complessivo business plan)
	 */
	@MapTo(target=MapTarget.CONF)
	String _importo_complessivo_business_plan;
	
	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO caratteristicheProgettoNGVO;
	
	@MapTo(target=MapTarget.CONF)		
	public String validationMethodsAbstractProgettoNG;
	
	/**  :: Jira 1428 - sistema neve -  _abstract_titoloProgettoNeve */
	@MapTo(target=MapTarget.CONF)
	String _abstract_titoloProgettoNeve;
	
	@MapTo(target=MapTarget.CONF)
	String _abstract_progetti_collaborazione;
	
	/** : Jira 1579 */
	@MapTo(target=MapTarget.NAMESPACE)
	String studiInCollaborazione;
	
	// Amianto -
    @MapTo(target=MODEL,name="_domanda")
    DomandaNGVO _domanda;
    
    /** : Jira: 1883 */
	@MapTo(target=MapTarget.INHERIT)
	String mqCoperture;
	
	
	public String get_abstract_rb_collaborazione() {
		return _abstract_rb_collaborazione;
	}

	public void set_abstract_rb_collaborazione(String _abstract_rb_collaborazione) {
		this._abstract_rb_collaborazione = _abstract_rb_collaborazione;
	}

	public String get_abstract_progetti_collaborazione() {
		return _abstract_progetti_collaborazione;
	}

	public void set_abstract_progetti_collaborazione(
			String _abstract_progetti_collaborazione) {
		this._abstract_progetti_collaborazione = _abstract_progetti_collaborazione;
	}

	public String getStudiInCollaborazione() {
		return studiInCollaborazione;
	}

	public void setStudiInCollaborazione(String studiInCollaborazione) {
		this.studiInCollaborazione = studiInCollaborazione;
	}

	public String get_abstractProgetto_visualizza_solo_data_inizio_progetto() {
		return _abstractProgetto_visualizza_solo_data_inizio_progetto;
	}

	public void set_abstractProgetto_visualizza_solo_data_inizio_progetto(
			String _abstractProgetto_visualizza_solo_data_inizio_progetto) {
		this._abstractProgetto_visualizza_solo_data_inizio_progetto = _abstractProgetto_visualizza_solo_data_inizio_progetto;
	}
		
}
