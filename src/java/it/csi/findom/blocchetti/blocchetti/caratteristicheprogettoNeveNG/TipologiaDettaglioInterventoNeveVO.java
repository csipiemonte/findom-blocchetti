/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaDettaglioInterventoNeveVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	String idInterventoDettaglio;
	
	@MapTo(target=MapTarget.INHERIT)
	String idDettIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String codDettIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrDettIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String dettaglioIntervento;

	@MapTo(target=MapTarget.INHERIT)
	String idCampoDettIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String codCampoDettIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrCampoDettIntervento;
	

	@MapTo(target=MapTarget.INHERIT)
	String checked = "false";


	public String getIdInterventoDettaglio() {
		return idInterventoDettaglio;
	}


	public void setIdInterventoDettaglio(String idInterventoDettaglio) {
		this.idInterventoDettaglio = idInterventoDettaglio;
	}


	public String getIdDettIntervento() {
		return idDettIntervento;
	}


	public void setIdDettIntervento(String idDettIntervento) {
		this.idDettIntervento = idDettIntervento;
	}


	public String getCodDettIntervento() {
		return codDettIntervento;
	}


	public void setCodDettIntervento(String codDettIntervento) {
		this.codDettIntervento = codDettIntervento;
	}


	public String getDescrDettIntervento() {
		return descrDettIntervento;
	}


	public void setDescrDettIntervento(String descrDettIntervento) {
		this.descrDettIntervento = descrDettIntervento;
	}


	public String getDettaglioIntervento() {
		return dettaglioIntervento;
	}


	public void setDettaglioIntervento(String dettaglioIntervento) {
		this.dettaglioIntervento = dettaglioIntervento;
	}


	public String getIdCampoDettIntervento() {
		return idCampoDettIntervento;
	}


	public void setIdCampoDettIntervento(String idCampoDettIntervento) {
		this.idCampoDettIntervento = idCampoDettIntervento;
	}


	public String getCodCampoDettIntervento() {
		return codCampoDettIntervento;
	}


	public void setCodCampoDettIntervento(String codCampoDettIntervento) {
		this.codCampoDettIntervento = codCampoDettIntervento;
	}


	public String getDescrCampoDettIntervento() {
		return descrCampoDettIntervento;
	}


	public void setDescrCampoDettIntervento(String descrCampoDettIntervento) {
		this.descrCampoDettIntervento = descrCampoDettIntervento;
	}


	public String getChecked() {
		return checked;
	}


	public void setChecked(String checked) {
		this.checked = checked;
	}
	
}
