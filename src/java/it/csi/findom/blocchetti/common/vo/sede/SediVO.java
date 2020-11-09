/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.sede;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.Arrays;

public class SediVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	SedeItemVO[] sediList;

	@MapTo(target = INHERIT)
	String flagSedeAttiva;

	@MapTo(target = INHERIT)
	String flagSedeNoPiemonte;
	
	@MapTo(target = INHERIT)
	String flagSedeMaiAttivata;
	
	@MapTo(target = INHERIT)
	String indiceSedeSelected;
	
	/** Get | Set */
	public boolean isEmpty() {
		return sediList == null;
	}

	@Override
	public String toString() {
		return "SediVO [sedeList=" + Arrays.toString(sediList) + ", flagSedeAttiva=" + flagSedeAttiva + "]";
	}

	public SedeItemVO[] getSediList() {
		return sediList;
	}

	public void setSediList(SedeItemVO[] sediList) {
		this.sediList = sediList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFlagSedeAttiva() {
		return flagSedeAttiva;
	}

	public void setFlagSedeAttiva(String flagSedeAttiva) {
		this.flagSedeAttiva = flagSedeAttiva;
	}

	public String getFlagSedeNoPiemonte() {
		return flagSedeNoPiemonte;
	}

	public void setFlagSedeNoPiemonte(String flagSedeNoPiemonte) {
		this.flagSedeNoPiemonte = flagSedeNoPiemonte;
	}

	public String getFlagSedeMaiAttivata() {
		return flagSedeMaiAttivata;
	}

	public void setFlagSedeMaiAttivata(String flagSedeMaiAttivata) {
		this.flagSedeMaiAttivata = flagSedeMaiAttivata;
	}

	public String getIndiceSedeSelected() {
		return indiceSedeSelected;
	}

	public void setIndiceSedeSelected(String indiceSedeSelected) {
		this.indiceSedeSelected = indiceSedeSelected;
	}

}
