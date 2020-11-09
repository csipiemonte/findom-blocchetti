/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura;

import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.findom.blocchetti.blocchetti.formaFinanziamento.FormaFinanziamentoVO;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.findom.blocchetti.blocchetti.soggettoDelegato.SoggettoDelegatoVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.findom.blocchetti.blocchetti.dichiarazioni96.DichiarazioniNGVO;
import it.csi.findom.blocchetti.blocchetti.estremiBancari.EstremiBancariVO;
import it.csi.findom.blocchetti.common.vo.documentazione.DocumentazioneVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;
import it.csi.findom.blocchetti.blocchetti.entiProgetto.EntiProgettoVO;
import it.csi.findom.blocchetti.blocchetti.sedeLegale.SedeLegaleVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;
import it.csi.findom.blocchetti.common.vo.sede.SediVO;

public class DirettivaInput extends SectionPageInput {

		@MapTo(target=MODEL,name="_operatorePresentatore")
		OperatorePresentatoreVo _operatorePresentatore;
		
		@MapTo(target=MODEL,name="_sedeLegale")
		SedeLegaleVO _sedeLegale;
		
		@MapTo(target=MODEL,name="_estremiBancari")
		EstremiBancariVO _estremiBancari;
		
		@MapTo(target=MODEL,name="_soggettoDelegato")
		SoggettoDelegatoVO _soggettoDelegato;
		
		@MapTo(target=MODEL,name="_legaleRappresentante")
		LegaleRappresentanteVO _legaleRappresentante;
		
		@MapTo(target=MODEL,name="_domanda")
		DomandaNGVO _domanda;
		
		@MapTo(target=MODEL,name="_entiProgetto")
		EntiProgettoVO _entiProgetto;
		
		
		@MapTo(target=MODEL,name="_documentazione")
		DocumentazioneVO _documentazione; 
		
		@MapTo(target=MODEL,name="_dichiarazioni")
		DichiarazioniNGVO _dichiarazioni; 
		
		@MapTo(target=MODEL,name="_allegati")
		AllegatiVO _allegati;
		
		@MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
		CaratteristicheProgettoNGVO _caratteristicheProgetto;

		@MapTo(target=MapTarget.MODEL ,name="_pianoSpese")
		PianoSpeseVO _pianoSpese;
		
		@MapTo(target=MapTarget.MODEL ,name="_formaFinanziamento")
		FormaFinanziamentoVO _formaFinanziamento;
		
		@MapTo(target=MapTarget.MODEL ,name="_tipologiaAiuto")
		TipologiaAiutoNGVO _tipologiaAiuto;
		
		@MapTo(target = MODEL, name = "_sedi")
		SediVO _sedi;
		
}

