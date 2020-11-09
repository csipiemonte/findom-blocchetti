/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class BilancioAziendaAAEPVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "capitaleSociale")
	String capitaleSociale;
	@MapTo(target = INHERIT, name = "codiceFiscale")
	String codiceFiscale;
	@MapTo(target = INHERIT, name = "dataAggiornamento")
	String dataAggiornamento;
	@MapTo(target = INHERIT, name = "denominazione")
	String denominazione;
	@MapTo(target = INHERIT, name = "iscrizioneAlboCOOP")
	String iscrizioneAlboCOOP;
	@MapTo(target = INHERIT, name = "numIscrizioneCCIAA")
	String numIscrizioneCCIAA;
	@MapTo(target = INHERIT, name = "numIscrizioneRegistroImprese")
	String numIscrizioneRegistroImprese;
	@MapTo(target = INHERIT, name = "numREA")
	String numREA;
	@MapTo(target = INHERIT, name = "partitaIVA")
	String partitaIVA;
	
	@MapTo(target = INHERIT, name = "ubicazione")
	String ubicazione;
	@MapTo(target = INHERIT, name = "dettaglioBilancio")
	List<DettaglioBilancioAziendaAAEPVO> dettaglioBilancio;
	public String getCapitaleSociale() {
		return capitaleSociale;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public String getIscrizioneAlboCOOP() {
		return iscrizioneAlboCOOP;
	}
	public String getNumIscrizioneCCIAA() {
		return numIscrizioneCCIAA;
	}
	public String getNumIscrizioneRegistroImprese() {
		return numIscrizioneRegistroImprese;
	}
	public String getNumREA() {
		return numREA;
	}
	public String getPartitaIVA() {
		return partitaIVA;
	}
	public String getUbicazione() {
		return ubicazione;
	}
	public List<DettaglioBilancioAziendaAAEPVO> getDettaglioBilancio() {
		return dettaglioBilancio;
	}
	public void setCapitaleSociale(String capitaleSociale) {
		this.capitaleSociale = capitaleSociale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public void setIscrizioneAlboCOOP(String iscrizioneAlboCOOP) {
		this.iscrizioneAlboCOOP = iscrizioneAlboCOOP;
	}
	public void setNumIscrizioneCCIAA(String numIscrizioneCCIAA) {
		this.numIscrizioneCCIAA = numIscrizioneCCIAA;
	}
	public void setNumIscrizioneRegistroImprese(String numIscrizioneRegistroImprese) {
		this.numIscrizioneRegistroImprese = numIscrizioneRegistroImprese;
	}
	public void setNumREA(String numREA) {
		this.numREA = numREA;
	}
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}
	public void setDettaglioBilancio(
			List<DettaglioBilancioAziendaAAEPVO> dettaglioBilancio) {
		this.dettaglioBilancio = dettaglioBilancio;
	}
	
}
