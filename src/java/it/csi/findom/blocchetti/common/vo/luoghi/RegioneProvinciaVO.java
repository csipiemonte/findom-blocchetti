/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.luoghi;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RegioneProvinciaVO extends CommonalityVO {

	@MapTo(target = INHERIT, name = "regione")
	String regione;

	@MapTo(target = INHERIT, name = "idProv")
	String idProv;
	
	@MapTo(target = INHERIT, name = "descrizione")
	String descrizione;

	@MapTo(target = INHERIT, name = "sigla")
	String sigla;

	

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getIdProv() {
		return idProv;
	}

	public void setIdProv(String idProv) {
		this.idProv = idProv;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public String toString() {
		return "ProvinciaVO [regione=" + regione + ", idProv=" + idProv + ", descrizione=" + descrizione + ", sigla=" + sigla + "]";
	}

}
