/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFieraSecEd;

import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DescrizioneFieraSecEdInput extends CommonalityInput{
	
	@MapTo(target=MapTarget.MODEL, name="_descrizioneFieraSecEd")
	DescrizioneFieraSecEdVO descrizioneFieraSecEd;
	
	@MapTo(target=MapTarget.CONF)
	String _descrizioneFieraSecEd_nazioneSenzaItalia;
	
	@MapTo(target=MapTarget.CONF)
	String _descrizioneFieraSecEd_data_inizio_fine_progetto_seconda_ed;
	
	/**
	 * Recupero il valore selezionato da utente:
	 * - descrDettIntervento: (Europa o Fuori Europa )
	 * - idDettIntervento: = 65 Europa
	 * - idDettIntervento: = 66 Fuori Europa
	 */
	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO caratteristicheProgetto;

	public DescrizioneFieraSecEdVO getdescrizioneFieraSecEd() {
		return descrizioneFieraSecEd;
	}

	public void setdescrizioneFieraSecEd(DescrizioneFieraSecEdVO descrizioneFieraSecEd) {
		this.descrizioneFieraSecEd = descrizioneFieraSecEd;
	}

	public String get_descrizioneFieraSecEd_nazioneSenzaItalia() {
		return _descrizioneFieraSecEd_nazioneSenzaItalia;
	}

	public void set_descrizioneFieraSecEd_nazioneSenzaItalia(
			String _descrizioneFieraSecEd_nazioneSenzaItalia) {
		this._descrizioneFieraSecEd_nazioneSenzaItalia = _descrizioneFieraSecEd_nazioneSenzaItalia;
	}

	public CaratteristicheProgettoNGVO getCaratteristicheProgetto() {
		return caratteristicheProgetto;
	}

	public void setCaratteristicheProgetto(
			CaratteristicheProgettoNGVO caratteristicheProgetto) {
		this.caratteristicheProgetto = caratteristicheProgetto;
	}
	
}
