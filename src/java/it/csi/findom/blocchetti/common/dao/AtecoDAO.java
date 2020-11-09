/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.ateco.AtecoVo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class AtecoDAO {

  private static  String QUERY_ATECO_LIST = "SELECT id_ateco AS id, cod_ateco AS codice, desc_ateco AS descrizione, cod_ateco_norm AS codnormalizzato," + 
  		" a.cod_sezione AS codicesezione, v.desc_sezione AS descrizionesezione" + 
  		" FROM  ext_d_ateco a, findom_v_sezioni_ateco v" + 
  		" WHERE a.cod_sezione=v.cod_sezione ";
  
  private static  String QUERY_ATECO_AMMISSIBILI_LIST = "SELECT id_ateco AS id, cod_ateco AS codice, desc_ateco AS descrizione, cod_ateco_norm AS codnormalizzato," + 
	  		" cod_sezione AS codicesezione, desc_sezione AS descrizionesezione" + 
	  		" FROM findom_v_ateco_ammissibili" + 
	  		" WHERE id_bando = :idBando ";
  
  private static  String QUERY_ATECO_AMMISSIBILI_BY_COD = "SELECT id_ateco AS id, cod_ateco AS codice, desc_ateco AS descrizione, cod_ateco_norm AS codnormalizzato," + 
	  		" cod_sezione AS codicesezione, desc_sezione AS descrizionesezione" + 
	  		" FROM findom_v_ateco_ammissibili" + 
	  		" WHERE id_bando = :idBando and  cod_ateco = :codAteco2007Sede";
  
  private static  String QUERY_ATECO = "select id_ateco AS id, cod_ateco AS codice, desc_Ateco AS descrizione, cod_livello AS livello, "
		 + " cod_ateco_norm AS codnormalizzato, cod_sezione AS codicesezione "
		 + " FROM ext_d_ateco "
		 + " WHERE cod_ateco_norm = :codiceNorm ";

  public static List<AtecoVo> getListAtecoVoByCodDesc(String codx, String descrx, Logger logger) throws CommonalityException {

    List<AtecoVo> atecoVoList = null;

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

//    Map<String, Object> params = new HashMap<>();

    String query = QUERY_ATECO_LIST;
        
    if (StringUtils.isNotEmpty(codx) ) {
//		query += "AND  cod_ateco_norm LIKE '%:codx%' ";
//		params.put("codx", StringUtils.trim(codx));
		query += "AND cod_ateco_norm LIKE '%"+StringUtils.trim(codx)+"%' ";
	}
	
	if (StringUtils.isNotEmpty(descrx)) {
//		query += "AND lower(desc_ateco) LIKE '%:descrx%' ";
//		params.put("descrx", StringUtils.lowerCase(StringUtils.trim(descrx)));
		query += "AND lower(desc_ateco) LIKE '%"+StringUtils.lowerCase(StringUtils.trim(descrx))+"%' ";
	}
//	SqlParameterSource namedParameters = new MapSqlParameterSource(params);
	SqlParameterSource namedParameters = new MapSqlParameterSource();

	query += "ORDER BY codice";
	
	logger.info("[AtecoDAO::getListAtecoVoByCodDesc] query="+query);

	
    try {
    	atecoVoList = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<AtecoVo>(AtecoVo.class));
        return atecoVoList;
    }
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }
  
  public static List<AtecoVo> getListAtecoVoByIdBandoCodDesc(Integer idBando, String codx, String descrx, Logger logger) throws CommonalityException {

	    List<AtecoVo> atecoVoList = null;

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    Map<String, Object> params = new HashMap<>();

	    String query = QUERY_ATECO_AMMISSIBILI_LIST;
	        
	    if (StringUtils.isNotEmpty(codx) ) {
//			query += "AND  cod_ateco_norm LIKE '%:codx%' ";
//			params.put("codx", StringUtils.trim(codx));
			query += "AND cod_ateco_norm LIKE '%"+StringUtils.trim(codx)+"%' ";
		}
		
		if (StringUtils.isNotEmpty(descrx)) {
//			query += "AND lower(desc_ateco) LIKE '%:descrx%' ";
//			params.put("descrx", StringUtils.lowerCase(StringUtils.trim(descrx)));
			query += "AND lower(desc_ateco) LIKE '%"+StringUtils.lowerCase(StringUtils.trim(descrx))+"%' ";
		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		params.put("idBando", idBando);
		
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);

		query += "ORDER BY codice";
		
		logger.info("[AtecoDAO::getListAtecoVoByCodDesc] query="+query);

		
	    try {
	    	atecoVoList = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<AtecoVo>(AtecoVo.class));
	        return atecoVoList;
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

	  }


  public static AtecoVo getAtecoAmmissibilitVoByCodice(Integer idBando,String codice, Logger logger) throws CommonalityException {

	    AtecoVo ateco = new AtecoVo();

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    Map<String, Object> params = new HashMap<>();
	    params.put("codAteco2007Sede", codice);
	    params.put("idBando", idBando);
	    
	    
	    SqlParameterSource namedParameters = new MapSqlParameterSource(params);

		
	    try {
	    	List<AtecoVo> atecoVoList = jdbcTemplate.query(QUERY_ATECO_AMMISSIBILI_BY_COD, namedParameters, new BeanPropertyRowMapper<>(AtecoVo.class));
	    	if(atecoVoList != null && atecoVoList.size()>0){
				return (AtecoVo)atecoVoList.get(0);
	    	}
	    	return ateco;
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

	  }


  
  public static AtecoVo getAtecoVoByCodiceNorm(String codiceNorm) throws CommonalityException {

	    AtecoVo ateco = new AtecoVo();

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    Map<String, Object> params = new HashMap<>();

	    SqlParameterSource namedParameters = new MapSqlParameterSource("codiceNorm", StringUtils.trim(codiceNorm));

		
	    try {
	    	List<AtecoVo> atecoVoList = jdbcTemplate.query(QUERY_ATECO, namedParameters, new BeanPropertyRowMapper<>(AtecoVo.class));
	    	if(atecoVoList != null && atecoVoList.size()>0){
				return (AtecoVo)atecoVoList.get(0);
	    	}
	    	return ateco;
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

	  }


}
