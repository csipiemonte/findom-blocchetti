/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianospese;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class VoceDiSpesaVO extends CommonalityVO {

    
	@MapTo(target = MapTarget.INHERIT)
	String idVoceSpesa;

	@MapTo(target = MapTarget.INHERIT)
	String codVoceSpesa;
	
	@MapTo(target = MapTarget.INHERIT)
	String descrVoceSpesa;

	public String getIdVoceSpesa() {
		return idVoceSpesa;
	}

	public String getCodVoceSpesa() {
		return codVoceSpesa;
	}

	public String getDescrVoceSpesa() {
		return descrVoceSpesa;
	}

	public void setIdVoceSpesa(String idVoceSpesa) {
		this.idVoceSpesa = idVoceSpesa;
	}

	public void setCodVoceSpesa(String codVoceSpesa) {
		this.codVoceSpesa = codVoceSpesa;
	}

	public void setDescrVoceSpesa(String descrVoceSpesa) {
		this.descrVoceSpesa = descrVoceSpesa;
	}

	
	
	
}
