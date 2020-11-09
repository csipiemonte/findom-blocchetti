/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.abstractprogetto;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AbstractProgettoPoloAppVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=MapTarget.INHERIT)
	private String poloInnovazione;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza0;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza1;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza2;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza3;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza4;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza5;
	
	@MapTo(target=MapTarget.INHERIT)
	private String poloAppartenenza6;
	

	public String getPoloInnovazione() {
		return poloInnovazione;
	}

	public void setPoloInnovazione(String poloInnovazione) {
		this.poloInnovazione = poloInnovazione;
	}

	public String getPoloAppartenenza0() {
		return poloAppartenenza0;
	}

	public void setPoloAppartenenza0(String poloAppartenenza0) {
		this.poloAppartenenza0 = poloAppartenenza0;
	}

	public String getPoloAppartenenza1() {
		return poloAppartenenza1;
	}

	public void setPoloAppartenenza1(String poloAppartenenza1) {
		this.poloAppartenenza1 = poloAppartenenza1;
	}

	public String getPoloAppartenenza2() {
		return poloAppartenenza2;
	}

	public void setPoloAppartenenza2(String poloAppartenenza2) {
		this.poloAppartenenza2 = poloAppartenenza2;
	}

	public String getPoloAppartenenza3() {
		return poloAppartenenza3;
	}

	public void setPoloAppartenenza3(String poloAppartenenza3) {
		this.poloAppartenenza3 = poloAppartenenza3;
	}

	public String getPoloAppartenenza4() {
		return poloAppartenenza4;
	}

	public void setPoloAppartenenza4(String poloAppartenenza4) {
		this.poloAppartenenza4 = poloAppartenenza4;
	}

	public String getPoloAppartenenza5() {
		return poloAppartenenza5;
	}

	public void setPoloAppartenenza5(String poloAppartenenza5) {
		this.poloAppartenenza5 = poloAppartenenza5;
	}

	public String getPoloAppartenenza6() {
		return poloAppartenenza6;
	}

	public void setPoloAppartenenza6(String poloAppartenenza6) {
		this.poloAppartenenza6 = poloAppartenenza6;
	}
	
	
	
	

}
