/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1.P5;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;

public class PageInput extends SectionPageInput {
  
  @MapTo(target=MODEL,name="_operatorePresentatore")
  OperatorePresentatoreVo operatorePresentatore;
}
