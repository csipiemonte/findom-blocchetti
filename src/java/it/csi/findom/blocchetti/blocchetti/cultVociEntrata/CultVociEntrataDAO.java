/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultVociEntrata;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CultVociEntrataDAO {

	private static final String QUERY_VOCI_ENTRATA = "SELECT "
	    + " COALESCE(TO_CHAR(a.id_voce_entrata),'') AS idVoceEntrata, "
	    + "a.flag_duplicabile AS flagDuplicabile, "
	    + "b.descrizione AS descrizione, "
	    + "b.descr_breve AS descrBreve, " 
	    + "b.flag_edit AS flagEdit, "
	    + "b.indicazioni AS indicazioni "
	    + "FROM findom_r_bandi_voci_entrata a,findom_d_voci_entrata b  "
	    + "WHERE a.id_bando = :idBando and " 
	    + "      a.id_voce_entrata = b.id_voce_entrata and "
	    + "  	 date_trunc('day', a.dt_inizio) <= CURRENT_DATE and "
	    + "		(a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE) "
	    + "ORDER BY descrizione";
	
	private static final String QUERY_TOTALE_ENTRATE = "SELECT "
			 + "(xpath('/tree-map/_vociEntrata/map/totale/text()', aggr_t_model.serialized_model))[1]::text AS totale "
			 + "FROM aggr_t_model "
			 + "WHERE aggr_t_model.model_id = :idDomanda" ;
	
	//estrae le voci di entrata usate per popolare combo voci di entrata

	public static List<VoceEntrataItemVO> getVociEntrataList(String idBando, Logger logger) throws CommonalityException {
	
		List<VoceEntrataItemVO> rlist = new ArrayList<VoceEntrataItemVO>();
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		
		logger.debug("[CultVociEntrataDAO::getVociEntrataList] query:" + QUERY_VOCI_ENTRATA);
		   
		try {
			
			rlist = (List<VoceEntrataItemVO>) jdbcTemplate.query(QUERY_VOCI_ENTRATA, namedParameters,
				new BeanPropertyRowMapper<VoceEntrataItemVO>(VoceEntrataItemVO.class));
		
		} catch (DataAccessException e) {
				throw new CommonalityException(e);
		}
           
		logger.debug("[CultVociEntrataDAO::getVociEntrataList] rlist:" + rlist);
		return rlist; 
	}	
	
	public static String getTotaleEntrate(Integer idDomanda, Logger logger) throws CommonalityException {
		 
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    String totale = "";
	    try {
			logger.debug("[CultVociEntrata::getTotaleEntrate] query:" + QUERY_TOTALE_ENTRATE);

	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_TOTALE_ENTRATE, namedParameters);
	    	if (row!=null && row.next())
	    		totale = row.getString("totale");
	    }
	    catch (EmptyResultDataAccessException e) {
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
		logger.debug("[CultVociEntrata::getTotaleEntrate] totale:" + totale);

    	return totale;    
	}

}
