/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.costituzioneImpresa;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CostituzioneImpresaDAO {

  private static final String QUERY_DATA_COSTITUZIONE_IMPRESA = "SELECT "
		+ "(xpath('/tree-map/_costituzioneImpresa/map/dataCostituzioneImpresa/text()',  aggr_t_model.serialized_model))[1]::text AS dataCostituzioneImpresa "
  		+ " FROM  aggr_t_model WHERE  aggr_t_model.model_id = :idDomanda" ;
	
	
  public static String getDataCostituzioneImpresa(Integer idDomanda) throws CommonalityException {

    String dataCostituzioneImpresa = "";

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

    try {
    	
    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_COSTITUZIONE_IMPRESA, namedParameters);
    	if (row!=null && row.next()){
    		dataCostituzioneImpresa = row.getString("dataCostituzioneImpresa");
    	}    	
    	
    }catch (EmptyResultDataAccessException e) {
    	
	}
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }
	return dataCostituzioneImpresa;

  }

}
