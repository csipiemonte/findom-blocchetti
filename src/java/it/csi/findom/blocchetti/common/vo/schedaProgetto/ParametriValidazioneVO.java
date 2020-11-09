/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.schedaProgetto;

import java.util.List;

/*
 * Questa classe
 * é stata definita per essere di supporto
 * alla validazione globale di parametri
 * sulla scheda progetto usata ad oggi
 * per Amianto...
 */
public class ParametriValidazioneVO {

	private String tipoBeneficiario;
	private String idDettIntervento;
	private Integer idCriterio;
	private Integer idSpecifica;
	private Integer idParametro;
	private String isParamChecked = "false";
	private String idTipoIntervento = "";
	private int numCriteri = 0;
	private int numParamSelezionati = 0;
	private String mex = "";
	
	/**
	 * costruttore std
	 */
	public ParametriValidazioneVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Costruttore con parametri
	 * @param tipoBeneficiario
	 * @param idTipoIntervento
	 * @param idDettIntervento
	 * @param idCriterio
	 * @param idSpecifica
	 * @param idParametro
	 * @param isParamChecked
	 */
	public ParametriValidazioneVO(String tipoBeneficiario,
			String idTipoIntervento, String idDettIntervento,
			Integer idCriterio, Integer idSpecifica, Integer idParametro,
			String isParamChecked) {
		this.tipoBeneficiario = tipoBeneficiario;
		this.idDettIntervento = idDettIntervento;
		this.idCriterio = idCriterio;
		this.idSpecifica = idSpecifica;
		this.idParametro = idParametro;
		this.isParamChecked = isParamChecked;
		this.idTipoIntervento = idTipoIntervento;
	}
	
	

//	public ParametriValidazioneVO(
//			String tipoBeneficiario, 
//			String idTipoIntervento, 
//			String idDettIntervento, 
//			Integer idCriterio, 
//			Integer idSpecifica,
//			Integer idParametro, 
//			String isParamChecked,	 
//			int numeroParametri,
//			int totSpecifiche
//			) {
//		this.tipoBeneficiario = tipoBeneficiario;
//		this.idDettIntervento = idDettIntervento;
//		this.idCriterio = idCriterio;
//		this.idSpecifica = idSpecifica;
//		this.idParametro = idParametro;
//		this.isParamChecked = isParamChecked;
//		this.idTipoIntervento = idTipoIntervento;
//		this.numParamSelezionati = numeroParametri;
//		this.totSpecifiche = totSpecifiche;
//	}

//	public ParametriValidazioneVO(int idSpecifica, int numParam, int totChecked) {
//		this.idSpecifica = idSpecifica;
//		this.numParamSelezionati = numParam;
//		this.totSpecifiche = totChecked;
//	}
	
	

//	public ParametriValidazioneVO(String tipoBeneficiario, String idDettIntervento, Integer idCriterio, Integer idSpecifica, Integer idParametro, String isParamChecked, String idTipoIntervento, String mex) {
//		this.tipoBeneficiario = tipoBeneficiario;
//		this.idDettIntervento = idDettIntervento;
//		this.idCriterio = idCriterio;
//		this.idSpecifica = idSpecifica;
//		this.idParametro = idParametro;
//		this.isParamChecked = isParamChecked;
//		this.idTipoIntervento = idTipoIntervento;
//		this.mex = mex;
//	}



	public ParametriValidazioneVO(String tipoBeneficiario2,
			String idDettIntervento2, Integer idCriterio2,
			Integer idSpecifica2, Integer idParametro2, String isParamChecked2,
			String idTipoIntervento2, String msgError01) {
	}



	public ParametriValidazioneVO(String numCriteri, String nParamSelezionati) {
		this.numCriteri = Integer.parseInt(numCriteri);
		this.numParamSelezionati = Integer.parseInt(nParamSelezionati);
	}

	
	

	public ParametriValidazioneVO(String tipoBeneficiario, String idTipoIntervento, String idDettIntervento, Integer idCriterio, Integer idSpecifica, Integer idParametro, int nCr, int nPSel, String msgError) {
		this.tipoBeneficiario=tipoBeneficiario;
		this.idTipoIntervento=idTipoIntervento;
		this.idDettIntervento=idDettIntervento;
		this.idCriterio=idCriterio;
		this.idSpecifica=idSpecifica;
		this.idParametro=idParametro;
		this.numCriteri=nCr;
		this.numParamSelezionati=nPSel;
		this.mex=msgError;
	}

	public ParametriValidazioneVO(String msgError) {
		setMex(msgError);
	}

	/** Get | Set */
	public String getIdDettIntervento() {
		return idDettIntervento;
	}

	public void setIdDettIntervento(String idDettIntervento) {
		this.idDettIntervento = idDettIntervento;
	}

	public Integer getIdCriterio() {
		return idCriterio;
	}

	public void setIdCriterio(Integer idCriterio) {
		this.idCriterio = idCriterio;
	}

	public Integer getIdSpecifica() {
		return idSpecifica;
	}

	public void setIdSpecifica(Integer idSpecifica) {
		this.idSpecifica = idSpecifica;
	}

	public Integer getIdParametro() {
		return idParametro;
	}

	public void setIdParametro(Integer idParametro) {
		this.idParametro = idParametro;
	}

	public String getIsParamChecked() {
		return isParamChecked;
	}

	public void setIsParamChecked(String isParamChecked) {
		this.isParamChecked = isParamChecked;
	}

	public String getIdTipoIntervento() {
		return idTipoIntervento;
	}

	public void setIdTipoIntervento(String idTipoIntervento) {
		this.idTipoIntervento = idTipoIntervento;
	}


	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
	


	public int getNumParamSelezionati() {
		return numParamSelezionati;
	}

	public void setNumParamSelezionati(int numParamSelezionati) {
		this.numParamSelezionati = numParamSelezionati;
	}
	

	public String getMex() {
		return mex;
	}

	public void setMex(String mex) {
		this.mex = mex;
	}

	public int getNumCriteri() {
		return numCriteri;
	}

	public void setNumCriteri(int numCriteri) {
		this.numCriteri = numCriteri;
	}


} // -/ fine
