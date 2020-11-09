/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.estremiBancari;

import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.commonality.ControlIban;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class EstremiBancari extends Commonality {

	  EstremiBancariInput input = new EstremiBancariInput();

	  @Override
	  public EstremiBancariInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[EstremiBancari::inject] BEGIN");
	    
	    String intestatarioCC = "";
	    
	    try {
	    	
	        EstremiBancariOutput output = new EstremiBancariOutput();
	        
	        /** jira: 2018 - inizio */
	        if ("true".equals(input._estremiBancari_intestatario_denominazione))
		    {
	        	
	        	OperatorePresentatoreVo _operatorePresentatore = input.operatorePresentatore;
	        	
	        	intestatarioCC = (String) _operatorePresentatore.getDenominazione();
	        	logger.info("[EstremiBancari::inject] intestatarioCC: " + intestatarioCC);
	        	
	        	if (StringUtils.isBlank(intestatarioCC)) {
	        		logger.info("[EstremiBancari::inject] intestatarioCC non valorizzato");
	        		
	        	} else {
	        		logger.info("[EstremiBancari::inject] intestatarioCC risulta: "+intestatarioCC);
	        	}
		    }
	  			
  			/** jira: 2018 - fine */
  			
	  			
	        if(isBeneficiarioAziendaEstera(info)){
	        	output.setMaxLengthIBAN(60);
	        	output.setMaxLengthBIC(20);
	        } else {
	        	output.setMaxLengthIBAN(27);
	        	output.setMaxLengthBIC(11);
	        }
	        
	        /** jira 2018 - */
	        if ("true".equals(input._estremiBancari_intestatario_denominazione))
		    {
	        	output.setIntestatarioCC(intestatarioCC);
		    }
	        
	        return output;

	    } finally {
	      logger.info("[EstremiBancari::inject] END");
	    }
	  }

	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[EstremiBancari::modelValidate] _costituzioneImpresa  BEGIN");
			
		String ERRMSG_CAMPO_OBBLIGATORIO_EB = "- il campo &#233; obbligatorio";
		String ERRMSG_EB_IBAN = "- Verificare il codice IBAN";
		String ERRMSG_EB_BIC = "- Il campo BIC &#233; formalmente non corretto";
		String ERRMSG_EB_IBAN_DUPLICATO = "ATTENZIONE - L'utente ha gi&#224; presentato domanda oppure &#232; gi&#224; presente una domanda contenente lo stesso IBAN";
		String ERRMSG_EB_IBAN_IMPREVISTO = "- ATTENZIONE - Il salvataggio non &#233; andato a buon fine";

		try {

	  		EstremiBancariVO _estremiBancari = input._estremiBancari;
	  		
	  		if (_estremiBancari != null) {
	  			logger.info("[EstremiBancari::modelValidate] _estremiBancari:" + _estremiBancari);

	  			logger.info("[EstremiBancari::modelValidate] isBeneficiarioAziendaEstera=" + isBeneficiarioAziendaEstera(info));
	  			
	  			String iban = (String) _estremiBancari.getIban();
				logger.info("[EstremiBancari::modelValidate] iban:" + iban);
				
				// eseguo conversione in maiuscolo del codice iban digitato 
  				iban = iban.toUpperCase();
  				logger.info("[EstremiBancari::modelValidate] iban toUppercase : " + iban);
  				
				if(isBeneficiarioAziendaEstera(info)){
					// azienda estera
					if (StringUtils.isBlank(iban)) {
						addMessage(newMessages,"_estremiBancari_iban", ERRMSG_CAMPO_OBBLIGATORIO_EB);
						logger.warn("[EstremiBancari::modelValidate] iban non valorizzato");
					}else if (iban.length() > 40){
						addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
						logger.warn("[EstremiBancari::modelValidate] iban > 40 caratteri");
					}else if (!StringUtils.isAlphanumeric(iban)){
						addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
						logger.warn("[EstremiBancari::modelValidate] iban contenente caratteri non alfanumerici");	
					}
					
				}else{
					
					/** : jira 1900 - bando bonus piemonte - */
					if( input._estremiBancari_duplicazioneIban.equals("true")) 
					{
		  				boolean error = false;
		  				
		  				if (StringUtils.isBlank(iban)) {
							addMessage(newMessages,"_estremiBancari_iban", ERRMSG_CAMPO_OBBLIGATORIO_EB);
							logger.warn("[EstremiBancari::modelValidate] iban non valorizzato");
							error=true;
						}
						else 
							if (iban.length() != 27){
								addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
								logger.warn("[EstremiBancari::modelValidate] iban non ha 27 caratteri");
								error=true;
							}
							else 
								if (org.apache.commons.lang3.StringUtils.containsWhitespace(iban)) {
									addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
									logger.warn("[EstremiBancari::modelValidate] iban contenente spazi");	
									error=true;
								}
								else 
									if (!ControlIban.isValid(iban)) {
										addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
										logger.warn("[EstremiBancari::modelValidate] iban contenente caratteri non alfanumerici");	
										error=true;
									}
		  				
		  				FinStatusInfo statusInfo = info.getStatusInfo();
		  				boolean isIbanDuplicato = false;
		  				boolean isSuccessUpdate = false;

		  				String codiceIbanXML = ""; 
		  				String cfBeneficiario = statusInfo.getCodFiscaleBeneficiario();
		  				logger.info("cfBeneficiario: " + cfBeneficiario);
		  				
		  				if(!error) 
		  				{
		  					logger.info("recupero iban salvato su xml ");
		  					codiceIbanXML = EstremiBancariDAO.getCodiceIban(info.getStatusInfo().getNumProposta());
		  					logger.info("codiceIbanXML: " + codiceIbanXML);
		  					
		  					
		  					if (StringUtils.isBlank(codiceIbanXML))
		  					{
		  						logger.debug(" iban su xml assente, soggetto ha appena creato la domanda ");
		  						isIbanDuplicato = isIbanPresentOnDB(iban, cfBeneficiario, logger);
			  					
			  					if(isIbanDuplicato){
			  						addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN_DUPLICATO);
			  						logger.warn("[EstremiBancari::modelValidate] codice iban risulta presente a sistema");
			  						
			  					}else{
			  						isSuccessUpdate = registerIbanOnDb(iban, cfBeneficiario, logger);
			  						logger.info("[EstremiBancari::modelValidate] registazione iban eseguita su database, su xml al temine del salva pagina");
			  						
			  						if(!isSuccessUpdate){
			  							logger.info("update non andato a buon fine: " + isSuccessUpdate);
			  							addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN_IMPREVISTO);
			  							logger.warn("[EstremiBancari::modelValidate] ATTENZIONE - update non andato a buon fine");
			  						}
			  					}
			  				 }
		  					 else {
				  					logger.debug("verifico se su db risuta gia presente un iban escludendo dal controllo il cf e piva del soggetto che ha salvato iban la prima volta... ");
				  					isIbanDuplicato = isIbanUniqueOnDB(iban, cfBeneficiario, logger);
				  					
				  					if(isIbanDuplicato){
				  						addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN_DUPLICATO);
				  						logger.warn("[EstremiBancari::modelValidate] codice iban risulta presente a sistema");
				  						
				  					}else{
				  						logger.info("non esiste a livello di bando, aggiorno iban presente del cf o piva che sta compilando domanda ...");
				  						isSuccessUpdate = registerIbanOnDb(iban, cfBeneficiario, logger);
				  						
				  						if(!isSuccessUpdate){
				  							logger.info("update non andato a buon fine: " + isSuccessUpdate);
				  							addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN_IMPREVISTO);
				  							logger.warn("[EstremiBancari::modelValidate] ATTENZIONE - update non andato a buon fine");
				  						}
				  					}
		  					}
		  				}// fine blk-assenza errori
		  				
					  } else {
						  // std
						  if (StringUtils.isBlank(iban)) {
							  addMessage(newMessages,"_estremiBancari_iban", ERRMSG_CAMPO_OBBLIGATORIO_EB);
							  logger.warn("[EstremiBancari::modelValidate] iban non valorizzato");
						  }
						  else 
							  if (iban.length() != 27){
								  addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
								  logger.warn("[EstremiBancari::modelValidate] iban non ha 27 caratteri");
							  }
							  else 
								  if (org.apache.commons.lang3.StringUtils.containsWhitespace(iban)) {
									  addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
									  logger.warn("[EstremiBancari::modelValidate] iban contenente spazi");	
								  }
								  else 
									  if (!ControlIban.isValid(iban)) {
										  addMessage(newMessages,"_estremiBancari_iban", ERRMSG_EB_IBAN);
										  logger.warn("[EstremiBancari::modelValidate] iban contenente caratteri non alfanumerici");	
									  }
					  }
				}

	  			// BIC
	  			String bic = (String) _estremiBancari.getBic();
	  			logger.info("[EstremiBancari::modelValidate] bic:" + bic);
	  			
	  			if(isBeneficiarioAziendaEstera(info)){
	  				// 	azienda estera
		  			if (StringUtils.isBlank(bic)) {
		  			   addMessage(newMessages,"_estremiBancari_bic", ERRMSG_CAMPO_OBBLIGATORIO_EB);
		  			   logger.warn("[EstremiBancari::modelValidate] il campo BIC non valorizzato");
		  			   
		  			} 
		  			else 
		  				if ( bic.length() > 14){
		  					addMessage(newMessages,"_estremiBancari_bic", ERRMSG_EB_BIC);
		  					logger.warn("[EstremiBancari::modelValidate]  il campo BIC > 14 caratteri");
		  			
		  				}else 
		  					if (!StringUtils.isAlphanumeric(bic)) {
		  						addMessage(newMessages,"_estremiBancari_bic", ERRMSG_EB_BIC);
		  						logger.warn("[EstremiBancari::modelValidate] il campo BIC contiene caratteri non alfanumerici");
		  			}
	  			}else{ 
	  				
	  				/** bando bonus piemonte */
					if( input._estremiBancari_bicObbligatorio.equals("false")) 
					{
						logger.info("[EstremiBancari::modelValidate] non risulta obbligatorio il campo bic ");
						// versione std
						if (StringUtils.isBlank(bic)) {
							addMessage(newMessages,"_estremiBancari_bic", ERRMSG_CAMPO_OBBLIGATORIO_EB);
							logger.warn("[EstremiBancari::modelValidate] il campo BIC non valorizzato");
						} 
						else 
							if ((bic.length() != 8) && (bic.length() != 11)) {
								addMessage(newMessages,"_estremiBancari_bic", ERRMSG_EB_BIC);
								logger.warn("[EstremiBancari::modelValidate]  il campo BIC non ha 8 o 11 caratteri");
							}
							else 
								if (!StringUtils.isAlphanumeric(bic)) {
									addMessage(newMessages,"_estremiBancari_bic", ERRMSG_EB_BIC);
									logger.warn("[EstremiBancari::modelValidate] il campo BIC contiene caratteri non alfanumerici");
								}
					}
	  			}
	  			
	  			// intestatario
	  			String intestatarioCC = (String) _estremiBancari.getIntestatarioCC();
	  			if (StringUtils.isBlank(intestatarioCC)) {
	  			   addMessage(newMessages,"_estremiBancari_intestatarioCC", ERRMSG_CAMPO_OBBLIGATORIO_EB);
	  			   logger.warn("[EstremiBancari::modelValidate] intestatarioCC non valorizzato");
	  			   
	  			} else {
	  			  logger.debug("[EstremiBancari::modelValidate] intestatarioCC:" + intestatarioCC);
	  			}
	  			
	  		} else {
	  			logger.warn("[EstremiBancari::modelValidate] _estremiBancari non presente o vuoto");
	  		}
		  
		} catch(Exception ex) {
			logger.error("[EstremiBancari::modelValidate] ", ex);
		}
		finally {
			logger.info("[EstremiBancari::modelValidate] _estremiBancari  END");
		}

		return newMessages;

	  }

	private boolean registerIbanOnDb(String iban, String cfBeneficiario, Logger logger) throws CommonalityException {
		return EstremiBancariDAO.updateIbanByCf(iban, cfBeneficiario, logger);
	}

	private boolean isIbanPresentOnDB(String iban, String cfBeneficiario, Logger logger) throws CommonalityException {
		return EstremiBancariDAO.getIsIbanDuplicato(iban, cfBeneficiario, logger);
	}
	
	private boolean isIbanUniqueOnDB(String iban, String cfBeneficiario, Logger logger) throws CommonalityException {
		return EstremiBancariDAO.getIsIbanUniqueOnDB(iban, cfBeneficiario, logger);
	}
	  
	/**
	 * Restituisce TRUE se il beneficiario loggato non e' italiano
	 * ( ossia se shell_t_soggetti.sigla_nazione != null AND !='000' )
	 * @param info
	 * @return
	 */
	  private boolean isBeneficiarioAziendaEstera(FinCommonInfo info) {
		boolean ret = false;
		String sn = info.getStatusInfo().getSiglaNazione();
		if (StringUtils.isNotBlank(sn) && !StringUtils.equals(sn,"000")){
			ret = true;
		}
		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[EstremiBancari::isBeneficiarioAziendaEstera] ="+ret);
		return ret;
	  }

	@Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }


}
