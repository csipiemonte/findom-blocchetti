/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.ipa;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.io.Serializable;

/**
 * Classe non piu' usata da quando e' stata aggiunta alla ricerca su IPA delle unita' organizzative (o dipartimenti o uffici)
 * la ricerca su findom_d_dipartimenti, nel caso in cui la ricerca su IPA non dà risultati.  
 * @author mauro.bottero
 *
 */
public class DipartimentoIpaVO extends CommonalityVO{

	// Indice Pubblica Amministrazione
	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "codiceIPA")
	String codiceIPA;
	
	@MapTo(target = INHERIT, name = "ou")
	String ou;
	
	@MapTo(target = INHERIT, name = "codiceUnivocoUO")
	String codiceUnivocoUO;
	
	@MapTo(target = INHERIT, name = "codiceUO")
	String codiceUO;
	
	@MapTo(target = INHERIT, name = "descrizione")
	String descrizione;

	public String toString(){
		StringBuffer sb = new StringBuffer();
	
		sb.append("ipa=[codiceIPA="+codiceIPA);
		sb.append(",ou="+ou);
		sb.append(",codiceUnivocoUO="+codiceUnivocoUO);
		sb.append(",codiceUO="+codiceUO);
		sb.append(",descrizione="+descrizione);
		
		return  sb.toString();
	}
	
	public String getCodiceIPA() {
		return codiceIPA;
	}
	public void setCodiceIPA(String codiceIPA) {
		this.codiceIPA = codiceIPA;
	}
//	public String getCodiceFiscaleAmm() {
//		return codiceFiscaleAmm;
//	}
//	public void setCodiceFiscaleAmm(String codiceFiscaleAmm) {
//		this.codiceFiscaleAmm = codiceFiscaleAmm;
//	}
//	public String getCognomeResp() {
//		return cognomeResp;
//	}
//	public void setCognomeResp(String cognomeResp) {
//		this.cognomeResp = cognomeResp;
//	}
//	public String getContatti() {
//		return contatti;
//	}
//	public void setContatti(String contatti) {
//		this.contatti = contatti;
//	}
//	public String getDataVerifica() {
//		return dataVerifica;
//	}
//	public void setDataVerifica(String dataVerifica) {
//		this.dataVerifica = dataVerifica;
//	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public String getCodiceUnivocoUO() {
		return codiceUnivocoUO;
	}

	public void setCodiceUnivocoUO(String codiceUnivocoUO) {
		this.codiceUnivocoUO = codiceUnivocoUO;
	}

	public String getCodiceUO() {
		return codiceUO;
	}

	public void setCodiceUO(String codiceUO) {
		this.codiceUO = codiceUO;
	}
//	public String getMacroTipoAmm() {
//		return macroTipoAmm;
//	}
//	public void setMacroTipoAmm(String macroTipoAmm) {
//		this.macroTipoAmm = macroTipoAmm;
//	}
//	public String getMail() {
//		return mail;
//	}
//	public void setMail(String mail) {
//		this.mail = mail;
//	}
//	public String getElle() {
//		return elle;
//	}
//	public void setElle(String elle) {
//		this.elle = elle;
//	}
//	public String getNomeResp() {
//		return nomeResp;
//	}
//	public void setNomeResp(String nomeResp) {
//		this.nomeResp = nomeResp;
//	}
//	public String getCodicePostale() {
//		return codicePostale;
//	}
//	public void setCodicePostale(String codicePostale) {
//		this.codicePostale = codicePostale;
//	}
//	public String getProvincia() {
//		return provincia;
//	}
//	public void setProvincia(String provincia) {
//		this.provincia = provincia;
//	}
//	public String getRegione() {
//		return regione;
//	}
//	public void setRegione(String regione) {
//		this.regione = regione;
//	}
//	public String getSitoIstituzionale() {
//		return sitoIstituzionale;
//	}
//	public void setSitoIstituzionale(String sitoIstituzionale) {
//		this.sitoIstituzionale = sitoIstituzionale;
//	}
//	public String getSt() {
//		return st;
//	}
//	public void setSt(String st) {
//		this.st = st;
//	}
//	public String getVia() {
//		return via;
//	}
//	public void setVia(String via) {
//		this.via = via;
//	}
//	public String getTipoAmm() {
//		return tipoAmm;
//	}
//	public void setTipoAmm(String tipoAmm) {
//		this.tipoAmm = tipoAmm;
//	}
//	public String getTitoloResp() {
//		return titoloResp;
//	}
//	public void setTitoloResp(String titoloResp) {
//		this.titoloResp = titoloResp;
//	}

//	public IpaVO(String codiceIPA, String descrizione) {
//		super();
//		this.codiceIPA = codiceIPA;
//		this.descrizione = descrizione;
//	}
//	public IpaVO() {
//		super();
//		
//	}
	
			 
}
