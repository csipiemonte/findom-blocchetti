/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.riferimenti;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PersonaImpresaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT, name = "provincia")
	String provincia;
	
	@MapTo(target = INHERIT, name = "codiceFiscale")
	String codiceFiscale;
	@MapTo(target = INHERIT, name = "cognome")
	String cognome;
	@MapTo(target = INHERIT, name = "nome")
	String nome;
	@MapTo(target = INHERIT, name = "telefono")
	String telefono;
	@MapTo(target = INHERIT, name = "email")
	String email;
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
