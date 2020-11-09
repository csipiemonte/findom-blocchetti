/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.dipartimento.DipartimentoVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DipartimentoDAO {

	private static final String QUERY_DIPARTIMENTI = "SELECT a.id_dipartimento AS id, a.codice AS codice, a.descrizione AS descrizione "			 	
		    + " FROM findom_d_dipartimenti a "	
		    + " WHERE a.id_ente_strutt = :idEnteStrutt and"
		    + " (a.dt_inizio is null  or a.dt_inizio <= CURRENT_DATE) and (a.dt_fine is null or a.dt_fine > CURRENT_DATE) ";
		  
	private static final String SELECT_UO_BY_CODICE_UO = "SELECT id_dipartimento AS id, codice AS codice, descrizione AS descrizione "			 	
		    + " FROM findom_d_dipartimenti "	
		    + " WHERE codice = :codice AND id_ente_strutt = :idEnteStrutt AND "
		    + " (dt_inizio is null OR dt_inizio <= CURRENT_DATE) AND (dt_fine is null OR dt_fine > CURRENT_DATE) ";
	
	private static final String INSERT_UNITA_ORGANIZZATIVA = "INSERT INTO findom_d_dipartimenti (id_dipartimento, codice, descrizione, id_ente_strutt, dt_inizio, dt_fine) "
			+ " VALUES ((SELECT max(id_dipartimento)+1 FROM findom_d_dipartimenti), "
			+ " :codice, :descrizione, :idEnteStrutt, "
			+ " now(), null)";
	   
	public static List<DipartimentoVO> getDipartimentiList(String idEnteStrutt) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		Integer idEnteStruttInt =new Integer(0);
		try {
			idEnteStruttInt = Integer.parseInt(idEnteStrutt);
		} catch (NumberFormatException e1) {
			return null;
		}
			
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idEnteStrutt", idEnteStruttInt, Types.NUMERIC);

		try {
			return jdbcTemplate.query(QUERY_DIPARTIMENTI, namedParameters,
					new BeanPropertyRowMapper<DipartimentoVO>(DipartimentoVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	public static DipartimentoVO getUnitaOrganizzativaByIdEnteECodice (String idEnteStrutt, String codice, Logger logger) throws CommonalityException {
		final String logprefix = "[DipartimentoDAO::getUnitaOrganizzativaByIdEnteECodice] ";
		logger.info(logprefix + " BEGIN");
		DipartimentoVO dipartimentoVO = null;
		String query = SELECT_UO_BY_CODICE_UO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			Integer idEnteStruttInt = Integer.parseInt(idEnteStrutt);
			params.addValue("idEnteStrutt", idEnteStruttInt, Types.INTEGER);
			params.addValue("codice", codice, Types.VARCHAR);
			
			logger.info(logprefix + " query = " + query);
			ArrayList<DipartimentoVO> lista = (ArrayList<DipartimentoVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<DipartimentoVO>(DipartimentoVO.class));
			if(lista!=null && lista.size()>0 && lista.get(0)!=null){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size() + "; ne restituisco il primo elemento");
				dipartimentoVO = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return dipartimentoVO;
	}
	
	public static String insertUnitaOrganizzativa(String codice, String descrizione, String idEnteStrutt, Logger logger) throws CommonalityException { 
		final String logprefix = "[PartnerDAO:: insertUnitaOrganizzativa] ";
		logger.info(logprefix + " BEGIN");
		String idUnitaOrganizzativa = "";
		String insertStatement = "";
		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(StringUtils.isBlank(codice) || StringUtils.isBlank(descrizione)  || StringUtils.isBlank(idEnteStrutt)){
				return idUnitaOrganizzativa;
			}

			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			insertStatement = INSERT_UNITA_ORGANIZZATIVA;

			params.addValue("codice", codice.trim(), java.sql.Types.VARCHAR);			
			params.addValue("descrizione", descrizione, java.sql.Types.VARCHAR);
			Integer idEnteStruttInt = Integer.parseInt(idEnteStrutt);
			params.addValue("idEnteStrutt", idEnteStruttInt, java.sql.Types.INTEGER);

			jdbcTemplate.update(insertStatement, params, keyHolder, new String[]{"id_dipartimento"});
			Number idUO = keyHolder.getKey();
			if(idUO!=null){
				idUnitaOrganizzativa = idUO.toString();
			}
		}catch (Exception e) {
			logger.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return	idUnitaOrganizzativa;	
	}

}
