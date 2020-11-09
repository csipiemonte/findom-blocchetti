/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.util;

import it.csi.findom.blocchetti.common.vo.aaep.AttivitaEconomicaVO;
import it.csi.findom.blocchetti.common.vo.aaep.CaricaVO;
import it.csi.findom.blocchetti.common.vo.aaep.CessazioneVO;
import it.csi.findom.blocchetti.common.vo.aaep.ContattiVO;
import it.csi.findom.blocchetti.common.vo.aaep.DatoCostitutivoVO;
import it.csi.findom.blocchetti.common.vo.aaep.DettagliAlboArtigianoVO;
import it.csi.findom.blocchetti.common.vo.aaep.DettagliCameraCommercioVO;
import it.csi.findom.blocchetti.common.vo.aaep.ImpresaVO;
import it.csi.findom.blocchetti.common.vo.aaep.PersonaVO;
import it.csi.findom.blocchetti.common.vo.aaep.ProcConcorsVO;
import it.csi.findom.blocchetti.common.vo.aaep.SedeVO;
import it.csi.findom.blocchetti.common.vo.aaep.SezioneSpecialeVO;
import it.csi.findom.blocchetti.common.vo.aaep.UbicazioneVO;
import it.csi.findom.findomwebnew.dto.aaep.AttivitaEconomica;
import it.csi.findom.findomwebnew.dto.aaep.Carica;
import it.csi.findom.findomwebnew.dto.aaep.Cessazione;
import it.csi.findom.findomwebnew.dto.aaep.Contatti;
import it.csi.findom.findomwebnew.dto.aaep.DatoCostitutivo;
import it.csi.findom.findomwebnew.dto.aaep.DettagliAlboArtigiano;
import it.csi.findom.findomwebnew.dto.aaep.DettagliCameraCommercio;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.findom.findomwebnew.dto.aaep.Persona;
import it.csi.findom.findomwebnew.dto.aaep.ProcConcors;
import it.csi.findom.findomwebnew.dto.aaep.Sede;
import it.csi.findom.findomwebnew.dto.aaep.SezioneSpeciale;
import it.csi.findom.findomwebnew.dto.aaep.Ubicazione;
import it.csi.melograno.aggregatore.business.javaengine.vo.StatusInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrasformaClassiAAEP2VO extends StatusInfo {

	private static final String CLASS_NAME = "ClonaClassiVO";

	/**
	 * Trasformo l'oggetto impresaVO che arriva, dai WS di VO, in un
	 * oggetto serializzabile
	 * 
	 * @param impresa
	 * @return
	 */
	public static ImpresaVO impresa2ImpresaVO(Impresa imprImp) {
	
		ImpresaVO imprVO = null;

		if (imprImp != null) {
			imprVO = new ImpresaVO();
			imprVO.setCessazione(cessazione2cessazioneVO(imprImp.getCessazione()));
			imprVO.setCodiceFiscale(imprImp.getCodiceFiscale());
			imprVO.setCodiciATECO(attivitaEconomica2attivitaEconomicaVO(imprImp.getCodiciATECO()));
			imprVO.setCodNaturaGiuridica(imprImp.getCodNaturaGiuridica());
			imprVO.setDataAggiornamento(imprImp.getDataAggiornamento());
			
			imprVO.setDataInizioValidita(imprImp.getDataInizioValidita());
			
			/** Jira: 1707: dataIscrizioneRegistroImprese */
			imprVO.setDataIscrizioneRegistroImprese(imprImp.getDettagliCameraCommercio().getDataIscrizioneRegistroImprese());
			
			imprVO.setDescrFonte(imprImp.getDescrFonte());
			imprVO.setDescrNaturaGiuridica(imprImp.getDescrNaturaGiuridica());
			imprVO.setDettagliAlboArtigiano(dettaglioAA2dettaglioAAVO(imprImp.getDettagliAlboArtigiano()));
			imprVO.setDettagliCameraCommercio(dettaglioCC2dettaglioCCVO(imprImp.getDettagliCameraCommercio()));
			imprVO.setIdAzienda(imprImp.getIdAzienda());
			imprVO.setIdFonte(imprImp.getIdFonte());
			imprVO.setIdNaturaGiuridica(imprImp.getIdNaturaGiuridica());
			imprVO.setIdSede(imprImp.getIdSede());
			imprVO.setPartitaIva(imprImp.getPartitaIva());
			imprVO.setPostaElettronicaCertificata(imprImp.getPostaElettronicaCertificata());
			imprVO.setRagioneSociale(imprImp.getRagioneSociale());
			imprVO.setSedi(listaSedi2listaSediVO(imprImp.getSedi()));
			imprVO.setAnnoDenunciaAddetti(imprImp.getAnnoDenunciaAddetti());
			imprVO.setCessazioneFunzioneSedeLegale(
					cessazione2cessazioneVO(imprImp.getCessazioneFunzioneSedeLegale()));
			imprVO.setCodFonte(imprImp.getCodFonte());
			imprVO.setDatoCostitutivo(datoCostitutivo2datoCostitutivoVO(imprImp.getDatoCostitutivo()));
			imprVO.setDescrIndicStatoAttiv(imprImp.getDescrIndicStatoAttiv());
			imprVO.setDescrIndicTrasfSede(imprImp.getDescrIndicTrasfSede());
			imprVO.setFlagLocalizzazionePiemonte(imprImp.getFlagLocalizzazionePiemonte());
			imprVO.setFlgAggiornamento(imprImp.getFlgAggiornamento());
			imprVO.setImpresaCessata(imprImp.getImpresaCessata());
			imprVO.setIndicStatoAttiv(imprImp.getIndicStatoAttiv());
			imprVO.setIndicTrasfSede(imprImp.getIndicTrasfSede());
			imprVO.setListaPersone(listaPersone2listaPersoneVO(imprImp.getListaPersone()));
			imprVO.setListaProcConcors(listaProcConcors2listaProcConcorsVO(imprImp.getListaProcConcors()));
			imprVO.setListaSezSpecInfoc(listaSezioneSpeciale2listaSezioneSpecialeVO(imprImp.getListaSezSpecInfoc()));
			imprVO.setNumAddettiFam(imprImp.getNumAddettiFam());
			imprVO.setNumAddettiSubord(imprImp.getNumAddettiSubord());
			imprVO.setTestoOggettoSociale(imprImp.getTestoOggettoSociale());
		}
		return imprVO;
	}

	private static CessazioneVO cessazione2cessazioneVO(Cessazione cessImp) {

		CessazioneVO cess = null;
		if (cessImp != null) {
			cess = new CessazioneVO();
			cess.setCessazione(cessImp.getCessazione());
			cess.setCodCausaleCessazione(cessImp.getCodCausaleCessazione());
			cess.setDataCessazione(cessImp.getDataCessazione());
			cess.setDataDenunciaCessazione(cessImp.getDataDenunciaCessazione());
			cess.setDescrCausaleCessazione(cessImp.getDescrCausaleCessazione());
		}
		return cess;
	}

	private static List<AttivitaEconomicaVO> attivitaEconomica2attivitaEconomicaVO(
			List<AttivitaEconomica> listAttEco) {
		

		List<AttivitaEconomicaVO> listaAE = null;
		if (listAttEco != null) {
			listaAE = new ArrayList<AttivitaEconomicaVO>();
			for (Iterator itr = listAttEco.iterator(); itr.hasNext();) {
				AttivitaEconomica attEcoImp = (AttivitaEconomica) itr.next();

				AttivitaEconomicaVO attEc = new AttivitaEconomicaVO();

				attEc.setAnnoDiRiferimento(attEcoImp.getAnnoDiRiferimento());
				attEc.setCodiceATECO(attEcoImp.getCodiceATECO());
				attEc.setCodImportanzaAA(attEcoImp.getCodImportanzaAA());
				attEc.setCodImportanzaRI(attEcoImp.getCodImportanzaRI());
				attEc.setDataCessazione(attEcoImp.getDataCessazione());
				attEc.setDataInizio(attEcoImp.getDataInizio());
				attEc.setDescrImportanzaAA(attEcoImp.getDescrImportanzaAA());
				attEc.setDescrImportanzaRI(attEcoImp.getDescrImportanzaRI());
				attEc.setDescrizione(attEcoImp.getDescrizione());
				
				listaAE.add(attEc);
			}
		}
		return listaAE;
	}

	private static DettagliAlboArtigianoVO dettaglioAA2dettaglioAAVO(DettagliAlboArtigiano dettaglioAAImp) {
		
		DettagliAlboArtigianoVO dett = null;
		if (dettaglioAAImp != null) {
			dett = new DettagliAlboArtigianoVO();
			dett.setDataDeliberaIscrizione(dettaglioAAImp.getDataDeliberaIscrizione());
			dett.setDescrIterIscrizione(dettaglioAAImp.getDescrIterIscrizione());
			dett.setFlgIterIscrizione(dettaglioAAImp.getFlgIterIscrizione());
			dett.setNumeroIscrizione(dettaglioAAImp.getNumeroIscrizione());
			dett.setProvinciaIscrizione(dettaglioAAImp.getProvinciaIscrizione());
		}

		return dett;
	}

	private static DettagliCameraCommercioVO dettaglioCC2dettaglioCCVO(DettagliCameraCommercio dettagliCCImp) {
		
		DettagliCameraCommercioVO dett = null;
		if (dettagliCCImp != null) {
			dett = new DettagliCameraCommercioVO();

			dett.setAnno(dettagliCCImp.getAnno());
			dett.setDataAggiornamento(dettagliCCImp.getDataAggiornamento());
			dett.setDataCancellazioneREA(dettagliCCImp.getDataCancellazioneREA());
			dett.setDataIscrizioneREA(dettagliCCImp.getDataIscrizioneREA());
			dett.setDataIscrizioneRegistroImprese(dettagliCCImp.getDataIscrizioneRegistroImprese());
			dett.setNumero(dettagliCCImp.getNumero());
			dett.setNumIscrizionePosizioneREA(dettagliCCImp.getNumIscrizionePosizioneREA());
			dett.setNumRegistroImprese(dettagliCCImp.getNumRegistroImprese());
			dett.setProvincia(dettagliCCImp.getProvincia());
			dett.setSiglaProvincia(dettagliCCImp.getSiglaProvincia());
			dett.setSiglaProvinciaIscrizioneREA(dettagliCCImp.getSiglaProvinciaIscrizioneREA());
			dett.setTribunale(dettagliCCImp.getTribunale());
		}
		return dett;
	}

	private static List<SedeVO> listaSedi2listaSediVO(List<Sede> sediList) {
		
		List<SedeVO> lista = null;
		if (sediList != null) {
			lista = new ArrayList<SedeVO>();

			for (Iterator itr = sediList.iterator(); itr.hasNext();) {
				Sede sede = (Sede) itr.next();
				lista.add(sede2SedeVO(sede));
			}
		}

		return lista;
	}

	private static ContattiVO contatti2contattiVO(Contatti contattiImp) {
		ContattiVO cont = null;
		if (contattiImp != null) {
			cont = new ContattiVO();

			cont.setEmail(contattiImp.getEmail());
			cont.setFax(contattiImp.getFax());
			cont.setNumeroVerde(contattiImp.getNumeroVerde());
			cont.setSitoWeb(contattiImp.getSitoWeb());
			cont.setTelefono(contattiImp.getTelefono());
		}
		return cont;
	}

	private static UbicazioneVO ubicazione2ubicazioneVO(Ubicazione ubicazioneImp) {

		UbicazioneVO ubi = null;
		if (ubicazioneImp != null) {
			ubi = new UbicazioneVO();
			ubi.setAltreIndicazioni(ubicazioneImp.getAltreIndicazioni());
			ubi.setCap(ubicazioneImp.getCap());
			ubi.setCodCatastale(ubicazioneImp.getCodCatastale());
			ubi.setCodISTATComune(ubicazioneImp.getCodISTATComune());
			ubi.setCodNazione(ubicazioneImp.getCodNazione());
			ubi.setCodQuartiere(ubicazioneImp.getCodQuartiere());
			ubi.setCoordinataX(ubicazioneImp.getCoordinataX());
			ubi.setCoordinataY(ubicazioneImp.getCoordinataY());
			ubi.setDescrComune(ubicazioneImp.getDescrComune());
			ubi.setDescrizioneEstesa(ubicazioneImp.getDescrizioneEstesa());
			ubi.setGeometriaGJSON(ubicazioneImp.getGeometriaGJSON());
			ubi.setIndirizzo(ubicazioneImp.getIndirizzo());
			ubi.setNomeNazione(ubicazioneImp.getNomeNazione());
			ubi.setNumeroCivico(ubicazioneImp.getNumeroCivico());
			ubi.setSiglaProvincia(ubicazioneImp.getSiglaProvincia());
			ubi.setToponimo(ubicazioneImp.getToponimo());
		}
		return ubi;
	}

	private static DatoCostitutivoVO datoCostitutivo2datoCostitutivoVO(DatoCostitutivo datoImp) {
	
		DatoCostitutivoVO dato = null;
		if (datoImp != null) {
			dato = new DatoCostitutivoVO();

			dato.setCapitaleSocDelib(datoImp.getCapitaleSocDelib());
			dato.setCapitalesocVers(datoImp.getCapitalesocVers());
			dato.setCapitSocDeliberato(datoImp.getCapitSocDeliberato());
			dato.setCapitSocSottoscr(datoImp.getCapitSocSottoscr());
			dato.setCapitSocVersato(datoImp.getCapitSocVersato());
			dato.setCodDurataCS(datoImp.getCodDurataCS());
			dato.setCodFormaAmministr(datoImp.getCodFormaAmministr());
			dato.setCodTipoAtto(datoImp.getCodTipoAtto());
			dato.setCodTipoConferim(datoImp.getCodTipoConferim());
			dato.setDataCostituzione(datoImp.getDataCostituzione());
			dato.setDataFineEsAmmt(datoImp.getDataFineEsAmmt());
			dato.setDataFondazione(datoImp.getDataFondazione());
			dato.setDataIniEsAmmt(datoImp.getDataIniEsAmmt());
			dato.setDataRegAtto(datoImp.getDataRegAtto());
			dato.setDataScadenzaPrimoEsercizio(datoImp.getDataScadenzaPrimoEsercizio());
			dato.setDataTermineSocieta(datoImp.getDataTermineSocieta());
			dato.setDescrFormaAmministr(datoImp.getDescrFormaAmministr());
			dato.setDescrProvUffReg(datoImp.getDescrProvUffReg());
			dato.setDescrSiglaProvNotaio(datoImp.getDescrSiglaProvNotaio());
			dato.setDescrTipoAtto(datoImp.getDescrTipoAtto());
			dato.setDescrTipoConferim(datoImp.getDescrTipoConferim());
			dato.setFlagDurataIllimitata(datoImp.getFlagDurataIllimitata());
			dato.setLocalitaNotaio(datoImp.getLocalitaNotaio());
			dato.setNotaio(datoImp.getNotaio());
			dato.setNumAnniEsAmmt(datoImp.getNumAnniEsAmmt());
			dato.setNumAzioniCapitSociale(datoImp.getNumAzioniCapitSociale());
			dato.setNumMaxMembriCS(datoImp.getNumMaxMembriCS());
			dato.setNumMembriCSCarica(datoImp.getNumMembriCSCarica());
			dato.setNumMinMembriCS(datoImp.getNumMinMembriCS());
			dato.setNumRegAtto(datoImp.getNumRegAtto());
			dato.setNumRepertorio(datoImp.getNumRepertorio());
			dato.setNumSoci(datoImp.getNumSoci());
			dato.setNumSociCarica(datoImp.getNumSociCarica());
			dato.setScadenzaEsSucc(datoImp.getScadenzaEsSucc());
			dato.setSiglaProvNotaio(datoImp.getSiglaProvNotaio());
			dato.setSiglaProvUffRegistro(datoImp.getSiglaProvUffRegistro());
			dato.setTotFondoConsortE(datoImp.getTotFondoConsortE());
			dato.setTotFondoConsortile(datoImp.getTotFondoConsortile());
			dato.setTotQuoteCapitSoc(datoImp.getTotQuoteCapitSoc());
			dato.setTotQuoteCapitSocE(datoImp.getTotQuoteCapitSocE());
			dato.setUfficioRegistro(datoImp.getUfficioRegistro());
			dato.setValAzioniCapitSoc(datoImp.getValAzioniCapitSoc());
			dato.setValoreAzioniCapitSoc(datoImp.getValoreAzioniCapitSoc());
			dato.setValutaCapitale(datoImp.getValutaCapitale());
			dato.setValutaCapitSociale(datoImp.getValutaCapitSociale());
		}
		return dato;
	}

	private static List<PersonaVO> listaPersone2listaPersoneVO(
			List<Persona> listaPersone) {

		List<PersonaVO> lista = null;
		if (listaPersone != null) {
			lista = new ArrayList<PersonaVO>();

			for (Iterator itr = listaPersone.iterator(); itr.hasNext();) {
				Persona persImp = (Persona) itr.next();

				PersonaVO persona = new PersonaVO();
				persona.setCodiceFiscale(persImp.getCodiceFiscale());
				persona.setCognome(persImp.getCognome());
				persona.setDescrTipoPersona(persImp.getDescrTipoPersona());
				persona.setIdAzienda(persImp.getIdAzienda());
				persona.setIdFonteDato(persImp.getIdFonteDato());
				persona.setIdPersona(persImp.getIdPersona());
				persona.setListaCariche(listaCarica2listaCaricaVO(persImp.getListaCariche()));
				persona.setNome(persImp.getNome());
				persona.setTipoPersona(persImp.getTipoPersona());
				lista.add(persona);
			}
		}
		return lista;
	}

	private static List<CaricaVO> listaCarica2listaCaricaVO(List<Carica> listaCarica) {

		List<CaricaVO> lista = null;
		if (listaCarica != null) {

			lista = new ArrayList<CaricaVO>();
			for (Iterator itr = listaCarica.iterator(); itr.hasNext();) {
				Carica caricaImp = (Carica) itr.next();

				CaricaVO carica = new CaricaVO();
				carica.setCodCarica(caricaImp.getCodCarica());
				carica.setCodDurataCarica(caricaImp.getCodDurataCarica());
				carica.setCodFiscaleAzienda(caricaImp.getCodFiscaleAzienda());
				carica.setCodFiscalePersona(caricaImp.getCodFiscalePersona());
				carica.setDataFineCarica(caricaImp.getDataFineCarica());
				carica.setDataInizioCarica(caricaImp.getDataInizioCarica());
				carica.setDataPresentazCarica(caricaImp.getDataPresentazCarica());
				carica.setDescrAzienda(caricaImp.getDescrAzienda());
				carica.setDescrCarica(caricaImp.getDescrCarica());
				carica.setDescrDurataCarica(caricaImp.getDescrDurataCarica());
				carica.setFlagRappresentanteLegale(caricaImp.getFlagRappresentanteLegale());
				carica.setIdAzienda(caricaImp.getIdAzienda());
				carica.setIdFonteDato(caricaImp.getIdFonteDato());
				carica.setNumAnniEsercCarica(caricaImp.getNumAnniEsercCarica());
				carica.setProgrCarica(caricaImp.getProgrCarica());
				carica.setProgrPersona(caricaImp.getProgrPersona());
				lista.add(carica);
			}
		}
		return lista;
	}

	private static List<ProcConcorsVO> listaProcConcors2listaProcConcorsVO(
			List<ProcConcors> listaPC) {


		List<ProcConcorsVO> lista = null;
		if (listaPC != null) {
			lista = new ArrayList<ProcConcorsVO>();

			for (Iterator itr = listaPC.iterator(); itr.hasNext();) {
				ProcConcors procCon = (ProcConcors) itr.next();

				ProcConcorsVO pr = new ProcConcorsVO();
				pr.setCodAtto(procCon.getCodAtto());
				pr.setCodLiquidazione(procCon.getCodLiquidazione());
				pr.setDataAperturaProc(procCon.getDataAperturaProc());
				pr.setDataChiusuraLiquidaz(procCon.getDataChiusuraLiquidaz());
				pr.setDataEsecConcordPrevent(procCon.getDataEsecConcordPrevent());
				pr.setDataFineLiquidaz(procCon.getDataFineLiquidaz());
				pr.setDataRegistroAtto(procCon.getDataRegistroAtto());
				pr.setDataRevocalLiquidaz(procCon.getDataRevocalLiquidaz());
				pr.setDescIndicatEsecutAtto(procCon.getDescIndicatEsecutAtto());
				pr.setDescrAltreIndicazioni(procCon.getDescrAltreIndicazioni());
				pr.setDescrCodAtto(procCon.getDescrCodAtto());
				pr.setDescrNotaio(procCon.getDescrNotaio());
				pr.setDescrTribunale(procCon.getDescrTribunale());
				pr.setIdAAEPAzienda(procCon.getIdAAEPAzienda());
				pr.setIdAAEPFonteDato(procCon.getIdAAEPFonteDato());
				pr.setLocalRegistroAtto(procCon.getLocalRegistroAtto());
				pr.setNumRestistrAtto(procCon.getNumRestistrAtto());
				pr.setProgrLiquidazione(procCon.getProgrLiquidazione());
				pr.setSiglaProvRegAtto(procCon.getSiglaProvRegAtto());
				lista.add(pr);
			}
		}
		return lista;
	}

	private static List<SezioneSpecialeVO> listaSezioneSpeciale2listaSezioneSpecialeVO(
			List<SezioneSpeciale> listaSS) {
		

		List<SezioneSpecialeVO> lista = null;
		if (listaSS != null) {
			lista = new ArrayList<SezioneSpecialeVO>();

			for (Iterator itr = listaSS.iterator(); itr.hasNext();) {
				SezioneSpeciale ssImp = (SezioneSpeciale) itr.next();

				SezioneSpecialeVO sez = new SezioneSpecialeVO();
				sez.setCodAlbo(ssImp.getCodAlbo());
				sez.setCodiceSezSpec(ssImp.getCodiceSezSpec());
				sez.setCodSezione(ssImp.getCodSezione());
				sez.setDataFine(ssImp.getDataFine());
				sez.setDataInizio(ssImp.getDataInizio());
				sez.setFlColtDir(ssImp.getFlColtDir());
				sez.setIdAzienda(ssImp.getIdAzienda());
				sez.setIdFonteDato(ssImp.getIdFonteDato());
				sez.setIdSezioneSpeciale(ssImp.getIdSezioneSpeciale());
				lista.add(sez);
			}
		}

		return lista;
	}

	public static SedeVO sede2SedeVO(Sede sedeImp) {
		SedeVO sede = new SedeVO();
		if (sedeImp != null) {
			sede.setAteco(attivitaEconomica2attivitaEconomicaVO(sedeImp.getAteco()));
			sede.setCodiceAteco2007(sedeImp.getCodiceAteco2007());
			sede.setDescrizioneAteco2007(sedeImp.getDescrizioneAteco2007());
			
			sede.setCodCausaleCessazione(sedeImp.getCodCausaleCessazione());
			sede.setCodSettore(sedeImp.getCodSettore());
			sede.setContatti(contatti2contattiVO(sedeImp.getContatti()));
			sede.setDataAggiornam(sedeImp.getDataAggiornam());
			sede.setDataCessazione(sedeImp.getDataCessazione());
			sede.setDataInizioAttivita(sedeImp.getDataInizioAttivita());
			sede.setDataInizioValidita(sedeImp.getDataInizioValidita());
			sede.setDataNumeroDipendenti(sedeImp.getDataNumeroDipendenti());
			sede.setDenominazione(sedeImp.getDenominazione());
			sede.setDescrCausaleCessazione(sedeImp.getDescrCausaleCessazione());
			sede.setDescrSettore(sedeImp.getDescrSettore());
			sede.setDescrTipoSede(sedeImp.getDescrTipoSede());
			sede.setFonteDato(sedeImp.getFonteDato());
			sede.setIdAzienda(sedeImp.getIdAzienda());
			sede.setIdSede(sedeImp.getIdSede());
			sede.setNumeroDipendenti(sedeImp.getNumeroDipendenti());
			sede.setRiferimento(sedeImp.getRiferimento());
			sede.setTipoSede(sedeImp.getTipoSede());
			sede.setUbicazione(ubicazione2ubicazioneVO(sedeImp.getUbicazione()));
		}
		return sede;
	}

}
