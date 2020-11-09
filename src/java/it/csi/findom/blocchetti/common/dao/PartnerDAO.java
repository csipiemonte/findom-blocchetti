/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.abstractprogetto.AbstractProgettoCPVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.StatoDomandaVO;
import it.csi.findom.blocchetti.common.vo.operatorepresentatore.OperatorePresentatoreVo;
import it.csi.findom.blocchetti.common.vo.partner.AcronimoBandiVO;
import it.csi.findom.blocchetti.common.vo.partner.CapofilaAcronimoPartnerVO;
import it.csi.findom.blocchetti.common.vo.partner.CapofilaAcronimoVO;
import it.csi.findom.blocchetti.common.vo.partner.PartnerEliminatoVO;
import it.csi.findom.blocchetti.common.vo.partner.PartnerItemVO;
import it.csi.findom.blocchetti.common.vo.partner.SpeseProgettiComuniVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class PartnerDAO {	   	
	private static final String INSERT_PARTNER = "INSERT INTO shell_t_partner (cod_fiscale,  denominazione, sigla_nazione ,dt_attivazione, dt_disattivazione) "
			+ "VALUES ( upper(:codiceFiscale),  :denominazione, :idStato, now(), null)";
	private static final String INSERT_PARTNER_CON_DIP = "INSERT INTO shell_t_partner (cod_fiscale, cod_ufficio, denominazione, sigla_nazione ,dt_attivazione, dt_disattivazione) "
			+ "VALUES ( upper(:codiceFiscale), :codiceDipartimento, :denominazione, :idStato, now(), null)";
	
	private static final String UPDATE_PARTNER = "UPDATE shell_t_partner SET cod_fiscale = :codiceFiscale, cod_ufficio = :codiceDipartimento, denominazione = :denominazione, "
			+ " sigla_nazione = :idStato WHERE id_partner = :idPartner";
	
	private static final String SELECT_VERIFICA_ESISTENZA_PARTNER = "SELECT id_partner AS idPartner " 
			+ "FROM shell_t_partner WHERE cod_fiscale = upper(:codFiscale) AND sigla_nazione = :idStato #condizione# ";
	
	private static final String SELECT_CAPOFILA_ACRONIMO_PARTNER_BY_ID_PARTNER = "SELECT id_capofila_acronimo as idCapofilaAcronimo, id_partner AS idPartner, "
			+ "id_domanda_partner AS idDomandaPartner, dt_attivazione AS dtAttivazione, dt_disattivazione AS dtDisattivazione "
			+ "FROM shell_r_capofila_acronimo_partner WHERE id_partner = :idPartner ";
	
	private static final String INSERT_CAPOFILA_ACRONIMO_PARTNER = "INSERT INTO shell_r_capofila_acronimo_partner "
			+ "(id_capofila_acronimo, id_partner, dt_attivazione) "
			+ "VALUES ( :idCapofilaAcronimo, :idPartner, now())";
	
	private static final String INSERT_CAPOFILA_ACRONIMO_PARTNER_ALL = "INSERT INTO shell_r_capofila_acronimo_partner "
			+ "(id_capofila_acronimo, id_partner, id_domanda_partner, dt_attivazione) "
			+ "VALUES ( :idCapofilaAcronimo, :idPartner, :idDomandaPartner,now())";
	
	private static final String SELECT_VERIFICA_ESISTENZA_DOMANDA_PARTNER_PER_CAPOFILA = "SELECT id_domanda_partner AS idDomandaPartner "
			+ "FROM shell_r_capofila_acronimo_partner WHERE id_capofila_acronimo = :idCapofilaAcronimo AND id_partner = :idPartner and dt_disattivazione is null ";
	
	private static final String SELECT_VERIFICA_ESISTENZA_DOMANDA_PARTNER = "SELECT id_domanda_partner AS idDomandaPartner "
			+ "FROM shell_r_capofila_acronimo_partner WHERE id_partner = :idPartner and dt_disattivazione is null AND id_domanda_partner is not null";
		
	private static final String SELECT_STATO_DOMANDA = "SELECT a.model_id AS idDomanda, a.model_state_fk AS idStatoDomanda, b.model_state_descr AS descrStatoDomanda "
			+ "FROM aggr_t_model a, aggr_d_model_state b "
			+ "WHERE a.model_id = :idDomanda AND a.model_state_fk = b.model_state AND b.end_date is null ";
	
	private static final String DELETE_CAPOFILA_ACRONIMO_PARTNER = "DELETE FROM shell_r_capofila_acronimo_partner WHERE id_capofila_acronimo = :idCapofilaAcronimo AND id_partner = :idPartner ";
	
	private static final String UPDATE_DISATTIVA_CAPOFILA_ACRONIMO_PARTNER = "UPDATE shell_r_capofila_acronimo_partner SET dt_disattivazione = now() "
			+ ", note = :motivazione "
			+ " WHERE id_capofila_acronimo = :idCapofilaAcronimo AND id_partner = :idPartner ";
	
	private static final String UPDATE_STATO_DOMANDA = "UPDATE aggr_t_model SET model_state_fk = :statoDomanda WHERE model_id = :idDomanda";
	
	//
	private static final String SELECT_ACRONIMI_VALIDI_BY_ID_BANDO = "SELECT id_acronimo_bando AS idAcronimoBando, id_bando AS idBando, acronimo_progetto AS acronimoProgetto, "
			+ "titolo_progetto AS titoloProgetto, dt_attivazione AS dtAttivazione, dt_disattivazione AS dtDisattivazione"
			+ " FROM shell_t_acronimo_bandi WHERE id_bando = :idBando AND dt_disattivazione is null ";
	
	private static final String INSERT_ACRONIMO_BANDI = "INSERT INTO shell_t_acronimo_bandi (id_bando, acronimo_progetto, titolo_progetto, dt_attivazione, dt_disattivazione) "
			+ "VALUES ( :idBando, :acronimoProgetto, :titoloProgetto, now(), null)";
	
	private static final String SELECT_CAPOFILA_ACRONIMO_PARTNER_BY_ID_CAPOFILA_ACRONIMO = "SELECT id_capofila_acronimo as idCapofilaAcronimo, id_partner AS idPartner, "
			+ "id_domanda_partner AS idDomandaPartner, dt_attivazione AS dtAttivazione, dt_disattivazione AS dtDisattivazione, note AS note "
			+ "FROM shell_r_capofila_acronimo_partner WHERE id_capofila_acronimo = :idCapofilaAcronimo ";
	
	private static final String UPDATE_DISATTIVA_ACRONIMO_BANDO = "UPDATE shell_t_acronimo_bandi SET dt_disattivazione = now() "
			+ " WHERE id_acronimo_bando = :idAcronimoBando ";
	
	private static final String SELECT_ID_PARTNER_BY_ATTRS = "SELECT id_partner AS idPartner "
			+ "FROM shell_t_partner WHERE cod_fiscale = :codiceFiscale AND "
			+ "sigla_nazione = :siglaNazione AND dt_disattivazione is null #condizione# "; 
	
	private static final String SELECT_ACRONIMI_BY_ID_PARTNER_ID_BANDO = "SELECT c.id_acronimo_bando AS idAcronimoBando, c.id_bando AS idBando, "+
			"c.acronimo_progetto AS acronimoProgetto, c.titolo_progetto AS titoloProgetto, "+ 
	        " b.id_capofila_acronimo AS idCapofilaAcronimo "+ 
			"FROM shell_r_capofila_acronimo_partner a "+
			"JOIN shell_t_capofila_acronimo b ON a.id_capofila_acronimo = b.id_capofila_acronimo AND b.dt_disattivazione IS NULL "+
			"JOIN shell_t_acronimo_bandi c ON c.id_acronimo_bando = b.id_acronimo_bando AND c. id_bando = :idBando AND c.dt_disattivazione IS NULL "+
			"WHERE a.id_partner = :idPartner AND "+ 
			"a.id_domanda_partner is null AND "+
			"a.dt_disattivazione IS NULL ";
	
	private static final String SELECT_ACRONIMI_BY_ID_PARTNER_ID_BANDO_ID_DOMANDA = "SELECT c.id_acronimo_bando AS idAcronimoBando, c.id_bando AS idBando, "+
			"c.acronimo_progetto AS acronimoProgetto, c.titolo_progetto AS titoloProgetto, "+ 
	        " b.id_capofila_acronimo AS idCapofilaAcronimo "+ 
			"FROM shell_r_capofila_acronimo_partner a "+
			"JOIN shell_t_capofila_acronimo b ON a.id_capofila_acronimo = b.id_capofila_acronimo AND b.dt_disattivazione IS NULL "+
			"JOIN shell_t_acronimo_bandi c ON c.id_acronimo_bando = b.id_acronimo_bando AND c. id_bando = :idBando AND c.dt_disattivazione IS NULL "+
			"WHERE a.id_partner = :idPartner AND "+ 
			"(a.id_domanda_partner = :idDomandaPartner OR a.id_domanda_partner is null) AND "+ 			
			"a.dt_disattivazione IS NULL ";
	
	private static final String INSERT_CAPOFILA_ACRONIMO = "INSERT INTO shell_t_capofila_acronimo (id_domanda, id_acronimo_bando, dt_attivazione, dt_disattivazione) "
			+ "VALUES ( :idDomanda, :idAcronimoBando, now(), null)";
	
	private static final String UPDATE_DISATTIVA_CAPOFILA_ACRONIMO = "UPDATE shell_t_capofila_acronimo SET dt_disattivazione = now() "
			+ "WHERE id_capofila_acronimo = :idCapofilaAcronimo";
	
	private static final String SELECT_ACRONIMO_BANDI_BY_ID_ACRONIMO_BANDO = "SELECT id_acronimo_bando AS idAcronimoBando, id_bando AS idBando, acronimo_progetto AS acronimoProgetto, "
			+ "titolo_progetto AS titoloProgetto, dt_attivazione AS dtAttivazione, dt_disattivazione AS dtDisattivazione"
			+ " FROM shell_t_acronimo_bandi WHERE id_acronimo_bando = :idAcronimoBando ";
	
	private static final String UPDATE_DOMANDA_PARTNER_CAPOFILA_ACRONIMO_PARTNER = "UPDATE shell_r_capofila_acronimo_partner SET id_domanda_partner = :idDomandaPartner, "
			+ "dt_attivazione = now() WHERE id_capofila_acronimo = :idCapofilaAcronimo AND id_partner = :idPartner ";
	
	private static final String SELECT_CAPOFILA_ACRONIMO_BY_ID_CAPOFILA_ACRONIMO = "SELECT id_capofila_acronimo AS idCapofilaAcronimo, id_domanda AS idDomanda, "
			+ " id_acronimo_bando AS idAcronimoBando, dt_attivazione AS dtAttivazione, dt_disattivazione AS dtDisattivazione"
			+ " FROM shell_t_capofila_acronimo WHERE id_capofila_acronimo = :idCapofilaAcronimo ";
	
    private static final String SELECT_DATI_CAPOFILA_BY_ID_CAPOFILA_ACRONIMO = "SELECT c.cod_fiscale AS codiceFiscale, c.denominazione AS denominazione	"
    		+ "FROM shell_t_capofila_acronimo a	"
    		+ "JOIN shell_t_domande b ON a.id_domanda = b.id_domanda "
    		+ "JOIN shell_t_soggetti c ON c.id_soggetto = b.id_soggetto_beneficiario "
    		+ "WHERE a.id_capofila_acronimo = :idCapofilaAcronimo ";
	
    private static final String SELECT_XML_RUOLO_PROGETTI_COMUNI = "SELECT "
			+ "(xpath('/tree-map/_ruoloProgettiComuni/map/ruolo/text()',  aggr_t_model.serialized_model))[1]::text AS ruolo "
	  		+ " FROM  aggr_t_model WHERE aggr_t_model.model_id = :idDomanda" ;
    
    private static final String SELECT_XML_ABSTRACT_PROGETTI_COMUNI = "SELECT "
			+ "(xpath('/tree-map/_abstractProgetto/map/idCapofilaAcronimo/text()',  aggr_t_model.serialized_model))[1]::text AS idCapofilaAcronimo, "
			+ "(xpath('/tree-map/_abstractProgetto/map/idAcronimoBando/text()',  aggr_t_model.serialized_model))[1]::text AS idAcronimoBando,"
			+ "(xpath('/tree-map/_abstractProgetto/map/acronimoProgetto/text()',  aggr_t_model.serialized_model))[1]::text AS acronimoProgetto, "
			+ "(xpath('/tree-map/_abstractProgetto/map/titolo/text()',  aggr_t_model.serialized_model))[1]::text AS titolo, "
			+ "(xpath('/tree-map/_abstractProgetto/map/sintesi/text()',  aggr_t_model.serialized_model))[1]::text AS sintesi, "
			+ "(xpath('/tree-map/_abstractProgetto/map/durataPrevista/text()',  aggr_t_model.serialized_model))[1]::text AS durataPrevista "
	  		+ " FROM  aggr_t_model WHERE aggr_t_model.model_id = :idDomanda" ;
    
    private static final String UPDATE_CAPOFILA_ACRONIMO_PARTNER_CANC_DOMANDA = "UPDATE shell_r_capofila_acronimo_partner SET id_domanda_partner = NULL "
			+ " WHERE id_capofila_acronimo = :idCapofilaAcronimo AND id_partner = :idPartner ";
    
//    private static final String SELECT_VERIFICA_ESISTENZA_ALTRE_DOMANDE_BENEFICIARIO_CAPOFILA = "SELECT a.id_domanda AS idAltraDomandaCapofila FROM shell_t_domande a "
//    		+ " JOIN findom_t_sportelli_bandi b ON a.id_sportello_bando = b.id_sportello_bando AND b.dt_chiusura IS NULL AND b.id_bando = :idBando "
//    		+ " JOIN shell_t_capofila_acronimo c ON a.id_domanda = c.id_domanda AND c.dt_disattivazione IS NULL "
//    		+ " WHERE a.id_soggetto_beneficiario = :idSoggettoBeneficiario AND a.id_domanda != :idDomanda ";
    
    private static final String SELECT_VERIFICA_ESISTENZA_ALTRE_DOMANDE_BENEFICIARIO_CAPOFILA = "SELECT a.id_domanda AS idAltraDomandaCapofila FROM shell_t_domande a "
    		+ " JOIN findom_t_sportelli_bandi b ON a.id_sportello_bando = b.id_sportello_bando AND b.dt_chiusura IS NULL AND b.id_bando = :idBando "
    		+ " JOIN aggr_t_model c ON a.id_domanda = c.model_id AND c.model_state_fk != 'NV' "
    		+ " WHERE a.id_soggetto_beneficiario = :idSoggettoBeneficiario AND a.id_domanda != :idDomanda ";
    
    private static final String SELECT_PARTNER_BY_ATTRS = "SELECT id_partner AS idPartner, cod_fiscale AS codiceFiscale, cod_ufficio AS codiceUnitaOrganizzativa,"
    		+ " denominazione AS denominazione, sigla_nazione AS idStato "
			+ " FROM shell_t_partner WHERE cod_fiscale = :codiceFiscale AND sigla_nazione = :idStato #vincoliAggiuntivi# ";   
   
    private static final String SELECT_SOGGETTI_BY_ATTRS = "SELECT cod_fiscale AS codiceFiscale, cod_ufficio AS codiceDipartimento, "
    		+ " denominazione AS denominazione, sigla_nazione AS idStato "
			+ " FROM shell_t_soggetti WHERE cod_fiscale = :codiceFiscale ";
    
    
//    private static String SELECT_SPESE_PROGETTO_COMUNE_BY_ID_DOMANDE = "SELECT id_domanda AS idDomanda, id_tipol_intervento AS idTipologiaIntervento,  "
//      		+ " tipologia_intervento AS descrTipologiaIntervento, id_dett_tipol_intervento AS idDettTipologiaIntervento, "
//      		+ " dettaglio_intervento AS descrDettaglioTipologiaIntervento, id_voce_spesa AS idVoceSpesa, voce_spesa as descrVoceSpesa, "
//      		+ " replace AS importo " 
//      		+ " FROM  findom_v_domande_interventi_spese " 
//      		+ " WHERE id_domanda IN (:idDomandaList)";
    
    private static String SELECT_SPESE_PROGETTO_COMUNE_BY_ID_DOMANDE = "SELECT y.idDomanda, y.tipoRecord, y.idTipologiaIntervento, y.descrTipologiaIntervento, "
    		+ " y.codTipoIntervento, y.idDettTipologiaIntervento, y.descrDettTipologiaIntervento, y.codDettIntervento, y.idVoceSpesa,  "
    		+ " y.descrVoceSpesa, y.codVoceSpesa, y.totaleVoceSpesa "    		
    		+ " FROM ( SELECT x.id_domanda                                             AS idDomanda,  "
    		+ " unnest(xpath('//tipoRecord/text()'::text, x.cat))::text::integer       AS tipoRecord, "
    		+ " unnest(xpath('//idTipoIntervento/text()'::text, x.cat))::text::integer AS idTipologiaIntervento, "
    		+ " unnest(xpath('//descrTipoIntervento/text()'::text, x.cat))::text       AS descrTipologiaIntervento, "
    		+ " (xpath('//codTipoIntervento/text()'::text, x.cat))::text               AS codTipoIntervento, "
    		+ " (xpath('//idDettIntervento/text()'::text, x.cat))::text                AS idDettTipologiaIntervento, "
    		+ " (xpath('//descrDettIntervento/text()'::text, x.cat))::text             AS descrDettTipologiaIntervento, "
    		+ " (xpath('//codDettIntervento/text()'::text, x.cat))::text               AS codDettIntervento, "
    		+ " (xpath('//idVoceSpesa/text()'::text, x.cat))::text                     AS idVoceSpesa, "
    		+ " (xpath('//descrVoceSpesa/text()'::text, x.cat))::text                  AS descrVoceSpesa, "
    		+ " (xpath('//codVoceSpesa/text()'::text, x.cat))::text                    AS codVoceSpesa, "
    		+ " (xpath('//totaleVoceSpesa/text()'::text, x.cat))::text                 AS totaleVoceSpesa "
    		+ " FROM ( SELECT m.model_id AS id_domanda,  "
    		+ "        unnest(xpath('/tree-map/_pianoSpese/map/pianoSpeseList/list/map'::text, m.serialized_model)) AS cat "
    		+ "        FROM aggr_t_model m) x) y "
    		//+ " WHERE y.idDomanda in (:idDomandaList) and y.tipoRecord = '3'"
    		+ " WHERE y.tipoRecord = '3' #vincoliAggiuntivi# "
    		//+ " ORDER BY y.codTipoIntervento,y.codDettIntervento, y.codVoceSpesa ";
    		// Jira: 1686 ::2R + " ORDER BY y.idTipologiaIntervento, y.idDettTipologiaIntervento, y.idVoceSpesa ";
    		+ " ORDER BY y.descrVoceSpesa";
    
    private static final String SELECT_PARTNER_BY_ID_PARTNER = "SELECT id_partner AS idPartner, cod_fiscale AS codiceFiscale, cod_ufficio AS codiceUnitaOrganizzativa,"
    		+ " denominazione AS denominazione, sigla_nazione AS idStato "
			+ " FROM shell_t_partner WHERE id_partner = :idPartner "; 
    
    
    private static final String SELECT_STATO_DOMANDA_BY_ID_PARTNER = "SELECT a.id_domanda_partner AS idDomanda, b.model_state_fk AS idStatoDomanda, "
    		+ " c.model_state_descr AS descrStatoDomanda  "
    		+ " FROM shell_r_capofila_acronimo_partner a  "
    		+ " JOIN aggr_t_model b ON a.id_domanda_partner = b.model_id "
    		+ " JOIN aggr_d_model_state c ON b.model_state_fk = c.model_state  "
    		+ " WHERE a.id_capofila_acronimo = :idCapofilaAcronimo AND a.id_partner = :idPartner AND a.dt_disattivazione is null ";
    
    private static final String SELECT_ID_SOGGETTO_BY_CF = "SELECT id_soggetto AS idSoggetto FROM shell_t_soggetti WHERE cod_fiscale = :codiceFiscale ";
    
    private static final String UPDATE_DENOMINAZIONE_SOGGETTO_BY_ID = "UPDATE shell_t_soggetti SET denominazione = :denominazione "
			+ " WHERE id_soggetto = :idSoggetto ";
    
    private static final String UPDATE_RIATTIVA_RELAZIONE_CAPOFILA_ACRONIMO_PARTNER = "UPDATE shell_r_capofila_acronimo_partner SET dt_disattivazione = NULL,  "
    		+ " id_domanda_partner = NULL, note = NULL, dt_attivazione = now() "
			+ " WHERE id_capofila_acronimo = :idCapofilaAcronimo AND id_partner = :idPartner ";
    
    private static final String SELECT_XML_OPERATORE_PRESENTATORE = "SELECT "
			+ "(xpath('/tree-map/_operatorePresentatore/map/codiceFiscale/text()', aggr_t_model.serialized_model))[1]::text AS codiceFiscale, "
			+ "(xpath('/tree-map/_operatorePresentatore/map/codiceDipartimento/text()',  aggr_t_model.serialized_model))[1]::text AS codiceDipartimento, "
			+ "(xpath('/tree-map/_operatorePresentatore/map/idStato/text()',  aggr_t_model.serialized_model))[1]::text AS idStato "
	  		+ " FROM aggr_t_model WHERE aggr_t_model.model_id = :idDomanda" ;
    
    private static final String SELECT_DATI_PARTNER_ELIMINATI_BY_ID_CAPOFILA_ACRONIMO = "SELECT b.id_partner AS idPartner, "
    		+ "b.cod_fiscale AS codiceFiscale, b.denominazione AS denominazione, b.cod_ufficio AS codiceDipartimento, "
    		+ "d.descrizione AS descrDipartimento, b.sigla_nazione AS idStato, c.descrizione_stato AS descrStato, "
    		+ "a.id_domanda_partner AS idDomandaPartner,a.note AS note "
    		+ "FROM SHELL_R_CAPOFILA_ACRONIMO_PARTNER a "
    		+ "JOIN SHELL_T_PARTNER b ON a.id_partner = b.id_partner "
    		+ "LEFT JOIN EXT_D_STATI_ESTERI c ON b.sigla_nazione = c.cod_stato "
    		+ "LEFT JOIN FINDOM_D_DIPARTIMENTI d ON b.cod_ufficio = d.codice "
    		+ "WHERE a.id_capofila_acronimo = :idCapofilaAcronimo AND a.dt_disattivazione IS NOT NULL";
    
    
	public static String insertShellTPartner(PartnerItemVO partnerItemVO, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: insertShellTPartner] ";
		logger.info(logprefix + " BEGIN");
		String idPartnerGenerato = "";
		String insertStatement = "";
		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(partnerItemVO ==null){
				return idPartnerGenerato;
			}
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			String codiceFiscale = partnerItemVO.getCodiceFiscale();
			String codiceDipartimento = partnerItemVO.getCodiceDipartimento();
			String denominazione = partnerItemVO.getDenominazione();
			String idStato = partnerItemVO.getIdStato();
			
			if(StringUtils.isBlank(codiceDipartimento)){
				insertStatement = INSERT_PARTNER;
			}else{
				insertStatement = INSERT_PARTNER_CON_DIP;
			}
			
			params.addValue("codiceFiscale", codiceFiscale, java.sql.Types.VARCHAR);
			if(StringUtils.isNotBlank(codiceDipartimento)){				
			   params.addValue("codiceDipartimento", codiceDipartimento, java.sql.Types.VARCHAR);
			}
			params.addValue("denominazione", denominazione, java.sql.Types.VARCHAR);
			params.addValue("idStato", idStato, java.sql.Types.VARCHAR);

			jdbcTemplate.update(insertStatement, params, keyHolder, new String[]{"id_partner"});
			Number idPartner = keyHolder.getKey();
			if(idPartner!=null){
				idPartnerGenerato = idPartner.toString();
			}
		}catch (Exception e) {
			logger.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return	idPartnerGenerato;		
	}

	public static void updateShellTPartner(PartnerItemVO partnerItemVO, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: updateShellTPartner] ";
		logger.info(logprefix + " BEGIN");
		try {		

			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("codiceFiscale", partnerItemVO.getCodiceFiscale());
			namedParameters.addValue("codiceDipartimento", partnerItemVO.getCodiceDipartimento());
			namedParameters.addValue("denominazione", partnerItemVO.getDenominazione());
			namedParameters.addValue("idStato", partnerItemVO.getIdStato());
		
			Integer idPartnerInt = Integer.parseInt(partnerItemVO.getIdPartner());
			namedParameters.addValue("idPartner", idPartnerInt, Types.INTEGER);

			jdbcTemplate.update(UPDATE_PARTNER, namedParameters);
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}

	public static String verificaEsistenzaPartner(PartnerItemVO partnerItemVO, Logger logger) throws CommonalityException { 
		final String logprefix = "[PartnerDAO:: verificaEsistenzaPartner] ";
		logger.info(logprefix + " BEGIN");
		String idPartner = "";
		String query = SELECT_VERIFICA_ESISTENZA_PARTNER;
		try {
			Map<String, Object> params = new HashMap<>();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
		
			params.put("codFiscale", partnerItemVO.getCodiceFiscale());
			
			//non considero più la denominzaione come criterio
			//params.put("denominazione", partnerItemVO.getDenominazione());
			//e quindi tolgo dalla select  AND upper(denominazione) = upper(:denominazione)
			
			params.put("idStato", partnerItemVO.getIdStato());

			String condizione = "";
			if(StringUtils.isNotBlank(partnerItemVO.getCodiceDipartimento())){
				condizione =  " AND cod_ufficio = :codiceDipartimento";	
				params.put("codiceDipartimento", partnerItemVO.getCodiceDipartimento());
			}
			String q = query.replace("#condizione#", condizione);
			logger.info(logprefix + " query = " + q);
			SqlParameterSource namedParameters = new MapSqlParameterSource(params);

			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(q, namedParameters);			
			if(rlist!=null && !rlist.isEmpty() && rlist.size()==1 && rlist.get(0)!=null && (((Map)rlist.get(0)).get("idPartner")!=null)){			
				Integer idPartnerInt = (Integer)((Map)rlist.get(0)).get("idPartner");
				idPartner = String.valueOf(idPartnerInt);
			}
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return idPartner;
	}
	
	public static ArrayList<CapofilaAcronimoPartnerVO> getCapofilaAcronimoPartnerListByIdPartner(String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getCapofilaAcronimoPartnerListByIdPartner] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<CapofilaAcronimoPartnerVO> lista = null;
		String query = SELECT_CAPOFILA_ACRONIMO_PARTNER_BY_ID_PARTNER;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idPartner", idPartnerInt , java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			lista = (ArrayList<CapofilaAcronimoPartnerVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<CapofilaAcronimoPartnerVO>(CapofilaAcronimoPartnerVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}
	
	public static void insertShellRCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: insertShellRCapofilaAcronimoPartner] ";
		logger.info(logprefix + " BEGIN");

		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(StringUtils.isBlank(idCapofilaAcronimo) || StringUtils.isBlank(idPartner)){
				return;
			}
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);	

			jdbcTemplate.update(INSERT_CAPOFILA_ACRONIMO_PARTNER, params);			

		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}		
	}
	//overload del metodo precedente
	public static void insertShellRCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner,String idDomandaPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: insertShellRCapofilaAcronimoPartner] ";
		logger.info(logprefix + " BEGIN");
		String insertStatement = "";
		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(StringUtils.isBlank(idCapofilaAcronimo) || StringUtils.isBlank(idPartner)){
				return;
			}
			if(StringUtils.isBlank(idDomandaPartner)){
				insertStatement = INSERT_CAPOFILA_ACRONIMO_PARTNER;
			}else{
				insertStatement = INSERT_CAPOFILA_ACRONIMO_PARTNER_ALL;
			}
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);
			
			if(StringUtils.isNotBlank(idDomandaPartner)){
				params.addValue("idDomandaPartner",  Integer.parseInt(idDomandaPartner), java.sql.Types.INTEGER);
			}			
			jdbcTemplate.update(insertStatement, params);			

		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}		
	}
	
	public static String verificaEsistenzaDomandaPartnerPerCapofila(String idCapofilaAcronimo, String idPartner, Logger logger) throws CommonalityException { 
		final String logprefix = "[PartnerDAO:: verificaEsistenzaDomandaPartnerPerCapofila] ";
		logger.info(logprefix + " BEGIN");
		String idDomandaPartner = "";
		String query = SELECT_VERIFICA_ESISTENZA_DOMANDA_PARTNER_PER_CAPOFILA;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idPartnerInt = Integer.parseInt(idPartner);
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			
			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, params);
			if(rlist!=null && !rlist.isEmpty() && rlist.size()==1 && rlist.get(0)!=null && (((Map)rlist.get(0)).get("idDomandaPartner")!=null)){				
				idDomandaPartner = ((Map)rlist.get(0)).get("idDomandaPartner").toString();
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return idDomandaPartner;
	}
	
	public static ArrayList<String> verificaEsistenzaDomandaPartner(String idPartner, Logger logger) throws CommonalityException { 
		final String logprefix = "[PartnerDAO:: verificaEsistenzaDomandaPartner] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<String> idDomandaPartnerList = null;
		String query = SELECT_VERIFICA_ESISTENZA_DOMANDA_PARTNER;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idPartnerInt = Integer.parseInt(idPartner);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			
			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, params);
			if(rlist!=null && !rlist.isEmpty() && rlist.size()>0){				
				idDomandaPartnerList = new ArrayList<>();
			    for (Iterator<Map<String, Object>> iterator = rlist.iterator(); iterator.hasNext();) {
					Map<String, Object> map = (Map<String, Object>) iterator.next();					
					String curIdDomandaPartner = map.get("idDomandaPartner").toString();
					idDomandaPartnerList.add(curIdDomandaPartner);
				}
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return idDomandaPartnerList;
	}
	
	public static StatoDomandaVO getStatoDomanda(String idDomanda, Logger logger) throws CommonalityException {		
		final String logprefix = "[PartnerDAO:: getStatoDomanda] ";
		logger.info(logprefix + " BEGIN");				
		it.csi.findom.blocchetti.common.vo.domandaNG.StatoDomandaVO statoDomandaVO = null;
		String query = SELECT_STATO_DOMANDA;
		
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			
			Integer idDomandaInt = Integer.parseInt(idDomanda);
			params.addValue("idDomanda", idDomandaInt, java.sql.Types.INTEGER);
			
			List<StatoDomandaVO> statoDomandaVOList = jdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(StatoDomandaVO.class));
	    	if(statoDomandaVOList != null && statoDomandaVOList.size()>0){
	    		statoDomandaVO = (StatoDomandaVO)statoDomandaVOList.get(0);
	    	}

		} catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return statoDomandaVO;
	}


	public static Integer deleteCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: deleteCapofilaAcronimoPartner] ";
		logger.info(logprefix + " BEGIN");		
		int esito = 0;	
		String query = DELETE_CAPOFILA_ACRONIMO_PARTNER;	
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(StringUtils.isBlank(idCapofilaAcronimo) || StringUtils.isBlank(idPartner)){
				return null;
			}
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);	
			
			esito = jdbcTemplate.update(query, params);
			logger.debug(logprefix + " cancellazione avvenuta, esito = " + esito);
			
			
		} catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		
		return esito;
	}
	
	public static void disattivaRelCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner,String motivazione, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: disattivaRelCapofilaAcronimoPartner] ";
		logger.info(logprefix + " BEGIN");
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);	
			params.addValue("motivazione", motivazione, java.sql.Types.VARCHAR);	
			
			jdbcTemplate.update(UPDATE_DISATTIVA_CAPOFILA_ACRONIMO_PARTNER, params);
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}
	
	public static void updateStatoDomanda(String idDomanda,String statoDomanda, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: updateStatoDomanda] ";
		logger.info(logprefix + " BEGIN");
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idDomandaInt = Integer.parseInt(idDomanda);
			params.addValue("statoDomanda", statoDomanda);
			params.addValue("idDomanda",idDomandaInt);	
			
			jdbcTemplate.update(UPDATE_STATO_DOMANDA, params);
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}
	
	public static ArrayList<AcronimoBandiVO> getAcronimoBandiListByIdBando(Integer idBando, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getAcronimoBandiListByIdBando] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<AcronimoBandiVO> lista = null;
		String query = SELECT_ACRONIMI_VALIDI_BY_ID_BANDO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			params.addValue("idBando", idBando , java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			lista = (ArrayList<AcronimoBandiVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<AcronimoBandiVO>(AcronimoBandiVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}
	
	public static Integer insertShellTAcronimoBandi(Integer idBando, String acronimoProgetto, String titoloProgetto, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: insertShellTAcronimoBandi] ";
		logger.info(logprefix + " BEGIN");
		Integer idAcronimoBandoGenerato = null;
		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(idBando == null || StringUtils.isEmpty(acronimoProgetto) || StringUtils.isEmpty(titoloProgetto)){
				return idAcronimoBandoGenerato;
			}
			KeyHolder keyHolder = new GeneratedKeyHolder();			

			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			params.addValue("acronimoProgetto", acronimoProgetto, java.sql.Types.VARCHAR);
			params.addValue("titoloProgetto", titoloProgetto, java.sql.Types.VARCHAR);			

			jdbcTemplate.update(INSERT_ACRONIMO_BANDI, params, keyHolder, new String[]{"id_acronimo_bando"});
			Number idAcronimoBando = keyHolder.getKey();
			if(idAcronimoBando!=null){
				idAcronimoBandoGenerato = (Integer)idAcronimoBando;
			}
		}catch (Exception e) {
			logger.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return	idAcronimoBandoGenerato;		
	}
	
	
	public static ArrayList<CapofilaAcronimoPartnerVO> getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(String idCapofilaAcronimo, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getCapofilaAcronimoPartnerListByIdCapofilaAcronimo] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<CapofilaAcronimoPartnerVO> lista = null;
		String query = SELECT_CAPOFILA_ACRONIMO_PARTNER_BY_ID_CAPOFILA_ACRONIMO;
		if(StringUtils.isBlank(idCapofilaAcronimo)){
			return lista;
		}
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			lista = (ArrayList<CapofilaAcronimoPartnerVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<CapofilaAcronimoPartnerVO>(CapofilaAcronimoPartnerVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}
	
	public static void disattivaRelAcronimoBando(String idAcronimoBando, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: disattivaRelAcronimoBando] ";
		logger.info(logprefix + " BEGIN");
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idAcronimoBandoInt = Integer.parseInt(idAcronimoBando);			
			params.addValue("idAcronimoBando", idAcronimoBandoInt, java.sql.Types.INTEGER);	

			jdbcTemplate.update(UPDATE_DISATTIVA_ACRONIMO_BANDO, params);
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}

	public static String getIdPartnerByAttrs(String codiceFiscale, String codiceUnitaOrganizzativa, String siglaNazione, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getIdPartnerByAttrs] ";
		logger.debug(logprefix + " BEGIN");
		String idPartner = "";
		String query = SELECT_ID_PARTNER_BY_ATTRS;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("codiceFiscale", codiceFiscale, java.sql.Types.VARCHAR);			
			//params.addValue("denominazione", denominazione, java.sql.Types.VARCHAR); non uso piu' la denominazione come criterio di confronto, quindi nella select tolgo AND denominazione = :denominazione
			params.addValue("siglaNazione", siglaNazione, java.sql.Types.VARCHAR);	
						
			String condizione = "";
			if(StringUtils.isNotBlank(codiceUnitaOrganizzativa)){
				condizione =  " AND cod_ufficio = :codiceUnitaOrganizzativa";	
				params.addValue("codiceUnitaOrganizzativa", codiceUnitaOrganizzativa);
			}
			query = query.replace("#condizione#", condizione);
			
			logger.debug(logprefix + " codiceFiscale = " + codiceFiscale);
			logger.debug(logprefix + " codiceUnitaOrganizzativa = " + codiceUnitaOrganizzativa);
			logger.debug(logprefix + " siglaNazione = " + siglaNazione);
			logger.debug(logprefix + " query = " + query);

			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, params);
			if(rlist!=null && !rlist.isEmpty() && rlist.size()==1 && rlist.get(0)!=null && (((Map)rlist.get(0)).get("idPartner")!=null)){			
				Integer idPartnerInt = (Integer)((Map)rlist.get(0)).get("idPartner");
				idPartner = String.valueOf(idPartnerInt);
			}
		}catch (Exception e) {
			logger.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.debug(logprefix + " END");
		}
		return idPartner;	
	}
	//se il parametro idDomanda e' null, estrae gli acronimi per il caso INSERIMENTO (non legati ad alcuna domanda)
	//se il parametro idDomanda non e' null, estrae gli acronimi per il caso MODIFICA (non legati ad alcuna domanda eccetto la domanda passata)
	public static ArrayList<AcronimoBandiVO> getAcronimoBandiListByIdPartnerIdBando(String idPartner, Integer idBando, Integer idDomandaPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getAcronimoBandiListByIdBando] ";
		logger.debug(logprefix + " BEGIN");
		ArrayList<AcronimoBandiVO> lista = null;
		String query = SELECT_ACRONIMI_BY_ID_PARTNER_ID_BANDO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			if(idDomandaPartner!=null){					
				query = SELECT_ACRONIMI_BY_ID_PARTNER_ID_BANDO_ID_DOMANDA;
			}
			
			params.addValue("idBando", idBando , java.sql.Types.INTEGER);			
			Integer idPartnerInt = Integer.parseInt(idPartner);					
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);	
			
			if(idDomandaPartner!=null){	
				params.addValue("idDomandaPartner", idDomandaPartner, java.sql.Types.INTEGER);	
			}
			
			logger.debug(logprefix + " idPartner = " + idPartner);
			logger.debug(logprefix + " idBando = " + idBando);
			logger.debug(logprefix + " idDomandaPartner = " + idDomandaPartner);
			logger.debug(logprefix + " query = " + query);
			lista = (ArrayList<AcronimoBandiVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<AcronimoBandiVO>(AcronimoBandiVO.class));
			if(lista!=null && lista.size()>0){
				logger.debug(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.debug(logprefix + " END");
		}
		return lista;
	}
	
	public static Integer insertShellTCapofilaAcronimo(Integer idDomanda, Integer idAcronimoBando, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: insertShellTCapofilaAcronimo] ";
		logger.info(logprefix + " BEGIN");
		Integer idCapofilaAcronimoGenerato = null;
		try {	
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));		

			if(idDomanda == null || idAcronimoBando == null){
				return idCapofilaAcronimoGenerato;
			}
			KeyHolder keyHolder = new GeneratedKeyHolder();			
				
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);			
			params.addValue("idAcronimoBando", idAcronimoBando, java.sql.Types.INTEGER);					

			jdbcTemplate.update(INSERT_CAPOFILA_ACRONIMO, params, keyHolder, new String[]{"id_capofila_acronimo"});
			Number idCapofilaAcronimo = keyHolder.getKey();
			if(idAcronimoBando!=null){
				idCapofilaAcronimoGenerato = (Integer)idCapofilaAcronimo;
			}
		}catch (Exception e) {
			logger.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return	idCapofilaAcronimoGenerato;		
	}
	
	public static void disattivaRelCapofilaAcronimo(String idCapofilaAcronimo, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: disattivaRelCapofilaAcronimo] ";
		logger.info(logprefix + " BEGIN");
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);			
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);	

			jdbcTemplate.update(UPDATE_DISATTIVA_CAPOFILA_ACRONIMO, params);
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}
	
	public static AcronimoBandiVO getAcronimoBandoByIdAcronimoBando(String idAcronimoBando, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getAcronimoBandoByIdAcronimoBando] ";
		logger.info(logprefix + " BEGIN");
		if(StringUtils.isBlank(idAcronimoBando)){
			return null;
		}
		AcronimoBandiVO acronimoBandiVO = null;
		String query = SELECT_ACRONIMO_BANDI_BY_ID_ACRONIMO_BANDO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			

			Integer idAcronimoBandoInt = Integer.parseInt(idAcronimoBando);
			params.addValue("idAcronimoBando", idAcronimoBandoInt , java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			ArrayList<AcronimoBandiVO> lista = (ArrayList<AcronimoBandiVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<AcronimoBandiVO>(AcronimoBandiVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
				acronimoBandiVO = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return acronimoBandiVO;
	}
	
	public static void updateDomandaPartnerCapofilaAcronimoPartner(String idDomandaPartner, String idCapofilaAcronimo, String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: updateDomandaPartnerCapofilaAcronimoPartner] ";
		logger.info(logprefix + " BEGIN");
		if(StringUtils.isBlank(idDomandaPartner) || StringUtils.isBlank(idCapofilaAcronimo) || StringUtils.isBlank(idPartner)){
			logger.info(logprefix + " esco dal metodo senza fare nulla perche' idDomandaPartner o idCapofilaAcronimo o idPartner sono nulli");
			return;
		}
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			
			
			Integer idDomandaPartnerInt = Integer.parseInt(idDomandaPartner);
			params.addValue("idDomandaPartner", idDomandaPartnerInt , java.sql.Types.INTEGER);			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idPartner", idPartnerInt , java.sql.Types.INTEGER);

			jdbcTemplate.update(UPDATE_DOMANDA_PARTNER_CAPOFILA_ACRONIMO_PARTNER, params);
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}

	public static CapofilaAcronimoVO getCapofilaAcronimoByIdCapofilaAcronimo(String idCapofilaAcronimo, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getCapofilaAcronimoByIdCapofilaAcronimo] ";
		logger.info(logprefix + " BEGIN");
		CapofilaAcronimoVO capofilaAcronimoVO = null;
		String query = SELECT_CAPOFILA_ACRONIMO_BY_ID_CAPOFILA_ACRONIMO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			

			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			ArrayList<CapofilaAcronimoVO> lista = (ArrayList<CapofilaAcronimoVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<CapofilaAcronimoVO>(CapofilaAcronimoVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
				capofilaAcronimoVO = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return capofilaAcronimoVO;
	}
	
	public static OperatorePresentatoreVo getDatiCapofilaByIdCapofilaAcronimo (String idCapofilaAcronimo, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getDatiCapofilaByIdCapofilaAcronimo] ";
		logger.info(logprefix + " BEGIN");
		OperatorePresentatoreVo operatorePresentatoreVo = null;
		String query = SELECT_DATI_CAPOFILA_BY_ID_CAPOFILA_ACRONIMO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			

			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			ArrayList<OperatorePresentatoreVo> lista = (ArrayList<OperatorePresentatoreVo>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<OperatorePresentatoreVo>(OperatorePresentatoreVo.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
				operatorePresentatoreVo = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return operatorePresentatoreVo;
	}

	public static String getRuoloProgettoComuneXml(Integer idDomanda, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getRuoloProgettoComuneXml] ";
		logger.info(logprefix + " BEGIN");
		String ruolo = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("idDomanda", idDomanda, Types.INTEGER);
			SqlRowSet row = jdbcTemplate.queryForRowSet(SELECT_XML_RUOLO_PROGETTI_COMUNI, params);
			if (row!=null && row.next()){
				ruolo = row.getString("ruolo");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return ruolo;
	}	
	
	public static AbstractProgettoCPVO getAbstractProgettoComuneXml (Integer idDomanda, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO::getAbstractProgettoComuneXml] ";
		logger.info(logprefix + " BEGIN");
		AbstractProgettoCPVO abstractProgettoCPVO = null;
		String query = SELECT_XML_ABSTRACT_PROGETTI_COMUNI;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("idDomanda", idDomanda, Types.INTEGER);			
			logger.info(logprefix + " query = " + query);
			ArrayList<AbstractProgettoCPVO> lista = (ArrayList<AbstractProgettoCPVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<AbstractProgettoCPVO>(AbstractProgettoCPVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
				abstractProgettoCPVO = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return abstractProgettoCPVO;
	}
	
	public static void updateCapofilaAcronimoPartnerCancDomanda(String idCapofilaAcronimo, String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO::updateCapofilaAcronimoPartnerCancDomanda] ";
		logger.info(logprefix + " BEGIN");
		if(StringUtils.isBlank(idCapofilaAcronimo) || StringUtils.isBlank(idPartner)){
			logger.info(logprefix + " esco dal metodo senza fare nulla perche' idCapofilaAcronimo o idPartner sono nulli");
			return;
		}
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
						
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idPartner", idPartnerInt , java.sql.Types.INTEGER);

			jdbcTemplate.update(UPDATE_CAPOFILA_ACRONIMO_PARTNER_CANC_DOMANDA, params);
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}
	
	public static ArrayList<String> verificaEsistenzaAltreDomandeBeneficiarioCapofila(Integer idBando, Integer idSoggettoBeneficiario, Integer idDomanda, Logger logger) throws CommonalityException { 
		final String logprefix = "[PartnerDAO:: verificaEsistenzaAltreDomandeBeneficiarioCapofila] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<String> idAltreDomandeCapofilaList = null;
		String query = SELECT_VERIFICA_ESISTENZA_ALTRE_DOMANDE_BENEFICIARIO_CAPOFILA;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
				
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			params.addValue("idSoggettoBeneficiario", idSoggettoBeneficiario, java.sql.Types.INTEGER);
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			
			List<Map<String, Object>> rlist = jdbcTemplate.queryForList(query, params);
			if(rlist!=null && !rlist.isEmpty() && rlist.size()>0){				
				//idDomandaPartner = ((Map)rlist.get(0)).get("idDomandaPartner").toString();
				for (Iterator<Map<String, Object>> iterator = rlist.iterator(); iterator.hasNext();) {
					Map<String, Object> map = (Map<String, Object>) iterator.next();
					if(map!=null && !map.isEmpty()){
						BigDecimal curIdDomanda = (BigDecimal) map.get("idAltraDomandaCapofila");
						if(curIdDomanda!=null){
							if(idAltreDomandeCapofilaList==null){
								idAltreDomandeCapofilaList = new ArrayList<String>();
							}
							idAltreDomandeCapofilaList.add(curIdDomanda.toPlainString());
						}
					}
				}
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return idAltreDomandeCapofilaList;
	}

	public static ArrayList<PartnerItemVO> getPartnerListByAttrs(String codiceFiscale, String idStato, String codiceUnitaOrganizzativa, boolean soloAttivi, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getPartnerListByAttrs] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<PartnerItemVO> lista = null;
		String query = SELECT_PARTNER_BY_ATTRS;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			params.addValue("codiceFiscale", codiceFiscale, java.sql.Types.VARCHAR);
			params.addValue("idStato", idStato, java.sql.Types.VARCHAR);			
			
			StringBuilder vincoliAggiuntivi = new StringBuilder();			
			if(StringUtils.isNotBlank(codiceUnitaOrganizzativa)){
				vincoliAggiuntivi.append(" AND cod_ufficio = :codiceUnitaOrganizzativa " );	
				params.addValue("codiceUnitaOrganizzativa", codiceUnitaOrganizzativa);
			}
			if(soloAttivi){
				vincoliAggiuntivi.append(" AND dt_disattivazione IS NULL"); 
			}
			query = query.replace("#vincoliAggiuntivi#", vincoliAggiuntivi);			
			
			logger.info(logprefix + " query = " + query);
			lista = (ArrayList<PartnerItemVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<PartnerItemVO>(PartnerItemVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}
    //il metodo assume  che in shell_t_soggetti non esista piu' di un record con lo stesso codice fiscale
	public static ArrayList<PartnerItemVO> getSoggettiListByCF(String codiceFiscale, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getSoggettiListByAttrs] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<PartnerItemVO> lista = null;
		String query = SELECT_SOGGETTI_BY_ATTRS;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			params.addValue("codiceFiscale", codiceFiscale, java.sql.Types.VARCHAR);
			
			logger.info(logprefix + " query = " + query);
			//forzatura: i dati restituiti li metto in una lista di PartnerItemVO (i dati estratti hanno lo stesso nome di alcuni attributi di PartnerItemVO)
			lista = (ArrayList<PartnerItemVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<PartnerItemVO>(PartnerItemVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}
	
	public static ArrayList<SpeseProgettiComuniVO> getSpeseProgettiComuniListByIdDomande(List<Integer> idDomandeList, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getSpeseProgettiComuniListByIdDomande] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<SpeseProgettiComuniVO> lista = null;
		String query = SELECT_SPESE_PROGETTO_COMUNE_BY_ID_DOMANDE;
		
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			StringBuilder vincoliAggiuntivi = new StringBuilder();			
			if(idDomandeList!=null && !idDomandeList.isEmpty()){
				vincoliAggiuntivi.append(" AND y.idDomanda in (:idDomandaList) " );	
				params.addValue("idDomandaList",idDomandeList );
			}
			query = query.replace("#vincoliAggiuntivi#", vincoliAggiuntivi);
			
			logger.info(logprefix + " query = " + query);
			lista = (ArrayList<SpeseProgettiComuniVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<SpeseProgettiComuniVO>(SpeseProgettiComuniVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}

		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}
	
	public static PartnerItemVO getPartnerByIdPartner(String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getPartnerByIdPartner] ";
		logger.info(logprefix + " BEGIN");
		if(StringUtils.isBlank(idPartner)){
			return null;
		}
		PartnerItemVO partnerItemVO = null;
		String query = SELECT_PARTNER_BY_ID_PARTNER;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			

			Integer idPartnerInt = Integer.parseInt(idPartner);		
			params.addValue("idPartner", idPartnerInt, Types.INTEGER);
			logger.info(logprefix + " query = " + query);
			ArrayList<PartnerItemVO> lista = (ArrayList<PartnerItemVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<PartnerItemVO>(PartnerItemVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
				partnerItemVO = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return partnerItemVO;
	}
	
	public static StatoDomandaVO getStatoDomandaPartnerByIds(String idCapofilaAcronimo, String idPartner, Logger logger)  throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getStatoDomandaPartnerByIds] ";
		logger.info(logprefix + " BEGIN");				
		it.csi.findom.blocchetti.common.vo.domandaNG.StatoDomandaVO statoDomandaVO = null;
		String query = SELECT_STATO_DOMANDA_BY_ID_PARTNER;
		
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));			
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);
			
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);
			
			List<StatoDomandaVO> statoDomandaVOList = jdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(StatoDomandaVO.class));
	    	if(statoDomandaVOList != null && statoDomandaVOList.size()>0 && statoDomandaVOList.get(0)!=null){
	    		statoDomandaVO = (StatoDomandaVO)statoDomandaVOList.get(0);
	    	}

		} catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return statoDomandaVO;
	}
	
	public static String getIdSoggettoByCF(String codiceFiscale, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: getIdSoggettoByCF] ";
		logger.info(logprefix + " BEGIN");
		String idSoggetto = "";

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("codiceFiscale", codiceFiscale, java.sql.Types.VARCHAR);			
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(SELECT_ID_SOGGETTO_BY_CF, params);
			if (row!=null && row.next()){
				idSoggetto = row.getString("idSoggetto");
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return idSoggetto;
	}

	
	public static void updateDenominazioneSoggettoById(String idSoggetto, String denominazione, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO::updateDenominazioneSoggettoById] ";
		logger.info(logprefix + " BEGIN");
		if(StringUtils.isBlank(idSoggetto)){
			logger.info(logprefix + " esco dal metodo senza fare nulla perche' idSoggetto e' nullo");
			return;
		}
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
						
			Integer idSoggettoInt = Integer.parseInt(idSoggetto);
			params.addValue("idSoggetto", idSoggettoInt , java.sql.Types.INTEGER);
			params.addValue("denominazione", denominazione , java.sql.Types.VARCHAR);

			jdbcTemplate.update(UPDATE_DENOMINAZIONE_SOGGETTO_BY_ID, params);
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}
	public static void riattivaRelazioneCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO:: riattivaRelazioneCapofilaAcronimoPartner] ";
		logger.info(logprefix + " BEGIN");
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);
			Integer idPartnerInt = Integer.parseInt(idPartner);
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);	

			jdbcTemplate.update(UPDATE_RIATTIVA_RELAZIONE_CAPOFILA_ACRONIMO_PARTNER, params);
			
		} catch (DataAccessException e) {
			throw new CommonalityException(e);
		}finally {
			logger.info(logprefix + " END");
		}
	}
	
//	public static String getUOOperatorePresentatoreXml(Integer idDomanda, Logger logger) throws CommonalityException {
//		final String logprefix = "[PartnerDAO:: getUOOperatorePresentatoreXml] ";
//		logger.info(logprefix + " BEGIN");
//		String codiceDipartimento = "";
//
//		try {
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
//
//			params.addValue("idDomanda", idDomanda, Types.INTEGER);
//			SqlRowSet row = jdbcTemplate.queryForRowSet(SELECT_XML_UO_OPERATORE_PRESENTATORE, params);
//			if (row!=null && row.next()){
//				codiceDipartimento = row.getString("codiceDipartimento");
//			}
//		}catch (Exception e) {
//			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
//			throw new CommonalityException(e);
//		} finally {
//			logger.info(logprefix + " END");
//		}
//		return codiceDipartimento;
//	}	
	public static OperatorePresentatoreVo getOperatorePresentatoreXml (Integer idDomanda, Logger logger) throws CommonalityException {
		final String logprefix = "[PartnerDAO::getOperatorePresentatoreXml] ";
		logger.info(logprefix + " BEGIN");
		OperatorePresentatoreVo operatorePresentatoreVo = null;
		String query = SELECT_XML_OPERATORE_PRESENTATORE;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			params.addValue("idDomanda", idDomanda, Types.INTEGER);			
			logger.info(logprefix + " query = " + query);
			ArrayList<OperatorePresentatoreVo> lista = (ArrayList<OperatorePresentatoreVo>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<OperatorePresentatoreVo>(OperatorePresentatoreVo.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
				operatorePresentatoreVo = lista.get(0);
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return operatorePresentatoreVo;
	}

	
	public static ArrayList<PartnerEliminatoVO> getPartnerEliminatiListByIdCapofilaAcronimo(String idCapofilaAcronimo, Logger logger) throws CommonalityException {
		
		final String logprefix = "[PartnerDAO:: getPartnerEliminatiListByIdCapofilaAcronimo] ";
		logger.info(logprefix + " BEGIN");
		ArrayList<PartnerEliminatoVO> lista = null;
		String query = SELECT_DATI_PARTNER_ELIMINATI_BY_ID_CAPOFILA_ACRONIMO;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
			
			Integer idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);			
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
		
			logger.info(logprefix + " query = " + query);
			lista = (ArrayList<PartnerEliminatoVO>) jdbcTemplate.query(query, params, new BeanPropertyRowMapper<PartnerEliminatoVO>(PartnerEliminatoVO.class));
			if(lista!=null && lista.size()>0){
				logger.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
		}catch (Exception e) {
			logger.info(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new CommonalityException(e);
		} finally {
			logger.info(logprefix + " END");
		}
		return lista;
	}	
	
}
