/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.schedaProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class ParametroVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT, name = "idParametro")
	Integer idParametro;
	
	@MapTo(target = INHERIT, name = "idSpecifica")
	Integer idSpecifica;
	
	@MapTo(target = INHERIT, name = "idCriterio")
	Integer idCriterio;
	
	@MapTo(target = INHERIT, name = "descrBreveParametro")
	String descrBreveParametro;
	
	@MapTo(target = INHERIT, name = "descrizioneParametro")
	String descrizioneParametro;
	
	@MapTo(target = INHERIT, name = "ordineParametro")
	Integer ordineParametro;
	
	@MapTo(target = INHERIT, name = "punteggioParametro")
	String punteggioParametro;
	
	// lo tratto come stringa perche freemarker lo scrive con il punto delle migliaia
	@MapTo(target = INHERIT, name = "idParametroValut")
	String idParametroValut;
	
	@MapTo(target = INHERIT, name = "checked")
	String checked;
	
	public Integer getIdParametro() {
		return idParametro;
	}
	public void setIdParametro(Integer idParametro) {
		this.idParametro = idParametro;
	}
	public Integer getIdSpecifica() {
		return idSpecifica;
	}
	public void setIdSpecifica(Integer idSpecifica) {
		this.idSpecifica = idSpecifica;
	}
	public Integer getIdCriterio() {
		return idCriterio;
	}
	public void setIdCriterio(Integer idCriterio) {
		this.idCriterio = idCriterio;
	}
	public String getDescrBreveParametro() {
		return descrBreveParametro;
	}
	public void setDescrBreveParametro(String descrBreveParametro) {
		this.descrBreveParametro = descrBreveParametro;
	}
	public String getDescrizioneParametro() {
		return descrizioneParametro;
	}
	public void setDescrizioneParametro(String descrizioneParametro) {
		this.descrizioneParametro = descrizioneParametro;
	}
	public Integer getOrdineParametro() {
		return ordineParametro;
	}
	public void setOrdineParametro(Integer ordineParametro) {
		this.ordineParametro = ordineParametro;
	}
	public String getPunteggioParametro() {
		return punteggioParametro;
	}
	public void setPunteggioParametro(String punteggioParametro) {
		this.punteggioParametro = punteggioParametro;
	}
	public String getIdParametroValut() {
		return idParametroValut;
	}
	public void setIdParametroValut(String idParam) {
		if(idParam!=null && idParam.contains(".")){
			idParam.replace(".", "");
		}
		this.idParametroValut = idParam;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("ParametroVO [");
		sb.append(" idParametro=" + idParametro);
//		sb.append(", idSpecifica=" + idSpecifica);
//		sb.append(", idCriterio=" + idCriterio);
		sb.append(", descrBreveParametro=" + descrBreveParametro);
//		sb.append(", descrizioneParametro=" + descrizioneParametro);
//		sb.append(", ordineParametro=" + ordineParametro);
//		sb.append(", punteggioParametro=" + punteggioParametro);
		sb.append(", idParametroValut=" + idParametroValut);
//		sb.append(", checked=" + checked);
		sb.append("]");
		
		return sb.toString();
	}
	

}
