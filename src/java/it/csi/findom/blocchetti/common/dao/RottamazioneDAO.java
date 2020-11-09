/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.rottamazione.AlimentazioneVeicoloVO;
import it.csi.findom.blocchetti.common.vo.rottamazione.ItemTipoRottamazioneVO;
import it.csi.findom.blocchetti.common.vo.veicoli.CategoriaVeicoloVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class RottamazioneDAO {

	public static List<AlimentazioneVeicoloVO> getAlimentazioneVeicoliList(Long idSportelloBando, Logger logger) throws CommonalityException {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<AlimentazioneVeicoloVO> avList = new ArrayList<AlimentazioneVeicoloVO>();
		
		namedParameters.addValue("idSportelloBando", idSportelloBando);
/*
 * : Modifica richiesta in data: 02/ott/2020 @DT 
 * 
		String query = "SELECT id_aliment_veicolo, descr_breve, descrizione" +
				 		" FROM findom_d_alimentazione_veicoli order by descr_breve";
*/
		String query = " SELECT a.id_aliment_veicolo, descr_breve, descrizione" +
		 		" FROM findom_d_alimentazione_veicoli a" +
		 		" JOIN findom_r_tipi_rottamaz_alimentaz_veicoli b ON a.id_aliment_veicolo = b.id_aliment_veicolo" + 
		 		" WHERE id_sportello_bando = :idSportelloBando " +
		 		" ORDER BY descr_breve";
		
		logger.debug("[RottamazioneDAO::getAlimentazioneVeicolo] query="+query);
		try {

			List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
			if ((rowList!=null)&&(rowList.size()>0)) {
				for (Map<String, Object> record : rowList) {
				    if(record!=null){
				    	AlimentazioneVeicoloVO tmp = creaAliment(record);			  
				   		avList.add(tmp);
				    }
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.debug("[RottamazioneDAO::getAlimentazioneVeicoliList] EmptyResultDataAccessException");
		}
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		return avList;
	}

	public static List<CategoriaVeicoloVO> getCategoriaVeicoloList(Long idSportelloBando, Logger logger) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
		List<CategoriaVeicoloVO> catList = new ArrayList<CategoriaVeicoloVO>();
		
		namedParameters.addValue("idSportelloBando", idSportelloBando);
		
/*		: modifica richiesta in data: 02/10/2020 da @DT
		String query = "SELECT id_categ_veicolo, descr_breve,  descrizione " +
						" FROM findom_d_categ_veicoli order by descr_breve;";
*/
		
		String query = "SELECT a.id_categ_veicolo, descr_breve, descrizione " +
				" FROM findom_d_categ_veicoli a " +
				" JOIN findom_r_tipi_rottamaz_categ_veicoli b ON a.id_categ_veicolo = b.id_categ_veicolo " +
				" WHERE id_sportello_bando = :idSportelloBando" +
				" ORDER BY descr_breve";
		
		logger.debug("[RottamazioneDAO::getCategoriaVeicoloList] query="+query);
		try {

			List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
			if ((rowList!=null)&&(rowList.size()>0)) {
				logger.debug("[RottamazioneDAO::getCategoriaVeicoloList] rowList.size="+rowList.size());
				for (Map<String, Object> record : rowList) {
				    if(record!=null){
				    	CategoriaVeicoloVO tmp = creaCatVeicolo(record);			  
				    	catList.add(tmp);
				    }
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.debug("[RottamazioneDAO::getCategoriaVeicoloList] EmptyResultDataAccessException");
		}
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		return catList;
	}
	
	
	
	
	private static CategoriaVeicoloVO creaCatVeicolo(Map record) {

		CategoriaVeicoloVO item = new CategoriaVeicoloVO();
		
		String descrBr = (String) record.get("descr_breve");		      
		item.setIdCategoriaVeicolo(descrBr);
		
		String descr = (String) record.get("descrizione");		      
		item. setDescrCategoriaVeicolo(descr);
		
		return item;
	}



	private static AlimentazioneVeicoloVO creaAliment(Map record) {
		
		AlimentazioneVeicoloVO item = new AlimentazioneVeicoloVO();
		
		String descrBr = (String) record.get("descr_breve");		      
		item.setIdAlimentazioneVeicolo(descrBr);
		
		String descr = (String) record.get("descrizione");		      
		item.setDescrAlimentazioneVeicolo(descr);
		
		return item;
	}

	public static List<ItemTipoRottamazioneVO> getTipoRottamazioneList( Logger logger, Long idSportelloBando) throws CommonalityException {
		
		logger.debug("[RottamazioneDAO::getTipoRottamazioneList] BEGIN");
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		List<ItemTipoRottamazioneVO> lista = new ArrayList<ItemTipoRottamazioneVO>();
		
		namedParameters.addValue("idSportelloBando", idSportelloBando);
		
/*		
 * 	: modifica richiesta in data: 02/ottobre/2020 da @DT 
		String query = "SELECT id_tipo_rottamazione, codice_tipo_rottamazione, descrizione_tipo_rottamazione " +
					   " FROM findom_d_tipi_rottamazione order by descrizione_tipo_rottamazione";
 		logger.debug("[RottamazioneDAO::getTipoRottamazioneList] query="+query);
*/ 
		
		String query = "SELECT a.id_tipo_rottamazione, codice_tipo_rottamazione, a.descrizione_tipo_rottamazione" +
				" FROM findom_d_tipi_rottamazione a" +
				" WHERE EXISTS" +
				" (SELECT NULL FROM findom_r_tipi_rottamaz_alimentaz_veicoli c WHERE c.id_sportello_bando = :idSportelloBando AND c.id_tipo_rottamazione = a.id_tipo_rottamazione)" +
				" OR EXISTS" +
				" (SELECT NULL FROM findom_r_tipi_rottamaz_categ_veicoli d WHERE d.id_sportello_bando = :idSportelloBando AND d.id_tipo_rottamazione = a.id_tipo_rottamazione)" +
				" ORDER BY descrizione_tipo_rottamazione";
		
		try {
		
			List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
			if ((rowList!=null)&&(rowList.size()>0)) {
				logger.debug("[RottamazioneDAO::getTipoRottamazioneList] rowList.size="+rowList.size());
				for (Map<String, Object> record : rowList) {
				    if(record!=null){
				    	ItemTipoRottamazioneVO tmp = creaItemTipoRottamazione(record);			  
				    	lista.add(tmp);
				    }
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.debug("[RottamazioneDAO::getTipoRottamazioneList] EmptyResultDataAccessException");
		}
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		logger.debug("[RottamazioneDAO::getTipoRottamazioneList] END");
		return lista;
	}

	private static ItemTipoRottamazioneVO creaItemTipoRottamazione(	Map<String, Object> record) {
		ItemTipoRottamazioneVO item = new ItemTipoRottamazioneVO();
		
		String tipoRott = (String) record.get("codice_tipo_rottamazione");
		item.setIdTipo(tipoRott);

		String descr = (String) record.get("descrizione_tipo_rottamazione");
		item.setDescrTipo(descr);
		
		return item;
	}

	
	
	public static List<CategoriaVeicoloVO> getCategoriaVeicoloBySportelloList( Logger logger, Long idSportello) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
		List<CategoriaVeicoloVO> catList = new ArrayList<CategoriaVeicoloVO>();
		 
		namedParameters.addValue("idSportello", idSportello);
		
		String query = "SELECT DISTINCT c.id_categ_veicolo, c.descr_breve, c.descrizione " +
						"FROM findom_d_categ_veicoli c , findom_r_tipi_rottamaz_categ_veicoli r " +
						"WHERE r.id_sportello_bando = :idSportello " +
						"and r.dt_inizio <= now() " +
						"and (r.dt_fine > now() or r.dt_fine is null) " +
						"and c.id_categ_veicolo = r.id_categ_veicolo " +
						" order by c.descr_breve" ;
		 
		logger.debug("[RottamazioneDAO::getCategoriaVeicoloRottamazioneList] query="+query);
		try {

			List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
			if ((rowList!=null)&&(rowList.size()>0)) {
				logger.debug("[RottamazioneDAO::getCategoriaVeicoloBySportelloList] rowList.size="+rowList.size());
				for (Map<String, Object> record : rowList) {
				    if(record!=null){
				    	CategoriaVeicoloVO tmp = creaCatVeicolo(record);			  
				    	catList.add(tmp);
				    }
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.debug("[RottamazioneDAO::getCategoriaVeicoloBySportelloList] EmptyResultDataAccessException");
		}
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		return catList;
	}

	public static List<AlimentazioneVeicoloVO> getAlimentazioneVeicoliPrivatiList(Logger logger, Long idSportello)
			throws CommonalityException {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	
		namedParameters.addValue("idSportello", idSportello);
		
		List<AlimentazioneVeicoloVO> avList = new ArrayList<AlimentazioneVeicoloVO>();
		 
		String query = "  SELECT DISTINCT a.id_aliment_veicolo,  descr_breve,  descrizione " +
						" FROM findom_d_alimentazione_veicoli a, findom_r_tipi_rottamaz_alimentaz_veicoli b " +
						" WHERE a.id_aliment_veicolo = b.id_aliment_veicolo " +
						" AND id_sportello_bando = :idSportello " +
						" AND b.dt_inizio <= now() " +
						" AND (b.dt_fine > now() or b.dt_fine is null) " +
						" order by descr_breve " ;

		logger.debug("[RottamazioneDAO::getAlimentazioneVeicoliPrivatiList] query="+query);
		try {

			List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
			if ((rowList!=null)&&(rowList.size()>0)) {
				for (Map<String, Object> record : rowList) {
				    if(record!=null){
				    	AlimentazioneVeicoloVO tmp = creaAliment(record);			  
				   		avList.add(tmp);
				    }
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.debug("[RottamazioneDAO::getAlimentazioneVeicoliPrivatiList] EmptyResultDataAccessException");
		}
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		return avList;
	}
	
}
