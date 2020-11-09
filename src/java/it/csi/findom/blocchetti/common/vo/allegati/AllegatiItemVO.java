/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.allegati;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class AllegatiItemVO extends CommonalityVO {


	private static final long serialVersionUID = 1L;
	@MapTo(target = INHERIT)
	DocumentoVO documento;

	public DocumentoVO getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoVO documento) {
		this.documento = documento;
	}

  @Override
  public String toString() {
    return "AllegatiItemVO [documento=" + documento + "]";
  }


}
