/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianospese;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

/**
 *  1) importoMinimoErogabile
 *  2) importoMassimoErogabile
 *  3) budgetDisponibile
 *  4) percMaxContributoErogabile
 *  5) totaleSpeseMinimo
 *  6) totaleSpeseMassimo
 *  
 * @author Amministratore
 */
public class ImportoPianoSpeseVO extends CommonalityVO 
{
	private static final long serialVersionUID = 1L;

	
	@MapTo(target = MapTarget.INHERIT)
	String totale;
	
	// 1
	@MapTo(target = MapTarget.INHERIT)
	Integer importoMinimoErogabile;
	
	// 2
	@MapTo(target = MapTarget.INHERIT)
	Integer importoMassimoErogabile;
	
	// 3
	@MapTo(target = MapTarget.INHERIT)
	Integer budgetDisponibile;
	
	// 4
	@MapTo(target = MapTarget.INHERIT)
	Integer percMaxContributoErogabile;
	
	// 5
	@MapTo(target = MapTarget.INHERIT)
	Integer totaleSpeseMinimo;
	
	// 6
	@MapTo(target = MapTarget.INHERIT)
	Integer totaleSpeseMassimo;
	
	
	
	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}

	public Integer getImportoMinimoErogabile() {
		return importoMinimoErogabile;
	}

	public void setImportoMinimoErogabile(Integer importoMinimoErogabile) {
		this.importoMinimoErogabile = importoMinimoErogabile;
	}

	public Integer getImportoMassimoErogabile() {
		return importoMassimoErogabile;
	}

	public void setImportoMassimoErogabile(Integer importoMassimoErogabile) {
		this.importoMassimoErogabile = importoMassimoErogabile;
	}

	public Integer getBudgetDisponibile() {
		return budgetDisponibile;
	}

	public void setBudgetDisponibile(Integer budgetDisponibile) {
		this.budgetDisponibile = budgetDisponibile;
	}

	public Integer getPercMaxContributoErogabile() {
		return percMaxContributoErogabile;
	}

	public void setPercMaxContributoErogabile(Integer percMaxContributoErogabile) {
		this.percMaxContributoErogabile = percMaxContributoErogabile;
	}

	public Integer getTotaleSpeseMinimo() {
		return totaleSpeseMinimo;
	}

	public void setTotaleSpeseMinimo(Integer totaleSpeseMinimo) {
		this.totaleSpeseMinimo = totaleSpeseMinimo;
	}

	public Integer getTotaleSpeseMassimo() {
		return totaleSpeseMassimo;
	}

	public void setTotaleSpeseMassimo(Integer totaleSpeseMassimo) {
		this.totaleSpeseMassimo = totaleSpeseMassimo;
	}


}
