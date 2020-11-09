/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class FormaFinanziamentoOutput extends CommonalityOutput {

	@MapTo(target=NAMESPACE)
	List<TipoFormaFinanziamentoVO> formaFinanziamentoList;
	
	@MapTo(target=NAMESPACE)
	String importoProposto;
	
	@MapTo(target=NAMESPACE)
	String importoRichiesto;	 

	@MapTo(target=NAMESPACE)
	String msgWarning;
	
	/** ----------------------------- Jira: 1646 ::  */
	@MapTo(target=NAMESPACE)
	List hd;
	
	/** Jira: 1772 */
	@MapTo(target=NAMESPACE)
	FormaFinanziamentoVO _formaFinanziamento;
	
	
	/** Jira: 1772 */
	@MapTo(target=NAMESPACE)
	String cessioneCreditoChecked;
	
	@MapTo(target=NAMESPACE)
	String importoSogliaLimiteInferiore;
	
	@MapTo(target=NAMESPACE)
	String viewWarningAgevolazioni;
	
	@MapTo(target=NAMESPACE)
	String contributoContoInteresse;
	
	@MapTo(target=NAMESPACE)
	String importoContributoContoInteresse;

	@MapTo(target=NAMESPACE)
	String contributoTotaleRichiesto;
	
	
	/** Jira: 1772 */
	@MapTo(target=NAMESPACE)
	String cessioneCreditoImporto;
	
	@MapTo(target=NAMESPACE)
	String cb1;
	
	@MapTo(target=NAMESPACE)
	String cb21;

	
	/** Get | Set */
	public FormaFinanziamentoVO get_formaFinanziamento() {
		return _formaFinanziamento;
	}

	public void set_formaFinanziamento(FormaFinanziamentoVO _formaFinanziamento) {
		this._formaFinanziamento = _formaFinanziamento;
	}

	public List<TipoFormaFinanziamentoVO> getFormaFinanziamentoList() {
		return formaFinanziamentoList;
	}

	public void setFormaFinanziamentoList(
			List<TipoFormaFinanziamentoVO> formaFinanziamentoList) {
		this.formaFinanziamentoList = formaFinanziamentoList;
	}

	public String getImportoProposto() {
		return importoProposto;
	}

	public void setImportoProposto(String importoProposto) {
		this.importoProposto = importoProposto;
	}

	public String getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setImportoRichiesto(String importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public String getMsgWarning() {
		return msgWarning;
	}

	public void setMsgWarning(String msgWarning) {
		this.msgWarning = msgWarning;
	}

	public List getHd() {
		return hd;
	}

	public void setHd(List hd) {
		this.hd = hd;
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

	public String getImportoSogliaLimiteInferiore() {
		return importoSogliaLimiteInferiore;
	}

	public void setImportoSogliaLimiteInferiore(String importoSogliaLimiteInferiore) {
		this.importoSogliaLimiteInferiore = importoSogliaLimiteInferiore;
	}


	public String getViewWarningAgevolazioni() {
		return viewWarningAgevolazioni;
	}

	public void setViewWarningAgevolazioni(String viewWarningAgevolazioni) {
		this.viewWarningAgevolazioni = viewWarningAgevolazioni;
	}

	public String getCb1() {
		return cb1;
	}

	public void setCb1(String cb1) {
		this.cb1 = cb1;
	}

	public String getCb21() {
		return cb21;
	}

	public void setCb21(String cb21) {
		this.cb21 = cb21;
	}

}
