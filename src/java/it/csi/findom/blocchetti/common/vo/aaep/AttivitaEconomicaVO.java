/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AttivitaEconomicaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT) String annoDiRiferimento;
	@MapTo(target = INHERIT) String codImportanzaAA;
	@MapTo(target = INHERIT) String codImportanzaRI;
	@MapTo(target = INHERIT) String codiceATECO;
	@MapTo(target = INHERIT) String dataCessazione;
	@MapTo(target = INHERIT) String dataInizio;
	@MapTo(target = INHERIT) String descrImportanzaAA;
	@MapTo(target = INHERIT) String descrImportanzaRI;
	@MapTo(target = INHERIT) String descrizione;
	
	public String getAnnoDiRiferimento() {
		return annoDiRiferimento;
	}
	public void setAnnoDiRiferimento(String annoDiRiferimento) {
		this.annoDiRiferimento = annoDiRiferimento;
	}
	public String getCodImportanzaAA() {
		return codImportanzaAA;
	}
	public void setCodImportanzaAA(String codImportanzaAA) {
		this.codImportanzaAA = codImportanzaAA;
	}
	public String getCodImportanzaRI() {
		return codImportanzaRI;
	}
	public void setCodImportanzaRI(String codImportanzaRI) {
		this.codImportanzaRI = codImportanzaRI;
	}
	public String getCodiceATECO() {
		return codiceATECO;
	}
	public void setCodiceATECO(String codiceATECO) {
		this.codiceATECO = codiceATECO;
	}
	public String getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(String dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getDescrImportanzaAA() {
		return descrImportanzaAA;
	}
	public void setDescrImportanzaAA(String descrImportanzaAA) {
		this.descrImportanzaAA = descrImportanzaAA;
	}
	public String getDescrImportanzaRI() {
		return descrImportanzaRI;
	}
	public void setDescrImportanzaRI(String descrImportanzaRI) {
		this.descrImportanzaRI = descrImportanzaRI;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
    
}
