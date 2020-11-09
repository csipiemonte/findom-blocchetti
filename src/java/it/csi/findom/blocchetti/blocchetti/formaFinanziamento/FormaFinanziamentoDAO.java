/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class FormaFinanziamentoDAO 
{

	  private static String QUERY_FORME_FINANZIAMENTO = "SELECT " +
			  "  a.id_forma_finanziamento AS idFormaFinanziamento, " +
			  "  b.cod_forma_finanziamento  AS codFormaFinanziamento, " +
			  "  b.descrizione              AS descrFormaFinanziamento, " +
			  "  b.tipo_forma_finanziamento AS tipoFormaFinanziamento, " +
			  "  b.cod_forma_finanziamento ||' - '|| b.descrizione  AS formaFinanziamento, " +
			  "  a.flag_obbligatorio  AS flagObbligatorio, " +
			  "  a.perc_prevista  AS percPrevista, 'false' AS checked " +
			  " FROM   findom_r_bandi_forme_finanziamenti a, " +
			  " findom_d_forme_finanziamenti b " +
			  " WHERE  a.id_bando = :idBando  and  " +
			  "        a.id_forma_finanziamento = b.id_forma_finanziamento and ";
	  
	  private static String QUERY_IMPORTO_MAX = "SELECT " +
			  "  a.importo_massimo_erogabile AS importoMassimoErogabile " +
			  " FROM   findom_t_bandi a " +
			  " WHERE  a.id_bando = :idBando ";
	  
	  private static String QUERY_IMPORTO_MIN = "SELECT " +
			  "  a.importo_minimo_erogabile AS importoMinimoErogabile " +
			  " FROM   findom_t_bandi a " +
			  " WHERE  a.id_bando = :idBando ";
	  
	  
	  private static String QUERY_FORME_FINANZIAMENTO_CON_IMPORTI_MIN_MAX = "SELECT " +
			  "  a.id_forma_finanziamento AS idFormaFinanziamento, " +
			  "  b.cod_forma_finanziamento  AS codFormaFinanziamento, " +
			  "  b.descrizione              AS descrFormaFinanziamento, " +
			  "  b.tipo_forma_finanziamento AS tipoFormaFinanziamento, " +
			  "  b.cod_forma_finanziamento ||' - '|| b.descrizione  AS formaFinanziamento, " +
			  "  a.flag_obbligatorio  AS flagObbligatorio, " +
			  "  a.perc_prevista  AS percPrevista, 'false' AS checked, " +
			  "  a.importo_minimo_erogabile as importoMinErogabile, " +
			  "  a.importo_massimo_erogabile as importoMaxErogabile " +
			  " FROM   findom_r_bandi_forme_finanziamenti a, " +
			  " findom_d_forme_finanziamenti b " +
			  " WHERE  a.id_bando = :idBando  and  " +
			  "        a.id_forma_finanziamento = b.id_forma_finanziamento and ";
	  
	  
	  /**
	   * - rr
	   * query per estrarre la % variabile
	   * per cascun bando, con la quale
	   * si calcolerà il max importo richiesto sul totale delle spese:
	   * Esempio:
	   * % (a) = 80.00
	   *   (b) = totSpese = 1000.00
	   *   (c) = maxImportoRichiedibile risulterà dato da: (b * a) / 100;
	   *   maxImportoRichiedibile = 800.00
	   *   
	   * @param idBando
	   * @param dataInvio
	   * @param logger
	   * @return
	   * @throws CommonalityException
	   */
	  private static final String QUERY_PARAMETRI_CALCOLO_PERCENTUALE = "SELECT perc_max_contributo_erogabile AS percTotSpese"
			+ " FROM findom_t_bandi"
			+ " WHERE  id_bando = :idBando ";
	  
	  
	  /** Jira: 1671 */
	  private static String QUERY_FORME_FINANZIAMENTO_CALCOLO_PERCENTUALI = "SELECT " +
			  "  a.id_forma_finanziamento AS idFormaFinanziamento, " +
			  "  b.cod_forma_finanziamento  AS codFormaFinanziamento, " +
			  "  b.descrizione              AS descrFormaFinanziamento, " +
			  "  b.tipo_forma_finanziamento AS tipoFormaFinanziamento, " +
			  "  b.cod_forma_finanziamento ||' - '|| b.descrizione  AS formaFinanziamento, " +
			  "  a.flag_obbligatorio  AS flagObbligatorio, " +
			  "  a.perc_prevista  AS percPrevista, 'false' AS checked " +
			  " FROM   findom_r_bandi_forme_finanziamenti a, " +
			  " findom_d_forme_finanziamenti b " +
			  " WHERE  a.id_bando = :idBando  and  " +
			  " a.id_forma_finanziamento = b.id_forma_finanziamento and ";
	  
	  private static String QUERY_IMPORTO_MAX_EROGABILE = "SELECT " +
			  " a.importo_massimo_erogabile AS importoMassimoErogabile " +
			  " FROM findom_r_bandi_forme_finanziamenti a " +
			  " WHERE a.id_bando = :idBando " +
			  " AND a.id_forma_finanziamento = :idFormaFinanziamento ";
	  
	  private static String QUERY_IMPORTO_MIN_EROGABILE = "SELECT " +
			  " a.importo_minimo_erogabile AS importoMinimoErogabile " +
			  " FROM findom_r_bandi_forme_finanziamenti a " +
			  " WHERE a.id_bando = :idBando " +
			  " AND a.id_forma_finanziamento = :idFormaFinanziamento ";
	  
	  
	  private static final String QUERY_NODO_FORMA_FINANZIAMENTO_CONTRIBUTO_TOT_RICHIESTO = "SELECT "
	  			+ " unnest(xpath('/tree-map/_formaFinanziamento/map/formaFinanziamentoList/list/map[codFormaFinanziamento=01]/contributoTotaleRichiesto/text()'::text, " 
	  			+ " aggr_t_model.serialized_model))::text AS contributoTotaleRichiesto " 
	  			+ " FROM  aggr_t_model " 
	  			+ " WHERE aggr_t_model.model_id= :idDomanda" ;
	  
	  /**
	   * - rr
	   * Recupero la % perc_max_contributo_erogabile
	   * per idBando
	   * @param idBando
	   * @return
	   * @throws CommonalityException
	   */
	  public static String getPercentuale(String idBando, Logger logger) throws CommonalityException {
	
		    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idBando", idBando, Types.NUMERIC);
			try {
				SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_PARAMETRI_CALCOLO_PERCENTUALE, namedParameters);
				logger.info("");
				if (row!=null && row.next())
					return row.getString("percTotSpese");
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
			
	  
	  public static	List<TipoFormaFinanziamentoVO> getFormeFinanziamentoList(String idBando, String dataInvio, Logger logger) throws CommonalityException {
			
			List<TipoFormaFinanziamentoVO> rlist = null;
			// rlist = (List<TipoFormaFinanziamentoVO>) SessionCache.getInstance().get("formeFinanziamentoConsList");
			if (rlist != null)
				return rlist;  
			 
		String query = QUERY_FORME_FINANZIAMENTO; 
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	
	    //NB: qui assumo che dt_inizio sia sempre non nulla sul db		
		if(StringUtils.isBlank(dataInvio)) {
		   query +="  (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE))  ";
		}else{
		   query +="  (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}			
		query += "ORDER BY b.cod_forma_finanziamento ";		 
	    logger.debug("[FormaFinanziamento::inject] getFormeFinanziamentoList(idBando:" + idBando + " , dataInvio: " + dataInvio + ") query:" + query);	
	        
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	
		try {
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipoFormaFinanziamentoVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		// SessionCache.getInstance().set("formeFinanziamentoConsList", rlist);
		return rlist;
		
	  }
	  
	  
	  public static	List<TipoFormaFinanziamentoVO> getFormeFinanziamentoConImpMinMaxList(String idBando, String dataInvio, Logger logger) throws CommonalityException {
			
			List<TipoFormaFinanziamentoVO> rlist = null;
			if (rlist != null)
				return rlist;  
			 
		String query = QUERY_FORME_FINANZIAMENTO_CON_IMPORTI_MIN_MAX; 
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	
	    //NB: qui assumo che dt_inizio sia sempre non nulla sul db		
		if(StringUtils.isBlank(dataInvio)) {
		   query +="  (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE))  ";
		}else{
		   query +="  (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}			
		query += "ORDER BY b.cod_forma_finanziamento ";		 
	    logger.debug("[FormaFinanziamento::inject] getFormeFinanziamentoList(idBando:" + idBando + " , dataInvio: " + dataInvio + ") query:" + query);	
	        
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	
		try {
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipoFormaFinanziamentoVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		// SessionCache.getInstance().set("formeFinanziamentoConsList", rlist);
		return rlist;
		
	  }
	  
	  public static String getImportoMassimoErogabile(String idBando, Logger logger) throws CommonalityException {
	
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		try {
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_MAX, namedParameters);
			if (row!=null && row.next())
				return row.getString("importoMassimoErogabile");
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
	
		public static String getImportoMinimoErogabile(String idBando, Logger logger) throws CommonalityException {
			
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		    try {
		    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_MIN, namedParameters);
		    	if (row!=null && row.next())
		    		return row.getString("importoMinimoErogabile");
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
	
	
		/* : Jira 1671 -*/
		public static String getImpMaxErogErreBandi(String idBando, String idFormaFinanziamento, Logger logger) throws CommonalityException {
	
		    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idBando", idBando, Types.NUMERIC);
			namedParameters.addValue("idFormaFinanziamento", idFormaFinanziamento, Types.NUMERIC);
			
			try {
				SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_MAX_EROGABILE, namedParameters);
				logger.info("row: " + row);
				
				if (row!=null && row.next())
					return row.getString("importoMassimoErogabile");
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
	

		/* : Jira 1671 -*/
		public static String getImpMinErogErreBandi(String idBando, String idFormaFinanziamento, Logger logger) throws CommonalityException
		{
			logger.info("Recupero limite minimo erogabile per agv_richiesta...");
			logger.info("idBando: " + idBando);
			logger.info("idFormaFinanziamento: " + idFormaFinanziamento);
			
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idBando", idBando, Types.NUMERIC);
			namedParameters.addValue("idFormaFinanziamento", idFormaFinanziamento, Types.NUMERIC);
			
			try {
				SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_MIN_EROGABILE, namedParameters);
				logger.info("row: " + row);
				
				if (row!=null && row.next())
					return row.getString("importoMinimoErogabile");
				else 
					return "";
			}
			catch (EmptyResultDataAccessException e) {
				return "";
			}
		    catch (DataAccessException e1) {
		      throw new CommonalityException(e1);
		      
		    }
		}
		
		
		/** jira: 1938 */
		public static String  getContributoTotRichiesto (Integer idDomanda, Logger logger) throws CommonalityException {
			
		    logger.info("[FormaFinanziamentoDAO::getContributoTotRichiesto] BEGIN");
		    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
		   
		    try {
		    	SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_NODO_FORMA_FINANZIAMENTO_CONTRIBUTO_TOT_RICHIESTO, namedParameters);
		    	
		    	if (sqlRowSet!=null && sqlRowSet.next()){
		    		logger.info("[FormaFinanziamentoDAO::getContributoTotRichiesto] nodo _formaFinanziamento gia' presente nell'xml");
		    		return sqlRowSet.getString("contributoTotaleRichiesto");
		    	}else {
		    		logger.debug("[FormaFinanziamentoDAO::getContributoTotRichiesto] nodo _formaFinanziamento non presente nell'xml");
		    		return null;
		    	}
		    	
		    }
		    catch (EmptyResultDataAccessException e) {
		    	logger.debug("[FormaFinanziamentoDAO::getContributoTotRichiesto] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _caratteristicheProgetto");
		    	return null;
			}
		    catch (DataAccessException e) {
		    	logger.debug("[CaratteristicheProgettoNGDAO::getContributoTotRichiesto] eccezione DataAccessException nel recupero dell'eventuale nodo _caratteristicheProgetto");
		        throw new CommonalityException(e);
		    }finally{
		    	logger.debug("[CaratteristicheProgettoNGDAO::getContributoTotRichiesto] END");
		    }

		}
	}
	
	
	

	
