/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class TipoFormaFinanziamentoVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target=INHERIT)
	String idFormaFinanziamento;
	      
	@MapTo(target=INHERIT)
	String codFormaFinanziamento;
	      
	@MapTo(target=INHERIT)
	String descrFormaFinanziamento;
	      
	@MapTo(target=INHERIT)
	String tipoFormaFinanziamento;

	@MapTo(target=INHERIT)
	String formaFinanziamento;		      
	      
	@MapTo(target=INHERIT)
	String flagObbligatorio;		      
	      
	@MapTo(target=INHERIT)
	String percPrevista;		      
   
	@MapTo(target=INHERIT)
	String checked;

	@MapTo(target=INHERIT)
	String importoFormaAgevolazione;
	
	@MapTo(target=INHERIT)
	String importoMinErogabile;
	
	@MapTo(target=INHERIT)
	String importoMaxErogabile;
	
	@MapTo(target=INHERIT)
	String contributoContoInteresse;
	
	
	public TipoFormaFinanziamentoVO() {
	}
	

	public TipoFormaFinanziamentoVO(String idFormaFinanziamento, String flagObbligatorio, String percPrevista, String checked, String importoFormaAgevolazione) {
		this.idFormaFinanziamento = idFormaFinanziamento;
		this.flagObbligatorio = flagObbligatorio;
		this.percPrevista = percPrevista;
		this.checked = checked;
		this.importoFormaAgevolazione = importoFormaAgevolazione;
	}
	
	


	/** Get | Set */
	public String getIdFormaFinanziamento() {
		return idFormaFinanziamento;
	}

	public String getCodFormaFinanziamento() {
		return codFormaFinanziamento;
	}

	public String getDescrFormaFinanziamento() {
		return descrFormaFinanziamento;
	}

	public String getTipoFormaFinanziamento() {
		return tipoFormaFinanziamento;
	}

	public String getFormaFinanziamento() {
		return formaFinanziamento;
	}

	public String getFlagObbligatorio() {
		return flagObbligatorio;
	}

	public String getPercPrevista() {
		return percPrevista;
	}

	public String getChecked() {
		return checked;
	}

	public void setIdFormaFinanziamento(String idFormaFinanziamento) {
		this.idFormaFinanziamento = idFormaFinanziamento;
	}

	public void setCodFormaFinanziamento(String codFormaFinanziamento) {
		this.codFormaFinanziamento = codFormaFinanziamento;
	}

	public void setDescrFormaFinanziamento(String descrFormaFinanziamento) {
		this.descrFormaFinanziamento = descrFormaFinanziamento;
	}

	public void setTipoFormaFinanziamento(String tipoFormaFinanziamento) {
		this.tipoFormaFinanziamento = tipoFormaFinanziamento;
	}

	public void setFormaFinanziamento(String formaFinanziamento) {
		this.formaFinanziamento = formaFinanziamento;
	}

	public void setFlagObbligatorio(String flagObbligatorio) {
		this.flagObbligatorio = flagObbligatorio;
	}

	public void setPercPrevista(String percPrevista) {
		this.percPrevista = percPrevista;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getImportoFormaAgevolazione() {
		return importoFormaAgevolazione;
	}

	public void setImportoFormaAgevolazione(String importoFormaAgevolazione) {
		this.importoFormaAgevolazione = importoFormaAgevolazione;
	}


	public String getImportoMaxErogabile() {
		return importoMaxErogabile;
	}


	public void setImportoMaxErogabile(String importoMaxErogabile) {
		this.importoMaxErogabile = importoMaxErogabile;
	}


	public String getImportoMinErogabile() {
		return importoMinErogabile;
	}


	public void setImportoMinErogabile(String importoMinErogabile) {
		this.importoMinErogabile = importoMinErogabile;
	}


	public String getContributoContoInteresse() {
		return contributoContoInteresse;
	}


	public void setContributoContoInteresse(String contributoContoInteresse) {
		this.contributoContoInteresse = contributoContoInteresse;
	}
	
	
	

}
