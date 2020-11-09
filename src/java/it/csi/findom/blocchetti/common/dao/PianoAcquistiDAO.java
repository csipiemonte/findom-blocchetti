/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.pianoAcquistiAutomezzi.DettaglioVoceSpesaInterventoVO;
import it.csi.findom.blocchetti.common.vo.veicoli.CategoriaVeicoloVOV2;
import it.csi.findom.blocchetti.common.vo.veicoli.CategoriaVeicoloVOV3;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
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

public class PianoAcquistiDAO {

	private static String QUERY_MODEL_TOTALE = "SELECT " +
			" (xpath('/tree-map/_pianoSpese/map/totale/text()'::text, " +
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

	public static List<CategoriaVeicoloVOV2> getCategoriaVeicolo(int idVoceSpesa, Integer idSportelloBando, Logger logger) throws CommonalityException {

		String prf = "[PianoAcquistiDAO::getCategoriaVeicolo] ";
		logger.info(prf + " BEGIN ");
		
		String sessionName = "CategoriaVeicolo_"+idVoceSpesa;
		logger.info(prf + " sessionName="+sessionName);
		
		List<CategoriaVeicoloVOV2> rlist = (List) SessionCache.getInstance().get(sessionName);
		
		if (rlist != null){
			logger.info(prf + "leggo rlist da sessione");
		
		}else{
			rlist = new ArrayList<CategoriaVeicoloVOV2>();
		
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idVoceSpesa", idVoceSpesa, Types.NUMERIC);
			namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
			
			logger.info(prf + "idVoceSpesa="+idVoceSpesa);
			
			try {
				
/*
 * : modifica richiesta in data: 02/10/2020 da @DT
 * 				
				String query = "SELECT ca.importo_contributo as  importo_contributo, c.descr_breve as descr_breve_massa_veicolo, c.descrizione as descrizione_massa_veicolo,  " +
							" c.id_categ_veicolo as id_categoria_veicolo, ca.id_voce_spesa as id_voce_spesa " +
							" FROM findom_t_contrib_acq_veicoli ca, findom_d_categ_veicoli  c " +
						" WHERE ca.id_voce_spesa = :idVoceSpesa " +
						" AND c.id_categ_veicolo = ca.id_categ_veicolo " +
						" order by  ca.id_categ_veicolo";
*/
				String query = "SELECT ca.importo_contributo as  importo_contributo, c.descr_breve as descr_breve_massa_veicolo, c.descrizione as descrizione_massa_veicolo," +
						" c.id_categ_veicolo as id_categoria_veicolo, ca.id_voce_spesa as id_voce_spesa" +
						" FROM findom_t_contrib_acq_veicoli ca, findom_d_categ_veicoli  c " +
						" WHERE ca.id_voce_spesa = :idVoceSpesa " +
						" AND ca.id_sportello_bando = :idSportelloBando " +
						" AND c.id_categ_veicolo = ca.id_categ_veicolo " +
						" order by  ca.id_categ_veicolo";
				
				
				
				logger.info(prf + "query="+query);
				
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(CategoriaVeicoloVOV2.class));
				if (rlist!=null) {
					logger.info(prf + " rlist.size =  " + rlist.size());
				}
			}catch (EmptyResultDataAccessException e) {
				SessionCache.getInstance().set(sessionName, rlist);
				return rlist;
			}catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
				
			SessionCache.getInstance().set(sessionName, rlist);
		}
		logger.info(prf + " END ");
		return rlist;
	}
	
	public static List<CategoriaVeicoloVOV3> getCategoriaVeicoloBySportello(int idVoceSpesa, Integer idSportelloBando, Logger logger) throws CommonalityException {

		String prf = "[PianoAcquistiDAO::getCategoriaVeicoloBySportello] ";
		logger.info(prf + " BEGIN ");

		String sessionName = "CtgVeicolo_"+idVoceSpesa+"_idSportelloBando_"+idSportelloBando;
		logger.info(prf + " sessionName="+sessionName);
		
		List<CategoriaVeicoloVOV3> rlist = (List) SessionCache.getInstance().get(sessionName);

		if (rlist != null){
			logger.info(prf + "leggo rlist da sessione");
		
		}else{

			rlist = new ArrayList<CategoriaVeicoloVOV3>();
		
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idVoceSpesa", idVoceSpesa, Types.NUMERIC);
			namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
			
			logger.info(prf + "idVoceSpesa="+idVoceSpesa);
			
			try {

				String query = "SELECT  ca.importo_contributo as importo_contributo, c.descr_breve as descr_breve_massa_veicolo, c.descrizione as descrizione_massa_veicolo," +
						" c.id_categ_veicolo as id_categoria_veicolo, e.emissioni_co2, e.emissioni_nox," 	+
						" 'CO2: '|| e.emissioni_co2 || ' - ' || 'NOX: ' || e.emissioni_nox as emissioni,"   +
						" RANK () OVER(PARTITION BY ca.id_voce_spesa, e.emissioni_co2 ORDER BY ca.importo_contributo DESC) as tipo_record_emissione," +
						" ca.id_voce_spesa as id_voce_spesa" 												+
						" FROM findom_t_contrib_acq_veicoli ca" 											+
						" LEFT JOIN findom_d_categ_veicoli c ON c.id_categ_veicolo = ca.id_categ_veicolo" 	+
						" LEFT JOIN findom_d_emissioni_veicoli e ON e.id_emissione = ca.id_emissione" 		+
						" WHERE id_voce_spesa = :idVoceSpesa AND ca.id_sportello_bando = :idSportelloBando" +
						" order by c.descr_breve";

				logger.info(prf + "query="+query);
				
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(CategoriaVeicoloVOV3.class));
				if (rlist!=null) {
					logger.info(prf + " rlist.size =  " + rlist.size());
				}
			}catch (EmptyResultDataAccessException e) {
				SessionCache.getInstance().set(sessionName, rlist);
				return rlist;
			}catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
				
			SessionCache.getInstance().set(sessionName, rlist);
		}
		logger.info(prf + " END ");
		return rlist;
	}
	
/*	
	public static List<CategoriaVeicoloVOV3> getFullEmissioniVeicoloBySportello( Integer idSportelloBando, Logger logger) throws CommonalityException 
	{
		String prf = "[PianoAcquistiDAO::getFullEmissioniVeicoloBySportello] ";
		logger.info(prf + " BEGIN ");
		
		String sessionName = "fullEmissioni_"+idSportelloBando;
		logger.info(prf + " sessionName="+sessionName);
		
		List<CategoriaVeicoloVOV3> rlist = (List) SessionCache.getInstance().get(sessionName);
		
		if (rlist != null){
			logger.info(prf + "leggo rlist da sessione");
		
		}else{
		
			rlist = new ArrayList<CategoriaVeicoloVOV3>();
		
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
			
			logger.info(prf + "idSportelloBando="+idSportelloBando);
			
			try {
				
				String query = "SELECT  distinct a.id_emissione, a.emissioni_co2, a.emissioni_nox, 'CO2: '|| a.emissioni_co2 || ' - ' || 'NOX: ' || a.emissioni_nox as emissioni" +
						" FROM findom_d_emissioni_veicoli a" 											+
						" inner join findom_t_contrib_acq_veicoli b on b.id_emissione = a.id_emissione" 		+
						" where b.id_sportello_bando = :idSportelloBando";

				logger.info(prf + "query="+query);
				
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(CategoriaVeicoloVOV3.class));
				if (rlist!=null) {
					logger.info(prf + " rlist.size =  " + rlist.size());
				}
			}catch (EmptyResultDataAccessException e) {
				SessionCache.getInstance().set(sessionName, rlist);
				return rlist;
			}catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
				
			SessionCache.getInstance().set(sessionName, rlist);
		}
		logger.info(prf + " END ");
		return rlist;
	}
*/	
	
	public static List<CategoriaVeicoloVOV3> getFullEmissioniVeicoloByIdVoce( Integer idVoceSpesa, Integer idSportelloBando, String flagStatoAcquistoAuto, Logger logger) throws CommonalityException 
	{
		String prf = "[PianoAcquistiDAO::getFullEmissioniVeicoloByIdVoce] ";
		logger.info(prf + " BEGIN ");
		
		String sessionName = "fullEmissioni_"+idVoceSpesa+"_sportello_"+idSportelloBando+"_flag_"+flagStatoAcquistoAuto;
		logger.info(prf + " sessionName="+sessionName);
		
		List<CategoriaVeicoloVOV3> rlist = (List) SessionCache.getInstance().get(sessionName);
		
		if (rlist != null){
			logger.info(prf + "leggo rlist da sessione");
		
		}else{
		
			rlist = new ArrayList<CategoriaVeicoloVOV3>();
		
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
			namedParameters.addValue("idVoceSpesa", idVoceSpesa, Types.NUMERIC);
			
			logger.info(prf + "idSportelloBando="+idSportelloBando);
			logger.info(prf + "idVoceSpesa="+idVoceSpesa);
			logger.info(prf + "flagStatoAcquistoAuto="+flagStatoAcquistoAuto);
			
			try {
				
				String query = " ";
				query += "SELECT * FROM ( ";	
				query += " SELECT  ca.importo_contributo as importo_contributo, ";	
				
				if(flagStatoAcquistoAuto.equals("false")) {
					query +=" e.emissioni_co2 || ' - ' || e.emissioni_nox as emissioni,";
					query +=" e.id_emissione,";
				}else{
					query +=" e.emissioni_co2 as emissioni,";
					query +=" e.id_emissione,";
				}	
					
				query += " RANK () OVER(PARTITION BY ca.id_voce_spesa, e.emissioni_co2 ORDER BY ca.importo_contributo DESC) as tipo_record_emissione,";		    
				query += " ca.id_voce_spesa as id_voce_spesa ";
				query += " FROM findom_t_contrib_acq_veicoli ca";  
				query += " LEFT JOIN findom_d_emissioni_veicoli e ON e.id_emissione = ca.id_emissione"; 	
				query += " WHERE ca.id_voce_spesa = :idVoceSpesa";
				query += " AND ca.id_sportello_bando = :idSportelloBando";
				query += " ORDER BY ca.id_categ_veicolo) b";
				
				if(flagStatoAcquistoAuto.equals("false")) {
					query +=" WHERE b.tipo_record_emissione in (1,2)";
				}else{
					query +=" WHERE b.tipo_record_emissione = 1";
				}
				
				logger.info(prf + "query="+query);
				
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(CategoriaVeicoloVOV3.class));
				if (rlist!=null) {
					logger.info(prf + " rlist.size =  " + rlist.size());
				}
			}catch (EmptyResultDataAccessException e) {
				SessionCache.getInstance().set(sessionName, rlist);
				return rlist;
			}catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
				
			SessionCache.getInstance().set(sessionName, rlist);
		}
		logger.info(prf + " END ");
		return rlist;
	}
	
	
/*
	public static List<CategoriaVeicoloVOV3> getEmissioniVeicoloBySportello(int idVoceSpesa, Integer idSportelloBando, Logger logger) throws CommonalityException {

		String prf = "[PianoAcquistiDAO::getEmissioniVeicoloBySportello] ";
		logger.info(prf + " BEGIN ");
		
		String sessionName = "CtgVeicolo_"+idVoceSpesa;
		logger.info(prf + " sessionName="+sessionName);
		
		List<CategoriaVeicoloVOV3> rlist = (List) SessionCache.getInstance().get(sessionName);
		
		if (rlist != null){
			logger.info(prf + "leggo rlist da sessione");
		
		}else{
			rlist = new ArrayList<CategoriaVeicoloVOV3>();
		
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idVoceSpesa", idVoceSpesa, Types.NUMERIC);
			namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
			
			logger.info(prf + "idVoceSpesa="+idVoceSpesa);
			logger.info(prf + "idSportelloBando="+idSportelloBando);
			
			try {
				
				String query = "SELECT  ca.importo_contributo as importo_contributo, c.descr_breve as descr_breve_massa_veicolo, c.descrizione as descrizione_massa_veicolo," +
						" c.id_categ_veicolo as id_categoria_veicolo, e.emissioni_co2, e.emissioni_nox," 	+
						" 'CO2: '|| e.emissioni_co2 || ' - ' || 'NOX: ' || e.emissioni_nox as emissioni,"   +
						" RANK () OVER(PARTITION BY ca.id_voce_spesa, e.emissioni_co2 ORDER BY ca.importo_contributo DESC) as tipo_record_emissione," +
						" ca.id_voce_spesa as id_voce_spesa" 												+
						" FROM findom_t_contrib_acq_veicoli ca" 											+
						" LEFT JOIN findom_d_categ_veicoli c ON c.id_categ_veicolo = ca.id_categ_veicolo" 	+
						" LEFT JOIN findom_d_emissioni_veicoli e ON e.id_emissione = ca.id_emissione" 		+
						" WHERE id_voce_spesa = :idVoceSpesa AND ca.id_sportello_bando = :idSportelloBando" +
						" order by ca.id_categ_veicolo";

				logger.info(prf + "query="+query);
				
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(CategoriaVeicoloVOV3.class));
				if (rlist!=null) {
					logger.info(prf + " rlist.size =  " + rlist.size());
				}
			}catch (EmptyResultDataAccessException e) {
				SessionCache.getInstance().set(sessionName, rlist);
				return rlist;
			}catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
				
			SessionCache.getInstance().set(sessionName, rlist);
		}
		logger.info(prf + " END ");
		return rlist;
	}
*/
	
	/**
	 * 
	 * @param idTipoInterventoPar
	 * @param dataInvio
	 * @param idBando
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<DettaglioVoceSpesaInterventoVO> getVociDiSpesaTipoInterventoList(String idTipoInterventoPar, String dataInvio, Integer idBando, Logger logger) throws CommonalityException  {

		String prf = "[PianoAcquistiDAO::getVociDiSpesaTipoInterventoList] ";
		logger.info(prf + " BEGIN ");
		
		String sessionName = "vociDiSpesaTipoIntervento_"+idTipoInterventoPar+"_idBando_"+idBando;
		logger.info(prf + " sessionName="+sessionName);
		
		List<DettaglioVoceSpesaInterventoVO> rlist = (List) SessionCache.getInstance().get(sessionName);
		
		if (rlist != null && !rlist.isEmpty()){
			logger.debug(prf + "leggo VociDiSpesaTipoInterventoList da sessione");
		
		}else{
			
			rlist = new ArrayList<DettaglioVoceSpesaInterventoVO>();
			
			String query = " ";
			query += "SELECT a.id_tipol_intervento          AS id_tipo_intervento, \n";	
			query += "       a.cod_tipol_intervento         AS cod_tipo_intervento, \n";		    
			query += "       a.descrizione_tipol_intervento AS descr_tipo_intervento, \n";
			query += "       a.id_voce_spesa                AS id_voce_spesa, \n";
			query += "       a.cod_voce_spesa               AS cod_voce_spesa, \n";
			query += "       a.descrizione_voce_spesa       AS descr_voce_spesa \n";	
			query += "FROM   findom_v_voci_spesa a \n";           
			query += "WHERE  a.id_tipol_intervento = '" + idTipoInterventoPar + "' and \n";
			query += "       a.id_bando = " + idBando + " and \n";
	
			if(StringUtils.isBlank(dataInvio)) {
				query +="     (date_trunc('day', a.dt_inizio_intervento) <= CURRENT_DATE and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= CURRENT_DATE)) and \n";
				query +="     (date_trunc('day', a.dt_inizio_voce_spesa) <= CURRENT_DATE and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= CURRENT_DATE))  \n";
			}else{
				query +="     (date_trunc('day',a.dt_inizio_intervento) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and \n";
				query +="     (date_trunc('day',a.dt_inizio_voce_spesa) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  \n";
			}	
			query += " ORDER BY a.cod_tipol_intervento, a.cod_voce_spesa ";
	
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			SqlParameterSource namedParameters = new MapSqlParameterSource();
			logger.info(prf + " getVociDiSpesaTipoInterventoList(idTipoInterventoPar:" + (idTipoInterventoPar) + " , dataInvio = " + dataInvio + ") query: " + query);
	
			try {
				List<Map<String, Object>> rowList = jdbcTemplate.queryForList(query, namedParameters);
				if ((rowList!=null)&&(rowList.size()>0)) {
	
					logger.info(prf + " rowList.size =  " + rowList.size());
					
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
					 SessionCache.getInstance().set(sessionName, rlist);
					return rlist;
				}
			}
			catch (EmptyResultDataAccessException e) {
				 SessionCache.getInstance().set(sessionName, rlist);
				return rlist;
			}
			catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
			
			 SessionCache.getInstance().set(sessionName, rlist);
		}
		
		logger.info(prf + " END ");
		return rlist;
	}
	
	
	public static List<DettaglioVoceSpesaInterventoVO> getVociDiSpesaDettaglioInterventoList(String idDettInterventoPar, String dataInvio, Integer idBando, Logger logger)  throws CommonalityException 
	{
		String prf = "[PianoAcquistiDAO::getVociDiSpesaDettaglioInterventoList] ";
		logger.debug(prf + " BEGIN");

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

		logger.debug(prf + "(idDettInterventoPar:" +  (idDettInterventoPar) + " , dataInvio = " + dataInvio + ") query: " + query);
		List<DettaglioVoceSpesaInterventoVO> rlist = new ArrayList<DettaglioVoceSpesaInterventoVO>();

		logger.debug(prf + " rlist: " + rlist);

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
		logger.debug(prf + " END");
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

}
