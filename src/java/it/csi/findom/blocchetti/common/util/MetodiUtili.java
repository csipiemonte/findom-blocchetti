/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;

import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.TipologiaInterventoNeveVO;
import it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB.CultFormaAgevolazioneBDAO;
import it.csi.findom.blocchetti.blocchetti.formaFinanziamento.FormaFinanziamentoDAO;
import it.csi.findom.blocchetti.blocchetti.ruoloProgettiComuni.RuoloProgettiComuniVO;
import it.csi.findom.blocchetti.common.dao.DipartimentoDAO;
import it.csi.findom.blocchetti.common.dao.DocumentazioneNGDAO;
import it.csi.findom.blocchetti.common.dao.EnteDAO;
import it.csi.findom.blocchetti.common.dao.PartnerDAO;
import it.csi.findom.blocchetti.common.ldap.IpaLdap;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoCPVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiItemVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;
import it.csi.findom.blocchetti.common.vo.allegati.DocumentoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaDettaglioInterventoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.cultPianospese.DettaglioVoceSpesaInterventoCulturaVO;
import it.csi.findom.blocchetti.common.vo.dipartimento.DipartimentoVO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.StatoDomandaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.partner.AcronimoBandiVO;
import it.csi.findom.blocchetti.common.vo.partner.CapofilaAcronimoPartnerVO;
import it.csi.findom.blocchetti.common.vo.partner.PartnerItemVO;
import it.csi.findom.blocchetti.common.vo.partner.PartnerVO;
import it.csi.findom.blocchetti.common.vo.pianoAcquistiAutomezzi.DettaglioAcquistiVO;
import it.csi.findom.blocchetti.common.vo.pianospese.DettaglioCostiVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.common.vo.premialitaprogetto.PremialitaProgettoItemVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.DettaglioAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGVO;
import it.csi.findom.blocchetti.commonality.Constants;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

public class MetodiUtili 
{
	
	public static String recuperaPercByIdBando(String idBando, Logger logger) {
		
		 String ris = "";
		try {
			ris = FormaFinanziamentoDAO.getPercentuale(idBando, logger);
			logger.info("percentuale recuperata dal db risulta: " + ris);
			return ris;
		} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return ris;
    }
    
	  /**
	   * Calcolo utile per 
	   * bandi industria
	   * che richiedono calcolo max importo richiedibile
	   * limitato dalla % del totale delle spese.
	   * 
	   * @param totaleSpeseBD
	   * @param percentuale
	   * @param idBando
	   * @return
	   */
	  public static BigDecimal calcoloMaxImporto(BigDecimal totaleSpeseBD, BigDecimal percentuale) {
		  
		  BigDecimal ris = new BigDecimal(0);
		  ris = totaleSpeseBD.multiply(percentuale).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
		  return ris;
	  }
	  
	  /** 
	   * Recupero importo_max_erogabile
	   * dal db
	   * in base ad idBando
	   * 
	   * */
	  public static String recuperaImpMaxErogabileByIdBando(String idBando, Logger logger) {
			
		  String ris = "";
		try {
			ris = FormaFinanziamentoDAO.getImportoMassimoErogabile(idBando, logger);
			return ris;
		} catch (NullPointerException npe) {
			ris = "";
			return ris;
		}catch (CommonalityException e) {
			e.printStackTrace();
		}
		return ris;
	  }
	
    /**
     * statoCompilazioneUploadAllegati  
     * @param allegatiVO
     * @param info
     * @param logprefix
     * @param logger
     * @return String un valore tra:
     * 				PAGINA_NON_COMPILATA (compilazione non iniziata) 
     *              PAGINA_IN_COMPILAZIONE (compilazione in corso, ma non ancora terminata) 
     *              PAGINA_COMPILATA (compilazione terminata, ovvero se ci sono degli allegati obbligatori questi sono gia' stati uploadati) 
     */   
    public static String statoCompilazioneUploadAllegati(AllegatiVO allegatiVO, FinCommonInfo info, String logprefix, Logger logger){
    	String esito = Constants.PAGINA_NON_COMPILATA;    	
		if (allegatiVO != null && !allegatiVO.isEmpty()) {

			String elencoIdAllegatiObbligatori = allegatiVO.getElencoIdAllegatiObbligatori();	// TODO: questa non risulta elenco allegati obbligatori... (da verificare )	
			
			
			AllegatiItemVO[] allegatiList = allegatiVO.getAllegatiList();

			logger.info(logprefix + " elencoIdAllegatiObbligatori " + elencoIdAllegatiObbligatori);
			logger.info(logprefix + " allegatiList " + allegatiList);

			if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")){
				if(allegatiList!=null && allegatiList.length!=0){
					esito = Constants.PAGINA_IN_COMPILAZIONE;
					//in idTipologieSalvateMap metto le distinte tipologie documento degli allegati gia' salvati
					Map<String, String> idTipologieSalvateMap = new HashMap<>();				   
					for(int i=0; i<allegatiList.length;i++){
						AllegatiItemVO docMap = allegatiList[i];						
						if(docMap!=null){
							DocumentoVO documento = docMap.getDocumento();
							if(documento!=null){
								String idTipologia = documento.getIdTipologia();
								logger.info(logprefix + " documento salvato corrente = "+documento);
								logger.info(logprefix + " id: "+idTipologia);
								idTipologieSalvateMap.put(idTipologia,"x");
							}
						}
					}					
					// elimino primo carattere ','			
					String strTmp = elencoIdAllegatiObbligatori.substring(1);
					String[] arrayIdTipolObbl = strTmp.split(",");
					int counter = 0;
					for (int i = 0; i < arrayIdTipolObbl.length; i++) {
						if(idTipologieSalvateMap.containsKey(arrayIdTipolObbl[i])){							
							logger.debug(logprefix + " trovato tra i doc salvati la tipologia arrayIdTipolObbl["+i+"]="+arrayIdTipolObbl[i]);
							counter ++; // trovato un doc obbligatorio
						}
					}				
					if(counter==arrayIdTipolObbl.length){
						// se tutti i doc obbligatori sono stati trovati tra i documenti allegati allora la pagina e' considerata come compilata completamente 
						esito = Constants.PAGINA_COMPILATA;				
					}	
				}
			}else{
				//non ci sono allegati obbligatori
				esito = Constants.PAGINA_COMPILATA; //Se non ci sono allegati obbligatori, la pagina e' da considerarsi compilata (nella verifica finale si verifichera' 
				//se mancano degli allegati che solo in certe circostanze sono obbligatori)
			}			
		}else {
			//in questo caso la pagina non e' stata compilata e quindi non so se ci sono allegati obbligatori (l'elenco degli obbligatori viene salvato 
			//sull'xml al primo salvataggio della pagina) 
			try {				
				boolean esistonoAllegatiObbligatori = Utils.esistonoAllegatiObbligatori(info, logger);//se non esistono allegati obbligatori considero la pag come compilata				
				if(!esistonoAllegatiObbligatori){
					esito = Constants.PAGINA_COMPILATA;
				}
			} catch (CommonalityException e) {
				logger.error(logprefix + "si e' verificato un errore verificando se ci sono allegati obbligatori" , e);
			}
		}
		return esito;    	
    }
    
    public static String statoCompilazioneUploadAllegatiPerTipoInterventoSelezionato(AllegatiVO allegatiVO, FinCommonInfo info, String logprefix, Logger logger){
    	String esito = Constants.PAGINA_NON_COMPILATA;    	
		if (allegatiVO != null && !allegatiVO.isEmpty()) {

			String elencoIdAllegatiObbligatori = allegatiVO.getElencoIdAllegatiObbligatori();	// TODO: questa non risulta elenco allegati obbligatori... (da verificare )	
			
			
			AllegatiItemVO[] allegatiList = allegatiVO.getAllegatiList();

			logger.info(logprefix + " elencoIdAllegatiObbligatori " + elencoIdAllegatiObbligatori);
			logger.info(logprefix + " allegatiList " + allegatiList);

			if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")){
				if(allegatiList!=null && allegatiList.length!=0){
					esito = Constants.PAGINA_IN_COMPILAZIONE;
					//in idTipologieSalvateMap metto le distinte tipologie documento degli allegati gia' salvati
					Map<String, String> idTipologieSalvateMap = new HashMap<>();				   
					for(int i=0; i<allegatiList.length;i++){
						AllegatiItemVO docMap = allegatiList[i];						
						if(docMap!=null){
							DocumentoVO documento = docMap.getDocumento();
							if(documento!=null){
								String idTipologia = documento.getIdTipologia();
								logger.info(logprefix + " documento salvato corrente = "+documento);
								logger.info(logprefix + " id: "+idTipologia);
								idTipologieSalvateMap.put(idTipologia,"x");
							}
						}
					}					
					// elimino primo carattere ','			
					String strTmp = elencoIdAllegatiObbligatori.substring(1);
					String[] arrayIdTipolObbl = strTmp.split(",");
					int counter = 0;
					for (int i = 0; i < arrayIdTipolObbl.length; i++) {
						if(idTipologieSalvateMap.containsKey(arrayIdTipolObbl[i])){							
							logger.debug(logprefix + " trovato tra i doc salvati la tipologia arrayIdTipolObbl["+i+"]="+arrayIdTipolObbl[i]);
							counter ++; // trovato un doc obbligatorio
						}
					}				
					if(counter==arrayIdTipolObbl.length){
						// se tutti i doc obbligatori sono stati trovati tra i documenti allegati allora la pagina e' considerata come compilata completamente 
						esito = Constants.PAGINA_COMPILATA;				
					}	
				}
			}else{
				//non ci sono allegati obbligatori
				esito = Constants.PAGINA_COMPILATA; //Se non ci sono allegati obbligatori, la pagina e' da considerarsi compilata (nella verifica finale si verifichera' 
				//se mancano degli allegati che solo in certe circostanze sono obbligatori)
			}			
		}else {
			//in questo caso la pagina non e' stata compilata e quindi non so se ci sono allegati obbligatori (l'elenco degli obbligatori viene salvato 
			//sull'xml al primo salvataggio della pagina) 
			try {				
				boolean esistonoAllegatiObbligatori = Utils.esistonoAllegatiObbligatori(info, logger);//se non esistono allegati obbligatori considero la pag come compilata				
				if(!esistonoAllegatiObbligatori){
					esito = Constants.PAGINA_COMPILATA;
				}
			} catch (CommonalityException e) {
				logger.error(logprefix + "si e' verificato un errore verificando se ci sono allegati obbligatori" , e);
			}
		}
		return esito;    	
    }
    
    
    /**
     * 
     * @param allegati
     * @param info
     * @param logger
     * @param logprefix
     * @return se ritorna false, sicuramente la pagina ha degli allegati obbligatori e non e' stato uploadato neppure un allegato (obbligatorio o non obbligatorio)
     *         se ritorna true, o non ci sono allegato obbligatori, o ce ne sono, ma in tal caso non e' detto che tutti gli allegatori obbligatori siano
     *         stati uploadati (e' necessario un ulteriore controllo)
     */
    public static boolean verificaCompilazioneUploadAllegati(AllegatiVO allegati, FinCommonInfo info,Logger logger, String logprefix) 
    {
    	boolean isUploadCompiled = false;
	
    	if (allegati != null) 
    	{
			isUploadCompiled = true;
			String elencoIdAllegatiObbligatori = allegati.getElencoIdAllegatiObbligatori();			
		
			if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")) {
				if (allegati.getAllegatiList() != null) {
					List<AllegatiItemVO> allegatiList = Arrays.asList(allegati.getAllegatiList());			
					if(allegatiList==null || (allegatiList!=null && allegatiList.isEmpty())) {
						isUploadCompiled = false;
					}
				}
			}
			logger.debug( logprefix + "tab 'Allegati e Dichiarazioni/Upload allegati' compilato");
		
    	} else {
			// la pagina non e' mai stata salvata; 
    		// verifico se ci sono allegati obbligatori; 
    		// se non ce ne sono considero la pagina come compilata 
			try {				
				isUploadCompiled = !Utils.esistonoAllegatiObbligatori(info, logger);	
				
			} catch (CommonalityException e) {
				logger.error(logprefix + "si e' verificato un errore verificando se ci sono allegati obbligatori" , e);
			}
		}
		return isUploadCompiled;
	}
    
    
    
    /**
     * versione semplificata del metodo verificaCompilazioneUploadAllegati (che mantengo per retrocompatibilita') 
     * @param allegati
     * @param info
     * @param logger
     * @param logprefix
     * @return se almeno un allegato è stato uploadato, ritorna true indipendentemente dalla presenza o meno di allegati obbligatori;
     * 		   se nessun allegato è stato uploadato: se esistono allegati obbligatori ritorna false, altrimenti ritorna true
     */
    public static boolean verificaCompilazioneUploadAllegati2(AllegatiVO allegati, FinCommonInfo info,Logger logger, String logprefix) 
    {
    	boolean isUploadCompiled = false;
	
    	if (allegati != null && allegati.getAllegatiList() != null && allegati.getAllegatiList().length>0) 
    	{
			isUploadCompiled = true;
			logger.debug( logprefix + "tab 'Allegati e Dichiarazioni/Upload allegati' compilato");
		
    	} else {
			// la pagina non e' mai stata salvata; 
    		// verifico se ci sono allegati obbligatori; 
    		// se non ce ne sono considero la pagina come compilata 
			try {				
				isUploadCompiled = !Utils.esistonoAllegatiObbligatori(info, logger);			
			
			} catch (CommonalityException e) {
				logger.error(logprefix + "si e' verificato un errore verificando se ci sono allegati obbligatori" , e);
			}
		}
		return isUploadCompiled;
	}
    
    
    
    /**
     * questo metodo va chiamato se sussistono le condizioni per cui idAllegato deve essere presente in allegati
     * @param allegati
     * @param idAllegato
     * @param logger
     * @param logprefix
     * @return true se idAllegato è presente in allegati, false altrimenti
     */
    public static boolean verificaPresenzaAllegatoUploadAllegati(AllegatiVO allegati, String idAllegato, Logger logger, String logprefix) 
    {
    	boolean trovato = false;
    
    	if (allegati != null && !allegati.isEmpty()) 
    	{
    		//cerco nella lista degli allegati l'allegato con id passato  
    		AllegatiItemVO[] allegatiList = allegati.getAllegatiList();
    		logger.debug( logprefix + " verificaPresenzaAllegatoUploadAllegati allegatiList="+allegatiList);
    	
    		if(allegatiList!=null)
    		{
    			for(int i=0; i<allegatiList.length;i++)
    			{
    				AllegatiItemVO allegato = allegatiList[i];
    			
    				if(allegato!=null)
    				{
    					DocumentoVO documento = allegato.getDocumento();
    					logger.debug( logprefix + " documento="+documento);
    				
    					if(documento!=null)
    					{
    						String idTipologia = (String) documento.getIdTipologia();
    						logger.debug(logprefix + " idTipologia="+idTipologia);

    						if (StringUtils.equals(idTipologia, idAllegato)){
    							trovato = true;
    							break;
    						}
    					}
    				}
    			} //chiude il for
    		} //chiude test null su allegatiList
    	}
    	logger.debug( logprefix + " verificaPresenzaAllegatoUploadAllegati trovato="+trovato);
    	return trovato;
    }   
    
    
    /***
     * 
     * @param allegati
     * @param idAllegato
     * @param logger
     * @param logprefix
     * @return
     */
    public static boolean verificaPresenzaAllegatoUploadCondizionati(AllegatiVO allegati, String idAllegato, Logger logger, String logprefix) 
    {
    	boolean trovato = false;
    
    	if (allegati != null && !allegati.isEmpty()) 
    	{
    		//cerco nella lista degli allegati l'allegato con id passato  
    		AllegatiItemVO[] allegatiList = allegati.getAllegatiList();
    		logger.info("[DEBUG] " + logprefix + " allegatiList="+allegatiList.length);
    	
    		if(allegatiList!=null)
    		{
    			for(int i=0; i<allegatiList.length;i++)
    			{
    				AllegatiItemVO allegato = allegatiList[i];
    			
    				if(allegato!=null)
    				{
    					DocumentoVO documento = allegato.getDocumento();
    					logger.info("[DEBUG] " + logprefix + " documento="+documento);
    				
    					if(documento!=null)
    					{
    						String idTipologia = (String) documento.getIdTipologia();
    						logger.info("[DEBUG] " + logprefix + " idTipologia="+idTipologia);

    						if (StringUtils.equals(idTipologia, idAllegato)){
    							trovato = true;
    							break;
    						}
    					}
    				}
    			} //chiude il for
    		} //chiude test null su allegatiList
    	}
    	return trovato;
    }
    
    
    
    /**
     * il metodo assume che allegati!=null e allegati.getAllegatiList()!=null (in caso contrario non deve essere chiamato)
     * @param allegati
     * @param logger
     * @param logprefix
     * @return true se sono stati uploadati tutti gli allegati obbligatori oppure se non ci sono allegati obbligatori associati al bando
     */
    public static boolean verificaPresenzaTuttiAllegatiObbligatori(AllegatiVO allegati, Logger logger, String logprefix) {
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatori BEGIN");     
    	boolean tuttiAllegatiObbligatoriPresenti = false; 

    	
    	String elencoIdAllegatiObbligatori = allegati.getElencoIdAllegatiObbligatori();			
    	
    	logger.info(logprefix + " elencoIdAllegatiObbligatori="+elencoIdAllegatiObbligatori); 

    	Map<String,String> idTipologieSalvateMap = new HashMap<String,String>();			
    	AllegatiItemVO[] allegatiList = allegati.getAllegatiList();			

    	for(int i=0; i<allegatiList.length;i++){
    		AllegatiItemVO docMap = allegatiList[i];				
    		if(docMap!=null){
    			DocumentoVO documento = docMap.getDocumento();
    			logger.info("[DEBUG] " + logprefix + " documento="+documento);
    			if(documento!=null){
    				String idTipologia = documento.getIdTipologia();
    				logger.info("[DEBUG] " + logprefix + " idTipologia="+idTipologia);

    				idTipologieSalvateMap.put(idTipologia,"x");
    			}
    		}
    	}	
    	logger.info(logprefix + " idTipologieSalvateMap="+idTipologieSalvateMap);
    	
    	if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")){
    		// elimino primo carattere ','			
    		String strTmp = elencoIdAllegatiObbligatori.substring(1);
    		String[] arrayIdTipolObbl = strTmp.split(",");
    		int counter = 0;
    		for (int i = 0; i < arrayIdTipolObbl.length; i++) {
    			if(idTipologieSalvateMap.containsKey(arrayIdTipolObbl[i])){
    				logger.info("[DEBUG] " + logprefix + "arrayIdTipolObbl["+i+"]="+arrayIdTipolObbl[i]);
    				counter ++;
    			}
    		}
    		if(counter==arrayIdTipolObbl.length){
    			// se i doc contati sono pari a quelli attesi allora ci sono tutti
    			tuttiAllegatiObbligatoriPresenti = true;
    		}
    	}else{
    		//se non ci sono obbligatori ritorna true
    		tuttiAllegatiObbligatoriPresenti = true;
    	}
    	
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatori END");
    	return tuttiAllegatiObbligatoriPresenti; 

    }

    public static boolean verificaPresenzaTuttiAllegatiObbligatori2(AllegatiVO allegati, Integer idBando, Integer idSportelloBando, Integer idDomanda, Integer idTipolBeneficiario, Boolean isObbligatorio, Logger logger, String logprefix)
    {
    	boolean tuttiDocumentiObbligatoriPresenti = true;
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatori2 BEGIN");     

		//Lettura allegati obbligatori per tipologia intervento
		List<TipologiaAllegatoVO> elencoAllegatiObbligatori = null;
		try
		{
			elencoAllegatiObbligatori = DocumentazioneNGDAO.getTipologiaAllegatoList(idBando, idSportelloBando, idDomanda, Boolean.TRUE, logger);
		}
		catch (CommonalityException e)
		{
			//  Auto-generated catch block
			e.printStackTrace();
		}
		if (elencoAllegatiObbligatori == null || elencoAllegatiObbligatori.isEmpty()) return tuttiDocumentiObbligatoriPresenti;

		//Allegati salvati su db
    	AllegatiItemVO[] allegatiList = allegati.getAllegatiList();			
    	HashMap<Integer, String> hmAllegatiPresenti = new HashMap<Integer, String>();
		for (AllegatiItemVO item : allegatiList)
		{
			hmAllegatiPresenti.put(new Integer(item.getDocumento().getIdTipologia()), item.getDocumento().getIdTipologia());
		}
		logger.info(logprefix + " hmAllegatiPresenti = " + hmAllegatiPresenti);

		//Verifica tipologia intervento
    	for (TipologiaAllegatoVO tipologiaAllegatoVO : elencoAllegatiObbligatori)
    	{
    		if (! hmAllegatiPresenti.containsKey(tipologiaAllegatoVO.getIdallegato()))
    		{
    			tuttiDocumentiObbligatoriPresenti = false;
    		}
    	}
    	
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatori2 END");

    	return tuttiDocumentiObbligatoriPresenti; 
    }

    /**
     * - 2R
     * @param allegati
     * @param allegatiCondizionati
     * @param logger
     * @param logprefix
     * @return
     */
    public static boolean verificaPresenzaAllegatiCondizionatiObbligatori(AllegatiVO allegati, String[] allegatiCondizionati, Logger logger, String logprefix) 
    {
    	logger.info(logprefix + " verificaPresenzaAllegatiCondizionatiObbligatori BEGIN");     
    	boolean tuttiAllegatiObbligatoriPresenti = false; 
    	
    	String elencoIdAllegatiObbligatori = null;
    	
    	String tmp_elencoIdAllegatiObbligatori = allegati.getElencoIdAllegatiObbligatori();			
    	logger.info(logprefix + " tmp_elencoIdAllegatiObbligatori="+tmp_elencoIdAllegatiObbligatori); 
    	
    	elencoIdAllegatiObbligatori = tmp_elencoIdAllegatiObbligatori;
    	logger.info(" tmp_elencoIdAllegatiObbligatori="+elencoIdAllegatiObbligatori);
    	
    	for (int i = 0; i < allegatiCondizionati.length; i++) {
    		if(allegatiCondizionati != null){
    			elencoIdAllegatiObbligatori = tmp_elencoIdAllegatiObbligatori.concat(","+allegatiCondizionati[i]);
    			logger.info("id: " + elencoIdAllegatiObbligatori);
    		}
		}
    	
    	Map<String,String> idTipologieSalvateMap = new HashMap<String,String>();			
    	AllegatiItemVO[] allegatiList = allegati.getAllegatiList();			

    	for(int i=0; i<allegatiList.length;i++){
    		AllegatiItemVO docMap = allegatiList[i];				
    		if(docMap!=null){
    			DocumentoVO documento = docMap.getDocumento();
    			logger.info("[DEBUG] " + logprefix + " documento="+documento);
    			if(documento!=null){
    				String idTipologia = (String) documento.getIdTipologia();
    				logger.info("[DEBUG] " + logprefix + " idTipologia="+idTipologia);

    				idTipologieSalvateMap.put(idTipologia,"x");
    			}
    		}
    	}	
    	logger.debug(logprefix + " idTipologieSalvateMap="+idTipologieSalvateMap);
    	
    	if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")){
    		// elimino primo carattere ','			
    		String strTmp = elencoIdAllegatiObbligatori.substring(1);
    		String[] arrayIdTipolObbl = strTmp.split(",");
    		int counter = 0;
    		for (int i = 0; i < arrayIdTipolObbl.length; i++) {
    			if(idTipologieSalvateMap.containsKey(arrayIdTipolObbl[i])){
    				logger.info("[DEBUG] " + logprefix + "arrayIdTipolObbl["+i+"]="+arrayIdTipolObbl[i]);
    				counter ++;
    			}
    		}
    		if(counter==arrayIdTipolObbl.length){
    			// se i doc contati sono pari a quelli attesi allora ci sono tutti
    			tuttiAllegatiObbligatoriPresenti = true;
    		}
    	}else{
    		//se non ci sono obbligatori ritorna true
    		tuttiAllegatiObbligatoriPresenti = true;
    	}
    	
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatori END");
    	return tuttiAllegatiObbligatoriPresenti; 

    }
    
    
    /**
     * se il parametro tremina con ,00 viene restituita una stringa priva di quei tre caratteri
     * @param perc
     * @return
     */
    public static  String formattaPercentualePerVis(String perc){
  	  String percFormattata = perc;
  	  if(perc!=null){
  		  if(perc.endsWith(",00")){
  			  percFormattata = perc.substring(0, perc.length()-3);
  		  }
  	  }
  	  return percFormattata;
    }
    

    
    /**
     * 01/01/2019 2R
     * Se un campo contiene una parola senza spazi
     * salvando e stampando in pdf, le tabelle vengono
     * deformate.
     * Per evitare questo, ( esempio: Jira: 1198)
     * viene eseguito il controllo sui campi interessati,
     * chiamando questo metodo, e passando il valore del campo corrente.
     * Se la parola non contiene spazi, e supera i 24 caratteri, restituisce un valore booleano:
     * true : la parola contiene spazi
     * false: la parola non contiene spazi.
     */
    public static boolean validateMessageText(String space)
    {
    	boolean presenteSpace = false;
    	String s = space;
    	for(int i = 0; i < s.length(); i++){
    		if(s.charAt(i) == ' ') {
    			presenteSpace = true;
    		}
    	}
    	return presenteSpace;
    }
    
    public static boolean isBefore(String dataDaComparare, String dataChiusuraSportello, Logger logger)
    {
    	final String OLD_FORMAT = "yyyy-MM-dd";
    	final String NEW_FORMAT = "dd-MM-yyyy";
    	
    	boolean isBefore = false;
    	
    	logger.info("dataChiusuraSportello: " + dataChiusuraSportello);
    	logger.info("dataDaComparare: " + dataDaComparare);
    	
    	String newDataDaComparare;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    	try {
			Date d = sdf.parse(dataDaComparare);
			sdf.applyPattern(NEW_FORMAT);
			newDataDaComparare = sdf.format(d);
			logger.info("newDataDaComparare: " + newDataDaComparare);
			
			Date dateSelezionata = sdf.parse(newDataDaComparare);
	        Date dateChiusura = sdf.parse(dataChiusuraSportello);
	        
	        Calendar newDataChiusura = Calendar.getInstance();
	        newDataChiusura.setTime(dateChiusura);
	        newDataChiusura.add(Calendar.YEAR, -1);
	        
	        Date dataChiusuraSportelloMeno1Anno = newDataChiusura.getTime();
	        logger.info("dateChiusura2: " + dataChiusuraSportelloMeno1Anno); // data chiusura -1 anno
	        
	        if (dateSelezionata.compareTo(dataChiusuraSportelloMeno1Anno) > 0) {
	        	isBefore=false;
	        } else if (dateSelezionata.compareTo(dataChiusuraSportelloMeno1Anno) < 0 || dateSelezionata.compareTo(dataChiusuraSportelloMeno1Anno) == 0) {
	        	isBefore=true;
	        } else {
	        	logger.info("da gestire... ?!?");
	        }
	        
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return isBefore;
    }
    
    
    
    
    /**
     * ritorna una Map con tutte le tipologie intervento (o tipologie-dettaglio intervento) selezionate in caratteristicheProgetto
     * @param caratteristicheProgettoNGVO
     * @param logger
     * @return
     */
    public static Map<String,String> getInterventiSelezionati(CaratteristicheProgettoNGVO caratteristicheProgettoNGVO, Logger logger){ 
    	String logprefix = "[MetodiUtili::getInterventiSelezionati] ";
    	logger.info(logprefix + "BEGIN");
    	Map<String,String> tipolIntervCheckedMap = null; //new HashMap<String,String>();
    	 			   
    	if(caratteristicheProgettoNGVO!=null){
    		TipologiaInterventoVO[] tipologiaInterventoList = caratteristicheProgettoNGVO.getTipologiaInterventoList(); 
    		if(tipologiaInterventoList != null && tipologiaInterventoList.length>0){
    			for(int i=0; i<tipologiaInterventoList.length;i++){
    				TipologiaInterventoVO tipolIntMap = tipologiaInterventoList[i];
    				
    				String checkInt = tipolIntMap.getChecked();		
    				String idTipoIntervento = tipolIntMap.getIdTipoIntervento();
    				logger.debug( logprefix + "codTipoIntervento = "+idTipoIntervento+" check="+checkInt);    				

    				if("true".equals(checkInt)){
    					if(tipolIntervCheckedMap==null){
    						tipolIntervCheckedMap = new HashMap<String,String>();
    						
    					}
    					TipologiaDettaglioInterventoVO[] dettaglioInterventoList = tipolIntMap.getDettaglioInterventoList();
    					if(dettaglioInterventoList!=null && dettaglioInterventoList.length>0){
    						for(int j=0; j<dettaglioInterventoList.length;j++){
    							TipologiaDettaglioInterventoVO dettInterventoMap = dettaglioInterventoList[j];
    							String checkDettInt = dettInterventoMap.getChecked();
    							String idDettIntervento = dettInterventoMap.getIdDettIntervento();						
    							logger.debug( logprefix + "idDettIntervento = "+idDettIntervento+" chekInt="+checkDettInt);
    							if("true".equals(checkDettInt)){
    								tipolIntervCheckedMap.put(idTipoIntervento+"-"+idDettIntervento,"0");
    							}
    						}
    					}else{
    						tipolIntervCheckedMap.put(idTipoIntervento+"-","0");
    					}
    				}
    			}
    		}
    	} 
    	logger.info(logprefix + "END");    	
    	return tipolIntervCheckedMap;
    }
    
    
    
    /**
     * restituisce una List contenente gli idTipologiaIntervento selezionati in caratteristicheProgettoNGVO
     * @param caratteristicheProgettoNGVO
     * @param logger
     * @return
     */                        
    public static List<String> getIdTipoIntSelezionatiList(CaratteristicheProgettoNGVO caratteristicheProgettoNGVO, Logger logger){ 
    	String logprefix = "[MetodiUtili::getIdTipoIntSelezionati] ";
    	logger.info(logprefix + "BEGIN");
    	List<String> tipolIntervCheckedList = null; 
    	 			   
    	if(caratteristicheProgettoNGVO!=null){
    		TipologiaInterventoVO[] tipologiaInterventoList = caratteristicheProgettoNGVO.getTipologiaInterventoList(); 
    		if(tipologiaInterventoList != null && tipologiaInterventoList.length>0){
    			for(int i=0; i<tipologiaInterventoList.length;i++){
    				TipologiaInterventoVO curTipologiaInterventoVO = tipologiaInterventoList[i];
    				
    				if(curTipologiaInterventoVO == null){
    					continue;
    				}
    				
    				String curChecked = curTipologiaInterventoVO.getChecked();
    				logger.info(logprefix + " curChecked: " + curChecked); // true
    				
    				String curIdTipoIntervento = curTipologiaInterventoVO.getIdTipoIntervento();    				   				
    				logger.info(logprefix + " curIdTipoIntervento: " + curIdTipoIntervento); // 87
    				
    				if("true".equals(curChecked)){
    					if(tipolIntervCheckedList==null){
    						tipolIntervCheckedList = new ArrayList<String>();
    					}
    					logger.info( logprefix + "curIdTipoIntervento = "+curIdTipoIntervento+" e' selezionato "); 
    					tipolIntervCheckedList.add(curIdTipoIntervento);    					
    				}
    			}
    		}
    	} 
    	logger.info(logprefix + "END");    	
    	return tipolIntervCheckedList;
    }
    
    
    
    /**
     * ritorna una Map con tutti i dettagli costo presenti in piano spese
     * @param pianoSpeseMap
     * @param logger
     * @return
     */
    public static Map<String,String> getDettaglioCosti(PianoSpeseVO pianoSpeseMap, Logger logger){ 
    	String logprefix = "[MetodiUtili::getDettaglioCosti] ";
    	logger.info(logprefix + "BEGIN");

    	Map<String,String> dettaglioCostiMap = null;     	
    	if(pianoSpeseMap!=null) {
    		DettaglioCostiVO[] dettaglioCostiList = pianoSpeseMap.getDettaglioCostiList();
    		if (dettaglioCostiList != null && dettaglioCostiList.length>0){	
    			dettaglioCostiMap = new HashMap<String,String>();

    			logger.debug( logprefix + "dettaglioCostiList.size="+dettaglioCostiList.length);
    			for(int i=0; i<dettaglioCostiList.length;i++){
    				DettaglioCostiVO detCostoMap=dettaglioCostiList[i]; 
    				if(detCostoMap!=null){
    					String id = detCostoMap.getIdentificativo(); // 3-4 , 2-2 == in dettaglioInterventoList sono idTipoIntervento-idDettIntervento
    					dettaglioCostiMap.put(id,"0");
    				}
    			}
    		}
    	}
    	return dettaglioCostiMap; 
    }    
    
    
    
    /**
     * ritorna false se esiste almeno una chiave in interventiMap non presente in dettaglioCostiMap, true altrimenti
     * @param interventiMap
     * @param dettaglioCostiMap
     * @param logger
     * @return
     */
    public static boolean ctrlInterventiPresentiInDettCosti(Map<String,String> interventiMap, Map<String,String> dettaglioCostiMap, Logger logger){
    	String logprefix = "[MetodiUtili::ctrlInterventiPresentiInDettCosti] ";
    	boolean existInterventoNonInDettCosti = false;
    	if(interventiMap!=null && !interventiMap.isEmpty() && dettaglioCostiMap!=null && !dettaglioCostiMap.isEmpty()){
    		for(String chiave : interventiMap.keySet()){ 
    			if(!dettaglioCostiMap.containsKey(chiave)){
    				logger.warn( logprefix + "Tipologia 'idTipoIntervento-idDettIntervento' = ["+chiave + "] non presente nel dettaglio costi");
    				existInterventoNonInDettCosti = true;
    				break;
    			}
    		}
    	}
    	return existInterventoNonInDettCosti;
    }
    
    
    
    /**
     * ritorna false se esiste almeno una chiave in dettaglioCostiMap non presente in interventiMap, true altrimenti
     * @param dettaglioCostiMap
     * @param interventiMap
     * @param logger
     * @return
     */
    public static boolean ctrlDettCostiPresentiInInterventi( Map<String,String> dettaglioCostiMap,Map<String,String> interventiMap, Logger logger){
    	String logprefix = "[MetodiUtili::ctrlDettCostiPresentiInInterventi] ";
    	boolean existDettCostoNonInInterventi = false;
    	if(dettaglioCostiMap!=null && !dettaglioCostiMap.isEmpty() && interventiMap!=null && !interventiMap.isEmpty()){
    		for(Object chiave : dettaglioCostiMap.keySet()){    			
    			if(!interventiMap.containsKey(chiave)){			
    				logger.debug( logprefix + "Spesa identificativo = ["+chiave + "] non presente nelle Tipologie");
    				existDettCostoNonInInterventi = true;
    				break;
    			}
    		}
    	}
    	return existDettCostoNonInInterventi;
    }
    
    
    
    /**
     * verifica se in tipologiaInterventoList e' stata selezionato l'intervento idTipologiaIntervento e in tal caso ritorna true
     * @param tipologiaInterventoList
     * @param idTipologiaIntervento    
     * @return
     */
    public static boolean controllaSelezioneTipologiaIntervento(TipologiaInterventoVO[] tipologiaInterventoList, String idTipologiaIntervento){
    	boolean esito = false;
    	if(tipologiaInterventoList==null){
    		return esito;
    	}
    	for (TipologiaInterventoVO tipologiaInterventoVO : tipologiaInterventoList) {    		
			 if(tipologiaInterventoVO!=null && tipologiaInterventoVO.getChecked()!=null && tipologiaInterventoVO.getChecked().equalsIgnoreCase("true")){
				 if(tipologiaInterventoVO.getIdTipoIntervento()!=null && tipologiaInterventoVO.getIdTipoIntervento().equals(idTipologiaIntervento)){
					 esito = true;
					 break;
				 }
			 }
    	}
    	return esito;		
    }
    
    
    
    /**
     * verifica se in tipologiaInterventoList e' stata selezionato l'intervento idTipologiaIntervento e in tal caso ritorna true
     * @param tipologiaInterventoList
     * @param idTipologiaIntervento    
     * @return
     */
//    public static boolean controllaSelezioneTipologiaInterventoNeve(TipologiaInterventoNeveVO[] tipologiaInterventoList, String idTipologiaIntervento){
//    	boolean esito = false;
//    	if(tipologiaInterventoList==null){
//    		return esito;
//    	}
//    	for (TipologiaInterventoNeveVO tipologiaInterventoVO : tipologiaInterventoList) {    		
//			 if(tipologiaInterventoVO!=null && tipologiaInterventoVO.getChecked()!=null && tipologiaInterventoVO.getChecked().equalsIgnoreCase("true")){
//				 if(tipologiaInterventoVO.getIdTipoIntervento()!=null && tipologiaInterventoVO.getIdTipoIntervento().equals(idTipologiaIntervento)){
//					 esito = true;
//					 break;
//				 }
//			 }
//    	}
//    	return esito;		
//    }
    
    
    
    /**
     * verifica se in premialitaProgettoItemVOArray e' stata selezionata la premialita' idPremialita e in tal caso ritorna true
     * @param premialitaProgettoItemVOArray
     * @param idPremialita
     * @return
     */
//    public static boolean controllaSelezionePremialita(PremialitaProgettoItemVO[] premialitaProgettoItemVOArray, String idPremialita){
//    	boolean esito = false;
//    	if(premialitaProgettoItemVOArray==null){
//    		return esito;
//    	}
//    	for (PremialitaProgettoItemVO curPremialita : premialitaProgettoItemVOArray) {    		
//			 if(curPremialita!=null && curPremialita.getChecked()!=null && curPremialita.getChecked().equalsIgnoreCase("true")){				 
//				 if( !StringUtils.isBlank(curPremialita.getIdPremialita()) && curPremialita.getIdPremialita().equals(idPremialita)){
//					 esito = true;
//					 break;
//				 }
//			 }
//    	}
//    	return esito;		
//    }
   
    
    
    /**
     * se in messageList il 'campo errore' contenuto in segnalazioneErrore è presente, allora restituisce il messaggio di 
     * segnalazioneErrore prefissato da un a capo html; altrimenti restituisce il messaggio originale; 
     * il carattetere non viene aggiunto se il testo del messaggio e' numerico, perche' in quel caso
     * si tratta dell'id di un record di una tabella e non di un messaggio destinato all'utente 
     * @param messageList
     * @param segnalazioneErrore
     * @return
     */
    public static String prefixErrMsg(List<CommonalityMessage> messageList, SegnalazioneErrore segnalazioneErrore){
    	String retMsg = "";
    	if( segnalazioneErrore!=null){
    		String tmpMsg = segnalazioneErrore.getMsgErrore();
    		if(messageList!=null && !messageList.isEmpty()){ 
    			String campoErrore = segnalazioneErrore.getCampoErrore();
    			for (CommonalityMessage message : messageList) {
    				if(!StringUtils.isBlank(message.getField()) && message.getField().equalsIgnoreCase(campoErrore)){
    					if(!StringUtils.isNumeric(tmpMsg)){  //se e' numerico non e' un messaggio per l'utente, ma l'id di  un record di una tabella
    						tmpMsg = "<br/>" + tmpMsg;
    						
    					}
    				}
    			}    			
    		}
    		retMsg = tmpMsg;
    	}
    	return retMsg;  
    }
    
    
    
    /**
     * Verifico se data risulta corretta
     * @param date
     * @return
     */
    public static boolean validate(final String date) 
	{
		Pattern pattern;
		Matcher matcher;

		final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
		pattern = Pattern.compile(DATE_PATTERN);

		matcher = pattern.matcher(date);

		if (matcher.matches()) {

			matcher.reset();

			if (matcher.find()) {

				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (day.equals("31")
						&& (month.equals("4") || month.equals("6")
								|| month.equals("9") || month.equals("11")
								|| month.equals("04") || month.equals("06") || month
									.equals("09"))) {
					return false; // only 1,3,5,7,8,10,12 has 31 days
				} else if (month.equals("2") || month.equals("02")) {
					// leap year
					if (year % 4 == 0) {
						if (day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					} else {
						if (day.equals("29") || day.equals("30")
								|| day.equals("31")) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

    
    /**
     * restituisce la lista degli id degli eventuali dettagli tipologie agevolazioni (dettaglioAiuto) selezionati
     * @param tipologiaAiutoNGVO
     * @param logger
     * @return
     */
    public static List<String> getIdDettagliAgevolazioneSelezionatiList (TipologiaAiutoNGVO tipologiaAiutoNGVO,Logger logger)
    {
    	String logprefix = "[MetodiUtili::getIdDettagliAgevolazioneSelezionatiList] ";
    	logger.info(logprefix + "BEGIN");
    	List<String> idDettTipolAgevolCheckedList = null; 
    	
    
		if (tipologiaAiutoNGVO!=null && tipologiaAiutoNGVO.getTipologiaAiutoList()!= null){
			List<TipologiaAiutoNGItemListVO> tipologiaAiutoNGItemListVO = new ArrayList<TipologiaAiutoNGItemListVO>(Arrays.asList(tipologiaAiutoNGVO.getTipologiaAiutoList()));
			for(int i=0; i<tipologiaAiutoNGItemListVO.size();i++){
				TipologiaAiutoNGItemListVO curTipologiaAiuto= tipologiaAiutoNGItemListVO.get(i); 
				if(curTipologiaAiuto!=null){
					String curChecked = curTipologiaAiuto.getChecked();
					if(StringUtils.isNotBlank(curChecked) && curChecked.equals("true")){	
						
						if(curTipologiaAiuto.getDettaglioAiutoList()!=null && curTipologiaAiuto.getDettaglioAiutoList().length>0){
							List<DettaglioAiutoNGItemListVO> dettaglioAiutoNGItemListVO = new ArrayList<DettaglioAiutoNGItemListVO>(Arrays.asList(curTipologiaAiuto.getDettaglioAiutoList()));
							for (DettaglioAiutoNGItemListVO curDettaglioAiutoNGItemListVO : dettaglioAiutoNGItemListVO) {
								String curDettChecked = curDettaglioAiutoNGItemListVO.getChecked();
								if(StringUtils.isNotBlank(curDettChecked) && curDettChecked.equals("true")){
									String curIdDettAiuto = curDettaglioAiutoNGItemListVO.getIdDettAiuto();
									if(idDettTipolAgevolCheckedList==null){
										idDettTipolAgevolCheckedList = new ArrayList<>();										
									}
									idDettTipolAgevolCheckedList.add(curIdDettAiuto);																		
								}								
							}
						}						
					}	 
				}
			}
		}
		logger.info(logprefix + "END");
		return idDettTipolAgevolCheckedList;
    }
    
    // ---------------------------------------------------------------- : presnza allegati supplementari per beneficiario - 2r - inizio
    /**
     * Metodo che verifica su tutti i prossimi bandi java,
     * a partire da fine febbraio 2019, esempio: Cinema 2019
     * la presenza o meno del numero di documenti supplementari 
     * obbligatori in riferimento all'idSportello
     * 
     * @param idSportello
     * @param idDomanda
     * @param logger
     * @return
     */
    public static int isPresenteAllegatoSupplementare(Integer idSportello, Integer idDomanda, Logger logger){
    	
    	String logprefix = "[MetodiUtili::isPresenteAllegatoSupplementare] ";
    	logger.info(logprefix + "BEGIN");
    	
    	logger.info(logprefix + "idSportello: " + idSportello);
    	logger.info(logprefix + "idDomanda: " + idDomanda);
    	
    	int numeroDocumentiSupplementari = 0;
    	try {
    		if(idSportello != null && idDomanda != null){
    			numeroDocumentiSupplementari = DocumentazioneNGDAO.getNumeroAllegatiSupplementari(idSportello, idDomanda, logger);
    			logger.info(logprefix + "numeroDocumentiSupplementari vale: " + numeroDocumentiSupplementari);
    			// return numeroDocumentiSupplementari;
    		}
		} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return numeroDocumentiSupplementari;
    }

    
    /**
     * 
     * @param idDomanda
     * @param logger
     * @return
     */
    public static int getTipologiaBeneficiario(Integer idDomanda, Logger logger){
    	
    	String logprefix = "[MetodiUtili::getTipologiaBeneficiario] ";
    	logger.info(logprefix + "BEGIN");
    	
    	logger.info(logprefix + "idDomanda vale: " + idDomanda);
    	
    	
    	int idTipologiaBeneficiario = 0;
    	try {
			idTipologiaBeneficiario = DocumentazioneNGDAO.getIdTipologiaBeneficiario(idDomanda, logger);
			logger.info(logprefix + "idTipologiaBeneficiario vale: " + idTipologiaBeneficiario);
		} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return idTipologiaBeneficiario;
    	
    }
    
    
    
    /** : q1  per sistema neve b1 */
    public static int getTotVociSpesaSNB1(Integer idBando, Integer idCategVoceSpesa, String dataInvio, Logger logger){
    	
    	String logprefix = "[MetodiUtili::getTotVociSpesaSNB1] ";
    	logger.info(logprefix + "BEGIN");
    	
    	logger.info(logprefix + "idBando vale: " + idBando);
    	logger.info(logprefix + "idCategVoceSpesa vale: " + idCategVoceSpesa);
    	logger.info(logprefix + "dataInvio vale: " + dataInvio);
    	
    	
    	int cntVociSpesa = 0;
    	try {
    		cntVociSpesa = CultFormaAgevolazioneBDAO.getTotVociSpesa(idBando, idCategVoceSpesa, dataInvio, logger);
			logger.info(logprefix + "cntVociSpesa vale: " + cntVociSpesa);
			
		} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return cntVociSpesa;
    	
    }
    
    
    
    /** : q3  per sistema neve b1 */
    public static int getTotVociEntrataSNB1(Integer idBando, String dataInvio, Logger logger){
    	
    	String logprefix = "[MetodiUtili::getTotVociSpesaSNB1] ";
    	logger.info(logprefix + "BEGIN");
    	
    	logger.info(logprefix + "idBando vale: " + idBando);
    	logger.info(logprefix + "dataInvio vale: " + dataInvio);
    	
    	
    	int cntVociEntrata = 0;
    	try {
    		cntVociEntrata = CultFormaAgevolazioneBDAO.getTotVociEntrata(idBando, dataInvio, logger);
			logger.info(logprefix + "cntVociSpesa vale: " + cntVociEntrata);
			
		} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return cntVociEntrata;
    	
    }
    
    /***
     * 
     * @param idSportello
     * @param idTipologiaBeneficiario
     * @param logger
     * @return
     */
    public static List<TipologiaAllegatoVO> getElencoDocumentiObbligatoriSupplementari(Integer idSportello, Integer idTipologiaBeneficiario, Logger logger){
		
    	String logprefix = "[MetodiUtili::getElencoDocumentiObbligatoriSupplementari] ";
    	logger.info(logprefix + "BEGIN");
    	
    	List<TipologiaAllegatoVO> documentiObbligatoriSupplementariList = new ArrayList();
    	
    	try {
    		
    		if ( idSportello != null && idTipologiaBeneficiario!=null ) {
				documentiObbligatoriSupplementariList = DocumentazioneNGDAO.getTipologiaAllegatoSupplementareList(idSportello, idTipologiaBeneficiario, logger);
			}else{
				logger.info(logprefix + "idSportello: " + idSportello);
				logger.info(logprefix + "idTipologiaBeneficiario: " + idTipologiaBeneficiario);
			}
			
		} catch (CommonalityException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
    	return documentiObbligatoriSupplementariList;
    	
    }
    
    
    /***
     * 
     * @param info
     * @param logprefix
     * @param logger
     * @return
     */
//    public static Integer getIdBando(FinCommonInfo info, String logprefix, Logger logger){
//    	
//    	logger.info(logprefix + "BEGIN");
//    	
//    	Integer idBando = null;
//    	
//    	if (info.getStatusInfo() != null) {
//			idBando = info.getStatusInfo().getTemplateId();
//		}
//		return idBando;
//    	
//    }
    
    
    
    /**
     * Metodo che restituisce elenco di 
     * documenti supplementari in riferimento 
     * ad eventuale nuovo idSportello
     * 
     * @param idBando
     * @param idSportello
     * @param idDomanda
     * @param isAllegatiIntegrativiPresenti
     * @param logger
     * @return
     */
    public static List<TipologiaAllegatoVO> getElencoDocSupplByIdSportello(Integer idBando, Integer idSportello, Integer idDomanda, boolean isAllegatiIntegrativiPresenti, Logger logger)
    {
    	List<TipologiaAllegatoVO> elencoAllegatiIntegrativi = new ArrayList<>();
    	try {
			if (idBando != null && idSportello!=null && idDomanda != null) {
				elencoAllegatiIntegrativi = DocumentazioneNGDAO.getTipologiaAllegatoList(idBando, idSportello, idDomanda, isAllegatiIntegrativiPresenti, logger);
			}
    	} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return elencoAllegatiIntegrativi;
    }
    
    
    
    
    /**
     * Posizione del separatore punto:
     * 
     * */
    public static int posizionePunto (String s)
	{
		int pos = -1;
		if(s!=null && !s.isEmpty()){
			pos = s.indexOf('.');
		}
		
		return pos;
	}
    
    
    
    
    /**
     * Restituisco 
     * una stringa con formato ##.00
     */
    public static String getStringaConDoppioZero (String s, int pos, Logger logger)
	{
		String ris = "";
		if(s!=null && !s.isEmpty())
		{
			ris = s.substring(0,pos)+".00";
			logger.info("ris vale: " + ris);
		}
		
		return ris;
	}
    
    
    
    
    /**
     * 
     * @param inputVal
     * @param logger
     * @return
     */
    public static boolean confrontaDecimaliUguali(String inputVal, Logger logger)
    {
    	boolean isValidNumber = false;
    	int posPunto = 0;
    	String inputValueTemp = "";
    	
    	if(!inputVal.isEmpty() && inputVal!=null)
    	{
    		// verifico se contiene virgola
        	if(inputVal.indexOf(",")!=-1)
        	{
        		logger.info("Contiene la virgola!");
        		// eseguo replace , to .
        		inputValueTemp = inputVal.replace(",", ".");
        		
        		int len = inputVal.length();
        		logger.info("lunghezza numero: " + len);
        		posPunto = MetodiUtili.posizionePunto(inputValueTemp);
        		logger.info("Contiene punto alla posizione: " + posPunto);
        		
        		if(posPunto!=-1)
      	        {
        			inputValueTemp = MetodiUtili.getStringaConDoppioZero(inputVal, posPunto, logger); // valore da comparare 2: 5000.00
        			
        			logger.info("inputValueTemp: " + inputValueTemp); // 12.00
        			
        			/* Valore originale, con soli i primi 2 decimali...*/
        		    inputVal = (inputVal.substring(0,posPunto+(len-posPunto)));
        		    inputVal = inputVal.replace(",", "."); // 12.56
        		    logger.info("inputVal: " + inputVal );
        		    
        		    // eseguo confronto decimali:
        		    isValidNumber = MetodiUtili.confrontaDecimali(inputVal, inputValueTemp);
        		    logger.info("isValidNumber: " + isValidNumber);
        			
        			if(isValidNumber){
        				logger.info("Puoi salvare!");
        				isValidNumber = true;
        			}else{
        				logger.info("NON puoi salvare!");
        				isValidNumber = false;
        			}
      	        }
        	}else
        		if(inputVal.indexOf(".")!=-1)
        		{
        			logger.info("Contiene il punto!");
        			
        			posPunto = MetodiUtili.posizionePunto(inputVal);
            		logger.info("Contiene punto alla posizione: " + posPunto); // pos.: 4
            		
            		if(posPunto!=-1)
          	        {
            			inputValueTemp = MetodiUtili.getStringaConDoppioZero(inputVal, posPunto, logger); // valore da comparare 2
            			logger.info("inputValueTemp: " + inputValueTemp); // 
            			
            		    // eseguo confronto decimali:
            		    isValidNumber = MetodiUtili.confrontaDecimali(inputVal, inputValueTemp);
            		    logger.info("isValidNumber: " + isValidNumber);
            			
            			if(isValidNumber){
            				logger.info("Puoi salvare!");
            				isValidNumber = true;
            			}else{
            				logger.info("NON puoi salvare!");
            				isValidNumber = false;
            			}
          	        }
        			
        		}
		  }
    	return isValidNumber;
    }
    
    
    
    
    /**
     * Confronto di decimali a 2 posizioni
     */
    public static boolean confrontaDecimali(String valoreOriginale, String valoreSenzaDecimali ){
		boolean ris = false;
		
		if(valoreOriginale.isEmpty() || valoreSenzaDecimali.isEmpty()){
			return false;
		}
		
		if(valoreOriginale.equals(valoreSenzaDecimali))
		{
			ris= true;
		}
		
		return ris;
	}
    
    
    
    
    /**
     * Verifica decimali a zero
     */
//    public static boolean isDecimaliZero(BigDecimal valoreOriginale)
//    {
//		boolean ris = false;
//		String bdStr = valoreOriginale.toString();
//		
//		int pos = posizionePunto(bdStr);
//		
//		String decimali = "";
//		String decimaliTmp = "";
//		
//		if(pos !=-1){
//			decimali = bdStr.substring(pos, (pos+3)); // 03
//			decimaliTmp = "00";
//			
//			// confronta
//			if(confrontaDecimali(decimali, decimaliTmp)){
//				ris= true;
//			}
//		}
//		
//		return ris;
//	}
    
    
    
    
    /**
     * Calcola BigDecimal arrotondato
     * @return 
     * 
     */
    public static BigDecimal roundBD(String val){ // 130709.50
		BigDecimal Output= new BigDecimal(val).setScale(0, RoundingMode.HALF_UP);
		return Output.setScale(2);
	}
    
    public static String roundBDToString(String val){
		BigDecimal Output= new BigDecimal(val).setScale(0, RoundingMode.HALF_UP);
		return Output.setScale(2).toString().replace(",", ".");
	}
    
    
    
    
    /**
     * Verifica presenza numero 
     * con al massimo 2 decimali
     * separati da virgola.
     * 
     * @param string
     * @return
     */
//    public static boolean isNumberWith2Decimals(String string) 
//    {
//    	return string.matches("^\\d+\\,\\d{2}$");
//    }
    
    
    
    
    /**
     * Verifico validita
     * numero decimale separato solo da virgola
     * 
     * @param source
     * @return
     */
    public static boolean containNumbersOnly(String source)// 90.01
    {
        boolean result = false;
        Pattern pattern = Pattern.compile("[0-9]"); //correct pattern for both float and integer.
        pattern = Pattern.compile("\\d+"); //correct pattern for both float and integer.

        result = pattern.matcher(source).matches();
        return result;
    }
    
    
    
    
    /**
     * Metodo per confrontare 2 date:
     * 
     * @param data1
     * @param data2
     * @return
     */
    public static int confrontaDate(Date data1, Date data2) 
	{
		int ret = data1.compareTo(data2);
		
		if (ret > 0) {
			return ret;
		}
		else if (ret < 0) {
			return ret;
		}
		else {
			return 0;
		}
	}
    
    
    
    
    /**
	 * Verifico formato data corretta
	 * @param date
	 * @return
	 */
	public static boolean isDateValid(String date) 
	{
		final String DATE_FORMAT = "dd/MM/yyyy";

        try 
        {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        }
        catch (ParseException e) {
            return false;
        }
	}
	
	
	
	
	/**
	 * recupera le unita' organizzative (denominate anche dipartimenti o codici ufficio) eventualmente associate al codice fiscale passato.
	 * La ricerca avviene prima sul server IPA, e se non vengono trovate unita' organizzative viene fatto un secondo tentativo sul DB di findom
	 * @param info
	 * @param codiceFiscale
	 * @param cercaSuIpa booleano ; se true viene effettuata la ricerca della unita organizzativa su Ipa e poi eventualmente su
	 *     findom_d_dipartimenti; se false solo su findom_d_dipartimenti
	 * @param logger
	 * @return
	 * @throws Exception
	 */
	public static List<DipartimentoVO> cercaUnitaOrganizzative(FinCommonInfo info, String codiceFiscale, boolean cercaSuIpa, Logger logger) throws Exception {
		logger.info("[MetodiUtili::cercaUnitaOrganizzative] BEGIN ");
		String nomeUOInSessione = "unitaOrganizzativeMap";
		List<DipartimentoVO> dipartimentoList = null;
		if( SessionCache.getInstance().get(nomeUOInSessione)!=null )
		{
			//per evitare il rischio di considerare le UO di un altro beneficiario (per mancata pulizia della sessione),
			//in sessione metto una Map le cui entry sono coppie chiave (un codice fiscale) - valore (lista delle UO associate al codice fiscale)
			//Quando si tratta di cercare le UO per un certo CF viene chiamato questo metodo che verifica se la mappa in sessione contiene
			//una lista di UO relativa al CF passato; se c'e' la uso, altrimenti la calcolo e poi la metto in sessione nella Map. 
			//La Map in sessione viene resettata e all'uscita del metodo avra' solo la entry relativa al codice fiscale passato nel parametro del metodo.
			//Questo metodo e' il solo punto dove uso questo oggetto in sessione, per cui dovrebbe essere evitato l'utilizzo di UO non associate al codice fiscale passato
			Map<String, List<DipartimentoVO>> unitaOrganizzativeMap = (Map) SessionCache.getInstance().get(nomeUOInSessione);
			logger.info("[MetodiUtili::cercaUnitaOrganizzative] unitaOrganizzativeMap= "+unitaOrganizzativeMap);
			
			if(unitaOrganizzativeMap!=null && unitaOrganizzativeMap.get(codiceFiscale)!=null){
				dipartimentoList = unitaOrganizzativeMap.get(codiceFiscale);
				logger.info("[MetodiUtili::cercaUnitaOrganizzative] dipartimentoList ottenuto da sessione ha: " + (dipartimentoList==null ? "0" : dipartimentoList.size()) + " elementi");
				
				SessionCache.getInstance().set(nomeUOInSessione, null); 
				Map<String, List<DipartimentoVO>> mapPerCFCorrente = new HashMap<>();
				mapPerCFCorrente.put(codiceFiscale,dipartimentoList);
				SessionCache.getInstance().set(nomeUOInSessione, mapPerCFCorrente);
				logger.info("[MetodiUtili::cercaUnitaOrganizzative] END ");
				return dipartimentoList;
			}
		}
		
		//  : Pochettino : ma la ricerca su IPA non ha senso solo per gli enti di ricerca e simili?????
		// qui cerca su IPA per chiunque azienda
		//if(datiIpa!=null){
		if(cercaSuIpa){
			//07/2019: per ottimizzare (limitatamente alla pag del beneficiario) si decide di effettuare la ricerca su ipa solo se la ricerca su IPA 
			//del codice ipa fatta nel guscio e messa in sessione e' valorizzata (se questo si verifica il parametro cercaSuIpa passato vale true).
			//nella gestione partner il parametro vale sempre true, perche' il cf del partner e' diverso dal cf del beneficiario (che e' quello 
			//che potrebbe avere un codice ipa associato) 
			dipartimentoList = IpaLdap.cercaDipartimentiIpa(codiceFiscale, logger);
		}
		logger.info("[MetodiUtili::cercaUnitaOrganizzative] dipartimentoList ottenuto da IpaDAO.cercaDipartimentiIpa ha: " + (dipartimentoList==null ? "0" : dipartimentoList.size()) + " elementi");
		if(dipartimentoList == null || dipartimentoList.isEmpty()){
			//se non trovato le unita' organizzative su IPA, cerco su findom_d_enti_strutturati
			String idEnteStrutt = EnteDAO.getIdEnteStrutturato(codiceFiscale);
			if(idEnteStrutt!=null){
				logger.info("[MetodiUtili::cercaUnitaOrganizzative]  idEnteStrutt non nullo " );
				dipartimentoList = DipartimentoDAO.getDipartimentiList(idEnteStrutt);	
				logger.info("[MetodiUtili::cercaUnitaOrganizzative] dipartimentoList ottenuto da findom_d_enti_strutturati ha: " + (dipartimentoList==null ? "0" : dipartimentoList.size()) + " elementi");
			}
		}
		if(dipartimentoList==null){
			dipartimentoList = new ArrayList<>();
		}else{
			DipartimentoVO dipNoScelta= new DipartimentoVO();
			dipNoScelta.setId(0L);
			dipNoScelta.setCodice("-1");
			dipNoScelta.setDescrizione("Nessuna Unità organizzativa");
			dipartimentoList.add(0, dipNoScelta);
		}
		//resetto la Map in sessione e la popolo con la solo entry relativa al codice fiscale passato al parametro
		SessionCache.getInstance().set(nomeUOInSessione, null); 
		Map<String, List<DipartimentoVO>> mapPerCFCorrente = new HashMap<>();
		mapPerCFCorrente.put(codiceFiscale,dipartimentoList);
		SessionCache.getInstance().set(nomeUOInSessione, mapPerCFCorrente);
		
		logger.info("[MetodiUtili::cercaUnitaOrganizzative] END ");
		return dipartimentoList;
	}
	
	
	
	
	// INIZIO METODI PER LA GESTIONE CAPOFILA PARTNER 	
	/**
	 * ritorna true se il bando corrente è di tipo progetto comune, false altrimenti
	 * @param info
	 * @return
	 */
//	public static boolean verificaSeBandoDiTipoProgettoComune(FinCommonInfo info){		
//		if(info.getStatusInfo() != null && Objects.toString(info.getStatusInfo().getFlagProgettiComuni(), "false").equals("true")){			
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * ritorna l'id del ruolo del beneficiario nel progetto comune, o stringa vuota se il ruolo non e' 
	 * ancora stato definito nella domanda corrente
	 * @param ruoloProgettiComuniVO
	 * @return
	 */
	public static String trovaRuoloBeneficiarioInProgettoComune(RuoloProgettiComuniVO ruoloProgettiComuniVO){
		String ruolo = "";
		if(ruoloProgettiComuniVO!=null){
			String ruoloBeneficiario = Objects.toString(ruoloProgettiComuniVO.getRuolo(), "");
			ruolo = ruoloBeneficiario;
		}
		return ruolo;
	}
	
	/**
	 * se il ruolo del beneficiario e' partner, verifica che il capofila non abbia modificato il titolo o l'acronimo del progetto comune
	 * @param domandaNGVO
	 * @param ruoloProgettiComuniVO
	 * @param abstractProgettoCPVO
	 * @param info
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static String verificaVariazioniTitoloAcronimoProgettoComune(DomandaNGVO domandaNGVO,
			                                                              RuoloProgettiComuniVO ruoloProgettiComuniVO, 
			                                                              AbstractProgettoCPVO abstractProgettoCPVO,
			                                                              FinCommonInfo info,
			                                                              Logger logger) throws CommonalityException{
		String logprefix = "[MetodiUtili::verificaVariazioniTitoloAcronimoProgettoComune] ";
		logger.info(logprefix + "  BEGIN");
		String retMsg = "";		
		boolean progettoComune = false;
		
		String ERRMSG_ACRONIMO_TITOLO_CAMBIATI = "Progetto / Abstract progetto: L'acronimo '%s' o il titolo '%s' del progetto comune sono stati modificati dal Capofila '%s - %s'. E' necessario selezionarli nuovamente e salvare ";
		//verifico se la domanda e' di un bando di tipo "progetto comune" 
		if(domandaNGVO!=null){
			String flagProgettiComuni = domandaNGVO.getFlagProgettiComuni();
			if(StringUtils.isNotBlank(flagProgettiComuni) && flagProgettiComuni.equalsIgnoreCase("true")){
				progettoComune = true;
			}
		}

		if(progettoComune){
			//verifico se il ruolo del beneficiario nel progetto comune e' "partner"
			if(ruoloProgettiComuniVO!=null && StringUtils.isNotBlank(ruoloProgettiComuniVO.getRuolo())){				
				if(ruoloProgettiComuniVO.getRuolo().equals(Constants.ID_RUOLO_PARTNER_PRG_COMUNE)){					
					//ottengo da abstractProgettoCP della domanda corrente l'idAcronimoBando e lo uso per accedere a shell_t_acronimo_bandi					
					if(abstractProgettoCPVO!=null){						 
						String idAcronimoBando = abstractProgettoCPVO.getIdAcronimoBando();
						AcronimoBandiVO acronimoBandiVO = PartnerDAO.getAcronimoBandoByIdAcronimoBando(idAcronimoBando, logger);
						if(acronimoBandiVO!=null){
							if(StringUtils.isNotBlank(acronimoBandiVO.getDtDisattivazione())){
								//Il record su shell_t_acronimo_bandi è disattivato, quindi acronimo e/o titolo sono cambiati
								String idCapofilaAcronimoUltimoSalvato = abstractProgettoCPVO.getIdCapofilaAcronimo();								
								String acronimoProgettoUltimoSalvato = abstractProgettoCPVO.getAcronimoProgetto();
								String titoloProgettoUltimoSalvato = abstractProgettoCPVO.getTitolo();
								logger.info(logprefix + " data disattivazione valorizzata, quindi acronimo e/o titolo sono cambiati ");
								OperatorePresentatoreVo datiCapofila = PartnerDAO.getDatiCapofilaByIdCapofilaAcronimo(idCapofilaAcronimoUltimoSalvato, logger);
								
								String cfCapofila = (StringUtils.isNotBlank(datiCapofila.getCodiceFiscale())) ? datiCapofila.getCodiceFiscale() : "";
								String denomCapofila = (StringUtils.isNotBlank(datiCapofila.getDenominazione())) ? datiCapofila.getDenominazione() : "";
								
								retMsg = ERRMSG_ACRONIMO_TITOLO_CAMBIATI;
								retMsg = String.format(ERRMSG_ACRONIMO_TITOLO_CAMBIATI,acronimoProgettoUltimoSalvato,titoloProgettoUltimoSalvato,cfCapofila,denomCapofila);
							}
						}
					}
				}
			}
		}
		logger.info(logprefix + "  END");
		return retMsg;
	}
	
	/**
	 * se il ruolo del beneficario e' capofila, il metodo controlla che tutte le domande dei suoi partner siano in stato inviato;
	 * il metodo ritorna una segnalazione solo se la domanda contiene gia' i dati della gestione partner; se non ci sono non segnala errore, tanto 
	 * l'obbligatorieta' della gestione partner viene rilevata e segnalata da altri controlli
	 * @param domandaNGVO
	 * @param ruoloProgettiComuniVO
	 * @param abstractProgettoCPVO
	 * @param info
	 * @param logger
	 * @return l'eventuale messaggio di errore; la stringa vuota se la domanda non e' relativa ad un progetto comune, o non appartiene ad un capofila,
	 * o tutti i partner hanno la propria domanda in stato INVIATA 
	 * @throws CommonalityException
	 */
	public static String verificaStatoDomandePartnerProgettoComune(DomandaNGVO domandaNGVO,
			RuoloProgettiComuniVO ruoloProgettiComuniVO, 
			AbstractProgettoCPVO abstractProgettoCPVO,
			FinCommonInfo info,
			Logger logger) throws CommonalityException{
		String logprefix = "[MetodiUtili::verificaStatoDomandePartnerProgettoComune] ";
		logger.info(logprefix + "  BEGIN");  
		
		String retMsg = "";		
		boolean progettoComune = false;
		
		//verifico se la domanda  e' di un bando di tipo "progetto comune" 
		if(domandaNGVO!=null){
			String flagProgettiComuni = domandaNGVO.getFlagProgettiComuni();
			if(StringUtils.isNotBlank(flagProgettiComuni) && flagProgettiComuni.equalsIgnoreCase("true")){
				progettoComune = true;
			}
		}
		if(progettoComune){
			//verifico se il ruolo del beneficiario nel progetto comune e' "capofila"
			if(ruoloProgettiComuniVO!=null && StringUtils.isNotBlank(ruoloProgettiComuniVO.getRuolo())){				
				if(ruoloProgettiComuniVO.getRuolo().equals(Constants.ID_RUOLO_CAPOFILA_PRG_COMUNE)){					
					//ottengo da abstractProgettoCP della domanda corrente l'idAcronimoBando e lo uso per accedere a shell_t_acronimo_bandi					
					if(abstractProgettoCPVO!=null){						 
						String idCapofilaAcronimo = abstractProgettoCPVO.getIdCapofilaAcronimo();						
						ArrayList<CapofilaAcronimoPartnerVO> capVOList = PartnerDAO.getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(idCapofilaAcronimo, logger);						
						if(capVOList!=null && !capVOList.isEmpty()){
							StringBuilder errMsg = new StringBuilder("");
							for (CapofilaAcronimoPartnerVO capofilaAcronimoPartnerVO : capVOList) {								
								boolean daSegnalare = false;
								String datiPartner = "";
								String datiDomanda = "";
								if(StringUtils.isBlank(capofilaAcronimoPartnerVO.getDtDisattivazione())){
									String idPartner = capofilaAcronimoPartnerVO.getIdPartner();
									String idDomandaPartner = capofilaAcronimoPartnerVO.getIdDomandaPartner();
									
									if(StringUtils.isBlank(idDomandaPartner)){										
										daSegnalare = true;
										datiDomanda = " - domanda non ancora associata al progetto comune ";
										logger.info(logprefix + " trovato idDomandaPartner null per partner " + idPartner ); 
										
										
									}else{
										StatoDomandaVO statoDomandaVOPartner = PartnerDAO.getStatoDomanda(idDomandaPartner, logger);
										if(!statoDomandaVOPartner.getIdStatoDomanda().equals(Constants.STATO_DOMANDA_INVIATA)){
											daSegnalare = true;	
											datiDomanda = " - domanda "+idDomandaPartner + " - "+ statoDomandaVOPartner.getDescrStatoDomanda();
											
											logger.info(logprefix + "  trovato la domanda " + idDomandaPartner + "in stato non inviato, ma : " +statoDomandaVOPartner.getDescrStatoDomanda() ); 
										}
									}
									if(daSegnalare){
										 
										PartnerItemVO partnerItemVO = PartnerDAO.getPartnerByIdPartner(idPartner, logger);
										datiPartner = " partner "+ partnerItemVO.getCodiceFiscale() + " - " + partnerItemVO.getDenominazione();
										logger.info(logprefix + "  trovato un caso da segnalare : " + datiPartner + " - " + datiDomanda); 
										errMsg.append (datiPartner + datiDomanda + "<br>");
									}
								}
							}
							if(errMsg.length() > 0){
								logger.info(logprefix + " errMsg.length() > 0" ); 
								//uso come separatore # per fare in modo che il messaggio sia trattato in modo particolare nella pagina di visualizzazione anomalie
								retMsg = "Controlli sul progetto comune # I seguenti partner non hanno ancora inviato la propria domanda <br>" + errMsg.toString();
							}
						}
					}  
				}
			}
		}
		logger.info(logprefix + "  END"); 
		return retMsg;
	}
	
	
	
	
    /**
     * controlla che lo stato delle domande dei partner eventualmente presenti nell'xml sia aggiornato; se non lo fosse (succede se 
     * l'utente non ha fatto accesso a gestione partner dopo un qualsiasi cambio di stato di almeno una domanda dei partner) devo segnalare errore,
     * altrimenti potrebbe succedere che il globalValidate() non da' errore, per cui l'utente conclude e/o invia la domanda, ma la stampa, 
     * che ha l'xml come sorgente dati, riporteebbe uno stato errato di almeno una domanda di un partner 
     * @param domandaNGVO
     * @param ruoloProgettiComuniVO
     * @param abstractProgettoCPVO
     * @param partnerVO
     * @param info
     * @param logger
     * @return
     * @throws CommonalityException
     */
	public static String verificaStatoDomandePartnerAggiornatoInXml(DomandaNGVO domandaNGVO,
			RuoloProgettiComuniVO ruoloProgettiComuniVO, 
			AbstractProgettoCPVO abstractProgettoCPVO,PartnerVO partnerVO,
			FinCommonInfo info,
			Logger logger) throws CommonalityException{
		String logprefix = "[MetodiUtili::verificaStatoDomandePartnerAggiornatoInXml] ";
		logger.info(logprefix + "  BEGIN");  

		String retMsg = "";
		boolean progettoComune = false;
		String ERRMSG_STATI_DOMANDE_PARTNER_NON_AGGIORNATI = "Progetto / Gestione Partner: Lo stato delle domande dei partner salvato nella presente domanda non risulta aggiornato. E' necessario accedere in sola visualizzazione alla Gestione Partner affinché lo stato si aggiorni automaticamente";
		//verifico se la domanda  e' di un bando di tipo "progetto comune" 
		if(domandaNGVO!=null){
			String flagProgettiComuni = (String)domandaNGVO.getFlagProgettiComuni();
			if(StringUtils.isNotBlank(flagProgettiComuni) && flagProgettiComuni.equalsIgnoreCase("true")){
				progettoComune = true;
			}
		}
		if(!progettoComune){
			return retMsg;
		}
		//verifico se il ruolo del beneficiario nel progetto comune e' "capofila"
		if(ruoloProgettiComuniVO==null || StringUtils.isBlank(ruoloProgettiComuniVO.getRuolo()) ||				
				!ruoloProgettiComuniVO.getRuolo().equals(Constants.ID_RUOLO_CAPOFILA_PRG_COMUNE)){
			return retMsg;
		}
		//ottengo da abstractProgettoCP della domanda corrente l'idAcronimoBando e lo uso per accedere a shell_t_acronimo_bandi					
		if(abstractProgettoCPVO==null){
			return retMsg;
		}						 
		String idCapofilaAcronimo = abstractProgettoCPVO.getIdCapofilaAcronimo();						
		ArrayList<CapofilaAcronimoPartnerVO> capVOList = PartnerDAO.getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(idCapofilaAcronimo, logger);						
		if(capVOList==null || capVOList.isEmpty()){
			return retMsg;
		}
		boolean trovatoUnoStatoNonAggiornato = false;
		for (CapofilaAcronimoPartnerVO capofilaAcronimoPartnerVO : capVOList) {								
			if(StringUtils.isBlank(capofilaAcronimoPartnerVO.getDtDisattivazione())){
				String idPartner = capofilaAcronimoPartnerVO.getIdPartner();
				String idDomandaPartner = capofilaAcronimoPartnerVO.getIdDomandaPartner();

				if(StringUtils.isNotBlank(idDomandaPartner)){		
					//leggo dal DB lo stato reale della domanda del partner
					StatoDomandaVO statoDomandaVOPartner = PartnerDAO.getStatoDomanda(idDomandaPartner, logger);

					//cerco il partner corrente nell'xml 
					if(partnerVO != null && partnerVO.getPartnerItemVOArray() != null && 
							partnerVO.getPartnerItemVOArray().length!=0	){
						PartnerItemVO[] xmlPartnerList = partnerVO.getPartnerItemVOArray();
						for (int i = 0; i < xmlPartnerList.length; i++){
							PartnerItemVO curXmlPartnerItemVO = xmlPartnerList[i];
							if(curXmlPartnerItemVO!=null && StringUtils.isNotBlank(curXmlPartnerItemVO.getIdPartner())){
								if(curXmlPartnerItemVO.getIdPartner().equals(idPartner)){
									//leggo dall'xml lo stato della domanda del partner e lo confronto con lo stato reale
									String curIdStatoDomandaPartnerInXml = Objects.toString(curXmlPartnerItemVO.getIdStatoDomandaPartner(), "");									
									String idStatoDomandaPartnerReale = Objects.toString(statoDomandaVOPartner.getIdStatoDomanda(), "");
									if(!curIdStatoDomandaPartnerInXml.equals(idStatoDomandaPartnerReale)){
							
										logger.info(logprefix + "  trovato uno stato domanda non aggiornato" );
										trovatoUnoStatoNonAggiornato = true;
										break;
									}
								}
							}												
						}						
						if(trovatoUnoStatoNonAggiornato){
							retMsg = ERRMSG_STATI_DOMANDE_PARTNER_NON_AGGIORNATI;
							break;
						}						
					}
				}
			}
		}
		logger.info(logprefix + "  END"); 
		return retMsg;
	}

	
	/***
	 * 
	 * @param valoreDaConvertireInBigDecimal
	 * @param logger
	 * @return
	 */
	public static BigDecimal stringToBigDecimal(String valoreDaConvertireInBigDecimal, Logger logger) {
		
		BigDecimal bd = new BigDecimal(valoreDaConvertireInBigDecimal);
		logger.info(bd);
		
		
		return bd;
	}



	/***
	 * 
	 * @param primoValore
	 * @param secondoValore
	 * @param logger
	 * @return
	 */
	public static boolean isGreater(String primoValore, String secondoValore, Logger logger) 
	{
			 boolean isMaggioreDi = false;
			 
			 /** verifico valore argomenti */
			 logger.info("arg1: primoValore vale: " + primoValore);
			 logger.info("arg2: secondoValore vale: " + secondoValore);
			 
			 if(primoValore.contains(",")){
				 primoValore = primoValore.replace(",", ".");
				 logger.info("primoValore vale: " + primoValore);
			 }
			 
			 if(secondoValore.contains(",")){
				 secondoValore = secondoValore.replace(",", ".");
				 logger.info("secondoValore vale: " + secondoValore);
			 }
			 
			 /**
			  * Convert StringToBigDecimal
			  * 
			  * */
			 BigDecimal primoValoreBD = new BigDecimal("0.00");
			 primoValoreBD = MetodiUtili.stringToBigDecimal(primoValore, logger);
			 logger.info("primoValoreBD vale: " + primoValoreBD);
			 
			 BigDecimal secondoValoreBD = new BigDecimal("0.00");
			 secondoValoreBD = MetodiUtili.stringToBigDecimal(secondoValore, logger);
			 logger.info("secondoValoreBD vale: " + secondoValoreBD);
			 
			 /**
			  * Eseguo il compareTo
			  * per il maggiore di...
			  * 
			  * */
			 if (primoValoreBD.compareTo(secondoValoreBD) == 0) { 
		            logger.info(primoValoreBD + " e " + secondoValoreBD + " sono uguali."); 
		            isMaggioreDi = false;
		        } 
		        else if (primoValoreBD.compareTo(secondoValoreBD) == 1) { 
		        	logger.info(primoValoreBD + " risulta maggiore di " + secondoValoreBD + "."); 
		            isMaggioreDi = true;
		        } 
		        else { 
		        	logger.info(primoValoreBD + " risulta minore di " + secondoValoreBD + ".");
		            isMaggioreDi = false;
		        }
			return isMaggioreDi; 
	}



	/**
	 * Verifica il minore fra 2 valori tipo String
	 * convertendo in BigDecimal
	 * e restituento un boolean.
	 * 
	 * @param primoValore
	 * @param secondoValore
	 * @param logger
	 * @return
	 */
	public static boolean isMinoreUguale(String primoValore, String secondoValore, Logger logger) {
		
		
		boolean isMinoreDi = false;
		 
		 /** verifico valore argomenti */
		 logger.info("arg1: primoValore vale: " + primoValore);
		 logger.info("arg2: secondoValore vale: " + secondoValore);
		 
		 if(primoValore.contains(",")){
			 primoValore = primoValore.replace(",", ".");
			 logger.info("primoValore vale: " + primoValore);
		 }
		 
		 if(secondoValore.contains(",")){
			 secondoValore = secondoValore.replace(",", ".");
			 logger.info("secondoValore vale: " + secondoValore);
		 }
		 
		 /**
		  * Convert StringToBigDecimal
		  * 
		  * */
		 BigDecimal primoValoreBD = new BigDecimal("0.00");
		 primoValoreBD = MetodiUtili.stringToBigDecimal(primoValore, logger);
		 logger.info("primoValoreBD vale: " + primoValoreBD);
		 
		 BigDecimal secondoValoreBD = new BigDecimal("0.00");
		 secondoValoreBD = MetodiUtili.stringToBigDecimal(secondoValore, logger);
		 logger.info("secondoValoreBD vale: " + secondoValoreBD);
		 
		 /**
		  * Eseguo il compareTo
		  * per il minore di...
		  * 
		  * */
		 if (primoValoreBD.compareTo(secondoValoreBD) == 0) { 
	            logger.info(primoValoreBD + " e " + secondoValoreBD + " sono uguali."); 
	            isMinoreDi = true;
	        } 
	        else if (primoValoreBD.compareTo(secondoValoreBD) == 1) { 
	        	logger.info(primoValoreBD + " risulta maggiore di " + secondoValoreBD + "."); 
	        	isMinoreDi = false;
	        } 
	        else { 
	        	logger.info(primoValoreBD + " risulta minore di " + secondoValoreBD + ".");
	        	isMinoreDi = true;
	        }
		return isMinoreDi;
		
		
	}
	
	//FINE METODI PER LA GESTIONE CAPOFILA PARTNER
	
	
	
	/** Jira 1671: -2R inizio **/
	public static String recuperoImportoMinErogabileByIdBando(String idBando, String idFormaFinanziamento, Logger logger) {
		
	    String ris = "";
	    
		try {
			if(idBando != null && idFormaFinanziamento != null) {
				ris = FormaFinanziamentoDAO.getImpMinErogErreBandi(idBando, idFormaFinanziamento, logger);
				logger.debug("Debug importoMinErogabile risulta: ***" + ris); 
			}
		} catch (CommonalityException e) {
			e.printStackTrace();
		}
		return ris;
	}
	
	/***
	 * 
	 * @param idBando
	 * @param idFormaFinanziamento
	 * @param logger
	 * @return
	 */
	public static String recuperoImportoMaxErogabileByIdBando(String idBando, String idFormaFinanziamento, Logger logger) {
		
		String ris = "";
	    
		try {
			if(idBando != null && idFormaFinanziamento != null) {
				ris = FormaFinanziamentoDAO.getImpMaxErogErreBandi(idBando, idFormaFinanziamento, logger);
				logger.info("Debug importoMaxErogabile risulta: ***" + ris); // 500000.00|40000.00
			}
			
		} catch (CommonalityException e2) {
			e2.printStackTrace();
		}
		return ris;
	}
	
	/***
	 * 
	 * @param base
	 * @param pct
	 * @param limiteMax
	 * @param limiteMin
	 * @param logger
	 * @return
	 */
	public static boolean isLimitiMinMaxCorretti(String base, String pct, String limiteMax, String limiteMin, Logger logger) 
	{
		boolean max = false;
		boolean min = false;
		boolean ris = false;
		
		BigDecimal importo = new BigDecimal(0);
		
		logger.info("importo: " + base);
		logger.info("percentuale: " + pct);
		
		BigDecimal bd_importoRichiesto =new BigDecimal(base);
		logger.info("String (importo) ---> BigDecimal "+bd_importoRichiesto); 
		
		BigDecimal bd_percentualeRichiesta =new BigDecimal(pct);
		logger.info("String (percentuale) ---> BigDecimal "+bd_percentualeRichiesta); 
		
		BigDecimal bd_limiteMax =new BigDecimal(limiteMax).setScale(2);
		logger.info("String (bd_limiteMax) ---> BigDecimal "+bd_limiteMax);
		
		BigDecimal bd_limiteMin =new BigDecimal(limiteMin).setScale(2);
		logger.info("String (bd_limiteMin) ---> BigDecimal "+bd_limiteMin);
		
		final BigDecimal ONE_HUNDRED = new BigDecimal(100);
		BigDecimal zero = new BigDecimal(BigInteger.ZERO);
		
		importo =  bd_importoRichiesto.multiply(bd_percentualeRichiesta).divide(ONE_HUNDRED).setScale(2);
		logger.info(importo);
		
		BigDecimal bd_importoPercentuale =new BigDecimal(0).setScale(2);
		logger.info("String (bd_importoPercentuale) ---> BigDecimal "+bd_importoPercentuale);
		
		bd_importoPercentuale = bd_importoRichiesto.subtract(importo);
		logger.info("Importo richiesto: " + bd_importoRichiesto + " - importo " + importo + " = " + bd_importoPercentuale);
		
		if(bd_importoPercentuale.compareTo(zero) == 0 || bd_importoPercentuale.compareTo(zero) == 0.00) {
			bd_importoPercentuale = importo;
			logger.info("Importo richiesto: " + bd_importoPercentuale);
		}
		
		if(bd_limiteMax.compareTo(bd_importoPercentuale) == 1 ||  bd_limiteMax.compareTo(bd_importoPercentuale) == 0) {
			max = true;
		}
		
		if(bd_importoPercentuale.compareTo(bd_limiteMin) == 1 &&  bd_importoPercentuale.compareTo(zero) == 1) {
			min = true;
		}
		
		if(max && min) {
			ris =true;
		}
				
		return ris;
	}
	
	
	/***
	 * 
	 * @param base
	 * @param limiteMax
	 * @param logger
	 * @return
	 */
	public static boolean isLimiteMaxCorretto(String base, String limiteMax, Logger logger) 
	{
//		boolean max = false;
		boolean ris = false;
		
		logger.info("importo: " + base);
		
		BigDecimal bd_importoRichiesto =new BigDecimal(base);
		logger.info("String (importo) ---> BigDecimal "+bd_importoRichiesto); 
		
		BigDecimal bd_limiteMax =new BigDecimal(limiteMax).setScale(2);
		logger.info("String (bd_limiteMax) ---> BigDecimal "+bd_limiteMax);
		
		BigDecimal bd_importoPercentuale =new BigDecimal(0).setScale(2);
		logger.info("String (bd_importoPercentuale) ---> BigDecimal "+bd_importoPercentuale);
		
		if(bd_limiteMax.compareTo(bd_importoRichiesto) == 1 ||  bd_limiteMax.compareTo(bd_importoRichiesto) == 0) {
			ris = true;
		}
		
		return ris;
	}
	
	
	/***
	 * 
	 * @param base
	 * @param limiteMin
	 * @param logger
	 * @return
	 */
	public static boolean isLimiteMinCorretto(String base, String limiteMin, Logger logger) 
	{
		boolean ris = false;
		
		logger.info("importo: " + base);
		
		if(base.indexOf(",")!=-1){
			base = base.replace(",", ".");
		}
		logger.info("base: "+base); 
		
		BigDecimal bd_importoRichiesto =new BigDecimal(base).setScale(2);
		logger.info("BigDecimal (bd_importoRichiesto) ---> BigDecimal "+bd_importoRichiesto); 
		
		
		BigDecimal bd_limiteMin =new BigDecimal(limiteMin).setScale(2);
		logger.info("String (bd_limiteMin) ---> BigDecimal "+bd_limiteMin);
		
		BigDecimal zero = new BigDecimal(BigInteger.ZERO);
		
		if(bd_importoRichiesto.compareTo(bd_limiteMin) == 1 &&  bd_importoRichiesto.compareTo(zero) == 1) {
			ris = true;
		}
		
		return ris;
	}
	
	
	public static boolean isLimiteMinoreUguale(String importo, String limiteMax, Logger logger) 
	{
		boolean ris = false;
		
		if(importo.indexOf(",")!=-1){
			importo = importo.replace(",", ".");
		}
		logger.info("importo: "+importo); 
		
		BigDecimal bd_importoRichiesto =new BigDecimal(importo).setScale(2);
		logger.info("BigDecimal (bd_importoRichiesto) ---> BigDecimal "+bd_importoRichiesto); 
		
		
		BigDecimal bd_limiteMax =new BigDecimal(limiteMax).setScale(2);
		logger.info("String (bd_limiteMax) ---> BigDecimal "+bd_limiteMax);
		
		if(bd_importoRichiesto.compareTo(bd_limiteMax) <= 0) {
			ris = true;
		}
		
		return ris;
	}
	
	public static boolean isLimiteMaggioreUguale(String importo, String limiteMin, Logger logger) 
	{
		boolean ris = false;
		
		if(importo.indexOf(",")!=-1){
			importo = importo.replace(",", ".");
		}
		logger.info("importo: "+importo); 
		
		BigDecimal bd_importoRichiesto =new BigDecimal(importo).setScale(2);
		logger.info("BigDecimal (bd_importoRichiesto) ---> BigDecimal "+bd_importoRichiesto); 
		
		BigDecimal bd_limiteMin =new BigDecimal(limiteMin).setScale(2);
		logger.info("String (bd_limiteMin) ---> BigDecimal "+bd_limiteMin);
		
		if(bd_importoRichiesto.compareTo(bd_limiteMin) >= 0) {
			ris = true;
		}
		
		return ris;
	}
	
	/***
	 * 
	 * @param totaleSpese
	 * @param cessioneCreditoImporto
	 * @param importo_richiesto
	 * @param logger
	 * @return
	 */
	public static boolean checkTotaleSpese(String totaleSpese, String cessioneCreditoImporto, String importo_richiesto, Logger logger) {
		
		logger.info("debug: inizio verifica cessione credito importo...");
		boolean ris = false;
		
		String str_cessioneCreditoImporto = cessioneCreditoImporto.replace(",", ".");
		logger.info("String (str_cessioneCreditoImporto) ---> BigDecimal "+str_cessioneCreditoImporto);
		
		BigDecimal bd_cessioneCreditoImporto =new BigDecimal(str_cessioneCreditoImporto).setScale(2);
		logger.info("BigDecimal (bd_cessioneCreditoImporto) ---> BigDecimal "+bd_cessioneCreditoImporto); 
		
		
		BigDecimal bd_importo_richiesto =new BigDecimal(importo_richiesto).setScale(2);
		logger.info("String (bd_importo_richiesto) ---> BigDecimal "+bd_importo_richiesto);
		
 		BigDecimal bd_somma =new BigDecimal("0");
		bd_somma = (bd_importo_richiesto.add(bd_cessioneCreditoImporto).setScale(2));
		logger.info("String (bd_somma) ---> BigDecimal "+bd_somma);
		
		String str_totaleSpese = totaleSpese.replace(",", ".");
		logger.info("String (str_totaleSpese) ---> BigDecimal "+str_totaleSpese);
		
		BigDecimal bd_totaleSpese =new BigDecimal(str_totaleSpese).setScale(2);
		logger.info("String (bd_totaleSpese) ---> BigDecimal "+bd_totaleSpese);
		
		if(bd_totaleSpese.compareTo(bd_somma) == 1 ||  bd_totaleSpese.compareTo(bd_somma) == 0) {
			ris = true;
		}
		
		return ris;
	}
	/** Jira 1671: -2R fine **/
	
	
	public static boolean compare(Map<String, String> dettagliInterventiCompatibili, Map<String, String> dettagliBeneficiarioCompatibili, Logger logger) {
		boolean result = false;
		
		Object key = null;
		Object val = null;
		
		if(dettagliBeneficiarioCompatibili != null && !dettagliBeneficiarioCompatibili.isEmpty()){
			
			for(Map.Entry<String, String> entry1 : dettagliBeneficiarioCompatibili.entrySet()) {
				key = entry1.getKey();
				val = entry1.getValue();
				break;
			}
		}
		
		if(dettagliBeneficiarioCompatibili != null && !dettagliBeneficiarioCompatibili.isEmpty()){
			
			for(Map.Entry<String, String> entry2 : dettagliInterventiCompatibili.entrySet()) {
				if(	entry2.getKey().equals(key)) {
					if( entry2.getValue().equals(val)) {
						result = true;
						break;
					}
				}
			}
		}
		
		return result;
	}

	public static BigDecimal minimoTraDueBD(BigDecimal primoVal, BigDecimal secondoVal, Logger logger) {
		
		BigDecimal resultBD = primoVal.min(secondoVal);
		logger.info(resultBD);
		return resultBD;
	}

	
	
	/** Jira 2008 */
	public static boolean verificaSpesaB(Map<String, BigDecimal> totVociSpesaPerCategoriaMap, Logger logger) {
		  boolean ris = false;
		  
		  int compare = 0;
		  
		  BigDecimal totaleCtgAC = new BigDecimal(0.00);
		  
		  BigDecimal BD_totCtgA = new BigDecimal(0.00);
		  BigDecimal BD_totCtgB = new BigDecimal(0.00);
		  BigDecimal BD_totCtgC = new BigDecimal(0.00);
		  
		  for (Entry<String, BigDecimal> entry : totVociSpesaPerCategoriaMap.entrySet()) {
			if("A".equals(entry.getKey())){
				BD_totCtgA = entry.getValue();
			}
			else if("B".equals(entry.getKey())){
				BD_totCtgB = entry.getValue();
			}
			else if("C".equals(entry.getKey())){
				BD_totCtgC = entry.getValue();
			}
		  }
		  
		  logger.info("Totale voci spesa categoria A: " + BD_totCtgA);
		  logger.info("Totale voci spesa categoria B: " + BD_totCtgB);
		  logger.info("Totale voci spesa categoria C: " + BD_totCtgC);
		  
		  
		  // eseguo confronti per verificare se B risulta < ( A+C )
		  totaleCtgAC = BD_totCtgA.add(BD_totCtgC);
		  logger.info("Totale voci spesa categorie A+C: " + totaleCtgAC);
		  
		  compare = BD_totCtgB.compareTo(totaleCtgAC);
		  
	      if( compare == -1 ){
	    	  logger.info("Totale voci spesa categoriaB: "+BD_totCtgB+" risulta minore del totle categoriaC: " +totaleCtgAC);
	    	  ris=true;
	      }
		  
		  logger.info("ris vale: " + ris);
		  return ris;
	}

	public static Map<String, BigDecimal> calcolaTotParziali( List<DettaglioVoceSpesaInterventoCulturaVO> pianoSpeseList, Logger logger) {
		
		logger.info("call calcolaTotParziali ... inizio ...");
		  
		  Map<String, BigDecimal> risMap = new HashMap<>();
		  
		  String subStringCtg  = "";
		  String sommaVociCtgA = "";
		  String sommaVociCtgB = "";
		  String sommaVociCtgC = "";
		  
		  BigDecimal bd_sommaVociCtgA = new BigDecimal(0.00);
		  BigDecimal bd_sommaVociCtgB = new BigDecimal(0.00);
		  BigDecimal bd_sommaVociCtgC = new BigDecimal(0.00);
		  
		  BigDecimal bd_totVociCtgA = new BigDecimal(0.00);
		  BigDecimal bd_totVociCtgB = new BigDecimal(0.00);
		  BigDecimal bd_totVociCtgC = new BigDecimal(0.00);
		  
		  if(pianoSpeseList!=null && !pianoSpeseList.isEmpty())
		  {
			  for (int i = 0; i < pianoSpeseList.size(); i++) {
				  
				  if(pianoSpeseList.get(i).getTipoRecord().equals("3"))
				  {
					  if(pianoSpeseList.get(i).getTotaleVoceSpesa() !=null && !pianoSpeseList.get(i).getTotaleVoceSpesa().isEmpty())
					  {
						  logger.info("DescrVoceSpesa	: " + pianoSpeseList.get(i).getDescrVoceSpesa());
						  logger.info("TipoRecord		: " + pianoSpeseList.get(i).getTipoRecord());
						  logger.info("TotaleVoceSpesa	: " + pianoSpeseList.get(i).getTotaleVoceSpesa());
						  
						  subStringCtg = pianoSpeseList.get(i).getDescrVoceSpesa().substring(0,1);
						  logger.info("subStringCtg	: " + subStringCtg);
						  
						  if(subStringCtg.equalsIgnoreCase("A"))
						  {
							  sommaVociCtgA = pianoSpeseList.get(i).getTotaleVoceSpesa();
							  logger.info("somma parziale ctgA	: " + sommaVociCtgA);
							  
							  if(sommaVociCtgA.matches("^\\d+(,\\d{1,2})?$"))
							  {
								  BigDecimal bd_parzialeVoceSpesaA = new BigDecimal(sommaVociCtgA.replace(',', '.'));
								  bd_sommaVociCtgA = bd_sommaVociCtgA.add(bd_parzialeVoceSpesaA);
								  logger.info("somma parziale ctgA	: " + bd_sommaVociCtgA);
								  // risMap.put(pianoSpeseList.get(i).getDescrVoceSpesa().substring(0,2), bd_sommaVociCtgA);
							  }
							  
						  } // fine lettura parziale ctgA
						  
						  else if(subStringCtg.equalsIgnoreCase("B"))
						  {
							  sommaVociCtgB = pianoSpeseList.get(i).getTotaleVoceSpesa();
							  logger.info("somma parziale ctgB	: " + sommaVociCtgB);
							  
							  if(sommaVociCtgB.matches("^\\d+(,\\d{1,2})?$"))
							  {
								  BigDecimal bd_parzialeVoceSpesaB = new BigDecimal(sommaVociCtgB.replace(',', '.'));
								  bd_sommaVociCtgB = bd_sommaVociCtgB.add(bd_parzialeVoceSpesaB);
								  logger.info("somma parziale ctgB	: " + bd_sommaVociCtgB);
								  // risMap.put(pianoSpeseList.get(i).getDescrVoceSpesa().substring(0,2), bd_parzialeVoceSpesaB);
							  }
							  
						  } // fine lettura parziale ctgB
						  
						  else if(subStringCtg.equalsIgnoreCase("C"))
						  {
							  sommaVociCtgC = pianoSpeseList.get(i).getTotaleVoceSpesa();
							  logger.info("somma parziale ctgC	: " + sommaVociCtgC);
							  
							  if(sommaVociCtgC.matches("^\\d+(,\\d{1,2})?$"))
							  {
								  BigDecimal bd_parzialeVoceSpesaC = new BigDecimal(sommaVociCtgC.replace(',', '.'));
								  bd_sommaVociCtgC = bd_sommaVociCtgC.add(bd_parzialeVoceSpesaC);
								  logger.info("somma parziale ctgC	: " + bd_sommaVociCtgC);
								  // risMap.put(pianoSpeseList.get(i).getDescrVoceSpesa().substring(0,2), bd_parzialeVoceSpesaC);
							  }
							  
						  } // fine lettura parziale ctgC
					  }
				  } // fine lettura info record 3
			  }
			  
			  logger.info("somma parziale ctgA	: " + bd_sommaVociCtgA);
			  logger.info("somma parziale ctgB	: " + bd_sommaVociCtgB);
			  logger.info("somma parziale ctgC	: " + bd_sommaVociCtgC);
			  
			  bd_totVociCtgA = bd_sommaVociCtgA;
			  risMap.put("A", bd_totVociCtgA);
			  
			  bd_totVociCtgB = bd_sommaVociCtgB;
			  risMap.put("B", bd_totVociCtgB);
			  
			  bd_totVociCtgC = bd_sommaVociCtgC;
			  risMap.put("C", bd_totVociCtgC);
			  
			  
		  }
		  logger.info("call calcolaTotParziali ... fine ...");
		  return risMap;
	}

	
	
	/** Jira 2015 -2r */
	public static BigDecimal getTotSpeseConnAttivita( List<DettaglioVoceSpesaInterventoCulturaVO> interventiList, Logger logger) {
		
		BigDecimal bd_totSpeseAttivita = new BigDecimal(0.00);
		
		String totSpConnAttivita = "0";
		
		if (interventiList!=null && !interventiList.isEmpty())
		{
			   for(int j=0; j<interventiList.size(); j++)
			   {
			       DettaglioVoceSpesaInterventoCulturaVO curInterventoMap =  interventiList.get(j);
			       
			       if(curInterventoMap!=null)
			       {
			          
					  String tipoRecord = (String) curInterventoMap.getTipoRecord();
					  logger.info("idDettIntervento: " + tipoRecord);
					  
					  String idCatVoceSpesa = (String) curInterventoMap.getIdCatVoceSpesa();
					  logger.info("idCatVoceSpesa: " + idCatVoceSpesa);
					  
					  String totaleVoceSpesa = (String) curInterventoMap.getTotaleVoceSpesa();
					  logger.info("totaleVoceSpesa: " + totaleVoceSpesa);
					  
					  if(tipoRecord!=null && tipoRecord.equals("4")){
						  if(idCatVoceSpesa!=null && idCatVoceSpesa.equals("1")){
							  totSpConnAttivita = totaleVoceSpesa;
							  logger.info("totaleVoceSpesa: " + totaleVoceSpesa); // 760,00
							  
							  // gestisco eventuale decimali separati da virgola 
							  if(totSpConnAttivita.matches("^\\d+(,\\d{1,2})?$")) {
								  bd_totSpeseAttivita = new BigDecimal(totSpConnAttivita.replace(',', '.'));
								  logger.info("bd_totSpeseAttivita	: " + bd_totSpeseAttivita);
								  break;
							  }
						  }
					  }
			       }
			   } 
        }
		
		return bd_totSpeseAttivita;
	}

	public static boolean verificaImportoMax( BigDecimal bd_totSpeseConnesseAttivita, BigDecimal totaleSpeseMassimo, Logger logger) {
		
		boolean isCorretto = true;
		int compare = 0;
		
		if(totaleSpeseMassimo!=null){
			
			logger.info("compare  bd_totSpeseConnesseAttivita: "+bd_totSpeseConnesseAttivita+" >= totaleSpeseMassimo: "+totaleSpeseMassimo);
			compare = bd_totSpeseConnesseAttivita.compareTo(totaleSpeseMassimo);
			logger.info("risultato compare max: "+compare);
			
			/* se (1) a > b */
			if(compare == 1){
				logger.info("Spese connesse alle attivita risulta maggiore del totale max consentito di €: "+totaleSpeseMassimo);
				isCorretto=false;
			}
		}
		
		logger.info("isCorretto: "+isCorretto);
		return isCorretto;
	}

	/**
	 * verifica totale importo minimo
	 * @param bd_totSpeseConnAttivita
	 * @param totSpeseMinimo
	 * @param logger
	 * @return
	 */
	public static boolean verificaImportoMin( BigDecimal bd_totSpeseConnAttivita, BigDecimal totSpeseMinimo, Logger logger) {
		
		boolean isCorretto = true;
		int compare = 0;
		
		if(totSpeseMinimo!=null){
			
			compare = bd_totSpeseConnAttivita.compareTo(totSpeseMinimo);
			logger.info("risultato compare max: "+compare);
			
			/* (-1) a < b */
			if(compare < 0){
				logger.info("Spese connesse alle attivita risulta minore del totale min consentito di €: "+totSpeseMinimo);
				isCorretto=false;
			}
		}
		
		logger.info("isCorretto: "+isCorretto);
		return isCorretto;
	}

//	public static boolean confrontaUguaglianzaBD( BigDecimal speseConnesseAttivitaInFormaAgevBD, BigDecimal speseConnesseAttivitaInPianoSpeseBD, Logger logger) {
//		
//		boolean isUguali = true;
//		int compare = 0;
//		
//		if(speseConnesseAttivitaInFormaAgevBD!=null && speseConnesseAttivitaInPianoSpeseBD!=null){
//			
//			compare = speseConnesseAttivitaInFormaAgevBD.compareTo(speseConnesseAttivitaInPianoSpeseBD);
//			logger.info("risultato compare max: "+compare);
//			
//			/* (0) a == b */
//			if(compare != 0){
//				logger.info("Spese connesse alle attivita forma agv diverse da piano spese: "+compare);
//				isUguali=false;
//			}
//		}
//		
//		logger.info("isUguali: "+isUguali);
//		return isUguali;
//	}

	/**
	 * Verifica se provincia risulta in Piemonte
	 * @param provinceList
	 * @param provinvia
	 * @param logger
	 * @return
	 */
//	public static boolean verificaSedeLegale(List<ProvinciaVO> provinceList, String provinvia, Logger logger) {
//		
//		boolean isProvPiemontese = false;
//		
//		if(provinceList!=null && !provinceList.isEmpty()) {
//			if(provinvia!=null){
//				for (ProvinciaVO s : provinceList) {
//		            if (s.getCodice().contains(provinvia)) {
//		            	isProvPiemontese=true;
//		            	break;
//		            }
//		        }
//			}
//		}
//		return isProvPiemontese;
//	}

	/** jira 2015 -2r */
	public static boolean verificaTipoVociSpesa( List<DettaglioVoceSpesaInterventoCulturaVO> pianoSpeseList, Logger logger) {
		  
		  boolean ris = true;
		  
		  if(pianoSpeseList!=null && !pianoSpeseList.isEmpty())
		  {
			  for (int i = 0; i < pianoSpeseList.size(); i++) 
			  {
				  logger.info("DescrVoceSpesa	: " + pianoSpeseList.get(i).getDescrVoceSpesa());
				  logger.info("TipoRecord		: " + pianoSpeseList.get(i).getTipoRecord());
				  logger.info("TotaleVoceSpesa	: " + pianoSpeseList.get(i).getTotaleVoceSpesa());
				  
				  if(pianoSpeseList.get(i).getTipoRecord().equals("3")){
					  if(!pianoSpeseList.get(i).getDescrVoceSpesa().substring(0,1).equals("B")){
						  if(pianoSpeseList.get(i).getTotaleVoceSpesa() !=null 
								  && !pianoSpeseList.get(i).getTotaleVoceSpesa().isEmpty() 
								  && (!pianoSpeseList.get(i).getTotaleVoceSpesa().equals("0") 
										  || !pianoSpeseList.get(i).getTotaleVoceSpesa().equals("0,00")
										  || !pianoSpeseList.get(i).getTotaleVoceSpesa().equals("0.00"))){
							  
							  logger.info("DescrVoceSpesa	: " + pianoSpeseList.get(i).getDescrVoceSpesa());
							  logger.info("TipoRecord		: " + pianoSpeseList.get(i).getTipoRecord());
							  logger.info("TotaleVoceSpesa	: " + pianoSpeseList.get(i).getTotaleVoceSpesa());
							  
							  ris=false;
							  break;
						  }
					  }
				  }
			  }
		  }
		  return ris;
	  }

    /**
     * Verifica presenza allegati obbligatori se è stata scelta almeno una tipologia intervento
     * @param allegati
     * @param logger
     * @param logprefix
     * @return true se sono stati uploadati tutti gli allegati obbligatori oppure se non ci sono allegati obbligatori associati al bando
     */
    public static List<String> verificaPresenzaTuttiAllegatiObbligatoriTipolIntervento(AllegatiVO allegati, Integer idBando, Integer idSportelloBando, Integer idDomanda, Integer idTipolBeneficiario, List<Integer> idTipolIntervento, List<String> errorList, Logger logger, String logprefix)
    {
    	final String ERRMSG_ALLEGATI_OBBL = "Allegati e Dichiarazioni / Allegati: Risulta obbligatorio allegare il documento ";
    											
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatoriTipolIntervento BEGIN");     

		//Lettura allegati obbligatori per tipologia intervento
		List<TipologiaAllegatoVO> elencoAllegatiObbligatori = null;
		try
		{
			elencoAllegatiObbligatori = DocumentazioneNGDAO.getTipologiaAllegatoTipolInterventoList(idBando, idSportelloBando, idDomanda, idTipolBeneficiario, idTipolIntervento, logger);
		}
		catch (CommonalityException e)
		{
			//  Auto-generated catch block
			e.printStackTrace();
		}
		if (elencoAllegatiObbligatori == null || elencoAllegatiObbligatori.isEmpty()) return errorList;

		//Allegati salvati su db
    	AllegatiItemVO[] allegatiList = allegati.getAllegatiList();			
    	HashMap<Integer, String> hmAllegatiPresenti = new HashMap<Integer, String>();
		for (AllegatiItemVO item : allegatiList)
		{
			hmAllegatiPresenti.put(new Integer(item.getDocumento().getIdTipologia()), item.getDocumento().getIdTipologia());
		}
		logger.info(logprefix + " hmAllegatiPresenti = " + hmAllegatiPresenti);

		//Verifica tipologia intervento
    	for (TipologiaAllegatoVO tipologiaAllegatoVO : elencoAllegatiObbligatori)
    	{
    		if (! hmAllegatiPresenti.containsKey(tipologiaAllegatoVO.getIdallegato()))
    		{
    			errorList.add(ERRMSG_ALLEGATI_OBBL + tipologiaAllegatoVO.getDescrizione());
    		}
    	}
    	
    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatoriTipolIntervento END");

    	return errorList; 
    }

    public static List<Integer> getTipologiaIntervento(TipologiaInterventoVO[] arrTipologiaInterventoVO, Logger logger)
    {
    	String logprefix = "[MetodiUtili::getTipologiaIntervento] ";
    	logger.info(logprefix + "BEGIN");
    	
    	logger.info(logprefix + "arrTipologiaInterventoVO vale: " + arrTipologiaInterventoVO);
    	
		List<Integer> elencoIdTipolIntervento = new ArrayList<Integer>();
		if (arrTipologiaInterventoVO == null || arrTipologiaInterventoVO.length == 0) return elencoIdTipolIntervento;

		for (TipologiaInterventoVO tipologiaInterventoVO : arrTipologiaInterventoVO)
		{
			if (StringUtils.isNotEmpty(tipologiaInterventoVO.getChecked()) && tipologiaInterventoVO.getChecked().equals("true"))
			{
				elencoIdTipolIntervento.add(new Integer(tipologiaInterventoVO.getIdTipoIntervento()));
			}
		}

		return elencoIdTipolIntervento;
    }

    public static List<String> verificaPresenzaTuttiAllegatiObbligatoriPerVeicoliAcquistati(AllegatiVO allegati, List<String> errorList, Logger logger, String logprefix)
    {
    	final String ID_ALLEGATO_LINEA_A_RINNOVO_AUTOMEZZI_FATTURA_ACQUISTO = "345";
    	final String ID_ALLEGATO_LINEA_A_RINNOVO_AUTOMEZZI_CERTIFICATO_ROTTAMAZIONE= "347";
    	final String ID_ALLEGATO_LINEA_A_RINNOVO_AUTOMEZZI_SCANSIONE_LIBRETTO = "348";

    	final String ERRMSG_ALLEGATI_OBBL = "Allegati e Dichiarazioni / Allegati: Gli allegati LINEA A - RINNOVO AUTOMEZZI - Scansione del libretto di circolazione del veicolo acquistato intestato al soggetto richiedente, LINEA A - RINNOVO AUTOMEZZI - Certificato di rottamazione del veicolo rottamato e LINEA A - RINNOVO AUTOMEZZI - Fattura di acquisto del veicolo sono obbligatori ";

    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatoriPerVeicoliAcquistati BEGIN");     

		//Allegati salvati su db
    	AllegatiItemVO[] allegatiList = allegati.getAllegatiList();			
    	HashMap<String, String> hmAllegatiPresenti = new HashMap<String, String>();
		for (AllegatiItemVO item : allegatiList)
		{
			hmAllegatiPresenti.put(item.getDocumento().getIdTipologia(), item.getDocumento().getIdTipologia());
		}
		logger.info(logprefix + " hmAllegatiPresenti = " + hmAllegatiPresenti);

		//Se uno dei veicoli inseriti negli acquisti ha il flag AutoDaAcquistare a false (quindi il veicolo è già stato acquistato), 3 allegati diventano obbligatori (id_allegato => 345, 347, 348) e bisogna controllare che siano stati uploadati
		if (! hmAllegatiPresenti.containsKey(ID_ALLEGATO_LINEA_A_RINNOVO_AUTOMEZZI_FATTURA_ACQUISTO)
			|| ! hmAllegatiPresenti.containsKey(ID_ALLEGATO_LINEA_A_RINNOVO_AUTOMEZZI_CERTIFICATO_ROTTAMAZIONE)
			|| ! hmAllegatiPresenti.containsKey(ID_ALLEGATO_LINEA_A_RINNOVO_AUTOMEZZI_SCANSIONE_LIBRETTO))
		{
			errorList.add(ERRMSG_ALLEGATI_OBBL);
		}

    	logger.info(logprefix + " verificaPresenzaTuttiAllegatiObbligatoriPerVeicoliAcquistati END");

    	return errorList; 
    }
    
    
    
    /**
  	 * Converte Object[] to List
  	 * @param dettaglioAcquistiVOList
  	 * @return
  	 */
	public static List<DettaglioAcquistiVO> convertArrayObjectToList( DettaglioAcquistiVO[] dettaglioAcquistiVOList, Logger logger) 
	{
		String logprefix = "[MetodiUtili::convertArrayObjectToList] ";
		logger.info(logprefix + " convertArrayObjectToList BEGIN");     
		
		List<DettaglioAcquistiVO> dettAcqPerLineaSelected;
		dettAcqPerLineaSelected = Arrays.asList(dettaglioAcquistiVOList);
		
		logger.info(logprefix + " convertArrayObjectToList EMD");     
		return dettAcqPerLineaSelected;
	}
	
	/**
	 * 
	 * @param dataDaVerificare
	 * @param dataSoglia
	 * @param logger
	 * @return
	 */
	public static boolean isDataRangeValida(String dataDaVerificare, String dataSoglia, Logger logger) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dtDaVerificare = null;
		Date dtSoglia = null;

		boolean ris = false;
		
		if( dataDaVerificare != null && !dataDaVerificare.isEmpty()){
			
			try {
				// convert String to Date
				dtDaVerificare = sdf.parse(dataDaVerificare);
				
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} 
		}
		
		if( dataSoglia != null && !dataSoglia.isEmpty()){
			
			try {
				// convert String to Date
				dtSoglia = sdf.parse(dataSoglia);
				
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} 
		}
		

		logger.info("dtDaVerificare : " + sdf.format(dtDaVerificare));
		logger.info("dtSoglia : " + sdf.format(dtSoglia));

        // eseguo controllo
        if (dtDaVerificare!=null && dtDaVerificare.after(dtSoglia)) {
        	logger.info("dtDaVerificare: "+dtDaVerificare+ " risulta posteriore dtSoglia: "+dtSoglia);
        	ris=true;
        }

        if (dtDaVerificare!=null && dtDaVerificare.before(dtSoglia)) {
        	logger.info("dtDaVerificare: "+dtDaVerificare+ " risulta precedente dtSoglia: "+dtSoglia);
        	ris=false;
        }

        if (dtDaVerificare!=null && dtDaVerificare.equals(dtSoglia)) {
        	logger.info("dtDaVerificare: "+dtDaVerificare+ " risulta uguale a dtSoglia"+dtSoglia);
        	ris=true;
        }
        
		return ris;
	}
    
    /**
     * isDataValida
     * @param dataDaValidare
     * @param logger
     * @return
     */
	public static boolean isDataValida(String dataDaValidare, Logger logger) 
	{
		logger.info("[MetodiUtili::isDataValida]  dataDaValidare ");
		
		DateValidator validator = DateValidator.getInstance();
		boolean ris = true;
		
		if (!StringUtils.isBlank(dataDaValidare)) {
			logger.info("dataDaValidare:" + dataDaValidare);
			
			if (!validator.isValid(dataDaValidare, "dd/MM/yyyy") || !(dataDaValidare.matches("\\d{2}/\\d{2}/\\d{4}"))) {
				ris = false;
			} 
		} 
		else {
			ris = false;
		}
		
		return ris;
	}

	
	
	public static Date convertStringToDate(String strData, Logger logger) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data = null;
		
		if(!StringUtils.isBlank(strData)){
			
			try {
				// convert String to Date
				data = sdf.parse(strData);
				logger.info("data convertita: " + data);
				
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} 
		}

		return data;
	}
}// -/ end class
