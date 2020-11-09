/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinanziamento;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.CONF;
import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.MODEL;
import it.csi.findom.blocchetti.blocchetti.descrizioneFiera.DescrizioneFieraVO;
import it.csi.findom.blocchetti.blocchetti.descrizioneFieraSecEd.DescrizioneFieraSecEdVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.domandaNG.DomandaNGVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class FormaFinanziamentoInput extends CommonalityInput {

	  @MapTo(target=MODEL,name="_pianoSpese")
	  PianoSpeseVO _pianoSpese;
	  
	  @MapTo(target=MODEL,name="_formaFinanziamento")
	  FormaFinanziamentoVO _formaFinanziamento;

	  @MapTo(target=CONF,name="_progetto_agevolrichiesta_importo_unico")
	  String _progetto_agevolrichiesta_importo_unico;
	  
	  @MapTo(target=CONF,name="_progetto_agevolazione_una_e_una_sola_forma_fin")
	  String _progetto_agevolazione_una_e_una_sola_forma_fin;
	  
	  /** Jira: 903 - */
	  @MapTo(target=CONF,name="_progetto_agevolrichiesta_importo_europeo")
	  String _progetto_agevolrichiesta_importo_europeo;
	  
	  /** Jira: 903 - */
	  @MapTo(target=MapTarget.MODEL, name="_descrizioneFiera")
	  DescrizioneFieraVO descrizioneFiera;
	  
	  // : Jira per bando Cinema - inizio
	  @MapTo(target=CONF,name="_progetto_agevolrichiesta_importo_attivita_produttive")
	  String _progetto_agevolrichiesta_importo_attivita_produttive;

	  // : Unesco - Importo richiedibile <= 80% totSpese
	  @MapTo(target=CONF,name="_progetto_agevolrichiesta_max_importo_perc_var")
	  String _progetto_agevolrichiesta_max_importo_perc_var;

	  // : Unesco - Importo da calcolare anche in base ad importo max erogabile per il bando
	  @MapTo(target=CONF,name="_forma_fin_importo_max_erogabile")
	  String _forma_fin_importo_max_erogabile;

	  @MapTo(target=MapTarget.MODEL ,name="_caratteristicheProgetto")
	  CaratteristicheProgettoNGVO caratteristicheProgettoNGVO;

	  /** Jira: 1418 - */
	  @MapTo(target=CONF,name="_progetto_agevolrichiesta_importo_europeo_voucher_sec_ed")
	  String _progetto_agevolrichiesta_importo_europeo_voucher_sec_ed;
	  
	  /** Jira: 1418 - */
	  @MapTo(target=MapTarget.MODEL, name="_descrizioneFieraSecEd")	  
	  public DescrizioneFieraSecEdVO _descrizioneFieraSecEd;
	  
	  /** Jira: 1855 */
      @MapTo(target=MODEL,name="_domanda")
      DomandaNGVO _domanda;
	  
	  /** Jira: 1537 - */
	  @MapTo(target=CONF,name="_progetto_forme_finanziamento_custom")
	  String _progetto_forme_finanziamento_custom;
	  
	  /** Jira: 1646 -  */
	  @MapTo(target=CONF,name="_forma_finanziamento_is_checkbox_nascoste")
	  String _forma_finanziamento_is_checkbox_nascoste;
	  
	  /** Jira: 1646 -  */
	  @MapTo(target=CONF,name="_forma_finanziamento_id_checkbox_nascoste")
	  String _forma_finanziamento_id_checkbox_nascoste;
	  
	  /** Jira: 1671 -  */
	  @MapTo(target=CONF,name="_progetto_forme_finanziamento_imp_min_max_by_bando")
	  String _progetto_forme_finanziamento_imp_min_max_by_bando;
	  
	  /** Jira: 1671*/
	  @MapTo(target=CONF,name="_forma_finanziamento_cessione_credito")
	  String _forma_finanziamento_cessione_credito;
	  
	  
	  /** Jira: 1855*/
	  @MapTo(target=MapTarget.CONF)		
	  public String validationMethodsFormaFinanziamento;
	  
	  /** Jira: Innometro */
	  @MapTo(target=CONF,name="_formaFinanziamento_ImpMinMaxPercByDb")
	  String _formaFinanziamento_ImpMinMaxPercByDb;
	  
}
