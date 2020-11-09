/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinContrNeve;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.TipologiaInterventoNeveVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class FormaFinContrNeveVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target=INHERIT)
	String importoRichiestoCat97;
	
	@MapTo(target=INHERIT)
	String importoRichiestoCat98;
	
	@MapTo(target=INHERIT)
	String importoRichiestoCat99;
	
	@MapTo(target=INHERIT)
	String str_spesaMaxDichiarabile_97;
	
	public String getStr_spesaMaxDichiarabile_97() {
		return str_spesaMaxDichiarabile_97;
	}

	public void setStr_spesaMaxDichiarabile_97(String str_spesaMaxDichiarabile_97) {
		this.str_spesaMaxDichiarabile_97 = str_spesaMaxDichiarabile_97;
	}

	@MapTo(target=INHERIT)
	String spesa_max_dichiarabile_catg99;
	
	@MapTo(target=INHERIT)
	String strTotCat97;
	
	@MapTo(target=INHERIT)
	String strTotCat98;
	
	@MapTo(target=INHERIT)
	String totCat99;
	
	
	@MapTo(target=INHERIT)
	String str_spesaMaxDichiarabile_98;
	
	@MapTo(target=INHERIT)
	String totaleImportoContributo;
	
	@MapTo(target=INHERIT)
	String totaleSpeseContributo;
	
	@MapTo(target=MapTarget.INHERIT)
	TipologiaInterventoNeveVO[] tipologiaInterventoList;
	

	public TipologiaInterventoNeveVO[] getTipologiaInterventoList() {
		return tipologiaInterventoList;
	}

	public void setTipologiaInterventoList(
			TipologiaInterventoNeveVO[] tipologiaInterventoList) {
		this.tipologiaInterventoList = tipologiaInterventoList;
	}

	public String getStr_spesaMaxDichiarabile_98() {
		return str_spesaMaxDichiarabile_98;
	}

	public void setStr_spesaMaxDichiarabile_98(String str_spesaMaxDichiarabile_98) {
		this.str_spesaMaxDichiarabile_98 = str_spesaMaxDichiarabile_98;
	}

	public String getImportoRichiestoCat97() {
		return importoRichiestoCat97;
	}

	public void setImportoRichiestoCat97(String importoRichiestoCat97) {
		this.importoRichiestoCat97 = importoRichiestoCat97;
	}

	public String getImportoRichiestoCat98() {
		return importoRichiestoCat98;
	}

	public void setImportoRichiestoCat98(String importoRichiestoCat98) {
		this.importoRichiestoCat98 = importoRichiestoCat98;
	}

	public String getImportoRichiestoCat99() {
		return importoRichiestoCat99;
	}

	public void setImportoRichiestoCat99(String importoRichiestoCat99) {
		this.importoRichiestoCat99 = importoRichiestoCat99;
	}

	public String getSpesa_max_dichiarabile_catg99() {
		return spesa_max_dichiarabile_catg99;
	}

	public void setSpesa_max_dichiarabile_catg99(String spesa_max_dichiarabile_catg99) {
		this.spesa_max_dichiarabile_catg99 = spesa_max_dichiarabile_catg99;
	}

	public String getStrTotCat97() {
		return strTotCat97;
	}

	public void setStrTotCat97(String strTotCat97) {
		this.strTotCat97 = strTotCat97;
	}

	public String getStrTotCat98() {
		return strTotCat98;
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

	public String getTotaleImportoContributo() {
		return totaleImportoContributo;
	}

	public void setTotaleImportoContributo(String totaleImportoContributo) {
		this.totaleImportoContributo = totaleImportoContributo;
	}

	public String getTotaleSpeseContributo() {
		return totaleSpeseContributo;
	}

	public void setTotaleSpeseContributo(String totaleSpeseContributo) {
		this.totaleSpeseContributo = totaleSpeseContributo;
	}
	
}
