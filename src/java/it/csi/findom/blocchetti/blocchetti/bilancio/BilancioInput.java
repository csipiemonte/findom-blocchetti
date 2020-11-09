/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.findom.blocchetti.blocchetti.costituzioneImpresa.CostituzioneImpresaVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class BilancioInput extends CommonalityInput {

	@MapTo(target=MODEL,name="_bilancio")
	BilancioVO _bilancio;
	
	@MapTo(target=MODEL,name="_costituzioneImpresa")
	CostituzioneImpresaVo _costituzioneImpresa;
	
	@MapTo(target = REQUEST, name = "_command")
	String command;
	
	@MapTo(target=CONF,name="_bilancio_anno")
	String bilancio_anno;
	
	@MapTo(target=CONF,name="_bilancio_anno_readonly")
	String bilancio_anno_readonly;
	
	@MapTo(target=CONF,name="_bilancio_speseRS")
	String bilancio_speseRS;

	@MapTo(target=CONF,name="_bilancio_creditiVsClienti")               
	String bilancio_creditiVsClienti;
	
	@MapTo(target=CONF,name="_bilancio_debitiVsFornitori")              
	String bilancio_debitiVsFornitori;
	
	@MapTo(target=CONF,name="_bilancio_creditiCommScad")                
	String bilancio_creditiCommScad;
	
	@MapTo(target=CONF,name="_bilancio_disponibilitaLiquide")           
	String bilancio_disponibilitaLiquide;
	
	@MapTo(target=CONF,name="_bilancio_totaleBilancio")                 
	String bilancio_totaleBilancio;
	
	@MapTo(target=CONF,name="_bilancio_totalePatrimonio")               
	String bilancio_totalePatrimonio;
	
	@MapTo(target=CONF,name="_bilancio_debitiSoci")                     
	String bilancio_debitiSoci;
	
	@MapTo(target=CONF,name="_bilancio_debitiBanche")                   
	String bilancio_debitiBanche;
	
	@MapTo(target=CONF,name="_bilancio_debitiFornScad")                 
	String bilancio_debitiFornScad;
	
	@MapTo(target=CONF,name="_bilancio_debitiImpreseCollegate")         
	String bilancio_debitiImpreseCollegate;
	
	@MapTo(target=CONF,name="_bilancio_debitiControllanti")             
	String bilancio_debitiControllanti;
	
	@MapTo(target=CONF,name="_bilancio_debitiTributari")                
	String bilancio_debitiTributari;
	
	@MapTo(target=CONF,name="_bilancio_debitiTributariScad")            
	String bilancio_debitiTributariScad;
	
	@MapTo(target=CONF,name="_bilancio_ricavi")                         
	String bilancio_ricavi;
	
	@MapTo(target=CONF,name="_bilancio_totaleValoreProduzione")         
	String bilancio_totaleValoreProduzione;
	
	@MapTo(target=CONF,name="_bilancio_variazioneLavoriInCorso")        
	String bilancio_variazioneLavoriInCorso;
	
	@MapTo(target=CONF,name="_bilancio_ammortamentiImm")                
	String bilancio_ammortamentiImm;
	
	@MapTo(target=CONF,name="_bilancio_ammortamentiMat")                
	String bilancio_ammortamentiMat;
	
	@MapTo(target=CONF,name="_bilancio_totaleCostiProduzione")          
	String bilancio_totaleCostiProduzione;
	
	@MapTo(target=CONF,name="_bilancio_proventiFinanziari")             
	String bilancio_proventiFinanziari;
	
	@MapTo(target=CONF,name="_bilancio_interessiPassivi")               
	String bilancio_interessiPassivi;
	
	@MapTo(target=CONF,name="_bilancio_proventiGestioneAccessoria")     
	String bilancio_proventiGestioneAccessoria;
	
	@MapTo(target=CONF,name="_bilancio_oneriGestioneAccessoria")        
	String bilancio_oneriGestioneAccessoria;
	
	@MapTo(target=CONF,name="_bilancio_ebitda")                         
	String bilancio_ebitda;
	
	@MapTo(target=CONF,name="_bilancio_ebit")                           
	String bilancio_ebit;
	
	@MapTo(target=CONF,name="_bilancio_indiceRotazione")                
	String bilancio_indiceRotazione;
	
	@MapTo(target=CONF,name="_bilancio_dso")                            
	String bilancio_dso;
	
	@MapTo(target=CONF,name="_bilancio_dpo")                            
	String bilancio_dpo;
	
	@MapTo(target=CONF,name="_bilancio_ula")                            
	String bilancio_ula;

	@MapTo(target=CONF,name="_ente_impresa_dati_bilancio_custom")                            
	String ente_impresa_dati_bilancio_custom;
	
	@MapTo(target=CONF,name="_datiBilancio_bilanci_chiusi_customLabel")                            
	String _datiBilancio_bilanci_chiusi_customLabel;
	
	@MapTo(target=CONF,name="_campi_bilancio_obbligatori")                            
	String _campi_bilancio_obbligatori;
	
}
