/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianoAcquistiAutomezzi;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettaglioVoceSpesaInterventoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	String idTipoIntervento;
	      
	@MapTo(target=MapTarget.INHERIT)
	String codTipoIntervento;
	      
	@MapTo(target=MapTarget.INHERIT)
	String descrTipoIntervento;

	@MapTo(target=MapTarget.INHERIT)
	String idDettIntervento;
	   
	@MapTo(target=MapTarget.INHERIT)
	String codDettIntervento;
	      
	@MapTo(target=MapTarget.INHERIT)
	String descrDettIntervento;
			
	@MapTo(target=MapTarget.INHERIT)
	String idVoceSpesa;
	      
	@MapTo(target=MapTarget.INHERIT)
	String codVoceSpesa;	
	            
	@MapTo(target=MapTarget.INHERIT)
	String descrVoceSpesa;
	
	@MapTo(target=MapTarget.INHERIT)
	String titoloTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String titoloDettIntervento;
	   
	@MapTo(target=MapTarget.INHERIT)
	String titoloVoceDiSpesa; 

	@MapTo(target=MapTarget.INHERIT)
	String tipoRecord; 

	@MapTo(target=MapTarget.INHERIT)
	String totaleVoceSpesa;

	@MapTo(target=MapTarget.INHERIT)
	String totale;
	
	@MapTo(target=MapTarget.INHERIT)
	String idMassaVeicolo;
	            
	@MapTo(target=MapTarget.INHERIT)
	String descrMassaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String totaleBonusPremialita;

	@MapTo(target=MapTarget.INHERIT)
	String totaleVoceSpesaPremialita;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioniCo2;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioniNox;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioni;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String tipoRecordEmissione;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String targa;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String checkAutoAcquistata;

	public String getIdTipoIntervento() {
		return idTipoIntervento;
	}

	public String getCodTipoIntervento() {
		return codTipoIntervento;
	}

	public String getDescrTipoIntervento() {
		return descrTipoIntervento;
	}

	public String getIdDettIntervento() {
		return idDettIntervento;
	}

	public String getCodDettIntervento() {
		return codDettIntervento;
	}

	public String getDescrDettIntervento() {
		return descrDettIntervento;
	}

	public String getIdVoceSpesa() {
		return idVoceSpesa;
	}

	public String getCodVoceSpesa() {
		return codVoceSpesa;
	}

	public String getDescrVoceSpesa() {
		return descrVoceSpesa;
	}

	public String getTitoloDettIntervento() {
		return titoloDettIntervento;
	}

	public String getTitoloVoceDiSpesa() {
		return titoloVoceDiSpesa;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public String getTotaleVoceSpesa() {
		return totaleVoceSpesa;
	}

	public String getTitoloTipoIntervento() {
		return titoloTipoIntervento;
	}
	
	

	public void setTitoloTipoIntervento(String titoloTipoIntervento) {
		this.titoloTipoIntervento = titoloTipoIntervento;
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

	public void setIdDettIntervento(String idDettIntervento) {
		this.idDettIntervento = idDettIntervento;
	}

	public void setCodDettIntervento(String codDettIntervento) {
		this.codDettIntervento = codDettIntervento;
	}

	public void setDescrDettIntervento(String descrDettIntervento) {
		this.descrDettIntervento = descrDettIntervento;
	}

	public void setIdVoceSpesa(String idVoceSpesa) {
		this.idVoceSpesa = idVoceSpesa;
	}

	public void setCodVoceSpesa(String codVoceSpesa) {
		this.codVoceSpesa = codVoceSpesa;
	}

	public void setDescrVoceSpesa(String descrVoceSpesa) {
		this.descrVoceSpesa = descrVoceSpesa;
	}

	public void setTitoloDettIntervento(String titoloDettIntervento) {
		this.titoloDettIntervento = titoloDettIntervento;
	}

	public void setTitoloVoceDiSpesa(String titoloVoceDiSpesa) {
		this.titoloVoceDiSpesa = titoloVoceDiSpesa;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public void setTotaleVoceSpesa(String totaleVoceSpesa) {
		this.totaleVoceSpesa = totaleVoceSpesa;
	}

	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}
	
	public String getIdMassaVeicolo() {
		return idMassaVeicolo;
	}

	public void setIdMassaVeicolo(String idMassaVeicolo) {
		this.idMassaVeicolo = idMassaVeicolo;
	}

	public String getDescrMassaVeicolo() {
		return descrMassaVeicolo;
	}

	public void setDescrMassaVeicolo(String descrMassaVeicolo) {
		this.descrMassaVeicolo = descrMassaVeicolo;
	}

	public String getTotaleBonusPremialita() {
		return totaleBonusPremialita;
	}

	public void setTotaleBonusPremialita(String totaleBonusPremialita) {
		this.totaleBonusPremialita = totaleBonusPremialita;
	}
	


	public String getTotaleVoceSpesaPremialita() {
		return totaleVoceSpesaPremialita;
	}

	public void setTotaleVoceSpesaPremialita(String totaleVoceSpesaPremialita) {
		this.totaleVoceSpesaPremialita = totaleVoceSpesaPremialita;
	}

	public String getEmissioniCo2() {
		return emissioniCo2;
	}

	public void setEmissioniCo2(String emissioniCo2) {
		this.emissioniCo2 = emissioniCo2;
	}

	public String getEmissioniNox() {
		return emissioniNox;
	}

	public void setEmissioniNox(String emissioniNox) {
		this.emissioniNox = emissioniNox;
	}

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
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("DettaglioVoceSpesaInterventoVO [");
		sb.append("idTipoIntervento=" + idTipoIntervento );
		sb.append(", codTipoIntervento=" + codTipoIntervento );
		sb.append(", descrTipoIntervento=" + descrTipoIntervento );
		sb.append(", idDettIntervento=" + idDettIntervento );
		sb.append(", codDettIntervento=" + codDettIntervento );
		sb.append(", descrDettIntervento=" + descrDettIntervento );
		sb.append(", idVoceSpesa=" + idVoceSpesa );
		sb.append(", codVoceSpesa=" + codVoceSpesa );
		sb.append(", descrVoceSpesa=" + descrVoceSpesa );
		sb.append(", titoloTipoIntervento=" + titoloTipoIntervento );
		sb.append(", titoloDettIntervento=" + titoloDettIntervento );
		sb.append(", titoloVoceDiSpesa=" + titoloVoceDiSpesa );
		sb.append(", tipoRecord=" + tipoRecord );
		sb.append(", totaleVoceSpesa=" + totaleVoceSpesa );
		sb.append(", totaleBonusPremialita=" + totaleBonusPremialita );
		sb.append(", totaleVoceSpesaPremialita=" + totaleVoceSpesaPremialita);
		sb.append(", totale=" + totale );
		sb.append("]");
		
		return sb.toString();
	}
}
