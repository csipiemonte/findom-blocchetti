/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class BilancioVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "anno")
	String anno;
	@MapTo(target = INHERIT, name = "annoPrec")
	String annoPrec;
	
	@MapTo(target=INHERIT,name="creditiVsClienti")               
	String creditiVsClienti;

	@MapTo(target=INHERIT,name="debitiVsFornitori")              
	String debitiVsFornitori;

	@MapTo(target=INHERIT,name="creditiCommScad")                
	String creditiCommScad;

	@MapTo(target=INHERIT,name="disponibilitaLiquide")           
	String disponibilitaLiquide;

	@MapTo(target=INHERIT,name="totaleBilancio")                 
	String totaleBilancio;

	@MapTo(target=INHERIT,name="totalePatrimonio")               
	String totalePatrimonio;

	@MapTo(target=INHERIT,name="debitiSoci")                     
	String debitiSoci;

	@MapTo(target=INHERIT,name="debitiBanche")                   
	String debitiBanche;

	@MapTo(target=INHERIT,name="debitiFornScad")                 
	String debitiFornScad;

	@MapTo(target=INHERIT,name="debitiImpreseCollegate")         
	String debitiImpreseCollegate;

	@MapTo(target=INHERIT,name="debitiControllanti")             
	String debitiControllanti;

	@MapTo(target=INHERIT,name="debitiTributari")                
	String debitiTributari;

	@MapTo(target=INHERIT,name="debitiTributariScad")            
	String debitiTributariScad;

	@MapTo(target=INHERIT,name="ricavi")                         
	String ricavi;

	@MapTo(target=INHERIT,name="totaleValoreProduzione")         
	String totaleValoreProduzione;

	@MapTo(target=INHERIT,name="variazioneLavoriInCorso")        
	String variazioneLavoriInCorso;

	@MapTo(target=INHERIT,name="ammortamentiImm")                
	String ammortamentiImm;

	@MapTo(target=INHERIT,name="ammortamentiMat")                
	String ammortamentiMat;

	@MapTo(target=INHERIT,name="totaleCostiProduzione")          
	String totaleCostiProduzione;

	@MapTo(target=INHERIT,name="proventiFinanziari")             
	String proventiFinanziari;

	@MapTo(target=INHERIT,name="interessiPassivi")               
	String interessiPassivi;

	@MapTo(target=INHERIT,name="proventiGestioneAccessoria")     
	String proventiGestioneAccessoria;

	@MapTo(target=INHERIT,name="oneriGestioneAccessoria")        
	String oneriGestioneAccessoria;

	@MapTo(target=INHERIT,name="ebitda")                         
	String ebitda;

	@MapTo(target=INHERIT,name="ebit")                           
	String ebit;

	@MapTo(target=INHERIT,name="indiceRotazione")                
	String indiceRotazione;

	@MapTo(target=INHERIT,name="dso")                            
	String dso;

	@MapTo(target=INHERIT,name="dpo")                            
	String dpo;

	@MapTo(target=INHERIT,name="ula")                            
	String ula;

	@MapTo(target=INHERIT,name="speseRS")                            
	String speseRS;
	
	@MapTo(target=INHERIT,name="totCreditiVsClienti")                            
	String totCreditiVsClienti;
	
	@MapTo(target=INHERIT,name="creditiVsClientiPrec")               
	String creditiVsClientiPrec;

	@MapTo(target=INHERIT,name="debitiVsFornitoriPrec")              
	String debitiVsFornitoriPrec;

	@MapTo(target=INHERIT,name="creditiCommScadPrec")                
	String creditiCommScadPrec;

	@MapTo(target=INHERIT,name="disponibilitaLiquidePrec")           
	String disponibilitaLiquidePrec;

	@MapTo(target=INHERIT,name="totaleBilancioPrec")                 
	String totaleBilancioPrec;

	@MapTo(target=INHERIT,name="totalePatrimonioPrec")               
	String totalePatrimonioPrec;

	@MapTo(target=INHERIT,name="debitiSociPrec")                     
	String debitiSociPrec;

	@MapTo(target=INHERIT,name="debitiBanchePrec")                   
	String debitiBanchePrec;

	@MapTo(target=INHERIT,name="debitiFornScadPrec")                 
	String debitiFornScadPrec;

	@MapTo(target=INHERIT,name="debitiImpreseCollegatePrec")         
	String debitiImpreseCollegatePrec;

	@MapTo(target=INHERIT,name="debitiControllantiPrec")             
	String debitiControllantiPrec;

	@MapTo(target=INHERIT,name="debitiTributariPrec")                
	String debitiTributariPrec;

	@MapTo(target=INHERIT,name="debitiTributariScadPrec")            
	String debitiTributariScadPrec;

	@MapTo(target=INHERIT,name="ricaviPrec")                         
	String ricaviPrec;

	@MapTo(target=INHERIT,name="totaleValoreProduzionePrec")         
	String totaleValoreProduzionePrec;

	@MapTo(target=INHERIT,name="variazioneLavoriInCorsoPrec")        
	String variazioneLavoriInCorsoPrec;

	@MapTo(target=INHERIT,name="ammortamentiImmPrec")                
	String ammortamentiImmPrec;

	@MapTo(target=INHERIT,name="ammortamentiMatPrec")                
	String ammortamentiMatPrec;

	@MapTo(target=INHERIT,name="totaleCostiProduzionePrec")          
	String totaleCostiProduzionePrec;

	@MapTo(target=INHERIT,name="proventiFinanziariPrec")             
	String proventiFinanziariPrec;

	@MapTo(target=INHERIT,name="interessiPassiviPrec")               
	String interessiPassiviPrec;

	@MapTo(target=INHERIT,name="proventiGestioneAccessoriaPrec")     
	String proventiGestioneAccessoriaPrec;

	@MapTo(target=INHERIT,name="oneriGestioneAccessoriaPrec")        
	String oneriGestioneAccessoriaPrec;

	@MapTo(target=INHERIT,name="ebitdaPrec")                         
	String ebitdaPrec;

	@MapTo(target=INHERIT,name="ebitPrec")                           
	String ebitPrec;

	@MapTo(target=INHERIT,name="indiceRotazionePrec")                
	String indiceRotazionePrec;

	@MapTo(target=INHERIT,name="dsoPrec")                            
	String dsoPrec;

	@MapTo(target=INHERIT,name="dpoPrec")                            
	String dpoPrec;

	@MapTo(target=INHERIT,name="ulaPrec")                            
	String ulaPrec;

	@MapTo(target=INHERIT,name="speseRSPrec")                            
	String speseRSPrec;
	
	@MapTo(target=INHERIT,name="totCreditiVsClientiPrec")                            
	String totCreditiVsClientiPrec;
	
	@MapTo(target=INHERIT,name="fonteDatiAnno")                            
	String fonteDatiAnno;

	@MapTo(target=INHERIT,name="fonteDatiAnnoPrec")                            
	String fonteDatiAnnoPrec;
	
	public String getFonteDatiAnno() {
		return fonteDatiAnno;
	}
	public String getFonteDatiAnnoPrec() {
		return fonteDatiAnnoPrec;
	}
	public void setFonteDatiAnno(String fonteDatiAnno) {
		this.fonteDatiAnno = fonteDatiAnno;
	}
	public void setFonteDatiAnnoPrec(String fonteDatiAnnoPrec) {
		this.fonteDatiAnnoPrec = fonteDatiAnnoPrec;
	}
	public String getAnno() {
		return anno;
	}
	public String getAnnoPrec() {
		return annoPrec;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public void setAnnoPrec(String annoPrec) {
		this.annoPrec = annoPrec;
	}
	public String getCreditiVsClienti() {
		return creditiVsClienti;
	}
	public String getDebitiVsFornitori() {
		return debitiVsFornitori;
	}
	public String getCreditiCommScad() {
		return creditiCommScad;
	}
	public String getDisponibilitaLiquide() {
		return disponibilitaLiquide;
	}
	public String getTotaleBilancio() {
		return totaleBilancio;
	}
	public String getTotalePatrimonio() {
		return totalePatrimonio;
	}
	public String getDebitiSoci() {
		return debitiSoci;
	}
	public String getDebitiBanche() {
		return debitiBanche;
	}
	public String getDebitiFornScad() {
		return debitiFornScad;
	}
	public String getDebitiImpreseCollegate() {
		return debitiImpreseCollegate;
	}
	public String getDebitiControllanti() {
		return debitiControllanti;
	}
	public String getDebitiTributari() {
		return debitiTributari;
	}
	public String getDebitiTributariScad() {
		return debitiTributariScad;
	}
	public String getRicavi() {
		return ricavi;
	}
	public String getTotaleValoreProduzione() {
		return totaleValoreProduzione;
	}
	public String getVariazioneLavoriInCorso() {
		return variazioneLavoriInCorso;
	}
	public String getAmmortamentiImm() {
		return ammortamentiImm;
	}
	public String getAmmortamentiMat() {
		return ammortamentiMat;
	}
	public String getTotaleCostiProduzione() {
		return totaleCostiProduzione;
	}
	public String getProventiFinanziari() {
		return proventiFinanziari;
	}
	public String getInteressiPassivi() {
		return interessiPassivi;
	}
	public String getProventiGestioneAccessoria() {
		return proventiGestioneAccessoria;
	}
	public String getOneriGestioneAccessoria() {
		return oneriGestioneAccessoria;
	}
	public String getEbitda() {
		return ebitda;
	}
	public String getEbit() {
		return ebit;
	}
	public String getIndiceRotazione() {
		return indiceRotazione;
	}
	public String getDso() {
		return dso;
	}
	public String getDpo() {
		return dpo;
	}
	public String getUla() {
		return ula;
	}
	public void setCreditiVsClienti(String creditiVsClienti) {
		this.creditiVsClienti = creditiVsClienti;
	}
	public void setDebitiVsFornitori(String debitiVsFornitori) {
		this.debitiVsFornitori = debitiVsFornitori;
	}
	public void setCreditiCommScad(String creditiCommScad) {
		this.creditiCommScad = creditiCommScad;
	}
	public void setDisponibilitaLiquide(String disponibilitaLiquide) {
		this.disponibilitaLiquide = disponibilitaLiquide;
	}
	public void setTotaleBilancio(String totaleBilancio) {
		this.totaleBilancio = totaleBilancio;
	}
	public void setTotalePatrimonio(String totalePatrimonio) {
		this.totalePatrimonio = totalePatrimonio;
	}
	public void setDebitiSoci(String debitiSoci) {
		this.debitiSoci = debitiSoci;
	}
	public void setDebitiBanche(String debitiBanche) {
		this.debitiBanche = debitiBanche;
	}
	public void setDebitiFornScad(String debitiFornScad) {
		this.debitiFornScad = debitiFornScad;
	}
	public void setDebitiImpreseCollegate(String debitiImpreseCollegate) {
		this.debitiImpreseCollegate = debitiImpreseCollegate;
	}
	public void setDebitiControllanti(String debitiControllanti) {
		this.debitiControllanti = debitiControllanti;
	}
	public void setDebitiTributari(String debitiTributari) {
		this.debitiTributari = debitiTributari;
	}
	public void setDebitiTributariScad(String debitiTributariScad) {
		this.debitiTributariScad = debitiTributariScad;
	}
	public void setRicavi(String ricavi) {
		this.ricavi = ricavi;
	}
	public void setTotaleValoreProduzione(String totaleValoreProduzione) {
		this.totaleValoreProduzione = totaleValoreProduzione;
	}
	public void setVariazioneLavoriInCorso(String variazioneLavoriInCorso) {
		this.variazioneLavoriInCorso = variazioneLavoriInCorso;
	}
	public void setAmmortamentiImm(String ammortamentiImm) {
		this.ammortamentiImm = ammortamentiImm;
	}
	public void setAmmortamentiMat(String ammortamentiMat) {
		this.ammortamentiMat = ammortamentiMat;
	}
	public void setTotaleCostiProduzione(String totaleCostiProduzione) {
		this.totaleCostiProduzione = totaleCostiProduzione;
	}
	public void setProventiFinanziari(String proventiFinanziari) {
		this.proventiFinanziari = proventiFinanziari;
	}
	public void setInteressiPassivi(String interessiPassivi) {
		this.interessiPassivi = interessiPassivi;
	}
	public void setProventiGestioneAccessoria(String proventiGestioneAccessoria) {
		this.proventiGestioneAccessoria = proventiGestioneAccessoria;
	}
	public void setOneriGestioneAccessoria(String oneriGestioneAccessoria) {
		this.oneriGestioneAccessoria = oneriGestioneAccessoria;
	}
	public void setEbitda(String ebitda) {
		this.ebitda = ebitda;
	}
	public void setEbit(String ebit) {
		this.ebit = ebit;
	}
	public void setIndiceRotazione(String indiceRotazione) {
		this.indiceRotazione = indiceRotazione;
	}
	public void setDso(String dso) {
		this.dso = dso;
	}
	public void setDpo(String dpo) {
		this.dpo = dpo;
	}
	public void setUla(String ula) {
		this.ula = ula;
	}
	public String getSpeseRS() {
		return speseRS;
	}
	public String getTotCreditiVsClienti() {
		return totCreditiVsClienti;
	}
	public void setSpeseRS(String speseRS) {
		this.speseRS = speseRS;
	}
	public void setTotCreditiVsClienti(String totCreditiVsClienti) {
		this.totCreditiVsClienti = totCreditiVsClienti;
	}
	public String getSpeseRSPrec() {
		return speseRSPrec;
	}
	public String getTotCreditiVsClientiPrec() {
		return totCreditiVsClientiPrec;
	}
	public String getCreditiVsClientiPrec() {
		return creditiVsClientiPrec;
	}
	public String getDebitiVsFornitoriPrec() {
		return debitiVsFornitoriPrec;
	}
	public String getCreditiCommScadPrec() {
		return creditiCommScadPrec;
	}
	public String getDisponibilitaLiquidePrec() {
		return disponibilitaLiquidePrec;
	}
	public String getTotaleBilancioPrec() {
		return totaleBilancioPrec;
	}
	public String getTotalePatrimonioPrec() {
		return totalePatrimonioPrec;
	}
	public String getDebitiSociPrec() {
		return debitiSociPrec;
	}
	public String getDebitiBanchePrec() {
		return debitiBanchePrec;
	}
	public String getDebitiFornScadPrec() {
		return debitiFornScadPrec;
	}
	public String getDebitiImpreseCollegatePrec() {
		return debitiImpreseCollegatePrec;
	}
	public String getDebitiControllantiPrec() {
		return debitiControllantiPrec;
	}
	public String getDebitiTributariPrec() {
		return debitiTributariPrec;
	}
	public String getDebitiTributariScadPrec() {
		return debitiTributariScadPrec;
	}
	public String getRicaviPrec() {
		return ricaviPrec;
	}
	public String getTotaleValoreProduzionePrec() {
		return totaleValoreProduzionePrec;
	}
	public String getVariazioneLavoriInCorsoPrec() {
		return variazioneLavoriInCorsoPrec;
	}
	public String getAmmortamentiImmPrec() {
		return ammortamentiImmPrec;
	}
	public String getAmmortamentiMatPrec() {
		return ammortamentiMatPrec;
	}
	public String getTotaleCostiProduzionePrec() {
		return totaleCostiProduzionePrec;
	}
	public String getProventiFinanziariPrec() {
		return proventiFinanziariPrec;
	}
	public String getInteressiPassiviPrec() {
		return interessiPassiviPrec;
	}
	public String getProventiGestioneAccessoriaPrec() {
		return proventiGestioneAccessoriaPrec;
	}
	public String getOneriGestioneAccessoriaPrec() {
		return oneriGestioneAccessoriaPrec;
	}
	public String getEbitdaPrec() {
		return ebitdaPrec;
	}
	public String getEbitPrec() {
		return ebitPrec;
	}
	public String getIndiceRotazionePrec() {
		return indiceRotazionePrec;
	}
	public String getDsoPrec() {
		return dsoPrec;
	}
	public String getDpoPrec() {
		return dpoPrec;
	}
	public String getUlaPrec() {
		return ulaPrec;
	}
	public void setSpeseRSPrec(String speseRSPrec) {
		this.speseRSPrec = speseRSPrec;
	}
	public void setTotCreditiVsClientiPrec(String totCreditiVsClientiPrec) {
		this.totCreditiVsClientiPrec = totCreditiVsClientiPrec;
	}
	public void setCreditiVsClientiPrec(String creditiVsClientiPrec) {
		this.creditiVsClientiPrec = creditiVsClientiPrec;
	}
	public void setDebitiVsFornitoriPrec(String debitiVsFornitoriPrec) {
		this.debitiVsFornitoriPrec = debitiVsFornitoriPrec;
	}
	public void setCreditiCommScadPrec(String creditiCommScadPrec) {
		this.creditiCommScadPrec = creditiCommScadPrec;
	}
	public void setDisponibilitaLiquidePrec(String disponibilitaLiquidePrec) {
		this.disponibilitaLiquidePrec = disponibilitaLiquidePrec;
	}
	public void setTotaleBilancioPrec(String totaleBilancioPrec) {
		this.totaleBilancioPrec = totaleBilancioPrec;
	}
	public void setTotalePatrimonioPrec(String totalePatrimonioPrec) {
		this.totalePatrimonioPrec = totalePatrimonioPrec;
	}
	public void setDebitiSociPrec(String debitiSociPrec) {
		this.debitiSociPrec = debitiSociPrec;
	}
	public void setDebitiBanchePrec(String debitiBanchePrec) {
		this.debitiBanchePrec = debitiBanchePrec;
	}
	public void setDebitiFornScadPrec(String debitiFornScadPrec) {
		this.debitiFornScadPrec = debitiFornScadPrec;
	}
	public void setDebitiImpreseCollegatePrec(String debitiImpreseCollegatePrec) {
		this.debitiImpreseCollegatePrec = debitiImpreseCollegatePrec;
	}
	public void setDebitiControllantiPrec(String debitiControllantiPrec) {
		this.debitiControllantiPrec = debitiControllantiPrec;
	}
	public void setDebitiTributariPrec(String debitiTributariPrec) {
		this.debitiTributariPrec = debitiTributariPrec;
	}
	public void setDebitiTributariScadPrec(String debitiTributariScadPrec) {
		this.debitiTributariScadPrec = debitiTributariScadPrec;
	}
	public void setRicaviPrec(String ricaviPrec) {
		this.ricaviPrec = ricaviPrec;
	}
	public void setTotaleValoreProduzionePrec(String totaleValoreProduzionePrec) {
		this.totaleValoreProduzionePrec = totaleValoreProduzionePrec;
	}
	public void setVariazioneLavoriInCorsoPrec(String variazioneLavoriInCorsoPrec) {
		this.variazioneLavoriInCorsoPrec = variazioneLavoriInCorsoPrec;
	}
	public void setAmmortamentiImmPrec(String ammortamentiImmPrec) {
		this.ammortamentiImmPrec = ammortamentiImmPrec;
	}
	public void setAmmortamentiMatPrec(String ammortamentiMatPrec) {
		this.ammortamentiMatPrec = ammortamentiMatPrec;
	}
	public void setTotaleCostiProduzionePrec(String totaleCostiProduzionePrec) {
		this.totaleCostiProduzionePrec = totaleCostiProduzionePrec;
	}
	public void setProventiFinanziariPrec(String proventiFinanziariPrec) {
		this.proventiFinanziariPrec = proventiFinanziariPrec;
	}
	public void setInteressiPassiviPrec(String interessiPassiviPrec) {
		this.interessiPassiviPrec = interessiPassiviPrec;
	}
	public void setProventiGestioneAccessoriaPrec(
			String proventiGestioneAccessoriaPrec) {
		this.proventiGestioneAccessoriaPrec = proventiGestioneAccessoriaPrec;
	}
	public void setOneriGestioneAccessoriaPrec(String oneriGestioneAccessoriaPrec) {
		this.oneriGestioneAccessoriaPrec = oneriGestioneAccessoriaPrec;
	}
	public void setEbitdaPrec(String ebitdaPrec) {
		this.ebitdaPrec = ebitdaPrec;
	}
	public void setEbitPrec(String ebitPrec) {
		this.ebitPrec = ebitPrec;
	}
	public void setIndiceRotazionePrec(String indiceRotazionePrec) {
		this.indiceRotazionePrec = indiceRotazionePrec;
	}
	public void setDsoPrec(String dsoPrec) {
		this.dsoPrec = dsoPrec;
	}
	public void setDpoPrec(String dpoPrec) {
		this.dpoPrec = dpoPrec;
	}
	public void setUlaPrec(String ulaPrec) {
		this.ulaPrec = ulaPrec;
	}
	
}
