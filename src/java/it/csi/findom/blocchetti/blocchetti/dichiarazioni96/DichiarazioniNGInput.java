/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioni96;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoVO;
import it.csi.findom.blocchetti.common.vo.sede.SediVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class DichiarazioniNGInput extends CommonalityInput {

	  @MapTo(target = MODEL, name = "_dichiarazioni")
      DichiarazioniNGVO dichiarazioni;
	  
	  @MapTo(target = MODEL, name = "_abstractProgetto")
      AbstractProgettoVO abstractProgetto;	  
	  
	  @MapTo(target = MODEL, name = "_sedi")
      SediVO sedi;

	  @MapTo(target = CONF, name = "_dichiarazioni_view_sezione_nota_integrativa_bilancio")
      String _dichiarazioni_view_sezione_nota_integrativa_bilancio;
	 
	  @MapTo(target = MapTarget.CONTEXT, name = "xformId")
      String xformId; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformProg")
	  Integer xformProg; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformState")
      String xformState; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xcommId")
      String xcommId; 
	  
}
