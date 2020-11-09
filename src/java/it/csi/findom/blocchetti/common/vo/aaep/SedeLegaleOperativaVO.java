/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class SedeLegaleOperativaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT) List<AttivitaEconomicaVO> ateco;
    @MapTo(target = INHERIT) String codCausaleCessazione;
    @MapTo(target = INHERIT) String codSettore;
    @MapTo(target = INHERIT) ContattiVO contatti;
    @MapTo(target = INHERIT) String dataAggiornam;
    @MapTo(target = INHERIT) String dataCessazione;
    @MapTo(target = INHERIT) String dataInizioAttivita;
    @MapTo(target = INHERIT) String dataInizioValidita;
    @MapTo(target = INHERIT) String dataNumeroDipendenti;
    @MapTo(target = INHERIT) String denominazione;
    @MapTo(target = INHERIT) String descrCausaleCessazione;
    @MapTo(target = INHERIT) String descrSettore;
    @MapTo(target = INHERIT) String descrTipoSede;
    @MapTo(target = INHERIT) String codiceAteco2007;
    @MapTo(target = INHERIT) String descrizioneAteco2007;
    @MapTo(target = INHERIT) it.csi.aaep.aaeporch.business.TipologiaFonte fonteDato; // le classi ENUM sono serializzabili
    @MapTo(target = INHERIT) String idAzienda;
    @MapTo(target = INHERIT) String idSede;
    @MapTo(target = INHERIT) String numeroDipendenti;
    @MapTo(target = INHERIT) String riferimento;
    @MapTo(target = INHERIT) it.csi.aaep.aaeporch.business.TipologiaSede tipoSede; // le classi ENUM sono serializzabili
    @MapTo(target = INHERIT) UbicazioneVO ubicazione;
    
    // : Jira 901 - inizio
    @MapTo(target = INHERIT) String sedeLegaleOperativa;
    @MapTo(target = INHERIT) String sedeOperativa;
    @MapTo(target = INHERIT) String sedeLegale;
    
    
    public String getSedeLegaleOperativa() {
		return sedeLegaleOperativa;
	}
	public void setSedeLegaleOperativa(String sedeLegaleOperativa) {
		this.sedeLegaleOperativa = sedeLegaleOperativa;
	}
	public String getSedeOperativa() {
		return sedeOperativa;
	}
	public void setSedeOperativa(String sedeOperativa) {
		this.sedeOperativa = sedeOperativa;
	}
	public String getSedeLegale() {
		return sedeLegale;
	}
	public void setSedeLegale(String sedeLegale) {
		this.sedeLegale = sedeLegale;
	}
	// : Jira 901 - fine
	public List<AttivitaEconomicaVO> getAteco() {
		return ateco;
	}
	public void setAteco(List<AttivitaEconomicaVO> ateco) {
		this.ateco = ateco;
	}
	public String getCodCausaleCessazione() {
		return codCausaleCessazione;
	}
	public void setCodCausaleCessazione(String codCausaleCessazione) {
		this.codCausaleCessazione = codCausaleCessazione;
	}
	public String getCodSettore() {
		return codSettore;
	}
	public void setCodSettore(String codSettore) {
		this.codSettore = codSettore;
	}
	public ContattiVO getContatti() {
		return contatti;
	}
	public void setContatti(ContattiVO contatti) {
		this.contatti = contatti;
	}
	public String getDataAggiornam() {
		return dataAggiornam;
	}
	public void setDataAggiornam(String dataAggiornam) {
		this.dataAggiornam = dataAggiornam;
	}
	public String getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(String dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public String getDataInizioAttivita() {
		return dataInizioAttivita;
	}
	public void setDataInizioAttivita(String dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDataNumeroDipendenti() {
		return dataNumeroDipendenti;
	}
	public void setDataNumeroDipendenti(String dataNumeroDipendenti) {
		this.dataNumeroDipendenti = dataNumeroDipendenti;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getDescrCausaleCessazione() {
		return descrCausaleCessazione;
	}
	public void setDescrCausaleCessazione(String descrCausaleCessazione) {
		this.descrCausaleCessazione = descrCausaleCessazione;
	}
	public String getDescrSettore() {
		return descrSettore;
	}
	public void setDescrSettore(String descrSettore) {
		this.descrSettore = descrSettore;
	}
	public String getDescrTipoSede() {
		return descrTipoSede;
	}
	public void setDescrTipoSede(String descrTipoSede) {
		this.descrTipoSede = descrTipoSede;
	}
	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}
	public String getIdSede() {
		return idSede;
	}
	public void setIdSede(String idSede) {
		this.idSede = idSede;
	}
	public String getNumeroDipendenti() {
		return numeroDipendenti;
	}
	public void setNumeroDipendenti(String numeroDipendenti) {
		this.numeroDipendenti = numeroDipendenti;
	}
	public String getRiferimento() {
		return riferimento;
	}
	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}
	public UbicazioneVO getUbicazione() {
		return ubicazione;
	}
	public void setUbicazione(UbicazioneVO ubicazione) {
		this.ubicazione = ubicazione;
	}
	public it.csi.aaep.aaeporch.business.TipologiaFonte getFonteDato() {
		return fonteDato;
	}
	public void setFonteDato(it.csi.aaep.aaeporch.business.TipologiaFonte fonteDato) {
		this.fonteDato = fonteDato;
	}
	public it.csi.aaep.aaeporch.business.TipologiaSede getTipoSede() {
		return tipoSede;
	}
	public void setTipoSede(it.csi.aaep.aaeporch.business.TipologiaSede tipoSede) {
		this.tipoSede = tipoSede;
	}
	public String getDescrizioneAteco2007() {
		return descrizioneAteco2007;
	}
	public void setDescrizioneAteco2007(String descrizioneAteco2007) {
		this.descrizioneAteco2007 = descrizioneAteco2007;
	}
	public String getCodiceAteco2007() {
		return codiceAteco2007;
	}
	public void setCodiceAteco2007(String codiceAteco2007) {
		this.codiceAteco2007 = codiceAteco2007;
	}

}
