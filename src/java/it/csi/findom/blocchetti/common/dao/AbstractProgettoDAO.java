/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class AbstractProgettoDAO {

	private static String QUERY_MODEL_COLLABORAZIONE = "SELECT " +
			" (xpath('/tree-map/_abstractProgetto/map/collaborazione/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS tipo_collaborazione " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda"; 

  
  
	public static String getTipoCollaborazione(Integer idDomanda) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
		try {
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_MODEL_COLLABORAZIONE, namedParameters);
			if (row!=null && row.next())
				return row.getString("tipo_collaborazione");
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
  

