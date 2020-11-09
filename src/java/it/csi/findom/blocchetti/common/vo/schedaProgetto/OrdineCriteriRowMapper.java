/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.schedaProgetto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class OrdineCriteriRowMapper implements ParameterizedRowMapper {
	public TreeMap<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
		TreeMap<Integer, Integer> mappa = new TreeMap<Integer, Integer>();
		
		Integer k = rs.getInt("id_criterio");
		Integer v = rs.getInt("ordine_criterio");
		mappa.put(k, v);
		return mappa;
	}
}
