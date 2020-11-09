/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.entiProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EnteProgettoItemVO extends CommonalityVO {
	  
  @MapTo(target = INHERIT, name = "codiceEnte")
  String codiceEnte;

  @MapTo(target = INHERIT, name = "descrizioneEnte")
  String descrizioneEnte;
 
  @MapTo(target = INHERIT, name = "popolazione")
  String popolazione;
 
  @MapTo(target = INHERIT, name = "classificazione")
  String classificazione;
 
  @MapTo(target = INHERIT, name = "tipoEnte")
  String tipoEnte;

  @MapTo(target = INHERIT, name = "partecipazioneProgetto")
  String partecipazioneProgetto;

  @MapTo(target = INHERIT, name = "certificazioneAmbientale")
  String certificazioneAmbientale;

  @MapTo(target = INHERIT, name = "certificazioneAmbientaleText")
  String certificazioneAmbientaleText;
  //MB2018_11_14 ini nuovi campi 
  @MapTo(target = INHERIT, name = "pianoComunale")
  String pianoComunale;
  
  @MapTo(target = INHERIT, name = "paes")
  String paes;
  //MB2018_11_14 fine nuovi campi 
  
  @MapTo(target = INHERIT, name = "pattoSindaci")
  String pattoSindaci;

  @MapTo(target = INHERIT, name = "richiestaAgevolazioni")
  String richiestaAgevolazioni;
  
  @MapTo(target = INHERIT, name = "richiestaAgevolazioniText")
  String richiestaAgevolazioniText;
  
  @MapTo(target = INHERIT, name = "daCancellare")
  String daCancellare;
  
  public String getDescrizioneEnte() {
    return descrizioneEnte;
  }

  public void setDescrizioneEnte(String descrizioneEnte) {
    this.descrizioneEnte = descrizioneEnte;
  }

  public String getPopolazione() {
	return popolazione;
}

public String getClassificazione() {
	return classificazione;
}

public void setPopolazione(String popolazione) {
	this.popolazione = popolazione;
}

public void setClassificazione(String classificazione) {
	this.classificazione = classificazione;
}


public String getCodiceEnte() {
	return codiceEnte;
}

public String getTipoEnte() {
	return tipoEnte;
}

public void setCodiceEnte(String codiceEnte) {
	this.codiceEnte = codiceEnte;
}

public void setTipoEnte(String tipoEnte) {
	this.tipoEnte = tipoEnte;
}

public String getPartecipazioneProgetto() {
	return partecipazioneProgetto;
}

public void setPartecipazioneProgetto(String partecipazioneProgetto) {
	this.partecipazioneProgetto = partecipazioneProgetto;
}

public String getDaCancellare() {
	return daCancellare;
}

public void setDaCancellare(String daCancellare) {
	this.daCancellare = daCancellare;
}

public String getCertificazioneAmbientale() {
	return certificazioneAmbientale;
}

public String getCertificazioneAmbientaleText() {
	return certificazioneAmbientaleText;
}

public String getPattoSindaci() {
	return pattoSindaci;
}

public String getRichiestaAgevolazioni() {
	return richiestaAgevolazioni;
}

public void setCertificazioneAmbientale(String certificazioneAmbientale) {
	this.certificazioneAmbientale = certificazioneAmbientale;
}

public void setCertificazioneAmbientaleText(String certificazioneAmbientaleText) {
	this.certificazioneAmbientaleText = certificazioneAmbientaleText;
}

public void setPattoSindaci(String pattoSindaci) {
	this.pattoSindaci = pattoSindaci;
}

public void setRichiestaAgevolazioni(String richiestaAgevolazioni) {
	this.richiestaAgevolazioni = richiestaAgevolazioni;
}

public String getRichiestaAgevolazioniText() {
	return richiestaAgevolazioniText;
}

public void setRichiestaAgevolazioniText(String richiestaAgevolazioniText) {
	this.richiestaAgevolazioniText = richiestaAgevolazioniText;
}
//MB2018_11_14 ini
public String getPianoComunale() {
	return pianoComunale;
}

public String getPaes() {
	return paes;
}

public void setPianoComunale(String pianoComunale) {
	this.pianoComunale = pianoComunale;
}

public void setPaes(String paes) {
	this.paes = paes;
}
//MB2018_11_14 fine

}
