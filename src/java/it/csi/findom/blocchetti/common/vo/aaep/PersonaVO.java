/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class PersonaVO extends CommonalityVO{

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT) String codiceFiscale;
    @MapTo(target = INHERIT) String cognome;
    @MapTo(target = INHERIT) String descrTipoPersona;
    @MapTo(target = INHERIT) String idAzienda;
    @MapTo(target = INHERIT) String idFonteDato;
    @MapTo(target = INHERIT) String idPersona;
    @MapTo(target = INHERIT) List<CaricaVO> listaCariche;
    @MapTo(target = INHERIT) String nome;
    @MapTo(target = INHERIT) String tipoPersona;
    
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
	public String getDescrTipoPersona() {
		return descrTipoPersona;
	}
	public void setDescrTipoPersona(String descrTipoPersona) {
		this.descrTipoPersona = descrTipoPersona;
	}
	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}
	public String getIdFonteDato() {
		return idFonteDato;
	}
	public void setIdFonteDato(String idFonteDato) {
		this.idFonteDato = idFonteDato;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public List<CaricaVO> getListaCariche() {
		return listaCariche;
	}
	public void setListaCariche(List<CaricaVO> listaCariche) {
		this.listaCariche = listaCariche;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
    
}
