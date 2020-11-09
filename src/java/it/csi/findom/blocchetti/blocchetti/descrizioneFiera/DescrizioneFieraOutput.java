/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFiera;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class DescrizioneFieraOutput extends CommonalityOutput {
	
	@MapTo(target=NAMESPACE)
	List<StatoEsteroVO> statoEsteroSLList;
	
	@MapTo(target=NAMESPACE)
	DescrizioneFieraVO descrizioneFiera;
	
	@MapTo(target=NAMESPACE)
	String statoEstero;

	public String getStatoEstero() {
		return statoEstero;
	}

	public void setStatoEstero(String statoEstero) {
		this.statoEstero = statoEstero;
	}

	public List<StatoEsteroVO> getStatoEsteroSLList() {
		return statoEsteroSLList;
	}

	public void setStatoEsteroSLList(List<StatoEsteroVO> statoEsteroSLList) {
		this.statoEsteroSLList = statoEsteroSLList;
	}

	public DescrizioneFieraVO getDescrizioneFiera() {
		return descrizioneFiera;
	}

	public void setDescrizioneFiera(DescrizioneFieraVO descrizioneFiera) {
		this.descrizioneFiera = descrizioneFiera;
	}
	
}
