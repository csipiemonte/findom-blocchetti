/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CessazioneVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT) Boolean cessazione;
	@MapTo(target = INHERIT) String codCausaleCessazione;
	@MapTo(target = INHERIT) String dataCessazione;
	@MapTo(target = INHERIT) String dataDenunciaCessazione;
	@MapTo(target = INHERIT) String descrCausaleCessazione;
	
	public Boolean getCessazione() {
		return cessazione;
	}
	public void setCessazione(Boolean cessazione) {
		this.cessazione = cessazione;
	}
	public String getCodCausaleCessazione() {
		return codCausaleCessazione;
	}
	public void setCodCausaleCessazione(String codCausaleCessazione) {
		this.codCausaleCessazione = codCausaleCessazione;
	}
	public String getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(String dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public String getDataDenunciaCessazione() {
		return dataDenunciaCessazione;
	}
	public void setDataDenunciaCessazione(String dataDenunciaCessazione) {
		this.dataDenunciaCessazione = dataDenunciaCessazione;
	}
	public String getDescrCausaleCessazione() {
		return descrCausaleCessazione;
	}
	public void setDescrCausaleCessazione(String descrCausaleCessazione) {
		this.descrCausaleCessazione = descrCausaleCessazione;
	}
}
