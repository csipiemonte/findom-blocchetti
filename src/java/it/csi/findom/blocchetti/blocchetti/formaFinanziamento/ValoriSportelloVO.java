/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class ValoriSportelloVO  extends CommonalityVO {

	private static final long serialVersionUID = 1L;
	      
	@MapTo(target=INHERIT)
	String idTipolIntervento;
	      
	@MapTo(target=INHERIT)
	String importoMinimoErogabile;		      
	      
	@MapTo(target=INHERIT)
	String importoMassimoErogabile;		      
	      
	@MapTo(target=INHERIT)
	String percMaxContributoErogabile;
	
	
	public String getIdTipolIntervento() {
		return idTipolIntervento;
	}

	public void setIdTipolIntervento(String idTipolIntervento) {
		this.idTipolIntervento = idTipolIntervento;
	}


	public String getIdTiploIntervento() {
		return idTipolIntervento;
	}

	public void setIdTiploIntervento(String idTipolIntervento) {
		this.idTipolIntervento = idTipolIntervento;
	}

	public String getImportoMinimoErogabile() {
		return importoMinimoErogabile;
	}
	
	public void setImportoMinimoErogabile(String importoMinimoErogabile) {
		this.importoMinimoErogabile = importoMinimoErogabile;
	}
	
	public String getImportoMassimoErogabile() {
		return importoMassimoErogabile;
	}
	
	public void setImportoMassimoErogabile(String importoMassimoErogabile) {
		this.importoMassimoErogabile = importoMassimoErogabile;
	}
	
	public String getPercMaxContributoErogabile() {
		return percMaxContributoErogabile;
	}

	public void setPercMaxContributoErogabile(String percMaxContributoErogabile) {
		this.percMaxContributoErogabile = percMaxContributoErogabile;
	}

}
