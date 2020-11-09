/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.caratteristicheProgetto;

import java.util.Arrays;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaInterventoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore
	 */
	public TipologiaInterventoVO() {
		//  Auto-generated constructor stub
	}
	
	public TipologiaInterventoVO(String descrTipoIntervento, String tipologiaIntervento, String checked, String numDettagli) {
		this.descrTipoIntervento = descrTipoIntervento;
		this.tipologiaIntervento = tipologiaIntervento;
		this.checked = checked;
		this.numDettagli = numDettagli;
	}


	@MapTo(target=MapTarget.INHERIT)
	TipologiaDettaglioInterventoVO[] dettaglioInterventoList;

	@MapTo(target=MapTarget.INHERIT)
	String idTipoIntervento;

	@MapTo(target=MapTarget.INHERIT)
	String codTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String tipologiaIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String idCampoTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String codCampoTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrCampoTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String flagObbligatorio;

	@MapTo(target=MapTarget.INHERIT)
	String checked;
	
	@MapTo(target=MapTarget.INHERIT)
	String numDettagli;

	@MapTo(target=MapTarget.INHERIT)
	String disabilitato;
	
	@MapTo(target=MapTarget.INHERIT)
	String soggettoAbilitato;
	
	
	public TipologiaDettaglioInterventoVO[] getDettaglioInterventoList() {
		return dettaglioInterventoList;
	}

	public String getIdTipoIntervento() {
		return idTipoIntervento;
	}

	public String getCodTipoIntervento() {
		return codTipoIntervento;
	}

	public String getDescrTipoIntervento() {
		return descrTipoIntervento;
	}

	public String getTipologiaIntervento() {
		return tipologiaIntervento;
	}

	public String getIdCampoTipoIntervento() {
		return idCampoTipoIntervento;
	}

	public String getCodCampoTipoIntervento() {
		return codCampoTipoIntervento;
	}

	public String getDescrCampoTipoIntervento() {
		return descrCampoTipoIntervento;
	}

	public String getFlagObbligatorio() {
		return flagObbligatorio;
	}

	public String getChecked() {
		return checked;
	}

	public String getNumDettagli() {
		return numDettagli;
	}

	public void setDettaglioInterventoList(
			TipologiaDettaglioInterventoVO[] dettaglioInterventoList) {
		this.dettaglioInterventoList = dettaglioInterventoList;
	}

	public void setIdTipoIntervento(String idTipoIntervento) {
		this.idTipoIntervento = idTipoIntervento;
	}

	public void setCodTipoIntervento(String codTipoIntervento) {
		this.codTipoIntervento = codTipoIntervento;
	}

	public void setDescrTipoIntervento(String descrTipoIntervento) {
		this.descrTipoIntervento = descrTipoIntervento;
	}

	public void setTipologiaIntervento(String tipologiaIntervento) {
		this.tipologiaIntervento = tipologiaIntervento;
	}

	public void setIdCampoTipoIntervento(String idCampoTipoIntervento) {
		this.idCampoTipoIntervento = idCampoTipoIntervento;
	}

	public void setCodCampoTipoIntervento(String codCampoTipoIntervento) {
		this.codCampoTipoIntervento = codCampoTipoIntervento;
	}

	public void setDescrCampoTipoIntervento(String descrCampoTipoIntervento) {
		this.descrCampoTipoIntervento = descrCampoTipoIntervento;
	}

	public void setFlagObbligatorio(String flagObbligatorio) {
		this.flagObbligatorio = flagObbligatorio;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public void setNumDettagli(String numDettagli) {
		this.numDettagli = numDettagli;
	}

	@Override
	public String toString() {
		return "TipologiaInterventoVO [dettaglioInterventoList="
				+ Arrays.toString(dettaglioInterventoList)
				+ ", idTipoIntervento=" + idTipoIntervento
				+ ", codTipoIntervento=" + codTipoIntervento
				+ ", descrTipoIntervento=" + descrTipoIntervento
				+ ", tipologiaIntervento=" + tipologiaIntervento
				+ ", idCampoTipoIntervento=" + idCampoTipoIntervento
				+ ", codCampoTipoIntervento=" + codCampoTipoIntervento
				+ ", descrCampoTipoIntervento=" + descrCampoTipoIntervento
				+ ", flagObbligatorio=" + flagObbligatorio 
				+ ", checked=" + checked 
				+ ", numDettagli=" + numDettagli 
				+ ", disabilitato="+disabilitato
				+ ", soggettoAbilitato="+soggettoAbilitato+"]";
	}

	public String getDisabilitato() {
		return disabilitato;
	}

	public void setDisabilitato(String disabilitato) {
		this.disabilitato = disabilitato;
	}

	public String getSoggettoAbilitato() {
		return soggettoAbilitato;
	}

	public void setSoggettoAbilitato(String soggettoAbilitato) {
		this.soggettoAbilitato = soggettoAbilitato;
	}

}
