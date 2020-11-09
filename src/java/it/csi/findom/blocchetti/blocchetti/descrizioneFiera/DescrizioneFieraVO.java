/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFiera;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DescrizioneFieraVO extends CommonalityVO{

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=MapTarget.INHERIT)
	String stato;

	@MapTo(target=MapTarget.INHERIT)
	private String nomeFiera;
	
	@MapTo(target=MapTarget.INHERIT)
	private String statoEstero;
	
	@MapTo(target=MapTarget.INHERIT)
	private String statoEsteroDescrizione;

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getNomeFiera() {
		return nomeFiera;
	}

	public void setNomeFiera(String nomeFiera) {
		this.nomeFiera = nomeFiera;
	}

	public String getStatoEstero() {
		return statoEstero;
	}

	public void setStatoEstero(String statoEstero) {
		this.statoEstero = statoEstero;
	}

	public String getStatoEsteroDescrizione() {
		return statoEsteroDescrizione;
	}

	public void setStatoEsteroDescrizione(String statoEsteroDescrizione) {
		this.statoEsteroDescrizione = statoEsteroDescrizione;
	}
	
}
