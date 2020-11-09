/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioniSA;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;
import java.util.Map;

public class DichiarazioniSAOutput extends CommonalityOutput {
	
	@MapTo(target=NAMESPACE)
	List<ElementoDichiarazioniVO> dichiarazioniList;
	
	@MapTo(target=NAMESPACE)
	Map<String,String> dettaglioDichiarazioniInputMap;

	
	public List<ElementoDichiarazioniVO> getDichiarazioniList() {
		System.out.println("DichiarazioniSAOutput::getDichiarazioniList ");
		return dichiarazioniList;
	}

	public Map<String, String> getDettaglioDichiarazioniInputMap() {
		return dettaglioDichiarazioniInputMap;
	}

	public void setDichiarazioniList(List<ElementoDichiarazioniVO> dichiarazioniList) {
		
		if(dichiarazioniList!=null)
			System.out.println("DichiarazioniSAOutput::setDichiarazioniList "+dichiarazioniList.toString());
		else
			System.out.println("DichiarazioniSAOutput::setDichiarazioniList NULL");
		
		this.dichiarazioniList = dichiarazioniList;
	}

	public void setDettaglioDichiarazioniInputMap(
			Map<String, String> dettaglioDichiarazioniInputMap) {
		this.dettaglioDichiarazioniInputMap = dettaglioDichiarazioniInputMap;
	}
}
