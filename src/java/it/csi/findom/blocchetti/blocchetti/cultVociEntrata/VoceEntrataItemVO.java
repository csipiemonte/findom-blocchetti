/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultVociEntrata;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class VoceEntrataItemVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String idVoceEntrata;				         

	@MapTo(target=INHERIT)
	String descrizione;

	@MapTo(target=INHERIT)
	String descrBreve;

	@MapTo(target=INHERIT)
	String dettaglio;

	@MapTo(target=INHERIT)
	String indicazioni;				                             

	@MapTo(target=INHERIT)
	String flagDuplicabile;

	@MapTo(target=INHERIT)
	String flagEdit;
	
	@MapTo(target=INHERIT)
	String importo;

	@MapTo(target=INHERIT)
	String tipoRecord;
	
	@MapTo(target=INHERIT)
	String elaborato;

	@MapTo(target=INHERIT)
	String trovato;
	
	@MapTo(target=INHERIT)
	String daCancellare;
	
	public String getIdVoceEntrata() {
		return idVoceEntrata;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getDescrBreve() {
		return descrBreve;
	}

	public String getDettaglio() {
		return dettaglio;
	}

	public String getIndicazioni() {
		return indicazioni;
	}

	public String getFlagDuplicabile() {
		return flagDuplicabile;
	}

	public String getFlagEdit() {
		return flagEdit;
	}

	public void setIdVoceEntrata(String idVoceEntrata) {
		this.idVoceEntrata = idVoceEntrata;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setDescrBreve(String descrBreve) {
		this.descrBreve = descrBreve;
	}

	public void setDettaglio(String dettaglio) {
		this.dettaglio = dettaglio;
	}

	public void setIndicazioni(String indicazioni) {
		this.indicazioni = indicazioni;
	}

	public void setFlagDuplicabile(String flagDuplicabile) {
		this.flagDuplicabile = flagDuplicabile;
	}

	public void setFlagEdit(String flagEdit) {
		this.flagEdit = flagEdit;
	}

	public String getImporto() {
		return importo;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getElaborato() {
		return elaborato;
	}

	public void setElaborato(String elaborato) {
		this.elaborato = elaborato;
	}

	public String getTrovato() {
		return trovato;
	}

	public void setTrovato(String trovato) {
		this.trovato = trovato;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}
	
}
