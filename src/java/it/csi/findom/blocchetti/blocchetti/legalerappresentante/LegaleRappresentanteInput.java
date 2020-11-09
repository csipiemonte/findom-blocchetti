/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.legalerappresentante;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONTEXT;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class LegaleRappresentanteInput extends CommonalityInput {

  @MapTo(target=REQUEST,name="importLRFromAAEP")
  String importLRFromAAEP;
  
  
  @MapTo(target=REQUEST,name="newLRNotFromAAEP")
  String newLRNotFromAAEP;
  
  @MapTo(target=REQUEST,name="identificativoLRsuAAEP")
  String identificativoLRsuAAEP;
  
  @MapTo(target=REQUEST,name="_legaleRappresentante.provinciaNascita")
  String legaleRappresentanteDotProvinciaNascita;
  
  @MapTo(target=REQUEST,name="_legaleRappresentante.provinciaResidenza")
  String legaleRappresentanteDotprovinciaResidenza;
  
  @MapTo(target=MODEL,name="_legaleRappresentante")
  LegaleRappresentanteVO legaleRappresentante;
  
  @MapTo(target=CONTEXT,name="xformId")
  String xformId;

  @MapTo(target=CONTEXT,name="xformProg")
  Integer xformProg;
  
  @MapTo(target=CONTEXT,name="xformName")
  String xformName;
  
  @MapTo(target=CONF)
  String _legaleRappresentante_genere;
  
  @MapTo(target=CONF)
  String _legaleRappresentante_presenza_residenza;
  
  /** CR-3: Condomini */
  @MapTo(target=CONF)
  String _controllo_coerenza_codice_fiscale_persona_fisica;
  
  /** Jira: 1842:  01/04/2020 */
  @MapTo(target=CONF)
  String _legalerappresentante_res_prov_piemonte;
  
}
