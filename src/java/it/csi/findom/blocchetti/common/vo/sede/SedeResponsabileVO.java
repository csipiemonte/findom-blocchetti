/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.sede;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SedeResponsabileVO extends CommonalityVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MapTo(target = MapTarget.INHERIT)
	String idOperatoreRT = "";
	
	@MapTo(target = MapTarget.INHERIT)
	String idSedeResponsabile = "";
	
	@MapTo(target = MapTarget.INHERIT)
	String denominazioneSedeResponsabile = "";
	
	@MapTo(target = MapTarget.INHERIT)
	String indirizzoSedeResponsabile = "";
	
	@MapTo(target = MapTarget.INHERIT)
	String comuneSedeResponsabile = "";
	
	@MapTo(target = MapTarget.INHERIT)
	String sigleProvSedeResponsabile = "";

	public String getIdOperatoreRT() {
		return idOperatoreRT;
	}

	public void setIdOperatoreRT(String idOperatoreRT) {
		this.idOperatoreRT = idOperatoreRT;
	}

	public String getIdSedeResponsabile() {
		return idSedeResponsabile;
	}

	public void setIdSedeResponsabile(String idSedeResponsabile) {
		this.idSedeResponsabile = idSedeResponsabile;
	}

	public String getDenominazioneSedeResponsabile() {
		return denominazioneSedeResponsabile;
	}

	public void setDenominazioneSedeResponsabile(String denominazioneSedeResponsabile) {
		this.denominazioneSedeResponsabile = denominazioneSedeResponsabile;
	}

	public String getIndirizzoSedeResponsabile() {
		return indirizzoSedeResponsabile;
	}

	public void setIndirizzoSedeResponsabile(String indirizzoSedeResponsabile) {
		this.indirizzoSedeResponsabile = indirizzoSedeResponsabile;
	}

	public String getComuneSedeResponsabile() {
		return comuneSedeResponsabile;
	}

	public void setComuneSedeResponsabile(String comuneSedeResponsabile) {
		this.comuneSedeResponsabile = comuneSedeResponsabile;
	}

	public String getSigleProvSedeResponsabile() {
		return sigleProvSedeResponsabile;
	}

	public void setSigleProvSedeResponsabile(String sigleProvSedeResponsabile) {
		this.sigleProvSedeResponsabile = sigleProvSedeResponsabile;
	}

}
