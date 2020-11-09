/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.RegioneProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.SiglaProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class LuoghiDAO {

	private static final String QUERY_STATI_ESTERI = "SELECT cod_stato AS codice, descrizione_stato AS descrizione, sigla_nazione as sigla  "
			+ "FROM ext_d_stati_esteri ORDER BY descrizione";

	private static final String QUERY_STATI_ESTERI_UE = "SELECT cod_stato AS codice, descrizione_stato AS descrizione, sigla_nazione as sigla  "
			+ "FROM ext_d_stati_esteri where flag_ue = 'S' ORDER BY descrizione";
	 
	private static final String QUERY_STATI_ESTERI_UE_BY_CODE = "SELECT cod_stato AS codice, descrizione_stato AS descrizione, sigla_nazione as sigla  "
			+ "FROM ext_d_stati_esteri where flag_ue = 'S' AND cod_stato = :codStato ORDER BY descrizione";
	
	private static final String QUERY_PROVINCE = "SELECT prov AS codice, desprov AS descrizione, sigprov AS sigla "
			+ "FROM ext_d_province ORDER BY descrizione";
	
	private static final String QUERY_PROVINCIA_TORINO = "SELECT prov AS codice, desprov AS descrizione, sigprov AS sigla "
			+ "FROM ext_d_province a"
			+ " WHERE a.prov = '001'"
			+ " ORDER BY descrizione";
	
	private static final String QUERY_PROVINCE_BY_REGIONE_01 = "SELECT prov AS codice, desprov AS descrizione, sigprov AS sigla "
			+ "FROM ext_d_province WHERE regione = '01' ORDER BY descrizione";

	private static final String QUERY_PROVINCE_BY_REGIONE_01_PROV = "SELECT prov AS codice, desprov AS descrizione, sigprov AS sigla "
			+ "FROM ext_d_province WHERE regione = '01' AND prov = :codProvincia ORDER BY descrizione";
	
	private static final String QUERY_REGIONI_E_PROVINCE = "SELECT regione AS regione, prov AS idProv, desprov AS descrizione, sigprov AS sigla "
			+ "FROM ext_d_province ORDER BY desprov";
	
	private static final String QUERY_COMUNI = "SELECT comune AS codice, descom AS descrizione "
			+ "FROM ext_d_comuni WHERE prov= :codiceProvincia ORDER BY descrizione";

	private static final String QUERY_SIGLE_PROVINCE = "SELECT sigprov AS sigla "
			+ "FROM ext_d_province WHERE prov= :codiceProvincia ";

	private static final String QUERY_CODICE_COMUNE = "SELECT comune AS codice "
			+ "FROM ext_d_comuni WHERE prov= :codiceProvincia ";
	
	private static final String QUERY_CODICE_STATO_ESTERO = "SELECT cod_stato AS codice " 
			+ "FROM ext_d_stati_esteri WHERE descrizione_stato = upper(:descrStato)";
	
	private static final String QUERY_STATI_ESTERI_SENZA_ITALIA = "SELECT cod_stato AS codice, descrizione_stato AS descrizione, sigla_nazione as sigla  "
			+ "FROM ext_d_stati_esteri WHERE cod_stato not in ('000') ORDER BY descrizione";
	
	private static final String QUERY_SIGLA_CONTINENTE = "SELECT sigla_continente AS continente "
			+ "FROM ext_d_stati_esteri  WHERE cod_stato = upper(:statoEstero)";
	
	//gestione beneficiario estero qqq
	private static final String SELECT_DESCR_STATO_BY_COD_STATO = "SELECT descrizione_stato AS descrStato FROM ext_d_stati_esteri WHERE cod_stato = :codStato ";
	
	/** Jira: 1697 ::2R */
	private static final String QUERY_PROVINCE_SENZA_PIEMONTE = "SELECT prov AS codice, desprov AS descrizione, sigprov AS sigla FROM ext_d_province WHERE prov NOT IN ('006','005','096','004','003','001','103','002') ORDER BY descrizione;";
	
	
	public static List<StatoEsteroVO> getStatoEsteroList(Logger logger) throws CommonalityException {
		List<StatoEsteroVO> rlist = null;
		rlist = (List) SessionCache.getInstance().get("statoEsteroConsList");
		if (rlist != null)
			return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		try {
			rlist = jdbcTemplate.query(QUERY_STATI_ESTERI, namedParameters,
					new BeanPropertyRowMapper<>(StatoEsteroVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.debug("[LuoghiDAO::getStatoEsteroList] query:" + QUERY_STATI_ESTERI);
		SessionCache.getInstance().set("statoEsteroConsList", rlist);
		return rlist;
	}
	
	/**
	 * Query per bandi che richiedono
	 * estrarre Nazioni estere
	 * eccetto Italia
	 * 
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<StatoEsteroVO> getStatoEsteroListSenzaItalia(Logger logger) throws CommonalityException {
		List<StatoEsteroVO> rlist = null;
		rlist = (List) SessionCache.getInstance().get("statoEsteroListSenzaItalia");
		if (rlist != null)
			return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		try {
			rlist = jdbcTemplate.query(QUERY_STATI_ESTERI_SENZA_ITALIA, namedParameters,
					new BeanPropertyRowMapper<>(StatoEsteroVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.debug("[LuoghiDAO::getStatoEsteroListSenzaItalia] query:" + QUERY_STATI_ESTERI_SENZA_ITALIA);
		SessionCache.getInstance().set("statoEsteroListSenzaItalia", rlist);
		return rlist;
	}

	public static List<StatoEsteroVO> getStatoEsteroListUE(Logger logger) throws CommonalityException 
	{
		List<StatoEsteroVO> rlist = null;
		rlist = (List<StatoEsteroVO>) SessionCache.getInstance().get("statoEsteroConsListUE");
	
		if (rlist != null)
			return rlist;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate
		(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource")
		);

		SqlParameterSource namedParameters = null;

		try {
			rlist = jdbcTemplate.query(QUERY_STATI_ESTERI_UE, namedParameters, new BeanPropertyRowMapper<>(StatoEsteroVO.class));
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}

		logger.info("[LuoghiDAO::getStatoEsteroListUE] query:" + QUERY_STATI_ESTERI);
		SessionCache.getInstance().set("statoEsteroConsListUE", rlist);
		return rlist;
	}


	public static List<StatoEsteroVO> getStatoEsteroListUEByCodStato(String codStato, Logger logger) throws CommonalityException {
		List<StatoEsteroVO> rlist = null;
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		Map<String, String> params = new HashMap<>();
		params.put("codStato", codStato);
		
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);

		try {
			rlist = jdbcTemplate.query(QUERY_STATI_ESTERI_UE_BY_CODE, namedParameters,
					new BeanPropertyRowMapper<>(StatoEsteroVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		logger.debug("[LuoghiDAO::getStatoEsteroListUEByCodStato] query:" + QUERY_STATI_ESTERI);
		return rlist;
	}
	
	public static List<ProvinciaVO> getProvinciaList(Logger logger) throws CommonalityException {
		List<ProvinciaVO> rlist = null;
//		rlist = (List) SessionCache.getInstance().get("provinciaConsList");
//		if (rlist != null)
//			return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		logger.info("[LuoghiDAO::getProvinciaList] query:" + QUERY_PROVINCE);

		try {
			rlist = jdbcTemplate.query(QUERY_PROVINCE, namedParameters, new BeanPropertyRowMapper<>(ProvinciaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
//		SessionCache.getInstance().set("provinciaConsList", rlist);
		return rlist;
	}
	
	
	/** Jira: 1697 ::2R */
	public static List<ProvinciaVO> getProvinceSenzaPiemonteList(Logger logger) throws CommonalityException {
		List<ProvinciaVO> rlist = null;
		rlist = (List) SessionCache.getInstance().get("provinceSenzaPiemonteConsList");
		if (rlist != null)
			return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		logger.debug("[LuoghiDAO::getProvinceSenzaPiemonteList] query:" + QUERY_PROVINCE_SENZA_PIEMONTE);

		try {
			rlist = jdbcTemplate.query(QUERY_PROVINCE_SENZA_PIEMONTE, namedParameters, new BeanPropertyRowMapper<>(ProvinciaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		SessionCache.getInstance().set("provinceSenzaPiemonteConsList", rlist);
		return rlist;
	}

	public static List<ProvinciaVO> getProvinciaList(String sediExtraPiemonte, Logger logger) throws CommonalityException {
		List<ProvinciaVO> rlist = null;
//		rlist = (List) SessionCache.getInstance().get("provincePiemonteConsList");
//		if (rlist != null)
//			return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;
		String query = QUERY_PROVINCE;
		
		if ("false".equals(sediExtraPiemonte))
			query = QUERY_PROVINCE_BY_REGIONE_01;
		logger.debug("[LuoghiDAO::getProvinciaList] query:" + query);

		try {
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(ProvinciaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
//		 SessionCache.getInstance().set("provincePiemonteConsList", rlist);
		return rlist;
	}
	
	
	public static List<ProvinciaVO> getProvinciaTorinoList(Logger logger) throws CommonalityException {
		List<ProvinciaVO> rlist = null;
//		 rlist = (List) SessionCache.getInstance().get("provincePiemonteConsList");
//		 if (rlist != null) return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;
		String query = QUERY_PROVINCIA_TORINO;
		
		logger.debug("[LuoghiDAO::getProvinciaList] query:" + query);

		try {
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(ProvinciaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
//		 SessionCache.getInstance().set("provincePiemonteConsList", rlist);
		return rlist;
	}
	

	/**
	 * Query restituisce solo province della Regione Piemonte
	 * 
	 * @param codProvincia
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static ProvinciaVO getProvinciaPiemonteByCode(String codProvincia, Logger logger) throws CommonalityException 
	{
		ProvinciaVO provinciaVO = null;
		List<ProvinciaVO> provinciaVOList = null;		

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		Map<String, String> params = new HashMap<>();
		params.put("codProvincia", codProvincia);
		
		SqlParameterSource namedParameters = new MapSqlParameterSource(params);
		

		try {
			//jira 909. Con queryForObject() se non vengono estratti record viene sollevata una eccezione; con query() non viene sollevata eccezione
			//rlist = jdbcTemplate.queryForObject(QUERY_PROVINCE_BY_REGIONE_01_PROV, namedParameters, new BeanPropertyRowMapper<>(ProvinciaVO.class));
			provinciaVOList = jdbcTemplate.query(QUERY_PROVINCE_BY_REGIONE_01_PROV, namedParameters, new BeanPropertyRowMapper<>(ProvinciaVO.class));
			if (provinciaVOList != null && !provinciaVOList.isEmpty()) {
				provinciaVO = provinciaVOList.get(0);
	    	 }
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		return provinciaVO;  //eventualmente null
	}

	
	
	public static List<RegioneProvinciaVO> getProvinceRegImpresaList(Logger logger) throws CommonalityException {
		List<RegioneProvinciaVO> rlist = null;
		rlist = (List<RegioneProvinciaVO>) SessionCache.getInstance().get("provinceRegImpresaConsList");
		if (rlist != null)
			return rlist;

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = null;

		logger.info("[LuoghiDAO::getProvinceRegImpresaList] query:" + QUERY_REGIONI_E_PROVINCE);

		try {
			rlist = jdbcTemplate.query(QUERY_REGIONI_E_PROVINCE, namedParameters, new BeanPropertyRowMapper<>(RegioneProvinciaVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		SessionCache.getInstance().set("provinceRegImpresaConsList", rlist);
		return rlist;
	}
	
	public static List<ComuneVO> getComuneList(String codiceProvincia, Logger logger) throws CommonalityException {
		logger.debug("[LuoghiDAO::getComuneList] (codiceProvincia:" + Utils.quote(codiceProvincia) + ") query:"
				+ QUERY_COMUNI);

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = new MapSqlParameterSource("codiceProvincia", codiceProvincia);

		List<ComuneVO> rlist = new ArrayList<ComuneVO>();
		try {
			rlist = jdbcTemplate.query(QUERY_COMUNI, namedParameters, new BeanPropertyRowMapper<>(ComuneVO.class));
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		return rlist;
	}

	/**
	 * 
	 * @param codiceProvincia
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<SiglaProvinciaVO> getSiglaProvinciaList(String codiceProvincia, Logger logger)
			throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		SqlParameterSource namedParameters = new MapSqlParameterSource("codiceProvincia", codiceProvincia);

		logger.debug("[LuoghiDAO::getSiglaProvinciaList] (codiceProvincia:" + Utils.quote(codiceProvincia) + ") query:"
				+ QUERY_SIGLE_PROVINCE);
		List<SiglaProvinciaVO> rlist = new ArrayList<SiglaProvinciaVO>();
		try {
			for (Map<String, Object> record : jdbcTemplate.queryForList(QUERY_SIGLE_PROVINCE, namedParameters)) {
				SiglaProvinciaVO vo = new SiglaProvinciaVO();
				vo.setSigla((String) record.get("sigla"));
				rlist.add(vo);
			}
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}

		return rlist;
	}
	
	public static String getCodiceComuneByDescrizione(String codiceProvincia, String descrComune, Logger logger) throws CommonalityException {
		logger.info("[LuoghiDAO::getCodiceComuneByDescrizione] query:"
				+ QUERY_CODICE_COMUNE);

		String query = QUERY_CODICE_COMUNE;
		logger.info(" query + " +query);

		descrComune.replace("'", "''");
		query += "and descom like :descrComune ";
		
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		Map<String, Object> params = new HashMap<>();
		params.put("codiceProvincia", codiceProvincia);
		params.put("descrComune", descrComune);

		SqlParameterSource namedParameters = new MapSqlParameterSource(params);


		String comune = "";
		try {
			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, namedParameters);
			
			
			if(rlist!=null && !rlist.isEmpty() && rlist.size()==1){
				comune = ((Map)rlist.get(0)).get("codice").toString();
				logger.info(" comune + " +comune);
			}
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		return comune;
		
	}
	
	// : Jira 905 - inizio : da verificare
	public static String getSiglaContinenteByCodStato(String statoEstero) throws CommonalityException {

		String query = QUERY_SIGLA_CONTINENTE;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		Map<String, Object> params = new HashMap<>();
		params.put("statoEstero", statoEstero);

		SqlParameterSource namedParameters = new MapSqlParameterSource(params);

		String siglaContinente = "";
		try {
			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, namedParameters);
			
			if(rlist!=null && !rlist.isEmpty() && rlist.size()==1){
				try {
					siglaContinente = ((Map)rlist.get(0)).get("continente").toString();
					if(siglaContinente.isEmpty() || siglaContinente == null)
					{
						siglaContinente = "FEU";
					}
				} catch (NullPointerException npe) {
					siglaContinente = "FEU";
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (DataAccessException | NullPointerException e) {
			throw new CommonalityException(e);
		}
		
		return siglaContinente;
		
	}
	// : Jira 905 - fine
	public static String getCodiceStatoEsteroByDescrizione(String descrStatoEstero, Logger logger) throws CommonalityException {
			logger.debug("[LuoghiDAO::getCodiceStatoEsteroByDescrizione] query:"
					+ QUERY_CODICE_STATO_ESTERO);

			String query = QUERY_CODICE_STATO_ESTERO;
				
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			Map<String, Object> params = new HashMap<>();
			params.put("descrStato", descrStatoEstero);

			SqlParameterSource namedParameters = new MapSqlParameterSource(params);


			String codice = "";
			try {
				List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, namedParameters);
				if(rlist!=null && !rlist.isEmpty() && rlist.size()==1){
					codice = ((Map)rlist.get(0)).get("codice").toString();
				}
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
			return codice;
	}
	
	//gestione beneficiario estero qqq
	public static String getDescrStatoByCodStato(String codStato, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getDescrStatoByCodStato] ";
		logger.info(logprefix + " BEGIN");
		String descrStato = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("codStato", codStato, java.sql.Types.VARCHAR);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(SELECT_DESCR_STATO_BY_COD_STATO, params);
			if (row!=null && row.next()){
				descrStato = row.getString("descrStato");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return descrStato;
	}
	
	public static ProvinciaVO getDatiProvincia(List<ProvinciaVO> provinciaList, String siglaProvincia) {
		ProvinciaVO prov = new ProvinciaVO();
		for (ProvinciaVO provincia : provinciaList){
			if(StringUtils.equals(siglaProvincia, provincia.getSigla())){
				prov.setCodice(provincia.getCodice());
				prov.setDescrizione(provincia.getDescrizione());
				break;
			}
		}
		return prov;
	}


}
