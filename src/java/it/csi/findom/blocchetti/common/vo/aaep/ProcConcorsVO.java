/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class ProcConcorsVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT) String codAtto;
    @MapTo(target = INHERIT) String codLiquidazione;
    @MapTo(target = INHERIT) String dataAperturaProc;
    @MapTo(target = INHERIT) String dataChiusuraLiquidaz;
    @MapTo(target = INHERIT) String dataEsecConcordPrevent;
    @MapTo(target = INHERIT) String dataFineLiquidaz;
    @MapTo(target = INHERIT) String dataRegistroAtto;
    @MapTo(target = INHERIT) String dataRevocalLiquidaz;
    @MapTo(target = INHERIT) String descIndicatEsecutAtto;
    @MapTo(target = INHERIT) String descrAltreIndicazioni;
    @MapTo(target = INHERIT) String descrCodAtto;
    @MapTo(target = INHERIT) String descrNotaio;
    @MapTo(target = INHERIT) String descrTribunale;
    @MapTo(target = INHERIT) String idAAEPAzienda;
    @MapTo(target = INHERIT) String idAAEPFonteDato;
    @MapTo(target = INHERIT) String localRegistroAtto;
    @MapTo(target = INHERIT) String numRestistrAtto;
    @MapTo(target = INHERIT) String progrLiquidazione;
    @MapTo(target = INHERIT) String siglaProvRegAtto;
	
    public String getCodAtto() {
		return codAtto;
	}
	public void setCodAtto(String codAtto) {
		this.codAtto = codAtto;
	}
	public String getCodLiquidazione() {
		return codLiquidazione;
	}
	public void setCodLiquidazione(String codLiquidazione) {
		this.codLiquidazione = codLiquidazione;
	}
	public String getDataAperturaProc() {
		return dataAperturaProc;
	}
	public void setDataAperturaProc(String dataAperturaProc) {
		this.dataAperturaProc = dataAperturaProc;
	}
	public String getDataChiusuraLiquidaz() {
		return dataChiusuraLiquidaz;
	}
	public void setDataChiusuraLiquidaz(String dataChiusuraLiquidaz) {
		this.dataChiusuraLiquidaz = dataChiusuraLiquidaz;
	}
	public String getDataEsecConcordPrevent() {
		return dataEsecConcordPrevent;
	}
	public void setDataEsecConcordPrevent(String dataEsecConcordPrevent) {
		this.dataEsecConcordPrevent = dataEsecConcordPrevent;
	}
	public String getDataFineLiquidaz() {
		return dataFineLiquidaz;
	}
	public void setDataFineLiquidaz(String dataFineLiquidaz) {
		this.dataFineLiquidaz = dataFineLiquidaz;
	}
	public String getDataRegistroAtto() {
		return dataRegistroAtto;
	}
	public void setDataRegistroAtto(String dataRegistroAtto) {
		this.dataRegistroAtto = dataRegistroAtto;
	}
	public String getDataRevocalLiquidaz() {
		return dataRevocalLiquidaz;
	}
	public void setDataRevocalLiquidaz(String dataRevocalLiquidaz) {
		this.dataRevocalLiquidaz = dataRevocalLiquidaz;
	}
	public String getDescIndicatEsecutAtto() {
		return descIndicatEsecutAtto;
	}
	public void setDescIndicatEsecutAtto(String descIndicatEsecutAtto) {
		this.descIndicatEsecutAtto = descIndicatEsecutAtto;
	}
	public String getDescrAltreIndicazioni() {
		return descrAltreIndicazioni;
	}
	public void setDescrAltreIndicazioni(String descrAltreIndicazioni) {
		this.descrAltreIndicazioni = descrAltreIndicazioni;
	}
	public String getDescrCodAtto() {
		return descrCodAtto;
	}
	public void setDescrCodAtto(String descrCodAtto) {
		this.descrCodAtto = descrCodAtto;
	}
	public String getDescrNotaio() {
		return descrNotaio;
	}
	public void setDescrNotaio(String descrNotaio) {
		this.descrNotaio = descrNotaio;
	}
	public String getDescrTribunale() {
		return descrTribunale;
	}
	public void setDescrTribunale(String descrTribunale) {
		this.descrTribunale = descrTribunale;
	}
	public String getIdAAEPAzienda() {
		return idAAEPAzienda;
	}
	public void setIdAAEPAzienda(String idAAEPAzienda) {
		this.idAAEPAzienda = idAAEPAzienda;
	}
	public String getIdAAEPFonteDato() {
		return idAAEPFonteDato;
	}
	public void setIdAAEPFonteDato(String idAAEPFonteDato) {
		this.idAAEPFonteDato = idAAEPFonteDato;
	}
	public String getLocalRegistroAtto() {
		return localRegistroAtto;
	}
	public void setLocalRegistroAtto(String localRegistroAtto) {
		this.localRegistroAtto = localRegistroAtto;
	}
	public String getNumRestistrAtto() {
		return numRestistrAtto;
	}
	public void setNumRestistrAtto(String numRestistrAtto) {
		this.numRestistrAtto = numRestistrAtto;
	}
	public String getProgrLiquidazione() {
		return progrLiquidazione;
	}
	public void setProgrLiquidazione(String progrLiquidazione) {
		this.progrLiquidazione = progrLiquidazione;
	}
	public String getSiglaProvRegAtto() {
		return siglaProvRegAtto;
	}
	public void setSiglaProvRegAtto(String siglaProvRegAtto) {
		this.siglaProvRegAtto = siglaProvRegAtto;
	}

}
