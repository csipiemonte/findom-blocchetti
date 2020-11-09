/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S4;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.Direttiva;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractDirettiva;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.IndexStatus;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.progetti.FinSection;
import freemarker.core.Environment.Namespace;
import org.apache.log4j.Logger;
import java.util.TreeMap;

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
		logger.info("[Section::getInternalIndexStatus] BEGIN");
		String _outcome = "UNCOMPILED";
		////////////////////////////////////////////////////////////////////////////////////////////
		// regola per determinare lo stato di compilazione della sezione "Progetto"

		// tab "Informazioni sul progetto"
		boolean isInformazioniProgettoCompiled = false;
		if (input.caratteristicheProgetto != null) {
			isInformazioniProgettoCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]tab 'Progetto/Informazioni sul progetto' compilato");
		} else {
			logger.info("[Section::getInternalIndexStatus]tab 'Progetto/Informazioni sul progetto' non compilato");
		}

		// tab "Agevolazione richiesta" (composto da due sezioni _formaFinanziamento
		// _tipologiaAiuto)
		boolean isAgevolazioneRichiestaCompiled = false;
		if (input._tipologiaAiuto != null) {
			isAgevolazioneRichiestaCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]tab 'Progetto/Agevolazione richiesta' compilato");
		} else {
			logger.info("[Section::getInternalIndexStatus]tab 'Progetto/Agevolazione richiesta' non compilato");
		}

		// tira le somme
		if (isInformazioniProgettoCompiled && isAgevolazioneRichiestaCompiled) {
			_outcome = "TERMINATED";
		}
		logger.info("[Section::getInternalIndexStatus]STATO SEZIONE 'Progetto' _outcome:" + (String) _outcome);

		// regola per determinare lo stato di compilazione della sezione "Progetto"
		////////////////////////////////////////////////////////////////////////////////////////////
		logger.info("[INFO] " + "--- SECTION VALIDATION RULE (SVR) END ---");
		
		return new IndexStatus(true, _outcome);
	}

}
