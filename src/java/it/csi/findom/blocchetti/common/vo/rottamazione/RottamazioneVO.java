/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.rottamazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RottamazioneVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "idTipo")
	String idTipo;
	@MapTo(target = INHERIT, name = "tipo")
	String tipo;
	@MapTo(target = INHERIT, name = "idCategoria")
	String idCategoria;	
	@MapTo(target = INHERIT, name = "descrCategoria")
	String descrCategoria;
	@MapTo(target = INHERIT, name = "marca")
	String marca;
	@MapTo(target = INHERIT, name = "modello")
	String modello;
	@MapTo(target = INHERIT, name = "targa")
	String targa;
	@MapTo(target = INHERIT, name = "idAlimentazione")
	String idAlimentazione;
	@MapTo(target = INHERIT, name = "descrAlimentazione")
	String descrAlimentazione;
	
	// jira 2104 in data 05/10/2020
	@MapTo(target = INHERIT, name = "telaio")
	String telaio;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getDescrCategoria() {
		return descrCategoria;
	}
	public void setDescrCategoria(String descrCategoria) {
		this.descrCategoria = descrCategoria;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModello() {
		return modello;
	}
	public void setModello(String modello) {
		this.modello = modello;
	}
	public String getTarga() {
		return targa;
	}
	public void setTarga(String targa) {
		this.targa = targa;
	}
	public String getIdAlimentazione() {
		return idAlimentazione;
	}
	public void setIdAlimentazione(String idAlimentazione) {
		this.idAlimentazione = idAlimentazione;
	}
	public String getDescrAlimentazione() {
		return descrAlimentazione;
	}
	public void setDescrAlimentazione(String descrAlimentazione) {
		this.descrAlimentazione = descrAlimentazione;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public String getTelaio() {
		return telaio;
	}
	
	public void setTelaio(String telaio) {
		if(telaio == null || telaio.isEmpty()){
			this.telaio = "-";
		}else{
			this.telaio = telaio;
		}
	}
	
	public String toString() {
		return "RottamazioneVO [idTipo=" + idTipo + ", tipo=" + tipo
				+ ", idCategoria=" + idCategoria + ", descrCategoria="
				+ descrCategoria + ", marca=" + marca + ", modello=" + modello
				+ ", targa=" + targa + ", idAlimentazione=" + idAlimentazione
				+ ", descrAlimentazione=" + descrAlimentazione + "]";
	}
	
}
