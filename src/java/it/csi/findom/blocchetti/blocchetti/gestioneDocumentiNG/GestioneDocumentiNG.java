/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.gestioneDocumentiNG;

import it.csi.findom.blocchetti.common.dao.DocumentazioneNGDAO;
import it.csi.findom.blocchetti.common.util.MetodiUtili;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiItemVO;
import it.csi.findom.blocchetti.common.vo.allegati.AllegatiVO;
import it.csi.findom.blocchetti.common.vo.allegati.DocumentoVO;
import it.csi.findom.blocchetti.common.vo.documentazione.DocumentazioneItemVO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoIntegrativoVO;
import it.csi.findom.blocchetti.common.vo.documentazione.TipologiaAllegatoVO;
import it.csi.findom.blocchetti.commonality.FinCommonInfo;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonInfo;
import it.csi.melograno.aggregatore.business.javaengine.commonality.Commonality;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityInput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityMessage;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityOutput;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;

import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class GestioneDocumentiNG extends Commonality {

	GestioneDocumentiInput input = new GestioneDocumentiInput();
	
	@Override
	public List<CommonalityMessage> commandValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {
		return null;
	}

	@Override
	public CommonalityInput getInput() throws CommonalityException {
		return input;
	}

	@Override
	public CommonalityOutput inject(CommonInfo infol, List<CommonalityMessage> arg1) throws CommonalityException {

		FinCommonInfo info = (FinCommonInfo) infol;
		GestioneDocumentiOutput output = new GestioneDocumentiOutput();
		
		Logger logger = Logger.getLogger(info.getLoggerName());	    		
		logger.info("[GestioneDocumentiNG::inject] GestioneDocumentiNG  BEGIN");
		
		// lista docuementi allegati
		AllegatiItemVO[] allegatiList = new AllegatiItemVO[0];

		// lista di tutti i documenti abbligatori
		List<TipologiaAllegatoVO> documentiObbligatoriList = new ArrayList<>();
		
		// : Gestione Jira: 1317 : Allegati integrativi
		// lista di tutti i documenti abbligatori integrativi
		List<TipologiaAllegatoIntegrativoVO> documentiObbligatoriIntegrativiList = new ArrayList<>();

		//Lista allegati obbligatori legati alla tipologia intervento
		List<TipologiaAllegatoVO> elencoAllegatiObbligatoriTipolIntervento = null;

		// stringa contenente gli Id_tipologia di tutti i doc differebili per questo bando
		String stringaIdTipolDifferibili = "";

		// lista dei documenti obbligatori MENO quelli gia inseriti
		List<TipologiaAllegatoVO> documentiObblMancantiList = new ArrayList<>();

		// lista con tutti i documenti caricabili dall'utente (obbligatori e non)
		List<DocumentazioneItemVO> documentiList = new ArrayList<>();

		// mappa degli errori di Index
		ErrorsIndexVO errorsIndexMap = new ErrorsIndexVO();

		// indico l'esistenza di un errore e su quale elemento e' avvenuto
		String errorOnElement = null;

		// messaggio operazione OK 
		String operIndexOk = null;
		
		/** jira  @gf bonus cultura 2020 - */
		String msg_allegati_opzionali_per_bando = "false";

		//// valorizzazione
		if (info.getCurrentPage() != null) {
			
			// recupero idBando
			Integer idBando = info.getStatusInfo().getTemplateId();
			logger.info("[GestioneDocumentiNG::inject] idBando= " + idBando );
			
			if ("true".equals(input.msgDocumentiOpzionaliPerBando)) 
			{
				if(idBando==96){
					msg_allegati_opzionali_per_bando = "true";
					logger.info("[GestioneDocumentiNG::inject] visualizza msg upload allegati opzionali per bonus cultura 2020= " + msg_allegati_opzionali_per_bando );
				}
			}

			//////////////////////////////////////
			// parametri messi in sessione dalla ACTION che gestisce INDEX, NON modificare questi valori
			String INDEX_ERROR = "index_error";
			String INDEX_ACTION_ERROR = "index_action_error"; //
			String INDEX_OK = "index_ok";
			String INDEX_ERROR_READ_MSG = "read_error";
			String INDEX_ERROR_PARAM_MSG = "param_error";
			String INDEX_ERROR_DELETE_MSG = "delete_error";
			String INDEX_ERROR_INSERT_MSG = "insert_error";
			String INDEX_ERROR_INSERT_EXSITING_MSG = "insert_exsisting_error";
			String INDEX_OK_INSERT_MSG = "insert_ok";
			String INDEX_OK_DELETE_MSG = "delete_ok";
			String ERROR_ESTENSIONE_FILE = "error_estensione_file";
			
			String UPLD_ERROR_READ_FILE_MSG = "Lettura non riuscita";
			String UPLD_ERROR_PARAM_MSG = "Parametri non validi."; //Parametri non validi OPPURE file non trovato
			String UPLD_ERROR_PARAM_NULL_MSG = "Non é stata selezionata la Tipologia di documento."; //Parametri non validi OPPURE file non trovato
			String UPLD_ERROR_DELETE_FILE_MSG = "Cancellazione non riuscita";
			String UPLD_ERROR_INSERT_FILE_MSG = "Inserimento non riuscito";
			String UPLD_ERROR_INSERT_FILE_NULL_MSG = "Non é stato selezionato alcun file.";
			String UPLD_ERROR_INSERT_EXSISTING_FILE_MSG = "Inserimento non riuscito; impossibile caricare due volte un file con lo stesso nome.";
			String UPLD_ACTION_ERROR_MSG = "Evidenziato un problema in fase di upload."; 
			String UPLD_ACTION_ERROR_MSG_EXCEED = "La dimensione del file é superiore alla soglia massima di 5Mb.";
			String UPLD_OK_MSG = "Operazione riuscita con successo";
			String UPLD_ERROR_ESTENSIONE_FILE_MSG = "Estensione file non consentita";

			String indexError = (String)SessionCache.getInstance().get(INDEX_ERROR);
			logger.info("[GestioneDocumentiNG::inject] >>>>> indexError="+indexError );
			if(!StringUtils.isBlank(indexError)){
				SessionCache.getInstance().set(INDEX_ERROR,null);
				logger.info("[GestioneDocumentiNG::inject] >>>>> rimosso indexError" );

				if(StringUtils.equals("read_error",indexError)){
					errorsIndexMap.allegati_read= UPLD_ERROR_READ_FILE_MSG;
				}
				if(StringUtils.equals("param_error",indexError)){
					errorsIndexMap.allegati= UPLD_ERROR_PARAM_MSG;
				}
				if(StringUtils.equals("param_null_error",indexError)){
					errorsIndexMap.allegati= UPLD_ERROR_PARAM_NULL_MSG;
				}				
				if(StringUtils.equals("delete_error",indexError)){
					errorsIndexMap.allegati_delete= UPLD_ERROR_DELETE_FILE_MSG;
				}
				if(StringUtils.equals("insert_error",indexError)){
					errorsIndexMap.allegati= UPLD_ERROR_INSERT_FILE_MSG;
				}
				if(StringUtils.equals("insert_error_file_null",indexError)){
					errorsIndexMap.allegati= UPLD_ERROR_INSERT_FILE_NULL_MSG;
				}				
				if(StringUtils.equals("insert_exsisting_error",indexError)){
					// tentativo di inserire un file gia presente su quel nodo di Index
					errorsIndexMap.allegati= UPLD_ERROR_INSERT_EXSISTING_FILE_MSG;
				}
				if(StringUtils.equals("error_estensione_file",indexError)){
					// tentativo di inserire un file con estensione non corretta
					errorsIndexMap.allegati= UPLD_ERROR_ESTENSIONE_FILE_MSG;
				}
			}

			String indexOK = (String)SessionCache.getInstance().get(INDEX_OK);
			logger.info("[GestioneDocumentiNG::inject] >>>>> indexOK="+indexOK );
			if(!StringUtils.isBlank(indexOK)){
				SessionCache.getInstance().set(INDEX_OK,null);
				logger.info("[GestioneDocumentiNG::inject] >>>>> rimosso indexOK" );
				operIndexOk = UPLD_OK_MSG;
			}
			
			// Possibili Errori generati dall'interceptor di Struts2
			// struts.messages.error.uploading = A general error that occurs when the file could not be uploaded
			// struts.messages.error.file.too.large = Occurs when the uploaded file is too large as specified by maximumSize.
			// struts.messages.error.content.type.not.allowed = Occurs when the uploaded file does not match the expected content types specified
			// struts.messages.error.file.extension.not.allowed = Occurs when uploaded file has disallowed extension
			// struts.messages.upload.error.SizeLimitExceededException = Occurs when the upload request (as a whole) exceed configured struts.multipart.maxSize
			// struts.messages.upload.error.<Exception class SimpleName> = Occurs when any other exception took place during file upload process
			String indexActionError = (String)SessionCache.getInstance().get(INDEX_ACTION_ERROR);
			logger.info("[GestioneDocumentiNG::inject] >>>>> indexActionError="+indexActionError );
			if(!StringUtils.isBlank(indexActionError)){
				SessionCache.getInstance().set(INDEX_ACTION_ERROR,null);
				logger.info("[GestioneDocumentiNG::inject] >>>>> rimosso indexActionError" );
				if(indexActionError.contains("exceed")){
					errorsIndexMap.allegati=UPLD_ACTION_ERROR_MSG_EXCEED;
				}else{
					errorsIndexMap.allegati=UPLD_ACTION_ERROR_MSG;
				}
			}
			
			// fine GESTIONE DEI  MESSAGGI

			if ( !info.getFormState().equals("IN") && !info.getFormState().equals("CO") && !info.getFormState().equals("NV")) {
			
				errorOnElement = input.deletedoc;
				if(errorOnElement==null){
					errorOnElement = input.viewedoc;
				}
				logger.info("[GestioneDocumentiNG::inject] >>>>> errorOnElement= "+errorOnElement);
				
				// recupero i documenti salvati nell'XML
				AllegatiVO allegatiMap = input.allegati;
				if (allegatiMap != null) {
					logger.info("[GestioneDocumentiNG::inject] allegatiMap NOT NULL");
					allegatiList = allegatiMap.getAllegatiList();
					if(allegatiList != null){
						logger.info("[GestioneDocumentiNG::inject] allegatiList.size:" + allegatiList.length);
					}else{
						logger.info("[GestioneDocumentiNG::inject] allegatiList NULL");
					}
				} 
				
				/* ----------------------------------------------------------------------------------------------------------------------------------------- */
				//  : step1: Jira 1317:  - inizio : Gestione visualizzazione e caricamento documenti in upload... - start modifica -
				/* ----------------------------------------------------------------------------------------------------------------------------------------- */
				Integer idDomanda = info.getStatusInfo().getNumProposta();
				Integer idSportello = info.getStatusInfo().getNumSportello();

				//Lettura dati presenti in sessione e su db legati alla domanda
				Integer idSportelloBando = info.getStatusInfo().getNumSportello();
				List<Integer> idTipolIntervento = MetodiUtili.getTipologiaIntervento(input._caratteristicheProgetto != null ? input._caratteristicheProgetto.getTipologiaInterventoList() : null, logger);
				Integer idTipolBeneficiario = MetodiUtili.getTipologiaBeneficiario(idDomanda, logger);

				// : interrogo nuova tabella:
				// Verifico se ci sono allegati integrativi per il nuovo beneficiario
//				List<TipologiaAllegatoIntegrativoVO> elencoAllegatiIntegrativi = new ArrayList<>();
				boolean isAllegatiIntegrativiPresenti = false;
				int numeroAllegatiIntegrativi = 0;
				numeroAllegatiIntegrativi = MetodiUtili.isPresenteAllegatoSupplementare(idSportello, idDomanda, logger);
				// int numeroAllegatiIntegrativi = DocumentazioneNGDAO.getNumeroAllegatiSupplementari(idSportello, idDomanda, logger);
				
				if(numeroAllegatiIntegrativi > 0)
				{
					isAllegatiIntegrativiPresenti = true;
					logger.info("test: Sono presenti: " + numeroAllegatiIntegrativi + " allegati supplementari!!!");
					// recupero la lista di documenti supplementari
					documentiObbligatoriList = MetodiUtili.getElencoDocSupplByIdSportello(idBando, idSportello, idDomanda, isAllegatiIntegrativiPresenti, logger);
					// documentiObbligatoriList = DocumentazioneNGDAO.getTipologiaAllegatoList(idBando, idSportello, idDomanda, isAllegatiIntegrativiPresenti, logger);
					
					if(documentiObbligatoriList != null){
						logger.info("Presente allegato supplementare!");
					}else{
						logger.debug("NON risulta presente allegato supplementare!");
					}
					
				}else{
					logger.info("test: NON sono presenti allegati integrativi!!!");
					documentiObbligatoriList = DocumentazioneNGDAO.getTipologiaAllegatoList(info.getStatusInfo().getTemplateId(), null, null, true, logger);
				}

				//JIRA FINDOM-2068 1561 ven 18/09/2020
				//Vengono aggiunti all'elenco allegati principale, gli allegati obbligatori tenendo conto della tipologia intervento selezionata dall'utente
				elencoAllegatiObbligatoriTipolIntervento = DocumentazioneNGDAO.getTipologiaAllegatoTipolInterventoList(idBando, idSportelloBando, idDomanda, idTipolBeneficiario, idTipolIntervento, logger);
				if (documentiObbligatoriList != null && elencoAllegatiObbligatoriTipolIntervento != null && !elencoAllegatiObbligatoriTipolIntervento.isEmpty())
				{
					documentiObbligatoriList.addAll(elencoAllegatiObbligatoriTipolIntervento);
				}
				
				if(documentiObbligatoriList!=null){
					logger.info("[GestioneDocumentiNG::inject] Dimensione elenco documenti obbligatori: " + documentiObbligatoriList.size());
				}
				/* ------------------------------------------------------- fine --------------------------------------------------------------------- */
				
				// popolo la lista di tutti i documenti
				// List<TipologiaAllegatoVO> documentiListTmp = DocumentazioneNGDAO.getTipologiaAllegatoList(info.getStatusInfo().getTemplateId(), false, logger);
				List<TipologiaAllegatoVO> documentiListTmp = DocumentazioneNGDAO.getTipologiaAllegatoList(info.getStatusInfo().getTemplateId(), null, null, false, logger); // : x test
				
				if(documentiListTmp!=null){
					logger.info("[GestioneDocumentiNG::inject] documentiListTmp.size:" + documentiListTmp.size());
					// sostituisco eventuali apici singoli con \'
					for (int i = 0, n = documentiListTmp.size(); i < n; i++) {
						TipologiaAllegatoVO documento = documentiListTmp.get(i);
						DocumentazioneItemVO docTmp = new DocumentazioneItemVO();
						docTmp.setIdallegato(Integer.toString(documento.getIdallegato()));
						docTmp.setDescrizione(documento.getDescrizione().replace("'","\\'"));
						docTmp.setObbligatorio(documento.getFlag_obbl());
						docTmp.setDifferibile(documento.getDifferibile());
						documentiList.add(docTmp);
						
						// genero una stringa con gli Id_tipologia dei doc differibili
						String differibile = documentiListTmp.get(i).getDifferibile();
						String idTipologiaDocDiff = documentiListTmp.get(i).getIdallegato()+"";
						logger.info("[GestioneDocumentiNG::inject] idTipologiaDocDiff-differibile=" + idTipologiaDocDiff + "-" +differibile);
						if(StringUtils.equals("S",differibile)){
							stringaIdTipolDifferibili = stringaIdTipolDifferibili + "[" + idTipologiaDocDiff + "]";
						}
					}
				}else{
					logger.info("[GestioneDocumentiNG::inject] documentiListTmp.size: NULL" );
				}
				logger.info("[GestioneDocumentiNG::inject] stringaIdTipolDifferibili=" + stringaIdTipolDifferibili);
				
				if(!documentiObbligatoriList.isEmpty()){

					if(allegatiList == null){
						logger.info("[GestioneDocumentiNG::inject] allegatiList NULL");
						documentiObblMancantiList = documentiObbligatoriList;
						logger.debug("[GestioneDocumentiNG::inject] documentiObblMancantiList=" + documentiObblMancantiList);

					} else {
						logger.info("[GestioneDocumentiNG::inject] allegatiList.size()=" + allegatiList.length);
						// riempio la lista documentiObblMancantiList con i soli doc obbligatori mancanti
						// riempio una lista con tutti i doc abbligatori MENO quelli gia' inseriti
					
						logger.info("[GestioneDocumentiNG::inject] documentiObbligatoriList=" + documentiObbligatoriList.size());
						logger.info("[GestioneDocumentiNG::inject] allegatiList=" + allegatiList.length);

						for (int j = 0, n = documentiObbligatoriList.size(); j < n; j++) {

							String idTipologiaDocObbl = documentiObbligatoriList.get(j).getIdallegato()+"";
							logger.info("[GestioneDocumentiNG::inject] idTipologiaDocObbl=" + idTipologiaDocObbl);
							
							boolean docObblPresente = false;
							for (int k = 0, m = allegatiList.length; k < m; k++) {
								AllegatiItemVO docum = allegatiList[k];
								DocumentoVO dcs =docum.getDocumento();
								
								String idTipologiaXML = dcs.getIdTipologia();
								logger.info("[GestioneDocumentiNG::inject] idTipologia=" + idTipologiaXML);
								
								// : ( da cancellare ) x test nomeFile - 
								String nomeFile = dcs.getNomeFile();
								logger.info("[GestioneDocumentiNG::inject] nomeFile=" + nomeFile); // *** da cancellare dopo il test
								
								if(StringUtils.equals(idTipologiaDocObbl,idTipologiaXML)){
									logger.info("[GestioneDocumentiNG::inject] FOUNDED doc obbligatorio nel XML");
									docObblPresente = true;
								}
							}
							logger.info("[GestioneDocumentiNG::inject] docObblPresente=" + docObblPresente);
							if(!docObblPresente){
								documentiObblMancantiList.add(documentiObbligatoriList.get(j));
							}
						}
						logger.info("[GestioneDocumentiNG::inject] documentiObblMancantiList=" + documentiObblMancantiList.size());
					}
				}

			} else if ( info.getFormState().equals("IN") || info.getFormState().equals("CO") || info.getFormState().equals("NV")) {
				logger.info("[GestioneDocumentiNG::inject] sola lettura");
				
				// popolo la lista di tutti i documenti
				List<TipologiaAllegatoVO> documentiListTmp =  DocumentazioneNGDAO.getTipologiaAllegatoList(info.getStatusInfo().getTemplateId(), null, null, false, logger);
				if(documentiListTmp!=null){
					logger.info("[GestioneDocumentiNG::inject] documentiListTmp.size:" + documentiListTmp.size());
					// sostituisco eventuali apici singoli con \'
					for (int i = 0, n = documentiListTmp.size(); i < n; i++) {

						// genero una stringa con gli Id_tipologia dei doc differibili
						String differibile = documentiListTmp.get(i).getDifferibile();
						String idTipologiaDocDiff = documentiListTmp.get(i).getIdallegato()+"";
						logger.info("[GestioneDocumentiNG::inject] idTipologiaDocDiff-differibile=" + idTipologiaDocDiff + "-" +differibile);
						if(StringUtils.equals("S",differibile)){
							stringaIdTipolDifferibili = stringaIdTipolDifferibili + "[" + idTipologiaDocDiff + "]";
						}
					}
				}else{
					logger.info("[GestioneDocumentiNG::inject] documentiListTmp.size: NULL" );
				}
				
				AllegatiVO allegatiMap = input.allegati;
				if (allegatiMap != null) {
					logger.info("[GestioneDocumentiNG::inject] allegatiMap NOT NULL");
					allegatiList = allegatiMap.getAllegatiList();
					logger.info("[GestioneDocumentiNG::inject] allegatiList.size:" + allegatiList!=null?allegatiList.length:0);
				} 
			}
		}
		
		//// namespace
		output.allegatiList=allegatiList;
		output.documentiObblMancantiList=documentiObblMancantiList.toArray(new TipologiaAllegatoVO[0]);
		output.documentiList=documentiList.toArray(new DocumentazioneItemVO[0]);
		output.errorsIndexVO= errorsIndexMap;
		output.errorOnElement=errorOnElement;
		output.operIndexOk=operIndexOk;
		output.stringaIdTipolDifferibili=stringaIdTipolDifferibili;
		output.setUrlContextSportello(info.getStatusInfo().getContextSportello());
		
		/*** bonus cultura 2020 */
		if ("true".equals(input.msgDocumentiOpzionaliPerBando)) {
			output.setMsg_allegati_opzionali_per_bando(msg_allegati_opzionali_per_bando);
		}
		
		logger.info("[GestioneDocumentiNG::inject] UrlContextSportello = " + info.getStatusInfo().getContextSportello());
		logger.info("[GestioneDocumentiNG::inject] _gestioneDocumenti END");

		return output;
	}

	@Override
	public List<CommonalityMessage> modelValidate(CommonInfo arg0, List<CommonalityMessage> arg1)
			throws CommonalityException {
		// No validazione!
		return null;
	}

}
