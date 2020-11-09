/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.gestioneDocumentiNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class GestioneDocumentiInput extends CommonalityInput {

	  @MapTo(target = MapTarget.REQUEST, name = "deletedoc")
      String deletedoc;
	  
	  @MapTo(target = MapTarget.REQUEST, name = "viewedoc")
      String viewedoc;
	  
	  @MapTo(target=MODEL,name="_allegati")
	  AllegatiVO allegati;	  
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformId")
      String xformId; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformProg")
	  Integer xformProg; 
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xformState")
      String xformState;
	  
	  @MapTo(target = MapTarget.CONTEXT, name = "xcommId")
      String xcommId;
	  
	  @MapTo(target = CONF, name = "msgDocumentiOpzionaliPerBando")
	  String msgDocumentiOpzionaliPerBando;

		@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
		CaratteristicheProgettoNGVO _caratteristicheProgetto;
}
