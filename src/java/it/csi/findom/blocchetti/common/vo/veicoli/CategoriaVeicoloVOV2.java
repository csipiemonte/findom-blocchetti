/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.veicoli;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CategoriaVeicoloVOV2 extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=MapTarget.INHERIT)
	String idMassaVeicolo;
	      
	@MapTo(target=MapTarget.INHERIT)
	String descrBreveMassaVeicolo;

	@MapTo(target=MapTarget.INHERIT)
	String descrizioneMassaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String idCategoriaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String idVoceSpesa;
	
	@MapTo(target=MapTarget.INHERIT)
	String importoContributo;

	public String getIdCategoriaVeicolo() {
		return idCategoriaVeicolo;
	}

	public void setIdCategoriaVeicolo(String idCategoriaVeicolo) {
		this.idCategoriaVeicolo = idCategoriaVeicolo;
	}

	public String getIdMassaVeicolo() {
		return idMassaVeicolo;
	}

	public void setIdMassaVeicolo(String idMassaVeicolo) {
		this.idMassaVeicolo = idMassaVeicolo;
	}

	public String getDescrBreveMassaVeicolo() {
		return descrBreveMassaVeicolo;
	}

	public void setDescrBreveMassaVeicolo(String descrBreveMassaVeicolo) {
		this.descrBreveMassaVeicolo = descrBreveMassaVeicolo;
	}

	public String getDescrizioneMassaVeicolo() {
		return descrizioneMassaVeicolo;
	}

	public void setDescrizioneMassaVeicolo(String descrizioneMassaVeicolo) {
		this.descrizioneMassaVeicolo = descrizioneMassaVeicolo;
	}

	public String getIdVoceSpesa() {
		return idVoceSpesa;
	}

	public void setIdVoceSpesa(String idVoceSpesa) {
		this.idVoceSpesa = idVoceSpesa;
	}

	public String getImportoContributo() {
		return importoContributo;
	}

	public void setImportoContributo(String importoContributo) {
		this.importoContributo = importoContributo;
	}
}
