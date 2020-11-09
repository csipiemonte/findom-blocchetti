/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.partner;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AcronimoBandiVO extends CommonalityVO {

	private static final long serialVersionUID = 1L;
   
	@MapTo(target = MapTarget.INHERIT)
	String idAcronimoBando;

	@MapTo(target = MapTarget.INHERIT)
	String idBando;

	@MapTo(target = MapTarget.INHERIT)
	String acronimoProgetto;

	@MapTo(target=INHERIT)
	String titoloProgetto;

	@MapTo(target=INHERIT)
	String dtAttivazione;
	
	@MapTo(target=INHERIT)
	String dtDisattivazione;
	
	//non e' in shell_t_acronimoBandi, ma lo aggiungo per avere l'idCapofilaAcronimo associato ad ogni elemento della combo degli acronimi, 
	//in modo che scegliendo un acronimo si possa popolare un campo hidden anche con questa informazione che puo' cosi' essere salvata nell'xml del partner
	@MapTo(target = MapTarget.INHERIT)
	String idCapofilaAcronimo;  

	public String getIdAcronimoBando() {
		return idAcronimoBando;
	}

	public void setIdAcronimoBando(String idAcronimoBando) {
		this.idAcronimoBando = idAcronimoBando;
	}

	public String getIdBando() {
		return idBando;
	}

	public void setIdBando(String idBando) {
		this.idBando = idBando;
	}

	public String getAcronimoProgetto() {
		return acronimoProgetto;
	}

	public void setAcronimoProgetto(String acronimoProgetto) {
		this.acronimoProgetto = acronimoProgetto;
	}

	public String getTitoloProgetto() {
		return titoloProgetto;
	}

	public void setTitoloProgetto(String titoloProgetto) {
		this.titoloProgetto = titoloProgetto;
	}

	public String getDtAttivazione() {
		return dtAttivazione;
	}

	public void setDtAttivazione(String dtAttivazione) {
		this.dtAttivazione = dtAttivazione;
	}

	public String getDtDisattivazione() {
		return dtDisattivazione;
	}

	public void setDtDisattivazione(String dtDisattivazione) {
		this.dtDisattivazione = dtDisattivazione;
	}

	public String getIdCapofilaAcronimo() {
		return idCapofilaAcronimo;
	}

	public void setIdCapofilaAcronimo(String idCapofilaAcronimo) {
		this.idCapofilaAcronimo = idCapofilaAcronimo;
	}
	
}
