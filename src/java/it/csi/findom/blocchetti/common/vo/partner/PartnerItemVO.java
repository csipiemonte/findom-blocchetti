/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class PartnerItemVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	String idPartner;
	
	@MapTo(target = MapTarget.INHERIT)
	String codiceFiscale;
	
	@MapTo(target = MapTarget.INHERIT)
	String denominazione;
	
	//contiene DipartimentoVO.codice
	@MapTo(target = MapTarget.INHERIT)
	String codiceDipartimento;  
	
	@MapTo(target = MapTarget.INHERIT)
	String descrDipartimento;	
	
	@MapTo(target = MapTarget.INHERIT)
	String idStato;	
	
	@MapTo(target = MapTarget.INHERIT)
	String descrStato;
	
	@MapTo(target=INHERIT)
	String daCancellare;
	
	@MapTo(target=INHERIT)
	String idDomandaPartner;
	
	@MapTo(target=INHERIT)
	String statoDomandaPartner;
	
	@MapTo(target=INHERIT)
	String idStatoDomandaPartner;

	public String getIdPartner() {
		return idPartner;
	}

	public void setIdPartner(String idPartner) {
		this.idPartner = idPartner;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodiceDipartimento() {
		return codiceDipartimento;
	}

	public void setCodiceDipartimento(String codiceDipartimento) {
		this.codiceDipartimento = codiceDipartimento;
	}

	public String getDescrDipartimento() {
		return descrDipartimento;
	}

	public void setDescrDipartimento(String descrDipartimento) {
		this.descrDipartimento = descrDipartimento;
	}

	public String getIdStato() {
		return idStato;
	}

	public void setIdStato(String idStato) {
		this.idStato = idStato;
	}

	public String getDescrStato() {
		return descrStato;
	}

	public void setDescrStato(String descrStato) {
		this.descrStato = descrStato;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	public String getIdDomandaPartner() {
		return idDomandaPartner;
	}

	public void setIdDomandaPartner(String idDomandaPartner) {
		this.idDomandaPartner = idDomandaPartner;
	}

	public String getStatoDomandaPartner() {
		return statoDomandaPartner;
	}

	public void setStatoDomandaPartner(String statoDomandaPartner) {
		this.statoDomandaPartner = statoDomandaPartner;
	}

	public String getIdStatoDomandaPartner() {
		return idStatoDomandaPartner;
	}

	public void setIdStatoDomandaPartner(String idStatoDomandaPartner) {
		this.idStatoDomandaPartner = idStatoDomandaPartner;
	}
	
}
