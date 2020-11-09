/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.schedaProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SchedaProgettoInput extends CommonalityInput {

	@MapTo(target=MapTarget.MODEL,name="_criteri")
	SchedaProgettoVO schedaProgettoVO;
	
	@MapTo(target=MODEL,name="_caratteristicheProgetto")
	CaratteristicheProgettoNGVO caratteristicheProgettoNG;
	  
	@MapTo(target=MapTarget.CONF)
	String criteriToView;
	
	@MapTo(target=MapTarget.CONF,name="_schedaprogetto_dipendeda_caratteristicheprogetto")
	String schedaProgettoDipendeDaCaratteristicheProgetto;
}
