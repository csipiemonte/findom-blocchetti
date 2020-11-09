/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.documentazione;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DocumentazioneItemVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	String idallegato;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrizione;

	@MapTo(target=MapTarget.INHERIT)
	String obbligatorio;

	@MapTo(target=MapTarget.INHERIT)
	String differibile;

	
	public String getIdallegato() {
		return idallegato;
	}

	public void setIdallegato(String idallegato) {
		this.idallegato = idallegato;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public String getDifferibile() {
		return differibile;
	}

	public void setDifferibile(String differibile) {
		this.differibile = differibile;
	}
	
}
