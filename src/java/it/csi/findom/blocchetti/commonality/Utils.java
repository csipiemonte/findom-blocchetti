/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.commonality;

import java.util.ArrayList;
import java.util.List;

import it.csi.findom.blocchetti.common.dao.DocumentazioneNGDAO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public abstract class Utils {

  public static String quote(Object obj) {
    String res;
    if (obj != null) {
      if (obj instanceof String) {
        String str = (String) obj;
        res = StringUtils.isBlank(str) ? "blank" : "'" + str + "'";
      }
      else {
        res = obj.toString();
      }
    }
    else {
      res = "null";
    }
    return res;
  }
  
  public static boolean isAlphaSpaceApostropheCommaDot(String str) {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if ( (Character.isLetter(str.charAt(i)) == false) &&
          (str.charAt(i) != ' ') && (str.charAt(i) != '\'') &&
          (str.charAt(i) != ',') && (str.charAt(i) != '.')) {
        return false;
      }
    }
    return true;
  }
  
  public static String getValueFromRequestOrModel(CommonalityVO request, CommonalityVO model, String fieldName) throws CommonalityException {
    String value = null;
    if(request!=null) {
      value = request.getFieldValue(fieldName);
    }
    if (StringUtils.isBlank(value) && model!=null) {
      value = model.getFieldValue(fieldName);
    }
    return value==null?"":value;
  }
  
  public static 	//ritorna true se la stringa in input rappresenta uno zero
	boolean isZero(String val){
	   boolean retVal = false;
	   if(val !=null){
	      if(val.equals("0")||val.equals("0,0") || val.equals("0,00")){
	         retVal = true;
	      }
	   }
	   return retVal;
	}
  

  /**
   * TODO: in fase di test... sospendo... 07/02/2019
   * esistonoAllegatiObbligatori
   * @param info
   * @param logger
   * @return true se esistono allegati obbligatori per il bando corrente, false altrimenti
   * @throws CommonalityException
   */
  public static boolean esistonoAllegatiObbligatori(FinCommonInfo info, Logger logger) throws CommonalityException
  {
		// TODO:  - recupero parametri utili per successive query su nuova tabella:
		Integer idBando = info.getStatusInfo().getTemplateId(); 
		logger.info("test: idBando vale: " + idBando);
		Integer idDomanda = info.getStatusInfo().getNumProposta(); 
		logger.info("test: idDomanda vale: " + idDomanda);
		Integer idSportello = info.getStatusInfo().getNumSportello(); 
		logger.info("test: idSportello vale: " + idSportello);

		boolean esistonoAllegatiObbligatori = true;

	  //verifico tramite select se ci sono allegati obbligatori per il bando passato nel parametro info
	  List<TipologiaAllegatoVO> documentiObbligatoriList = new ArrayList<>();
	  
	  // TODO: test  ... ripristinato
	   // documentiObbligatoriList = DocumentazioneNGDAO.getTipologiaAllegatoList(info.getStatusInfo().getTemplateId(), true, logger); // originale
	   
	  documentiObbligatoriList = DocumentazioneNGDAO.getTipologiaAllegatoList(idBando, idSportello, idDomanda, true, logger);
	  if(documentiObbligatoriList==null || documentiObbligatoriList.isEmpty()){
		  logger.debug( "[it.csi.findom.blocchetti.commonality.Utils::verificaEsistenzaAllegatiObbligatori] non ci sono allegati obbligatori per il bando corrente" );
		  esistonoAllegatiObbligatori = false;
	  }	
	  return esistonoAllegatiObbligatori;
  }
}
