/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class CaratteristicheProgettoNeveNGOutput extends CommonalityOutput {

	@MapTo(target=MapTarget.NAMESPACE)
	List<TipologiaInterventoNeveVO> tipologiaInterventoList;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String esistonoDettagli;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String viewWarningSpese;

	@MapTo(target=MapTarget.NAMESPACE)
	String caratteristicheProgettoLabel;
	
	// RR2018_05_30 CR-1 Voucher
	@MapTo(target=MapTarget.NAMESPACE)
	String tipoBeneficiario;
	
	/** Jira: 1410 
	 * tente ha già compilato il Tab degli indicatori, 
	 * torna su Informazioni sul progetto e
	 * deseleziona la tipologia di intervento "CAT C..." e salva,
	 * i dati contenuti nel Tab degli indicatori devono essere cancellati su XML
	 * */ 
	@MapTo(target=MapTarget.NAMESPACE)
	String isSelectedIdTipoIntervento99;
	
	@MapTo(target=MapTarget.NAMESPACE)
	boolean isGrandiStazioni;

	public String getIsSelectedIdTipoIntervento99() {
		return isSelectedIdTipoIntervento99;
	}

	public void setIsSelectedIdTipoIntervento99(String isSelectedIdTipoIntervento99) {
		this.isSelectedIdTipoIntervento99 = isSelectedIdTipoIntervento99;
	}

	public List<TipologiaInterventoNeveVO> getTipologiaInterventoList() {
		return tipologiaInterventoList;
	}

	public void setTipologiaInterventoList(
			List<TipologiaInterventoNeveVO> tipologiaInterventoList) {
		this.tipologiaInterventoList = tipologiaInterventoList;
	}

	public String getEsistonoDettagli() {
		return esistonoDettagli;
	}

	public void setEsistonoDettagli(String esistonoDettagli) {
		this.esistonoDettagli = esistonoDettagli;
	}

	public String getViewWarningSpese() {
		return viewWarningSpese;
	}

	public void setViewWarningSpese(String viewWarningSpese) {
		this.viewWarningSpese = viewWarningSpese;
	}

	public String getCaratteristicheProgettoLabel() {
		return caratteristicheProgettoLabel;
	}

	public void setCaratteristicheProgettoLabel(String caratteristicheProgettoLabel) {
		this.caratteristicheProgettoLabel = caratteristicheProgettoLabel;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public boolean isGrandiStazioni() {
		return isGrandiStazioni;
	}

	public void setGrandiStazioni(boolean isGrandiStazioni) {
		this.isGrandiStazioni = isGrandiStazioni;
	}
}
