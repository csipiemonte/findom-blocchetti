/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.documentazione;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DocumentazioneVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	DocumentazioneItemVO[] documentoList;
	
	@MapTo(target=MapTarget.INHERIT)
	String documentoSelectedToDel;


	public String getDocumentoSelectedToDel() {
		return documentoSelectedToDel;
	}

	public void setDocumentoSelectedToDel(String documentoSelectedToDel) {
		this.documentoSelectedToDel = documentoSelectedToDel;
	}

	public DocumentazioneItemVO[] getDocumentoList() {
		return documentoList;
	}

	public void setDocumentoList(DocumentazioneItemVO[] documentoList) {
		this.documentoList = documentoList;
	}

	
}
