/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.findom.blocchetti.common.vo.premialitaprogetto.PremialitaProgettoItemVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class PremialitaProgettoDAO {

	private static final String QUERY_PREMIALITA = "SELECT "	 
		    + "       a.id_bando                  AS id_bando, " 
		    + "       a.id_premialita || ''             AS id_premialita, "
		    + "       c.descrizione               AS descr_premialita, "
		    + "       c.flag_tipo_dato_richiesto  AS tipo_dato_richiesto, " 
		    + "       c.dato_richiesto            AS dato_richiesto, "
		     + "      c.link                      AS link_premialita, 'false' as checked "
	        + " FROM findom_r_bandi_premialita a, "
		    + "      findom_v_domande_nuova_gestione b, " 
		    + "      findom_d_premialita c "
	        + " WHERE a.id_bando = b.id_bando and "
	        + "       a.id_premialita = c.id_premialita ";
		   	
		
		public static List<PremialitaProgettoItemVO> getPremialitaList(String idDomanda, String dataInvio) throws CommonalityException {
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
					(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

			String query = QUERY_PREMIALITA;
			if(StringUtils.isBlank(dataInvio)) {
				   query += " AND (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE)) and ";
				}else{
					query += " AND (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy'))) and ";
				}			
				
			query +=  " b.id_domanda='" + idDomanda + "' ";
			query += " order by c.descrizione ";

			
			SqlParameterSource namedParameters = null;

			try {
				return jdbcTemplate.query(query, namedParameters,
						new BeanPropertyRowMapper<PremialitaProgettoItemVO>(PremialitaProgettoItemVO.class));
			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		}


}

