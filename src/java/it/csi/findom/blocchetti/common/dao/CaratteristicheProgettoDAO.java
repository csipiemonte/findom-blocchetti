/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CaratteristicheProgettoDAO {
	
	
	private static final String QUERY_DESCR_TIPOLOGIA_INTERVENTO_BY_ID = " select descrizione from findom_d_tipol_interventi where id_tipol_intervento = :idTipolIntervento ";

	private static final String QUERY_DESCR_DETTAGLIO_INTERVENTO_BY_ID = " select descrizione from findom_d_dett_tipol_interventi where id_dett_tipol_intervento = :idDetIntervento ";
	

	public static String getDescrTipolInterventoById(String idTipolIntervento, Logger logger) throws CommonalityException {
		 
		String rval = "";
		if(StringUtils.isBlank(idTipolIntervento)){
			return rval;
		}
		
		Integer idTipoInterventoInt = Integer.parseInt(idTipolIntervento);

		logger.debug("[CaratteristicheProgettoDAO::getDescrTipolInterventoById] idTipoInterventoInt:" + idTipoInterventoInt);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idTipolIntervento", idTipoInterventoInt, Types.NUMERIC);		

		try {
			rval = jdbcTemplate.queryForObject(QUERY_DESCR_TIPOLOGIA_INTERVENTO_BY_ID, namedParameters, String.class);
		}
		catch (DataAccessException e) {
			logger.debug("[CaratteristicheProgettoDAO::getDescrTipolInterventoById] DataAccessException:" + e.getMessage());
		}

		return rval;

	}
	
	/** CR5: Condominio ::2R */
	public static String getDescrDettaglioById(String idDettaglioIntervento, Logger logger) throws CommonalityException {
		 
		String rval = "";
		if(StringUtils.isBlank(idDettaglioIntervento)){
			return rval;
		}
		
		Integer idDetIntervento = Integer.parseInt(idDettaglioIntervento);
		logger.info("[CaratteristicheProgettoDAO::getDescrDettaglioById] idDetIntervento:" + idDetIntervento);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idDetIntervento", idDetIntervento, Types.NUMERIC);		

		try {
			rval = jdbcTemplate.queryForObject(QUERY_DESCR_DETTAGLIO_INTERVENTO_BY_ID, namedParameters, String.class);
		}
		catch (DataAccessException e) {
			logger.debug("[CaratteristicheProgettoDAO::getDescrDettaglioById] DataAccessException:" + e.getMessage());
		}

		return rval;

	}
}
