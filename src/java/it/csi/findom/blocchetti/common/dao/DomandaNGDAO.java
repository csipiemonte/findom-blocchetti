/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.domandaNG.AttoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiSoggettoVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiStrutturaOrganizzativaVo;
import it.csi.findom.blocchetti.common.vo.domandaNG.DatiTipolBeneficiarioVo;
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

public class DomandaNGDAO {

  private static final String QUERY_DATI_TIPOL_BENEFICIARIO = "SELECT v.cod_stereotipo as codstereotipo, "
	 + "v.descr_stereotipo as descrstereotipo, v.flag_pubblico_privato||'' as flag, v.codice_tipol_beneficiario as codicetipologiabeneficiario, "
	 +  "v.descrizione_tipol_beneficiario as descrizione "
	 +  "FROM findom_v_domande_nuova_gestione v "		
	 +  "WHERE  v.id_domanda = :idDomanda" ;

  private static final String QUERY_DATI_STRUTTURA_ORGANIZZATIVA = "SELECT b.id_settore as idsettore, b.descrizione as descrsettore, "
	+  "c.id_direzione as iddirezione, c.descrizione as descrdirezione "
	+  "FROM findom_t_bandi a "	
	+  "LEFT JOIN findom_d_settori b ON (a.id_settore =  b.id_settore) "
	+  "LEFT JOIN findom_d_direzioni c ON( b.id_direzione = c.id_direzione) "	 
	+  "WHERE a.id_bando = :idBando";
	
  private static final String QUERY_FLAG_PUBBLICO_PRIVATO = "SELECT v.flag_pubblico_privato as flag "
	 +  "FROM findom_v_domande_nuova_gestione v "		
	 +  "WHERE  v.id_domanda = :idDomanda" ;
  
  private static final String QUERY_COD_STEREOTIPO = "SELECT cod_stereotipo AS stereotipo "			 
		  + "FROM  findom_v_domande_nuova_gestione "
		  + "WHERE  id_domanda = :idDomanda ";

  private static final String QUERY_NORMATIVA = "SELECT b.normativa AS normativa "
			+  "FROM FINDOM_T_BANDI a "
			+ " LEFT JOIN FINDOM_V_CLASSIF_BANDI b ON (a.id_classificazione = b.id_azione) " 
			+  "WHERE a.id_bando = :idBando";
  
  private static final String QUERY_ATTO = "SELECT a.num_atto as numAtto,  to_char(a.dt_atto, 'dd/mm/yyyy') as dataAtto "
			+  "FROM FINDOM_T_SPORTELLI_BANDI a "	
			+  "WHERE a.id_bando = :idBando";
  
  private static final String QUERY_DATA_APERTURA_SPORTELLO = "SELECT to_char(a.dt_apertura::TIMESTAMP, 'DD-MM-YYYY') as dataAperturaSportello "
			+  "FROM findom_t_sportelli_bandi a "	
			+  "WHERE id_bando = :idBando";
  
  private static final String QUERY_ORA_APERTURA_SPORTELLO = "SELECT to_char(a.dt_apertura::TIMESTAMP, 'HH24:MI:SS') as oraAperturaSportello "
			+  "FROM findom_t_sportelli_bandi a "	
			+  "WHERE id_bando = :idBando";
  
  private static final String QUERY_DATA_CHIUSURA_SPORTELLO = "SELECT to_char(a.dt_chiusura::TIMESTAMP, 'DD-MM-YYYY') as dataChiusuraSportello "
			+  "FROM findom_t_sportelli_bandi a "	
			+  "WHERE id_bando = :idBando";
  
  private static final String QUERY_ORA_CHIUSURA_SPORTELLO = "SELECT to_char(a.dt_chiusura::TIMESTAMP, 'HH24:MI:SS') as oraChiusuraSportello "
			+  "FROM findom_t_sportelli_bandi a "	
			+  "WHERE id_bando = :idBando";
  
  private static final String QUERY_DATI_SOGGETTO = "SELECT id_soggetto AS idSoggetto, cod_fiscale AS codiceFiscale, denominazione AS denominazione,  "
  		+ "id_forma_giuridica AS idFormaGiuridica, cognome AS cognome,  nome AS nome, cod_ufficio AS codiceUnitaOrganizzativa, sigla_nazione AS siglaNazione "
  		+  "FROM shell_t_soggetti  "	
		+  "WHERE id_soggetto = :idSoggetto";
  
  
  
  /**
   * : Jira 1410 -2R-
   * Recuperare codice tipologia beneficiario
   * di Sistema neme in base alla idDomanda
   * 
   */
  private static final String QUERY_CODICE_TIPO_BENEFICIARIO = "SELECT v.codice_tipol_beneficiario as codicetipologiabeneficiario "			 
		  + "FROM  findom_v_domande_nuova_gestione v "
		  + "WHERE  v.id_domanda = :idDomanda ";
  
  public static DatiTipolBeneficiarioVo getDatiTipolBeneficiario(String idDomanda) throws CommonalityException {

    DatiTipolBeneficiarioVo datiTipolBeneficiario = new DatiTipolBeneficiarioVo();

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

    try {
    	 List<DatiTipolBeneficiarioVo> datiTipolBeneficiarioList = jdbcTemplate.query(QUERY_DATI_TIPOL_BENEFICIARIO, namedParameters, new BeanPropertyRowMapper<>(DatiTipolBeneficiarioVo.class));
    	 if (datiTipolBeneficiarioList != null && !datiTipolBeneficiarioList.isEmpty()) {
    		 return datiTipolBeneficiarioList.get(0);
    	 }
    	
    	return datiTipolBeneficiario;
    }
    catch (DataAccessException e) {
      throw new CommonalityException(e);
    }

  }

  public static DatiStrutturaOrganizzativaVo getDatiStrutturaOrganizzativa(String idBando) throws CommonalityException {

	  DatiStrutturaOrganizzativaVo datiStrutturaOrganizzativa = new DatiStrutturaOrganizzativaVo();

	  NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	  MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	  namedParameters.addValue("idBando", idBando, Types.NUMERIC);

	  try {
		  List<DatiStrutturaOrganizzativaVo> datiStrutturaOrganizzativaList = jdbcTemplate.query(QUERY_DATI_STRUTTURA_ORGANIZZATIVA, namedParameters, new BeanPropertyRowMapper<>(DatiStrutturaOrganizzativaVo.class));

		  if (datiStrutturaOrganizzativaList != null && !datiStrutturaOrganizzativaList.isEmpty()) {
			  return datiStrutturaOrganizzativaList.get(0);
		  }

		  return datiStrutturaOrganizzativa;
	  }
	  catch (DataAccessException e) {
		  throw new CommonalityException(e);
	  }

  }
  
  public static int getFlagPubblicoPrivato(Integer idDomanda, Logger logger) throws CommonalityException {

		//boolean flagPubblico = false;  
	  	Integer flagPubblicoPrivato = null;
		
		logger.debug("[DomandaNGDAO::getFlagPubblicoPrivato] getFlagPubblicoPrivato() query:" + QUERY_FLAG_PUBBLICO_PRIVATO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

	    try {
	    	flagPubblicoPrivato = (Integer)jdbcTemplate.queryForObject(QUERY_FLAG_PUBBLICO_PRIVATO, namedParameters, Integer.class);
	    }
	    catch (DataAccessException e) {
	      logger.debug("[DomandaNGDAO::getFlagPubblicoPrivato] DataAccessException:" + e.getMessage());
	    }
	    
	    return flagPubblicoPrivato.intValue();
	    
	}
  
  
  public static String getStereotipoImpresa(Integer idDomanda, Logger logger) throws CommonalityException {

		//boolean flagPubblico = false;  
	  	String rval = "";
		
		logger.debug("[DomandaNGDAO::getStereotipoImpresa] getStereotipoImpresa() query:" + QUERY_COD_STEREOTIPO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

	    try {
	    	rval = jdbcTemplate.queryForObject(QUERY_COD_STEREOTIPO, namedParameters, String.class);
	    }
	    catch (DataAccessException e) {
	      logger.debug("[DomandaNGDAO::getFlagPubblicoPrivato] DataAccessException:" + e.getMessage());
	    }
	    
	    return rval;
	    
	}
  
  
  /**
   * Jira: 1410
   * Tipo beneficiario:
   * 
   * @param idBando
   * @param logger
   * @return
   * @throws CommonalityException
   */
  public static String getCodiceTipoBeneficiario(Integer idDomanda, Logger logger) throws CommonalityException {

	  	String rval = "";
		
		logger.info("[DomandaNGDAO::getCodiceTipoBeneficiario] getCodiceTipoBeneficiario() query:" + QUERY_CODICE_TIPO_BENEFICIARIO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idDomanda", idDomanda, Types.NUMERIC);

	    try {
	    	rval = jdbcTemplate.queryForObject(QUERY_CODICE_TIPO_BENEFICIARIO, namedParameters, String.class);
	    }
	    catch (DataAccessException e) {
	      logger.debug("[DomandaNGDAO::getCodiceTipoBeneficiario] DataAccessException:" + e.getMessage());
	    }
	    
	    return rval;
	    
	}
  
  
  public static String getNormativa(String idBando, Logger logger) throws CommonalityException {
		 
	  	String rval = "";
		
		logger.debug("[DomandaNGDAO::getNormativa] query:" + QUERY_NORMATIVA);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_NORMATIVA, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("normativa");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[DomandaNGDAO::getNormativa] si e' verificata una DataAccessException ");
	         throw new CommonalityException(e);
	    }
	    return rval;
	}
  
  public static AttoVO getAtto(String idBando, Logger logger) throws CommonalityException {

	  AttoVO attoVO = new AttoVO();
	  logger.debug("[DomandaNGDAO::getAtto] query:" + QUERY_ATTO);
	  NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	  MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	  namedParameters.addValue("idBando", idBando, Types.NUMERIC);

	  try {
		  List<AttoVO> attoVOList = jdbcTemplate.query(QUERY_ATTO, namedParameters, new BeanPropertyRowMapper<>(AttoVO.class));

		  if (attoVOList != null && !attoVOList.isEmpty()) {
			  attoVO =  attoVOList.get(0);
		  }

		  return attoVO;
	  }
	  catch (DataAccessException e) {
		  logger.error("[DomandaNGDAO::getAtto] si e' verificata una DataAccessException ");
		  throw new CommonalityException(e);
	  }

  }

  public static String getDataAperturaSportello(String idBando, Logger logger) throws CommonalityException {
	
	  String rval = "";
		
		logger.debug("[DomandaNGDAO::getDataAperturaSportello] query:" + QUERY_DATA_APERTURA_SPORTELLO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_APERTURA_SPORTELLO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("dataAperturaSportello");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[DomandaNGDAO::getDataAperturaSportello] si e' verificata una DataAccessException ");
	    	throw new CommonalityException(e);
	    }
	    return rval;
  }

  public static String getOraAperturaSportello(String idBando, Logger logger) throws CommonalityException {

	  String rval = "";

	  logger.debug("[DomandaNGDAO::getOraAperturaSportello] query:" + QUERY_ORA_APERTURA_SPORTELLO);

	  NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	  MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	  namedParameters.addValue("idBando", idBando, Types.NUMERIC);

	  try {
		  SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_ORA_APERTURA_SPORTELLO, namedParameters);
		  if (row!=null && row.next()){
			  rval =  row.getString("oraAperturaSportello");
		  }
	  }
	  catch (EmptyResultDataAccessException e) {}
	  catch (DataAccessException e) {
		  logger.error("[DomandaNGDAO::getOraAperturaSportello] si e' verificata una DataAccessException ");
		  throw new CommonalityException(e);
	  }
	  return rval;
  }

  public static String getDataChiusuraSportello(String idBando, Logger logger) throws CommonalityException {
		
		String rval = "";
		
		logger.debug("[DomandaNGDAO::getDataChiusuraSportello] query:" + QUERY_DATA_CHIUSURA_SPORTELLO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_DATA_CHIUSURA_SPORTELLO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("dataChiusuraSportello");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[DomandaNGDAO::getDataChiusuraSportello] si e' verificata una DataAccessException ");
	         throw new CommonalityException(e);
	    }
	    return rval;
	}

	public static String getOraChiusuraSportello(String idBando, Logger logger) throws CommonalityException {

		String rval = "";
		
		logger.debug("[DomandaNGDAO::getOraChiusuraSportello] query:" + QUERY_ORA_CHIUSURA_SPORTELLO);
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    
	    try {
	    	SqlRowSet row = jdbcTemplate.queryForRowSet(QUERY_ORA_CHIUSURA_SPORTELLO, namedParameters);
	    	if (row!=null && row.next()){
	    		rval =  row.getString("oraChiusuraSportello");
	    	}
	    }
	    catch (EmptyResultDataAccessException e) {}
	    catch (DataAccessException e) {
	    	logger.error("[DomandaNGDAO::getOraChiusuraSportello] si e' verificata una DataAccessException ");
	         throw new CommonalityException(e);
	    }
	    return rval;
	}
	/**
	 * restituisce i dati di un soggetto dato il suo id
	 * @param idSoggetto
	 * @return
	 * @throws CommonalityException
	 */
	public static DatiSoggettoVO getDatiSoggetto(Integer idSoggetto) throws CommonalityException {

		DatiSoggettoVO datiSoggettoVO = new DatiSoggettoVO();

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("idSoggetto", idSoggetto, Types.INTEGER);

		try {
			List<DatiSoggettoVO> datiSoggettoVOList = jdbcTemplate.query(QUERY_DATI_SOGGETTO, namedParameters, new BeanPropertyRowMapper<>(DatiSoggettoVO.class));

			if (datiSoggettoVOList != null && !datiSoggettoVOList.isEmpty()) {
				return datiSoggettoVOList.get(0);
			}

			return datiSoggettoVO;
		}
		catch (DataAccessException e) {
			throw new CommonalityException(e);
		}

	}
	
	


}
