/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.dipartimento.DipartimentoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class OperatorePresentatoreDAO {

 
  private static final String QUERY_STATO_OPERATORE_PRESENTATORE = "SELECT "
			+ "(xpath('/tree-map/_operatorePresentatore/map/idStato/text()',  aggr_t_model.serialized_model))[1]::text AS idStato "
	  		+ " FROM  aggr_t_model WHERE  aggr_t_model.model_id = :idDomanda" ;
  
  private static final String QUERY_UO_OPERATORE_PRESENTATORE = "SELECT "
			+ "(xpath('/tree-map/_operatorePresentatore/map/codiceDipartimento/text()',  aggr_t_model.serialized_model))[1]::text AS codiceDipartimento "
	  		+ " FROM  aggr_t_model WHERE  aggr_t_model.model_id = :idDomanda" ;
  
  private static final String QUERY_PRESENZA_CODICE_REGIONALE = "SELECT exists( SELECT 1 FROM findom_t_bonus_turismo a WHERE a.cod_regionale = :codiceRegionale);";
  	
  /** verifica duplicato codice regionale */
  private static final String QUERY_PRESENZA_CODICE_REGIONALE_DUPLICATO = "SELECT id_domanda::text AS idDomanda"
		    + " FROM ("
			+ " SELECT id_domanda, unnest(xpath('//_operatorePresentatore/map/codiceRegionale/text()'::text, m.serialized_model))::text as cod_regionale"
		    + " FROM shell_t_domande d"
			+ " JOIN findom_t_sportelli_bandi s ON d.id_sportello_bando = s.id_sportello_bando"
		    + " JOIN aggr_t_model m ON d.id_domanda = m.model_id"
			+ " WHERE s.id_bando = :idBando AND dt_invio_domanda IS NOT NULL) f"
	  		+ " WHERE UPPER(f.cod_regionale) = UPPER(:codRegionale)" ;

  private static final String QUERY_DIPARTIMENTI_MULTIPLI = "\nSELECT d.id_dipartimento AS id, d.descrizione AS descrizione, rsa.importo_contributo AS importo, d.codice as codice\n"
		   +" FROM findom_r_bandi_soggetti_abilitati rsa\n"
		   +" JOIN findom_t_soggetti_abilitati sa ON rsa.id_sogg_abil = sa.id_sogg_abil\n"
		   +" AND sa.codice_fiscale = :codiceFiscale\n"
		   +" JOIN findom_d_dipartimenti d ON rsa.id_dipartimento = d.id_dipartimento\n"
		   +" WHERE rsa.id_bando = :idBando\n"
		   +" AND now() >= rsa.dt_inizio\n"
		   +" AND (rsa.dt_fine is null OR now() <= rsa.dt_fine)\n"
		   +" AND now() > d.dt_inizio\n"
		   +" AND (d.dt_fine is null OR now() <= d.dt_fine)\n";
  
  
  public static String getCodiceRegionaleDuplicato(Integer idBando, String codRegionale, Logger logger) throws CommonalityException {
	  final String logprefix = "[OperatorePresentatoreDAO:: getCodiceRegionaleDuplicato] ";
	  logger.info(logprefix + " BEGIN");
	  String idDomanda = "";

	  NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	  MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	  namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	  namedParameters.addValue("codRegionale", codRegionale, Types.VARCHAR);

	  try {

		  SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_PRESENZA_CODICE_REGIONALE_DUPLICATO, namedParameters);
		  logger.info(QUERY_PRESENZA_CODICE_REGIONALE_DUPLICATO);
		  if (row!=null && row.next()){
			  idDomanda = row.getString("idDomanda");
		  }    	

	  }catch (EmptyResultDataAccessException e) {
		  logger.error(logprefix + " si e' verificata una EmptyResultDataAccessException durante l'esecuzione del metodo " + e);
		  throw new CommonalityException(e);
	  }
	  catch (DataAccessException e) {
		  logger.error(logprefix + " si e' verificata una DataAccessException durante l'esecuzione del metodo " + e);
		  throw new CommonalityException(e);
	  }
	  return idDomanda;
  }
  
 	/** Jira: 1978 -2r */
	public static boolean getIsCodRegionalePresente(String codiceRegionale, Logger logger) throws CommonalityException {
			
		final String logprefix = "[OperatorePresentatoreDAO:: getIsCodRegionalePresente] ";
		
		logger.info(logprefix + " BEGIN");
		
		Boolean flag = false;
	
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			params.addValue("codiceRegionale", codiceRegionale, java.sql.Types.VARCHAR);	
	
			flag = jdbcTemplate.queryForObject(QUERY_PRESENZA_CODICE_REGIONALE, params, Boolean.class);
	
		} catch (EmptyResultDataAccessException e) {
	    	logger.debug("[OperatorePresentatoreDAO::getIsCodRegionalePresente] eccezione EmptyResultDataAccessException");
		
		} catch (DataAccessException e) {
	    	logger.warn("[OperatorePresentatoreDAO::getIsCodRegionalePresente] eccezione DataAccessException");
	        throw new CommonalityException(e);
	    
		} finally{
	    	logger.debug("[OperatorePresentatoreDAO::getIsCodRegionalePresente] END");
	    }
			
		return flag;
	}
	
	
  	
  public static String getIdStatoInXmlOperatatorePresentatore(Integer idDomanda, Logger logger) throws CommonalityException {
	  final String logprefix = "[OperatorePresentatoreDAO:: getIdStatoInXmlOperatatorePresentatore] ";
	  logger.info(logprefix + " BEGIN");
	  String idStatoOperatatorePresentatore = "";

	  NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	  MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	  namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

	  try {

		  SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_STATO_OPERATORE_PRESENTATORE, namedParameters);
		  if (row!=null && row.next()){
			  idStatoOperatatorePresentatore = row.getString("idStato");
		  }    	

	  }catch (EmptyResultDataAccessException e) {
		  logger.error(logprefix + " si e' verificata una EmptyResultDataAccessException durante l'esecuzione del metodo " + e);
		  throw new CommonalityException(e);
	  }
	  catch (DataAccessException e) {
		  logger.error(logprefix + " si e' verificata una DataAccessException durante l'esecuzione del metodo " + e);
		  throw new CommonalityException(e);
	  }
	  return idStatoOperatatorePresentatore;
  }
  
  public static String getCodUOInXmlOperatatorePresentatore(Integer idDomanda, Logger logger) throws CommonalityException {
	  final String logprefix = "[OperatorePresentatoreDAO:: getCodUOInXmlOperatatorePresentatore] ";
	  logger.info(logprefix + " BEGIN");
	  String codUOOperatatorePresentatore = "";

	  NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	  MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	  namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

	  try {

		  SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_UO_OPERATORE_PRESENTATORE, namedParameters);
		  if (row!=null && row.next()){
			  codUOOperatatorePresentatore = row.getString("codiceDipartimento");
		  }    	

	  }catch (EmptyResultDataAccessException e) {
		  logger.error(logprefix + " si e' verificata una EmptyResultDataAccessException durante l'esecuzione del metodo " + e);
		  throw new CommonalityException(e);
	  }
	  catch (DataAccessException e) {
		  logger.error(logprefix + " si e' verificata una DataAccessException durante l'esecuzione del metodo " + e);
		  throw new CommonalityException(e);
	  }
	  return codUOOperatatorePresentatore;

  }
  
  
  /** jira 2142 */
  public static List<DipartimentoVO> getDatiDipartimentoMultipli(Integer idBando, String codiceFiscale, Logger logger) throws CommonalityException {
	  
	    List<DipartimentoVO> rlist = null;

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	Map<String, Object> params = new HashMap<>();
	params.put("idBando", idBando);
	params.put("codiceFiscale", codiceFiscale);
	
	SqlParameterSource namedParameters = new MapSqlParameterSource(params);
	
	logger.info("[OperatorePresentatoreDAO::getDatiDipartimentoMultipli] query:" + QUERY_DIPARTIMENTI_MULTIPLI);

	    try {
	      rlist = jdbcTemplate.query(QUERY_DIPARTIMENTI_MULTIPLI, namedParameters, new BeanPropertyRowMapper<>(DipartimentoVO.class));
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
	    return rlist;
  }

}
