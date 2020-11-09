/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.S3;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.findom.blocchetti.progetti.presentazione.bonusCultura.Direttiva;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractDirettiva;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SectionPageInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.IndexStatus;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiItemVO;
import it.csi.findom.blocchetti.common.vo.allegati.DocumentoVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.progetti.FinSection;
import freemarker.core.Environment.Namespace;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

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

		String logprefix = "--- BSH:SVR --- ";

		////////////////////////////////////////////////////////////////////////////////////////////
		// inizializzazioni generali

		// esito finale, all'inizio é "UNCOMPILED", diventa "IN_PROGRESS" se anche un solo tab risulta compilato e "TERMINATED" se lo sono tutti e sussistono condizioni di completezza dell'intera sezione
		String _outcome = "UNCOMPILED";

		// dichiarazione e inizializzazione variabili della regola

		// inizializzazioni generali
		////////////////////////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////////////////////////
		// regola per determinare lo stato di compilazione della sezione "Documentazione"

		// tab "Dichiarazioni"
		boolean isDichiarazioniCompiled = false;
		if (input.dichiarazioni != null && !input.dichiarazioni.isEmpty()) {
			isDichiarazioniCompiled = true;
			_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]tab 'Documentazione/Dichiarazioni' compilato");
		} 

		// tab "Upload"
		boolean isUploadCompiled = false;
		if (input.allegatiVO != null && !input.allegatiVO.isEmpty()) {
			
			isUploadCompiled = true;
			
			AllegatiVO _allegati = input.allegatiVO;
			String elencoIdAllegatiObbligatori = _allegati.getElencoIdAllegatiObbligatori();
			logger.info("[Section::getInternalIndexStatus] elencoIdAllegatiObbligatori " + elencoIdAllegatiObbligatori);
			
			AllegatiItemVO[] allegatiList = _allegati.getAllegatiList();
			
			if (elencoIdAllegatiObbligatori!=null && !elencoIdAllegatiObbligatori.equals("")){
							
				if(allegatiList==null || allegatiList.length==0){
					isUploadCompiled = false;
				} else {
						
					Map<String, String> idTipologieSalvateMap = new HashMap<>();
				
					for(int i=0; i<allegatiList.length;i++){
						AllegatiItemVO docMap = allegatiList[i];
						//logger.debug("[DEBUG] " + logprefix + " docMap="+docMap);
						if(docMap!=null){
							DocumentoVO documento = docMap.getDocumento();
							logger.debug("[DEBUG] " + logprefix + " documento="+documento);
							if(documento!=null){
								String idTipologia = documento.getIdTipologia();
								logger.debug("[DEBUG] " + logprefix + " idTipologia="+idTipologia);
								idTipologieSalvateMap.put(idTipologia,"x");
							}
						}
					}
			
					// elimino primo carattere ','			
					String strTmp = elencoIdAllegatiObbligatori.substring(1);
					String[] arrayIdTipolObbl = strTmp.split(",");
					int counter = 0;
					for (int i = 0; i < arrayIdTipolObbl.length; i++) {
						if(idTipologieSalvateMap.containsKey(arrayIdTipolObbl[i])){
							logger.debug("[DEBUG] " + logprefix + "arrayIdTipolObbl["+i+"]="+arrayIdTipolObbl[i] + "INNN");
							counter ++; // trovato un doc obbligatorio
						}
					}
				
					if(counter!=arrayIdTipolObbl.length){
						// se i doc contati sono diversi da quelli attesi allora ERRORE
						isUploadCompiled = false;				
					}	
				}
			}
			if(allegatiList!=null && allegatiList.length!=0)
				_outcome = "IN_PROGRESS";
			logger.info("[Section::getInternalIndexStatus]_outcome " + _outcome);	
		} 
		// tira le somme
		if (isDichiarazioniCompiled && isUploadCompiled) {
			_outcome = "TERMINATED";
		}
		logger.info("[INFO] " + logprefix + "STATO SEZIONE 'Documentazione' _outcome:" + (String) _outcome);

		// regola per determinare lo stato di compilazione della sezione "Documentazione"
		////////////////////////////////////////////////////////////////////////////////////////////

		logger.info("[INFO] " + "--- SECTION VALIDATION RULE (SVR) END ---");

		return new IndexStatus(true,_outcome);
	}

}
