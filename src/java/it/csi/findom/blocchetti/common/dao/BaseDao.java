/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.dao;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class BaseDao extends JdbcDaoSupport
{
	public static void addStringToIntegerValues(StringBuilder query, List<String> codes)
	{
		if (codes != null)
		{
			boolean first = true;
			for (String code : codes)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					query.append(", ");
				}
				if (code != null)
				{
					query.append(Long.valueOf(code));
				}
				else
				{
					query.append("null");
				}
			}
		}
	}

	public static void addIntegerToStringValues(StringBuilder query, List<Integer> codes)
	{
		if (codes != null)
		{
			boolean first = true;
			for (Integer code : codes)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					query.append(", ");
				}
				if (code != null)
				{
					query.append(code);
				}
				else
				{
					query.append("null");
				}
			}
		}
	}
}
