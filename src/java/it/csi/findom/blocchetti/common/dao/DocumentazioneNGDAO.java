/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoVO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaDocumentoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.text.MessageFormat;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DocumentazioneNGDAO extends BaseDao
{
	private static final String QUERY_TIPOLOGIA_DOCUMENTAZIONE_NG = "SELECT ra.id_allegato as idallegato, "
			+ "ra.flag_obbligatorio as flag_obbl, " // + "ra.dt_inizio as dt_inizio, ra.dt_fine as dt_fine, "
			+ "al.descrizione as descrizione , ra.flag_differibile as differibile " + " FROM findom_r_bandi_allegati ra , findom_d_allegati al " + " WHERE "
			+ "ra.id_bando = :templateId "
			+ " AND (date_trunc('day',ra.dt_fine) >= CURRENT_DATE or ra.dt_fine is null)  AND ra.id_allegato=al.id_allegato ";

	private static final String QUERY_TIPOLOGIA_ALLEGATO_NG = "SELECT ra.id_allegato as idallegato, "
			+ "ra.flag_obbligatorio as flag_obbl, "
			+ "al.descrizione as descrizione, ra.flag_differibile as differibile " 
			+ "FROM findom_r_bandi_allegati ra , findom_d_allegati al " 
			+ "WHERE ra.id_bando = :templateId "
			+ "AND (date_trunc('day',ra.dt_fine) >= CURRENT_DATE or ra.dt_fine is null) AND ra.id_allegato=al.id_allegato ";
	
	// ------------------------------------------------------------------------------- : 2r -  - inizio
	private static final String QUERY_NUMERO_ALLEGATI_SUPPLEMENTARI = "SELECT COUNT(*) as numeroAllegati "
			 + "FROM findom_t_allegati_sportello a "		
			 + "WHERE id_sportello_bando = :idSportelloBando"
			 + " AND id_tipol_beneficiario = ("
			 + "SELECT id_tipol_beneficiario "
			 + "FROM shell_t_domande "
			 + "WHERE id_domanda= :idDomanda"
			 +")";
	
	private static final String QUERY_ID_TIPOLOGIA_BENEFICIARIO = "SELECT id_tipol_beneficiario "
			 + "FROM shell_t_domande "
			 + "WHERE id_domanda= :idDomanda"
			 +";";
	// ------------------------------------------------------------------------------- : 2r -  - fine
	
	// ------------------------------------------------------------------------------- : 2r -  - inizio
	private static final String QUERY_ELENCO_ALLEGATO_SUPPLEMENTARE_NG = "SELECT a.id_allegato as idallegato, b.descrizione "
			+"FROM findom_t_allegati_sportello a,  findom_d_allegati b "
			+"WHERE a.id_sportello_bando= :idSportello "
			+"AND a.id_tipol_beneficiario= :idTipolBeneficiario "
			+"AND a.id_allegato = b.id_allegato "
			+"AND a.flag_obbligatorio = 'S' "
			+";";
	// ------------------------------------------------------------------------------- : 2r -  - fine
	
	// : UNION
	private static final String QUERY_TIPOLOGIA_ALLEGATO_INTEGRATIVO_COMPLETO_NG = 
			"(SELECT ra.id_allegato as idallegato, "			
		  + "ra.flag_obbligatorio as flag_obbl, "			
		  + "al.descrizione as descrizione, "
		  + "ra.flag_differibile as differibile "
		  + "FROM findom_r_bandi_allegati ra, findom_d_allegati al "
		  + "WHERE ra.id_bando = :templateId "
		  + "AND (date_trunc('day',ra.dt_fine) >= CURRENT_DATE or ra.dt_fine is null) "
		  + "AND ra.id_allegato=al.id_allegato "
		  + "AND ra.flag_obbligatorio = 'S' "
		  + "order by ra.id_allegato) "
		  + "UNION "
		  + "(SELECT al.id_allegato as idallegato, "
		  + "b.flag_obbligatorio as flag_obbl, "
		  + "al.descrizione as descrizione, "
		  + "b.flag_differibile as differibile "
		  + "FROM findom_t_allegati_sportello b, findom_d_allegati al "
		  + "WHERE b.id_sportello_bando = :idSportello "
		  + "AND b.id_tipol_beneficiario = ( "
		  + "SELECT sd.id_tipol_beneficiario as id_tipol_beneficiario "
		  + "FROM shell_t_domande sd "
		  + "WHERE id_domanda= :idDomanda "
		  + "AND b.id_allegato = al.id_allegato)"
		  + "AND B.ID_TIPOL_INTERVENTO IS NULL);";

	private static final String QUERY_TIPOLOGIA_ALLEGATO_INTEGRATIVO_COMPLETO_NG_TIPOL_INTERVENTO = 
		" SELECT B.ID_ALLEGATO AS IDALLEGATO, \n" +
		" B.FLAG_OBBLIGATORIO AS FLAG_OBBL, \n" + 
		" AL.DESCRIZIONE AS DESCRIZIONE, \n" +
		" B.FLAG_DIFFERIBILE AS DIFFERIBILE, \n" +
		" B.ID_TIPOL_INTERVENTO \n" +
		" FROM FINDOM_T_ALLEGATI_SPORTELLO B, FINDOM_D_ALLEGATI AL \n" +
		" WHERE B.ID_ALLEGATO = AL.ID_ALLEGATO AND B.FLAG_OBBLIGATORIO = :flagObbligatorio \n" +
		" AND B.ID_SPORTELLO_BANDO = :idSportelloBando \n" +
		" AND B.ID_TIPOL_BENEFICIARIO IN (SELECT ID_TIPOL_BENEFICIARIO \n" +
		" FROM SHELL_T_DOMANDE \n" +
		" WHERE ID_DOMANDA = :idDomanda \n" +
		" AND ID_TIPOL_BENEFICIARIO = :idTipolBeneficiario) \n" +
		" {0} \n "+
		" ORDER BY AL.DESCRIZIONE ";

	public static List<TipologiaDocumentoVO> getTipologiaDocumentoList(Integer templateId, Boolean isObbligatorio, Logger logger) throws CommonalityException 
	{
		List<TipologiaDocumentoVO> rlist = null;
		String nomeAttributo = "tipologiaDocumentoConsList-" + isObbligatorio + "-" + templateId;
		if (SessionCache.getInstance().get(nomeAttributo) != null) {
			rlist = (List) SessionCache.getInstance().get(nomeAttributo);
			logger.info(
					"[DocumentazioneNGDAO::getTipologiaDocumentoList] getTipologiaDocumentoList() letta da sessione");
			return rlist;
		} else {
			try {
				
			
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("templateId", templateId, Types.NUMERIC);

			String query = QUERY_TIPOLOGIA_DOCUMENTAZIONE_NG;
			if (isObbligatorio) {
				query += " AND ra.flag_obbligatorio = 'S'";
			} else {
				query += " AND (ra.flag_obbligatorio IS NULL or ra.flag_obbligatorio = 'N')";
			}
			//
			rlist = jdbcTemplate.query(query,
					namedParameters, new BeanPropertyRowMapper<>(TipologiaDocumentoVO.class));

			SessionCache.getInstance().set(nomeAttributo, rlist);
			
			return rlist;
			
			} catch (DataAccessException e) {
				logger.error(
						"[DocumentazioneNGDAO::getTipologiaDocumentoList] getTipologiaDocumentoList()", e);
				throw new CommonalityException(e);
			}

		}
		

	}

	
	// ------------------------------------------------------------------------------- : 2r - inizio sostituisce la precedente per test in locale 
	/** Query di recupero documenti obbligatori custom */
	public static List<TipologiaAllegatoVO> getTipologiaAllegatoList( Integer templateId, Integer idSportello, Integer idDomanda, Boolean isObbligatorio, Logger logger )throws CommonalityException 
	{
		// valuto i parametri di ingresso
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] templateId vale: " + templateId);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] idSportello vale: " + idSportello);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] idDomanda vale: " + idDomanda);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] isObbligatorio vale: " + isObbligatorio);
		
		// salvo qui il risultato della query nuova
		List<TipologiaAllegatoVO> rlist = null;
		
		String nomeAttributo = "getTipologiaAllegatoConsList-" + isObbligatorio + "-" + templateId;
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] nomeAttributo vale: " + nomeAttributo);
		
//		if (SessionCache.getInstance().get(nomeAttributo) != null) 
//		{
//			rlist = (List) SessionCache.getInstance().get(nomeAttributo);
//			logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] getTipologiaAllegatoList() letta da sessione");
//			return rlist;
//		}
//		else 
		 if( templateId != null && idSportello != null && idDomanda != null && isObbligatorio )
		{
			try 
			{
				// esegui la query custom:
				NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("templateId", templateId, Types.NUMERIC);
				namedParameters.addValue("idSportello", idSportello, Types.NUMERIC);
				namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
				
				String query = QUERY_TIPOLOGIA_ALLEGATO_INTEGRATIVO_COMPLETO_NG;

				logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] query: " + query);
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaAllegatoVO.class));
				logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] rlist()" + rlist);
				// SessionCache.getInstance().set(nomeAttributo, rlist);
			
				return rlist;
			
			} catch (DataAccessException e) {
				logger.error("[DocumentazioneNGDAO::getTipologiaAllegatoList] getTipologiaDocumentoList()", e);
				throw new CommonalityException(e);
			}
		}	// : fine verifica per eseguire eventuale query custom ...	
		else 
		{
			try 
			{
				NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("templateId", templateId, Types.NUMERIC);

				String query = QUERY_TIPOLOGIA_ALLEGATO_NG;
				if (isObbligatorio) {
					// query += "AND ra.flag_obbligatorio " + (isObbligatorio ? " = 'S' " : " IS
					// NULL ");
					query += " AND ra.flag_obbligatorio = 'S'";
				}
				query += " order by al.descrizione ";
				//
				logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] query: " + query);
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaAllegatoVO.class));
				logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] rlist()" + rlist);
				// SessionCache.getInstance().set(nomeAttributo, rlist);
			
				return rlist;
			
			} catch (DataAccessException e) {
				logger.error("[DocumentazioneNGDAO::getTipologiaAllegatoList] getTipologiaDocumentoList()", e);
				throw new CommonalityException(e);
			}
		}
	}// fine metodo getTipologiaAllegatoList()
	// ------------------------------------------------------------------------------- :  - 2r - fine
	
	
	
	// ------------------------------------------------------------------------------------------------------------------- : 2r - inizio
	/**
	 * : Test conteggio numero allegati integrativi
	 * 
	 * @param idSportelloBando
	 * @param idDomanda
	 * @param logger
	 * @return
	 * @throws CommonalityException
	 */
	public static int getIdTipologiaBeneficiario(Integer idDomanda, Logger logger) throws CommonalityException {

	  	Integer idTipologiaBeneficiario = null;
		
		logger.info("[DocumentazioneNGDAO::getIdTipologiaBeneficiario] getIdTipologiaBeneficiario() query:" + QUERY_ID_TIPOLOGIA_BENEFICIARIO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    // namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);

	    try {
	    	idTipologiaBeneficiario = (Integer)jdbcTemplate.queryForObject(QUERY_ID_TIPOLOGIA_BENEFICIARIO, namedParameters, Integer.class);
	    }
	    catch (DataAccessException e) {
	      logger.info("[DocumentazioneNGDAO::getIdTipologiaBeneficiario] DataAccessException:" + e.getMessage());
	    }
	    
	    return idTipologiaBeneficiario.intValue();
	    
	}
	// ------------------------------------------------------------------------------------------------------------------- : 2r - fine 
	
	// ------------------------------------------------------------------------------------------------------------------- : 2r - inizio
	public static int getNumeroAllegatiSupplementari(Integer idSportelloBando, Integer idDomanda, Logger logger) throws CommonalityException {

	  	Integer numeroAllegatiIntegrativi = null;
		
		logger.info("[DocumentazioneNGDAO::getNumeroAllegatiIntegrativi] getNumeroAllegatiIntegrativi() query:" + QUERY_NUMERO_ALLEGATI_SUPPLEMENTARI);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
	    namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);

	    try {
	    	numeroAllegatiIntegrativi = (Integer)jdbcTemplate.queryForObject(QUERY_NUMERO_ALLEGATI_SUPPLEMENTARI, namedParameters, Integer.class);
	    }
	    catch (DataAccessException e) {
	      logger.info("[DocumentazioneNGDAO::getNumeroAllegatiIntegrativi] DataAccessException:" + e.getMessage());
	    }
	    
	    return numeroAllegatiIntegrativi.intValue();
	    
	}
	// ------------------------------------------------------------------------------------------------------------------- : 2r - fine

	// ------------------------------------------------------------------------------------------------------------------- : 2r - inizio
	public static List<TipologiaAllegatoVO> getTipologiaAllegatoSupplementareList(Integer idSportello, Integer idTipolBeneficiario, Logger logger) throws CommonalityException {
		
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoSupplementareList] idSportello vale: " + idSportello);
		List<TipologiaAllegatoVO> rlist = null;
		
		try 
		{
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("idSportello", idSportello, Types.NUMERIC);
			namedParameters.addValue("idTipolBeneficiario", idTipolBeneficiario, Types.NUMERIC);

			String query = QUERY_ELENCO_ALLEGATO_SUPPLEMENTARE_NG;
			logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoList] query: " + query);
			rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaAllegatoVO.class));
			logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoSupplementareList] rlist()" + rlist);
		
			return rlist;
		
		} catch (DataAccessException e) {
			logger.error("[DocumentazioneNGDAO::getTipologiaAllegatoSupplementareList] getTipologiaAllegatoSupplementareList()", e);
			throw new CommonalityException(e);
		}
	}
	// ------------------------------------------------------------------------------------------------------------------- : 2r - fine

	// ------------------------------------------------------------------------------------------------------------------- : 2r - inizio
	/** Query di recupero documenti obbligatori custom */
	public static List<TipologiaAllegatoVO> getTipologiaAllegatoTipolInterventoList(Integer idBando, Integer idSportelloBando, Integer idDomanda, Integer idTipolBeneficiario, List<Integer> idTipolIntervento, Logger logger) throws CommonalityException 
	{
		// valuto i parametri di ingresso
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] idBando vale: " + idBando);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] idSportelloBando vale: " + idSportelloBando);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] idDomanda vale: " + idDomanda);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] idTipolBeneficiario vale: " + idTipolBeneficiario);
		logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] idTipolIntervento vale: " + idTipolIntervento);

		if (idTipolIntervento == null || idTipolIntervento.size() == 0) return null;

		// salvo qui il risultato della query nuova
		List<TipologiaAllegatoVO> rlist = null;

		if (idBando != null && idSportelloBando != null && idDomanda != null)
		{
			try 
			{
				// eseguo la query custom:
				NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("idBando", idBando, Types.NUMERIC);
				namedParameters.addValue("idSportelloBando", idSportelloBando, Types.NUMERIC);
				namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);
				namedParameters.addValue("idTipolBeneficiario", idTipolBeneficiario, Types.NUMERIC);
				namedParameters.addValue("flagObbligatorio", "S", Types.VARCHAR);

				String query = QUERY_TIPOLOGIA_ALLEGATO_INTEGRATIVO_COMPLETO_NG_TIPOL_INTERVENTO;

				StringBuilder subQuery = new StringBuilder("");
				subQuery.append(" AND B.ID_TIPOL_INTERVENTO IN (");
				addIntegerToStringValues(subQuery, idTipolIntervento);
				subQuery.append(") ");

				query = new MessageFormat(query).format(new Object[] {subQuery.toString()});

				logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] query: " + query);
				rlist = jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper<>(TipologiaAllegatoVO.class));
				logger.info("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList] rlist()" + rlist);

				return rlist;
			}
			catch (DataAccessException e)
			{
				logger.error("[DocumentazioneNGDAO::getTipologiaAllegatoTipolInterventoList]", e);
				throw new CommonalityException(e);
			}
		}	// : fine verifica per eseguire eventuale query custom ...

		return rlist;
	}// fine metodo getTipologiaAllegatoList()
	// ------------------------------------------------------------------------------- :  - 2r - fine
}
