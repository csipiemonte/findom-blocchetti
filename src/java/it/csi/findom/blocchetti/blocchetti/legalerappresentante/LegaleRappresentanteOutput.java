/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.legalerappresentante;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.NAMESPACE;
import it.csi.findom.blocchetti.common.vo.aaep.PersonaINFOCVO;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ComuneVO;
import it.csi.findom.blocchetti.common.vo.luoghi.ProvinciaVO;
import it.csi.findom.blocchetti.common.vo.luoghi.StatoEsteroVO;
import it.csi.findom.blocchetti.common.vo.tipodocriconoscimento.TipoDocRiconoscimentoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

class LegaleRappresentanteOutput extends CommonalityOutput {

	@MapTo(target = NAMESPACE)
	List<StatoEsteroVO> statoEsteroList;

	@MapTo(target = NAMESPACE)
	List<ProvinciaVO> provinciaList;

	@MapTo(target = NAMESPACE)
	List<ComuneVO> comuneNascitaList;

	@MapTo(target = NAMESPACE, name = "_legaleRappresentante_siglaProvinciaNascita")
	String legaleRappresentante_siglaProvinciaNascita;

	@MapTo(target = NAMESPACE, name = "_legaleRappresentante_siglaProvinciaResidenza")
	String legaleRappresentante_siglaProvinciaResidenza;

	@MapTo(target = NAMESPACE)
	List<ComuneVO> comuneResidenzaList;

	@MapTo(target = NAMESPACE)
	List<TipoDocRiconoscimentoVO> tipoDocRiconoscimentoList;

	@MapTo(target = NAMESPACE)
	String presenzaSD;

	@MapTo(target = NAMESPACE)
	List<LRVo> listLRAAEP;

	@MapTo(target = NAMESPACE)
	PersonaINFOCVO legRapprAAEP;

	@MapTo(target = NAMESPACE)
	LegaleRappresentanteVO LRMap;

	@MapTo(target = NAMESPACE)
	String viewFromLR;

	@MapTo(target = NAMESPACE)
	String lrFromAAEP;

	@MapTo(target = NAMESPACE)
	String presenzaLRAAEPSuDB;

	@MapTo(target = NAMESPACE)
	String mostraMsgAAEP;

	@MapTo(target = NAMESPACE)
	String mostraMsgSelLR;

	@MapTo(target = NAMESPACE)
	String mostraMsgDatiUltimaDomanda;
	
	@MapTo(target = NAMESPACE)
	String visMsgLRdaUltimaDomanda;
	
	@MapTo(target = NAMESPACE)
	String visMsgLRdaAAEP;
	
	/** Jira: 1842 :  */
	@MapTo(target = NAMESPACE)
	String svuotaCampiResidenza;
	
	/** Jira: 1842 :  - */
	@MapTo(target = NAMESPACE)
	List<ProvinciaVO> provinciaResidenzaList;
	
}
