/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.bilancio;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class BilancioDAO {

  private static final String QUERY_NODO_BILANCIO = "SELECT " +
		" unnest(xpath('/tree-map/_bilancio'::text, " +
		" aggr_t_model.serialized_model))::text AS nodobilancio " +
		" FROM  aggr_t_model " +
		" WHERE aggr_t_model.model_id= :idDomanda" ;
	
  
  public static String  getNodoBilancio(Integer idDomanda, Logger logger) throws CommonalityException {
	    logger.debug("[BilancioDAO::getNodoBilancio] BEGIN");
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_NODO_BILANCIO, namedParameters);
	    	if (sqlRowSet!=null && sqlRowSet.next()){
	    		logger.debug("[BilancioDAO::getNodoBilancio] nodo _bilancio gia' presente nell'xml");
	    		return sqlRowSet.getString("nodobilancio");
	    	}else {
	    		logger.debug("[BilancioDAO::getNodoBilancio] nodo _bilancio non presente nell'xml");
	    		return null;
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.debug("[BilancioDAO::getNodoBilancio] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _bilancio");
	    	return null;
		}
	    catch (DataAccessException e) {
	    	logger.debug("[BilancioDAO::getNodoBilancio] eccezione DataAccessException nel recupero dell'eventuale nodo _bilancio");
	        throw new CommonalityException(e);
	    }finally{
	    	logger.debug("[BilancioDAO::getNodoBilancio] END");
	    }

  }

}
