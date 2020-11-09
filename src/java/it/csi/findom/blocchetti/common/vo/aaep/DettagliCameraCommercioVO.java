/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.aaep;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DettagliCameraCommercioVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT) String anno;
	@MapTo(target = INHERIT) String dataAggiornamento;
	@MapTo(target = INHERIT) String dataCancellazioneREA;
	@MapTo(target = INHERIT) String dataIscrizioneREA;
	@MapTo(target = INHERIT) String dataIscrizioneRegistroImprese;
	@MapTo(target = INHERIT) String numIscrizionePosizioneREA;
	@MapTo(target = INHERIT) String numRegistroImprese;
	@MapTo(target = INHERIT) String numero;
	@MapTo(target = INHERIT) String provincia;
	@MapTo(target = INHERIT) String siglaProvincia;
	@MapTo(target = INHERIT) String siglaProvinciaIscrizioneREA;
	@MapTo(target = INHERIT) String tribunale;
	
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getDataCancellazioneREA() {
		return dataCancellazioneREA;
	}
	public void setDataCancellazioneREA(String dataCancellazioneREA) {
		this.dataCancellazioneREA = dataCancellazioneREA;
	}
	public String getDataIscrizioneREA() {
		return dataIscrizioneREA;
	}
	public void setDataIscrizioneREA(String dataIscrizioneREA) {
		this.dataIscrizioneREA = dataIscrizioneREA;
	}
	public String getDataIscrizioneRegistroImprese() {
		return dataIscrizioneRegistroImprese;
	}
	public void setDataIscrizioneRegistroImprese(
			String dataIscrizioneRegistroImprese) {
		this.dataIscrizioneRegistroImprese = dataIscrizioneRegistroImprese;
	}
	public String getNumIscrizionePosizioneREA() {
		return numIscrizionePosizioneREA;
	}
	public void setNumIscrizionePosizioneREA(String numIscrizionePosizioneREA) {
		this.numIscrizionePosizioneREA = numIscrizionePosizioneREA;
	}
	public String getNumRegistroImprese() {
		return numRegistroImprese;
	}
	public void setNumRegistroImprese(String numRegistroImprese) {
		this.numRegistroImprese = numRegistroImprese;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}
	public String getSiglaProvinciaIscrizioneREA() {
		return siglaProvinciaIscrizioneREA;
	}
	public void setSiglaProvinciaIscrizioneREA(String siglaProvinciaIscrizioneREA) {
		this.siglaProvinciaIscrizioneREA = siglaProvinciaIscrizioneREA;
	}
	public String getTribunale() {
		return tribunale;
	}
	public void setTribunale(String tribunale) {
		this.tribunale = tribunale;
	}
    
}
