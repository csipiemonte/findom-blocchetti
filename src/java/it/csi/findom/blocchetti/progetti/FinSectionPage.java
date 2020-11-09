/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti;

import freemarker.core.Environment.Namespace;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.findom.blocchetti.vo.FinUserInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractSectionPage;

import java.util.List;
import java.util.TreeMap;

public abstract class FinSectionPage extends AbstractSectionPage {

  public FinSectionPage(TreeMap<String, Object> context, Namespace namespace, TreeMap<String, Object> model) {
    super(context,namespace,model);
  }
  
  
	@Override
	protected void initConfigurations() {
		
	}

	@Override
	public void postModelValidate(CommonInfo arg0, List<CommonalityMessage> arg1) {
		// nothing for domweb
	}
	
	@Override
	protected CommonInfo createCommonInfo() {
		FinCommonInfo commonInfo = new FinCommonInfo();
		
		commonInfo.setApplicativo((String)context.get("applicativo"));
		commonInfo.setLoggerName((String)context.get("loggerName"));
		commonInfo.setUserInfo((FinUserInfo) context.get("userInfo"));
		commonInfo.setStatusInfo((FinStatusInfo) context.get("statusInfo"));
		
		// da fare prima quello sotto?
		
		commonInfo.setCurrentPage((String)context.get("xpageXp"));
		commonInfo.setFormState((String) context.get("xformState"));
		commonInfo.setCurrentSect((String)context.get("xsectXp"));
		
		if (commonInfo.getFormState()!=null) {
			if (commonInfo.getFormState().equals("BZ") || 
				commonInfo.getFormState().equals("OK") || 
				commonInfo.getFormState().equals("OW") || 
				commonInfo.getFormState().equals("KO"))
			{
				commonInfo.setRWByState(true);
			}
			else
			{
				commonInfo.setRWByState(false);
			}
		}
		
		
		
		return commonInfo;
	}
	
}
