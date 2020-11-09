/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class DimensioniNGOutput extends CommonalityOutput {
	
	@MapTo(target=NAMESPACE)
	List<ClassificazioneDimensioniImpresaVO> classificazioneDimensioniImpresaList;
	
	@MapTo(target=NAMESPACE)
	List<DatiImpresaVO> datiImpresaList;

	@MapTo(target=NAMESPACE)
	List<DatiImpresaVO> datiImpresaListAAEP;	
	
	@MapTo(target=NAMESPACE)
	List<RisorseUmaneVO> risorseUmaneList;
		
	@MapTo(target=NAMESPACE)
	ImpresaVO enteImpresa;
		
	@MapTo(target=NAMESPACE)
	int flagPubblicoPrivato;

	@MapTo(target=NAMESPACE)
	String mostraMsgAAEP;
	
	@MapTo(target=NAMESPACE)
	String viewSezioneDimensioniImpresa;

	
	public List<DatiImpresaVO> getDatiImpresaList() {
		return datiImpresaList;
	}

	public void setDatiImpresaList(List<DatiImpresaVO> datiImpresaList) {
		this.datiImpresaList = datiImpresaList;
	}
	
	public List<DatiImpresaVO> getDatiImpresaListAAEP() {
		return datiImpresaListAAEP;
	}

	public void setDatiImpresaListAAEP(List<DatiImpresaVO> datiImpresaListAAEP) {
		this.datiImpresaListAAEP = datiImpresaListAAEP;
	}

	public List<RisorseUmaneVO> getRisorseUmaneList() {
		return risorseUmaneList;
	}

	public void setRisorseUmaneList(List<RisorseUmaneVO> risorseUmaneList) {
		this.risorseUmaneList = risorseUmaneList;
	}

	public List<ClassificazioneDimensioniImpresaVO> getClassificazioneDimensioniImpresaList() {
		return classificazioneDimensioniImpresaList;
	}

	public void setClassificazioneDimensioniImpresaList(
			List<ClassificazioneDimensioniImpresaVO> classificazioneDimensioniImpresaList) {
		this.classificazioneDimensioniImpresaList = classificazioneDimensioniImpresaList;
	}

	public ImpresaVO getEnteImpresa() {
		return enteImpresa;
	}

	public void setEnteImpresa(ImpresaVO enteImpresa) {
		this.enteImpresa = enteImpresa;
	}

	public int getFlagPubblicoPrivato() {
		return flagPubblicoPrivato;
	}

	public void setFlagPubblicoPrivato(int flagPubblicoPrivato) {
		this.flagPubblicoPrivato = flagPubblicoPrivato;
	}

	public String getMostraMsgAAEP() {
		return mostraMsgAAEP;
	}

	public void setMostraMsgAAEP(String mostraMsgAAEP) {
		this.mostraMsgAAEP = mostraMsgAAEP;
	}

	public String getViewSezioneDimensioniImpresa() {
		return viewSezioneDimensioniImpresa;
	}

	public void setViewSezioneDimensioniImpresa(String viewSezioneDimensioniImpresa) {
		this.viewSezioneDimensioniImpresa = viewSezioneDimensioniImpresa;
	}
						
	
}
