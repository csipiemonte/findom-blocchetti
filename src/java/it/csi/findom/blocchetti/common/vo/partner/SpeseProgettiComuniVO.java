/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SpeseProgettiComuniVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	String tipoRecord;                //record con funzione di: "1" = titolo con solo tipologia; "2" = titolo con tipologia e dettaglio; "3" = voce di spesa
		
	@MapTo(target = MapTarget.INHERIT)
	String idDomanda;

	@MapTo(target = MapTarget.INHERIT)
	String idTipologiaIntervento;

	@MapTo(target=INHERIT)
	String descrTipologiaIntervento;
	
	@MapTo(target=INHERIT)
	String codTipoIntervento;

	@MapTo(target=INHERIT)
	String idDettTipologiaIntervento;
	
	@MapTo(target=INHERIT)
	String descrDettTipologiaIntervento;
	
	@MapTo(target=INHERIT)
	String codDettIntervento;
	
	@MapTo(target=INHERIT)
	String idVoceSpesa;
	
	@MapTo(target=INHERIT)
	String descrVoceSpesa;
	
	@MapTo(target=INHERIT)
	String codVoceSpesa;
	
	@MapTo(target=INHERIT)
	String totaleVoceSpesa;
	

	@MapTo(target = MapTarget.INHERIT)
	String chiaveInterventoDettaglio; //qualunque sia il tipoRecord, identifica l'intervento (ed eventuale dettaglio intervento) a cui si riferisce l'oggetto corrente 
	
	@MapTo(target = MapTarget.INHERIT)
	String chiaveInterventoDettaglioVoceSpesa;    //valorizzato solo per tipoRecord = 3; identifica l'oggetto perche' è costituito da id intervento , id dettaglio e id voce spesa
	
	@MapTo(target = MapTarget.INHERIT)
	String titoloIntervento;          //valorizzato solo per tipoRecord = 1; contiene la stringa da visualizzare a video 
	
	@MapTo(target = MapTarget.INHERIT)
	String titoloDettaglio; //valorizzato solo per tipoRecord = 2; contiene la stringa da visualizzare a video 
	
	@MapTo(target = MapTarget.INHERIT)
	String titoloVoceSpesa;                //valorizzato solo per tipoRecord = 3; contiene la stringa da visualizzare a video

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getIdTipologiaIntervento() {
		return idTipologiaIntervento;
	}

	public void setIdTipologiaIntervento(String idTipologiaIntervento) {
		this.idTipologiaIntervento = idTipologiaIntervento;
	}

	public String getDescrTipologiaIntervento() {
		return descrTipologiaIntervento;
	}

	public void setDescrTipologiaIntervento(String descrTipologiaIntervento) {
		this.descrTipologiaIntervento = descrTipologiaIntervento;
	}

	public String getCodTipoIntervento() {
		return codTipoIntervento;
	}

	public void setCodTipoIntervento(String codTipoIntervento) {
		this.codTipoIntervento = codTipoIntervento;
	}

	public String getIdDettTipologiaIntervento() {
		return idDettTipologiaIntervento;
	}

	public void setIdDettTipologiaIntervento(String idDettTipologiaIntervento) {
		this.idDettTipologiaIntervento = idDettTipologiaIntervento;
	}

	public String getDescrDettTipologiaIntervento() {
		return descrDettTipologiaIntervento;
	}

	public void setDescrDettTipologiaIntervento(String descrDettTipologiaIntervento) {
		this.descrDettTipologiaIntervento = descrDettTipologiaIntervento;
	}

	public String getCodDettIntervento() {
		return codDettIntervento;
	}

	public void setCodDettIntervento(String codDettIntervento) {
		this.codDettIntervento = codDettIntervento;
	}

	public String getIdVoceSpesa() {
		return idVoceSpesa;
	}

	public void setIdVoceSpesa(String idVoceSpesa) {
		this.idVoceSpesa = idVoceSpesa;
	}

	public String getDescrVoceSpesa() {
		return descrVoceSpesa;
	}

	public void setDescrVoceSpesa(String descrVoceSpesa) {
		this.descrVoceSpesa = descrVoceSpesa;
	}

	public String getCodVoceSpesa() {
		return codVoceSpesa;
	}

	public void setCodVoceSpesa(String codVoceSpesa) {
		this.codVoceSpesa = codVoceSpesa;
	}

	public String getTotaleVoceSpesa() {
		return totaleVoceSpesa;
	}

	public void setTotaleVoceSpesa(String totaleVoceSpesa) {
		this.totaleVoceSpesa = totaleVoceSpesa;
	}

	public String getChiaveInterventoDettaglio() {
		return chiaveInterventoDettaglio;
	}

	public void setChiaveInterventoDettaglio(String chiaveInterventoDettaglio) {
		this.chiaveInterventoDettaglio = chiaveInterventoDettaglio;
	}

	public String getChiaveInterventoDettaglioVoceSpesa() {
		return chiaveInterventoDettaglioVoceSpesa;
	}

	public void setChiaveInterventoDettaglioVoceSpesa(
			String chiaveInterventoDettaglioVoceSpesa) {
		this.chiaveInterventoDettaglioVoceSpesa = chiaveInterventoDettaglioVoceSpesa;
	}

	public String getTitoloIntervento() {
		return titoloIntervento;
	}

	public void setTitoloIntervento(String titoloIntervento) {
		this.titoloIntervento = titoloIntervento;
	}

	public String getTitoloDettaglio() {
		return titoloDettaglio;
	}

	public void setTitoloDettaglio(String titoloDettaglio) {
		this.titoloDettaglio = titoloDettaglio;
	}

	public String getTitoloVoceSpesa() {
		return titoloVoceSpesa;
	}

	public void setTitoloVoceSpesa(String titoloVoceSpesa) {
		this.titoloVoceSpesa = titoloVoceSpesa;
	}
	
}
