/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.ente.EnteLocaleVO;
import it.csi.findom.blocchetti.common.vo.ente.EnteStrutturatoVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class EnteDAO {

	private static final String QUERY_ENTE_PER_CF = " SELECT a.id_ente_strutt AS id, a.codice AS codice, a.descrizione AS descrizione "			 	
		   + " FROM findom_d_enti_strutturati a "		
		   + " WHERE a.cod_fiscale = :codFiscale  and "
		   + " ( a.dt_inizio is null or a.dt_inizio <= CURRENT_DATE) and (a.dt_fine is null or a.dt_fine > CURRENT_DATE) ";
		
	private static final String QUERY_ENTE_LOCALE_PER_CF = "SELECT CODICE_ISTAT AS codice, DENOMINAZIONE_COMPLETA AS descrizione, "
			+ "popolazione, classificazione, TIPO_ENTE AS tipoente "
			+ "FROM findom_v_enti_locali_piemontesi "
			+ "WHERE codice_fiscale = :codFiscale";
	
	private static final String QUERY_ENTE_LOCALE_PIEMONTE = "SELECT codice_istat as codice, denominazione_completa as descrizione, popolazione, classificazione, tipo_ente as tipoente "
			+ " FROM findom_v_enti_locali_piemontesi "
			+ " WHERE tipo_ente='COMUNE' and popolazione <= 5000 "
			+ " ORDER BY denominazione asc "; 
    
    private static final String QUERY_ENTE_LOCALE_PIEMONTE_CON_CLAUSOLA = "SELECT codice_istat as codice, denominazione_completa as descrizione, popolazione, classificazione, tipo_ente as tipoente "
					+ " FROM findom_v_enti_locali_piemontesi "
					+ " WHERE tipo_ente='COMUNE' and  #clausola# "
					+ " ORDER BY denominazione asc "; 
	
	private static final String QUERY_CODICE_ISTAT_PER_CF = "SELECT codice_istat "
			+ "FROM findom_v_enti_locali_piemontesi "
			+ "WHERE codice_fiscale = :codFiscale";
	
	private static final String INSERT_ENTE_STRUTTURATO = "INSERT INTO findom_d_enti_strutturati (id_ente_strutt, codice, cod_fiscale, descrizione, dt_inizio, dt_fine) "
			+ "VALUES ((SELECT max(id_ente_strutt)+1 FROM findom_d_enti_strutturati), "
			+ " (SELECT max(codice::numeric)+1 FROM findom_d_enti_strutturati), "
			+ ":codiceFiscale, :descrizione, "
			+ " now(), null)";
	
	

	
	// ritorna l'idEnteStrutturato del codice fiscale passato	
	public static String getIdEnteStrutturato(String codiceFiscale) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String idEnteStrutt = null;
		
		 if(codiceFiscale!=null) {	
			 
			SqlParameterSource namedParameters = new MapSqlParameterSource("codFiscale", codiceFiscale);
		 	try {
		 		List<EnteStrutturatoVO> listaEnteStrutt = jdbcTemplate.query(QUERY_ENTE_PER_CF, namedParameters,
						new BeanPropertyRowMapper<EnteStrutturatoVO>(EnteStrutturatoVO.class));
		 		if (listaEnteStrutt != null && !listaEnteStrutt.isEmpty()) {
		 			idEnteStrutt = listaEnteStrutt.get(0).getId();
		 		}
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		 }
		 return idEnteStrutt;
	}
	
		// Recupero denominazione e forma giuridica del soggetto da shell_t_soggetti in base al cf	
	public static List<EnteLocaleVO> getEnteLocale(String codiceFiscale) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		List<EnteLocaleVO> listaEnteStrutt = null;
		
		if(codiceFiscale!=null) {	
			 
			SqlParameterSource namedParameters = new MapSqlParameterSource("codFiscale", codiceFiscale);
		 	try {
		 		listaEnteStrutt = jdbcTemplate.query(QUERY_ENTE_LOCALE_PER_CF, namedParameters,
						new BeanPropertyRowMapper<EnteLocaleVO>(EnteLocaleVO.class));
		 		
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		}
		return listaEnteStrutt;
	}	 
	
	// Recupero denominazione e forma giuridica del soggetto da shell_t_soggetti in base al cf	
	public static List<EnteLocaleVO> getComuniPiemonteList() throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	
		List<EnteLocaleVO> listaEnte = null;
			 
		SqlParameterSource namedParameters = new MapSqlParameterSource();
	 	try {
	 		listaEnte = jdbcTemplate.query(QUERY_ENTE_LOCALE_PIEMONTE, namedParameters,
					new BeanPropertyRowMapper<EnteLocaleVO>(EnteLocaleVO.class));
	 		
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		return listaEnte;
	}
	
	//estrae i comuni del piemonte eventualmente filtrati da clausola	
	public static List<EnteLocaleVO> getComuniPiemonteList(String clausola, Logger logger) throws CommonalityException {
		logger.info("[EnteDAO::getComuniPiemonteList - versione con clausola] BEGIN");
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		List<EnteLocaleVO> listaEnte = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource();
		try {
			if(StringUtils.isBlank(clausola)){
			  clausola = "1=1"; //clausola finta	
			}
			String query = QUERY_ENTE_LOCALE_PIEMONTE_CON_CLAUSOLA; 
			query = query.replace("#clausola#", clausola);
			logger.info("[EnteDAO::getComuniPiemonteList - versione con clausola] query = " + query);
			listaEnte = jdbcTemplate.query(query, namedParameters,
					new BeanPropertyRowMapper<EnteLocaleVO>(EnteLocaleVO.class));
			logger.info("[EnteDAO::getComuniPiemonteList - versione con clausola] listaEnte ha num elementi = " + listaEnte.size());
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.info("[EnteDAO::getComuniPiemonteList - versione con clausola] END");
		return listaEnte;
	}	
	
	public static String getCodIstat(String codiceFiscale) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String codiceIstat = null;

		if(codiceFiscale!=null) {	

			//SqlParameterSource namedParameters = new MapSqlParameterSource("codFiscale", codiceFiscale);
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("codFiscale", codiceFiscale, Types.VARCHAR);


			try {
				//List<String> listaIstat = jdbcTemplate.query(QUERY_CODICE_ISTAT_PER_CF, namedParameters,new BeanPropertyRowMapper<String>(String.class));
				List<String> listaIstat =jdbcTemplate.queryForList(QUERY_CODICE_ISTAT_PER_CF,namedParameters,String.class);
				//codiceIstat = jdbcTemplate.queryForObject(QUERY_CODICE_ISTAT_PER_CF, namedParameters, String.class);
				if (listaIstat != null && !listaIstat.isEmpty()) {
					codiceIstat = listaIstat.get(0);
				}
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		}
		return codiceIstat;
	}
	
	public static String insertEnteStrutturato(String codiceFiscale, String descrizioneEnteStrutturato, Logger logger) throws CommonalityException { 
		final String logprefix = "[PartnerDAO:: insertEnteStrutturato] ";
		logger.info(logprefix + " BEGIN");
		String idEnteStrutturato = "";
		String insertStatement = "";
		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(StringUtils.isBlank(codiceFiscale)|| StringUtils.isBlank(descrizioneEnteStrutturato)){
				return idEnteStrutturato;
			}

			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			insertStatement = INSERT_ENTE_STRUTTURATO;
			

			params.addValue("codiceFiscale", codiceFiscale, java.sql.Types.VARCHAR);			
			params.addValue("descrizione", descrizioneEnteStrutturato, java.sql.Types.VARCHAR);			

			jdbcTemplate.update(insertStatement, params, keyHolder, new String[]{"id_ente_strutt"});
			Number idEnteStrutt = keyHolder.getKey();
			if(idEnteStrutt!=null){
				idEnteStrutturato = idEnteStrutt.toString();
			}
		}catch (Exception e) {
			logger.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return	idEnteStrutturato;	
	}

}
