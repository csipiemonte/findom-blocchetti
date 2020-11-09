/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.estremiBancari;

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

public class EstremiBancariDAO {

	private static final String QUERY_IBAN_PRESENTE 	= "SELECT EXISTS (SELECT iban FROM findom_t_soggetti_bonus_covid WHERE UPPER(iban) = UPPER(:iban) LIMIT 1);";
	private static final String QUERY_IBAN_DUPLICATO 	= "SELECT EXISTS (SELECT iban FROM findom_t_soggetti_bonus_covid WHERE UPPER(iban) = UPPER(:iban) OR ((cod_fiscale = :cfBeneficiario OR partita_iva = :cfBeneficiario) AND iban IS NOT NULL));";
	
	/** query esegue verifica che iban sia unico a livello di bando escludendo eventule iban salvato dal soggetto con cf e piva in sessione */
	private static final String QUERY_IBAN_UNIQUE_ON_DB = "SELECT EXISTS (SELECT iban FROM findom_t_soggetti_bonus_covid WHERE UPPER(iban) = UPPER(:iban) AND ((cod_fiscale NOT IN (:cfBeneficiario) AND partita_iva NOT IN (:cfBeneficiario))));";
	private static final String UPDATE_IBAN_PER_CF 		= "UPDATE findom_t_soggetti_bonus_covid SET iban= UPPER(:iban) WHERE cod_fiscale= :cfBeneficiario OR (partita_iva = :cfBeneficiario);";
	
	private static String QUERY_CODICE_IBAN = "SELECT " +
			" (xpath('/tree-map/_estremiBancari/map/iban/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS iban_xml " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";
	
	/** Gestione Bonus Piemonte covid19 */
	public static boolean getIsIbanPresente(String iban, Logger logger) throws CommonalityException {
		
		final String logprefix = "[EstremiBancariDAO:: getIsIbanPresenteByCf] ";
		
		logger.info(logprefix + " BEGIN");
		
		Boolean flag = false;

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			params.addValue("iban", iban, java.sql.Types.VARCHAR);	

			flag = (Boolean) jdbcTemplate.queryForObject(QUERY_IBAN_PRESENTE, params, Boolean.class);

		} catch (EmptyResultDataAccessException e) {
	    	logger.debug("[EstremiBancariDAO::getIsIbanPresenteByCf] eccezione EmptyResultDataAccessException");
		
		} catch (DataAccessException e) {
	    	logger.warn("[EstremiBancariDAO::getIsIbanPresenteByCf] eccezione DataAccessException");
	        throw new CommonalityException(e);
	    
		} finally{
	    	logger.debug("[EstremiBancariDAO::getIsIbanPresenteByCf] END");
	    }
			
		return flag;
	}
	
	
	/** 
	 * Gestione Bonus Piemonte covid19
	 * query: esegue verifica che iban da salvare, sia unico a livello di bando.
	 * - Esclude dal controllo, eventuale iban salvato dal soggetto 
	 * 	 a cui corrisponde (cf e piva) di chi sta compilando la domanda... 
	 **/
	public static boolean getIsIbanUniqueOnDB(String iban,  String cfBeneficiario, Logger logger) throws CommonalityException {
		
		final String logprefix = "[EstremiBancariDAO:: getIsIbanUniqueOnDB] ";
		
		logger.info(logprefix + " BEGIN");
		
		Boolean flag = false;

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			params.addValue("iban", iban, java.sql.Types.VARCHAR);	
			params.addValue("cfBeneficiario", cfBeneficiario, java.sql.Types.VARCHAR);
			
			flag = (Boolean) jdbcTemplate.queryForObject(QUERY_IBAN_UNIQUE_ON_DB, params, Boolean.class);

		} catch (EmptyResultDataAccessException e) {
	    	logger.debug("[EstremiBancariDAO::getIsIbanUniqueOnDB] eccezione EmptyResultDataAccessException");
		
		} catch (DataAccessException e) {
	    	logger.warn("[EstremiBancariDAO::getIsIbanUniqueOnDB] eccezione DataAccessException");
	        throw new CommonalityException(e);
	    
		} finally{
	    	logger.debug("[EstremiBancariDAO::getIsIbanUniqueOnDB] END");
	    }
			
		return flag;
	}
	
	
	public static boolean getIsIbanDuplicato(String iban, String cfBeneficiario, Logger logger) throws CommonalityException {
		
		final String logprefix = "[EstremiBancariDAO:: getIsIbanDuplicatoByCf] ";
		
		logger.info(logprefix + " BEGIN");
		
		Boolean flag = false;

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			params.addValue("iban", iban, java.sql.Types.VARCHAR);	
			params.addValue("cfBeneficiario", cfBeneficiario, java.sql.Types.VARCHAR);

			flag = (Boolean) jdbcTemplate.queryForObject(QUERY_IBAN_DUPLICATO, params, Boolean.class);

		} catch (EmptyResultDataAccessException e) {
	    	logger.debug("[EstremiBancariDAO::getIsIbanDuplicatoByCf] eccezione EmptyResultDataAccessException");
		
		} catch (DataAccessException e) {
	    	logger.warn("[EstremiBancariDAO::getIsIbanDuplicatoByCf] eccezione DataAccessException");
	        throw new CommonalityException(e);
	    
		} finally{
	    	logger.debug("[EstremiBancariDAO::getIsIbanDuplicatoByCf] END");
	    }
			
		return flag;
	}


	public static boolean updateIbanByCf(String iban, String cfBeneficiario,  Logger logger) throws CommonalityException {
		final String logprefix = "[EstremiBancariDAO:: updateIbanByCf] ";
		logger.info(logprefix + " BEGIN");
		
		 if(iban!=null) {	
			
			try {
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
				namedParameters.addValue("iban", iban);
				namedParameters.addValue("cfBeneficiario", cfBeneficiario);
				
				Integer cnt = jdbcTemplate.update(UPDATE_IBAN_PER_CF, namedParameters);
				logger.info(" cnt: " + cnt);
				return cnt != null && cnt > 0;
				
			} 
			catch (DataAccessException e) {
				return false;
			}finally {
				logger.info(logprefix + " END");
			}
		}
		return false;
	 }
	
	
	/**
	 * Recupero codice iban
	 */
	public static String getCodiceIban(Integer idDomanda) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
		try {
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_CODICE_IBAN, namedParameters);
			if (row!=null && row.next())
				return row.getString("iban_xml");
			else 
				return "";
		}
		catch (EmptyResultDataAccessException e) {
			return "";
		}
		catch (DataAccessException e) {
		  throw new CommonalityException(e);
		}

	}
	
}
