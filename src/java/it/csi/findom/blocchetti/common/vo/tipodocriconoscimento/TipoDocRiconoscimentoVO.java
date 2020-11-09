/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.tipodocriconoscimento;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipoDocRiconoscimentoVO extends CommonalityVO {
	  
  @MapTo(target = INHERIT, name = "codice")
  String codice;

  @MapTo(target = INHERIT, name = "descrizione")
  String descrizione;

  @MapTo(target = INHERIT, name = "idTipoDocRiconoscimento")
  String idTipoDocRiconoscimento;

  @MapTo(target = INHERIT, name = "numDocumentoRiconoscimento")
  String numDocumentoRiconoscimento;
  @MapTo(target = INHERIT, name = "docRilasciatoDa")
  String docRilasciatoDa;
  @MapTo(target = INHERIT, name = "dataRilascioDoc")
  String dataRilascioDoc;
  
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

  public String getIdTipoDocRiconoscimento() {
	return idTipoDocRiconoscimento;
}

public void setIdTipoDocRiconoscimento(String idTipoDocRiconoscimento) {
	this.idTipoDocRiconoscimento = idTipoDocRiconoscimento;
}

public String getNumDocumentoRiconoscimento() {
	return numDocumentoRiconoscimento;
}

public String getDocRilasciatoDa() {
	return docRilasciatoDa;
}

public String getDataRilascioDoc() {
	return dataRilascioDoc;
}

public void setNumDocumentoRiconoscimento(String numDocumentoRiconoscimento) {
	this.numDocumentoRiconoscimento = numDocumentoRiconoscimento;
}

public void setDocRilasciatoDa(String docRilasciatoDa) {
	this.docRilasciatoDa = docRilasciatoDa;
}

public void setDataRilascioDoc(String dataRilascioDoc) {
	this.dataRilascioDoc = dataRilascioDoc;
}

@Override
  public String toString() {
    return "TipoDocRiconoscimentoVO [codice=" + codice + ", descrizione=" + descrizione +  "]";
  }

}
