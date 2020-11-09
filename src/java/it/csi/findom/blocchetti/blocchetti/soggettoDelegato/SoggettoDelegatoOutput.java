/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.soggettoDelegato;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.common.vo.tipodocriconoscimento.TipoDocRiconoscimentoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class SoggettoDelegatoOutput extends CommonalityOutput {

  @MapTo(target=NAMESPACE)
  List<StatoEsteroVO> statoEsteroList;
	
  @MapTo(target=NAMESPACE)
  List<ProvinciaVO> provinciaList;
	
  @MapTo(target=NAMESPACE)
  List<ComuneVO> comuneNascitaList;

  @MapTo(target=NAMESPACE)
  List<ComuneVO> comuneResidenzaList;

  @MapTo(target=NAMESPACE)
  List<TipoDocRiconoscimentoVO> tipoDocRiconoscimentoList;
  
  @MapTo(target=NAMESPACE, name="_soggettoDelegato_siglaProvinciaNascita")
  String soggettoDelegato_siglaProvinciaNascita;
	
  @MapTo(target=NAMESPACE, name="_soggettoDelegato_siglaProvinciaResidenza")
  String soggettoDelegato_siglaProvinciaResidenza;

public List<StatoEsteroVO> getStatoEsteroList() {
	return statoEsteroList;
}

public void setStatoEsteroList(List<StatoEsteroVO> statoEsteroList) {
	this.statoEsteroList = statoEsteroList;
}

public List<ProvinciaVO> getProvinciaList() {
	return provinciaList;
}

public void setProvinciaList(List<ProvinciaVO> provinciaList) {
	this.provinciaList = provinciaList;
}

public List<ComuneVO> getComuneNascitaList() {
	return comuneNascitaList;
}

public void setComuneNascitaList(List<ComuneVO> comuneNascitaList) {
	this.comuneNascitaList = comuneNascitaList;
}

public List<ComuneVO> getComuneResidenzaList() {
	return comuneResidenzaList;
}

public void setComuneResidenzaList(List<ComuneVO> comuneResidenzaList) {
	this.comuneResidenzaList = comuneResidenzaList;
}

public List<TipoDocRiconoscimentoVO> getTipoDocRiconoscimentoList() {
	return tipoDocRiconoscimentoList;
}

public void setTipoDocRiconoscimentoList(
		List<TipoDocRiconoscimentoVO> tipoDocRiconoscimentoList) {
	this.tipoDocRiconoscimentoList = tipoDocRiconoscimentoList;
}

public String getSoggettoDelegato_siglaProvinciaNascita() {
	return soggettoDelegato_siglaProvinciaNascita;
}

public void setSoggettoDelegato_siglaProvinciaNascita(
		String soggettoDelegato_siglaProvinciaNascita) {
	this.soggettoDelegato_siglaProvinciaNascita = soggettoDelegato_siglaProvinciaNascita;
}

public String getSoggettoDelegato_siglaProvinciaResidenza() {
	return soggettoDelegato_siglaProvinciaResidenza;
}

public void setSoggettoDelegato_siglaProvinciaResidenza(
		String soggettoDelegato_siglaProvinciaResidenza) {
	this.soggettoDelegato_siglaProvinciaResidenza = soggettoDelegato_siglaProvinciaResidenza;
}
	

}
