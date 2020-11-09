/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.commonality;

import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.findom.blocchetti.vo.FinUserInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class FinCommonInfo extends CommonInfo {

	@MapTo(target=MapTarget.NAMESPACE)
	private FinUserInfo userInfo;
	
	@MapTo(target=MapTarget.NAMESPACE)
	private FinStatusInfo statusInfo;
	
	@MapTo(target=MapTarget.NAMESPACE)
	private boolean accessoForbidden;
		
	public FinUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(FinUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public FinStatusInfo getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(FinStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}
	public boolean isAccessoForbidden() {
		accessoForbidden = false;
		return accessoForbidden;
	}
	public void setAccessoForbidden(boolean accessoForbidden) {
//		this.accessoForbidden = accessoForbidden;
		this.accessoForbidden = false;
	}
}
