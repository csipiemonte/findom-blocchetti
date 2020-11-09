/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CaricaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
    @MapTo(target = INHERIT) String codCarica;
    @MapTo(target = INHERIT) String codDurataCarica;
    @MapTo(target = INHERIT) String codFiscaleAzienda;
    @MapTo(target = INHERIT) String codFiscalePersona;
    @MapTo(target = INHERIT) String dataFineCarica;
    @MapTo(target = INHERIT) String dataInizioCarica;
    @MapTo(target = INHERIT) String dataPresentazCarica;
    @MapTo(target = INHERIT) String descrAzienda;
    @MapTo(target = INHERIT) String descrCarica;
    @MapTo(target = INHERIT) String descrDurataCarica;
    @MapTo(target = INHERIT) String flagRappresentanteLegale;
    @MapTo(target = INHERIT) String idAzienda;
    @MapTo(target = INHERIT) String idFonteDato;
    @MapTo(target = INHERIT) String numAnniEsercCarica;
    @MapTo(target = INHERIT) String progrCarica;
    @MapTo(target = INHERIT) String progrPersona;
    
	public String getCodCarica() {
		return codCarica;
	}
	public void setCodCarica(String codCarica) {
		this.codCarica = codCarica;
	}
	public String getCodDurataCarica() {
		return codDurataCarica;
	}
	public void setCodDurataCarica(String codDurataCarica) {
		this.codDurataCarica = codDurataCarica;
	}
	public String getCodFiscaleAzienda() {
		return codFiscaleAzienda;
	}
	public void setCodFiscaleAzienda(String codFiscaleAzienda) {
		this.codFiscaleAzienda = codFiscaleAzienda;
	}
	public String getCodFiscalePersona() {
		return codFiscalePersona;
	}
	public void setCodFiscalePersona(String codFiscalePersona) {
		this.codFiscalePersona = codFiscalePersona;
	}
	public String getDataFineCarica() {
		return dataFineCarica;
	}
	public void setDataFineCarica(String dataFineCarica) {
		this.dataFineCarica = dataFineCarica;
	}
	public String getDataInizioCarica() {
		return dataInizioCarica;
	}
	public void setDataInizioCarica(String dataInizioCarica) {
		this.dataInizioCarica = dataInizioCarica;
	}
	public String getDataPresentazCarica() {
		return dataPresentazCarica;
	}
	public void setDataPresentazCarica(String dataPresentazCarica) {
		this.dataPresentazCarica = dataPresentazCarica;
	}
	public String getDescrAzienda() {
		return descrAzienda;
	}
	public void setDescrAzienda(String descrAzienda) {
		this.descrAzienda = descrAzienda;
	}
	public String getDescrCarica() {
		return descrCarica;
	}
	public void setDescrCarica(String descrCarica) {
		this.descrCarica = descrCarica;
	}
	public String getDescrDurataCarica() {
		return descrDurataCarica;
	}
	public void setDescrDurataCarica(String descrDurataCarica) {
		this.descrDurataCarica = descrDurataCarica;
	}
	public String getFlagRappresentanteLegale() {
		return flagRappresentanteLegale;
	}
	public void setFlagRappresentanteLegale(String flagRappresentanteLegale) {
		this.flagRappresentanteLegale = flagRappresentanteLegale;
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
	public String getNumAnniEsercCarica() {
		return numAnniEsercCarica;
	}
	public void setNumAnniEsercCarica(String numAnniEsercCarica) {
		this.numAnniEsercCarica = numAnniEsercCarica;
	}
	public String getProgrCarica() {
		return progrCarica;
	}
	public void setProgrCarica(String progrCarica) {
		this.progrCarica = progrCarica;
	}
	public String getProgrPersona() {
		return progrPersona;
	}
	public void setProgrPersona(String progrPersona) {
		this.progrPersona = progrPersona;
	}

}
