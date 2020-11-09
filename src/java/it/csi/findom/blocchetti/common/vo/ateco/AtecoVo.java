/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.ateco;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AtecoVo extends CommonalityVO {

  @MapTo(target = INHERIT, name = "id")
  Long id;

  @MapTo(target = INHERIT, name = "codice")
  String codice;

  @MapTo(target = INHERIT, name = "descrizione")
  String descrizione;

  @MapTo(target = INHERIT, name = "codnormalizzato")
  String codnormalizzato;

  @MapTo(target = INHERIT, name = "codicesezione")
  String codicesezione;

  @MapTo(target = INHERIT, name = "descrizionesezione")
  String descrizionesezione;

  @MapTo(target = INHERIT, name = "livello")
  String livello;

public Long getId() {
	return id;
}

public void setId(Long id) {
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

public String getCodnormalizzato() {
	return codnormalizzato;
}

public void setCodnormalizzato(String codnormalizzato) {
	this.codnormalizzato = codnormalizzato;
}

public String getCodicesezione() {
	return codicesezione;
}

public void setCodicesezione(String codicesezione) {
	this.codicesezione = codicesezione;
}

public String getDescrizionesezione() {
	return descrizionesezione;
}

public void setDescrizionesezione(String descrizionesezione) {
	this.descrizionesezione = descrizionesezione;
}

public String getLivello() {
	return livello;
}

public void setLivello(String livello) {
	this.livello = livello;
}
  
}
