/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.abstractprogettoNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class AbstractProgettoNGDAO {
	
	private static final String QUERY_DATA_INIZIO_MAX_INIZIO_FINE_PROGETTO = 
			"SELECT " 
			+ " to_char(a.dt_inizio_progetto, 'DD/MM/YYYY') AS dataInizioProgetto, "
			+ " to_char(a.dt_max_inizio_progetto, 'DD/MM/YYYY') AS max_inizio_progetto,"
			+ " to_char(a.dt_fine_progetto, 'DD/MM/YYYY') AS fine_progetto"
			+ " from findom_t_bandi a "
			+ " where id_bando = :idBando;";

	private static final String QUERY_DATA_INIZIO_PROGETTO = "SELECT a.dt_inizio_progetto, to_char(a.dt_inizio_progetto, 'DD/MM/YYYY') AS dataInizioProgetto "
			+ " from findom_t_bandi a "
			+ " where id_bando = :idBando;";
	
	private static final String QUERY_DATA_FINE_PROGETTO = "SELECT a.dt_fine_progetto, to_char(a.dt_fine_progetto, 'DD/MM/YYYY') AS dataFineProgetto "
			+ " from findom_t_bandi a "
			+ " where id_bando = :idBando;";
	
	private static final String QUERY_TITOLO_SISTEMA_NEVE = "SELECT edizione AS edizione "
			+ " from findom_t_sportelli_bandi "
			+ " where id_bando = :idBando;";
	
	private static final String QUERY_TITOLO_SISTEMA_NEVE_BY_SPORTELLO = "SELECT edizione AS edizione "
			+ " from findom_t_sportelli_bandi "
			+ " where id_sportello_bando = :idSportelloBando;";
	
	/**
	 * Jira: 1355
	 */
	private static final String QUERY_DATA_MAX_INIZIO_PROGETTO = "SELECT a.dt_max_inizio_progetto , to_char(a.dt_max_inizio_progetto, 'DD/MM/YYYY') AS dataMaxInizioProgetto "
			+ " from findom_t_bandi a "
			+ " where id_bando = :idBando;";
	
	

	/**
	 * getDataInizioProgetto
	 * 
	 * @param idBando
	 * @return
	 * @throws CommonalityException
	 */
	public static String getDataInizioProgetto(String idBando, Logger logger) throws CommonalityException {
			
		String rval = "";
		logger.info("[AbstractProgettoNGDAO::getDataInizioProgetto] query:" + QUERY_DATA_INIZIO_PROGETTO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_INIZIO_PROGETTO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("dataInizioProgetto");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[AbstractProgettoNGDAO::getDataInizioProgetto] si e' verificata una DataAccessException ");
	         throw new CommonalityException(e);
	    }
	    return rval;
	}
	
	/**
	 * getDataFineProgetto
	 * 
	 * @param idBando
	 * @return
	 */
	public static String getDataFineProgetto(String idBando, Logger logger) throws CommonalityException {
		
		String rval = "";
		
		logger.info("[AbstractProgettoNGDAO::getDataFineProgetto] query:" + QUERY_DATA_FINE_PROGETTO);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_FINE_PROGETTO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("dataFineProgetto");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[AbstractProgettoNGDAO::getDataFineProgetto] si e' verificata una DataAccessException ");
	         throw new CommonalityException(e);
	    }
	    return rval;
	}
	
	/**
	 * Jira: 1355
	 */
	public static String getDataMaxInizioProgetto(String idBando, Logger logger) throws CommonalityException {
		
		String rval = "";
		
		logger.info("[AbstractProgettoNGDAO::getDataMaxInizioProgetto] query:" + QUERY_DATA_MAX_INIZIO_PROGETTO);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_MAX_INIZIO_PROGETTO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("dataMaxInizioProgetto");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[AbstractProgettoNGDAO::getDataMaxInizioProgetto] si e' verificata una DataAccessException ");
	         throw new CommonalityException(e);
	    }
	    return rval;
	}
	
	/**
	 * 
	 * @param idBando
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<String> getDateInizioMaxFineProgettoList(String idBando, Logger logger)
			throws CommonalityException {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		
		try {
			List<String> dateList = new ArrayList<String>();
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_INIZIO_MAX_INIZIO_FINE_PROGETTO, namedParameters);
	    	if (row!=null && row.next()){
	    		dateList.add(row.getString("dataInizioProgetto"));
	    		dateList.add(row.getString("max_inizio_progetto"));
	    		dateList.add(row.getString("fine_progetto"));
	    	}

	    	return dateList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	
	/**
	 * Jira: 1428: sistema neve
	 */
	public static String getTitoloSistemaNeveByIdBando(String idBando, Logger logger) throws CommonalityException {
		
		String rval = "";
		logger.debug("[AbstractProgettoNGDAO::getTitoloSistemaNeveByIdBando] query:" + QUERY_TITOLO_SISTEMA_NEVE);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_TITOLO_SISTEMA_NEVE, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("edizione");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.error("[AbstractProgettoNGDAO::getTitoloSistemaNeveByIdBando] si e' verificata una DataAccessException ");
	    	throw new CommonalityException(e);
	    }
	    return rval;
	}
	
	public static String getTitoloSistemaNeveByIdSportelloBando(String idSportelloBando, Logger logger) throws CommonalityException {
		
		String rval = "";
		logger.debug("[AbstractProgettoNGDAO::getTitoloSistemaNeveByIdSportelloBando] query:" + QUERY_TITOLO_SISTEMA_NEVE_BY_SPORTELLO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_TITOLO_SISTEMA_NEVE_BY_SPORTELLO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("edizione");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.error("[AbstractProgettoNGDAO::getTitoloSistemaNeveByIdSportelloBando] si e' verificata una DataAccessException ");
	    	throw new CommonalityException(e);
	    }
	    return rval;
	}
	
}
