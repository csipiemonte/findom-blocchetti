/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultVociEntrata;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class CultVociEntrataOutput extends CommonalityOutput {
    
    @MapTo(target=NAMESPACE)
   	List<VoceEntrataItemVO> vociEntrataList;	

    @MapTo(target=NAMESPACE)
   	List<VoceEntrataItemVO> vociEntrataScelteList;	
    
    @MapTo(target=NAMESPACE)
   	List<VoceEntrataItemVO> vociEntrataRiepilogoList;
    
    @MapTo(target=NAMESPACE)
   	String totaleGenerale;
    
    @MapTo(target=NAMESPACE)
   	String viewWarningEntrateAgevolazioni;
}
    
