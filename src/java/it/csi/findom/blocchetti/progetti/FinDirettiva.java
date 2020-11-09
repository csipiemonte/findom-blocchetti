/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.progetti;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import freemarker.core.Environment.Namespace;
import it.csi.findom.blocchetti.vo.FinUserInfo;
import it.csi.findom.blocchetti.vo.FinStatusInfo;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.progetti.AbstractDirettiva;
import it.csi.melograno.aggregatore.business.javaengine.progetti.SubModelIndexQuery;

public abstract class FinDirettiva extends AbstractDirettiva {

	protected static String QUERY_INDEX_SOGGETTI = "select count(s.model_id)+1 from aggr_t_submodel s "
			+ "where s.model_id = :idDomanda and s.xml_type = 'soggetto'";

	protected static String QUERY_INDEX_ATTIVITA = "SELECT nextval('seq_shell_t_attivita') as valore";

	protected Map<String, SubModelIndexQuery> subModelIndexQuery = new HashMap<>();

	public FinDirettiva(TreeMap<String, Object> context, Namespace namespace, TreeMap<String, Object> model) {
		super(context, namespace, model);
		initializeSubModelIndexQuery();
	}

	protected void initializeSubModelIndexQuery() {
		subModelIndexQuery.put("soggetto", new SubModelIndexQuery(QUERY_INDEX_SOGGETTI, true));
		subModelIndexQuery.put("attivita", new SubModelIndexQuery(QUERY_INDEX_ATTIVITA, false,
				"_s_attivitaIndividuale.attivitaIndividualeList[0].datiIdentificativi.attivitaID"));
		subModelIndexQuery.put("corso", new SubModelIndexQuery(QUERY_INDEX_ATTIVITA, false,
				"_s_attivita.attivitaList[0].attivitaDatiIdentificativi.attivitaID"));
	}

	@Override
	public SubModelIndexQuery getSubModelIndexQuery(String submodel) {
		return subModelIndexQuery.get(submodel);
	}

	@Override
	protected CommonInfo createCommonInfo() {
		FinCommonInfo commonInfo = new FinCommonInfo();

		commonInfo.setApplicativo((String) context.get("applicativo"));
		commonInfo.setLoggerName((String) context.get("loggerName"));
		commonInfo.setUserInfo((FinUserInfo) context.get("userInfo"));
		commonInfo.setStatusInfo((FinStatusInfo) context.get("statusInfo"));
		return commonInfo;
	}

	@Override
	public void initConfigurations() {
		// presi da bando00
 
		// -------------------------------------------//
		// ------ operatorePresentatore ------------- //
		// -------------------------------------------//
		
		/** se a true nasconde il campo codice Ipa anche per i bandi che hanno 
		 *  ente beneficiario con flag=2 (pubblico) 
		 *  se richiesto da specifiche del bando. 
		 **/
		configuration.put("codice_ipa_nascosto", "false");
		
		// operatorePresentatore_dipartimenti = true/false, abilita/disabilita la combo
		// per la scelta dei dipartimenti; usato in S1_P1 (template.xhtml,
		// template_RO.xhtml, inject.bsh, model_validation_rules.bsh)
		// usato in S1_P1_SEL_ATECO ( template.xhtml )
		// se true comporta che deve essere gestita su pdf_template.xhtml la
		// visualizzazione o meno della descrizione del dipartimento, in base alla
		// valorizzazione dell'idDipartimento nell'xml 
		// se true comporta che deve essere gestito su xslt_template.xsl il campo
		// dipartimento
		configuration.put("operatorePresentatore_dipartimenti", "true");
		
		/** jira: 2142: 
		 *  se a true visualizza il combo-box per i bandi che hanno 
		 *  : (1) cf e (3) dipartimenti universitari 
		 *  : bando richiesto: Beni librari 2020
		 *  
		 *  Tabelle db interessate:
		 *  - findom_r_bandi_soggetti_abilitati
		 *  - findom_t_soggetti_abilitati
		 *  - findom_d_dipartimenti  
		 **/
		configuration.put("_operatorePresentatore_dipartimentiMultipli", "false");
				
		/**
		 * Se valore a true: verranno visualizzati i seguenti campi in:
		 * Anagrafica Ente/Impresa > Beneficiario > Dati identificativi
		 * - Settore prevalente (ATECO 2007)
		 * - Descrizione
		 * - Settore attività economica
		 */
		configuration.put("_operatorePresentatore_ateco", "true");
		
		/**
		 * Se valore a true: 
		 * visualizzo la select in
		 * Anagrafica Ente/Impresa > Beneficiario > Dati identificativi
		 * - Settore attività economica
		 */
		configuration.put("_operatorePresentatore_settore_attivita_economica", "true");
		
		
		/**
		 * Se valore a true: 
		 * nascondo il campo 'pec'
		 */
		configuration.put("_operatorePresentatore_indirizzoPec", "false");
		
		// -------------------------------------------//
		// ------ costituzioneImpresa --------------- //
		// -------------------------------------------//
		
		// _costituzioneImpresa_costituzione_in_corso = true/false, visualizza/nasconde
		// il checkbox "Impresa in fase di costituzione" 
		// NOTA: il valore del checkbox "Impresa in fase di costituzione" é usato in
		// index_rule_datiImpresa_dati_bilancio.bsh; ricordarsi di adattare a mano la
		// logica nell'index rule a seconda di questa configurazione 
		configuration.put("_costituzioneImpresa_costituzione_in_corso", "true");
		// _costituzioneImpresa_iscrizione_in_corso = true/false, visualizza/nasconde il
		// checkbox "Iscrizione in corso" 
		configuration.put("_costituzioneImpresa_iscrizione_in_corso", "true");		
		
		// -------------------------------------------//
		// ------ legale rappresentante--------------- //
		// -------------------------------------------//
		
		// _legaleRappresentante_genere = true/false, abilita/disabilita il campo "genere"
		configuration.put("_legaleRappresentante_genere", "false");
		
		// _legaleRappresentante_presenza_soggetto_delegato = true/false,
		// abilita/disabilita il radio "soggetto delegato"; usato in S1_P6 in
		// template.xhtml e template_RO.xhtml; -->
		// se true comporta che deve essere creato su aggr_t_template_index un record
		// per la pagina S1_P6
		// se true comporta che deve essere gestita su pdf_template.xhtml la possibilità
		// che il soggetto delegato sia dichiarato presente 
		// se true comporta che deve essere gestito su xslt_template.xsl il template
		// anagraficaSoggettoDelegato
		// se true comporta che deve essere fatto su global_validation_rules.bsh il
		// controllo sulla compilazione di S1_P6 e dell'allegato con id = 20
		// se true comporta che devono essere scritte su
		// index_rule_anagrafica_soggetto_delegato.bsh le regole di
		// abilitazione/disabilitazione della S1_P6		
		configuration.put("_legaleRappresentante_presenza_soggetto_delegato", "true");
		
		// _legaleRappresentante_presenza_residenza = true/false, visualizza/nasconde la
		// sottosezione 'residenza' -->
		// usato in S1_P3, model validation rules e in global validation rules -->
		configuration.put("_legaleRappresentante_presenza_residenza", "true");		
		
		/** Jira: 1809 label dinamiche   */
		configuration.put("_toggle_dinamico_label", "false");
		configuration.put("_legaleRappresentante_toggle_dinamico_label", "");	

		// -------------------------------------------//
		// ------ soggettoDelegato------------------- //
		// -------------------------------------------//
		
		// _soggettoDelegato_genere = true/false, abilita/disabilita il campo "genere"
		configuration.put("_soggettoDelegato_genere", "false");

		// _soggettoDelegato_presenza_residenza = true/false, visualizza/nasconde la
		// sottosezione 'residenza'
		// usato in S1_P6, model validation rules e in global validation rules
		configuration.put("_soggettoDelegato_presenza_residenza", "true");		
		
		// -------------------------------------------//
		// ------ beneficiarioPF--------------------- //
		// -------------------------------------------//
		
		// _beneficiarioPF_genere = true/false, abilita/disabilita il campo "genere"
		configuration.put("_beneficiarioPF_genere", "false");

		// true/false, visualizza/nasconde la
		// sottosezione 'residenza'
		// usato in S1_P6, model validation rules e in global validation rules
		configuration.put("_beneficiarioPF_presenza_residenza", "false");	

		// -------------------------------------------//
		// ------ sedeLegale------------------------- //
		// -------------------------------------------//
		
		// _ente_impresa_SL_altri_recapiti; se true vengono visualizzati i seguenti recapiti
		// nella pagina della sede legale:Persona riferimento, Email, Telefono, Cellulare;
		// se false i recapiti sono: Telefono, Indirizzo PEC, Email
		configuration.put("_ente_impresa_SL_altri_recapiti", "false");
		
		/** Jira: 1770 : - 
		 *  Recapiti sede legale senza pec 
		 *  se = false  : visualizzo il campo
		 *  se = true   : nascondo il campo
		 **/
		configuration.put("_ente_impresa_SL_altri_recapiti_senza_pec","false");
		
		// se il blocchetto viene incluso in una pagina che include altri blocchetti con dati precompilabili da AAEP,
		// questa variabile deve essere messa a false per evitare che compaia anche un ulteriore messaggio 
		// di avvenuta precompilazione con dati da AAEP
		configuration.put("_sede_legale_visualizza_nota_precompilazione", "false");

		// -------------------------------------------//
		// ------ riferimenti------------------------ //
		// -------------------------------------------//
		
		// _riferimenti_view_sezione_societa_consulenza, se true viene visualizzata la
		// corrispondente sezione:
		// - Societa di consulenza autorizzata ad intrattenere contatti
		// - Persona dell' ente impresa autorizzata ad intrattenere contatti
		configuration.put("_riferimenti_view_sezione_societa_consulenza", "true");	
		
		// obbligo di compilazione della sezione: Ente/Impresa: se true, frase custom
		configuration.put("_riferimenti_obbligo_compilazione", "false");	
		
		/** Jira: 1843 
		 *  Gestione label dinamiche per il blocchetto: riferimenti
		 *  1) Persona dell'ente / impresa autorizzata ad intrattenere i contatti
		 *  2) Societa di consulenza autorizzata ad intrattenere contatti
		 *  3) Consulente autorizzato ad intrattenere contatti
		 **/
		configuration.put("_riferimenti_per_ent_imp_aut_int_cont_label", "");
		configuration.put("_riferimenti_soc_con_aut_int_cont_label", "");
		configuration.put("_riferimenti_con_aut_int_cont_label", "");

		// -------------------------------------------//
		// ------ bilancio--------------------------- //
		// -------------------------------------------//

		// _bilancio_XYZ = true/false, abilita/disabilita la voce "XYZ"
		configuration.put("_bilancio_anno", "true");				
		configuration.put("_bilancio_speseRS", "true");
		configuration.put("_bilancio_creditiCommScad", "true");
		configuration.put("_bilancio_disponibilitaLiquide", "true");
		configuration.put("_bilancio_totaleBilancio", "true");
		configuration.put("_bilancio_totalePatrimonio", "true");
		configuration.put("_bilancio_debitiSoci", "true");
		configuration.put("_bilancio_debitiBanche", "true");
		configuration.put("_bilancio_debitiFornScad", "true");
		configuration.put("_bilancio_debitiImpreseCollegate", "true");
		configuration.put("_bilancio_debitiControllanti", "true");
		configuration.put("_bilancio_debitiTributariScad", "true");
		configuration.put("_bilancio_ricavi", "true");
		configuration.put("_bilancio_totaleValoreProduzione", "true");
		configuration.put("_bilancio_variazioneLavoriInCorso", "true");
		configuration.put("_bilancio_ammortamentiImm", "true");
		configuration.put("_bilancio_ammortamentiMat", "true");
		configuration.put("_bilancio_totaleCostiProduzione", "true");
		configuration.put("_bilancio_proventiFinanziari", "true");
		configuration.put("_bilancio_interessiPassivi", "true");
		configuration.put("_bilancio_proventiGestioneAccessoria", "true");
		configuration.put("_bilancio_oneriGestioneAccessoria", "true");
		configuration.put("_bilancio_ebitda", "true");
		configuration.put("_bilancio_ebit", "true");
		configuration.put("_bilancio_indiceRotazione", "true");
		configuration.put("_bilancio_dso", "true");
		configuration.put("_bilancio_dpo", "true");
		configuration.put("_bilancio_ula", "true");
		configuration.put("_bilancio_debitiVsFornitori", "true");
		configuration.put("_bilancio_debitiTributari", "true");
		configuration.put("_bilancio_creditiVsClienti", "true");

		// -------------------------------------------//
		// ------ dimensioniNG----------------------- //
		// -------------------------------------------//
		// _ente_impresa_dimensioni; se true viene visualizzata la tabella dei dati storici 
		// nella videata dimensioni dell'impresa
		
		configuration.put("_ente_impresa_dimensioni", "true");	
		
		// ---------------------------------------------------------------------//
		// ------ entiProgetto--------------------------------------- //
		// ---------------------------------------------------------------------//
		//se valorizzato, contiene l'id_parametro che in findom_d_parametri individua un record in cui il 'valore' e' un vincolo 
		//per il caricamento della combo dei comuni in entiProgetto
		configuration.put("_entiProgetto_parametro_caricamento_comuni", "");	

		// ---------------------------------------------------------------------//
		// ------ strutturaOrganizzativa e capacitaFinanziaria----------------- //
		// ---------------------------------------------------------------------//
		
		//se valorizzata, il valore viene messo a video a sinistra della textArea 'Struttura organizzativa, risorse umane e strumentali ....'
		configuration.put("_struttura_org_nota_esplicativa", "");
		
		// _struttura_org_progetto_inserito; se true viene visualizzata il check
		// raggruppamentoComuni1, usato anche in model-validation-rule
		configuration.put("_struttura_org_progetto_inserito", "true");
		
		// _struttura_org_progetto_approvato; se true viene visualizzata il check
		// raggruppamentoComuni2, usato anche in model-validation-rule
		configuration.put("_struttura_org_progetto_approvato", "true");
		
		// 12-01-2018 _capacita_fin_estremi_atti_approvazione; se true in
		// _capacitaFinanziaria viene visualizzata la tabella degli estremi degli atti
		// di approvazione; usato nell'inject, nei template e nel model validation rules
		// del blocchetto; gestione 'manuale' in stampa -->
		configuration.put("_capacita_fin_estremi_atti_approvazione", "true");
		
		// 06-02-2018 _capacita_fin_merito_creditizio; se true in _capacitaFinanziaria
		// viene visualizzata la text area del merito creditizio; usato nei template e
		// nel model validation rules del blocchetto; gestione 'manuale' in stampa
		configuration.put("_capacita_fin_merito_creditizio", "false");
		
		// 06-02-2018 _capacita_fin_label_check_progetto_approvato; questa variabile
		// deve essere valorizzata con il testo della check box -->
		// 'Il progetto e' stato approvato ....' nel caso in cui il testo stesso non sia
		// quello quello standard; se e' uguale allo standard NON valorizzare questa
		// variabile -->
		// oppure non definirla proprio (come é per i vecchi bandi): la check box avra'
		// il testo standard, anche se si rigenera la pagina; usata solo nel template
		// del blocchetto _capacitaFinanziaria
		configuration.put("_capacita_fin_label_check_progetto_approvato",
				"Il progetto è stato approvato con provvedimento dell'organo decisionale");
		
		// capacita_fin_label_check_progetto_inserito; questa variabile e' l'equivalente di _capacita_fin_label_check_progetto_approvato 
		// per il check box 'Il progetto e' inserito...'   
		configuration.put("_capacita_fin_label_check_progetto_inserito","");
		
		/** 
		 * Variabile di configurazione presente nel blocchetto: capacitaFinanziaria ...
		 * Il testo richiesto, se la variabile risulta avere contenuto, é il seguente:
		 * Capacit&#224; finanziaria, adeguatezza della struttura patrimoniale e/o della capacit&#224;
		 * economica in termini di affidabilit&#224; economico-finanziaria in rapporto all'intervento
		 * che deve essere realizzato, merito creditizio (non richiesto per i soggetti pubblici)
		 * */
		configuration.put("_capacita_fin_label_areatext_titolo","");
		
		
		// -------------------------------------------//
		// ------ caratteristicheProgettoNG---------- //
		// -------------------------------------------//
		
		// _progetto_caratteristiche_una_e_una_sola_tipologia; se true il controllo é
		// che sia stata selezionata una e una sola tipologia intervento; se false il
		// controllo é che sia stata selezionata almeno una tipologia intervento; usato
		// in model validation rules
		configuration.put("_progetto_caratteristiche_una_e_una_sola_tipologia", "false");
		
		// _progetto_caratteristiche_una_e_una_sola_tipologia_secondaria; se true il controllo é
		// che sia stata selezionata una e una sola tipologia intervento in tabella secondaria; 
		// se false il controllo é che sia stata selezionata almeno una tipologia intervento; usato
		// in model validation rules
		configuration.put("_progetto_caratteristiche_una_e_una_sola_tipologia_secondaria", "false");

		// _progetto_caratteristiche_uno_e_uno_solo_dettaglio; se true il controllo é
		// che sia stato selezionato uno e uno solo dettaglio per ciascuna tipologia
		// selezionata; se false il controllo é che sia stato selezionato almeno un
		// dettaglio per ciascuna tipologia selezionata; usato in model validation
		// rules
		configuration.put("_progetto_caratteristiche_uno_e_uno_solo_dettaglio", "false");
		
		/**
		 *  Jira: 1361
		 *  Per il bando:
		 *  Promozione del libro:
		 *  Se il beneficiario è pubblico deve poter selezionare solamente le tipologie di intervento 
		 *  	PROMOZIONE DEL LIBRO E DELLA LETTURA
		 *  	PREMI E CONCORSI LETTERARI
		 *  Se seleziona la tipologia di intervento 
		 *  	VALORIZZAZIONE DELLE ISTITUZIONI CULTURALI PIEMONTESI
		 *  	il sistema restituisce errore e visualizza il seguente messaggio:
		 *  	"Per i beneficiari pubblici non è ammessa la tipologia di intervento Valorizzazione delle istituzione culturali piemontesi"
		 */
		configuration.put("_progetto_caratteristiche_tipo_intervento", "false");
		
		// -------------------------------------------//
		// ------ poli ------------------------------- //
		// -------------------------------------------//
		// _poloAppartenenza_interpolo = true/false, visualizza/nasconde i campi
		// 'Interpolo' e 'Polo contributore' nella sezione _abstract ; usato in model
		// validation rules e S4_P1-->
		configuration.put("_poloAppartenenza_interpolo", "true");
		// _poloAssociazione_associato;  true/false, visualizza/nasconde i radio/button per la scelta esclusiva: 
		// Sono già associato / Non sono gia associato ad un Polo
		configuration.put("_poloAssociazione_associato", "false");
		
		/** Jira: 1523 -  Pass Studi fattibilita 
		 *  nserire un controllo che non permetta di inserire "no" 
		 *  - utente salva solo se radio-button valorizzato Si 
		 **/
		configuration.put("_poloAssociazione_associato_no_off", "false");
		
		// -------------------------------------------//
		// ------ abstractprogettoNG ----------------- //
		// -------------------------------------------//
		
		// _abstract_cup = true/false, visualizza/nasconde il campo Codice CUP nella
		// sezione _abstract
		configuration.put("_abstract_cup", "true");
		
		// _abstract_durata_prevista = numero intero, parametro per la verifica nella
		// MVR della sezione _abstract -->
		// i casi 999 e zero (0) sono gestiti in questo modo:
		// 999 non visualizza campo durata mesi e non esegue controlli
		// con lo (zero) 0 -> visualizza il campo ed esegue controlli di obbligatorieta e formato
		configuration.put("_abstract_durata_prevista", "12");
		
		// _abstract_acronimo = true/false, visualizza/nasconde il campo 'Acronimo
		// progetto' nella sezione _abstract ; usato in model validation rules e
		// S4_P1
		configuration.put("_abstract_acronimo", "true");
		
		// _abstract_ruolo = true/false, visualizza/nasconde il campo 'Ruolo del
		// presentatore nel progetto' nella sezione _abstract ; usato in model
		// validation rules e S4_P1
		configuration.put("_abstract_ruolo", "true");
		
		/** campo input solo numerico opzionale
		 *  - utilizzato sui bandi: Amianto
		 *  - blocchetto interessato: abstractProgettoNG
		 **/
		configuration.put("_abstract_campoNumericoOpzionale", "false");
		
		/**
		 * Controllo data inizio progetto e data inizio e fine progetto
		 * 
		 */
		configuration.put("_abstractProgetto_data_inizio_fine_progetto", "false");
		
		/**
		 * Abilito visualizzazione del solo campo-data-inizio-progetto ( senza data fine progetto )
		 * lavora in coppia con : _abstractProgetto_data_inizio_fine_progetto 
		 * entrambe le variabili di configurazione devono essere a (true) dal bando richiesto...
		 */
		configuration.put("_abstractProgetto_visualizza_solo_data_inizio_progetto", "false");
		
		
		/** label dinamiche per campi data inizio e data fine progetto */
		configuration.put("_abstractProgetto_label_data_inizio_progetto", "");
		configuration.put("_abstractProgetto_label_data_fine_progetto", "");
		
		/**
		 * Controllo data inizio progetto e data fine progetto
		 * per il bando Voucher Seconda edizione
		 * 
		 */
		configuration.put("_descrizioneFieraSecEd_data_inizio_fine_progetto_seconda_ed", "false");
		
		/************************************************************************
		 * Per bandi cultura: Jira: 1335
		 * 	Locazione: _abstractProgetto
		 * 	Inserimento di radio-button per correlazione bando
		 * _abstract_corealizzazione -> _abstract_corealizzazione
		 ************************************************************************/
		 configuration.put("_abstract_corealizzazione", "false"); 
		 
		//le seguenti erano presenti solo in Direttiva di demo bando cultura
		configuration.put("caratteristicheProgettoCustomLabel","");
		configuration.put("abstractProgettoCustomLabel","");
		configuration.put("titoloProgettoCustomLabel","");
		configuration.put("sintesiProgettoCustomLabel","");	
		
		
		// --------------------------------------------------//
		// ------ sediNG, dettaglioSede, cultDettaglioSede-- //
		// --------------------------------------------------//		

		// _sede_senza_tipo_sede = true/false, disabilita/abilita la gestione dei tipi_Sede e la validazione
		// in pratica se _sede_senza_tipo_sede = true il comportamento è quello tipico dei bandiCultura ***
		configuration.put("_sede_senza_tipo_sede", "false");

		configuration.put("_sedi_visualizza_nota_compilazione", "false");
		
		/** 
		 * nota dinamica personalizzabile cnfg per bando 
		 * blocchetto interessato: sediNG
		 * utilizzato in bando: 
		 * - asd covid19
		 **/
		configuration.put("_sedi_visualizza_nota_personalizzata", "");
		
		// _dettaglioSede_max_una_sede_intervento = true/false, attiva/disattiva il
		// controllo di presenza di non più di una sede intervento ; usato in model
		// validation rules, command validation rules e in S4_P2
		configuration.put("_dettaglioSede_max_una_sede_intervento", "false");
		
		/** recuperare e visualizzare solo Torino come provincia
		 *  blocchetto: dettaglioSede
		 *  bando utilizzatore:
		 *  - innometro
		 */
		configuration.put("_dettaglioSede_solo_prv_torino_attiva", "false");

		//se valorizzata indica il numero massimo di sedi consentito
		configuration.put("_num_max_sedi", "");			
		
		// _sedi_dichiarazione_sede_mai_attivata = true/false, visualizza/nasconde il
		// campo "Non sarà attivata alcuna sede in Piemonte" ; usato in model validation
		// rules e S4_P2
		configuration.put("_sedi_dichiarazione_sede_mai_attivata", "true");

		/** _sedi_dichiarazione_sede_non_attiva = true/false, visualizza/nasconde il
		 * campo "Dichiaro che la sede intervento non é ancora attiva sul territorio
		 * piemontese" e sara' attivata entro i termini stabiliti del bando e conformamente
		 * al Regolamento (UE) 651/2014  
		 * usato in model validation rules e S4_P2 */ 
		// true : visualizza la checkbox
		// false: nasconde la checkbox
		 configuration.put("_sedi_dichiarazione_sede_non_attiva", "true");
		
		// _dettaglioSede_riferimento_cartografico = true/false, abilita/disabilita la
		// sottosezione "Riferimento cartografico" ; usato in model validation rules e
		// in S4_P2_DETT_SEDE
		configuration.put("_dettaglioSede_riferimento_cartografico", "true");
		
		// _sedi_extra_Piemonte = true/false, consente di inserire anche sedi al di
		// fuori del territorio piemontese; usato in command_validation rules,
		// S4_P2_DETT_SEDE, S4_P2_RIC_NUOVA_SEDE
		// se false, recupera da db solo Province Piemontesi
		configuration.put("_sedi_extra_Piemonte", "false");

		// _sedi_estere = true/false, consente di inserire anche sedi all'interno
		// dell'Unione Europea, usato in command_validation rules e S4_P2_DETT_SEDE
		configuration.put("_sedi_estere", "true");
		
		//_sedi_sedi_intervento_richieste: da impostare a false per i bandi che non richiedono la presenza di almeno 
		// una sede intervento (ma potrebbero essercene anche piu' di una)
		//Per cui true è il caso normale, e nel caso in cui fosse false per coerenza _dettaglioSede_max_una_sede_intervento 
		//dovrebbe essere false (in quanto _dettaglioSede_max_una_sede_intervento e' di fatto usata, se true, per controllare 
		// che ci sia non piu' di una sede intervento)
		configuration.put("_sedi_sedi_intervento_richieste", "true");	
		
		//per evitare di impostare la nota compilazione della pagina sedi nell'inject, nel quale c'e' gia' una logica 
		// complessa per stabilire il testo corretto,
		//introduco questa variabile che se valorizzata contiene il testo stesso della nota di compilazione e ha la 
		// precedenza sul valore che verrebbe impostato
		//nell'inject
		configuration.put("_sedi_nota_compilazione", "");
		
		//visualizza/nasconde il check 
		configuration.put("visNotaAccordoPiemonteVDA", "false");
		
		// Jira: 1658 -  :: : visualizza una checkbox se il bando lo richiede
		configuration.put("_sedi_cb_settore_ateco_non_operativo", "false");
		
		//se valorizzata, il valore diventa la label del campo indirizzo pec del dettaglio sede; altrimenti la label e' quella di default scritta nell'html
		/** Modifica la seguente label di default:
		 *  Indirizzo	PEC utilizzato per le comunicazioni con A.d.G.
		 **/
		configuration.put("_sedi_indirizzo_pec_label", "");	
		
		/** Jira: 1761: campi pec e teelfono visibili se variabili a false */
		configuration.put("_cultDettaglioSede_senza_pec", "false");
		configuration.put("_cultDettaglioSede_senza_telefono", "false");
		

		// -------------------------------------------//
		// ------ premialitaProgettoNG--------------- //
		// -------------------------------------------//
		
		// _progetto_ctrl_premialita attiva/disattiva il controllo che sia stata
		// selezionata non più di una premialità
		configuration.put("_progetto_ctrl_premialita", "false");
		

		// -------------------------------------------//
		// ------ pianoSpese e cultPianoSpese-------- //
		// -------------------------------------------//
		
		//la seguente era presente solo nel cfg dei bandi cultura e visualizza o 
		// meno la Quota parte per spese generali e di funzionamento
		configuration.put("_progetto_spese_quota_inseribile", "false");
		
		//se valorizzato comporta che venga controllato sul salva delle spese e nel globalValidate 
		// che le voci di spesa associate al bando aventi descr_breve like %STE non superino una percentuale, 
		// data dalla variabile _limite_sup_spese_tecniche, del totale delle altre spese
		configuration.put("_limite_sup_spese_tecniche", "");		        

		//se valorizzato comporta che venga controllato sul salva delle spese e nel globalValidate 
		// che il totale delle spese non sia inferiore al suo valore
		configuration.put("_limite_inf_spese", "");

		//se valorizzato comporta che venga controllato sul salva delle spese che il totale delle spese 
		// non sia superiore al suo valore
		configuration.put("_limite_sup_spese", "");		
		
		// esclude la visualizzazione della colonna fornitore: false: nasconde, true: visualizza blocco di codice
		configuration.put("_fornitore_non_presente", "false");
		
		//se valorizzato viene visualizzato come warning sul salva della pagina delle spese quando il totale è inferiore al minimo ammesso
		configuration.put("_warning_limite_inf_spese", "");
		
		/**  
		 * Jira: 1337   - 
		 * - Configurazione importi da sportello
		 * */
		configuration.put("_cult_forma_agv_cfg_da_sportello", "false");
		
		
		/**  
		 * Jira: 2144 - 
		 * - Configurazione importi da sportello
		 * */
		configuration.put("_cult_forma_agv_cfg_da_sportello", "false");
		
		
		
		//nel blocchetto 'pianoSpese', serve per impostare un ordinamento customizzato delle voci di spesa
		//relative ad un intervento e visualizzate in sola lettura nella parte alta della pagina;  
		//se valorizzata, contiene la clausola order by da usare (la select e' su findom_v_voci_spesa che ha alias 'a') 
		configuration.put("_pianospese_ordinamento_voci_intervento", "");
		
		// -------------------------------------------------------------------//
		// ------ Dettaglio dei costi nel Tab: Spese -------------------------//
		// -------------------------------------------------------------------//
		/**
		 * Indicare gli importi in euro al netto dell'IVA, a meno che risulti indetraibile
		 * Colonna ( Descrizione Servizio/Bene )
		 * se colonna visibile, 	: _pianoSpese_dettaglio_costi_col_descrizione_visibile a "false"
		 * se colonna NON visibile	: _pianoSpese_dettaglio_costi_col_descrizione_visibile a "true"
		 */
		configuration.put("_pianoSpese_dettaglio_costi_col_descrizione_visibile", "false");
		
		/**
		 * Jira: 1809
		 * - sotto al dettaglio dei costi a video viene indicato:
		 * label std 		: Indicare gli importi in euro al netto dell'IVA, a meno che risulti indetraibile
		 * label dinamica	: (personalizzabile per specifico bando)
		 **/
		configuration.put("_pianoSpese_dettaglio_costi_label", "");
		
		/***
		 * nota personalizzabile per bandi cultura
		 * blocchetto interessato: cultPianoSpese
		 * bando che lo utilizza : asd coovid19
		 */
		configuration.put("_cultPianoSpese_nota_personalizzabile", "");
		
		/***
		 * 2nda nota personalizzabile 
		 * :: solo per bandi cultura
		 * blocchetto interessato: cultPianoSpese
		 * bando che lo utilizza : 
		 * beni librari 2020
		 */
		configuration.put("_cultPianoSpese_nota_personalizzabile_due", "");
		
		
		/***
		 * var di configurazione per bandi cultura
		 * blocchetto interessato: cultPianoSpese
		 * bandi che lo utilizzano : 
		 * - beniLibrari2020
		 * 
		 * Jira 2008:
		 * - il totale delle spese con descrVoceSpesa che inizia per B deve essere minore del totale delle altre spese;
		 * - non possono essere inserite solo voci di spesa tipo B;
		 * - non bloccante nella pagina
		 * - bloccante in validazione finale
		 */
		configuration.put("_cultPianoSpese_somme_parziali_subcatg", "false");
		
		
		/***
		 * var di configurazione per bandi cultura
		 * blocchetto interessato: cultPianoSpese
		 * bandi che lo utilizzano : 
		 * - beniLibrari2020
		 * 
		 * Jira ?!?  ?!? :
		 * - la var di cfg a true, permette di eseguire verifiche sul totale delle spese
		 *   recuperando i dati degli importi (min) e (max) per bando.
		 *   Ulteriori controlli sono descritti nella jira.
		 * - call procedure di riferimento ( fn_findom_get_param )
		 */
		configuration.put("_cultPianoSpese_procedure_tot_spese", "false");
		
		
		// -------------------------------------------//
		// ------   pianoSpeseSemplificato   -------- //
		// -------------------------------------------//
		

		// Regola S81
		// Il Sistema verifica che nella pagina Spese non siano stati inseriti valori decimali negli importi
		// se valorizzato indica il numero di decimali desiderati
		configuration.put("_num_decimali_spese", "");
	
		// Label visualizzata dal blocchetto, se non definita o null viene visualizzata la label "Piano delle spese"
		configuration.put("_piano_spese_custom_label", "Spese riferite esclusivamente alla stagione sciistica di riferimento");

		// ------------------------------------------------//
		// ------ formaFinanziamento, tipologiaAiutoNG---- //
		// ------------------------------------------------//

		// visualizzazione o meno del campo "Importo dell'agevolazione pubblica
		// richiesta" in alternativa alla colonna 'Importo' nella tabella delle forme di
		// agevolazione; 
		// se true il campo é presente, é mappato nell'xml su importoRichiesto e la
		// colonna non si visualizza; se false avviene il contrario e in questo caso
		// nell'xml su importo_richiesto é mappato il totale dell'ultima riga in tabella
		// 
		// usato in model validation rules e in S4_P4 (template e inject)
		// se false comporta che deve essere gestita su pdf_template.xhtml la tabella
		// con colonna importo; se true la colonna non c'é -->
		// la trasformata non é influenzata da questa configurazione: il campo
		// 'importo_forma_finanziamento' (in forme_finanziamento) é sempre presente e
		// non viene valorizzato quando la colonna 'Importo' non c'é (caso true)
		// 
		configuration.put("_progetto_agevolrichiesta_importo_unico", "false");
		
		// _progetto_agevolazione_uno_e_uno_solo_tipo_aiuto; se true il controllo é che
		// sia stato selezionato uno e uno solo tipo di aiuto; se false il controllo é
		// che sia stata selezionato almeno un tipo aiuto; usato in model validation
		// rules
		/**
		 * true : uno e uno solo tipo di aiuto;
		 * false: selezionare almeno un tipo aiuto;
		 **/
		configuration.put("_progetto_agevolazione_uno_e_uno_solo_tipo_aiuto", "false");
		
		// _progetto_agevolazione_uno_e_uno_solo_dettaglio; se true il controllo é che
		// sia stato selezionato uno e uno solo dettaglio per ciascun tipo aiuto
		// selezionato; se false il controllo é che sia stato selezionato almeno un
		// dettaglio per ciascuna tipo aiuto selezionato; usato in model validation
		// rules 
		/**
		 * true : uno e uno solo dettaglio per ciascun tipo aiuto
		 * false: almeno un dettaglio per ciascun tipo aiuto selezionato
		 **/
		configuration.put("_progetto_agevolazione_uno_e_uno_solo_dettaglio", "false");
		
		// _progetto_agevolazione_una_e_una_sola_forma_fin; se true il controllo é che
		// sia stata selezionata una e una sola forma di finanziamento; se false il
		// controllo é che sia stata selezionata almeno una forma di finanziamento;
		// usato in model validation rules
		/**
		 * true	: selezionare una e una sola forma di finanziamento;
		 * false: selezionare almeno una forma di finanziamento;
		 **/
		configuration.put("_progetto_agevolazione_una_e_una_sola_forma_fin", "false");
		
		// utile per agevolazioni senza dettaglio: Vedi UNESCO
		configuration.put("_progetto_agevolazione_nessun_dettaglio", "false");
		
		/**
		 * Bando con richiesta agevolazione
		 * >= €  30.000.00 e 
		 * <= € 200.000.00
		 * richiesto per esempio in:
		 * Bando Cinema
		 */
		configuration.put("_progetto_agevolrichiesta_importo_attivita_produttive", "false");
		
		//se valorizzate, le seguenti due variabili specificano l'importo massimo erogabile in agevolazione richiesta differenziati
		//in base a una qualche caratteristica (tipicamente il flag pubblico privato delle tipologia di beneficiario); 
		// uso dei nomi generici "_1" e "_2" per non legare le variabili a specifiche condizioni.
		//Se valorizzate sovrascrivono l'eventuale valore presente su findom_t_bandi, colonna importo_massimo_erogabile
		configuration.put("_progetto_agevolrichiesta_max_erogabile_1", "");
		configuration.put("_progetto_agevolrichiesta_max_erogabile_2", "");	
		
		//se valorizzata, il suo valore viene visualizzato come titolo del toggle 'altre spese' del blocchetto altreSpese
		configuration.put("_altreSpese_label_toggle","");
				
		// -------------------------------------------//
		// ------ formaFinanziamento custom --------- //
		// -------------------------------------------//
		/**
		 * Se a true
		 * viene elaborato il calcolo del massimo importo richiesto
		 * il quale sarà in riferimento ad una variabile in %
		 * Esempio:
		 * % (a) percentuale    =   80 (ricavata da query su t_bandi)
		 *   (b) totaleSpese    = 1000 (come esempio)
		 *   (c) percMaxImporto = (a * b)/100 => 800
		 *   Verifica: 
		 *   	se importoRichiesto risulta ( <= 80% ) del totaleSpese ( salva i dati )
		 *   quindi: 
		 *   	se importoRichiesto = 1000 (non salva)
		 *      se importoRichiesto =  800 (salva)
		 */
		configuration.put("_progetto_agevolrichiesta_max_importo_perc_var", "false");
		
		/**
		 * Jira: 1646 -  
		 *  - Modifica al calcolo del contributo in Agevolazione richiesta
		 *  
		 *  Nella pagina Agevolazione richiesta,
		 *  saranno presenti varie (Forme di agevolazione).
		 *  In base alla configurazione del bando,
		 *  puo essere richiesto di nascondere campi di importo.
		 *  
		 *  Gli (id) dei campi di importo da nascondere sarranno settati nel file (Direttiva)
		 *  del bando interessato.
		 *  
		 *  Esempio:
		 *  configuration.put("_forma_finanziamento_id_checkbox_nascoste", "10,19");
		 */
		configuration.put("_forma_finanziamento_is_checkbox_nascoste", "false");
		configuration.put("_forma_finanziamento_id_checkbox_nascoste", "");

		// -------------------------------------------//
		// ------ dichiarazioni**-------------------- //
		// -------------------------------------------//
		
		// _dichiarazioni_dichiarazioni = true/false, abilita/disabilita la sottosezione
		// "dichiarazioni"
		configuration.put("_dichiarazioni_dichiarazioni", "true");

		// _dichiarazioni_impegni = true/false, abilita/disabilita la sottosezione
		// "impegni"
		configuration.put("_dichiarazioni_impegni", "true");

		// _dichiarazioni_autorizzazioni = true/false, abilita/disabilita la
		// sottosezione "autorizzazioni"
		configuration.put("_dichiarazioni_autorizzazioni", "true");

		
		///////////////////////////////////////////////////////////////////////////////////
		//                                VARIABILI CUSTOM                               //
        ///////////////////////////////////////////////////////////////////////////////////
		
		// _costituzioneImpresa_data_solo_primo_salvataggio = true/false, non consente/consente di salvare 
		// la data costituzione più di una volta
		configuration.put("_costituzioneImpresa_data_solo_primo_salvataggio", "true");
		
		/**
		 * Jira: 1590:
		 * - dove: pagina beneficiario
		 * - Aggiungere campo Iscrizione al Registro Imprese
		 * - Il campo deve contenere una data non posteriore alla data odierna 
		 * 		e deve essere configurabile per Bando
		 */
		configuration.put("_costituzioneImpresa_data_iscrizione_registro_imprese", "false");

		//Bilancio
		configuration.put("_bilancio_anno_readonly", "false");
		
		//BilancioPrev 
		configuration.put("_bilancioPrev_anno", "false");
		configuration.put("_bilancioPrev_speseRS", "false");		
		configuration.put("_bilancioPrev_creditiCommScad", "false");		
		configuration.put("_bilancioPrev_disponibilitaLiquide", "false");		
		configuration.put("_bilancioPrev_totaleBilancio", "false");		
		configuration.put("_bilancioPrev_totalePatrimonio", "false");		
		configuration.put("_bilancioPrev_debitiSoci", "false");		
		configuration.put("_bilancioPrev_debitiBanche", "false");		
		configuration.put("_bilancioPrev_debitiFornScad", "false");
		configuration.put("_bilancioPrev_debitiImpreseCollegate", "false");		
		configuration.put("_bilancioPrev_debitiControllanti", "false");		
		configuration.put("_bilancioPrev_debitiTributariScad", "false");		  
		configuration.put("_bilancioPrev_ricavi", "false");		
		configuration.put("_bilancioPrev_totaleValoreProduzione", "false");		
		configuration.put("_bilancioPrev_variazioneLavoriInCorso", "false");		
		configuration.put("_bilancioPrev_ammortamentiImm", "false");		
		configuration.put("_bilancioPrev_ammortamentiMat", "false");		
		configuration.put("_bilancioPrev_totaleCostiProduzione", "false");		
		configuration.put("_bilancioPrev_proventiFinanziari", "false");		
		configuration.put("_bilancioPrev_interessiPassivi", "false");		
		configuration.put("_bilancioPrev_proventiGestioneAccessoria", "false");		
		configuration.put("_bilancioPrev_oneriGestioneAccessoria", "false");		
		configuration.put("_bilancioPrev_ebitda", "false");		
		configuration.put("_bilancioPrev_ebit", "false");		
		configuration.put("_bilancioPrev_indiceRotazione", "false");		
		configuration.put("_bilancioPrev_dso", "false");		
		configuration.put("_bilancioPrev_dpo", "false");		
		configuration.put("_bilancioPrev_ula", "false");		
		configuration.put("_bilancioPrev_debitiVsFornitori", "false");		
		configuration.put("_bilancioPrev_debitiTributari", "false");		
		configuration.put("_bilancioPrev_creditiVsClienti", "false");		

		configuration.put("_ente_impresa_dati_bilancio_custom", "false");

		// Lasciare a true : per bandi std
		configuration.put("_abstract_titoloProgetto", "true");

		// _abstract_titolo_nascosto = true/false, attiva/disattiva la
		// visualizzazione del titolo a video, ma lo rende presente nel file xml; 
		configuration.put("_abstract_titolo_nascosto", "false");
		
		/**
		 *  _sedi_relative_progetto_proposto_legale_operativa
		 *  consente se a true, di visualizzare un msg riferito a sedi di tipo:
		 *  - legale/operativa
		 *  - legale
		 *  - operativa
		 */
		configuration.put("_sedi_relative_progetto_proposto_legale_operativa", "false");
		
		
		/**
		 *  _tipi_sede_legale_operativa
		 *  consente se a true, di visualizzare un msg riferito a sedi di tipo:
		 *  - legale/operativa
		 *  - legale
		 *  - operativa
		 */
		configuration.put("_tipi_sede_legale_operativa", "false");
		
		/**
		 *  _statoEstero_legale_operativaid_flagSedeNoPiemonte
		 *  in Progetto > Sedi
		 */
		configuration.put("_statoEstero_legale_operativa", "false");
		
		configuration.put("_progetto_agevolazione_due_forme_fin", "true");

		//_progetto_caratteristiche_due_dettagli; se impresa, e bando custom, deve selezionare entrambi i dettagli per una 
		//ed una sola tipologia
		configuration.put("_progetto_caratteristiche_due_dettagli", "true");

		// _caricamento_custom_dett_intervento;  se true il controllo visualizzerà tipo di dettaglio personalizzato 
		// al bando PoliC; se false il controllo visualizzato resta quello standard S4_P1 
		// Se true, sono enduser == 2, bando custom, stampo dettagli solo agevolazioni fondo perduto per ogni tipologia
		configuration.put("_caricamento_custom_dett_intervento", "false");
		
		/**
		 * _requisitiMerito_checkbox:
		 *  utile per visualizzare/non visualizzare blocco codice con relative checkbox
		 *  usato ad esempio in Voucher-fiere
		*/
		configuration.put("_requisitiMerito_checkbox", "false");
		
		
		/**
		 * _requisitiMeritoSecEd_checkbox:
		 *  utile per visualizzare/non visualizzare blocco codice con relative checkbox
		 *  usato ad esempio in Voucher-fiere II Edizione
		*/
		configuration.put("_requisitiMeritoSecEd_checkbox", "false");
		
		/**
		 * Priorita
		 * Variabile di configurazione per bandi
		 * che utilizzano su specifica il blocchetto:
		 * - priorita
		 * Esempio: Sistema neve B1 
		 * Note: sostituire checkbox con radio-button
		 */
		configuration.put("_priorita_checkbox", "false");
		
		/**
		 * Nazione estera da selezionare
		 * eccetto Italia
		 * usato in 
		 * descrizioneFiera
		 * 
		 */
		configuration.put("_descrizioneFiera_nazioneSenzaItalia", "false");
		
		/**
		 * CR-1: Vooucher
		 * la seguenti variabili risultano presenti solo in Direttiva di Voucher
		 * 
		 * _caratteristicheProgettoTipoBeneficiario:
		 *  utile per visualizzare/non visualizzare il campo: (Tipologia beneficiario)
		 *  in Progetto > Informazioni di progetto:
		 *  
		 *  _abstract_titoloProgetto:
		 *   utile per nascondere il campo, quando il bando lo richiede, nella sezione:
		 *   Progetto > Informazioni di progetto: (Titolo)
		 */
		configuration.put("_caratteristicheProgettoTipoBeneficiario","false");

		// ------ ANAGRAFICA ENTE IMPRESA - DICHIARAZIONI ----------
		// _dichiarazioni_view_sezione_nota_integrativa_bilancio, se true viene visualizzata la
		// corrispondente sezione
		configuration.put("_dichiarazioni_view_sezione_nota_integrativa_bilancio", "true");

		/**
		 * Dimensioni d'impresa:
		 * per Voucher non richiesto: Grande
		 */
		configuration.put("_assenza_dimensioneImpresa_grande", "false");
		
		/**
		 * Variabile di configurazione
		 * utile per i bandi che richiedono
		 * per il beneficiario= Piccola media impresa (code: SME), in Bando VoucherIR 
		 * la visualizzare su blocchetto: Dimensioni
		 * deve risultare come segue:
		 * - micro, piccola o media 		( se variabile a true e beneficiario essere :  SME )
		 * - micro, piccola, media, grande 	( se variabile a false e beneficiario essere:  AER )
		 * Esempio: VoucherIR
		 * 
		 * */
		configuration.put("_dimensione_no_grande_tipol_beneficiario", "false");

		/**
		 * S4_P7
		 * su richiesta da specifiche di bando
		 * mettere a true tale variabile di configurazione
		 * abilita la visualizzazione di una descrizione custom
		 * Esempio: Voucher Jira: 900
		 */
		configuration.put("_autosost_economica_motivazione", "false");

		/**
		 *  _dettaglioSede_pec = true/false, abilita/disabilita 
		 *  campo della pec
		 */
		configuration.put("_dettaglioSede_pec", "false");

		/**
		 *  _dettaglioSede_tipoSede_checkbox = true/false, abilita/disabilita 
		 *  tipo sede con checkbox
		 */
		//non usata configuration.put("_dettaglioSede_tipoSede_checkbox", "false");

		/**
		 *  _dettaglioSede_tipoSede_select = true/false, abilita/disabilita 
		 *  tipo sede con select
		 */
		//non usata configuration.put("_dettaglioSede_tipoSede_select", "false");

		/**
		 * Selezionare solo un singolo dettaglio
		 * in Dettaglio intervento in caratteristicheProgettoNG
		 * 
		 */
		configuration.put("_caratteristicheProgettoNG_singoloDettaglio", "false");
		
		 /**
		 * bonus covid Piemonte
		 * - recupero importoContributo da tabella: findom_t_soggetti_bonus_covid
		 *   mediante cf del soggetto collegato per la richiesta di un importo
		 *   definito in tabella e in base al codiceAteco del soggetto.
		 * : bando bonusPiemonte
		 * : blocchetto interessato: caratteristicheProgettoNG
		 * 
		 */
		configuration.put("_caratteristicheProgettoNG_imp_contr_covid", "false");
		
		/**
		 * bonus cultura 2020 (covid)
		 * - recupero importoContributo da tabella: 
		 * 1) findom_t_soggetti_abilitati 
		 * 2) findom_r_bandi_soggetti_abilitati
		 * tramite cf del soggetto connesso in sessione...
		 * e salvo su xml da caratteristicheProgetto
		 */
		configuration.put("_caratteristicheProgettoNG_imp_contr_by_sogg_abilitati", "false");


		/**
		 * Bando con richiesta limite agevolazione
		 * in base alla nazione scelta:
		 * Se in Europa: limite <=5000 €
		 * Se fuori Europa = <=7000€
		 */
		configuration.put("_progetto_agevolrichiesta_importo_europeo", "false");
		
		/**
		 * Bando con richiesta limite agevolazione
		 * in base alla nazione scelta:
		 * Se in Europa: limite <=5000 €
		 * Se fuori Europa = <=7000€
		 * solo per bando Voucher Seconda Edizione 2019
		 */
		configuration.put("_progetto_agevolrichiesta_importo_europeo_voucher_sec_ed", "false");

		// _sedi_selezione_min_una = true/false, attiva/disattiva il
		// controllo di presenza di almeno una sede selezionata; 
		configuration.put("_sedi_selezione_min_una", "false");	

		/**
		 * _sedeLegale_recapiti_versione_A
		 * se true, nasconde email come richiesto da Sottosezione Recapiti
		 * Versione A:
		 * - Telefono
		 * - Indirizzo PEC
		 */
		//non usata configuration.put("_sedeLegale_recapiti_versione_A", "false");
		
		// 08/10/2018: ASR :: controllo bando con richiesta calcolo importo min e/o max; 
		configuration.put("_forma_fin_importo_max_erogabile", "false");
		
		/**
		 * Jira: 1158
		 * Bando: ASR 2018
		 * Non viene richiesta la dicitura presente nella sezione Indicatori std.
		 * "Nel caso in cui vengano compilati indicatori di tipo 'Ante' è obbligatorio compilare i corrispondenti indicatori di tipo 'Post'"
		 * 
		 */
		configuration.put("_indicatori_tipo_ante_post", "false");
		
		/**
		 * Jira: 1935
		 * Richista controllo sul salva del valore singolo indicatore
		 * - blocchetto interessato: indicatori
		 * - bando che ne fa uso: asd covid19
		 * 
		 */
		configuration.put("_indicatori_controllo_valore_indicatore", "false");
		
		/** Regole di compilazione per Bando-00_bis
			con regole per bandi dematerializzati, rif.to: Jira: 1164 */
		configuration.put("_rgl_comp_demat_bando_00_bis", "false");
		
		/** Nel caso in cui un bando non necessiti di visualizzare 
		 *  la nota con riferimento a sede legale/operativa
		 *  se false: non visualizza la nota legale/operativa
		 *  se true : visualizza la nota legale/operativa
		 * */
		configuration.put("_sede_leg_oper_amm_detgl_int_max_una_sede_int", "false");
		
		/** messaggio custom per Empowerment Finanziamento */
		configuration.put("_datiBilancio_bilanci_chiusi_customLabel", "false");
		
		///////////////////////////////////////////////////////////////////////////////////
		// Variabile di configurazione per il bando Sistema neve 						 //
		//////////////////////////////////////////////////////////////////////////////////
		configuration.put("_caratteristicheProgettoNG_idTipoIntervento_99", "false");

		///////////////////////////////////////////////////////////////////////////////////
		//         VARIABILI PER CHIAMATE A METODI DI VALIDAZIONE AGGIUNTIVI             //
		///////////////////////////////////////////////////////////////////////////////////

		//validationMethodsAbstractProgettoNG:
		//-definisce la lista, eventualmente vuota, dei metodi da eseguire nel modelValidate() del blocchetto a cui si riferisce, 
		// in questo caso abstractprogettoNG
		//-a regime di questo tipo di variabili di configurazione ce ne dovrebbe essere al piu' una per blocchetto
		//-se sovrascritta nella classe Direttiva di un bando la si valorizza tramite un metodo che restituisce una stringa json 
		// contenente i metodi (piu'parametri) da chiamare -nella classe Input del blocchetto, AbstractProgettoNGInput in questo caso, 
		// se e' presente un attributo mappato su questa variabile, deve essere public e deve
		// iniziare per "validationMethods" -se si aggiunge in FinDirettiva una variabile di questo tipo significa che 
		// per il blocchetto a cui la variabile di riferisce, per un certo bando servono validazioni
		// aggiuntive, per cui la classe Input del blocchetto dovra' definire un attributo omonimo e public. 
		// I bandi che non necessitano per quel blocchetto di validazioni
		// aggiuntive ereditano da FinDirettiva la variabile di configurazione pari a "" e per loro la validazioni aggiuntive non verranno eseguite
		configuration.put("validationMethodsAbstractProgettoNG","");

		//contiene le informazioni su eventuali metodi di validazione da chiamare dal modelValidate del blocchetto premialitaProgettoNG
		configuration.put("validationMethodsPremialitaProgettoNG","");

		//contiene le informazioni su eventuali metodi di validazione da chiamare dal modelValidate del blocchetto formaFinanziamento
		configuration.put("validationMethodsFormaFinanziamento","");

		//contiene le informazioni su eventuali metodi di validazione da chiamare dal modelValidate del blocchetto caratteristicheProgettoNG
		configuration.put("validationMethodsCaratteristicheProgettoNG","");
		
		//contiene le informazioni su eventuali metodi di validazione da chiamare dal modelValidate del blocchetto caratteristicheProgettoSecNG
		configuration.put("validationMethodsCaratteristicheProgettoSecNG","");
		
		//contiene le informazioni su eventuali metodi di validazione da chiamare dal modelValidate del blocchetto sediNG
		configuration.put("validationMethodsSediNG","");
		
		//contiene le informazioni su eventuali metodi di validazione da chiamare dal modelValidate del blocchetto TipologiaAiutoNG
		configuration.put("validationMethodsTipologiaAiutoNG","");
		
		configuration.put("_abstract_titoloProgettoNeve", "false");
		
		configuration.put("_relazioni_assunzioni_progetto", "false");

		configuration.put("_progetto_caratteristiche_tipo_intervento_beneficiario_custom", "false");
		
		/**
		 * se il bando attiva a true questa variabile di cfg
		 * nel blocchetto formaFinanziamento elaborerà
		 * controlli nel model validate in base 
		 * alle specifiche richieste per il bando...
		 * Esempio: VoucherIR, Empowerment contributo...
		 * - importo richiesto <= (perc_contributo_massimo_erogabile * totale_spese)
		 * - importo richiesto >= importo_minimo_erogabile
		 * - importo richiesto <= importo_massimo_erogabile 
		 */
		configuration.put("_progetto_forma_finanziamento_custom", "false");
		
		/** 
		 * Jira: 1525
		 * Label custom per Empowerment contributo
		 * - se la variabile a true, visualizzera il seguente msg:
		 * true		/ Specificare le U.L.A. (Unit&#224; lavorative annue) rilevate al momento della presentazione della domanda sulla Misura Empowerment Internazionale - Linea A
		 * false 	/ U.L.A. (Unit&#224; lavorative annue) rilevate al momento della presentazione della domanda
		 * */
		configuration.put("_dimensioni_lblRisorseUmaneCustom", "false");
		configuration.put("_risorseUmaneAttivate_lblRisorseUmaneAttivateCustom", "true");
		
		/** Jira: 1549 -  recupero importi da database: min, max, percentuale */
		configuration.put("_progetto_forme_finanziamento_custom","false");
		
		/** Label custom per Template blocchetto: PrioritaNG : usato in Sistema Neve B1/B2 */
		configuration.put("labelPrioritaTemplate","Priorita");
		
		/** Variabile di configurazione custom per blocchetto: PrioritaNG : 
		 * Esempio: Sistema Neve B1 or B2 
		 * - true: visualizza bloco di codice: Inserimento in Studio Uncem
		 * - false: NON visualizza Inserimento in Studio Uncem
		 * */
		configuration.put("_inserimentoStudioUncem","false");
		
		/**
		 * Jira: 1561
		 * - Aggiunta radio-button per progetti in collaborazione e relativo controllo 
		 * - Possibili scelte: 
		 * 		1. Progetti / Studi in collaborazione 
		 * 		2. Nessuna collaborazione
		 * 
		 * Se l'utente seleziona la tipologia di intervento
		 * 	"Servizi qualificati di Ricerca ed Innovazione"
		 * 	e sceglie "Progetti / Studi in collaborazione" nel radio button,
		 *  deve essere restituito il seguente errore: 
		 * (msg): 	La collaborazione &#233; ammessa solamente per le tipologie di intervento 
		 * 			'Progetti di ricerca e sviluppo' oopure per
		 * 			'Studi di fattibilit&#224; tecnica'
		 */
		configuration.put("_abstract_rb_collaborazione", "false");
		
		
		// ------ dimensioniNG----------------------- //
		/**
		 * Jira: 1587
		 * _dimensioni_risorse_umane
		 * Se true	: visualizza blocco di codice per i bandi che ereditano configurazione da FinDirettiva
		 * Se false	: nasconde blocco di codice al bando che ha in Direttiva la variabile a false
		 */
		configuration.put("_dimensioni_risorse_umane", "true");
		
		/**
		 * Jira ancora mancante: 05/05/2020
		 * Se il bando non deve visualizzare la select-option dimensioni impresa,
		 * setta la variabile a true.
		 * Di default a false, per Findirettiva... 
		 **/
		configuration.put("_dimensioni_dim_impresa", "false");
		
		/**
		 * Jira: 1588
		 * Dimensione selezionabile solo: micro e piccola (NO media)
		 */
		configuration.put("_assenza_dimensioneImpresa_media", "false");
		
		/**
		 * Jira: 1589:
		 * _importo_complessivo_business_plan
		 * Dove: Pagina Informazioni sul progetto 
		 * Cosa: aggiungere campo (Importo complessivo del business plan)
		 * Tipo campo: (Campo numerico con importo con al massimo due decimali) 
		 */
		configuration.put("_importo_complessivo_business_plan", "false");
		
		
		/**
		 * Jira: 1592:
		 * - dove: pagina Profilo dell'impresa
		 * - Aggiungere colonna alla tabella dei soci
		 * - : Tipologia Soci  
		 * - deve essere configurabile per Bando
		 */
		configuration.put("_azienda_col_tipologia_soci", "false");
		
		
		/**
		 * Jira: 1600:
		 * - dove: pagina Profilo dell'impresa
		 * - Modificare controllo su Codice Fiscale
		 * - deve essere configurabile per Bando
		 */
		configuration.put("_bypassa_verifica_codice_fiscale_estero", "false");
		
		
		/**
		 * CR-3: Condomini : controllo sospeso non piu richiesto...
		 * Controllo coerenza CFiscale persona fisica
		 * in base ai dati inseriti nelle pagini:
		 * - LegaleRappresentante 
		 * - SoggettoDelegato 
		 **/
//		configuration.put("_controllo_coerenza_codice_fiscale_persona_fisica", "false");
		
		/**
		 * :: 
		 * Implemento una var di configurazione
		 * presente in alcuni blocchetti,
		 * assente in Direttiva e Findirettiva
		 * 
		 */
		configuration.put("tipolInterventoPresente", "false");
		
		/**
		 * Jira: 1697
		 * - Gestione sedi estere 
		 */
		configuration.put("_dettaglio_sedi_stati_esteri", "false");
		
		/**
		 * Jira: 1697
		 * Gestione sedi estere:
		 * - label dinamica: In data
		 */
		configuration.put("_dettaglio_sedi_stati_esteri_label", "");
		
		/** Jira: 1705: Campi obbligatori per la gestione del bilancio */
		configuration.put("_campi_bilancio_obbligatori", "false");
		
		/** Metodo  : Dichiarazioni dinamiche */
		configuration.put("_dichiarazione_provvedimentiAutorizzatori", "false");
		
		/** Visualizza campi recapiti se variabile a true
		 *  Campi: [pec, telefono, email] 
		 *  in blocchetto: beneficiarioPF
		 **/
		configuration.put("_beneficiarioPF_presenza_recapiti", "false");
		
		/** Questa variabile lavora in coppia con la:
		 *  1) _beneficiarioPF_presenza_recapiti
		 *  e la
		 *  2) _beneficiarioPF_pec_facoltativa
		 *  
		 *  Se il bando interessato necessita di una pec nel blocchetto beneficiarioPF 
		 *  senza obbligo di compilazione, 
		 *  occorre che metta a (true) le 2 variabili di configurazione sopra descritte (1) e (2)
		 *  in blocchetto: beneficiarioPF
		 **/
		configuration.put("_beneficiarioPF_pec_facoltativa", "false");
		
		/** relativa label pec custom per blocchetto beneficiarioPF */
		configuration.put("_beneficiarioPF_indirizzo_pec_label", "false");
		
		/** 
		 * Jira: 1671 -  
		 * verifica custom agevolazione richiesta in base ai limiti: min, max, percentuale
		 * configurabile per bando
		 **/
		configuration.put("_progetto_forme_finanziamento_imp_min_max_by_bando","false");
		
		
		/**
		 *  Jira: 1772 - 
		 *  cessione del credito configurabile per bando 
		 */
	    configuration.put("_forma_finanziamento_cessione_credito", "false");
	    
	    /**
		 *  Jira: 1797 - 
		 *  bypass controllo valore indicatore se flag_alfa presente nella tabella: findom_r_bandi_indicatori
		 *  risulta essere a true ed il valore indicatore può risultare alfanumerico...
		 *  Se false esegue il controllo sul valore indicatore (numerico e decimale)
		 *  configurabile per bando
		 */
	    configuration.put("_indicatori_flag_alfa", "false");
	    
	    /**
		 *  Jira:  - 
		 *  Il bando Asd covid, richiede ordine inverso della descrizione voci 
		 *  in tabella indicatori.
		 *  Operazione richiesta lato codice java e non da database.
		 *  Eseguita con variabile di configurazione per bando.
		 */
	    configuration.put("_indicatori_reverse_descrizione_list", "false");
	    
	    /**
		 *  Jira: 1809 - 
		 *  - testo dinamico per toggle in blocchetto: SedeLegale
		 */
	    configuration.put("_sedeLegale_toggle_dinamico_label", "");
	    
	    
	    /**
		 *  Jira: 1807 FSE - 
		 *  - Visualizzare solo province del piemonte
		 *  - Provincia (select)
		 *  - Comune (select)
		 *  configurabile per bando
		 *  nel blocchetto: sedeLegale
		 */
	    configuration.put("_sedeLegale_solo_province_piemonte", "false");
	    
	    
	    /**
		 *  Jira: 1824 - 
		 *  - testo dinamico per blocchetto: legalerappresentante
		 *  	La domanda deve essere firmata digitalmente dal legale rappresentante o dal soggetto delegato (se previsto dal bando).
		 */
	    configuration.put("_lr_domanda_firmata_dgt_label", "La domanda deve essere firmata digitalmente dal legale rappresentante o dal soggetto delegato (se previsto dal bando)");
	    
	    
	    /**
		 *  Jira: 1842 FSE - 
		 *  - Visualizzare per la residenza solo province del piemonte
		 *  - Provincia (select)
		 *  - Comune (select)
		 *  configurabile per bando
		 *  nel blocchetto: legalerappresentante
		 */
	    configuration.put("_legalerappresentante_res_prov_piemonte", "false");
	    
	    
	    /**
		 *  Jira: 1846 DemoCulturaBis - 
		 *  - Per evitare regressione sui bandi cultura
		 *  - selezionando la (select) Provincia
		 *  si richiama un evento reload della pagina con record 1100 configurato su database
		 *  Alcuni bandi con cfg custom, necessitano di eseguire reload della pagina con record 1300 o 1400
		 *  a seconda della configurazione del bando.
		 *  Blocchetto interessato: sedeLegale
		 */
	    configuration.put("_sedeLegale_col_rec_1300", "false");
	    
	    /**
	     * Legame tra criteri (blocchetto schedaProgetto) e tipologie di intervento (blocchetto CaratteristicheProgettoNG) 
	     * Se la variabile è TRUE allora la sezione "Scheca Progetto" si attiva solo dopo aver compilato 
	     * i dati CaratteristicheProgettoNG generalmente nella pagina "Informazioni sul progetto/iniziativa" 
	     * nella sezione "Progetto/iniziativa"
	     * Blocchetto interessato: schedaProgetto
	     */
	    configuration.put("_schedaprogetto_dipendeda_caratteristicheprogetto", "false");
	    
	    /**
	     * Modifica richiesta per bando: Sostegno imprese
	     * Se beneficiario risulta: 
	     * - Lavoratore autonomo
	     * 		la query estrae come forma giuridica solo : (Lavoratore autonomo)
	     * - Piccola/Media/ impresa
	     * 		la query estrae come forma giuridica tutto ad eccetto di (Lavoratore autonomo)
	     * Blocchetto interessato: operatorePresentatore
	     */
	    configuration.put("_operatorePresentatore_formaGiuridica_lavoratoreAutonomo", "false");
	    
	    /**
	     * Modifica richiesta per bando: Sostegno imprese
	     * Se in pagina beneficiario: 
	     * - visualizzare Messaggio di alert configurabile per bando
	     * Blocchetto interessato: operatorePresentatore
	     * 	template.xhtml
	     */
	    configuration.put("_operatorePresentatore_mex_alert", "false");
	    
	    /** 
	     * Usata per bando: Innometro
		 * verifiche da eseguire:
		 *  - importo fondo perduto
		 *  - conto interesse 
		 *  - totale imprto agevolazioni
		 *  
		 * secondo valori presenti in tabella: findom_r_bandi_forme_finanziamenti e findom_d_forme_finanziamenti
		 * - perc_prevista: 20%
		 * - importo_massimo_erogabile: 10000.00
		 * - limite inferiore per spese: 5000.00
		 **/
		configuration.put("_formaFinanziamento_ImpMinMaxPercByDb","false");
		
		/** richiesta verifica duplicazione IBAN : */
		configuration.put("_estremiBancari_duplicazioneIban", "false");
		
		/** campo bic obbliagtorio opzionale se true */
		configuration.put("_estremiBancari_bicObbligatorio", "false");
		
		/***
		 * nota personalizzabile per bandi che lo richiedano
		 * blocchetto interessato: estremiBancari
		 * bando che lo utilizza : asd coovid19
		 * 
		 * IMPORTANTE:
		 * Per i prossimi bandi cultura 2020, ricordare di mettere a true la variabile
		 * nel bando interessato: rif.: @lv
		 * - sport
		 * - beni librari 2020
		 * - spettacolo 
		 */
		configuration.put("_estremiBancari_nota_personalizzabile", "");
		
		/**
		 * var di cfg per bando Aps: jira 2018
		 * se la var-cfg risulta a (true), 
		 * nel blocchetto: estremiBancari
		 * verrà recuperato da xml il valore della denominazione (ragione sociale) 
		 * e con tale valore verrà precompilato il campo: Intestatario del c/c in estremi bancari
		 * e salvato su xml. Il campo Intestatario c/c sarà read-only 
		 */
		configuration.put("_estremiBancari_intestatario_denominazione", "false");
		
		/** campo pec oppure email obbligatori se true
		 *  - si verifichi che almeno uno dei 2 campi sia compilato
		 *    se nessuno dei 2, errore
		 *  blocchetto: sedeLegale
		 *  utilizzato per bando: bonus-piemonte
		 **/
		configuration.put("_sedeLegale_emailOrPecObbligatorio", "false");
		
		
		/** nuovo campo cir: codice identificativo regionale 
		 *  blocchetto: operatore presentatore
		 *  bando che lo utilizza:
		 *  - bonus piemonte turismo idBando: 90
		 *   
		 **/
		configuration.put("_operatorePresentatore_cir", "false");
		
		/** var cfg bonus piemonte turismo 
		 *  per il recupero importo in base al codice regionale presente 
		 *  su tabella FINDOM_T_BONUS_TURISMO
		 *  - blocchetto interessato: caratteristicheprogettoNG
		 *  - cir : codice identificativo regionale 
		 **/
	    configuration.put("_caratteristicheProgettoNG_imp_contr_cir", "false");
	    
	    /** var cfg Rinnovo Automezzi 2020 
		 * Se true abilita l'obbligatorieta di compilazione della tabella "Dati della rottamazione"
		 * solo se e' stata selezionata la tipologia intervento "Rinnovo automezzi inquinanti flotte pubbliche"
		 *  - blocchetto interessato: rottamazione
		 **/
	    configuration.put("_rottamazione_ctrl_obbl_comp", "false");
	    
	    /** var cfg 
	     *  - bandi: aps - industria
	     *  - numero e relativa data di Iscrizione al Registro APS
		 *  - blocchetto interessato: operatorePresentatoreSemplificato
		 **/
	    configuration.put("_operatorePresentatore_codiceAps", "false");
	    
	    
	    /** var cfg 
	     *  - bandi: aps - industria
	     *  - limite max data di Iscrizione al Registro APS
		 *  - blocchetto interessato: operatorePresentatoreSemplificato
		 **/
	    configuration.put("_operatorePresentatore_dataIscrLimMax", "false");
	    
	    /** var cfg 
	     *  - esegue call ad una procedure 
	     *    recupera importi da db
	     *    e se valorizzati, in base alle specifiche del bando
	     *    vengono eseguiti i relativi controlli...
	     *   
	     * Usato nei bandi:
	     * - beniLibrari2020 in blocchetto: cultPianoSpese
	     * 
	     **/
	    configuration.put("verifica_importi_da_procedure", "false");
	    
	    /**
	     * var cfg 
	     * - se a true, esegue nel blocchetto interessato: cultFormaAgevolazioneB
	     *   calcolo di:
	     *   - importo erogabile 
	     *   - recupero contributo max. ultimi 3 anni
	     *   - differenza Spese connesse attivita - tot. entrate 
	     *   - saldo contabile previsto
	     **/
	    configuration.put("_cultFormaAgvB_cntb_max_ultimi_tre_anni", "false");
	    
	    
	    /**
	     * :: msgDocumentiOpzionaliPerBando
	     * Gestione messaggio per bando
	     * Variabile configurabile per bando. Se a true,
	     * recupera idBando e visualizza per questi un messaggio specifico presente nel div della pagina interessata.
	     * Usato in : Bonus cultura 2020
	     * Blocchetto che ad oggi lo utilizza:
	     * - gestioneDocumentiNG
	     * */
	    configuration.put("msgDocumentiOpzionaliPerBando", "false");
	    
	    /** 
	     * label dinamica per campo email 
	     * in blocchetto: beneficiarioPF
	     **/
	    configuration.put("_beneficiarioPF_indirizzo_email_label", "");
	    
	    /**
	     * Se la variabile di configurazione viene definita a "true"
	     * viene nascosto il blocco di codice : Settore Ateco
	     * nel blocchetto di riferimento: dettaglioSedeLegaleOperativa
	     */
	    configuration.put("_dettaglioSedeLegaleOperativa_settore_ateco", "false");
	    
	}

}
