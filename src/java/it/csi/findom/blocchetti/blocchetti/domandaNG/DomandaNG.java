/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.domandaNG;

import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.common.vo.domandaNG.AttoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiSoggettoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiStrutturaOrganizzativaVo;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiTipolBeneficiarioVo;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

public class DomandaNG extends Commonality {
	
	DomandaNGInput input = new DomandaNGInput();

	@Override
	public DomandaNGInput getInput() throws CommonalityException {
		return input;
	}   
	
	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> inputMessages) throws CommonalityException {
		//OK
		FinCommonInfo info = (FinCommonInfo) info1;
		//OK
		Logger logger = Logger.getLogger(info.getLoggerName());
	    logger.info("[DomandaNG::inject] BEGIN");
	    try {
			  //OK
		    DomandaNGOutput ns = new DomandaNGOutput();
		    
//			//MB2017_02_16 ini		
//			String idSettore = "";
//			String descrSettore = "";
//			String idDirezione = "";
//			String descrDirezione = "";
//			//MB2017_02_16 fine
		    
//		    if (info.getCurrentPage() != null) {
//		      if (info.getCurrentPage().contains("S1_P1") && info.isRWByState()) {
//		    	  if (info.getStatusInfo().getIdPso()!=null && StringUtils.isNumeric(info.getStatusInfo().getIdPso()))
//		    		  ns.psoList = DomandaDao.getListPsoVoByIdPso(new Integer(info.getStatusInfo().getIdPso()));
//		      }
//		    }
						
			String idDomanda = info.getStatusInfo().getNumProposta()+"";
			String idBando = info.getStatusInfo().getTemplateId()+"";
		    
			DatiTipolBeneficiarioVo datiTipolBeneficiario = DomandaNGDAO.getDatiTipolBeneficiario(idDomanda);
			DatiStrutturaOrganizzativaVo datiStruttOrganizzativa = DomandaNGDAO.getDatiStrutturaOrganizzativa(idBando);
			String normativa = DomandaNGDAO.getNormativa(idBando, logger);
			AttoVO attoVO = DomandaNGDAO.getAtto(idBando, logger);
			
			// : Data ed ora apertura / chiusura ed ora di sportello - inizio
			String dataAperturaSportello = DomandaNGDAO.getDataAperturaSportello(idBando, logger);
			if(dataAperturaSportello == null) dataAperturaSportello = "-";
			logger.info("[DomandaNG::inject] dataAperturaSportello: " + dataAperturaSportello);
			
			String oraAperturaSportello = DomandaNGDAO.getOraAperturaSportello(idBando, logger);
			if(oraAperturaSportello == null) oraAperturaSportello = "-";
			logger.info("[DomandaNG::inject] oraChiusuraSportello: " + oraAperturaSportello);
			
			String dataChiusuraSportello = DomandaNGDAO.getDataChiusuraSportello(idBando, logger);
			if(dataChiusuraSportello == null) dataChiusuraSportello = "-";
			logger.info("[DomandaNG::inject] dataChiusuraSportello: " + dataChiusuraSportello);
			
			String oraChiusuraSportello = DomandaNGDAO.getOraChiusuraSportello(idBando, logger);
			if(oraChiusuraSportello == null) oraChiusuraSportello = "-";
			logger.info("[DomandaNG::inject] oraChiusuraSportello: " + oraChiusuraSportello);
			
			String annoCorrente = getAnnoCorrente()+"";
			if(annoCorrente == null || annoCorrente.isEmpty()) annoCorrente = "-";
			logger.info("[DomandaNG::inject] annoCorrente: " + annoCorrente);
			
			FinStatusInfo statusInfo = info.getStatusInfo();
            String flagProgettiComuni = (statusInfo==null || StringUtils.isBlank(statusInfo.getFlagProgettiComuni())) ? "" : statusInfo.getFlagProgettiComuni();
         	
			// : Data ed ora apertura / chiusura ed ora di sportello - fine
			
//				codTipologiaUtente = (String)datiTipolBeneficiario.getFlag();
//				stereotipoDomanda = (String)datiTipolBeneficiario.getCodstereotipo();
//				descrTipologiaUtente = (String)datiTipolBeneficiario.getDescrizione();
//				descrStereotipoDomanda = (String)datiTipolBeneficiario.getDescrstereotipo();
//				codiceTipologiaBeneficiario = (String)datiTipolBeneficiario.getCodicetipologiabeneficiario();
			
	  
//			   idSettore = (datiStruttOrganizzativa.getIdsettore()== null) ? "" : ((Integer)datiStruttOrganizzativa.getIdsettore()).toString();
//			   descrSettore = (datiStruttOrganizzativa.getDescrsettore()==null) ? "" :(String)datiStruttOrganizzativa.getDescrsettore();
//			   idDirezione = (datiStruttOrganizzativa.getIddirezione() == null) ? "" : ((Integer)datiStruttOrganizzativa.getIddirezione()).toString();
//			   descrDirezione = (datiStruttOrganizzativa.getDescrdirezione()==null) ? "" :(String)datiStruttOrganizzativa.getDescrdirezione();
				
			//// namespace
			ns.codTipologiaUtente = (datiTipolBeneficiario.getFlag()== null) ? "" : ((String)datiTipolBeneficiario.getFlag());
			ns.stereotipoDomanda = (datiTipolBeneficiario.getCodstereotipo()== null) ? "" : ((String)datiTipolBeneficiario.getCodstereotipo());
			ns.descrTipologiaUtente = (datiTipolBeneficiario.getDescrizione()== null) ? "" : ((String)datiTipolBeneficiario.getDescrizione());
			ns.descrStereotipoDomanda = (datiTipolBeneficiario.getDescrstereotipo()== null) ? "" : ((String)datiTipolBeneficiario.getDescrstereotipo());
			ns.codiceTipologiaBeneficiario = (datiTipolBeneficiario.getCodicetipologiabeneficiario()== null) ? "" : ((String)datiTipolBeneficiario.getCodicetipologiabeneficiario());
			
			
			ns.idSettore =(datiStruttOrganizzativa.getIdsettore()== null) ? "" : ((Integer)datiStruttOrganizzativa.getIdsettore()).toString();
			ns.descrSettore = (datiStruttOrganizzativa.getDescrsettore()==null) ? "" :((String)datiStruttOrganizzativa.getDescrsettore());
			ns.idDirezione = (datiStruttOrganizzativa.getIddirezione() == null) ? "" : ((Integer)datiStruttOrganizzativa.getIddirezione()).toString();
			ns.descrDirezione = (datiStruttOrganizzativa.getDescrdirezione()==null) ? "" :((String)datiStruttOrganizzativa.getDescrdirezione());
			
		    
			ns.flagPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger); 
            ns.normativa = (normativa == null) ? "" : normativa;
            ns.numAtto = (attoVO==null) ? "" : (attoVO.getNumAtto()==null ? "" : attoVO.getNumAtto());
            ns.dataAtto = (attoVO==null) ? "" : (attoVO.getDataAtto()==null ? "" : attoVO.getDataAtto());
            
            // : data apertura sportello
            ns.dataAperturaSportello =  (dataAperturaSportello == null) ? "" : dataAperturaSportello;
            ns.oraAperturaSportello =  (oraAperturaSportello == null) ? "" : oraAperturaSportello;
            ns.dataChiusuraSportello =  (dataChiusuraSportello == null) ? "" : dataChiusuraSportello;
            ns.oraChiusuraSportello =  (oraChiusuraSportello == null) ? "" : oraChiusuraSportello;
            ns.annoCorrente = (annoCorrente == null) ? "" : annoCorrente;
            
            ns.flagProgettiComuni = flagProgettiComuni;
            DatiSoggettoVO datiSoggettoVO = DomandaNGDAO.getDatiSoggetto(statusInfo.getIdSoggettoBeneficiario());
            ns.siglaNazioneSoggettoBeneficiario = datiSoggettoVO.getSiglaNazione();
            //i due seguenti attributi sono gia' presenti in _operatorePresentatore e sono sempre aggiornati rispetto alla domanda
//          ns.codiceUnitaOrganizzativaSoggettoBeneficiario = datiSoggettoVO.getCodiceUnitaOrganizzativa(); //e' il dipartimento o l'ufficio
//          ns.denominazioneSoggettoBeneficiario = datiSoggettoVO.getDenominazione();
			
		    return ns;
		  }
		  catch(Exception ex) {
		    throw new CommonalityException(ex);
		  }
		  finally {
		    logger.info("[DomandaNG::inject] END");
		  }
	}
	
		
	/**
	 * Recupero anno corrente
	 * richiesto solo per alcuni bandi 
	 * esempio: Voucher
	 * @return
	 */
	public int getAnnoCorrente() {
		
		int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
		return annoCorrente;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
		return null;
	}

  @Override
  public List<CommonalityMessage> commandValidate(CommonInfo info, List<CommonalityMessage> inputMessages) {
    return null;
  }



}
