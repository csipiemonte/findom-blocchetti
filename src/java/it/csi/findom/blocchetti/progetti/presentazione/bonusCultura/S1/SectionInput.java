/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.findom.blocchetti.blocchetti.soggettoDelegato.SoggettoDelegatoVO;
import it.csi.findom.blocchetti.blocchetti.estremiBancari.EstremiBancariVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;
import it.csi.findom.blocchetti.blocchetti.riferimenti.RiferimentiVO;
import it.csi.findom.blocchetti.blocchetti.sedeLegale.SedeLegaleVO;

public class SectionInput extends SectionPageInput {


  @MapTo(target=MODEL,name="_operatorePresentatore")
  OperatorePresentatoreVo operatorePresentatore;
  
  @MapTo(target=MODEL,name="_riferimenti")
  RiferimentiVO riferimenti;

  @MapTo(target=MODEL,name="_sedeLegale")
  SedeLegaleVO sedeLegale;
  
  @MapTo(target=MODEL,name="_estremiBancari")
  EstremiBancariVO estremiBancari;
  
  @MapTo(target=MODEL,name="_soggettoDelegato")
  SoggettoDelegatoVO soggettoDelegato;
  
  @MapTo(target=MODEL,name="_legaleRappresentante")
  LegaleRappresentanteVO legaleRappresentante;
  
}
