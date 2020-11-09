/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import java.io.Serializable;


public class SegnalazioneErrore implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private String campoErrore;
	private String msgErrore;
	
	public String getCampoErrore() {
		return campoErrore;
	}
	public String getMsgErrore() {
		return msgErrore;
	}
	public void setCampoErrore(String campoErrore) {
		this.campoErrore = campoErrore;
	}
	public void setMsgErrore(String msgErrore) {
		this.msgErrore = msgErrore;
	}
}
