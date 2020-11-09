/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.veicoli;

import it.csi.findom.blocchetti.common.vo.pianoAcquistiAutomezzi.DettaglioAcquistiVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

/**
 * Classe definita per la gestione del bando
 * Automezzi privati 2020
 * - 3 nuovi attributi richiesti in riferimento alla jira 2071:
 * 
 * 1) emissioniCo2
 * 2) emissioniNox
 * 3) emissioni
 * 
 * @author 2r
 *
 */
public class CategoriaVeicoloVOV3 extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=MapTarget.INHERIT)
	DettaglioAcquistiVO dettaglioAcquistiVO;
	
	@MapTo(target=MapTarget.INHERIT)
	String idMassaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String idRecord;
	      
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
	
	/** jira 2701 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioni;
	
	/** jira 2701 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String tipoRecordEmissione;
	
	/** jira 2701 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String idEmissione;
	
	/** jira 2701 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String targa;
	
	/** jira 2105 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String telaio;
	
	/** jira 2701 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String checkAutoAcquistata;
	

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

//	public String getEmissioniCo2() {
//		return emissioniCo2;
//	}
//
//	public void setEmissioniCo2(String emissioniCo2) {
//		this.emissioniCo2 = emissioniCo2;
//	}
//
//	public String getEmissioniNox() {
//		return emissioniNox;
//	}
//
//	public void setEmissioniNox(String emissioniNox) {
//		this.emissioniNox = emissioniNox;
//	}

	public String getEmissioni() {
		return emissioni;
	}

	public void setEmissioni(String emissioni) {
		this.emissioni = emissioni;
	}

	public String getTipoRecordEmissione() {
		return tipoRecordEmissione;
	}

	public void setTipoRecordEmissione(String tipoRecordEmissione) {
		this.tipoRecordEmissione = tipoRecordEmissione;
	}

	public String getTarga() {
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

	public String getCheckAutoAcquistata() {
		return checkAutoAcquistata;
	}

	public void setCheckAutoAcquistata(String checkAutoAcquistata) {
		this.checkAutoAcquistata = checkAutoAcquistata;
	}

	public String getIdEmissione() {
		return idEmissione;
	}

	public void setIdEmissione(String idEmissione) {
		this.idEmissione = idEmissione;
	}

	public String getIdRecord() {
		return idRecord;
	}

	public void setIdRecord(String idRecord) {
		this.idRecord = idRecord;
	}

	@Override
	public String toString() {
		return "CategoriaVeicoloVOV3 [idMassaVeicolo=" + idMassaVeicolo
				+ ", idRecord=" + idRecord + ", descrBreveMassaVeicolo="
				+ descrBreveMassaVeicolo + ", descrizioneMassaVeicolo="
				+ descrizioneMassaVeicolo + ", idCategoriaVeicolo="
				+ idCategoriaVeicolo + ", idVoceSpesa=" + idVoceSpesa
				+ ", importoContributo=" + importoContributo
//				+ ", emissioniCo2=" + emissioniCo2 + ", emissioniNox="	+ emissioniNox 
				+ ", emissioni=" + emissioni
				+ ", tipoRecordEmissione=" + tipoRecordEmissione
				+ ", idEmissione=" + idEmissione + ", targa=" + targa
				+ ", checkAutoAcquistata=" + checkAutoAcquistata + "]";
	}

	public DettaglioAcquistiVO getDettaglioAcquistiVO() {
		return dettaglioAcquistiVO;
	}

	public void setDettaglioAcquistiVO(DettaglioAcquistiVO dettaglioAcquistiVO) {
		this.dettaglioAcquistiVO = dettaglioAcquistiVO;
	}

	public String getTelaio() {
		return telaio;
	}

	public void setTelaio(String telaio) {
		this.telaio = telaio;
	}
	
	
}
