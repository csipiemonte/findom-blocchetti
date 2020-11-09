/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S3.P2;

import it.csi.findom.blocchetti.blocchetti.urlPagRegoleCompilazione.UrlPagRegoleCompilazione;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractSection;
import it.csi.melograno.aggregatore.business.javaengine.commonality.IndexStatus;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S3.Section;
import it.csi.findom.blocchetti.blocchetti.accessoForbidden.AccessoForbidden;
import it.csi.findom.blocchetti.blocchetti.dichiarazioni96.DichiarazioniNG;
import it.csi.findom.blocchetti.progetti.FinSectionPage;
import freemarker.core.Environment.Namespace;
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
		return new Class[] {AccessoForbidden.class, UrlPagRegoleCompilazione.class, DichiarazioniNG.class };
	}

	@Override
	public AbstractSection getParentSection() {
		return new Section(context, namespace, model);
	}

	public IndexStatus getInternalIndexStatus(CommonInfo info) {
	    IndexStatus is = new IndexStatus();
	    is.setIndexItemEnabled(false);
	    if(input.operatorePresentatore!=null) {
	        is.setIndexItemEnabled(true);
	    }
	    return is;
	}
	
	@Override
	public SectionPageInput getInput() {
		return input;
	}
	@Override
	public String getReloadCommandLabel() {
		return "C_reload_S1_P1";
	}
}
