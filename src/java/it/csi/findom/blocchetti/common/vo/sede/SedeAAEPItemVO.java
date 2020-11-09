/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.sede;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SedeAAEPItemVO extends CommonalityVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	String idTipoSede;
	
	
	@MapTo(target = INHERIT)
	String idProvinciaSede;
	
	@MapTo(target = MapTarget.INHERIT)
	String idSede;
	@MapTo(target = MapTarget.INHERIT)
	String descrTipoSede;
	@MapTo(target = MapTarget.INHERIT)
	String descrProvinciaSede;
	@MapTo(target = MapTarget.INHERIT)
	String siglaProvinciaSede;
	@MapTo(target = MapTarget.INHERIT)
	String idComuneSede;
	@MapTo(target = MapTarget.INHERIT)
	String descrComuneSede;
	@MapTo(target = MapTarget.INHERIT)
	String indirizzoSede;
	@MapTo(target = MapTarget.INHERIT)
	String numeroCivicoSede;
	@MapTo(target = MapTarget.INHERIT)
	String capSede;
	@MapTo(target = MapTarget.INHERIT)
	String indirizzoPecSede;
	@MapTo(target = MapTarget.INHERIT)
	String telefonoSede;
	@MapTo(target = MapTarget.INHERIT)
	String codAteco2007Sede;
	@MapTo(target = MapTarget.INHERIT)
	String descrAteco2007Sede;
	@MapTo(target = MapTarget.INHERIT)
	String idAteco2007Sede;
	@MapTo(target = MapTarget.INHERIT)
	String codAteco2007denormSede;
	@MapTo(target = MapTarget.INHERIT)
	String coordX;
	@MapTo(target = MapTarget.INHERIT)
	String coordY;
	@MapTo(target = MapTarget.INHERIT)
	String posizioneSedeAAEP;
	@MapTo(target = MapTarget.INHERIT)
	String stato;
	@MapTo(target = MapTarget.INHERIT)
	String statoEstero;
	@MapTo(target = MapTarget.INHERIT)
	String cittaEstera;
	@MapTo(target = MapTarget.INHERIT)
	String statoEsteroDescrizione;
	public String getIdTipoSede() {
		return idTipoSede;
	}
	public void setIdTipoSede(String idTipoSede) {
		this.idTipoSede = idTipoSede;
	}
	public String getIdProvinciaSede() {
		return idProvinciaSede;
	}
	public void setIdProvinciaSede(String idProvinciaSede) {
		this.idProvinciaSede = idProvinciaSede;
	}
	public String getIdSede() {
		return idSede;
	}
	public void setIdSede(String idSede) {
		this.idSede = idSede;
	}
	public String getDescrTipoSede() {
		return descrTipoSede;
	}
	public void setDescrTipoSede(String descrTipoSede) {
		this.descrTipoSede = descrTipoSede;
	}
	public String getDescrProvinciaSede() {
		return descrProvinciaSede;
	}
	public void setDescrProvinciaSede(String descrProvinciaSede) {
		this.descrProvinciaSede = descrProvinciaSede;
	}
	public String getSiglaProvinciaSede() {
		return siglaProvinciaSede;
	}
	public void setSiglaProvinciaSede(String siglaProvinciaSede) {
		this.siglaProvinciaSede = siglaProvinciaSede;
	}
	public String getIdComuneSede() {
		return idComuneSede;
	}
	public void setIdComuneSede(String idComuneSede) {
		this.idComuneSede = idComuneSede;
	}
	public String getDescrComuneSede() {
		return descrComuneSede;
	}
	public void setDescrComuneSede(String descrComuneSede) {
		this.descrComuneSede = descrComuneSede;
	}
	public String getIndirizzoSede() {
		return indirizzoSede;
	}
	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}
	public String getNumeroCivicoSede() {
		return numeroCivicoSede;
	}
	public void setNumeroCivicoSede(String numeroCivicoSede) {
		this.numeroCivicoSede = numeroCivicoSede;
	}
	public String getCapSede() {
		return capSede;
	}
	public void setCapSede(String capSede) {
		this.capSede = capSede;
	}
	public String getIndirizzoPecSede() {
		return indirizzoPecSede;
	}
	public void setIndirizzoPecSede(String indirizzoPecSede) {
		this.indirizzoPecSede = indirizzoPecSede;
	}
	public String getTelefonoSede() {
		return telefonoSede;
	}
	public void setTelefonoSede(String telefonoSede) {
		this.telefonoSede = telefonoSede;
	}
	public String getCodAteco2007Sede() {
		return codAteco2007Sede;
	}
	public void setCodAteco2007Sede(String codAteco2007Sede) {
		this.codAteco2007Sede = codAteco2007Sede;
	}
	public String getDescrAteco2007Sede() {
		return descrAteco2007Sede;
	}
	public void setDescrAteco2007Sede(String descrAteco2007Sede) {
		this.descrAteco2007Sede = descrAteco2007Sede;
	}
	public String getIdAteco2007Sede() {
		return idAteco2007Sede;
	}
	public void setIdAteco2007Sede(String idAteco2007Sede) {
		this.idAteco2007Sede = idAteco2007Sede;
	}
	public String getCodAteco2007denormSede() {
		return codAteco2007denormSede;
	}
	public void setCodAteco2007denormSede(String codAteco2007denormSede) {
		this.codAteco2007denormSede = codAteco2007denormSede;
	}
	public String getCoordX() {
		return coordX;
	}
	public void setCoordX(String coordX) {
		this.coordX = coordX;
	}
	public String getCoordY() {
		return coordY;
	}
	public void setCoordY(String coordY) {
		this.coordY = coordY;
	}
	public String getPosizioneSedeAAEP() {
		return posizioneSedeAAEP;
	}
	public void setPosizioneSedeAAEP(String posizioneSedeAAEP) {
		this.posizioneSedeAAEP = posizioneSedeAAEP;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getStatoEstero() {
		return statoEstero;
	}
	public void setStatoEstero(String statoEstero) {
		this.statoEstero = statoEstero;
	}
	public String getCittaEstera() {
		return cittaEstera;
	}
	public void setCittaEstera(String cittaEstera) {
		this.cittaEstera = cittaEstera;
	}
	public String getStatoEsteroDescrizione() {
		return statoEsteroDescrizione;
	}
	public void setStatoEsteroDescrizione(String statoEsteroDescrizione) {
		this.statoEsteroDescrizione = statoEsteroDescrizione;
	}

}
