/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DomandaNGVO extends CommonalityVO {

	@MapTo(target=INHERIT)
	  String codTipologiaUtente;
	  
	  @MapTo(target=INHERIT)
	  String stereotipoDomanda;
	 
	  @MapTo(target=INHERIT)
	  String descrTipologiaUtente;
	  
	  @MapTo(target=INHERIT)
	  String descrStereotipoDomanda;
	  
	  @MapTo(target=INHERIT)
	  String codiceTipologiaBeneficiario;
	  
	  @MapTo(target=INHERIT)
	  String idSettore;
		
	  @MapTo(target=INHERIT)
	  String descrSettore;
	  
	  @MapTo(target=INHERIT)
	  String idDirezione;

	  @MapTo(target=INHERIT)
	  String descrDirezione;

	  @MapTo(target=INHERIT)
	  Integer flagPubblicoPrivato;
	  
	  @MapTo(target=INHERIT)
	  String normativa;
	  
	  @MapTo(target=INHERIT)
	  String numAtto;
	  
	  @MapTo(target=INHERIT)
	  String dataAtto;
	  
	  @MapTo(target=INHERIT)
	  String flagProgettiComuni;
	  
	  @MapTo(target=INHERIT)
	  String denominazioneSoggettoBeneficiario;
	  
	  @MapTo(target=INHERIT)
	  String siglaNazioneSoggettoBeneficiario;
	    
	  @MapTo(target=INHERIT)
	  String codiceUnitaOrganizzativaSoggettoBeneficiario;	  
	 
	
	public String getCodTipologiaUtente() {
		return codTipologiaUtente;
	}

	public String getStereotipoDomanda() {
		return stereotipoDomanda;
	}

	public String getDescrTipologiaUtente() {
		return descrTipologiaUtente;
	}

	public String getDescrStereotipoDomanda() {
		return descrStereotipoDomanda;
	}

	public String getCodiceTipologiaBeneficiario() {
		return codiceTipologiaBeneficiario;
	}

	public String getIdSettore() {
		return idSettore;
	}

	public String getDescrSettore() {
		return descrSettore;
	}

	public String getIdDirezione() {
		return idDirezione;
	}

	public String getDescrDirezione() {
		return descrDirezione;
	}

	public Integer getFlagPubblicoPrivato() {
		return flagPubblicoPrivato;
	}

	public void setCodTipologiaUtente(String codTipologiaUtente) {
		this.codTipologiaUtente = codTipologiaUtente;
	}

	public void setStereotipoDomanda(String stereotipoDomanda) {
		this.stereotipoDomanda = stereotipoDomanda;
	}

	public void setDescrTipologiaUtente(String descrTipologiaUtente) {
		this.descrTipologiaUtente = descrTipologiaUtente;
	}

	public void setDescrStereotipoDomanda(String descrStereotipoDomanda) {
		this.descrStereotipoDomanda = descrStereotipoDomanda;
	}

	public void setCodiceTipologiaBeneficiario(String codiceTipologiaBeneficiario) {
		this.codiceTipologiaBeneficiario = codiceTipologiaBeneficiario;
	}

	public void setIdSettore(String idSettore) {
		this.idSettore = idSettore;
	}

	public void setDescrSettore(String descrSettore) {
		this.descrSettore = descrSettore;
	}

	public void setIdDirezione(String idDirezione) {
		this.idDirezione = idDirezione;
	}

	public void setDescrDirezione(String descrDirezione) {
		this.descrDirezione = descrDirezione;
	}

	public void setFlagPubblicoPrivato(Integer flagPubblicoPrivato) {
		this.flagPubblicoPrivato = flagPubblicoPrivato;
	}

	public String getNormativa() {
		return normativa;
	}

	public void setNormativa(String normativa) {
		this.normativa = normativa;
	}

	public String getNumAtto() {
		return numAtto;
	}

	public void setNumAtto(String numAtto) {
		this.numAtto = numAtto;
	}

	public String getDataAtto() {
		return dataAtto;
	}

	public void setDataAtto(String dataAtto) {
		this.dataAtto = dataAtto;
	}	

	public String getFlagProgettiComuni() {
		return flagProgettiComuni;
	}

	public void setFlagProgettiComuni(String flagProgettiComuni) {
		this.flagProgettiComuni = flagProgettiComuni;
	}

	public String getDenominazioneSoggettoBeneficiario() {
		return denominazioneSoggettoBeneficiario;
	}

	public void setDenominazioneSoggettoBeneficiario(
			String denominazioneSoggettoBeneficiario) {
		this.denominazioneSoggettoBeneficiario = denominazioneSoggettoBeneficiario;
	}

	public String getSiglaNazioneSoggettoBeneficiario() {
		return siglaNazioneSoggettoBeneficiario;
	}

	public void setSiglaNazioneSoggettoBeneficiario(
			String siglaNazioneSoggettoBeneficiario) {
		this.siglaNazioneSoggettoBeneficiario = siglaNazioneSoggettoBeneficiario;
	}

	public String getCodiceUnitaOrganizzativaSoggettoBeneficiario() {
		return codiceUnitaOrganizzativaSoggettoBeneficiario;
	}

	public void setCodiceUnitaOrganizzativaSoggettoBeneficiario(
			String codiceUnitaOrganizzativaSoggettoBeneficiario) {
		this.codiceUnitaOrganizzativaSoggettoBeneficiario = codiceUnitaOrganizzativaSoggettoBeneficiario;
	}
	
	

	@Override
	public String toString() {
	    return "DomandaNGVO [codTipologiaUtente=" + codTipologiaUtente + ", stereotipoDomanda=" + stereotipoDomanda + ", flagPubblicoPrivato=" +
	flagPubblicoPrivato + ", codTipologiaUtente=" + codTipologiaUtente + ", flagProgettiComuni=" + flagProgettiComuni + 
	", denominazioneSoggettoBeneficiario = " + denominazioneSoggettoBeneficiario + ", siglaNazioneSoggettoBeneficiario = " + siglaNazioneSoggettoBeneficiario + 
	", codiceUnitaOrganizzativaSoggettoBeneficiario = " + codiceUnitaOrganizzativaSoggettoBeneficiario + "]";
	  }
}
