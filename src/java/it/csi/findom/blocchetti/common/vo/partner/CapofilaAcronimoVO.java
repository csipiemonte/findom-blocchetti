/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CapofilaAcronimoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	String idCapofilaAcronimo;                  

	@MapTo(target = MapTarget.INHERIT)
	String idDomanda;
	
	@MapTo(target = MapTarget.INHERIT)
	String idAcronimoBando;

	@MapTo(target=INHERIT)
	String dtAttivazione;

	@MapTo(target=INHERIT)
	String dtDisattivazione;

	public String getIdCapofilaAcronimo() {
		return idCapofilaAcronimo;
	}

	public void setIdCapofilaAcronimo(String idCapofilaAcronimo) {
		this.idCapofilaAcronimo = idCapofilaAcronimo;
	}

	public String getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getIdAcronimoBando() {
		return idAcronimoBando;
	}

	public void setIdAcronimoBando(String idAcronimoBando) {
		this.idAcronimoBando = idAcronimoBando;
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

	
}
