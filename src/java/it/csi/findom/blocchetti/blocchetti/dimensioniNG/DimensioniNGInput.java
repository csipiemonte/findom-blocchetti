/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.blocchetti.costituzioneImpresa.CostituzioneImpresaVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;


public class DimensioniNGInput extends CommonalityInput {

	@MapTo(target=MODEL,name="_costituzioneImpresa")
	CostituzioneImpresaVo _costituzioneImpresa;
	  
	@MapTo(target=MODEL,name="_dimensioni")
	DimensioniImpresaVO _dimensioni;
	
	@MapTo(target=CONF,name="_ente_impresa_dimensioni")
	String _ente_impresa_dimensioni;
	
	@MapTo(target=CONF,name="_anno_riferimento_per_storico")
	Integer _anno_riferimento_per_storico;
	
	/**
	 * Per bandi che non richiedono Dimensioni impresa: Grande
	 * Esempio: Voucher
	 */
	@MapTo(target=CONF,name="_assenza_dimensioneImpresa_grande")
	String _assenza_dimensioneImpresa_grande;
	
	@MapTo(target=CONF,name="_assenza_dimensioneImpresa_media")
	String _assenza_dimensioneImpresa_media;
	
	
	/**
	 * Per bandi che non richiedono Dimensioni impresa: Grande
	 * in riferimento al beneficiario
	 * Esempio: VoucherIR
	 */
	@MapTo(target=CONF,name="_dimensione_no_grande_tipol_beneficiario")
	String _dimensione_no_grande_tipol_beneficiario;
	
	/**
	 * Jira: 1587
	 */
	@MapTo(target=CONF,name="_dimensioni_risorse_umane")
	String _dimensioni_risorse_umane;
	
	@MapTo(target=CONF,name="_dimensioni_dim_impresa")
	String _dimensioni_dim_impresa;
	

	
}
