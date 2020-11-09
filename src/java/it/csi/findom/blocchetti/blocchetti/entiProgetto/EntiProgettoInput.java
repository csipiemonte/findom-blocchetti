/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.entiProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class EntiProgettoInput extends CommonalityInput {

	  @MapTo(target=REQUEST,name="codIstatEnteSelezionato")
	  String codIstatEnteSelezionato;

	  @MapTo(target=MODEL,name="_entiProgetto")
	  EntiProgettoVO _entiProgetto;

	  @MapTo(target=MODEL,name="_domanda")
	  DomandaNGVO _domanda;
	  
	  @MapTo(target=CONF,name="_entiProgetto_parametro_caricamento_comuni")
	  String _entiProgetto_parametro_caricamento_comuni;
}
