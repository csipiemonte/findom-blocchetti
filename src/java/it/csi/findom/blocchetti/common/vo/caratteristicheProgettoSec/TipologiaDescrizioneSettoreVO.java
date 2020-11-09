/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.caratteristicheProgettoSec;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class TipologiaDescrizioneSettoreVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	/** idDettTipolInterventoSettore, dettaglioCodiceSettore, dettaglioDescrizioneSettore  */
	
	public TipologiaDescrizioneSettoreVO() {
	}
	
	@MapTo(target=MapTarget.INHERIT)
	String checked = "false";
	
	@MapTo(target=MapTarget.INHERIT)
	String idBando;
	
	@MapTo(target=MapTarget.INHERIT)
	Integer idDettTipolInterventoSettore;
	
	@MapTo(target=MapTarget.INHERIT)
	String dettaglioCodiceSettore;
	
	@MapTo(target=MapTarget.INHERIT)
	String dettaglioDescrizioneSettore;
	
	/** idTipolIntervento flagObbligatorio, codice, descrizione */
	@MapTo(target=MapTarget.INHERIT)
	String idTipolIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String flagObbligatorio;
	
	@MapTo(target=MapTarget.INHERIT)
	String codice;

	@MapTo(target=MapTarget.INHERIT)
	String descrizione;
	

	public String getChecked() {
		return checked;
	}


	public void setChecked(String checked) {
		this.checked = checked;
	}



	public Integer getIdDettTipolInterventoSettore() {
		return idDettTipolInterventoSettore;
	}



	public void setIdDettTipolInterventoSettore(Integer idDettTipolInterventoSettore) {
		this.idDettTipolInterventoSettore = idDettTipolInterventoSettore;
	}



	public String getDettaglioCodiceSettore() {
		return dettaglioCodiceSettore;
	}



	public void setDettaglioCodiceSettore(String dettaglioCodiceSettore) {
		this.dettaglioCodiceSettore = dettaglioCodiceSettore;
	}



	public String getDettaglioDescrizioneSettore() {
		return dettaglioDescrizioneSettore;
	}



	public void setDettaglioDescrizioneSettore(String dettaglioDescrizioneSettore) {
		this.dettaglioDescrizioneSettore = dettaglioDescrizioneSettore;
	}



	public String getIdTipolIntervento() {
		return idTipolIntervento;
	}



	public void setIdTipolIntervento(String idTipolIntervento) {
		this.idTipolIntervento = idTipolIntervento;
	}



	public String getFlagObbligatorio() {
		return flagObbligatorio;
	}



	public void setFlagObbligatorio(String flagObbligatorio) {
		this.flagObbligatorio = flagObbligatorio;
	}



	public String getCodice() {
		return codice;
	}



	public void setCodice(String codice) {
		this.codice = codice;
	}



	public String getDescrizione() {
		return descrizione;
	}



	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public String getIdBando() {
		return idBando;
	}


	public void setIdBando(String idBando) {
		this.idBando = idBando;
	}
	
}
