/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DimensioniImpresaVO extends CommonalityVO{
	
	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String idDimensioneImpresa;
	
	@MapTo(target=INHERIT)
	RisorseUmaneVO[] risorseUmaneList;

	@MapTo(target=INHERIT)
	DatiImpresaVO[] datiImpresaList;
	
	public String getIdDimensioneImpresa() {
		return idDimensioneImpresa;
	}

	public void setIdDimensioneImpresa(String idDimensioneImpresa) {
		this.idDimensioneImpresa = idDimensioneImpresa;
	}

	public RisorseUmaneVO[] getRisorseUmaneList() {
		return risorseUmaneList;
	}

	public void setRisorseUmaneList(RisorseUmaneVO[] risorseUmaneList) {
		this.risorseUmaneList = risorseUmaneList;
	}

	public DatiImpresaVO[] getDatiImpresaList() {
		return datiImpresaList;
	}

	public void setDatiImpresaList(DatiImpresaVO[] datiImpresaList) {
		this.datiImpresaList = datiImpresaList;
	}



	

}
