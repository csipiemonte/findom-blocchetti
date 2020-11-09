/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.accessoForbidden;

import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AccessoForbidden extends Commonality {

	  AccessoForbiddenInput input = new AccessoForbiddenInput();

	  @Override
	  public AccessoForbiddenInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[AccessoForbidden::inject] BEGIN");
	    try {
	        AccessoForbiddenOutput output = new AccessoForbiddenOutput();
	        
	        
	        boolean accessoForbidden = false; // variabile per controllo che l'utente non cerchi di entrare in questa pagina se prima non ha compilato la S1_P1
			
			if (!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
				
					OperatorePresentatoreVo _operatorePresentatore = (OperatorePresentatoreVo) input._operatorePresentatore;
					if(_operatorePresentatore == null){
						accessoForbidden = true;
					}
			}
			logger.debug("[AccessoForbidden::inject] accessoForbidden="+accessoForbidden);
	        output.setAccessoForbidden(accessoForbidden);
	        
	        return output;
	    }
	    finally {
	      logger.info("[AccessoForbidden::inject] END");
	    }
	  }

	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[AccessoForbidden::modelValidate] _accessoForbidden  BEGIN");
			
		logger.info("[AccessoForbidden::modelValidate] _accessoForbidden END");
		
		return newMessages;

	  }
	  
	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }


}
