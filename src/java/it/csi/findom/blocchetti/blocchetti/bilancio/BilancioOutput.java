/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class BilancioOutput extends CommonalityOutput {
		
	@MapTo(target=NAMESPACE)
	ImpresaVO enteImpresa;
	
	@MapTo(target=NAMESPACE)
	BilancioVO aaepMap;

	@MapTo(target=NAMESPACE)
	BilancioVO bilMap;
	
	@MapTo(target=NAMESPACE)
	String ebitda;
	@MapTo(target=NAMESPACE)
	String ebitdaPrec;
	@MapTo(target=NAMESPACE)
	String ebit;
	@MapTo(target=NAMESPACE)
	String ebitPrec;
	
	@MapTo(target=NAMESPACE)
	String mostraMsgAAEP;
		
	public ImpresaVO getEnteImpresa() {
		return enteImpresa;
	}

	public void setEnteImpresa(ImpresaVO enteImpresa) {
		this.enteImpresa = enteImpresa;
	}
	
	public BilancioVO getAaepMap() {
		return aaepMap;
	}

	public BilancioVO getBilMap() {
		return bilMap;
	}

	public void setAaepMap(BilancioVO aaepMap) {
		this.aaepMap = aaepMap;
	}

	public void setBilMap(BilancioVO bilMap) {
		this.bilMap = bilMap;
	}
	
	public String getEbitda() {
		return ebitda;
	}

	public String getEbitdaPrec() {
		return ebitdaPrec;
	}

	public String getEbit() {
		return ebit;
	}

	public String getEbitPrec() {
		return ebitPrec;
	}

	public void setEbitda(String ebitda) {
		this.ebitda = ebitda;
	}

	public void setEbitdaPrec(String ebitdaPrec) {
		this.ebitdaPrec = ebitdaPrec;
	}

	public void setEbit(String ebit) {
		this.ebit = ebit;
	}

	public void setEbitPrec(String ebitPrec) {
		this.ebitPrec = ebitPrec;
	}

	public String getMostraMsgAAEP() {
		return mostraMsgAAEP;
	}

	public void setMostraMsgAAEP(String mostraMsgAAEP) {
		this.mostraMsgAAEP = mostraMsgAAEP;
	}
	
}
