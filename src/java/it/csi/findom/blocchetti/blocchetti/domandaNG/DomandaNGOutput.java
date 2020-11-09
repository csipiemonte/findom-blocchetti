/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class DomandaNGOutput extends CommonalityOutput {

  @MapTo(target=NAMESPACE)
  String codTipologiaUtente;
  
  @MapTo(target=NAMESPACE)
  String stereotipoDomanda;
 
  @MapTo(target=NAMESPACE)
  String descrTipologiaUtente;
  
  @MapTo(target=NAMESPACE)
  String descrStereotipoDomanda;
  
  @MapTo(target=NAMESPACE)
  String codiceTipologiaBeneficiario;
  
  @MapTo(target=NAMESPACE)
  String idSettore;
	
  @MapTo(target=NAMESPACE)
  String descrSettore;
  
  @MapTo(target=NAMESPACE)
  String idDirezione;

  @MapTo(target=NAMESPACE)
  String descrDirezione;

  @MapTo(target=NAMESPACE)
  Integer flagPubblicoPrivato;
  
  @MapTo(target=NAMESPACE)
  String normativa;
  
  @MapTo(target=NAMESPACE)
  String numAtto;
  
  @MapTo(target=NAMESPACE)
  String dataAtto;
  
  @MapTo(target=NAMESPACE)
  String dataAperturaSportello;
  
  @MapTo(target=NAMESPACE)
  String oraAperturaSportello;
  
  @MapTo(target=NAMESPACE)
  String dataChiusuraSportello;
  
  @MapTo(target=NAMESPACE)
  String oraChiusuraSportello;
  
  @MapTo(target=NAMESPACE)
  String annoCorrente;
  
  @MapTo(target=NAMESPACE)
  String flagProgettiComuni;  
  
  @MapTo(target=NAMESPACE)
  String denominazioneSoggettoBeneficiario;
  
  @MapTo(target=NAMESPACE)
  String siglaNazioneSoggettoBeneficiario;
    
  @MapTo(target=NAMESPACE)
  String codiceUnitaOrganizzativaSoggettoBeneficiario;
					
//  
//  @Override
//  public String toString() {
//    return "DomandaNamespace [gruppoOperatore=" + gruppoOperatore + ", codiceOperatore=" + codiceOperatore + ", psoList=" + psoList + ", imInAziendale=" + imInAziendale + "]";
//  }
//  
 
}
