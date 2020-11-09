/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.ruoloProgettiComuni;

import it.csi.findom.blocchetti.common.vo.partner.PartnerEliminatoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class RuoloProgettiComuniOutput extends CommonalityOutput {
	@MapTo(target=MapTarget.NAMESPACE)
	String soloLettura;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String visNotaEliminazioneP;  //in caso di utente con ruolo partner, vale true se si deve visualizzare la nota di eliminazione di questo partner
	
	@MapTo(target=MapTarget.NAMESPACE)
	String notaEliminazioneP;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String visNotaEliminazioneC; //in caso di utente con ruolo capofila, vale true se si deve visualizzare la tabelle con le eventuali note di eliminazione di tutti i partner eliminati

	@MapTo(target=MapTarget.NAMESPACE)
	List<PartnerEliminatoVO> peVOList;
	
	public String getSoloLettura() {
		return soloLettura;
	}

	public void setSoloLettura(String soloLettura) {
		this.soloLettura = soloLettura;
	}

}
