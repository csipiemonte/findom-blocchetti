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

public class RegoleDAO {

	public static boolean isAssociataRegolaAlBando(String codRegola, String idBando, Logger logger) throws CommonalityException {
		logger.debug("[RegoleDAO::isAssociataRegolaAlBando] codRegola:" + codRegola + ", idBando:"+ idBando);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		boolean isAss = false;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idBando", Integer.parseInt(idBando), Types.NUMERIC);	
		params.addValue("codRegola", codRegola, Types.VARCHAR);

		String query = "select count(*) from  findom_r_bandi_regole r, findom_d_regole re " +
					" where r.id_bando = :idBando " +
					" AND re.cod_regola = :codRegola  " + 
					" AND re.id_regola = r.id_regola " ;

		logger.debug("[RegoleDAO::isAssociataRegolaAlBando] query:" + query);
		try {
			Integer num = jdbcTemplate.queryForObject(query, params, Integer.class);
			if(num>0) isAss = true;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.debug("[RegoleDAO::isAssociataRegolaAlBando] END. isAss:"+isAss);
		return isAss;
	}

}
