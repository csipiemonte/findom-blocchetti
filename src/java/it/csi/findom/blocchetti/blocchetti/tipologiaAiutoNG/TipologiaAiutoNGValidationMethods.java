/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.tipologiaAiutoNG;

import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.util.SegnalazioneErrore;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.CaratteristicheProgettoNGVO;
import it.csi.findom.blocchetti.common.vo.caratteristicheProgetto.TipologiaInterventoVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.DettaglioAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGItemListVO;
import it.csi.findom.blocchetti.common.vo.tipologiaAiutoNG.TipologiaAiutoNGVO;
import it.csi.findom.blocchetti.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * metodi static di validazione invocati via reflection se presenti in variabile di configurazione
 * @author mauro.bottero
 *
 */
public class TipologiaAiutoNGValidationMethods implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Serve per i bandi che prevedono dei vincoli tra dettaglio tipologie di agevolazione e tipologie intervento. 
	 * In particolare controlla che se esistono dei dettagli tipologia di agevolazione selezionati, questi siano compatibili 
	 * con le tipologie intervento selezionate in 'Informazioni sul progetto'
	 * @param input
	 * @param logger
	 * @param obj una Map<String,ArrayList<String>> in cui la chiave String e' un idDettaglioTipologiaAgevolazione e l'arrayList e' l'elenco delle tipologie
	 *            intervento che sono compatibili con tale dettaglio 
	 * @return
	 */
	public static ArrayList<SegnalazioneErrore> ctrlCoerenzaDettAiutoTipologiaIntervento(TipologiaAiutoNGInput input, Logger logger, Object obj){
		logger.info("[TipologiaAiutoNGValidationMethods::ctrlCoerenzaDettAiutoTipologiaIntervento] BEGIN");
		ArrayList<SegnalazioneErrore> result = null;
		if(input==null ||obj==null) {
			return result;
		}
		@SuppressWarnings("unchecked")  //la correttezza dei tipi e' garantita dal corretto popolamento delll'attributo objArg dell'oggetto MethodCall
		Map<String,ArrayList<String>> objMap = (Map<String,ArrayList<String>>) obj;

		CaratteristicheProgettoNGVO caratteristicheProgettoNGVO = input.caratteristicheProgettoNGVO;		
		if(caratteristicheProgettoNGVO==null || caratteristicheProgettoNGVO.getTipologiaInterventoList()==null || caratteristicheProgettoNGVO.getTipologiaInterventoList().length==0){
			return result; //non dovrebbe mai accadere perche' le informazioni sul progetto dovrebbero essere gia' state salvate
		}
		TipologiaInterventoVO[] tipologiaInterventoVOList = caratteristicheProgettoNGVO.getTipologiaInterventoList();
		
		//per fare la corretta sostituzione al segnaposto nel messaggio di errore
		String sost = "quanto selezionato";
		List<String> interventiselez = MetodiUtili.getIdTipoIntSelezionatiList(caratteristicheProgettoNGVO, logger);
		if(interventiselez!=null && !interventiselez.isEmpty()){
			sost = (interventiselez.size() == 1)? "la tipologia intervento selezionata" : "le tipologie intervento selezionate"; 			
		}
		String ERRMSG_DETT_TIPOLOGIA_NON_AMMESSO = String.format("- il dettaglio tipologia di agevolazione non è compatibile con %s nella pagina 'Informazioni sul progetto'", sost);
		
		
		TipologiaAiutoNGVO tipologiaAiutoNGVO = input._tipologiaAiuto;
		if (tipologiaAiutoNGVO!=null && tipologiaAiutoNGVO.getTipologiaAiutoList()!= null){
			List<TipologiaAiutoNGItemListVO> tipologiaAiutoNGItemListVO = new ArrayList<TipologiaAiutoNGItemListVO>(Arrays.asList(tipologiaAiutoNGVO.getTipologiaAiutoList()));
			for(int i=0; i<tipologiaAiutoNGItemListVO.size();i++){
				TipologiaAiutoNGItemListVO curTipologiaAiuto= tipologiaAiutoNGItemListVO.get(i); 
				if(curTipologiaAiuto!=null){
					String curChecked = (String) curTipologiaAiuto.getChecked();
					if(StringUtils.isNotBlank(curChecked) && curChecked.equals("true")){	
						
						if(curTipologiaAiuto.getDettaglioAiutoList()!=null && curTipologiaAiuto.getDettaglioAiutoList().length>0){
							List<DettaglioAiutoNGItemListVO> dettaglioAiutoNGItemListVO = new ArrayList<DettaglioAiutoNGItemListVO>(Arrays.asList(curTipologiaAiuto.getDettaglioAiutoList()));
							for (DettaglioAiutoNGItemListVO curDettaglioAiutoNGItemListVO : dettaglioAiutoNGItemListVO) {
								String curDettChecked = curDettaglioAiutoNGItemListVO.getChecked();
								if(StringUtils.isNotBlank(curDettChecked) && curDettChecked.equals("true")){
									String curIdDettAiuto = curDettaglioAiutoNGItemListVO.getIdDettAiuto();
									//verifico se per il dettaglio corrente esiste un vincolo, ovvero se il dettaglio aiuto e' selezionabile solo se 
									//sono state selezionate certe tipologie intervento in 'Informazioni sul progetto'
									if(objMap.containsKey(curIdDettAiuto)){
										ArrayList<String> idTipologieInterventoCompatibiliList = objMap.get(curIdDettAiuto);
										if (idTipologieInterventoCompatibiliList!=null && !idTipologieInterventoCompatibiliList.isEmpty()){
											boolean interventoCompatibileSelezionato = false;
											for (String idInterventoCompatibile : idTipologieInterventoCompatibiliList) {												
												interventoCompatibileSelezionato = MetodiUtili.controllaSelezioneTipologiaIntervento(tipologiaInterventoVOList, idInterventoCompatibile);
												if(interventoCompatibileSelezionato){
													break;
											    }
											}
											if(!interventoCompatibileSelezionato){
												//nessuna tipologia intervento compatibile con il dettaglio aiuto corrente e' stata selezionata, quindi segnalo errore
												if(result==null){
													result = new ArrayList<>();
												}
												SegnalazioneErrore segnalazione1 = new SegnalazioneErrore();									
												segnalazione1.setCampoErrore("_dettAiuto_valore_testo");
												segnalazione1.setMsgErrore(ERRMSG_DETT_TIPOLOGIA_NON_AMMESSO);
										        result.add(segnalazione1);
										        SegnalazioneErrore segnalazione2 = new SegnalazioneErrore();									
										        segnalazione2.setCampoErrore("_dettAiuto_valore_idDettAiuto");
										        segnalazione2.setMsgErrore(curIdDettAiuto);
										        result.add(segnalazione2);										       
											}											
										}
									}									
								}								
							}
						}						
					}	 
				}
			}
		}
		logger.info("[TipologiaAiutoNGValidationMethods::ctrlCoerenzaDettAiutoTipologiaIntervento] END");
		return result;
	}
}
