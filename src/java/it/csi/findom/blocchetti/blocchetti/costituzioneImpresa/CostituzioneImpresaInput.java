/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.costituzioneImpresa;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class CostituzioneImpresaInput extends CommonalityInput {

	  @MapTo(target=MODEL,name="_costituzioneImpresa")
	  CostituzioneImpresaVo _costituzioneImpresa;
	  
	  @MapTo(target = CONF, name = "_costituzioneImpresa_costituzione_in_corso")
      String costituzioneImpresaCostituzioneInCorso;

	  @MapTo(target = CONF, name = "_costituzioneImpresa_iscrizione_in_corso")
      String costituzioneImpresaIscrizioneInCorso;
	  
	  @MapTo(target = CONF, name = "_costituzioneImpresa_data_solo_primo_salvataggio")
      String costituzioneImpresaDataSoloPrimoSalvataggio;
	  
	  /**  :: Jira: 1590 */
	  @MapTo(target = CONF, name = "_costituzioneImpresa_data_iscrizione_registro_imprese")
      String _costituzioneImpresa_data_iscrizione_registro_imprese;
	  
}
