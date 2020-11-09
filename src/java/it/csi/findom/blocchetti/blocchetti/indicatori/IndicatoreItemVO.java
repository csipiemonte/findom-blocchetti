/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class IndicatoreItemVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String idIndicatore;

	@MapTo(target=INHERIT)
	String codIndicatore;        
	
	@MapTo(target=INHERIT)
	String descrIndicatore;      
	
	@MapTo(target=INHERIT)
	String unitaMisuraIndicatore;
    
	@MapTo(target=INHERIT)
	String valoreIndicatore;
	
	@MapTo(target=INHERIT)
	String linkIndicatore;       

	@MapTo(target=INHERIT)
	String flagObbligatorio;
	
	/** Jira: 1797 */
	@MapTo(target=INHERIT)
	String flagAlfa;
	
	public String getFlagAlfa() {
		return flagAlfa;
	}

	public void setFlagAlfa(String flagAlfa) {
		this.flagAlfa = flagAlfa;
	}

	public String getFlagObbligatorio() {
		return flagObbligatorio;
	}

	public void setFlagObbligatorio(String flagObbligatorio) {
		this.flagObbligatorio = flagObbligatorio;
	}

	public String getIdIndicatore() {
		return idIndicatore;
	}

	public void setIdIndicatore(String idIndicatore) {
		this.idIndicatore = idIndicatore;
	}

	public String getValoreIndicatore() {
		return valoreIndicatore;
	}

	public void setValoreIndicatore(String valoreIndicatore) {
		this.valoreIndicatore = valoreIndicatore;
	}

	public String getCodIndicatore() {
		return codIndicatore;
	}

	public String getDescrIndicatore() {
		return descrIndicatore;
	}

	public String getUnitaMisuraIndicatore() {
		return unitaMisuraIndicatore;
	}

	public String getLinkIndicatore() {
		return linkIndicatore;
	}

	public void setCodIndicatore(String codIndicatore) {
		this.codIndicatore = codIndicatore;
	}

	public void setDescrIndicatore(String descrIndicatore) {
		this.descrIndicatore = descrIndicatore;
	}

	public void setUnitaMisuraIndicatore(String unitaMisuraIndicatore) {
		this.unitaMisuraIndicatore = unitaMisuraIndicatore;
	}

	public void setLinkIndicatore(String linkIndicatore) {
		this.linkIndicatore = linkIndicatore;
	}
	
}
