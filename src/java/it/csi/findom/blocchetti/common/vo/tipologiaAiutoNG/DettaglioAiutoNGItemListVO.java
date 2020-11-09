/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettaglioAiutoNGItemListVO  extends CommonalityVO {

	@MapTo(target = INHERIT)
	String idTipoAiutoDett;
	
	@MapTo(target = INHERIT)
	String idDettAiuto;
	
	@MapTo(target = INHERIT)
	String checked;
	
	@MapTo(target = INHERIT)
	String descrDettAiuto;

	@MapTo(target = INHERIT)
	String codiceDettAiuto;
		
	
	public String getIdTipoAiutoDett() {
		return idTipoAiutoDett;
	}

	public String getIdDettAiuto() {
		return idDettAiuto;
	}

	public void setIdTipoAiutoDett(String idTipoAiutoDett) {
		this.idTipoAiutoDett = idTipoAiutoDett;
	}

	public void setIdDettAiuto(String idDettAiuto) {
		this.idDettAiuto = idDettAiuto;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getDescrDettAiuto() {
		return descrDettAiuto;
	}

	public String getCodiceDettAiuto() {
		return codiceDettAiuto;
	}

	public void setDescrDettAiuto(String descrDettAiuto) {
		this.descrDettAiuto = descrDettAiuto;
	}

	public void setCodiceDettAiuto(String codiceDettAiuto) {
		this.codiceDettAiuto = codiceDettAiuto;
	}
	
	
}
