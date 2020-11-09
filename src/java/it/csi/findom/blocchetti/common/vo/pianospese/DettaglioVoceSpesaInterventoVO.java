/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianospese;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettaglioVoceSpesaInterventoVO extends CommonalityVO {

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
	
	/**
	 * : : solo per sistema neve
	 */
	@MapTo(target=MapTarget.INHERIT)
	String totVoceSpesa_CTA9768ASF;
	
	@MapTo(target=MapTarget.INHERIT)
	String totVoceSpesa_CTA9767ASD;
	
	@MapTo(target=MapTarget.INHERIT)
	String totVoceSpesa_CTA9869PD;
	
	@MapTo(target=MapTarget.INHERIT)
	String totVoceSpesa_CTA9870PF;
	
	@MapTo(target=MapTarget.INHERIT)
	String totVoceSpesa_CTC;
	
	//attributo per gestire visualizzazione delle spesa, valori ammessi: "selezionato", "nonselezionato"
	@MapTo(target=MapTarget.INHERIT)
	String visibilita;


	public String getTotVoceSpesa_CTC() {
		return totVoceSpesa_CTC;
	}

	public void setTotVoceSpesa_CTC(String totVoceSpesa_CTC) {
		this.totVoceSpesa_CTC = totVoceSpesa_CTC;
	}

	public String getTotVoceSpesa_CTA9768ASF() {
		return totVoceSpesa_CTA9768ASF;
	}

	public void setTotVoceSpesa_CTA9768ASF(String totVoceSpesa_CTA9768ASF) {
		this.totVoceSpesa_CTA9768ASF = totVoceSpesa_CTA9768ASF;
	}

	public String getTotVoceSpesa_CTA9767ASD() {
		return totVoceSpesa_CTA9767ASD;
	}

	public void setTotVoceSpesa_CTA9767ASD(String totVoceSpesa_CTA9767ASD) {
		this.totVoceSpesa_CTA9767ASD = totVoceSpesa_CTA9767ASD;
	}

	public String getTotVoceSpesa_CTA9869PD() {
		return totVoceSpesa_CTA9869PD;
	}

	public void setTotVoceSpesa_CTA9869PD(String totVoceSpesa_CTA9869PD) {
		this.totVoceSpesa_CTA9869PD = totVoceSpesa_CTA9869PD;
	}

	public String getTotVoceSpesa_CTA9870PF() {
		return totVoceSpesa_CTA9870PF;
	}

	public void setTotVoceSpesa_CTA9870PF(String totVoceSpesa_CTA9870PF) {
		this.totVoceSpesa_CTA9870PF = totVoceSpesa_CTA9870PF;
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
		sb.append(", totale=" + totale );
		sb.append(", visibilita=" + visibilita );
		sb.append("]");
		
		return sb.toString();
	}

	public String getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(String visibilita) {
		this.visibilita = visibilita;
	} 
	
}
