/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dimensioniNG;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
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

public class DimensioniNGDAO {

  private static final String QUERY_DATI_NODO_DIMENSIONI = "SELECT " +
			" unnest(xpath('/tree-map/_dimensioni'::text, " +
			" aggr_t_model.serialized_model))::text AS nododimensioni " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda ";
  

	private static final String QUERY_DATI_ULA = "SELECT id_ula AS id, descr_ula AS descrizione "
			+ "FROM findom_v_ula_tipol_beneficiari_bandi "
			+ "WHERE id_bando = :idBando and id_tipol_beneficiario = (SELECT id_tipol_beneficiario "
			+ "FROM shell_t_domande "
			+ "WHERE id_domanda = :idDomanda) "
			+ "ORDER BY id_ula ";
	
	private static final String QUERY_CLASSIFICAZIONE = "SELECT id_dimensione AS codice, descrizione "
	        + "FROM findom_d_dimensioni_imprese "
	        + "ORDER BY codice";
	
	private static final String QUERY_CLASSIFICAZIONE_SENZA_GRANDE = "SELECT id_dimensione AS codice, descrizione "
	        + "FROM findom_d_dimensioni_imprese "
			+ "WHERE id_dimensione IN (1,2,3)"
	        + "ORDER BY codice";
	
	private static final String QUERY_CLASSIFICAZIONE_SENZA_MEDIA = "SELECT id_dimensione AS codice, descrizione "
	        + "FROM findom_d_dimensioni_imprese "
			+ "WHERE id_dimensione IN (1,2)"
	        + "ORDER BY codice";
	   
	
  public static String getNodoDimensioni(Integer idDomanda) throws CommonalityException {


    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
    String nodoDimensioni = null;
    try {
    	List<String> listaNodoDimensioni = jdbcTemplate.query(QUERY_DATI_NODO_DIMENSIONI, namedParameters, new BeanPropertyRowMapper<>(String.class));
    	 if (listaNodoDimensioni != null && !listaNodoDimensioni.isEmpty()) {
    		 return listaNodoDimensioni.get(0);
    	 }
    	 
    	return nodoDimensioni;
    }
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }

  public static List<DatiUlaVO>getUlaByIdBandoIdTipolBeneficiario(Integer idBando, Integer idDomanda)  throws CommonalityException {

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    List<DatiUlaVO> datiUlaList = null;
	    try {
	    	datiUlaList = jdbcTemplate.query(QUERY_DATI_ULA, namedParameters, new BeanPropertyRowMapper<>(DatiUlaVO.class));	    		    	
	    	return datiUlaList;
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

	  }
  
  public static List<ClassificazioneDimensioniImpresaVO> getClassificazioneDimensioniImpresaList(Logger logger) throws CommonalityException {
	  List<ClassificazioneDimensioniImpresaVO> rlist = null;
		rlist = (List) SessionCache.getInstance().get("classificazioneDimensioniImpresaConsList");
		if (rlist != null)
			return rlist;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		try {
			rlist = jdbcTemplate.query(QUERY_CLASSIFICAZIONE, namedParameters,
					new BeanPropertyRowMapper<>(ClassificazioneDimensioniImpresaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.debug("[DimensioniNGDAO::classificazioneDimensioniImpresaList] query:" + QUERY_CLASSIFICAZIONE);
		SessionCache.getInstance().set("classificazioneDimensioniImpresaConsList", rlist);
		return rlist;
		
	}	
  
  	public static List<ClassificazioneDimensioniImpresaVO> getClassificazioneDimensioniImpresaListSenzaGrande(Logger logger) throws CommonalityException {
	  List<ClassificazioneDimensioniImpresaVO> rlist = null;
		rlist = (List) SessionCache.getInstance().get("getClassificazioneDimensioniImpresaListSenzaGrande");
		if (rlist != null)
			return rlist;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		try {
			rlist = jdbcTemplate.query(QUERY_CLASSIFICAZIONE_SENZA_GRANDE, namedParameters,
					new BeanPropertyRowMapper<>(ClassificazioneDimensioniImpresaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.debug("[DimensioniNGDAO::getClassificazioneDimensioniImpresaListSenzaGrande] query:" + QUERY_CLASSIFICAZIONE_SENZA_GRANDE);
		SessionCache.getInstance().set("getClassificazioneDimensioniImpresaListSenzaGrande", rlist);
		return rlist;
		
	}
  	
  	
  	/**
  	 * Jira: 1588
  	 * Dimensione selezionabile solo: micro e piccola (NO media)
  	 */
  	public static List<ClassificazioneDimensioniImpresaVO> getClassificazioneDimensioniImpresaListSenzaMedia(Logger logger) throws CommonalityException {
  	  List<ClassificazioneDimensioniImpresaVO> rlist = null;
  		rlist = (List) SessionCache.getInstance().get("getClassificazioneDimensioniImpresaListSenzaMedia");
  		if (rlist != null)
  			return rlist;
  		
  		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
  				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

  		SqlParameterSource namedParameters = null;

  		try {
  			rlist = jdbcTemplate.query(QUERY_CLASSIFICAZIONE_SENZA_MEDIA, namedParameters,
  					new BeanPropertyRowMapper<>(ClassificazioneDimensioniImpresaVO.class));
  		} catch (DataAccessException e) {
  			throw new CommonalityException(e);
  		}
  		logger.info("[DimensioniNGDAO::getClassificazioneDimensioniImpresaListSenzaMedia] query:" + QUERY_CLASSIFICAZIONE_SENZA_MEDIA);
  		SessionCache.getInstance().set("getClassificazioneDimensioniImpresaListSenzaMedia", rlist);
  		return rlist;
  		
  	}
}
