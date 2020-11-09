/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class PartnerVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	PartnerItemVO[] partnerItemVOArray;	
	
	@MapTo(target = INHERIT)
	String indicePartnerSelezionato;  //indice del partner selezionato nella lista su xml, che puo' essere diverso dalla posizione che ha a video, per via dell'ordinamento
	
	@MapTo(target = INHERIT)
	String idPartnerSelezionato;  //id_partner del record selezionato (serve per identificare il record da visualizzare quando vado nel dettaglio)
	
	@MapTo(target = INHERIT)
	String operazioneCorrente;
	
	@MapTo(target = INHERIT)
	String note;
	
	public boolean isEmpty() {
		return partnerItemVOArray == null;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PartnerVO: ");
		sb.append("indicePartnerSelezionato = " + indicePartnerSelezionato +";");
		sb.append("idPartnerSelezionato = " + idPartnerSelezionato +";");
		sb.append("operazioneCorrente = " + operazioneCorrente +";");
		sb.append("note = " + note +";");
		sb.append("[partnerList=" + Arrays.toString(partnerItemVOArray) + "]");
		return sb.toString();
		//return "PartnerVO: indicePartnerSelezionato = " + indicePartnerSelezionato + "; [partnerList=" + Arrays.toString(partnerItemVOArray) + "]";
	}
	
	public PartnerItemVO[] getPartnerItemVOArray() {
		return partnerItemVOArray;
	}
	public void setPartnerItemVOArray(PartnerItemVO[] partnerItemVOArray) {
		this.partnerItemVOArray = partnerItemVOArray;
	}
	public String getIndicePartnerSelezionato() {
		return indicePartnerSelezionato;
	}
	public void setIndicePartnerSelezionato(String indicePartnerSelezionato) {
		this.indicePartnerSelezionato = indicePartnerSelezionato;
	}
	public String getOperazioneCorrente() {
		return operazioneCorrente;
	}
	public void setOperazioneCorrente(String operazioneCorrente) {
		this.operazioneCorrente = operazioneCorrente;
	}
	
	public String getIdPartnerSelezionato() {
		return idPartnerSelezionato;
	}
	public void setIdPartnerSelezionato(String idPartnerSelezionato) {
		this.idPartnerSelezionato = idPartnerSelezionato;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}	

}
