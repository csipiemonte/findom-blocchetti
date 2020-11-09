/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.tipologiaAiutoNG;

import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.DettaglioAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGItemListVO;
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

public class TipologiaAiutoNGDAO {
  
  public static List<TipologiaAiutoNGItemListVO> getTipologiaAiutoList(String idBando, String dataInvio, Logger logger) throws CommonalityException {		
		String query = " ";
		
	    //MB2016_04_11 ini nuova versione con utilizzo di vista dopo ristrutturazione del db (il bando ora è collegato direttamente con il dettaglio 
	    //e non con la tipologia)
	    //la vista al suo interno fa una distinct, ma potrebbe succedere che una tipologia esce piu' di una volta
	    //perche' nella tavola di relazione bandi-dettagli i dettagli hanno coppie data inizio data fine diverse;
	    //con il vincolo sulla validità delle date e il distinct dovrebbero uscire da questa select solo i bandi voluti 
	    query += " SELECT distinct a.id_tipol_aiuto AS idTipoAiuto, ";	
	    query += " a.descrizione_tipol_aiuto AS descrTipoAiuto, a.cod_tipol_aiuto as codTipoAiuto, ";	
	    query += " 'false' AS checked, '0' AS numDettagli ";		      //impostazione con valori di default di 'checked' e 'numDettagli'
        query += " FROM   findom_v_tipol_aiuti a  ";                      
        query += " WHERE  a.id_bando= :idBando and ";
        if(StringUtils.isBlank(dataInvio)) {
		   query +=" (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE))  ";
		}else{
		   query +=" (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}					   
	    query += " ORDER BY a.id_tipol_aiuto ";
	    //MB2016_04_11 fine
		
	    List<TipologiaAiutoNGItemListVO> rlist = null;
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);

	    
	    logger.info("[TipologiaAiutoNGDAO::getTipologiaAiutoList] idBando: " + idBando + ", dataInvio: " + (dataInvio==null? "null" : dataInvio) + ") query: " + query);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		try {
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaAiutoNGItemListVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		return rlist;
	}
	
	
	//ritorna la lista di map, che rappresenta il dettaglio tipologia di aiuto associato all'idTipoAiuto passato
	//MB2016_04_11 List getDettaglioTipolAiutoList(String idTipoAiuto){
	public static List<DettaglioAiutoNGItemListVO> getDettaglioTipolAiutoList(String idTipoAiuto, String dataInvio, String idBando, Logger logger) throws CommonalityException {  //MB2016_04_11
	
		String query = "";
		
		//MB2016_04_11 ini nuova versione
		query += "SELECT id_dett_tipol_aiuti  AS idDettAiuto, ";
		query += "       descrizione          AS descrDettAiuto, ";
		query += "       id_tipol_aiuto       AS idTipoAiutoDett,  ";		
		query += "       link                 AS linkDettAiuto, "; 
		query += "       codice               AS codiceDettAiuto, ";
		query += "       'false'              AS checked ";
		query += "FROM   findom_v_dett_tipol_aiuti "; 	
		query += "WHERE  id_tipol_aiuto = :idTipoAiuto and ";
		query += "       id_bando = :idBando and ";
		if(StringUtils.isBlank(dataInvio)) {
		    query +=" (date_trunc('day', dt_inizio) <= CURRENT_DATE and (dt_fine is null or date_trunc('day',dt_fine) >= CURRENT_DATE))  ";
		}else{
		    query +=" (date_trunc('day',dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (dt_fine is null or date_trunc('day',dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}
		query += "ORDER BY id_tipol_aiuto,id_dett_tipol_aiuti ";
		//MB2016_04_11 fine
	
		logger.info("[TipologiaAiutoNGDAO::getDettaglioTipolAiutoList] idTipoAiuto:" + idTipoAiuto + " ,dataInvio =  " + dataInvio + " ,idBando =  " + idBando + " ) query: " + query); //MB2016_04_11 aggiunto dataInvio e idBando
				
		List<DettaglioAiutoNGItemListVO> rlist = null;
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idTipoAiuto", idTipoAiuto, Types.NUMERIC);
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	
	    
	    logger.info("[TipologiaAiutoNGDAO::getDettaglioTipolAiutoList] idBando: " + idBando + ", dataInvio: " + (dataInvio==null? "null" : dataInvio) + ") query: " + query);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	
		try {
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(DettaglioAiutoNGItemListVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}	
		
		return rlist;
	}
}
