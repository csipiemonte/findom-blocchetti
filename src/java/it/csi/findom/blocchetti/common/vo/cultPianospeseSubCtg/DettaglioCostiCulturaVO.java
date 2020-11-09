/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.cultPianospeseSubCtg;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettaglioCostiCulturaVO extends CommonalityVO {

	@MapTo(target = MapTarget.INHERIT)
	String identificativo;
	
	@MapTo(target=MapTarget.INHERIT)
	String daCancellare;
	
	@MapTo(target=MapTarget.INHERIT)
	String titolo;

	@MapTo(target=MapTarget.INHERIT)
	String idRecord;
	
	@MapTo(target=MapTarget.INHERIT)
	String idVoceSpesa;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrVoceSpesa;
		
	@MapTo(target=MapTarget.INHERIT)
	String descrizioneServizioBene;
	
	@MapTo(target=MapTarget.INHERIT)
	String fornitore;
	
	@MapTo(target=MapTarget.INHERIT)
	String codiceFiscale;
	
	@MapTo(target=MapTarget.INHERIT)
	String importoProposto;
	
	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public String getIdVoceSpesa() {
		return idVoceSpesa;
	}

	public String getDescrizioneServizioBene() {
		return descrizioneServizioBene;
	}

	public String getFornitore() {
		return fornitore;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getImportoProposto() {
		return importoProposto;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	public void setIdVoceSpesa(String idVoceSpesa) {
		this.idVoceSpesa = idVoceSpesa;
	}

	public void setDescrizioneServizioBene(String descrizioneServizioBene) {
		this.descrizioneServizioBene = descrizioneServizioBene;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public void setImportoProposto(String importoProposto) {
		this.importoProposto = importoProposto;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getIdRecord() {
		return idRecord;
	}

	public String getDescrVoceSpesa() {
		return descrVoceSpesa;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public void setIdRecord(String idRecord) {
		this.idRecord = idRecord;
	}

	public void setDescrVoceSpesa(String descrVoceSpesa) {
		this.descrVoceSpesa = descrVoceSpesa;
	}
}
