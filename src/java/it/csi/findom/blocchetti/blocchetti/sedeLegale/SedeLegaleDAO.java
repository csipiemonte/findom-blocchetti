/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.sedeLegale;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class SedeLegaleDAO {

  private static final String QUERY_NODO_SEDE_LEGALE = "SELECT " +
	          				" unnest(xpath('/tree-map/_sedeLegale'::text, " +
							" aggr_t_model.serialized_model))::text AS sedelegale " +
	               			" FROM  aggr_t_model " +
	           				" WHERE aggr_t_model.model_id = :idDomanda" ;
	
	
  public static String getNodoSedeLegale(Integer idDomanda) throws CommonalityException {

    String sedelegale = "";

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

    SqlParameterSource namedParameters = new MapSqlParameterSource("idDomanda", idDomanda);

    try {
    	 List<String> sedelegaleList = jdbcTemplate.query(QUERY_NODO_SEDE_LEGALE, 
    			 namedParameters, new BeanPropertyRowMapper<>(String.class));
    	 if (sedelegaleList != null && !sedelegaleList.isEmpty()) {
    		 sedelegale = sedelegaleList.get(0);
    	 }
    	
    	return sedelegale;
    }
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }

}
