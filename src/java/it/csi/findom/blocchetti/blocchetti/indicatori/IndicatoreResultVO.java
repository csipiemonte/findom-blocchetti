/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;

public class IndicatoreResultVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
		String idTipoIndicatore;
	     String descrTipoIndicatore;
	     String idIndicatore;
	     String codIndicatore         ;
	     String descrIndicatore       ;
	     String unitaMisuraIndicatore ;
	     String valoreIndicatore      ;
	     String linkIndicatore        ;
	     String flagObbligatorio      ;
	     
	     /** Jira: 1797 */
	     String flagAlfa;
	     
		public String getFlagAlfa() {
			return flagAlfa;
		}
		public void setFlagAlfa(String flagAlfa) {
			this.flagAlfa = flagAlfa;
		}
		public String getIdTipoIndicatore() {
			return idTipoIndicatore;
		}
		public String getDescrTipoIndicatore() {
			return descrTipoIndicatore;
		}
		public String getIdIndicatore() {
			return idIndicatore;
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
		public String getValoreIndicatore() {
			return valoreIndicatore;
		}
		public String getLinkIndicatore() {
			return linkIndicatore;
		}
		public String getFlagObbligatorio() {
			return flagObbligatorio;
		}
		public void setIdTipoIndicatore(String idTipoIndicatore) {
			this.idTipoIndicatore = idTipoIndicatore;
		}
		public void setDescrTipoIndicatore(String descrTipoIndicatore) {
			this.descrTipoIndicatore = descrTipoIndicatore;
		}
		public void setIdIndicatore(String idIndicatore) {
			this.idIndicatore = idIndicatore;
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
		public void setValoreIndicatore(String valoreIndicatore) {
			this.valoreIndicatore = valoreIndicatore;
		}
		public void setLinkIndicatore(String linkIndicatore) {
			this.linkIndicatore = linkIndicatore;
		}
		public void setFlagObbligatorio(String flagObbligatorio) {
			this.flagObbligatorio = flagObbligatorio;
		}

}
