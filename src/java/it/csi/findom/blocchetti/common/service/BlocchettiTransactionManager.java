/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.service;

import java.io.Serializable;

import it.csi.findom.blocchetti.common.dao.DipartimentoDAO;
import it.csi.findom.blocchetti.common.dao.EnteDAO;
import it.csi.findom.blocchetti.common.dao.PartnerDAO;
import it.csi.findom.blocchetti.common.vo.dipartimento.DipartimentoVO;
import it.csi.findom.blocchetti.common.vo.partner.PartnerItemVO;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.melograno.aggregatore.business.javaengine.commonality.SessionCache;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
/**
 * i metodi di questa classe se annotati @Transactional diventano transazionali (limitatamente alle modifiche fatte 
 * su tavole del DB, non all'xml della domanda) 
 * @author mauro.bottero
 *
 */
public class BlocchettiTransactionManager implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public BlocchettiTransactionManager(){
		
	}
    
	/**
	 * 
	 * @param partnerItemVO
	 * @param idCapofilaAcronimoCorrente
	 * @param logger
	 * @throws CommonalityException
	 */
	@Transactional
	public void inserisciPartner(PartnerItemVO partnerItemVO, String idCapofilaAcronimoCorrente, Logger logger) throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::inserisciPartner] BEGIN ");
		String idPartnerIns = PartnerDAO.insertShellTPartner(partnerItemVO, logger);
		if(idPartnerIns!=null){			
			//l'id ottenuto da insert viene messo in sessione per aggiornare, nell'inject del chiamante, il model
			SessionCache.getInstance().set("idPartnerIns",idPartnerIns);
			PartnerDAO.insertShellRCapofilaAcronimoPartner(idCapofilaAcronimoCorrente, idPartnerIns, logger);
		}
		//eventuale aggiornamento delle unita organizzative 
		if(StringUtils.isNotBlank(partnerItemVO.getCodiceDipartimento())){
			String codiceFiscaleBeneficiario = partnerItemVO.getCodiceFiscale();
			String denominazioneBeneficiario = partnerItemVO.getDenominazione(); 
			String idUnitaOrganizzativa = "0";  //passo 0 anche se fossi nel caso in cui IPA e' giu' e le uo le ho prese dal DB e quindi avrei l'id; non e' ottimizzato, ma l'effetto e' uguale
			String codiceUnitaOrganizzativa = partnerItemVO.getCodiceDipartimento();
			String descrizioneUnitaOrganizzativa = partnerItemVO.getDescrDipartimento();
		    aggiornaUnitaOrganizzative(codiceFiscaleBeneficiario, denominazioneBeneficiario, 
                                       idUnitaOrganizzativa,  codiceUnitaOrganizzativa,
                                       descrizioneUnitaOrganizzativa, logger);
		}
		//eventuale aggiornamento della denominazione del soggetto avente il codice fiscale partnerItemVO.codiceFiscale
		aggiornaDenominazioneSoggetto(partnerItemVO.getCodiceFiscale(), partnerItemVO.getDenominazione(), logger);
		logger.info("[BlocchettiTransactionManager::inserisciPartner] END ");
	}
	
	
	@Transactional
	public void aggiornaPartner(PartnerItemVO partnerItemVO, Logger logger) throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::aggiornaPartner] BEGIN ");
		PartnerDAO.updateShellTPartner(partnerItemVO, logger);
		if(StringUtils.isNotBlank(partnerItemVO.getCodiceDipartimento())){
			String codiceFiscaleBeneficiario = partnerItemVO.getCodiceFiscale();
			String denominazioneBeneficiario = partnerItemVO.getDenominazione(); 
			String idUnitaOrganizzativa = "0";  //passo 0 anche se fossi nel caso in cui IPA e' giu' e le uo le ho prese dal DB e quindi avrei l'id; non e' ottimizzato, ma l'effetto e' uguale
			String codiceUnitaOrganizzativa = partnerItemVO.getCodiceDipartimento();
			String descrizioneUnitaOrganizzativa = partnerItemVO.getDescrDipartimento();
			aggiornaUnitaOrganizzative(codiceFiscaleBeneficiario, denominazioneBeneficiario, 
					idUnitaOrganizzativa,  codiceUnitaOrganizzativa,
					descrizioneUnitaOrganizzativa, logger);
		}
		//eventuale aggiornamento della denominazione del soggetto avente il codice fiscale partnerItemVO.codiceFiscale
		aggiornaDenominazioneSoggetto(partnerItemVO.getCodiceFiscale(), partnerItemVO.getDenominazione(), logger);
		logger.info("[BlocchettiTransactionManager::aggiornaPartner] END ");
	}
	
	/**
	 * 
	 * @param idCapofilaAcronimo
	 * @param idPartner
	 * @param codiceFiscaleBeneficiario
	 * @param denominazioneBeneficiario
	 * @param idUnitaOrganizzativa
	 * @param codiceUnitaOrganizzativa
	 * @param descrizioneUnitaOrganizzativa
	 * @param logger
	 * @throws CommonalityException
	 */
	@Transactional
	public void insertShellRCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner,
			                                        String codiceFiscaleBeneficiario, String denominazioneBeneficiario, 
			                                        String idUnitaOrganizzativa, String codiceUnitaOrganizzativa,
		                   	                        String descrizioneUnitaOrganizzativa,
			                                        Logger logger)throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::insertShellRCapofilaAcronimoPartner] BEGIN ");
		//inserimento su tabella di relazione tra capofila acronimo e partner 
		PartnerDAO.insertShellRCapofilaAcronimoPartner(idCapofilaAcronimo,idPartner, logger);
		//eventuale aggiornamento delle unita organizzative
		if(StringUtils.isNotBlank(codiceUnitaOrganizzativa)){
			aggiornaUnitaOrganizzative(codiceFiscaleBeneficiario, denominazioneBeneficiario, 
										idUnitaOrganizzativa,  codiceUnitaOrganizzativa,
										descrizioneUnitaOrganizzativa, logger);
		}
		//eventuale aggiornamento della denominazione del soggetto avente il codice fiscale partnerItemVO.codiceFiscale
		aggiornaDenominazioneSoggetto(codiceFiscaleBeneficiario, denominazioneBeneficiario, logger);
		logger.info("[BlocchettiTransactionManager::insertShellRCapofilaAcronimoPartner] END ");
	}

	/**
	 * 
	 * @param idCapofilaAcronimo
	 * @param idPartner
	 * @param idDomanda
	 * @param statoDomanda
	 * @param motivazione
	 * @param logger
	 * @throws CommonalityException
	 */
	@Transactional
	public void annullaPartner(String idCapofilaAcronimo, String idPartner,String idDomanda,String statoDomanda,String motivazione, Logger logger) throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::annullaPartner] BEGIN ");
		//1)disattivo la relazione tra il partner e l'acronimo su shell_r_capofila_acronimo_partner 
		PartnerDAO.disattivaRelCapofilaAcronimoPartner(idCapofilaAcronimo, idPartner, motivazione, logger);
		//2) invalido la domanda del partner: nella tabella aggr_t_model imposto model_state_fk con il valore 'NV', 'Invalidata'.
		PartnerDAO.updateStatoDomanda(idDomanda, statoDomanda, logger);
		logger.info("[BlocchettiTransactionManager::annullaPartner] END ");
	}
		
	/**
	 * Il metodo puo' essere chiamato sia da un blocchetto sia da un altro metodo transazionale; Propagation.REQUIRED dovrebbe assicurare che la transazionalita' c'e' sempre
	 * Inserisce se non gia' presenti su findom_d_enti_strutt e su su findom_d_dipartimenti i dati relativi 
	 * al codice fiscale beneficiario e al codice unita' organizzativa passati
	 * @param codiceFiscaleBeneficiario
	 * @param denominazioneBeneficiario
	 * @param idUnitaOrganizzativa  e' la stringa vuota se unita organizzativa presa da IPA, oppure valorizzato con un id_dipartimento di findom_d_dipartimenti
	 * @param codiceUnitaOrganizzativa
	 * @param descrizioneUnitaOrganizzativa
	 * @param logger
	 * @return stringa vuota se non e' stato necessario inserire un nuovo record in findom_d_dipartimenti, oppure il nuovo id_dipartimento inserito in findom_d_dipartimenti
	 * @throws CommonalityException
	 */
	@Transactional (propagation=Propagation.REQUIRED) //da API di Spring: Support a current transaction, create a new one if none exists.
	public String aggiornaUnitaOrganizzative(String codiceFiscaleBeneficiario, String denominazioneBeneficiario, 
			                                 String idUnitaOrganizzativa, String codiceUnitaOrganizzativa,
			                                 String descrizioneUnitaOrganizzativa, Logger logger) throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::aggiornaUnitaOrganizzative] BEGIN ");
		String idUO ="";
		if(StringUtils.isBlank(codiceUnitaOrganizzativa) || codiceUnitaOrganizzativa.equals("-1")){
			//inserisco la U.O. solo se non è quella che indica nessuna scelta 
			return idUO;
		}
		
		if(StringUtils.isNotBlank(idUnitaOrganizzativa) && !idUnitaOrganizzativa.equals("0")){
			//succede se non ho ottenuto da IPA le unita organizzative (il servizio potrebbe non rispondere), ma dal db di findom che ha gli id;
			//in questo caso il chiamante non deve fare nulla
			return idUO;  
		}
		String idEnteStrutt = EnteDAO.getIdEnteStrutturato(codiceFiscaleBeneficiario);	
	
		if(StringUtils.isBlank(idEnteStrutt)){		
			//inserisco un nuovo record su findom_d_enti_strutturati e mi faccio restituire il nuovo id_ente_strutt			
			idEnteStrutt = EnteDAO.insertEnteStrutturato(codiceFiscaleBeneficiario,denominazioneBeneficiario, logger);
			logger.info("[BlocchettiTransactionManager::aggiornaUnitaOrganizzative] idEnteStrutt usato nell'insert su findom_d_enti_strutturati = " + idEnteStrutt);
		}		
		//verifico se l'unita organizzativa passata esiste su findom_d_dipartimenti, usando come criteri di ricerca id ente strutturato e codice unita organizzativa
		DipartimentoVO unitaOrganizzativaDB = DipartimentoDAO.getUnitaOrganizzativaByIdEnteECodice(idEnteStrutt, codiceUnitaOrganizzativa, logger);
		 
		if(unitaOrganizzativaDB==null || unitaOrganizzativaDB.getId()==null){
			
			//succede se ho ottenuto le unita organizzative da IPA e il record scelto ha un codice che non e' ancora 
			//presente, per l'id ente strutturato passato, in findom_d_dipartimenti; 
			//procedo quindi all'inserimento di un nuovo record su tale tabella e a salvare in idUO l'id del record

			idUO = DipartimentoDAO.insertUnitaOrganizzativa(codiceUnitaOrganizzativa, descrizioneUnitaOrganizzativa, idEnteStrutt, logger);
			logger.info("[BlocchettiTransactionManager::aggiornaUnitaOrganizzative] idUO usato nell'insert su findom_d_dipartimenti = " + idUO);

		}else{
			//in questo caso il record scelto ha un codice che e' gia' presente, per l'id ente strutturato passato, in findom_d_dipartimenti;
			//non faccio quindi la insert su findom_d_dipartimenti, ma ritorno comunque l'id in modo che il chiamante possa eventualmente usarlo 			
			idUO = unitaOrganizzativaDB.getId() + "";
		}		
		logger.info("[BlocchettiTransactionManager::aggiornaUnitaOrganizzative] END ");
		return idUO;
	}

	@Transactional
	public void aggiornaPartnerGiaEsistente(String idCapofilaAcronimo, PartnerItemVO partnerItemVO, 
											String idPartnerGiaEsistente,
											Logger logger) throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::aggiornaPartnerGiaEsistente] BEGIN ");
		if(StringUtils.isBlank(idCapofilaAcronimo) || StringUtils.isBlank(idPartnerGiaEsistente) || partnerItemVO == null){
			return;
		}
		//disattivo la relazione tra il capofila e il partner che ho modificato e che e' diventato uguale al partner avente id = idPartnerGiaEsistente
		PartnerDAO.disattivaRelCapofilaAcronimoPartner(idCapofilaAcronimo, partnerItemVO.getIdPartner(), "disattivazione automatica per modifica partner su partner gia esistente", logger);
		//inserisco una nuova relazione tra il capofila e il partner avente id = idPartnerGiaEsistente
		PartnerDAO.insertShellRCapofilaAcronimoPartner(idCapofilaAcronimo, idPartnerGiaEsistente ,"", logger);
		//eventuale aggiornamento unita' organizzative		
		if(StringUtils.isNotBlank(partnerItemVO.getCodiceDipartimento())){
			String codiceFiscaleBeneficiario = partnerItemVO.getCodiceFiscale();
			String denominazioneBeneficiario = partnerItemVO.getDenominazione(); 
			String idUnitaOrganizzativa = "0";  //passo 0 anche se fossi nel caso in cui IPA e' giu' e le uo le ho prese dal DB e quindi avrei l'id; non e' ottimizzato, ma l'effetto e' uguale
			String codiceUnitaOrganizzativa = partnerItemVO.getCodiceDipartimento();
			String descrizioneUnitaOrganizzativa = partnerItemVO.getDescrDipartimento();
			aggiornaUnitaOrganizzative(codiceFiscaleBeneficiario, denominazioneBeneficiario, 
					idUnitaOrganizzativa,  codiceUnitaOrganizzativa,
					descrizioneUnitaOrganizzativa, logger);
		}
		//eventuale aggiornamento della denominazione del soggetto avente il codice fiscale partnerItemVO.codiceFiscale
		aggiornaDenominazioneSoggetto(partnerItemVO.getCodiceFiscale(), partnerItemVO.getDenominazione(), logger);
		logger.info("[BlocchettiTransactionManager::aggiornaPartnerGiaEsistente] END ");
	}
	
	/**
	 * poiche' non si puo' inserire due volte la stessa coppia (id_capofila_acronimo , id_partner) nella shell_r_capofila_acronimo_partner,
	 * quando un partner che era stato eliminato in precedenza (e aveva una domanda, per cui nella shell_r_capofila_acronimo_partner
	 * il record era solo stato eliminato logicamente) viene reinserito, viene fatto una update al record di shell_r_capofila_acronimo_partner,
	 * ponenedo a null la data disattivazione, la nota, l'id domanda partner e impostando la data attivazione ad adesso  
	 * @param idCapofilaAcronimo
	 * @param idPartner
	 * @param codiceFiscaleBeneficiario
	 * @param denominazioneBeneficiario
	 * @param idUnitaOrganizzativa
	 * @param codiceUnitaOrganizzativa
	 * @param descrizioneUnitaOrganizzativa
	 * @param logger
	 * @throws CommonalityException
	 */
	@Transactional
	public void riattivaRelazioneCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner,
			                                        String codiceFiscaleBeneficiario, String denominazioneBeneficiario, 
			                                        String idUnitaOrganizzativa, String codiceUnitaOrganizzativa,
		                   	                        String descrizioneUnitaOrganizzativa,
			                                        Logger logger)throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::riattivaRelazioneCapofilaAcronimoPartner] BEGIN ");
		//aggiornamento su tabella di relazione tra capofila acronimo e partner al fine di riattivare la relazione tra capofila e partner
		PartnerDAO.riattivaRelazioneCapofilaAcronimoPartner(idCapofilaAcronimo,idPartner, logger);
		//eventuale aggiornamento delle unita organizzative
		if(StringUtils.isNotBlank(codiceUnitaOrganizzativa)){
			aggiornaUnitaOrganizzative(codiceFiscaleBeneficiario, denominazioneBeneficiario, 
										idUnitaOrganizzativa,  codiceUnitaOrganizzativa,
										descrizioneUnitaOrganizzativa, logger);
		}
		//eventuale aggiornamento della denominazione del soggetto avente il codice fiscale partnerItemVO.codiceFiscale
		aggiornaDenominazioneSoggetto(codiceFiscaleBeneficiario, denominazioneBeneficiario, logger);
		logger.info("[BlocchettiTransactionManager::riattivaRelazioneCapofilaAcronimoPartner] END ");
	}
	
	private void aggiornaDenominazioneSoggetto(String codiceFiscale, String denominazione, Logger logger) throws CommonalityException{
		logger.info("[BlocchettiTransactionManager::aggiornaDenominazioneSoggetto] BEGIN ");
		String idSoggetto = PartnerDAO.getIdSoggettoByCF(codiceFiscale, logger);
		if(StringUtils.isNotBlank(idSoggetto)){
			PartnerDAO.updateDenominazioneSoggettoById(idSoggetto, denominazione, logger);
		}
		logger.info("[BlocchettiTransactionManager::aggiornaDenominazioneSoggetto] END ");	
	}
}
