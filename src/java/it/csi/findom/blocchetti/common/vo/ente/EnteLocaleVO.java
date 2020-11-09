/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.ente;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EnteLocaleVO extends CommonalityVO {
	  
  @MapTo(target = INHERIT, name = "codice")
  String codice;

  @MapTo(target = INHERIT, name = "descrizione")
  String descrizione;
 
  @MapTo(target = INHERIT, name = "popolazione")
  String popolazione;
 
  @MapTo(target = INHERIT, name = "classificazione")
  String classificazione;
 
  @MapTo(target = INHERIT, name = "tipoente")
  String tipoente;

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

  public String getPopolazione() {
	return popolazione;
}

public String getClassificazione() {
	return classificazione;
}

public String getTipoente() {
	return tipoente;
}

public void setPopolazione(String popolazione) {
	this.popolazione = popolazione;
}

public void setClassificazione(String classificazione) {
	this.classificazione = classificazione;
}

public void setTipoente(String tipoente) {
	this.tipoente = tipoente;
}

@Override
  public String toString() {
    return "EnteStrutturatoVO [codice=" + codice + ", descrizione=" + descrizione +  "]";
  }

}
