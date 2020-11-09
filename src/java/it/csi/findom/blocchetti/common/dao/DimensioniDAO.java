/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class DimensioniDAO {
	
	private static final String QUERY_ID_DIMENSIONE_BY_DESCR = " select id_dimensione as id from findom_d_dimensioni_imprese where upper(descrizione) = upper(:descrizione) ";
	private static final String QUERY_DESCR_BY_ID_DIMENSIONE= " select descrizione as descr from findom_d_dimensioni_imprese where id_dimensione = :idDimensione ";
	
	public static String getIdDimensioneByDescr(String descrizione, Logger logger) throws CommonalityException {		 
		String rval = "";
		logger.debug("[DimensioniDAO::getIdDimensioneByDescr] viene cercata la dimensione avente descrizione:" + descrizione);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("descrizione", descrizione);
		try {			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_ID_DIMENSIONE_BY_DESCR, namedParameters);
	    	if (row!=null && row.next()){
	    		Integer id =  row.getInt("id");
	    		rval = String.valueOf(id);
	    	}
		}
		catch (DataAccessException e) {
			logger.debug("[DimensioniDAO::getIdDimensioneByDescr] DataAccessException:" + e.getMessage());
		}
		return rval;
	}
	
	public static String getDescrByIdDimensione(String idDimensione, Logger logger) throws CommonalityException {		 
		String rval = "";
		logger.debug("[DimensioniDAO::getDescrByIdDimensione] viene cercata la dimensione avente id:" + idDimensione);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		Integer idDimensioneInt = 0;
		try {
			idDimensioneInt = Integer.parseInt(idDimensione);
		} catch (NumberFormatException e1) {
			return null;
		}

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idDimensione", idDimensioneInt, java.sql.Types.INTEGER);
		try {			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DESCR_BY_ID_DIMENSIONE, namedParameters);
	    	if (row!=null && row.next()){	    		 
	    		rval = row.getString("descr");
	    	}
		}
		catch (DataAccessException e) {
			logger.debug("[DimensioniDAO::getDescrByIdDimensione] DataAccessException:" + e.getMessage());
		}
		return rval;
	}
}
