/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ParametriDAO {
	
	private static final String QUERY_PARAMETRO_BY_ID = " select valore from findom_d_parametri where id_parametro = :idParametro ";


	public static String getValoreParametroById(int idParametro, Logger logger) throws CommonalityException {
		 
		String rval = "";

		logger.debug("[ParametriDAO::getValoreParametroById] parametro:" + idParametro);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idParametro", idParametro, Types.NUMERIC);		

		try {
			rval = jdbcTemplate.queryForObject(QUERY_PARAMETRO_BY_ID, namedParameters, String.class);
		}
		catch (DataAccessException e) {
			logger.debug("[ParametriDAO::getValoreParametroById] DataAccessException:" + e.getMessage());
		}

		return rval;

	}
}
