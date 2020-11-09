/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.sedeLegale;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.REQUEST;
import it.csi.findom.blocchetti.common.vo.legalerappresentante.LegaleRappresentanteVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SedeLegaleInput extends CommonalityInput {

  @MapTo(target=CONF, name="_ente_impresa_SL_altri_recapiti")
  String enteImpresaSLAltriRecapiti;

  @MapTo(target=CONF, name="_sede_legale_visualizza_nota_precompilazione")
  String sedeLegaleVisualizzaNotaPrecompilazione;
  
  /**Jira: 1770 */
  @MapTo(target=CONF, name="_ente_impresa_SL_altri_recapiti_senza_pec")
  String _ente_impresa_SL_altri_recapiti_senza_pec;
  
  @MapTo(target=CONF, name="_sedi_extra_Piemonte")
  String _sedi_extra_Piemonte;
  

  @MapTo(target=MODEL, name="_sedeLegale")
  SedeLegaleVO sedeLegaleModel;

  @MapTo(target=REQUEST, name="_sedeLegale.provincia")
  String sedeLegale_provincia;
  
  @MapTo(target=MODEL, name="_legaleRappresentante")
  LegaleRappresentanteVO _legaleRappresentante;
  
  /** Jira: 1842:  modifica in corso ... 01/04/2020 */
  @MapTo(target=CONF, name="_sedeLegale_solo_province_piemonte")
  String _sedeLegale_solo_province_piemonte;
  
  /** bonus Piemonte covid19 */
  @MapTo(target=CONF, name="_sedeLegale_emailOrPecObbligatorio")
  String  _sedeLegale_emailOrPecObbligatorio;
  
}
