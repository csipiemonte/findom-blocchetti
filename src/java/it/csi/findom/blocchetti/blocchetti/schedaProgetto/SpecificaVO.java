/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.schedaProgetto;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;
import java.util.TreeMap;

public class SpecificaVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "idSpecifica")
	Integer idSpecifica;
	
	@MapTo(target = INHERIT, name = "idCriterio")
	Integer idCriterio;
	
	@MapTo(target = INHERIT, name = "descrBreveSpecifica")
	String descrBreveSpecifica;
	
	@MapTo(target = INHERIT, name = "descrizioneSpecifica")
	String descrizioneSpecifica;
	
	@MapTo(target = INHERIT, name = "ordineSpecifica")
	Integer ordineSpecifica;
	
	@MapTo(target = INHERIT, name = "tipoCampo")
	String tipoCampo;
	
	@MapTo(target = INHERIT, name = "parametriList")
	ParametroVO[] arrayParametri;
	
	// serve per disegnare il template.xhtml
	@MapTo(target = MODEL)
	List<ParametroVO> listaParametri;
	
	// serve per comporre la struttura dei criteri leggendola dal DB
	TreeMap<Integer, ParametroVO>  mappaParametri;
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("SpecificaVO [idSpecifica=" + idSpecifica);
		sb.append(", idCriterio=" + idCriterio);
		sb.append(", descrBreveSpecifica=" + descrBreveSpecifica);
//		sb.append(", descrizioneSpecifica=" + descrizioneSpecifica);
//		sb.append(", ordineSpecifica=" + ordineSpecifica);
//		sb.append(", tipoCampo=" + tipoCampo);
		
		if (listaParametri!=null){
			sb.append(", listaParametri=(");
			for (ParametroVO sp : listaParametri) {
				sb.append(sp.toString());
			}
			sb.append(")");
		}else{
			sb.append(", listaParametri=null");
		}
		
		if(arrayParametri!=null){
			sb.append(", arrayParametri=[");
			for (int i = 0; i < arrayParametri.length; i++) {
				sb.append(arrayParametri[i].toString());
			}
			sb.append("]");
		}else{
			sb.append(", arrayParametri=null");
		}
		
		if (mappaParametri!=null){
			sb.append(", mappaParametri=[");
			for (Integer id1 : mappaParametri.keySet()) {
				sb.append(mappaParametri.get(id1).toString());
			}
			sb.append("]");
		}else{
			sb.append(", mappaParametri=null");
		}
		sb.append("}");
		
		return sb.toString();
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

	public List<ParametroVO> getListaParametri() {
		return listaParametri;
	}

	public void setListaParametri(List<ParametroVO> listaParametri) {
		this.listaParametri = listaParametri;
	}
	
	public TreeMap<Integer, ParametroVO> getMappaParametri() {
		return mappaParametri;
	}

	public void setMappaParametri(TreeMap<Integer, ParametroVO> mappaParametri) {
		this.mappaParametri = mappaParametri;
	}

	public ParametroVO[] getArrayParametri() {
		return arrayParametri;
	}

	public void setArrayParametri(ParametroVO[] arrayParametri) {
		this.arrayParametri = arrayParametri;
	}
}
