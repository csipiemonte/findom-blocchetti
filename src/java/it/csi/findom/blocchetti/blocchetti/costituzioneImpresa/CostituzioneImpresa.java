/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.costituzioneImpresa;

import it.csi.findom.blocchetti.common.dao.LuoghiDAO;
import it.csi.findom.blocchetti.common.util.TrasformaClassiAAEP2VO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.RegioneProvinciaVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.CostituzioneImpresaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.VistaUltimaDomandaDto;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.melograno.aggregatore.util.Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.log4j.Logger;

public class CostituzioneImpresa extends Commonality {

	CostituzioneImpresaInput input = new CostituzioneImpresaInput();

	@Override
	public CostituzioneImpresaInput getInput() throws CommonalityException {
		return input;
	}   

	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {
		FinCommonInfo info = (FinCommonInfo) info1;
		Logger logger = Logger.getLogger(info.getLoggerName());
		logger.info("[CostituzioneImpresa::inject] BEGIN");

		try {
			CostituzioneImpresaOutput ns = new CostituzioneImpresaOutput();

			String costituzioneInCorso = null;
			String dataIscrizioneRegistroImprese = "";
			String iscrizioneInCorso = null;

			List<RegioneProvinciaVO> provinceRegImpresaList = new ArrayList<RegioneProvinciaVO>();

			if (input.costituzioneImpresaCostituzioneInCorso.equals("true")) {
				costituzioneInCorso = "";
			}
			String dataCostituzioneImpresaXML = "";
			//serve a stabilire se sono presenti i dati su AAEP
			boolean datiSuAAEP = false;

			CostituzioneImpresaVo costImpr = new CostituzioneImpresaVo();

			ImpresaVO entImprAAEP = (ImpresaVO)TrasformaClassiAAEP2VO.impresa2ImpresaVO((Impresa)SessionCache.getInstance().get("enteImpresa"));// dataIscrizioneRegistroImpresa: trovato: 21/04/2000

			if(entImprAAEP!=null){
				logger.info("[CostituzioneImpresa::inject] entImprAAEP idAzienda="+entImprAAEP.getIdAzienda());

				String siglP = entImprAAEP.getDettagliCameraCommercio().getSiglaProvinciaIscrizioneREA();
				logger.info("[CostituzioneImpresa::inject] siglP="+siglP);
				datiSuAAEP = true;

			} else {
				logger.info("[CostituzioneImpresa::inject] entImprAAEP non in sessione");
				entImprAAEP = new ImpresaVO();
			}

			if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {             
				//dataCostituzioneImpresaXML serve solo per stabilire l'abilitazione del campo data costituzione impresa;
				//dataCostituzioneImpresaValue contiene il valore messo nel campo ed risulta preso da xml o, se non c'risulta su xml, da AAEP
				String dataCostituzioneImpresaValue = "";
				String dataIscrizioneRegistroImpresaValue = "";
				CostituzioneImpresaVo _costituzioneImpresa = input._costituzioneImpresa;

				//se _costituzioneImpresa nullo, significa che sull'xml non e' mai stato salvato e quindi uso i dati presi da AAEP
				if(_costituzioneImpresa == null ) {			
					//e' la prima volta che vado a server dopo che sono entrato per la prima volta nella pagina
					dataCostituzioneImpresaXML = "00/00/0000";  //per abilitare il campo data

					if( entImprAAEP != null ){			
						if(entImprAAEP.getDatoCostitutivo()!=null){						
							dataCostituzioneImpresaValue = (String)entImprAAEP.getDatoCostitutivo().getDataCostituzione(); 
						}

						logger.info("[CostituzioneImpresa::inject] dataCostituzioneImpresaXML="+dataCostituzioneImpresaXML);

						String siglP =  "";
						if(entImprAAEP.getDettagliCameraCommercio()!=null){
							siglP = entImprAAEP.getDettagliCameraCommercio().getSiglaProvinciaIscrizioneREA();
						}
						logger.info("[CostituzioneImpresa::inject] siglP="+siglP);

						/*******************************************************
						 * Jira: 1699
						 * - aggiunta del campo data iscrizione registro imprese
						 * da AAEP e nodo: dettagliCameraCommercio  
						 * atributo del nodo: <dataIscrizioneRegistroImprese>
						 * 
						/*******************************************************/
						if ("true".equals(input._costituzioneImpresa_data_iscrizione_registro_imprese)) {
							if (entImprAAEP != null) {
								if (entImprAAEP.getDettagliCameraCommercio() != null) {
									dataIscrizioneRegistroImpresaValue = entImprAAEP.getDettagliCameraCommercio().getDataIscrizioneRegistroImprese() != null ? entImprAAEP.getDettagliCameraCommercio().getDataIscrizioneRegistroImprese() : "";
									logger.info("[CostituzioneImpresa::inject] dataIscrizioneRegistroImpresaValue= " + dataIscrizioneRegistroImpresaValue); // dato recuperato da aaep : 21/04/2000:fca
									costImpr.setDataIscrizioneRegistroImprese(dataIscrizioneRegistroImpresaValue);
								}
							}
						}

						costImpr.setCostituzioneInCorso(""); // non esiste su AAEP
						costImpr.setDataCostituzioneImpresa(dataCostituzioneImpresaValue);
						costImpr.setIscrizioneInCorso(""); // non esiste su AAEP
						costImpr.setProvincia(siglP);
						costImpr.setProvinciaDescrizione(""); // non esiste su AAEP, qui lo inizializzo

						List<RegioneProvinciaVO> listaProv = LuoghiDAO.getProvinceRegImpresaList(logger);
						for(int i=0; i<listaProv.size(); i++){
							RegioneProvinciaVO provMap = (RegioneProvinciaVO)listaProv.get(i);
							logger.info("[CostituzioneImpresa::inject] provMap="+provMap.toString());
							if(StringUtils.equals(siglP,provMap.getSigla())){
								logger.info("[CostituzioneImpresa::inject] trovato="+provMap.getDescrizione());

								costImpr.setProvinciaDescrizione(provMap.getDescrizione());

								break;
							}
						}
					}

					//  aggiorna con informazioni inserite nell'ultima domanda
					costImpr = aggiornaConInformazioniUltimaDomandaCostituzioneImpresa (costImpr, datiSuAAEP, logger);

					if(costImpr.getDataCostituzioneImpresa()!=null){						
						dataCostituzioneImpresaValue = (String)costImpr.getDataCostituzioneImpresa(); 
					}

					/** Jira: 1700 */
					if ("true".equals(input._costituzioneImpresa_data_iscrizione_registro_imprese)) {

						//  15/11/2019 BEGIN - aggiorna con informazioni inserite nell'ultima domanda
						if(costImpr.getDataIscrizioneRegistroImprese() != null){						
							dataIscrizioneRegistroImprese = (String)costImpr.getDataIscrizioneRegistroImprese(); 
							logger.info("[CostituzioneImpresa::inject] dataIscrizioneRegistroImprese: " + dataIscrizioneRegistroImprese); // found: 21/04/2000
						}
					}

				}else{			    
					//_costituzioneImpresa non nullo, quindi non considero piu AAEP                							
					dataCostituzioneImpresaValue = _costituzioneImpresa.getDataCostituzioneImpresa();

					//leggo la data di costituzione impresa dal DB per stabilire se abilitare o disabilitare il campo;
					//infatti la valorizzazione nell'xml in memoria non significa necessariamente che ci sia già stato almeno un salvataggio

					dataCostituzioneImpresaXML = CostituzioneImpresaDAO.getDataCostituzioneImpresa(info.getStatusInfo().getNumProposta());  

					if(dataCostituzioneImpresaXML==null || dataCostituzioneImpresaXML.isEmpty()){
						logger.info("[CostituzioneImpresa::inject] sono nel caso di _costituzioneImpresa letto da DB non nullo; dataCostituzioneImpresaXML e null");
						dataCostituzioneImpresaXML = "00/00/0000";
					} else{
						logger.info("[CostituzioneImpresa::inject] sono nel caso di _costituzioneImpresa letto da DB non nullo; dataCostituzioneImpresaXML NON e null e vale : " + dataCostituzioneImpresaXML);
					}		       	

					logger.info("[CostituzioneImpresa::inject] dataCostituzioneImpresaXML:" + dataCostituzioneImpresaXML);

					String datadatasa = _costituzioneImpresa.getDataCostituzioneImpresa();
					logger.info("[CostituzioneImpresa::inject] datadatasa:" + datadatasa);

					costImpr.setCostituzioneInCorso(_costituzioneImpresa.getCostituzioneInCorso()); 				
					costImpr.setDataCostituzioneImpresa(dataCostituzioneImpresaValue);  				
					costImpr.setIscrizioneInCorso(_costituzioneImpresa.getIscrizioneInCorso()); 
					costImpr.setProvincia(_costituzioneImpresa.getProvincia());
					costImpr.setProvinciaDescrizione(_costituzioneImpresa.getProvinciaDescrizione()); 

					/**  :: inizio Jira 1707 */
					if ("true".equals(input._costituzioneImpresa_data_iscrizione_registro_imprese)) {
						dataIscrizioneRegistroImprese = _costituzioneImpresa.getDataIscrizioneRegistroImprese();
						logger.info("[CostituzioneImpresa::inject] str_dataIscrizioneRegistroImprese= "+dataIscrizioneRegistroImprese);
						costImpr.setDataIscrizioneRegistroImprese(dataIscrizioneRegistroImprese);

						if ("true".equals(input._costituzioneImpresa.getIscrizioneInCorso())){
							iscrizioneInCorso = _costituzioneImpresa.getIscrizioneInCorso();
							costImpr.setIscrizioneInCorso(iscrizioneInCorso);
							logger.info("iscrizione in corso risulta a : " + iscrizioneInCorso);
						}else{
							iscrizioneInCorso = "false";
							costImpr.setIscrizioneInCorso(iscrizioneInCorso);
						}
					}
					/**  :: fine Jira 1707 */

					logger.info("[CostituzioneImpresa::inject] _costituzioneImpresa non NULL");

					if ("true".equals(input.costituzioneImpresaCostituzioneInCorso)) {
						costituzioneInCorso = _costituzioneImpresa.getCostituzioneInCorso();
					}

				}
				//lista delle province italiane
				provinceRegImpresaList = LuoghiDAO.getProvinceRegImpresaList(logger);				

			}else{
				//domanda in stato 'IN'
				CostituzioneImpresaVo _costituzioneImpresa = input._costituzioneImpresa;

				dataCostituzioneImpresaXML = "00/00/0000";
				if(_costituzioneImpresa!=null && _costituzioneImpresa.getDataCostituzioneImpresa() !=null){
					dataCostituzioneImpresaXML = _costituzioneImpresa.getDataCostituzioneImpresa();
				}

			}

			if ("true".equals(input.costituzioneImpresaCostituzioneInCorso)) {
				ns.setCostituzioneInCorso(costituzioneInCorso);
			}

			ns.setDataCostituzioneImpresaXML(dataCostituzioneImpresaXML);
			ns.setProvinceRegImpresaList(provinceRegImpresaList);
			ns.setEntImprAAEP(entImprAAEP);

			String dataCostituzioneImpresaAAEP = "";
			if (!entImprAAEP.isEmpty() && entImprAAEP.getDatoCostitutivo()!=null) {
				dataCostituzioneImpresaAAEP = entImprAAEP.getDatoCostitutivo().getDataCostituzione();
			}
			ns.setDataCostituzioneImpresaAAEP(dataCostituzioneImpresaAAEP);

			String siglaProvinciaIscrizioneREA = "";
			if (!entImprAAEP.isEmpty() && entImprAAEP.getDettagliCameraCommercio()!=null) {
				siglaProvinciaIscrizioneREA = entImprAAEP.getDettagliCameraCommercio().getSiglaProvinciaIscrizioneREA();
			}
			ns.setSiglaProvinciaIscrizioneREA(siglaProvinciaIscrizioneREA);

			/**  :: inizio Jira 1699 */
			String dataIscrizioneRegistroImpreseAAEP = "";
			if ("true".equals(input._costituzioneImpresa_data_iscrizione_registro_imprese)) {

				if (!entImprAAEP.isEmpty() && entImprAAEP.getDettagliCameraCommercio() != null) {
					dataIscrizioneRegistroImpreseAAEP = entImprAAEP.getDettagliCameraCommercio().getDataIscrizioneRegistroImprese();
					logger.info("dataIscrizioneRegistroImpreseAAEP risulta: " + dataIscrizioneRegistroImpreseAAEP); // recuperato: 21/04/2000
				}
				ns.setDataIscrizioneRegistroImpreseAAEP(dataIscrizioneRegistroImpreseAAEP);
			}
			/**  :: fine Jira 1699 */

			ns.setCostImpr(costImpr);
			ns.iscrizioneInCorso = iscrizioneInCorso;

			logger.info("[CostituzioneImpresa::inject] _costituzioneImpresa END");
			return ns;
		}
		catch(Exception ex) {
			throw new CommonalityException(ex);
		}
		finally {
			logger.info("[CostituzioneImpresa::inject] END");
		}
	}

	/**
	 * provinceRegImpresaList
	 */
	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo infol, List<CommonalityMessage> inputMessages) {

		FinCommonInfo info = (FinCommonInfo) infol;
		List<CommonalityMessage> newMessages = new ArrayList<CommonalityMessage>();

		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[CostituzioneImpresa::modelValidate] _costituzioneImpresa  BEGIN");

		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo é obbligatorio";
		String ERRMSG_FORMATO_CAMPO_DATA = "- il formato della data non é valido";
		String ERRMSG_CAMPO_OBBLIGATORIO_PISC = "- valorizzare questo campo o il campo 'Iscrizione in corso'";
		String ERRMSG_CAMPO_OBBLIGATORIO_ISCR = "- valorizzare questo campo o il campo 'Provincia d'iscrizione registro imprese'";
		String ERRMSG_DATACOSTIMPRESA_SUCC = "- La data costituzione impresa é successiva alla data odierna";
		String ERRMSG_DATA_REGISTRAZIONE_IMPRESA_SUCC = "- La data indicata risulta essere successiva alla data odierna";

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();

		CostituzioneImpresaVo _costituzioneImpresa = input._costituzioneImpresa;
		String costituzioneInCorso  = "";
		String dataIscrizioneRegistroImprese = "";

		try {
			if (_costituzioneImpresa != null) {

				if (input.costituzioneImpresaCostituzioneInCorso.equals("true")) {
					costituzioneInCorso  = (String) _costituzioneImpresa.getCostituzioneInCorso();
				}
				if((!input.costituzioneImpresaCostituzioneInCorso.equals("true")) || ((input.costituzioneImpresaCostituzioneInCorso.equals("true")) && (StringUtils.isBlank(costituzioneInCorso) || (!costituzioneInCorso.equals("true"))))){

					//Data costituzione impresa
					String dataCostituzioneImpresa = (String) _costituzioneImpresa.getDataCostituzioneImpresa();
					if (StringUtils.isBlank(dataCostituzioneImpresa)) {
						addMessage(newMessages,"_costituzioneImpresa_dataCostituzioneImpresa", ERRMSG_CAMPO_OBBLIGATORIO);
						logger.info("[CostituzioneImpresa::modelValidate] data costituzione impresa non valorizzato");
					}else{
						Date dataCostituzioneImpresaParse = sdf.parse(dataCostituzioneImpresa, new ParsePosition(0));
						if (!DateValidator.getInstance().isValid(dataCostituzioneImpresa, "dd/MM/yyyy") || !(dataCostituzioneImpresa.matches("\\d{2}/\\d{2}/\\d{4}")) ) {			
							addMessage(newMessages,"_costituzioneImpresa_dataCostituzioneImpresa", ERRMSG_FORMATO_CAMPO_DATA);
							logger.info("[CostituzioneImpresa::modelValidate]  data costituzione impresa formalmente non corretta");
						}else if (dataCostituzioneImpresaParse.after(today)) {
							logger.info("[CostituzioneImpresa::modelValidate] - la data costituzione impresa risulta successiva alla data odierna:" + dataCostituzioneImpresa);
							addMessage(newMessages,"_costituzioneImpresa_dataCostituzioneImpresa", ERRMSG_DATACOSTIMPRESA_SUCC);
						} else{
							logger.info("[CostituzioneImpresa::modelValidate] data costituzione impresa:" + dataCostituzioneImpresa);
						}
					}

					// se flag "Impresa in fase di costituzione" = TRUE allora non faccio controlli su "Provincia d'iscrizione registro imprese" e su "Iscrizione in corso"
					//Provincia d'iscrizione registro imprese
					String provIscrizioneRegImprese = (String) _costituzioneImpresa.getProvincia();

					if (input.costituzioneImpresaIscrizioneInCorso.equals("true")) {
						String iscrizioneInCorso  = (String) _costituzioneImpresa.getIscrizioneInCorso();
						if(StringUtils.isBlank(iscrizioneInCorso) || (!iscrizioneInCorso.equals("true"))){
							if (StringUtils.isBlank(provIscrizioneRegImprese) || provIscrizioneRegImprese.equals("-")) {
								if (input.costituzioneImpresaIscrizioneInCorso.equals("true")) {
									addMessage(newMessages,"_costituzioneImpresa_provincia", ERRMSG_CAMPO_OBBLIGATORIO_PISC);
									addMessage(newMessages,"_costituzioneImpresa_iscrizioneInCorso", ERRMSG_CAMPO_OBBLIGATORIO_ISCR);
									logger.info("[CostituzioneImpresa::modelValidate]  Provincia d'iscrizione registro imprese e iscrizioneInCorso non valorizzate");
								} else {
									addMessage(newMessages,"_costituzioneImpresa_provincia", ERRMSG_CAMPO_OBBLIGATORIO);
									logger.info("[CostituzioneImpresa::modelValidate]  Provincia d'iscrizione registro imprese non valorizzato");
								}
							} 
						}
					} else {
						if (StringUtils.isBlank(provIscrizioneRegImprese) || provIscrizioneRegImprese.equals("-")) {
							if (input.costituzioneImpresaIscrizioneInCorso.equals("true")) {
								addMessage(newMessages,"_costituzioneImpresa_provincia", ERRMSG_CAMPO_OBBLIGATORIO_PISC);
								addMessage(newMessages,"_costituzioneImpresa_iscrizioneInCorso", ERRMSG_CAMPO_OBBLIGATORIO_ISCR);
								logger.info("[CostituzioneImpresa::modelValidate]  Provincia d'iscrizione registro imprese e iscrizioneInCorso non valorizzate");
							} else {
								addMessage(newMessages,"_costituzioneImpresa_provincia", ERRMSG_CAMPO_OBBLIGATORIO);
								logger.info("[CostituzioneImpresa::modelValidate]  Provincia d'iscrizione registro imprese non valorizzato");
							}
						}
					}
				}

				/**  :: inizio Jira: 1590 ::modelValidate */
				if (input._costituzioneImpresa_data_iscrizione_registro_imprese.equals("true")) {

					if(!costituzioneInCorso.equals("true") && !"true".equals(input._costituzioneImpresa.getIscrizioneInCorso())){

						dataIscrizioneRegistroImprese = (String) _costituzioneImpresa.getDataIscrizioneRegistroImprese();
						logger.info("[CostituzioneImpresa::modelValidate] data iscrizione al registro imprese risulta: " + dataIscrizioneRegistroImprese);

						// verifico se campo risulta compilato
						if (StringUtils.isBlank(dataIscrizioneRegistroImprese)) {
							addMessage(newMessages,"_costituzioneImpresa_dataIscrizioneRegistroImprese", ERRMSG_CAMPO_OBBLIGATORIO);
							logger.info("[CostituzioneImpresa::modelValidate] data costituzione impresa non valorizzato");

						} else {

							Date dataIscrizioneRegistroImpreseParse = sdf.parse(dataIscrizioneRegistroImprese, new ParsePosition(0));

							if (!DateValidator.getInstance().isValid(dataIscrizioneRegistroImprese, "dd/MM/yyyy") || !(dataIscrizioneRegistroImprese.matches("\\d{2}/\\d{2}/\\d{4}")) ) {			
								addMessage(newMessages,"_costituzioneImpresa_dataIscrizioneRegistroImprese", ERRMSG_FORMATO_CAMPO_DATA);
								logger.info("[CostituzioneImpresa::modelValidate]  data costituzione impresa formalmente non corretta");

							} else if (dataIscrizioneRegistroImpreseParse.after(today)) {
								logger.info("[CostituzioneImpresa::modelValidate] - La data registrazione impresa risulta essere successiva alla data odierna: " + dataIscrizioneRegistroImpreseParse);
								addMessage(newMessages,"_costituzioneImpresa_dataIscrizioneRegistroImprese", ERRMSG_DATA_REGISTRAZIONE_IMPRESA_SUCC);

							} else {
								logger.info("[CostituzioneImpresa::modelValidate] data costituzione impresa:" + dataIscrizioneRegistroImprese);
							}
						} //  :: fine Jira: 1590
					}else{
						logger.info("Bypassa obbligo compilazione campi!");
					}
				}
			}

		} catch(Exception ex) {
			logger.error("[CostituzioneImpresa::modelValidate] ", ex);
		}
		finally {
			logger.info("[CostituzioneImpresa::modelValidate] _costituzioneImpresa  END");
		}

		return newMessages;
	}

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
		// nothing to validate
		return null;
	}

	private CostituzioneImpresaVo aggiornaConInformazioniUltimaDomandaCostituzioneImpresa(CostituzioneImpresaVo costituzioneImpresa, boolean datiSuAAEP, Logger logger) {
		// utilizzo i dati dell'ultima domanda inserita
		String prf = "[CostituzioneImpresa::aggiornaConInformazioniUltimaDomandaCostituzioneImpresa] ";
		
		VistaUltimaDomandaDto datiUltimoBeneficiario = ((VistaUltimaDomandaDto)SessionCache.getInstance().get("datiUltimoBeneficiario"));
		logger.info(prf +" datiUltimoBeneficiario in sessione=" + datiUltimoBeneficiario);
	
		if (datiUltimoBeneficiario != null && datiUltimoBeneficiario.getCostituzioneImpresa()!=null) {

			logger.debug(prf + " datiUltimoBeneficiario OperatorePresentatore.CF=" + datiUltimoBeneficiario.getOperatorePresentatore().getCodiceFiscale());
			
			CostituzioneImpresaDto ultimaCostituzioneImpresa = datiUltimoBeneficiario.getCostituzioneImpresa();
			costituzioneImpresa.setCostituzioneInCorso(ultimaCostituzioneImpresa.getCostituzioneInCorso()); // non esiste su AAEP
			costituzioneImpresa.setIscrizioneInCorso(ultimaCostituzioneImpresa.getIscrizioneInCorso()); // non esiste su AAEP

			if (datiSuAAEP) {

				costituzioneImpresa.setDataCostituzioneImpresa(Utils.isEmpty(costituzioneImpresa.getDataCostituzioneImpresa()) ? ultimaCostituzioneImpresa.getDataCostituzioneImpresa() : costituzioneImpresa.getDataCostituzioneImpresa());

				if (Utils.isEmpty(costituzioneImpresa.getProvincia())) {
					// non esiste su AAEP, qui lo inizializzo
					costituzioneImpresa.setProvinciaDescrizione(ultimaCostituzioneImpresa.getProvinciaDescrizione()); // non esiste su AAEP, qui lo inizializzo
					costituzioneImpresa.setProvincia(ultimaCostituzioneImpresa.getProvincia());	
				}

			} else {
				costituzioneImpresa.setProvinciaDescrizione(ultimaCostituzioneImpresa.getProvinciaDescrizione()); // non esiste su AAEP, qui lo inizializzo
				costituzioneImpresa.setProvincia(ultimaCostituzioneImpresa.getProvincia());
				costituzioneImpresa.setDataCostituzioneImpresa(ultimaCostituzioneImpresa.getDataCostituzioneImpresa());				
			}

		}
		return costituzioneImpresa;
	}

}
