/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazioneB;

import it.csi.findom.blocchetti.common.vo.pianospese.ImportoPianoSpeseVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CultFormaAgevolazioneBDAO extends JdbcTemplate{

	private static final String QUERY_IMPORTO_EROGABILE = "SELECT "
			 + "(xpath('/tree-map/_formaAgevolazioneB/map/importoErogabile/text()',  aggr_t_model.serialized_model))[1]::text AS importoerogabile "
			 + "FROM aggr_t_model "
			 + "WHERE aggr_t_model.model_id = :idDomanda" ;
	
	/** jira 2009 - */
	private static final String QUERY_ID_SOGGETTO_ABILITATO = "SELECT a.id_sogg_abil AS idsogabilitato"
			+ " FROM findom_t_soggetti_abilitati a"
			+ " WHERE  UPPER(a.codice_fiscale) = UPPER(:codFiscaleBeneficiario) ";
	
	private static final String QUERY_IMPORTO_CONTRIBUTO_MAX_ULTIMO_TRIENNIO = "SELECT rsa.importo_contributo AS importoMaxContributoUltimiTreAnni"
			+ " FROM findom_r_bandi_soggetti_abilitati rsa"
			+ " WHERE rsa.id_bando = :idBando "
			+ " AND id_sogg_abil = :idSoggetto"
			+ " AND now() >= rsa.dt_inizio"
			+ " AND (rsa.dt_fine is null OR now() <= rsa.dt_fine)";

	
	private static final String QUERY_PARAMETRI_CALCOLO = "SELECT NVL(perc_quota_parte, 100.00) AS percQuotaParteSpeseGenFunz, "
		+ " NVL(perc_max_contributo_erogabile, 100.00) AS percMassimaContributoErogabile, "
		+ " importo_minimo_erogabile AS importoMinimoErogabile, "
		+ " importo_massimo_erogabile AS importoMassimoErogabile "	 
		+ " FROM findom_t_bandi"
		+ " WHERE  id_bando = :idBando ";
	
	/** QUERY_TOT_VOCI_SPESA   per sistema neve b1 */
	private static final String QUERY_TOT_VOCI_SPESA = "SELECT count(id_voce_spesa)"
			 + " FROM findom_v_voci_spesa a"
			 + " WHERE  id_bando = :idBando"
			 + " AND a.id_categ_voce_spesa = :idCategVoceSpesa"
			 + " AND";
	
	/** QUERY_TOT_VOCI_ENTRATA per verificare numero di voci entrate presenti by idBando: per sistema neve b1 */
	private static final String QUERY_TOT_VOCI_ENTRATA = "SELECT count(a.id_voce_entrata)"
			 + " FROM findom_r_bandi_voci_entrata a"
			 + " WHERE  a.id_bando = :idBando"
			 + " AND";
	
	/** jira 2144 */
	private static final String QUERY_COUNT_IMPORTI = "SELECT count(distinct(rsa.importo_contributo))" 
			+ " FROM findom_r_bandi_soggetti_abilitati rsa"
			+ " WHERE id_bando = :idBando"
			+ " AND id_sogg_abil = :idSoggetto"
			+ " AND now() >= rsa.dt_inizio"
			+ " AND (rsa.dt_fine is null OR now() <= rsa.dt_fine)";
	
	/** jira 2144 : as (id; codice; descrizione; importo) -  idSoggetto, idDipartimento, importo */
	private static final String QUERY_ELENCO_IMPORTI = "SELECT rsa.id_sogg_abil as idSoggetto, rsa.id_dipartimento as idDipartimento, rsa.importo_contributo as importo" 
			+ " FROM findom_r_bandi_soggetti_abilitati rsa"
			+ " WHERE id_bando = :idBando"
			+ " AND id_sogg_abil = :idSoggetto"
			+ " AND now() >= rsa.dt_inizio"
			+ " AND (rsa.dt_fine is null OR now() <= rsa.dt_fine)";
	
	
	/** getTotVociSpesa per sistema neve b1 */
	public static int getTotVociSpesa(Integer idBando, Integer idCategVoceSpesa, String dataInvio, Logger logger) throws CommonalityException {

		Integer numeroVoci = null;
		
		String query = QUERY_TOT_VOCI_SPESA;
		
		logger.info("[CultFormaAgevolazioneBDAO::getTotVociSpesa] getTotVociSpesa() query:" + query);
		
		if(StringUtils.isBlank(dataInvio)) {
		   query +="     (date_trunc('day', a.dt_inizio_intervento) <= CURRENT_DATE and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= CURRENT_DATE)) and ";
		   query +="     (date_trunc('day', a.dt_inizio_voce_spesa) <= CURRENT_DATE and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= CURRENT_DATE))  ";
		}else{
		   query +="     (date_trunc('day',a.dt_inizio_intervento) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_intervento is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
		   query +="     (date_trunc('day',a.dt_inizio_voce_spesa) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine_voce_spesa is null or date_trunc('day',a.dt_fine_voce_spesa) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) ";
		}	
	    query += " ; ";
		logger.info("debug: query risulta: " + query);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		namedParameters.addValue("idCategVoceSpesa", idCategVoceSpesa, Types.NUMERIC);

		try {
			numeroVoci = (Integer)jdbcTemplate.queryForObject(query, namedParameters, Integer.class);
		}
		catch (DataAccessException e) {
		  logger.info("[CultFormaAgevolazioneBDAO::getTotVociSpesa] DataAccessException:" + e.getMessage());
		}
		
		return numeroVoci.intValue();
		
	}
	
	
	/** : query per il recupero numero voci di entrata: per sistema neve b1 */
	public static int getTotVociEntrata(Integer idBando, String dataInvio, Logger logger) throws CommonalityException {

		Integer numeroVoci = null;
		
		String query = QUERY_TOT_VOCI_ENTRATA;
		
		logger.info("[CultFormaAgevolazioneBDAO::getTotVociEntrata] getTotVociEntrata() query:" + query);
		
		if(StringUtils.isBlank(dataInvio)) {
		   query +="     (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE))";
		}else{
		   query +="     (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine_intervento) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))";
		}	
	    query += " ; ";
		logger.info("debug: query risulta: " + query);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);

		try {
			numeroVoci = (Integer)jdbcTemplate.queryForObject(query, namedParameters, Integer.class);
		}
		catch (DataAccessException e) {
		  logger.debug("[CultFormaAgevolazioneBDAO::getTotVociEntrata] DataAccessException:" + e.getMessage());
		}
		
		return numeroVoci.intValue();
		
	}
	
	
	public static String getImportoErogabileFromXml(Integer idDomanda, Logger logger) throws CommonalityException {
		 
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_EROGABILE, namedParameters);
	    	if (row!=null && row.next())
	    		return row.getString("importoerogabile");
	    	else 
	    		return "0,00";
	    }
	    catch (EmptyResultDataAccessException e) {
	    	return "0,00";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	}
	
	public static ParametriCalcoloBVO getParametriCalcolo(String idBando) throws CommonalityException {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);

		try {
			return jdbcTemplate.queryForObject(QUERY_PARAMETRI_CALCOLO, namedParameters,
					new BeanPropertyRowMapper<ParametriCalcoloBVO>(ParametriCalcoloBVO.class));
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	/**********************************************************************
	 * Jira: 1337:
	 * Richiamo procedura [ fn_findom_get_param ]
	 *  che recupera i parametri del Bando tramite <idDomanda>
	 *  
	 *  :: I dati recuperati hanno le seguenti colonne ::
	 *  ImportoPianoSpeseVO.java : la classe di riferimento campi seguenti:
	 *  
	 * - t_importo_minimo_erogabile
	 * - t_importo_massimo_erogabile
	 * - t_budget_disponibile
	 * - t_perc_max_contributo_erogabile
	 * - t_totale_spese_minimo
	 * - t_totale_spese_massimo
	 * 
	 * @param src
	 * @param tipoRecord
	 * @return
	 * @throws SQLException 
	 ***********************************************************************/
	public static ImportoPianoSpeseVO getImportiPianoSpese(Integer idDomanda, Logger logger) throws CommonalityException, SQLException 
	{
		Connection conn = null;
		DataSource ds = null;
		ResultSet rs = null;
		CallableStatement callableSt = null;
		
		ImportoPianoSpeseVO importoPianoSpesa = new ImportoPianoSpeseVO();
		
		try {
			
			ds = (DataSource) (new InitialContext()).lookup("java:jboss/findomweb/jdbc/findomwebDS");
			conn = ds.getConnection();
			String sql = "{call fn_findom_get_param(?)}";
			
			callableSt = conn.prepareCall(sql);
			callableSt.setInt(1, idDomanda);
			
			rs = callableSt.executeQuery();
			while (rs.next()) {
				importoPianoSpesa.setImportoMinimoErogabile(rs.getInt("t_importo_minimo_erogabile"));
				importoPianoSpesa.setImportoMassimoErogabile(rs.getInt("t_importo_massimo_erogabile"));
				importoPianoSpesa.setBudgetDisponibile(rs.getInt("t_budget_disponibile"));
				importoPianoSpesa.setPercMaxContributoErogabile(rs.getInt("t_perc_max_contributo_erogabile"));
				importoPianoSpesa.setTotaleSpeseMinimo(rs.getInt("t_totale_spese_minimo"));
				importoPianoSpesa.setTotaleSpeseMassimo(rs.getInt("t_totale_spese_massimo"));
			}
		}
		
		catch (DataAccessException | NamingException e) {
			throw new CommonalityException(e);
		}
		finally
		{
			if (rs != null) {
				rs.close();
			}

			if (callableSt != null) {
				callableSt.close();
			}
			
			if(conn != null){
					conn.close();
			}
			
			if(ds != null){
				ds.getConnection().close();
			}
		}
		
		return importoPianoSpesa;
  }


	public static String getIdSoggettoAbilitato(String codFiscaleBeneficiario, Logger logger) throws CommonalityException {
		
		String ris = "";
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("codFiscaleBeneficiario", codFiscaleBeneficiario, Types.VARCHAR);
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_ID_SOGGETTO_ABILITATO, namedParameters);
	    	if (row!=null && row.next()){
	    		ris = row.getString("idsogabilitato");
	    		logger.info("idSoggetto abilitato vale: "+ris);
	    	}
	    	else{
	    		ris = "";
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	ris = "0,00";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
		
		return ris;
	}


	public static String getImportoContributoUltimoTriennio( Integer idBando, Integer idSoggetto, Logger logger) throws CommonalityException {
		
		String ris = "";
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.INTEGER);
	    namedParameters.addValue("idSoggetto", idSoggetto, Types.INTEGER);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_CONTRIBUTO_MAX_ULTIMO_TRIENNIO, namedParameters);
	    	if (row!=null && row.next()){
	    		ris = row.getString("importoMaxContributoUltimiTreAnni");
	    		logger.info("importoMaxContributoUltimiTreAnni vale: "+ris);
	    	}
	    	else{
	    		ris = "0,00";
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	ris = "0,00";
		}
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
		return ris;
	}
	
	
	/** jira 2144 */
	public static Integer getNumeroImportiByIdSoggettoBeneficiario(Integer idBando, Integer idSoggetto, Logger logger) throws CommonalityException {
		
		logger.info("[CultFormaAgevolazioneBDAO::getNumeroImportiByIdSoggettoBeneficiario] idBando = "+idBando);
		Integer num = 0;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		Map<String, Integer> params = new HashMap<>();
		params.put("idBando", idBando);
		params.put("idSoggetto", idSoggetto);
		
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		
		logger.info("[CultFormaAgevolazioneBDAO::getNumeroImportiByIdSoggettoBeneficiario] query:" + QUERY_COUNT_IMPORTI);
		
		num = jdbcTemplate.queryForObject(QUERY_COUNT_IMPORTI, params, Integer.class);
		
		logger.info("[CultFormaAgevolazioneBDAO::getNumeroImportiByIdSoggettoBeneficiario] num:" + num);
		
		return num;
	}
	
	/** jira 2144 : QUERY_ELENCO_IMPORTI */
	public static List<CultFormaAgevolazioneBVO> getImportiList(Integer idBando, Integer idSoggetto, Logger logger) throws CommonalityException {
		  
	    List<CultFormaAgevolazioneBVO> rlist = null;

	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		Map<String, Object> params = new HashMap<>();
		params.put("idBando", idBando);
		params.put("idSoggetto", idSoggetto);
		
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		
		logger.info("[CultFormaAgevolazioneBDAO::getImportiList] query:" + QUERY_ELENCO_IMPORTI);

	    try {
	      rlist = jdbcTemplate.query(QUERY_ELENCO_IMPORTI, namedParameters, new BeanPropertyRowMapper<>(CultFormaAgevolazioneBVO.class));
	    }
	    catch (DataAccessException e) {
	      throw new CommonalityException(e);
	    }
	    
	    return rlist;
    }
	
}
