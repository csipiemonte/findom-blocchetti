/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;

import java.util.List;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaAiutoNGItemListVO  extends CommonalityVO  {
	
	@MapTo(target = INHERIT)
	String idTipoAiuto;

	@MapTo(target = INHERIT)
	String descrTipoAiuto;
    	  
    @MapTo(target = INHERIT)
	String codTipoAiuto;
    
	@MapTo(target = INHERIT)
	String numDettagli;
	
	@MapTo(target = INHERIT)
	String checked;
	
	@MapTo(target = INHERIT, name = "dettaglioAiutoList")
	DettaglioAiutoNGItemListVO[] dettaglioAiutoList;

	
	public String getNumDettagli() {
		return numDettagli;
	}

	public void setNumDettagli(String setNumDettagli) {
		this.numDettagli = setNumDettagli;
	}

	public String getIdTipoAiuto() {
		return idTipoAiuto;
	}

    public String getChecked() {
		return checked;
	}

	public void setIdTipoAiuto(String idTipoAiuto) {
		this.idTipoAiuto = idTipoAiuto;
	}

	public String getDescrTipoAiuto() {
		return descrTipoAiuto;
	}

	public String getCodTipoAiuto() {
		return codTipoAiuto;
	}

	public void setDescrTipoAiuto(String descrTipoAiuto) {
		this.descrTipoAiuto = descrTipoAiuto;
	}

	public void setCodTipoAiuto(String codTipoAiuto) {
		this.codTipoAiuto = codTipoAiuto;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public DettaglioAiutoNGItemListVO[] getDettaglioAiutoList() {
		return dettaglioAiutoList;
	}

	public void setDettaglioAiutoList(
			DettaglioAiutoNGItemListVO[] dettaglioAiutoList) {
		this.dettaglioAiutoList = dettaglioAiutoList;
	}


	
	
}
