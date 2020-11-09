/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNG;

import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaDettaglioInterventoVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class CaratteristicheProgettoNGDAO {

	private static final String QUERY_TIPOLOGIA_INTERVENTI = " SELECT a.id_tipol_intervento AS id_tipo_intervento, "
			+ "      c.codice||' - '||c.descrizione  AS tipologia_intervento, "
			+ "      c.codice                        AS cod_tipo_intervento, "
			+ "      c.descrizione                   AS descr_tipo_intervento, "
			+ "      c.id_campo_intervento           AS id_campo_tipo_intervento, "
			+ "      d.cod_campo_intervento          AS cod_campo_tipo_intervento, "
			+ "      d.descrizione                   AS descr_campo_tipo_intervento, "
			+ "      a.flag_obbligatorio             AS flag_obbligatorio "
			+ " FROM findom_r_bandi_tipol_interventi a  "
			+ "      JOIN  findom_v_domande_nuova_gestione b ON a.id_bando = b.id_bando "
			+ "      JOIN  findom_d_tipol_interventi c ON a.id_tipol_intervento = c.id_tipol_intervento "
			+ "      LEFT OUTER JOIN findom_d_campi_intervento d  ON c.id_campo_intervento = d.id_campo_intervento  "
			+ " WHERE ";
	
	
	private static final String QUERY_TIPOLOGIA_INTERVENTI_AND_DETTAGLI = " SELECT a.id_tipol_intervento AS id_tipo_intervento, "
			+ "      c.codice||' - '||c.descrizione  AS tipologia_intervento, "
			+ "      c.codice                        AS cod_tipo_intervento, "
			+ "      c.descrizione                   AS descr_tipo_intervento, "
			+ "      c.id_campo_intervento           AS id_campo_tipo_intervento, "
			+ "      d.cod_campo_intervento          AS cod_campo_tipo_intervento, "
			+ "      d.descrizione                   AS descr_campo_tipo_intervento, "
			+ "      a.flag_obbligatorio             AS flag_obbligatorio, "
			+ "      e.descrizione					 AS dettaglio "	
			+ " FROM findom_r_bandi_tipol_interventi a  "
			+ "      JOIN  findom_v_domande_nuova_gestione b ON a.id_bando = b.id_bando "
			+ "      JOIN  findom_d_tipol_interventi c ON a.id_tipol_intervento = c.id_tipol_intervento "
			+ "		 JOIN findom_d_dett_tipol_interventi e ON e.id_tipol_intervento = c.id_tipol_intervento "
			+ "      LEFT OUTER JOIN findom_d_campi_intervento d  ON c.id_campo_intervento = d.id_campo_intervento  "
			+ " WHERE ";
	
	private static final String QUERY_DETTAGLIO_INTERVENTO = "select a.id_dett_tipol_intervento AS id_dett_intervento, "
			+ "       a.codice                   AS cod_dett_intervento, "
			+ "       a.descrizione              AS descr_dett_intervento,  "
			+ "       a.codice||' - '||a.descrizione  AS dettaglio_intervento, "
			+ "       a.id_tipol_intervento      AS id_intervento_dettaglio, "
			+ "       a.id_campo_intervento      AS id_campo_dett_intervento, "
			+ "       b.cod_campo_intervento     AS cod_campo_dett_intervento, "
			+ "       b.descrizione              AS descr_campo_dett_intervento "
			+ " from findom_d_dett_tipol_interventi a "
			+ "      LEFT OUTER JOIN  findom_d_campi_intervento b  ON a.id_campo_intervento = b.id_campo_intervento "
			+ " where a.id_tipol_intervento = :idTipolIntervento" + " order by a.id_tipol_intervento,a.codice ";
	
	
	private static final String QUERY_TIPOLOGIA_INTERVENTI_CUSTOM = " SELECT a.id_tipol_intervento AS id_tipo_intervento, "
			+ "      c.codice||' - '||c.descrizione  AS tipologia_intervento, "
			+ "      c.codice                        AS cod_tipo_intervento, "
			+ "      c.descrizione                   AS descr_tipo_intervento, "
			+ "      c.id_campo_intervento           AS id_campo_tipo_intervento, "
			+ "      d.cod_campo_intervento          AS cod_campo_tipo_intervento, "
			+ "      d.descrizione                   AS descr_campo_tipo_intervento, "
			+ "      a.flag_obbligatorio             AS flag_obbligatorio "
			+ " FROM findom_r_bandi_tipol_interventi a  "
			+ "      JOIN  findom_v_domande_nuova_gestione b ON a.id_bando = b.id_bando "
			+ "      JOIN  findom_d_tipol_interventi c ON a.id_tipol_intervento = c.id_tipol_intervento "
			+ "      LEFT OUTER JOIN findom_d_campi_intervento d  ON c.id_campo_intervento = d.id_campo_intervento  "
			+ " WHERE ";
	
	
	private static final String QUERY_NODO_CARATT_PRJ_NG = "SELECT " +
			" unnest(xpath('/tree-map/_caratteristicheProgetto'::text, " +
			" aggr_t_model.serialized_model))::text AS nodocaratteristiche " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda" ;
	
	private static final String QUERY_FLAG_PROGETTO = "SELECT flag_scheda_progetto FROM findom_t_bandi WHERE id_bando = :idBando";
	
	private static final String QUERY_TIPOLOG_INTERV_ASSOCIATE = "SELECT distinct(id_tipol_intervento) FROM findom_v_scheda_progetto_valut "+
																" WHERE id_bando = :idBando AND id_tipol_intervento is not null " +
																" ORDER BY id_tipol_intervento";
	
	
	private static final String QUERY_IMPORTO_COVID = "SELECT importo_contributo FROM findom_t_soggetti_bonus_covid WHERE cod_fiscale = :cfBeneficiario OR (partita_iva = :cfBeneficiario)";
	
	/** ira: 1977 - Recupero importo contributo by cir= codice identificativo regionale - */
	private static final String QUERY_IMPORTO_BY_CIR = "SELECT a.importo_contributo FROM findom_t_bonus_turismo a WHERE a.cod_regionale = UPPER(:codiceRegionale);";
	
	/** Jira: 1969 - */
	private static final String QUERY_CARATT_PRJ_NG_ID_DETTAGLIO_INTERVENTO = "SELECT " +
			" (xpath('/tree-map/_caratteristicheProgetto/map/tipologiaInterventoList/list/map[checked=\"true\"]/dettaglioInterventoList/list/map[checked=\"true\"]/idDettIntervento/text()'::text, " +
			" aggr_t_model.serialized_model))[1]::text AS dettagliointervento " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda" ;
	
	
	/** Jira: 1977 recupero importo in base al cir= codice identificativo regionale - */
	private static final String QUERY_CARATT_PRJ_NG_CODICE_REGIONALE_TURISMO = "SELECT (xpath('/tree-map/_operatorePresentatore/map/codiceRegionale/text()'::text," +
			" aggr_t_model.serialized_model)) [1]::text AS cod_regionale " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :idDomanda ;" ;
	
	/** Jira: 1977 verifico se importo risulta salvato su nodo xml - */
	private static final String QUERY_IMPORTO_CONTRIBUTO_CODE_REGIONALE = "SELECT (xpath('/tree-map/_caratteristicheProgetto/map/importoContributo/text()'::text," +
			" aggr_t_model.serialized_model)) [1]::text AS imp_regionale " +
			" FROM  aggr_t_model " +
			" WHERE aggr_t_model.model_id= :numeroDomanda ;" ;
	
	/** jira  @gf : bonus cultura 2020 - */
	private static final String QUERY_IMPORTO_BONUS_CULTURA = "SELECT "
			+ " b.importo_contributo AS contributo"
			+ " FROM findom_t_soggetti_abilitati a"
			+ " JOIN findom_r_bandi_soggetti_abilitati b ON a.id_sogg_abil = b.id_sogg_abil"
			+ " WHERE a.codice_fiscale = :cfBeneficiario" 
			+ " AND b.id_bando = :idBando";
	
	
	/** Gestione Bonus cultura 2020 covid19 - */
	public static String getImportoBonusCultura(Integer idBando, String cfBeneficiario, Logger logger) throws CommonalityException {
		
		final String logprefix = "[CaratteristicheProgettoNGDAO:: getImportoBonusCultura] ";
		
		logger.info(logprefix + " BEGIN");
		
		String impContributoRis = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("idBando", idBando, java.sql.Types.INTEGER);	
			params.addValue("cfBeneficiario", cfBeneficiario, java.sql.Types.VARCHAR);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_BONUS_CULTURA, params);
			if (row!=null && row.next()){
				impContributoRis = row.getString("contributo");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		
		return impContributoRis;
	}
	
	
	/** Jira: 1969 - */
	public static String  getIdDettInterventoCaratteristichePrjNG(Integer idDomanda, Logger logger) throws CommonalityException {
		
	    logger.info("[CaratteristicheProgettoNGDAO::getIdDettInterventoCaratteristichePrjNG] BEGIN");
	    
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_CARATT_PRJ_NG_ID_DETTAGLIO_INTERVENTO, namedParameters);
	    	if (sqlRowSet!=null && sqlRowSet.next()){
	    		logger.debug("[CaratteristicheProgettoNGDAO::getIdDettInterventoCaratteristichePrjNG] nodo _caratteristicheProgetto gia' presente nell'xml");
	    		return sqlRowSet.getString("dettagliointervento");
	    	}else {
	    		logger.debug("[CaratteristicheProgettoNGDAO::getIdDettInterventoCaratteristichePrjNG] nodo _caratteristicheProgetto non presente nell'xml");
	    		return null;
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CaratteristicheProgettoNGDAO::getIdDettInterventoCaratteristichePrjNG] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _caratteristicheProgetto");
	    	return null;
		}
	    catch (DataAccessException e) {
	    	logger.debug("[CaratteristicheProgettoNGDAO::getIdDettInterventoCaratteristichePrjNG] eccezione DataAccessException nel recupero dell'eventuale nodo _caratteristicheProgetto");
	        throw new CommonalityException(e);
	    }finally{
	    	logger.debug("[CaratteristicheProgettoNGDAO::getIdDettInterventoCaratteristichePrjNG] END");
	    }

	}
	
	
	public static String  getNodoCaratteristichePrjNG(Integer idDomanda, Logger logger) throws CommonalityException {
	    logger.debug("[CaratteristicheProgettoNGDAO::getNodoCaratteristichePrjNG] BEGIN");
	    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    try {
	    	SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_NODO_CARATT_PRJ_NG, namedParameters);
	    	if (sqlRowSet!=null && sqlRowSet.next()){
	    		logger.debug("[CaratteristicheProgettoNGDAO::getNodoCaratteristichePrjNG] nodo _caratteristicheProgetto gia' presente nell'xml");
	    		return sqlRowSet.getString("nodocaratteristiche");
	    	}else {
	    		logger.debug("[CaratteristicheProgettoNGDAO::getNodoCaratteristichePrjNG] nodo _caratteristicheProgetto non presente nell'xml");
	    		return null;
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CaratteristicheProgettoNGDAO::getNodoCaratteristichePrjNG] eccezione EmptyResultDataAccessException nel recupero dell'eventuale nodo _caratteristicheProgetto");
	    	return null;
		}
	    catch (DataAccessException e) {
	    	logger.debug("[CaratteristicheProgettoNGDAO::getNodoCaratteristichePrjNG] eccezione DataAccessException nel recupero dell'eventuale nodo _caratteristicheProgetto");
	        throw new CommonalityException(e);
	    }finally{
	    	logger.debug("[CaratteristicheProgettoNGDAO::getNodoCaratteristichePrjNG] END");
	    }

	}

	public static List<TipologiaInterventoVO> getTipologiaInterventoList(String idDomanda, String dataInvio, Logger logger) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String query = QUERY_TIPOLOGIA_INTERVENTI;
		
		if (StringUtils.isBlank(dataInvio)) {
			query += " (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE)) and ";
		} else {
			query += " (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
		}
		query += " b.id_domanda='" + idDomanda + "'  ORDER BY c.codice ";

		logger.info("[CaratteristicheProgettoNGDAO::getTipologiaInterventoList] (idDomanda:" + Utils.quote(idDomanda) + ") query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaInterventoDaoDto.class));

			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoDaoDto documento : datiTipolBeneficiarioList) {

					TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
					docTmp.setTipologiaIntervento(documento.getTipologiaIntervento());
					Integer idCampoTipoInterventoInt = documento.getIdCampoTipoIntervento();
					String idCampoTipoIntervento = idCampoTipoInterventoInt == null ? "" : idCampoTipoInterventoInt.toString();
					docTmp.setIdCampoTipoIntervento(idCampoTipoIntervento);
					docTmp.setCodCampoTipoIntervento(documento.getCodCampoTipoIntervento());
					docTmp.setDescrCampoTipoIntervento(documento.getDescrCampoTipoIntervento());
					docTmp.setFlagObbligatorio(documento.getFlagObbligatorio());
					docTmp.setChecked("false");
					docTmp.setNumDettagli("0");
					datiTipologiaInterventoList.add(docTmp);
				}
			}

			return datiTipologiaInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	/**
	 * 
	 * @param idDomanda
	 * @param dataInvio
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<TipologiaInterventoVO> getTipologiaInterventoList2(String idDomanda, String dataInvio, Logger logger) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String query = QUERY_TIPOLOGIA_INTERVENTI;
		if (StringUtils.isBlank(dataInvio)) {
			query += "     (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE)) and ";
		} else {
			query += "     (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy'))) and ";
		}
		query += " b.id_domanda='" + idDomanda + "'";
		query += " AND a.id_tipol_intervento NOT IN ( select a.id_tipol_intervento from ";
		query += " findom_r_bandi_tipol_interventi a where id_tipol_intervento = '96')";
		query += " ORDER BY c.codice;";

		logger.debug("[CaratteristicheProgettoNGDAO::getTipologiaInterventoList2] (idDomanda:" + Utils.quote(idDomanda)
				+ ") query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters,
					new BeanPropertyRowMapper<>(TipologiaInterventoDaoDto.class));

			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoDaoDto documento : datiTipolBeneficiarioList) {

					TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					
					//  - Jira 1321 - inizio
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					
					docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
					docTmp.setTipologiaIntervento(documento.getTipologiaIntervento());

					Integer idCampoTipoInterventoInt = documento.getIdCampoTipoIntervento();
					String idCampoTipoIntervento = idCampoTipoInterventoInt == null ? ""
							: idCampoTipoInterventoInt.toString();
					docTmp.setIdCampoTipoIntervento(idCampoTipoIntervento);

					docTmp.setCodCampoTipoIntervento(documento.getCodCampoTipoIntervento());
					docTmp.setDescrCampoTipoIntervento(documento.getDescrCampoTipoIntervento());
					docTmp.setFlagObbligatorio(documento.getFlagObbligatorio());
					docTmp.setChecked("false");
					docTmp.setNumDettagli("0");
					datiTipologiaInterventoList.add(docTmp);
				}
			}

			return datiTipologiaInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	/**
	 * Jira: 1410
	 * Sistema neve
	 * Se beneficiario risulta Grandi stazioni flagpubblicoprivato = 1
	 * non deve poter selezionare Cat.C: idTipoIntervento 99
	 * filtrare anche i dettagli...
	 * 
	 * @param idDomanda
	 * @param dataInvio
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<TipologiaInterventoVO> getTipologiaInterventoList3(String idDomanda, String dataInvio, Logger logger) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String query = QUERY_TIPOLOGIA_INTERVENTI_AND_DETTAGLI;
		if (StringUtils.isBlank(dataInvio)) {
			query += " (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE)) and ";
		} else {
			query += " (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy'))) and ";
		}
		query += " b.id_domanda='" + idDomanda + "'";
		query += " AND a.id_tipol_intervento NOT IN ('99')";
		query += " AND e.id_tipol_intervento IN ('97', '98')";
		query += " AND e.id_dett_tipol_intervento IN ('67', '69')";
		query += " ORDER BY c.codice;";

		logger.info("[CaratteristicheProgettoNGDAO::getTipologiaInterventoList3] (idDomanda:" + Utils.quote(idDomanda)
				+ ") query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters,
					new BeanPropertyRowMapper<>(TipologiaInterventoDaoDto.class));

			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoDaoDto documento : datiTipolBeneficiarioList) 
				{
					TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
					docTmp.setTipologiaIntervento(documento.getTipologiaIntervento());

					Integer idCampoTipoInterventoInt = documento.getIdCampoTipoIntervento();
					String idCampoTipoIntervento = idCampoTipoInterventoInt == null ? "" : idCampoTipoInterventoInt.toString();
					docTmp.setIdCampoTipoIntervento(idCampoTipoIntervento);
					docTmp.setCodCampoTipoIntervento(documento.getCodCampoTipoIntervento());
					docTmp.setDescrCampoTipoIntervento(documento.getDescrCampoTipoIntervento());
					docTmp.setFlagObbligatorio(documento.getFlagObbligatorio());
					docTmp.setChecked("false");
					docTmp.setNumDettagli("0");
					datiTipologiaInterventoList.add(docTmp);
				}
			}

			return datiTipologiaInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}

	public static List<TipologiaDettaglioInterventoVO> getDettaglioInterventoList(Integer idTipolIntervento)
			throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idTipolIntervento", idTipolIntervento, Types.NUMERIC);
		try {
			List<TipologiaDettaglioInterventoVO> dettaglioInterventoList = jdbcTemplate.query(
					QUERY_DETTAGLIO_INTERVENTO, namedParameters,
					new BeanPropertyRowMapper<>(TipologiaDettaglioInterventoVO.class));

			return dettaglioInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	
	/** Jira: 1521 -: Bando-VoucherIR Tipologie intervento condizionate dal beneficiario - inizio */
	public static List<TipologiaInterventoVO> getTipologiaInterventoListCustom(String idDomanda, String dataInvio, String tipoBeneficiario, Logger logger) throws CommonalityException 
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		logger.info("debug: tipo beneficiario: " + tipoBeneficiario);
		
		String query = QUERY_TIPOLOGIA_INTERVENTI_CUSTOM;
		
		if (StringUtils.isBlank(dataInvio)) {
			query += " (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE)) and ";
		} else {
			query += " (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy'))) and ";
		}
		
		query += " b.id_domanda='" + idDomanda +"'";
		
		if( tipoBeneficiario != null && !tipoBeneficiario.isEmpty() && tipoBeneficiario.equalsIgnoreCase("SME")){
			query += " AND a.id_tipol_intervento NOT IN ('113')";
		}else{
			query += " AND a.id_tipol_intervento IN ('113')";
		}
		
		query += " ORDER BY c.codice ";
		
		logger.info("[CaratteristicheProgettoNGDAO::getTipologiaInterventoListCustom] (idDomanda:" + Utils.quote(idDomanda)	+ ") query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaInterventoDaoDto.class));

			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoDaoDto documento : datiTipolBeneficiarioList) {

					TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
					docTmp.setTipologiaIntervento(documento.getTipologiaIntervento());

					Integer idCampoTipoInterventoInt = documento.getIdCampoTipoIntervento();
					String idCampoTipoIntervento = idCampoTipoInterventoInt == null ? "" : idCampoTipoInterventoInt.toString();
					docTmp.setIdCampoTipoIntervento(idCampoTipoIntervento);
					docTmp.setCodCampoTipoIntervento(documento.getCodCampoTipoIntervento());
					docTmp.setDescrCampoTipoIntervento(documento.getDescrCampoTipoIntervento());
					docTmp.setFlagObbligatorio(documento.getFlagObbligatorio());
					docTmp.setChecked("false");
					docTmp.setNumDettagli("0");
					datiTipologiaInterventoList.add(docTmp);
				}
			}

			return datiTipologiaInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	/** Jira: 1521 -: Bando-VoucherIR Tipologie intervento condizionate dal beneficiario - fine 
	 * @throws CommonalityException */

	public static boolean getFlagSchedaProgetto(Integer idBando, Logger logger) throws CommonalityException {
		logger.debug("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] BEGIN");
		Boolean flag = false;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		
		logger.debug("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] idBando="+idBando);
		logger.debug("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] query="+QUERY_FLAG_PROGETTO);
	    try {

	    	flag = (Boolean) jdbcTemplate.queryForObject(QUERY_FLAG_PROGETTO, namedParameters, Boolean.class);
	    	
	    	logger.debug("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] flag=["+flag+"]");

	    } catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] eccezione EmptyResultDataAccessException");
		} catch (DataAccessException e) {
	    	logger.warn("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] eccezione DataAccessException");
	        throw new CommonalityException(e);
	    } finally{
	    	logger.debug("[CaratteristicheProgettoNGDAO::getFlagSchedaProgetto] END");
	    }
	    return flag;
	}

	public static List<Integer> getTipologieInterventoAssociateBando(Integer idBando, Logger logger) throws CommonalityException {
		
		logger.debug("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] BEGIN");
		List<Integer >lista = null;
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idBando", idBando, Types.NUMERIC);
		
		logger.debug("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] idBando="+idBando);
		logger.debug("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] query="+QUERY_TIPOLOG_INTERV_ASSOCIATE);
		
		try{
			lista = jdbcTemplate.queryForList(QUERY_TIPOLOG_INTERV_ASSOCIATE,namedParameters, Integer.class);
			if(lista!=null)
				logger.debug("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] lista="+lista.size());
		} catch (EmptyResultDataAccessException e) {
	    	logger.debug("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] eccezione EmptyResultDataAccessException");
		} catch (DataAccessException e) {
	    	logger.warn("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] eccezione DataAccessException");
	        throw new CommonalityException(e);
	    } finally{
	    	logger.debug("[CaratteristicheProgettoNGDAO::getTipologieInterventoAssociateBando] END");
	    }
		
		return lista;
	}
	
	
	/** Gestione Bonus Piemonte covid19 */
	public static String getImportoContributoCovid(String cfBeneficiario, Logger logger) throws CommonalityException {
		
		final String logprefix = "[CaratteristicheProgettoNGDAO:: getImportoContributoCovid] ";
		
		logger.info(logprefix + " BEGIN");
		
		String impContributoRis = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("cfBeneficiario", cfBeneficiario, java.sql.Types.VARCHAR);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_COVID, params);
			if (row!=null && row.next()){
				impContributoRis = row.getString("importo_contributo");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		
		return impContributoRis;
	}
	
	
	
	/** Jira: 1977 - Gestione importo by codice identificativo regionale presente in tabella: findom_t_bonus_turismo - */
	public static String getImportoContributoByCIR(String codiceRegionale, Logger logger) throws CommonalityException {
		
		final String logprefix = "[CaratteristicheProgettoNGDAO:: getImportoContributoByCIR] ";
		
		logger.info(logprefix + " BEGIN");
		
		String impContributoRis = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("codiceRegionale", codiceRegionale, java.sql.Types.VARCHAR);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_BY_CIR, params);
			if (row!=null && row.next()){
				impContributoRis = row.getString("importo_contributo");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		
		return impContributoRis;
	}
	
	
	
	/** Jira: 1977 - Recupero cir = codice identificativo regionale del turismo by idDomanda - */
	public static String getCodeRegionaleByIdDomanda(Integer idDomanda, Logger logger) throws CommonalityException {
		
		final String logprefix = "[CaratteristicheProgettoNGDAO:: getCodeRegionaleByIdDomanda] ";
		
		logger.info(logprefix + " BEGIN");
		
		String ris = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("idDomanda", idDomanda, java.sql.Types.NUMERIC);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_CARATT_PRJ_NG_CODICE_REGIONALE_TURISMO, params);
			if (row!=null && row.next()){
				ris = row.getString("cod_regionale");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		
		return ris;
	}
	
	
	/** Jira: 1977 - Verifico importo regionale se risulta salvato su xml by idDomanda - */
	public static String getImpCntrCodRegByIdDomanda(String idDomanda, Logger logger) throws CommonalityException {
		
		final String logprefix = "[CaratteristicheProgettoNGDAO:: getImpCntrCodRegByIdDomanda] ";
		
		logger.info(logprefix + " BEGIN");
		Integer numeroDomanda = Integer.parseInt(idDomanda);
		logger.info(logprefix + "numeroDomanda vale: "+numeroDomanda);
		String ris = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("numeroDomanda", numeroDomanda, java.sql.Types.NUMERIC);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_IMPORTO_CONTRIBUTO_CODE_REGIONALE, params);
			if (row!=null && row.next()){
				ris = row.getString("imp_regionale");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		
		return ris;
	}

	public static List<TipologiaInterventoVO> listaTipologiaInterventoNonValide(Integer idSportello, Logger logger) 
			throws CommonalityException{
		final String logprefix = "[CaratteristicheProgettoNGDAO:: isTipologiaInterventoValida] ";
		logger.info(logprefix + " BEGIN");
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
		logger.info(logprefix+"idSportello= " + idSportello);
		
		String query = "SELECT b.id_tipol_intervento as idTipoIntervento, a.codice as codTipoIntervento, a.descrizione as descrTipoIntervento, "+
						" '' as tipologiaIntervento, 0 as idCampoTipoIntervento, '' as  codCampoTipoIntervento, '' as descrCampoTipoIntervento, "+
						" '' as flagObbligatorio "+
						" FROM findom_d_tipol_interventi a "+
						" JOIN findom_r_bandi_tipol_interventi b ON a.id_tipol_intervento = b.id_tipol_intervento "+
						" JOIN findom_t_sportelli_bandi c ON b.id_bando = c.id_bando "+
						" WHERE c.id_sportello_bando = :idSportello" +
						" AND b.dt_fine <= now()::date "+
						" AND c.dt_apertura::date <= b.dt_fine "+ 
						" AND ((b.dt_fine <= c.dt_chiusura) OR c.dt_chiusura IS NULL)";
		
		logger.info(logprefix + " query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 namedParameters.addValue("idSportello", idSportello, Types.NUMERIC);

		List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoDaoDto> lista = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaInterventoDaoDto.class));

			if (lista != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoDaoDto documento : lista) {

					TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
					datiTipologiaInterventoList.add(docTmp);
				}
			}
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
		
		logger.info(logprefix + " END");
		return datiTipologiaInterventoList;
	}


	public static List<TipologiaInterventoVO> getTipologiaInterventoListNew(
			String idDomanda, Integer idSportello, String dataInvio, Logger logger) throws CommonalityException {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		logger.debug("[CaratteristicheProgettoNGDAO::getTipologiaInterventoListNew] idDomanda:"+idDomanda);
		logger.debug("[CaratteristicheProgettoNGDAO::getTipologiaInterventoListNew] idSportello:"+idSportello);
		logger.debug("[CaratteristicheProgettoNGDAO::getTipologiaInterventoListNew] dataInvio:"+dataInvio);
		
		String query = "SELECT * FROM ( " +
				" SELECT b.id_tipol_intervento as id_tipo_intervento,  " +
				" a.codice||' - '||a.descrizione as tipologia_intervento, " +
				" a.codice as cod_tipo_intervento,  " +
				" a.descrizione as descr_tipo_intervento,   " +
				"  0 as id_campo_tipo_intervento,  " +
				" '' as  cod_campo_tipo_intervento,  " +
				" '' as descr_campo_tipo_intervento,   " +
				" '' as flag_obbligatorio  , " +
				" 'true' as disabilitato " +
				" FROM findom_d_tipol_interventi a  JOIN findom_r_bandi_tipol_interventi b ON a.id_tipol_intervento = b.id_tipol_intervento   " +
				" JOIN findom_t_sportelli_bandi c ON b.id_bando = c.id_bando   " +
				" WHERE c.id_sportello_bando = :idSportello " + 
				"   AND b.dt_fine <= now()::date  " +
				"   AND c.dt_apertura::date <= b.dt_fine  " +
				"   AND ((b.dt_fine <= c.dt_chiusura) OR c.dt_chiusura IS NULL) " +
				" UNION " +
				" SELECT a.id_tipol_intervento AS id_tipo_intervento, " +
				"     c.codice||' - '||c.descrizione  AS tipologia_intervento,       " + 
				"     c.codice AS cod_tipo_intervento,        " +
				"     c.descrizione  AS descr_tipo_intervento,        " +
				"     c.id_campo_intervento  AS id_campo_tipo_intervento,    " +
				"      d.cod_campo_intervento  AS cod_campo_tipo_intervento,        " +
				"      d.descrizione   AS descr_campo_tipo_intervento,  " +
				"      a.flag_obbligatorio    AS flag_obbligatorio , " +
				"      'false' AS disabilitato " +
				" FROM findom_r_bandi_tipol_interventi a        " +
				"     JOIN  findom_v_domande_nuova_gestione b ON a.id_bando = b.id_bando       " +
				"     JOIN  findom_d_tipol_interventi c ON a.id_tipol_intervento = c.id_tipol_intervento        " +
				"     LEFT OUTER JOIN findom_d_campi_intervento d ON c.id_campo_intervento = d.id_campo_intervento   " +
				" WHERE  <REPL_1> " +
				"   and b.id_domanda=:idDomanda" +
				" ) as xx order by xx.cod_tipo_intervento";
		
		String repl_1 = "";
		if (StringUtils.isBlank(dataInvio)) {
			repl_1 = " (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) > CURRENT_DATE)) ";
		} else {
			repl_1 = " (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) ";
		}

		query = query.replace("<REPL_1>", repl_1);
		logger.info("[CaratteristicheProgettoNGDAO::getTipologiaInterventoListNew] query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
		namedParameters.addValue("idSportello", idSportello, Types.NUMERIC);
		
		List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaInterventoDaoDto.class));
			
			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoDaoDto documento : datiTipolBeneficiarioList) {

					TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
					docTmp.setTipologiaIntervento(documento.getTipologiaIntervento());
					Integer idCampoTipoInterventoInt = documento.getIdCampoTipoIntervento();
					String idCampoTipoIntervento = idCampoTipoInterventoInt == null ? "" : idCampoTipoInterventoInt.toString();
					docTmp.setIdCampoTipoIntervento(idCampoTipoIntervento);
					docTmp.setCodCampoTipoIntervento(documento.getCodCampoTipoIntervento());
					docTmp.setDescrCampoTipoIntervento(documento.getDescrCampoTipoIntervento());
					docTmp.setFlagObbligatorio(documento.getFlagObbligatorio());
					docTmp.setChecked("false");
					docTmp.setNumDettagli("0");
					docTmp.setDisabilitato(documento.getDisabilitato());
					datiTipologiaInterventoList.add(docTmp);
				}
			}

			return datiTipologiaInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}


	public static List<Integer> getListaTipolInterventoAbilitatePerBeneficiario(
			String idBando, String cfBeneficiario, String idDipartimento, Logger logger) throws CommonalityException {
		String prf = "[CaratteristicheProgettoNGDAO::getListaTipolInterventoAbilitatePerBeneficiario] ";
		logger.debug(prf + " idBando:"+idBando);
		logger.debug(prf + " cfBeneficiario:"+cfBeneficiario);
		logger.debug(prf + " idDipartimento:"+idDipartimento);
		
		String query = "SELECT sa.id_tipol_intervento FROM findom_r_bandi_soggetti_abilitati sa , findom_t_soggetti_abilitati so " +
					" WHERE sa.id_bando = :idBando " +
					" AND so.codice_fiscale = :cfBeneficiario " +
					" AND so.id_sogg_abil = sa.id_sogg_abil ";
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idBando", Integer.parseInt(idBando), Types.NUMERIC);
		params.addValue("cfBeneficiario", cfBeneficiario, Types.VARCHAR);

		if (StringUtils.isNotBlank(idDipartimento)) {
			query = query + " AND sa.id_dipartimento = :idDipartimento";
			params.addValue("idDipartimento", Integer.parseInt(idDipartimento), Types.NUMERIC);
		}
		
		logger.debug(prf + " query:"+query);
				 
		List<Integer> lista = null;

		try {
			lista = jdbcTemplate.queryForList(query, params, Integer.class);
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}

		if(lista!=null){
			logger.debug(prf + " lista.size()="+lista.size());
		}else{
			logger.debug(prf + " lista NULLA");
		}
		return lista;
	}
}
