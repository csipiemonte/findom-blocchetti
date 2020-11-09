/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFiera;

import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaDettaglioInterventoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DescrizioneFiera extends Commonality{
	
	DescrizioneFieraInput input = new DescrizioneFieraInput();

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0,
			List<CommonalityMessage> arg1) throws CommonalityException {
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[DescrizioneFiera::inject] BEGIN");
		
		String descrizioneStato = "";
		String nomeFiera = "";
		String statoEstero = "";
		
		try 
		{
			DescrizioneFieraOutput ns = new DescrizioneFieraOutput();
			logger.info("[DescrizioneFiera::inject] inizializzo ns ");
			
			List<StatoEsteroVO> statoEsteroSLList = new ArrayList<StatoEsteroVO>();
			
			DescrizioneFieraVO _descrizioneFiera = input.descrizioneFiera;
			logger.info("[DescrizioneFiera::inject] inizializzo descrizioneFiera ");
			
			if( _descrizioneFiera != null && !_descrizioneFiera.isEmpty()){
				
				descrizioneStato = _descrizioneFiera.getStatoEsteroDescrizione();
				logger.info("[DescrizioneFiera::inject] descrizioneStato risulta " + descrizioneStato);
				
				nomeFiera = _descrizioneFiera.getNomeFiera();
				logger.info("[DescrizioneFiera::inject] nomeFiera risulta " + nomeFiera);
				
				statoEstero = _descrizioneFiera.getStatoEstero();
				_descrizioneFiera.setStatoEstero(statoEstero);
				logger.info("[DescrizioneFiera::inject] statoEstero risulta " + statoEstero);
				
				
			}else{
				logger.info("[DescrizioneFiera::inject] _descrizioneFiera risulta vuoto");
			}
			
			
			String operazione = "INS";
			Integer idDomanda = info.getStatusInfo().getNumProposta();
			logger.info("[DescrizioneFiera::inject] idDomanda: " + idDomanda);
			
			String descrizioneFieraDaDomanda = DescrizioneFieraDAO.getNodoDescrizioneFiera(idDomanda);
			logger.info("[DescrizioneFiera::inject] descrizioneFieraDaDomanda: " + descrizioneFieraDaDomanda);
			
			if(descrizioneFieraDaDomanda != null){
				logger.info("[DescrizioneFiera::inject] descrizioneFieraDaDomanda != null ");
				logger.info("[DescrizioneFiera::inject] operazione MOD ");
				String descrizione = descrizioneFieraDaDomanda;
				logger.info("[DescrizioneFiera::inject] descrizione" + descrizione);
				operazione = "MOD";
			}
			
			
			
			logger.info("[DescrizioneFiera::inject] operazione" + operazione);
			
			if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
				
				// se bando non standard
				statoEsteroSLList  = LuoghiDAO.getStatoEsteroListSenzaItalia(logger);
				
				int dimElencoStatoEstero = statoEsteroSLList.size();
				logger.info("[DescrizioneFiera::inject] dimElencoStatoEstero risulta di dimensioni:" + dimElencoStatoEstero);
				
			}
			
			ns.setStatoEsteroSLList(statoEsteroSLList);
			ns.setStatoEstero(statoEstero);
			
			logger.info("[DescrizioneFiera::inject] _decrizioneFiera END");
			return ns;
		}
		catch (Exception ex) {
			throw new CommonalityException(ex);
		} 
		finally {
			logger.info("[DescrizioneFiera::inject] END");
		}
	}

	/**
	 * Model Validate
	 */
	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> arg1) throws CommonalityException {
		
		FinCommonInfo info = (FinCommonInfo) info1;
		
		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());
		
		logger.info("[DescrizioneFiera::modelValidate]  BEGIN");
		
		/**
		 * COSTANTI per msg di errore
		 */
		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo risulta obbligatorio";
		String ERRMSG_CAMPO_NAZIONE_FEU = "- la Nazione selezionata risulta fuori Europa!";
		String ERRMSG_CAMPO_NAZIONE_IEU = "- la Nazione selezionata risulta in Europa!";
		
		DescrizioneFieraVO _descrizioneFiera = input.descrizioneFiera;
		
		String siglaContinente = "";
		/**
		 * Inizio con la validazione campi, 
		 * presenti in template.xhtml
		 */
		if( _descrizioneFiera != null)
		{
			logger.info("[DescrizioneFiera::modelValidate] _descrizioneFiera:" + _descrizioneFiera);
			
			//controllo singoli campi
			logger.info("[DescrizioneFiera::modelValidate] [DescrizioneFiera.java] controllo singoli campi: ");
			
			
			/**
			 * Nome della fiera:
			 * - recuperato da Oggetto: DescrizioneFieraVO
			 */
			String nomeFiera =  _descrizioneFiera.getNomeFiera();
			logger.info("[DescrizioneFiera::modelValidate] nomeFiera risulta: " + nomeFiera);
			
            if (StringUtils.isBlank(nomeFiera)) {
				addMessage(newMessages,"_descrizioneFiera_nomeFiera", ERRMSG_CAMPO_OBBLIGATORIO);
				logger.warn("[DescrizioneFiera::modelValidate] nomeFiera non valorizzato");
			}
            
            
         // Jira 905 Voucher: recupero nodo caratteristicheProgetto
             Map<String,String> tipolIntervCheckedMap = new HashMap<String,String>();
             
             String idTipoIntervento = "";
             String idDettIntervento = "";
             String idDettaglioInterventoSelezionato = "";
             Boolean isDettaglioInterventoEuropa = false;
             String descrDettIntervento = "";
             
             logger.info("[DescrizioneFiera::modelValidate] recupero il nodo caratteristicheProgetto: ");
     		CaratteristicheProgettoNGVO caratteristicheProgettoMap = input.caratteristicheProgetto; 
     		
     		if(caratteristicheProgettoMap!=null)
     		{
     			logger.info("[DescrizioneFiera::modelValidate] se il nodo caratteristicheProgetto non risulta vuoto: ");
     			logger.info("[DescrizioneFiera::modelValidate] recupero le tipologie di intervento: ");
     			TipologiaInterventoVO[] tipologiaInterventoList = caratteristicheProgettoMap.getTipologiaInterventoList();  //presa da xml
     			
     			if(tipologiaInterventoList != null && tipologiaInterventoList.length>0)
     			{
     				logger.info("[DescrizioneFiera::modelValidate] se le tipologie di intervento sono presenti, le ciclo: ");
     				for(int i=0; i<tipologiaInterventoList.length;i++)
     				{
     					logger.info("[DescrizioneFiera::modelValidate] definisco una Mappa di tipologie di intervento: ");
     					TipologiaInterventoVO tipolIntMap = tipologiaInterventoList[i];
     					
     					String codTipoIntervento = (String)tipolIntMap.getCodTipoIntervento();
     					logger.info("[DescrizioneFiera::modelValidate] recupero codice tipo intervento: " + codTipoIntervento);
     					
     					String chek = (String)tipolIntMap.getChecked();	
     					logger.info("[DescrizioneFiera::modelValidate] verifico la check: " + chek);
     					
     					logger.debug("[DescrizioneFiera::modelValidate] codTipoIntervento = "+codTipoIntervento+" chek="+chek);
     					
     					idTipoIntervento = (String)tipolIntMap.getIdTipoIntervento();
     					logger.info("[DescrizioneFiera::modelValidate] recupero la idTipoIntervento: " + idTipoIntervento);
     					
     					if("true".equals(chek))
     					{
     						logger.info("[DescrizioneFiera::modelValidate] se la check risulta a true: ");
     						
     						TipologiaDettaglioInterventoVO[] dettaglioInterventoList = tipolIntMap.getDettaglioInterventoList();
     						logger.info("[DescrizioneFiera::modelValidate] recupero la lista di dettaglio intervento: ");
     						
     						if(dettaglioInterventoList!=null && dettaglioInterventoList.length>0)
     						{
     							logger.info("[DescrizioneFiera::modelValidate] se la lista di dettaglio intervento risulta piena: si, vi ciclo ");
     							
     							for(int j=0; j<dettaglioInterventoList.length;j++)
     							{
     								TipologiaDettaglioInterventoVO interventoMap = dettaglioInterventoList[j];
     								logger.info("[DescrizioneFiera::modelValidate] definisco una Mappa con elenco dettaglio interventiList ");
     								
     								String chekInt = (String)interventoMap.getChecked();
     								logger.info("[DescrizioneFiera::modelValidate] recupero la check: " + chekInt);
     								
     								descrDettIntervento = (String) interventoMap.getDescrDettIntervento();
     								logger.info("[DescrizioneFiera::modelValidate] recupero la descrDettIntervento: " + descrDettIntervento);
     								
     								idDettIntervento = (String)interventoMap.getIdDettIntervento();	
     								logger.info("[DescrizioneFiera::modelValidate] recupero la idDettIntervento: " + idDettIntervento);
     								
     								logger.debug("[DescrizioneFiera::modelValidate]idDettIntervento = "+idDettIntervento+" chekInt="+chekInt);
     								
     								
     								if("true".equals(chekInt)){
     									logger.info("[DescrizioneFiera::modelValidate] se la check risulta a true: " + chekInt);
     									logger.info("[DescrizioneFiera::modelValidate] la inserisco nella mappa: tipolIntervCheckedMap");
     									tipolIntervCheckedMap.put(idTipoIntervento+"-"+idDettIntervento,"0");
     									idDettaglioInterventoSelezionato = idDettIntervento;
     									logger.info("[DescrizioneFiera::modelValidate] memorizzo idDettaglioInterventoSelezionato: " + idDettaglioInterventoSelezionato);
     									
     									descrDettIntervento = (String) interventoMap.getDescrDettIntervento();
         								logger.info("[DescrizioneFiera::modelValidate] recupero la descrDettIntervento: " + descrDettIntervento);
         								if (descrDettIntervento.equalsIgnoreCase("Europa")) {
											isDettaglioInterventoEuropa = true;
										}
     								}
     							}
     						}else{
     							tipolIntervCheckedMap.put(idTipoIntervento+"-","0");
     						}
     					}
     				}
     			} else {		
     				logger.debug( "[DescrizioneFiera::modelValidate] tipologiaInterventoList NULL or Empty");
     			}
     		} else {
     			// se siamo qui, e' gia' stato segnalato l'errore alla REGOLA V9	
     			logger.debug( "[DescrizioneFiera::modelValidate] caratteristicheProgettoMap NULL");
     		}
     		
     		/**
             * Stato selezionato
             */
            String statoEstero =  _descrizioneFiera.getStatoEstero(); // Es.: 201
            logger.info("[DescrizioneFiera::modelValidate] statoEstero risulta: " + statoEstero);
            
            
     		if (statoEstero != null && !statoEstero.isEmpty()) 
     		{
				siglaContinente = LuoghiDAO.getSiglaContinenteByCodStato(statoEstero);
				
			}
     		
	     	logger.info("[DescrizioneFiera::modelValidate] siglaContinente recuperata da db risulta: " + siglaContinente);
	     		
	     	boolean isEuropa = false;
	     	if(siglaContinente.equalsIgnoreCase("EU")){
	     		logger.warn("[DescrizioneFiera::modelValidate] siglaContinente risulta: " + siglaContinente);
	     		isEuropa = true;
	     	}else{
	     		logger.info("[DescrizioneFiera::modelValidate] NON risulta una Nazione EUROPEA: ");
	     	}
	     	
	     	/**
	     	 * isEuropa = true ( Selezionato Nazione presente in Europa)
	     	 * isDettaglioInterventoEuropa = true ( Selezionato checkbox = 65 : Europa )
	     	 */
	     	// se 65 e null, errore
	     	if(idDettaglioInterventoSelezionato.equalsIgnoreCase("65") && !isEuropa && (statoEstero != null && !statoEstero.isEmpty()) )
	     	{
	     		addMessage(newMessages,"_descrizioneFiera_statoEstero", ERRMSG_CAMPO_NAZIONE_FEU);
				logger.warn("[DescrizioneFiera::modelValidate] statoEstero risulta fuori Europa");
	     	}
	     	
	     	// se 66 e EU : Errore
	     	if(idDettaglioInterventoSelezionato.equalsIgnoreCase("66") && isEuropa && statoEstero != null && !statoEstero.isEmpty())
	     	{
	     		addMessage(newMessages,"_descrizioneFiera_statoEstero", ERRMSG_CAMPO_NAZIONE_IEU);
				logger.warn("[DescrizioneFiera::modelValidate] statoEstero in Europa");
	     	}
     		
     		if (StringUtils.isBlank(statoEstero)) {
				addMessage(newMessages,"_descrizioneFiera_statoEstero", ERRMSG_CAMPO_OBBLIGATORIO);
				logger.warn("[DescrizioneFiera::modelValidate] statoEstero non valorizzato");
			}
			else {				
				logger.info("[DescrizioneFiera::modelValidate] statoEstero: " + statoEstero);
			}
     		
		}else {
			logger.error("[DescrizioneFiera::modelValidate] DescrizioneFiera non presente o vuoto");	
		}
		
		return newMessages;
	} 

}
