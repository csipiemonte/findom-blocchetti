/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.ruoloProgettiComuni;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoCPVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RuoloProgettiComuniInput extends CommonalityInput {

	@MapTo(target=MapTarget.MODEL,name="_ruoloProgettiComuni")
	RuoloProgettiComuniVO ruoloProgettiComuniVO;

	@MapTo(target=MODEL,name="_operatorePresentatore")
	OperatorePresentatoreVo operatorePresentatoreVO;
	
	@MapTo(target=MapTarget.MODEL ,name="_abstractProgetto")
	AbstractProgettoCPVO abstractProgettoCPVO;

}
