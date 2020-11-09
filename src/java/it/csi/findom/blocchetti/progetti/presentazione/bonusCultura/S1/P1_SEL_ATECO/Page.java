/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1.P1_SEL_ATECO;

import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1.Section;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractSection;
import it.csi.melograno.aggregatore.business.javaengine.commonality.IndexStatus;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.findom.blocchetti.blocchetti.selezioneAteco.SelezioneAteco;
import it.csi.findom.blocchetti.progetti.FinSectionPage;
import freemarker.core.Environment.Namespace;
import java.util.TreeMap;

public class Page extends FinSectionPage { // refactoring

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
		return new Class[] { SelezioneAteco.class};
	}

	@Override
	public AbstractSection getParentSection() {
		return new Section(context, namespace, model);
	}

	public IndexStatus getInternalIndexStatus(CommonInfo info) {
		IndexStatus is = new IndexStatus();
		is.setIndexItemEnabled(true);
		return is;
	}
	
	@Override
	public SectionPageInput getInput() {
		return null;
	}
	@Override
	public String getReloadCommandLabel() {
		return "";
	}
}
