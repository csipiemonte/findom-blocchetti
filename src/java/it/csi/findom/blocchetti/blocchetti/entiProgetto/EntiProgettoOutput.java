/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.entiProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.ente.EnteLocaleVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class EntiProgettoOutput extends CommonalityOutput {

	  @MapTo(target=NAMESPACE)
	  List<EnteLocaleVO> entiList;

	  @MapTo(target=NAMESPACE)
	  List<EnteProgettoItemVO> entiProgettoList;

	  @MapTo(target=NAMESPACE)
	  String totalePopolazione;
	  
	  @MapTo(target=NAMESPACE)
	  String totalePopolazioneGruppo;
	  
	  @MapTo(target=NAMESPACE)
	  String codiceTipologiaBeneficiario;

	public List<EnteLocaleVO> getEntiList() {
		return entiList;
	}

	public List<EnteProgettoItemVO> getEntiProgettoList() {
		return entiProgettoList;
	}

	public String getTotalePopolazione() {
		return totalePopolazione;
	}

	public String getTotalePopolazioneGruppo() {
		return totalePopolazioneGruppo;
	}

	public void setEntiList(List<EnteLocaleVO> entiList) {
		this.entiList = entiList;
	}

	public void setEntiProgettoList(List<EnteProgettoItemVO> entiProgettoList) {
		this.entiProgettoList = entiProgettoList;
	}

	public void setTotalePopolazione(String totalePopolazione) {
		this.totalePopolazione = totalePopolazione;
	}

	public void setTotalePopolazioneGruppo(String totalePopolazioneGruppo) {
		this.totalePopolazioneGruppo = totalePopolazioneGruppo;
	}

	public String getCodiceTipologiaBeneficiario() {
		return codiceTipologiaBeneficiario;
	}

	public void setCodiceTipologiaBeneficiario(String codiceTipologiaBeneficiario) {
		this.codiceTipologiaBeneficiario = codiceTipologiaBeneficiario;
	}
}
