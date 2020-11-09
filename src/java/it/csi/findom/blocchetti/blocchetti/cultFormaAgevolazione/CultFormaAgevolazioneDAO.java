/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.cultFormaAgevolazione;

import it.csi.findom.blocchetti.common.vo.pianospese.ImportoPianoSpeseVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CultFormaAgevolazioneDAO extends JdbcTemplate{

	private static final String QUERY_IMPORTO_EROGABILE = "SELECT "
			 + "(xpath('/tree-map/_formaAgevolazione/map/importoErogabile/text()',  aggr_t_model.serialized_model))[1]::text AS importoerogabile "
			 + "FROM aggr_t_model "
			 + "WHERE aggr_t_model.model_id = :idDomanda" ;

	
	private static final String QUERY_PARAMETRI_CALCOLO = "SELECT NVL(perc_quota_parte, 100.00) AS percQuotaParteSpeseGenFunz, "
		+ " NVL(perc_max_contributo_erogabile, 100.00) AS percMassimaContributoErogabile, "
		+ " importo_minimo_erogabile AS importoMinimoErogabile, "
		+ " importo_massimo_erogabile AS importoMassimoErogabile "	 
		+ " FROM findom_t_bandi"
		+ " WHERE  id_bando = :idBando ";
	
	
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
	
	public static ParametriCalcoloVO getParametriCalcolo(String idBando) throws CommonalityException {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
											(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);

		try {
			return jdbcTemplate.queryForObject(QUERY_PARAMETRI_CALCOLO, namedParameters,
					new BeanPropertyRowMapper<ParametriCalcoloVO>(ParametriCalcoloVO.class));
			
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

}
