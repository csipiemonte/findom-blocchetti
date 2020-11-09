/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.abstractprogetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

/**
 * Classe definita per bando BonusPiemonte
 * a supporto di soggetti che richiedono bonus covid-19
 * Oggetto fa riferimento alla tabella: findom_t_soggetti_bonus_covid
 * 12/05/2020 
 * @author 2r
 *
 */
public class SoggettoBonusCovidListVO  extends CommonalityVO  {
	
	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	String codFiscale;

	@MapTo(target = INHERIT)
	String codAteco;
    	  
    @MapTo(target = INHERIT)
	String importoContributo;

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getCodAteco() {
		return codAteco;
	}

	public void setCodAteco(String codAteco) {
		this.codAteco = codAteco;
	}

	public String getImportoContributo() {
		return importoContributo;
	}

	public void setImportoContributo(String importoContributo) {
		this.importoContributo = importoContributo;
	}
	
}
