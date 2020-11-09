/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.formaFinContrNeve;

import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.CaratteristicheProgettoNeveNGDAO;
import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.CaratteristicheProgettoNeveNGVO;
import it.csi.findom.blocchetti.blocchetti.caratteristicheprogettoNeveNG.TipologiaInterventoNeveVO;
import it.csi.findom.blocchetti.common.dao.DomandaNGDAO;
import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.common.vo.pianospese.DettaglioVoceSpesaInterventoVO;
import it.csi.findom.blocchetti.common.vo.pianospese.PianoSpeseVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.commonality.Utils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class FormaFinContrNeve extends Commonality {

	FormaFinContrNeveInput input = new FormaFinContrNeveInput();
	FormaFinContrNeveOutput output = new FormaFinContrNeveOutput();

	boolean isDecimaliUguali = false;

	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo info1, List<CommonalityMessage> messages)
			throws CommonalityException {

		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}


	/** --------------------------------------------------------------------------------------- inject */
	@Override
	public CommonalityOutput inject(CommonInfo info1, List<CommonalityMessage> messages) throws CommonalityException 
	{
		FinCommonInfo info = (FinCommonInfo) info1;

		Logger logger = Logger.getLogger(info.getLoggerName());
		String str = "[FormaFinContrNeve::inject] ";

		logger.info(str + " _cultFormaAgevolazione BEGIN");
		//		FormaFinContrNeveOutput output = new FormaFinContrNeveOutput();


		/** ------------------------------------------------ Definizione variabili - inizio - ---------- */
		final String MSG_ERRORE_COMPILAZIONE_SPESE = "ATTENZIONE: piano spese non risulta compilato!";
		final String NON_PREVISTO = "Non previsto";

		String isGrandiStazioni = "false";

		boolean isDichiaratoImportoMaxErogabile_97 = false;
		boolean isDichiaratoImportoMaxErogabile_98 = false;
		boolean isDichiaratoImportoMaxErogabile_99 = false;

		boolean isSpeseDichiarate_97 = false;
		boolean isSpeseDichiarate_98 = false;
		boolean isSpeseDichiarate_99 = false;

		int flagPubblicoPrivato = DomandaNGDAO.getFlagPubblicoPrivato(info.getStatusInfo().getNumProposta(), logger);
		logger.info(str + " flagPubblicoPrivato vale= " + flagPubblicoPrivato);

		int numTipologie 	= 0;
		int result			= 0;

		/************************************** 
		 * Jira: 1410 | MSTAZ1 | GSTAZ | MSTAZ2 
		 **************************************/
		String isSelectedIdTipoIntervento97= "false";
		String isSelectedIdTipoIntervento98= "false";
		String isSelectedIdTipoIntervento99 = "false";

		String codeTipoBeneficiario = "";
		codeTipoBeneficiario = DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) != null ? DomandaNGDAO.getCodiceTipoBeneficiario(info.getStatusInfo().getNumProposta(), logger) : "";
		logger.info(str + " codeTipoBeneficiario vale= " + codeTipoBeneficiario); 

		String esistonoDettagli 	= "true";
		String viewWarningSpese 	= "false";
		String speseNonCompilate 	= "false";

		String totaleSpeseContrCatg 				= "0,00";
		String totCat99  							= "0,00";
		String strTotCat97 							= "0,00";
		String strTotCat98 							= "0,00";
		String strImportoCat97 						= "0,00";
		String strImportoCat98 						= "0,00";
		String strImportoCat99 						= "0,00";
		String strTotImportoRichiesto 				= "0,00"; // Totale importo contributo
		
		String tot_VoceSpesaParzialeCatA_97_67 		= "0.00";
		String tot_VoceSpesaParzialeCatA_97_68 		= "0.00";
		String tot_VoceSpesaParzialeCatA_98_69 		= "0.00";
		String tot_VoceSpesaParzialeCatA_98_70		= "0.00";
		String tot_VoceSpesaCatC_99 				= "0.00";
		String str_spesaMaxDichiarabile_99 			= "0.00";

		String str_spesaMaxDichiarabile_97 			= "0,00";
		String str_spesaMaxDichiarabile_98 			= "0,00";

		String str_IdTipoIntervento					= null;
		String str_IdDettIntervento 				= null;
		String str_IdVoceSpesa 						= null;
		String str_TipoRecord 						= null;

		// ------------------------------------------------------------------------ Percentuale max erogabile
		String str_percMaxErogabile_CatA_97 = "0.00";
		String str_percMaxErogabile_CatA_98 = "0.00";
		String str_percMaxErogabile_CatC_99 = "0.00";

		// ------------------------------------------------------------- Importo max erogabile formato stringa
		String str_importoMaxErogabile_CatA_97 = "0.00";
		String str_importoMaxErogabile_CatA_98 = "0.00";
		String str_importoMaxErogabile_CatA_99 = "0.00";


		Integer idSportello = info.getStatusInfo().getNumSportello();
		logger.info(str + " idSportello vale= " + idSportello);

		BigDecimal bd_totaleSpeseContrCatg 	= new BigDecimal(0.00);

		BigDecimal bd_parziale_CatA_97_67 	= new BigDecimal(0.00);
		BigDecimal bd_parziale_CatA_97_68 	= new BigDecimal(0.00);
		BigDecimal bd_totale_CatA_97 		= new BigDecimal(0.00);

		BigDecimal bd_parziale_CatA_98_69 	= new BigDecimal(0.00);
		BigDecimal bd_parziale_CatA_98_70 	= new BigDecimal(0.00);
		BigDecimal bd_totale_CatA_98 		= new BigDecimal(0.00);

		BigDecimal bd_totale_CatC_99 		= new BigDecimal(0.00);

		BigDecimal tmpTotaleBdecimal = new BigDecimal(0.00);

		/** Importo richiesto */
		BigDecimal bd_importoRichiesto_97 		= new BigDecimal(0.00);
		BigDecimal bd_importoRichiesto_98 		= new BigDecimal(0.00);
		BigDecimal bd_importoRichiesto_99 		= new BigDecimal(0.00);
		BigDecimal bd_importoTotaleRichiesto 	= new BigDecimal(0.00); // bd totale importo richiesto

		/** Totale spese dichiarate */
		BigDecimal bd_totaleSpeseDichiarate_97 	= new BigDecimal(0.00);
		BigDecimal bd_totaleSpeseDichiarate_98 	= new BigDecimal(0.00);
		BigDecimal bd_totaleSpeseDichiarate_99 	= new BigDecimal(0.00);

		BigDecimal bd_totaleSpeseDichiarate 	= new BigDecimal(0.00);

		/** Importo max erogabile */
		BigDecimal bd_importoMaxErogabile_97 	= new BigDecimal(0.00);
		BigDecimal bd_importoMaxErogabile_98 	= new BigDecimal(0.00);
		BigDecimal bd_importoMaxErogabile_99 	= new BigDecimal(0.00);

		/** Spesa max dichiarabile */
		BigDecimal bd_spesaMaxDichiarabile_97 = new BigDecimal(0.00);
		BigDecimal bd_spesaMaxDichiarabile_98 = new BigDecimal(0.00);
		BigDecimal bd_spesaMaxDichiarabile_99 = new BigDecimal(0.00);
		
		/** Percentuali per tipo di intervento */
		BigDecimal bd_percMaxErogabile_CatA_97 = new BigDecimal(0.00);
		BigDecimal bd_percMaxErogabile_CatA_98 = new BigDecimal(0.00);
		BigDecimal bd_percMaxErogabile_CatC_99 = new BigDecimal(0.00);

		List<TipologiaInterventoNeveVO> tipologiaInterventoList = new ArrayList<>();
		List<ValoriSportelloVO> valoriContributoList 			= new ArrayList<ValoriSportelloVO>();
		List<String> totaliContributoList 						= new ArrayList<String>();

		// inject
		FormaFinContrNeveVO cultFormaAgevolazioneMap = input._formaAgevolazione; 	// legge da xml


		/** ----------------------------------------------- Definizione variabili - fine - ---------- */

		// valorizzazione
		if (info.getCurrentPage() != null) 	{

			//	SessionCache.getInstance().set("vociDiSpesaConsList", null); 
			// lista che viene messa in sessione in progetto-spese				

			String idDomanda = info.getStatusInfo().getNumProposta() + "";
			logger.info(str + " idDomanda vale = " + idDomanda);

			String dataInvio = "";
			if (info.getStatusInfo().getDataTrasmissione() != null) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				dataInvio = df.format(info.getStatusInfo().getDataTrasmissione());
			}

			if( flagPubblicoPrivato == 1 && codeTipoBeneficiario.equalsIgnoreCase("GSTAZ"))
			{
				/** Jira: 1410: 
				 * -- verifico se beneficiario risulta 'Grandi stazioni' 
				 * e filtro anche per dettagli - inizio */

				tipologiaInterventoList = CaratteristicheProgettoNeveNGDAO.getTipologiaInterventoList3(idDomanda, dataInvio, logger); // lista-3 presa da tabelle DB
				isGrandiStazioni= "true";
				logger.info(str + " tipologiaInterventoList vale = " + tipologiaInterventoList.size());
				logger.info(str + "ciclo sulla lista di interventi per Grandi stazioni: ");
			
			} else {
				/** Jira: 1410: -- verifico se beneficiario risulta 'micro stazioni' privato MSTAZ1 */

				tipologiaInterventoList = CaratteristicheProgettoNeveNGDAO.getTipologiaInterventoList(idDomanda, dataInvio, logger); // lista-1 presa da tabelle DB
				logger.info(str + " tipologiaInterventoList vale = " + tipologiaInterventoList.size());
			}

			CaratteristicheProgettoNeveNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
			
			
			if (_caratteristicheProgetto != null) 
			{
				// Leggo da xml
				List<TipologiaInterventoNeveVO> tipologiaInterventoSalvataList = 
						new ArrayList<TipologiaInterventoNeveVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList()));

				logger.info(str + " tipologiaInterventoSalvataList da xml ");

				if (tipologiaInterventoSalvataList != null) 
				{
					for (int i = 0; i < tipologiaInterventoList.size(); i++) 
					{
						TipologiaInterventoNeveVO curTipologia = tipologiaInterventoList.get(i);

						if (curTipologia != null) 
						{
							for (Object o : tipologiaInterventoSalvataList) 
							{
								TipologiaInterventoNeveVO curTipologiaSalvata = (TipologiaInterventoNeveVO) o;

								if (curTipologiaSalvata != null) 
								{
									if (curTipologia.getIdTipoIntervento().equals(curTipologiaSalvata.getIdTipoIntervento())) 
									{
										String checked = curTipologiaSalvata.getChecked();

										if (StringUtils.isBlank(checked)) {
											checked = "false";
										}

										if( curTipologiaSalvata.getIdTipoIntervento().equals("97") && checked.equals("true")) 
										{
											logger.info(str + "idTipoInterventoSelezionato: " +curTipologiaSalvata.getIdTipoIntervento()+ " risulta selezionato!");
											isSelectedIdTipoIntervento97= "true";
										}

										if( curTipologiaSalvata.getIdTipoIntervento().equals("98") && checked.equals("true")) 
										{
											logger.info(str + "idTipoInterventoSelezionato: " +curTipologiaSalvata.getIdTipoIntervento()+ " risulta selezionato!");
											isSelectedIdTipoIntervento98= "true";
										}

										if( curTipologiaSalvata.getIdTipoIntervento().equals("99") && checked.equals("true")) 
										{
											logger.info(str + "idTipoInterventoSelezionato: " +curTipologiaSalvata.getIdTipoIntervento()+ " risulta selezionato!");
											isSelectedIdTipoIntervento99= "true";
										}

										curTipologia.setChecked(checked);
										logger.info(str + "idTipoInterventoSelezionato: " +curTipologia.getIdTipoIntervento()+ " vale: " + checked);
										break;
									}
								}
							}
						}
					} // chiude for esterno
				}
			} // chiude test null _caratteristicheProgetto

			logger.info(str + "- idSportello: "	+ idSportello);
			logger.info(str + "- codeTipoBeneficiario: "	+ codeTipoBeneficiario);
			Integer idCodiceBeneficiario = CaratteristicheProgettoNeveNGDAO.getIdTipolBeneficiarioByIdSportello(idSportello, codeTipoBeneficiario, logger);
			logger.info(str + "- idCodiceBeneficiario: "	+ idCodiceBeneficiario);

			valoriContributoList = CaratteristicheProgettoNeveNGDAO.getContributiInterventoListByIdSportello(idSportello, idCodiceBeneficiario, logger);
			logger.info(str + "- valoriContributoList: "	+ valoriContributoList.size());

			// per ceccare di default la tipologia se ce ne fosse una sola
			numTipologie = tipologiaInterventoList.size();
			if (numTipologie == 1) {
				TipologiaInterventoNeveVO unicaTipologia = tipologiaInterventoList.get(0);
				if (unicaTipologia != null) {
					unicaTipologia.setChecked("true");
				}
			} // chiude test su numTipologie == 1

			viewWarningSpese = "false";

			PianoSpeseVO pianoSpeseMap 	= input._pianoSpese;

			if (pianoSpeseMap != null && !pianoSpeseMap.isEmpty()) 
			{
				DettaglioVoceSpesaInterventoVO[] dettaglioCostiList = pianoSpeseMap.getPianoSpeseList();
				totaleSpeseContrCatg= pianoSpeseMap.getTotale()+".00";
				logger.info(str + "totaleSpeseContrCatg risulta: " + totaleSpeseContrCatg); 

				bd_totaleSpeseContrCatg = new BigDecimal(totaleSpeseContrCatg);
				bd_totaleSpeseContrCatg = bd_totaleSpeseContrCatg.setScale(2, RoundingMode.CEILING);
				logger.info(str + "big-bd_totaleSpeseContrCatg risulta: " + bd_totaleSpeseContrCatg); 

				totaleSpeseContrCatg = bd_totaleSpeseContrCatg.toString().replace(".", ",");
				logger.info(str + "totaleSpeseContrCatg risulta: " + totaleSpeseContrCatg); // 82778.00 step:01

				if(tipContrAllineatoSpese(logger)){
					logger.info(str + "Spese e Tipologie Contributo disallineati ");
					viewWarningSpese = "true";
					speseNonCompilate="false";
				
				} else {
					logger.info(str + "Spese e Tipologie Contributo allineati ");
				}

				if( !totaleSpeseContrCatg.equals("0") && !totaleSpeseContrCatg.equals("0.00") && !totaleSpeseContrCatg.equals("0,00")) 
				{
					speseNonCompilate="false";

					if (dettaglioCostiList != null && dettaglioCostiList.length > 0) 
					{
						logger.info(str + "dettaglioCostiList.size="+ dettaglioCostiList.length);

						/** ----------------------------------------------------------------- inizio */
						for (int i = 0; i < tipologiaInterventoList.size(); i++) 
						{
							for (int j = 0; j < dettaglioCostiList.length; j++) 
							{
								if(StringUtils.equals(dettaglioCostiList[j].getVisibilita(),"selezionato")){ 

									str_IdTipoIntervento = dettaglioCostiList[j].getIdTipoIntervento();
									logger.info(str + "IdTipoIntervento vale: " + str_IdTipoIntervento); // IdTipoIntervento vale: 97

									str_IdDettIntervento = dettaglioCostiList[j].getIdDettIntervento();
									logger.info(str + "IdDettIntervento vale: " + str_IdDettIntervento); // IdDettIntervento vale: 67

									str_IdVoceSpesa = dettaglioCostiList[j].getIdVoceSpesa();
									logger.info(str + "IdVoceSpesa vale: " + str_IdVoceSpesa);

									str_TipoRecord = dettaglioCostiList[j].getTipoRecord();
									logger.info(str + "TipoRecord vale: "+ str_TipoRecord);


									// se intervento 97 
									if( str_IdTipoIntervento != null && str_IdTipoIntervento.equals("97") && str_TipoRecord.equals("2") && isSelectedIdTipoIntervento97.equals("true")){

										/** Intervento: 97, dettagli: 67 */
										if (str_IdTipoIntervento != null
												&& str_IdTipoIntervento.equals("97")
												&& str_IdDettIntervento != null
												&& str_IdDettIntervento.equals("67")
												&& str_TipoRecord != null
												&& str_TipoRecord.equals("2")
												&& str_IdVoceSpesa.isEmpty()) {
											tot_VoceSpesaParzialeCatA_97_67 = dettaglioCostiList[j].getTotaleVoceSpesa();

											if(tot_VoceSpesaParzialeCatA_97_67.isEmpty()){
												tot_VoceSpesaParzialeCatA_97_67 = "0";
											}
											logger.info(str + "totale voce spesa parziale CatA_97_67 vale: " + tot_VoceSpesaParzialeCatA_97_67);
										}

										/** Intervento: 97, dettagli: 68 */
										if (str_IdTipoIntervento != null
												&& str_IdTipoIntervento.equals("97")
												&& str_IdDettIntervento != null
												&& str_IdDettIntervento.equals("68")
												&& str_TipoRecord != null
												&& str_TipoRecord.equals("2")
												&& str_IdVoceSpesa.isEmpty()) {
											tot_VoceSpesaParzialeCatA_97_68 = dettaglioCostiList[j].getTotaleVoceSpesa();

											if(tot_VoceSpesaParzialeCatA_97_68.isEmpty()){
												tot_VoceSpesaParzialeCatA_97_68 = "0";
											}
											logger.info(str + "totale voce spesa parziale CatA_97_68 vale: " + tot_VoceSpesaParzialeCatA_97_68);
										}
									}


									// se intervento 98 
									if( str_IdTipoIntervento != null && str_IdTipoIntervento.equals("98") && str_TipoRecord.equals("2") && isSelectedIdTipoIntervento98.equals("true"))
									{
										/** Intervento: 98, dettagli: 69 */
										if (str_IdTipoIntervento != null
												&& str_IdTipoIntervento.equals("98")
												&& str_IdDettIntervento != null
												&& str_IdDettIntervento.equals("69")
												&& str_TipoRecord != null
												&& str_TipoRecord.equals("2")
												&& str_IdVoceSpesa.isEmpty()) {
											tot_VoceSpesaParzialeCatA_98_69 = dettaglioCostiList[j].getTotaleVoceSpesa();
											if(tot_VoceSpesaParzialeCatA_98_69.isEmpty()){
												tot_VoceSpesaParzialeCatA_98_69 = "0";
											}
											logger.info(str + "totale voce spesa parziale CatA_98_69 vale: " + tot_VoceSpesaParzialeCatA_98_69);
										}

										/** Intervento: 98, dettagli: 70 */
										if (str_IdTipoIntervento != null
												&& str_IdTipoIntervento.equals("98")
												&& str_IdDettIntervento != null
												&& str_IdDettIntervento.equals("70")
												&& str_TipoRecord != null
												&& str_TipoRecord.equals("2")
												&& str_IdVoceSpesa.isEmpty()) {
											tot_VoceSpesaParzialeCatA_98_70 = dettaglioCostiList[j].getTotaleVoceSpesa();
											if(tot_VoceSpesaParzialeCatA_98_70.isEmpty()){
												tot_VoceSpesaParzialeCatA_98_70 = "0";
											}
											logger.info(str + "totale voce spesa parziale CatA_98_70 vale: " + tot_VoceSpesaParzialeCatA_98_70);
										}
									}

									// se intervento 99
									if( str_IdTipoIntervento != null && str_IdTipoIntervento.equals("99") && str_TipoRecord.equals("1") && isSelectedIdTipoIntervento99.equals("true"))
									{
										if (str_TipoRecord.equals("1")) {

											/** Intervento: 99, no-dettagli */
											if (str_IdTipoIntervento != null
													&& str_IdTipoIntervento.equals("99")
													&& str_TipoRecord != null
													&& str_TipoRecord.equals("1")) {
												tot_VoceSpesaCatC_99 = dettaglioCostiList[j].getTotaleVoceSpesa();
												if(tot_VoceSpesaCatC_99.isEmpty()){
													tot_VoceSpesaCatC_99 = "0";
												}
												logger.info(str + "totale voce spesa parziale CatA_99 vale: " + tot_VoceSpesaCatC_99); // totale voce spesa parziale CatA_99 vale: 79046 step: 2
											}
										}
									}
								} // if selezionato
							} // -/ fine for interno
						} // 	-/ fine for esterno

						/** Eseguo somma voci spesa per categoria: - inizio - */

						if (isSelectedIdTipoIntervento97.equals("true")) 
						{
							logger.info(str + "Cat 97 risulta: " + tot_VoceSpesaParzialeCatA_97_67);
							bd_parziale_CatA_97_67 = new BigDecimal(tot_VoceSpesaParzialeCatA_97_67);
							logger.info(str + "big-decimal parziale_CatA_97_67 risulta: " + bd_parziale_CatA_97_67);

							bd_parziale_CatA_97_68 = new BigDecimal( tot_VoceSpesaParzialeCatA_97_68);
							logger.info(str + "big-decimal parziale_CatA_97_68 risulta: " + bd_parziale_CatA_97_68);

							bd_totale_CatA_97 = bd_parziale_CatA_97_67.add(bd_parziale_CatA_97_68);
							bd_totale_CatA_97 = bd_totale_CatA_97.setScale(2, RoundingMode.CEILING);
							logger.info(str + "big-decimal bd_totale_CatA_97 risulta: " + bd_totale_CatA_97);

						} else {
							bd_totale_CatA_97 = new BigDecimal(0.00);
							logger.info(str + "big-decimal bd_totale_CatA_97 risulta: " + bd_totale_CatA_97);
						}
						logger.info(str + "************************************************************************ Cat 98  ");



						if (isSelectedIdTipoIntervento98.equals("true")) 
						{
							bd_parziale_CatA_98_69 = new BigDecimal(tot_VoceSpesaParzialeCatA_98_69);
							logger.info(str + "big-decimal bd_parziale_CatA_98_69 risulta: " + bd_parziale_CatA_98_69);

							bd_parziale_CatA_98_70 = new BigDecimal(tot_VoceSpesaParzialeCatA_98_70);
							logger.info(str + "big-decimal bd_parziale_CatA_98_70 risulta: " + bd_parziale_CatA_98_70);

							bd_totale_CatA_98 = bd_parziale_CatA_98_69.add(bd_parziale_CatA_98_70);
							bd_totale_CatA_98 = bd_totale_CatA_98.setScale(2, RoundingMode.CEILING);
							logger.info(str + "big-decimal bd_totale_CatA_98 risulta: " + bd_totale_CatA_98);

						} else {
							bd_totale_CatA_98= new BigDecimal(0.00);
							logger.info("big-decimal bd_totale_CatA_98 risulta: " + bd_totale_CatA_98);
						}
						logger.info(str + "************************************************************************ Cat 99  ");

						if (isSelectedIdTipoIntervento99.equals("true")) 
						{
							bd_totale_CatC_99 = new BigDecimal(tot_VoceSpesaCatC_99);
							bd_totale_CatC_99 = bd_totale_CatC_99.setScale(2, RoundingMode.CEILING);
							logger.info(str + "big-decimal bd_totale_CatC_99 risulta: " + bd_totale_CatC_99);

						} else {
							bd_totale_CatC_99= new BigDecimal(0.00);
							logger.info(str + "big-decimal bd_totale_CatC_99 risulta: " + bd_totale_CatC_99);
						}
						logger.info(str + "************************************************************************ Totale categorie");


						strTotCat97 = bd_totale_CatA_97.toString().replace(".", ",");
						logger.info(str + "big-strTotCat97 risulta: " + strTotCat97);

						strTotCat98 = bd_totale_CatA_98.toString().replace(".", ",");
						logger.info(str + "big-strTotCat98 risulta: " + strTotCat98);

						totCat99 	= bd_totale_CatC_99.toString().replace(".", ",");
						logger.info(str + "totCat99 risulta: " + totCat99);

						tmpTotaleBdecimal = bd_totale_CatA_97.add(bd_totale_CatA_98).add(bd_totale_CatC_99);
						logger.info(str + "tmpTotaleBdecimal risulta: " + tmpTotaleBdecimal); // tmpTotaleBdecimal risulta: 82778.00	


						/** ---------------------------------------------------------------- Comparazione di BigDecimal - inizio - */
						result = tmpTotaleBdecimal.compareTo(bd_totaleSpeseContrCatg);
						logger.info(str + "Result vale: " + result);

						if (result == 0) 
						{
							logger.info(str + tmpTotaleBdecimal + " uguale a " + bd_totaleSpeseContrCatg);
						}

						else if (result == 1) 
						{
							logger.info(str + tmpTotaleBdecimal + " > di " + bd_totaleSpeseContrCatg);
						} 

						else if (result == -1) 
						{

							// speseNonCompilate= "true";
							totaleSpeseContrCatg = tmpTotaleBdecimal.toString().replace(".", ",");
							logger.info(str + tmpTotaleBdecimal + " < di " + bd_totaleSpeseContrCatg);
						}
						/** ---------------------------------------------------- Comparazione di BigDecimal - fine - */

						bd_percMaxErogabile_CatA_97 = new BigDecimal(str_percMaxErogabile_CatA_97.toString());
						bd_percMaxErogabile_CatA_98 = new BigDecimal(str_percMaxErogabile_CatA_98.toString());
						bd_percMaxErogabile_CatC_99 = new BigDecimal(str_percMaxErogabile_CatC_99.toString());

						if (valoriContributoList.size() > 0) 
						{
							for (int i = 0; i < valoriContributoList.size(); i++) 
							{
								if( valoriContributoList.get(i).getIdTiploIntervento().equals("97"))
								{
									str_percMaxErogabile_CatA_97 = valoriContributoList.get(i).getPercMaxContributoErogabile();
									logger.info(str + "% max erogabile Categ. A0:97 " + str_percMaxErogabile_CatA_97 );

									str_importoMaxErogabile_CatA_97 = valoriContributoList.get(i).getImportoMassimoErogabile();

									if( str_importoMaxErogabile_CatA_97 == null){
										str_importoMaxErogabile_CatA_97 = NON_PREVISTO;
									}
									logger.info(str + "importo max erogabile Categ. A0:97 " + str_importoMaxErogabile_CatA_97 );
								}

								if( valoriContributoList.get(i).getIdTiploIntervento().equals("98"))
								{
									str_percMaxErogabile_CatA_98 = valoriContributoList.get(i).getPercMaxContributoErogabile();
									logger.info(str + "% max erogabile Categ. A1:98 " + str_percMaxErogabile_CatA_98 );

									str_importoMaxErogabile_CatA_98 = valoriContributoList.get(i).getImportoMassimoErogabile();

									if( str_importoMaxErogabile_CatA_98 == null){
										str_importoMaxErogabile_CatA_98 = NON_PREVISTO;
									}
									logger.info(str + "% importo max erogabile Categ. A1:98 " + str_importoMaxErogabile_CatA_98 );
								}

								if( valoriContributoList.get(i).getIdTiploIntervento().equals("99"))
								{
									str_percMaxErogabile_CatC_99 = valoriContributoList.get(i).getPercMaxContributoErogabile();
									logger.info(str + "% max erogabile Categ. C:99 " + str_percMaxErogabile_CatC_99 ); // 50.00

									str_importoMaxErogabile_CatA_99 = valoriContributoList.get(i).getImportoMassimoErogabile(); // 50000

									if( str_importoMaxErogabile_CatA_99 == null){
										str_importoMaxErogabile_CatA_99 = NON_PREVISTO;
									}
									logger.info(str + "importo max erogabile Categ. C:99 " + str_importoMaxErogabile_CatA_99 ); // 50000.00
								}
							}
						}


						// Verifica dati not null
						if (totaleSpeseContrCatg != null)
						{
							if (totaleSpeseContrCatg != null
									&& !str_percMaxErogabile_CatA_97.equals("0")
									&& !str_percMaxErogabile_CatA_97.equals("0.00")) {
								totaleSpeseContrCatg = replaceCommaToPoint(totaleSpeseContrCatg, logger);
								logger.info(str + "totaleSpeseContrCatg risulta: "	+ totaleSpeseContrCatg); 
							}


							bd_totale_CatA_97 = bd_totale_CatA_97.setScale(2, RoundingMode.CEILING);
							logger.info(str + "bd_totale_CatA_97 risulta: "	+ bd_totale_CatA_97);	
							bd_totaleSpeseDichiarate_97 = bd_totale_CatA_97;
							logger.info(str + "bd_totaleSpeseDichiarate_97 risulta: "	+ bd_totaleSpeseDichiarate_97); 


							bd_totale_CatA_98 = bd_totale_CatA_98.setScale(2, RoundingMode.CEILING);
							logger.info(str + "bd_totale_CatA_98 risulta: "	+ bd_totale_CatA_98);
							bd_totaleSpeseDichiarate_98 = bd_totale_CatA_98;
							logger.info(str + "bd_totaleSpeseDichiarate_98 risulta: "	+ bd_totaleSpeseDichiarate_98);


							bd_totale_CatC_99 = bd_totale_CatC_99.setScale(2, RoundingMode.CEILING);
							logger.info(str + "bd_totale_CatC_99 risulta: "	+ bd_totale_CatC_99);
							bd_totaleSpeseDichiarate_99 = bd_totale_CatC_99;
							logger.info(str + "bd_totaleSpeseDichiarate_99 risulta: "	+ bd_totaleSpeseDichiarate_99);


							if (totaleSpeseContrCatg != null) 
							{
								bd_totaleSpeseDichiarate = new BigDecimal(totaleSpeseContrCatg);
								bd_totaleSpeseDichiarate = bd_totaleSpeseDichiarate.setScale(2, RoundingMode.CEILING);
								logger.info(str + "bd_totaleSpeseDichiarate risulta: "	+ bd_totaleSpeseDichiarate);
							
							} else {
								totaleSpeseContrCatg = bd_totaleSpeseDichiarate.toString().replace(".", ",");
								logger.info(str + "Attenzione, totaleSpeseDichiarate non risulta compilato: "	+ totaleSpeseContrCatg);
							}


							/** ------------------------------------------------------------------- Percentuale max erogabile -inizio  */
							if (str_percMaxErogabile_CatA_97 != null) {
								str_percMaxErogabile_CatA_97 = replaceCommaToPoint(str_percMaxErogabile_CatA_97, logger);
								bd_percMaxErogabile_CatA_97 = new BigDecimal(str_percMaxErogabile_CatA_97);
								logger.info(str + "bd_percMaxErogabile risulta: "+ bd_percMaxErogabile_CatA_97); 
							} 


							if (str_percMaxErogabile_CatA_98 != null) { 
								str_percMaxErogabile_CatA_98 = replaceCommaToPoint(str_percMaxErogabile_CatA_98, logger);
								bd_percMaxErogabile_CatA_98 = new BigDecimal(str_percMaxErogabile_CatA_98);
								logger.info(str + "d_percMaxErogabile risulta: "+ bd_percMaxErogabile_CatA_98);
							
							} else {
								logger.info(str + "Attenzione, str_percMaxErogabile_CatA_98 non risulta compilato: " + str_percMaxErogabile_CatA_98);
							}


							if (str_percMaxErogabile_CatC_99 != null) {
								str_percMaxErogabile_CatC_99 = replaceCommaToPoint(str_percMaxErogabile_CatC_99, logger);
								bd_percMaxErogabile_CatC_99 = new BigDecimal(str_percMaxErogabile_CatC_99);
								logger.info(str + "bd_percMaxErogabile risulta: "	+ bd_percMaxErogabile_CatC_99);
							} else {
								logger.info(str + "Attenzione, str_percMaxErogabile_CatA_97 non risulta compilato: "	+ str_percMaxErogabile_CatC_99);
							}

							

							/** -------------------------------------------------------------------------- Importo max erogabile -inizio */
							if (str_importoMaxErogabile_CatA_97 != null	&& !str_importoMaxErogabile_CatA_97.equals(NON_PREVISTO)) 
							{
								str_importoMaxErogabile_CatA_97 = replaceCommaToPoint(str_importoMaxErogabile_CatA_97, logger);
								bd_importoMaxErogabile_97 = new BigDecimal(str_importoMaxErogabile_CatA_97);
								logger.info(str + "bd_importoMaxErogabile_97 risulta: " + bd_importoMaxErogabile_97);
								isDichiaratoImportoMaxErogabile_97 = true;
								logger.info(str + "isDichiaratoImportoMaxErogabile_97 risulta: " + isDichiaratoImportoMaxErogabile_97);

							} else {
								logger.info(str + "Attenzione, str_importoMaxErogabile_CatA_97 non risulta compilato: " + str_importoMaxErogabile_CatA_97);
							}


							if (str_importoMaxErogabile_CatA_98 != null	&& !str_importoMaxErogabile_CatA_98.equals(NON_PREVISTO)) 
							{
								str_importoMaxErogabile_CatA_98 = replaceCommaToPoint(str_importoMaxErogabile_CatA_98, logger);
								bd_importoMaxErogabile_98 = new BigDecimal(str_importoMaxErogabile_CatA_98);
								logger.info(str + "bd_importoMaxErogabile_98 risulta: " + bd_importoMaxErogabile_98);
								isDichiaratoImportoMaxErogabile_98 = true;
								logger.info(str + "isDichiaratoImportoMaxErogabile_98 risulta: " + isDichiaratoImportoMaxErogabile_98);
							
							} else {
								logger.info(str + "Attenzione, str_importoMaxErogabile_CatA_98 non risulta compilato: " + str_importoMaxErogabile_CatA_98);
							}


							if (str_importoMaxErogabile_CatA_99 != null	&& !str_importoMaxErogabile_CatA_99.equals(NON_PREVISTO)) 
							{
								str_importoMaxErogabile_CatA_99 = replaceCommaToPoint(str_importoMaxErogabile_CatA_99, logger);
								bd_importoMaxErogabile_99 = new BigDecimal(str_importoMaxErogabile_CatA_99);
								isDichiaratoImportoMaxErogabile_99 = true;
								logger.info(str + "bd_importoMaxErogabile_99 risulta: " + bd_importoMaxErogabile_99); // 50000.00
							
							} else {
								logger.debug(str + "Attenzione, str_importoMaxErogabile_CatA_99 non risulta compilato: " + str_importoMaxErogabile_CatA_99);
							}


							
							/** --------------------------------------------------------------------------- Spesa max dichiarabile -inizio */
							if (str_spesaMaxDichiarabile_97 != null && !str_spesaMaxDichiarabile_97.equals(NON_PREVISTO) && isSelectedIdTipoIntervento97.equals("true")) // 0,00
							{
								if(cultFormaAgevolazioneMap!=null) {

									if(!StringUtils.isBlank(str_spesaMaxDichiarabile_97)){                
										str_spesaMaxDichiarabile_97 = (String)cultFormaAgevolazioneMap.getStr_spesaMaxDichiarabile_97();
										if (str_spesaMaxDichiarabile_97.equals("Non previsto")) {
											str_spesaMaxDichiarabile_97 = "0,00";
											logger.info(str + "str_spesaMaxDichiarabile_98 risulta: " + str_spesaMaxDichiarabile_97);
										}else{
											str_spesaMaxDichiarabile_97 = (String)cultFormaAgevolazioneMap.getStr_spesaMaxDichiarabile_97();
											logger.info(str + "str_spesaMaxDichiarabile_97 risulta: " + str_spesaMaxDichiarabile_97); 
										}
									}
								}
								logger.info(str + "str_spesaMaxDichiarabile_97 vale: " + str_spesaMaxDichiarabile_97);

								if (verificaAssenzaCaratteri(str_spesaMaxDichiarabile_97, logger)) {
									logger.info(str + "isSpeseDichiarate_97 risulta non numerico!" + bd_spesaMaxDichiarabile_97);
									
								} else {
									str_spesaMaxDichiarabile_97 = replaceCommaToPoint(str_spesaMaxDichiarabile_97, logger); 
									bd_spesaMaxDichiarabile_97 = new BigDecimal(str_spesaMaxDichiarabile_97);
									logger.info(str + "bd_spesaMaxDichiarabile_97 risulta: " + bd_spesaMaxDichiarabile_97);
									isSpeseDichiarate_97 = true;
									logger.info(str + "isSpeseDichiarate_97 risulta: " + isSpeseDichiarate_97);
								}

							} else {
								logger.info(str + "Attenzione, str_spesaMaxDichiarabile_97 non risulta compilato: " + str_spesaMaxDichiarabile_97);
							}
							

							if (str_spesaMaxDichiarabile_98 != null && !str_spesaMaxDichiarabile_98.equals(NON_PREVISTO) && isSelectedIdTipoIntervento98.equals("true")) 
							{
								if(cultFormaAgevolazioneMap!=null) {

									if(!StringUtils.isBlank(str_spesaMaxDichiarabile_98))
									{
										str_spesaMaxDichiarabile_98 = (String)cultFormaAgevolazioneMap.getStr_spesaMaxDichiarabile_98();
									
										if (str_spesaMaxDichiarabile_98.equals("Non previsto")) {
											str_spesaMaxDichiarabile_98 = "0,00";
											logger.info(str + "str_spesaMaxDichiarabile_98 risulta: " + str_spesaMaxDichiarabile_98);
										
										} else {
											str_spesaMaxDichiarabile_98 = (String)cultFormaAgevolazioneMap.getStr_spesaMaxDichiarabile_98();
										}
									}
								}
								logger.info(str + "str_spesaMaxDichiarabile_98 vale: " + str_spesaMaxDichiarabile_98);

								if (verificaAssenzaCaratteri(str_spesaMaxDichiarabile_98, logger)) {
									logger.info(str + "isSpeseDichiarate_98 risulta non numerico!" + bd_spesaMaxDichiarabile_98);
								
								} else {
									str_spesaMaxDichiarabile_98 = replaceCommaToPoint(str_spesaMaxDichiarabile_98, logger);
									bd_spesaMaxDichiarabile_98 = new BigDecimal(str_spesaMaxDichiarabile_98);
									logger.info(str + "bd_spesaMaxDichiarabile_98 risulta: " + bd_spesaMaxDichiarabile_98);
									isSpeseDichiarate_98 = true;
									logger.info(str + "isSpeseDichiarate_98 risulta: " + isSpeseDichiarate_98);
								}

							} else {
								logger.info(str + "Attenzione, str_spesaMaxDichiarabile_98 non risulta compilato: " + str_spesaMaxDichiarabile_98);
							}


							if (str_spesaMaxDichiarabile_99 != null && !str_spesaMaxDichiarabile_99.equals(NON_PREVISTO)) 
							{
								str_spesaMaxDichiarabile_99 = replaceCommaToPoint(str_spesaMaxDichiarabile_99, logger);
								bd_spesaMaxDichiarabile_99 = new BigDecimal(str_spesaMaxDichiarabile_99);
								logger.info(str + "bd_spesaMaxDichiarabile_99 risulta: " + bd_spesaMaxDichiarabile_99);
								isSpeseDichiarate_99 = true;
								logger.info(str + "isSpeseDichiarate_99 risulta: " + isSpeseDichiarate_99);
								
							} else {
								logger.info(str + "Attenzione, str_spesaMaxDichiarabile_99 non risulta compilato: " + str_spesaMaxDichiarabile_99);
							}


							if (isDichiaratoImportoMaxErogabile_99) 
							{
								if (verificaPrecondizione(bd_totaleSpeseDichiarate_99, bd_percMaxErogabile_CatC_99, bd_importoMaxErogabile_99, logger)) 
								{
									/** Calcolo importo richiesto per Categoria: C + ( importo max erogabile ) */
									bd_importoRichiesto_99 = calcoloImportoRichiestoCategoriaC(bd_totaleSpeseDichiarate_99, bd_percMaxErogabile_CatC_99, bd_importoMaxErogabile_99, logger);
									bd_importoRichiesto_99 = bd_importoRichiesto_99.setScale(2, RoundingMode.CEILING);
									logger.info(str + "bd_importoRichiesto_99 risulta: "	+ bd_importoRichiesto_99);
									logger.info(str + "importo richiesto per la Categoria A:99 risulta: " + bd_importoRichiesto_99);
									strImportoCat99 = bd_importoRichiesto_99.toString().replace(".", ",");
									logger.info(str + "importo strImportoCat99 per la Categoria A:99 risulta: " + strImportoCat99);
								}
							} 

							
							
							/** --------------------------------------------------------------------------- Totale spese dichiarate -inizio */
							
							/** Calcolo totale importo richiesto per A: 97 */
							if (bd_totaleSpeseDichiarate_97 != null ) {

								if (str_spesaMaxDichiarabile_97 != null 
										&& !str_spesaMaxDichiarabile_97.equals("0,00") 
										&& !str_spesaMaxDichiarabile_97.equals("0.00") 
										&& !str_spesaMaxDichiarabile_97.equalsIgnoreCase("Non previsto")) 
								{ 
									bd_spesaMaxDichiarabile_97 = new BigDecimal(str_spesaMaxDichiarabile_97);
									bd_spesaMaxDichiarabile_97 = bd_spesaMaxDichiarabile_97.setScale(2, RoundingMode.CEILING);

									if(str_spesaMaxDichiarabile_97.indexOf('.') != -1)
									{
										str_spesaMaxDichiarabile_97 = str_spesaMaxDichiarabile_97.replace('.', ',');
										logger.info(str + "str_spesaMaxDichiarabile_97 risulta: "	+ str_spesaMaxDichiarabile_97); // 1000.00 *****
									}

									logger.info(str + "bd_spesaMaxDichiarabile_97 risulta: "	+ bd_spesaMaxDichiarabile_97); // 1000.00 *****
									
									if (verificaPrecondizione(bd_totaleSpeseDichiarate_97, bd_percMaxErogabile_CatA_97, bd_spesaMaxDichiarabile_97, logger)) 
									{
										/** Calcolo importo richiesto per Categoria: A:97 ( spesa max dichiarabile ) */
										bd_importoRichiesto_97 = calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA(bd_totaleSpeseDichiarate_97, bd_percMaxErogabile_CatA_97, bd_spesaMaxDichiarabile_97, logger);
										bd_importoRichiesto_97 = bd_importoRichiesto_97.setScale(2, RoundingMode.CEILING); // 600.00
										logger.info(str + "bd_importoRichiesto_97 risulta: "	+ bd_importoRichiesto_97);
										strImportoCat97 = bd_importoRichiesto_97.toString().replace(".", ",");
										logger.info(str + "strImportoCat97 risulta: "	+ strImportoCat97);
									}
									logger.debug(str + "Esito del calcolo importo richiesto per la Categoria A:97 "+ bd_importoRichiesto_97);
									
								} else {
									logger.debug(str + " ... ");
									
									if (verificaPrecondizione(bd_totaleSpeseDichiarate_97, bd_percMaxErogabile_CatA_97, logger)) 
									{
										/** Calcolo importo richiesto per Categoria: A:97 ( spesa max dichiarabile ) */
										bd_importoRichiesto_97 = calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA97_MSTZ(bd_totaleSpeseDichiarate_97, bd_percMaxErogabile_CatA_97, logger);
										// bd_importoRichiesto_97 = bd_importoRichiesto_97.setScale(2, RoundingMode.CEILING); // 600.00
										logger.info(str + "bd_importoRichiesto_97 risulta: "	+ bd_importoRichiesto_97);
										strImportoCat97 = bd_importoRichiesto_97.toString().replace(".", ",");
										logger.info(str + "strImportoCat97 risulta: "	+ strImportoCat97);
									}
									logger.debug(str + "Esito del calcolo importo richiesto per la Categoria A:97 "+ bd_importoRichiesto_97);
								}
							}
							
							
							/** Calcolo totale importo richiesto per A: 98 */
							if (bd_totaleSpeseDichiarate_98 != null ) {

								if (str_spesaMaxDichiarabile_98 != null && !str_spesaMaxDichiarabile_98.equals("0,00")) { 
									bd_spesaMaxDichiarabile_98 = new BigDecimal(str_spesaMaxDichiarabile_98);
									bd_spesaMaxDichiarabile_98 = bd_spesaMaxDichiarabile_98.setScale(2, RoundingMode.CEILING);

									if(str_spesaMaxDichiarabile_98.indexOf('.') != -1)
									{
										str_spesaMaxDichiarabile_98 = str_spesaMaxDichiarabile_98.replace('.', ',');
										logger.info(str + "str_spesaMaxDichiarabile_98 risulta: "	+ str_spesaMaxDichiarabile_98); 
									}

									logger.info(str + "bd_spesaMaxDichiarabile_98 risulta: "	+ bd_spesaMaxDichiarabile_98); 
								}

								if (verificaPrecondizione(bd_totaleSpeseDichiarate_98, bd_percMaxErogabile_CatA_98, bd_spesaMaxDichiarabile_98, logger)) 
								{
									/** Calcolo importo richiesto per Categoria: A:98 ( spesa max dichiarabile ) */
									bd_importoRichiesto_98 = calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA(bd_totaleSpeseDichiarate_98, bd_percMaxErogabile_CatA_98, bd_spesaMaxDichiarabile_98, logger);
									bd_importoRichiesto_98 = bd_importoRichiesto_98.setScale(2, RoundingMode.CEILING);
									logger.info(str + "bd_importoRichiesto_98 risulta: "	+ bd_importoRichiesto_98);
									strImportoCat98 = bd_importoRichiesto_98.toString().replace(".", ",");
									logger.info(str + "strImportoCat98 risulta: "	+ strImportoCat98);
								}
								logger.info(str + "Esito del calcolo importo richiesto per la Categoria A:98 "+ bd_importoRichiesto_98);
							} 


							bd_importoTotaleRichiesto =  bd_importoRichiesto_97.add(bd_importoRichiesto_98).add(bd_importoRichiesto_99);
							bd_importoTotaleRichiesto = bd_importoTotaleRichiesto.setScale(2, RoundingMode.FLOOR);
							logger.info(str + "bd_importoTotaleRichiesto "+ bd_importoTotaleRichiesto);
							strTotImportoRichiesto = bd_importoTotaleRichiesto.toString().replace(".", ",");
							logger.info(str + "strTotImportoRichiesto "+ strTotImportoRichiesto);
						} 

					} // -/ fine if
					
				} else {
					
					logger.info(str + "Esito del calcolo importo richiesto per la Categoria A: " + MSG_ERRORE_COMPILAZIONE_SPESE);

					viewWarningSpese="true";
					logger.info(str + " viewWarningSpese risulta: " + viewWarningSpese);

					speseNonCompilate="true";
					logger.info(str + " speseNonCompilate risulta: " + speseNonCompilate);
				}
				
			} else {
				logger.info(str + " Piano spese risulta null!");
				speseNonCompilate="true";
			}
		}



		/** namespace - output importoRichiestoCat98 */
		output.tipologiaInterventoList = tipologiaInterventoList;

		output.esistonoDettagli = esistonoDettagli;
		logger.info(str + "esistonoDettagli: " + esistonoDettagli);

		output.viewWarningSpese = viewWarningSpese;
		logger.info(str + "viewWarningSpese: " + viewWarningSpese);

		output.speseNonCompilate = speseNonCompilate;
		logger.info(str + "speseNonCompilate: " + speseNonCompilate);

		output.isGrandiStazioni = isGrandiStazioni;
		logger.info(str + "isGrandiStazioni: " + isGrandiStazioni);

		/** Tipo intervento selezionato */
		output.setIsSelectedIdTipoIntervento97(isSelectedIdTipoIntervento97);
		logger.info(str + "isSelectedIdTipoIntervento97: " + isSelectedIdTipoIntervento97);

		output.setIsSelectedIdTipoIntervento98(isSelectedIdTipoIntervento98);
		logger.info(str + "isSelectedIdTipoIntervento98: " + isSelectedIdTipoIntervento98);

		output.setIsSelectedIdTipoIntervento99(isSelectedIdTipoIntervento99);
		logger.info(str + "isSelectedIdTipoIntervento99: " + isSelectedIdTipoIntervento99);

		/** Contributi */
		output.valoriContributoList= valoriContributoList;
		logger.info(str + "valoriContributoList: " + valoriContributoList.size());

		output.totaliContributoList=totaliContributoList;
		logger.info(str + "totaliContributoList: " + totaliContributoList.size());
		
		/** Totale spese */
		output.totaleSpeseContrCatg= totaleSpeseContrCatg.replace(".", ",");
		logger.info(str + "totaleSpeseContrCatg: " + totaleSpeseContrCatg);

		/** Totale spese dichiarate Cat. A: Spese per la sicurezza delle aree sciabili */
		output.strTotCat97=strTotCat97;
		logger.info(str + "strTotCat97: " + strTotCat97);

		/** Totale spese dichiarate Cat. A: Spese per la produzione di neve programmata */
		output.strTotCat98=strTotCat98;
		logger.info(str + "strTotCat98: " + strTotCat98);

		/** Totale spese dichiarate Cat. C */
		output.totCat99=totCat99;
		logger.info(str + "totCat99: " + totCat99);

		output.strImportoCat97=strImportoCat97;	
		logger.info(str + "strImportoCat97: " + strImportoCat97);

		output.strImportoCat98=strImportoCat98;	
		logger.info(str + "strImportoCat98: " + strImportoCat98);

		output.strImportoCat99=strImportoCat99;	
		logger.info(str + "strImportoCat99: " + strImportoCat99);

		output.strTotImportoRichiesto=strTotImportoRichiesto;
		logger.info(str + "strTotImportoRichiesto: " + strTotImportoRichiesto);
		
		/** Campo input: spesa max dichiarabile */
		output.str_spesaMaxDichiarabile_97 = str_spesaMaxDichiarabile_97.replace(".", ",");
		logger.info(str + "str_spesaMaxDichiarabile_97: " + str_spesaMaxDichiarabile_97);
		
		output.str_spesaMaxDichiarabile_98 = str_spesaMaxDichiarabile_98;
		logger.info(str + "str_spesaMaxDichiarabile_98: " + str_spesaMaxDichiarabile_98);
		
		/** percentuali per tipologia intervento */
		output.bd_percMaxErogabile_CatA_97 = bd_percMaxErogabile_CatA_97.toString();
		output.bd_percMaxErogabile_CatA_98 = bd_percMaxErogabile_CatA_98.toString();
		output.bd_percMaxErogabile_CatC_99 = bd_percMaxErogabile_CatC_99.toString();

		logger.info(str + "_formaFinContrNeve ***end*** ");
		
		return output;
	}


	/**
	 * Verifico se le spese e le Tipologie di Contributo sono allineate
	 * @param logger
	 * @return TRUE se sono disallineati
	 */
	private boolean tipContrAllineatoSpese(Logger logger) {

		// Riporto in questa funzione il codice esatto presente nelle GVR (Direttiva)

		String logprefix = "[FormaFinContrNeve::tipContrAllineatoSpese] ";
		boolean disallineamento = false;

		// verifico che le "Tipologie di Contributo" siano allineate con le "Spese" valorizzate  

		PianoSpeseVO pianoSpese = input._pianoSpese;

		if(pianoSpese!=null){

			List<DettaglioVoceSpesaInterventoVO> pianoSpeseList = Arrays.asList(pianoSpese.getPianoSpeseList());

			// guardo nelle "Spese" e
			// mi segno tutti i tipiRecord = 1 e 2 con visibilita="selezionato"
			// Le Spese potrebbero essere di meno nel caso in cui l'utente non le abbia ancora salvate
			Map<String,String> numSpeseValorizzateMap = new HashMap<String,String>();

			for (DettaglioVoceSpesaInterventoVO dett : pianoSpeseList) {
				if((StringUtils.equals(dett.getTipoRecord(), "1") || StringUtils.equals(dett.getTipoRecord(), "2"))
						&& !StringUtils.equals(dett.getVisibilita(), "nonselezionato")
						){

					logger.info(logprefix + "DTVP: tipoRecord="+dett.getTipoRecord()+" , idTipoIntervento="+dett.getIdTipoIntervento()+", TotaleVoceSpesa="+dett.getTotaleVoceSpesa()+", visibilita="+dett.getVisibilita());

					if(numSpeseValorizzateMap.containsKey(dett.getIdTipoIntervento())){

						String valore = numSpeseValorizzateMap.get(dett.getIdTipoIntervento());
						logger.info(logprefix + "DTVP: tipoRecord="+dett.getTipoRecord()+" , valore="+valore+", visibilita="+dett.getVisibilita());

						BigDecimal valoreBDprec = new BigDecimal(valore);
						logger.info(logprefix + "DTVP: valoreBDprec="+valoreBDprec);

						double valoreBD = 0.0D;
						if(dett.getTotaleVoceSpesa()!=null){
							BigDecimal tmp = new BigDecimal(dett.getTotaleVoceSpesa());
							valoreBD = tmp.doubleValue();
						}
						logger.info(logprefix + "DTVP: valoreBD="+valoreBD);

						double nuovoValore = valoreBDprec.doubleValue() + valoreBD;
						logger.info(logprefix + "DTVP: nuovoValore="+nuovoValore);

						numSpeseValorizzateMap.put(dett.getIdTipoIntervento(), nuovoValore+"");

					}else{
						numSpeseValorizzateMap.put(dett.getIdTipoIntervento(), dett.getTotaleVoceSpesa());
						logger.info(logprefix + "DTVP: tipoRecord="+dett.getTipoRecord()+" , TotaleVoceSpesa="+dett.getTotaleVoceSpesa()+", visibilita="+dett.getVisibilita());
					}
				}
			}
			logger.info(logprefix + " numSpeseValorizzateMap="+numSpeseValorizzateMap);

			FormaFinContrNeveVO formAgevolazione = input._formaAgevolazione;

			if(formAgevolazione!=null){

				// popolo una tabella con le Tipologie di Contributo "checked"
				Map<String,String> numTipolContribMap = new HashMap<String,String>();

				// controllo se le formAgevolazione="checked" corrispondono alle Spese="selezionate"
				TipologiaInterventoNeveVO[] tipologiaInterventoArr = formAgevolazione.getTipologiaInterventoList();
				// logger.info(logprefix + " tipologiaInterventoArr="+tipologiaInterventoArr);

				if(tipologiaInterventoArr!=null && tipologiaInterventoArr.length>0){

					for (int i = 0; i < tipologiaInterventoArr.length; i++) {
						TipologiaInterventoNeveVO tipInt = tipologiaInterventoArr[i];
						if(StringUtils.equals("true", tipInt.getChecked())){
							numTipolContribMap.put(tipInt.getIdTipoIntervento(),tipInt.getChecked() );
						}
					}
					logger.info(logprefix + " numTipolContribMap="+numTipolContribMap);

					for (String key : numSpeseValorizzateMap.keySet()) {

						if(StringUtils.equals(key, "97")){ 
							if(!numTipolContribMap.containsKey(key)){

								//spese non hanno corrispondente "Tipologie di Contributo"
								logger.warn(logprefix + " Tipologia di Contributo nulle, Spese non nulle");
								disallineamento = true;
								break;
							}else{

								logger.info(logprefix + "key="+key+", spesa = "+numSpeseValorizzateMap.get(key)+" , TipolContrib="+formAgevolazione.getStrTotCat97());

								if(confrontaValori(numSpeseValorizzateMap.get(key), formAgevolazione.getStrTotCat97(), logger)){
									// valore della spesa diverso da quello della Tipologia di Intervento
									logger.info(logprefix + "valore della spesa diverso da quello della Tipologia di Intervento, key="+key);
									disallineamento = true;
									break;
								}
							}
						}

						if(StringUtils.equals(key, "98")){
							if(!numTipolContribMap.containsKey(key)){

								//spese non hanno corrispondente "Tipologie di Contributo"
								logger.warn(logprefix + " Tipologia di Contributo nulle, Spese non nulle");
								disallineamento = true;
								break;
							}else{

								logger.info(logprefix + "key="+key+", spesa = "+numSpeseValorizzateMap.get(key)+" , TipolContrib="+formAgevolazione.getStrTotCat98());

								if(confrontaValori(numSpeseValorizzateMap.get(key), formAgevolazione.getStrTotCat98(), logger)){
									// valore della spesa diverso da quello della Tipologia di Intervento
									logger.info(logprefix + "valore della spesa diverso da quello della Tipologia di Intervento, key="+key);
									disallineamento = true;
									break;
								}
							}
						}

						if(StringUtils.equals(key, "99")){
							if( !numTipolContribMap.containsKey(key)){

								//spese non hanno corrispondente "Tipologie di Contributo"
								logger.warn(logprefix + " Tipologia di Contributo nulle, Spese non nulle");
								disallineamento = true;
								break;
							}else{

								logger.info(logprefix + "key="+key+",spesa = "+numSpeseValorizzateMap.get(key)+" , TipolContrib="+formAgevolazione.getTotCat99());

								if(confrontaValori(numSpeseValorizzateMap.get(key), formAgevolazione.getTotCat99(), logger)){
									// valore della spesa diverso da quello della Tipologia di Intervento
									logger.info(logprefix + "valore della spesa diverso da quello della Tipologia di Intervento, key="+key);
									disallineamento = true;
									break;
								}
							}
						}
					}

				}else{
					if(numSpeseValorizzateMap.size()>0){
						// ho delle Spese ma non delle Tipologie di Intervento
						logger.warn(logprefix + " Tipologia di Contributo nulle, Spese non nulle");
						disallineamento = true;
					}
				}

			}else{
				// Questo non dovrebbe mai accadere, se le "Tipologia di Contributo" sono nulle vengo fermato prima
				logger.warn(logprefix + " Tipologia di Contributo nulle, Spese non nulle");
				disallineamento = true;
			}

		}
		return disallineamento;
	}

	/**
	 * @param string
	 * @param importoRichiestoCat97
	 * @return TRUE se i valori sono diversi
	 */
	private boolean confrontaValori(String spesa, String tipolContributo, Logger logger) {
		boolean ret = true;

		String sp = arrotondaValoreEFormatta(spesa, logger);
		String contr = arrotondaValoreEFormatta(tipolContributo, logger);
		logger.info("[Direttiva::confrontaValori] spesa="+sp+", contributo="+contr);

		if(StringUtils.equals(sp, contr)){
			ret = false;
		}
		return ret;
	}

	private String arrotondaValoreEFormatta(String valore, Logger logger){

		int numDecimali = 2;

		if(StringUtils.isBlank(valore)){
			return "";
		}
		if(valore.indexOf('.') == -1 || numDecimali<=0 ){
			return DecimalFormat.decimalFormat(valore, numDecimali);
		}else{
			BigDecimal bd = new BigDecimal(valore).setScale(numDecimali, RoundingMode.HALF_UP);
			return bd.toString().replace('.', ',');		       
		}		
	}

	private boolean verificaAssenzaCaratteri(String valore, Logger logger) {
		boolean ris = true;
		if (valore != null) {
			if (valore.matches("^[0-9]*\\,?[0-9]*$")) {
				logger.info("[FormaFinContrNeve::verificaAssenzaCaratteri] Il valore contiene solo numeri: "+ valore);
				ris = false;
			} else {
				logger.info("[FormaFinContrNeve::verificaAssenzaCaratteri] ATTENZIONE: "+ valore+ " contiene anche stringhe!");
			}
		}
		return ris;
	}

	/** --------------------------------------------------------- Metodi  --------------------------------------------------------- */

	/****************************************
	 * Verifica precondizione con 3 argomenti
	 * 
	 * @param bd_totaleSpeseDichiarate
	 * @param bd_percMaxErogabile
	 * @param bd_spesaMaxDichiarabile
	 * @return
	 ****************************************/

	private static boolean verificaPrecondizione(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, BigDecimal bd_spesaMaxDichiarabile, Logger logger) 
	{
		boolean isOK = true;
		boolean valueNotNull = false;
		boolean valueNegative = false;

		/**
		 * Precondizione-1 calcolo importo richiesto: - valori passati siano
		 * diversi da null
		 */
		if (valueNotNull = checkIsNull(bd_totaleSpeseDichiarate, bd_percMaxErogabile, bd_spesaMaxDichiarabile)) 
		{
			/** Precondizione-2: Valori non negativi */
			logger.info("[FormaFinContrNeve::verificaPrecondizione] *** valueNotNull *** : " + valueNotNull);
			logger.info("[FormaFinContrNeve::verificaPrecondizione] *** valueNegative *** : " + valueNegative);
			if (!(valueNegative = isNegative(bd_totaleSpeseDichiarate, bd_percMaxErogabile, bd_spesaMaxDichiarabile, logger))) 
			{
				isOK = true;
			}

		} 
		else {
			logger.info("[FormaFinContrNeve::verificaPrecondizione]*** ATTENZIONE *** : Precondizione per il calcolo importo richiesto NON superata!");
		}

		return isOK;
	}


	/**********************************************
	 * Metodo a 3 argomenti, 
	 *  che verifica se il valore BigDecimal
	 * 	- risulta negativo:
	 *  ritorna:
	 * 		- true 	se il BigDecimal negativo
	 * 		- false 	se il BigDecimal positivo
	 * *******************************************/
	private static boolean isNegative(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, BigDecimal bd_spesaMaxDichiarabile, Logger logger) 
	{
		logger.info(bd_totaleSpeseDichiarate.signum() == -1);
		logger.info(bd_percMaxErogabile.signum() == -1);

		if (bd_totaleSpeseDichiarate != null && bd_percMaxErogabile != null) 
		{
			if ((bd_totaleSpeseDichiarate.signum() == -1)
					&& (bd_percMaxErogabile.signum() == -1)
					&& (bd_spesaMaxDichiarabile.signum() == -1)) {

				return true;
			} 
			else {
				return false;
			}
		}

		return false;
	}



	/************************************
	 * Verifica con 3 argomenti 
	 *  se i valori passati sono:
	 *  - diversi da 'null'
	 * @param bd_totaleSpeseDichiarate
	 * @param bd_percMaxErogabile
	 * @param bd_spesaMaxDichiarabile
	 * @return
	 *************************************/

	private static boolean checkIsNull(BigDecimal bd_totaleSpeseDichiarate,	BigDecimal bd_percMaxErogabile, BigDecimal bd_spesaMaxDichiarabile) 
	{
		boolean ris = false;

		if (bd_totaleSpeseDichiarate != null && bd_percMaxErogabile != null	&& bd_spesaMaxDichiarabile != null) 
		{
			ris = true;
		}
		return ris;
	}



	/*************************
	 * Verifico presenza comma
	 ************************/
	public static boolean isPresentComma(String stringa) 
	{
		boolean ris = false;

		// Precondizione: stringa non null
		if (stringa.length() > 0) 
		{
			if (stringa.indexOf(",") != -1) 
			{
				ris = true;
			}
		}

		return ris;
	}



	/***************************
	 * Sostituisce la:
	 * - (,) col (.)
	 * 
	 * @param stringaConVirgola
	 * @return
	 ***************************/

	public static String replaceCommaToPoint(String stringaConVirgola, Logger logger) 
	{
		// precondizione: presenza virgola nella stringa passata come argomento
		if (stringaConVirgola != null) 
		{
			if (stringaConVirgola.indexOf(",") != -1) 
			{
				logger.info("[FormaFinContrNeve::replaceCommaToPoint]Risulta presente un virgola, eseguo sostituzione!");

				stringaConVirgola = stringaConVirgola.replace(",", ".");
			} 
			else {
				logger.info("[FormaFinContrNeve::replaceCommaToPoint]Virgola non trovata.");
			}
		}
		logger.info("[FormaFinContrNeve::replaceCommaToPoint]Valore dopo il replace risulta: " + stringaConVirgola);
		return stringaConVirgola;
	}



	/*************************************************************
	 * Verifica precondizione con 2 argomenti: 
	 * 	prima del calcolo importo-richiesto:
	 * @param bd_totaleSpeseDichiarate
	 * @param bd_percMaxErogabile
	 * @return
	 *************************************************************/

	public static boolean verificaPrecondizione(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, Logger logger) 
	{
		boolean isOK = true;
		boolean valueNotNull = false;
		boolean valueNegative = false;

		/** Precondizione-1: calcolo importo richiesto: - valori passati siano diversi da null */
		if (valueNotNull = checkIsNull(bd_totaleSpeseDichiarate, bd_percMaxErogabile)) 
		{
			/** Precondizione-2: Valori non negativi */
			logger.info("[FormaFinContrNeve::verificaPrecondizione]*** valueNotNull *** : " + valueNotNull);
			logger.info("[FormaFinContrNeve::verificaPrecondizione]*** valueNegative *** : " + valueNegative);

			if (!(valueNegative = isNegative(bd_totaleSpeseDichiarate, bd_percMaxErogabile, logger))) 
			{
				isOK = true;
			}
		} 
		else {
			logger.info("[FormaFinContrNeve::verificaPrecondizione]*** ATTENZIONE *** : Precondizione per il calcolo importo richiesto NON superata!");
		}

		return isOK;
	}



	/************************************
	 * Verifica con 2 argomenti 
	 * se i valori passati sono:
	 * - diversi da 'null'
	 * 
	 * @param bd_totaleSpeseDichiarate
	 * @param bd_percMaxErogabile
	 * @return
	 ************************************/

	public static boolean checkIsNull(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile) 
	{
		boolean ris = false;

		if (bd_totaleSpeseDichiarate != null && bd_percMaxErogabile != null) {
			ris = true;
		}
		return ris;
	}



	/** ------------------------------------- Funzioni calcolo importo richiesto in base alla Categoria selezionata --- */

	/** Calcolo importo richiesto per Categoria: C + ( importo max erogabile ) */

	public static BigDecimal calcoloImportoRichiestoCategoriaC (BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, BigDecimal bd_importoMaxErogabile, Logger logger) 
	{
		BigDecimal bd_impRicCatC = new BigDecimal(0.00);
		BigDecimal cento = new BigDecimal(100);
		BigDecimal bd_tmpImpRicCatC = new BigDecimal(0.00);
		boolean isMinoreContrMaxErogabile = false;
		boolean isMinoreTotImpRichiesto = false;
		
		// pre-condizione: valori BigDecimal
		if (isWhole(bd_totaleSpeseDichiarate, logger) && isWhole(bd_percMaxErogabile, logger) && isWhole(bd_importoMaxErogabile, logger)) 
		{
			/**
			 * verifico il valore minimo tra: - bd_totaleSpeseDichiarate - bd_spesaMaxDichiarabile
			 * 
			 * == 0  : sono uguali 
			 * == 1  : primo 	valore risulta maggiore [ bd_totaleSpeseDichiarate ] 
			 * == -1 : secondo 	valore risulta maggiore [ bd_spesaMaxDichiarabile  ]
			 */

			isMinoreContrMaxErogabile = checkImpRichMinContrMaxErogabile(bd_totaleSpeseDichiarate, bd_importoMaxErogabile, logger);

			if (isMinoreContrMaxErogabile) 
			{
				bd_impRicCatC = (bd_totaleSpeseDichiarate.multiply(bd_percMaxErogabile).divide(cento).setScale(2));
				logger.info("[FormaFinContrNeve::calcoloImportoRichiestoCategoriaC]Importo richiesto per la Categoria A, risulta: " + bd_impRicCatC);
			} 
			else {
				// calcolo: bd_totaleSpeseDichiarate * bd_percMaxErogabile
				
				bd_tmpImpRicCatC = (bd_totaleSpeseDichiarate.multiply(bd_percMaxErogabile).divide(cento).setScale(2));
				logger.info("*** modifica in corso: step1: " + bd_tmpImpRicCatC);
				
				isMinoreTotImpRichiesto = checkImpRichMinContrMaxErogabile(bd_tmpImpRicCatC, bd_importoMaxErogabile, logger);
				
				if (isMinoreTotImpRichiesto) 
				{
					bd_impRicCatC = bd_tmpImpRicCatC.setScale(2);
					logger.info("[FormaFinContrNeve::calcoloImportoRichiestoCategoriaC]Importo richiesto per la Categoria C, risulta: " + bd_impRicCatC);
				} else {
					bd_impRicCatC = bd_importoMaxErogabile;
					logger.info("[FormaFinContrNeve::calcoloImportoRichiestoCategoriaC]Importo richiesto per la Categoria C, risulta: " + bd_impRicCatC);
				}
				
				// bd_impRicCatC = bd_importoMaxErogabile;
			}
		}

		return bd_impRicCatC;
	}


	/**
	 * @param bd_impRicCatC
	 * @param bd_importoMaxErogabile
	 * @param logger
	 * @return
	 */
	private static boolean checkImpRichMinContrMaxErogabile(BigDecimal bd_impRicCatC, BigDecimal bd_importoMaxErogabile, Logger logger) 
	{
		boolean isImportoRichiestoMinoreContrMaxErogabile = false;

		BigDecimal bd_minore = new BigDecimal(0.00);

		int result;
		result = bd_impRicCatC.compareTo(bd_importoMaxErogabile);
		logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile]Result vale: " + result);

		if (result == 0) 
		{
			isImportoRichiestoMinoreContrMaxErogabile = true;

			bd_minore = bd_impRicCatC;
			logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile]*** bd_minore *** : " + bd_minore);

			logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile]"+bd_impRicCatC + " uguale a " + bd_importoMaxErogabile);
		}
		else if (result == 1) 
		{
			bd_minore = bd_importoMaxErogabile;
			logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile]*** bd_minore *** : " + bd_minore);
			logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile] "+bd_impRicCatC + " > di " + bd_importoMaxErogabile);
		} 
		else if (result == -1) 
		{
			isImportoRichiestoMinoreContrMaxErogabile = true;

			bd_minore = bd_impRicCatC;
			logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile]*** bd_minore *** : " + bd_minore);
			logger.info("[FormaFinContrNeve::checkImpRichMinContrMaxErogabile]"+bd_impRicCatC + " < di " + bd_importoMaxErogabile);
		}

		return isImportoRichiestoMinoreContrMaxErogabile;
	}



	/** Calcolo importo richiesto per Categoria: A + ( spesa max dichiarabile ) */

	public static BigDecimal calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, BigDecimal bd_spesaMaxDichiarabile, Logger logger) 
	{
		BigDecimal bd_impRicCatA = new BigDecimal(0.00);
		BigDecimal cento = new BigDecimal(100);
		BigDecimal valoreMinimo = new BigDecimal(0.00);

		// pre-condizione: valori BigDecimal
		if (isWhole(bd_totaleSpeseDichiarate, logger) && isWhole(bd_percMaxErogabile, logger) && isWhole(bd_spesaMaxDichiarabile, logger)) 
		{
			/**
			 * verifico il valore minimo tra: - bd_totaleSpeseDichiarate e bd_spesaMaxDichiarabile
			 * 
			 * == 0 	: sono uguali 
			 * == 1 	: primo valore 		risulta maggiore [bd_totaleSpeseDichiarate] 
			 * == -1 	: secondo valore	risulta maggiore [bd_spesaMaxDichiarabile]
			 */
			valoreMinimo = valoreMinimo(bd_totaleSpeseDichiarate, bd_spesaMaxDichiarabile, logger);

			// eseguo calcolo importo richiesto: (bd_totaleSpeseDichiarate * bd_percMaxErogabile) / percento;
			bd_impRicCatA = (valoreMinimo.multiply(bd_percMaxErogabile).divide(cento)).setScale(2, RoundingMode.HALF_UP);
			logger.info("[FormaFinContrNeve::calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA]Importo richiesto per la Categoria A, risulta: " + bd_impRicCatA);
		}

		return bd_impRicCatA;
	}
	
	
	
	/** Calcolo importo richiesto per Categoria: A + ( se Micro Stazioni e senza spesa max dichiarabile ) */
	public static BigDecimal calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA97_MSTZ(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, Logger logger) 
	{
		BigDecimal bd_impRicCatA97 = new BigDecimal(0.00);
		BigDecimal cento = new BigDecimal(100);

		// pre-condizione: valori BigDecimal
		if (isWhole(bd_totaleSpeseDichiarate, logger) && isWhole(bd_percMaxErogabile, logger)) 
		{
			// eseguo calcolo importo richiesto: (bd_totaleSpeseDichiarate * bd_percMaxErogabile) / percento;
			bd_impRicCatA97 = (bd_totaleSpeseDichiarate.multiply(bd_percMaxErogabile).divide(cento)).setScale(2, RoundingMode.HALF_UP);
			logger.info("[FormaFinContrNeve::calcoloImportoRichiestoSpesaMaxDichiarabileCategoriaA97_MSTZ]Importo richiesto per la Categoria A97, risulta: " + bd_impRicCatA97);
		}

		return bd_impRicCatA97;
	}


	/*********************************
	 * Recupero il valore minimo
	 * @param bd_totaleSpeseDichiarate
	 * @param bd_spesaMaxDichiarabile
	 * @return
	 ********************************/

	private static BigDecimal valoreMinimo(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_spesaMaxDichiarabile, Logger logger) 
	{
		BigDecimal bd_minore = new BigDecimal(0.00);

		int result;
		result = bd_totaleSpeseDichiarate.compareTo(bd_spesaMaxDichiarabile);
		logger.info("[FormaFinContrNeve::valoreMinimo] Result vale: " + result);

		if (result == 0) {
			bd_minore = bd_totaleSpeseDichiarate;
			logger.info("[FormaFinContrNeve::valoreMinimo]" + bd_totaleSpeseDichiarate + " uguale a " + bd_spesaMaxDichiarabile);
		}
		else if (result == 1) 
		{
			bd_minore = bd_spesaMaxDichiarabile;
			logger.info("[FormaFinContrNeve::valoreMinimo]"+bd_totaleSpeseDichiarate + " > di " + bd_spesaMaxDichiarabile);
		}
		else if (result == -1) 
		{
			bd_minore = bd_totaleSpeseDichiarate;
			logger.info("[FormaFinContrNeve::valoreMinimo]"+bd_totaleSpeseDichiarate + " < di " + bd_spesaMaxDichiarabile);
		}

		return bd_minore;
	}



	/** Calcolo importo richiesto per Categoria: A:97 */
	public static BigDecimal calcoloImportoRichiestoCategoriaA(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, Logger logger) 
	{
		BigDecimal bd_impRicCatA = new BigDecimal(0.00);
		BigDecimal cento = new BigDecimal(100);

		// pre-condizione: valori BigDecimal presenti:
		if (isWhole(bd_totaleSpeseDichiarate, logger) && isWhole(bd_percMaxErogabile, logger)) 
		{
			// eseguo calcolo importo richiesto: (bd_totaleSpeseDichiarate * bd_percMaxErogabile) / percento;
			bd_impRicCatA = (bd_totaleSpeseDichiarate.multiply(bd_percMaxErogabile).divide(cento));
			logger.info("[FormaFinContrNeve::calcoloImportoRichiestoCategoriaA]Importo richiesto per la Categoria A, risulta: " + bd_impRicCatA);
		}

		return bd_impRicCatA;
	}



	/** Metodo che verifica se il valore risulta essere tipo: BigDecimal */
	public static boolean isWhole(BigDecimal bigDecimal, Logger logger) 
	{
		boolean ris = false;
		logger.info("[FormaFinContrNeve::isWhole] bigDecimal risulta: " + bigDecimal);
		if( bigDecimal.toPlainString().matches("\\d+(\\.[0-9]+)?")){
			ris = true;
		}
		// ris = bigDecimal.setScale(0, RoundingMode.HALF_UP).compareTo(bigDecimal) == 0;
		logger.info("[FormaFinContrNeve::isWhole] ris risulta: " + ris);
		return ris;
	}					



	/**********************************************
	 * Metodo che verifica se il valore BigDecimal
	 * risulta negativo:
	 * ritorna:
	 * - true 	se il BigDecimal negativo
	 * - false 	se il BigDecimal positivo
	 * 
	 * @param b
	 * @return
	 **********************************************/

	public static boolean isNegative(BigDecimal bd_totaleSpeseDichiarate, BigDecimal bd_percMaxErogabile, Logger logger)
	{
		logger.info("[FormaFinContrNeve::isNegative] bd_totaleSpeseDichiarate :"+ (bd_totaleSpeseDichiarate.signum() == -1));
		logger.info("[FormaFinContrNeve::isNegative] bd_percMaxErogabile :"+ (bd_percMaxErogabile.signum() == -1));

		if (bd_totaleSpeseDichiarate != null && bd_percMaxErogabile != null) {
			if ((bd_totaleSpeseDichiarate.signum() == -1) && (bd_percMaxErogabile.signum() == -1)) {
				return true;
			}
			else {
				return false;
			}
		}

		return false;
	}



	/****************
	 * model validate
	 ***************/

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) arg0;

		List<CommonalityMessage> newMessages = new ArrayList<>();
		Logger logger = Logger.getLogger(info.getLoggerName());
		String prf="[FormaFinContrNeve::modelValidate] ";
		logger.info(prf + " _formaAgevolazione BEGIN");
		
		/** ---------------------------------------------------- Definizione variabili totali per categoria - inizio */
		// Totale importo categoria 97
		String importoRichiestoCat97 = null;
		
		// Totale importo categoria 98
		String tot_importoRichiestoCat98 = null;
		
		// Totale importo categoria 99
		String importoRichiestoCat99 = null;
		/** ---------------------------------------------------- Definizione variabili totali per categoria - fine */
		
		String isSelectedIdTipoIntervento97= "false";
		String isSelectedIdTipoIntervento98= "false";
		String isSelectedIdTipoIntervento99 = "false";

		String ERRMSG_CAMPO_OBBLIGATORIO = "- il campo &#233; obbligatorio";
		String ERRMSG_IMPORTO_FORMATO = "- Solo valori numerici maggiore di 0 con al massimo 2 decimali (Es.: 12345,67)";   
		String ERRMSG_IMPORTO_ASSENTE_CTG97 = "- Per la categoria selezionata non sono state valorizzate voci di spesa. Verifica la sezione Spese!";
		String ERRMSG_IMPORTO_ASSENTE_CTG98 = "- Per la categoria selezionata non sono state valorizzate voci di spesa. Verifica la sezione Spese!";
		String ERRMSG_IMPORTO_ASSENTE_CTG99 = "- Per la categoria selezionata non sono state valorizzate voci di spesa. Verifica la sezione Spese!";
		FormaFinContrNeveVO cultFormaAgevolazioneMap = input._formaAgevolazione; 

		CaratteristicheProgettoNeveNGVO _caratteristicheProgetto = input._caratteristicheProgetto;
		List<TipologiaInterventoNeveVO> tipologiaInterventoList = new ArrayList<TipologiaInterventoNeveVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList()));

		if (_caratteristicheProgetto != null) 
		{
			// Leggo da xml
			List<TipologiaInterventoNeveVO> tipologiaInterventoSalvataList = new ArrayList<TipologiaInterventoNeveVO>(Arrays.asList(_caratteristicheProgetto.getTipologiaInterventoList())); // presa

			logger.info(prf + " tipologiaInterventoSalvataList da xml ");

			if (tipologiaInterventoSalvataList != null) {

				for (int i = 0; i < tipologiaInterventoList.size(); i++) 
				{
					TipologiaInterventoNeveVO curTipologia = tipologiaInterventoList.get(i);

					if (curTipologia != null) {

						for (Object o : tipologiaInterventoSalvataList) 
						{
							TipologiaInterventoNeveVO curTipologiaSalvata = (TipologiaInterventoNeveVO) o;

							if (curTipologiaSalvata != null) {

								if (curTipologia.getIdTipoIntervento().equals(curTipologiaSalvata.getIdTipoIntervento())) {

									String checked = curTipologiaSalvata.getChecked();

									if (StringUtils.isBlank(checked)) {
										checked = "false";
									}

									if( curTipologiaSalvata.getIdTipoIntervento().equals("97") && checked.equals("true")) {
										logger.info(prf + "idTipoInterventoSelezionato: " +curTipologiaSalvata.getIdTipoIntervento()+ " risulta selezionato!");
										isSelectedIdTipoIntervento97= "true";
									}

									if( curTipologiaSalvata.getIdTipoIntervento().equals("98") && checked.equals("true")) {
										logger.info(prf + "idTipoInterventoSelezionato: " +curTipologiaSalvata.getIdTipoIntervento()+ " risulta selezionato!");
										isSelectedIdTipoIntervento98= "true";
									}

									if( curTipologiaSalvata.getIdTipoIntervento().equals("99") && checked.equals("true")) {
										logger.info(prf + "idTipoInterventoSelezionato: " +curTipologiaSalvata.getIdTipoIntervento()+ " risulta selezionato!");
										isSelectedIdTipoIntervento99= "true";
									}

									curTipologia.setChecked(checked);
									logger.debug(prf + "idTipoInterventoSelezionato: " +curTipologia.getIdTipoIntervento()+ " vale: " + checked);
									break;
								}
							}
						}
					}
				} // chiude for esterno
			}
		} // chiude test null _caratteristicheProgetto

		// _formaAgevolazione.importoRichiestoCat98
		if(cultFormaAgevolazioneMap!=null) 
		{
			if (isSelectedIdTipoIntervento97.equals("true")) 
			{
				// importo richiesto: last column
				importoRichiestoCat97 = cultFormaAgevolazioneMap.getImportoRichiestoCat97();
				logger.info(prf + "importoRichiestoCat97: " + importoRichiestoCat97); // 0,00

				if ((importoRichiestoCat97.equals("0") || importoRichiestoCat97.equals("0.00") || importoRichiestoCat97 .equals("0,00")) ) {
					logger.info(prf + "importoRichiestoCat97: " + importoRichiestoCat97);
					addMessage(newMessages, "_formaAgevolazione_importoRichiestoCat97", ERRMSG_IMPORTO_ASSENTE_CTG97);
				}else{
					logger.info(prf + "importoRichiestoCat97 risulta > 0 e vale: " + importoRichiestoCat97);
				}
				
				//il campo "importoRichiesto" e' obbligatorio e numerico positivo
				String str_spesaMaxDichiarabile_97 = (String)cultFormaAgevolazioneMap.getStr_spesaMaxDichiarabile_97(); // 30000,00
				logger.info(prf + "str_spesaMaxDichiarabile_97: " + str_spesaMaxDichiarabile_97);
				
				if (str_spesaMaxDichiarabile_97 != null && (!str_spesaMaxDichiarabile_97.equalsIgnoreCase("Non previsto")))
				{
					if (str_spesaMaxDichiarabile_97.matches("^[0-9]*\\,?[0-9]*$")) {
						logger.info(prf + "Valore contiene solo numeri: " + str_spesaMaxDichiarabile_97);
					} 
					else {
						logger.info(prf + "ATTENZIONE: str_spesaMaxDichiarabile_97: " + str_spesaMaxDichiarabile_97 + " contiene anche stringhe!");
						addMessage(newMessages, "_formaAgevolazione_str_spesaMaxDichiarabile_97", ERRMSG_IMPORTO_FORMATO);
					}

					if (StringUtils.isBlank(str_spesaMaxDichiarabile_97)) 
					{
						logger.info(prf + " campo obbligatorio!"); 
						addMessage( newMessages, "_formaAgevolazione_str_spesaMaxDichiarabile_97", ERRMSG_CAMPO_OBBLIGATORIO);
					}

					if (!str_spesaMaxDichiarabile_97.matches("^\\d+(,\\d{1,2})?$") || Utils.isZero(str_spesaMaxDichiarabile_97)) {
						logger.info(prf + " - il valore deve essere numerico, maggiore di zero e senza decimali");
						addMessage(newMessages,	"_formaAgevolazione_str_spesaMaxDichiarabile_97", ERRMSG_IMPORTO_FORMATO);
					}
				}else{
					logger.info(prf + " str_spesaMaxDichiarabile_97 valore null o Non previsto! ");
				}
			}

			if (isSelectedIdTipoIntervento98.equals("true")) 
			{
				if (cultFormaAgevolazioneMap.strTotCat98 != null) 
				{
					if (cultFormaAgevolazioneMap.strTotCat98.matches("^[0-9]*\\,?[0-9]*$")) 
					{
						tot_importoRichiestoCat98 = cultFormaAgevolazioneMap.strTotCat98;
						logger.info(prf + "importoRichiestoCat98: " + tot_importoRichiestoCat98);

						if ((tot_importoRichiestoCat98.equals("0") || tot_importoRichiestoCat98.equals("0.00") || tot_importoRichiestoCat98.equals("0,00"))) 
						{
							logger.info(prf + "tot_importoRichiestoCat98: " + tot_importoRichiestoCat98);
							addMessage(newMessages, "_formaAgevolazione_importoRichiestoCat98", ERRMSG_IMPORTO_ASSENTE_CTG98); 
							logger.info(prf + "tot_importoRichiestoCat98: " + ERRMSG_IMPORTO_ASSENTE_CTG98);
						} else {
							logger.info(prf + "Nessun errore");
						}
					} else {
						logger.info(prf + "ATTENZIONE: importoRichiestoCat98: " + tot_importoRichiestoCat98 + " contiene anche stringhe!");
						addMessage(newMessages, "_formaAgevolazione_strTotCat98", ERRMSG_IMPORTO_FORMATO);
					}
				}else{
					logger.info(prf + " valore null!");
				}
				
				String str_spesaMaxDichiarabile_98 = (String)cultFormaAgevolazioneMap.getStr_spesaMaxDichiarabile_98(); // 30000,00
				logger.info(prf + "str_spesaMaxDichiarabile_98: " + str_spesaMaxDichiarabile_98);

				if (str_spesaMaxDichiarabile_98 != null) 
				{
					if (str_spesaMaxDichiarabile_98.matches("^[0-9]*\\,?[0-9]*$")) {
						logger.info(prf + "Valore contiene solo numeri: " + str_spesaMaxDichiarabile_98);
					} 
					else {
						logger.info(prf + "ATTENZIONE: str_spesaMaxDichiarabile_98: " + str_spesaMaxDichiarabile_98 + " contiene anche stringhe!");
						addMessage(newMessages, "_formaAgevolazione_str_spesaMaxDichiarabile_98", ERRMSG_IMPORTO_FORMATO);
					}

					if (StringUtils.isBlank(str_spesaMaxDichiarabile_98)) 
					{
						logger.info(prf + " campo obbligatorio!"); 
						addMessage( newMessages, "_formaAgevolazione_str_spesaMaxDichiarabile_98", ERRMSG_CAMPO_OBBLIGATORIO);
					}

					if (!str_spesaMaxDichiarabile_98.matches("^\\d+(,\\d{1,2})?$") || Utils.isZero(str_spesaMaxDichiarabile_98)) {
						logger.info(prf + " - il valore deve essere numerico, maggiore di zero e senza decimali");
						addMessage(newMessages,	"_formaAgevolazione_str_spesaMaxDichiarabile_98", ERRMSG_IMPORTO_FORMATO);
					}
				}else{
					logger.info(prf + " str_spesaMaxDichiarabile_98 valore null! ");
				}
			}

			if (isSelectedIdTipoIntervento99.equals("true")) 
			{
				importoRichiestoCat99 = cultFormaAgevolazioneMap.totCat99;
				logger.info("importoRichiestoCat99: " + importoRichiestoCat99);

				if (importoRichiestoCat99 != null) {
					if ((importoRichiestoCat99.equals("0") || importoRichiestoCat99.equals("0.00") || importoRichiestoCat99.equals("0,00"))	&& isSelectedIdTipoIntervento99.equals("true")) {
						logger.info(prf + "importoRichiestoCat99: " + importoRichiestoCat99);
						addMessage(newMessages,	"_formaAgevolazione_importoRichiestoCat99",	ERRMSG_IMPORTO_ASSENTE_CTG99);
					}
					
				} else {
					logger.info(prf + "importoRichiestoCat99: null");
					addMessage(newMessages,	"_formaAgevolazione_importoRichiestoCat99",	ERRMSG_IMPORTO_ASSENTE_CTG99);
				}
			}

		}else{
			logger.info(prf + " _formaAgevolazione non presente o vuoto");
		}

		logger.info(prf + " END");
		return newMessages;
	}
}
