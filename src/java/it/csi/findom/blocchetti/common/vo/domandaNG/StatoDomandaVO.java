/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class StatoDomandaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	String idDomanda;
	
	@MapTo(target = INHERIT)
	String idStatoDomanda;	
	
	@MapTo(target = INHERIT)
	String descrStatoDomanda;

	public String getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getIdStatoDomanda() {
		return idStatoDomanda;
	}

	public void setIdStatoDomanda(String idStatoDomanda) {
		this.idStatoDomanda = idStatoDomanda;
	}

	public String getDescrStatoDomanda() {
		return descrStatoDomanda;
	}

	public void setDescrStatoDomanda(String descrStatoDomanda) {
		this.descrStatoDomanda = descrStatoDomanda;
	}
	
	
	
	
}
