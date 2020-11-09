/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.selezioneAteco;

import it.csi.findom.blocchetti.common.dao.AtecoDAO;
import it.csi.findom.blocchetti.common.vo.ateco.AtecoVo;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class SelezioneAteco extends Commonality {

	SelezioneAtecoInput input = new SelezioneAtecoInput();

	@Override
	public SelezioneAtecoInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[SelezioneAteco::inject] BEGIN");

		try {
			SelezioneAtecoOutput ns = new SelezioneAtecoOutput();
			List<AtecoVo> atecoFoundList = new ArrayList<AtecoVo>();
			String viewAtecoResult = "false";
			String codiceAteco = input.codiceAteco;
			String descrizioneAteco = input.descrizioneAteco;
			String comando = input.command;
			String codiceAtecoNormalizzato = "";

			if (info.getCurrentPage() != null) {
//				if (info.getCurrentPage().contains("S1_P1_SEL_ATECO") && !info.getFormState().equals("IN")
				if (info.getCurrentPage().contains("SEL_ATECO") && !info.getFormState().equals("IN")  //non aggiungo test su NV in quanto se la domanda e' in uno stato finale non puo' eseguire questo blocchetto
						&& !info.getFormState().equals("CO")) {
					logger.info("[SelezioneAteco::inject] comando =" + comando);
					if (comando != null && comando.contains("C_pulisci_campi_ricerca_ateco")) {
						// cancello i campi
						codiceAteco = "";
						descrizioneAteco = "";
						viewAtecoResult = "false";
						atecoFoundList = null;
					}

					if (!StringUtils.isBlank(codiceAteco) || !StringUtils.isBlank(descrizioneAteco)) {

						viewAtecoResult = "true";
						if (inputMessages != null && !inputMessages.isEmpty()) {
							logger.info("[SelezioneAteco::inject] MSGG1=" + findMessage(inputMessages, "id_codiceAteco"));
							logger.info("[SelezioneAteco::inject] MSGG2=" + findMessage(inputMessages, "id_descrizioneAteco"));
							CommonalityMessage nessunaSelezione = findMessage(inputMessages, "atecoNotSelected");
							logger.info("[SelezioneAteco::inject]  nessunaSelezione =" + nessunaSelezione);
							if (nessunaSelezione != null) {
								logger.info("[SelezioneAteco::inject]  faccio query getListAtecoVoByCodDesc");
								logger.info("[SelezioneAteco::inject]  codiceAteco="+codiceAteco);
								logger.info("[SelezioneAteco::inject]  descrizioneAteco="+descrizioneAteco);
								codiceAtecoNormalizzato = codiceAteco.replaceAll("\\.", "");
								atecoFoundList =  AtecoDAO.getListAtecoVoByCodDesc(codiceAtecoNormalizzato, descrizioneAteco, logger);
								logger.info("[SelezioneAteco::inject]  elementi trovati="+atecoFoundList.size());
							}
						} else {
							// faccio la query solo se non ho errori nei dati
							logger.info("[SelezioneAteco::inject]  faccio query getListAtecoVoByCodDesc");
							logger.info("[SelezioneAteco::inject]  codiceAteco="+codiceAteco);
							logger.info("[SelezioneAteco::inject]  descrizioneAteco="+descrizioneAteco);
							codiceAtecoNormalizzato = codiceAteco.replaceAll("\\.", "");
							atecoFoundList = AtecoDAO.getListAtecoVoByCodDesc(codiceAtecoNormalizzato, descrizioneAteco, logger);
							logger.info("[SelezioneAteco::inject]  elementi trovati="+atecoFoundList.size());
						}
					}
				}
			}

			ns.atecoFoundList = atecoFoundList;
			ns.viewAtecoResult=viewAtecoResult;
			ns.codiceAteco=codiceAteco;
			ns.descrizioneAteco=descrizioneAteco;
			logger.info("[SelezioneAteco::inject]  _selezioneAteco S1_P1 END");

			return ns;
		} catch (CommonalityException ex) {
			throw new CommonalityException(ex);
		} finally {
			logger.info("[SelezioneAteco::inject] END");
		}
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		return null;
	}

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info1, List<CommonalityMessage> inputMessages)
			throws CommonalityException {

		
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[SelezioneAteco::commandValidate] BEGIN");
		
		String _commandLabel = input.commandLabelModel;
	    
		if (!StringUtils.isBlank(_commandLabel)) {

			String ERRMSG_SELEZ = "- selezionare almeno una voce";
			String ERRMSG_ALPHANUMER = "- inserire solo caratteri alfanumerici";
			String ERRMSG_PARAM = "- inserire almeno un filtro di ricerca";

			logger.debug("SelezioneAteco::commandValidate] validazione comandi custom per C_trova_ateco");
			if (_commandLabel.contains("C_trova_ateco")) {
				String codiceAteco = input.codiceAteco;
				logger.debug("SelezioneAteco::commandValidate] codiceAteco:" + codiceAteco );
				
				//rimozione di eventuali caratteri '.' per cercare sempre sul codice ateco normalizzato 
				codiceAteco = codiceAteco.replaceAll("\\.", "");

				// verifica che la stringa, a cui sono stati rimossi gli eventuali punti, sia composta solo da caratteri alfanumerici		
				if (StringUtils.isNotEmpty(codiceAteco) && !StringUtils.isAlphanumericSpace(codiceAteco) ) {
					logger.warn("SelezioneAteco::commandValidate] Attenzione, codiceAteco non valido, utilizzare solo caratteri numerici");
					addMessage(newMessages,"id_codiceAteco", ERRMSG_ALPHANUMER);
				}

				// verifica che la stringa sia composta solo da caratteri alfanumerici
				String descrizioneAteco = input.descrizioneAteco;
				logger.debug("SelezioneAteco::commandValidate] descrizioneAteco:" + descrizioneAteco );
				if (StringUtils.isNotEmpty(descrizioneAteco) && !StringUtils.isAlphaSpace(descrizioneAteco)) {
					logger.warn("SelezioneAteco::commandValidate] Attenzione, descrizionrAteco non valida, utilizzare solo caratteri alfanumerici");
					addMessage(newMessages,"id_descrizioneAteco", ERRMSG_ALPHANUMER );
				}

				// verifica che le stringhe non siano entrambe vuote
				if (StringUtils.isEmpty(descrizioneAteco) && StringUtils.isEmpty(codiceAteco)){
					logger.warn("SelezioneAteco::commandValidate] Attenzione, inserire almeno un patrametro");
					addMessage(newMessages,"atecoParamNull", ERRMSG_PARAM );
				}
			}
			
			if (_commandLabel.contains("C_salva_ateco")) {
			
				// idAteco2007 -- se non arriva questa allora non e' stata selezionata nessuna voce
				//String idAteco2007 = ServletActionContext.getRequest().getParameter("_operatorePresentatore.idAteco2007");  
				String idAteco2007 = input.tmpOperatorePresentatore_idAteco2007;  
				logger.debug("SelezioneAteco::commandValidate] idAteco2007:" + idAteco2007 );
				if (StringUtils.isBlank(idAteco2007)) {
					logger.warn("SelezioneAteco::commandValidate] Attenzione, selezionare almeno un codice dalla lista");
					addMessage(newMessages,"atecoNotSelected", ERRMSG_SELEZ);
				} 			
			}
		}			
			
		logger.info("[SelezioneAteco::commandValidate] END");
	
		return newMessages;
	}

	private CommonalityMessage findMessage(List<CommonalityMessage> messages, String field) {
		for (CommonalityMessage message : messages) {
			if (field.equals(message.getField()))
				return message;
		}
		return null;
	}

}
