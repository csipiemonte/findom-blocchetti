/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.azienda;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AziendaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "capitaleSociale")
	String capitaleSociale;
	
	@MapTo(target = INHERIT, name = "codiceFiscale")
	String codiceFiscale;	
	
	@MapTo(target = INHERIT, name = "partitaIVA")
	String partitaIVA;
	
	@MapTo(target = INHERIT, name = "denominazione")
	String denominazione;
	
	@MapTo(target = INHERIT, name = "idFormaGiuridica")
	String idFormaGiuridica;
	
	@MapTo(target = INHERIT, name = "codiceNazioneSedeLegale")
	String codiceNazioneSedeLegale;
	
	@MapTo(target = INHERIT, name = "quota")
	String quota;
	
	@MapTo(target = INHERIT, name = "codiceFormaGiuridica")
	String codiceFormaGiuridica;
	
	@MapTo(target = INHERIT, name = "descrFormaGiuridica")
	String descrFormaGiuridica;
	
	@MapTo(target = INHERIT, name = "descrNazioneSedeLegale")
	String descrNazioneSedeLegale;
	
	@MapTo(target = INHERIT, name = "aziendaSocioList")
	AziendaSocioVO[] aziendaSocioList;
	
	public String getCapitaleSociale() {
		return capitaleSociale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public String getIdFormaGiuridica() {
		return idFormaGiuridica;
	}

	public String getCodiceNazioneSedeLegale() {
		return codiceNazioneSedeLegale;
	}

	public String getQuota() {
		return quota;
	}

	public AziendaSocioVO[] getAziendaSocioList() {
		return aziendaSocioList;
	}

	public void setCapitaleSociale(String capitaleSociale) {
		this.capitaleSociale = capitaleSociale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public void setIdFormaGiuridica(String idFormaGiuridica) {
		this.idFormaGiuridica = idFormaGiuridica;
	}

	public void setCodiceNazioneSedeLegale(String codiceNazioneSedeLegale) {
		this.codiceNazioneSedeLegale = codiceNazioneSedeLegale;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public String getCodiceFormaGiuridica() {
		return codiceFormaGiuridica;
	}

	public String getDescrFormaGiuridica() {
		return descrFormaGiuridica;
	}

	public String getDescrNazioneSedeLegale() {
		return descrNazioneSedeLegale;
	}

	public void setCodiceFormaGiuridica(String codiceFormaGiuridica) {
		this.codiceFormaGiuridica = codiceFormaGiuridica;
	}

	public void setDescrFormaGiuridica(String descrFormaGiuridica) {
		this.descrFormaGiuridica = descrFormaGiuridica;
	}

	public void setDescrNazioneSedeLegale(String descrNazioneSedeLegale) {
		this.descrNazioneSedeLegale = descrNazioneSedeLegale;
	}

	public void setAziendaSocioList(AziendaSocioVO[] aziendaSocioList) {
		this.aziendaSocioList = aziendaSocioList;
	}
	
	
}
