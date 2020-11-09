/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S3;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.findom.blocchetti.blocchetti.dichiarazioni96.DichiarazioniNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;

public class SectionInput extends SectionPageInput {
  
  @MapTo(target=MODEL,name="_dichiarazioni")
  DichiarazioniNGVO dichiarazioni; 
  
  @MapTo(target=MODEL,name="_allegati")
  AllegatiVO allegatiVO;
}
