/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.documentazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipologiaDocumentoVO extends CommonalityVO{
	
	
	@MapTo(target=INHERIT)
	Integer idallegato;
	
	@MapTo(target=INHERIT)
	String flag_obbl;

	
	@MapTo(target=INHERIT)
	String descrizione;
	
	


	public Integer getIdallegato() {
		return idallegato;
	}


	public void setIdallegato(Integer idallegato) {
		this.idallegato = idallegato;
	}


	public String getFlag_obbl() {
		return flag_obbl;
	}


	public void setFlag_obbl(String flag_obbl) {
		this.flag_obbl = flag_obbl;
	}


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}



	
	
}
