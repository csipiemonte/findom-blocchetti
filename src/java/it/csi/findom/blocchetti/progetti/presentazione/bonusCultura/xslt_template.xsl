<!--
  Copyright Regione Piemonte - 2020
  SPDX-License-Identifier: EUPL-1.2-or-later
-->
<xsl:stylesheet version="1.0" xmlns:xsl= "http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes"/>
   
   <!-- inizio - Gestione caratteri speciali, vengono mantenuti (.), (,) e (;), (:) -->
	<xsl:variable name="caratteriConsentiti"
        select="'\s ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzàèìòù0123456789;.,&quot;:&#160;&#224;&#232;&#236;&#242;&#249;'"/>
        
	<xsl:template name="stripSpecialChars">
 		<xsl:param name="string" />
 		 <xsl:value-of select="translate($string,translate($string, $caratteriConsentiti, ' '),' ')"/>
 	</xsl:template>
 	<!-- fine - Gestione caratteri speciali, vengono mantenuti (.), (,), (:) e (;) -->
 	

	
	<xsl:template match="/">
		<domanda_di_agevolazione xmlns="http://www.csi.it/interscambio/finaziamenti" version="1.0.0">
			<progetti>
				<xsl:apply-templates/>
			</progetti>
		</domanda_di_agevolazione>
	</xsl:template>

	<xsl:template match="/tree-map">
	<progetto>
		
		<norma_incentivazione>@@NORMA_INCENTIVAZIONE@@</norma_incentivazione>
		<titolo_misura_linea></titolo_misura_linea>
		<id_progetto>FD@@ID_DOMANDA@@</id_progetto>
		<riferimento_progetto_collegato></riferimento_progetto_collegato>
		<codice_asse>@@CODICE_ASSE_PRIORITARIO@@</codice_asse>
		<descrizione_asse>@@DESCRIZIONE_ASSE_PRIORITARIO@@</descrizione_asse>
		<codice_misura>@@CODICE_MISURA@@</codice_misura>
		<descrizione_misura>@@DESCRIZIONE_MISURA@@</descrizione_misura>
		<descrizione_estesa_misura></descrizione_estesa_misura>
		<descrizione_bando>@@DESCR_BANDO@@</descrizione_bando>
		<destinatario_descrizione>@@DESTINATARIO_DESCRIZIONE@@</destinatario_descrizione>
		<destinatario_indirizzo>@@DESTINATARIO_INDIRIZZO@@</destinatario_indirizzo>
		<destinatario_cap>@@DESTINATARIO_CAP@@</destinatario_cap>
		<destinatario_citta>@@DESTINATARIO_CITTA@@</destinatario_citta>
		<destinatario_direzione>@@DESTINATARIO_DIREZIONE@@</destinatario_direzione>
		<codice_intervento>@@CODICE_INTERVENTO@@</codice_intervento>
		<descrizione_intervento>@@DESCRIZIONE_INTERVENTO@@</descrizione_intervento> <!-- univoco -->
		
		<!-- questi 2 li popolo prendendo valori da XML  , se non ci sono dettagli metto i 2 valori dell'intervento -->
		<xsl:for-each select="_caratteristicheProgetto/map/tipologiaInterventoList/list/map">
			<xsl:param name="codTipoIntervento" select="codTipoIntervento"/>
			<xsl:param name="descrTipoIntervento" select="descrTipoIntervento"/>
			<xsl:param name="selezionato" select="checked"/>
			<xsl:choose>
				<xsl:when test="count(dettaglioInterventoList/list/map)>'0'">
					<xsl:for-each select="dettaglioInterventoList/list/map">
						<xsl:if test="checked='true'">
							<codice_dettaglio_intervento><xsl:value-of select="$codTipoIntervento"/>-<xsl:value-of select="codDettIntervento" /></codice_dettaglio_intervento>
							<descrizione_dettaglio_intervento><xsl:value-of select="$descrTipoIntervento"/>-<xsl:value-of select="descrDettIntervento"/></descrizione_dettaglio_intervento> 
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="$selezionato='true'"><!-- metto nella trasformata solo la tipologia di intervento selezionata -->
						<codice_dettaglio_intervento><xsl:value-of select="$codTipoIntervento"/></codice_dettaglio_intervento>
						<descrizione_dettaglio_intervento><xsl:value-of select="$descrTipoIntervento"/></descrizione_dettaglio_intervento>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		
<!-- OK FATTO -->		<xsl:call-template name="premialita"/>   			<!-- NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE  -->

<!--  tracciato nodo "progettoDiAgevolazione" MODIFICATO, aggiunto attributo "sede_non_ancora_attivata_in_piemonte" -->
<!-- OK FATTO -->		<xsl:call-template name="progettoDiAgevolazione"/> 			<!-- Obbl - Univoco -->

<!-- tracciato nodo "anagraficaBeneficiario" MODIFICATO, aggiunti 4 attributi,  intestatario_cc, costituzione_incorso, 
																				cod_settore_attivita_economica,descr_settore_attivita_economica -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaBeneficiario"/>			<!-- Obbl - Univoco -->

<!-- OK FATTO -->		<xsl:call-template name="anagraficaBeneficiarioAAEP"/>			<!-- Obbl - Univoco -->

<!-- OK FATTO -->		<xsl:call-template name="anagraficaPartecipante"/>  		<!-- 0-N sezione facoltativa  -->


 <!--   template nodo [altre_anagrafiche]  0-N - facoltativo  -->
 <!--   template identico al nodo [anagraficaBeneficiario] ma SENZA nuovi campi: intestatario_cc, costituzione_incorso, cod_settore_attivita_economica,descr_settore_attivita_economica -->
 <!-- 	Aggiunto nuovo attributo "quota" -->
<!-- OK FATTO -->		<xsl:call-template name="altreAnagraficheSocConsulenzaAutorizzata"/>  		<!-- 0-1 sezione facoltativa ,  dati di Societa di consulenza autorizzata ad intrattenere contatti con Adg/OI VA in altreAnagrafiche -->
<!-- OK FATTO -->		<xsl:call-template name="altreAnagraficheAzControllanti"/>  				<!-- 0-N sezione facoltativa ,  dati di Aziende Controllanti  -->
<!-- OK FATTO -->		<xsl:call-template name="altreAnagraficheAzControllate"/>  					<!-- 0-N sezione facoltativa ,  dati di Aziende Controllate  -->
<!-- OK FATTO -->		<xsl:call-template name="altreAnagraficheSocio"/>  							<!-- 0-N sezione facoltativa ,  dati dei soci che hanno CodiceFiscale=11 char  -->

 <!--   template nodo [anagrafica_persona_fisica]  - Obbl - 1-N  -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaPersonaFisica"/>					<!-- template nodo [anagrafica_persona_fisica] contenente dati Legale Rappresentante -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaSoggettoDelegato"/>				<!-- template nodo [anagrafica_persona_fisica] contenente dati Soggetto delegato -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaPersonaFisicaPersonaAut"/>		<!-- template nodo [anagrafica_persona_fisica] contenente dati Persona dell'impresa autorizzata ad intrattenere contatti con Adg/OI -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaPersonaFisicaConsulenteAut"/>	<!-- template nodo [anagrafica_persona_fisica] contenente dati Consulente autorizzato ad intrattenere contatti con Adg/OI -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaPersonaFisicaSocio"/> 		 	<!-- template nodo [anagrafica_persona_fisica] contenente dati Soci -->

<!-- OK FATTO -->		<xsl:call-template name="spesePartner"/>  				<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiFunzioniAnnoPartner"/>  	<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiRichiestiPartner"/>  		<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="contributiRichiestiPartner"/>  <!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaProdotti"/>  		<!-- 0-N sezione facoltativa  --> <!-- del beneficiario --> 

<!-- 	template nodo [fatturato] 0-N facoltativa 	-->
<!-- va compilato 3 volte per la sezione Dati Impresa>Dimensioni>Dimensioni d'impresa,
           	una volta pee la colonna Fatturato,
	 		una volta per la colonna Totale Bilancio 
	 		una volta per la colonna ULA -->
<!-- OK FATTO -->		<xsl:call-template name="fatturatoA1"/>  			<!-- colonna Fatturato -->
<!-- OK FATTO -->		<xsl:call-template name="fatturatoTotBilancio"/>  	<!-- colonna Totale Bilancio -->
<!-- OK FATTO -->		<xsl:call-template name="fatturatoULA"/>  			<!-- colonna ULA-->

<!-- OK FATTO -->		<xsl:call-template name="dimensioniAAEP"/>  			<!-- dati dimenzioni provenienti da AAEP -->

<!--   template nodo [dati_bilancio] 0-N facoltativa , compilato una volta per ogni anno -->
<!-- OK FATTO -->		<xsl:call-template name="datiBilancio"/>  				<!-- NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE --> 
<!-- OK FATTO -->		<xsl:call-template name="datiBilancioPrecedente"/> 		<!-- NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE -->
<!-- OK FATTO -->		<xsl:call-template name="datiBilancioAAEP"/>  				<!-- NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE --> 
<!-- OK FATTO -->		<xsl:call-template name="datiBilancioPrecedenteAAEP"/> 		<!-- NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE -->

<!-- OK FATTO -->		<xsl:call-template name="risorseUmane"/> 				<!-- 0-N sezione facoltativa  TODO : campo tipologia_risorsa??? -->
<!-- OK FATTO -->		<xsl:call-template name="cronoprogramma"/> 				<!-- 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="cronoprogrammaDiSpesa"/> 		<!-- 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="indicatoriDiRisultato"/> 		<!-- 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="indicatori2020"/> 			<!-- NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE : 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="indicatoriAmbiente"/> 		<!-- 0-N sezione facoltativa  -->

<!-- OK FATTO -->		<xsl:call-template name="speseProgetto"/>			<!-- Obbl 1-N -->

<!-- OK FATTO -->		<xsl:call-template name="sovracosti"/> 				<!-- 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiPersonale"/>			<!-- 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiEntiRicerca"/> 		<!-- 0-N sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="strumentiAttrezzature"/> 	<!-- 0-N sezione facoltativa  -->

<!-- OK FATTO -->		<xsl:call-template name="datiTecnici"/>				<!-- 0-N sezione facoltativa  -->

<!--  tracciato nodo "dichiarazione_impegnativa" MODIFICATO, aggiunti 3 attributi  -->
<!-- OK FATTO -->		<xsl:call-template name="dichiarazioneImpegnativa"/>		<!-- Univoco - sezione facoltativa  --> 

<!-- OK FATTO -->		<xsl:call-template name="indiceDocumentiAllegati"/> 		<!-- Univoco - sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiFunzioniAnno"/>				<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiBrevetti"/> 					<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="costiRichiesti"/> 					<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="contributiRichiesti"/> 			<!-- sezione facoltativa  -->
<!-- OK FATTO -->		<xsl:call-template name="progettoRaggruppamento"/>			<!-- sezione facoltativa  -->

<!-- OK FATTO -->		<xsl:call-template name="formeFinanziamento"/> 	<!--  NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE : NODO facoltativo 0-N -->
<!-- OK FATTO -->		<xsl:call-template name="altreSedi"/> 			<!--  NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE : NODO facoltativo 0-N -->
<!-- OK FATTO -->		<xsl:call-template name="dettaglioSpese"/> 		<!--  NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE : NODO facoltativo 0-N -->
<!-- OK FATTO -->		<xsl:call-template name="sediAAEP"/> 			<!--  NODO NUOVO, DATI NOSTRI NON GESTITI DA FINPIEMONTE : NODO facoltativo 0-N -->
<!-- OK FATTO -->		<xsl:call-template name="progettiRS"/> 			
<!-- OK FATTO -->		<xsl:call-template name="riepilogoCostiResiduiContributiRichiesti"/>
<!-- OK FATTO -->		<xsl:call-template name="capacitaFinanziaria"/>
<!-- OK FATTO -->		<xsl:call-template name="strutturaOrganizzativa"/>
<!-- OK FATTO -->		<xsl:call-template name="autosostenibilita"/>
<!-- OK FATTO -->		<xsl:call-template name="unioneComuni"/>
<!-- OK FATTO -->		<xsl:call-template name="raggruppamentoEnti"/>
<!-- OK FATTO -->		<xsl:call-template name="entiProgetto"/>

<!--   template nodo [anagrafica_persona_fisica]  - Obbl - 1-N  -->
<!-- OK FATTO -->		<xsl:call-template name="anagraficaPersonaFisicaAAEP"/>					<!-- template nodo [anagrafica_persona_fisica] contenente dati Legale Rappresentante -->
                        <xsl:call-template name="documentiDemat"/>					<!-- template nodo [documenti_demat] contenente i dati dei file in caso di domande dematerializzate -->
		</progetto>
	</xsl:template>

	<xsl:template name="progettoDiAgevolazione" >
	<progetto_di_agevolazione>
			<cup><xsl:value-of select="_abstractProgetto/map/codiceCUP"/></cup> 
			<durataPrevista><xsl:value-of select="_abstractProgetto/map/durataPrevista" /></durataPrevista>
			<codice_fase>00</codice_fase> <!-- passare ZEROZERO sempre -->
			<!-- da 2015-06-10 a 10/06/2015 -->
			<data_presentazione><xsl:value-of select="substring(_domanda/map/dataTrasmissione,9,2)"/>/<xsl:value-of select="substring(_domanda/map/dataTrasmissione,6,2)"/>/<xsl:value-of select="substring(_domanda/map/dataTrasmissione,1,4)"/></data_presentazione>
			<ora_presentazione><xsl:value-of select="substring(_domanda/map/dataTrasmissione,12,8)"/></ora_presentazione>
			<partitaiva_unitalocale><xsl:value-of select="_operatorePresentatore/map/partitaIva"/></partitaiva_unitalocale>
		<xsl:choose>
			<xsl:when test="_sedi/map/flagSedeAttiva='checked'">
			<!--  se questo campo e' valorizzato passo una sede fittizia  -->
				<codice_ateco></codice_ateco>
				<descrizione_attivita_unitalocale></descrizione_attivita_unitalocale>
				<codice_settore_unitalocale></codice_settore_unitalocale>
				<descrizione_settore_unitalocale></descrizione_settore_unitalocale> 
				<stato_unitalocale>ITALIA</stato_unitalocale> <!-- fisso -->
				<cod_stato_unitalocale>000</cod_stato_unitalocale> <!-- ZEROZEROZERO, italia -->
				<cap_unitalocale>10100</cap_unitalocale>
				<codice_comune_unitalocale>001272</codice_comune_unitalocale>
				<comune_unitalocale>Torino</comune_unitalocale>
				<provincia_unitalocale>Torino</provincia_unitalocale>
				<coordinata_x></coordinata_x>
				<coordinata_y></coordinata_y>
				<zona_censuaria_87_3_c></zona_censuaria_87_3_c><!-- passare sempre null -->
				<strutturale></strutturale><!-- passare sempre null -->
				<data_ultima_validita_zona_censuaria_87_3_c></data_ultima_validita_zona_censuaria_87_3_c><!-- passare sempre null -->
				<modalita_registrazione>Manuale</modalita_registrazione> <!-- passare sempre 'Manuale' , valori ammessi ['Manuale','Servizio Georeferenziazione'] -->
				<indirizzo_unitalocale>Sede non ancora attiva sul territorio piemontese</indirizzo_unitalocale>
				<telefono_unitalocale></telefono_unitalocale>
				<fax_unitalocale></fax_unitalocale>
				<email_unitalocale></email_unitalocale>
			</xsl:when>
			<xsl:otherwise>
			<!-- passo la sede vera -->
			<xsl:for-each select="_sedi/map/sediList/list/map"><!-- PRENDO solo la sede con idTipoSede=3, se non c'e' prendo la prima quella con idTipoSede=1 -->
			<xsl:choose>
				<xsl:when test="(idTipoSede = 3) or (idTipoSede = 1)">  <!-- o vale 1 o vale 3, non possono esserci entrembi ma uno ci deve essere -->
					<codice_ateco><xsl:value-of select="codAteco2007Sede"/></codice_ateco>
					<descrizione_attivita_unitalocale><xsl:value-of select="descrAteco2007Sede"/></descrizione_attivita_unitalocale>
					<codice_settore_unitalocale><xsl:value-of select="codiceSettoreAtecoSede"/></codice_settore_unitalocale>
					<descrizione_settore_unitalocale><xsl:value-of select="descrizioneSettoreAtecoSede"/></descrizione_settore_unitalocale>					
					<xsl:choose>
						<xsl:when test="stato='Italia'">							
							<stato_unitalocale>ITALIA</stato_unitalocale> <!-- fisso -->
							<cod_stato_unitalocale>000</cod_stato_unitalocale> <!-- ZEROZEROZERO, italia -->
							<codice_comune_unitalocale><xsl:value-of select="idComuneSede"/></codice_comune_unitalocale>
							<comune_unitalocale><xsl:value-of select="descrComuneSede"/></comune_unitalocale>
							<provincia_unitalocale><xsl:value-of select="siglaProvinciaSede"/></provincia_unitalocale>																					
						</xsl:when>
						<xsl:otherwise>
						<xsl:variable name="statoEst" select="translate(statoEsteroDescrizione,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
							<stato_sede><xsl:value-of select="$statoEst"/></stato_sede>
							<cod_stato_sede><xsl:value-of select="statoEstero"/></cod_stato_sede>
							<citta_estera><xsl:value-of select="cittaEstera"/></citta_estera>
					</xsl:otherwise>
					</xsl:choose>
					<cap_unitalocale><xsl:value-of select="capSede"/></cap_unitalocale>					
					<coordinata_x><xsl:value-of select="coordX"/></coordinata_x>
					<coordinata_y><xsl:value-of select="coordY"/></coordinata_y>
					<zona_censuaria_87_3_c></zona_censuaria_87_3_c><!-- passare sempre null -->
					<strutturale></strutturale><!-- passare sempre null -->
					<data_ultima_validita_zona_censuaria_87_3_c></data_ultima_validita_zona_censuaria_87_3_c><!-- passare sempre null -->
					<xsl:choose>
					<xsl:when test="coordX=''">
						<modalita_registrazione>Manuale</modalita_registrazione> <!-- passare sempre 'Manuale' , valori ammessi ['Manuale','Servizio Georeferenziazione'] -->
					</xsl:when>
					<xsl:otherwise>
						<modalita_registrazione></modalita_registrazione>
					</xsl:otherwise>
					</xsl:choose>
					<indirizzo_unitalocale><xsl:value-of select="indirizzoSede"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="numeroCivicoSede"/></indirizzo_unitalocale>
					<telefono_unitalocale><xsl:value-of select="telefonoSede"/></telefono_unitalocale>
					<fax_unitalocale></fax_unitalocale>
					<email_unitalocale><xsl:value-of select="indirizzoPecSede"/></email_unitalocale>
				</xsl:when>
			</xsl:choose>
			</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
			<sede_non_attivata_in_piemonte><xsl:value-of select="_sedi/map/flagSedeMaiAttivata"/></sede_non_attivata_in_piemonte>		 
			<data_inizio_progetto></data_inizio_progetto> <!-- passare sempre null -->
			<data_fine_progetto></data_fine_progetto> <!-- passare sempre null -->
			<titolo_progetto><xsl:value-of select="_abstractProgetto/map/titolo"/></titolo_progetto>
			<!-- descrizione_progetto><xsl:value-of select="_abstractProgetto/map/descrizione"/></descrizione_progetto-->
			<descrizione_progetto></descrizione_progetto>
						
			<!-- inizio - Gestione caratteri speciali -->
			<abstract_progetto>
			  <xsl:call-template name="stripSpecialChars">
			  	<xsl:text><![CDATA[<sintesi>]]></xsl:text> 
			  	<xsl:with-param name="string" select="_abstractProgetto/map/sintesi"/>
			  	<xsl:text><![CDATA[</sintesi>]]></xsl:text> 
			  	</xsl:call-template>
			</abstract_progetto>
			<!-- fine - Gestione caratteri speciali -->
			
			
			<tipo_impianto_progetto></tipo_impianto_progetto> <!-- passare sempre null -->
			<impianto_progetto></impianto_progetto> <!-- passare sempre null -->
			<tipo_operazione_progetto></tipo_operazione_progetto> <!-- passare sempre null -->
			<obiettivi_attivita_progetto></obiettivi_attivita_progetto> <!-- passare sempre null -->
			<elementi_verificafinale_progetto></elementi_verificafinale_progetto> <!-- passare sempre null -->
			<ricadute_economicoproduttive_progetto></ricadute_economicoproduttive_progetto> <!-- passare sempre null -->
			<ricadute_ambientali></ricadute_ambientali> <!-- passare sempre null -->
			<incidenza_pariopportunita_progetto></incidenza_pariopportunita_progetto> <!-- passare sempre null -->
			<codice_sportello_domanda></codice_sportello_domanda> <!-- passare sempre null -->
			<sigla_provincia_sportello_domanda></sigla_provincia_sportello_domanda> <!-- passare sempre null -->
			<id_consulente_sportello></id_consulente_sportello> <!-- passare sempre null -->
			<nominativo_consulente_sportello></nominativo_consulente_sportello> <!-- passare sempre null -->
			<email_consulente_sportello></email_consulente_sportello> <!-- passare sempre null -->
			<data_approvazione_businessplan></data_approvazione_businessplan> <!-- passare sempre null -->
			<data_prevista_invio_documentazionedispesa></data_prevista_invio_documentazionedispesa> <!-- passare sempre null -->
			<importo_contributo><xsl:value-of select="_caratteristicheProgetto/map/importoContributo"/></importo_contributo>	
			<finanziamento_richiesto></finanziamento_richiesto>
			<importo_spesa_ammissibile></importo_spesa_ammissibile> <!--  passare sempre null -->
			<spesa_ammessa></spesa_ammessa> <!--  passare sempre null -->
		
		<xsl:for-each select="_tipologiaAiuto/map/tipologiaAiutoList/list/map">
			<!-- se non c'e' dettaglio passare codice e descrizione della Base Giuridica se c'e' dettaglio passare codice e descrizione del dettaglio -->
			<xsl:if test="checked='true'">
				<xsl:choose>
					<xsl:when test="dettaglioAiutoList!=''">
						<!-- uso i dati del "Dettaglio" della "Base Giuridica" -->
						<xsl:for-each select="dettaglioAiutoList/list/map">
							<xsl:if test="checked='true'">
								<tipologia_regime_di_aiuto><xsl:value-of select="descrDettAiuto"/></tipologia_regime_di_aiuto>
								<codice_tipologia_regime_di_aiuto><xsl:value-of select="codiceDettAiuto"/></codice_tipologia_regime_di_aiuto>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<!-- uso i dati della "Base Giuridica" -->
						<tipologia_regime_di_aiuto><xsl:value-of select="descrTipoAiuto"/></tipologia_regime_di_aiuto>
						<codice_tipologia_regime_di_aiuto><xsl:value-of select="codTipoAiuto"/></codice_tipologia_regime_di_aiuto>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
		
		<data_ultima_validita_tipologia_aiuto></data_ultima_validita_tipologia_aiuto> <!-- passare sempre null, valida per 87_3_c -->
		<fondo_rotativo_richiesto></fondo_rotativo_richiesto> <!-- passare sempre null -->
		<impatto_progetto></impatto_progetto>  <!-- passare sempre null -->
		<info_raggruppamento></info_raggruppamento> <!-- passare sempre null -->
		<modalita_realizzative_progetto></modalita_realizzative_progetto> <!-- passare sempre null -->
		<dimostrazione_sostenibilita_progetto></dimostrazione_sostenibilita_progetto>  <!-- passare sempre null -->
		<ricadute_economiche_progetto_complessivo></ricadute_economiche_progetto_complessivo>  <!-- passare sempre null -->
		<!-- i seguenti 4 campi sono relativi alla determina bancaria -->
		<data_determina>@@DATA_DETERMINA@@</data_determina> <!-- data_Atto -->
		<numero_determina>@@NUMERO_DETERMINA@@</numero_determina> <!-- numero_Atto -->
		<data_protocollo></data_protocollo>  <!-- passare sempre null -->
		<numero_protocollo></numero_protocollo>  <!-- passare sempre null -->
		<finanziamento_bancario></finanziamento_bancario><!-- passare sempre null -->
		<sede_non_ancora_attivata_in_piemonte><xsl:value-of select="_sedi/map/flagSedeAttiva"/></sede_non_ancora_attivata_in_piemonte>  <!-- campo non gestito da finpiemonte -->
		<dichiarazione_attivita_economica>
			<xsl:if test="_domanda/map/stereotipoDomanda='OR'">
			<xsl:if test="_naturaAttivitaProgetto/map/naturaAttivita='economica'">Si</xsl:if>
			<xsl:if test="_naturaAttivitaProgetto/map/naturaAttivita='nonEconomica'">No</xsl:if>
			</xsl:if>
		</dichiarazione_attivita_economica>
		</progetto_di_agevolazione>
	</xsl:template>


	<xsl:template name="anagraficaBeneficiario" >
		<anagrafica_beneficiario> 
			<cod_tipo_utente><xsl:value-of select="_domanda/map/codTipologiaUtente"/></cod_tipo_utente> <!-- 2 = ente pubblico, 1 = privato --> 
			<tipo_utente>Beneficiario</tipo_utente> 
			<ragione_sociale_impresa><xsl:value-of select="_operatorePresentatore/map/denominazione"/></ragione_sociale_impresa>
			<tipologia_ente><xsl:value-of select="_domanda/map/descrStereotipoDomanda"/></tipologia_ente> <!-- metto codice stereotipo -->
			<codice_dipartimento><xsl:value-of select="_operatorePresentatore/map/codiceDipartimento"/></codice_dipartimento>			
			<descrizione_dipartimento><xsl:value-of select="_operatorePresentatore/map/descrizioneDipartimento"/></descrizione_dipartimento>
			<forma_giuridica_impresa><xsl:value-of select="_operatorePresentatore/map/descrizioneFormaGiuridica"/></forma_giuridica_impresa>
			<cod_forma_giuridica_impresa><xsl:value-of select="_operatorePresentatore/map/codiceFormaGiuridica"/></cod_forma_giuridica_impresa>
			<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatore/map/codiceFiscale"/></codice_fiscale_impresa>
			<id_ente></id_ente> <!-- passare sempre null -->
			<partitaiva_sedelegale><xsl:value-of select="_operatorePresentatore/map/partitaIva"/></partitaiva_sedelegale>
			<codice_ateco><xsl:value-of select="_operatorePresentatore/map/codicePrevalenteAteco2007"/></codice_ateco>
			<descr_ateco><xsl:value-of select="_operatorePresentatore/map/descrizioneAteco2007"/></descr_ateco>
			<codice_attivita_ufficioitalianocambi></codice_attivita_ufficioitalianocambi> <!-- passare sempre null -->
			<descrizione_attivita_ufficiocambi></descrizione_attivita_ufficiocambi> <!-- passare sempre null -->
			<xsl:choose>
				<xsl:when test="_sedeLegale/map/stato='Italia'">
				<xsl:variable name="stat" select="translate(_sedeLegale/map/stato,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
					<stato_sedelegale><xsl:value-of select="$stat"/></stato_sedelegale>
					<cod_stato_sedelegale>00</cod_stato_sedelegale> <!-- 00 per italia -->
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="stat2" select="translate(_sedeLegale/map/statoEsteroDescrizione,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
					<stato_sedelegale><xsl:value-of select="$stat2"/></stato_sedelegale>
					<cod_stato_sedelegale><xsl:value-of select="_sedeLegale/map/statoEstero"/></cod_stato_sedelegale>
					<citta_estera><xsl:value-of select="_sedeLegale/map/cittaEstera"/></citta_estera>
				</xsl:otherwise>
			</xsl:choose>		
			<cap_sedelegale><xsl:value-of select="_sedeLegale/map/cap"/></cap_sedelegale>
			<provincia_sedelegale><xsl:value-of select="_sedeLegale/map/provinciaSigla"/></provincia_sedelegale>
			<codice_comune_sedelegale><xsl:value-of select="_sedeLegale/map/comune"/></codice_comune_sedelegale>
			<codice_settore><xsl:value-of select="_operatorePresentatore/map/codiceSettoreAteco"/></codice_settore>
			<descrizione_settore><xsl:value-of select="_operatorePresentatore/map/descrizioneSettoreAteco"/></descrizione_settore>
			<codice_attivita_prevalente><xsl:value-of select="_operatorePresentatore/map/codicePrevalenteAteco2007"/></codice_attivita_prevalente>
			<descrizione_attivita_prevalente><xsl:value-of select="_operatorePresentatore/map/descrizioneAteco2007"/></descrizione_attivita_prevalente>
			<comune_sedelegale><xsl:value-of select="_sedeLegale/map/comuneDescrizione"/></comune_sedelegale>
			<indirizzo_sedelegale><xsl:value-of select="_sedeLegale/map/indirizzo"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="_sedeLegale/map/numCivico"/></indirizzo_sedelegale>
			<telefono_sedelegale><xsl:value-of select="_sedeLegale/map/telefono"/></telefono_sedelegale>
			<fax_sedelegale></fax_sedelegale> <!-- null -->
			<email_sedelegale><xsl:value-of select="_sedeLegale/map/email"/></email_sedelegale> <!-- null -->
			<pec_sedelegale><xsl:value-of select="_sedeLegale/map/pec"/></pec_sedelegale>
			
			<data_costituzione_societa><xsl:value-of select="_costituzioneImpresa/map/dataCostituzioneImpresa"/></data_costituzione_societa> <!-- solo la DATA , non ora-->
			
			<xsl:choose>
			<xsl:when test="_domanda/map/codTipologiaUtente='2'"> <!-- 2 = ente pubblico, 1 = privato --> 
				<dimensione_azienda>ALL</dimensione_azienda><!--  null se e' Ente Pubblico -->
				<cod_dimensione_azienda>X</cod_dimensione_azienda> 
			</xsl:when>
			<xsl:otherwise>
				<dimensione_azienda><xsl:value-of select="_dimensioni/map/descrizioneDimensioneImpresa"/></dimensione_azienda>
				<xsl:choose>
				<!--  valori ammessi [P,M,G,I,X] I per micro, X per ente pubblico-->
				<xsl:when test="_dimensioni/map/idDimensioneImpresa='1'">
					<cod_dimensione_azienda>I</cod_dimensione_azienda> 
				</xsl:when>
				<xsl:when test="_dimensioni/map/idDimensioneImpresa='2'">
					<cod_dimensione_azienda>P</cod_dimensione_azienda> 
				</xsl:when>
				<xsl:when test="_dimensioni/map/idDimensioneImpresa='3'">
					<cod_dimensione_azienda>M</cod_dimensione_azienda> 
				</xsl:when>
				<xsl:when test="_dimensioni/map/idDimensioneImpresa='4'">
					<cod_dimensione_azienda>G</cod_dimensione_azienda> 
				</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
			</xsl:choose>
			
			<tipologia_registro_iscrizione>CCIAA</tipologia_registro_iscrizione>
			<!-- albo artigiani o altri albi-->
			<provincia_iscrizione></provincia_iscrizione><!-- passare sempre null -->
			<n_iscrizione_registro></n_iscrizione_registro><!-- passare sempre null -->
			<data_iscrizione></data_iscrizione><!-- passare sempre null -->
			<!-- registro imprese = CCIAA -->
			<provincia_iscrizione_registroimprese><xsl:value-of select="_costituzioneImpresa/map/provinciaDescrizione"/></provincia_iscrizione_registroimprese>
			<n_iscrizione_registroimprese></n_iscrizione_registroimprese><!-- passare sempre null -->
			<data_iscrizione_registroimprese></data_iscrizione_registroimprese><!-- passare sempre null -->
			<iscrizione_incorso><xsl:value-of select="_costituzioneImpresa/map/iscrizioneInCorso"/></iscrizione_incorso>
			<agenzia></agenzia> <!-- passare sempre null -->
			<provincia_agenzia></provincia_agenzia> <!-- passare sempre null -->
			<comune_agenzia></comune_agenzia> <!-- passare sempre null -->
			<cod_comune_agenzia></cod_comune_agenzia> <!-- passare sempre null -->
			<cap_agenzia></cap_agenzia> <!-- passare sempre null -->
			<indirizzo_agenzia></indirizzo_agenzia> <!-- passare sempre null -->
			
			<cin><xsl:value-of select="substring(_estremiBancari/map/iban,5,1)"/></cin>
			<abi><xsl:value-of select="substring(_estremiBancari/map/iban,6,5)"/></abi>
			<cab><xsl:value-of select="substring(_estremiBancari/map/iban,11,5)"/></cab>
			<iban><xsl:value-of select="_estremiBancari/map/iban"/></iban>
			<bic><xsl:value-of select="_estremiBancari/map/bic"/></bic>
			<n_contocorrente><xsl:value-of select="substring(_estremiBancari/map/iban,16,12)"/></n_contocorrente>
			<istituto_di_credito></istituto_di_credito><!-- passare sempre null -->
			<intestatario_cc><xsl:value-of select="_estremiBancari/map/intestatarioCC"/></intestatario_cc> <!-- attributo nuovo -->
			<capitale_sociale><xsl:value-of select="_azienda/map/capitaleSociale"/></capitale_sociale>
			<campo_attivita><xsl:value-of select="_attivita/map/descrizione"/></campo_attivita>
			<data_ultimo_esercizio><xsl:value-of select="_bilancio/map/anno"/></data_ultimo_esercizio>
			<xsl:choose>
			<xsl:when test="_abstractProgetto/map/ruolo='C'">
				<ruolo_beneficiario>capofila</ruolo_beneficiario>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="_abstractProgetto/map/ruolo='P'">
						<ruolo_beneficiario>partner</ruolo_beneficiario>
					</xsl:when>
					<xsl:otherwise>
						<ruolo_beneficiario>singolo</ruolo_beneficiario>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
			</xsl:choose>
			<classificazione_ente><xsl:value-of select="_domanda/map/descrTipologiaUtente"/></classificazione_ente>
			<incaricato></incaricato><!-- passare sempre null -->
			<telefono_incaricato></telefono_incaricato><!-- passare sempre null -->
			<fax_incaricato></fax_incaricato><!-- passare sempre null -->
			<email_incaricato></email_incaricato><!-- passare sempre null -->
			<codice_ramo_attivita_economica></codice_ramo_attivita_economica><!-- passare sempre null -->
			<descrizione_ramo_attivita_economica></descrizione_ramo_attivita_economica> <!-- passare sempre null -->
			<descrizione_albo_professionale></descrizione_albo_professionale> <!-- passare sempre null -->
			<numero_albo_professionale></numero_albo_professionale> <!-- passare sempre null -->
			<data_iscrizione_albo_professionale></data_iscrizione_albo_professionale> <!-- passare sempre null -->
			<costituzione_incorso><xsl:value-of select="_costituzioneImpresa/map/costituzioneInCorso"/></costituzione_incorso> <!-- campo non gestito da finpiemonte -->
			<cod_settore_attivita_economica><xsl:value-of select="_operatorePresentatore/map/codiceAttivitaEconomica" /></cod_settore_attivita_economica> <!-- campo non gestito da finpiemonte -->
			<descr_settore_attivita_economica><xsl:value-of select="_operatorePresentatore/map/descrizioneAttivitaEconomica" /></descr_settore_attivita_economica> <!-- campo non gestito da finpiemonte -->
			<codice_ipa><xsl:value-of select="_operatorePresentatore/map/codiceIpa"/></codice_ipa> 
		</anagrafica_beneficiario> 
	</xsl:template>

	<xsl:template name="anagraficaBeneficiarioAAEP" >
		<anagrafica_beneficiario_AAEP> 			
			<ragione_sociale_impresa><xsl:value-of select="_operatorePresentatoreAAEP/map/denominazione"/></ragione_sociale_impresa>			
			<forma_giuridica_impresa><xsl:value-of select="_operatorePresentatoreAAEP/map/descrizioneFormaGiuridica"/></forma_giuridica_impresa>
			<cod_forma_giuridica_impresa><xsl:value-of select="_operatorePresentatoreAAEP/map/codiceFormaGiuridica"/></cod_forma_giuridica_impresa>
			<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatoreAAEP/map/codiceFiscale"/></codice_fiscale_impresa>
			<partitaiva_sedelegale><xsl:value-of select="_operatorePresentatoreAAEP/map/partitaIva"/></partitaiva_sedelegale>
			<codice_ateco><xsl:value-of select="_operatorePresentatoreAAEP/map/codicePrevalenteAteco2007"/></codice_ateco>
			<descr_ateco><xsl:value-of select="_operatorePresentatoreAAEP/map/descrizioneAteco2007"/></descr_ateco>

		<xsl:choose>
			<xsl:when test="_sedeLegaleAAEP/map/stato='Italia'">
			<xsl:variable name="stat" select="translate(_sedeLegaleAAEP/map/stato,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
				<stato_sedelegale><xsl:value-of select="$stat"/></stato_sedelegale>
				<cod_stato_sedelegale>00</cod_stato_sedelegale> <!-- 00 per italia -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="stat2" select="translate(_sedeLegaleAAEP/map/statoEsteroDescrizione,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
				<stato_sedelegale><xsl:value-of select="$stat2"/></stato_sedelegale>
				<cod_stato_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/statoEstero"/></cod_stato_sedelegale>
			</xsl:otherwise>
		</xsl:choose>
			
		
			<cap_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/cap"/></cap_sedelegale>
			<provincia_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/provinciaSigla"/></provincia_sedelegale>
			<codice_comune_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/comune"/></codice_comune_sedelegale>
			<codice_attivita_prevalente><xsl:value-of select="_operatorePresentatoreAAEP/map/codicePrevalenteAteco2007"/></codice_attivita_prevalente>
			<descrizione_attivita_prevalente><xsl:value-of select="_operatorePresentatoreAAEP/map/descrizioneAteco2007"/></descrizione_attivita_prevalente>
			<pec><xsl:value-of select="_operatorePresentatoreAAEP/map/indirizzoPec"/></pec>
			
			<comune_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/comuneDescrizione"/></comune_sedelegale>
			<indirizzo_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/indirizzo"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="_sedeLegaleAAEP/map/numCivico"/></indirizzo_sedelegale>
			<telefono_sedelegale><xsl:value-of select="_sedeLegaleAAEP/map/telefono"/></telefono_sedelegale>						
				
			<data_costituzione_societa><xsl:value-of select="_costituzioneImpresaAAEP/map/dataCostituzioneImpresa"/></data_costituzione_societa> <!-- solo la DATA , non ora-->

			<!-- albo artigiani o altri albi-->
			<provincia_iscrizione><xsl:value-of select="_operatorePresentatoreAAEP/map/provinciaIscrizione"/></provincia_iscrizione>
			<n_iscrizione_registro><xsl:value-of select="_operatorePresentatoreAAEP/map/numeroIscrizione"/></n_iscrizione_registro>
			<data_iscrizione><xsl:value-of select="_operatorePresentatoreAAEP/map/dataDeliberaIscrizione"/></data_iscrizione>
			<!-- cessazione -->
			<cessazione><xsl:value-of select="_operatorePresentatoreAAEP/map/cessazione"/></cessazione>
			<indice_stato_attivita><xsl:value-of select="_operatorePresentatoreAAEP/map/indiceStatoAttivita"/></indice_stato_attivita>
			<cod_causale_cessazione><xsl:value-of select="_operatorePresentatoreAAEP/map/codCausaleCessazione"/></cod_causale_cessazione>
			<descr_causale_cessazione><xsl:value-of select="_operatorePresentatoreAAEP/map/descrCausaleCessazione"/></descr_causale_cessazione>
			<data_denuncia_cessazione><xsl:value-of select="_operatorePresentatoreAAEP/map/dataDenunciaCessazione"/></data_denuncia_cessazione>
			
			<!-- registro imprese = CCIAA -->
			<provincia_iscrizione_registroimprese><xsl:value-of select="_costituzioneImpresaAAEP/map/provinciaDescrizione"/></provincia_iscrizione_registroimprese>
			<sigla_provincia_iscrizione_registroimprese><xsl:value-of select="_costituzioneImpresaAAEP/map/provincia"/></sigla_provincia_iscrizione_registroimprese>			
						
			<capitale_sociale_deliberato><xsl:value-of select="_aziendaAAEP/map/capitaleSociale"/></capitale_sociale_deliberato>
			<capitale_sottoscritto><xsl:value-of select="_aziendaAAEP/map/capitaleSottoscritto"/></capitale_sottoscritto>
			<capitale_versato><xsl:value-of select="_aziendaAAEP/map/capitaleVersato"/></capitale_versato>						
		</anagrafica_beneficiario_AAEP> 
	</xsl:template>
	<xsl:template name="altreAnagraficheSocConsulenzaAutorizzata" > <!-- sezione FACOLTATIVA - dati di Societa di consulenza autorizzata ad intrattenere contatti con Adg/OI VA in altreAnagrafiche -->
	<xsl:if test="_riferimenti/map/societa!=''">	
	<altre_anagrafiche>
		<cod_tipo_utente>3</cod_tipo_utente>
		<tipo_utente>Societa consulenza autorizzata adg/oi</tipo_utente>
		<ragione_sociale_impresa><xsl:value-of select="_riferimenti/map/societa/map/ragioneSociale"/></ragione_sociale_impresa>
		<tipologia_ente></tipologia_ente>
		<forma_giuridica_impresa></forma_giuridica_impresa>
		<cod_forma_giuridica_impresa>NA</cod_forma_giuridica_impresa>
		<codice_fiscale_impresa><xsl:value-of select="_riferimenti/map/societa/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>
		<partitaIva_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/partitaIVA"/></partitaIva_sedelegale>
		<codice_ateco></codice_ateco>
		<descr_ateco></descr_ateco>
		<codice_attivita_ufficioitalianocambi></codice_attivita_ufficioitalianocambi>
		<descrizione_attivita_ufficiocambi></descrizione_attivita_ufficiocambi>
		<stato_sedelegale>ITALIA</stato_sedelegale>
		<cod_stato_sedelegale>000</cod_stato_sedelegale>
		<cap_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/cap"/></cap_sedelegale>
		<provincia_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/provincia"/></provincia_sedelegale>
		<codice_comune_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/comune"/></codice_comune_sedelegale> 
		<codice_settore></codice_settore>
		<descrizione_settore></descrizione_settore>
		<codice_attivita_prevalente></codice_attivita_prevalente>
		<descrizione_attivita_prevalente></descrizione_attivita_prevalente>
		<comune_sedelegale></comune_sedelegale>
		<indirizzo_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/indirizzo"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="_riferimenti/map/societa/map/numeroCivico"/></indirizzo_sedelegale>
		<telefono_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/telefono"/></telefono_sedelegale>
		<fax_sedelegale></fax_sedelegale>
		<email_sedelegale><xsl:value-of select="_riferimenti/map/societa/map/email"/></email_sedelegale>
		<pec_sedelegale></pec_sedelegale>
		<data_costituzione_societa></data_costituzione_societa>
		<dimensione_azienda></dimensione_azienda>
		<cod_dimensione_azienda></cod_dimensione_azienda>
		<tipologia_registro_iscrizione>CCIAA</tipologia_registro_iscrizione>
		<provincia_iscrizione></provincia_iscrizione>
		<n_iscrizione_registro></n_iscrizione_registro>
		<data_iscrizione></data_iscrizione>
		<provincia_iscrizione_registroimprese></provincia_iscrizione_registroimprese>
		<n_iscrizione_registroimprese></n_iscrizione_registroimprese>
		<data_iscrizione_registroimprese></data_iscrizione_registroimprese>
		<iscrizione_incorso></iscrizione_incorso>
		<agenzia></agenzia>
		<provincia_agenzia></provincia_agenzia>
		<comune_agenzia></comune_agenzia>
		<cod_comune_agenzia></cod_comune_agenzia>
		<cap_agenzia></cap_agenzia>
		<indirizzo_agenzia></indirizzo_agenzia>
		<cin></cin>
		<abi></abi>
		<cab></cab>
		<iban></iban>
		<bic></bic>
		<n_contocorrente></n_contocorrente>
		<istituto_di_credito></istituto_di_credito>
		<capitale_sociale></capitale_sociale>
		<campo_attivita></campo_attivita>
		<data_ultimo_esercizio></data_ultimo_esercizio>
		<ruolo_beneficiario></ruolo_beneficiario>
		<classificazione_ente></classificazione_ente>
		<incaricato></incaricato>
		<telefono_incaricato></telefono_incaricato>
		<fax_incaricato></fax_incaricato>
		<email_incaricato></email_incaricato>
		<codice_ramo_attivita_economica></codice_ramo_attivita_economica>
		<descrizione_ramo_attivita_economica></descrizione_ramo_attivita_economica>
		<descrizione_albo_professionale></descrizione_albo_professionale>
		<numero_albo_professionale></numero_albo_professionale> 
		<data_iscrizione_albo_professionale></data_iscrizione_albo_professionale>
		<quota></quota>
	</altre_anagrafiche>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="altreAnagraficheAzControllanti" > <!-- sezione FACOLTATIVA - dati di Aziende Controllanti -->
	<xsl:if test="_aziendeControllanti!=''">
	<xsl:for-each select="_aziendeControllanti/map/aziendeControllantiList/list/map">
	<altre_anagrafiche>
		<cod_tipo_utente>4</cod_tipo_utente>
		<tipo_utente>Azienda controllante</tipo_utente>
		<ragione_sociale_impresa><xsl:value-of select="azienda/map/denominazione"/></ragione_sociale_impresa>
		<tipologia_ente></tipologia_ente>
		<forma_giuridica_impresa><xsl:value-of select="azienda/map/descrFormaGiuridica"/></forma_giuridica_impresa>
		<cod_forma_giuridica_impresa><xsl:value-of select="azienda/map/codiceFormaGiuridica"/></cod_forma_giuridica_impresa>
		<codice_fiscale_impresa><xsl:value-of select="azienda/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>
		<partitaIva_sedelegale></partitaIva_sedelegale>
		<codice_ateco></codice_ateco>
		<descr_ateco></descr_ateco>
		<codice_attivita_ufficioitalianocambi></codice_attivita_ufficioitalianocambi>
		<descrizione_attivita_ufficiocambi></descrizione_attivita_ufficiocambi>
		<stato_sedelegale><xsl:value-of select="translate(azienda/map/descrNazioneSedeLegale,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/></stato_sedelegale>
		<cod_stato_sedelegale><xsl:value-of select="azienda/map/codiceNazioneSedeLegale"/></cod_stato_sedelegale>
		<cap_sedelegale></cap_sedelegale>
		<provincia_sedelegale></provincia_sedelegale>
		<codice_comune_sedelegale></codice_comune_sedelegale> 
		<codice_settore></codice_settore>
		<descrizione_settore></descrizione_settore>
		<codice_attivita_prevalente></codice_attivita_prevalente>
		<descrizione_attivita_prevalente></descrizione_attivita_prevalente>
		<comune_sedelegale></comune_sedelegale>
		<indirizzo_sedelegale></indirizzo_sedelegale>
		<telefono_sedelegale></telefono_sedelegale>
		<fax_sedelegale></fax_sedelegale>
		<email_sedelegale></email_sedelegale>
		<pec_sedelegale></pec_sedelegale>
		<data_costituzione_societa></data_costituzione_societa>
		<dimensione_azienda></dimensione_azienda>
		<cod_dimensione_azienda></cod_dimensione_azienda>
		<tipologia_registro_iscrizione>CCIAA</tipologia_registro_iscrizione>
		<data_iscrizione></data_iscrizione>
		<n_iscrizione_registro></n_iscrizione_registro>
		<data_iscrizione></data_iscrizione>
		<provincia_iscrizione_registroimprese></provincia_iscrizione_registroimprese>
		<n_iscrizione_registroimprese></n_iscrizione_registroimprese>
		<data_iscrizione_registroimprese></data_iscrizione_registroimprese>
		<iscrizione_incorso></iscrizione_incorso>
		<agenzia></agenzia>
		<provincia_agenzia></provincia_agenzia>
		<comune_agenzia></comune_agenzia>
		<cod_comune_agenzia></cod_comune_agenzia>
		<cap_agenzia></cap_agenzia>
		<indirizzo_agenzia></indirizzo_agenzia>
		<cin></cin>
		<abi></abi>
		<cab></cab>
		<iban></iban>
		<bic></bic>
		<n_contocorrente></n_contocorrente>
		<istituto_di_credito></istituto_di_credito>
		<capitale_sociale></capitale_sociale>
		<campo_attivita></campo_attivita>
		<data_ultimo_esercizio></data_ultimo_esercizio>
		<ruolo_beneficiario></ruolo_beneficiario>
		<classificazione_ente></classificazione_ente>
		<incaricato></incaricato>
		<telefono_incaricato></telefono_incaricato>
		<fax_incaricato></fax_incaricato>
		<email_incaricato></email_incaricato>
		<codice_ramo_attivita_economica></codice_ramo_attivita_economica>
		<descrizione_ramo_attivita_economica></descrizione_ramo_attivita_economica>
		<descrizione_albo_professionale></descrizione_albo_professionale>
		<numero_albo_professionale></numero_albo_professionale> 
		<data_iscrizione_albo_professionale></data_iscrizione_albo_professionale>
		<quota><xsl:value-of select="azienda/map/quota"/></quota>
	</altre_anagrafiche>
	</xsl:for-each>
	</xsl:if>
	</xsl:template>
 
	<xsl:template name="altreAnagraficheAzControllate" > <!-- sezione FACOLTATIVA - dati di Aziende Controllate -->
	<xsl:if test="_aziendeControllate!=''">
	<xsl:for-each select="_aziendeControllate/map/aziendeControllateList/list/map">	
	<altre_anagrafiche>
		<cod_tipo_utente>5</cod_tipo_utente>
		<tipo_utente>Azienda controllata</tipo_utente>
		<ragione_sociale_impresa><xsl:value-of select="azienda/map/denominazione"/></ragione_sociale_impresa>
		<tipologia_ente></tipologia_ente>
		<forma_giuridica_impresa><xsl:value-of select="azienda/map/descrFormaGiuridica"/></forma_giuridica_impresa>
		<cod_forma_giuridica_impresa><xsl:value-of select="azienda/map/codiceFormaGiuridica"/></cod_forma_giuridica_impresa>
		<codice_fiscale_impresa><xsl:value-of select="azienda/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>
		<partitaIva_sedelegale></partitaIva_sedelegale>
		<codice_ateco></codice_ateco>
		<descr_ateco></descr_ateco>
		<codice_attivita_ufficioitalianocambi></codice_attivita_ufficioitalianocambi>
		<descrizione_attivita_ufficiocambi></descrizione_attivita_ufficiocambi>
		<stato_sedelegale><xsl:value-of select="translate(azienda/map/descrNazioneSedeLegale,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/></stato_sedelegale>
		<cod_stato_sedelegale><xsl:value-of select="azienda/map/codiceNazioneSedeLegale"/></cod_stato_sedelegale>
		<cap_sedelegale></cap_sedelegale>
		<provincia_sedelegale></provincia_sedelegale>
		<codice_comune_sedelegale></codice_comune_sedelegale> 
		<codice_settore></codice_settore>
		<descrizione_settore></descrizione_settore>
		<codice_attivita_prevalente></codice_attivita_prevalente>
		<descrizione_attivita_prevalente></descrizione_attivita_prevalente>
		<comune_sedelegale></comune_sedelegale>
		<indirizzo_sedelegale></indirizzo_sedelegale>
		<telefono_sedelegale></telefono_sedelegale>
		<fax_sedelegale></fax_sedelegale>
		<email_sedelegale></email_sedelegale>
		<pec_sedelegale></pec_sedelegale>
		<data_costituzione_societa></data_costituzione_societa>
		<dimensione_azienda></dimensione_azienda>
		<cod_dimensione_azienda></cod_dimensione_azienda>
		<tipologia_registro_iscrizione>CCIAA</tipologia_registro_iscrizione>
		<data_iscrizione></data_iscrizione>
		<n_iscrizione_registro></n_iscrizione_registro>
		<data_iscrizione></data_iscrizione>
		<provincia_iscrizione_registroimprese></provincia_iscrizione_registroimprese>
		<n_iscrizione_registroimprese></n_iscrizione_registroimprese>
		<data_iscrizione_registroimprese></data_iscrizione_registroimprese>
		<iscrizione_incorso></iscrizione_incorso>
		<agenzia></agenzia>
		<provincia_agenzia></provincia_agenzia>
		<comune_agenzia></comune_agenzia>
		<cod_comune_agenzia></cod_comune_agenzia>
		<cap_agenzia></cap_agenzia>
		<indirizzo_agenzia></indirizzo_agenzia>
		<cin></cin>
		<abi></abi>
		<cab></cab>
		<iban></iban>
		<bic></bic>
		<n_contocorrente></n_contocorrente>
		<istituto_di_credito></istituto_di_credito>
		<capitale_sociale></capitale_sociale>
		<campo_attivita></campo_attivita>
		<data_ultimo_esercizio></data_ultimo_esercizio>
		<ruolo_beneficiario></ruolo_beneficiario>
		<classificazione_ente></classificazione_ente>
		<incaricato></incaricato>
		<telefono_incaricato></telefono_incaricato>
		<fax_incaricato></fax_incaricato>
		<email_incaricato></email_incaricato>
		<codice_ramo_attivita_economica></codice_ramo_attivita_economica>
		<descrizione_ramo_attivita_economica></descrizione_ramo_attivita_economica>
		<descrizione_albo_professionale></descrizione_albo_professionale>
		<numero_albo_professionale></numero_albo_professionale> 
		<data_iscrizione_albo_professionale></data_iscrizione_albo_professionale>
		<quota><xsl:value-of select="azienda/map/quota"/></quota>
	</altre_anagrafiche>
	</xsl:for-each>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="altreAnagraficheSocio" > <!-- sezione FACOLTATIVA - dati dei soci che hanno CodiceFiscale=11 char -->
	<xsl:if test="_azienda/map/aziendaSocioList!=''">
	<xsl:for-each select="_azienda/map/aziendaSocioList/list/map">
	<xsl:variable name="codLength" select="string-length(socio/map/codiceFiscale)" />
	<altre_anagrafiche>
		<cod_tipo_utente>6</cod_tipo_utente>
		<tipo_utente>Socio</tipo_utente>
		<ragione_sociale_impresa><xsl:value-of select="socio/map/denominazione"/></ragione_sociale_impresa>
		<tipologia_ente></tipologia_ente>
		<forma_giuridica_impresa></forma_giuridica_impresa>
		<cod_forma_giuridica_impresa>NA</cod_forma_giuridica_impresa>
		<codice_fiscale_impresa><xsl:value-of select="socio/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>
	<xsl:choose>
	<xsl:when test="$codLength = '11'">
		<partitaIva_sedelegale><xsl:value-of select="socio/map/codiceFiscale"/></partitaIva_sedelegale>
	</xsl:when>
	<xsl:otherwise>
		<partitaIva_sedelegale></partitaIva_sedelegale>
	</xsl:otherwise>
	</xsl:choose>
		<codice_ateco></codice_ateco>
		<descr_ateco></descr_ateco>
		<codice_attivita_ufficioitalianocambi></codice_attivita_ufficioitalianocambi>
		<descrizione_attivita_ufficiocambi></descrizione_attivita_ufficiocambi>
		<stato_sedelegale></stato_sedelegale>
		<cod_stato_sedelegale></cod_stato_sedelegale>
		<cap_sedelegale></cap_sedelegale>
		<provincia_sedelegale></provincia_sedelegale>
		<codice_comune_sedelegale></codice_comune_sedelegale> 
		<codice_settore></codice_settore>
		<descrizione_settore></descrizione_settore>
		<codice_attivita_prevalente></codice_attivita_prevalente>
		<descrizione_attivita_prevalente></descrizione_attivita_prevalente>
		<comune_sedelegale></comune_sedelegale>
		<indirizzo_sedelegale></indirizzo_sedelegale>
		<telefono_sedelegale></telefono_sedelegale>
		<fax_sedelegale></fax_sedelegale>
		<email_sedelegale></email_sedelegale>
		<pec_sedelegale></pec_sedelegale>
		<data_costituzione_societa></data_costituzione_societa>
		<dimensione_azienda></dimensione_azienda>
		<cod_dimensione_azienda></cod_dimensione_azienda>
		<tipologia_registro_iscrizione>CCIAA</tipologia_registro_iscrizione>
		<data_iscrizione></data_iscrizione>
		<n_iscrizione_registro></n_iscrizione_registro>
		<data_iscrizione></data_iscrizione>
		<provincia_iscrizione_registroimprese></provincia_iscrizione_registroimprese>
		<n_iscrizione_registroimprese></n_iscrizione_registroimprese>
		<data_iscrizione_registroimprese></data_iscrizione_registroimprese>
		<iscrizione_incorso></iscrizione_incorso>
		<agenzia></agenzia>
		<provincia_agenzia></provincia_agenzia>
		<comune_agenzia></comune_agenzia>
		<cod_comune_agenzia></cod_comune_agenzia>
		<cap_agenzia></cap_agenzia>
		<indirizzo_agenzia></indirizzo_agenzia>
		<cin></cin>
		<abi></abi>
		<cab></cab>
		<iban></iban>
		<bic></bic>
		<n_contocorrente></n_contocorrente>
		<istituto_di_credito></istituto_di_credito>
		<capitale_sociale></capitale_sociale>
		<campo_attivita></campo_attivita>
		<data_ultimo_esercizio></data_ultimo_esercizio>
		<ruolo_beneficiario></ruolo_beneficiario>
		<classificazione_ente></classificazione_ente>
		<incaricato></incaricato>
		<telefono_incaricato></telefono_incaricato>
		<fax_incaricato></fax_incaricato>
		<email_incaricato></email_incaricato>
		<codice_ramo_attivita_economica></codice_ramo_attivita_economica>
		<descrizione_ramo_attivita_economica></descrizione_ramo_attivita_economica>
		<descrizione_albo_professionale></descrizione_albo_professionale>
		<numero_albo_professionale></numero_albo_professionale> 
		<data_iscrizione_albo_professionale></data_iscrizione_albo_professionale> 
		<quota><xsl:value-of select="socio/map/quota"/></quota>
	</altre_anagrafiche>
	</xsl:for-each>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="anagraficaPartecipante" > <!-- sezione FACOLTATIVA -->
	<!-- sezione non utilizzata, non passo questo nodo -->
	<xsl:if test="_anagrafica_partecipante!=''">
	<anagrafica_partecipante>
		<codice_fiscale_impresa></codice_fiscale_impresa>
		<id_ente></id_ente>
		<ragione_sociale_impresa></ragione_sociale_impresa>
		<forma_giuridica_impresa></forma_giuridica_impresa>
		<partitaIva_impresa></partitaIva_impresa>
		<cap_sede></cap_sede>
		<stato_sede></stato_sede>
		<codice_comune_sedel></codice_comune_sedel>
		<comune_sede></comune_sede>
		<indirizzo_sede></indirizzo_sede>
		<telefono_sede></telefono_sede>
		<fax_sede></fax_sede>
		<email_sede></email_sede>
		<codice_ateco></codice_ateco>
		<persona_di_contatto></persona_di_contatto>
		<fatturato_ultimo_anno></fatturato_ultimo_anno>
		<addetti_ultimo_anno></addetti_ultimo_anno>
		<totale_mesi_uomo></totale_mesi_uomo>
		<unita_svolgimento></unita_svolgimento>
		<flag_capofila></flag_capofila>
	</anagrafica_partecipante>
	</xsl:if> 
	</xsl:template>
	
	<xsl:template name="spesePartner" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_spese_partner!=''">
	<spese_partner>
		<codice_tipologia_spesa></codice_tipologia_spesa>
		<descr_tipologia_spesa></descr_tipologia_spesa>
		<importo_spesa_richiesta_finanziata></importo_spesa_richiesta_finanziata>
		<tipo_periodo></tipo_periodo>
		<valore_periodo></valore_periodo>
		<anno_periodo></anno_periodo>
		<importo_spesa_richiesta_non_finanziata></importo_spesa_richiesta_non_finanziata>
		<cod_dett_intervento></cod_dett_intervento>
		<percentuale_iva></percentuale_iva>
		<importo_iva></importo_iva>
		<importo_totale></importo_totale>
		<flag_iva_costo></flag_iva_costo>
	</spese_partner>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="costiFunzioniAnnoPartner" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_funzioni_anno_partner">
	<costi_funzioni_anno_partner>
		<anno></anno>
		<funzione></funzione>
		<costo></costo>
		<descrizione_anno></descrizione_anno>
		<descrizione_funzione></descrizione_funzione>
	</costi_funzioni_anno_partner>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="costiRichiestiPartner" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_richiesti_partner!=''">
	<costi_richiesti_partner>
		<anno></anno>
		<funzione></funzione>
		<costo></costo>
		<descrizione_anno></descrizione_anno>
		<descrizione_funzione></descrizione_funzione>
	</costi_richiesti_partner>
	</xsl:if> 
	</xsl:template>
	
	<xsl:template name="contributiRichiestiPartner" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_contributi_richiesti_partner!=''">
	<contributi_richiesti_partner>
		<cod_contributo></cod_contributo>
		<descr_contributo></descr_contributo>
		<importo_contributo_anno1></importo_contributo_anno1>
		<importo_contributo_anno2></importo_contributo_anno2>
		<importo_contributo_anno3></importo_contributo_anno3>
	</contributi_richiesti_partner>
	</xsl:if> 
	</xsl:template>
           
	<!-- Template per Socio proveniente da DatiImpresa > Profilo dell'impresa > Socio -->
	<xsl:template name="anagraficaPersonaFisicaSocio"> <!-- Tipo : Socio -->
	<xsl:param name="codFiscOP" select="_operatorePresentatore/map/codiceFiscale"/>
	<xsl:for-each select="_azienda/map/aziendaSocioList/list/map">
	<xsl:variable name="codLength" select="string-length(socio/map/codiceFiscale)" />
	<xsl:if test="$codLength = '16'"> <!-- prendo solo quelli con CF effettivo non PIVA -->
	<anagrafica_persona_fisica>
		<codice_fiscale_impresa><xsl:value-of select="$codFiscOP"/></codice_fiscale_impresa>
		<id_ente></id_ente>  <!-- passare sempre null -->
		<tipo_personafisica>Socio</tipo_personafisica> 
		<id_soggetto></id_soggetto> <!-- null -->
		<codice_fiscale_soggetto><xsl:value-of select="socio/map/codiceFiscale"/></codice_fiscale_soggetto>
		<cognome><xsl:value-of select="socio/map/denominazione"/></cognome>
		<nome></nome>
		<data_nascita></data_nascita>
		<stato_nascita></stato_nascita>
		<codice_comune_nascita></codice_comune_nascita>
		<comune_nascita></comune_nascita>
		<provincia_nascita></provincia_nascita>
		<stato_indirizzo></stato_indirizzo>
		<cod_stato_indirizzo></cod_stato_indirizzo>
		<provincia_indirizzo></provincia_indirizzo>
		<codice_comune_indirizzo></codice_comune_indirizzo>
		<comune_indirizzo></comune_indirizzo>
		<cap_indirizzo></cap_indirizzo>
		<indirizzo></indirizzo>
		<telefono></telefono>
		<fax></fax>  <!-- passare sempre null -->
		<email></email>
		<tipo_documento_riconoscimento></tipo_documento_riconoscimento>
		<numero_documento_riconoscimento></numero_documento_riconoscimento>
		<datarilascio_documento_riconoscimento></datarilascio_documento_riconoscimento>
		<datascadenza_documento_riconoscimento></datascadenza_documento_riconoscimento> <!--  null -->
		<descrizione_ente_rilascio_documento_riconoscimento></descrizione_ente_rilascio_documento_riconoscimento>
		<quota_partecipazione><xsl:value-of select="socio/map/quota"/></quota_partecipazione>
		<genere_soggetto></genere_soggetto> <!-- passare sempre null -->
		<tipo_socio></tipo_socio> <!-- passare sempre null -->
		<titolo_studio></titolo_studio> <!-- passare sempre null -->
		<condizione_occupazionale></condizione_occupazionale> <!-- passare sempre null -->
		<cittadinanza></cittadinanza> <!-- passare sempre null -->
		<tipo_svantaggio></tipo_svantaggio> <!-- passare sempre null -->
		<flag_richiesta_sostegnoalreddito></flag_richiesta_sostegnoalreddito> <!-- passare sempre null -->
		<cod_ruolo></cod_ruolo> <!-- passare sempre null -->
		<ruolo></ruolo> <!-- passare sempre null -->
		<cod_tipo_accesso></cod_tipo_accesso> <!-- passare sempre null -->
		<tipo_accesso></tipo_accesso> <!-- passare sempre null -->
		<login></login> <!-- passare sempre null -->
		<carica></carica> <!-- passare sempre null -->
		<cod_cittadinanza></cod_cittadinanza> <!-- passare sempre null -->
		<cod_tipo_soggetto></cod_tipo_soggetto> <!-- passare sempre null -->
		<nucleo_familiare> <!-- passare sempre null -->
			<codice_nucleo_familiare></codice_nucleo_familiare> <!-- passare sempre null -->
			<descrizione_nucleo_familiare></descrizione_nucleo_familiare> <!-- passare sempre null -->
			<valore_nucleo_familiare></valore_nucleo_familiare> <!-- passare sempre null -->
		</nucleo_familiare> <!-- passare sempre null -->
	</anagrafica_persona_fisica>
	</xsl:if>
	</xsl:for-each>
	</xsl:template>
	
	<!-- Persona dell'impresa autorizzata ad intrattenere contatti con Adg/OI -->
	<xsl:template name="anagraficaPersonaFisicaPersonaAut"> <!-- Tipo : Referente -->
	<xsl:if test="_riferimenti/map/personaImpresa/map/codiceFiscale!=''">
	<anagrafica_persona_fisica>
		<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatore/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>  <!-- passare sempre null -->
		<tipo_personafisica>Referente</tipo_personafisica>
		<id_soggetto></id_soggetto> <!-- null -->
		<codice_fiscale_soggetto><xsl:value-of select="_riferimenti/map/personaImpresa/map/codiceFiscale"/></codice_fiscale_soggetto>
		<cognome><xsl:value-of select="_riferimenti/map/personaImpresa/map/cognome"/></cognome>
		<nome><xsl:value-of select="_riferimenti/map/personaImpresa/map/nome"/></nome>
		<data_nascita></data_nascita>
		<stato_nascita></stato_nascita>
		<codice_comune_nascita></codice_comune_nascita>
		<comune_nascita></comune_nascita>
		<provincia_nascita></provincia_nascita>
		<stato_indirizzo></stato_indirizzo>
		<cod_stato_indirizzo></cod_stato_indirizzo>
		<provincia_indirizzo></provincia_indirizzo>
		<codice_comune_indirizzo></codice_comune_indirizzo>
		<comune_indirizzo></comune_indirizzo>
		<cap_indirizzo></cap_indirizzo>
		<indirizzo></indirizzo>
		<telefono><xsl:value-of select="_riferimenti/map/personaImpresa/map/telefono"/></telefono>
		<fax></fax>  <!-- passare sempre null -->
		<email><xsl:value-of select="_riferimenti/map/personaImpresa/map/email"/></email>
		<tipo_documento_riconoscimento></tipo_documento_riconoscimento>
		<numero_documento_riconoscimento></numero_documento_riconoscimento>
		<datarilascio_documento_riconoscimento></datarilascio_documento_riconoscimento>
		<datascadenza_documento_riconoscimento></datascadenza_documento_riconoscimento> <!--  null -->
		<descrizione_ente_rilascio_documento_riconoscimento></descrizione_ente_rilascio_documento_riconoscimento>
		<quota_partecipazione></quota_partecipazione> <!-- passare sempre null -->
		<genere_soggetto></genere_soggetto> <!-- passare sempre null -->
		<tipo_socio></tipo_socio> <!-- passare sempre null -->
		<titolo_studio></titolo_studio> <!-- passare sempre null -->
		<condizione_occupazionale></condizione_occupazionale> <!-- passare sempre null -->
		<cittadinanza></cittadinanza> <!-- passare sempre null -->
		<tipo_svantaggio></tipo_svantaggio> <!-- passare sempre null -->
		<flag_richiesta_sostegnoalreddito></flag_richiesta_sostegnoalreddito> <!-- passare sempre null -->
		<cod_ruolo></cod_ruolo> <!-- passare sempre null -->
		<ruolo></ruolo> <!-- passare sempre null -->
		<cod_tipo_accesso></cod_tipo_accesso> <!-- passare sempre null -->
		<tipo_accesso></tipo_accesso> <!-- passare sempre null -->
		<login></login> <!-- passare sempre null -->
		<carica></carica> <!-- passare sempre null -->
		<cod_cittadinanza></cod_cittadinanza> <!-- passare sempre null -->
		<cod_tipo_soggetto></cod_tipo_soggetto> <!-- passare sempre null -->
		<nucleo_familiare> <!-- passare sempre null -->
			<codice_nucleo_familiare></codice_nucleo_familiare> <!-- passare sempre null -->
			<descrizione_nucleo_familiare></descrizione_nucleo_familiare> <!-- passare sempre null -->
			<valore_nucleo_familiare></valore_nucleo_familiare> <!-- passare sempre null -->
		</nucleo_familiare> <!-- passare sempre null -->
	</anagrafica_persona_fisica>
	</xsl:if>
	</xsl:template>
	
	<!-- Consulente autorizzato ad intrattenere contatti con Adg/OI -->
	<xsl:template name="anagraficaPersonaFisicaConsulenteAut"> <!-- Tipo : Referente -->
	<xsl:if test="_riferimenti/map/consulente/map/codiceFiscale!=''">
	<anagrafica_persona_fisica> 
		<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatore/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente> <!-- passare sempre null -->
		<tipo_personafisica>Consulente</tipo_personafisica> 
		<id_soggetto></id_soggetto> <!-- null -->
		<codice_fiscale_soggetto><xsl:value-of select="_riferimenti/map/consulente/map/codiceFiscale"/></codice_fiscale_soggetto>
		<cognome><xsl:value-of select="_riferimenti/map/consulente/map/cognome"/></cognome>
		<nome><xsl:value-of select="_riferimenti/map/consulente/map/nome"/></nome>
		<data_nascita></data_nascita>
		<stato_nascita></stato_nascita>
		<codice_comune_nascita></codice_comune_nascita>
		<comune_nascita></comune_nascita>
		<provincia_nascita></provincia_nascita>
		<stato_indirizzo></stato_indirizzo>
		<cod_stato_indirizzo></cod_stato_indirizzo>
		<provincia_indirizzo></provincia_indirizzo>
		<codice_comune_indirizzo></codice_comune_indirizzo>
		<comune_indirizzo></comune_indirizzo>
		<cap_indirizzo></cap_indirizzo>
		<indirizzo></indirizzo>
		<telefono><xsl:value-of select="_riferimenti/map/consulente/map/telefono"/></telefono>
		<fax></fax>  <!-- passare sempre null -->
		<email><xsl:value-of select="_riferimenti/map/consulente/map/email"/></email>
		<tipo_documento_riconoscimento></tipo_documento_riconoscimento>
		<numero_documento_riconoscimento></numero_documento_riconoscimento>
		<datarilascio_documento_riconoscimento></datarilascio_documento_riconoscimento>
		<datascadenza_documento_riconoscimento></datascadenza_documento_riconoscimento> <!--  null -->
		<descrizione_ente_rilascio_documento_riconoscimento></descrizione_ente_rilascio_documento_riconoscimento>
		<quota_partecipazione></quota_partecipazione> <!-- passare sempre null -->
		<genere_soggetto></genere_soggetto> <!-- passare sempre null -->
		<tipo_socio></tipo_socio> <!-- passare sempre null -->
		<titolo_studio></titolo_studio> <!-- passare sempre null -->
		<condizione_occupazionale></condizione_occupazionale> <!-- passare sempre null -->
		<cittadinanza></cittadinanza> <!-- passare sempre null -->
		<tipo_svantaggio></tipo_svantaggio> <!-- passare sempre null -->
		<flag_richiesta_sostegnoalreddito></flag_richiesta_sostegnoalreddito> <!-- passare sempre null -->
		<cod_ruolo></cod_ruolo> <!-- passare sempre null -->
		<ruolo></ruolo> <!-- passare sempre null -->
		<cod_tipo_accesso></cod_tipo_accesso> <!-- passare sempre null -->
		<tipo_accesso></tipo_accesso> <!-- passare sempre null -->
		<login></login> <!-- passare sempre null -->
		<carica></carica> <!-- passare sempre null -->
		<cod_cittadinanza></cod_cittadinanza> <!-- passare sempre null -->
		<cod_tipo_soggetto></cod_tipo_soggetto> <!-- passare sempre null -->
		<nucleo_familiare> <!-- passare sempre null -->
			<codice_nucleo_familiare></codice_nucleo_familiare> <!-- passare sempre null -->
			<descrizione_nucleo_familiare></descrizione_nucleo_familiare> <!-- passare sempre null -->
			<valore_nucleo_familiare></valore_nucleo_familiare> <!-- passare sempre null -->
		</nucleo_familiare> <!-- passare sempre null -->
	</anagrafica_persona_fisica>
	</xsl:if>
	</xsl:template>

<xsl:template name="anagraficaPersonaFisicaAAEP" > <!-- Tipo : Legale Rappresentante DATI PROVENIENTI DA AAEP-->
	<anagrafica_persona_fisica_AAEP> 
		<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatoreAAEP/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>  <!-- passare sempre null -->
		<tipo_personafisica>Rappresentante Legale</tipo_personafisica>
		<id_soggetto></id_soggetto> <!-- null -->
		<codice_fiscale_soggetto><xsl:value-of select="_legaleRappresentanteAAEP/map/codiceFiscale"/></codice_fiscale_soggetto>
		<cognome><xsl:value-of select="_legaleRappresentanteAAEP/map/cognome"/></cognome>
		<nome><xsl:value-of select="_legaleRappresentanteAAEP/map/nome"/></nome>
		<data_nascita><xsl:value-of select="_legaleRappresentanteAAEP/map/dataNascita"/></data_nascita>	
	<xsl:choose>
		<xsl:when test="_legaleRappresentanteAAEP/map/siglaProvinciaNascita!=''">
			<comune_nascita><xsl:value-of select="_legaleRappresentanteAAEP/map/comuneNascitaDescrizione"/></comune_nascita>
			<provincia_nascita><xsl:value-of select="_legaleRappresentanteAAEP/map/siglaProvinciaNascita"/></provincia_nascita>
		</xsl:when>
		<xsl:otherwise>
			<stato_nascita><xsl:value-of select="_legaleRappresentanteAAEP/map/statoEsteroNascitaDescrizione"/></stato_nascita>
			<codice_comune_nascita></codice_comune_nascita>
			<comune_nascita></comune_nascita>			
		</xsl:otherwise>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test="_legaleRappresentanteAAEP/map/siglaProvinciaResidenza!=''">			
			<provincia_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/siglaProvinciaResidenza"/></provincia_indirizzo>
			<codice_comune_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/comuneResidenza"/></codice_comune_indirizzo>
			<comune_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/comuneResidenzaDescrizione"/></comune_indirizzo>
		</xsl:when>		
		<xsl:otherwise>
			<stato_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/statoEsteroResidenzaDescrizione"/></stato_indirizzo>
			<cod_stato_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/comuneResidenza"/></cod_stato_indirizzo>			
			<provincia_indirizzo></provincia_indirizzo>
			<codice_comune_indirizzo></codice_comune_indirizzo>
			<comune_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/comuneResidenzaDescrizione"/></comune_indirizzo>
		</xsl:otherwise>
	</xsl:choose>
		<cap_indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/cap"/></cap_indirizzo>
		<indirizzo><xsl:value-of select="_legaleRappresentanteAAEP/map/indirizzo"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="_legaleRappresentanteAAEP/map/numCivico"/></indirizzo>		
		<genere_soggetto><xsl:value-of select="_legaleRappresentanteAAEP/map/genere"/></genere_soggetto> <!-- passare sempre null -->		
	</anagrafica_persona_fisica_AAEP> 
	</xsl:template>
		
	<xsl:template name="anagraficaPersonaFisica" > <!-- Tipo : Legale Rappresentante -->
	<anagrafica_persona_fisica> 
		<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatore/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>  <!-- passare sempre null -->
		<tipo_personafisica>Rappresentante Legale</tipo_personafisica>
		<id_soggetto></id_soggetto> <!-- null -->
		<codice_fiscale_soggetto><xsl:value-of select="_legaleRappresentante/map/codiceFiscale"/></codice_fiscale_soggetto>
		<cognome><xsl:value-of select="_legaleRappresentante/map/cognome"/></cognome>
		<nome><xsl:value-of select="_legaleRappresentante/map/nome"/></nome>
		<data_nascita><xsl:value-of select="_legaleRappresentante/map/dataNascita"/></data_nascita>
	<xsl:choose>
		<xsl:when test="_legaleRappresentante/map/luogoNascita='Italia'">
			<stato_nascita><xsl:value-of select="_legaleRappresentante/map/luogoNascita"/></stato_nascita>
			<codice_comune_nascita><xsl:value-of select="_legaleRappresentante/map/comuneNascita"/></codice_comune_nascita>
			<comune_nascita><xsl:value-of select="_legaleRappresentante/map/comuneNascitaDescrizione"/></comune_nascita>
			<provincia_nascita><xsl:value-of select="_legaleRappresentante/map/siglaProvinciaNascita"/></provincia_nascita>
		</xsl:when>
		<xsl:otherwise>
			<stato_nascita><xsl:value-of select="_legaleRappresentante/map/statoEsteroNascitaDescrizione"/></stato_nascita>
			<codice_comune_nascita></codice_comune_nascita>
			<comune_nascita></comune_nascita>
			<provincia_nascita></provincia_nascita>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test="_legaleRappresentante/map/luogoResidenza='Italia'">
			<stato_indirizzo><xsl:value-of select="_legaleRappresentante/map/luogoResidenza"/></stato_indirizzo>
			<cod_stato_indirizzo>000</cod_stato_indirizzo>
			<provincia_indirizzo><xsl:value-of select="_legaleRappresentante/map/siglaProvinciaResidenza"/></provincia_indirizzo>
			<codice_comune_indirizzo><xsl:value-of select="_legaleRappresentante/map/comuneResidenza"/></codice_comune_indirizzo>
			<comune_indirizzo><xsl:value-of select="_legaleRappresentante/map/comuneResidenzaDescrizione"/></comune_indirizzo>
		</xsl:when>
		<xsl:otherwise>
			<stato_indirizzo><xsl:value-of select="_legaleRappresentante/map/statoEsteroResidenzaDescrizione"/></stato_indirizzo>
			<cod_stato_indirizzo><xsl:value-of select="_legaleRappresentante/map/statoEsteroResidenza"/></cod_stato_indirizzo>
			<provincia_indirizzo></provincia_indirizzo>
			<codice_comune_indirizzo></codice_comune_indirizzo>
			<comune_indirizzo><xsl:value-of select="_legaleRappresentante/map/cittaEsteraResidenza"/></comune_indirizzo>
		</xsl:otherwise>
	</xsl:choose>
		<cap_indirizzo><xsl:value-of select="_legaleRappresentante/map/cap"/></cap_indirizzo>
		<indirizzo><xsl:value-of select="_legaleRappresentante/map/indirizzo"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="_legaleRappresentante/map/numCivico"/></indirizzo>
		<telefono></telefono>  <!-- passare sempre null -->
		<fax></fax>  <!-- passare sempre null -->
		<email></email>  <!-- passare sempre null -->
		<tipo_documento_riconoscimento><xsl:value-of select="_legaleRappresentante/map/documento/map/descrizioneTipoDocRiconoscimento"/></tipo_documento_riconoscimento>
		<numero_documento_riconoscimento><xsl:value-of select="_legaleRappresentante/map/documento/map/numDocumentoRiconoscimento"/></numero_documento_riconoscimento>
		<datarilascio_documento_riconoscimento><xsl:value-of select="_legaleRappresentante/map/documento/map/dataRilascioDoc"/></datarilascio_documento_riconoscimento>
		<datascadenza_documento_riconoscimento></datascadenza_documento_riconoscimento> <!--  null -->
		<descrizione_ente_rilascio_documento_riconoscimento><xsl:value-of select="_legaleRappresentante/map/documento/map/docRilasciatoDa"/></descrizione_ente_rilascio_documento_riconoscimento>
		<quota_partecipazione></quota_partecipazione> <!-- passare sempre null -->
		<genere_soggetto></genere_soggetto> <!-- passare sempre null -->
		<tipo_socio></tipo_socio> <!-- passare sempre null -->
		<titolo_studio></titolo_studio> <!-- passare sempre null -->
		<condizione_occupazionale></condizione_occupazionale> <!-- passare sempre null -->
		<cittadinanza></cittadinanza> <!-- passare sempre null -->
		<tipo_svantaggio></tipo_svantaggio> <!-- passare sempre null -->
		<flag_richiesta_sostegnoalreddito></flag_richiesta_sostegnoalreddito> <!-- passare sempre null -->
		<cod_ruolo></cod_ruolo> <!-- passare sempre null -->
		<ruolo></ruolo> <!-- passare sempre null -->
		<cod_tipo_accesso></cod_tipo_accesso> <!-- passare sempre null -->
		<tipo_accesso></tipo_accesso> <!-- passare sempre null -->
		<login></login> <!-- passare sempre null -->
		<carica></carica> <!-- passare sempre null -->
		<cod_cittadinanza></cod_cittadinanza> <!-- passare sempre null -->
		<cod_tipo_soggetto></cod_tipo_soggetto> <!-- passare sempre null -->
		<nucleo_familiare> <!-- passare sempre null -->
			<codice_nucleo_familiare></codice_nucleo_familiare> <!-- passare sempre null -->
			<descrizione_nucleo_familiare></descrizione_nucleo_familiare> <!-- passare sempre null -->
			<valore_nucleo_familiare></valore_nucleo_familiare> <!-- passare sempre null -->
		</nucleo_familiare> <!-- passare sempre null -->
		<presenza_soggetto_delegato><xsl:value-of select="_legaleRappresentante/map/presenzaSoggettoDelegato"/></presenza_soggetto_delegato>
	</anagrafica_persona_fisica> 
	</xsl:template>
	
	<xsl:template name="anagraficaSoggettoDelegato" > <!-- Tipo : Soggetto delegato -->
	<xsl:if test="_soggettoDelegato!=''">
	<anagrafica_persona_fisica> 
		<codice_fiscale_impresa><xsl:value-of select="_operatorePresentatore/map/codiceFiscale"/></codice_fiscale_impresa>
		<id_ente></id_ente>  <!-- passare sempre null -->
		<tipo_personafisica>Soggetto delegato</tipo_personafisica>
		<id_soggetto></id_soggetto> <!-- null -->
		<codice_fiscale_soggetto><xsl:value-of select="_soggettoDelegato/map/codiceFiscale"/></codice_fiscale_soggetto>
		<cognome><xsl:value-of select="_soggettoDelegato/map/cognome"/></cognome>
		<nome><xsl:value-of select="_soggettoDelegato/map/nome"/></nome>
		<data_nascita><xsl:value-of select="_soggettoDelegato/map/dataNascita"/></data_nascita>
	<xsl:choose>
		<xsl:when test="_soggettoDelegato/map/luogoNascita='Italia'">
			<stato_nascita><xsl:value-of select="_soggettoDelegato/map/luogoNascita"/></stato_nascita>
			<codice_comune_nascita><xsl:value-of select="_soggettoDelegato/map/comuneNascita"/></codice_comune_nascita>
			<comune_nascita><xsl:value-of select="_soggettoDelegato/map/comuneNascitaDescrizione"/></comune_nascita>
			<provincia_nascita><xsl:value-of select="_soggettoDelegato/map/siglaProvinciaNascita"/></provincia_nascita>
		</xsl:when>
		<xsl:otherwise>
			<stato_nascita><xsl:value-of select="_soggettoDelegato/map/statoEsteroNascitaDescrizione"/></stato_nascita>			
			<codice_comune_nascita></codice_comune_nascita>
			<comune_nascita></comune_nascita>
			<provincia_nascita></provincia_nascita>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test="_soggettoDelegato/map/luogoResidenza='Italia'">
			<stato_indirizzo><xsl:value-of select="_soggettoDelegato/map/luogoResidenza"/></stato_indirizzo>
			<cod_stato_indirizzo>000</cod_stato_indirizzo>
			<provincia_indirizzo><xsl:value-of select="_soggettoDelegato/map/siglaProvinciaResidenza"/></provincia_indirizzo>
			<codice_comune_indirizzo><xsl:value-of select="_soggettoDelegato/map/comuneResidenza"/></codice_comune_indirizzo>
			<comune_indirizzo><xsl:value-of select="_soggettoDelegato/map/comuneResidenzaDescrizione"/></comune_indirizzo>
		</xsl:when>
		<xsl:otherwise>
			<stato_indirizzo><xsl:value-of select="_soggettoDelegato/map/statoEsteroResidenzaDescrizione"/></stato_indirizzo>
			<cod_stato_indirizzo><xsl:value-of select="_soggettoDelegato/map/statoEsteroResidenza"/></cod_stato_indirizzo>
			<citta_estera><xsl:value-of select="_soggettoDelegato/map/cittaEsteraResidenza"/></citta_estera>
			<provincia_indirizzo></provincia_indirizzo>
			<codice_comune_indirizzo></codice_comune_indirizzo>
			<comune_indirizzo></comune_indirizzo>
		</xsl:otherwise>
	</xsl:choose>
		<cap_indirizzo><xsl:value-of select="_soggettoDelegato/map/cap"/></cap_indirizzo>
		<indirizzo><xsl:value-of select="_soggettoDelegato/map/indirizzo"/>,<xsl:text>&#32;</xsl:text><xsl:value-of select="_soggettoDelegato/map/numCivico"/></indirizzo>
		<telefono></telefono>  <!-- passare sempre null -->
		<fax></fax>  <!-- passare sempre null -->
		<email></email>  <!-- passare sempre null -->
		<tipo_documento_riconoscimento><xsl:value-of select="_soggettoDelegato/map/documento/map/descrizioneTipoDocRiconoscimento"/></tipo_documento_riconoscimento>
		<numero_documento_riconoscimento><xsl:value-of select="_soggettoDelegato/map/documento/map/numDocumentoRiconoscimento"/></numero_documento_riconoscimento>
		<datarilascio_documento_riconoscimento><xsl:value-of select="_soggettoDelegato/map/documento/map/dataRilascioDoc"/></datarilascio_documento_riconoscimento>
		<datascadenza_documento_riconoscimento></datascadenza_documento_riconoscimento> <!--  null -->
		<descrizione_ente_rilascio_documento_riconoscimento><xsl:value-of select="_soggettoDelegato/map/documento/map/docRilasciatoDa"/></descrizione_ente_rilascio_documento_riconoscimento>
		<quota_partecipazione></quota_partecipazione> <!-- passare sempre null -->
		<genere_soggetto></genere_soggetto> <!-- passare sempre null -->
		<tipo_socio></tipo_socio> <!-- passare sempre null -->
		<titolo_studio></titolo_studio> <!-- passare sempre null -->
		<condizione_occupazionale></condizione_occupazionale> <!-- passare sempre null -->
		<cittadinanza></cittadinanza> <!-- passare sempre null -->
		<tipo_svantaggio></tipo_svantaggio> <!-- passare sempre null -->
		<flag_richiesta_sostegnoalreddito></flag_richiesta_sostegnoalreddito> <!-- passare sempre null -->
		<cod_ruolo></cod_ruolo> <!-- passare sempre null -->
		<ruolo></ruolo> <!-- passare sempre null -->
		<cod_tipo_accesso></cod_tipo_accesso> <!-- passare sempre null -->
		<tipo_accesso></tipo_accesso> <!-- passare sempre null -->
		<login></login> <!-- passare sempre null -->
		<carica></carica> <!-- passare sempre null -->
		<cod_cittadinanza></cod_cittadinanza> <!-- passare sempre null -->
		<cod_tipo_soggetto></cod_tipo_soggetto> <!-- passare sempre null -->
		<nucleo_familiare> <!-- passare sempre null -->
			<codice_nucleo_familiare></codice_nucleo_familiare> <!-- passare sempre null -->
			<descrizione_nucleo_familiare></descrizione_nucleo_familiare> <!-- passare sempre null -->
			<valore_nucleo_familiare></valore_nucleo_familiare> <!-- passare sempre null -->
		</nucleo_familiare> <!-- passare sempre null -->
	</anagrafica_persona_fisica> 
	</xsl:if>
	</xsl:template>

	<xsl:template name="anagraficaProdotti" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_anagrafica_prodotti!=''">
	<anagrafica_prodotti>
		<descrizione_prodotto></descrizione_prodotto>
		<ricavo_ultimo_esercizio></ricavo_ultimo_esercizio>
		<tipo_mercato></tipo_mercato>
		<cod_tipo_mercato></cod_tipo_mercato>
		<percentuale_posizionamento></percentuale_posizionamento>
	</anagrafica_prodotti>
	</xsl:if> 
	</xsl:template>

	<xsl:template name="fatturatoA1" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_dimensioni/map/datiImpresaList!=''">
	<fatturato>
		<descrizione_spesafatturato>fatturato</descrizione_spesafatturato>  <!-- valori :  (totale bilancio) o  (fatturato) o (ULA) -->
		<cod_spesafatturato>FT</cod_spesafatturato> <!-- valori : TB (se prima totale bilancio) o FT (se prima fatturato) o UL (se prima ULA)-->
		<importo_ultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[1]/fatturato" /></importo_ultimoanno>
		<importo_penultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[2]/fatturato" /></importo_penultimoanno>
		<importo_terzultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[3]/fatturato" /></importo_terzultimoanno>
	</fatturato>
	</xsl:if>
	</xsl:template>

	<xsl:template name="dimensioniAAEP" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_dimensioniAAEP/map/datiImpresaList!=''">
	<dimensioniAAEP>		
		<anno><xsl:value-of select="_dimensioniAAEP/map/datiImpresaList/list/map[1]/anno" /></anno>
		<fatturato><xsl:value-of select="_dimensioniAAEP/map/datiImpresaList/list/map[1]/fatturato" /></fatturato>
		<tot_bilancio><xsl:value-of select="_dimensioniAAEP/map/datiImpresaList/list/map[1]/totBilancio" /></tot_bilancio>
		<anno_precedente><xsl:value-of select="_dimensioniAAEP/map/datiImpresaList/list/map[2]/anno" /></anno_precedente>
		<fatturato_precedente><xsl:value-of select="_dimensioniAAEP/map/datiImpresaList/list/map[2]/fatturato" /></fatturato_precedente>
		<tot_bilancio_precedente><xsl:value-of select="_dimensioniAAEP/map/datiImpresaList/list/map[2]/totBilancio" /></tot_bilancio_precedente>
	</dimensioniAAEP>
	</xsl:if>
	</xsl:template>	
	
	<xsl:template name="fatturatoTotBilancio" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_dimensioni/map/datiImpresaList!=''">
	<fatturato>
		<descrizione_spesafatturato>totale bilancio</descrizione_spesafatturato>  <!-- valori :  (totale bilancio) o  (fatturato) o (ULA) -->
		<cod_spesafatturato>TB</cod_spesafatturato> <!-- valori : TB (se prima totale bilancio) o FT (se prima fatturato) o UL (se prima ULA)-->
		<importo_ultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[1]/totBilancio" /></importo_ultimoanno>
		<importo_penultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[2]/totBilancio" /></importo_penultimoanno>
		<importo_terzultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[3]/totBilancio" /></importo_terzultimoanno>
	</fatturato>
	</xsl:if>
	</xsl:template>

	<xsl:template name="fatturatoULA" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_dimensioni/map/datiImpresaList!=''">
	<fatturato>
		<descrizione_spesafatturato>ULA</descrizione_spesafatturato>  <!-- valori :  (totale bilancio) o  (fatturato) o (ULA) -->
		<cod_spesafatturato>UL</cod_spesafatturato> <!-- valori : TB (se prima totale bilancio) o FT (se prima fatturato) o UL (se prima ULA)-->
		<importo_ultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[1]/ula" /></importo_ultimoanno>
		<importo_penultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[2]/ula" /></importo_penultimoanno>
		<importo_terzultimoanno><xsl:value-of select="_dimensioni/map/datiImpresaList/list/map[3]/ula" /></importo_terzultimoanno>
	</fatturato>
	</xsl:if>
	</xsl:template>

	<xsl:template name="risorseUmane" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="count(_dimensioni/map/risorseUmaneList/list/map)>'0'">
		<xsl:for-each select="_dimensioni/map/risorseUmaneList/list/map">
		<risorse_umane>
		<tipologia_risorsa></tipologia_risorsa> 
		<descrizione_risorsa><xsl:value-of select="categoria"/></descrizione_risorsa>
		<totale_uomini><xsl:value-of select="numUomini"/></totale_uomini>
		<totale_donne><xsl:value-of select="numDonne"/></totale_donne>
		</risorse_umane>
		</xsl:for-each>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="cronoprogramma" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_cronoprogramma!=''">
	<cronoprogramma> 
		<durata_totale></durata_totale>
		<tipo_periodo_durata_totale></tipo_periodo_durata_totale>
		<codice_tipo_attivita></codice_tipo_attivita>
		<tipo_attivita></tipo_attivita>
		<tipo_periodo></tipo_periodo>
		<valore_periodo></valore_periodo>
		<anno_periodo></anno_periodo>
		<valore_anno_periodo></valore_anno_periodo>
		<durata_attivita></durata_attivita>
		<identificativo_numero></identificativo_numero>
		<descrizione_estesa_attivita></descrizione_estesa_attivita> 
	</cronoprogramma>
	</xsl:if> 
	</xsl:template>
	
	<xsl:template name="cronoprogrammaDiSpesa" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_cronoprogramma_dispesa!=''">
	<cronoprogramma_spesa>
		<durata_totale></durata_totale>
		<tipo_periodo_durata_totale></tipo_periodo_durata_totale>
		<codice_Tipo_attivita></codice_Tipo_attivita>
		<tipo_attivita></tipo_attivita>
		<tipo_periodo></tipo_periodo>
		<valore_periodo></valore_periodo>
		<anno_periodo></anno_periodo>
		<valore_anno_periodo></valore_anno_periodo>
		<durata_attivita></durata_attivita>
		<identificativo_numero></identificativo_numero>
		<descrizione_estesa_attivita></descrizione_estesa_attivita>
	</cronoprogramma_spesa>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="indicatoriAmbiente" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_indicatori_ambiente!=''">
	<indicatori_ambiente>
		<codice_indicatore></codice_indicatore>
		<descr_indicatore></descr_indicatore>
		<unita_misura></unita_misura>
		<valore_previsionale_inizio></valore_previsionale_inizio>
		<valore_previsionale_fine></valore_previsionale_fine>
	</indicatori_ambiente>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="indicatoriDiRisultato" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_indicatori_dirisultato!=''">
		<indicatori_di_risultato>
			<codice_indicatore></codice_indicatore>
			<descr_indicatore></descr_indicatore>
			<unita_misura></unita_misura>
			<valore_previsionale_inizio></valore_previsionale_inizio>
			<valore_previsionale_fine></valore_previsionale_fine>
		</indicatori_di_risultato>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="speseProgetto" >
	<xsl:for-each select="_pianoSpese/map/pianoSpeseList/list/map">
	<xsl:if test="(codTipoIntervento!='')  and (totaleVoceSpesa!='')">
	<spese_progetto> 
		<codice_tipologia_spesa><xsl:value-of select="codVoceSpesa"/></codice_tipologia_spesa>
		<descr_tipologia_spesa>
			<xsl:value-of select="descrTipoIntervento"/>
			<xsl:if test="descrDettIntervento!=''">-<xsl:value-of select="descrDettIntervento"/></xsl:if>
			<xsl:if test="descrVoceSpesa!=''">-<xsl:value-of select="descrVoceSpesa"/></xsl:if>
		</descr_tipologia_spesa>
		<importo_spesa_richiesta_finanziata><xsl:value-of select="totaleVoceSpesa"/></importo_spesa_richiesta_finanziata>
		<tipo_periodo></tipo_periodo>  <!-- null -->
		<valore_periodo></valore_periodo>  <!-- null -->
		<anno_periodo></anno_periodo>  <!-- null -->
		<importo_spesa_richiesta_non_finanziata></importo_spesa_richiesta_non_finanziata>  <!-- null -->
		<cod_dett_intervento>
			<xsl:value-of select="codTipoIntervento"/>
			<xsl:if test="codDettIntervento!=''">-<xsl:value-of select="codDettIntervento"/></xsl:if>
		</cod_dett_intervento>
		<percentuale_iva></percentuale_iva>  <!-- null -->
		<importo_iva></importo_iva>  <!-- null -->
		<importo_totale><xsl:value-of select="totaleVoceSpesa"/></importo_totale> 
		<flag_iva_costo>no</flag_iva_costo>  <!-- 'no' fisso -->
	</spese_progetto>
	</xsl:if>
	</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="sovracosti" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_sovracosti!=''">
	<sovracosti>
		<impianto_proposto_costo></impianto_proposto_costo>
		<impianto_proposto_bilancio_annuale></impianto_proposto_bilancio_annuale>
		<impianto_proposto_numero_anni></impianto_proposto_numero_anni>
		<impianto_proposto_per_anni></impianto_proposto_per_anni>
		<impianto_tradizionale_costo></impianto_tradizionale_costo>
		<impianto_tradizionale_bilancio_annuale></impianto_tradizionale_bilancio_annuale>
		<impianto_tradizionale_numero_anni></impianto_tradizionale_numero_anni>
		<impianto_tradizionale_bilancio_per_anni></impianto_tradizionale_bilancio_per_anni>
		<differenza_di_costo></differenza_di_costo>
		<differenza_bilancio_anni></differenza_bilancio_anni>
		<sovraccosti></sovraccosti>
	</sovracosti>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="costiPersonale" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_personale!=''">
	<costi_personale>
		<codice_tipo_rapporto></codice_tipo_rapporto>
		<descrizione_tipo_rapporto></descrizione_tipo_rapporto>
		<numero_unita></numero_unita>
		<qualifica></qualifica>
		<costo></costo>
		<tipo_periodo_impegno_lavorativo></tipo_periodo_impegno_lavorativo>
		<impegno_lavorativo></impegno_lavorativo>
		<anno_costi></anno_costi>
		<codice_fiscale_impresa_riferimento></codice_fiscale_impresa_riferimento>
		<eta></eta>
	</costi_personale>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="costiEntiRicerca" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_enti_ricerca!=''">
	<costi_enti_ricerca>
		<codice_fiscale_ente></codice_fiscale_ente>
		<id_ente></id_ente>
		<denominazione_ente></denominazione_ente>
		<descrizione_tipo_prestazione></descrizione_tipo_prestazione>
		<costo></costo>
	</costi_enti_ricerca>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="strumentiAttrezzature" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_strumenti_attrezzature!=''">
	<strumenti_attrezzature>
		<descrizione_strumenti></descrizione_strumenti>
		<costo_strumento></costo_strumento>
	</strumenti_attrezzature>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="datiTecnici" > <!-- sezione FACOLTATIVA -->
	<xsl:if test="_dati_tecnici!=''">
	<dati_tecnici> 
		<codice_dettaglio_intervento></codice_dettaglio_intervento>
		<codice_identificativo_dato_tecnico></codice_identificativo_dato_tecnico>
		<descrizione></descrizione>
		<nome_oggetto_visuale></nome_oggetto_visuale>
		<unita_di_misura></unita_di_misura>
		<valore_1></valore_1>
		<valore_2></valore_2>
	</dati_tecnici>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="dichiarazioneImpegnativa" ><!-- sezione FACOLTATIVA -->
	<xsl:if test="_dichiarazioni!=''">
	<dichiarazione_impegnativa> 
		<importoAiuti></importoAiuti>
		<dataRimborso></dataRimborso>
		<mezzoRimborso></mezzoRimborso>
		<sommaRimborso></sommaRimborso>
		<letteraRimborso></letteraRimborso>
		<importoContabilitaSpeciale></importoContabilitaSpeciale>
		<letteraContabilitaSpeciale></letteraContabilitaSpeciale>
		<altre_agevolazioni_richieste>
			<xsl:for-each select="_dichiarazioni/map/agevolazioniList/list/map">
				<xsl:value-of select="count(preceding-sibling::map)" /> - <xsl:value-of select="agevolazione/map/descrizione"/>
				<xsl:text>&#10;</xsl:text> <!-- newline character -->
				<!-- <xsl:text>&#13;</xsl:text>  carriage return character -->
			</xsl:for-each>
		</altre_agevolazioni_richieste>
		<autorizzazioni_acquisite>
		<xsl:choose>
		<xsl:when test="_dichiarazioni/map/provvedimentiAutorizzatori='0'">1</xsl:when>
		<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
		</autorizzazioni_acquisite>
		<autorizzazioni_richieste>
		<xsl:choose>
		<xsl:when test="_dichiarazioni/map/provvedimentiAutorizzatori='1'">1</xsl:when>
		<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
		</autorizzazioni_richieste>
		<autorizzazioni_non_previste>
		<xsl:choose>
		<xsl:when test="_dichiarazioni/map/provvedimentiAutorizzatori='2'">1</xsl:when>
		<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
		</autorizzazioni_non_previste>
		<finanziamento_richiesto></finanziamento_richiesto>
		<finanziamento_ricevuto></finanziamento_ricevuto>
		<data_autorizzazione_richiesta></data_autorizzazione_richiesta>
		<nessun_aiuto></nessun_aiuto>
	</dichiarazione_impegnativa>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="indiceDocumentiAllegati" >  <!-- sezione FACOLTATIVA -->
	<xsl:for-each select="_documentazione/map/documentoList/list/map">
	<indice_documenti_allegati>
		<numero_progressivo><xsl:value-of select="idallegato"/></numero_progressivo>
		<nome_documento><xsl:value-of select="descrizione"/></nome_documento>
		<descrizione_documento></descrizione_documento>
	</indice_documenti_allegati>
	</xsl:for-each> 
	</xsl:template>
	
	<xsl:template name="costiFunzioniAnno" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_funzioni_anno!=''">
	<costi_funzioni_anno>
		<anno></anno>
		<funzione></funzione>
		<costo></costo>
		<descrizione_anno></descrizione_anno>
		<descrizione_funzione></descrizione_funzione>
	</costi_funzioni_anno>
	</xsl:if> 
	</xsl:template>
	
	<xsl:template name="costiBrevetti" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_brevetti!=''">
	<costi_brevetti>
		<codice_fiscale></codice_fiscale>
		<id_brevetto></id_brevetto>
		<denominazione></denominazione>
		<descrizione_spesa></descrizione_spesa>
		<costo></costo>
	</costi_brevetti>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="costiRichiesti" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_costi_richiesti!=''">
	<costi_richiesti>
		<cod_anno></cod_anno>
		<cod_costo></cod_costo>
		<specificazioni></specificazioni>
		<descrizione_anno></descrizione_anno>
		<descrizione_costo></descrizione_costo>
		<totale_voce_costo></totale_voce_costo>
	</costi_richiesti>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="contributiRichiesti" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_contributi_richiesti!=''">
	<contributi_richiesti>
		<cod_contributo></cod_contributo>
		<descr_contributo></descr_contributo>
		<importo_contributo_anno1></importo_contributo_anno1>
		<importo_contributo_anno2></importo_contributo_anno2>
		<importo_contributo_anno3></importo_contributo_anno3>
	</contributi_richiesti>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="progettoRaggruppamento" >  <!-- sezione FACOLTATIVA -->
	<xsl:if test="_abstractProgetto!=''">
	<progetto_raggruppamento> 
		<acronimo_progetto><xsl:value-of select="_abstractProgetto/map/acronimo" /></acronimo_progetto>
		<nome_completo_progetto></nome_completo_progetto>
		<coordinatore></coordinatore>
		<organizzazione></organizzazione>
		<raggruppamento_ats></raggruppamento_ats>
		<atto_costitutivo_ats></atto_costitutivo_ats>
		<data_atto_costitutivo_ats></data_atto_costitutivo_ats>
		<indirizzo_coordinatore></indirizzo_coordinatore>
		<telefono_coordinatore></telefono_coordinatore>
		<fax_coordinatore></fax_coordinatore>
		<email_coordinatore></email_coordinatore>
		<costo_totale_progetto></costo_totale_progetto>
		<durata_progetto></durata_progetto>
		<richiesta_contributo></richiesta_contributo>
		<totale_persone_mese></totale_persone_mese>
		<area_scientifica_tecnologica>NA</area_scientifica_tecnologica>
		<tematica>NA</tematica>
		<traiettoria></traiettoria>
		<acronimo_traiettoria></acronimo_traiettoria>
		<linea_di_sviluppo></linea_di_sviluppo>
		<acronimo_linea_di_sviluppo></acronimo_linea_di_sviluppo>
		<polo_appartenenza><xsl:value-of select="_abstractProgetto/map/poloAppartenenzaDescrizione" /></polo_appartenenza>
		<interpolo><xsl:value-of select="_abstractProgetto/map/poloContributore" /></interpolo>
		<flag_interpolo><xsl:value-of select="_abstractProgetto/map/interpolo" /></flag_interpolo>
	</progetto_raggruppamento>
	</xsl:if>
	</xsl:template>
	
	
	
	<!-- _______________________________________________ -->
	<!--  CAMPI NOSTRI, non gestiti da finpiemonte -->
	<!-- _______________________________________________ -->
	
	<xsl:template name="premialita" >
	<xsl:for-each select="_premialitaProgetto/map/premialitaList/list/map">
	<premialita>
		<descrizione_premialita><xsl:value-of select="descrPremialita"/></descrizione_premialita>
		<tipo_dato_premialita><xsl:value-of select="tipoDatoRichiesto"/></tipo_dato_premialita>
		<valore_premialita><xsl:value-of select="valorePremialita"/></valore_premialita>
		<premialita_selezionata><xsl:value-of select="checked"/></premialita_selezionata>		
	</premialita>
	</xsl:for-each>
	</xsl:template>

	<xsl:template name="datiBilancioPrecedente" >
	<xsl:if test="_bilancio!=''">
	<dati_bilancio>
		<anno><xsl:value-of select="_bilancio/map/annoPrec"/></anno>
		<speseRS><xsl:value-of select="_bilancio/map/speseRSPrec"/></speseRS>
		<creditiVsClienti><xsl:value-of select="_bilancio/map/totCreditiVsClientiPrec"/></creditiVsClienti>
		<creditiCommScad><xsl:value-of select="_bilancio/map/creditiCommScadPrec"/></creditiCommScad>		
		<disponibilitaLiquide><xsl:value-of select="_bilancio/map/disponibilitaLiquidePrec"/></disponibilitaLiquide>
		<totaleBilancio><xsl:value-of select="_bilancio/map/totaleBilancioPrec"/></totaleBilancio>
		<totalePatrimonio><xsl:value-of select="_bilancio/map/totalePatrimonioPrec"/></totalePatrimonio>
		<debitiSoci><xsl:value-of select="_bilancio/map/debitiSociPrec"/></debitiSoci>
		<debitiBanche><xsl:value-of select="_bilancio/map/debitiBanchePrec"/></debitiBanche>
		<debitiVsFornitori><xsl:value-of select="_bilancio/map/debitiVsFornitoriPrec"/></debitiVsFornitori>		
		<debitiFornScad><xsl:value-of select="_bilancio/map/debitiFornScadPrec"/></debitiFornScad>		
		<debitiImpreseCollegate><xsl:value-of select="_bilancio/map/debitiImpreseCollegatePrec"/></debitiImpreseCollegate>
		<debitiControllanti><xsl:value-of select="_bilancio/map/debitiControllantiPrec"/></debitiControllanti>
		<debitiTributari><xsl:value-of select="_bilancio/map/debitiTributariPrec"/></debitiTributari>
		<debitiTributariScad><xsl:value-of select="_bilancio/map/debitiTributariScadPrec"/></debitiTributariScad>
		<ricavi><xsl:value-of select="_bilancio/map/ricaviPrec"/></ricavi>
		<totaleValoreProduzione><xsl:value-of select="_bilancio/map/totaleValoreProduzionePrec"/></totaleValoreProduzione>
		<variazioneLavoriInCorso><xsl:value-of select="_bilancio/map/variazioneLavoriInCorsoPrec"/></variazioneLavoriInCorso>
		<ammortamentiImm><xsl:value-of select="_bilancio/map/ammortamentiImmPrec"/></ammortamentiImm>
  		<ammortamentiMat><xsl:value-of select="_bilancio/map/ammortamentiMatPrec"/></ammortamentiMat>  		
  		<totaleCostiProduzione><xsl:value-of select="_bilancio/map/totaleCostiProduzionePrec"/></totaleCostiProduzione>  		
  		<proventiFinanziari><xsl:value-of select="_bilancio/map/proventiFinanziariPrec"/></proventiFinanziari>  		
  		<interessiPassivi><xsl:value-of select="_bilancio/map/interessiPassiviPrec"/></interessiPassivi>
  		<proventiGestioneAccessoria><xsl:value-of select="_bilancio/map/proventiGestioneAccessoriaPrec"/></proventiGestioneAccessoria>
  		<oneriGestioneAccessoria><xsl:value-of select="_bilancio/map/oneriGestioneAccessoriaPrec"/></oneriGestioneAccessoria>		 		
		<ebitda><xsl:value-of select="_bilancio/map/ebitdaPrec"/></ebitda>
		<ebit><xsl:value-of select="_bilancio/map/ebitPrec"/></ebit>		
  		<indiceRotazione><xsl:value-of select="_bilancio/map/indiceRotazionePrec"/></indiceRotazione>
		<dso><xsl:value-of select="_bilancio/map/dsoPrec"/></dso>
		<dpo><xsl:value-of select="_bilancio/map/dpoPrec"/></dpo>
		<ula><xsl:value-of select="_bilancio/map/ulaPrec"/></ula>		
	</dati_bilancio>
	</xsl:if>
	</xsl:template>

	<xsl:template name="datiBilancio">
	<xsl:if test="_bilancio!=''">
	<dati_bilancio>
		<anno><xsl:value-of select="_bilancio/map/anno"/></anno>
		<speseRS><xsl:value-of select="_bilancio/map/speseRS"/></speseRS>
		<creditiVsClienti><xsl:value-of select="_bilancio/map/totCreditiVsClienti"/></creditiVsClienti>
		<creditiCommScad><xsl:value-of select="_bilancio/map/creditiCommScad"/></creditiCommScad>
		<disponibilitaLiquide><xsl:value-of select="_bilancio/map/disponibilitaLiquide"/></disponibilitaLiquide>
		<totaleBilancio><xsl:value-of select="_bilancio/map/totaleBilancio"/></totaleBilancio>
		<totalePatrimonio><xsl:value-of select="_bilancio/map/totalePatrimonio"/></totalePatrimonio>
		<debitiSoci><xsl:value-of select="_bilancio/map/debitiSoci"/></debitiSoci>
		<debitiBanche><xsl:value-of select="_bilancio/map/debitiBanche"/></debitiBanche>	
		<debitiVsFornitori><xsl:value-of select="_bilancio/map/debitiVsFornitori"/></debitiVsFornitori>	
		<debitiFornScad><xsl:value-of select="_bilancio/map/debitiFornScad"/></debitiFornScad>		
		<debitiImpreseCollegate><xsl:value-of select="_bilancio/map/debitiImpreseCollegate"/></debitiImpreseCollegate>
		<debitiControllanti><xsl:value-of select="_bilancio/map/debitiControllanti"/></debitiControllanti>
		<debitiTributari><xsl:value-of select="_bilancio/map/debitiTributari"/></debitiTributari>
		<debitiTributariScad><xsl:value-of select="_bilancio/map/debitiTributariScad"/></debitiTributariScad>
		<ricavi><xsl:value-of select="_bilancio/map/ricavi"/></ricavi>
		<totaleValoreProduzione><xsl:value-of select="_bilancio/map/totaleValoreProduzione"/></totaleValoreProduzione>
		<variazioneLavoriInCorso><xsl:value-of select="_bilancio/map/variazioneLavoriInCorso"/></variazioneLavoriInCorso>
		<ammortamentiImm><xsl:value-of select="_bilancio/map/ammortamentiImm"/></ammortamentiImm>
  		<ammortamentiMat><xsl:value-of select="_bilancio/map/ammortamentiMat"/></ammortamentiMat>  		
  		<totaleCostiProduzione><xsl:value-of select="_bilancio/map/totaleCostiProduzione"/></totaleCostiProduzione>  		
  		<proventiFinanziari><xsl:value-of select="_bilancio/map/proventiFinanziari"/></proventiFinanziari>  		
  		<interessiPassivi><xsl:value-of select="_bilancio/map/interessiPassivi"/></interessiPassivi>
  		<proventiGestioneAccessoria><xsl:value-of select="_bilancio/map/proventiGestioneAccessoria"/></proventiGestioneAccessoria>
  		<oneriGestioneAccessoria><xsl:value-of select="_bilancio/map/oneriGestioneAccessoria"/></oneriGestioneAccessoria>		 		
		<ebitda><xsl:value-of select="_bilancio/map/ebitda"/></ebitda>
		<ebit><xsl:value-of select="_bilancio/map/ebit"/></ebit>		
  		<indiceRotazione><xsl:value-of select="_bilancio/map/indiceRotazione"/></indiceRotazione>
		<dso><xsl:value-of select="_bilancio/map/dso"/></dso>
		<dpo><xsl:value-of select="_bilancio/map/dpo"/></dpo>
		<ula><xsl:value-of select="_bilancio/map/ula"/></ula>		
	</dati_bilancio>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="datiBilancioPrecedenteAAEP" >
	<xsl:if test="_bilancioAAEP!=''">
	<dati_bilancioAAEP>
		<anno><xsl:value-of select="_bilancioAAEP/map/annoPrec"/></anno>
		<speseRS><xsl:value-of select="_bilancioAAEP/map/speseRSPrec"/></speseRS>
		<creditiVsClienti><xsl:value-of select="_bilancioAAEP/map/totCreditiVsClientiPrec"/></creditiVsClienti>
		<creditiCommScad><xsl:value-of select="_bilancioAAEP/map/creditiCommScadPrec"/></creditiCommScad>
		<disponibilitaLiquide><xsl:value-of select="_bilancioAAEP/map/disponibilitaLiquidePrec"/></disponibilitaLiquide>
		<totaleBilancio><xsl:value-of select="_bilancioAAEP/map/totaleBilancioPrec"/></totaleBilancio>
		<totalePatrimonio><xsl:value-of select="_bilancioAAEP/map/totalePatrimonioPrec"/></totalePatrimonio>
		<debitiSoci><xsl:value-of select="_bilancioAAEP/map/debitiSociPrec"/></debitiSoci>
		<debitiBanche><xsl:value-of select="_bilancioAAEP/map/debitiBanchePrec"/></debitiBanche>
		<debitiVsFornitori><xsl:value-of select="_bilancioAAEP/map/debitiVsFornitoriPrec"/></debitiVsFornitori>		
		<debitiFornScad><xsl:value-of select="_bilancioAAEP/map/debitiFornScadPrec"/></debitiFornScad>		
		<debitiImpreseCollegate><xsl:value-of select="_bilancioAAEP/map/debitiImpreseCollegatePrec"/></debitiImpreseCollegate>
		<debitiControllanti><xsl:value-of select="_bilancioAAEP/map/debitiControllantiPrec"/></debitiControllanti>
		<debitiTributari><xsl:value-of select="_bilancioAAEP/map/debitiTributariPrec"/></debitiTributari>
		<debitiTributariScad><xsl:value-of select="_bilancioAAEP/map/debitiTributariScadPrec"/></debitiTributariScad>
		<ricavi><xsl:value-of select="_bilancioAAEP/map/ricaviPrec"/></ricavi>
		<totaleValoreProduzione><xsl:value-of select="_bilancioAAEP/map/totaleValoreProduzionePrec"/></totaleValoreProduzione>
		<variazioneLavoriInCorso><xsl:value-of select="_bilancioAAEP/map/variazioneLavoriInCorsoPrec"/></variazioneLavoriInCorso>
		<ammortamentiImm><xsl:value-of select="_bilancioAAEP/map/ammortamentiImmPrec"/></ammortamentiImm>
  		<ammortamentiMat><xsl:value-of select="_bilancioAAEP/map/ammortamentiMatPrec"/></ammortamentiMat>  		
  		<totaleCostiProduzione><xsl:value-of select="_bilancioAAEP/map/totaleCostiProduzionePrec"/></totaleCostiProduzione>  		
  		<proventiFinanziari><xsl:value-of select="_bilancioAAEP/map/proventiFinanziariPrec"/></proventiFinanziari>  		
  		<interessiPassivi><xsl:value-of select="_bilancioAAEP/map/interessiPassiviPrec"/></interessiPassivi>
  		<proventiGestioneAccessoria><xsl:value-of select="_bilancioAAEP/map/proventiGestioneAccessoriaPrec"/></proventiGestioneAccessoria>
  		<oneriGestioneAccessoria><xsl:value-of select="_bilancioAAEP/map/oneriGestioneAccessoriaPrec"/></oneriGestioneAccessoria>		 			
	</dati_bilancioAAEP>
	</xsl:if>
	</xsl:template>
	
	<xsl:template name="datiBilancioAAEP">
	<xsl:if test="_bilancioAAEP!=''">
	<dati_bilancioAAEP>
		<anno><xsl:value-of select="_bilancioAAEP/map/anno"/></anno>
		<speseRS><xsl:value-of select="_bilancioAAEP/map/speseRS"/></speseRS>
		<creditiVsClienti><xsl:value-of select="_bilancioAAEP/map/totCreditiVsClienti"/></creditiVsClienti>
		<creditiCommScad><xsl:value-of select="_bilancioAAEP/map/creditiCommScad"/></creditiCommScad>
		<disponibilitaLiquide><xsl:value-of select="_bilancioAAEP/map/disponibilitaLiquide"/></disponibilitaLiquide>
		<totaleBilancio><xsl:value-of select="_bilancioAAEP/map/totaleBilancio"/></totaleBilancio>
		<totalePatrimonio><xsl:value-of select="_bilancioAAEP/map/totalePatrimonio"/></totalePatrimonio>
		<debitiSoci><xsl:value-of select="_bilancioAAEP/map/debitiSoci"/></debitiSoci>
		<debitiBanche><xsl:value-of select="_bilancioAAEP/map/debitiBanche"/></debitiBanche>	
		<debitiVsFornitori><xsl:value-of select="_bilancioAAEP/map/debitiVsFornitori"/></debitiVsFornitori>		
		<debitiFornScad><xsl:value-of select="_bilancioAAEP/map/debitiFornScad"/></debitiFornScad>		
		<debitiImpreseCollegate><xsl:value-of select="_bilancioAAEP/map/debitiImpreseCollegate"/></debitiImpreseCollegate>
		<debitiControllanti><xsl:value-of select="_bilancioAAEP/map/debitiControllanti"/></debitiControllanti>
		<debitiTributari><xsl:value-of select="_bilancioAAEP/map/debitiTributari"/></debitiTributari>
		<debitiTributariScad><xsl:value-of select="_bilancioAAEP/map/debitiTributariScad"/></debitiTributariScad>
		<ricavi><xsl:value-of select="_bilancioAAEP/map/ricavi"/></ricavi>
		<totaleValoreProduzione><xsl:value-of select="_bilancioAAEP/map/totaleValoreProduzione"/></totaleValoreProduzione>
		<variazioneLavoriInCorso><xsl:value-of select="_bilancioAAEP/map/variazioneLavoriInCorso"/></variazioneLavoriInCorso>
		<ammortamentiImm><xsl:value-of select="_bilancioAAEP/map/ammortamentiImm"/></ammortamentiImm>
  		<ammortamentiMat><xsl:value-of select="_bilancioAAEP/map/ammortamentiMat"/></ammortamentiMat>  		
  		<totaleCostiProduzione><xsl:value-of select="_bilancioAAEP/map/totaleCostiProduzione"/></totaleCostiProduzione>  		
  		<proventiFinanziari><xsl:value-of select="_bilancioAAEP/map/proventiFinanziari"/></proventiFinanziari>  		
  		<interessiPassivi><xsl:value-of select="_bilancioAAEP/map/interessiPassivi"/></interessiPassivi>
  		<proventiGestioneAccessoria><xsl:value-of select="_bilancioAAEP/map/proventiGestioneAccessoria"/></proventiGestioneAccessoria>
  		<oneriGestioneAccessoria><xsl:value-of select="_bilancioAAEP/map/oneriGestioneAccessoria"/></oneriGestioneAccessoria>		 			
	</dati_bilancioAAEP>
	</xsl:if>
	</xsl:template>
		
	<xsl:template name="formeFinanziamento">
	<xsl:for-each select="_formaFinanziamento/map/formaFinanziamentoList/list/map">
	<xsl:if test="checked='true'">
	<forme_finanziamento>
		<cod_forma_finanziamento><xsl:value-of select="codFormaFinanziamento" /></cod_forma_finanziamento>
        <descr_forma_finanziamento><xsl:value-of select="descrFormaFinanziamento" /></descr_forma_finanziamento>
        <id_forma_finanziamento><xsl:value-of select="idFormaFinanziamento" /></id_forma_finanziamento>
        <importo_forma_finanziamento><xsl:value-of select="importoFormaAgevolazione" /></importo_forma_finanziamento>
	</forme_finanziamento>
	</xsl:if>
	</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="indicatori2020" > <!-- sezione FACOLTATIVA -->
	<xsl:for-each select="_indicatori/map/tipoIndicatoreList/list/map">
		<xsl:param name="descrTipoIndicatore" select="descrTipoIndicatore"/>
		<xsl:param name="idTipoIndicatore" select="idTipoIndicatore"/>
		<xsl:for-each select="indicatoriList/list/map">
		<xsl:if test="valoreIndicatore!=''">
		<indicatori_2020>
			<descr_tipo_indicatore><xsl:value-of select="$descrTipoIndicatore"/></descr_tipo_indicatore>
			<id_tipo_indicatore><xsl:value-of select="$idTipoIndicatore"/></id_tipo_indicatore>
				<codice_indicatore><xsl:value-of select="codIndicatore"/></codice_indicatore>
				<descr_indicatore><xsl:value-of select="descrIndicatore"/></descr_indicatore>
				<unita_misura><xsl:value-of select="unitaMisuraIndicatore"/></unita_misura>
				<valore_previsionale_inizio></valore_previsionale_inizio> <!--  -->
				<valore_previsionale_fine><xsl:value-of select="valoreIndicatore"/></valore_previsionale_fine>
		</indicatori_2020>
		</xsl:if>
		</xsl:for-each>
	</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="dettaglioSpese">  <!-- sezione FACOLTATIVA -->
	<xsl:for-each select="_pianoSpese/map/dettaglioCostiList/list/map">
		<dettaglio_spese>
			<identificativo><xsl:value-of select="identificativo"/></identificativo>
			<titolo><xsl:value-of select="titolo"/></titolo>
			<idVoceSpesa><xsl:value-of select="idVoceSpesa"/></idVoceSpesa>
			<descrVoceSpesa><xsl:value-of select="descrVoceSpesa"/></descrVoceSpesa>
			<descrizioneServizioBene><xsl:value-of select="descrizioneServizioBene"/></descrizioneServizioBene>
			<fornitore><xsl:value-of select="fornitore"/></fornitore>
			<codiceFiscale><xsl:value-of select="codiceFiscale"/></codiceFiscale>
			<importoProposto><xsl:value-of select="importoProposto"/></importoProposto>
		</dettaglio_spese>
	</xsl:for-each>
	</xsl:template>	

	<xsl:template name="altreSedi">  <!-- sezione FACOLTATIVA --> <!-- TODO : questo va chiamato per tutte le sedi MENO una che e' stata inserita in progetto_di_agevolazione -->
	<xsl:for-each select="_sedi/map/sediList/list/map">
		<altre_sedi>
			<idSede><xsl:value-of select="idSede"/></idSede>
			<idTipoSede><xsl:value-of select="idTipoSede"/></idTipoSede>
			<descrTipoSede><xsl:value-of select="descrTipoSede"/></descrTipoSede>
			<idProvinciaSede><xsl:value-of select="idProvinciaSede"/></idProvinciaSede>
			<descrProvinciaSede><xsl:value-of select="descrProvinciaSede"/></descrProvinciaSede>
			<siglaProvinciaSede><xsl:value-of select="siglaProvinciaSede"/></siglaProvinciaSede>
			<idComuneSede><xsl:value-of select="idComuneSede"/></idComuneSede>
			<descrComuneSede><xsl:value-of select="descrComuneSede"/></descrComuneSede>
			<indirizzoSede><xsl:value-of select="indirizzoSede"/></indirizzoSede>
			<numeroCivicoSede><xsl:value-of select="numeroCivicoSede"/></numeroCivicoSede>
			<capSede><xsl:value-of select="capSede"/></capSede>
			<indirizzoPecSede><xsl:value-of select="indirizzoPecSede"/></indirizzoPecSede>
			<telefonoSede><xsl:value-of select="telefonoSede"/></telefonoSede>
			<codAteco2007Sede><xsl:value-of select="codAteco2007Sede"/></codAteco2007Sede>
			<descrAteco2007Sede><xsl:value-of select="descrAteco2007Sede"/></descrAteco2007Sede>
			<coordX><xsl:value-of select="coordX"/></coordX>
			<coordY><xsl:value-of select="coordY"/></coordY>
		</altre_sedi>
	</xsl:for-each>
	</xsl:template>	

	<xsl:template name="sediAAEP">  <!-- sezione FACOLTATIVA --> 
	<xsl:for-each select="_sediAAEP/map/sediListAAEP/list/map">
		<altre_sediAAEP>
			<idSede><xsl:value-of select="idSede"/></idSede>	
			<idTipoSede><xsl:value-of select="idTipoSede"/></idTipoSede>
			<descrTipoSede><xsl:value-of select="descrTipoSede"/></descrTipoSede>
			<idProvinciaSede><xsl:value-of select="idProvinciaSede"/></idProvinciaSede>
			<descrProvinciaSede><xsl:value-of select="descrProvinciaSede"/></descrProvinciaSede>
			<siglaProvinciaSede><xsl:value-of select="siglaProvinciaSede"/></siglaProvinciaSede>
			<idComuneSede><xsl:value-of select="idComuneSede"/></idComuneSede>
			<descrComuneSede><xsl:value-of select="descrComuneSede"/></descrComuneSede>
			<indirizzoSede><xsl:value-of select="indirizzoSede"/></indirizzoSede>
			<numeroCivicoSede><xsl:value-of select="numeroCivicoSede"/></numeroCivicoSede>
			<capSede><xsl:value-of select="capSede"/></capSede>						
			<codAteco2007Sede><xsl:value-of select="codAteco2007Sede"/></codAteco2007Sede>
			<descrAteco2007Sede><xsl:value-of select="descrAteco2007Sede"/></descrAteco2007Sede>		
		</altre_sediAAEP>
	</xsl:for-each>
	</xsl:template>	
	
	<xsl:template name="progettiRS">
	<xsl:if test="_progettiRS!=''">	
		<xsl:choose>
			<xsl:when test="_progettiRS/map/flagNoProgettiInCorso='checked'">
			<progettiRS>
				<flag_noPprogetti_in_corso><xsl:value-of select="_progettiRS/map/flagNoProgettiInCorso"/></flag_noPprogetti_in_corso>	
			</progettiRS>
			</xsl:when>
			<xsl:otherwise>			
			<xsl:for-each select="_progettiRS/map/progettiRSList/list/map">
			<progettiRS>
		 		<acronimo><xsl:value-of select="acronimo"/></acronimo>
		 		<ente_concedente><xsl:value-of select="enteConcedente"/></ente_concedente>
            	<attivita_RS><xsl:value-of select="attivitaRS"/></attivita_RS>
            	<tipo_aiuto><xsl:value-of select="tipoAiuto"/></tipo_aiuto>
            	<descrizione_tipo_aiuto><xsl:value-of select="descrTipoAiuto"/></descrizione_tipo_aiuto>
            	<costo_progetto><xsl:value-of select="costoProgetto"/></costo_progetto>
            	<costi_residui><xsl:value-of select="costiResidui"/></costi_residui>
            	<importo_aiuti_residui><xsl:value-of select="importoAiutiResidui"/></importo_aiuti_residui>                        
			</progettiRS>		
			</xsl:for-each>				
			</xsl:otherwise>
		</xsl:choose>		
	</xsl:if>
	</xsl:template>	
	<xsl:template name="riepilogoCostiResiduiContributiRichiesti">	
	<xsl:if test="_progettiRS!=''">	
	<riepilogo_costi_residui_contributi_richiesti>
		<somma_contributi_richiesti><xsl:value-of select="_progettiRS/map/sommaContribRichiestiProgetti"/></somma_contributi_richiesti>
		<somma_contributi_progetti><xsl:value-of select="_progettiRS/map/sommaImportoAiuti"/></somma_contributi_progetti>		
    	<somma_costi_progetti><xsl:value-of select="_progettiRS/map/sommaCostiProgetti"/></somma_costi_progetti>
    	<somma_costi_residui><xsl:value-of select="_progettiRS/map/sommaCostiResidui"/></somma_costi_residui>      
    	<totale_contributi_richiesti><xsl:value-of select="_progettiRS/map/totaleContributiRichiesti"/></totale_contributi_richiesti>
    	<totale_costi_residui><xsl:value-of select="_progettiRS/map/totaleCostiResidui"/></totale_costi_residui>
    </riepilogo_costi_residui_contributi_richiesti>
    </xsl:if>
	</xsl:template>	
	  
	<xsl:template name="capacitaFinanziaria">	
	<xsl:if test="_capacitaFinanziaria!=''">	
	<capacita_finanziaria>
		<xsl:if test="_capacitaFinanziaria/map/raggruppamentoComuni1='checked'">
			<progetto_inserito>Il progetto e' inserito nel piano triennale delle opere pubbliche (art. 21 del D.Lgs. 50/2016), o nei piani dei singoli soggetti in caso di raggruppamento temporaneo di Comuni</progetto_inserito>
		</xsl:if>
		<xsl:if test="_capacitaFinanziaria/map/raggruppamentoComuni2='checked'">
			<progetto_approvato>Il progetto e' stato approvato con provvedimento dell'organo decisionale nel quale e' stato inserito l'impegno a ottenere il cofinanziamento del progetto, o con provvedimenti degli organi decisionali in caso di raggruppamento temporaneo di Comuni</progetto_approvato>
		</xsl:if>			
    	<adeguatezza><xsl:value-of select="_capacitaFinanziaria/map/adeguatezza"/></adeguatezza>
    	
    	<xsl:for-each select="_capacitaFinanziaria/map/estremiAttoList/list/map">
			<estremi_atto>
		 		<ente><xsl:value-of select="ente"/></ente>
		 		<tipologia_atto><xsl:value-of select="tipologiaAtto"/></tipologia_atto>
            	<numero_atto><xsl:value-of select="numeroAtto"/></numero_atto>
            	<data_atto><xsl:value-of select="dataAtto"/></data_atto>            	                       
			</estremi_atto>	
		</xsl:for-each>			
    	<merito_creditizio><xsl:value-of select="_capacitaFinanziaria/map/meritoCreditizio"/></merito_creditizio>          	
    </capacita_finanziaria>
    </xsl:if>
	</xsl:template>	

	
	<xsl:template name="strutturaOrganizzativa">	
	<xsl:if test="_strutturaOrganizzativa!=''">	
	<struttura_organizzativa>
    	<descrizione><xsl:value-of select="_strutturaOrganizzativa/map/descrizione"/></descrizione>    	
     </struttura_organizzativa>
    </xsl:if>
	</xsl:template> 
	
	<xsl:template name="autosostenibilita">	
	<xsl:if test="_autosostenibilita!=''">	
	<autosostenibilita>
    	<descrizione><xsl:value-of select="_autosostenibilita/map/descrizione"/></descrizione>    	
     </autosostenibilita>
    </xsl:if>
	</xsl:template> 
	
	<xsl:template name="unioneComuni">	
	<xsl:if test="_unioneComuni!=''">	
	<unione_comuni>
		<data_costituzione><xsl:value-of select="_unioneComuni/map/dataCostituzione"/></data_costituzione>
		<flag_carta_associativa><xsl:value-of select="_unioneComuni/map/flagCartaAssociativa"/></flag_carta_associativa>				
    	<numero_delibera><xsl:value-of select="_unioneComuni/map/numDelibera"/></numero_delibera>
    	<data_delibera><xsl:value-of select="_unioneComuni/map/dataDelibera"/></data_delibera>
    </unione_comuni>
    </xsl:if>
	</xsl:template>
	
	<xsl:template name="raggruppamentoEnti">	
	<xsl:if test="_raggruppamentoEnti!=''">	
	<raggruppamento_enti>
		<data_costituzione><xsl:value-of select="_raggruppamentoEnti/map/dataCostituzione"/></data_costituzione>
		<forma_raggruppamento><xsl:value-of select="_raggruppamentoEnti/map/formaRaggruppamentoDescrizione"/></forma_raggruppamento>				    	
    </raggruppamento_enti>
    </xsl:if>
	</xsl:template>
	
	<xsl:template name="entiProgetto">	
	<xsl:if test="_entiProgetto!=''">
	<xsl:for-each select="_entiProgetto/map/entiProgettoList/list/map">
		<enti_progetto>
			<codice_ente><xsl:value-of select="codiceEnte"/></codice_ente>
			<denominazione_ente><xsl:value-of select="descrizioneEnte"/></denominazione_ente>
			<tipo_ente><xsl:value-of select="tipoEnte"/></tipo_ente>
			<popolazione><xsl:value-of select="popolazione"/></popolazione>				    	
			<classificazione><xsl:value-of select="classificazione"/></classificazione>		
			<partecipazione_progetto><xsl:value-of select="partecipazioneProgetto"/></partecipazione_progetto>
			<xsl:if test="certificazioneAmbientale='SI'">
			<certificazione_ambientale><xsl:value-of select="certificazioneAmbientaleText"/></certificazione_ambientale>			
			</xsl:if>
			<xsl:if test="certificazioneAmbientale='NO'">
			<certificazione_ambientale><xsl:value-of select="certificazioneAmbientale"/></certificazione_ambientale>	
			</xsl:if>
			<patto_sindaci><xsl:value-of select="pattoSindaci"/></patto_sindaci>
			<xsl:if test="richiestaAgevolazioni='SI'">
			<richiesta_agevolazioni><xsl:value-of select="richiestaAgevolazioniText"/></richiesta_agevolazioni>			
			</xsl:if>
			<xsl:if test="richiestaAgevolazioni='NO'">
			<richiesta_agevolazioni><xsl:value-of select="richiestaAgevolazioni"/></richiesta_agevolazioni>	
			</xsl:if>		
    	</enti_progetto>
	</xsl:for-each>			
    </xsl:if>
	</xsl:template> 


<!-- test replace nome_file -->
	<xsl:template name="documentiDemat">
		<xsl:if test="_domanda!=''">
			    <documenti_demat>		   
	            <xsl:for-each select="_allegati/map/allegatiList/list/map/documento/map">           
	            	<id_documento><xsl:value-of select="idTipologia"/></id_documento>
				      	<nome_file>
							<xsl:variable name="apos">'</xsl:variable>
							<xsl:variable name="nomeFileSpace">
								<xsl:value-of select="translate(nomeFile, $apos, '')"/>
							</xsl:variable>
							<xsl:value-of select="translate($nomeFileSpace, ' ', '')"/>
				      	</nome_file>                       
	            </xsl:for-each>
	         </documenti_demat>
		</xsl:if>
  	</xsl:template>
</xsl:stylesheet>
