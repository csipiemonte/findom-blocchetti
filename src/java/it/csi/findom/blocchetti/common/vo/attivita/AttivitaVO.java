/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.attivita;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AttivitaVO extends CommonalityVO {
	
	@MapTo(target=INHERIT)
	Long id;
	
	@MapTo(target=INHERIT)
	String codice;
	
	@MapTo(target=INHERIT)
	String descrizione;
	
	@MapTo(target=INHERIT)
	String descrtroncata;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrtroncata() {
		return descrtroncata;
	}

	public void setDescrtroncata(String descrtroncata) {
		this.descrtroncata = descrtroncata;
	}
	
	
}
