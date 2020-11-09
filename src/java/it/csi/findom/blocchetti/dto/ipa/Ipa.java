/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.dto.ipa;

import java.io.Serializable;

public class Ipa implements Serializable{

	// Indice Pubblica Amministrazione
	private static final long serialVersionUID = 1L;
	
	String codiceIPA;
	String codiceFiscaleAmm;
	String cognomeResp;
	String contatti;
	String dataVerifica; // yyyy-mm-dd
	String descrizione;
	String macroTipoAmm;
	String mail;
	String elle; //l (nome esteso della provincia??)
	String nomeResp;
	String codicePostale;
	String provincia;
	String regione;
	String sitoIstituzionale;
	String st;
	String via; //street
	String tipoAmm;
	String titoloResp;
	

	/*
	codice IPA o=atc_no
	 codiceFiscaleAmm=00120490032
	 cognomeResp=Martelli
	 contatti=Direzione@atc.novara.it#altro
	 dataVerifica=2015-03-17
	 description=Agenzia Territoriale per La Casa del Piemonte Nord
	 l(elle)=Novara
	 macroTipoAmm=PA
	 mail=atcnovco@legalmail.it
	 nomeResp=Fabio
	 postalCode=28100
	 provincia=NO
	 regione=Piemonte
	 sitoIstituzionale=www.atc.novara.it
	 st=ACCREDITATA
	 street=Via Boschi, 2
	 tipoAmm=L39
	 titoloResp=Direttore Facente Funzioni
	 */
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
	
		sb.append("ipa=[codiceIPA="+codiceIPA);
		sb.append(",codiceFiscaleAmm="+codiceFiscaleAmm);
		sb.append(",titoloResp="+titoloResp);
		sb.append(",cognomeResp="+cognomeResp);
		sb.append(",nomeResp="+nomeResp);
		sb.append(",contatti="+contatti);
		sb.append(",dataVerifica="+dataVerifica);
		sb.append(",descrizione="+descrizione);
		sb.append(",macroTipoAmm="+macroTipoAmm);
		sb.append(",mail="+mail);
		sb.append(",via="+via);
		sb.append(",codicePostale="+codicePostale);
		sb.append(",provincia="+provincia);
		sb.append(",elle="+elle);
		sb.append(",regione="+regione);
		sb.append(",st="+st);
		sb.append(",sitoIstituzionale="+sitoIstituzionale);
		sb.append(",tipoAmm="+tipoAmm);
		
		return  sb.toString();
	}
	
	public String getCodiceIPA() {
		return codiceIPA;
	}
	public void setCodiceIPA(String codiceIPA) {
		this.codiceIPA = codiceIPA;
	}
	public String getCodiceFiscaleAmm() {
		return codiceFiscaleAmm;
	}
	public void setCodiceFiscaleAmm(String codiceFiscaleAmm) {
		this.codiceFiscaleAmm = codiceFiscaleAmm;
	}
	public String getCognomeResp() {
		return cognomeResp;
	}
	public void setCognomeResp(String cognomeResp) {
		this.cognomeResp = cognomeResp;
	}
	public String getContatti() {
		return contatti;
	}
	public void setContatti(String contatti) {
		this.contatti = contatti;
	}
	public String getDataVerifica() {
		return dataVerifica;
	}
	public void setDataVerifica(String dataVerifica) {
		this.dataVerifica = dataVerifica;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getMacroTipoAmm() {
		return macroTipoAmm;
	}
	public void setMacroTipoAmm(String macroTipoAmm) {
		this.macroTipoAmm = macroTipoAmm;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getElle() {
		return elle;
	}
	public void setElle(String elle) {
		this.elle = elle;
	}
	public String getNomeResp() {
		return nomeResp;
	}
	public void setNomeResp(String nomeResp) {
		this.nomeResp = nomeResp;
	}
	public String getCodicePostale() {
		return codicePostale;
	}
	public void setCodicePostale(String codicePostale) {
		this.codicePostale = codicePostale;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getRegione() {
		return regione;
	}
	public void setRegione(String regione) {
		this.regione = regione;
	}
	public String getSitoIstituzionale() {
		return sitoIstituzionale;
	}
	public void setSitoIstituzionale(String sitoIstituzionale) {
		this.sitoIstituzionale = sitoIstituzionale;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getTipoAmm() {
		return tipoAmm;
	}
	public void setTipoAmm(String tipoAmm) {
		this.tipoAmm = tipoAmm;
	}
	public String getTitoloResp() {
		return titoloResp;
	}
	public void setTitoloResp(String titoloResp) {
		this.titoloResp = titoloResp;
	}
	
			 
}
