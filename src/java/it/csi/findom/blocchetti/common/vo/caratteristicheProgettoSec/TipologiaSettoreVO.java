/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.caratteristicheProgettoSec;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

/*************************************************************
 * ------------------------------------------- db
 * 	id_bando 				as idBando 
 * 	id_settore				as idSettore
 * 	id_tipol_intervento 	as idTipolIntervento
 * 	codice 					as codice
 * 	descrizione 			as descrizioneSettore
 * 	descrizione 			as dettaglioSettore
 * 	flag_obbligatorio 		as flag
 * ------------------------------------------- java
 * 	idBando				: Integer
 *  idSettore			: Integer
 *  idTipolIntervento	: Integer
 *  descrBreve			: varchar(200)
 *  descrizione			: varchar(200)
 *  codice				: varchar(20)
 *  flagObbligatorio	: char(1)
 *  
 *  checked
 *  numDettagli
 *  TipologiaDescrizioneSettoreVO[] descrizioneSettoreList;
 *  -------------------------------------------------------
 *  
 *  private Integer idBando;
	private Integer idSettore;
	private String descrBreve;
	private Integer idTipolIntervento;
	private String codice;
	private String descrizioneSettore;
	private String dettaglioSettore;
	private String flagObbligatorio;
 *************************************************************/

public class TipologiaSettoreVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	
	// .1 - non cancellare
//	@MapTo(target=MapTarget.INHERIT)
//	TipologiaDescrizioneSettoreVO[] dettaglioInterventoList;
	
	@MapTo(target=MapTarget.INHERIT)
	TipologiaDescrizioneSettoreVO[] dettaglioBandoList;
	
	// .2
	@MapTo(target=MapTarget.INHERIT)
	String idBando;
	
	// .3
	@MapTo(target=MapTarget.INHERIT)
	String descrBreve;
	
	// .4
	@MapTo(target=MapTarget.INHERIT)
	String idSettore;
	
	// .5
	@MapTo(target=MapTarget.INHERIT)
	String idTipolIntervento;
	
	// .6
	@MapTo(target=MapTarget.INHERIT)
	String codice;
	
	// .7
	@MapTo(target=MapTarget.INHERIT)
	String descrizioneSettore;
	
	// .8
	@MapTo(target=MapTarget.INHERIT)
	String dettaglioSettore;
	
	// .9
	@MapTo(target=MapTarget.INHERIT)
	String flagObbligatorio;
	

	// .10 - non cancellare 
	@MapTo(target=MapTarget.INHERIT)
	String checked;

	// .11 - non cancellare
	@MapTo(target=MapTarget.INHERIT)
	String numDettagli;

	
	/** Get | Set */



	public String getIdBando() {
		return idBando;
	}


	public void setIdBando(String idBando) {
		this.idBando = idBando;
	}


	public String getIdSettore() {
		return idSettore;
	}


	public void setIdSettore(String idSettore) {
		this.idSettore = idSettore;
	}


	public String getIdTipolIntervento() {
		return idTipolIntervento;
	}


	public void setIdTipolIntervento(String idTipolIntervento) {
		this.idTipolIntervento = idTipolIntervento;
	}


	public String getCodice() {
		return codice;
	}


	public void setCodice(String codice) {
		this.codice = codice;
	}


	public String getDescrizioneSettore() {
		return descrizioneSettore;
	}


	public void setDescrizioneSettore(String descrizioneSettore) {
		this.descrizioneSettore = descrizioneSettore;
	}


	public String getDettaglioSettore() {
		return dettaglioSettore;
	}


	public void setDettaglioSettore(String dettaglioSettore) {
		this.dettaglioSettore = dettaglioSettore;
	}


	public String getFlagObbligatorio() {
		return flagObbligatorio;
	}


	public void setFlagObbligatorio(String flagObbligatorio) {
		this.flagObbligatorio = flagObbligatorio;
	}


	public String getChecked() {
		return checked;
	}


	public void setChecked(String checked) {
		this.checked = checked;
	}


	public String getDescrBreve() {
		return descrBreve;
	}


	public void setDescrBreve(String descrBreve) {
		this.descrBreve = descrBreve;
	}


	public String getNumDettagli() {
		return numDettagli;
	}


	public void setNumDettagli(String numDettagli) {
		this.numDettagli = numDettagli;
	}


	public TipologiaDescrizioneSettoreVO[] getDettaglioBandoList() {
		return dettaglioBandoList;
	}


	public void setDettaglioBandoList(
			TipologiaDescrizioneSettoreVO[] dettaglioBandoList) {
		this.dettaglioBandoList = dettaglioBandoList;
	}

}
