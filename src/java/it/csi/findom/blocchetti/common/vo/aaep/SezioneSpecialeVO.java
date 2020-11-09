/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SezioneSpecialeVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT) String codAlbo;
    @MapTo(target = INHERIT) String codSezione;
    @MapTo(target = INHERIT) String codiceSezSpec;
    @MapTo(target = INHERIT) String dataFine;
    @MapTo(target = INHERIT) String dataInizio;
    @MapTo(target = INHERIT) String flColtDir;
    @MapTo(target = INHERIT) String idAzienda;
    @MapTo(target = INHERIT) String idFonteDato;
    @MapTo(target = INHERIT) String idSezioneSpeciale;
    
	public String getCodAlbo() {
		return codAlbo;
	}
	public void setCodAlbo(String codAlbo) {
		this.codAlbo = codAlbo;
	}
	public String getCodSezione() {
		return codSezione;
	}
	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}
	public String getCodiceSezSpec() {
		return codiceSezSpec;
	}
	public void setCodiceSezSpec(String codiceSezSpec) {
		this.codiceSezSpec = codiceSezSpec;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getFlColtDir() {
		return flColtDir;
	}
	public void setFlColtDir(String flColtDir) {
		this.flColtDir = flColtDir;
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
	public String getIdSezioneSpeciale() {
		return idSezioneSpeciale;
	}
	public void setIdSezioneSpeciale(String idSezioneSpeciale) {
		this.idSezioneSpeciale = idSezioneSpeciale;
	}
}
