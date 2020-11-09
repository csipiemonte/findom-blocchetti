/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.cultPianospeseSubCtg;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettaglioVoceSpesaInterventoCulturaVO extends CommonalityVO {

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
	String codiceTipolVoceSpesa;	// add1
	            
	@MapTo(target=MapTarget.INHERIT)
	String descrVoceSpesa;
	
	@MapTo(target=MapTarget.INHERIT)
	String titoloTipoIntervento;
	
	@MapTo(target=MapTarget.INHERIT)
	String titoloDettIntervento;
	   
	@MapTo(target=MapTarget.INHERIT)
	String titoloVoceDiSpesa; 

	@MapTo(target=MapTarget.INHERIT)
	String idCatVoceSpesa; 
	
	@MapTo(target=MapTarget.INHERIT)
	String descrizioneCatVoceSpesa; 
	
	@MapTo(target=MapTarget.INHERIT)
	String flagSpecificaz; 
	
	@MapTo(target=MapTarget.INHERIT)
	String tipoRecord; 

	@MapTo(target=MapTarget.INHERIT)
	String totaleVoceSpesa;

	@MapTo(target=MapTarget.INHERIT)
	String totale;
	
	@MapTo(target=MapTarget.INHERIT)
	String dettaglioVoce;
	
	@MapTo(target=MapTarget.INHERIT)
	String daCancellare;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrizioneTipolVoceSpesa; // add2
	
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

	public String getIdCatVoceSpesa() {
		return idCatVoceSpesa;
	}

	public String getDescrizioneCatVoceSpesa() {
		return descrizioneCatVoceSpesa;
	}

	public String getFlagSpecificaz() {
		return flagSpecificaz;
	}

	public void setIdCatVoceSpesa(String idCatVoceSpesa) {
		this.idCatVoceSpesa = idCatVoceSpesa;
	}

	public void setDescrizioneCatVoceSpesa(String descrizioneCatVoceSpesa) {
		this.descrizioneCatVoceSpesa = descrizioneCatVoceSpesa;
	}

	public void setFlagSpecificaz(String flagSpecificaz) {
		this.flagSpecificaz = flagSpecificaz;
	}

	public String getDettaglioVoce() {
		return dettaglioVoce;
	}

	public void setDettaglioVoce(String dettaglioVoce) {
		this.dettaglioVoce = dettaglioVoce;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	public String getCodiceTipolVoceSpesa() {
		return codiceTipolVoceSpesa;
	}

	public void setCodiceTipolVoceSpesa(String codiceTipolVoceSpesa) {
		this.codiceTipolVoceSpesa = codiceTipolVoceSpesa;
	}

	public String getDescrizioneTipolVoceSpesa() {
		return descrizioneTipolVoceSpesa;
	}

	public void setDescrizioneTipolVoceSpesa(String descrizioneTipolVoceSpesa) {
		this.descrizioneTipolVoceSpesa = descrizioneTipolVoceSpesa;
	} 

}
