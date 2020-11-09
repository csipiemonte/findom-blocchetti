/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.sedeLegale;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.aaep.SedeVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class SedeLegaleOutput extends CommonalityOutput {

  @MapTo(target=NAMESPACE)
  List<StatoEsteroVO> statoEsteroSLList;
	
  @MapTo(target=NAMESPACE)
  List<ProvinciaVO> provinciaSLList;
	
  @MapTo(target=NAMESPACE)
  List<ComuneVO> comuneSLList;

  @MapTo(target=NAMESPACE)
  SedeLegaleVO sedeLeg;
  
  @MapTo(target=NAMESPACE)
  SedeVO sedeLegaleAziCorrente;
	
  @MapTo(target=NAMESPACE)
  ImpresaVO enteImpresa;
	
  @MapTo(target=NAMESPACE, name="_sedeLegale_siglaProvincia")
  String sedeLegale_siglaProvincia;

  @MapTo(target=NAMESPACE)
  String visNotaPrecompilazioneSedeAAEP;
  
  @MapTo(target=NAMESPACE)
  String visNotaPrecompilazioneSedeUltimaDomanda;
  
  /** Jira: 1842 :  */
  @MapTo(target = NAMESPACE)
  String svuotaCampiResidenzaSL;
  

public String getSvuotaCampiResidenzaSL() {
	return svuotaCampiResidenzaSL;
}

public void setSvuotaCampiResidenzaSL(String svuotaCampiResidenzaSL) {
	this.svuotaCampiResidenzaSL = svuotaCampiResidenzaSL;
}

public SedeLegaleVO getSedeLeg() {
	return sedeLeg;
}

public void setSedeLeg(SedeLegaleVO sedeLeg) {
	this.sedeLeg = sedeLeg;
}

public SedeVO getSedeLegaleAziCorrente() {
	return sedeLegaleAziCorrente;
}

public void setSedeLegaleAziCorrente(SedeVO sedeLegaleAziCorrente) {
	this.sedeLegaleAziCorrente = sedeLegaleAziCorrente;
}

public ImpresaVO getEnteImpresa() {
	return enteImpresa;
}

public void setEnteImpresa(ImpresaVO enteImpresa) {
	this.enteImpresa = enteImpresa;
}

public String getSedeLegale_siglaProvincia() {
	return sedeLegale_siglaProvincia;
}

public void setSedeLegale_siglaProvincia(String sedeLegale_siglaProvincia) {
	this.sedeLegale_siglaProvincia = sedeLegale_siglaProvincia;
}

public List<StatoEsteroVO> getStatoEsteroSLList() {
	return statoEsteroSLList;
}

public void setStatoEsteroSLList(List<StatoEsteroVO> statoEsteroSLList) {
	this.statoEsteroSLList = statoEsteroSLList;
}

public List<ProvinciaVO> getProvinciaSLList() {
	return provinciaSLList;
}

public void setProvinciaSLList(List<ProvinciaVO> provinciaSLList) {
	this.provinciaSLList = provinciaSLList;
}

public List<ComuneVO> getComuneSLList() {
	return comuneSLList;
}

public void setComuneSLList(List<ComuneVO> comuneSLList) {
	this.comuneSLList = comuneSLList;
}

public String getVisNotaPrecompilazioneSedeAAEP() {
	return visNotaPrecompilazioneSedeAAEP;
}

public void setVisNotaPrecompilazioneSedeAAEP(
		String visNotaPrecompilazioneSedeAAEP) {
	this.visNotaPrecompilazioneSedeAAEP = visNotaPrecompilazioneSedeAAEP;
}

public String getVisNotaPrecompilazioneSedeUltimaDomanda() {
	return visNotaPrecompilazioneSedeUltimaDomanda;
}

public void setVisNotaPrecompilazioneSedeUltimaDomanda(
		String visNotaPrecompilazioneSedeUltimaDomanda) {
	this.visNotaPrecompilazioneSedeUltimaDomanda = visNotaPrecompilazioneSedeUltimaDomanda;
}



}
