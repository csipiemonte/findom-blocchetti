/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.abstractprogettoNG;

import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.util.ValidationUtil;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class AbstractProgettoNG extends Commonality {
	

	/**
	 * Input
	 */
	AbstractProgettoNGInput input = new AbstractProgettoNGInput();
	
	/**
	 * commandValidate
	 */
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {
		return null;
	}

	/**
	 * CommonalityInput
	 */
	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	/**
	 * CommonalityOutput
	 */
	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		AbstractProgettoNGOutput output = new AbstractProgettoNGOutput();

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[AbstractProgettoNG::inject] AbstractProgettoNG  BEGIN");
		
		//// dichiarazioni
		String stereotipoDomanda = "";
		
		/** Richiesta per tutti i bandi di sostituire label:
		 *  - Abstract del progetto di investimento
		 *  con: Abstract del progetto rif.: DT
		 **/
		String abstractProgettoLabel = "Abstract del progetto";
		String titoloProgettoLabel = "Titolo progetto";
		String sintesiProgettoLabel = "Sintesi progetto";	
		String titoloProgettoNeve = "";
		String studiInCollaborazione = "";
		
		String importoComplessivoBP = "";
		String mqCoperture = "0";
		
		//// valorizzazione
		if (info.getCurrentPage() != null) {		   
			//jira 1041 (le label devono essere impostate correttamente per tutti gli stati) 
		
		    Integer idDomanda = info.getStatusInfo().getNumProposta();
	      	 
		    stereotipoDomanda = DomandaNGDAO.getStereotipoImpresa(idDomanda, logger);	
		    logger.info("[AbstractProgettoNG::inject]  stereotipoDomanda: " + stereotipoDomanda);
		    
		    String idBando = info.getStatusInfo().getTemplateId()+"";
			logger.info("[AbstractProgettoNG::inject]  idBando: " + idBando);
			
			String idSportelloBando = info.getStatusInfo().getNumSportello()+"";
			logger.info("[AbstractProgettoNG::inject]  idSportelloBando: " + idSportelloBando);
			
            if(input.abstractProgettoCustomLabel != null && !input.abstractProgettoCustomLabel.isEmpty()) {		   
               abstractProgettoLabel = input.abstractProgettoCustomLabel;		   
            }	
            if(input.titoloProgettoCustomLabel != null && !input.titoloProgettoCustomLabel.isEmpty()) {		   
               titoloProgettoLabel = input.titoloProgettoCustomLabel;		   
            }
            if(input.sintesiProgettoCustomLabel != null && !input.sintesiProgettoCustomLabel.isEmpty()) {		   
               sintesiProgettoLabel = input.sintesiProgettoCustomLabel;		   
            } 
            
            if( "true".equals(input._abstract_titoloProgettoNeve))
	    	{
	    		 /** Jira: 1428 - recupero il titolo dal database per idBando 
	    		  * da tabella: findom_t_sportelli_bandi
	    		  * */
	    		 // titoloProgettoNeve = AbstractProgettoNGDAO.getTitoloSistemaNeveByIdBando(idBando, logger);
            	 titoloProgettoNeve = AbstractProgettoNGDAO.getTitoloSistemaNeveByIdSportelloBando(idSportelloBando, logger);
	    		 logger.info("debug: titolo neve risulta: " + titoloProgettoNeve);
	    	}
            
            /**  : Jira 1589 inject */
            AbstractProgettoVO _abstractProgetto = input.abstractProgetto;
            
        	if( "true".equals(input._importo_complessivo_business_plan))
        	{
        		if (_abstractProgetto != null ) 
                {
        			// importoComplessivoBP = (String) _abstractProgetto.getImportoComplessivoBPlan();
        			importoComplessivoBP = _abstractProgetto.getImporto_complessivo_business_plan();
        			logger.info("importoComplessivoBP vale: " + importoComplessivoBP);
                }
        	}
        	
        	if ("true".equals(input._abstract_rb_collaborazione))
			{
        		if (_abstractProgetto != null ) 
                {
        			studiInCollaborazione =  _abstractProgetto.getCollaborazione();
        			logger.info("mvd: studiInCollaborazione: " + studiInCollaborazione);
                }
			}
        	
        	/** Jira: 1883 Amianto */
        	if ("true".equals(input._abstract_campoNumericoOpzionale))
    		{
        		if (_abstractProgetto != null ) 
                {   
        			mqCoperture = _abstractProgetto.getMqCoperture()!= null ? _abstractProgetto.getMqCoperture() : "0";
        			logger.info("metriQuadrati: " + mqCoperture);
        			if(mqCoperture.isEmpty()){
        				mqCoperture="0";
        			}else{
        				mqCoperture = _abstractProgetto.getMqCoperture() != null ? _abstractProgetto.getMqCoperture() : "0";
        			}
        			logger.info("metriQuadrati: " + mqCoperture);
        			output.mqCoperture = mqCoperture;
                }
    		}
		}	
		
		//// namespace		
		output.stereotipoDomanda=stereotipoDomanda;  //anche se serve solo per il CUP, che e' configurato in base al bando, non sottopongo a configurazione
													// stereotipoDomanda che comunque e' una informazione che potrebbe eventualmente servire 
		
		output.abstractProgettoLabel= abstractProgettoLabel; 
		output.titoloProgettoLabel= titoloProgettoLabel;     
		output.sintesiProgettoLabel= sintesiProgettoLabel;   
		output.titoloProgettoNeve = titoloProgettoNeve;		 
		output.importoComplessivoBP = importoComplessivoBP;	 
		
		/**  :: Jira: 1579 */
		if ("true".equals(input._abstract_rb_collaborazione))
		{
			logger.info("output inject: studiInCollaborazione: " + studiInCollaborazione);
			output.studiInCollaborazione = studiInCollaborazione;
		}
		logger.info("[AbstractProgettoNG::inject]_abstractProgetto S4_P1 END");
		
		return output;
	}

	/**
	 * modelValidate
	 * 	validazioni standard:
	 * 		acronimo				: false per Voucher
	 * 		titolo					: false per Voucher
	 * 		durataPrevista			: !=99 ( vedo blk ) | Voucher= 999
	 * 		ruolo					: false per Voucher
	 * 		codiceCUP 				: false per Voucher
	 * 
	 * 		sintesi					: si per Voucher
	 * 		relAssunzioniProgetto	: presente in sintesi	
	 */
	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException 
	{
		FinCommonInfo info = (FinCommonInfo) info1;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());

		logger.info("[AbstractProgettoNG::modelValidate]  BEGIN");

		String idBando = info.getStatusInfo().getTemplateId()+"";
		logger.info("[AbstractProgettoNG::modelValidate]  idBando: " + idBando);
		
		String idSportelloBando = info.getStatusInfo().getNumSportello()+"";
		logger.info("[AbstractProgettoNG::inject]  idSportelloBando: " + idSportelloBando);
		
		/**
		 * COSTANTI per msg di errore
		 */
		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo é obbligatorio";
		String ERRMSG_CAMPO_ALFANUMERICO = "- il campo deve essere alfanumerico";
		String ERRMSG_CAMPO_LUNGHEZZA = "- il campo deve essere di 15 caratteri";
		String ERRMSG_CAMPO_NUMERICO_NO_DECIMALI = "- il valore deve essere un numero intero positivo, senza decimali";
		String ERRMSG_CAMPO_DURATAMAX_EXCEDED = "- il valore inserito supera la durata massima indicata per il bando in oggetto";
		String ERRMSG_CAMPO_DURATAMAX_ERRATA = "- il valore deve essere un numero intero positivo, senza decimali, diverso da zero";
		
		// Jira: 1355 -   - Inizio
		String ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO = "- Sono ammissibili progetti con inizio entro: ";
		String ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO = "La data inizio progetto deve risultare inferiore o uguale alla data fine progetto.";
		String ERRMSG_DATA_INIZIO_MINORE_UGUALE_MAX_FINE_PROGETTO = "La data inizio progetto deve risultare inferiore o uguale alla data massima di fine progetto.";
		String ERRMSG_DATA_FINE_MINORE_DATA_INIZIO= "- La data di fine progetto deve risultare maggiore od uguale alla data inizio progetto. ";
		String ERRMSG_INFERIORE_DATA_INIZIO_PROGETTO="- La data di inizio progetto selezionata deve risultare maggiore od uguale alla data inizio progetto. ";
		
		/**
		 * Costante per msg errori data inizio e fine progetto:
		 */
		String ERRMSG_DATA_NON_VALIDA = "- data non risulta valida!";
		String ERRMSG_DATA_FINE_PROGETTO_SUPERIORE = "- Sono ammissibili progetti con termine entro: ";
		String ERRMSG_DATA_INIZIO_PROGETTO_INFERIORE_DTINPRJDB = "- Sono ammissibili progetti con decorrenza dal ";
		String ERRMSG_PAROLA_SENZA_SPAZIO = "- Attenzione, verifica la parola digitata.<br/> Risulta trppo lunga e senza spazi.";
		String ERRMSG_FORMATO_DECIMALE = "- il valore deve essere numerico con al massimo 2 decimali";
			
		/**  :: Jira: 1561 */
		String ERRMSG_PROGETTO_COLLABORAZIONE = "- La collaborazione risulta ammessa solamente per le tipologie di intervento \"Progetti di ricerca e sviluppo\" oppure per \"Studi di fattibilit&#224; tecnica\" ";
		
		/** Jira: 1886   */
		String ERRMSG_IMPORTO_FORMATO = "- il valore deve essere numerico, maggiore di zero e senza decimali";   

		AbstractProgettoVO _abstractProgetto = input.abstractProgetto;
		
		/**
		 * Inizio: validazione campi
		 */
		if (_abstractProgetto != null ) 
		{
			logger.info("[AbstractProgettoNG::modelValidate] _abstractProgetto:" + _abstractProgetto);
		
			//controllo dei singoli campi
		     if ("true".equals(input._abstract_acronimo))
		     {
				// acronimo (se il bando lo prevede)
		    	logger.info("[AbstractProgettoNG::modelValidate] _abstract_acronimo: si");
				String acronimo =  _abstractProgetto.getAcronimo();
	            if (StringUtils.isBlank(acronimo)) {
					addMessage(newMessages,"_abstractProgetto_acronimo", ERRMSG_CAMPO_OBBLIGATORIO);
					logger.warn("[AbstractProgettoNG::modelValidate] acronimo non valorizzato");
				} else {				
					logger.info("[AbstractProgettoNG::modelValidate] acronimo: " + acronimo);
				}
		     }
		     
		     if ("true".equals(input._abstract_titoloProgetto))
		     {
		    	 String titolo = "";
		    	 
		    	 if( "true".equals(input._abstract_titoloProgettoNeve))
		    	 {
		    		 /** Jira: 1428 - recupero il titolo dal database per idBando 
		    		  * da tabella: findom_t_sportelli_bandi
		    		  * */
		    		 titolo = AbstractProgettoNGDAO.getTitoloSistemaNeveByIdSportelloBando(idSportelloBando, logger);
		    		 logger.info("[AbstractProgettoNG::modelValidate] _abstract_titoloProgettoNeve risulta: " + titolo);
		    		 
		    	 }else{
		    		 titolo = (_abstractProgetto.getTitolo()!=null) ? _abstractProgetto.getTitolo().trim() : null;
		    		 logger.info("[AbstractProgettoNG::modelValidate] _abstract_titoloProgetto risulta: " + titolo);
		    	 }
		    	 
		    	 if (StringUtils.isBlank(titolo)) 
		    	 {
		    		 addMessage(newMessages,"_abstractProgetto_titolo", ERRMSG_CAMPO_OBBLIGATORIO);
		    		 logger.warn("[AbstractProgettoNG::modelValidate] titolo non valorizzato");
		    	 } else	{
					boolean presenteSpazio = false;
					presenteSpazio = validateMessageText(titolo);
					
					if( !presenteSpazio )
					{
						logger.info("[AbstractProgettoNG::modelValidate] spazio NON presente");
						
						// verifico numero caratteri
						
						int len = titolo.length();
						logger.info("[AbstractProgettoNG::modelValidate] len risultano: " + len);
						
						if( len >= 32 ){
							logger.info("[AbstractProgettoNG::modelValidate] Parola troppo lunga senza spazio!");
							addMessage(newMessages,"_abstractProgetto_titolo", ERRMSG_PAROLA_SENZA_SPAZIO);
						}
					}
				}
		     }
		     
					
			/**
			 * Sintesi progetto:
			 * recuperato da Oggetto: AbstractProgettoVO
			 */			
			String sintesi = null;
			if(_abstractProgetto.getSintesi()!=null){
				sintesi = _abstractProgetto.getSintesi().trim();
			}
			logger.info("[AbstractProgettoNG::modelValidate] sintesi: " + sintesi);
			
			if (StringUtils.isBlank(sintesi)) {
				addMessage(newMessages,"_abstractProgetto_sintesi", ERRMSG_CAMPO_OBBLIGATORIO);
				logger.warn("[AbstractProgettoNG::modelValidate] sintesi non valorizzato");
			}
			else
			{
				boolean presenteSpazio = false;
				presenteSpazio = validateMessageText(sintesi);
				
				if( !presenteSpazio )
				{
					logger.debug("[AbstractProgettoNG::modelValidate] spazio NON presente");
					
					// verifico numero caratteri
					
					int len = sintesi.length();
					logger.debug("[AbstractProgettoNG::modelValidate] len risultano: " + len);
					
					if( len >= 32 ){
						logger.debug("[AbstractProgettoNG::modelValidate] Parola troppo lunga senza spazio!");
						addMessage(newMessages,"_abstractProgetto_sintesi", ERRMSG_PAROLA_SENZA_SPAZIO);
					}
				}
			}
			

			if (input._relazioni_assunzioni_progetto_ctrl !=null && !input._relazioni_assunzioni_progetto_ctrl.isEmpty())
			{
	            String relAssunzioniProgetto = _abstractProgetto.getRelAssunzioniProgetto();
	            logger.debug("[AbstractProgettoNG::modelValidate] relAssunzioniProgetto: si");
	            
				if (StringUtils.isBlank(relAssunzioniProgetto)) {
					addMessage(newMessages,"_abstractProgetto_relAssunzioniProgetto", ERRMSG_CAMPO_OBBLIGATORIO);
					logger.warn("[AbstractProgettoNG::modelValidate] relazioni assunzioni progetto non valorizzato");
				} else {				
					logger.info("[AbstractProgettoNG::modelValidate] relazioni assunzioni progetto : " + relAssunzioniProgetto);
				}
			}	
			
			if (input._relazioni_assunzioni_progetto.equals("true"))
			{
	            String relAssunzioniProgetto = _abstractProgetto.getRelAssunzioniProgetto();
	            logger.debug("[AbstractProgettoNG::modelValidate] relAssunzioniProgetto: " + relAssunzioniProgetto);
	            
				if (StringUtils.isBlank(relAssunzioniProgetto)) {
					addMessage(newMessages,"_abstractProgetto_relAssunzioniProgetto", ERRMSG_CAMPO_OBBLIGATORIO);
					logger.info("[AbstractProgettoNG::modelValidate] relazioni assunzioni progetto non valorizzato");
				} else {				
					logger.info("[AbstractProgettoNG::modelValidate] relazioni assunzioni progetto : " + relAssunzioniProgetto);
				}
			}
				
			if (!"999".equals(input._abstract_durata_prevista)) {
				
				// durata prevista progetto
				String durataPrevista = _abstractProgetto.getDurataPrevista();
				logger.debug("[AbstractProgettoNG::modelValidate] durataPrevista: si");
				
				// Se durata prevista != 0
				if((durataPrevista!=null) && (!durataPrevista.equals("0")))
				{
						
					if (StringUtils.isBlank(durataPrevista)) {
						addMessage(newMessages,"_abstractProgetto_durataPrevista", ERRMSG_CAMPO_OBBLIGATORIO);
						logger.warn("[AbstractProgettoNG::modelValidate] durataPrevista non valorizzata");
					} 
					else {
						//deve essere numerico e senza decimali			     
						if (!StringUtils.isNumeric(durataPrevista)) {
							addMessage(newMessages,"_abstractProgetto_durataPrevista", ERRMSG_CAMPO_NUMERICO_NO_DECIMALI);
							logger.warn("[AbstractProgettoNG::modelValidate] durataPrevista non numerica: " + durataPrevista);
							logger.info("[AbstractProgettoNG::modelValidate] durataPrevista non numerica: " + durataPrevista);
							
						}
						else{		
							logger.info("[AbstractProgettoNG::modelValidate]  durataPrevista: " + durataPrevista);
							//deve essere inferiore di un certo valore
							if(!input._abstract_durata_prevista.equals("0")){
								
								if(Integer.parseInt(durataPrevista) > Integer.parseInt(input._abstract_durata_prevista)){
									addMessage(newMessages,"_abstractProgetto_durataPrevista", ERRMSG_CAMPO_DURATAMAX_EXCEDED);
									logger.warn("[AbstractProgettoNG::modelValidate] durataPrevista (" + durataPrevista + ") troppo grande ");
									logger.info("[AbstractProgettoNG::modelValidate] durataPrevista (" + durataPrevista + ") troppo grande ");
								}
							}
						}
					}
				}else{
					addMessage(newMessages,"_abstractProgetto_durataPrevista", ERRMSG_CAMPO_DURATAMAX_ERRATA);
					logger.warn("[AbstractProgettoNG::modelValidate] durataPrevista (" + durataPrevista + ") deve risultare diversa da zero ");
					logger.info("[AbstractProgettoNG::modelValidate] durataPrevista (" + durataPrevista + ") deve risultare diversa da zero ");
				}
				
			}
			
			/**
			 * Jira: 1561:
			 * Validazione radio-button selezionato
			 */
			if ("true".equals(input._abstract_rb_collaborazione))
			{
				 
				String studiInCollaborazione =  _abstractProgetto.getCollaborazione();
				logger.debug("[AbstractProgettoNG::modelValidate] studiInCollaborazione: " + studiInCollaborazione);
				
	            if (StringUtils.isBlank(studiInCollaborazione)) {
					addMessage(newMessages,"_abstractProgetto_collaborazione", ERRMSG_CAMPO_OBBLIGATORIO);
					logger.warn("[AbstractProgettoNG::modelValidate] collaborazione non valorizzato");
				} else {				
					logger.info("[AbstractProgettoNG::modelValidate] studiInCollaborazione: " + studiInCollaborazione);
				}
	            
	            String[] validazione = input._abstract_progetti_collaborazione.split(",");
	            
	            for (int i = 0; i < validazione.length; i++) {
	            	logger.info(validazione[i]);
				}
	            TipologiaInterventoVO[] cp = input.caratteristicheProgettoNGVO.getTipologiaInterventoList();
	            int cntErrori = 0;
	            
	            for (int i = 0; i < cp.length; i++) 
	            {
	            	logger.info(": " + cp[i].getIdTipoIntervento());
	            	logger.info(": " + cp[i].getChecked());
	            	
	            	if ( cp[i].getChecked().equals("true") ) 
	            	{
							
            			for (int j = 0; j < validazione.length; j++) {
							if (cp[i].getIdTipoIntervento().equals(validazione[j])&& studiInCollaborazione.equalsIgnoreCase("PSC"))
							{
								cntErrori += 1;
								logger.debug("[AbstractProgettoNG::modelValidate]NON puoi salvare!");
							} else{
								logger.debug("[AbstractProgettoNG::modelValidate]puoi salvare");
							}
						}
					}
				}
	            
	            if(cntErrori == validazione.length){
					// msg di errore:
					addMessage(newMessages,	"_abstractProgetto_collaborazione", ERRMSG_PROGETTO_COLLABORAZIONE);
					logger.warn("[AbstractProgettoNG::modelValidate] " + ERRMSG_PROGETTO_COLLABORAZIONE);
	            }
	            
			}
			
			
			if ("true".equals(input._abstract_ruolo))
			{
				// ruolo (se il bando lo prevede) 
				String ruolo =  _abstractProgetto.getRuolo();
				logger.debug("[AbstractProgettoNG::modelValidate] ruolo: " + ruolo);
				
	            if (StringUtils.isBlank(ruolo)) {
					addMessage(newMessages,"_abstractProgetto_ruolo", ERRMSG_CAMPO_OBBLIGATORIO);
					logger.warn("[AbstractProgettoNG::modelValidate] ruolo non valorizzato");
				} else {				
					logger.info("[AbstractProgettoNG::modelValidate] ruolo: " + ruolo);
				}
			}
			
			/* Jira 1335   inizio */
			if ("true".equals(input._abstract_corealizzazione))
			{
				String corealizzazione =  _abstractProgetto.getCorrelazione();
				logger.debug("[AbstractProgettoNG::modelValidate] corealizzazione: puo valer si o no... " + corealizzazione);
				
	            if (StringUtils.isBlank(corealizzazione)) {
					addMessage(newMessages,"_abstractProgetto_corealizzazione", ERRMSG_CAMPO_OBBLIGATORIO);
					logger.info("[AbstractProgettoNG::modelValidate] corealizzazione non valorizzato");
				} else {				
					logger.info("[AbstractProgettoNG::modelValidate] corealizzazione: " + corealizzazione);
				}
			}
			/* Jira 1335   fine */
			
			if ("true".equals(input._abstract_cup))
			{
				// codice CUP 
				String codiceCUP = _abstractProgetto.getCodiceCUP();
				logger.info("debug8: codiceCUP: si");
				
				if (!StringUtils.isBlank(codiceCUP)) {				
					//se valorizzato deve essere alfanumerico di 15 caratteri			     
				   	if (!StringUtils.isAlphanumeric(codiceCUP)) {
				   	    addMessage(newMessages,"_abstractProgetto_codiceCUP", ERRMSG_CAMPO_ALFANUMERICO);
					    logger.warn("[AbstractProgettoNG::modelValidate] codiceCUP non alfanumerico: " + codiceCUP);
					    
				   	}else{
				   	   if (codiceCUP.length()!=15) { 
				   	       addMessage(newMessages,"_abstractProgetto_codiceCUP", ERRMSG_CAMPO_LUNGHEZZA);		   				   								
					       logger.warn("[AbstractProgettoNG::modelValidate] codiceCUP di lunghezza errata: " + codiceCUP);
					       
					   }else{			   								
					       logger.info("[AbstractProgettoNG::modelValidate] codiceCUP: " + codiceCUP);
					   }
					}
				}
			  }
			
			  /** Jira: 1883 Amianto */
			  if ("true".equals(input._abstract_campoNumericoOpzionale))
			  {
				// campo numerico metri quadrati
				String mqCoperture = _abstractProgetto.getMqCoperture();
				logger.info("dbg mqCoperture: " + mqCoperture);
				
				if (!StringUtils.isBlank(mqCoperture)) {				
						     
				   	if (!StringUtils.isAlphanumeric(mqCoperture)) {
				   	    addMessage(newMessages,"_abstractProgetto_mqCoperture", ERRMSG_CAMPO_ALFANUMERICO);
					    logger.warn("[AbstractProgettoNG::modelValidate] mqCoperture non alfanumerico: " + mqCoperture);
					    
				   	}else{
				   		if(!mqCoperture.matches("^(([1-9]\\d*)|0)(,\\d{1,2})?$")){
				   	       addMessage(newMessages,"_abstractProgetto_mqCoperture", ERRMSG_IMPORTO_FORMATO);		   				   								
					       logger.info("[AbstractProgettoNG::modelValidate] - il valore deve essere numerico, maggiore di zero e senza decimali: " + mqCoperture);
					       
					   }else{			   								
					       logger.info("[AbstractProgettoNG::modelValidate] mqCoperture: " + mqCoperture);
					   }
					}
				 }
			   }
			
			/**********************
			 * Validazione campi:
			 * - dataInizioProgetto
			 * - dataFineProgetto
			 **********************/
			if ("true".equals(input._abstractProgetto_data_inizio_fine_progetto))
			{
				// ----------------------------------------------------- check presenza data campi input a video:
				boolean isPresenteDataInizioProgetto = false;
				boolean isPresenteDataFineProgetto	 = false;
				
				// ----------------------------------------------------- check formato data: dd/MM/yyyy
				boolean isDateInizioProgettoFeValida = false;
				boolean isDateFineProgettoFeValida = false;
				
				// ----------------------------------------------------- Recupero data inizio e fine da frontend type: String
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				// data-inizio-progetto
				String dataInizioProgetto 	= _abstractProgetto.getDataInizioProgetto();
				logger.debug("[AbstractProgettoNG::modelValidate]dataInizioProgettoFE: " + dataInizioProgetto);
				
				// valida data-inizio-progetto
				if( MetodiUtili.isDateValid( dataInizioProgetto ) ){
					isPresenteDataInizioProgetto = true;
					logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto selezionata a video risulta presente e nel formato corretto: " + dataInizioProgetto);
				}else{
					addMessage(newMessages,"_abstractProgetto_dataInizioProgetto", ERRMSG_DATA_NON_VALIDA);
					logger.info("[AbstractProgettoNG::modelValidate] campo data inizio progetto non valorizzato");
				}
				
				
				if( "false".equals(input._abstractProgetto_visualizza_solo_data_inizio_progetto))
				{
					// controllo std

					String dataFineProgetto	= _abstractProgetto.getDataFineProgetto();
					logger.info("dataFineProgettoFE: " + dataFineProgetto);
					
					if( MetodiUtili.isDateValid(dataFineProgetto) ){
						isPresenteDataFineProgetto = true;
						logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto selezionata a video risulta presente e nel formato corretto: " + dataFineProgetto);
					}else{
						logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto selezionata a video NON risulta presente!" + isPresenteDataFineProgetto);
						addMessage(newMessages,"_abstractProgetto_dataFineProgetto", ERRMSG_DATA_NON_VALIDA);
						logger.info("[AbstractProgettoNG::modelValidate] campo data fine progetto non valorizzato");
					}
					
					// Se entrambi i campi input a video risultano presenti
					if( isPresenteDataInizioProgetto && isPresenteDataFineProgetto )
					{
						// ----------------------------------------------------- Recupero data inizio e fine da database type: String
						List<String> dateList = AbstractProgettoNGDAO.getDateInizioMaxFineProgettoList(idBando, logger);
						
						String str_dataInizioProgettoDAO 	= "";
						String str_dataMaxInizioProgettoDAO = "";		
						String str_dataFineProgettoDAO 		= "";
						boolean isPresentDataMaxInizioPrjDAO = false;
						boolean isDateAssentiDatabase = false;
						
						if (dateList == null || dateList.size() <= 0){
							logger.debug("[AbstractProgettoNG::modelValidate]lista date vuota... ");
						}else{
							for(int i=0; i<dateList.size(); i++)
							{
								if (i==0) 
								{
									if (dateList.get(0) != null) {
										str_dataInizioProgettoDAO = dateList.get(0);
										logger.debug("[AbstractProgettoNG::modelValidate]str_dataInizioProgettoDAO: " + str_dataInizioProgettoDAO);
									}
									
									if (dateList.get(1) != null) {
										str_dataMaxInizioProgettoDAO = dateList.get(1);
										isPresentDataMaxInizioPrjDAO= true;
										logger.debug("[AbstractProgettoNG::modelValidate]dataMaxInizioProgettoDAO: " + str_dataMaxInizioProgettoDAO);
									}
									
									if (dateList.get(2) != null) {
										str_dataFineProgettoDAO = dateList.get(2);
										logger.debug("[AbstractProgettoNG::modelValidate]dataFineProgettoDAO: " + str_dataFineProgettoDAO);
									}
									
									if(str_dataInizioProgettoDAO.isEmpty() && str_dataMaxInizioProgettoDAO.isEmpty() && str_dataFineProgettoDAO.isEmpty()){
										isDateAssentiDatabase=true;
										logger.debug("[AbstractProgettoNG::modelValidate]isDateAssentiDatabase: true");
									}
									
									break;
								}
							}
						}
						
						// ---------------------------------------------------------------------- eseguo validazione data: inizio
						if( MetodiUtili.validate(dataInizioProgetto) ){
							isDateInizioProgettoFeValida = true;
							logger.debug("[AbstractProgettoNG::modelValidate] isDateInizioProgettoFeValida: dataInizioProgetto= " + dataInizioProgetto +" - "+ isDateInizioProgettoFeValida);
						}else{
							addMessage(newMessages, "_abstractProgetto_dataInizioProgetto", ERRMSG_DATA_NON_VALIDA);
							logger.info("[AbstractProgettoNG::modelValidate]  dataInizioProgetto non valorizzato");
						}
						
						if( MetodiUtili.validate(dataFineProgetto) )
						{
							isDateFineProgettoFeValida = true;
							logger.debug("[AbstractProgettoNG::modelValidate] isDateFineProgettoFeValida: dataFineProgetto= " + dataFineProgetto +" - "+ isDateFineProgettoFeValida);
						}else{
							addMessage(newMessages, "_abstractProgetto_dataFineProgetto", ERRMSG_DATA_NON_VALIDA);
							logger.info("[AbstractProgettoNG::modelValidate]  dataInizioProgetto non valorizzato");
						}
						
						// ------- se date risultano valide - inizio
						if( isDateInizioProgettoFeValida && isDateFineProgettoFeValida )
						{
							// ----- eseguo conversione StringToDate: inizio
							try {
								
								Date dateInizioProgetto = null;
								if (dataInizioProgetto != null) {
									dateInizioProgetto = sdf.parse(dataInizioProgetto); // 10/02/2010
									logger.debug("[AbstractProgettoNG::modelValidate]dateInizioProgetto: " + sdf.format(dateInizioProgetto));
								}
								
								Date dateFineProgetto = null;
								if (dataFineProgetto != null) {
									dateFineProgetto = sdf.parse(dataFineProgetto);
									logger.debug("[AbstractProgettoNG::modelValidate]dateFineProgetto: " + sdf.format(dateFineProgetto));
								}
								
								Date dateInizioProgettoDAO = null;
								if (str_dataInizioProgettoDAO != null && !str_dataInizioProgettoDAO.isEmpty()) {
									dateInizioProgettoDAO	= sdf.parse(str_dataInizioProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate]dateInizioProgettoDAO: " + sdf.format(dateInizioProgettoDAO));
								}
								
								Date dateFineProgettoDAO = null;
								if ((str_dataFineProgettoDAO != null && !str_dataFineProgettoDAO.isEmpty()) && !str_dataInizioProgettoDAO.isEmpty()) {
									dateFineProgettoDAO = sdf.parse(str_dataFineProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate]dateFineProgettoDAO: " + sdf.format(dateFineProgettoDAO));
								}
								
								Date dateMaxInizioProgettoDAO = null;		
								
								if (isPresentDataMaxInizioPrjDAO) {

									if (str_dataMaxInizioProgettoDAO != null) {
										dateMaxInizioProgettoDAO = sdf.parse(str_dataMaxInizioProgettoDAO);
										logger.debug("[AbstractProgettoNG::modelValidate]dateMaxInizioProgettoDAO: " + sdf.format(dateMaxInizioProgettoDAO));
									}
								}else{
									logger.info("dateMaxInizioProgettoDAO: assente in database! - Nessun controllo!");
								}
								
								logger.debug("[AbstractProgettoNG::modelValidate]Inizio controlli ...");
								
								// **************************************************** inizio controlli ***
								// value = 0, data-1 e data-2 risultano uguali
								// value < 0, data-1 risulta < data-2
								// value > 0, data-1 risulta > data-2
								// code01: dataInizioProgetto < dataFineProgetto
								
								/*******************************************************************
								 *  1 controllo: 
								 *  Verifico che data inizio progettoFe sia <= data fine progettoFe:
								 *  ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO
								 */
								logger.debug("[AbstractProgettoNG::modelValidate] 1 controllo: dataInizioProgettoFe: "+dateInizioProgetto+" <= dataFineProgettoFe: "+dateFineProgetto);
								int ret1 = MetodiUtili.confrontaDate(dateInizioProgetto, dateFineProgetto);
								logger.info("ret1 vale: " + ret1);
								boolean code01 = false;
								if (ret1 > 0) {
									code01= true;
									addMessage(newMessages,"_abstractProgetto_dataInizioProgetto",ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO	+ ": " + sdf.format(dateFineProgetto) + " ");
									logger.info("[AbstractProgettoNG::modelValidate] data inizio progetto deve risultare minore od uguale alla data di fine progetto! " );
								}
								
								if (ret1 <= 0) {
									logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progettoFE <= data fine progettoFE : corretta!");
									logger.debug("[AbstractProgettoNG::modelValidate]dataInizioProgettoFe: "+dateInizioProgetto+" e dataFineProgettoFe: "+dateFineProgetto);
								}
								
								/*******************************************
								 *  2 controllo:
								 *  dateInizioProgettoFe >= dateInizioProgettoDAO
								 *  ERRMSG_DATA_INIZIO_PROGETTO_INFERIORE_DTINPRJDB
								 */
								logger.debug("[AbstractProgettoNG::modelValidate] 2 controllo: dataInizioProgettoFe: "+dateInizioProgetto+" >= dateInizioProgettoDAO: "+dateInizioProgettoDAO);
								if (dateInizioProgettoDAO != null) 
								{
									
									int ret2 = MetodiUtili.confrontaDate(dateInizioProgetto, dateInizioProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate]ret2 vale: " + ret2);
									
									if (ret2 >= 0) {
										logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto >= data inizio progetto database: corretto!");
										logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateInizioProgettoDAO));
									}
									else if (ret2 < 0 ) {
										logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto < data inizio progetto database: NON corretto!");
										addMessage(newMessages,"_abstractProgetto_dataInizioProgetto",ERRMSG_DATA_INIZIO_PROGETTO_INFERIORE_DTINPRJDB	+ ": " + sdf.format(dateInizioProgettoDAO) + " ");
										logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateInizioProgettoDAO));
									}
								
								} else {
									logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto assente su database, bypassa il controllo!");
								}
								
								/*******************************************
								 *  3 controllo:
								 *  dateInizioProgettoFe <= dateMaxInizioProgettoDAO
								 *  ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO
								 */
								logger.debug("[AbstractProgettoNG::modelValidate] 3 controllo: dataInizioProgettoFe <= dateMaxInizioProgettoDAO");
								if (dateMaxInizioProgettoDAO != null) 
								{
									int ret3 = MetodiUtili.confrontaDate(dateInizioProgetto, dateMaxInizioProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate]nret3 vale: " + ret3);
									
									if (ret3 > 0 && !code01) 
									{
										logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto > data max inizio progetto database: NON corretto!");
										addMessage( newMessages, "_abstractProgetto_dataInizioProgetto", ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO + ": " + sdf.format(dateMaxInizioProgettoDAO) + " ");
										logger.info(sdf.format(dateInizioProgetto) + " : " + sdf.format(dateMaxInizioProgettoDAO));
									}
									
									if (ret3 <= 0) {
										logger.debug("[AbstractProgettoNG::modelValidate]Data inizio progetto <= data max-inizio progetto database: corretto!");
										logger.info(sdf.format(dateInizioProgetto) + " : " + sdf.format(dateMaxInizioProgettoDAO));
									}
								}else{
									logger.debug("[AbstractProgettoNG::modelValidate]Data Max inizio progetto assente su database, bypassa il controllo!");
								}
								
								/*******************************************
								 *  4 controllo:
								 *  dateInizioProgettoFe <= dateFineProgettoDAO
								 *  ERRMSG_DATA_INIZIO_MINORE_UGUALE_FINE_PROGETTO
								 */
								logger.debug("[AbstractProgettoNG::modelValidate] 4 controllo: dataInizioProgettoFe <= dateFineProgettoDAO");
								if (dateFineProgettoDAO != null) 
								{
									
									int ret4 = MetodiUtili.confrontaDate(dateInizioProgetto, dateFineProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate]ret4 vale: " + ret4);
									
									
									if (ret4 <= 0) {
										logger.debug("[AbstractProgettoNG::modelValidate] La data inizio progetto deve risultare inferiore o uguale alla data fine progetto. NON corretto!!!");
										logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateFineProgettoDAO));
									}else{
										if(!code01){
											logger.debug("[AbstractProgettoNG::modelValidate] Controllo 4 NON corretto!");
											addMessage(newMessages,"_abstractProgetto_dataInizioProgetto", ERRMSG_DATA_INIZIO_MINORE_UGUALE_MAX_FINE_PROGETTO	+ ": " + sdf.format(dateFineProgettoDAO) + " ");
											logger.info(sdf.format(dateInizioProgetto)+" : "+ sdf.format(dateFineProgettoDAO));
										}
									}
									
								}else{
									logger.debug("[AbstractProgettoNG::modelValidate]Data Max inizio progetto assente su database, bypassa il controllo!");
								}
								
								
								/*******************************************
								 *  5 controllo:
								 *  data fine prjFe >= data inizio progetto da db : da verificare ***
								 *  ERRMSG_DATA_FINE_MINORE_DATA_INIZIO
								 */
								logger.debug("[AbstractProgettoNG::modelValidate] 5 controllo: dataFineProgettoFe >= dateInizioProgetto");
								// int ret5 = MetodiUtili.confrontaDate(dateFineProgetto, dateInizioProgetto);
								if (dateInizioProgettoDAO != null) 
								{
									
									int ret5 = MetodiUtili.confrontaDate(dateFineProgetto, dateInizioProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate] ret5 vale: " + ret5); 
									
									if (ret5 >=0 ) {
										logger.debug("[AbstractProgettoNG::modelValidate]Data fine progetto >= data fine progetto: corretto!");
										logger.info(sdf.format(dateFineProgetto)+" : "+ sdf.format(dateInizioProgetto));
									}
									
									if (ret5 < 0 ) {
										logger.debug("[AbstractProgettoNG::modelValidate]Controllo 5 NON superato! - La data di fine progetto deve risultare maggiore od uguale alla data inizio progetto. ");
										addMessage(newMessages,"_abstractProgetto_dataFineProgetto", ERRMSG_DATA_FINE_MINORE_DATA_INIZIO	+ ": " + sdf.format(dateInizioProgetto) + " ");
										logger.info(sdf.format(dateFineProgetto)+" : "+ sdf.format(dateInizioProgetto));
									}
									
								} else {
									logger.debug("[AbstractProgettoNG::modelValidate]data fine prjFe >= data inizio progetto da db , bypasso il controllo!");
								}
								
								
								/*******************************************
								 *  6 controllo:
								 *  data fine prjFe <= dateFineProgettoDAO
								 *  ERRMSG_DATA_FINE_PROGETTO_SUPERIORE
								 */
								logger.debug("[AbstractProgettoNG::modelValidate] 6 controllo: dataFineProgettoFe >= dateFineProgettoDAO ");
								if (dateFineProgettoDAO != null) 
								{
									
									int ret6 = MetodiUtili.confrontaDate(dateFineProgetto, dateFineProgettoDAO);
									logger.debug("[AbstractProgettoNG::modelValidate] ret6 vale: " + ret6);
									
									if (ret6 <=0 ) {
										logger.debug("[AbstractProgettoNG::modelValidate]Data fine progetto >= data fine progetto: corretto!");
										logger.info(sdf.format(dateFineProgetto) +" : "+ sdf.format(dateFineProgettoDAO));
									}
									
									if (ret6 >0 ) { 
										logger.debug("[AbstractProgettoNG::modelValidate]Controllo 6 NON superato!");
										addMessage(newMessages,"_abstractProgetto_dataFineProgetto", ERRMSG_DATA_FINE_PROGETTO_SUPERIORE	+ ": " + sdf.format(dateFineProgettoDAO) + " ");
										logger.info(sdf.format(dateFineProgetto) +" : "+ sdf.format(dateFineProgettoDAO));
									}

								} else {
									logger.debug("[AbstractProgettoNG::modelValidate]data fine prjFe <= dateFineProgettoDAO, bypasso il controllo!");
								}
								
								logger.debug("[AbstractProgettoNG::modelValidate]Fine controlli date!");
								
							} catch (ParseException e) {
								e.printStackTrace();
							}
							// ------ eseguo conversione StringToDate: fine
							
						}else{
							logger.debug("[AbstractProgettoNG::modelValidate] Possibilita presenza errori! Date digitate non valide !!!");
						} 
						// --------------- date risultano valide - fine
						
					}else{
						logger.debug("[AbstractProgettoNG::modelValidate]Campi obbligatori");
					}
					// ---------------idazione presenza data nei campi a video: fine
				}
				else{
					
					if(isPresenteDataInizioProgetto){
						
						// - controllo formato data sia corretto [fatto]
						// - controllo che data_inizio >= findom_t_bandi.dt_inizio_progetto (se non è NULL)
						String dtInizioProgettoDAO = AbstractProgettoNGDAO.getDataInizioProgetto(idBando, logger);
						logger.debug("[AbstractProgettoNG::modelValidate]dtInizioProgettoDAO risulta: " + dtInizioProgettoDAO);
						
						// - controllo che data_inizio <= findom_t_bandi.dt_max_inizio_progetto (se non è NULL)
						String dtMaxInizioProgettoDAO = AbstractProgettoNGDAO.getDataMaxInizioProgetto(idBando, logger);
						logger.debug("[AbstractProgettoNG::modelValidate]dtMaxInizioProgettoDAO risulta: " + dtMaxInizioProgettoDAO);
						
						boolean isDtMaxInizioPrjOk = false;
						if(!StringUtils.isBlank(dtMaxInizioProgettoDAO)){
							isDtMaxInizioPrjOk = verificaDataMaxInizioPrj(dataInizioProgetto, dtMaxInizioProgettoDAO, logger);
							logger.debug("[AbstractProgettoNG::modelValidate]isDtMaxInizioPrjOk vale: " + isDtMaxInizioPrjOk);
						}else{
							isDtMaxInizioPrjOk=true; // bypasso il controllo per data assente su db
						}
						
						if(!isDtMaxInizioPrjOk){
							addMessage( newMessages, "_abstractProgetto_dataInizioProgetto", ERRMSG_VERIFICA_DATA_MAX_INIZIO_PROGETTO);
							logger.debug("[AbstractProgettoNG::modelValidate] data errata ... ");
						}
						
					}else{
						logger.debug("[AbstractProgettoNG::modelValidate]Campo data inizio progetto mancante o non valida!");
					}
				}
				
			}// fine controllo date inizio e fine progetto se _abstractProgetto_data_inizio_fine_progetto == true
			
			
			
			/**   :: Jira 1589 mvd */ 
			if ("true".equals(input._importo_complessivo_business_plan))
			{
				// recupero il valore digitato da utente 
				String importoComplessivoBP = "0";
				
				if(_abstractProgetto.getImporto_complessivo_business_plan() !=null){
					importoComplessivoBP = _abstractProgetto.getImporto_complessivo_business_plan();
					logger.debug("[AbstractProgettoNG::modelValidate]importoComplessivoBP vale: " + importoComplessivoBP);
				}
				
				
				// verifico se campo risulta valorizzato 
				boolean isBlankImportoComplessivoBP = StringUtils.isBlank(importoComplessivoBP);
				logger.debug("[AbstractProgettoNG::modelValidate]isBlankImportoComplessivoBP risulta compilato ? " + isBlankImportoComplessivoBP);
				
				// se non valorizzato, lo segnalo
				if (isBlankImportoComplessivoBP) {
					logger.info("AbstractProgettoNG::modelValidate] _azienda.CapitaleSociale non presente o vuoto");
					addMessage(newMessages,"_abstractProgetto_importo_complessivo_business_plan", ERRMSG_CAMPO_OBBLIGATORIO);
					
				} else if (!importoComplessivoBP.matches("^\\d+(,\\d{1,2})?$")) { // verifica del formato decimale		   
					addMessage(newMessages,"_abstractProgetto_importo_complessivo_business_plan", ERRMSG_FORMATO_DECIMALE);
					logger.info("AbstractProgettoNG::modelValidate]  il importoComplessivoBP deve essere numerico con al max due decimali");
				}
			}
			
			//metodo generico che tramite reflection chiama eventuali ulteriori metodi di validazione, 
			//i cui nomi e argomenti sono contenuti nella var di configurazione validationMethodsAbstractProgettoNG
			logger.info("[AbstractProgettoNG::modelValidate()] inizio validazioni definite da variabile di configurazione");
			ArrayList<SegnalazioneErrore> segnalazioneList = ValidationUtil.validate(AbstractProgettoNGValidationMethods.class, input, logger);
			logger.debug("[AbstractProgettoNG::modelValidate]test: input: " + input);
			
			if(segnalazioneList!=null && !segnalazioneList.isEmpty()){
				for (SegnalazioneErrore segnalazioneErrore : segnalazioneList) {
					addMessage(newMessages, segnalazioneErrore.getCampoErrore(), MetodiUtili.prefixErrMsg(newMessages,segnalazioneErrore));			
				}
			}
			logger.info("[AbstractProgettoNG::modelValidate()] fine validazioni definite da variabile di configurazione");
		}
					
		return newMessages;
	} 
	

	private boolean verificaDataMaxInizioPrj(String dataInizioProgetto, String dtMaxInizioProgettoDAO, Logger logger) {
		/**
		 * condizione valida richiesta:
		 * dataInizioProgetto >= dtInizioProgettoDAO
		 */
		boolean ris = false;
		
		Date dataInizioPrj = null;
		Date dataMaxInizioPrjDB = null;
		
		dataInizioPrj = MetodiUtili.convertStringToDate(dataInizioProgetto, logger);
		dataMaxInizioPrjDB = MetodiUtili.convertStringToDate(dtMaxInizioProgettoDAO, logger);
		
		
		int confrontoDate = 0;
		confrontoDate = MetodiUtili.confrontaDate(dataInizioPrj, dataMaxInizioPrjDB);
		logger.debug("[AbstractProgettoNG::verificaDataMaxInizioPrj esito confronto date: " + confrontoDate);
		
		return ris;
	}

	private boolean verificaDataInizioPrj(String dataInizioProgetto, String dtInizioProgettoDAO, Logger logger) {
		/**
		 * condizione valida richiesta:
		 * dataInizioProgetto >= dtInizioProgettoDAO
		 */
		boolean ris = true;
		
		Date dataInizioPrj = null;
		Date dataInizioPrjDB = null;
		
		dataInizioPrj = MetodiUtili.convertStringToDate(dataInizioProgetto, logger);
		dataInizioPrjDB = MetodiUtili.convertStringToDate(dtInizioProgettoDAO, logger);
		
		int confrontoDate = 0;
		confrontoDate = MetodiUtili.confrontaDate(dataInizioPrj, dataInizioPrjDB);
		logger.info("esito confronto date: " + confrontoDate);
		
		// value = 0, dataInizioPrj e dataInizioPrjDB risultano uguali
		// value < 0, dataInizioPrj risulta < dataInizioPrjDB
		// value > 0, dataInizioPrj risulta > dataInizioPrjDB
		if(confrontoDate<0) ris = false;
		
		return ris;
	}

	private static boolean validateMessageText(String space)
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
	

}
