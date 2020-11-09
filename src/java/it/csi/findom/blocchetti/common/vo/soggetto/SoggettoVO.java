/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.soggetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SoggettoVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String idformagiuridica;
	
	@MapTo(target=INHERIT)
	String denominazione;
	//gestione beneficiario estero inizio qqq
	@MapTo(target=INHERIT)
	String idStato;
	//gestione beneficiario estero fine qqq
	public String getIdformagiuridica() {
		return idformagiuridica;
	}

	public void setIdformagiuridica(String idformagiuridica) {
		this.idformagiuridica = idformagiuridica;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	//gestione beneficiario estero inizio qqq
	public String getIdStato() {
		return idStato;
	}
	public void setIdStato(String idStato) {
		this.idStato = idStato;
	}	
	//gestione beneficiario estero inizio qqq
	
}
