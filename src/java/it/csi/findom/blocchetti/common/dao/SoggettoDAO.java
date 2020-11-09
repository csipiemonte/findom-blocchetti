/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.soggetto.SoggettoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class SoggettoDAO {

	private static final String QUERY_SOGGETTO_PER_CF = "SELECT COALESCE(denominazione, cognome || ' ' || nome, denominazione) AS denominazione, "
			 + " id_forma_giuridica AS idformagiuridica, sigla_nazione AS idStato FROM shell_t_soggetti  "
			 + " WHERE cod_fiscale = :codiceFiscale ";
	   	
	private static final String UPDATE_SOGGETTO_PER_CF = "UPDATE shell_t_soggetti SET denominazione = :denominazione, " +
				               "id_forma_giuridica = :idFormaGiuridica WHERE cod_fiscale = :codiceFiscale";
	
	
	//gestione beneficiario estero inizio qqq
	private static final String UPDATE_NAZIONE_SOGGETTO_PER_CF = "UPDATE shell_t_soggetti SET sigla_nazione = :siglaNazione WHERE cod_fiscale = :codiceFiscale";
	
	// Recupero denominazione e forma giuridica del soggetto da shell_t_soggetti in base al cf	
	public static List<SoggettoVO> getSoggettoByCodFiscale(String codiceFiscale) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		List<SoggettoVO> lista = null;
		
		 if(codiceFiscale!=null) {	
			 
			SqlParameterSource namedParameters = new MapSqlParameterSource("codiceFiscale", codiceFiscale);
		 	try {
		 		lista = jdbcTemplate.query(QUERY_SOGGETTO_PER_CF, namedParameters,
						new BeanPropertyRowMapper<SoggettoVO>(SoggettoVO.class));
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		 }
		 return lista;
	}

	// Recupero denominazione e forma giuridica del soggetto da shell_t_soggetti in base al cf	
	public static void updateSoggettoByCodFiscale(String codiceFiscale, String denominazione, String idFormaGiuridica) throws CommonalityException {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		 if(codiceFiscale!=null) {	
			 
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("codiceFiscale", codiceFiscale);
			namedParameters.addValue("denominazione", denominazione);
			namedParameters.addValue("idFormaGiuridica", idFormaGiuridica, Types.NUMERIC);
			
		 	try {
		 		jdbcTemplate.update(UPDATE_SOGGETTO_PER_CF, namedParameters);
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		 }
	}

	//gestione beneficiario estero qqq
	public static void updateNazioneSoggettoByCodFiscale(String codiceFiscale, String siglaNazione, Logger logger) throws CommonalityException {
		final String logprefix = "[SoggettoDAO:: updateNazioneSoggettoByCodFiscale] ";
		logger.info(logprefix + " BEGIN");

		if(StringUtils.isBlank(codiceFiscale)) {
			return; 
		}		 
		try {		
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			namedParameters.addValue("codiceFiscale", codiceFiscale);
			namedParameters.addValue("siglaNazione", siglaNazione);
			
			jdbcTemplate.update(UPDATE_NAZIONE_SOGGETTO_PER_CF, namedParameters);

		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}

		logger.info(logprefix + " END");
	}


}
