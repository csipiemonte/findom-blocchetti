/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CapofilaAcronimoPartnerVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	String idCapofilaAcronimo;

	@MapTo(target = MapTarget.INHERIT)
	String idPartner;

	@MapTo(target = MapTarget.INHERIT)
	String idDomandaPartner;

	@MapTo(target=INHERIT)
	String dtAttivazione;

	@MapTo(target=INHERIT)
	String dtDisattivazione;
	
	@MapTo(target=INHERIT)
	String note;

	public String getIdCapofilaAcronimo() {
		return idCapofilaAcronimo;
	}

	public void setIdCapofilaAcronimo(String idCapofilaAcronimo) {
		this.idCapofilaAcronimo = idCapofilaAcronimo;
	}

	public String getIdPartner() {
		return idPartner;
	}

	public void setIdPartner(String idPartner) {
		this.idPartner = idPartner;
	}

	public String getIdDomandaPartner() {
		return idDomandaPartner;
	}

	public void setIdDomandaPartner(String idDomandaPartner) {
		this.idDomandaPartner = idDomandaPartner;
	}

	public String getDtAttivazione() {
		return dtAttivazione;
	}

	public void setDtAttivazione(String dtAttivazione) {
		this.dtAttivazione = dtAttivazione;
	}

	public String getDtDisattivazione() {
		return dtDisattivazione;
	}

	public void setDtDisattivazione(String dtDisattivazione) {
		this.dtDisattivazione = dtDisattivazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
}
