/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DatiTipolBeneficiarioVo extends CommonalityVO {
			 
  @MapTo(target = INHERIT, name = "codstereotipo")
  String codstereotipo;

  @MapTo(target = INHERIT, name = "descrstereotipo")
  String descrstereotipo;
	  
  @MapTo(target = INHERIT, name = "flag")
  String flag;

  @MapTo(target = INHERIT, name = "codicetipologiabeneficiario")
  String codicetipologiabeneficiario;

  @MapTo(target = INHERIT, name = "descrizione")
  String descrizione;

  
  public String getCodstereotipo() {
	return codstereotipo;
  }
  
  
  public void setCodstereotipo(String codstereotipo) {
  	this.codstereotipo = codstereotipo;
  }
  
  
  public String getDescrstereotipo() {
  	return descrstereotipo;
  }
  
  
  public void setDescrstereotipo(String descrstereotipo) {
  	this.descrstereotipo = descrstereotipo;
  }
  
  
  public String getFlag() {
  	return flag;
  }
  
  
  public void setFlag(String flag) {
  	this.flag = flag;
  }
  
  
  public String getCodicetipologiabeneficiario() {
  	return codicetipologiabeneficiario;
  }
  
  
  public void setCodicetipologiabeneficiario(String codicetipologiabeneficiario) {
  	this.codicetipologiabeneficiario = codicetipologiabeneficiario;
  }
  
  
  public String getDescrizione() {
  	return descrizione;
  }
  
  
  public void setDescrizione(String descrizione) {
  	this.descrizione = descrizione;
  }


@Override
  public String toString() {
    return "PsoVo [codstereotipo=" + codstereotipo + ", descrstereotipo=" + descrstereotipo + ", flag=" + flag + ", codicetipologiabeneficiario=" + codicetipologiabeneficiario + ", descrizione=" + descrizione + "]";
  }

}
