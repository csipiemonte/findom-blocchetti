/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.selezioneAteco;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.ateco.AtecoVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class SelezioneAtecoOutput extends CommonalityOutput {

  @MapTo(target = NAMESPACE, name = "atecoFoundList")
  List<AtecoVo> atecoFoundList;
	
  @MapTo(target = NAMESPACE)
  String viewAtecoResult;

  @MapTo(target = NAMESPACE)
  String codiceAteco;

  @MapTo(target = NAMESPACE)
  String descrizioneAteco;




}
