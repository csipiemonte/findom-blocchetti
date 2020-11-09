/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.documentazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

/**
 * Nome della tabella: findom_t_allegati_sportello
 * campi tabella persi in considerazione nella classe java:
 * id					: serial : non implementata nella classe java 
 * id_sportello_bando	: db: integer -> java: Integer
 * id_tipol_beneficiario: db: integer -> java: Integer
 * id_allegato			: db: integer -> java: Integer
 * flag_obbligatorio	: db: char(1) -> java: String
 * flag_differibile		: db: char(1) -> java: String
 * flag_firma_digitale	: db: char(1) -> java: String
 * 
 * @author Amministratore
 */
public class TipologiaAllegatoIntegrativoVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;
	
	@MapTo(target=INHERIT)
	Integer idBando;
	
	@MapTo(target=INHERIT)
	Integer id_sportello_bando;
	
	@MapTo(target=INHERIT)
	Integer id_tipol_beneficiario;
	
	@MapTo(target=INHERIT)
	Integer id_allegato;
	
	@MapTo(target=INHERIT)
	String flag_obbligatorio;
	
	@MapTo(target=INHERIT)
	String flag_differibile;
	
	@MapTo(target=INHERIT)
	String flag_firma_digitale;
	
	@MapTo(target=INHERIT)
	String descrizione;
	
	@MapTo(target=INHERIT)
	String dtInizio;
	
	@MapTo(target=INHERIT)
	String dtFine;
	

	public Integer getIdBando() {
		return idBando;
	}

	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}

	public String getDtInizio() {
		return dtInizio;
	}

	public void setDtInizio(String dtInizio) {
		this.dtInizio = dtInizio;
	}

	public String getDtFine() {
		return dtFine;
	}

	public void setDtFine(String dtFine) {
		this.dtFine = dtFine;
	}

	public Integer getId_sportello_bando() {
		return id_sportello_bando;
	}

	public void setId_sportello_bando(Integer id_sportello_bando) {
		this.id_sportello_bando = id_sportello_bando;
	}

	public Integer getId_tipol_beneficiario() {
		return id_tipol_beneficiario;
	}

	public void setId_tipol_beneficiario(Integer id_tipol_beneficiario) {
		this.id_tipol_beneficiario = id_tipol_beneficiario;
	}

	public Integer getId_allegato() {
		return id_allegato;
	}

	public void setId_allegato(Integer id_allegato) {
		this.id_allegato = id_allegato;
	}

	public String getFlag_obbligatorio() {
		return flag_obbligatorio;
	}

	public void setFlag_obbligatorio(String flag_obbligatorio) {
		this.flag_obbligatorio = flag_obbligatorio;
	}

	public String getFlag_differibile() {
		return flag_differibile;
	}

	public void setFlag_differibile(String flag_differibile) {
		this.flag_differibile = flag_differibile;
	}

	public String getFlag_firma_digitale() {
		return flag_firma_digitale;
	}

	public void setFlag_firma_digitale(String flag_firma_digitale) {
		this.flag_firma_digitale = flag_firma_digitale;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
}
