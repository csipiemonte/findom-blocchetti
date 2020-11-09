/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.formaGiuridica;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class FormaGiuridicaVO extends CommonalityVO {


  @MapTo(target = INHERIT, name = "id")
  Long id;
	  
  @MapTo(target = INHERIT, name = "codice")
  String codice;

  @MapTo(target = INHERIT, name = "descrizione")
  String descrizione;
  
  @MapTo(target = INHERIT, name = "descrtroncata")
  String descrtroncata;
    

  public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getDescrtroncata() {
	return descrtroncata;
}

public void setDescrtroncata(String descrtroncata) {
	this.descrtroncata = descrtroncata;
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
    return "FormaGiuridicaVO [id=" + id + ", codice=" + codice + ", descrizione=" + descrizione + ", descrtroncata=" + descrtroncata + "]";
  }

}
