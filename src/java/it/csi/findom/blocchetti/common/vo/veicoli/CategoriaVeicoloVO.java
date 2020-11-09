/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.veicoli;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CategoriaVeicoloVO extends CommonalityVO {
	
	@MapTo(target=MapTarget.INHERIT)
	String idCategoriaVeicolo;
	      
	@MapTo(target=MapTarget.INHERIT)
	String descrCategoriaVeicolo;

	public String getIdCategoriaVeicolo() {
		return idCategoriaVeicolo;
	}

	public void setIdCategoriaVeicolo(String idCategoriaVeicolo) {
		this.idCategoriaVeicolo = idCategoriaVeicolo;
	}

	public String getDescrCategoriaVeicolo() {
		return descrCategoriaVeicolo;
	}

	public void setDescrCategoriaVeicolo(String descrCategoriaVeicolo) {
		this.descrCategoriaVeicolo = descrCategoriaVeicolo;
	}
}
