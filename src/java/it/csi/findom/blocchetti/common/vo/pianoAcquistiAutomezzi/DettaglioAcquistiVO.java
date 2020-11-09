/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.pianoAcquistiAutomezzi;

import it.csi.findom.blocchetti.common.vo.veicoli.CategoriaVeicoloVOV3;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class DettaglioAcquistiVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = MapTarget.INHERIT)
	List<CategoriaVeicoloVOV3> vociEmissioniList;
	
	@MapTo(target = MapTarget.INHERIT)
	String identificativo;
	
	@MapTo(target=MapTarget.INHERIT)
	String daCancellare;
	
	@MapTo(target=MapTarget.INHERIT)
	String titolo;

	@MapTo(target=MapTarget.INHERIT)
	String idRecord;
	
	@MapTo(target=MapTarget.INHERIT)
	String idVoceSpesa;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrVoceSpesa;
		
	@MapTo(target=MapTarget.INHERIT)
	String idMassaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrMassaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String idCategoriaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrCategoriaVeicolo;
	
	@MapTo(target=MapTarget.INHERIT)
	String importoCanone;
	
	@MapTo(target=MapTarget.INHERIT)
	String importoCanoneSoloLettura;
	
	@MapTo(target=MapTarget.INHERIT)
	String importoContributo;
	

	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioniCo2;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioniNox;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String emissioni;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String tipoRecordEmissione;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String targa;
	
	/** jira 2071 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String checkAutoAcquistata;
	
	/** jira 2701 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String idEmissione;
	
	@MapTo(target=MapTarget.INHERIT)
	String descrEmissioneVeicolo;
	
	/** jira 2105 -2r */
	@MapTo(target=MapTarget.INHERIT)
	String telaio;
	
	
	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getDaCancellare() {
		return daCancellare;
	}

	public void setDaCancellare(String daCancellare) {
		this.daCancellare = daCancellare;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getIdRecord() {
		return idRecord;
	}

	public void setIdRecord(String idRecord) {
		this.idRecord = idRecord;
	}

	public String getIdVoceSpesa() {
		return idVoceSpesa;
	}

	public void setIdVoceSpesa(String idVoceSpesa) {
		this.idVoceSpesa = idVoceSpesa;
	}

	public String getDescrVoceSpesa() {
		return descrVoceSpesa;
	}

	public void setDescrVoceSpesa(String descrVoceSpesa) {
		this.descrVoceSpesa = descrVoceSpesa;
	}

	public String getIdMassaVeicolo() {
		return idMassaVeicolo;
	}

	public void setIdMassaVeicolo(String idMassaVeicolo) {
		this.idMassaVeicolo = idMassaVeicolo;
	}

	public String getDescrMassaVeicolo() {
		return descrMassaVeicolo;
	}

	public void setDescrMassaVeicolo(String descrMassaVeicolo) {
		this.descrMassaVeicolo = descrMassaVeicolo;
	}

	public String getIdCategoriaVeicolo() {
		return idCategoriaVeicolo;
	}

	public void setIdCategoriaVeicolo(String idCategoriaVeicolo) {
		this.idCategoriaVeicolo = idCategoriaVeicolo;
	}

	public String getDescrCategoriaVeicolo() {
		return descrCategoriaVeicolo;
	}

	public void setDescrCategoriaVeicolo(String descrCategoriaVeicolo) {
		this.descrCategoriaVeicolo = descrCategoriaVeicolo;
	}

	public String getImportoCanone() {
		return importoCanone;
	}

	public void setImportoCanone(String importoCanone) {
		this.importoCanone = importoCanone;
	}

	public String getImportoCanoneSoloLettura() {
		return importoCanoneSoloLettura;
	}

	public void setImportoCanoneSoloLettura(String importoCanoneSoloLettura) {
		this.importoCanoneSoloLettura = importoCanoneSoloLettura;
	}

	public String getImportoContributo() {
		return importoContributo;
	}

	public void setImportoContributo(String importoContributo) {
		this.importoContributo = importoContributo;
	}

	public String getEmissioniCo2() {
		return emissioniCo2;
	}

	public void setEmissioniCo2(String emissioniCo2) {
		this.emissioniCo2 = emissioniCo2;
	}

	public String getEmissioniNox() {
		return emissioniNox;
	}

	public void setEmissioniNox(String emissioniNox) {
		this.emissioniNox = emissioniNox;
	}

	public String getEmissioni() {
		return emissioni;
	}

	public void setEmissioni(String emissioni) {
		this.emissioni = emissioni;
	}

	public String getTipoRecordEmissione() {
		return tipoRecordEmissione;
	}

	public void setTipoRecordEmissione(String tipoRecordEmissione) {
		this.tipoRecordEmissione = tipoRecordEmissione;
	}

	public String getTarga() {
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

	public String getIdEmissione() {
		return idEmissione;
	}

	public void setIdEmissione(String idEmissione) {
		this.idEmissione = idEmissione;
	}

	public String getCheckAutoAcquistata() {
		return checkAutoAcquistata;
	}

	public void setCheckAutoAcquistata(String checkAutoAcquistata) {
		this.checkAutoAcquistata = checkAutoAcquistata;
	}
	
	
	
	public List<CategoriaVeicoloVOV3> getVociEmissioniList() {
		return vociEmissioniList;
	}

	public void setVociEmissioniList(List<CategoriaVeicoloVOV3> vociEmissioniList) {
		this.vociEmissioniList = vociEmissioniList;
	}
	public String getDescrEmissioneVeicolo() {
		return descrEmissioneVeicolo;
	}

	public void setDescrEmissioneVeicolo(String descrEmissioneVeicolo) {
		this.descrEmissioneVeicolo = descrEmissioneVeicolo;
	}
	
	public String getTelaio() {
		return telaio;
	}

	public void setTelaio(String telaio) {
		this.telaio = telaio;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DettaglioAcquistiVO [");
		sb.append(" identificativo=" + identificativo );
		sb.append(", daCancellare=" + daCancellare );
		sb.append(", titolo=" + titolo );
		sb.append(", idRecord=" + idRecord );
		sb.append(", idVoceSpesa=" + idVoceSpesa );
		sb.append(", descrVoceSpesa=" + descrVoceSpesa );
		sb.append(", idMassaVeicolo=" + idMassaVeicolo );
		sb.append(", descrMassaVeicolo=" + descrMassaVeicolo );
		sb.append(", idCategoriaVeicolo=" + idCategoriaVeicolo );
		sb.append(", descrCategoriaVeicolo=" + descrCategoriaVeicolo );
		sb.append(", importoCanone=" + importoCanone );
		sb.append(", importoCanoneSoloLettura=" + importoCanoneSoloLettura );
		sb.append(", importoContributo=" + importoContributo );
		sb.append(", emissioniCo2=" + emissioniCo2 );
		sb.append(", emissioniNox=" + emissioniNox );
		sb.append(", emissioni=" + emissioni );
		sb.append(", tipoRecordEmissione=" + tipoRecordEmissione );
		sb.append(", targa=" + targa );
		sb.append(", telaio=" + telaio );
		sb.append(", checkAutoAcquistata=" + checkAutoAcquistata );
		sb.append(", idEmissione=" + idEmissione );
		sb.append(", descrEmissioneVeicolo=" + descrEmissioneVeicolo );
		sb.append("]");
		return sb.toString();
	}

}
