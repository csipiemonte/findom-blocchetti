/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PersonaINFOCVO extends PersonaVO  {
	
	@MapTo(target = INHERIT) String capResidenza;
	@MapTo(target = INHERIT) String codToponimoResid;
	@MapTo(target = INHERIT) String dataNascita;
	@MapTo(target = INHERIT) String descrComuneNascita;
	@MapTo(target = INHERIT) String descrComuneRes;
	@MapTo(target = INHERIT) String descrIndicatoriPotereF;
	@MapTo(target = INHERIT) String descrToponimoResid;
	@MapTo(target = INHERIT) String flagElettore;
	@MapTo(target = INHERIT) String indicPoteriFirma;
	@MapTo(target = INHERIT) String numCivicoResid;
	@MapTo(target = INHERIT) String percentPartecip;
	@MapTo(target = INHERIT) String progrOrdineVisura;
	@MapTo(target = INHERIT) String progrUnitaLocale;
	@MapTo(target = INHERIT) String quotaPartecipaz;
	@MapTo(target = INHERIT) String quotaPartecipazEuro;
	@MapTo(target = INHERIT) String sesso;
	@MapTo(target = INHERIT) String siglaProvNascita;
	@MapTo(target = INHERIT) String siglaProvResidenza;
	@MapTo(target = INHERIT) String viaResidenza;
	@MapTo(target = INHERIT) String codComuneNascita;
	@MapTo(target = INHERIT) String codComuneRes;
	@MapTo(target = INHERIT) String codCittadinanza;
	@MapTo(target = INHERIT) String codStatoNascita;
	@MapTo(target = INHERIT) String codStatoRes;
	@MapTo(target = INHERIT) String descrCittadinanza;
	@MapTo(target = INHERIT) String descrStatoNascita;
	@MapTo(target = INHERIT) String descrStatoRes;
	
	public String getCapResidenza() {
		return capResidenza;
	}
	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}
	public String getCodToponimoResid() {
		return codToponimoResid;
	}
	public void setCodToponimoResid(String codToponimoResid) {
		this.codToponimoResid = codToponimoResid;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getDescrComuneNascita() {
		return descrComuneNascita;
	}
	public void setDescrComuneNascita(String descrComuneNascita) {
		this.descrComuneNascita = descrComuneNascita;
	}
	public String getDescrComuneRes() {
		return descrComuneRes;
	}
	public void setDescrComuneRes(String descrComuneRes) {
		this.descrComuneRes = descrComuneRes;
	}
	public String getDescrIndicatoriPotereF() {
		return descrIndicatoriPotereF;
	}
	public void setDescrIndicatoriPotereF(String descrIndicatoriPotereF) {
		this.descrIndicatoriPotereF = descrIndicatoriPotereF;
	}
	public String getDescrToponimoResid() {
		return descrToponimoResid;
	}
	public void setDescrToponimoResid(String descrToponimoResid) {
		this.descrToponimoResid = descrToponimoResid;
	}
	public String getFlagElettore() {
		return flagElettore;
	}
	public void setFlagElettore(String flagElettore) {
		this.flagElettore = flagElettore;
	}
	public String getIndicPoteriFirma() {
		return indicPoteriFirma;
	}
	public void setIndicPoteriFirma(String indicPoteriFirma) {
		this.indicPoteriFirma = indicPoteriFirma;
	}
	public String getNumCivicoResid() {
		return numCivicoResid;
	}
	public void setNumCivicoResid(String numCivicoResid) {
		this.numCivicoResid = numCivicoResid;
	}
	public String getPercentPartecip() {
		return percentPartecip;
	}
	public void setPercentPartecip(String percentPartecip) {
		this.percentPartecip = percentPartecip;
	}
	public String getProgrOrdineVisura() {
		return progrOrdineVisura;
	}
	public void setProgrOrdineVisura(String progrOrdineVisura) {
		this.progrOrdineVisura = progrOrdineVisura;
	}
	public String getProgrUnitaLocale() {
		return progrUnitaLocale;
	}
	public void setProgrUnitaLocale(String progrUnitaLocale) {
		this.progrUnitaLocale = progrUnitaLocale;
	}
	public String getQuotaPartecipaz() {
		return quotaPartecipaz;
	}
	public void setQuotaPartecipaz(String quotaPartecipaz) {
		this.quotaPartecipaz = quotaPartecipaz;
	}
	public String getQuotaPartecipazEuro() {
		return quotaPartecipazEuro;
	}
	public void setQuotaPartecipazEuro(String quotaPartecipazEuro) {
		this.quotaPartecipazEuro = quotaPartecipazEuro;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public String getSiglaProvNascita() {
		return siglaProvNascita;
	}
	public void setSiglaProvNascita(String siglaProvNascita) {
		this.siglaProvNascita = siglaProvNascita;
	}
	public String getSiglaProvResidenza() {
		return siglaProvResidenza;
	}
	public void setSiglaProvResidenza(String siglaProvResidenza) {
		this.siglaProvResidenza = siglaProvResidenza;
	}
	public String getViaResidenza() {
		return viaResidenza;
	}
	public void setViaResidenza(String viaResidenza) {
		this.viaResidenza = viaResidenza;
	}
	public String getCodComuneNascita() {
		return codComuneNascita;
	}
	public void setCodComuneNascita(String codComuneNascita) {
		this.codComuneNascita = codComuneNascita;
	}
	public String getCodCittadinanza() {
		return codCittadinanza;
	}
	public void setCodCittadinanza(String codCittadinanza) {
		this.codCittadinanza = codCittadinanza;
	}
	public String getCodStatoNascita() {
		return codStatoNascita;
	}
	public void setCodStatoNascita(String codStatoNascita) {
		this.codStatoNascita = codStatoNascita;
	}
	public String getDescrCittadinanza() {
		return descrCittadinanza;
	}
	public void setDescrCittadinanza(String descrCittadinanza) {
		this.descrCittadinanza = descrCittadinanza;
	}
	public String getDescrStatoNascita() {
		return descrStatoNascita;
	}
	public void setDescrStatoNascita(String descrStatoNascita) {
		this.descrStatoNascita = descrStatoNascita;
	}
	public String getCodComuneRes() {
		return codComuneRes;
	}
	public void setCodComuneRes(String codComuneRes) {
		this.codComuneRes = codComuneRes;
	}
	public String getCodStatoRes() {
		return codStatoRes;
	}
	public void setCodStatoRes(String codStatoRes) {
		this.codStatoRes = codStatoRes;
	}
	public String getDescrStatoRes() {
		return descrStatoRes;
	}
	public void setDescrStatoRes(String descrStatoRes) {
		this.descrStatoRes = descrStatoRes;
	}
	
}

