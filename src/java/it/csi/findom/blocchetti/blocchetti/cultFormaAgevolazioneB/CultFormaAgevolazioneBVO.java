/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CultFormaAgevolazioneBVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=INHERIT)
	String idSoggetto;
	
	@MapTo(target=INHERIT)
	String idDipartimento;
	
	@MapTo(target=INHERIT)
	String codice;
	
	@MapTo(target=INHERIT)
	String descrizione;
	
	@MapTo(target=INHERIT)
	String importo;

	@MapTo(target=INHERIT)
	String importoRichiesto;
	
	@MapTo(target=INHERIT)
	String importoErogabile;		    
	
	@MapTo(target=INHERIT)
	String importoMinimoErogabile;
	
	@MapTo(target=INHERIT)
	String importoMassimoErogabile;
    
	@MapTo(target=INHERIT)
	String speseGeneraliFunz;

	@MapTo(target=INHERIT)
	String speseConnesseAttivita;
	
	@MapTo(target=INHERIT)
	String totaleEntrate;
	
	@MapTo(target=INHERIT)
	String percQuotaParteSpeseGenFunz; 
	
	@MapTo(target=INHERIT)
	String differenza;
	
	@MapTo(target=INHERIT)
	String campiNascosti;
	
	@MapTo(target=INHERIT)
	String campoNascostoTotaleEntrate;
	
	/** jira 2009 - */
	@MapTo(target=INHERIT)
	String saldoContabilePrevisto;
	
	/**
	 * Get | Set
	 * @return
	 */
	public String getImportoRichiesto() {
		return importoRichiesto;
	}

	public String getImportoErogabile() {
		return importoErogabile;
	}

	public String getImportoMinimoErogabile() {
		return importoMinimoErogabile;
	}

	public void setImportoRichiesto(String importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public void setImportoErogabile(String importoErogabile) {
		this.importoErogabile = importoErogabile;
	}

	public void setImportoMinimoErogabile(String importoMinimoErogabile) {
		this.importoMinimoErogabile = importoMinimoErogabile;
	}

	public String getSpeseGeneraliFunz() {
		return speseGeneraliFunz;
	}

	public String getSpeseConnesseAttivita() {
		return speseConnesseAttivita;
	}

	public void setSpeseGeneraliFunz(String speseGeneraliFunz) {
		this.speseGeneraliFunz = speseGeneraliFunz;
	}

	public void setSpeseConnesseAttivita(String speseConnesseAttivita) {
		this.speseConnesseAttivita = speseConnesseAttivita;
	}

	public String getTotaleEntrate() {
		return totaleEntrate;
	}

	public void setTotaleEntrate(String totaleEntrate) {
		this.totaleEntrate = totaleEntrate;
	}

	public String getPercQuotaParteSpeseGenFunz() {
		return percQuotaParteSpeseGenFunz;
	}

	public void setPercQuotaParteSpeseGenFunz(String percQuotaParteSpeseGenFunz) {
		this.percQuotaParteSpeseGenFunz = percQuotaParteSpeseGenFunz;
	}

	public String getImportoMassimoErogabile() {
		return importoMassimoErogabile;
	}

	public void setImportoMassimoErogabile(String importoMassimoErogabile) {
		this.importoMassimoErogabile = importoMassimoErogabile;
	}

	public String getDifferenza() {
		return differenza;
	}

	public void setDifferenza(String differenza) {
		this.differenza = differenza;
	}

	public String getCampiNascosti() {
		return campiNascosti;
	}

	public void setCampiNascosti(String campiNascosti) {
		this.campiNascosti = campiNascosti;
	}

	public String getCampoNascostoTotaleEntrate() {
		return campoNascostoTotaleEntrate;
	}

	public void setCampoNascostoTotaleEntrate(String campoNascostoTotaleEntrate) {
		this.campoNascostoTotaleEntrate = campoNascostoTotaleEntrate;
	}

	public String getSaldoContabilePrevisto() {
		return saldoContabilePrevisto;
	}

	public void setSaldoContabilePrevisto(String saldoContabilePrevisto) {
		this.saldoContabilePrevisto = saldoContabilePrevisto;
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

	public String getImporto() {
		return importo;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public String getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(String idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public String getIdDipartimento() {
		return idDipartimento;
	}

	public void setIdDipartimento(String idDipartimento) {
		this.idDipartimento = idDipartimento;
	}

}
