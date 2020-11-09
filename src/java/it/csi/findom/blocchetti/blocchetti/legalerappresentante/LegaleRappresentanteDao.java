/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.legalerappresentante;

import it.csi.findom.blocchetti.util.StringUtils;
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

public class LegaleRappresentanteDao {

  private static String QUERY_MODEL_FLG_AAEP = "SELECT aggr_t_model.model_id, " +
			" aggr_t_model.model_progr, " +
			" aggr_t_model.user_id, " +
			" aggr_t_model.template_code_fk, " +
			" unnest(xpath('/tree-map/_legaleRappresentante/map/lrFromAAEP/text()'::text, " +
			" aggr_t_model.serialized_model))::text AS lrFromAAEP, " +
			" unnest(xpath('/tree-map/_legaleRappresentante/map/lrFromAAEP'::text, " +
			" aggr_t_model.serialized_model))::text AS rti2 " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";
  
  /** CR5: Condominio :: */
  private static final String QUERY_CODICE_COMUNE_NASCITA_BY_DESC_COMUNE = " select codfisc from ext_d_comuni where descom = :int_codice_comune ";
  

  public static String getPresenzaLrAAEP(String idDomanda) throws CommonalityException {

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
    try {
    	SqlRowSet flag = jdbcTemplate.queryForRowSet(QUERY_MODEL_FLG_AAEP, namedParameters);
    	if (flag!=null && flag.next())
    		return flag.getString("lrFromAAEP");
    	else 
    		return null;
    }
    catch (EmptyResultDataAccessException e) {
    	return null;
	}
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }
  
  /** CR5: Condominio :: */
  public static String getCodiceComuneByDescrizione(String descrizioneComune, Logger logger) throws CommonalityException {
  	 
  	String rval = "";
  	if(StringUtils.isBlank(descrizioneComune)){
  		return rval;
  	}
  	
  	Integer int_codice_comune = Integer.parseInt(descrizioneComune);
  	logger.info("[LegaleRappresentanteDao::getCodiceComuneByDescrizione] int_codice_comune:" + int_codice_comune);

  	NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

  	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
  	namedParameters.addValue("int_codice_comune", int_codice_comune, Types.NUMERIC);		

  	try {
  		rval = jdbcTemplate.queryForObject(QUERY_CODICE_COMUNE_NASCITA_BY_DESC_COMUNE, namedParameters, String.class);
  	}
  	catch (DataAccessException e) {
  		logger.info("[LegaleRappresentanteDao::getCodiceComuneByDescrizione] DataAccessException:" + e.getMessage());
  	}
  	return rval;
  }
  
  
 
  
}
