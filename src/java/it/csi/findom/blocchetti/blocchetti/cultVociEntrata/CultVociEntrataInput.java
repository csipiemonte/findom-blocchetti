/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultVociEntrata;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazione.CultFormaAgevolazioneVO;
import it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB.CultFormaAgevolazioneBVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CultVociEntrataInput extends CommonalityInput {

    @MapTo(target=MODEL,name="_vociEntrata")
   	CultVociEntrataVO _vociEntrata;
    
    @MapTo(target=MODEL,name="_formaAgevolazione")
   	CultFormaAgevolazioneVO _formaAgevolazione;
        
    @MapTo(target=REQUEST,name="aggiornaRiepilogo")
    String aggiornaRiepilogo;
    
	@MapTo(target=MODEL,name="_formaAgevolazioneB")
	CultFormaAgevolazioneBVO _formaAgevolazioneB;
	
	@MapTo(target=CONF,name="_cultPianoSpese_procedure_tot_spese")
	String _cultPianoSpese_procedure_tot_spese;
    
}
    
