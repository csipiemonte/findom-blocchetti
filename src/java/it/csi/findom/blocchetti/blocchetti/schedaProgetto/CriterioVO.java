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

public class CriterioVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT, name = "idBando")
	Integer idBando;
	
	@MapTo(target = INHERIT, name = "idCriterio")
	Integer idCriterio;
	
	@MapTo(target = INHERIT, name = "descrBreveCriterio")
	String descrBreveCriterio;
	
	@MapTo(target = INHERIT, name = "descrizioneCriterio")
	String descrizioneCriterio;
	
	@MapTo(target = INHERIT, name = "ordineCriterio")
	Integer ordineCriterio;
	
	@MapTo(target = INHERIT, name = "idTipolIntervento")
	Integer idTipolIntervento;
	
	@MapTo(target = INHERIT, name = "specificheList")  //mappa il dato che arriva dall'XML
	SpecificaVO[] arraySpecifiche;
	
	// serve per disegnare il template.xhtml
	@MapTo(target = MODEL)
	List<SpecificaVO> listaSpecifiche;
	
	// e' valore ricalcolato
	@MapTo(target = MODEL)
	Integer posizioneCriterio;
	
	// serve per comporre la struttura dei criteri leggendola dal DB
	TreeMap<Integer, SpecificaVO> mappaSpecifiche;

	// Diventa "false" se sono vere le seguenti
	// input.schedaProgettoDipendeDaCaratteristicheProgetto = true
	// tipologia di intervento relativa al criterio e' selezionata
	// Se "false" il criterio e' in sola lettura
	@MapTo(target = INHERIT, name = "abilitato")
	String abilitato;
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n CriterioVO {idBando=" + idBando);
		sb.append(", idCriterio=" + idCriterio);
		sb.append(", posizioneCriterio=" + posizioneCriterio);
		sb.append(", descrBreveCriterio=" + descrBreveCriterio);
//		sb.append(", descrizioneCriterio=" + descrizioneCriterio);
//		sb.append(", ordineCriterio=" + ordineCriterio);
		sb.append(", idTipolIntervento=" + idTipolIntervento);
		sb.append(", abilitato=" + abilitato);
		
		if (listaSpecifiche!=null){
			sb.append(", listaSpecifiche=[");
			for (SpecificaVO sp : listaSpecifiche) {
				sb.append(sp.toString());
			}
			sb.append("]");
		}else{
			sb.append(", listaSpecifiche=null");
		}
		
		if (mappaSpecifiche!=null){
			sb.append(", mappaSpecifiche=[");
			for (Integer id1 : mappaSpecifiche.keySet()) {
				sb.append(mappaSpecifiche.get(id1).toString());
			}
			sb.append("]");
		}else{
			sb.append(", mappaSpecifiche=null");
		}
		
		if(arraySpecifiche!=null){
			sb.append(", arraySpecifiche=[");
			for (int i = 0; i < arraySpecifiche.length; i++) {
				sb.append(arraySpecifiche[i].toString());
			}
			sb.append("]");
		}else{
			sb.append(", arraySpecifiche=null");
		}
		sb.append("}");
		return sb.toString();
	}

	// GETTERS && SETTERS
	
	public Integer getIdBando() {
		return idBando;
	}

	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}

	public Integer getIdCriterio() {
		return idCriterio;
	}

	public void setIdCriterio(Integer idCriterio) {
		this.idCriterio = idCriterio;
	}

	public String getDescrBreveCriterio() {
		return descrBreveCriterio;
	}

	public void setDescrBreveCriterio(String descrBreveCriterio) {
		this.descrBreveCriterio = descrBreveCriterio;
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

	public void setMappaSpecifiche(TreeMap<Integer, SpecificaVO> mappaSpecifiche) {
		this.mappaSpecifiche = mappaSpecifiche;
	}

	public TreeMap<Integer, SpecificaVO> getMappaSpecifiche() {
		return mappaSpecifiche;
	}

	public Integer getIdTipolIntervento() {
		return idTipolIntervento;
	}

	public void setIdTipolIntervento(Integer idTipolIntervento) {
		this.idTipolIntervento = idTipolIntervento;
	}

	public SpecificaVO[] getArraySpecifiche() {
		return arraySpecifiche;
	}

	public void setArraySpecifiche(SpecificaVO[] arraySpecifiche) {
		this.arraySpecifiche = arraySpecifiche;
	}

	public List<SpecificaVO> getListaSpecifiche() {
		return listaSpecifiche;
	}

	public void setListaSpecifiche(List<SpecificaVO> listaSpecifiche) {
		this.listaSpecifiche = listaSpecifiche;
	}

	public Integer getPosizioneCriterio() {
		return posizioneCriterio;
	}

	public void setPosizioneCriterio(Integer posizioneCriterio) {
		this.posizioneCriterio = posizioneCriterio;
	}

	public String getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(String abilitato) {
		this.abilitato = abilitato;
	}

}
