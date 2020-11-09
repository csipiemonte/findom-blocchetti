/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.allegati;


/**
 * Classe utile per
 * - verificare quali allegati richiedere
 *   in base a specifiche condizioni.
 *   Usata per il bando:
 *    
 * @author roberto rega
 *
 */
public class AllegatiTempVO {
	
	private String beneficiario;
	private String idIntervento;
	private String idPremialita;
	private String idDettaglioIntervento;
	
	
	
	public AllegatiTempVO() {

	}

	
	
	public AllegatiTempVO(String beneficiario, String idIntervento, String idPremialita, String idDettaglioIntervento) 
	{
		this.beneficiario = beneficiario;
		this.idIntervento = idIntervento;
		this.idPremialita = idPremialita;
		this.idDettaglioIntervento = idDettaglioIntervento;
	}



	public String getBeneficiario() {
		return beneficiario;
	}



	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}



	public String getIdIntervento() {
		return idIntervento;
	}



	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}



	public String getIdPremialita() {
		return idPremialita;
	}



	public void setIdPremialita(String idPremialita) {
		this.idPremialita = idPremialita;
	}



	public String getIdDettaglioIntervento() {
		return idDettaglioIntervento;
	}



	public void setIdDettaglioIntervento(String idDettaglioIntervento) {
		this.idDettaglioIntervento = idDettaglioIntervento;
	}



	@Override
	public String toString() {
		return "AllegatiObj [beneficiario=" + beneficiario + ", idIntervento="
				+ idIntervento + ", idPremialita=" + idPremialita
				+ ", idDettaglioIntervento=" + idDettaglioIntervento + "]";
	}

}
