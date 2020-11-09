/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DatoCostitutivoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT) String capitSocDeliberato;
	@MapTo(target = INHERIT) String capitSocSottoscr;
	@MapTo(target = INHERIT) String capitSocVersato;
	@MapTo(target = INHERIT) String capitaleSocDelib;
	@MapTo(target = INHERIT) String capitalesocVers;
	@MapTo(target = INHERIT) String codDurataCS;
	@MapTo(target = INHERIT) String codFormaAmministr;
	@MapTo(target = INHERIT) String codTipoAtto;
	@MapTo(target = INHERIT) String codTipoConferim;
	@MapTo(target = INHERIT) String dataCostituzione;
	@MapTo(target = INHERIT) String dataFineEsAmmt;
	@MapTo(target = INHERIT) String dataFondazione;
	@MapTo(target = INHERIT) String dataIniEsAmmt;
	@MapTo(target = INHERIT) String dataRegAtto;
	@MapTo(target = INHERIT) String dataScadenzaPrimoEsercizio;
	@MapTo(target = INHERIT) String dataTermineSocieta;
	@MapTo(target = INHERIT) String descrFormaAmministr;
	@MapTo(target = INHERIT) String descrProvUffReg;
	@MapTo(target = INHERIT) String descrSiglaProvNotaio;
	@MapTo(target = INHERIT) String descrTipoAtto;
	@MapTo(target = INHERIT) String descrTipoConferim;
	@MapTo(target = INHERIT) String flagDurataIllimitata;
	@MapTo(target = INHERIT) String localitaNotaio;
	@MapTo(target = INHERIT) String notaio;
	@MapTo(target = INHERIT) String numAnniEsAmmt;
	@MapTo(target = INHERIT) String numAzioniCapitSociale;
	@MapTo(target = INHERIT) String numMaxMembriCS;
	@MapTo(target = INHERIT) String numMembriCSCarica;
	@MapTo(target = INHERIT) String numMinMembriCS;
	@MapTo(target = INHERIT) String numRegAtto;
	@MapTo(target = INHERIT) String numRepertorio;
	@MapTo(target = INHERIT) String numSoci;
	@MapTo(target = INHERIT) String numSociCarica;
	@MapTo(target = INHERIT) String scadenzaEsSucc;
	@MapTo(target = INHERIT) String siglaProvNotaio;
	@MapTo(target = INHERIT) String siglaProvUffRegistro;
	@MapTo(target = INHERIT) String totFondoConsortE;
	@MapTo(target = INHERIT) String totFondoConsortile;
	@MapTo(target = INHERIT) String totQuoteCapitSoc;
	@MapTo(target = INHERIT) String totQuoteCapitSocE;
	@MapTo(target = INHERIT) String ufficioRegistro;
	@MapTo(target = INHERIT) String valAzioniCapitSoc;
	@MapTo(target = INHERIT) String valoreAzioniCapitSoc;
	@MapTo(target = INHERIT) String valutaCapitSociale;
	@MapTo(target = INHERIT) String valutaCapitale;
	
	public String getCapitSocDeliberato() {
		return capitSocDeliberato;
	}
	public void setCapitSocDeliberato(String capitSocDeliberato) {
		this.capitSocDeliberato = capitSocDeliberato;
	}
	public String getCapitSocSottoscr() {
		return capitSocSottoscr;
	}
	public void setCapitSocSottoscr(String capitSocSottoscr) {
		this.capitSocSottoscr = capitSocSottoscr;
	}
	public String getCapitSocVersato() {
		return capitSocVersato;
	}
	public void setCapitSocVersato(String capitSocVersato) {
		this.capitSocVersato = capitSocVersato;
	}
	public String getCapitaleSocDelib() {
		return capitaleSocDelib;
	}
	public void setCapitaleSocDelib(String capitaleSocDelib) {
		this.capitaleSocDelib = capitaleSocDelib;
	}
	public String getCapitalesocVers() {
		return capitalesocVers;
	}
	public void setCapitalesocVers(String capitalesocVers) {
		this.capitalesocVers = capitalesocVers;
	}
	public String getCodDurataCS() {
		return codDurataCS;
	}
	public void setCodDurataCS(String codDurataCS) {
		this.codDurataCS = codDurataCS;
	}
	public String getCodFormaAmministr() {
		return codFormaAmministr;
	}
	public void setCodFormaAmministr(String codFormaAmministr) {
		this.codFormaAmministr = codFormaAmministr;
	}
	public String getCodTipoAtto() {
		return codTipoAtto;
	}
	public void setCodTipoAtto(String codTipoAtto) {
		this.codTipoAtto = codTipoAtto;
	}
	public String getCodTipoConferim() {
		return codTipoConferim;
	}
	public void setCodTipoConferim(String codTipoConferim) {
		this.codTipoConferim = codTipoConferim;
	}
	public String getDataCostituzione() {
		return dataCostituzione;
	}
	public void setDataCostituzione(String dataCostituzione) {
		this.dataCostituzione = dataCostituzione;
	}
	public String getDataFineEsAmmt() {
		return dataFineEsAmmt;
	}
	public void setDataFineEsAmmt(String dataFineEsAmmt) {
		this.dataFineEsAmmt = dataFineEsAmmt;
	}
	public String getDataFondazione() {
		return dataFondazione;
	}
	public void setDataFondazione(String dataFondazione) {
		this.dataFondazione = dataFondazione;
	}
	public String getDataIniEsAmmt() {
		return dataIniEsAmmt;
	}
	public void setDataIniEsAmmt(String dataIniEsAmmt) {
		this.dataIniEsAmmt = dataIniEsAmmt;
	}
	public String getDataRegAtto() {
		return dataRegAtto;
	}
	public void setDataRegAtto(String dataRegAtto) {
		this.dataRegAtto = dataRegAtto;
	}
	public String getDataScadenzaPrimoEsercizio() {
		return dataScadenzaPrimoEsercizio;
	}
	public void setDataScadenzaPrimoEsercizio(String dataScadenzaPrimoEsercizio) {
		this.dataScadenzaPrimoEsercizio = dataScadenzaPrimoEsercizio;
	}
	public String getDataTermineSocieta() {
		return dataTermineSocieta;
	}
	public void setDataTermineSocieta(String dataTermineSocieta) {
		this.dataTermineSocieta = dataTermineSocieta;
	}
	public String getDescrFormaAmministr() {
		return descrFormaAmministr;
	}
	public void setDescrFormaAmministr(String descrFormaAmministr) {
		this.descrFormaAmministr = descrFormaAmministr;
	}
	public String getDescrProvUffReg() {
		return descrProvUffReg;
	}
	public void setDescrProvUffReg(String descrProvUffReg) {
		this.descrProvUffReg = descrProvUffReg;
	}
	public String getDescrSiglaProvNotaio() {
		return descrSiglaProvNotaio;
	}
	public void setDescrSiglaProvNotaio(String descrSiglaProvNotaio) {
		this.descrSiglaProvNotaio = descrSiglaProvNotaio;
	}
	public String getDescrTipoAtto() {
		return descrTipoAtto;
	}
	public void setDescrTipoAtto(String descrTipoAtto) {
		this.descrTipoAtto = descrTipoAtto;
	}
	public String getDescrTipoConferim() {
		return descrTipoConferim;
	}
	public void setDescrTipoConferim(String descrTipoConferim) {
		this.descrTipoConferim = descrTipoConferim;
	}
	public String getFlagDurataIllimitata() {
		return flagDurataIllimitata;
	}
	public void setFlagDurataIllimitata(String flagDurataIllimitata) {
		this.flagDurataIllimitata = flagDurataIllimitata;
	}
	public String getLocalitaNotaio() {
		return localitaNotaio;
	}
	public void setLocalitaNotaio(String localitaNotaio) {
		this.localitaNotaio = localitaNotaio;
	}
	public String getNotaio() {
		return notaio;
	}
	public void setNotaio(String notaio) {
		this.notaio = notaio;
	}
	public String getNumAnniEsAmmt() {
		return numAnniEsAmmt;
	}
	public void setNumAnniEsAmmt(String numAnniEsAmmt) {
		this.numAnniEsAmmt = numAnniEsAmmt;
	}
	public String getNumAzioniCapitSociale() {
		return numAzioniCapitSociale;
	}
	public void setNumAzioniCapitSociale(String numAzioniCapitSociale) {
		this.numAzioniCapitSociale = numAzioniCapitSociale;
	}
	public String getNumMaxMembriCS() {
		return numMaxMembriCS;
	}
	public void setNumMaxMembriCS(String numMaxMembriCS) {
		this.numMaxMembriCS = numMaxMembriCS;
	}
	public String getNumMembriCSCarica() {
		return numMembriCSCarica;
	}
	public void setNumMembriCSCarica(String numMembriCSCarica) {
		this.numMembriCSCarica = numMembriCSCarica;
	}
	public String getNumMinMembriCS() {
		return numMinMembriCS;
	}
	public void setNumMinMembriCS(String numMinMembriCS) {
		this.numMinMembriCS = numMinMembriCS;
	}
	public String getNumRegAtto() {
		return numRegAtto;
	}
	public void setNumRegAtto(String numRegAtto) {
		this.numRegAtto = numRegAtto;
	}
	public String getNumRepertorio() {
		return numRepertorio;
	}
	public void setNumRepertorio(String numRepertorio) {
		this.numRepertorio = numRepertorio;
	}
	public String getNumSoci() {
		return numSoci;
	}
	public void setNumSoci(String numSoci) {
		this.numSoci = numSoci;
	}
	public String getNumSociCarica() {
		return numSociCarica;
	}
	public void setNumSociCarica(String numSociCarica) {
		this.numSociCarica = numSociCarica;
	}
	public String getScadenzaEsSucc() {
		return scadenzaEsSucc;
	}
	public void setScadenzaEsSucc(String scadenzaEsSucc) {
		this.scadenzaEsSucc = scadenzaEsSucc;
	}
	public String getSiglaProvNotaio() {
		return siglaProvNotaio;
	}
	public void setSiglaProvNotaio(String siglaProvNotaio) {
		this.siglaProvNotaio = siglaProvNotaio;
	}
	public String getSiglaProvUffRegistro() {
		return siglaProvUffRegistro;
	}
	public void setSiglaProvUffRegistro(String siglaProvUffRegistro) {
		this.siglaProvUffRegistro = siglaProvUffRegistro;
	}
	public String getTotFondoConsortE() {
		return totFondoConsortE;
	}
	public void setTotFondoConsortE(String totFondoConsortE) {
		this.totFondoConsortE = totFondoConsortE;
	}
	public String getTotFondoConsortile() {
		return totFondoConsortile;
	}
	public void setTotFondoConsortile(String totFondoConsortile) {
		this.totFondoConsortile = totFondoConsortile;
	}
	public String getTotQuoteCapitSoc() {
		return totQuoteCapitSoc;
	}
	public void setTotQuoteCapitSoc(String totQuoteCapitSoc) {
		this.totQuoteCapitSoc = totQuoteCapitSoc;
	}
	public String getTotQuoteCapitSocE() {
		return totQuoteCapitSocE;
	}
	public void setTotQuoteCapitSocE(String totQuoteCapitSocE) {
		this.totQuoteCapitSocE = totQuoteCapitSocE;
	}
	public String getUfficioRegistro() {
		return ufficioRegistro;
	}
	public void setUfficioRegistro(String ufficioRegistro) {
		this.ufficioRegistro = ufficioRegistro;
	}
	public String getValAzioniCapitSoc() {
		return valAzioniCapitSoc;
	}
	public void setValAzioniCapitSoc(String valAzioniCapitSoc) {
		this.valAzioniCapitSoc = valAzioniCapitSoc;
	}
	public String getValoreAzioniCapitSoc() {
		return valoreAzioniCapitSoc;
	}
	public void setValoreAzioniCapitSoc(String valoreAzioniCapitSoc) {
		this.valoreAzioniCapitSoc = valoreAzioniCapitSoc;
	}
	public String getValutaCapitSociale() {
		return valutaCapitSociale;
	}
	public void setValutaCapitSociale(String valutaCapitSociale) {
		this.valutaCapitSociale = valutaCapitSociale;
	}
	public String getValutaCapitale() {
		return valutaCapitale;
	}
	public void setValutaCapitale(String valutaCapitale) {
		this.valutaCapitale = valutaCapitale;
	}
}
