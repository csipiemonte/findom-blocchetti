/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.schedaProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class VistaCriterioVO extends CommonalityVO {

	@MapTo(target = INHERIT, name = "idBando")
	Integer idBando;

	@MapTo(target = INHERIT, name = "descrBreveBando")
	String descrBreveBando;
	
	@MapTo(target = INHERIT, name = "idCriterio")
	Integer idCriterio;
	
	@MapTo(target = INHERIT, name = "idTipolIntervento")
	Integer idTipolIntervento;
	
	@MapTo(target = INHERIT, name = "descrBreveCriterio")
	String descrBreveCriterio;
	
	@MapTo(target = INHERIT, name = "descrizioneTipolIntervento")
	String descrizioneTipolIntervento;
	
	@MapTo(target = INHERIT, name = "descrizioneCriterio")
	String descrizioneCriterio;
	
	@MapTo(target = INHERIT, name = "ordineCriterio")
	Integer ordineCriterio;
	
	@MapTo(target = INHERIT, name = "idSpecifica")
	Integer idSpecifica;
	
	@MapTo(target = INHERIT, name = "descrBreveSpecifica")
	String descrBreveSpecifica;
	
	@MapTo(target = INHERIT, name = "descrizioneSpecifica")
	String descrizioneSpecifica;
	
	@MapTo(target = INHERIT, name = "ordineSpecifica")
	Integer ordineSpecifica;
	
	@MapTo(target = INHERIT, name = "tipoCampo")
	String tipoCampo;
	
	@MapTo(target = INHERIT, name = "idParametro")
	Integer idParametro;
	
	@MapTo(target = INHERIT, name = "descrBreveParametro")
	String descrBreveParametro;
	
	@MapTo(target = INHERIT, name = "descrizioneParametro")
	String descrizioneParametro;
	
	@MapTo(target = INHERIT, name = "ordineParametro")
	Integer ordineParametro;
	
	@MapTo(target = INHERIT, name = "punteggioParametro")
	String punteggioParametro;
	
	@MapTo(target = INHERIT, name = "idParametroValut")
	Integer idParametroValut;
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n CriterioVO [");
		sb.append(", idBando="+idBando);
		sb.append(", idCriterio="+idCriterio);
		sb.append(", descrBreveCriterio="+descrBreveCriterio);
//		sb.append(", descrBreveBando="+descrBreveBando);
		sb.append(", idTipolIntervento="+idTipolIntervento);
//		sb.append(", descrizioneTipolIntervento="+descrizioneTipolIntervento);
//		sb.append(", descrizioneCriterio="+descrizioneCriterio);
//		sb.append(", ordineCriterio="+ordineCriterio);
		sb.append(", idSpecifica="+idSpecifica);
//		sb.append(", descrizioneSpecifica="+descrizioneSpecifica);
//		sb.append(", descrBreveSpecifica="+descrBreveSpecifica);
		sb.append(", tipoCampo="+tipoCampo);
//		sb.append(", ordineSpecifica="+ordineSpecifica);
		sb.append(", idParametro="+idParametro);
//		sb.append(", descrizioneParametro="+descrizioneParametro);
//		sb.append(", descrBreveCriterio="+descrBreveParametro);
//		sb.append(", descrBreveCriterio="+ordineParametro);
//		sb.append(", idParametroValut="+idParametroValut);
//		sb.append(", punteggioParametro="+punteggioParametro);
		sb.append("]");
		return sb.toString();
	}
	
	public Integer getIdBando() {
		return idBando;
	}
	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}
	public String getDescrBreveCriterio() {
		return descrBreveCriterio;
	}
	public void setDescrBreveCriterio(String descrBreveCriterio) {
		this.descrBreveCriterio = descrBreveCriterio;
	}
	public Integer getIdCriterio() {
		return idCriterio;
	}
	public void setIdCriterio(Integer idCriterio) {
		this.idCriterio = idCriterio;
	}
	public String getDescrizioneCriterio() {
		return descrizioneCriterio;
	}
	public void setDescrizioneCriterio(String descrizioneCriterio) {
		this.descrizioneCriterio = descrizioneCriterio;
	}
	public Integer getOrdineCriterio() {
		return ordineCriterio;
	}
	public void setOrdineCriterio(Integer ordineCriterio) {
		this.ordineCriterio = ordineCriterio;
	}
	public Integer getIdSpecifica() {
		return idSpecifica;
	}
	public void setIdSpecifica(Integer idSpecifica) {
		this.idSpecifica = idSpecifica;
	}
	public String getDescrBreveSpecifica() {
		return descrBreveSpecifica;
	}
	public void setDescrBreveSpecifica(String descrBreveSpecifica) {
		this.descrBreveSpecifica = descrBreveSpecifica;
	}
	public String getDescrizioneSpecifica() {
		return descrizioneSpecifica;
	}
	public void setDescrizioneSpecifica(String descrizioneSpecifica) {
		this.descrizioneSpecifica = descrizioneSpecifica;
	}
	public Integer getOrdineSpecifica() {
		return ordineSpecifica;
	}
	public void setOrdineSpecifica(Integer ordineSpecifica) {
		this.ordineSpecifica = ordineSpecifica;
	}
	public String getTipoCampo() {
		return tipoCampo;
	}
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	public Integer getIdParametro() {
		return idParametro;
	}
	public void setIdParametro(Integer idParametro) {
		this.idParametro = idParametro;
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
	public String getDescrBreveBando() {
		return descrBreveBando;
	}
	public void setDescrBreveBando(String descrBreveBando) {
		this.descrBreveBando = descrBreveBando;
	}
	public Integer getIdTipolIntervento() {
		return idTipolIntervento;
	}
	public void setIdTipolIntervento(Integer idTipolIntervento) {
		this.idTipolIntervento = idTipolIntervento;
	}
	public String getDescrizioneTipolIntervento() {
		return descrizioneTipolIntervento;
	}
	public void setDescrizioneTipolIntervento(String descrizioneTipolIntervento) {
		this.descrizioneTipolIntervento = descrizioneTipolIntervento;
	}
	public String getPunteggioParametro() {
		return punteggioParametro;
	}
	public void setPunteggioParametro(String punteggioParametro) {
		this.punteggioParametro = punteggioParametro;
	}
	public Integer getIdParametroValut() {
		return idParametroValut;
	}
	public void setIdParametroValut(Integer idParametroValut) {
		this.idParametroValut = idParametroValut;
	}
}
