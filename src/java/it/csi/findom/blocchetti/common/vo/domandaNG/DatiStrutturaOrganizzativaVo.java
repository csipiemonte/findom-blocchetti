/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DatiStrutturaOrganizzativaVo extends CommonalityVO {
			
  @MapTo(target = INHERIT, name = "idsettore")
  Integer idsettore;

  @MapTo(target = INHERIT, name = "descrsettore")
  String descrsettore;

  @MapTo(target = INHERIT, name = "iddirezione")
  Integer iddirezione;

  @MapTo(target = INHERIT, name = "descrdirezione")
  String descrdirezione;
  
public Integer getIdsettore() {
	return idsettore;
}


public void setIdsettore(Integer idsettore) {
	this.idsettore = idsettore;
}


public String getDescrsettore() {
	return descrsettore;
}


public void setDescrsettore(String descrsettore) {
	this.descrsettore = descrsettore;
}


public Integer getIddirezione() {
	return iddirezione;
}


public void setIddirezione(Integer iddirezione) {
	this.iddirezione = iddirezione;
}


public String getDescrdirezione() {
	return descrdirezione;
}


public void setDescrdirezione(String descrdirezione) {
	this.descrdirezione = descrdirezione;
}


@Override
  public String toString() {
    return "DatiStrutturaOrganizzativaVo [idsettore=" + idsettore + ", descrsettore=" + descrsettore + ", iddirezione=" + iddirezione + ", descrdirezione=" + descrdirezione + "]";
  }

}
