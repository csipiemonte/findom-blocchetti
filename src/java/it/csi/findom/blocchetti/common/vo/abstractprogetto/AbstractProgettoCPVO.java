/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.abstractprogetto;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AbstractProgettoCPVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=MapTarget.INHERIT)
	private String idCapofilaAcronimo;
	
	@MapTo(target=MapTarget.INHERIT)
	private String idAcronimoBando;
	
	@MapTo(target=MapTarget.INHERIT)
	private String acronimoProgetto;

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
	private String codiceCUP;
	
	@MapTo(target=MapTarget.INHERIT)
	private String associato;
	
	@MapTo(target=MapTarget.INHERIT)
	private String dataInizioProgetto;
	
	@MapTo(target=MapTarget.INHERIT)
	private String dataFineProgetto;	
	
	@MapTo(target=MapTarget.INHERIT)
	private String tipoBeneficiario;

	public String getIdCapofilaAcronimo() {
		return idCapofilaAcronimo;
	}

	public void setIdCapofilaAcronimo(String idCapofilaAcronimo) {
		this.idCapofilaAcronimo = idCapofilaAcronimo;
	}

	public String getIdAcronimoBando() {
		return idAcronimoBando;
	}

	public void setIdAcronimoBando(String idAcronimoBando) {
		this.idAcronimoBando = idAcronimoBando;
	}

	public String getAcronimoProgetto() {
		return acronimoProgetto;
	}

	public void setAcronimoProgetto(String acronimoProgetto) {
		this.acronimoProgetto = acronimoProgetto;
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

	public String getAssociato() {
		return associato;
	}

	public void setAssociato(String associato) {
		this.associato = associato;
	}

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

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}	
	
}
