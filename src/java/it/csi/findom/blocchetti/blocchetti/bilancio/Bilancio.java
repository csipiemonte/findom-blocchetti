/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import it.csi.findom.blocchetti.blocchetti.costituzioneImpresa.CostituzioneImpresaVo;
import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.util.TrasformaClassiAAEP2VO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.ws.aaep.AaepWs;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;


public class Bilancio extends Commonality {

	// Costanti: voci restituite da AAEP
	private static final String SPESERS = "B.I.2 costi di ricerca, di sviluppo e di pubblicita'";
	private static final String TOTCREDITIVSCLIENTI = "C.II.1. Totale crediti verso clienti";
	private static final String STATOPATRIMONIALE = "Stato patrimoniale";
	private static final String TOTDISPLIQUIDE = "C.IV. Totale disponibilita' liquide";
	private static final String TOTALEATTIVO = "Totale attivo";
	private static final String TOTPATRNETTO = "Totale patrimonio netto";
	private static final String TOTDEBITI = "D.3. Totale debiti verso soci per finanziamenti";
	private static final String TOTDEBITIVSBANCHE = "D.4. Totale debiti verso banche";
	private static final String TOTDEBITIVSFORNITORI = "D.7. Totale debiti verso fornitori";
	private static final String TOTDEBITIVSIMPCOLLEGATE = "D.10. Totale debiti verso imprese collegate";
	private static final String TOTDEBITIVSIMPCONTROLLANTI = "D.11. Totale debiti verso imprese controllanti";
	private static final String TOTDEBITITRIBUTARI = "D.12. Totale debiti tributari";
	private static final String RICAVI = "A.1. ricavi delle vendite e delle prestazioni";
	private static final String TOTVALPRODUZIONE = "A. Valore della produzione";
	private static final String VARIAZIONELAVORIINCORSO = "A.3. variazione dei lavori in corso su ordinazione";
	private static final String AMMIMMATERIALI = "B.10.a. ammortamento delle immobilizzazioni immateriali";
	private static final String AMMMATERIALI = "B.10.b. ammortamento delle immobilizzazioni materiali";
	private static final String TOTCOSTIPRODUZIONE = "B. Totale costi della produzione";
	private static final String PROVENTIFIN = "C.16. Totale altri proventi finanziari";
	private static final String TOTINTERESSI = "C.17. Totale interessi e altri oneri finanziari";
	private static final String TOTPROVENTI = "E.20. Totale proventi";
	private static final String TOTONERI = "E.21. Totale oneri";
	private static final String DIFFAB = "Differenza tra valore e costi della produzione (A - B)";
	
	private static final String VALATT = "valoreAttuale";
	private static final String VALPREC = "valorePrecedente";
	
	  BilancioInput input = new BilancioInput();

	  @Override
	  public BilancioInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[Bilancio::inject] BEGIN");
	    
	    String mostraMsgAAEP = "N";
	    try {
	        BilancioOutput ns = new BilancioOutput();
	        
			logger.info("[Bilancio::inject] _bilancio BEGIN");
			
			//// dichiarazioni
			String ebitda = "";
			String ebitdaPrec = "";		
			String ebit = "";
			String ebitPrec = "";
			BilancioVO aaepMap = new BilancioVO(); // dati provenienti da AAEP
			BilancioVO bilMap = new BilancioVO(); // oggetto da usare nel template
			
			boolean isDatiBilancioCustom = false;
			
			//  Gestione anno corrente
			Calendar cal = Calendar.getInstance();
			String annoCorrente = ""+cal.get(Calendar.YEAR);		// 2018
			String annoCorrenteM1 = ""+ (cal.get(Calendar.YEAR)-1);	// 2017
			String annoCorrenteM2 = ""+ (cal.get(Calendar.YEAR)-2);	// 2016
			
			String annoXML = null;
			String annoXMLPrec = null;
			
			// Gestione fonte dati CR-4
			String fonteDatiAnno = "XML";
			String fonteDatiAnnoPrec = "XML";
			
			if ("true".equals(input.ente_impresa_dati_bilancio_custom)) {
				isDatiBilancioCustom = true;
			}

			ImpresaVO enteImpresa = (ImpresaVO)TrasformaClassiAAEP2VO.impresa2ImpresaVO((Impresa)SessionCache.getInstance().get("enteImpresa"));
			
			String operazione = "INS";
			Integer idDomanda = info.getStatusInfo().getNumProposta();

			String nodoBilancio = BilancioDAO.getNodoBilancio(idDomanda, logger);
			
			if (StringUtils.isNotEmpty(nodoBilancio)) {
				operazione = "MOD";							
			}
			logger.info("[Bilancio::inject] operazione=" + operazione);
			
			if(enteImpresa!=null){
				logger.info("[Bilancio::inject] enteImpresa idAzienda="+enteImpresa.getIdAzienda());
				// enteImpresa in sessione arriva da AAEP
			}else{
				logger.info("[Bilancio::inject] enteImpresa non in sessione");
				enteImpresa = new ImpresaVO();
			}
			
			BilancioVO _bilancio = input._bilancio;
			logger.info("[Bilancio::inject] _bilancio:" + _bilancio);

			String comando = input.command;	
			logger.info("Comando eseguito: " + comando);
			
			// accedo per la prima volta alla pagina: non ho dati in xml ne da AAEP, ne configurazioni custom 
			if (operazione.equalsIgnoreCase("INS") && _bilancio==null){
				if( enteImpresa != null && enteImpresa.getIdAzienda()!=null ){
					
					logger.info("[Bilancio::inject] idAzienda:" + enteImpresa.getIdAzienda());
					BilancioAziendaAAEPVO bilancioAAEP = null;
					
					try {
						bilancioAAEP = (BilancioAziendaAAEPVO) AaepWs.getInstance().getBilancioAziendaFromAAEP(enteImpresa.getIdAzienda(), logger);
						mostraMsgAAEP = "S";
					}catch(Exception e){
						logger.error("Exception nel richiamo di getBilancioAzienda per azienda " + enteImpresa.getIdAzienda());
						logger.error(e.getMessage());
					}
					// qui recupero dati da AAEP
					if(bilancioAAEP!=null){
						
						Map mappaAAEP = generaMappaFromAAEP(bilancioAAEP, logger);
						
						logger.info("[Bilancio::inject] mappaAAEP:" + mappaAAEP);

						if( !isDatiBilancioCustom ){
							
							bilMap.setAnno("31/12/" + (String)getMapAttributi(mappaAAEP, STATOPATRIMONIALE).get(VALATT));
							
							String annoPrecedente = (String) (String) (String)getMapAttributi(mappaAAEP, STATOPATRIMONIALE).get(VALPREC);
							if(StringUtils.isNotBlank(annoPrecedente)){
								bilMap.setAnnoPrec("31/12/" + annoPrecedente);
							}else{
								bilMap.setAnnoPrec("");
							}
							
							String speseRS = (String) (String)getMapAttributi(mappaAAEP, SPESERS).get(VALATT);
							bilMap.setSpeseRS(DecimalFormat.decimalFormat(speseRS,2));
							
							String speseRSPrec = (String) (String)getMapAttributi(mappaAAEP, SPESERS).get(VALPREC);
							bilMap.setSpeseRSPrec(DecimalFormat.decimalFormat(speseRSPrec,2));
							
							String totCreditiVsClienti = (String) (String)getMapAttributi(mappaAAEP, TOTCREDITIVSCLIENTI).get(VALATT);
							bilMap.setTotCreditiVsClienti(DecimalFormat.decimalFormat(totCreditiVsClienti,2));
							
							String totCreditiVsClientiPrec = (String) (String)getMapAttributi(mappaAAEP, TOTCREDITIVSCLIENTI).get(VALPREC);
							bilMap.setTotCreditiVsClientiPrec(DecimalFormat.decimalFormat(totCreditiVsClientiPrec,2));
							
							bilMap.setCreditiCommScad(""); 		// Voce non presente in AAEP
							bilMap.setCreditiCommScadPrec("");	// Voce non presente in AAEP
							
							String disponibilitaLiquide = (String) (String)getMapAttributi(mappaAAEP, TOTDISPLIQUIDE).get(VALATT);
							bilMap.setDisponibilitaLiquide(DecimalFormat.decimalFormat(disponibilitaLiquide,2));
							
							String disponibilitaLiquidePrec = (String) (String)getMapAttributi(mappaAAEP, TOTDISPLIQUIDE).get(VALPREC);
							bilMap.setDisponibilitaLiquidePrec(DecimalFormat.decimalFormat(disponibilitaLiquidePrec,2));
							
							String totaleBilancio = (String) (String)getMapAttributi(mappaAAEP, TOTALEATTIVO).get(VALATT);
							bilMap.setTotaleBilancio(DecimalFormat.decimalFormat(totaleBilancio,2));
							
							String totaleBilancioPrec = (String) (String)getMapAttributi(mappaAAEP, TOTALEATTIVO).get(VALPREC);
							bilMap.setTotaleBilancioPrec(DecimalFormat.decimalFormat(totaleBilancioPrec,2));
							
							String totalePatrimonio = (String) (String)getMapAttributi(mappaAAEP, TOTPATRNETTO).get(VALATT);
							bilMap.setTotalePatrimonio(DecimalFormat.decimalFormat(totalePatrimonio,2));
							
							String totalePatrimonioPrec = (String) (String)getMapAttributi(mappaAAEP, TOTPATRNETTO).get(VALPREC);
							bilMap.setTotalePatrimonioPrec(DecimalFormat.decimalFormat(totalePatrimonioPrec,2));
							
							String debitiSoci = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITI).get(VALATT);
							bilMap.setDebitiSoci(DecimalFormat.decimalFormat(debitiSoci,2));
							
							String debitiSociPrec = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITI).get(VALPREC);
							bilMap.setDebitiSociPrec(DecimalFormat.decimalFormat(debitiSociPrec,2));
							
							String debitiBanche = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSBANCHE).get(VALATT);
							bilMap.setDebitiBanche(DecimalFormat.decimalFormat(debitiBanche,2));
							
							String debitiBanchePrec = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSBANCHE).get(VALPREC);
							bilMap.setDebitiBanchePrec(DecimalFormat.decimalFormat(debitiBanchePrec,2));
							
							String debitiVsFornitori = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSFORNITORI).get(VALATT);
							bilMap.setDebitiVsFornitori(DecimalFormat.decimalFormat(debitiVsFornitori,2));
							
							String debitiVsFornitoriPrec = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSFORNITORI).get(VALPREC);
							bilMap.setDebitiVsFornitoriPrec(DecimalFormat.decimalFormat(debitiVsFornitoriPrec,2));
							
							bilMap.setDebitiFornScad(""); 		// Voce non presente in AAEP
							bilMap.setDebitiFornScadPrec(""); 	// Voce non presente in AAEP
												
							String debitiImpreseCollegate = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCOLLEGATE).get(VALATT);
							bilMap.setDebitiImpreseCollegate(DecimalFormat.decimalFormat(debitiImpreseCollegate,2));
							
							String debitiImpreseCollegatePrec = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCOLLEGATE).get(VALPREC);
							bilMap.setDebitiImpreseCollegatePrec(DecimalFormat.decimalFormat(debitiImpreseCollegatePrec,2));
							
							String debitiControllanti = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCONTROLLANTI).get(VALATT);
							bilMap.setDebitiControllanti(DecimalFormat.decimalFormat(debitiControllanti,2));
							
							String debitiControllantiPrec = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCONTROLLANTI).get(VALPREC);
							bilMap.setDebitiControllantiPrec(DecimalFormat.decimalFormat(debitiControllantiPrec,2));
							
							String debitiTributari = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITITRIBUTARI).get(VALATT);
							bilMap.setDebitiTributari(DecimalFormat.decimalFormat(debitiTributari,2));
							
							String debitiTributariPrec = (String) (String)getMapAttributi(mappaAAEP, TOTDEBITITRIBUTARI).get(VALPREC);
							bilMap.setDebitiTributariPrec(DecimalFormat.decimalFormat(debitiTributariPrec,2));
							
							bilMap.setDebitiTributariScad(""); // Voce non presente in AAEP
							bilMap.setDebitiTributariScadPrec(""); // Voce non presente in AAEP
							
							String ricavi = (String) (String)getMapAttributi(mappaAAEP, RICAVI).get(VALATT);
							bilMap.setRicavi(DecimalFormat.decimalFormat(ricavi,2));
							
							String ricaviPrec = (String) (String)getMapAttributi(mappaAAEP, RICAVI).get(VALPREC);
							bilMap.setRicaviPrec(DecimalFormat.decimalFormat(ricaviPrec,2));
							
							String totaleValoreProduzione = (String) (String)getMapAttributi(mappaAAEP, TOTVALPRODUZIONE).get(VALATT);
							bilMap.setTotaleValoreProduzione(DecimalFormat.decimalFormat(totaleValoreProduzione,2));
							
							String totaleValoreProduzionePrec = (String) (String)getMapAttributi(mappaAAEP, TOTVALPRODUZIONE).get(VALPREC);
							bilMap.setTotaleValoreProduzionePrec(DecimalFormat.decimalFormat(totaleValoreProduzionePrec,2));
							
							String variazioneLavoriInCorso = (String) (String)getMapAttributi(mappaAAEP, VARIAZIONELAVORIINCORSO).get(VALATT);
							bilMap.setVariazioneLavoriInCorso(DecimalFormat.decimalFormat(variazioneLavoriInCorso,2));
							
							String variazioneLavoriInCorsoPrec = (String) (String)getMapAttributi(mappaAAEP, VARIAZIONELAVORIINCORSO).get(VALPREC);
							bilMap.setVariazioneLavoriInCorsoPrec(DecimalFormat.decimalFormat(variazioneLavoriInCorsoPrec,2));
							
							String ammortamentiImm = (String) (String)getMapAttributi(mappaAAEP, AMMIMMATERIALI).get(VALATT);
							bilMap.setAmmortamentiImm(DecimalFormat.decimalFormat(ammortamentiImm,2));
							
							String ammortamentiImmPrec = (String) (String)getMapAttributi(mappaAAEP, AMMIMMATERIALI).get(VALPREC);
							bilMap.setAmmortamentiImmPrec(DecimalFormat.decimalFormat(ammortamentiImmPrec,2));
							
							String ammortamentiMat = (String) (String)getMapAttributi(mappaAAEP, AMMMATERIALI).get(VALATT);
							bilMap.setAmmortamentiMat( DecimalFormat.decimalFormat(ammortamentiMat,2));
							
							String ammortamentiMatPrec = (String) (String)getMapAttributi(mappaAAEP, AMMMATERIALI).get(VALPREC);
							bilMap.setAmmortamentiMatPrec( DecimalFormat.decimalFormat(ammortamentiMatPrec,2));
							
							String totaleCostiProduzione = (String) (String)getMapAttributi(mappaAAEP, TOTCOSTIPRODUZIONE).get(VALATT);
							bilMap.setTotaleCostiProduzione(DecimalFormat.decimalFormat(totaleCostiProduzione,2));
							
							String totaleCostiProduzionePrec = (String) (String)getMapAttributi(mappaAAEP, TOTCOSTIPRODUZIONE).get(VALPREC);
							bilMap.setTotaleCostiProduzionePrec(DecimalFormat.decimalFormat(totaleCostiProduzionePrec,2));
							
							String proventiFinanziari = (String) (String)getMapAttributi(mappaAAEP, PROVENTIFIN).get(VALATT);
							bilMap.setProventiFinanziari(DecimalFormat.decimalFormat(proventiFinanziari,2));
							
							String proventiFinanziariPrec = (String) (String)getMapAttributi(mappaAAEP, PROVENTIFIN).get(VALPREC);
							bilMap.setProventiFinanziariPrec(DecimalFormat.decimalFormat(proventiFinanziariPrec,2));
							
							String interessiPassivi = (String) (String)getMapAttributi(mappaAAEP, TOTINTERESSI).get(VALATT);
							bilMap.setInteressiPassivi(DecimalFormat.decimalFormat(interessiPassivi,2));
							
							String interessiPassiviPrec = (String) (String)getMapAttributi(mappaAAEP, TOTINTERESSI).get(VALPREC);
							bilMap.setInteressiPassiviPrec(DecimalFormat.decimalFormat(interessiPassiviPrec,2));
							
							String proventiGestioneAccessoria = (String) (String)getMapAttributi(mappaAAEP, TOTPROVENTI).get(VALATT);
							bilMap.setProventiGestioneAccessoria(DecimalFormat.decimalFormat(proventiGestioneAccessoria,2));
							
							String proventiGestioneAccessoriaPrec = (String) (String)getMapAttributi(mappaAAEP, TOTPROVENTI).get(VALPREC);
							bilMap.setProventiGestioneAccessoriaPrec(DecimalFormat.decimalFormat(proventiGestioneAccessoriaPrec,2));
							
							String oneriGestioneAccessoria = (String) (String)getMapAttributi(mappaAAEP, TOTONERI).get(VALATT);
							bilMap.setOneriGestioneAccessoria(DecimalFormat.decimalFormat(oneriGestioneAccessoria,2));
							
							String oneriGestioneAccessoriaPrec = (String) (String)getMapAttributi(mappaAAEP, TOTONERI).get(VALPREC);
							bilMap.setOneriGestioneAccessoriaPrec(DecimalFormat.decimalFormat(oneriGestioneAccessoriaPrec,2));
							
							//String diffAB = (String)getMapAttributi(mappaAAEP, DIFFAB).get(VALATT);
							//String diffABPrec = (String)getMapAttributi(mappaAAEP, DIFFAB).get(VALPREC);
							// CALCOLIAMO LA DIFFERENZA TRA A e B (la voce su AAEP non sempre é valorizzata)
							String diffAB = differenzaNumeri(totaleValoreProduzione, totaleCostiProduzione);
							String diffABPrec = differenzaNumeri(totaleValoreProduzionePrec, totaleCostiProduzionePrec);
							logger.info("[Bilancio::inject] diffAB:" + diffAB);
							
							
							// NON SALVARE SU XML PER AAEP
							// EBITDA (Differenza tra valore e costi della produzione (A - B)+ Ammortamenti materiali + Ammortamenti immateriali)
							// EBITDA = -2 + -3 + -4
							// calcoliamo il valore se e solo se almeno un addendo é valorizzato, JIRA 281
							if (!StringUtils.isBlank(totaleValoreProduzione) || !StringUtils.isBlank(totaleCostiProduzione) ||
								!StringUtils.isBlank(ammortamentiMat) || !StringUtils.isBlank(ammortamentiImm)) {
								
								ebitda = sommaNumeri(diffAB, ammortamentiMat, ammortamentiImm, "0");
							}
							 			
							logger.info("[Bilancio::inject] ebitda:" + ebitda);		
										
							if (!StringUtils.isBlank(totaleValoreProduzionePrec) || !StringUtils.isBlank(totaleCostiProduzionePrec) ||
								!StringUtils.isBlank(ammortamentiMatPrec) || !StringUtils.isBlank(ammortamentiImmPrec)) {
								
								ebitdaPrec = sommaNumeri(diffABPrec, ammortamentiMatPrec, ammortamentiImmPrec, "0");
							}
							logger.info("[Bilancio::inject] ebitdaPrec:" + ebitdaPrec);
							
							// NON SALVARE SU XML PER AAEP
							// EBIT (Differenza tra valore e costi della produzione (A - B) + Proventi finanziari + Proventi gestione accessoria + Oneri gestione accessoria)
							// EBIT = -2 + -1 + -5 + -6
							// calcoliamo il valore se e solo se almeno un addendo é valorizzato, JIRA 281
							if (!StringUtils.isBlank(totaleValoreProduzione) || !StringUtils.isBlank(totaleCostiProduzione) ||
								!StringUtils.isBlank(proventiFinanziari) || !StringUtils.isBlank(proventiGestioneAccessoria) || !StringUtils.isBlank(oneriGestioneAccessoria)) {
																		
								ebit = sommaNumeri(diffAB, proventiFinanziari, proventiGestioneAccessoria, oneriGestioneAccessoria);
							}
							logger.info("[Bilancio::inject] ebit:" + ebit);
							
							// calcoliamo il valore se e solo se almeno un addendo é valorizzato, JIRA 281
							if (!StringUtils.isBlank(totaleValoreProduzionePrec) || !StringUtils.isBlank(totaleCostiProduzionePrec) ||
								!StringUtils.isBlank(proventiFinanziariPrec) || !StringUtils.isBlank(proventiGestioneAccessoriaPrec) || !StringUtils.isBlank(oneriGestioneAccessoriaPrec)) {
							
								ebitPrec = sommaNumeri(diffABPrec, proventiFinanziariPrec, proventiGestioneAccessoriaPrec, oneriGestioneAccessoriaPrec);
							}
							logger.info("[Bilancio::inject] ebitPrec:" + ebitPrec);
							
							bilMap.setIndiceRotazione(""); // Voce non presente in AAEP
							bilMap.setIndiceRotazionePrec(""); // Voce non presente in AAEP
	
							bilMap.setDso(""); // Voce non presente in AAEP
							bilMap.setDsoPrec(""); // Voce non presente in AAEP
							
							bilMap.setDpo(""); // Voce non presente in AAEP
							bilMap.setDpoPrec(""); // Voce non presente in AAEP
							
							bilMap.setUla(""); // Voce non presente in AAEP
							bilMap.setUlaPrec(""); // Voce non presente in AAEP
							
							aaepMap = bilMap;
						}
						// : se bando risulta con dati bilancio custom
						if( isDatiBilancioCustom){
						
							String annoAAEP = (String)getMapAttributi(mappaAAEP, STATOPATRIMONIALE).get(VALATT);
							
							// 2017
							if( annoAAEP != null && annoAAEP.equals( annoCorrenteM1 ) )
							{
								// fda
								fonteDatiAnno = "AAEP";
								bilMap.setFonteDatiAnno(fonteDatiAnno);
								
								bilMap.setAnno( "31/12/" + (String)getMapAttributi(mappaAAEP, STATOPATRIMONIALE).get(VALATT));
							
								String speseRS = (String)getMapAttributi(mappaAAEP, SPESERS).get(VALATT);
								bilMap.setSpeseRS(DecimalFormat.decimalFormat(speseRS,2));
								
								String totCreditiVsClienti = (String)getMapAttributi(mappaAAEP, TOTCREDITIVSCLIENTI).get(VALATT);
								bilMap.setTotCreditiVsClienti(DecimalFormat.decimalFormat(totCreditiVsClienti,2));
								
								bilMap.setCreditiCommScad(""); 		// Voce non presente in AAEP
								
								String disponibilitaLiquide = (String)getMapAttributi(mappaAAEP, TOTDISPLIQUIDE).get(VALATT);
								bilMap.setDisponibilitaLiquide(DecimalFormat.decimalFormat(disponibilitaLiquide,2));
								
								String totaleBilancio = (String)getMapAttributi(mappaAAEP, TOTALEATTIVO).get(VALATT);
								bilMap.setTotaleBilancio(DecimalFormat.decimalFormat(totaleBilancio,2));
								
								String totalePatrimonio = (String)getMapAttributi(mappaAAEP, TOTPATRNETTO).get(VALATT);
								bilMap.setTotalePatrimonio(DecimalFormat.decimalFormat(totalePatrimonio,2));
								
								String debitiSoci = (String)getMapAttributi(mappaAAEP, TOTDEBITI).get(VALATT);
								bilMap.setDebitiSoci(DecimalFormat.decimalFormat(debitiSoci,2));
								
								String debitiBanche = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSBANCHE).get(VALATT);
								bilMap.setDebitiBanche(DecimalFormat.decimalFormat(debitiBanche,2));
								
								String debitiVsFornitori = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSFORNITORI).get(VALATT);
								bilMap.setDebitiVsFornitori(DecimalFormat.decimalFormat(debitiVsFornitori,2));
								
								bilMap.setDebitiFornScad( ""); 		// Voce non presente in AAEP
													
								String debitiImpreseCollegate = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCOLLEGATE).get(VALATT);
								bilMap.setDebitiImpreseCollegate(DecimalFormat.decimalFormat(debitiImpreseCollegate,2));
								
								String debitiControllanti = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCONTROLLANTI).get(VALATT);
								bilMap.setDebitiControllanti(DecimalFormat.decimalFormat(debitiControllanti,2));
								
								String debitiTributari = (String)getMapAttributi(mappaAAEP, TOTDEBITITRIBUTARI).get(VALATT);
								bilMap.setDebitiTributari(DecimalFormat.decimalFormat(debitiTributari,2));
								
								bilMap.setDebitiTributariScad( ""); // Voce non presente in AAEP
								
								String ricavi = (String)getMapAttributi(mappaAAEP, RICAVI).get(VALATT);
								bilMap.setRicavi(DecimalFormat.decimalFormat(ricavi,2));
								
								String totaleValoreProduzione = (String)getMapAttributi(mappaAAEP, TOTVALPRODUZIONE).get(VALATT);
								bilMap.setTotaleValoreProduzione(DecimalFormat.decimalFormat(totaleValoreProduzione,2));
								
								String variazioneLavoriInCorso = (String)getMapAttributi(mappaAAEP, VARIAZIONELAVORIINCORSO).get(VALATT);
								bilMap.setVariazioneLavoriInCorso(DecimalFormat.decimalFormat(variazioneLavoriInCorso,2));
								
								String ammortamentiImm = (String)getMapAttributi(mappaAAEP, AMMIMMATERIALI).get(VALATT);
								bilMap.setAmmortamentiImm(DecimalFormat.decimalFormat(ammortamentiImm,2));
								
								String ammortamentiMat = (String)getMapAttributi(mappaAAEP, AMMMATERIALI).get(VALATT);
								bilMap.setAmmortamentiMat( DecimalFormat.decimalFormat(ammortamentiMat,2));
								
								String totaleCostiProduzione = (String)getMapAttributi(mappaAAEP, TOTCOSTIPRODUZIONE).get(VALATT);
								bilMap.setTotaleCostiProduzione(DecimalFormat.decimalFormat(totaleCostiProduzione,2));
								
								String proventiFinanziari = (String)getMapAttributi(mappaAAEP, PROVENTIFIN).get(VALATT);
								bilMap.setProventiFinanziari(DecimalFormat.decimalFormat(proventiFinanziari,2));
								
								String interessiPassivi = (String)getMapAttributi(mappaAAEP, TOTINTERESSI).get(VALATT);
								bilMap.setInteressiPassivi(DecimalFormat.decimalFormat(interessiPassivi,2));
								
								String proventiGestioneAccessoria = (String)getMapAttributi(mappaAAEP, TOTPROVENTI).get(VALATT);
								bilMap.setProventiGestioneAccessoria(DecimalFormat.decimalFormat(proventiGestioneAccessoria,2));
								
								String oneriGestioneAccessoria = (String)getMapAttributi(mappaAAEP, TOTONERI).get(VALATT);
								bilMap.setOneriGestioneAccessoria(DecimalFormat.decimalFormat(oneriGestioneAccessoria,2));
								
								//String diffAB = (String)getMapAttributi(mappaAAEP, DIFFAB).get(VALATT);
								//String diffABPrec = (String)getMapAttributi(mappaAAEP, DIFFAB).get(VALPREC);
								// CALCOLIAMO LA DIFFERENZA TRA A e B (la voce su AAEP non sempre risulta valorizzata)
								String diffAB = differenzaNumeri(totaleValoreProduzione, totaleCostiProduzione);
								logger.info("[Bilancio::inject] diffAB:" + diffAB);
								
								// NON SALVARE SU XML PER AAEP
								// EBITDA (Differenza tra valore e costi della produzione (A - B)+ Ammortamenti materiali + Ammortamenti immateriali)
								// EBITDA = -2 + -3 + -4
								// calcoliamo il valore se e solo se almeno un addendo risulta valorizzato, JIRA 281
								if (!StringUtils.isBlank(totaleValoreProduzione) || !StringUtils.isBlank(totaleCostiProduzione) ||
									!StringUtils.isBlank(ammortamentiMat) || !StringUtils.isBlank(ammortamentiImm)) {
									
									ebitda = sommaNumeri(diffAB, ammortamentiMat, ammortamentiImm, "0");
								}
								 			
								logger.info("[Bilancio::inject] ebitda:" + ebitda);		
											
								// NON SALVARE SU XML PER AAEP
								// EBIT (Differenza tra valore e costi della produzione (A - B) + Proventi finanziari + Proventi gestione accessoria + Oneri gestione accessoria)
								// EBIT = -2 + -1 + -5 + -6
								// calcoliamo il valore se e solo se almeno un addendo risulta valorizzato, JIRA 281
								if (!StringUtils.isBlank(totaleValoreProduzione) || !StringUtils.isBlank(totaleCostiProduzione) ||
									!StringUtils.isBlank(proventiFinanziari) || !StringUtils.isBlank(proventiGestioneAccessoria) || !StringUtils.isBlank(oneriGestioneAccessoria)) {
																			
									ebit = sommaNumeri(diffAB, proventiFinanziari, proventiGestioneAccessoria, oneriGestioneAccessoria);
								}
								logger.info("[Bilancio::inject] ebit:" + ebit);
								
								bilMap.setIndiceRotazione( ""); // Voce non presente in AAEP
								bilMap.setDso( ""); // Voce non presente in AAEP
								bilMap.setDpo( ""); // Voce non presente in AAEP
								bilMap.setUla( ""); // Voce non presente in AAEP
								aaepMap = bilMap;
								
							} // if solo annoAAEP 2017 in I colonna
							
							
							// anno 2016 in II colonna
							if( annoAAEP.equals( annoCorrenteM2 ) )
							{
								// fdap
								fonteDatiAnnoPrec = "AAEP";
								bilMap.setFonteDatiAnnoPrec( fonteDatiAnnoPrec);
								
								bilMap.setAnnoPrec( "31/12/" + (String)getMapAttributi(mappaAAEP, STATOPATRIMONIALE).get(VALATT));
							
								String speseRS = (String)(String)getMapAttributi(mappaAAEP, SPESERS).get(VALATT);
								bilMap.setSpeseRSPrec(DecimalFormat.decimalFormat(speseRS,2));
								
								String totCreditiVsClienti = (String)getMapAttributi(mappaAAEP, TOTCREDITIVSCLIENTI).get(VALATT);
								bilMap.setTotCreditiVsClientiPrec(DecimalFormat.decimalFormat(totCreditiVsClienti,2));
								
								bilMap.setCreditiCommScadPrec(""); 		// Voce non presente in AAEP
								
								String disponibilitaLiquide = (String)getMapAttributi(mappaAAEP, TOTDISPLIQUIDE).get(VALATT);
								bilMap.setDisponibilitaLiquidePrec(DecimalFormat.decimalFormat(disponibilitaLiquide,2));
								
								String totaleBilancio = (String)getMapAttributi(mappaAAEP, TOTALEATTIVO).get(VALATT);
								bilMap.setTotaleBilancioPrec(DecimalFormat.decimalFormat(totaleBilancio,2));
								
								String totalePatrimonio = (String)getMapAttributi(mappaAAEP, TOTPATRNETTO).get(VALATT);
								bilMap.setTotalePatrimonioPrec(DecimalFormat.decimalFormat(totalePatrimonio,2));
								
								String debitiSoci = (String)getMapAttributi(mappaAAEP, TOTDEBITI).get(VALATT);
								bilMap.setDebitiSociPrec(DecimalFormat.decimalFormat(debitiSoci,2));
								
								String debitiBanche = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSBANCHE).get(VALATT);
								bilMap.setDebitiBanchePrec(DecimalFormat.decimalFormat(debitiBanche,2));
								
								String debitiVsFornitori = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSFORNITORI).get(VALATT);
								bilMap.setDebitiVsFornitoriPrec(DecimalFormat.decimalFormat(debitiVsFornitori,2));
								
								bilMap.setDebitiFornScadPrec( ""); 		// Voce non presente in AAEP
													
								String debitiImpreseCollegate = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCOLLEGATE).get(VALATT);
								bilMap.setDebitiImpreseCollegatePrec(DecimalFormat.decimalFormat(debitiImpreseCollegate,2));
								
								String debitiControllanti = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCONTROLLANTI).get(VALATT);
								bilMap.setDebitiControllantiPrec(DecimalFormat.decimalFormat(debitiControllanti,2));
								
								String debitiTributari = (String)getMapAttributi(mappaAAEP, TOTDEBITITRIBUTARI).get(VALATT);
								bilMap.setDebitiTributariPrec(DecimalFormat.decimalFormat(debitiTributari,2));
								
								bilMap.setDebitiTributariScadPrec( ""); // Voce non presente in AAEP
								
								String ricavi = (String)getMapAttributi(mappaAAEP, RICAVI).get(VALATT);
								bilMap.setRicaviPrec(DecimalFormat.decimalFormat(ricavi,2));
								
								String totaleValoreProduzione = (String)getMapAttributi(mappaAAEP, TOTVALPRODUZIONE).get(VALATT);
								bilMap.setTotaleValoreProduzionePrec(DecimalFormat.decimalFormat(totaleValoreProduzione,2));
								
								String variazioneLavoriInCorso = (String)getMapAttributi(mappaAAEP, VARIAZIONELAVORIINCORSO).get(VALATT);
								bilMap.setVariazioneLavoriInCorsoPrec(DecimalFormat.decimalFormat(variazioneLavoriInCorso,2));
								
								String ammortamentiImm = (String)getMapAttributi(mappaAAEP, AMMIMMATERIALI).get(VALATT);
								bilMap.setAmmortamentiImmPrec(DecimalFormat.decimalFormat(ammortamentiImm,2));
								
								String ammortamentiMat = (String)getMapAttributi(mappaAAEP, AMMMATERIALI).get(VALATT);
								bilMap.setAmmortamentiMatPrec( DecimalFormat.decimalFormat(ammortamentiMat,2));
								
								String totaleCostiProduzione = (String)getMapAttributi(mappaAAEP, TOTCOSTIPRODUZIONE).get(VALATT);
								bilMap.setTotaleCostiProduzionePrec(DecimalFormat.decimalFormat(totaleCostiProduzione,2));
								
								String proventiFinanziari = (String)getMapAttributi(mappaAAEP, PROVENTIFIN).get(VALATT);
								bilMap.setProventiFinanziariPrec(DecimalFormat.decimalFormat(proventiFinanziari,2));
								
								String interessiPassivi = (String)getMapAttributi(mappaAAEP, TOTINTERESSI).get(VALATT);
								bilMap.setInteressiPassiviPrec(DecimalFormat.decimalFormat(interessiPassivi,2));
								
								String proventiGestioneAccessoria = (String)getMapAttributi(mappaAAEP, TOTPROVENTI).get(VALATT);
								bilMap.setProventiGestioneAccessoriaPrec(DecimalFormat.decimalFormat(proventiGestioneAccessoria,2));
								
								String oneriGestioneAccessoria = (String)getMapAttributi(mappaAAEP, TOTONERI).get(VALATT);
								bilMap.setOneriGestioneAccessoriaPrec(DecimalFormat.decimalFormat(oneriGestioneAccessoria,2));
								
								// CALCOLIAMO LA DIFFERENZA TRA A e B (la voce su AAEP non sempre risulta valorizzata)
								String diffAB = differenzaNumeri(totaleValoreProduzione, totaleCostiProduzione);
								logger.info("[Bilancio::inject] diffAB:" + diffAB);
								
								// NON SALVARE SU XML PER AAEP
								// EBITDA (Differenza tra valore e costi della produzione (A - B)+ Ammortamenti materiali + Ammortamenti immateriali)
								// EBITDA = -2 + -3 + -4
								// calcoliamo il valore se e solo se almeno un addendo risulta valorizzato, JIRA 281
								if (!StringUtils.isBlank(totaleValoreProduzione) || !StringUtils.isBlank(totaleCostiProduzione) ||
									!StringUtils.isBlank(ammortamentiMat) || !StringUtils.isBlank(ammortamentiImm)) {
									
									ebitdaPrec = sommaNumeri(diffAB, ammortamentiMat, ammortamentiImm, "0");
								}
								 			
								logger.info("[Bilancio::inject] ebitda:" + ebitda);		
											
								// NON SALVARE SU XML PER AAEP
								// EBIT (Differenza tra valore e costi della produzione (A - B) + Proventi finanziari + Proventi gestione accessoria + Oneri gestione accessoria)
								// EBIT = -2 + -1 + -5 + -6
								// calcoliamo il valore se e solo se almeno un addendo risulta valorizzato, JIRA 281
								if (!StringUtils.isBlank(totaleValoreProduzione) || !StringUtils.isBlank(totaleCostiProduzione) ||
									!StringUtils.isBlank(proventiFinanziari) || !StringUtils.isBlank(proventiGestioneAccessoria) || !StringUtils.isBlank(oneriGestioneAccessoria)) {
																			
									ebitPrec = sommaNumeri(diffAB, proventiFinanziari, proventiGestioneAccessoria, oneriGestioneAccessoria);
								}
								logger.info("[Bilancio::inject] ebit:" + ebit);
								
								bilMap.setIndiceRotazionePrec( ""); // Voce non presente in AAEP
								bilMap.setDsoPrec( ""); // Voce non presente in AAEP
								bilMap.setDpoPrec( ""); // Voce non presente in AAEP
								bilMap.setUlaPrec( ""); // Voce non presente in AAEP
								aaepMap = bilMap;
								
							} // if solo annoAAEP 2016
							
							
							String annoPrecedente = (String)getMapAttributi(mappaAAEP, STATOPATRIMONIALE).get(VALPREC);
							
							// se annoAAEP prec = 2017 va in I colonna, si usa fonteDatiAnno
							if( annoPrecedente.equals( annoCorrenteM1) ){
								
								if(StringUtils.isNotBlank(annoPrecedente)){
									bilMap.setAnno( "31/12/" + annoPrecedente);
								}else{
									bilMap.setAnno( "");
								}
								
								fonteDatiAnno = "AAEP";
								bilMap.setFonteDatiAnno( fonteDatiAnno);
								
								String speseRSPrec = (String)getMapAttributi(mappaAAEP, SPESERS).get(VALPREC);
								bilMap.setSpeseRS(DecimalFormat.decimalFormat(speseRSPrec,2));
								
								String totCreditiVsClientiPrec = (String)getMapAttributi(mappaAAEP, TOTCREDITIVSCLIENTI).get(VALPREC);
								bilMap.setTotCreditiVsClienti(DecimalFormat.decimalFormat(totCreditiVsClientiPrec,2));
								
								bilMap.setCreditiCommScad("");	// Voce non presente in AAEP
								
								String disponibilitaLiquidePrec = (String)getMapAttributi(mappaAAEP, TOTDISPLIQUIDE).get(VALPREC);
								bilMap.setDisponibilitaLiquide(DecimalFormat.decimalFormat(disponibilitaLiquidePrec,2));
								
								String totaleBilancioPrec = (String)getMapAttributi(mappaAAEP, TOTALEATTIVO).get(VALPREC);
								bilMap.setTotaleBilancio(DecimalFormat.decimalFormat(totaleBilancioPrec,2));
								
								String totalePatrimonioPrec = (String)getMapAttributi(mappaAAEP, TOTPATRNETTO).get(VALPREC);
								bilMap.setTotalePatrimonio(DecimalFormat.decimalFormat(totalePatrimonioPrec,2));
								
								String debitiSociPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITI).get(VALPREC);
								bilMap.setDebitiSoci(DecimalFormat.decimalFormat(debitiSociPrec,2));
								
								String debitiBanchePrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSBANCHE).get(VALPREC);
								bilMap.setDebitiBanche(DecimalFormat.decimalFormat(debitiBanchePrec,2));
								
								String debitiVsFornitoriPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSFORNITORI).get(VALPREC);
								bilMap.setDebitiVsFornitori(DecimalFormat.decimalFormat(debitiVsFornitoriPrec,2));
								
								bilMap.setDebitiFornScad( ""); 	// Voce non presente in AAEP
													
								String debitiImpreseCollegatePrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCOLLEGATE).get(VALPREC);
								bilMap.setDebitiImpreseCollegate(DecimalFormat.decimalFormat(debitiImpreseCollegatePrec,2));
								
								String debitiControllantiPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCONTROLLANTI).get(VALPREC);
								bilMap.setDebitiControllanti(DecimalFormat.decimalFormat(debitiControllantiPrec,2));
								
								String debitiTributariPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITITRIBUTARI).get(VALPREC);
								bilMap.setDebitiTributari(DecimalFormat.decimalFormat(debitiTributariPrec,2));
								
								bilMap.setDebitiTributariScad( ""); // Voce non presente in AAEP
								
								String ricaviPrec = (String)getMapAttributi(mappaAAEP, RICAVI).get(VALPREC);
								bilMap.setRicavi(DecimalFormat.decimalFormat(ricaviPrec,2));
								
								String totaleValoreProduzionePrec = (String)getMapAttributi(mappaAAEP, TOTVALPRODUZIONE).get(VALPREC);
								bilMap.setTotaleValoreProduzione(DecimalFormat.decimalFormat(totaleValoreProduzionePrec,2));
								
								String variazioneLavoriInCorsoPrec = (String)getMapAttributi(mappaAAEP, VARIAZIONELAVORIINCORSO).get(VALPREC);
								bilMap.setVariazioneLavoriInCorso(DecimalFormat.decimalFormat(variazioneLavoriInCorsoPrec,2));
								
								String ammortamentiImmPrec = (String)getMapAttributi(mappaAAEP, AMMIMMATERIALI).get(VALPREC);
								bilMap.setAmmortamentiImm(DecimalFormat.decimalFormat(ammortamentiImmPrec,2));
								
								String ammortamentiMatPrec = (String)getMapAttributi(mappaAAEP, AMMMATERIALI).get(VALPREC);
								bilMap.setAmmortamentiMat( DecimalFormat.decimalFormat(ammortamentiMatPrec,2));
								
								String totaleCostiProduzionePrec = (String)getMapAttributi(mappaAAEP, TOTCOSTIPRODUZIONE).get(VALPREC);
								bilMap.setTotaleCostiProduzione(DecimalFormat.decimalFormat(totaleCostiProduzionePrec,2));
								
								String proventiFinanziariPrec = (String)getMapAttributi(mappaAAEP, PROVENTIFIN).get(VALPREC);
								bilMap.setProventiFinanziari(DecimalFormat.decimalFormat(proventiFinanziariPrec,2));
								
								String interessiPassiviPrec = (String)getMapAttributi(mappaAAEP, TOTINTERESSI).get(VALPREC);
								bilMap.setInteressiPassivi(DecimalFormat.decimalFormat(interessiPassiviPrec,2));
								
								String proventiGestioneAccessoriaPrec = (String)getMapAttributi(mappaAAEP, TOTPROVENTI).get(VALPREC);
								bilMap.setProventiGestioneAccessoria(DecimalFormat.decimalFormat(proventiGestioneAccessoriaPrec,2));
								
								String oneriGestioneAccessoriaPrec = (String)getMapAttributi(mappaAAEP, TOTONERI).get(VALPREC);
								bilMap.setOneriGestioneAccessoria(DecimalFormat.decimalFormat(oneriGestioneAccessoriaPrec,2));
								
								//String diffABPrec = (String)getMapAttributi(mappaAAEP, DIFFAB).get(VALPREC);
								String diffABPrec = differenzaNumeri(totaleValoreProduzionePrec, totaleCostiProduzionePrec);
								
								if (!StringUtils.isBlank(totaleValoreProduzionePrec) || !StringUtils.isBlank(totaleCostiProduzionePrec) ||
									!StringUtils.isBlank(ammortamentiMatPrec) || !StringUtils.isBlank(ammortamentiImmPrec)) {
									
									ebitda = sommaNumeri(diffABPrec, ammortamentiMatPrec, ammortamentiImmPrec, "0");
								}
								logger.info("[Bilancio::inject] ebitdaPrec:" + ebitdaPrec);
								
								// calcoliamo il valore se e solo se almeno un addendo risulta valorizzato, JIRA 281
								if (!StringUtils.isBlank(totaleValoreProduzionePrec) || !StringUtils.isBlank(totaleCostiProduzionePrec) ||
									!StringUtils.isBlank(proventiFinanziariPrec) || !StringUtils.isBlank(proventiGestioneAccessoriaPrec) || !StringUtils.isBlank(oneriGestioneAccessoriaPrec)) {
								
									ebit = sommaNumeri(diffABPrec, proventiFinanziariPrec, proventiGestioneAccessoriaPrec, oneriGestioneAccessoriaPrec);
								}
								logger.info("[Bilancio::inject] ebitPrec:" + ebitPrec);
								bilMap.setIndiceRotazione( ""); // Voce non presente in AAEP
								bilMap.setDso( ""); // Voce non presente in AAEP
								bilMap.setDpo( ""); // Voce non presente in AAEP
								bilMap.setUla( ""); // Voce non presente in AAEP
								
								aaepMap = bilMap;
								
							} // if solo annoAAEPPrec 2017
							
							
							// annoAAEPPrec 2016 : colonna II, si usa fonteDatiAnnoPrec
							if( annoPrecedente.equals( annoCorrenteM2) ){
								
								fonteDatiAnnoPrec = "AAEP";
								bilMap.setFonteDatiAnnoPrec( fonteDatiAnnoPrec);
								
								if(StringUtils.isNotBlank(annoPrecedente)){
									bilMap.setAnnoPrec( "31/12/" + annoPrecedente);
								}else{
									bilMap.setAnnoPrec( "");
								}
								
								String speseRSPrec = (String)getMapAttributi(mappaAAEP, SPESERS).get(VALPREC);
								bilMap.setSpeseRSPrec(DecimalFormat.decimalFormat(speseRSPrec,2));
								
								String totCreditiVsClientiPrec = (String)getMapAttributi(mappaAAEP, TOTCREDITIVSCLIENTI).get(VALPREC);
								bilMap.setTotCreditiVsClientiPrec(DecimalFormat.decimalFormat(totCreditiVsClientiPrec,2));
								
								bilMap.setCreditiCommScadPrec("");	// Voce non presente in AAEP
								
								String disponibilitaLiquidePrec = (String)getMapAttributi(mappaAAEP, TOTDISPLIQUIDE).get(VALPREC);
								bilMap.setDisponibilitaLiquidePrec(DecimalFormat.decimalFormat(disponibilitaLiquidePrec,2));
								
								String totaleBilancioPrec = (String)getMapAttributi(mappaAAEP, TOTALEATTIVO).get(VALPREC);
								bilMap.setTotaleBilancioPrec(DecimalFormat.decimalFormat(totaleBilancioPrec,2));
								
								String totalePatrimonioPrec = (String)getMapAttributi(mappaAAEP, TOTPATRNETTO).get(VALPREC);
								bilMap.setTotalePatrimonioPrec(DecimalFormat.decimalFormat(totalePatrimonioPrec,2));
								
								String debitiSociPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITI).get(VALPREC);
								bilMap.setDebitiSociPrec(DecimalFormat.decimalFormat(debitiSociPrec,2));
								
								String debitiBanchePrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSBANCHE).get(VALPREC);
								bilMap.setDebitiBanchePrec(DecimalFormat.decimalFormat(debitiBanchePrec,2));
								
								String debitiVsFornitoriPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSFORNITORI).get(VALPREC);
								bilMap.setDebitiVsFornitoriPrec(DecimalFormat.decimalFormat(debitiVsFornitoriPrec,2));
								
								bilMap.setDebitiFornScadPrec( ""); 	// Voce non presente in AAEP
													
								String debitiImpreseCollegatePrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCOLLEGATE).get(VALPREC);
								bilMap.setDebitiImpreseCollegatePrec(DecimalFormat.decimalFormat(debitiImpreseCollegatePrec,2));
								
								String debitiControllantiPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITIVSIMPCONTROLLANTI).get(VALPREC);
								bilMap.setDebitiControllantiPrec(DecimalFormat.decimalFormat(debitiControllantiPrec,2));
								
								String debitiTributariPrec = (String)getMapAttributi(mappaAAEP, TOTDEBITITRIBUTARI).get(VALPREC);
								bilMap.setDebitiTributariPrec(DecimalFormat.decimalFormat(debitiTributariPrec,2));
								
								bilMap.setDebitiTributariScadPrec( ""); // Voce non presente in AAEP
								
								String ricaviPrec = (String)getMapAttributi(mappaAAEP, RICAVI).get(VALPREC);
								bilMap.setRicaviPrec(DecimalFormat.decimalFormat(ricaviPrec,2));
								
								String totaleValoreProduzionePrec = (String)getMapAttributi(mappaAAEP, TOTVALPRODUZIONE).get(VALPREC);
								bilMap.setTotaleValoreProduzionePrec(DecimalFormat.decimalFormat(totaleValoreProduzionePrec,2));
								
								String variazioneLavoriInCorsoPrec = (String)getMapAttributi(mappaAAEP, VARIAZIONELAVORIINCORSO).get(VALPREC);
								bilMap.setVariazioneLavoriInCorsoPrec(DecimalFormat.decimalFormat(variazioneLavoriInCorsoPrec,2));
								
								String ammortamentiImmPrec = (String)getMapAttributi(mappaAAEP, AMMIMMATERIALI).get(VALPREC);
								bilMap.setAmmortamentiImmPrec(DecimalFormat.decimalFormat(ammortamentiImmPrec,2));
								
								String ammortamentiMatPrec = (String)getMapAttributi(mappaAAEP, AMMMATERIALI).get(VALPREC);
								bilMap.setAmmortamentiMatPrec( DecimalFormat.decimalFormat(ammortamentiMatPrec,2));
								
								String totaleCostiProduzionePrec = (String)getMapAttributi(mappaAAEP, TOTCOSTIPRODUZIONE).get(VALPREC);
								bilMap.setTotaleCostiProduzionePrec(DecimalFormat.decimalFormat(totaleCostiProduzionePrec,2));
								
								String proventiFinanziariPrec = (String)getMapAttributi(mappaAAEP, PROVENTIFIN).get(VALPREC);
								bilMap.setProventiFinanziariPrec(DecimalFormat.decimalFormat(proventiFinanziariPrec,2));
								
								String interessiPassiviPrec = (String)getMapAttributi(mappaAAEP, TOTINTERESSI).get(VALPREC);
								bilMap.setInteressiPassiviPrec(DecimalFormat.decimalFormat(interessiPassiviPrec,2));
								
								String proventiGestioneAccessoriaPrec = (String)getMapAttributi(mappaAAEP, TOTPROVENTI).get(VALPREC);
								bilMap.setProventiGestioneAccessoriaPrec(DecimalFormat.decimalFormat(proventiGestioneAccessoriaPrec,2));
								
								String oneriGestioneAccessoriaPrec = (String)getMapAttributi(mappaAAEP, TOTONERI).get(VALPREC);
								bilMap.setOneriGestioneAccessoriaPrec(DecimalFormat.decimalFormat(oneriGestioneAccessoriaPrec,2));
								
								String diffABPrec = differenzaNumeri(totaleValoreProduzionePrec, totaleCostiProduzionePrec);
								
								if (!StringUtils.isBlank(totaleValoreProduzionePrec) || !StringUtils.isBlank(totaleCostiProduzionePrec) ||
									!StringUtils.isBlank(ammortamentiMatPrec) || !StringUtils.isBlank(ammortamentiImmPrec)) {
									
									ebitdaPrec = sommaNumeri(diffABPrec, ammortamentiMatPrec, ammortamentiImmPrec, "0");
								}
								logger.info("[Bilancio::inject] ebitdaPrec:" + ebitdaPrec);
								
								// calcoliamo il valore se e solo se almeno un addendo risulta valorizzato, JIRA 281
								if (!StringUtils.isBlank(totaleValoreProduzionePrec) || !StringUtils.isBlank(totaleCostiProduzionePrec) ||
									!StringUtils.isBlank(proventiFinanziariPrec) || !StringUtils.isBlank(proventiGestioneAccessoriaPrec) || !StringUtils.isBlank(oneriGestioneAccessoriaPrec)) {
								
									ebitPrec = sommaNumeri(diffABPrec, proventiFinanziariPrec, proventiGestioneAccessoriaPrec, oneriGestioneAccessoriaPrec);
								}
								logger.info("[Bilancio::inject] ebitPrec:" + ebitPrec);
								bilMap.setIndiceRotazionePrec( ""); // Voce non presente in AAEP
								bilMap.setDsoPrec( ""); // Voce non presente in AAEP
								bilMap.setDpoPrec( ""); // Voce non presente in AAEP
								bilMap.setUlaPrec( ""); // Voce non presente in AAEP
								
								aaepMap = bilMap;
								
							} // if solo annoAAEPPrec 2016
						}
					}
				}
			}
			logger.info("[Bilancio::inject] bilMap:" + bilMap);
			logger.info("[Bilancio::inject] aaepMap:" + aaepMap);

			// Se bilancio risulta popolato e non sono in configurazione bando custom per bilancio
			if (_bilancio != null && !_bilancio.isEmpty() && !isDatiBilancioCustom) {
							
				bilMap.setAnno(_bilancio.getAnno());
				bilMap.setAnnoPrec(_bilancio.getAnnoPrec());
				
				bilMap.setSpeseRS(_bilancio.getSpeseRS());
				bilMap.setSpeseRSPrec(_bilancio.getSpeseRSPrec());
				
				bilMap.setTotCreditiVsClienti(_bilancio.getTotCreditiVsClienti());
				bilMap.setTotCreditiVsClientiPrec(_bilancio.getTotCreditiVsClientiPrec());
				
				bilMap.setCreditiCommScad(_bilancio.getCreditiCommScad());
				bilMap.setCreditiCommScadPrec(_bilancio.getCreditiCommScadPrec());
				
				bilMap.setDisponibilitaLiquide(_bilancio.getDisponibilitaLiquide());
				bilMap.setDisponibilitaLiquidePrec(_bilancio.getDisponibilitaLiquidePrec());
				
				bilMap.setTotaleBilancio(_bilancio.getTotaleBilancio());
				bilMap.setTotaleBilancioPrec(_bilancio.getTotaleBilancioPrec());
				
				bilMap.setTotalePatrimonio(_bilancio.getTotalePatrimonio());
				bilMap.setTotalePatrimonioPrec(_bilancio.getTotalePatrimonioPrec());
				
				bilMap.setDebitiSoci(_bilancio.getDebitiSoci());
				bilMap.setDebitiSociPrec(_bilancio.getDebitiSociPrec());
				
				bilMap.setDebitiBanche(_bilancio.getDebitiBanche());
				bilMap.setDebitiBanchePrec(_bilancio.getDebitiBanchePrec());
				
				bilMap.setDebitiVsFornitori(_bilancio.getDebitiVsFornitori());
				bilMap.setDebitiVsFornitoriPrec(_bilancio.getDebitiVsFornitoriPrec());
				
				bilMap.setDebitiFornScad(_bilancio.getDebitiFornScad());
				bilMap.setDebitiFornScadPrec(_bilancio.getDebitiFornScadPrec());
				
				bilMap.setDebitiImpreseCollegate(_bilancio.getDebitiImpreseCollegate());
				bilMap.setDebitiImpreseCollegatePrec(_bilancio.getDebitiImpreseCollegatePrec());
				
				bilMap.setDebitiControllanti(_bilancio.getDebitiControllanti());
				bilMap.setDebitiControllantiPrec(_bilancio.getDebitiControllantiPrec());
				
				bilMap.setDebitiTributari(_bilancio.getDebitiTributari());
				bilMap.setDebitiTributariPrec(_bilancio.getDebitiTributariPrec());
				
				bilMap.setDebitiTributariScad(_bilancio.getDebitiTributariScad());
				bilMap.setDebitiTributariScadPrec(_bilancio.getDebitiTributariScadPrec());
				
				bilMap.setRicavi(_bilancio.getRicavi());
				bilMap.setRicaviPrec(_bilancio.getRicaviPrec());
				
				bilMap.setTotaleValoreProduzione(_bilancio.getTotaleValoreProduzione());
				bilMap.setTotaleValoreProduzionePrec(_bilancio.getTotaleValoreProduzionePrec());
				
				bilMap.setVariazioneLavoriInCorso(_bilancio.getVariazioneLavoriInCorso());
				bilMap.setVariazioneLavoriInCorsoPrec(_bilancio.getVariazioneLavoriInCorsoPrec());
				
				bilMap.setAmmortamentiImm(_bilancio.getAmmortamentiImm());
				bilMap.setAmmortamentiImmPrec(_bilancio.getAmmortamentiImmPrec());
				
				bilMap.setAmmortamentiMat(_bilancio.getAmmortamentiMat());
				bilMap.setAmmortamentiMatPrec(_bilancio.getAmmortamentiMatPrec());
				
				bilMap.setTotaleCostiProduzione(_bilancio.getTotaleCostiProduzione());
				bilMap.setTotaleCostiProduzionePrec(_bilancio.getTotaleCostiProduzionePrec());
				
				bilMap.setProventiFinanziari(_bilancio.getProventiFinanziari());
				bilMap.setProventiFinanziariPrec(_bilancio.getProventiFinanziariPrec());
				
				bilMap.setInteressiPassivi(_bilancio.getInteressiPassivi());
				bilMap.setInteressiPassiviPrec(_bilancio.getInteressiPassiviPrec());
				
				bilMap.setProventiGestioneAccessoria(_bilancio.getProventiGestioneAccessoria());
				bilMap.setProventiGestioneAccessoriaPrec(_bilancio.getProventiGestioneAccessoriaPrec());
				
				bilMap.setOneriGestioneAccessoria(_bilancio.getOneriGestioneAccessoria());
				bilMap.setOneriGestioneAccessoriaPrec(_bilancio.getOneriGestioneAccessoriaPrec());
				
				ebitda = _bilancio.getEbitda();
				ebitdaPrec = _bilancio.getEbitdaPrec();
				
				ebit = _bilancio.getEbit();
				ebitPrec = _bilancio.getEbitPrec();
				
				bilMap.setIndiceRotazione(_bilancio.getIndiceRotazione());
				bilMap.setIndiceRotazionePrec(_bilancio.getIndiceRotazionePrec());
				
				bilMap.setDso(_bilancio.getDso());
				bilMap.setDsoPrec(_bilancio.getDsoPrec());
				
				bilMap.setDpo(_bilancio.getDpo());
				bilMap.setDpoPrec(_bilancio.getDpoPrec());
				
				bilMap.setUla(_bilancio.getUla());
				bilMap.setUlaPrec(_bilancio.getUlaPrec());

			} 
			if(!mostraMsgAAEP.equals("S")){
				mostraMsgAAEP = "N";
			}

			// bilancio assente in xml e sono in dati bilancio custom 
			if ( (_bilancio == null || _bilancio.isEmpty()) && isDatiBilancioCustom ) 
			{
				bilMap.setAnno("31/12/" + annoCorrenteM1);
				bilMap.setAnnoPrec("31/12/"+ annoCorrenteM2);
				bilMap.setFonteDatiAnno( "XML");
			    bilMap.setFonteDatiAnnoPrec( "XML");
			}
			
			
			/* sposto qui il controllo inizio */
			// Se bilancio risulta popolato e sono in configurazione bando custom per bilancio
			if (_bilancio != null && !_bilancio.isEmpty() && isDatiBilancioCustom) {
			
				// recupero anno da nodo xml
				annoXML = ( (String) _bilancio.getAnno() ); 
				
				// se presente data completa
				if( annoXML != null && !annoXML.isEmpty() )
				{
					
					// recupero solo anno
					annoXML = ( (String) _bilancio.getAnno().substring(6));
					
					// verifico se annoXML == 2017
					if( annoXML.equals( annoCorrenteM1) ){
						
						// fda
						// fonteDatiAnno = "XML";
						bilMap.setFonteDatiAnno(_bilancio.getFonteDatiAnno());
						
						bilMap.setAnno(_bilancio.getAnno());
						bilMap.setSpeseRS(_bilancio.getSpeseRS());
						bilMap.setTotCreditiVsClienti(_bilancio.getTotCreditiVsClienti());
						bilMap.setCreditiCommScad(_bilancio.getCreditiCommScad());
						bilMap.setDisponibilitaLiquide(_bilancio.getDisponibilitaLiquide());
						bilMap.setTotaleBilancio(_bilancio.getTotaleBilancio());
						bilMap.setTotalePatrimonio(_bilancio.getTotalePatrimonio());
						bilMap.setDebitiSoci(_bilancio.getDebitiSoci());
						bilMap.setDebitiBanche(_bilancio.getDebitiBanche());
						bilMap.setDebitiVsFornitori(_bilancio.getDebitiVsFornitori());
						bilMap.setDebitiFornScad(_bilancio.getDebitiFornScad());
						bilMap.setDebitiImpreseCollegate(_bilancio.getDebitiImpreseCollegate());
						bilMap.setDebitiControllanti(_bilancio.getDebitiControllanti());
						bilMap.setDebitiTributari(_bilancio.getDebitiTributari());
						bilMap.setDebitiTributariScad(_bilancio.getDebitiTributariScad());
						bilMap.setRicavi(_bilancio.getRicavi());
						bilMap.setTotaleValoreProduzione(_bilancio.getTotaleValoreProduzione());
						bilMap.setVariazioneLavoriInCorso(_bilancio.getVariazioneLavoriInCorso());
						bilMap.setAmmortamentiImm(_bilancio.getAmmortamentiImm());
						bilMap.setAmmortamentiMat(_bilancio.getAmmortamentiMat());
						bilMap.setTotaleCostiProduzione(_bilancio.getTotaleCostiProduzione());
						bilMap.setProventiFinanziari(_bilancio.getProventiFinanziari());
						bilMap.setInteressiPassivi(_bilancio.getInteressiPassivi());
						bilMap.setProventiGestioneAccessoria(_bilancio.getProventiGestioneAccessoria());
						bilMap.setOneriGestioneAccessoria(_bilancio.getOneriGestioneAccessoria());
						ebitda = _bilancio.getEbitda();
						ebit = _bilancio.getEbit();
						bilMap.setIndiceRotazione(_bilancio.getIndiceRotazione());
						bilMap.setDso(_bilancio.getDso());
						bilMap.setDpo(_bilancio.getDpo());
						bilMap.setUla(_bilancio.getUla());
					} 
					
					// confronto con ( annoCorrente-2)
					// se presente, anno = 2016, posizionare in colonna: II - fdaP ( fonteDatiAnnoPrecedente )
					if( annoXML.equals( annoCorrenteM2)){
						
						// fonteDatiAnnoPrec = "XML";
						bilMap.setFonteDatiAnnoPrec(_bilancio.getFonteDatiAnnoPrec());
						
						bilMap.setAnnoPrec(_bilancio.getAnno());
						bilMap.setSpeseRSPrec(_bilancio.getSpeseRS());
						bilMap.setTotCreditiVsClientiPrec(_bilancio.getTotCreditiVsClienti());
						bilMap.setCreditiCommScadPrec(_bilancio.getCreditiCommScad());
						bilMap.setDisponibilitaLiquidePrec(_bilancio.getDisponibilitaLiquide());
						bilMap.setTotaleBilancioPrec(_bilancio.getTotaleBilancio());
						bilMap.setTotalePatrimonioPrec(_bilancio.getTotalePatrimonio());
						bilMap.setDebitiSociPrec(_bilancio.getDebitiSoci());
						bilMap.setDebitiBanchePrec(_bilancio.getDebitiBanche());
						bilMap.setDebitiVsFornitoriPrec(_bilancio.getDebitiVsFornitori());
						bilMap.setDebitiFornScadPrec(_bilancio.getDebitiFornScad());
						bilMap.setDebitiImpreseCollegatePrec(_bilancio.getDebitiImpreseCollegate());
						bilMap.setDebitiControllantiPrec(_bilancio.getDebitiControllanti());
						bilMap.setDebitiTributariPrec(_bilancio.getDebitiTributari());
						bilMap.setDebitiTributariScadPrec(_bilancio.getDebitiTributariScad());
						bilMap.setRicaviPrec(_bilancio.getRicavi());
						bilMap.setTotaleValoreProduzionePrec(_bilancio.getTotaleValoreProduzione());
						bilMap.setVariazioneLavoriInCorsoPrec(_bilancio.getVariazioneLavoriInCorso());
						bilMap.setAmmortamentiImmPrec(_bilancio.getAmmortamentiImm());
						bilMap.setAmmortamentiMatPrec(_bilancio.getAmmortamentiMat());
						bilMap.setTotaleCostiProduzionePrec(_bilancio.getTotaleCostiProduzione());
						bilMap.setProventiFinanziariPrec(_bilancio.getProventiFinanziari());
						bilMap.setInteressiPassiviPrec(_bilancio.getInteressiPassivi());
						bilMap.setProventiGestioneAccessoriaPrec(_bilancio.getProventiGestioneAccessoria());
						bilMap.setOneriGestioneAccessoriaPrec(_bilancio.getOneriGestioneAccessoria());
						ebitdaPrec = _bilancio.getEbitda();
						ebitPrec = _bilancio.getEbit();
						bilMap.setIndiceRotazionePrec(_bilancio.getIndiceRotazione());
						bilMap.setDsoPrec(_bilancio.getDso());
						bilMap.setDpoPrec(_bilancio.getDpo());
						bilMap.setUlaPrec(_bilancio.getUla());
					}
				}
					
				
				// recupero annoPrec da nodo xml
				annoXMLPrec = ((String) _bilancio.getAnnoPrec());
				
				if( annoXMLPrec != null && !annoXMLPrec.isEmpty()) {
					
					annoXMLPrec = ((String) _bilancio.getAnnoPrec().substring(6));
					
					// se annoXMLPrec = 2017, posizionare in colonna I, fda = XML
					if( annoXMLPrec.equals( annoCorrenteM1) ){
					
						// fda
						// fonteDatiAnno = "XML";
						bilMap.setFonteDatiAnno( _bilancio.getFonteDatiAnno());
						
						// visualizzo solo dati compresi nei 2 anni precedenti all'anno corrente
						bilMap.setAnno(_bilancio.getAnnoPrec());
						bilMap.setSpeseRS(_bilancio.getSpeseRSPrec());
						bilMap.setTotCreditiVsClienti(_bilancio.getTotCreditiVsClientiPrec());
						bilMap.setCreditiCommScad(_bilancio.getCreditiCommScadPrec());
						bilMap.setDisponibilitaLiquide(_bilancio.getDisponibilitaLiquidePrec());
						bilMap.setTotaleBilancio(_bilancio.getTotaleBilancioPrec());
						bilMap.setTotalePatrimonio(_bilancio.getTotalePatrimonioPrec());
						bilMap.setDebitiSoci(_bilancio.getDebitiSociPrec());
						bilMap.setDebitiBanche(_bilancio.getDebitiBanchePrec());
						bilMap.setDebitiVsFornitori(_bilancio.getDebitiVsFornitoriPrec());
						bilMap.setDebitiFornScad(_bilancio.getDebitiFornScadPrec());
						bilMap.setDebitiImpreseCollegate(_bilancio.getDebitiImpreseCollegatePrec());
						bilMap.setDebitiControllanti(_bilancio.getDebitiControllantiPrec());
						bilMap.setDebitiTributari(_bilancio.getDebitiTributariPrec());
						bilMap.setDebitiTributariScad(_bilancio.getDebitiTributariScadPrec());
						bilMap.setRicavi(_bilancio.getRicaviPrec());
						bilMap.setTotaleValoreProduzione(_bilancio.getTotaleValoreProduzionePrec());
						bilMap.setVariazioneLavoriInCorso(_bilancio.getVariazioneLavoriInCorsoPrec());
						bilMap.setAmmortamentiImm(_bilancio.getAmmortamentiImmPrec());
						bilMap.setAmmortamentiMat(_bilancio.getAmmortamentiMatPrec());
						bilMap.setTotaleCostiProduzione(_bilancio.getTotaleCostiProduzionePrec());
						bilMap.setProventiFinanziari(_bilancio.getProventiFinanziariPrec());
						bilMap.setInteressiPassivi(_bilancio.getInteressiPassiviPrec());
						bilMap.setProventiGestioneAccessoria(_bilancio.getProventiGestioneAccessoriaPrec());
						bilMap.setOneriGestioneAccessoria(_bilancio.getOneriGestioneAccessoriaPrec());
						ebitda = _bilancio.getEbitdaPrec();
						ebit = _bilancio.getEbitPrec();
						bilMap.setIndiceRotazione(_bilancio.getIndiceRotazionePrec());
						bilMap.setDso(_bilancio.getDsoPrec());
						bilMap.setDpo(_bilancio.getDpoPrec());
						bilMap.setUla(_bilancio.getUlaPrec());
						
					} // fine annoXMLPrec = 2017
					
					// annoXMLPrec = 2016
					if( annoXMLPrec.equals( annoCorrenteM2) ){
					
						// fonteDatiAnnoPrec = "XML";
						bilMap.setFonteDatiAnnoPrec( _bilancio.getFonteDatiAnnoPrec());
						
						bilMap.setAnnoPrec(_bilancio.getAnnoPrec());
						bilMap.setSpeseRSPrec(_bilancio.getSpeseRSPrec());
						bilMap.setTotCreditiVsClientiPrec(_bilancio.getTotCreditiVsClientiPrec());
						bilMap.setCreditiCommScadPrec(_bilancio.getCreditiCommScadPrec());
						bilMap.setDisponibilitaLiquidePrec(_bilancio.getDisponibilitaLiquidePrec());
						bilMap.setTotaleBilancioPrec(_bilancio.getTotaleBilancioPrec());
						bilMap.setTotalePatrimonioPrec(_bilancio.getTotalePatrimonioPrec());
						bilMap.setDebitiSociPrec(_bilancio.getDebitiSociPrec());
						bilMap.setDebitiBanchePrec(_bilancio.getDebitiBanchePrec());
						bilMap.setDebitiVsFornitoriPrec(_bilancio.getDebitiVsFornitoriPrec());
						bilMap.setDebitiFornScadPrec(_bilancio.getDebitiFornScadPrec());
						bilMap.setDebitiImpreseCollegatePrec(_bilancio.getDebitiImpreseCollegatePrec());
						bilMap.setDebitiControllantiPrec(_bilancio.getDebitiControllantiPrec());
						bilMap.setDebitiTributariPrec(_bilancio.getDebitiTributariPrec());
						bilMap.setDebitiTributariScadPrec(_bilancio.getDebitiTributariScadPrec());
						bilMap.setRicaviPrec(_bilancio.getRicaviPrec());
						bilMap.setTotaleValoreProduzionePrec(_bilancio.getTotaleValoreProduzionePrec());
						bilMap.setVariazioneLavoriInCorsoPrec(_bilancio.getVariazioneLavoriInCorsoPrec());
						bilMap.setAmmortamentiImmPrec(_bilancio.getAmmortamentiImmPrec());
						bilMap.setAmmortamentiMatPrec(_bilancio.getAmmortamentiMatPrec());
						bilMap.setTotaleCostiProduzionePrec(_bilancio.getTotaleCostiProduzionePrec());
						bilMap.setProventiFinanziariPrec(_bilancio.getProventiFinanziariPrec());
						bilMap.setInteressiPassiviPrec(_bilancio.getInteressiPassiviPrec());
						bilMap.setProventiGestioneAccessoriaPrec(_bilancio.getProventiGestioneAccessoriaPrec());
						bilMap.setOneriGestioneAccessoriaPrec(_bilancio.getOneriGestioneAccessoriaPrec());
						ebitdaPrec = _bilancio.getEbitdaPrec();
						ebitPrec = _bilancio.getEbitPrec();
						bilMap.setIndiceRotazionePrec(_bilancio.getIndiceRotazionePrec());
						bilMap.setDsoPrec(_bilancio.getDsoPrec());
						bilMap.setDpoPrec(_bilancio.getDpoPrec());
						bilMap.setUlaPrec(_bilancio.getUlaPrec());
						
					} // fine annoXMLPrec = 2016
				}// fine verifica annoXMLPrec 2017 o 2016
			}
			
			if(comando != null && comando.contains("C_pulisci_campi_bilancio")){
				bilMap = new BilancioVO();	
				ebitda = "";
				ebitdaPrec = "";		
				ebit = "";
				ebitPrec = "";
			}

			
			ns.setAaepMap(aaepMap);
			ns.setBilMap(bilMap);
			ns.setMostraMsgAAEP(mostraMsgAAEP);


			ns.setEnteImpresa(enteImpresa);
			// passo questi 4 perche inizialmente vengono calcolati con i dati di AAEP
			ns.setEbitda(ebitda);
			ns.setEbitdaPrec(ebitdaPrec);
			ns.setEbit(ebit);
			ns.setEbitPrec(ebitPrec);
						
	        return ns;
	    }
	    finally {
			logger.info("Bilancio::inject] _bilancio END");
	    }
	    
	  }

	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
		DateValidator validator = DateValidator.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
			
		logger.info("[Bilancio::modelValidate]  _bilancio  BEGIN");

		String ERRMSG_CAMPO_OBBLIGATORIO_BIL = "- il campo é obbligatorio";
		String ERRMSG_FORMATO_CAMPO_DATA = "- il formato della data non é valido";
		String ERRMSG_CAMPO_NUMERICO = "- il campo deve essere numerico";
		String ERRMSG_ANNO = "- la data indicata deve essere successiva alla data costituzione impresa";
		String ERRMSG_ANNO_COSTIMPRESA = "- impossibile determinare la data costituzione impresa";
		String ERRMSG_FORMATO_IMPORTO = "- il valore deve essere numerico con al massimo 2 decimali";  
		String ERRMSG_ANNO_FUTURO = "- la data indicata non puo' essere successiva alla data odierna";
		String ERRMSG_ANNO_PREC_SUCCESSIVO_AD_ANNO = "- la data indicata deve essere precedente alla data dell'ultimo esercizio finanziario chiuso";


		BilancioVO _bilancio = input._bilancio;
		String annoPrec = "";
		
		if (_bilancio != null) {

			  if (input.bilancio_anno.equals("true")) {
				Date dataCorrente = new Date(System.currentTimeMillis());
				String anno = (String)_bilancio.getAnno();
				annoPrec = (String)_bilancio.getAnnoPrec();
				logger.info("[Bilancio::modelValidate] anno="+anno);
				String dataCostituzioneImpresa = "";

				// anno
				if (StringUtils.isBlank(anno)) {
					addMessage(newMessages,"_bilancio_anno", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					logger.info("[Bilancio::modelValidate]  anno non valorizzato");
				} else {			  
					Date annoParse = sdf.parse(anno, new ParsePosition(0));
					logger.info("[Bilancio::modelValidate] annoParse="+annoParse);
					
					if (!validator.isValid(anno, "dd/MM/yyyy") || !(anno.matches("\\d{2}/\\d{2}/\\d{4}")) ) {			
					   addMessage(newMessages,"_bilancio_anno", ERRMSG_FORMATO_CAMPO_DATA);
					   logger.info("[Bilancio::modelValidate] formato anno non corretto");
					}else {
						// Controllo che la data sia uguale o successiva alla data di costituzione dell'impresa
						//_costituzioneImpresa.dataCostituzioneImpresa
						CostituzioneImpresaVo _costImpresa = input._costituzioneImpresa;
						if (_costImpresa != null) {
							dataCostituzioneImpresa = (String)_costImpresa.getDataCostituzioneImpresa();
							logger.info("[Bilancio::modelValidate] dataCostituzioneImpresa="+dataCostituzioneImpresa);

							Date dataCostituzioneImpresaParse = sdf.parse(dataCostituzioneImpresa, new ParsePosition(0));
							logger.info("[Bilancio::modelValidate] dataCostituzioneImpresaParse="+dataCostituzioneImpresaParse);
							if (annoParse.before(dataCostituzioneImpresaParse) || (annoParse.compareTo(dataCostituzioneImpresaParse)==0)) {
								logger.info("[Bilancio::modelValidate] - l'anno indicato é precedente alla data costituzione impresa:" + dataCostituzioneImpresa);
								addMessage(newMessages,"_bilancio_anno", ERRMSG_ANNO);
							}else{
							   //la data dell'ultimo esercizio finanziario chiuso non puo' essere futura
							   if(annoParse.after(dataCorrente)) {
								  logger.info("[Bilancio::modelValidate] - l'anno indicato é successivo alla data corrente");
								  addMessage(newMessages,"_bilancio_anno", ERRMSG_ANNO_FUTURO);
							   }
							   //inizio i controlli su penultimo esercizio chiuso
							   //se specificato, deve essere una data valida,  precedente all'ultimo esercizio chiuso (e quindi non futura)					       
							   if (!StringUtils.isBlank(annoPrec)) {
								   
								  if (!validator.isValid(annoPrec, "dd/MM/yyyy") || !(annoPrec.matches("\\d{2}/\\d{2}/\\d{4}")) ) {			
									 addMessage(newMessages,"_bilancio_annoPrec", ERRMSG_FORMATO_CAMPO_DATA);
									 logger.info("[Bilancio::modelValidate]  formato annoPrec non corretto");
								  }else {
									 Date annoPrecParse = sdf.parse(annoPrec, new ParsePosition(0));
									 if(annoPrecParse.after(annoParse) || (annoPrecParse.compareTo(annoParse)==0)) {
										logger.info("[Bilancio::modelValidate] - l'anno indicato é successivo alla data dell'ultimo esercizio finanziario chiuso");
										addMessage(newMessages,"_bilancio_annoPrec", ERRMSG_ANNO_PREC_SUCCESSIVO_AD_ANNO);
									 }else{
										if(annoPrecParse.before(dataCostituzioneImpresaParse) || (annoPrecParse.compareTo(dataCostituzioneImpresaParse)==0)) {
										  logger.info("[Bilancio::modelValidate] - l'anno prec indicato é precedente alla data costituzione impresa:" + dataCostituzioneImpresa);
										  addMessage(newMessages,"_bilancio_annoPrec", ERRMSG_ANNO);
										}					             
									 }	
								  }
							   }
							}
							logger.info("[Bilancio::modelValidate] anno >= dataCostituzioneImpresaParse=");
						} else{
							logger.info("[Bilancio::modelValidate] - data costituzione impresa nulla " );
							addMessage(newMessages,"_bilancio_anno", ERRMSG_ANNO_COSTIMPRESA);
						}
					} //chiude else del test isValid su anno
					logger.info("[Bilancio::modelValidate]  data costituzione impresa:" + dataCostituzioneImpresa);
				}//chiude else del test isBlank su anno
			}				
				
			boolean almenoUnValorePerAnnoPrec = false;
			
			/**Jira: 1697 */
			if ("true".equals(input._campi_bilancio_obbligatori)) {
				
				if (StringUtils.isBlank(annoPrec)) {
					addMessage(newMessages,"_bilancio_annoPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					logger.info("[Bilancio::modelValidate] anno precedente non valorizzato");
				}
			}
			/** speseRS */
			if (input.bilancio_speseRS.equals("true")) {
				String speseRS = (String)_bilancio.getSpeseRS();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					if(StringUtils.isBlank(speseRS)){
						addMessage(newMessages, "_bilancio_speseRS", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(speseRS) && (!speseRS.matches("^-?\\d+(,\\d{1,2})?$"))){ 
					addMessage(newMessages,"_bilancio_speseRS", ERRMSG_FORMATO_IMPORTO);				
					logger.info("[Bilancio::modelValidate]  speseRS formalmente non corretto: " + speseRS);
				}
				
				String speseRSPrec = (String)_bilancio.getSpeseRSPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					if(StringUtils.isBlank(speseRSPrec)){
						addMessage(newMessages, "_bilancio_speseRSPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(speseRSPrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!speseRSPrec.matches("^-?\\d+(,\\d{1,2})?$")){ 
					   addMessage(newMessages,"_bilancio_speseRSPrec", ERRMSG_FORMATO_IMPORTO);				
					   logger.info("[Bilancio::modelValidate]  speseRSPrec formalmente non corretto: " + speseRSPrec);
				   }
				}
			}			

			
			/** totCreditiVsClienti */
	 		if (input.bilancio_creditiVsClienti.equals("true")) {	
				String totCreditiVsClienti = (String)_bilancio.getTotCreditiVsClienti();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totCreditiVsClienti)){
						addMessage(newMessages, "_bilancio_totCreditiVsClienti", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totCreditiVsClienti) && (!totCreditiVsClienti.matches("^-?\\d+(,\\d{1,2})?$"))){ 
					addMessage(newMessages,"_bilancio_totCreditiVsClienti", ERRMSG_FORMATO_IMPORTO);				
					logger.info("[Bilancio::modelValidate]  totCreditiVsClienti formalmente non corretto: " + totCreditiVsClienti);
				}
				
				String totCreditiVsClientiPrec = (String)_bilancio.getTotCreditiVsClientiPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totCreditiVsClientiPrec)){
						addMessage(newMessages, "_bilancio_totCreditiVsClientiPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totCreditiVsClientiPrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!totCreditiVsClientiPrec.matches("^-?\\d+(,\\d{1,2})?$")){ 
					   addMessage(newMessages,"_bilancio_totCreditiVsClientiPrec", ERRMSG_FORMATO_IMPORTO);				
					   logger.info("[Bilancio::modelValidate]  totCreditiVsClientiPrec formalmente non corretto: " + totCreditiVsClientiPrec);
				   }
				}
			}			

			
			/** debitiVsFornitori */
			if (input.bilancio_debitiVsFornitori.equals("true")) {
				String debitiVsFornitori = (String)_bilancio.getDebitiVsFornitori();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiVsFornitori)){
						addMessage(newMessages, "_bilancio_debitiVsFornitori", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiVsFornitori) && (!debitiVsFornitori.matches("^-?\\d+(,\\d{1,2})?$"))){ 
					addMessage(newMessages,"_bilancio_debitiVsFornitori", ERRMSG_FORMATO_IMPORTO);				
					logger.info("[Bilancio::modelValidate]  debitiVsFornitori formalmente non corretto: " + debitiVsFornitori);
				}
				
				String debitiVsFornitoriPrec = (String)_bilancio.getDebitiVsFornitoriPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiVsFornitoriPrec)){
						addMessage(newMessages, "_bilancio_debitiVsFornitoriPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiVsFornitoriPrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!debitiVsFornitoriPrec.matches("^-?\\d+(,\\d{1,2})?$")){ 
					   addMessage(newMessages,"_bilancio_debitiVsFornitoriPrec", ERRMSG_FORMATO_IMPORTO);				
					   logger.info("[Bilancio::modelValidate]  debitiVsFornitoriPrec formalmente non corretto: " + debitiVsFornitoriPrec);
				   }
				}
			}					
					
			
			/** creditiCommScad */
			if (input.bilancio_creditiCommScad.equals("true")) {		
				String creditiCommScad = (String)_bilancio.getCreditiCommScad();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(creditiCommScad)){
						addMessage(newMessages, "_bilancio_creditiCommScad", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(creditiCommScad) && (!creditiCommScad.matches("^-?\\d+(,\\d{1,2})?$"))){ 
					addMessage(newMessages,"_bilancio_creditiCommScad", ERRMSG_FORMATO_IMPORTO);				
					logger.info("[Bilancio::modelValidate]  creditiCommScad formalmente non corretto: " + creditiCommScad);
				}
				
				String creditiCommScadPrec = (String)_bilancio.getCreditiCommScadPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(creditiCommScadPrec)){
						addMessage(newMessages, "_bilancio_creditiCommScadPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(creditiCommScadPrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!creditiCommScadPrec.matches("^-?\\d+(,\\d{1,2})?$")){ 
					   addMessage(newMessages,"_bilancio_creditiCommScadPrec", ERRMSG_FORMATO_IMPORTO);				
					   logger.info("[Bilancio::modelValidate]  creditiCommScadPrec formalmente non corretto: " + creditiCommScadPrec);
				   }
				}
			}			
						
			
			/** disponibilitaLiquide */
			if (input.bilancio_disponibilitaLiquide.equals("true")) {
				
				String disponibilitaLiquide = (String)_bilancio.getDisponibilitaLiquide();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(disponibilitaLiquide)){
						addMessage(newMessages, "_bilancio_disponibilitaLiquide", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(disponibilitaLiquide) && (!disponibilitaLiquide.matches("^-?\\d+(,\\d{1,2})?$"))){ 
					addMessage(newMessages,"_bilancio_disponibilitaLiquide", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  disponibilitaLiquide  formalmente non corretto: " + disponibilitaLiquide);
				}
				
				String disponibilitaLiquidePrec = (String)_bilancio.getDisponibilitaLiquidePrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(disponibilitaLiquidePrec)){
						addMessage(newMessages, "_bilancio_disponibilitaLiquidePrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(disponibilitaLiquidePrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!disponibilitaLiquidePrec.matches("^-?\\d+(,\\d{1,2})?$")){ 
					  addMessage(newMessages,"_bilancio_disponibilitaLiquidePrec", ERRMSG_FORMATO_IMPORTO);
					  logger.info("[Bilancio::modelValidate]  disponibilitaLiquidePrec formalmente non corretto: " + disponibilitaLiquidePrec);
				   }
				}
			}		    			

			
			/** totaleBilancio */
			if (input.bilancio_totaleBilancio.equals("true")) {	
				String totaleBilancio = (String)_bilancio.getTotaleBilancio();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totaleBilancio)){
						addMessage(newMessages, "_bilancio_totaleBilancio", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totaleBilancio) && (!totaleBilancio.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_totaleBilancio", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  totaleBilancio formalmente non corretto: " + totaleBilancio);
				}
				
				String totaleBilancioPrec = (String)_bilancio.getTotaleBilancioPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totaleBilancioPrec)){
						addMessage(newMessages, "_bilancio_totaleBilancioPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totaleBilancioPrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!totaleBilancioPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					  addMessage(newMessages,"_bilancio_totaleBilancioPrec", ERRMSG_FORMATO_IMPORTO);
					  logger.info("[Bilancio::modelValidate]  totaleBilancioPrec formalmente non corretto: " + totaleBilancioPrec);
				   }				
				}
			}			
			
			/** totalePatrimonio */
			if (input.bilancio_totalePatrimonio.equals("true")) {			
				String totalePatrimonio = (String)_bilancio.getTotalePatrimonio();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totalePatrimonio)){
						addMessage(newMessages, "_bilancio_totalePatrimonio", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totalePatrimonio) && (!totalePatrimonio.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_totalePatrimonio", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  totalePatrimonio formalmente non corretto: " + totalePatrimonio);
				}
				
				String totalePatrimonioPrec = (String)_bilancio.getTotalePatrimonioPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totalePatrimonioPrec)){
						addMessage(newMessages, "_bilancio_totalePatrimonioPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totalePatrimonioPrec)){
				   almenoUnValorePerAnnoPrec = true;  
				   if(!totalePatrimonioPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					  addMessage(newMessages,"_bilancio_totalePatrimonioPrec", ERRMSG_FORMATO_IMPORTO);
					  logger.info("[Bilancio::modelValidate]  totalePatrimonioPrec formalmente non corretto: " + totalePatrimonioPrec);
				   }
				}
			}			
			
			
			/** debitiSoci */
			if (input.bilancio_debitiSoci.equals("true")) {			
				String debitiSoci = (String)_bilancio.getDebitiSoci();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiSoci)){
						addMessage(newMessages, "_bilancio_debitiSoci", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiSoci) && (!debitiSoci.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiSoci", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiSoci formalmente non corretto: " + debitiSoci);
				}
				String debitiSociPrec = (String)_bilancio.getDebitiSociPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiSociPrec)){
						addMessage(newMessages, "_bilancio_debitiSociPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiSociPrec)){
				   almenoUnValorePerAnnoPrec = true; 
				   if(!debitiSociPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					  addMessage(newMessages,"_bilancio_debitiSociPrec", ERRMSG_FORMATO_IMPORTO);
					  logger.info("[Bilancio::modelValidate]  debitiSociPrec formalmente non corretto: " + debitiSociPrec);
				   }
				}
			}				
			
			
			/** debitiBanche */
			if (input.bilancio_debitiBanche.equals("true")) {			
				String debitiBanche = (String)_bilancio.getDebitiBanche();			
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiBanche)){
						addMessage(newMessages, "_bilancio_debitiBanche", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiBanche) && (!debitiBanche.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiBanche", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiBanche formalmente non corretto: " + debitiBanche);
				}
				
				String debitiBanchePrec = (String)_bilancio.getDebitiBanchePrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiBanchePrec)){
						addMessage(newMessages, "_bilancio_debitiBanchePrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiBanchePrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!debitiBanchePrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_debitiBanchePrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  debitiBanchePrec formalmente non corretto: " + debitiBanchePrec);
					}
				}
			}			
			
			
			/** debitiFornScad */
			if (input.bilancio_debitiFornScad.equals("true")) {			
				String debitiFornScad = (String)_bilancio.getDebitiFornScad();		
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiFornScad)){
						addMessage(newMessages, "_bilancio_debitiFornScad", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiFornScad) && (!debitiFornScad.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiFornScad", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiFornScad formalmente non corretto: " + debitiFornScad);
				}
				
				String debitiFornScadPrec = (String)_bilancio.getDebitiFornScadPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiFornScadPrec)){
						addMessage(newMessages, "_bilancio_debitiFornScadPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiFornScadPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!debitiFornScadPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_debitiFornScadPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  debitiFornScadPrec formalmente non corretto: " + debitiFornScadPrec);
					}
				}
			}			
			
			
			/** debitiImpreseCollegate */	
			if (input.bilancio_debitiImpreseCollegate.equals("true")) {			
				String debitiImpreseCollegate = (String)_bilancio.getDebitiImpreseCollegate();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiImpreseCollegate)){
						addMessage(newMessages, "_bilancio_debitiImpreseCollegate", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiImpreseCollegate) && (!debitiImpreseCollegate.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiImpreseCollegate", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiImpreseCollegate formalmente non corretto: " + debitiImpreseCollegate);
				}
				
				String debitiImpreseCollegatePrec = (String)_bilancio.getDebitiImpreseCollegatePrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiImpreseCollegatePrec)){
						addMessage(newMessages, "_bilancio_debitiImpreseCollegatePrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiImpreseCollegatePrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!debitiImpreseCollegatePrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_debitiImpreseCollegatePrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  debitiImpreseCollegatePrec formalmente non corretto: " + debitiImpreseCollegatePrec);
					}
				}
			}			
			
			
			/** debitiControllanti */	
			if (input.bilancio_debitiControllanti.equals("true")) {			
				String debitiControllanti = (String)_bilancio.getDebitiControllanti();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiControllanti)){
						addMessage(newMessages, "_bilancio_debitiControllanti", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiControllanti) && (!debitiControllanti.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiControllanti", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiControllanti formalmente non corretto: " + debitiControllanti);
				}
				String debitiControllantiPrec = (String)_bilancio.getDebitiControllantiPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiControllantiPrec)){
						addMessage(newMessages, "_bilancio_debitiControllantiPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiControllantiPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!debitiControllantiPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_debitiControllantiPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  debitiControllantiPrec formalmente non corretto: " + debitiControllantiPrec);
					}
				}
			}			
			
			
			
			/** debitiTributari */
			if (input.bilancio_debitiTributari.equals("true")) {		
				String debitiTributari = (String)_bilancio.getDebitiTributari();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiTributari)){
						addMessage(newMessages, "_bilancio_debitiTributari", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiTributari) && (!debitiTributari.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiTributari", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiTributari formalmente non corretto: " + debitiTributari);
				}
				String debitiTributariPrec = (String)_bilancio.getDebitiTributariPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiTributariPrec)){
						addMessage(newMessages, "_bilancio_debitiTributariPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiTributariPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!debitiTributariPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_debitiTributariPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  debitiTributariPrec formalmente non corretto: " + debitiTributariPrec);
					}
				}
			}			
			
			
			/** debitiTributariScad */	
			if (input.bilancio_debitiTributariScad.equals("true")) {			
				String debitiTributariScad = (String)_bilancio.getDebitiTributariScad();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiTributariScad)){
						addMessage(newMessages, "_bilancio_debitiTributariScad", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiTributariScad) && (!debitiTributariScad.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_debitiTributariScad", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  debitiTributariScad formalmente non corretto: " + debitiTributariScad);
				}
				
				String debitiTributariScadPrec = (String)_bilancio.getDebitiTributariScadPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(debitiTributariScadPrec)){
						addMessage(newMessages, "_bilancio_debitiTributariScadPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(debitiTributariScadPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!debitiTributariScadPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_debitiTributariScadPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  debitiTributariScadPrec formalmente non corretto: " + debitiTributariScadPrec);
					}
				}
			}			
			
			
			
			/** ricavi */
			if (input.bilancio_ricavi.equals("true")) {			
				String ricavi = (String)_bilancio.getRicavi();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ricavi)){
						addMessage(newMessages, "_bilancio_ricavi", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ricavi) && (!ricavi.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_ricavi", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  ricavi formalmente non corretto: " + ricavi);
				}
				
				String ricaviPrec = (String)_bilancio.getRicaviPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ricaviPrec)){
						addMessage(newMessages, "_bilancio_ricaviPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ricaviPrec)){
				   almenoUnValorePerAnnoPrec = true;
				   if(!ricaviPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					  addMessage(newMessages,"_bilancio_ricaviPrec", ERRMSG_FORMATO_IMPORTO);
					  logger.info("[Bilancio::modelValidate]  ricaviPrec formalmente non corretto: " + ricaviPrec);
				   }
				}
			}			
			
			
			
			/** totaleValoreProduzione */
			if (input.bilancio_totaleValoreProduzione.equals("true")) {			
				String totaleValoreProduzione = (String)_bilancio.getTotaleValoreProduzione();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totaleValoreProduzione)){
						addMessage(newMessages, "_bilancio_totaleValoreProduzione", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totaleValoreProduzione) && (!totaleValoreProduzione.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_totaleValoreProduzione", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  totaleValoreProduzione formalmente non corretto: " + totaleValoreProduzione);
				}
				String totaleValoreProduzionePrec = (String)_bilancio.getTotaleValoreProduzionePrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totaleValoreProduzionePrec)){
						addMessage(newMessages, "_bilancio_totaleValoreProduzionePrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totaleValoreProduzionePrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!totaleValoreProduzionePrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_totaleValoreProduzionePrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  totaleValoreProduzionePrec formalmente non corretto: " + totaleValoreProduzionePrec);
					}
				}
			}	

			
			
			/** variazioneLavoriInCorso */
			if (input.bilancio_variazioneLavoriInCorso.equals("true")) {			
				String variazioneLavoriInCorso = (String)_bilancio.getVariazioneLavoriInCorso();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(variazioneLavoriInCorso)){
						addMessage(newMessages, "_bilancio_variazioneLavoriInCorso", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(variazioneLavoriInCorso) && (!variazioneLavoriInCorso.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_variazioneLavoriInCorso", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  variazioneLavoriInCorso formalmente non corretto: " + variazioneLavoriInCorso);
				}
				String variazioneLavoriInCorsoPrec = (String)_bilancio.getVariazioneLavoriInCorsoPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(variazioneLavoriInCorsoPrec)){
						addMessage(newMessages, "_bilancio_variazioneLavoriInCorsoPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(variazioneLavoriInCorsoPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!variazioneLavoriInCorsoPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_variazioneLavoriInCorsoPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  variazioneLavoriInCorsoPrec formalmente non corretto: " + variazioneLavoriInCorsoPrec);
					}
				}
			}			
			
			
			/** ammortamentiImm */	
			if (input.bilancio_ammortamentiImm.equals("true")) {			
				String ammortamentiImm = (String)_bilancio.getAmmortamentiImm();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ammortamentiImm)){
						addMessage(newMessages, "_bilancio_ammortamentiImm", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ammortamentiImm) && (!ammortamentiImm.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_ammortamentiImm", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  ammortamentiImm formalmente non corretto: " + ammortamentiImm);
				}
				
				String ammortamentiImmPrec = (String)_bilancio.getAmmortamentiImmPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ammortamentiImmPrec)){
						addMessage(newMessages, "_bilancio_ammortamentiImmPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ammortamentiImmPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!ammortamentiImmPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_ammortamentiImmPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  ammortamentiImmPrec formalmente non corretto: " + ammortamentiImmPrec);
					}
				}
			}		
			
			
			
			/** ammortamentiMat */	
			if (input.bilancio_ammortamentiMat.equals("true")) {			
				String ammortamentiMat = (String)_bilancio.getAmmortamentiMat();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ammortamentiMat)){
						addMessage(newMessages, "_bilancio_ammortamentiMat", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ammortamentiMat) && (!ammortamentiMat.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_ammortamentiMat", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  ammortamentiMat formalmente non corretto: " + ammortamentiMat);
				}
				
				String ammortamentiMatPrec = (String)_bilancio.getAmmortamentiMatPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ammortamentiMatPrec)){
						addMessage(newMessages, "_bilancio_ammortamentiMatPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ammortamentiMatPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!ammortamentiMatPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_ammortamentiMatPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  ammortamentiMatPrec formalmente non corretto: " + ammortamentiMatPrec);
					}
				}
			}	
			
			
			
			/** totaleCostiProduzione */
			if (input.bilancio_totaleCostiProduzione.equals("true")) {			
				String totaleCostiProduzione = (String)_bilancio.getTotaleCostiProduzione();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totaleCostiProduzione)){
						addMessage(newMessages, "_bilancio_totaleCostiProduzione", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totaleCostiProduzione) && (!totaleCostiProduzione.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_totaleCostiProduzione", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  totaleCostiProduzione formalmente non corretto: " + totaleCostiProduzione);
				}
				
				String totaleCostiProduzionePrec = (String)_bilancio.getTotaleCostiProduzionePrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(totaleCostiProduzionePrec)){
						addMessage(newMessages, "_bilancio_totaleCostiProduzionePrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(totaleCostiProduzionePrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!totaleCostiProduzionePrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_totaleCostiProduzionePrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  totaleCostiProduzionePrec formalmente non corretto: " + totaleCostiProduzionePrec);
					}
				}
			}			
			
			
			/** proventiFinanziari */
			if (input.bilancio_proventiFinanziari.equals("true")) {			
				String proventiFinanziari = (String)_bilancio.getProventiFinanziari();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(proventiFinanziari)){
						addMessage(newMessages, "_bilancio_proventiFinanziari", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(proventiFinanziari) && (!proventiFinanziari.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_proventiFinanziari", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  proventiFinanziari formalmente non corretto: " + proventiFinanziari);
				}
				
				String proventiFinanziariPrec = (String)_bilancio.getProventiFinanziariPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(proventiFinanziariPrec)){
						addMessage(newMessages, "_bilancio_proventiFinanziariPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(proventiFinanziariPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!proventiFinanziariPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					  addMessage(newMessages,"_bilancio_proventiFinanziariPrec", ERRMSG_FORMATO_IMPORTO);
					  logger.info("[Bilancio::modelValidate]  proventiFinanziariPrec formalmente non corretto: " + proventiFinanziariPrec);
					}
				}
			}			
				
			/** interessiPassivi */
			if (input.bilancio_interessiPassivi.equals("true")) {			
				String interessiPassivi = (String)_bilancio.getInteressiPassivi();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(interessiPassivi)){
						addMessage(newMessages, "_bilancio_interessiPassivi", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(interessiPassivi) && (!interessiPassivi.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_interessiPassivi", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  interessiPassivi formalmente non corretto: " + interessiPassivi);
				}
				String interessiPassiviPrec = (String)_bilancio.getInteressiPassiviPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(interessiPassiviPrec)){
						addMessage(newMessages, "_bilancio_interessiPassiviPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(interessiPassiviPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!interessiPassiviPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_interessiPassiviPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  interessiPassiviPrec formalmente non corretto: " + interessiPassiviPrec);
					}
				}
			}			
			
			
			/** proventiGestioneAccessoria */
			if (input.bilancio_proventiGestioneAccessoria.equals("true")) {			
				String proventiGestioneAccessoria = (String)_bilancio.getProventiGestioneAccessoria();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(proventiGestioneAccessoria)){
						addMessage(newMessages, "_bilancio_proventiGestioneAccessoria", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(proventiGestioneAccessoria) && (!proventiGestioneAccessoria.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_proventiGestioneAccessoria", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  proventiGestioneAccessoria formalmente non corretto: " + proventiGestioneAccessoria);
				}
				String proventiGestioneAccessoriaPrec = (String)_bilancio.getProventiGestioneAccessoriaPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(proventiGestioneAccessoriaPrec)){
						addMessage(newMessages, "_bilancio_proventiGestioneAccessoriaPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(proventiGestioneAccessoriaPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!proventiGestioneAccessoriaPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_proventiGestioneAccessoriaPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  proventiGestioneAccessoriaPrec formalmente non corretto: " + proventiGestioneAccessoriaPrec);
					}
				}
			}			
				
			
			/** oneriGestioneAccessoria */
			if (input.bilancio_oneriGestioneAccessoria.equals("true")) {			
				String oneriGestioneAccessoria = (String)_bilancio.getOneriGestioneAccessoria();			
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(oneriGestioneAccessoria)){
						addMessage(newMessages, "_bilancio_oneriGestioneAccessoria", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(oneriGestioneAccessoria) && (!oneriGestioneAccessoria.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_oneriGestioneAccessoria", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  oneriGestioneAccessoria formalmente non corretto: " + oneriGestioneAccessoria);
				}
				String oneriGestioneAccessoriaPrec = (String)_bilancio.getOneriGestioneAccessoriaPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(oneriGestioneAccessoriaPrec)){
						addMessage(newMessages, "_bilancio_oneriGestioneAccessoriaPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(oneriGestioneAccessoriaPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!oneriGestioneAccessoriaPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_oneriGestioneAccessoriaPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  oneriGestioneAccessoriaPrec formalmente non corretto: " + oneriGestioneAccessoriaPrec);
					}
				}
			}			
				
			
			/** ebitda */
			if (input.bilancio_ebitda.equals("true")) {			
				String ebitda = (String)_bilancio.getEbitda();			
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ebitda)){
						addMessage(newMessages, "_bilancio_ebitda", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ebitda) && (!ebitda.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_ebitda", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  ebitda formalmente non corretto: " + ebitda);
				}
				String ebitdaPrec = (String)_bilancio.getEbitdaPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ebitdaPrec)){
						addMessage(newMessages, "_bilancio_ebitdaPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ebitdaPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!ebitdaPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_ebitdaPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  ebitdaPrec formalmente non corretto: " + ebitdaPrec);
					}
				}
			}			
				
			
			/** ebit */
			if (input.bilancio_ebit.equals("true")) {			
				String ebit = (String)_bilancio.getEbit();		
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ebit)){
						addMessage(newMessages, "_bilancio_ebit", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ebit) && (!ebit.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_ebit", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  ebit formalmente non corretto: " + ebit);
				}
				String ebitPrec = (String)_bilancio.getEbitPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ebitPrec)){
						addMessage(newMessages, "_bilancio_ebitPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ebitPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!ebitPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_ebitPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  ebitPrec formalmente non corretto: " + ebitPrec);
					}
				}
			}			
				
			
			/** indiceRotazione */	
			if (input.bilancio_indiceRotazione.equals("true")) {			
				String indiceRotazione = (String)_bilancio.getIndiceRotazione();	
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(indiceRotazione)){
						addMessage(newMessages, "_bilancio_indiceRotazione", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(indiceRotazione) && (!indiceRotazione.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_indiceRotazione", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  indiceRotazione formalmente non corretto: " + indiceRotazione);
				}
				
				String indiceRotazionePrec = (String)_bilancio.getIndiceRotazionePrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(indiceRotazionePrec)){
						addMessage(newMessages, "_bilancio_indiceRotazionePrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(indiceRotazionePrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!indiceRotazionePrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_indiceRotazionePrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  indiceRotazionePrec formalmente non corretto: " + indiceRotazionePrec);
					}
				}
			}			
				
			
			/** dso */	
			if (input.bilancio_dso.equals("true")) {			
				String dso = (String)_bilancio.getDso();			
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(dso)){
						addMessage(newMessages, "_bilancio_dso", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(dso) && (!dso.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_dso", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  dso formalmente non corretto: " + dso);
				}
				String dsoPrec = (String)_bilancio.getDsoPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(dsoPrec)){
						addMessage(newMessages, "_bilancio_dsoPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(dsoPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!dsoPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_dsoPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  dsoPrec formalmente non corretto: " + dsoPrec);
					}
				}
			}			
				
			
			/** dpo */	
			if (input.bilancio_dpo.equals("true")) {			
				String dpo = (String)_bilancio.getDpo();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(dpo)){
						addMessage(newMessages, "_bilancio_dpo", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(dpo) && (!dpo.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_dpo", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  dpo formalmente non corretto: " + dpo);
				}
				String dpoPrec = (String)_bilancio.getDpoPrec();

				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(dpoPrec)){
						addMessage(newMessages, "_bilancio_dpoPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(dpoPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!dpoPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_dpoPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  dpoPrec formalmente non corretto: " + dpoPrec);
					}
				}
			}		
			
			
			/** ula */
			if (input.bilancio_ula.equals("true")) {			
				String ula = (String)_bilancio.getUla();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ula)){
						addMessage(newMessages, "_bilancio_ula", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ula) && (!ula.matches("^-?\\d+(,\\d{1,2})?$"))){
					addMessage(newMessages,"_bilancio_ula", ERRMSG_FORMATO_IMPORTO);
					logger.info("[Bilancio::modelValidate]  ula formalmente non corretto: " + ula);
				}
				String ulaPrec = (String)_bilancio.getUlaPrec();
				
				/**Jira: 1697 */
				if ("true".equals(input._campi_bilancio_obbligatori)) {
					
					if(StringUtils.isBlank(ulaPrec)){
						addMessage(newMessages, "_bilancio_ulaPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
					}
				}
				
				if(StringUtils.isNotEmpty(ulaPrec)){
					almenoUnValorePerAnnoPrec = true;
					if(!ulaPrec.matches("^-?\\d+(,\\d{1,2})?$")){
					   addMessage(newMessages,"_bilancio_ulaPrec", ERRMSG_FORMATO_IMPORTO);
					   logger.info("[Bilancio::modelValidate]  ulaPrec formalmente non corretto: " + ulaPrec);
					}
				}
			}	
					
			/**Jira: 1697 */
			if ("true".equals(input._campi_bilancio_obbligatori)) {
				logger.info("[Bilancio::modelValidate]  anno penultimo esercizio finanziario chiuso non valorizzato");
			} else {
				
				//se almeno un valore della colonna 'Penultimo esercizio finanziario chiuso' e' valorizzato, allora diventa obbligatorio il campo data in cima alla colonna
				if(almenoUnValorePerAnnoPrec){
					if(StringUtils.isBlank(annoPrec)) {				
						addMessage(newMessages,"_bilancio_annoPrec", ERRMSG_CAMPO_OBBLIGATORIO_BIL);
						logger.info("[Bilancio::modelValidate]  anno penultimo esercizio finanziario chiuso non valorizzato");
					}
				}
			}  // /- end else std

		}

		logger.info("[Bilancio::modelValidate] _bilancio END");
		return newMessages;
	  }
	  
	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }
	  
	  // sommo numeri
	  private String sommaNumeri(String n1, String n2, String n3, String n4){
			
		String pin1 = (StringUtils.isBlank(n1))?"0":n1.replace(".","");
		String pin2 = (StringUtils.isBlank(n2))?"0":n2.replace(".","");
		String pin3 = (StringUtils.isBlank(n3))?"0":n3.replace(".","");
		String pin4 = (StringUtils.isBlank(n4))?"0":n4.replace(".","");

		Long sommaPi = Long.parseLong(pin1) + Long.parseLong(pin2) + Long.parseLong(pin3) - Long.parseLong(pin4);
		
		// non tratto la somma dei decimali (considero sempre i decimali nulli)
		return sommaPi + ",00";
	  }

	  // sommo numeri
	  private String differenzaNumeri(String n1, String n2)
	  {
		String pin1 = (StringUtils.isBlank(n1))?"0":n1.replace(".","");
		String pin2 = (StringUtils.isBlank(n2))?"0":n2.replace(".","");

		Long diff = Long.parseLong(pin1) - Long.parseLong(pin2);
		
		// non tratto la differenza dei decimali (considero sempre i decimali nulli)
		return diff + "";
	  }

		// scorro la lista restituita da AAEP e popolo una mappa
		private Map generaMappaFromAAEP(BilancioAziendaAAEPVO bilancio, Logger logger){
			
			Map mp = new HashMap();
			String chiave = "";
			
			if (bilancio.getDettaglioBilancio() != null) {
				
				for(DettaglioBilancioAziendaAAEPVO dettaglio:bilancio.getDettaglioBilancio()){
				
				if (dettaglio.getVoce() != null) {
						  
					// elimino eventuali spazi all'inizio ed alla fine della chiave
					// elimino i caratteri speciali
					chiave = dettaglio.getVoce().trim().replaceAll("[^\\p{ASCII}]", "");
					logger.info("[Bilancio::modelValidate] chiave=[" + chiave + "]");
					
					// spese RS
					if(StringUtils.equals(chiave, SPESERS)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					// totale crediti verso clienti
					if(StringUtils.equals(chiave, TOTCREDITIVSCLIENTI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}				
					// anno ultimo e penultimo esercizio finanziario chiuso  OK
					if(StringUtils.equals(chiave, STATOPATRIMONIALE)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					// Disponibilita liquide (Voce C.IV "Totale disponibilité liquide" dello Stato Patrimoniale Attivo)
					if(StringUtils.equals(chiave, TOTDISPLIQUIDE)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					// Totale bilancio (Totale attivo)
					if(StringUtils.equals(chiave, TOTALEATTIVO)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Totale patrimonio netto
					if(StringUtils.equals(chiave, TOTPATRNETTO)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Debiti verso soci per finanziamenti (Voce D 3 Stato Patrimoniale Passivo  "Totale debiti verso soci per finanziamenti")
					if(StringUtils.equals(chiave, TOTDEBITI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Debiti verso soci per finanziamenti (Voce D 4 "Totale debiti verso banche")
					if(StringUtils.equals(chiave, TOTDEBITIVSBANCHE)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Debiti verso soci per finanziamenti (Voce D 7 "Totale debiti verso fornitori")
					if(StringUtils.equals(chiave, TOTDEBITIVSFORNITORI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Debiti verso soci per finanziamenti (Voce D 10 "Totale debiti verso imprese collegate")
					if(StringUtils.equals(chiave, TOTDEBITIVSIMPCOLLEGATE)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Debiti verso soci per finanziamenti (Voce D 11 "Totale debiti verso imprese controllanti")
					if(StringUtils.equals(chiave, TOTDEBITIVSIMPCONTROLLANTI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Voce D 12 "Totale debiti tributari"
					if(StringUtils.equals(chiave, TOTDEBITITRIBUTARI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Ricavi (voce A1 conto economico)
					if(StringUtils.equals(chiave, RICAVI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Totale Valore della produzione  (voce A del conto  economico)
					if(StringUtils.equals(chiave, TOTVALPRODUZIONE)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Variazione lavori in corso su ordinazione (voce A 3 del conto  economico)
					if(StringUtils.equals(chiave, VARIAZIONELAVORIINCORSO)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Ammortamenti immateriali (Voce B 10 a) del conto economico) //
					if(StringUtils.equals(chiave, AMMIMMATERIALI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Ammortamenti materiali (Voce B 10 b) del conto economico) //
					if(StringUtils.equals(chiave, AMMMATERIALI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Totale Costi della produzione (voce B del conto economico)
					if(StringUtils.equals(chiave, TOTCOSTIPRODUZIONE)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Proventi finanziari (Voce C16 del conto economico "Totale altri proventi finanziari")
					if(StringUtils.equals(chiave, PROVENTIFIN)){ //
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Interessi passivi rettificati (Voce C 17 del conto economico "Totale interessi e altri oneri finanziari")
					if(StringUtils.equals(chiave, TOTINTERESSI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Proventi gestione accessoria (Voce E 20 del conto economico "Totale proventi") //
					if(StringUtils.equals(chiave, TOTPROVENTI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					//Oneri gestione accessoria (Voce E 21 del conto economico "Totale oneri") // 
					if(StringUtils.equals(chiave, TOTONERI)){
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
					// SALVARE SU XML PER AAEP
					if(StringUtils.equals(chiave, DIFFAB)){ //
						Map mpTmp = new HashMap();
						mpTmp.put(VALATT, dettaglio.getAnnoAttuale());
						mpTmp.put(VALPREC, dettaglio.getAnnoPrecedente());
						mp.put(chiave, mpTmp);
					}
				}
			  }
			}
			return mp;
		}
		
		// gestisco anche le chiavi che non esistono
		private Map getMapAttributi(Map mappaAAEP, String chiave){
			Map mp = new HashMap();
			if(mappaAAEP.get(chiave)!=null){
				mp.put(VALATT, ((HashMap) mappaAAEP.get(chiave)).get(VALATT));
				mp.put(VALPREC, ((HashMap) mappaAAEP.get(chiave)).get(VALPREC));
			}else{
				mp.put(VALATT, "");
				mp.put(VALPREC, "");
			}
			return mp;
		}

}
