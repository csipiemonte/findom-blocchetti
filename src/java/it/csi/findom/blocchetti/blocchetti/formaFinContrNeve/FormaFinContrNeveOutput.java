/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinContrNeve;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.TipologiaInterventoNeveVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class FormaFinContrNeveOutput extends CommonalityOutput 
{

	@MapTo(target=NAMESPACE)
	String viewWarningAgevolazioni;
	
	@MapTo(target=NAMESPACE)
	String totaleSpeseContrCatg;
	
	@MapTo(target=NAMESPACE)
	String strTotCat97;
	
	@MapTo(target=NAMESPACE)
	String strTotCat98;
	
	@MapTo(target=NAMESPACE)
	String totCat99;
	
	@MapTo(target=NAMESPACE)
	String strImportoCat97;
	
	@MapTo(target=NAMESPACE)
	String strImportoCat98;
	
	@MapTo(target=NAMESPACE)
	String strImportoCat99;
	
	@MapTo(target=NAMESPACE)
	String  strTotImportoRichiesto;
	
	@MapTo(target=NAMESPACE)
	String str_spesaMaxDichiarabile_97;
	

	@MapTo(target=NAMESPACE)
	String str_spesaMaxDichiarabile_98;
	
	

	/**
	 * Jira: 1337:
	 * Visualizzazione msg in caso di assenza importi
	 * presenti a database.
	 */
	@MapTo(target=NAMESPACE)
	String viewWarningImportiAgevolazioni;

	@MapTo(target=MapTarget.NAMESPACE)
	List<TipologiaInterventoNeveVO> tipologiaInterventoList;
	
	@MapTo(target=MapTarget.NAMESPACE)
	List<ValoriSportelloVO> valoriContributoList;
	
	@MapTo(target=MapTarget.NAMESPACE)
	List<String> totaliContributoList;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String esistonoDettagli;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String viewWarningSpese;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String speseNonCompilate;
	
	
	// Tipo beneficiario: Grandi stazioni
	@MapTo(target=MapTarget.NAMESPACE)
	String isGrandiStazioni;
	
	
	
	// Tipo intervento selezionato
	@MapTo(target=MapTarget.NAMESPACE)
	String isSelectedIdTipoIntervento97;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String isSelectedIdTipoIntervento98;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String isSelectedIdTipoIntervento99;
	
	/** ------------------------------------- Percentuali per tipologia di intervento */
	@MapTo(target=MapTarget.NAMESPACE)
	String bd_percMaxErogabile_CatA_97;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String bd_percMaxErogabile_CatA_98;
	
	@MapTo(target=MapTarget.NAMESPACE)
	String bd_percMaxErogabile_CatC_99;

	
	/** ------------------------------------- Get | Set */
	public String getStr_spesaMaxDichiarabile_97() {
		return str_spesaMaxDichiarabile_97;
	}

	public void setStr_spesaMaxDichiarabile_97(String str_spesaMaxDichiarabile_97) {
		this.str_spesaMaxDichiarabile_97 = str_spesaMaxDichiarabile_97;
	}
	
	
	public String getStrTotCat98() {
		return strTotCat98;
	}

	public String getSpeseNonCompilate() {
		return speseNonCompilate;
	}

	public void setSpeseNonCompilate(String speseNonCompilate) {
		this.speseNonCompilate = speseNonCompilate;
	}

	public String getIsSelectedIdTipoIntervento97() {
		return isSelectedIdTipoIntervento97;
	}

	public void setIsSelectedIdTipoIntervento97(String isSelectedIdTipoIntervento97) {
		this.isSelectedIdTipoIntervento97 = isSelectedIdTipoIntervento97;
	}

	public String getViewWarningAgevolazioni() {
		return viewWarningAgevolazioni;
	}

	public void setStrTotCat98(String strTotCat98) {
		this.strTotCat98 = strTotCat98;
	}
	

	public String getTotCat99() {
		return totCat99;
	}

	public void setTotCat99(String totCat99) {
		this.totCat99 = totCat99;
	}

	public String getSetTotCat98() {
		return strTotCat98;
	}

	public void setSetTotCat98(String strTotCat98) {
		this.strTotCat98 = strTotCat98;
	}
	

	public String getStrTotCat97() {
		return strTotCat97;
	}

	public void setStrTotCat97(String strTotCat97) {
		this.strTotCat97 = strTotCat97;
	}


	public List<String> getTotaliContributoList() {
		return totaliContributoList;
	}

	public void setTotaliContributoList(List<String> totaliContributoList) {
		this.totaliContributoList = totaliContributoList;
	}

	public String getTotaleSpeseContrCatg() {
		return totaleSpeseContrCatg;
	}

	public void setTotaleSpeseContrCatg(String totaleSpeseContrCatg) {
		this.totaleSpeseContrCatg = totaleSpeseContrCatg;
	}

	
	public List<ValoriSportelloVO> getValoriContributoList() {
		return valoriContributoList;
	}

	public void setValoriContributoList(List<ValoriSportelloVO> valoriContributoList) {
		this.valoriContributoList = valoriContributoList;
	}

	public String getIsSelectedIdTipoIntervento98() {
		return isSelectedIdTipoIntervento98;
	}

	public void setIsSelectedIdTipoIntervento98(String isSelectedIdTipoIntervento98) {
		this.isSelectedIdTipoIntervento98 = isSelectedIdTipoIntervento98;
	}

	public String getIsSelectedIdTipoIntervento99() {
		return isSelectedIdTipoIntervento99;
	}

	public void setIsSelectedIdTipoIntervento99(String isSelectedIdTipoIntervento99) {
		this.isSelectedIdTipoIntervento99 = isSelectedIdTipoIntervento99;
	}

	public String isGrandiStazioni() {
		return isGrandiStazioni;
	}

	public void setGrandiStazioni(String isGrandiStazioni) {
		this.isGrandiStazioni = isGrandiStazioni;
	}


	public String getViewWarningSpese() {
		return viewWarningSpese;
	}

	public void setViewWarningSpese(String viewWarningSpese) {
		this.viewWarningSpese = viewWarningSpese;
	}

	public String getEsistonoDettagli() {
		return esistonoDettagli;
	}

	public void setEsistonoDettagli(String esistonoDettagli) {
		this.esistonoDettagli = esistonoDettagli;
	}

	public String getViewWarningImportiAgevolazioni() {
		return viewWarningImportiAgevolazioni;
	}

	public void setViewWarningImportiAgevolazioni(
			String viewWarningImportiAgevolazioni) {
		this.viewWarningImportiAgevolazioni = viewWarningImportiAgevolazioni;
	}


	public void setViewWarningAgevolazioni(String viewWarningAgevolazioni) {
		this.viewWarningAgevolazioni = viewWarningAgevolazioni;
	}


	/********************************************************************
	 * :  -  
	 * @return
	 */
	public List<TipologiaInterventoNeveVO> getTipologiaInterventoList() {
		return tipologiaInterventoList;
	}

	public void setTipologiaInterventoList(
			List<TipologiaInterventoNeveVO> tipologiaInterventoList) {
		this.tipologiaInterventoList = tipologiaInterventoList;
	}


	public String getStrImportoCat97() {
		return strImportoCat97;
	}

	public void setStrImportoCat97(String strImportoCat97) {
		this.strImportoCat97 = strImportoCat97;
	}

	public String getStrImportoCat98() {
		return strImportoCat98;
	}

	public void setStrImportoCat98(String strImportoCat98) {
		this.strImportoCat98 = strImportoCat98;
	}

	public String getStrImportoCat99() {
		return strImportoCat99;
	}

	public void setStrImportoCat99(String strImportoCat99) {
		this.strImportoCat99 = strImportoCat99;
	}

	public String getStrTotImportoRichiesto() {
		return strTotImportoRichiesto;
	}

	public void setStrTotImportoRichiesto(String strTotImportoRichiesto) {
		this.strTotImportoRichiesto = strTotImportoRichiesto;
	}

	public String getStr_spesaMaxDichiarabile_98() {
		return str_spesaMaxDichiarabile_98;
	}

	public void setStr_spesaMaxDichiarabile_98(String str_spesaMaxDichiarabile_98) {
		this.str_spesaMaxDichiarabile_98 = str_spesaMaxDichiarabile_98;
	}

	public String getIsGrandiStazioni() {
		return isGrandiStazioni;
	}

	public void setIsGrandiStazioni(String isGrandiStazioni) {
		this.isGrandiStazioni = isGrandiStazioni;
	}

	public String getBd_percMaxErogabile_CatA_97() {
		return bd_percMaxErogabile_CatA_97;
	}

	public void setBd_percMaxErogabile_CatA_97(String bd_percMaxErogabile_CatA_97) {
		this.bd_percMaxErogabile_CatA_97 = bd_percMaxErogabile_CatA_97;
	}

	public String getBd_percMaxErogabile_CatA_98() {
		return bd_percMaxErogabile_CatA_98;
	}

	public void setBd_percMaxErogabile_CatA_98(String bd_percMaxErogabile_CatA_98) {
		this.bd_percMaxErogabile_CatA_98 = bd_percMaxErogabile_CatA_98;
	}

	public String getBd_percMaxErogabile_CatC_99() {
		return bd_percMaxErogabile_CatC_99;
	}

	public void setBd_percMaxErogabile_CatC_99(String bd_percMaxErogabile_CatC_99) {
		this.bd_percMaxErogabile_CatC_99 = bd_percMaxErogabile_CatC_99;
	}
		
}
