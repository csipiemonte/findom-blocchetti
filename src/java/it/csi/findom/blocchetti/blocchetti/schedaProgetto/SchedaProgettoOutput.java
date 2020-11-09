/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.schedaProgetto;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class SchedaProgettoOutput extends CommonalityOutput {
	
	@MapTo(target=MapTarget.NAMESPACE)
	List<CriterioVO> listaCriteri;

	// serve per la verifica delle pagine compilate per l'indice
	@MapTo(target=MapTarget.NAMESPACE)
	Integer numeroCriteriTotali;
	
	// serve per abilitare o meno i bottoni "Cancella" , "Salva", "Rirpristina"
	@MapTo(target=MapTarget.NAMESPACE)
	String bottoniAbilitati;
	
	/** 
	 * : :
	 * - Asd sport covid 2019 
	 **/
	@MapTo(target=MapTarget.NAMESPACE)
	String descrizioneTipolIntervento;
}
