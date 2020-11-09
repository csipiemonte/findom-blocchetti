/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.commonality;


public class ControlEmail {

	public static boolean ctrlFormatoIndirizzoEmail(String indirizzo){
		org.apache.commons.validator.routines.EmailValidator validator  = org.apache.commons.validator.routines.EmailValidator.getInstance();
		return validator.isValid(indirizzo);
	}
}
