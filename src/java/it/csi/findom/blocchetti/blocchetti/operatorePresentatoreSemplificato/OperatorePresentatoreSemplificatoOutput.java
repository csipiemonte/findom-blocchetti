/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.operatorePresentatoreSemplificato;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.attivita.AttivitaVO;
import it.csi.findom.blocchetti.common.vo.formaGiuridica.FormaGiuridicaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class OperatorePresentatoreSemplificatoOutput extends CommonalityOutput {

	  @MapTo(target=NAMESPACE)
	  List<AttivitaVO> settoreAttivitaEconomicaList;
	  
	  @MapTo(target=NAMESPACE)
	  List <FormaGiuridicaVO> datiFormaGiuridicaList;
	  
	  @MapTo(target=NAMESPACE)
	  ImpresaVO enteImpresa;
	  
	  @MapTo(target=NAMESPACE)
	  String codPrevalenteAtecoAAEP;

	  @MapTo(target=NAMESPACE)
	  String descrizioneAtecoAAEP;

	  @MapTo(target=NAMESPACE)
	  String idAtecoAAEP;
	  
	  @MapTo(target=NAMESPACE)
	  OperatorePresentatoreVo operPresent;

//	  Pochettino 21/6/2019 : commento la funzionalita Dipartimenti
//	  @MapTo(target=NAMESPACE)
//	  List<DipartimentoVO> dipartimentiList;

	  @MapTo(target=NAMESPACE)
	  String visNotaPrecompilazioneAAEP;
	  
	  @MapTo(target=NAMESPACE)
	  String visNotaPrecompilazioneUltimaDomanda;

	  @MapTo(target=NAMESPACE)
	  String visIpa;
	  
	  @MapTo(target=NAMESPACE)
	  String beneficiarioEstero;
	  
	  @MapTo(target=NAMESPACE)
	  String statoEsteroRO;
	  
	  @MapTo(target=NAMESPACE)
	  List<StatoEsteroVO> statoEsteroList;
	  
	  
	  @MapTo(target=NAMESPACE)
	  String unitaOrganizzativaRO;
	  
	  @MapTo(target=NAMESPACE)
	  String codTipoBeneficiario;
	  
	public String getCodTipoBeneficiario() {
		return codTipoBeneficiario;
	}

	public void setCodTipoBeneficiario(String codTipoBeneficiario) {
		this.codTipoBeneficiario = codTipoBeneficiario;
	}

	public List<AttivitaVO> getSettoreAttivitaEconomicaList() {
		return settoreAttivitaEconomicaList;
	}

	public void setSettoreAttivitaEconomicaList(
			List<AttivitaVO> settoreAttivitaEconomicaList) {
		this.settoreAttivitaEconomicaList = settoreAttivitaEconomicaList;
	}

	public List<FormaGiuridicaVO> getDatiFormaGiuridicaList() {
		return datiFormaGiuridicaList;
	}

	public void setDatiFormaGiuridicaList(
			List<FormaGiuridicaVO> datiFormaGiuridicaList) {
		this.datiFormaGiuridicaList = datiFormaGiuridicaList;
	}

	public ImpresaVO getEnteImpresa() {
		return enteImpresa;
	}

	public void setEnteImpresa(ImpresaVO enteImpresa) {
		this.enteImpresa = enteImpresa;
	}

	public String getCodPrevalenteAtecoAAEP() {
		return codPrevalenteAtecoAAEP;
	}

	public void setCodPrevalenteAtecoAAEP(String codPrevalenteAtecoAAEP) {
		this.codPrevalenteAtecoAAEP = codPrevalenteAtecoAAEP;
	}

	public String getDescrizioneAtecoAAEP() {
		return descrizioneAtecoAAEP;
	}

	public void setDescrizioneAtecoAAEP(String descrizioneAtecoAAEP) {
		this.descrizioneAtecoAAEP = descrizioneAtecoAAEP;
	}

	public String getIdAtecoAAEP() {
		return idAtecoAAEP;
	}

	public void setIdAtecoAAEP(String idAtecoAAEP) {
		this.idAtecoAAEP = idAtecoAAEP;
	}

	public OperatorePresentatoreVo getOperPresent() {
		return operPresent;
	}

	public void setOperPresent(OperatorePresentatoreVo operPresent) {
		this.operPresent = operPresent;
	}

//	public List<DipartimentoVO> getDipartimentiList() {
//		return dipartimentiList;
//	}
//
//	public void setDipartimentiList(List<DipartimentoVO> dipartimentiList) {
//		this.dipartimentiList = dipartimentiList;
//	}
	
	//gestione beneficiario estero inizio
	public String getBeneficiarioEstero() {
		return beneficiarioEstero;
	}
	public void setBeneficiarioEstero(String beneficiarioEstero) {
		this.beneficiarioEstero = beneficiarioEstero;
	}	
	public String getStatoEsteroRO() {
		return statoEsteroRO;
	}
	public void setStatoEsteroRO(String statoEsteroRO) {
		this.statoEsteroRO = statoEsteroRO;
	}
	public List<StatoEsteroVO> getStatoEsteroList() {
		return statoEsteroList;
	}
	public void setStatoEsteroList(List<StatoEsteroVO> statoEsteroList) {
		this.statoEsteroList = statoEsteroList;
	}
	//gestione beneficiario estero fine

	public String getUnitaOrganizzativaRO() {
		return unitaOrganizzativaRO;
	}

	public void setUnitaOrganizzativaRO(String unitaOrganizzativaRO) {
		this.unitaOrganizzativaRO = unitaOrganizzativaRO;
	}
	
}
