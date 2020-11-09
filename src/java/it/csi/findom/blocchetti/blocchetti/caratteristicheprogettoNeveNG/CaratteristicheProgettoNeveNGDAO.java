/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG;

import it.csi.findom.blocchetti.blocchetti.formaFinContrNeve.ValoriSportelloVO;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

// findom_d_tipol_interventi
public class CaratteristicheProgettoNeveNGDAO {

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
			+ " c.descrizione               AS descr_tipo_intervento, "
			+ " a.flag_obbligatorio         AS flag_obbligatorio, "
			+ " e.descrizione				AS dettaglio, "
			+ " e.id_dett_tipol_intervento  AS idDettaglio" 
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
	
	private static final String QUERY_DETTAGLIO_INTERVENTO_GRANDI_STAZIONI = "SELECT a.id_dett_tipol_intervento AS id_dett_intervento, "
			+ "       a.codice                   AS cod_dett_intervento, "
			+ "       a.descrizione              AS descr_dett_intervento,  "
			+ "       a.codice||' - '||a.descrizione  AS dettaglio_intervento, "
			+ "       a.id_tipol_intervento      AS id_intervento_dettaglio, "
			+ "       a.id_campo_intervento      AS id_campo_dett_intervento, "
			+ "       b.cod_campo_intervento     AS cod_campo_dett_intervento, "
			+ "       b.descrizione              AS descr_campo_dett_intervento "
			+ " FROM findom_d_dett_tipol_interventi a "
			+ " LEFT OUTER JOIN  findom_d_campi_intervento b  ON a.id_campo_intervento = b.id_campo_intervento "
			+ " WHERE a.id_tipol_intervento = :idTipolIntervento "
			+ " AND a.id_dett_tipol_intervento = :idDettTipolIntervento "
			+ " order by a.id_tipol_intervento, a.codice ";
	
	
	/**
	 * :  - sistema neve : 
	 */
	private static final String QUERY_CONTRIBUTO_INTERVENTO = "select id_tipol_intervento AS idTipolIntervento, "
			+ " importo_massimo_erogabile AS importoMassimoErogabile, "
			+ " perc_max_contributo_erogabile AS percMaxContributoErogabile "
			+ " FROM findom_t_valori_sportello "
			+ " WHERE id_sportello_bando = :idSportello" 
			+ " AND id_tipol_beneficiario = :idCodiceBeneficiario" 
			+ " ORDER BY id_tipol_intervento ";
	

	/**
	 *  : per Sistema Neve 
	 * Deve restituire id_tipol_beneficiario
	 * in base a:
	 * - id_sportello_bando
	 * - codice
	 */
	private static final String QUERY_ID_TIPOL_BENEFICIARIO = "select distinct(a.id_tipol_beneficiario) AS idTipolBeneficiario "
			+ " FROM findom_t_valori_sportello a "
			+ " JOIN  findom_d_tipol_beneficiari b ON a.id_tipol_beneficiario = b.id_tipol_beneficiario "
			+ " WHERE a.id_sportello_bando = :idSportello" 
			+ " AND b.codice = :idCodiceBeneficiario" 
			+ " ORDER BY a.id_tipol_beneficiario ";
	
	

	public static List<TipologiaInterventoNeveVO> getTipologiaInterventoList(String idDomanda, String dataInvio, Logger logger) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String query = QUERY_TIPOLOGIA_INTERVENTI;
		if (StringUtils.isBlank(dataInvio)) {
			query += " (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE)) and ";
		} else {
			query += " (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio
					+ "', 'dd/MM/yyyy'))) and ";
		}
		query += " b.id_domanda='" + idDomanda + "' and c.codice||' - '||c.descrizione IS NOT NULL "+"  ORDER BY c.codice ";

		logger.info("[CaratteristicheProgettoNGDAO::getTipologiaInterventoList] (idDomanda:" + Utils.quote(idDomanda)
				+ ") query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<TipologiaInterventoNeveVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoNeveDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters,
					new BeanPropertyRowMapper<>(TipologiaInterventoNeveDaoDto.class));
			
			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoNeveDaoDto documento : datiTipolBeneficiarioList) {

					TipologiaInterventoNeveVO docTmp = new TipologiaInterventoNeveVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					
					//  - Jira 1321 - inizio
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					docTmp.setIdDettaglio(documento.getIdDettaglio());
					//  - Jira 1321 - fine
					
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
	 * Jira: 1361
	 * 
	 * @param idDomanda
	 * @param dataInvio
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<TipologiaInterventoNeveVO> getTipologiaInterventoList2(String idDomanda, String dataInvio, Logger logger) throws CommonalityException {

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

		List<TipologiaInterventoNeveVO> datiTipologiaInterventoList = null;

		try {
			List<TipologiaInterventoNeveDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters,
					new BeanPropertyRowMapper<>(TipologiaInterventoNeveDaoDto.class));

			if (datiTipolBeneficiarioList != null) {
				datiTipologiaInterventoList = new ArrayList<>();
				for (TipologiaInterventoNeveDaoDto documento : datiTipolBeneficiarioList) {

					TipologiaInterventoNeveVO docTmp = new TipologiaInterventoNeveVO();
					Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
					String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
					docTmp.setIdTipoIntervento(idTipoIntervento);
					
					//  - Jira 1321 - inizio
					docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
					//  - Jira 1321 - fine
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
	
	// public static List<TipologiaInterventoVO> getTipologiaInterventoList3(String idDomanda, String dataInvio, Logger logger) throws CommonalityException 
	public static List<TipologiaInterventoNeveVO> getTipologiaInterventoList3(String idDomanda, String dataInvio, Logger logger) throws CommonalityException
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate ((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

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
		query += " AND e.id_dett_tipol_intervento IN ('67', '69')";
		query += " ORDER BY c.codice;";

		logger.info("[CaratteristicheProgettoNGDAO::getTipologiaInterventoList3] (idDomanda:" + Utils.quote(idDomanda)
				+ ") query:" + query);

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		List<TipologiaInterventoNeveVO> datiTipologiaInterventoList = null;
		// List<TipologiaInterventoVO> datiTipologiaInterventoList = null;

		try {
				List<TipologiaInterventoNeveDaoDto> datiTipolBeneficiarioList = jdbcTemplate.query(query, namedParameters,	new BeanPropertyRowMapper<>(TipologiaInterventoNeveDaoDto.class));
	
				if (datiTipolBeneficiarioList != null) 
				{
					datiTipologiaInterventoList = new ArrayList<>();
					for (TipologiaInterventoNeveDaoDto documento : datiTipolBeneficiarioList) 
					{
						TipologiaInterventoNeveVO docTmp = new TipologiaInterventoNeveVO();
						// TipologiaInterventoVO docTmp = new TipologiaInterventoVO();
						
						Integer idTipoInterventoInt = (Integer) documento.getIdTipoIntervento();
						String idTipoIntervento = idTipoInterventoInt == null ? "" : idTipoInterventoInt.toString();
						docTmp.setIdTipoIntervento(idTipoIntervento);
						
						docTmp.setCodTipoIntervento(documento.getCodTipoIntervento());
						docTmp.setDescrTipoIntervento(documento.getDescrTipoIntervento());
						docTmp.setTipologiaIntervento(documento.getTipologiaIntervento());
						
						//  - Sistema neve inizio 
						logger.info("Dettaglio: " + documento.getDettaglio());
						docTmp.setDettaglio(documento.getDettaglio());
						docTmp.setIdDettaglio(documento.getIdDettaglio());
						//  - Sistema neve fine
						
	
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
		} 
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}

	public static List<TipologiaDettaglioInterventoNeveVO> getDettaglioInterventoList(Integer idTipolIntervento)
			throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idTipolIntervento", idTipolIntervento, Types.NUMERIC);
		
		try {
			List<TipologiaDettaglioInterventoNeveVO> dettaglioInterventoList = jdbcTemplate.query(QUERY_DETTAGLIO_INTERVENTO, namedParameters,	new BeanPropertyRowMapper<>(TipologiaDettaglioInterventoNeveVO.class));

			return dettaglioInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	/**
	 * Query per Grandi stazioni
	 * 	con solo dettaglio: Sci da discesa
	 * 	se idDettaglio= 97 or 98
	 * id_tipol_intervento 		= :idTipolIntervento 		- idTipolIntervento
	 * id_dett_tipol_intervento = :idDettTipolIntervento	- idDettaglio
	 * 
	 * @param idTipolIntervento
	 * @return
	 * @throws CommonalityException
	 */
	public static List<TipologiaDettaglioInterventoNeveVO> getDettaglioInterventoGrdStzList(Integer idTipolIntervento, Integer idDettTipolIntervento)
			throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idTipolIntervento", idTipolIntervento, Types.NUMERIC);
		namedParameters.addValue("idDettTipolIntervento", idDettTipolIntervento, Types.NUMERIC);
		
		try {
			List<TipologiaDettaglioInterventoNeveVO> dettaglioInterventoList = jdbcTemplate.query(QUERY_DETTAGLIO_INTERVENTO_GRANDI_STAZIONI, namedParameters,	new BeanPropertyRowMapper<>(TipologiaDettaglioInterventoNeveVO.class));

			return dettaglioInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	/**
	 * Query per sistema neve
	 * Recupera i contributi
	 * in base alle tipologie di intervento 
	 * selezionate da utente
	 * by 
	 * @param idSportello: 73
	 * @param idTipologiaBeneficiario: 70
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static List<ValoriSportelloVO> getContributiInterventoListByIdSportello(Integer idSportello, Integer idCodiceBeneficiario, Logger logger) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idSportello", idSportello, Types.NUMERIC);
		namedParameters.addValue("idCodiceBeneficiario", idCodiceBeneficiario, Types.NUMERIC);
		
		/*
		 * :idSportello :idCodiceBeneficiario" 
		 * */
		try {
			List<ValoriSportelloVO> contributiInterventoList = jdbcTemplate.query(QUERY_CONTRIBUTO_INTERVENTO, namedParameters,	new BeanPropertyRowMapper<>(ValoriSportelloVO.class));

			return contributiInterventoList;
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}
	}
	
	
	/**
	 *   
	 * @param idDomanda
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static int getIdTipolBeneficiarioByIdSportello(Integer idSportello, String idCodiceBeneficiario, Logger logger) throws CommonalityException {

		//boolean flagPubblico = false;  
	  	Integer idTipolBeneficiario = null;
		
		logger.info("[CaratteristicheProgettoNGDAO::getIdTipolBeneficiarioByIdSportello] getFlagPubblicoPrivato() query:" + QUERY_ID_TIPOL_BENEFICIARIO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idSportello", idSportello, Types.NUMERIC);
	    namedParameters.addValue("idCodiceBeneficiario", idCodiceBeneficiario, Types.VARCHAR);

	    try {
	    	idTipolBeneficiario = (Integer)jdbcTemplate.queryForObject(QUERY_ID_TIPOL_BENEFICIARIO, namedParameters, Integer.class);
	    }
	    catch (DataAccessException e) {
	      logger.debug("[CaratteristicheProgettoNGDAO::getIdTipolBeneficiarioByIdSportello] DataAccessException:" + e.getMessage());
	    }
	    
	    return idTipolBeneficiario.intValue();
	}

}
