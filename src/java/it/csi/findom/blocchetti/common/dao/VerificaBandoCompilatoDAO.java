/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class VerificaBandoCompilatoDAO {
	
	private static final String VERIFICA_BANDO_EMPOWERMENT_FINANZIAMENTO_INVIATO = "SELECT dt_invio_domanda "
			+ " FROM findom_v_domande_nuova_gestione "
			+ " WHERE cod_fiscale_beneficiario = :codFiscale "
			+ " AND id_bando = '59'"
			+ " AND dt_invio_domanda IS NOT NULL;";
	
	public static String getVerificaBandoEmpowermentFinanziamentoInviato(String codiceFiscale) throws CommonalityException {

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				(DataSource) ServiceBeanLocator.getBeanByName("dataSource"));

		String domandaInviata = null;

		if(codiceFiscale!=null) {	

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("codFiscale", codiceFiscale, Types.VARCHAR);

			try {
				
				List<String> listaDomandeInviate =jdbcTemplate.queryForList(VERIFICA_BANDO_EMPOWERMENT_FINANZIAMENTO_INVIATO,namedParameters,String.class);
				
				if (listaDomandeInviate != null && !listaDomandeInviate.isEmpty()) {
					domandaInviata = listaDomandeInviate.get(0);
				}

			} catch (DataAccessException e) {
				throw new CommonalityException(e);
			}
		}
		return domandaInviata;
	}
}
