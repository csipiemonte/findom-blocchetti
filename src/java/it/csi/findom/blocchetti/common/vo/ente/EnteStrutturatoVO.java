/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.ente;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EnteStrutturatoVO extends CommonalityVO {


  @MapTo(target = INHERIT, name = "id")
  String id;
	  
  @MapTo(target = INHERIT, name = "codice")
  String codice;

  @MapTo(target = INHERIT, name = "descrizione")
  String descrizione;
  

  public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
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

  @Override
  public String toString() {
    return "EnteStrutturatoVO [id=" + id + ", codice=" + codice + ", descrizione=" + descrizione +  "]";
  }

}
