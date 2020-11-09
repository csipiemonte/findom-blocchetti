/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.tipodocriconoscimento.TipoDocRiconoscimentoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class TipoDocRiconoscimentoDAO {

  private static final String QUERY_TIPO_DOCUMENTO = "SELECT id_tipologia AS codice, descrizione AS descrizione "
		+ "FROM findom_d_tipologie_documenti ORDER BY codice ";
				
  
  public static List<TipoDocRiconoscimentoVO> getTipoDocRiconoscimentoList(Logger logger) throws CommonalityException {
	  List<TipoDocRiconoscimentoVO> rlist = new ArrayList<TipoDocRiconoscimentoVO>();

//	List<TipoDocRiconoscimentoVO> rlist = (List<TipoDocRiconoscimentoVO>) SessionCache.getInstance().get("tipoDocRiconoscimentoConsList");
//	if (rlist != null)
//		return rlist;
		
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

    SqlParameterSource namedParameters = null;


    try {
    	 rlist = jdbcTemplate.query(QUERY_TIPO_DOCUMENTO, 
    			 namedParameters, new BeanPropertyRowMapper<>(TipoDocRiconoscimentoVO.class));
    	 
		logger.debug("[LuoghiDAO::getTipoDocRiconoscimentoList] query:" + QUERY_TIPO_DOCUMENTO);
 		SessionCache.getInstance().set("tipoDocRiconoscimentoConsList", rlist);
 		return rlist;
 		
    }
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }

}
