/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFiera;

import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DescrizioneFieraInput extends CommonalityInput{
	
	@MapTo(target=MapTarget.MODEL, name="_descrizioneFiera")
	DescrizioneFieraVO descrizioneFiera;
	
	@MapTo(target=MapTarget.CONF)
	String _descrizioneFiera_nazioneSenzaItalia;
	
	/**
	 * Recupero il valore selezionato da utente:
	 * - descrDettIntervento: (Europa o Fuori Europa )
	 * - idDettIntervento: = 65 Europa
	 * - idDettIntervento: = 66 Fuori Europa
	 */
	@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO caratteristicheProgetto;

	public DescrizioneFieraVO getDescrizioneFiera() {
		return descrizioneFiera;
	}

	public void setDescrizioneFiera(DescrizioneFieraVO descrizioneFiera) {
		this.descrizioneFiera = descrizioneFiera;
	}

	public String get_descrizioneFiera_nazioneSenzaItalia() {
		return _descrizioneFiera_nazioneSenzaItalia;
	}

	public void set_descrizioneFiera_nazioneSenzaItalia(
			String _descrizioneFiera_nazioneSenzaItalia) {
		this._descrizioneFiera_nazioneSenzaItalia = _descrizioneFiera_nazioneSenzaItalia;
	}

	public CaratteristicheProgettoNGVO getCaratteristicheProgetto() {
		return caratteristicheProgetto;
	}

	public void setCaratteristicheProgetto(
			CaratteristicheProgettoNGVO caratteristicheProgetto) {
		this.caratteristicheProgetto = caratteristicheProgetto;
	}
	
}
