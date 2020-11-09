/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.commonality;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Classe delle costanti applicative.</p>
 *
 */
public class Constants {	
	public static final String PAGINA_NON_COMPILATA = "0";
	public static final String PAGINA_IN_COMPILAZIONE = "1";
	public static final String PAGINA_COMPILATA = "2";
	
	public static final String ID_DIMENSIONE_GRANDE = "4";
	public static final String ID_RUOLO_CAPOFILA = "C";
	public static final String ID_RUOLO_PARTNER = "P";
	public static final String ID_RUOLO_PROPONENTE_UNICO_PRG_COMUNE = "PU";
	public static final String STATO_DOMANDA_INVALIDATA = "NV";
	public static final String STATO_DOMANDA_CONCLUSA = "CO";
	public static final String STATO_DOMANDA_INVIATA = "IN";
	public static final String STATO_DOMANDA_BOZZA = "BZ";
	
	public static final String ID_STATO_ITALIA = "000";
	
	public static final String ID_RUOLO_CAPOFILA_PRG_COMUNE = "CP";
	public static final String ID_RUOLO_PARTNER_PRG_COMUNE = "PP";
	public static final Map<String, String> ABSTRACT_RUOLI = new HashMap<String, String>();
	static{
		ABSTRACT_RUOLI.put(ID_RUOLO_CAPOFILA, "Capofila");
		ABSTRACT_RUOLI.put(ID_RUOLO_PARTNER, "Partner");
	}
	
}
