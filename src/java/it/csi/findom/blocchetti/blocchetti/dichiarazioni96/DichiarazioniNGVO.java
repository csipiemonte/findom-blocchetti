/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioni96;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.findom.blocchetti.common.vo.agevolazioni.AgevolazioniVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DichiarazioniNGVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	AgevolazioniVO[] agevolazioniList; 
	
	@MapTo(target=INHERIT)
	String trattamentoDatiPersonali;

	@MapTo(target=INHERIT)
	String regoleCompilazione;

	@MapTo(target=INHERIT)
	String presaVisione;
	
	@MapTo(target=INHERIT)
	String attoCostitutivoProdotto;
	
	@MapTo(target=INHERIT)
	String provvedimentiAutorizzatori;

	public String getProvvedimentiAutorizzatori() {
		return provvedimentiAutorizzatori;
	}

	public void setProvvedimentiAutorizzatori(String provvedimentiAutorizzatori) {
		this.provvedimentiAutorizzatori = provvedimentiAutorizzatori;
	}

	public String getAttoCostitutivoProdotto() {
		return attoCostitutivoProdotto;
	}

	public void setAttoCostitutivoProdotto(String attoCostitutivoProdotto) {
		this.attoCostitutivoProdotto = attoCostitutivoProdotto;
	}

	public AgevolazioniVO[] getAgevolazioniList() {
		return agevolazioniList;
	}

	public void setAgevolazioniList(AgevolazioniVO[] agevolazioniList) {
		this.agevolazioniList = agevolazioniList;
	}

	public String getTrattamentoDatiPersonali() {
		return trattamentoDatiPersonali;
	}

	public void setTrattamentoDatiPersonali(String trattamentoDatiPersonali) {
		this.trattamentoDatiPersonali = trattamentoDatiPersonali;
	}

	public String getRegoleCompilazione() {
		return regoleCompilazione;
	}

	public void setRegoleCompilazione(String regoleCompilazione) {
		this.regoleCompilazione = regoleCompilazione;
	}

	public String getPresaVisione() {
		return presaVisione;
	}

	public void setPresaVisione(String presaVisione) {
		this.presaVisione = presaVisione;
	}
	
}
