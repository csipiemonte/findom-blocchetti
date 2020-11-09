/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.legalerappresentante;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class LegaleRappresentanteDocumentoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	String dataRilascioDoc; 
	
	@MapTo(target = INHERIT)
	String descrizioneTipoDocRiconoscimento;
	
	@MapTo(target = INHERIT)
	String docRilasciatoDa; 
	
	@MapTo(target = INHERIT)
	String idTipoDocRiconoscimento;
	
	@MapTo(target = INHERIT)
	String numDocumentoRiconoscimento;

	public String getDataRilascioDoc() {
		return dataRilascioDoc;
	}

	public void setDataRilascioDoc(String dataRilascioDoc) {
		this.dataRilascioDoc = dataRilascioDoc;
	}

	public String getDescrizioneTipoDocRiconoscimento() {
		return descrizioneTipoDocRiconoscimento;
	}

	public void setDescrizioneTipoDocRiconoscimento(String descrizioneTipoDocRiconoscimento) {
		this.descrizioneTipoDocRiconoscimento = descrizioneTipoDocRiconoscimento;
	}

	public String getDocRilasciatoDa() {
		return docRilasciatoDa;
	}

	public void setDocRilasciatoDa(String docRilasciatoDa) {
		this.docRilasciatoDa = docRilasciatoDa;
	}

	public String getIdTipoDocRiconoscimento() {
		return idTipoDocRiconoscimento;
	}

	public void setIdTipoDocRiconoscimento(String idTipoDocRiconoscimento) {
		this.idTipoDocRiconoscimento = idTipoDocRiconoscimento;
	}

	public String getNumDocumentoRiconoscimento() {
		return numDocumentoRiconoscimento;
	}

	public void setNumDocumentoRiconoscimento(String numDocumentoRiconoscimento) {
		this.numDocumentoRiconoscimento = numDocumentoRiconoscimento;
	} 

	
}
