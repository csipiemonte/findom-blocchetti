/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettagliAlboArtigianoVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT) String dataDeliberaIscrizione;
	@MapTo(target = INHERIT) String descrIterIscrizione;
	@MapTo(target = INHERIT) String flgIterIscrizione;
	@MapTo(target = INHERIT) String numeroIscrizione;
	@MapTo(target = INHERIT) String provinciaIscrizione;
	
	public String getDataDeliberaIscrizione() {
		return dataDeliberaIscrizione;
	}
	public void setDataDeliberaIscrizione(String dataDeliberaIscrizione) {
		this.dataDeliberaIscrizione = dataDeliberaIscrizione;
	}
	public String getDescrIterIscrizione() {
		return descrIterIscrizione;
	}
	public void setDescrIterIscrizione(String descrIterIscrizione) {
		this.descrIterIscrizione = descrIterIscrizione;
	}
	public String getFlgIterIscrizione() {
		return flgIterIscrizione;
	}
	public void setFlgIterIscrizione(String flgIterIscrizione) {
		this.flgIterIscrizione = flgIterIscrizione;
	}
	public String getNumeroIscrizione() {
		return numeroIscrizione;
	}
	public void setNumeroIscrizione(String numeroIscrizione) {
		this.numeroIscrizione = numeroIscrizione;
	}
	public String getProvinciaIscrizione() {
		return provinciaIscrizione;
	}
	public void setProvinciaIscrizione(String provinciaIscrizione) {
		this.provinciaIscrizione = provinciaIscrizione;
	}
}
