/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.formaGiuridica.FormaGiuridicaVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class FormaGiuridicaDAO {
  

  private static final String QUERY_FORMA_GIURIDICA = "SELECT g.id_forma_giuridica AS id, cod_forma_giuridica AS codice, descr_forma_giuridica AS descrizione, "
		   +     "CASE WHEN length(descr_forma_giuridica)>80 THEN substring(descr_forma_giuridica from 0 for 80) || '...' "
		   +     "ELSE descr_forma_giuridica END  AS descrtroncata "		
		   + "FROM ext_d_forme_giuridiche g  order by descrizione ";
  
  private static final String QUERY_FORMA_GIURIDICA_SOLO_LA = "SELECT g.id_forma_giuridica AS id, cod_forma_giuridica AS codice, descr_forma_giuridica AS descrizione, "
		   +     "CASE WHEN length(descr_forma_giuridica)>80 THEN substring(descr_forma_giuridica from 0 for 80) || '...' "
		   +     "ELSE descr_forma_giuridica END  AS descrtroncata "		
		   + "FROM ext_d_forme_giuridiche g  " 
		   + "WHERE g.id_forma_giuridica=4 ";
  
  private static final String QUERY_FORMA_GIURIDICA_SENZA_LA = "SELECT g.id_forma_giuridica AS id, cod_forma_giuridica AS codice, descr_forma_giuridica AS descrizione, "
		   +     "CASE WHEN length(descr_forma_giuridica)>80 THEN substring(descr_forma_giuridica from 0 for 80) || '...' "
		   +     "ELSE descr_forma_giuridica END  AS descrtroncata "		
		   + "FROM ext_d_forme_giuridiche g  "
		   + "WHERE g.id_forma_giuridica NOT IN(4) "
		   + "order by descrizione ";
  
  public static List<FormaGiuridicaVO> getDatiFormaGiuridica(Logger logger) throws CommonalityException {
    List<FormaGiuridicaVO> rlist = null;

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

    SqlParameterSource namedParameters = null;

    logger.debug("[FormaGiuridicaDAO::getDatiFormaGiuridica] query:" + QUERY_FORMA_GIURIDICA);

    try {
      rlist = jdbcTemplate.query(QUERY_FORMA_GIURIDICA, namedParameters, new BeanPropertyRowMapper<>(FormaGiuridicaVO.class));
    }
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }
    
    return rlist;
  }
  
  public static List<FormaGiuridicaVO> getDatiFormaGiuridicaSoloLA(Logger logger) throws CommonalityException {
	    List<FormaGiuridicaVO> rlist = null;

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    SqlParameterSource namedParameters = null;

	    logger.debug("[FormaGiuridicaDAO::getDatiFormaGiuridica] query:" + QUERY_FORMA_GIURIDICA_SOLO_LA);

	    try {
	      rlist = jdbcTemplate.query(QUERY_FORMA_GIURIDICA_SOLO_LA, namedParameters, new BeanPropertyRowMapper<>(FormaGiuridicaVO.class));
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
	    return rlist;
  }
  
  public static List<FormaGiuridicaVO> getDatiFormaGiuridicaSenzaLA(Logger logger) throws CommonalityException {
	    List<FormaGiuridicaVO> rlist = null;

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    SqlParameterSource namedParameters = null;

	    logger.debug("[FormaGiuridicaDAO::getDatiFormaGiuridica] query:" + QUERY_FORMA_GIURIDICA_SENZA_LA);

	    try {
	      rlist = jdbcTemplate.query(QUERY_FORMA_GIURIDICA_SENZA_LA, namedParameters, new BeanPropertyRowMapper<>(FormaGiuridicaVO.class));
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
	    return rlist;
  }

}
