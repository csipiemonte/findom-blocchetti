/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.allegati;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DocumentoVO extends CommonalityVO {

	
	
  @MapTo(target = INHERIT)
  String descrTipologia;

  @MapTo(target = INHERIT)
  String idFile;

  @MapTo(target = INHERIT)
  String nomeFile;

  @MapTo(target = INHERIT)
  String idTipologia;

  @MapTo(target = INHERIT)
  String differito;

  @MapTo(target = INHERIT)
  String presentato;

  public String getDescrTipologia() {
    return descrTipologia;
  }

  public void setDescrTipologia(String descrTipologia) {
    this.descrTipologia = descrTipologia;
  }

  public String getIdFile() {
    return idFile;
  }

  public void setIdFile(String idFile) {
    this.idFile = idFile;
  }

  public String getNomeFile() {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile) {
	System.out.println("****************************************************************");
	System.out.println("*** Nome file caricato da utente risulta: " + nomeFile + " ****");
	System.out.println("****************************************************************");
	this.nomeFile = nomeFile;
  }

  public String getIdTipologia() {
    return idTipologia;
  }

  public void setIdTipologia(String idTipologia) {
    this.idTipologia = idTipologia;
  }

  public String getDifferito() {
    return differito;
  }

  public void setDifferito(String differito) {
    this.differito = differito;
  }

  public String getPresentato() {
    return presentato;
  }

  public void setPresentato(String presentato) {
    this.presentato = presentato;
  }

  @Override
  public String toString() {
    return "DocumentoVO [descrTipologia=" + descrTipologia + ", idFile=" + idFile + ", nomeFile=" + nomeFile + ", idTipologia=" + idTipologia + ", differito=" + differito + ", presentato=" + presentato + "]";
  }

}
