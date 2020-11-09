/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.ruoloProgettiComuni;

import it.csi.findom.blocchetti.common.dao.PartnerDAO;
import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoCPVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.partner.CapofilaAcronimoPartnerVO;
import it.csi.findom.blocchetti.common.vo.partner.PartnerEliminatoVO;
import it.csi.findom.blocchetti.commonality.Constants;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

public class RuoloProgettiComuni extends Commonality {

	  RuoloProgettiComuniInput input = new RuoloProgettiComuniInput();

	  @Override
	  public RuoloProgettiComuniInput getInput() throws CommonalityException {
	    return input;
	  }

	  @Override
	  public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {

	    FinCommonInfo info = (FinCommonInfo) info1;

	    Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[RuoloProgettiComuni::inject] BEGIN");
	    try {
	    	RuoloProgettiComuniOutput output = new RuoloProgettiComuniOutput();
	    	String numDomandaCorrente = info.getStatusInfo().getNumProposta()+"";
	    	String soloLettura = "false";
	    	String ruoloXml = PartnerDAO.getRuoloProgettoComuneXml(info.getStatusInfo().getNumProposta(), logger);  
	    	if(StringUtils.isNotBlank(ruoloXml)){
	    		soloLettura = "true";
	    	}
	    	
	    	//visualizzazione nota/note eliminazione partner
	    	AbstractProgettoCPVO abstractProgettoCPVO = input.abstractProgettoCPVO;
	    	String idCapofilaAcronimo ="";
	    	if(abstractProgettoCPVO!=null){
	    		idCapofilaAcronimo = abstractProgettoCPVO.getIdCapofilaAcronimo();
	    		if(StringUtils.isNotBlank(idCapofilaAcronimo)){ 	    			
	    			//in caso di ruolo capofila, verifico se esistono eventuali partner eliminati che avevano gia' creato la propria domanda;
	    			//se ne esistono, li visualizzo con la relativa nota che il capofila ha inserito prima di eliminarli
	    			if(StringUtils.isNotBlank(ruoloXml)){ 
	    				if(ruoloXml.equals(Constants.ID_RUOLO_CAPOFILA_PRG_COMUNE)){
	    					List<PartnerEliminatoVO> peVOList = PartnerDAO.getPartnerEliminatiListByIdCapofilaAcronimo(idCapofilaAcronimo, logger);
	    					if(peVOList!=null && !peVOList.isEmpty()){
	    						output.visNotaEliminazioneC = "true";
	    						output.peVOList = peVOList;
	    					}

	    				}else if(ruoloXml.equals(Constants.ID_RUOLO_PARTNER_PRG_COMUNE)){
	    					ArrayList<CapofilaAcronimoPartnerVO> capVOList = PartnerDAO.getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(idCapofilaAcronimo, logger);
	    					if(capVOList!=null && !capVOList.isEmpty()){
	    						for (CapofilaAcronimoPartnerVO curCapVO : capVOList) {
	    							if(curCapVO!=null && StringUtils.isNotBlank(curCapVO.getIdDomandaPartner()) &&
	    									StringUtils.isNotBlank(curCapVO.getDtDisattivazione()) &&
	    									numDomandaCorrente.equals(curCapVO.getIdDomandaPartner())  &&
	    									StringUtils.isNotBlank(curCapVO.getNote())){
	    								//queste var sono usate nelle pagina RO, in quanto se siamo nelle condizioni per vedere la nota 
	    								//la domanda del partner e' in stato NV, per cui si usa il template_RO
	    								output.visNotaEliminazioneP = "true";
	    								output.notaEliminazioneP = curCapVO.getNote();
	    								break;
	    							}
	    						}
	    					}
	    				}
	    				//caso di ruolo Partner
	    			}
	    		}
	    	}
	    	

	    	output.soloLettura = soloLettura;
	    	return output;
	    }
	    finally {
	    	logger.info("[RuoloProgettiComuni::inject] END");
	    }
	  }
	 
	  @Override
	  public List<CommonalityMessage> modelValidate(CommonInfo info1, List<CommonalityMessage> inputMessages) {
		  
		FinCommonInfo info = (FinCommonInfo) info1;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();
			
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[RuoloProgettiComuni::modelValidate]  BEGIN");
			
		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo é obbligatorio";
		String ERRMSG_BENEFICIARIO_CAPOFILA_DI_ALTRA_DOMANDA = "- il beneficiario ha già inserito la domanda '%s' in qualità di capofila su questo bando";
		FinStatusInfo statusInfo = info.getStatusInfo();		
		Integer idSoggettoBeneficiario = statusInfo.getIdSoggettoBeneficiario();
		Integer idBando = info.getStatusInfo().getTemplateId();
		Integer idDomanda = info.getStatusInfo().getNumProposta();

		try {
			RuoloProgettiComuniVO ruoloProgettiComuniVO = input.ruoloProgettiComuniVO;

			if (ruoloProgettiComuniVO == null || StringUtils.isBlank(ruoloProgettiComuniVO.getRuolo())) {
				addMessage(newMessages,"_ruoloProgettiComuni", ERRMSG_CAMPO_OBBLIGATORIO);
				logger.info("[RuoloProgettiComuni::modelValidate] ruolo progetti comuni non valorizzato");
			}else{
				if(ruoloProgettiComuniVO.getRuolo().equals(Constants.ID_RUOLO_CAPOFILA_PRG_COMUNE)){
					OperatorePresentatoreVo operatorePresentatoreVO = input.operatorePresentatoreVO;
					String codiceUnitaOrganizzativaBeneficiario = Objects.toString(operatorePresentatoreVO.getCodiceDipartimento(), "");
					String idStatoBeneficiario = Objects.toString(operatorePresentatoreVO.getIdStato(), "");
					//cerco le eventuali altre domande (in stato diverso da NV) che il beneficiario ha sullo stesso bando
					ArrayList<String> idAltreDomandeCapofilaList = PartnerDAO.verificaEsistenzaAltreDomandeBeneficiarioCapofila(idBando, idSoggettoBeneficiario, idDomanda, logger);

					if(idAltreDomandeCapofilaList!=null && !idAltreDomandeCapofilaList.isEmpty()){
						for (String idAltraDomanda : idAltreDomandeCapofilaList) {
							idAltraDomanda = Objects.toString(idAltraDomanda);
							Integer idAltraDomandaInt = Integer.parseInt(idAltraDomanda);
							String ruoloAltraDomanda = PartnerDAO.getRuoloProgettoComuneXml(idAltraDomandaInt, logger);
							if(StringUtils.isNotBlank(ruoloAltraDomanda) && ruoloAltraDomanda.equals(Constants.ID_RUOLO_CAPOFILA_PRG_COMUNE)){
								//se il beneficiario (identificato da codice fiscale +  eventuale unita' organizzativa + idStato) 
								//ha altre domande sullo stesso bando, e con ruolo Capofila, segnalo errore						

								//String curUOAltraDomanda = Objects.toString(PartnerDAO.getUOOperatorePresentatoreXml(idAltraDomandaInt, logger));
								OperatorePresentatoreVo op = PartnerDAO.getOperatorePresentatoreXml(idAltraDomandaInt, logger);
								if(op!=null){
									String codiceUnitaOrganizzativaAltraDomanda = Objects.toString(op.getCodiceDipartimento(), "");
									String idStatoAltraDomanda = Objects.toString(op.getIdStato(), "");
									if(codiceUnitaOrganizzativaBeneficiario.equals(codiceUnitaOrganizzativaAltraDomanda) && 
											idStatoBeneficiario.equals(idStatoAltraDomanda)){
										String msg = String.format(ERRMSG_BENEFICIARIO_CAPOFILA_DI_ALTRA_DOMANDA,idAltraDomanda);
										addMessage(newMessages,"_ruoloProgettiComuni", msg);
										logger.info("[RuoloProgettiComuni::modelValidate] trovate altre domande fatte con ruolo capofila sullo stesso bando dal beneficiario capofila corrente");
										break;
									}
								}
							}
						}
					}	
				}
			}
			
		}
		catch(Exception ex) {
			logger.error("[RuoloProgettiComuni::modelValidate] ", ex);
		}
		finally {
			logger.info("RuoloProgettiComuni::modelValidate] END");
		}

		return newMessages;

	  }

	  @Override
	  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
	    // nothing to validate
	    return null;
	  }

}
