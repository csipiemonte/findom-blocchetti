/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.schedaProgetto;

import it.csi.findom.blocchetti.common.dao.CriterioDAO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.schedaProgetto.VistaCriterioVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class SchedaProgetto extends Commonality {

	  SchedaProgettoInput input = new SchedaProgettoInput();

	  @Override
	  public SchedaProgettoInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[SchedaProgetto::inject] BEGIN");

	    long beginTime = java.lang.System.currentTimeMillis() ;
		
    	SchedaProgettoOutput output = new SchedaProgettoOutput();
    	String numDomandaCorrente = info.getStatusInfo().getNumProposta()+"";
    	
    	logger.info("[SchedaProgetto::inject] numDomandaCorrente="+numDomandaCorrente);
    	logger.info("[SchedaProgetto::inject] templateId="+info.getStatusInfo().getTemplateId());
    	logger.info("[SchedaProgetto::inject] input.criteriToView="+input.criteriToView);
    	
    	logger.info("[SchedaProgetto::inject] input.schedaProgettoDipendeDaCaratteristicheProgetto="+input.schedaProgettoDipendeDaCaratteristicheProgetto);
    	    	
    	verificaBontaParametriConfigurazionePagina(logger);

    	Integer idBando = info.getStatusInfo().getTemplateId();
    	
    	/** recupero la descrizione della tipologia intervento by criterio */
    	String descrizioneTipolIntervento = "";
    	
    	
    	// abilito funzionamenti bottoni "Cancella" , "Salva", "Rirpristina" nella pagina
    	output.bottoniAbilitati = "true";
    	
    	// TreeMap < id_criterio, abilitato >, contiene i criteri della pagina corrente e il loro stato
		TreeMap<Integer, Boolean> mappaCriteriAbilitati = new TreeMap<Integer, Boolean>();
		
		// se c'e' dipendenza con le Caratteristiche di Progetto...
    	if("true".equals(input.schedaProgettoDipendeDaCaratteristicheProgetto)){
    		// visualizzo un criterio solo se la corrispettiva tipologia di intervento e' selezionata
    		
    		// TreeMap < id_criterio, id_tipol_intervento >
    		List<TreeMap<Integer, Integer>> listaMappaCriteriTipologieIntervento = CriterioDAO.getCriteriTipologieIntervento(idBando,input.criteriToView, logger);
    		
    		descrizioneTipolIntervento = CriterioDAO.getDescrizineTipologieIntervento(idBando, input.criteriToView, logger);
    		logger.info("[SchedaProgetto::inject] descrizioneTipolIntervento= "+descrizioneTipolIntervento);
    		
    		if(listaMappaCriteriTipologieIntervento!=null && !listaMappaCriteriTipologieIntervento.isEmpty()){
    			logger.info("[SchedaProgetto::inject] listaMappaCriteriTipologieIntervento.size="+listaMappaCriteriTipologieIntervento.size());
    			
    			// popolo la mappaCriteriAbilitati
    			for (TreeMap<Integer, Integer> item : listaMappaCriteriTipologieIntervento) {
    				
    				for (Integer idCrit : item.keySet()) {
    					
    					logger.info("[SchedaProgetto::inject] listaMappaCriteriTipologieIntervento chiave="+idCrit+", valore="+item.get(idCrit));
    					mappaCriteriAbilitati.put(idCrit, isTipologiaInterventoSelezionata(item.get(idCrit),input.caratteristicheProgettoNG, logger));	
    				}
    			}
    			
    		}else{
    			logger.info("[SchedaProgetto::inject] listaMappaCriteriTipologieIntervento NULL or empty");
    		}
    		
    		// definisco se far vedere nella pagina i bottoni "Cancella" , "Salva", "Rirpristina"
        	output.bottoniAbilitati = bottoniAbilitati(mappaCriteriAbilitati, input.criteriToView, logger);
    	}
    	
    	
    	
    	// mappaOridineCriteri <idCriterio , ordineCriterio>
    	TreeMap<Integer, Integer> mappaOridineCriteri = ricalcolaOrdinamentoCriteri(CriterioDAO.getOrdineCriteri(idBando, logger), logger);
    	
//    	if(mappaOridineCriteri!=null){
//    		logger.info("[SchedaProgetto::inject] mappaOridineCriteri size="+mappaOridineCriteri.size());
//    		for (Integer id1 : mappaOridineCriteri.keySet()) {
//    			System.out.println(" in = "+mappaOridineCriteri.toString());
//			}
//    	}else{
//    		logger.info("[SchedaProgetto::inject] mappaOridineCriteri NULL");
//    	}
   
    	List<CriterioVO> listaCriteri = new ArrayList<CriterioVO>();
    	
    	logger.info("[SchedaProgetto::inject] input.schedaProgettoVO ="+input.schedaProgettoVO);
    	
    	// criteri nell'XML
    	//CriterioVO[] crtInXML = input.schedaProgettoVO.getCriteriList();
    	CriterioVO[] crtInXML = null;
    	if(input.schedaProgettoVO!=null)
    		crtInXML = input.schedaProgettoVO.getCriteriList();
    	
    	logger.info("[SchedaProgetto::inject] crtInXML ="+crtInXML);
    	
    	String[] criteriToView = (input.criteriToView.replace(" ", "")).split(",");
    	
    	// ciclo sui criteri che devo visualizzare
    	// per ognuno controllo se e' presente nell'XML, se non lo e' lo carico da DB
    	for (String crtToV : criteriToView) {
			logger.info("[SchedaProgetto::inject] -------------------- crtToV="+crtToV);
			
			boolean criterioInXML = false;
			if (crtInXML!=null){
				for (CriterioVO crtToV2 : crtInXML) {
					//logger.info("[SchedaProgetto::inject] crtToV2.getIdCriterio() ="+crtToV2.getIdCriterio());
					
					if(StringUtils.equals(crtToV, crtToV2.getIdCriterio()+"")){
						logger.info("[SchedaProgetto::inject] trovato crtToV="+crtToV+", crtToV2.getIdCriterio() ="+crtToV2.getIdCriterio());
						criterioInXML = true;
						
						Integer nuovaPosiz = mappaOridineCriteri.get(crtToV2.getIdCriterio());
						crtToV2.setPosizioneCriterio(nuovaPosiz);
						
						crtToV2.setAbilitato("true");
						if("true".equals(input.schedaProgettoDipendeDaCaratteristicheProgetto)){
							logger.info("[SchedaProgetto::inject] crtToV2 mappaCriteriAbilitati="+mappaCriteriAbilitati.toString());
	    					if (!mappaCriteriAbilitati.isEmpty() && mappaCriteriAbilitati.containsKey(crtToV2.getIdCriterio()) && !mappaCriteriAbilitati.get(crtToV2.getIdCriterio())){
	    						crtToV2.setAbilitato("false");
	    						logger.info("[SchedaProgetto::inject] crtToV2 mappaCriteriAbilitati[getIdCriterio="+crtToV2.getIdCriterio() + ", abilitato="+mappaCriteriAbilitati.get(crtToV2.getIdCriterio())+"]");
	    					}
						}
						// trovato criterio nell'XML, lo aggiungo alla listaCriteri
						listaCriteri.add(trasformaArrToList(crtToV2,logger));
						break;
					}
				}
			}
			if(!criterioInXML){
				// criterio non presente nell'XML, lo prendo dal DB
				logger.info("[SchedaProgetto::inject] criterio "+crtToV+" non presente nell'XML, lo prendo dal DB");
				
		    	long begin=System.currentTimeMillis();
		    	
		    	List<VistaCriterioVO> crList = CriterioDAO.getCriteriList(idBando, crtToV, logger);
		    	long end = System.currentTimeMillis() - begin;
		    	logger.info("[SchedaProgetto::inject]tempo caricamento criterio = "+ end + " ms");
		    	
		    	if(crList!=null){
		    		logger.info("[SchedaProgetto::inject] trovato una lista di criteri di dimensione ="+crList.size());

		    		long begin2 = System.currentTimeMillis();
		    		List<CriterioVO> listaCriteriDB = popolaListaCriteri(crList, mappaOridineCriteri, logger);
		    		long end2 = System.currentTimeMillis() - begin2;
			    	logger.info("[SchedaProgetto::inject]tempo elaborazione criterio = "+ end2 + " ms");
			    	
			    	if(listaCriteriDB!=null && !listaCriteriDB.isEmpty()) 
				    {
			    		for (CriterioVO itm : listaCriteriDB) {
				    		itm.setAbilitato("true");
							if("true".equals(input.schedaProgettoDipendeDaCaratteristicheProgetto)){
								logger.info("[SchedaProgetto::inject] itm mappaCriteriAbilitati="+mappaCriteriAbilitati.toString());
		    					if (!mappaCriteriAbilitati.isEmpty() && mappaCriteriAbilitati.containsKey(itm.getIdCriterio()) && !mappaCriteriAbilitati.get(itm.getIdCriterio())){
		    						itm.setAbilitato("false");
		    						logger.info("[SchedaProgetto::inject] itm mappaCriteriAbilitati[getIdCriterio="+itm.getIdCriterio() + ", abilitato="+mappaCriteriAbilitati.get(itm.getIdCriterio())+"]");
		    					}
							}
							listaCriteri.add(itm);
						}
				    }
			    	
		    	}else{
		    		logger.info("[SchedaProgetto::inject] crList NULLA");
		    	}
			}
		}
    	
    	if(listaCriteri!=null){
    		logger.info("[SchedaProgetto::inject] trovato una listaCriteri di dimensione ="+listaCriteri.size());
    	}
    	
    	output.descrizioneTipolIntervento=descrizioneTipolIntervento;
    	output.listaCriteri = listaCriteri;
    	output.numeroCriteriTotali = CriterioDAO.getNumeroCriteri(idBando, logger);
    	
    	long totalTime = java.lang.System.currentTimeMillis() - beginTime;
		logger.info("[SchedaProgetto::inject] tempo di elaborazione = [" +totalTime +"] ms");
		
    	logger.info("[SchedaProgetto::inject] END");
    	return output;
	  }


	/**
	 * Controllo lo stato di abilitazione di ogni criterio da visualizzare
	 * @param mappaCriteriAbilitati
	 * @param criteriToView
	 * @param logger
	 * @return "true" se almeno un criterio nella pagina e' abilitato
	 */
	private String bottoniAbilitati(TreeMap<Integer, Boolean> mappaCriteriAbilitati, String criteriToView, Logger logger) {
		logger.info("[SchedaProgetto::bottoniAbilitati] BEGIN,");
		
		String res = "false";
		
		if(!mappaCriteriAbilitati.isEmpty()){
			for (Integer idCrit : mappaCriteriAbilitati.keySet()) {
				logger.info("[SchedaProgetto::bottoniAbilitati] criterio "+idCrit+", abilitato="+mappaCriteriAbilitati.get(idCrit));
				if(mappaCriteriAbilitati.get(idCrit)){
					res = "true";
					break;
				}
			}
		}else{
			logger.info("[SchedaProgetto::bottoniAbilitati] mappaCriteriAbilitati empty");
			res = "true";
		}
		
		logger.info("[SchedaProgetto::bottoniAbilitati] END, res="+res);
		return res;
	}

	/**
	 * Verifico il valore "checked" di ogni tipologia di intervento salvata nell'XML
	 * 
	 * @param idTipologiaInterv
	 * @param carattPrj 
	 * @param logger
	 * @return True se la tipologia intervento collegata e' selezionata
	 */
	private Boolean isTipologiaInterventoSelezionata(Integer idTipologiaInterv, CaratteristicheProgettoNGVO carattPrj, Logger logger) {
		logger.info("[SchedaProgetto::isTipologiaInterventoSelezionata] BEGIN, idTipologiaInterv="+idTipologiaInterv);
		
		Boolean res = false;
		if(carattPrj!=null){
			TipologiaInterventoVO[] arrayTipInt = carattPrj.getTipologiaInterventoList();
			for (int i = 0; i < arrayTipInt.length; i++) {
				TipologiaInterventoVO tipolInterv = arrayTipInt[i];
				logger.info("[SchedaProgetto::isTipologiaInterventoSelezionata] IdTipoIntervento="+tipolInterv.getIdTipoIntervento()+
						", Checked="+tipolInterv.getChecked());
				if(tipolInterv.getIdTipoIntervento().equals(idTipologiaInterv+"") && tipolInterv.getChecked().equals("true")){
					res = true;
					break;
				}
			}
		}else{
			logger.info("[SchedaProgetto::isTipologiaInterventoSelezionata] carattPrj NULL");
		}
		logger.info("[SchedaProgetto::isTipologiaInterventoSelezionata] END, res="+res);
		return res;
	}

	/**
	 * L'ordine dei criteri restituito dalla vista contiene dei buchi
	 * Riordino l'ordine elimenando i buchi e partendo col numero 0
	 * @param ordineCriteri
	 * @param logger
	 * @return
	 */
	private TreeMap<Integer, Integer> ricalcolaOrdinamentoCriteri( List<TreeMap<Integer, Integer>> listaOC, Logger logger) {
		logger.info("[SchedaProgetto::ricalcolaOrdinamentoCriteri] BEGIN");
		
		TreeMap<Integer, Integer> mappa = null;
		
		if(listaOC!=null){
			logger.info("[SchedaProgetto::ricalcolaOrdinamentoCriteri] listaOC size="+listaOC.size());
			mappa = new TreeMap<Integer, Integer>();
    		int i = 0;
    		for (TreeMap<Integer, Integer> itemMap : listaOC) {
    			for (Integer id1 : itemMap.keySet()) {
					//e' un ciclo di 1
    				mappa.put(id1, i);
				}
    			i++;
			}
    		
    	}else{
    		logger.info("[SchedaProgetto::ricalcolaOrdinamentoCriteri] listaOC null");
    	}
		logger.info("[SchedaProgetto::ricalcolaOrdinamentoCriteri] END");
		return mappa;
	}

	/**
	 * Verifico bonta dei parametri di configurazione: input.criteriToView e input.ordineVisualizzazioneCriteri
	 * @param logger
	 * @throws CommonalityException
	 */
	private void verificaBontaParametriConfigurazionePagina(Logger logger) throws CommonalityException {
		
    	if(StringUtils.isBlank(input.criteriToView)){
    		logger.error("[SchedaProgetto::verificaBontaParametriConfigurazionePagina] input.criteriToView null o vuoto");
    		throw new CommonalityException("Errore di configurazione dei Criteri da visualizzare");
    	}else{
    		try{
    			// verifico che siano tutti numerici
    			String[] criteriToView = (input.criteriToView.replace(" ", "")).split(",");
	    		for (String crt : criteriToView) {
	    			Integer.parseInt(crt);
				}
    		}catch (Exception e){
    			logger.error("[SchedaProgetto::verificaBontaParametriConfigurazionePagina] input.criteriToView contengono dati non numerici");
	    		throw new CommonalityException("Errore di configurazione dei Criteri da visualizzare");
    		}
    	}
	}

	/**
	 * Trasformo gli array delle specifiche e dei parametri presenti nll'oggetto
	 * restituito in input in liste
	 * @param crtToV2
	 * @param logger
	 * @return
	 */
	private CriterioVO trasformaArrToList(CriterioVO crtToV2, Logger logger) {
		CriterioVO criterio = crtToV2;
		
		List<SpecificaVO> listaSpecifiche = new ArrayList<SpecificaVO>();
		SpecificaVO[] arraySpec = criterio.getArraySpecifiche();
		for (SpecificaVO specificaVO : arraySpec) {
			logger.info("[SchedaProgetto::trasformaArrToList] IdSpecifica="+specificaVO.getIdSpecifica());
			
			SpecificaVO spec = specificaVO;
			spec.setIdCriterio(crtToV2.getIdCriterio());
			List<ParametroVO> listaParametri = new ArrayList<ParametroVO>();
			
			ParametroVO[] arrayParametri = specificaVO.getArrayParametri();
			for (ParametroVO parametroVO : arrayParametri) {
				logger.info("[SchedaProgetto::trasformaArrToList] IdParametro="+parametroVO.getIdParametro());
				parametroVO.setIdSpecifica(specificaVO.getIdSpecifica());
				listaParametri.add(parametroVO);
			}
			
			spec.setListaParametri(listaParametri);
			listaSpecifiche.add(spec);
		}
		criterio.setListaSpecifiche(listaSpecifiche);
		return criterio;
	}


	private List<CriterioVO> popolaListaCriteri(List<VistaCriterioVO> crList, TreeMap<Integer, Integer> mappaOridineCriteri, Logger logger) {
		logger.info("[SchedaProgetto::popolaListaCriteri] BEGIN");
		
		List<CriterioVO> lista = null;
		
		//mappa contenente 1 sola entry per criterio
		TreeMap<Integer, CriterioVO> criteriMap = new TreeMap<Integer, CriterioVO>();
		
		if (crList==null || (crList!=null && crList.isEmpty())){
			logger.info("[SchedaProgetto::popolaListaCriteri] END, crList nulla");
			return lista;
		}
		
		// Estraggo dalla lista crList i singoli oggetti e li riordino in una TreeMap
		for (VistaCriterioVO cr : crList) {
			
			CriterioVO criterio = null;
			SpecificaVO specifica = null;
			ParametroVO parametro = null;
			
			if(criteriMap.containsKey(cr.getIdCriterio())){
				
				criterio = criteriMap.get(cr.getIdCriterio());
				logger.info("[SchedaProgetto::popolaListaCriteri] criterio EXISTS=, idCriterio="+ criterio.getIdCriterio());
				if(criterio.getMappaSpecifiche().containsKey(cr.getIdSpecifica())){
					
					specifica = criterio.getMappaSpecifiche().get(cr.getIdSpecifica());
					
					logger.info("[SchedaProgetto::popolaListaCriteri] specifica EXISTS, idSPecifica="+ specifica.getIdSpecifica());
					// parametro sara' sempre NEW
					parametro = new ParametroVO();
		    		parametro.setDescrBreveParametro(cr.getDescrBreveParametro());
		    		parametro.setDescrizioneParametro(cr.getDescrizioneParametro());
		    		parametro.setIdCriterio(cr.getIdCriterio());
		    		parametro.setIdParametro(cr.getIdParametro());
		    		parametro.setIdSpecifica(cr.getIdSpecifica());
		    		parametro.setOrdineParametro(cr.getOrdineParametro());
		    		parametro.setPunteggioParametro(cr.getPunteggioParametro());
		    		parametro.setIdParametroValut(cr.getIdParametroValut()+"");
					
		    		TreeMap<Integer, ParametroVO>  mappaParametri = specifica.getMappaParametri();
		    		mappaParametri.put(cr.getIdParametro(), parametro);
		    		specifica.setMappaParametri(mappaParametri);
					
				}else{
					logger.info("[SchedaProgetto::popolaListaCriteri] specifica NEW");
					
					specifica = new SpecificaVO();
		    		specifica.setDescrBreveSpecifica(cr.getDescrBreveSpecifica());
		    		specifica.setDescrizioneSpecifica(cr.getDescrizioneSpecifica());
		    		specifica.setIdCriterio(cr.getIdCriterio());
		    		specifica.setIdSpecifica(cr.getIdSpecifica());
		    		specifica.setOrdineSpecifica(cr.getOrdineSpecifica());
		    		specifica.setTipoCampo(cr.getTipoCampo());
		    		
		    		parametro = new ParametroVO();
		    		parametro.setDescrBreveParametro(cr.getDescrBreveParametro());
		    		parametro.setDescrizioneParametro(cr.getDescrizioneParametro());
		    		parametro.setIdCriterio(cr.getIdCriterio());
		    		parametro.setIdParametro(cr.getIdParametro());
		    		parametro.setIdSpecifica(cr.getIdSpecifica());
		    		parametro.setOrdineParametro(cr.getOrdineParametro());
		    		parametro.setPunteggioParametro(cr.getPunteggioParametro());
		    		parametro.setIdParametroValut(cr.getIdParametroValut()+""); //  new
		    		
		    		TreeMap<Integer, ParametroVO>  mappaParametri = new TreeMap<Integer, ParametroVO>();
		    		mappaParametri.put(cr.getIdParametro(), parametro);
		    		specifica.setMappaParametri(mappaParametri);
					
		    		TreeMap<Integer, SpecificaVO> mappaSpecifiche = criterio.getMappaSpecifiche();
		    		mappaSpecifiche.put(cr.getIdSpecifica(), specifica);
		    		criterio.setMappaSpecifiche(mappaSpecifiche);
				}
				
			}else{
				logger.info("[SchedaProgetto::popolaListaCriteri] criterio NEW");
				
				criterio = new CriterioVO();
				criterio.setIdBando(cr.getIdBando());
				criterio.setIdCriterio(cr.getIdCriterio());
				criterio.setDescrBreveCriterio(cr.getDescrBreveCriterio());
				criterio.setDescrizioneCriterio(cr.getDescrizioneCriterio());
				criterio.setOrdineCriterio(cr.getOrdineCriterio());
				criterio.setIdTipolIntervento(cr.getIdTipolIntervento());
				
				Integer nuovaPosiz = mappaOridineCriteri.get(criterio.getIdCriterio());
				criterio.setPosizioneCriterio(nuovaPosiz);
				
				specifica = new SpecificaVO();
	    		specifica.setDescrBreveSpecifica(cr.getDescrBreveSpecifica());
	    		specifica.setDescrizioneSpecifica(cr.getDescrizioneSpecifica());
	    		specifica.setIdCriterio(cr.getIdCriterio());
	    		specifica.setIdSpecifica(cr.getIdSpecifica());
	    		specifica.setOrdineSpecifica(cr.getOrdineSpecifica());
	    		specifica.setTipoCampo(cr.getTipoCampo());
	    		
	    		parametro = new ParametroVO();
	    		parametro.setDescrBreveParametro(cr.getDescrBreveParametro());
	    		parametro.setDescrizioneParametro(cr.getDescrizioneParametro());
	    		parametro.setIdCriterio(cr.getIdCriterio());
	    		parametro.setIdParametro(cr.getIdParametro());
	    		parametro.setIdSpecifica(cr.getIdSpecifica());
	    		parametro.setOrdineParametro(cr.getOrdineParametro());
	    		parametro.setPunteggioParametro(cr.getPunteggioParametro());
	    		parametro.setIdParametroValut(cr.getIdParametroValut()+""); //  new
	    		
	    		TreeMap<Integer, ParametroVO>  mappaParametri = new TreeMap<Integer, ParametroVO>();
	    		mappaParametri.put(cr.getIdParametro(), parametro);
	    		specifica.setMappaParametri(mappaParametri);
	    		
	    		TreeMap<Integer, SpecificaVO> mappaSpecifiche = new TreeMap<Integer, SpecificaVO>();
	    		mappaSpecifiche.put(cr.getIdSpecifica(), specifica);
	    		criterio.setMappaSpecifiche(mappaSpecifiche);
	    		
			}
			criteriMap.put(cr.getIdCriterio(), criterio);
		}
		
		
		if(!criteriMap.isEmpty()){
			logger.info("[SchedaProgetto::popolaListaCriteri] criteriMap.size()="+criteriMap.size());
			 
//			logger.info("[SchedaProgetto::popolaListaCriteri] -----------------------------");
//			for (Integer id1 : criteriMap.keySet()) {
//				logger.info("[SchedaProgetto::popolaListaCriteri] criteriMap:" + criteriMap.get(id1));
//			}
//			logger.info("[SchedaProgetto::popolaListaCriteri] -----------------------------");
			
			//svuoto le mappaSpecifica e mappaCriteri presenti negli oggetti criterio della criteriMap
			// e popolo le corrispondenti listeSpecifiche e listeParametri
			lista = trasformaMappeInListe(criteriMap, logger);
//			logger.info("[SchedaProgetto::popolaListaCriteri] lista="+lista);
			
		}else{
			logger.info("[SchedaProgetto::popolaListaCriteri] criteriMap vuota");
		}
		
		logger.info("[SchedaProgetto::popolaListaCriteri] END");
		return lista;
	}

	private List<CriterioVO> trasformaMappeInListe( TreeMap<Integer, CriterioVO> criteriMap, Logger logger) {
		
		logger.info("[SchedaProgetto::trasformaMappeInListe] BEGIN " );
		List<CriterioVO> ll = new ArrayList<CriterioVO>();
		
		for (Integer id1 : criteriMap.keySet()) {
//			logger.info("[SchedaProgetto::trasformaMappeInListe] criteriMap:" + criteriMap.get(id1));
			
			CriterioVO cr = criteriMap.get(id1);
			if(cr.getMappaSpecifiche()!=null && !cr.getMappaSpecifiche().isEmpty()){
				
				List<SpecificaVO> listaSpecifiche = cr.getListaSpecifiche();
					
				if(listaSpecifiche==null) listaSpecifiche = new ArrayList<SpecificaVO>();
					
				SpecificaVO[] listaSP = cr.getArraySpecifiche();
				if(listaSP==null) listaSP = new SpecificaVO[cr.getMappaSpecifiche().size()];
				
				int i = 0;
				for (Integer id2 : cr.getMappaSpecifiche().keySet()) {
					
					SpecificaVO specifica = cr.getMappaSpecifiche().get(id2);
					
					if(specifica.getMappaParametri()!=null && !specifica.getMappaParametri().isEmpty()){
						
						List<ParametroVO> listaParametri = specifica.getListaParametri();
						if(listaParametri==null)
							listaParametri = new ArrayList<ParametroVO>();
						
						ParametroVO[] listaPR = specifica.getArrayParametri();
						if(listaPR==null)
							listaPR = new ParametroVO[specifica.getMappaParametri().size()];
						
						int j = 0;
						for (Integer id3 : specifica.getMappaParametri().keySet()) {
							
//							logger.info("[SchedaProgetto::trasformaMappeInListe] add parametro ="+id3);
							listaParametri.add(specifica.getMappaParametri().get(id3));
							listaPR[j] = specifica.getMappaParametri().get(id3);
							j++;
						}
						specifica.setListaParametri(listaParametri);
						specifica.setMappaParametri(null);
						specifica.setArrayParametri(listaPR);

					}
//					logger.info("[SchedaProgetto::trasformaMappeInListe] add specifica ="+id2);
					listaSP[i] = specifica;
					listaSpecifiche.add(specifica);
					i++;
				}
				cr.setMappaSpecifiche(null);
				cr.setArraySpecifiche(listaSP);
				cr.setListaSpecifiche(listaSpecifiche);
				
			}
			ll.add(cr);
		}
		logger.info("[SchedaProgetto::trasformaMappeInListe] trasformaMappeInListe END " );
		return ll;
	}

	@Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
		Logger logger = Logger.getLogger(info.getLoggerName());	  
		logger.info("[SchedaProgetto::modelValidate]  BEGIN");
		
		// Controllo che sia compilato almeno un parametro nella pagina...
		
		SchedaProgettoVO schprg = input.schedaProgettoVO;
		
		if(schprg!=null){
			CriterioVO[] listaCr = schprg.getCriteriList();
			
			String criteriToView = ","+input.criteriToView.replace(" ", "")+",";
			logger.info("[SchedaProgetto::modelValidate]  criteriToView="+criteriToView);
			
			List<Integer> errorList = new ArrayList<Integer>();
			int numCriteri = 0;
			
			// controllo solo i criteri che appartengono alla pagina su cui sono
			for (CriterioVO criterioVO : listaCr) {
				
				boolean criterioValorizzato = false;
				
				if(criteriToView.contains(","+criterioVO.getIdCriterio()+",")){
					logger.info("[SchedaProgetto::modelValidate] Criterio="+criterioVO.getIdCriterio()+" "+criterioVO.getDescrBreveCriterio());
				
					// trovato criterio, eseguo controllo
					numCriteri ++;
					
					SpecificaVO[] arraySpecifiche = criterioVO.getArraySpecifiche();
					if(arraySpecifiche!=null){
						
						boolean specificaValorizzata = false;
						for (SpecificaVO specificaVO : arraySpecifiche) {
							
							logger.info("[SchedaProgetto::modelValidate] specifica="+specificaVO.getIdSpecifica()+" "+specificaVO.getDescrBreveSpecifica());
							
							ParametroVO[] arrayParametri = specificaVO.getArrayParametri();
							boolean parametroValorizzato = false;
							for (ParametroVO parametroVO : arrayParametri) {
								
								logger.info("[SchedaProgetto::modelValidate] parametro ="+parametroVO.getIdParametro()+" = "+parametroVO.getChecked());
								
								//cerco almeno un parametro valorizzato
								if(StringUtils.equals(parametroVO.checked, "checked")){
									parametroValorizzato = true; 
									logger.info("[SchedaProgetto::modelValidate] parametroValorizzato ="+parametroValorizzato);
									break;
								}
							}
							if(parametroValorizzato){
								// trovato un parametro valorizzato, esco
								specificaValorizzata = true;
								logger.info("[SchedaProgetto::modelValidate] specificaValorizzata ="+specificaValorizzata);
								break;
							}
						}
						if(specificaValorizzata){
							// trovato una specifica valorizzata, esco
							criterioValorizzato = true;
							logger.info("[SchedaProgetto::modelValidate] criterioValorizzato ="+criterioValorizzato);
//							break;
						}
						
						if(!criterioValorizzato){
							errorList.add(criterioVO.getIdCriterio());
							logger.info("[SchedaProgetto::modelValidate] criterio non valorizzato="+criterioVO.getIdCriterio());
						}
						
					}else{
						errorList.add(criterioVO.getIdCriterio());
						logger.info("[SchedaProgetto::modelValidate] arraySpecifiche null, criterio non valorizzato="+criterioVO.getIdCriterio());
					}
				}
			}
			
			logger.info("[SchedaProgetto::modelValidate] numCriteri="+numCriteri);
			
			if(!errorList.isEmpty()){
				
				logger.info("[SchedaProgetto::modelValidate] errorList.size()="+errorList.size());
				if(numCriteri==errorList.size()){
					// non e' stato valorizzato nessun parametro nella pagina (tutti i criteri della pagina non sono stati compilati)
					for (Integer item : errorList) {
						logger.warn("[SchedaProgetto::modelValidate] non e' stato valorizzato nessun parametro per il criterio " + item);
						addMessage(newMessages,"_criterioID", item+"D");	
					}
					addMessage(newMessages,"_criterioMsg", "Valorizzare almeno un parametro in un criterio");	
				}
			}else{
				logger.info("[SchedaProgetto::modelValidate] errorList NULL");
			}
		}else{
			logger.info("[SchedaProgetto::modelValidate] input.schedaProgettoVO NULL");
		}
		
		logger.info("[SchedaProgetto::modelValidate]  END");
		return newMessages;
	  }

	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }

}
