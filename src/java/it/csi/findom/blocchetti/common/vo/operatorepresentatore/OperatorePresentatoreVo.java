/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.operatorepresentatore;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.findom.blocchetti.common.vo.soggetto.SoggettoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class OperatorePresentatoreVo extends CommonalityVO {


	@MapTo(target = INHERIT, name = "denominazione")
	String denominazione;
	
	@MapTo(target = INHERIT, name = "codiceFiscale")
	String codiceFiscale;
	
	@MapTo(target = INHERIT, name = "descrizioneFormaGiuridica")
	String descrizioneFormaGiuridica;
	
	@MapTo(target = INHERIT, name = "codiceFormaGiuridica")
	String codiceFormaGiuridica;
	
	@MapTo(target = INHERIT, name = "partitaIva")
	String partitaIva;
	
	@MapTo(target = INHERIT, name = "indirizzoPec")
	String indirizzoPec;
	
	@MapTo(target = INHERIT, name = "idFormaGiuridica")
	String idFormaGiuridica;
	
	@MapTo(target = INHERIT, name = "idDipartimento")
	String idDipartimento;
	
	@MapTo(target = INHERIT, name = "codiceDipartimento")
	String codiceDipartimento;
	
	@MapTo(target = INHERIT, name = "descrizioneDipartimento")
	String descrizioneDipartimento;
	
	@MapTo(target = INHERIT, name = "soggetto")
	SoggettoVO soggetto;
	
	@MapTo(target = INHERIT, name = "codicePrevalenteAteco2007")
	String codicePrevalenteAteco2007;
	@MapTo(target = INHERIT, name = "descrizioneAteco2007")
	String descrizioneAteco2007;
	@MapTo(target = INHERIT, name = "idAteco2007")
	String idAteco2007;
	
	@MapTo(target = INHERIT, name = "idAttivitaEconomica")
	String idAttivitaEconomica;
	@MapTo(target = INHERIT, name = "codiceAttivitaEconomica")
	String codiceAttivitaEconomica;
	@MapTo(target = INHERIT, name = "descrizioneAttivitaEconomica")
	String descrizioneAttivitaEconomica;

	@MapTo(target = INHERIT, name = "codiceIpa")
	String codiceIpa;
	//gestione beneficiario estero inizio qqq
	@MapTo(target = INHERIT, name = "idStato")
	String idStato;	
	@MapTo(target = INHERIT, name = "descrStato")
	String descrStato;
	//gestione beneficiario estero fine qqq
	
	/** Jira: 1974 -2r */
	@MapTo(target = INHERIT, name = "codiceRegionale")
	String codiceRegionale;
	
	/** Jira: 2005 -2r */
	@MapTo(target = INHERIT, name = "numeroIscrizioneRegistroAPS")
	String numeroIscrizioneRegistroAPS;
	
	/** Jira: 2005 -2r */
	@MapTo(target = INHERIT, name = "dataIscrizioneRegistroAPS")
	String dataIscrizioneRegistroAPS;
	
	
	public String getDenominazione() {
		return denominazione;
	}
	
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public String getDescrizioneFormaGiuridica() {
		return descrizioneFormaGiuridica;
	}
	
	public void setDescrizioneFormaGiuridica(String descrizioneFormaGiuridica) {
		this.descrizioneFormaGiuridica = descrizioneFormaGiuridica;
	}
	
	public String getCodiceFormaGiuridica() {
		return codiceFormaGiuridica;
	}
	
	public void setCodiceFormaGiuridica(String codiceFormaGiuridica) {
		this.codiceFormaGiuridica = codiceFormaGiuridica;
	}
	
	public String getPartitaIva() {
		return partitaIva;
	}
	
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	
	public String getIndirizzoPec() {
		return indirizzoPec;
	}
	
	public void setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
	}
	
	public String getIdFormaGiuridica() {
		return idFormaGiuridica;
	}
	
	public void setIdFormaGiuridica(String idFormaGiuridica) {
		this.idFormaGiuridica = idFormaGiuridica;
	}
	
	public SoggettoVO getSoggetto() {
		return soggetto;
	}
	
	public void setSoggetto(SoggettoVO soggetto) {
		this.soggetto = soggetto;
	}
	
	public String getCodicePrevalenteAteco2007() {
		return codicePrevalenteAteco2007;
	}
	
	public void setCodicePrevalenteAteco2007(String codicePrevalenteAteco2007) {
		this.codicePrevalenteAteco2007 = codicePrevalenteAteco2007;
	}
	
	public String getDescrizioneAteco2007() {
		return descrizioneAteco2007;
	}
	
	public void setDescrizioneAteco2007(String descrizioneAteco2007) {
		this.descrizioneAteco2007 = descrizioneAteco2007;
	}
	
	public String getIdAteco2007() {
		return idAteco2007;
	}
	
	public void setIdAteco2007(String idAteco2007) {
		this.idAteco2007 = idAteco2007;
	}
	
	public String getIdAttivitaEconomica() {
		return idAttivitaEconomica;
	}
	
	public void setIdAttivitaEconomica(String idAttivitaEconomica) {
		this.idAttivitaEconomica = idAttivitaEconomica;
	}
	
	public String getCodiceAttivitaEconomica() {
		return codiceAttivitaEconomica;
	}
	
	public void setCodiceAttivitaEconomica(String codiceAttivitaEconomica) {
		this.codiceAttivitaEconomica = codiceAttivitaEconomica;
	}
	
	public String getDescrizioneAttivitaEconomica() {
		return descrizioneAttivitaEconomica;
	}
	
	public void setDescrizioneAttivitaEconomica(String descrizioneAttivitaEconomica) {
		this.descrizioneAttivitaEconomica = descrizioneAttivitaEconomica;
	}
	
	public String getIdDipartimento() {
		return idDipartimento;
	}
	
	public void setIdDipartimento(String idDipartimento) {
		this.idDipartimento = idDipartimento;
	}	
	
	public String getCodiceDipartimento() {
		return codiceDipartimento;
	}
	
	public void setCodiceDipartimento(String codiceDipartimento) {
		this.codiceDipartimento = codiceDipartimento;
	}
	
	public String getDescrizioneDipartimento() {
		return descrizioneDipartimento;
	}
	
	public void setDescrizioneDipartimento(String descrizioneDipartimento) {
		this.descrizioneDipartimento = descrizioneDipartimento;
	}
	
	public String getCodiceIpa() {
		return codiceIpa;
	}
	
	public void setCodiceIpa(String codiceIpa) {
		this.codiceIpa = codiceIpa;
	}
	
	//gestione beneficiario estero inizio qqq
	public String getIdStato() {
		return idStato;
	}
	
	public void setIdStato(String idStato) {
		this.idStato = idStato;
	}
	
	public String getDescrStato() {
		return descrStato;
	}
	
	public void setDescrStato(String descrStato) {
		this.descrStato = descrStato;
	}	
	
	//gestione beneficiario estero fine qqq
	
	
	/** Jira: 1974 -2r */
	public String getCodiceRegionale() {
		return codiceRegionale;
	}
	
	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
	}

	public String getNumeroIscrizioneRegistroAPS() {
		return numeroIscrizioneRegistroAPS;
	}

	public void setNumeroIscrizioneRegistroAPS(String numeroIscrizioneRegistroAPS) {
		this.numeroIscrizioneRegistroAPS = numeroIscrizioneRegistroAPS;
	}

	public String getDataIscrizioneRegistroAPS() {
		return dataIscrizioneRegistroAPS;
	}

	public void setDataIscrizioneRegistroAPS(String dataIscrizioneRegistroAPS) {
		this.dataIscrizioneRegistroAPS = dataIscrizioneRegistroAPS;
	}
	
}
