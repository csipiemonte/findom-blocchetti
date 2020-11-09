/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.cultPianospese.DettaglioVoceSpesaInterventoCulturaVO;
import it.csi.findom.blocchetti.common.vo.pianospese.DettaglioVoceSpesaInterventoVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class PianoSpeseDAO {

  private static String QUERY_MODEL_TOTALE = "SELECT " +
			" (xpath('/tree-map/_pianoSpese/map/totale/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS datacimp " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";
  
  /** : Innometro Cr2:  e  ... -2r */
  private static String QUERY_MODEL_TOTALE_CONTRIBUTO_RICHIESTO = "SELECT " +
			" (xpath('/tree-map/_formaFinanziamento/map/totContributoRichiesto/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS datacimp " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";
  

  private static String QUERY_SPESE_CONNESSE_ATTIVITA = "SELECT " +
			" (xpath('/tree-map/_pianoSpese/map/speseConnesseAttivita/text()', " +
			" aggr_t_model.serialized_model))[1]::text AS datacimp " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";

  private static String QUERY_SPESE_GENERALI_E_FUNZ = "SELECT " +
			" (xpath('/tree-map/_pianoSpese/map/speseGeneraliEFunz/text()', " +
			" aggr_t_model.serialized_model))[1]::text AS datacimp " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";
  
  private static String QUERY_QUOTA_PARTE = "SELECT " +
			" (xpath('/tree-map/_pianoSpese/map/quotaParte/text()', " +
			" aggr_t_model.serialized_model))[1]::text AS datacimp " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda";
  
  
  public static String getTotaleSpese(Integer idDomanda) throws CommonalityException {

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
    try {
    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_MODEL_TOTALE, namedParameters);
    	if (row!=null && row.next())
    		return row.getString("datacimp");
    	else 
    		return "";
    }
    catch (EmptyResultDataAccessException e) {
    	return "";
	}
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }
  
  /** : Innometro Cr2:  e  ... -2r */
  public static String getTotaleContributoRichiesto(Integer idDomanda) throws CommonalityException {

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_MODEL_TOTALE_CONTRIBUTO_RICHIESTO, namedParameters);
	    	if (row!=null && row.next())
	    		return row.getString("datacimp");
	    	else 
	    		return "";
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return "";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

	  }
  
  public static String getTotaleSpeseConnesseAttivita(Integer idDomanda) throws CommonalityException {

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_SPESE_CONNESSE_ATTIVITA, namedParameters);
	    	if (row!=null && row.next())
	    		return row.getString("datacimp");
	    	else 
	    		return "";
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return "";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

  }
  
  
  public static String getTotaleSpeseGeneraliEFunz(Integer idDomanda) throws CommonalityException {

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_SPESE_GENERALI_E_FUNZ, namedParameters);
	    	if (row!=null && row.next())
	    		return row.getString("datacimp");
	    	else 
	    		return "";
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return "";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

  }

  public static String getQuotaParte(Integer idDomanda) throws CommonalityException {

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_QUOTA_PARTE, namedParameters);
	    	if (row!=null && row.next())
	    		return row.getString("datacimp");
	    	else 
	    		return "";
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return "";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }

}
  

  /**
   * 
   * @param idTipoInterventoPar
   * @param dataInvio
   * @param idBando
   * @param logger
   * @return
   * @throws CommonalityException
   */
  public static List<DettaglioVoceSpesaInterventoVO> getVociDiSpesaTipoInterventoList(String idTipoInterventoPar, String dataInvio, Integer idBando, String customOrderBy, Logger logger)  
		  throws CommonalityException 
  {
		String query = " ";
		query += "SELECT a.id_tipol_intervento          AS id_tipo_intervento, ";	
	    query += "       a.cod_tipol_intervento         AS cod_tipo_intervento, ";		    
	    query += "       a.descrizione_tipol_intervento AS descr_tipo_intervento, ";
	    query += "       a.id_voce_spesa                AS id_voce_spesa, ";
		query += "       a.cod_voce_spesa               AS cod_voce_spesa, ";
		query += "       a.descrizione_voce_spesa       AS descr_voce_spesa ";	
		query += "FROM   findom_v_voci_spesa a ";           
		query += "WHERE  a.id_tipol_intervento = '" + idTipoInterventoPar + "' and ";
		query += "       a.id_bando = " + idBando + " and ";
      
		if(StringUtils.isBlank(dataInvio)) {
		   query +="     (date_trunc('day', a.dt_inizio_intervento) <= CURRENT_DATE and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= CURRENT_DATE)) and ";
		   query +="     (date_trunc('day', a.dt_inizio_voce_spesa) <= CURRENT_DATE and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= CURRENT_DATE))  ";
		}else{
		   query +="     (date_trunc('day',a.dt_inizio_intervento) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
		   query +="     (date_trunc('day',a.dt_inizio_voce_spesa) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}
		if(StringUtils.isNotBlank(customOrderBy)){
			query += customOrderBy;
		}else{
			query += " ORDER BY a.cod_tipol_intervento, a.cod_voce_spesa ";
		}
		logger.info("debug: query risulta: " + query);
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    SqlParameterSource namedParameters = new MapSqlParameterSource();
		logger.info("[PianoSpeseDAO] getVociDiSpesaTipoInterventoList(idTipoInterventoPar:" + (idTipoInterventoPar) + " , dataInvio = " + dataInvio + ") query: " + query);
		List<DettaglioVoceSpesaInterventoVO> rlist = new ArrayList<DettaglioVoceSpesaInterventoVO>();
		logger.info("[PianoSpeseDAO] getVociDiSpesaTipoInterventoList rlist: " + rlist);

	    try {
	    	List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
	    	if ((rowList!=null)&&(rowList.size()>0)) {
	    		
	    	   Map firstRecord = (Map)rowList.get(0);
	   		   if(firstRecord!=null){
	   			DettaglioVoceSpesaInterventoVO tmpMap = creaMapPerVis(firstRecord, "1");			  
	   		      rlist.add(tmpMap);
	   		   }
	   		   
	   		   //le voci di spesa (considero nuovamente il primo record, che comunque corrisponde ad una voce di spesa)
	   		   for(int i=0; i<rowList.size(); i++){			    
	   		      Map curRecord = (Map)rowList.get(i);	
	   		      if(curRecord!=null){		     
	   		    	DettaglioVoceSpesaInterventoVO tmpMap = creaMapPerVis(curRecord,"3");
	   		         rlist.add(tmpMap);
	   		      }
	   		   }
	   		   
	    	} else { 
	    		return rlist;
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return rlist;
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
		return rlist;
	}
 
  public static List<DettaglioVoceSpesaInterventoVO> getVociDiSpesaDettaglioInterventoList(String idDettInterventoPar, String dataInvio, Integer idBando, Logger logger)  throws CommonalityException {
		String query = " ";
		query += "SELECT a.id_dett_tipol_intervento     AS id_dett_intervento, ";				
	    query += "       a.cod_dett_tipol_intervento    AS cod_dett_intervento, ";		    
	    query += "       a.descrizione_dett_tipol_intervento  AS descr_dett_intervento, ";
	    query += "       a.id_tipol_intervento          AS id_tipo_intervento, ";
		query += "       a.cod_tipol_intervento         AS cod_tipo_intervento, ";		    
	    query += "       a.descrizione_tipol_intervento AS descr_tipo_intervento, ";
	    query += "       a.id_voce_spesa                AS id_voce_spesa, ";
		query += "       a.cod_voce_spesa               AS cod_voce_spesa, ";
		query += "       a.descrizione_voce_spesa       AS descr_voce_spesa ";	
		query += "FROM   findom_v_voci_spesa a ";           
		query += "WHERE  a.id_dett_tipol_intervento = '" + idDettInterventoPar + "' and ";
		query += "       a.id_bando = " + idBando + " and ";
      
		if(StringUtils.isBlank(dataInvio)) {
		   query +="     (date_trunc('day', a.dt_inizio_intervento) <= CURRENT_DATE and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= CURRENT_DATE)) and ";
		   query +="     (date_trunc('day', a.dt_inizio_voce_spesa) <= CURRENT_DATE and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= CURRENT_DATE))  ";
		}else{
		   query +="     (date_trunc('day',a.dt_inizio_intervento) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
		   query +="     (date_trunc('day',a.dt_inizio_voce_spesa) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}	
	    query += " ORDER BY a.cod_tipol_intervento, a.cod_voce_spesa ";
		
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    SqlParameterSource namedParameters = new MapSqlParameterSource();
		logger.info("[PianoSpeseDAO] getVociDiSpesaDettaglioInterventoList(idDettInterventoPar:" +  (idDettInterventoPar) + " , dataInvio = " + dataInvio + ") query: " + query);
		List<DettaglioVoceSpesaInterventoVO> rlist = new ArrayList<DettaglioVoceSpesaInterventoVO>();
		logger.info("[PianoSpeseDAO] getVociDiSpesaDettaglioInterventoList rlist: " + rlist);

		  try {
		    	List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
		    	if ((rowList!=null)&&(rowList.size()>0)) {
		    		
		 		   //da tmpList ottengo una lista che ha un elemento in piu' (il primo, relativo all'intervento/dettaglio a cui si riferiscono le voci di spesa) 
		 		   //rispetto a tmpList. 
		 		   //Tutti gli elementi contengono dati sia dell'intervento sia delle voci di spesa, ma hanno un attributo tipoRecord che 
		 		   //in fase di visualizzazione viene usato per capire quali dati di quel record devono essere visualizzati, e in che modo

		    	   Map firstRecord = (Map)rowList.get(0);
		   		   if(firstRecord!=null){
		   			DettaglioVoceSpesaInterventoVO tmpMap = creaMapPerVis(firstRecord, "2");			  
		   		      rlist.add(tmpMap);
		   		   }
		   		   
		   		   //le voci di spesa (considero nuovamente il primo record, che comunque corrisponde ad una voce di spesa)
		   		   for(int i=0; i<rowList.size(); i++){			    
		   		      Map curRecord = (Map)rowList.get(i);	
		   		      if(curRecord!=null){		     
		   		    	DettaglioVoceSpesaInterventoVO tmpMap = creaMapPerVis(curRecord,"3");
		   		         rlist.add(tmpMap);
		   		      }
		   		   }
		   		   
		    	} else { 
		    		return rlist;
		    	}
		    }
		    catch (EmptyResultDataAccessException e) {
		    	return rlist;
			}
		    catch (DataAccessException e) {
		      throw new CommonalityException(e);
		    }
		    
			return rlist;
		
		
	}
  
  // findom_d_voci_spesa. descr_breve
  public static List<DettaglioVoceSpesaInterventoCulturaVO> getVociDiSpesaTipoInterventoCulturaList(String idTipoInterventoPar, String dataInvio, Integer idBando, Logger logger)  throws CommonalityException {
		String query = " ";
		query += "SELECT a.id_tipol_intervento          AS id_tipo_intervento, ";	
	    query += "       a.cod_tipol_intervento         AS cod_tipo_intervento, ";		    
	    query += "       a.descrizione_tipol_intervento AS descr_tipo_intervento, ";
	    query += "       a.id_voce_spesa                AS id_voce_spesa, ";
		query += "       a.cod_voce_spesa               AS cod_voce_spesa, ";
		query += "       a.descrizione_voce_spesa       AS descr_voce_spesa, ";	
		query += "       a.id_categ_voce_spesa          AS id_categ_voce_spesa,  ";
		query += "       a.descrizione_categ_voce_spesa AS descrizione_categ_voce_spesa,  ";
		query += "       a.flag_specificaz              AS flag_specificaz  ";	
		query += "FROM   findom_v_voci_spesa a ";           
		query += "WHERE  a.id_tipol_intervento = '" + idTipoInterventoPar + "' and ";
		query += "       a.id_bando = " + idBando + " and ";
    
		if(StringUtils.isBlank(dataInvio)) {
		   query +="     (date_trunc('day', a.dt_inizio_intervento) <= CURRENT_DATE and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= CURRENT_DATE)) and ";
		   query +="     (date_trunc('day', a.dt_inizio_voce_spesa) <= CURRENT_DATE and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= CURRENT_DATE))  ";
		}else{
		   query +="     (date_trunc('day',a.dt_inizio_intervento) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
		   query +="     (date_trunc('day',a.dt_inizio_voce_spesa) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}	
	    query += " ORDER BY a.cod_tipol_intervento, a.id_categ_voce_spesa, a.descrizione_voce_spesa ";
	    query += " COLLATE \"C\" ";
		
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    SqlParameterSource namedParameters = new MapSqlParameterSource();
		logger.info("[PianoSpeseDAO] getVociDiSpesaTipoInterventoList(idTipoInterventoPar:" + (idTipoInterventoPar) + " , dataInvio = " + dataInvio + ") query: " + query);
		List<DettaglioVoceSpesaInterventoCulturaVO> rlist = new ArrayList<DettaglioVoceSpesaInterventoCulturaVO>();
		logger.info("[PianoSpeseDAO] getVociDiSpesaTipoInterventoList rlist: " + rlist);

	    try {
	    	List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
	    	if ((rowList!=null)&&(rowList.size()>0)) {
	    		
	    	   Map firstRecord = (Map)rowList.get(0);
	   		   if(firstRecord!=null){
	   			DettaglioVoceSpesaInterventoCulturaVO tmpMap = creaMapPerVisCultura(firstRecord, "1");			  
	   		      rlist.add(tmpMap);
	   		   }
	   		   String idCatVoceSpesaPrec  = "";
			   
			   for(int i=0; i<rowList.size(); i++){			    
			      Map curRecord = (Map)rowList.get(i);	
			      if(curRecord!=null){			         
			        
			         //gestione 'rottura' per categoria voce di spesa		          		         
			         Integer curIdCatVoceSpesaInt = (Integer) curRecord.get("id_categ_voce_spesa") ;
			         String curIdCatVoceSpesa = curIdCatVoceSpesaInt == null ? "" : curIdCatVoceSpesaInt.toString();			          
			         if(
			            ( idCatVoceSpesaPrec.equals("") && (!curIdCatVoceSpesa.equals("")) ) ||
			            ( !idCatVoceSpesaPrec.equals("") && !idCatVoceSpesaPrec.equals(curIdCatVoceSpesa) )
			           ){			       		            
			               idCatVoceSpesaPrec = curIdCatVoceSpesa;
			               DettaglioVoceSpesaInterventoCulturaVO tmpMapCat = creaMapPerVisCultura(curRecord,"4");
			               rlist.add(tmpMapCat);
			         } 
			         		     
			         DettaglioVoceSpesaInterventoCulturaVO tmpMap = creaMapPerVisCultura(curRecord,"3");
			         rlist.add(tmpMap);			        
			      }
			   }			   	 	
	   		   
	    	} else { 
	    		return rlist;
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return rlist;
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
		return rlist;
	}

public static List<DettaglioVoceSpesaInterventoCulturaVO> getVociDiSpesaDettaglioInterventoCulturaList(String idDettInterventoPar, String dataInvio, String idTipoInterventoPar, Integer idBando, Logger logger)  throws CommonalityException {
	
	String query = " ";
		query += "SELECT a.id_dett_tipol_intervento     AS id_dett_intervento, ";				
	    query += "       a.cod_dett_tipol_intervento    AS cod_dett_intervento, ";		    
	    query += "       a.descrizione_dett_tipol_intervento  AS descr_dett_intervento, ";
	    query += "       a.id_tipol_intervento          AS id_tipo_intervento, ";
		query += "       a.cod_tipol_intervento         AS cod_tipo_intervento, ";		    
	    query += "       a.descrizione_tipol_intervento AS descr_tipo_intervento, ";
	    query += "       a.id_voce_spesa                AS id_voce_spesa, ";
		query += "       a.cod_voce_spesa               AS cod_voce_spesa, ";
		query += "       a.descrizione_voce_spesa       AS descr_voce_spesa, ";	
		query += "       a.id_categ_voce_spesa          AS id_categ_voce_spesa,  ";
		query += "       a.descrizione_categ_voce_spesa AS descrizione_categ_voce_spesa  ";	
		query += "FROM   findom_v_voci_spesa a ";           
		query += "WHERE  a.id_dett_tipol_intervento = '" + idDettInterventoPar + "' and ";
		
		 query += "       a.id_tipol_intervento = '" + idTipoInterventoPar + "' and ";            
         query += "       a.id_bando = " + idBando + " and ";
    
		if(StringUtils.isBlank(dataInvio)) {
		   query +="     (date_trunc('day', a.dt_inizio_intervento) <= CURRENT_DATE and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= CURRENT_DATE)) and ";
		   query +="     (date_trunc('day', a.dt_inizio_voce_spesa) <= CURRENT_DATE and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= CURRENT_DATE))  ";
		}else{
		   query +="     (date_trunc('day',a.dt_inizio_intervento) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
		   query +="     (date_trunc('day',a.dt_inizio_voce_spesa) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}	
	    query += " ORDER BY a.cod_tipol_intervento, a.cod_dett_tipol_intervento, a.id_categ_voce_spesa, a.cod_voce_spesa ";
		
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    SqlParameterSource namedParameters = new MapSqlParameterSource();
		logger.info("[PianoSpeseDAO] getVociDiSpesaDettaglioInterventoList(idDettInterventoPar:" +  (idDettInterventoPar) + " , dataInvio = " + dataInvio + ") query: " + query);
		List<DettaglioVoceSpesaInterventoCulturaVO> rlist = new ArrayList<DettaglioVoceSpesaInterventoCulturaVO>();
		logger.info("[PianoSpeseDAO] getVociDiSpesaDettaglioInterventoList rlist: " + rlist);

		  try {
		    	List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
		    	if ((rowList!=null)&&(rowList.size()>0)) {
		    		
		 		   //da tmpList ottengo una lista che ha un elemento in piu' (il primo, relativo all'intervento/dettaglio a cui si riferiscono le voci di spesa) 
		 		   //rispetto a tmpList. 
		 		   //Tutti gli elementi contengono dati sia dell'intervento sia delle voci di spesa, ma hanno un attributo tipoRecord che 
		 		   //in fase di visualizzazione viene usato per capire quali dati di quel record devono essere visualizzati, e in che modo

		    	   Map firstRecord = (Map)rowList.get(0);
		    	   logger.info("[PianoSpeseDAO] firstRecord: " + firstRecord);
		   		   if(firstRecord!=null){
		   			DettaglioVoceSpesaInterventoCulturaVO tmpMap = creaMapPerVisCultura(firstRecord, "2");			  
		   		      rlist.add(tmpMap);
		   		   }
		   		   
		   		String idCatVoceSpesaPrec  = ""; //MB2016_11_24 
				   
				   //le voci di spesa (considero nuovamente il primo record, che comunque corrisponde ad una voce di spesa)
				   for(int i=0; i<rowList.size(); i++){			    
				      Map curRecord = (Map)rowList.get(i);	
				      if(curRecord!=null){	
				        	          		         
				         Integer curIdCatVoceSpesaInt = (Integer) curRecord.get("id_categ_voce_spesa") ;
				         logger.info("[PianoSpeseDAO] curIdCatVoceSpesaInt: " + curIdCatVoceSpesaInt);
				         
				         String curIdCatVoceSpesa = curIdCatVoceSpesaInt == null ? "" : curIdCatVoceSpesaInt.toString();	
				         logger.info("[PianoSpeseDAO] curIdCatVoceSpesa: " + curIdCatVoceSpesa);
				         
				         if(
				            ( idCatVoceSpesaPrec.equals("") 
				            		&& (!curIdCatVoceSpesa.equals("")) ) 
				            		|| ( !idCatVoceSpesaPrec.equals("") 
				            		&& !idCatVoceSpesaPrec.equals(curIdCatVoceSpesa) )
				           ){			       		            
				             idCatVoceSpesaPrec = curIdCatVoceSpesa;
				             logger.info("[PianoSpeseDAO] idCatVoceSpesaPrec: " + idCatVoceSpesaPrec);
				             
				             DettaglioVoceSpesaInterventoCulturaVO tmpMapCat = creaMapPerVisCultura(curRecord,"4");
				             rlist.add(tmpMapCat);
				         }			   
				             
				         DettaglioVoceSpesaInterventoCulturaVO tmpMap = creaMapPerVisCultura(curRecord,"3");
				         rlist.add(tmpMap);
				      }
				   }
		   		   
		    	} else { 
		    		return rlist;
		    	}
		    }
		    catch (EmptyResultDataAccessException e) {
		    	return rlist;
			}
		    catch (DataAccessException e) {
		      throw new CommonalityException(e);
		    }
		    
			return rlist;
		
		
	}

//il secondo parametro di creaMapPerVis, tipoRecord, ha il significato seguente:
	//tipoRecord=="1"  significa record di cui vanno visualizzati i dati relativi alla tipologia intervento
	//tipoRecord=="2"  significa record di cui vanno visualizzati i dati relativi alla tipologia intervento e al dettaglio intervento
	//tipoRecord=="3"  significa record  di cui vanno visualizzati i dati relativi alla voce di spesa		
	
  private static DettaglioVoceSpesaInterventoVO  creaMapPerVis(Map src, String tipoRecord){
	   
		DettaglioVoceSpesaInterventoVO dest = new DettaglioVoceSpesaInterventoVO();
	   
	    //tipo intervento
		Integer idTipoInterventoInt = (Integer) src.get("id_tipo_intervento") ;
		
		String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();	
		dest.setIdTipoIntervento(idTipoIntervento);
		      
		String codTipoIntervento = (String) src.get("cod_tipo_intervento");		      
		dest.setCodTipoIntervento(codTipoIntervento);
		      
		String descrTipoIntervento = (String) src.get("descr_tipo_intervento");		      
		dest.setDescrTipoIntervento(descrTipoIntervento);
		
		//dettaglio intervento
		Integer idDettInterventoInt = (Integer) src.get("id_dett_intervento") ;
		
		String idDettIntervento = idDettInterventoInt == null ? "" : idDettInterventoInt.toString();	
		dest.setIdDettIntervento(idDettIntervento);
		   
		String codDettIntervento = (String) src.get("cod_dett_intervento");		      
		dest.setCodDettIntervento(codDettIntervento);
		      
	    String descrDettIntervento = (String) src.get("descr_dett_intervento");		      
	    dest.setDescrDettIntervento(descrDettIntervento);
				
		//voce di spesa         
		Integer idVoceSpesaInt = (Integer) src.get("id_voce_spesa");	              	
		
		String idVoceSpesa = idVoceSpesaInt == null ? "" : idVoceSpesaInt.toString();	      
		dest.setIdVoceSpesa(idVoceSpesa);
		      
		String codVoceSpesa = (String) src.get("cod_voce_spesa");		      
		dest.setCodVoceSpesa(codVoceSpesa);	
		            
		String descrVoceSpesa = (String) src.get("descr_voce_spesa");		      
		dest.setDescrVoceSpesa(descrVoceSpesa);
		
		 
		if(tipoRecord.equals("1")){			  
		   String titoloTipoIntervento = codTipoIntervento + " - " + descrTipoIntervento;
		   dest.setTitoloTipoIntervento(titoloTipoIntervento ); 			    
		}else if(tipoRecord.equals("2")){
		   
		   //la tipologia intervento va comunque visualizzata
		   String titoloTipoIntervento = codTipoIntervento + " - " + descrTipoIntervento;
		   dest.setTitoloTipoIntervento(titoloTipoIntervento );
		   
		   String titoloDettIntervento = " | " + codDettIntervento + " - " + descrDettIntervento;
		   dest.setTitoloDettIntervento(titoloDettIntervento );
		   
		} else if (tipoRecord.equals("3")){
		   String titoloVoceDiSpesa = codVoceSpesa + " - " + descrVoceSpesa;
		   dest.setTitoloVoceDiSpesa(titoloVoceDiSpesa ); 
		}			
		dest.setTipoRecord(tipoRecord);
		
		dest.setTotaleVoceSpesa("");			
		
		return dest;		
	}
  
  private static DettaglioVoceSpesaInterventoCulturaVO  creaMapPerVisCultura(Map src, String tipoRecord){
	    
	  DettaglioVoceSpesaInterventoCulturaVO dest = new DettaglioVoceSpesaInterventoCulturaVO();
	   
	    //tipo intervento
		Integer idTipoInterventoInt = (Integer) src.get("id_tipo_intervento") ;
		
		String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();	
		dest.setIdTipoIntervento(idTipoIntervento);
		      
		String codTipoIntervento = (String) src.get("cod_tipo_intervento");		      
		dest.setCodTipoIntervento(codTipoIntervento);
		      
		String descrTipoIntervento = (String) src.get("descr_tipo_intervento");	
		dest.setDescrTipoIntervento(descrTipoIntervento);
		
		//dettaglio intervento
		Integer idDettInterventoInt = (Integer) src.get("id_dett_intervento") ;
		
		String idDettIntervento = idDettInterventoInt == null ? "" : idDettInterventoInt.toString();	
		
		dest.setIdDettIntervento(idDettIntervento);
		   
		String codDettIntervento = (String) src.get("cod_dett_intervento");		
		dest.setCodDettIntervento(codDettIntervento);
		      
	    String descrDettIntervento = (String) src.get("descr_dett_intervento");
		dest.setDescrDettIntervento(descrDettIntervento);
				
		//voce di spesa         
		Integer idVoceSpesaInt = (Integer) src.get("id_voce_spesa");	
		
		String idVoceSpesa = idVoceSpesaInt == null ? "" : idVoceSpesaInt.toString();	
		dest.setIdVoceSpesa(idVoceSpesa);
		      
		String codVoceSpesa = (String) src.get("cod_voce_spesa");	
		dest.setCodVoceSpesa(codVoceSpesa);	
		            
		String descrVoceSpesa = (String) src.get("descr_voce_spesa");	
		dest.setDescrVoceSpesa(descrVoceSpesa);
		
		Integer idCatVoceSpesaInt = (Integer) src.get("id_categ_voce_spesa") ;
		
		String idCatVoceSpesa = idCatVoceSpesaInt == null ? "" : idCatVoceSpesaInt.toString();				      
		dest.setIdCatVoceSpesa(idCatVoceSpesa);
		
		String descrizioneCatVoceSpesa = (String) src.get("descrizione_categ_voce_spesa");		      
		dest.setDescrizioneCatVoceSpesa(descrizioneCatVoceSpesa);
		
		String flagSpecificaz = (String) src.get("flag_specificaz");	
		dest.setFlagSpecificaz(flagSpecificaz);
					 
		if(tipoRecord.equals("1")){			  
		   String titoloTipoIntervento = codTipoIntervento + " - " + descrTipoIntervento;
		   dest.setTitoloTipoIntervento(titoloTipoIntervento ); 			    
		}else if(tipoRecord.equals("2")){
		   
		   //la tipologia intervento va comunque visualizzata
		   String titoloTipoIntervento = codTipoIntervento + " - " + descrTipoIntervento;
		   dest.setTitoloTipoIntervento(titoloTipoIntervento );
		   
		   String titoloDettIntervento = " | " + codDettIntervento + " - " + descrDettIntervento;
		   dest.setTitoloDettIntervento(titoloDettIntervento );
		   
		} else if (tipoRecord.equals("3")){
		   String titoloVoceDiSpesa = codVoceSpesa + " - " + descrVoceSpesa;
		   dest.setTitoloVoceDiSpesa(titoloVoceDiSpesa ); 
		}			
		dest.setTipoRecord(tipoRecord);
		
		dest.setTotaleVoceSpesa("");			
		
		return dest;		
	}
  
}
