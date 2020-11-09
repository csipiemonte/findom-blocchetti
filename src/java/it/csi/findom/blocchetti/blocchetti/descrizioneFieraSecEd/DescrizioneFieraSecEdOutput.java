/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFieraSecEd;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class DescrizioneFieraSecEdOutput extends CommonalityOutput {
	
	@MapTo(target=NAMESPACE)
	List<StatoEsteroVO> statoEsteroSLList;
	
	@MapTo(target=NAMESPACE)
	DescrizioneFieraSecEdVO descrizioneFieraSecEd;
	
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

	public DescrizioneFieraSecEdVO getdescrizioneFieraSecEd() {
		return descrizioneFieraSecEd;
	}

	public void setdescrizioneFieraSecEd(DescrizioneFieraSecEdVO descrizioneFieraSecEd) {
		this.descrizioneFieraSecEd = descrizioneFieraSecEd;
	}

}
