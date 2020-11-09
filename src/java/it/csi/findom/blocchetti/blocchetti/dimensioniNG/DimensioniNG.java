/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import it.csi.findom.blocchetti.blocchetti.bilancio.BilancioAziendaAAEPVO;
import it.csi.findom.blocchetti.blocchetti.bilancio.DettaglioBilancioAziendaAAEPVO;
//import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.CaratteristicheProgettoNeveNGDAO;
import it.csi.findom.blocchetti.blocchetti.costituzioneImpresa.CostituzioneImpresaVo;
import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.util.TrasformaClassiAAEP2VO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.ws.aaep.AaepWs;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DimensioniNG extends Commonality {

	DimensioniNGInput input = new DimensioniNGInput();
	
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo infol, List<CommonalityMessage> arg1) throws CommonalityException {
		// <#-- note di dipendenze da altre commonalities: nessuna -->

		FinCommonInfo info = (FinCommonInfo) infol;
		DimensioniNGOutput output = new DimensioniNGOutput();
		
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[DimensioniNG::inject] DimensioniNG  BEGIN");
		
		
		/** --------------------------------------------------------- Jira: 1522 -:  -inizio */
		Integer idSportello = info.getStatusInfo().getNumSportello();
		logger.info("debug: idSportello vale= " + idSportello); // 80

		String codeTipoBeneficiario = "";
		codeTipoBeneficiario = DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) != null ? DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) : "";
		logger.info("test-lettura: codeTipoBeneficiario: "	+ codeTipoBeneficiario);
		
		
		String RICAVI = "A.1. ricavi delle vendite e delle prestazioni";
		String TOTBILANCIO = "Totale attivo";
		
		List<RisorseUmaneVO> risorseUmaneList = null;
		List<DatiImpresaVO> datiImpresaList = null;
		List<DatiImpresaVO> datiImpresaListAAEP = null;
		List<ClassificazioneDimensioniImpresaVO> classificazioneDimensioniImpresaList = new ArrayList<ClassificazioneDimensioniImpresaVO>();	
		ImpresaVO enteImpresa = null;
		DecimalFormat decimalFormatter = new DecimalFormat();
		
		//MB2017_12_19 ini
		int annoCorrente = 0;
		if (input._anno_riferimento_per_storico != null) {
		   annoCorrente = input._anno_riferimento_per_storico.intValue();
		} else {
		   annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
		}
		//MB2017_12_19 fine 
						
		String viewSezioneDimensioniImpresa = "true";
		String mostraMsgAAEP = "N";  //MB2017_09_29 definisco sempre, indipendentemente da var di configurazione, la variabile per evitare che nell'html sia usata senza essere definita
		// anni intercorsi tra l'anno corrente e l'anno di costituzione dell'impresa
		int anniEsercizio = 0;
		
		if (input._ente_impresa_dimensioni.equals("true")) {
//			enteImpresa = (ImpresaVO)SessionCache.getInstance().get("enteImpresa");
			enteImpresa = (ImpresaVO)TrasformaClassiAAEP2VO.impresa2ImpresaVO((Impresa)SessionCache.getInstance().get("enteImpresa"));
	
			// Se l'impresa e' in fase di costituzione o se e' stata costituita nell'anno in corso, 
			// la sezione "dimensioni dell'impresa" non deve essere visualizzata
			
			//da Jira FINDOM-21: il comportamento corretto dovrebbe essere il seguente:
			//1 ) La combobox 'Dimensione dell'impresa' é sempre visualizzata
			//2) la tabella con lo storico degli anni precedenti é visualizzata solo se la 'data costituzione impresa' 
			//   é valorizzata ed é anteriore all'anno corrente
			 
			//MB2017_12_19 String viewSezioneDimensioniImpresa = "true";  era una ripetizione
			
			CostituzioneImpresaVo costituzioneImpresa = input._costituzioneImpresa;
			logger.debug("[DimensioniNG::inject] costituzioneImpresa="+costituzioneImpresa);
	
	        //MB2017_09_29  String mostraMsgAAEP = "N";
			
			if(costituzioneImpresa!=null){
				//MB2015_09_29 String costituzioneInCorso = costituzioneImpresa.get("costituzioneImpresa");
				String costituzioneInCorso = costituzioneImpresa.getCostituzioneInCorso(); //MB2015_09_29
				logger.debug("[DimensioniNG::inject] costituzioneInCorso="+costituzioneInCorso);			
				
				String dataCostituzioneImpresa = costituzioneImpresa.getDataCostituzioneImpresa();
				logger.debug("[DimensioniNG::inject] dataCostituzioneImpresa="+dataCostituzioneImpresa);			
				
				if (StringUtils.equals(costituzioneInCorso,"true")){
					// flag "Impresa in fase di costituzione" = true
					viewSezioneDimensioniImpresa = "false";
					logger.debug("[DimensioniNG::inject] flag 'Impresa in fase di costituzione' = true");
				} else{
					
					if(StringUtils.isNotBlank(dataCostituzioneImpresa)){
						String annoCostituzioneImpresa = dataCostituzioneImpresa.substring(dataCostituzioneImpresa.lastIndexOf('/')+1,dataCostituzioneImpresa.length());
						logger.debug("[DimensioniNG::inject] annoCostituzioneImpresa="+annoCostituzioneImpresa);
						
						int annoCostituzioneImpresaInt = Integer.parseInt(annoCostituzioneImpresa);
						logger.debug("[DimensioniNG::inject] annoCostituzioneImpresaInt="+annoCostituzioneImpresaInt);
						
						//MB2017_12_19 int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
						logger.debug("[DimensioniNG::inject] annoCorrente="+annoCorrente);
	
						//if(StringUtils.equals(annoCostituzioneImpresa, annoCorrente+"")){
						if(annoCorrente == annoCostituzioneImpresaInt){
							//azienda costituita nell'anno in corso
							viewSezioneDimensioniImpresa = "false";
							logger.debug("[DimensioniNG::inject] azienda costituita nell'anno in corso");
						}else{
							// calcolo gli anni di esercizio
							anniEsercizio = annoCorrente - annoCostituzioneImpresaInt;
						}
					}
				}
			}
			logger.debug("[DimensioniNG::inject] anniEsercizio="+anniEsercizio);
			logger.debug("[DimensioniNG::inject] viewSezioneDimensioniImpresa="+viewSezioneDimensioniImpresa);
		}	
		
		// Se bando contiene variabile di cfg: _dimensione_no_grande_tipol_beneficiario a true
		if (input._dimensione_no_grande_tipol_beneficiario.equals("true")) {
			 
			// se codice-beneficiario risulta AER: recupera lista dimensioni compresa di 'Grande'
			if(codeTipoBeneficiario.equals("AER")){
				classificazioneDimensioniImpresaList = DimensioniNGDAO.getClassificazioneDimensioniImpresaList(logger);
			}
			
			// se id_tipol_beneficiario del bando VoucherIR risulta 'SME' - recupera lista dimensioni senza 'Grande'	
			if(codeTipoBeneficiario.equals("SME")){
				classificazioneDimensioniImpresaList = DimensioniNGDAO.getClassificazioneDimensioniImpresaListSenzaGrande(logger);
			}
		} else{
			
			// se bando non richiede Dimensioni: Grande, ma solo: Micro, Piccola e Media come per Voucher, passa di qui 
			if (input._assenza_dimensioneImpresa_grande.equals("true")) {
				classificazioneDimensioniImpresaList = DimensioniNGDAO.getClassificazioneDimensioniImpresaListSenzaGrande(logger);
			} 
			else if (input._assenza_dimensioneImpresa_media.equals("true")) { //  - Jira: 1588
				classificazioneDimensioniImpresaList = DimensioniNGDAO.getClassificazioneDimensioniImpresaListSenzaMedia(logger);
			}
			else {
				// altrimenti passa di qui per bandi STD: Micro, Piccola, Media e Grande
				classificazioneDimensioniImpresaList = DimensioniNGDAO.getClassificazioneDimensioniImpresaList(logger);
			}
		}
		
		DimensioniImpresaVO _dimensioni =input._dimensioni;		
		
			if (_dimensioni != null) {
			       //variabili corrispondenti ai totali dell'ultima riga della tabella
			       BigDecimal totUominiBD = new BigDecimal("0");
			       BigDecimal totDonneBD = new BigDecimal("0");
			       BigDecimal totGeneraleBD = new BigDecimal("0");
			
		 		   // leggo risorseUmaneList da XML 
		 		   if(_dimensioni.getRisorseUmaneList()!=null){
				      risorseUmaneList = Arrays.asList(_dimensioni.getRisorseUmaneList());			
				      if(risorseUmaneList!= null && !risorseUmaneList.isEmpty()){
					     logger.debug("[DimensioniNG::inject] risorseUmaneList.size()="+risorseUmaneList.size());
					     //ciclo su ogni riga della tabella per aggiornare i totali in base ai valori presenti su xml
					     
					     int i=0;
					     for(RisorseUmaneVO row:risorseUmaneList){
						
						    String curNumUominiStr = (String) row.getNumUomini();					
						    String curNumDonneStr = (String) row.getNumDonne();	
						   
						    BigDecimal curNumUominiBD = new BigDecimal("0");	  			
						    BigDecimal curNumDonneBD = new BigDecimal("0");					    
						    					 
						    if(curNumUominiStr==null || curNumUominiStr.equals("")) {
							   curNumUominiStr="0";
						    }	
						    if(curNumDonneStr==null || curNumDonneStr.equals("")) {
							   curNumDonneStr="0";
						    }				    
						  												
							if(decimalFormatter.decimalValidate(curNumUominiStr, false, true)){		       // ammessiNegativi=false; ammessoZero=true	
						       row.setNumUomini(decimalFormatter.decimalFillZero(curNumUominiStr, 2));   // se non e' un numero decimale valido, in tabella resta il valore originale e il BigDecimal usato per i calcoli resta = 0
						       curNumUominiBD = decimalFormatter.getBigDecimalFromString(curNumUominiStr, false); // ammessiNegativi=false; usato nel calcolo del totale di riga e di colonna
						    }
						    if(decimalFormatter.decimalValidate(curNumDonneStr, false, true)){		       // ammessiNegativi=false; ammessoZero=true	
						       row.setNumDonne(decimalFormatter.decimalFillZero(curNumDonneStr, 2));    // se non e' un numero decimale valido, in tabella resta il valore originale e il BigDecimal usato per i calcoli resta = 0
						       curNumDonneBD = decimalFormatter.getBigDecimalFromString(curNumDonneStr, false); // ammessiNegativi=false; usato nel calcolo del totale di riga e di colonna
						    }
						     
						    // calcolo i totali
						    if(i==risorseUmaneList.size()-1){
						       //ultima iterazione; scrivo in tabella i totali complessivi
						      
						      String totUominiStr = decimalFormatter.getStringFromBigDecimal(totUominiBD, 2);
						      row.setNumUomini(totUominiStr);
						      
						      String totDonneStr = decimalFormatter.getStringFromBigDecimal(totDonneBD, 2);
						      row.setNumDonne(totDonneStr);
						   
						      totGeneraleBD = totUominiBD.add(totDonneBD);					      
						      String totGeneraleStr = decimalFormatter.getStringFromBigDecimal(totGeneraleBD, 2);
						      row.setTotale(totGeneraleStr);
						   
						    }else{
						       //per i totali generali
	                           totUominiBD=totUominiBD.add(curNumUominiBD);					     
						       totDonneBD=totDonneBD.add(curNumDonneBD);
						       
						       //per la riga corrente
						       BigDecimal totRigaBD = curNumUominiBD.add(curNumDonneBD);	
						       String totRigaStr = decimalFormatter.getStringFromBigDecimal(totRigaBD, 2);					      
						       //row.setNumDonne(totRigaStr);
						       row.setTotale(totRigaStr);
						    }	
						    
						    i++;
						    
					     } //chiude il for
					  } //chiude test non empty su ulaAttivateList				
				   }
			} //chiude test null su _dimensioni
		
		
	  	int flagPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);
	  	output.setFlagPubblicoPrivato(flagPubblicoPrivato);
	  	
	    if (input._ente_impresa_dimensioni.equals("true")) {	
	    
	      if (flagPubblicoPrivato==1){
	    
			// popolo la lista datiImpresaList solo se l'impresa non e' in fase di costituzione
			if(viewSezioneDimensioniImpresa!=null && viewSezioneDimensioniImpresa.equals("true")){  //MB2015_09_29 trasformato test di un booleano in test di una stringa
	
				// solo se non ho mai salvato la pagina devo caricare la lista datiImpresaList manualmente
				boolean loadFirstTimeAnniEsercizio = true;
				
			    if(_dimensioni != null) {
					datiImpresaList = Arrays.asList(_dimensioni.getDatiImpresaList());
					if(datiImpresaList!=null && !datiImpresaList.isEmpty()){
						logger.debug("[DimensioniNG::inject] datiImpresaList.size()="+datiImpresaList.size());
						loadFirstTimeAnniEsercizio = false;
					}
				}
				
				// se anniEsercizio = 1 --> allora una riga = anno -1
				// se anniEsercizio = 2 --> allora due righe = anno -1 e anno -2
				// se anniEsercizio >= 3 --> allora tre righe = anno -1 , anno -2, anno -3
				if(loadFirstTimeAnniEsercizio){
					logger.debug("[DimensioniNG::inject] carico la lista datiImpresaList manualmente");
					datiImpresaList = new ArrayList<DatiImpresaVO>();
					
					logger.debug("[DimensioniNG::inject] annoCorrente="+annoCorrente);					
					
					if(anniEsercizio>=1){
						logger.debug("[DimensioniNG::inject] anniEsercizio>=1");
						DatiImpresaVO annoMapMenoUno = new DatiImpresaVO();
						annoMapMenoUno.setUla("");			 			
						annoMapMenoUno.setFatturato("");			 	
						annoMapMenoUno.setTotBilancio(""); 				
						annoMapMenoUno.setAnno((annoCorrente-1)+"");
						datiImpresaList.add(annoMapMenoUno); 			
					}
				  
					if(anniEsercizio>=2){
						logger.debug("[DimensioniNG::inject] anniEsercizio>=2");
						DatiImpresaVO annoMapMenoDue = new DatiImpresaVO();
						annoMapMenoDue.setUla("");	
						annoMapMenoDue.setFatturato("");	
						annoMapMenoDue.setTotBilancio("");						
						annoMapMenoDue.setAnno((annoCorrente-2)+""); 
						datiImpresaList.add(annoMapMenoDue); 
					}
					
					if(anniEsercizio>=3){
						logger.debug("[DimensioniNG::inject] anniEsercizio>=3");
						DatiImpresaVO annoMapMenoTre = new DatiImpresaVO();
						annoMapMenoTre.setUla("");	
						annoMapMenoTre.setFatturato("");	
						annoMapMenoTre.setTotBilancio("");	
						annoMapMenoTre.setAnno((annoCorrente-3)+"");
						datiImpresaList.add(annoMapMenoTre); 
					}				

				}
				//MB2015_09_25 ini aggiungo, se non presenti, a fatturato e totBilancio i decimali (concateno la stringa ",00")
				else{
				   if(datiImpresaList!=null && !datiImpresaList.isEmpty()){
				     //aggiungo un eventuale ,00 a fatturato e totBilancio se non presente
				     for(int j=0; j<datiImpresaList.size();j++){
				    	 DatiImpresaVO row = (DatiImpresaVO) datiImpresaList.get(j);
				         
				         String ula = (String) row.getUla();
						 row.setUla(decimalFormatter.decimalFormat(ula,2));
						 
						 String fatturato = (String) row.getFatturato();
						 row.setFatturato(decimalFormatter.decimalFormat(fatturato,2));
						 
						 String totBilancio = (String) row.getTotBilancio();
						 row.setTotBilancio(decimalFormatter.decimalFormat(totBilancio,2));
						 
				     }//chiude il for su datiImpresaList
				   
				   } //chiude test null su datiImpresaList
				
				}
				//MB2015_09_25 fine
			}
		  }
	    }

		if(risorseUmaneList==null || risorseUmaneList.isEmpty()){
			
			risorseUmaneList = new ArrayList<RisorseUmaneVO>();
			
			//carichiamo le risorse umane dalla vista
			Integer idBando = info.getStatusInfo().getTemplateId();
			Integer idDomanda = info.getStatusInfo().getNumProposta();
			
			List<DatiUlaVO> listUla = DimensioniNGDAO.getUlaByIdBandoIdTipolBeneficiario(idBando, idDomanda);
			if (listUla!=null && listUla.size()!=0){
				
				for(DatiUlaVO ula:listUla){
					RisorseUmaneVO mappa = new RisorseUmaneVO();
					mappa.setCategoria(ula.getDescrizione());
					mappa.setNumUomini("0,00");	
					mappa.setNumDonne("0,00");
					mappa.setTotale("0,00");		
					risorseUmaneList.add(mappa);
				}
				// aggiungiamo la riga dei totali
				RisorseUmaneVO m6 = new RisorseUmaneVO();
				m6.setCategoria("Totale");	
				m6.setNumUomini("0,00");	
				m6.setNumDonne("0,00");
				m6.setTotale("0,00");		
				risorseUmaneList.add(m6);	
				
			} else {
				
				// le risorse umane sono composte da queste 5 categorie 
				// li metto qui per scrivere meno HTML
				//RISORSE UMANE MOCK
				RisorseUmaneVO m1 = new RisorseUmaneVO();
				m1.setCategoria("Dirigenti");	
				m1.setNumUomini("0,00");	
				m1.setNumDonne("0,00");
				m1.setTotale("0,00");		
				risorseUmaneList.add(m1);
			
				RisorseUmaneVO m2 = new RisorseUmaneVO();
				m2.setCategoria("Quadri");	
				m2.setNumUomini("0,00");	
				m2.setNumDonne("0,00");
				m2.setTotale("0,00");		
				risorseUmaneList.add(m2);
			
				RisorseUmaneVO m3 = new RisorseUmaneVO();
				m3.setCategoria("Impiegati");	
				m3.setNumUomini("0,00");	
				m3.setNumDonne("0,00");
				m3.setTotale("0,00");		
				risorseUmaneList.add(m3);
			
				RisorseUmaneVO m4 = new RisorseUmaneVO();
				m4.setCategoria("Operai");
				m4.setNumUomini("0,00");	
				m4.setNumDonne("0,00");
				m4.setTotale("0,00");		
				risorseUmaneList.add(m4);
			
				RisorseUmaneVO m5 = new RisorseUmaneVO();
				m5.setCategoria("Personale non strutturato");	
				m5.setNumUomini("0,00");	
				m5.setNumDonne("0,00");
				m5.setTotale("0,00");		
				risorseUmaneList.add(m5);
			
				RisorseUmaneVO m6 = new RisorseUmaneVO();
				m6.setCategoria("Totale");	
				m6.setNumUomini("0,00");	
				m6.setNumDonne("0,00");
				m6.setTotale("0,00");		
				risorseUmaneList.add(m6);
			}
			
			logger.debug("[DimensioniNG::inject] risorseUmaneList popolata per la prima volta");
		}

		if (input._ente_impresa_dimensioni.equals("true")) {	
			output.setViewSezioneDimensioniImpresa(viewSezioneDimensioniImpresa);
		}
	    
		output.setRisorseUmaneList(risorseUmaneList);
		
		if (input._ente_impresa_dimensioni.equals("true")) {
			if (flagPubblicoPrivato==1){
		if(viewSezioneDimensioniImpresa!=null && viewSezioneDimensioniImpresa.equals("true")){  //MB2015_09_29 trasformato test di un booleano in test di una stringa		
			output.setDatiImpresaList(datiImpresaList);
			
			// AA - inizio
			String operazione = "INS";
			Integer idDomanda = info.getStatusInfo().getNumProposta();
			
			String nododimensioni = DimensioniNGDAO.getNodoDimensioni(idDomanda);
			if (nododimensioni!=null){
				operazione = "MOD";					
			}
			
			logger.debug("[DimensioniNG::inject] operazione=" + operazione);
			
			if (operazione.equalsIgnoreCase("INS")){	
			
				// AA - sposto la parte di AAEP in questo punto 
				// in modo tale che venga eseguita quando siamo in inserimento e 
				// in modo da avere ancora disponibili i dati in caso di validazione fallita
				//////////////////AAEP
				logger.debug("[DimensioniNG::inject] datiImpresaList="+datiImpresaList);
				if(enteImpresa!=null){
					logger.info("[DimensioniNG::inject] enteImpresa idAzienda="+enteImpresa.getIdAzienda());
					// enteImpresa in sessione arriva da AAEP
				}else{
					logger.info("[DimensioniNG::inject] enteImpresa non in sessione");
					enteImpresa = new ImpresaVO();
				}

				if( enteImpresa.getIdAzienda() != null ){
					String annoAttuale = "";
					String annoPrecedente = "";
					String fatturatoAttuale = "";
					String fatturatePrecedente = "";
					String totBilancioAttuale = "";
					String totBilancioPrecedente = "";
					
					BilancioAziendaAAEPVO bilancioAAEP = null;
					
					try {
						bilancioAAEP = (BilancioAziendaAAEPVO) AaepWs.getInstance().getBilancioAziendaFromAAEP(enteImpresa.getIdAzienda(), logger);
						mostraMsgAAEP = "S";
					}catch(Exception e){
						logger.error("Exception nel richiamo di getBilancioAzienda per azienda " + enteImpresa.getIdAzienda());
						logger.error(e.getMessage());
					}

					if(bilancioAAEP!=null && bilancioAAEP.getDettaglioBilancio() != null){

						for(DettaglioBilancioAziendaAAEPVO rig:bilancioAAEP.getDettaglioBilancio()){
							// estraggo dalla lista (voce="Conto economico") gli anni dei bilanci
							if(StringUtils.equals(rig.getVoce(), "Conto economico")){
								logger.debug("[DimensioniNG::inject] rig 11="+rig);
								// {voce=Conto economico, tipoRigo=1, livelloIndentazione=0, annoAttuale=2013}
								//  ricprendo il valore del nodo "annoAttuale" e/o "annoPrecedente"
								annoAttuale = rig.getAnnoAttuale();
								annoPrecedente = rig.getAnnoPrecedente();
								break;
							}
						}
						logger.debug("[DimensioniNG::inject] annoAttuale="+annoAttuale);
						logger.debug("[DimensioniNG::inject] annoPrecedente="+annoPrecedente);
						
						logger.debug("[DimensioniNG::inject] ++++++++++1++++++++++++");
						
						for(DettaglioBilancioAziendaAAEPVO rig:bilancioAAEP.getDettaglioBilancio()){
							// estraggo dalla lista (voce="A.1. ricavi delle vendite e delle prestazioni") i valori dei fatturati
							if(StringUtils.contains(rig.getVoce(), RICAVI)){
								logger.debug("[DimensioniNG::inject] rig 22="+rig);

								// lego il fatturato all'anno
								if(StringUtils.isNotBlank(rig.getAnnoAttuale())){
									logger.debug("[DimensioniNG::inject]  sono in annoAttuale");
									for(DatiImpresaVO dirig:datiImpresaList){
										if(StringUtils.equals(annoAttuale, dirig.getAnno())){
											logger.debug("[DimensioniNG::inject]  trovato datiImpresaList:"+dirig);
											fatturatoAttuale = rig.getAnnoAttuale();
											dirig.setFatturato(decimalFormatter.decimalFormat(fatturatoAttuale.replace(".",""),2));
											break;
										}
									}
								}
								if(StringUtils.isNotBlank(rig.getAnnoPrecedente())){
									logger.debug("[DimensioniNG::inject]  sono in annoPrecedente");
									for(DatiImpresaVO dirig:datiImpresaList){

										if(StringUtils.equals(annoPrecedente, dirig.getAnno())){
											logger.debug("[DimensioniNG::inject]  trovato datiImpresaList:"+dirig);
											fatturatePrecedente = rig.getAnnoPrecedente();
											dirig.setFatturato(decimalFormatter.decimalFormat(fatturatePrecedente.replace(".",""),2));
											break;
										}
									}
								}								
							}
							if(StringUtils.contains(rig.getVoce(), TOTBILANCIO)){
								logger.debug("[DimensioniNG::inject] riga bilancio ="+rig);

								// leggo il totale di bilancio all'anno
								if(StringUtils.isNotBlank(rig.getAnnoAttuale())){
									logger.debug("[DimensioniNG::inject]  sono in annoAttuale");
									for(DatiImpresaVO dirig:datiImpresaList){
										if(StringUtils.equals(annoAttuale, dirig.getAnno())){
											logger.debug("[DimensioniNG::inject]  trovato datiImpresaList:"+dirig);
											totBilancioAttuale = rig.getAnnoAttuale();
											dirig.setTotBilancio(decimalFormatter.decimalFormat(totBilancioAttuale.replace(".",""),2));
											break;
										}
									}
								}
								if(StringUtils.isNotBlank(rig.getAnnoPrecedente())){
									logger.debug("[DimensioniNG::inject]  sono in annoPrecedente");
									for(DatiImpresaVO dirig:datiImpresaList){

										if(StringUtils.equals(annoPrecedente, dirig.getAnno())){
											logger.debug("[DimensioniNG::inject]  trovato datiImpresaList:"+dirig);
											totBilancioPrecedente = rig.getAnnoPrecedente();
											dirig.setTotBilancio(decimalFormatter.decimalFormat(totBilancioPrecedente.replace(".",""),2));
											break;
										}
									}
								}
							}
						}
						//logger.debug("[DimensioniNG::inject]  posy datiImpresaList:"+datiImpresaList);
						logger.debug("[DimensioniNG::inject] ++++++++++2++++++++++++");
						
						// popolo una lista per salvare i dati provenienti da AAEP sull'XML
						if(StringUtils.isNotBlank(annoAttuale)){
							datiImpresaListAAEP = new ArrayList<DatiImpresaVO>();
							DatiImpresaVO annoAttMap = new DatiImpresaVO();
							annoAttMap.setAnno(annoAttuale);
							//annoAttMap.put("fatturato",  fatturatoAttuale);
							// AA - formattazione del campo fatturato (da AAEP arriva con il separatore delle migliaia e senza decimali)
							annoAttMap.setFatturato(decimalFormatter.decimalFormat(fatturatoAttuale.replace(".",""),2));
							annoAttMap.setTotBilancio(decimalFormatter.decimalFormat(totBilancioAttuale.replace(".",""),2));
							datiImpresaListAAEP.add(annoAttMap);
							
							if(StringUtils.isNotBlank(annoPrecedente)){
								DatiImpresaVO annoPrecMap = new DatiImpresaVO();
								annoPrecMap.setAnno(annoPrecedente);
								// AA - formattazione del campo fatturato (da AAEP arriva con il separatore delle migliaia e senza decimali)
								//annoPrecMap.put("fatturate",  fatturatePrecedente);
								annoPrecMap.setFatturato(decimalFormatter.decimalFormat(fatturatePrecedente.replace(".",""),2));
								annoPrecMap.setTotBilancio(decimalFormatter.decimalFormat(totBilancioPrecedente.replace(".",""),2));
								datiImpresaListAAEP.add(annoPrecMap);
							}
						}
					}
				}
				//////////////////AAEP	
				
				output.setDatiImpresaListAAEP(datiImpresaListAAEP);
			}
			// AA - fine
			}
		}
		output.setEnteImpresa(enteImpresa);
		//MB2017_09_29 ns.put("mostraMsgAAEP", mostraMsgAAEP);
		}
		//if(caricaClassifDimensioniImpresa == void) {
		output.setClassificazioneDimensioniImpresaList(classificazioneDimensioniImpresaList);
		//}
		output.setMostraMsgAAEP(mostraMsgAAEP);  //MB2017_09_29 metto sempre nel ns la variabile per evitare che nell'html sia usata senza essere definita


		logger.info("[DimensioniNG::inject] DimensioniNG S2_P4 END");
		return output;
	}


	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1)
			throws CommonalityException {
		FinCommonInfo info = (FinCommonInfo) info1;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());

		logger.info("[DimensioniNG::modelValidate]  BEGIN");

		DecimalFormat formatter = new DecimalFormat();
		
		String ERRMSG_CAMPONULL = "- dato mancante, inserire un valore";
		// String ERRMSG_CAMPONOTNUMBER = "- dato errato, inserire un valore numerico intero positivo";		
		// String ERRMSG_TOTALE = "- dato mancante, il totale deve essere un valore numerico intero positivo";
		String ERRMSG_ALMENO_UN_VALORE = "- inserire almeno un elemento nella tabella 'Risorse umane' ";
		// String ERRMSG_FORMATO_IMPORTO = "- il valore deve essere numerico con al massimo 2 decimali"; 
		String ERRMSG_FORMATO_DECIMALE_POSITIVO = "- il valore deve essere numerico positivo con al massimo 2 decimali"; 
		String ERRMSG_FORMATO_DECIMALE = "- il valore deve essere numerico con al massimo 2 decimali";   


		DimensioniImpresaVO _dimensioni = input._dimensioni;
		if (_dimensioni != null ) 
		{
			if (input._dimensioni_risorse_umane.equals("true")) 
			{
				List<RisorseUmaneVO> risorseUmaneList = Arrays.asList(_dimensioni.getRisorseUmaneList());
				if (risorseUmaneList != null && !risorseUmaneList.isEmpty()) 
				{
					boolean almenoUnValoreSignificativo = false;
					
					for (int i = 0; i < risorseUmaneList.size();  i++) 
					{			
						RisorseUmaneVO risorseUmaneMap = (RisorseUmaneVO) risorseUmaneList.get(i);
						
						if (risorseUmaneMap != null && !risorseUmaneMap.isEmpty()) 
						{						
							String numUomini = risorseUmaneMap.getNumUomini();
							
							if (!StringUtils.isBlank(numUomini)) 
							{
								if(!formatter.decimalValidate(numUomini, false, true))
								{
									addMessage(newMessages,"_dimensioni_risorseUmaneList_numUomini", ERRMSG_FORMATO_DECIMALE_POSITIVO);
									addMessage(newMessages,"_dimensioni_risorseUmaneList_numUomini_riga", i+"U");
									logger.warn("[DimensioniNG::modelValidate] errore numUomini; valore non valido alla riga " + i);
									
								} else if ((!numUomini.equals("0")) && (!numUomini.equals("0,00")) && (i != risorseUmaneList.size()-1)){ //non considero la riga dei totali
									almenoUnValoreSignificativo = true;
								}
							}
							
							String numDonne = risorseUmaneMap.getNumDonne();
							
							if (!StringUtils.isBlank(numDonne)) 
							{
								if(!formatter.decimalValidate(numDonne, false, true))
								{	
									addMessage(newMessages,"_dimensioni_risorseUmaneList_numDonne", ERRMSG_FORMATO_DECIMALE_POSITIVO);
									addMessage(newMessages,"_dimensioni_risorseUmaneList_numDonne_riga", i+"D");
									logger.warn("[DimensioniNG::modelValidate] errore numDonne; valore non valido alla riga " + i);
								
								} else if ((!numDonne.equals("0")) && (!numDonne.equals("0,00")) && (i != risorseUmaneList.size()-1)){  //non considero la riga dei totali
									almenoUnValoreSignificativo = new Boolean(true);
								}
							}
						}
					}				
					
					if(!almenoUnValoreSignificativo)
					{
						addMessage(newMessages,"_dimensioni", ERRMSG_ALMENO_UN_VALORE);
					}
				}
				
			}else{
				logger.info("Nessun dato da risorse ... blocchetto non richiesto!");
			} // fine variabile-configurazione
			
			int flagPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);  
			if (flagPubblicoPrivato==1){
				
				if (input._dimensioni_dim_impresa.equals("false")){
					// versione std
					String  idDimensioneImpresa = (String) _dimensioni.getIdDimensioneImpresa();
					if (StringUtils.isBlank(idDimensioneImpresa) || "-1".equals(idDimensioneImpresa)) {
						addMessage(newMessages,"_dimensioni_dimImpresa", ERRMSG_CAMPONULL);
						logger.warn("[DimensioniNG::modelValidate] errore idDimensioneImpresa NULL ");
					}
				}
			}
						
			if (input._ente_impresa_dimensioni.equals("true")){
					
				// verifico il formato dei valori nella sezione "Dimensione d'Impresa" 
				List<DatiImpresaVO> datiImpresaList = _dimensioni.getDatiImpresaList()!=null?Arrays.asList(_dimensioni.getDatiImpresaList()):null;
				
				
				if (datiImpresaList != null && !datiImpresaList.isEmpty()) {
					for (int i = 0, n = datiImpresaList.size(); i < n; i++) {
						DatiImpresaVO datiImpresaMap = (DatiImpresaVO) datiImpresaList.get(i);
	
						String ula = datiImpresaMap.getUla();	
						if(StringUtils.isBlank(ula)){
							addMessage(newMessages,"_dimensioni_datiImpresaList_ula", ERRMSG_CAMPONULL);
							addMessage(newMessages,"_dimensioni_datiImpresaList_ula_riga", i+"U");
							logger.warn("[DimensioniNG::modelValidate] errore ula obbligatoria riga " + i);
						}else if(StringUtils.isNotBlank(ula) && (!ula.matches("^\\d+(,\\d{1,2})?$"))){	//accetta numeri positivi con fino a 2 decimali  					
							addMessage(newMessages,"_dimensioni_datiImpresaList_ula", ERRMSG_FORMATO_DECIMALE_POSITIVO);
							addMessage(newMessages,"_dimensioni_datiImpresaList_ula_riga", i+"U");
							logger.warn("[DimensioniNG::modelValidate] errore ula NOT NUMBER riga " + i);
						}
						
						String fatturato = datiImpresaMap.getFatturato();					
						if(StringUtils.isBlank(fatturato)){
							addMessage(newMessages,"_dimensioni_datiImpresaList_fatturato", ERRMSG_CAMPONULL);
							addMessage(newMessages,"_dimensioni_datiImpresaList_fatturato_riga", i+"F");
							logger.warn("[DimensioniNG::modelValidate] errore fatturato obbligatorio riga " + i);
						}else if(!fatturato.matches("^\\d+(,\\d{1,2})?$")){  //accetta numeri positivi con fino a 2 decimali 
							addMessage(newMessages,"_dimensioni_datiImpresaList_fatturato", ERRMSG_FORMATO_DECIMALE_POSITIVO);
							addMessage(newMessages,"_dimensioni_datiImpresaList_fatturato_riga", i+"F");
							logger.warn("[DimensioniNG::modelValidate] errore fatturato NOT NUMBER riga " + i);
						}
						
						String totBilancio = datiImpresaMap.getTotBilancio();
						if(StringUtils.isBlank(totBilancio)){
							addMessage(newMessages,"_dimensioni_datiImpresaList_totBilancio", ERRMSG_CAMPONULL);
							addMessage(newMessages,"_dimensioni_datiImpresaList_totBilancio_riga", i+"T");
							logger.warn("[DimensioniNG::modelValidate] errore totBilancio obbligatorio riga " + i);
						}else if(!totBilancio.matches("^-?\\d+(,\\d{1,2})?$")){  //accetta numeri positivi o negativi con fino a 2 decimali 
							addMessage(newMessages,"_dimensioni_datiImpresaList_totBilancio", ERRMSG_FORMATO_DECIMALE);
							addMessage(newMessages,"_dimensioni_datiImpresaList_totBilancio_riga", i+"T");
							logger.warn("[DimensioniNG::modelValidate] errore totBilancio NOT NUMBER riga " + i);
						}
					}
				}
			}
		}

		logger.info("[DimensioniNG::modelValidate] S2_P4 END");
		return newMessages;
	}
}
