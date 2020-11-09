/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;



import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class FormaFinanziamentoVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	TipoFormaFinanziamentoVO[] formaFinanziamentoList;
	
	@MapTo(target=INHERIT)
	String importoRichiesto;
	
	@MapTo(target=INHERIT)
	String totContributoRichiesto;
	
	@MapTo(target=INHERIT)
	String totFinanziamentoRichiesto;
	
	@MapTo(target=INHERIT)
	String importoFormaAgevolazione;
	
	/** Jira: */
	@MapTo(target=INHERIT)
	String cessioneCreditoChecked;
	
	@MapTo(target=INHERIT)
	String cessioneCreditoImporto;
	
	public String getImportoFormaAgevolazione() {
		return importoFormaAgevolazione;
	}

	public void setImportoFormaAgevolazione(String importoFormaAgevolazione) {
		this.importoFormaAgevolazione = importoFormaAgevolazione;
	}

	public String getTotFinanziamentoRichiesto() {
		return totFinanziamentoRichiesto;
	}

	public void setTotFinanziamentoRichiesto(String totFinanziamentoRichiesto) {
		this.totFinanziamentoRichiesto = totFinanziamentoRichiesto;
	}

	public String getTotContributoRichiesto() {
		return totContributoRichiesto;
	}

	public void setTotContributoRichiesto(String totContributoRichiesto) {
		this.totContributoRichiesto = totContributoRichiesto;
	}

	public TipoFormaFinanziamentoVO[] getFormaFinanziamentoList() {
		return formaFinanziamentoList;
	}

	public void setFormaFinanziamentoList(TipoFormaFinanziamentoVO[] formaFinanziamentoList) {
		this.formaFinanziamentoList = formaFinanziamentoList;
	}

	public String getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setImportoRichiesto(String importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public String getCessioneCreditoChecked() {
		return cessioneCreditoChecked;
	}

	public void setCessioneCreditoChecked(String cessioneCreditoChecked) {
		this.cessioneCreditoChecked = cessioneCreditoChecked;
	}

	public String getCessioneCreditoImporto() {
		return cessioneCreditoImporto;
	}

	public void setCessioneCreditoImporto(String cessioneCreditoImporto) {
		this.cessioneCreditoImporto = cessioneCreditoImporto;
	}
}
