/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.rottamazione;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class RottamazioneElencoVO extends CommonalityVO {
	  
	private static final long serialVersionUID = 1L;
	
	@MapTo(target=INHERIT)
	ItemRottamazioneVO[] itemRottamazioneVOList;

	public ItemRottamazioneVO[] getItemRottamazioneVOList() {
		return itemRottamazioneVOList;
	}

	public void setItemRottamazioneVOList(
			ItemRottamazioneVO[] itemRottamazioneVOList) {
		this.itemRottamazioneVOList = itemRottamazioneVOList;
	}
	  
}
