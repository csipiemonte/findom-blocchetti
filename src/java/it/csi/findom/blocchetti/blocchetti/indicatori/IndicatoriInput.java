/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class IndicatoriInput extends CommonalityInput {

	  @MapTo(target=MODEL,name="_indicatori")
	  IndicatoriVO _indicatori;
	  
	  @MapTo(target=CONF,name="_indicatori_tipo_ante_post")
	  String _indicatori_tipo_ante_post;
	  
	  /** Jira: 1797 */
	  @MapTo(target=CONF,name="_indicatori_flag_alfa")
	  String _indicatori_flag_alfa;
	  
	  /** asd covid19 */
	  @MapTo(target=CONF,name="_indicatori_reverse_descrizione_list")
	  String _indicatori_reverse_descrizione_list;
	  
	  /** asd covid19 */
	  @MapTo(target=CONF,name="_indicatori_controllo_valore_indicatore")
	  String _indicatori_controllo_valore_indicatore;
		
}
