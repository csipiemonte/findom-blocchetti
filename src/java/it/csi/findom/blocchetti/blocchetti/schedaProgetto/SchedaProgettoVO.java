/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.schedaProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class SchedaProgettoVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT)
	CriterioVO[] criteriList;
	
	@MapTo(target = INHERIT)
	Integer numeroCriteriTotali;
	
	public CriterioVO[] getCriteriList() {
		return criteriList;
	}

	public void setCriteriList(CriterioVO[] criteriList) {
		this.criteriList = criteriList;
	}

	public Integer getNumeroCriteriTotali() {
		return numeroCriteriTotali;
	}

	public void setNumeroCriteriTotali(Integer numeroCriteriTotali) {
		this.numeroCriteriTotali = numeroCriteriTotali;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("SchedaProgettoVO [");
		sb.append(" numeroCriteriTotali="+numeroCriteriTotali);
		sb.append(", criteriList=(" + Arrays.toString(criteriList)+")");
		sb.append(" ]");
		return sb.toString();
	}
}
