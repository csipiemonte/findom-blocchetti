/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.schedaProgetto.CriteriTipoligieInterventoRowMapper;
import it.csi.findom.blocchetti.common.vo.schedaProgetto.OrdineCriteriRowMapper;
import it.csi.findom.blocchetti.common.vo.schedaProgetto.VistaCriterioVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CriterioDAO {

	private static final String QUERY_CRITERI = "SELECT id_bando, descr_breve_bando, id_tipol_intervento, descrizione_tipol_intervento," +
       "id_criterio, descrizione_criterio, descr_breve_criterio, ordine_criterio, id_specifica," +
       "descrizione_specifica, descr_breve_specifica, tipo_campo, ordine_specifica," +
       "id_parametro, descrizione_parametro, descr_breve_parametro, ordine_parametro," +
       "id_parametro_valut, punteggio_parametro " +
       " FROM findom_v_scheda_progetto_valut" +
       " WHERE id_bando = :idBando" +
       " ##ANDCLAUSE## " +
       " ORDER BY ordine_criterio, ordine_specifica, ordine_parametro";
	
	private static final String QUERY_COUNTCRITERI = "SELECT count(distinct(id_criterio)) FROM findom_v_scheda_progetto_valut" + 
			 " WHERE id_bando = :idBando" ;
	
	private static final String QUERY_ORDINECRITERI = "SELECT distinct(id_Criterio), ordine_criterio FROM findom_v_scheda_progetto_valut "+
			" WHERE id_bando = :idBando ORDER BY ordine_criterio, id_Criterio";
	
//	private static final String QUERY_CRITERITIPOLOGIEVENTO = "SELECT distinct(id_tipol_intervento), id_criterio FROM findom_v_scheda_progetto_valut "+
//			" WHERE id_bando = :idBando AND id_criterio IN (##ANDCLAUSE##) AND id_tipol_intervento is not null ORDER BY id_tipol_intervento, id_criterio";
	 
	private static final String QUERY_CRITERITIPOLOGIEVENTO = "SELECT distinct(id_tipol_intervento), id_criterio FROM findom_v_scheda_progetto_valut "+
			" WHERE id_bando = :idBando ##ANDCLAUSE## AND id_tipol_intervento is not null ORDER BY id_tipol_intervento, id_criterio";
	
	private static final String QUERY_DESCRIZIONE_TIPOLOGIA_INTERVENTO_BY_CRITERIO = " SELECT b.descrizione FROM findom_r_bandi_criteri_valut a "
			+ " INNER JOIN findom_d_tipol_interventi b ON a.id_tipol_intervento = b.id_tipol_intervento "
			+ " WHERE a.id_bando = :idBando "
			+ " AND a.id_criterio= :idCriterioSelezionato; "; 
	
	/** Jira: 1969 -2r : recupero tipologia intervento selezionato */
	private static final String QUERY_CRITERI_ID_TIPO_INTERVENTO = "SELECT " +
			" (xpath('/tree-map/_criteri/map/criteriList/list/map[idCriterio=31]/idTipolIntervento/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS idtipointervento " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda" ;
	
	/** Jira: 1969 -2r : recupero idParametro selezionato */
	private static final String QUERY_CRITERI_ID_PARAMETRO = "SELECT " +
			" (xpath('/tree-map/_criteri/map/criteriList/list/map[idCriterio=31]/specificheList/list/map[idSpecifica=63]/parametriList/list/map[checked=\"checked\"]/idParametro/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS idparametro " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda" ;
	
	/** Jira: 1969 -2r : recupero idCriterio selezionato */
	private static final String QUERY_CRITERI_IDCRITERIO_SELEZIONATO = "SELECT " +
			" (xpath('/tree-map/_criteri/map/criteriList/list/map/idCriterio/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS idcriteriosel " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda" ;
	
	
	/** Jira: 1969 -2r */
	public static String getIdCriterioSelezionato(Integer idDomanda, Logger logger) throws CommonalityException {
		
		logger.info("[CriterioDAO::getIdCriterioSelezionato] BEGIN");
	    
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    
	    try {
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_CRITERI_IDCRITERIO_SELEZIONATO, namedParameters);
			if (sqlRowSet!=null && sqlRowSet.next()){
				logger.debug("[CriterioDAO::getIdCriterioSelezionato] nodo _criteri gia' presente nell'xml");
				return sqlRowSet.getString("idcriteriosel");
			}else {
				logger.debug("[CriterioDAO::getIdCriterioSelezionato] nodo _criteri non presente nell'xml");
				return null;
			}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CriterioDAO::getIdCriterioSelezionato] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _criteri");
	    	return null;
		}
	    catch (DataAccessException e) {
	    	logger.debug("[CriterioDAO::getIdCriterioSelezionato] eccezione DataAccessException nel recupero dell'eventuale nodo _criteri");
	    	throw new CommonalityException(e);
	    }finally{
	    	logger.debug("[CriterioDAO::getIdCriterioSelezionato] END");
	    }
	}
	
	
	/** Jira: 1969 -2r */
	public static String getIdParametroCriteri(Integer idDomanda, Logger logger) throws CommonalityException {
		logger.info("[CriterioDAO::getIdParametroCriteri] BEGIN");
	    
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    
	    try {
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_CRITERI_ID_PARAMETRO, namedParameters);
			if (sqlRowSet!=null && sqlRowSet.next()){
				logger.debug("[CriterioDAO::getIdParametroCriteri] nodo _criteri gia' presente nell'xml");
				return sqlRowSet.getString("idparametro");
			}else {
				logger.debug("[CriterioDAO::getIdParametroCriteri] nodo _criteri non presente nell'xml");
				return null;
			}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CriterioDAO::getIdParametroCriteri] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _criteri");
	    	return null;
		}
	    catch (DataAccessException e) {
	    	logger.debug("[CriterioDAO::getIdParametroCriteri] eccezione DataAccessException nel recupero dell'eventuale nodo _criteri");
	    	throw new CommonalityException(e);
	    }finally{
	    	logger.debug("[CriterioDAO::getIdParametroCriteri] END");
	    }
	}
	
	/** Jira: 1969 -2r */
	public static String  getIdTipoInterventoCriteri(Integer idDomanda, Logger logger) throws CommonalityException {
		
	    logger.info("[CriterioDAO::getIdTipoInterventoCriteri] BEGIN");
	    
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    
	    try {
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_CRITERI_ID_TIPO_INTERVENTO, namedParameters);
			if (sqlRowSet!=null && sqlRowSet.next()){
				logger.debug("[CriterioDAO::getIdTipoInterventoCriteri] nodo _criteri gia' presente nell'xml");
				return sqlRowSet.getString("idtipointervento");
			}else {
				logger.debug("[CriterioDAO::getIdTipoInterventoCriteri] nodo _criteri non presente nell'xml");
				return null;
			}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CriterioDAO::getIdTipoInterventoCriteri] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _criteri");
	    	return null;
		}
	    catch (DataAccessException e) {
	    	logger.debug("[CriterioDAO::getIdTipoInterventoCriteri] eccezione DataAccessException nel recupero dell'eventuale nodo _criteri");
	    	throw new CommonalityException(e);
	    }finally{
	    	logger.debug("[CriterioDAO::getIdTipoInterventoCriteri] END");
	    }
	}
	 
	public static List<VistaCriterioVO> getCriteriList(Integer idBando, String criteriToView, Logger logger) throws CommonalityException {
		List<VistaCriterioVO> rlist = null;
		
		logger.debug("[CriterioDAO::getCriteriList] idBando = "+idBando);
		logger.debug("[CriterioDAO::getCriteriList] criteriToView = "+criteriToView);
		
		String sessionName = "criteriList_"+idBando;
		if(criteriToView!=null ){
			if( criteriToView.contains(",")){
				sessionName = sessionName + "_" +criteriToView.trim().replace(",", "_");
			}else{
				sessionName = sessionName + "_" +criteriToView.trim();
			}
		}
		logger.debug("[CriterioDAO::getCriteriList] sessionName="+sessionName);
		
		rlist = (List) SessionCache.getInstance().get(sessionName);
		if (rlist != null){
			logger.debug("[CriterioDAO::getCriteriList] leggo lista criteri da sessione");
		
		}else{
			
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	
			String andClause = "";
			
			Map<String, Integer> params = new HashMap<>();
			params.put("idBando", idBando);
			SqlParameterSource namedParameters = new MapSqlParameterSource(params);
			
			if(StringUtils.isNotBlank(criteriToView)){
				if(criteriToView.contains(",")){
					andClause = " AND id_criterio IN ( "+ criteriToView + ")";
				}else{
					andClause = " AND id_criterio = "+ criteriToView + " ";
				}
			}
			String query = QUERY_CRITERI.replace("##ANDCLAUSE##", andClause);
			
			logger.debug("[CriterioDAO::getCriteriList] query:" + query);
			
			try {
				rlist = jdbcTemplate.query(query, namedParameters,
						new BeanPropertyRowMapper<>(VistaCriterioVO.class));
				
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		
			SessionCache.getInstance().set(sessionName, rlist);
		}

		return rlist;
	}

	@SuppressWarnings("deprecation")
	public static Integer getNumeroCriteri(Integer idBando, Logger logger) {
		
		logger.debug("[CriterioDAO::getNumeroCriteri] idBando = "+idBando);
		
		Integer num = (Integer) SessionCache.getInstance().get(idBando+"_schedaCorso_numeroCriteri");
		if (num != null){
			logger.debug("[CriterioDAO::getNumeroCriteri] in sessione, num:" + num);
			return num;
		}
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		Map<String, Integer> params = new HashMap<>();
		params.put("idBando", idBando);
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		
		logger.debug("[CriterioDAO::getNumeroCriteri] query:" + QUERY_COUNTCRITERI);
		
		num = jdbcTemplate.queryForObject(QUERY_COUNTCRITERI, params, Integer.class);
		
		logger.debug("[CriterioDAO::getNumeroCriteri] num:" + num);
		
		SessionCache.getInstance().set(idBando+"_schedaCorso_numeroCriteri", num);
		return num;
	}
	
	public static List<TreeMap<Integer, Integer>> getOrdineCriteri(Integer idBando, Logger logger) 
				throws CommonalityException {
		
		logger.debug("[CriterioDAO::getOrdineCriteri] idBando = "+idBando);
				
		List<TreeMap<Integer, Integer>> lista = null;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		Map<String, Integer> params = new HashMap<>();
		params.put("idBando", idBando);
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		
		logger.debug("[CriterioDAO::getOrdineCriteri] query:" + QUERY_ORDINECRITERI);
		
		try {
			lista = jdbcTemplate.query(QUERY_ORDINECRITERI, namedParameters,
					new OrdineCriteriRowMapper());
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
//		SessionCache.getInstance().set(idBando+"_schedaCorso_numeroCriteri", num);
		return lista;
	}

	public static List<TreeMap<Integer, Integer>> getCriteriTipologieIntervento( Integer idBando, String criteriToView, Logger logger) throws CommonalityException {
		logger.debug("[CriterioDAO::getCriteriTipologieIntervento] idBando = "+idBando);
		
		List<TreeMap<Integer, Integer>> lista = null;
		
//		Integer num = (Integer) SessionCache.getInstance().get(idBando+"_schedaCorso_numeroCriteri");
//		if (num != null){
//			logger.debug("[CriterioDAO::getNumeroCriteri] in sessione, num:" + num);
//			return num;
//		}
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		Map<String, Integer> params = new HashMap<>();
		params.put("idBando", idBando);
//		params.put("criteriToView", criteriToView);
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		
		String query = QUERY_CRITERITIPOLOGIEVENTO;
		
		if(StringUtils.isNotBlank(criteriToView)){
			String rpc = " AND ";
			if(criteriToView.contains(",")){
				rpc = rpc + " id_criterio IN (" + criteriToView + ") ";
			}else{
				rpc = rpc + " id_criterio =" + criteriToView + " ";
			}
			query = QUERY_CRITERITIPOLOGIEVENTO.replace("##ANDCLAUSE##", rpc);
		}
		logger.debug("[CriterioDAO::getCriteriTipologieIntervento] query:" + query);
		
		try {
			lista = jdbcTemplate.query(query, namedParameters, new CriteriTipoligieInterventoRowMapper());
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
//		SessionCache.getInstance().set(idBando+"_schedaCorso_numeroCriteri", num);
		return lista;
	}

	/**
	 * Recupero la descrizione tipologia intervento
	 * del criterio selezionato
	 * 
	 * @param idBando
	 * @param criteriToView
	 * @param logger
	 * @return
	 */
	public static String getDescrizineTipologieIntervento(Integer idBando, String idCriterio, Logger logger) {
		
		String rval = "";
		
		Integer idCriterioSelezionato = Integer.parseInt(idCriterio);

		logger.info("[CriterioDAO::getDescrizineTipologieIntervento] idBando:" + idBando);
		logger.info("[CriterioDAO::getDescrizineTipologieIntervento] idCriterioSelezionato:" + idCriterioSelezionato);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);	
		namedParameters.addValue("idCriterioSelezionato", idCriterioSelezionato, Types.NUMERIC);

		try {
			rval = jdbcTemplate.queryForObject(QUERY_DESCRIZIONE_TIPOLOGIA_INTERVENTO_BY_CRITERIO, namedParameters, String.class);
		}
		catch (DataAccessException e) {
			logger.debug("[CaratteristicheProgettoDAO::getDescrTipolInterventoById] DataAccessException:" + e.getMessage());
		}

		return rval;
	}
	
}
