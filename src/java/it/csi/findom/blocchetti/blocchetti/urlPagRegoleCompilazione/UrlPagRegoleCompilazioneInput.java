/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.urlPagRegoleCompilazione;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class UrlPagRegoleCompilazioneInput extends CommonalityInput {
	  
	
	  @MapTo(target = MapTarget.CONTEXT, name = "urlpath")
      String urlpath; 
	
	  @MapTo(target = MapTarget.CONTEXT, name = "xformId")
      String xformId; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformProg")
	  Integer xformProg; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformState")
      String xformState; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xcommId")
      String xcommId; 
}
