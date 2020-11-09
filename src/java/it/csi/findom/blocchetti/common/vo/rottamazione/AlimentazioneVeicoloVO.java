/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.rottamazione;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AlimentazioneVeicoloVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	String idAlimentazioneVeicolo;
	      
	@MapTo(target=MapTarget.INHERIT)
	String descrAlimentazioneVeicolo;

	public String getIdAlimentazioneVeicolo() {
		return idAlimentazioneVeicolo;
	}

	public void setIdAlimentazioneVeicolo(String idAlimentazioneVeicolo) {
		this.idAlimentazioneVeicolo = idAlimentazioneVeicolo;
	}

	public String getDescrAlimentazioneVeicolo() {
		return descrAlimentazioneVeicolo;
	}

	public void setDescrAlimentazioneVeicolo(String descrAlimentazioneVeicolo) {
		this.descrAlimentazioneVeicolo = descrAlimentazioneVeicolo;
	}
	

}
