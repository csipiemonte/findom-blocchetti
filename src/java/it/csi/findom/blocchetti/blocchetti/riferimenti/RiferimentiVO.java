/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.riferimenti;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RiferimentiVO extends CommonalityVO  {

	private static final long serialVersionUID = 1L;

	 @MapTo(target = INHERIT, name = "societa")
	 SocietaVO societa;
	
	 @MapTo(target = INHERIT, name = "personaImpresa")
	 PersonaImpresaVO personaImpresa; 
	 		
	 @MapTo(target = INHERIT, name = "consulente")
	 ConsulenteVO consulente; 
}
