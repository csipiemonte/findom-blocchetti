/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.abstractprogetto;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AbstractProgettoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	private String titolo;
	
	@MapTo(target=MapTarget.INHERIT)
	private String sintesi;
	
	@MapTo(target=MapTarget.INHERIT)
	private String relAssunzioniProgetto;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza;

	@MapTo(target=MapTarget.INHERIT)
	private String interpolo;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloContributore;
	
	@MapTo(target=MapTarget.INHERIT)
	private String durataPrevista;
	
	@MapTo(target=MapTarget.INHERIT)
	private String acronimo;
	
	@MapTo(target=MapTarget.INHERIT)
	private String codiceCUP;
	
	@MapTo(target=MapTarget.INHERIT)
	private String ruolo;
	
	@MapTo(target=MapTarget.INHERIT)
	private String associato;
	
	/** Jira: 1883 */
	@MapTo(target=MapTarget.INHERIT)
	private String mqCoperture;
	
	/** Data inizio progetto */
	@MapTo(target=MapTarget.INHERIT)
	private String dataInizioProgetto;
	
	@MapTo(target=MapTarget.INHERIT)
	private String dataFineProgetto;
	
	/** 2R:: CR-1 Voucher */
	@MapTo(target=MapTarget.INHERIT)
	private String tipoBeneficiario;
	
	/** 2R:: Jira: 1561-1579 30082019 */
	@MapTo(target=MapTarget.INHERIT)
	private String collaborazione;
	
	/** 2R:: Jira 1335 20022019 */
	@MapTo(target=MapTarget.INHERIT)
	private String corealizzazione;
	
	/** 2R:: Jira: 1589 del 05072019 */
	@MapTo(target=MapTarget.INHERIT)
	private String importo_complessivo_business_plan;
	

	/** Get | Set */
	public String getCorrelazione() {
		return corealizzazione;
	}

	public void setCorrelazione(String corealizzazione) {
		this.corealizzazione = corealizzazione;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
	
	public String getPoloAppartenenza() {
		return poloAppartenenza;
	}

	public void setPoloAppartenenza(String poloAppartenenza) {
		this.poloAppartenenza = poloAppartenenza;
	}

	public String getInterpolo() {
		return interpolo;
	}

	public void setInterpolo(String interpolo) {
		this.interpolo = interpolo;
	}

	public String getPoloContributore() {
		return poloContributore;
	}

	public void setPoloContributore(String poloContributore) {
		this.poloContributore = poloContributore;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSintesi() {
		return sintesi;
	}

	public void setSintesi(String sintesi) {
		this.sintesi = sintesi;
	}

	public String getRelAssunzioniProgetto() {
		return relAssunzioniProgetto;
	}

	public void setRelAssunzioniProgetto(String relAssunzioniProgetto) {
		this.relAssunzioniProgetto = relAssunzioniProgetto;
	}

	public String getDurataPrevista() {
		return durataPrevista;
	}

	public void setDurataPrevista(String durataPrevista) {
		this.durataPrevista = durataPrevista;
	}

	public String getCodiceCUP() {
		return codiceCUP;
	}

	public void setCodiceCUP(String codiceCUP) {
		this.codiceCUP = codiceCUP;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getAssociato() {
		return associato;
	}

	public void setAssociato(String associato) {
		this.associato = associato;
	}

	/**
	 * Get | Set
	 * - data inizio progetto
	 * - data fine progetto
	 * 
	 * @return
	 */
	public String getDataInizioProgetto() {
		return dataInizioProgetto;
	}

	public void setDataInizioProgetto(String dataInizioProgetto) {
		this.dataInizioProgetto = dataInizioProgetto;
	}

	public String getDataFineProgetto() {
		return dataFineProgetto;
	}

	public void setDataFineProgetto(String dataFineProgetto) {
		this.dataFineProgetto = dataFineProgetto;
	}

	public String getCollaborazione() {
		System.out.println(collaborazione);
		return collaborazione;
	}

	public void setCollaborazione(String collaborazione) {
		System.out.println(collaborazione);
		this.collaborazione = collaborazione;
	}

	public String getImporto_complessivo_business_plan() {
		return importo_complessivo_business_plan;
	}

	public void setImporto_complessivo_business_plan(String importo_complessivo_business_plan) {
		this.importo_complessivo_business_plan = importo_complessivo_business_plan;
	}
	
	public String getCorealizzazione() {
		return corealizzazione;
	}

	public void setCorealizzazione(String corealizzazione) {
		this.corealizzazione = corealizzazione;
	}

	public String getMqCoperture() {
		return mqCoperture;
	}

	public void setMqCoperture(String mqCoperture) {
		this.mqCoperture = mqCoperture;
	}
}
