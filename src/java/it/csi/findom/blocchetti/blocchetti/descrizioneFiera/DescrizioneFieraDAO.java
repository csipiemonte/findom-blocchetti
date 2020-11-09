/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.descrizioneFiera;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class DescrizioneFieraDAO {
	
	private static final String QUERY_NODO_DESCRIZIONE_FIERA = "SELECT " +
			" unnest(xpath('/tree-map/_descrizioneFiera'::text, " +
			" aggr_t_model.serialized_model))::text AS descrizioneFiera " +
   			" FROM  aggr_t_model " +
				" WHERE aggr_t_model.model_id = :idDomanda" ;
	
	private static final String QUERY_NODO_CARATTERISTICHE_PROGETTO = "SELECT " +
			" unnest(xpath('/tree-map/_caratteristicheProgetto'::text, " +
			" aggr_t_model.serialized_model))::text AS caratteristicheProgetto " +
   			" FROM  aggr_t_model " +
				" WHERE aggr_t_model.model_id = :idDomanda" ;
	
	private static final String QUERY_DETTAGLIO_INTERVENTO_EUROPA = "SELECT sigla_continente AS continente "
			+ " from ext_d_stati_esteri "
			+ " where cod_stato = upper(:statoEstero);";
	
	public static String getNodoDescrizioneFiera(Integer idDomanda) throws CommonalityException {
	
		String descrizioneFiera = "";
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		SqlParameterSource namedParameters = new MapSqlParameterSource("idDomanda", idDomanda);
		
		try {
				List<String> descrizioneFieraList = jdbcTemplate.query(QUERY_NODO_DESCRIZIONE_FIERA, namedParameters, new BeanPropertyRowMapper<>(String.class));
				
				if (descrizioneFieraList != null && !descrizioneFieraList.isEmpty()) {
					descrizioneFiera = descrizioneFieraList.get(0);
				}
			
				return descrizioneFiera;
			}
			catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
	}


	public static String getNodoCarrateristicheProgetto(Integer idDomanda) throws CommonalityException {
	
		String carrateristicheProgetto = "";
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		SqlParameterSource namedParameters = new MapSqlParameterSource("idDomanda", idDomanda);
		
		try {
			List<String> carrateristicheProgettoList = jdbcTemplate.query(QUERY_NODO_CARATTERISTICHE_PROGETTO, 
												namedParameters, new BeanPropertyRowMapper<>(String.class));
			
			if (carrateristicheProgettoList != null && !carrateristicheProgettoList.isEmpty()) {
				carrateristicheProgetto = carrateristicheProgettoList.get(0);
			}
		
			return carrateristicheProgetto;
			}
		catch (DataAccessException e) {
				throw new CommonalityException(e);
		}
	}
	
	public static String getDescrizioneFieraNazione(String statoEstero) throws CommonalityException {
		
		
		
		String query = QUERY_DETTAGLIO_INTERVENTO_EUROPA;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		Map<String, Object> params = new HashMap<>();
		params.put("statoEstero", statoEstero);
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		
		String siglaContinente = "";
		
		try 
		{
			
			 List<String> descrizioneFieraNazioneList = jdbcTemplate.query(QUERY_DETTAGLIO_INTERVENTO_EUROPA, namedParameters, new BeanPropertyRowMapper<>(String.class));
			if (descrizioneFieraNazioneList != null && !descrizioneFieraNazioneList.isEmpty()) {
				siglaContinente = descrizioneFieraNazioneList.get(0);
			}
		}
		catch (DataAccessException e) {
				throw new CommonalityException(e);
		}
		return siglaContinente;
	}
}

