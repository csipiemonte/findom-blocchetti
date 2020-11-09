/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.attivita.AttivitaVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class AttivitaDAO {

	private static final String QUERY_ATTIVITA_ECONOMICHE = "SELECT id_settore_attivita AS id, cod_settore AS codice, desc_settore AS descrizione, "
	    +     "CASE WHEN length(desc_settore)>80 THEN substring(desc_settore from 0 for 80) || '...' "
	    +     "ELSE desc_settore  END  AS descrtroncata "
	    + "FROM findom_d_settore_attivita "
	    + "WHERE  (dt_inizio is null  or dt_inizio <= CURRENT_DATE) and dt_fine is null "
	    + "ORDER BY desc_settore ";
	   	
	
	public static List<AttivitaVO> getSettoreAttivitaEconomicaList() throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		try {
			return jdbcTemplate.query(QUERY_ATTIVITA_ECONOMICHE, namedParameters,
					new BeanPropertyRowMapper<AttivitaVO>(AttivitaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}

}
