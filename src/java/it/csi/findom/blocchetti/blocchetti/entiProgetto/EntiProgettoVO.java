/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.entiProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EntiProgettoVO extends CommonalityVO {
	  
	private static final long serialVersionUID = 1L;

@MapTo(target = INHERIT, name = "entiProgettoList")
  EnteProgettoItemVO[] entiProgettoList;
  
  @MapTo(target = INHERIT, name = "totalePopolazione")
  String totalePopolazione;
  
  @MapTo(target = INHERIT, name = "totalePopolazioneGruppo")
  String totalePopolazioneGruppo;

public EnteProgettoItemVO[] getEntiProgettoList() {
	return entiProgettoList;
}

public void setEntiProgettoList(EnteProgettoItemVO[] entiProgettoList) {
	this.entiProgettoList = entiProgettoList;
}

public String getTotalePopolazione() {
	return totalePopolazione;
}

public String getTotalePopolazioneGruppo() {
	return totalePopolazioneGruppo;
}

public void setTotalePopolazione(String totalePopolazione) {
	this.totalePopolazione = totalePopolazione;
}

public void setTotalePopolazioneGruppo(String totalePopolazioneGruppo) {
	this.totalePopolazioneGruppo = totalePopolazioneGruppo;
}

 

}
