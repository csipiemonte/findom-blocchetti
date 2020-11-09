/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S1;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.Direttiva;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
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

		////////////////////////////////////////////////////////////////////////////////////////////
		// inizializzazioni generali

		// esito finale, all'inizio e' "UNCOMPILED", diventa "IN_PROGRESS" se anche un
		// solo tab risulta compilato e "TERMINATED" se lo sono tutti e sussistono
		// condizioni di completezza dell'intera sezione
		String _outcome = "UNCOMPILED";

		// dichiarazione e inizializzazione variabili della regola

		// inizializzazioni generali
		////////////////////////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////////////////////////
		// regola per determinare lo stato di compilazione della sezione "Anagrafica"

		// tab Beneficiario
		boolean isBeneficiarioCompiled = false;
		if (input.operatorePresentatore != null && !input.operatorePresentatore.isEmpty()) {
			isBeneficiarioCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]  tab 'Anagrafica/Beneficiario' compilato");
		} else {
			logger.info("[Section::getInternalIndexStatus]  tab 'Anagrafica/Beneficiario' non compilato");
		}

		//  scommentare 
		// tab Legale Rappresentante
		boolean isLegaleRappresentanteCompiled = false;
		boolean isSoggettoDelegatoRequired = false; 

		if (input.legaleRappresentante != null && !input.legaleRappresentante.isEmpty()) {
			LegaleRappresentanteVO legaleRappr = input.legaleRappresentante;
			String presenzaSoggettoDelegato = (String) legaleRappr.getPresenzaSoggettoDelegato();
		    if(presenzaSoggettoDelegato!=null && presenzaSoggettoDelegato.equals("si")){
		       isSoggettoDelegatoRequired = true;
		    }
			isLegaleRappresentanteCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus] tab 'Anagrafica/Legale Rappresentante' compilato");
		} else {
			logger.info("[Section::getInternalIndexStatus] tab 'Anagrafica/Legale Rappresentante' non compilato");
		}


		// tab Sede Legale
		boolean isSedeLegaleCompiled = false;
		if (input.sedeLegale != null && !input.sedeLegale.isEmpty()) {
			isSedeLegaleCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]  tab 'Anagrafica/Sede Legale' compilato");
		} else {
			logger.info("[Section::getInternalIndexStatus]  tab 'Anagrafica/Sede Legale' non compilato");
		}

		// tab Estremi Bancari
		boolean isEstremiBancariCompiled = false;
		if (input.estremiBancari != null && !input.estremiBancari.isEmpty()) {
			isEstremiBancariCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]  tab 'Anagrafica/Estremi Bancari' compilato");
		} else {
			logger.info("[Section::getInternalIndexStatus]  tab 'Anagrafica/Estremi Bancari' non compilato");
		}


		// tira le somme
		//if (isBeneficiarioCompiled && isSedeLegaleCompiled && isEstremiBancariCompiled && isRiferimentiCompiled && isSoggettoDelegatoCompiled) {
		if (isBeneficiarioCompiled && isLegaleRappresentanteCompiled && isSedeLegaleCompiled && isEstremiBancariCompiled) {
			_outcome = "TERMINATED";
		}
		logger.info("[Section::getInternalIndexStatus]  STATO SEZIONE 'Anagrafica' _outcome:" + (String) _outcome);

		// regola per determinare lo stato di compilazione della sezione "Anagrafica"
		////////////////////////////////////////////////////////////////////////////////////////////


		logger.debug("[Section::getInternalIndexStatus] --- SECTION VALIDATION RULE (SVR) END ---");

		//_outcome = "TERMINATED";  //  rimuovere
		return new IndexStatus(true,_outcome);
	}

}
