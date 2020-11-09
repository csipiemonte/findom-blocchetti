/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.urlPagRegoleCompilazione;

import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class UrlPagRegoleCompilazione extends Commonality {

	  UrlPagRegoleCompilazioneInput input = new UrlPagRegoleCompilazioneInput();

	  @Override
	  public UrlPagRegoleCompilazioneInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[UrlPagRegoleCompilazione::inject] BEGIN");
	    try {
	        UrlPagRegoleCompilazioneOutput output = new UrlPagRegoleCompilazioneOutput();
			
			   // compongo l'url della pagina "Regole di compilazione"
	        String urlPagRegoleCompilazione = "";
	        String aggrMode = "";
	        if(!info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")){
	        	aggrMode="AGGR_modeId=ACCESS_RW_MODE";
	        }else{
	        	aggrMode="AGGR_modeId=ACCESS_RO_MODE";
	        }

	        String command = "&_command=ACCESS_PAGE";
			String aggrFormId = "&AGGR_formId=" + input.xformId;
			String aggrFormPr = "&AGGR_formProg=" + input.xformProg;
			String aggrSecId  = "&AGGR_sectId=9000";  // fisso, dipende dagli indici
			// devo spezzare in sottostringhe perche altrimenti freemarker non parsifica
			String aggrSectXp = "&AGGR_sectXp=/" + "/*[" + "@" + "id='S9']"; // fisso, dipende dagli indici
			String aggrPageId = "&AGGR_pageId=9100"; // fisso, dipende dagli indici
			// devo spezzare in sottostringhe perche altrimenti freemarker non parsifica
			String aggrPageXp = "&AGGR_pageXp=/" + "/*[" + "@" + "id='S9_P1']"; // fisso, dipende dagli indici
			String aggrState = "&AGGR_formState=" + input.xformState;
			
//			urlPagRegoleCompilazione = "/findomwebnew/aggregatoreUtil/exec.do?" + aggrMode + command + aggrFormId + aggrFormPr + aggrSecId + aggrSectXp + aggrPageId + aggrPageXp + aggrState;
			urlPagRegoleCompilazione = "/"+info.getStatusInfo().getContextSportello()+"/aggregatoreUtil/exec.do?" + aggrMode + command + aggrFormId + aggrFormPr + aggrSecId + aggrSectXp + aggrPageId + aggrPageXp + aggrState;
			
			//// namespace				
			output.setUrlPagRegoleCompilazione(urlPagRegoleCompilazione);
			logger.info("[UrlPagRegoleCompilazione::inject] urlPagRegoleCompilazione=["+urlPagRegoleCompilazione+"]");
			
	        return output;
	    }
	    finally {
	      logger.info("[UrlPagRegoleCompilazione::inject] END");
	    }
	  }

	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
		return newMessages;

	  }
	  
	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }


}
