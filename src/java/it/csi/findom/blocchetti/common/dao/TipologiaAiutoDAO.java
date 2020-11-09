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

public class TipologiaAiutoDAO {
	
	private static final String QUERY_FLAG_OBBLIGATORIO_TIPOL_AIUTO = "SELECT MIN(b.flag_obbligatorio) "
			+ " FROM findom_d_dett_tipol_aiuti a, findom_r_bandi_dett_tipol_aiuti b "
			+ " WHERE a.id_dett_tipol_aiuti = b.id_dett_tipol_aiuti "
			+ " AND a.id_tipol_aiuto = :idTipolAiuto "
			+ " AND b.id_bando = :idBando ";


	public static String getFlagObbligatorio(String idBando, String idTipoAiuto, Logger logger) throws CommonalityException {
		 
		String rval = "";

		logger.debug("[DomandaNGDAO::getStereotipoImpresa] getStereotipoImpresa() query:" + QUERY_FLAG_OBBLIGATORIO_TIPOL_AIUTO);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idTipolAiuto", idTipoAiuto, Types.NUMERIC);
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);

		try {
			rval = jdbcTemplate.queryForObject(QUERY_FLAG_OBBLIGATORIO_TIPOL_AIUTO, namedParameters, String.class);
		}
		catch (DataAccessException e) {
			logger.debug("[DomandaNGDAO::getFlagPubblicoPrivato] DataAccessException:" + e.getMessage());
		}

		return rval;

	}
}
