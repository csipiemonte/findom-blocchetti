/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.io.Serializable;
import java.util.List;

public class ImpresaVO extends CommonalityVO implements Serializable{

	private static final long serialVersionUID = 1L;

	// attributi di it.csi.aaep.aaeporch.business.Impresa;
	@MapTo(target = INHERIT) CessazioneVO cessazione;
	@MapTo(target = INHERIT) String codNaturaGiuridica;
	@MapTo(target = INHERIT) String codiceFiscale;
	@MapTo(target = INHERIT) List<AttivitaEconomicaVO> codiciATECO;
	@MapTo(target = INHERIT) String dataAggiornamento;
	@MapTo(target = INHERIT) String dataInizioValidita;
	@MapTo(target = INHERIT) String descrFonte;
	@MapTo(target = INHERIT) String descrNaturaGiuridica;
	@MapTo(target = INHERIT) DettagliAlboArtigianoVO dettagliAlboArtigiano;
    @MapTo(target = INHERIT) DettagliCameraCommercioVO dettagliCameraCommercio;
    @MapTo(target = INHERIT) String idAzienda;
    @MapTo(target = INHERIT) String idFonte;
    @MapTo(target = INHERIT) String idNaturaGiuridica;
    @MapTo(target = INHERIT) String idSede;
    @MapTo(target = INHERIT) String partitaIva;
    @MapTo(target = INHERIT) String postaElettronicaCertificata;
    @MapTo(target = INHERIT) String ragioneSociale;
    
    /** Jira: 1707: dataIscrizioneRegistroImprese */
    @MapTo(target = INHERIT) String dataIscrizioneRegistroImprese;
    
    @MapTo(target = INHERIT) List<SedeVO> sedi;
	
	// attributi di it.csi.aaep.aaeporch.business.ImpresaINFOC;
	@MapTo(target = INHERIT) String annoDenunciaAddetti;
	@MapTo(target = INHERIT) CessazioneVO cessazioneFunzioneSedeLegale;
	@MapTo(target = INHERIT) String codFonte;
	@MapTo(target = INHERIT) DatoCostitutivoVO datoCostitutivo;
	@MapTo(target = INHERIT) String descrIndicStatoAttiv;
	@MapTo(target = INHERIT) String descrIndicTrasfSede;
	@MapTo(target = INHERIT) String flagLocalizzazionePiemonte;
	@MapTo(target = INHERIT) String flgAggiornamento;
	@MapTo(target = INHERIT) String impresaCessata;
	@MapTo(target = INHERIT) String indicStatoAttiv;
	@MapTo(target = INHERIT) String indicTrasfSede;
	@MapTo(target = INHERIT) List<PersonaVO> listaPersone;
	@MapTo(target = INHERIT) List<ProcConcorsVO> listaProcConcors;
	@MapTo(target = INHERIT) List<SezioneSpecialeVO> listaSezSpecInfoc;
	@MapTo(target = INHERIT) String numAddettiFam;
	@MapTo(target = INHERIT) String numAddettiSubord;
	@MapTo(target = INHERIT) List<String> testoOggettoSociale;
	
	public CessazioneVO getCessazione() {
		return cessazione;
	}
	public void setCessazione(CessazioneVO cessazione) {
		this.cessazione = cessazione;
	}
	public String getCodNaturaGiuridica() {
		return codNaturaGiuridica;
	}
	public void setCodNaturaGiuridica(String codNaturaGiuridica) {
		this.codNaturaGiuridica = codNaturaGiuridica;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public List<AttivitaEconomicaVO> getCodiciATECO() {
		return codiciATECO;
	}
	public void setCodiciATECO(List<AttivitaEconomicaVO> codiciATECO) {
		this.codiciATECO = codiciATECO;
	}
	public String getDescrNaturaGiuridica() {
		return descrNaturaGiuridica;
	}
	public void setDescrNaturaGiuridica(String descrNaturaGiuridica) {
		this.descrNaturaGiuridica = descrNaturaGiuridica;
	}
	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDescrFonte() {
		return descrFonte;
	}
	public void setDescrFonte(String descrFonte) {
		this.descrFonte = descrFonte;
	}
	public DettagliAlboArtigianoVO getDettagliAlboArtigiano() {
		return dettagliAlboArtigiano;
	}
	public void setDettagliAlboArtigiano(DettagliAlboArtigianoVO dettagliAlboArtigiano) {
		this.dettagliAlboArtigiano = dettagliAlboArtigiano;
	}
	public DettagliCameraCommercioVO getDettagliCameraCommercio() {
		return dettagliCameraCommercio;
	}
	public void setDettagliCameraCommercio(
			DettagliCameraCommercioVO dettagliCameraCommercio) {
		this.dettagliCameraCommercio = dettagliCameraCommercio;
	}
	public String getIdFonte() {
		return idFonte;
	}
	public void setIdFonte(String idFonte) {
		this.idFonte = idFonte;
	}
	public String getIdNaturaGiuridica() {
		return idNaturaGiuridica;
	}
	public void setIdNaturaGiuridica(String idNaturaGiuridica) {
		this.idNaturaGiuridica = idNaturaGiuridica;
	}
	public String getIdSede() {
		return idSede;
	}
	public void setIdSede(String idSede) {
		this.idSede = idSede;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	public String getPostaElettronicaCertificata() {
		return postaElettronicaCertificata;
	}
	public void setPostaElettronicaCertificata(String postaElettronicaCertificata) {
		this.postaElettronicaCertificata = postaElettronicaCertificata;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public List<SedeVO> getSedi() {
		return sedi;
	}
	public void setSedi(List<SedeVO> sedi) {
		this.sedi = sedi;
	}
	public String getAnnoDenunciaAddetti() {
		return annoDenunciaAddetti;
	}
	public void setAnnoDenunciaAddetti(String annoDenunciaAddetti) {
		this.annoDenunciaAddetti = annoDenunciaAddetti;
	}
	public CessazioneVO getCessazioneFunzioneSedeLegale() {
		return cessazioneFunzioneSedeLegale;
	}
	public void setCessazioneFunzioneSedeLegale(
			CessazioneVO cessazioneFunzioneSedeLegale) {
		this.cessazioneFunzioneSedeLegale = cessazioneFunzioneSedeLegale;
	}
	public String getCodFonte() {
		return codFonte;
	}
	public void setCodFonte(String codFonte) {
		this.codFonte = codFonte;
	}
	public DatoCostitutivoVO getDatoCostitutivo() {
		return datoCostitutivo;
	}
	public void setDatoCostitutivo(DatoCostitutivoVO datoCostitutivo) {
		this.datoCostitutivo = datoCostitutivo;
	}
	public String getDescrIndicStatoAttiv() {
		return descrIndicStatoAttiv;
	}
	public void setDescrIndicStatoAttiv(String descrIndicStatoAttiv) {
		this.descrIndicStatoAttiv = descrIndicStatoAttiv;
	}
	public String getDescrIndicTrasfSede() {
		return descrIndicTrasfSede;
	}
	public void setDescrIndicTrasfSede(String descrIndicTrasfSede) {
		this.descrIndicTrasfSede = descrIndicTrasfSede;
	}
	public String getFlagLocalizzazionePiemonte() {
		return flagLocalizzazionePiemonte;
	}
	public void setFlagLocalizzazionePiemonte(String flagLocalizzazionePiemonte) {
		this.flagLocalizzazionePiemonte = flagLocalizzazionePiemonte;
	}
	public String getFlgAggiornamento() {
		return flgAggiornamento;
	}
	public void setFlgAggiornamento(String flgAggiornamento) {
		this.flgAggiornamento = flgAggiornamento;
	}
	public String getImpresaCessata() {
		return impresaCessata;
	}
	public void setImpresaCessata(String impresaCessata) {
		this.impresaCessata = impresaCessata;
	}
	public String getIndicStatoAttiv() {
		return indicStatoAttiv;
	}
	public void setIndicStatoAttiv(String indicStatoAttiv) {
		this.indicStatoAttiv = indicStatoAttiv;
	}
	public String getIndicTrasfSede() {
		return indicTrasfSede;
	}
	public void setIndicTrasfSede(String indicTrasfSede) {
		this.indicTrasfSede = indicTrasfSede;
	}
	public List<PersonaVO> getListaPersone() {
		return listaPersone;
	}
	public void setListaPersone(List<PersonaVO> listaPersone) {
		this.listaPersone = listaPersone;
	}
	public List<ProcConcorsVO> getListaProcConcors() {
		return listaProcConcors;
	}
	public void setListaProcConcors(List<ProcConcorsVO> listaProcConcors) {
		this.listaProcConcors = listaProcConcors;
	}
	public List<SezioneSpecialeVO> getListaSezSpecInfoc() {
		return listaSezSpecInfoc;
	}
	public void setListaSezSpecInfoc(List<SezioneSpecialeVO> listaSezSpecInfoc) {
		this.listaSezSpecInfoc = listaSezSpecInfoc;
	}
	public String getNumAddettiFam() {
		return numAddettiFam;
	}
	public void setNumAddettiFam(String numAddettiFam) {
		this.numAddettiFam = numAddettiFam;
	}
	public String getNumAddettiSubord() {
		return numAddettiSubord;
	}
	public void setNumAddettiSubord(String numAddettiSubord) {
		this.numAddettiSubord = numAddettiSubord;
	}
	public List<String> getTestoOggettoSociale() {
		return testoOggettoSociale;
	}
	public void setTestoOggettoSociale(List<String> testoOggettoSociale) {
		this.testoOggettoSociale = testoOggettoSociale;
	}
	
	/** Jira: 1707 */
	public String getDataIscrizioneRegistroImprese() {
		return dataIscrizioneRegistroImprese;
	}
	
	/** Jira: 1707 */
	public void setDataIscrizioneRegistroImprese(
			String dataIscrizioneRegistroImprese) {
		this.dataIscrizioneRegistroImprese = dataIscrizioneRegistroImprese;
	}
	
}
