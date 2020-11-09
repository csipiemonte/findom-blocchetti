/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.soggettoDelegato;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SoggettoDelegatoInput extends CommonalityInput {

  @MapTo(target=MODEL, name="_soggettoDelegato")
  SoggettoDelegatoVO _soggettoDelegato;

  @MapTo(target=REQUEST, name="_soggettoDelegato.provinciaNascita")
  String soggettoDelegatoProvinciaNascitaRequest;
  
  @MapTo(target=REQUEST, name="_soggettoDelegato.provinciaResidenza")
  String soggettoDelegatoProvinciaResidenzaRequest;

  @MapTo(target=CONF, name="_soggettoDelegato_genere")
  String _soggettoDelegato_genere;
 
  @MapTo(target=CONF, name="_soggettoDelegato_presenza_residenza")
  String _soggettoDelegato_presenza_residenza;  
  
}
