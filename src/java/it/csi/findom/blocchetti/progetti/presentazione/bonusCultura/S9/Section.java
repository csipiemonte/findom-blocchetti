/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S9;

import freemarker.core.Environment.Namespace;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.progetti.FinSection;
import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.Direttiva;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.IndexStatus;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractDirettiva;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;

import java.util.TreeMap;

import org.apache.log4j.Logger;

public class Section extends FinSection {

	SectionInput input = new SectionInput();

	public Section(TreeMap<String, Object> context, Namespace namespace, TreeMap<String, Object> model) {
		super(context, namespace, model);
	}

	@Override
	public void commandValidate() {
	}

	@Override
	public AbstractDirettiva getDirettiva() {
		return new Direttiva(context, namespace, model);
	}

	@Override
	public void initConfigurations() {
	}

	@Override
	public SectionPageInput getInput() {
		return input;
	}

	@Override
	public IndexStatus getInternalIndexStatus(CommonInfo info1) throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[Section S9::getInternalIndexStatus] BEGIN");

		logger.info("[Section S9::getInternalIndexStatus] _outcome:TERMINATED");

		logger.info("[Section S9::getInternalIndexStatus] END");
		
		return new IndexStatus(true,  "TERMINATED");
	}

}
