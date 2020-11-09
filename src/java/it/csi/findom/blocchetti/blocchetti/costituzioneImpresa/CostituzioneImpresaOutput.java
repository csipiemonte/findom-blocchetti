/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.costituzioneImpresa;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.RegioneProvinciaVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class CostituzioneImpresaOutput extends CommonalityOutput {
	
	@MapTo(target=NAMESPACE)
	String dataCostituzioneImpresaXML;
	
	@MapTo(target=NAMESPACE)
	List<RegioneProvinciaVO> provinceRegImpresaList;
	
	@MapTo(target=NAMESPACE)
	ImpresaVO entImprAAEP;

	@MapTo(target=NAMESPACE)
	CostituzioneImpresaVo costImpr;
	
	@MapTo(target=NAMESPACE)
	String costituzioneInCorso;
	
	@MapTo(target=NAMESPACE)
	String iscrizioneInCorso;
	
	@MapTo(target=NAMESPACE)
	String dataCostituzioneImpresaAAEP;

	@MapTo(target=NAMESPACE)
	String siglaProvinciaIscrizioneREA;
	
	@MapTo(target=NAMESPACE)
	String dataIscrizioneRegistroImpreseAAEP;
	
	
	public String getDataIscrizioneRegistroImpreseAAEP() {
		return dataIscrizioneRegistroImpreseAAEP;
	}

	public void setDataIscrizioneRegistroImpreseAAEP(
			String dataIscrizioneRegistroImpreseAAEP) {
		this.dataIscrizioneRegistroImpreseAAEP = dataIscrizioneRegistroImpreseAAEP;
	}

	public String getDataCostituzioneImpresaXML() {
		return dataCostituzioneImpresaXML;
	}

	public void setDataCostituzioneImpresaXML(String dataCostituzioneImpresaXML) {
		this.dataCostituzioneImpresaXML = dataCostituzioneImpresaXML;
	}

	public List<RegioneProvinciaVO> getProvinceRegImpresaList() {
		return provinceRegImpresaList;
	}

	public void setProvinceRegImpresaList(
			List<RegioneProvinciaVO> provinceRegImpresaList) {
		this.provinceRegImpresaList = provinceRegImpresaList;
	}

	public ImpresaVO getEntImprAAEP() {
		return entImprAAEP;
	}

	public void setEntImprAAEP(ImpresaVO entImprAAEP) {
		this.entImprAAEP = entImprAAEP;
	}

	public CostituzioneImpresaVo getCostImpr() {
		return costImpr;
	}

	public void setCostImpr(CostituzioneImpresaVo costImpr) {
		this.costImpr = costImpr;
	}

	public String getCostituzioneInCorso() {
		return costituzioneInCorso;
	}

	public void setCostituzioneInCorso(String costituzioneInCorso) {
		this.costituzioneInCorso = costituzioneInCorso;
	}

	public String getDataCostituzioneImpresaAAEP() {
		return dataCostituzioneImpresaAAEP;
	}

	public String getSiglaProvinciaIscrizioneREA() {
		return siglaProvinciaIscrizioneREA;
	}

	/** Jira: 1699 :: */
	public void setDataCostituzioneImpresaAAEP(String dataCostituzioneImpresaAAEP) {
		this.dataCostituzioneImpresaAAEP = dataCostituzioneImpresaAAEP;
	}

	/** Jira: 1699 :: */
	public void setSiglaProvinciaIscrizioneREA(String siglaProvinciaIscrizioneREA) {
		this.siglaProvinciaIscrizioneREA = siglaProvinciaIscrizioneREA;
	}
	
	public String getIscrizioneInCorso() {
		return iscrizioneInCorso;
	}

	public void setIscrizioneInCorso(String iscrizioneInCorso) {
		this.iscrizioneInCorso = iscrizioneInCorso;
	}

}
