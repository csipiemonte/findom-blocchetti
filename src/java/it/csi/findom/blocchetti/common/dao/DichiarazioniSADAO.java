/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.blocchetti.dichiarazioniSA.ElementoDichiarazioniVO;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DichiarazioniSADAO {

	private static final String QUERY_CONF_DICHIARAZIONI = "SELECT c.id, c.id_padre AS idPadre, "
			+ " c.field_type AS typeField,"
			 + " c.field_name AS nameField,"
			 + " c.output_field_name AS outputNameField,"
			 + " c.selected_value AS valueSelectedField,"
			 + " c.not_selected_value AS valueNotSelectedField,"
			 + " c.field_label AS labelField,"
			 + " c.field_class AS classField,"
			 + " c.tag_xml AS parentTagXml, "
			 + " c.maxlength , "
			 + " c.mandatory, "
			 + " c.default_value AS defaultValueField, "
			 + " c.depends_on AS dependsOn, "
			 + " c.depends_on_value AS dependsOnValue, "
			 + " c.include_html AS includeHTML, "
			 + " c.ordinamento AS ordinamento "
			+ " FROM findom_d_conf_dichiarazioni c"
			+ " WHERE c.id_sportello_bando = :idSportello"
			+ " ORDER BY c.ordinamento";

	public static List<ElementoDichiarazioniVO> getConfDichiarazioni(Integer numSportello, Logger logger) {

		logger.debug("[DichiarazioniSADAO::getConfDichiarazioni] numSportello=" +numSportello);
		logger.debug("[DichiarazioniSADAO::getConfDichiarazioni] query:" + QUERY_CONF_DICHIARAZIONI);
		
		List<ElementoDichiarazioniVO> result = new ArrayList<ElementoDichiarazioniVO>();
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idSportello", numSportello, Types.NUMERIC);

	    try {
	    	result = jdbcTemplate.query(QUERY_CONF_DICHIARAZIONI, namedParameters, new BeanPropertyRowMapper<>(ElementoDichiarazioniVO.class));
	    }
	    catch (DataAccessException e) {
	      logger.debug("[DichiarazioniSADAO::getConfDichiarazioni] DataAccessException:" + e.getMessage());
	    }
	    
	    return result;
	}

}
