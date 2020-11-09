/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1.P3;

import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1.Section;
import it.csi.findom.blocchetti.blocchetti.legalerappresentante.LegaleRappresentante;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractSection;
import it.csi.melograno.aggregatore.business.javaengine.commonality.IndexStatus;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.findom.blocchetti.blocchetti.accessoForbidden.AccessoForbidden;
import it.csi.findom.blocchetti.progetti.FinSectionPage;
import freemarker.core.Environment.Namespace;
import org.apache.log4j.Logger;
import java.util.TreeMap;

public class Page extends FinSectionPage { // refactoring

	private PageInput input = new PageInput();
	
	public Page(TreeMap<String, Object> context, Namespace namespace, TreeMap<String, Object> model) {
		super(context, namespace, model);
	}

	protected void initConfigurations() {
	}

	// estensione possibile
	protected void customInitializeData() {
		// valorizzare eventuali variabili di classe specifiche della sezione ( esempio
		// imInAziendale)
	}

	protected void customInitializeCommand(String commandLabel) {
		// gestione commands (reload gia' gestito in super classe)
	}

	public Class[] getCommonalities() {
		
		return new Class[] { AccessoForbidden.class, LegaleRappresentante.class};
	}

	@Override
	public AbstractSection getParentSection() {
		return new Section(context, namespace, model);
	}

	public IndexStatus getInternalIndexStatus(CommonInfo info) {
		// la pagina "Anagrafica/Riferimenti" risulta abilitata solo se l'operatore presentatore risulta esistere
	    IndexStatus is = new IndexStatus();
	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[Page3::getInternalIndexStatus] BEGIN");
	    logger.info("[Page3::getInternalIndexStatus] input.operatorePresentatore=" +input.operatorePresentatore);

	    is.setIndexItemEnabled(false);
	    if(input.operatorePresentatore!=null) {
	        is.setIndexItemEnabled(true);
		    logger.info("[Page3::getInternalIndexStatus] is.getIndexItemEnabled=true");
	    }
	    
	    logger.info("[Page3::getInternalIndexStatus] END");
	    return is;
	}
	
	@Override
	public SectionPageInput getInput() {
		return input;
	}
	@Override
	public String getReloadCommandLabel() {
		return "";
	}
}
