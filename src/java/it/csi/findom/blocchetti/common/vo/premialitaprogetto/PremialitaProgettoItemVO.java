/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.premialitaprogetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PremialitaProgettoItemVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;


	@MapTo(target=INHERIT)
	String checked; 
	
	@MapTo(target=INHERIT)
	String idPremialita;

	@MapTo(target=INHERIT)
	String descrPremialita;
	
	@MapTo(target=INHERIT)
	String tipoDatoRichiesto;
	
	@MapTo(target=INHERIT)
	String 	datoRichiesto;
	
	@MapTo(target=INHERIT)
	String 	linkPremialita;	
	
	@MapTo(target=INHERIT)
	String 	valorePremialita;
	
	public String getChecked() {
		return checked;
	}


	public void setChecked(String checked) {
		this.checked = checked;
	}


	public String getIdPremialita() {
		return idPremialita;
	}


	public void setIdPremialita(String idPremialita) {
		this.idPremialita = idPremialita;
	}


	public String getDescrPremialita() {
		return descrPremialita;
	}


	public void setDescrPremialita(String descrPremialita) {
		this.descrPremialita = descrPremialita;
	}


	public String getTipoDatoRichiesto() {
		return tipoDatoRichiesto;
	}


	public void setTipoDatoRichiesto(String tipoDatoRichiesto) {
		this.tipoDatoRichiesto = tipoDatoRichiesto;
	}


	public String getDatoRichiesto() {
		return datoRichiesto;
	}


	public void setDatoRichiesto(String datoRichiesto) {
		this.datoRichiesto = datoRichiesto;
	}


	public String getLinkPremialita() {
		return linkPremialita;
	}


	public void setLinkPremialita(String linkPremialita) {
		this.linkPremialita = linkPremialita;
	}


	public String getValorePremialita() {
		return valorePremialita;
	}


	public void setValorePremialita(String valorePremialita) {
		this.valorePremialita = valorePremialita;
	} 

}
