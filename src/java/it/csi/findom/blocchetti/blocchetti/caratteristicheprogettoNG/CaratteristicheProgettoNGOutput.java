/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNG;

import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class CaratteristicheProgettoNGOutput extends CommonalityOutput {

	@MapTo(target=MapTarget.NAMESPACE)
	List<TipologiaInterventoVO> tipologiaInterventoList;
	
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
	 * tente ha gia compilato il Tab degli indicatori, 
	 * torna su Informazioni sul progetto e
	 * deseleziona la tipologia di intervento "CAT C..." e salva,
	 * i dati contenuti nel Tab degli indicatori devono essere cancellati su XML
	 * */ 
	@MapTo(target=MapTarget.NAMESPACE)
	String isSelectedIdTipoIntervento99;
	
	/** bonus covid 19 */
	@MapTo(target=MapTarget.NAMESPACE)
	String importoContributo;

	public String getIsSelectedIdTipoIntervento99() {
		return isSelectedIdTipoIntervento99;
	}

	public void setIsSelectedIdTipoIntervento99(String isSelectedIdTipoIntervento99) {
		this.isSelectedIdTipoIntervento99 = isSelectedIdTipoIntervento99;
	}

}
