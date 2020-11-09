/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettaglioBilancioAziendaAAEPVO extends CommonalityVO {


	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "annoAttuale")
	String annoAttuale;
	@MapTo(target = INHERIT, name = "annoPrecedente")
	String annoPrecedente;
	@MapTo(target = INHERIT, name = "livelloIndentazione")
	String livelloIndentazione;
	@MapTo(target = INHERIT, name = "tipoRigo")
	String tipoRigo;
	@MapTo(target = INHERIT, name = "voce")
	String voce;
	
	
	public String getAnnoAttuale() {
		return annoAttuale;
	}
	public String getAnnoPrecedente() {
		return annoPrecedente;
	}
	public String getLivelloIndentazione() {
		return livelloIndentazione;
	}
	public String getTipoRigo() {
		return tipoRigo;
	}
	public String getVoce() {
		return voce;
	}
	public void setAnnoAttuale(String annoAttuale) {
		this.annoAttuale = annoAttuale;
	}
	public void setAnnoPrecedente(String annoPrecedente) {
		this.annoPrecedente = annoPrecedente;
	}
	public void setLivelloIndentazione(String livelloIndentazione) {
		this.livelloIndentazione = livelloIndentazione;
	}
	public void setTipoRigo(String tipoRigo) {
		this.tipoRigo = tipoRigo;
	}
	public void setVoce(String voce) {
		this.voce = voce;
	}
	
}
