/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.legalerappresentante;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class LegaleRappresentanteVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;

	@MapTo(target = INHERIT)
	String lrFromAAEP;
	
	@MapTo(target = INHERIT)
	String cap; //cap"));

	@MapTo(target = INHERIT)
	String cittaEsteraResidenza; //cittaEsteraResidenza"));
	
	@MapTo(target = INHERIT)
	String codiceFiscale; //codiceFiscale"));
	
	@MapTo(target = INHERIT)
	String cognome; //cognome"));
	
	@MapTo(target = INHERIT)
	String genere;

	@MapTo(target = INHERIT)
	String comuneNascita; //comuneNascita"));
	
	@MapTo(target = INHERIT)
	String comuneNascitaDescrizione; //comuneNascitaDescrizione"));
	
	@MapTo(target = INHERIT)
	String comuneResidenza; //comuneResidenza"));
	
	@MapTo(target = INHERIT)
	String comuneResidenzaDescrizione; //comuneResidenzaDescrizione"));
	
	@MapTo(target = INHERIT)
	String dataNascita; //dataNascita"));

	@MapTo(target = INHERIT)
	LegaleRappresentanteDocumentoVO documento;

	@MapTo(target = INHERIT)
	String indirizzo; //indirizzo"));
	
	@MapTo(target = INHERIT)
	String luogoNascita; //luogoNascita"));
	
	@MapTo(target = INHERIT)
	String luogoResidenza; //luogoResidenza"));
	
	@MapTo(target = INHERIT)
	String nome; //nome"));
	
	@MapTo(target = INHERIT)
	String numCivico; //numCivico"));
	
	@MapTo(target = INHERIT)
	String presenzaSoggettoDelegato; //presenzaSoggettoDelegato"));
	
	@MapTo(target = INHERIT)
	String provinciaNascita; //provinciaNascita"));
	
	@MapTo(target = INHERIT)
	String provinciaNascitaDescrizione; //provinciaNascitaDescrizione"));
	
	@MapTo(target = INHERIT)
	String provinciaResidenza; //provinciaResidenza"));
	
	@MapTo(target = INHERIT)
	String provinciaResidenzaDescrizione; //provinciaResidenzaDescrizione"));
	
	@MapTo(target = INHERIT)
	String siglaProvinciaNascita; //siglaProvinciaNascita"));
	
	@MapTo(target = INHERIT)
	String siglaProvinciaResidenza; //siglaProvinciaResidenza"));
	
	@MapTo(target = INHERIT)
	String statoEsteroNascita; //statoEsteroNascita"));
	
	@MapTo(target = INHERIT)
	String statoEsteroNascitaDescrizione; //statoEsteroNascitaDescrizione"));
	
	@MapTo(target = INHERIT)
	String statoEsteroResidenza; //statoEsteroResidenza"));
	
	@MapTo(target = INHERIT)
	String statoEsteroResidenzaDescrizione; //statoEsteroResidenzaDescrizione"));

	
	
	public String getLrFromAAEP() {
		return lrFromAAEP;
	}

	public void setLrFromAAEP(String lrFromAAEP) {
		this.lrFromAAEP = lrFromAAEP;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCittaEsteraResidenza() {
		return cittaEsteraResidenza;
	}

	public void setCittaEsteraResidenza(String cittaEsteraResidenza) {
		this.cittaEsteraResidenza = cittaEsteraResidenza;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public String getComuneNascitaDescrizione() {
		return comuneNascitaDescrizione;
	}

	public void setComuneNascitaDescrizione(String comuneNascitaDescrizione) {
		this.comuneNascitaDescrizione = comuneNascitaDescrizione;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public String getComuneResidenzaDescrizione() {
		return comuneResidenzaDescrizione;
	}

	public void setComuneResidenzaDescrizione(String comuneResidenzaDescrizione) {
		this.comuneResidenzaDescrizione = comuneResidenzaDescrizione;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public LegaleRappresentanteDocumentoVO getDocumento() {
		return documento;
	}

	public void setDocumento(LegaleRappresentanteDocumentoVO documento) {
		this.documento = documento;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getLuogoResidenza() {
		return luogoResidenza;
	}

	public void setLuogoResidenza(String luogoResidenza) {
		this.luogoResidenza = luogoResidenza;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumCivico() {
		return numCivico;
	}

	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}

	public String getPresenzaSoggettoDelegato() {
		return presenzaSoggettoDelegato;
	}

	public void setPresenzaSoggettoDelegato(String presenzaSoggettoDelegato) {
		this.presenzaSoggettoDelegato = presenzaSoggettoDelegato;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getProvinciaNascitaDescrizione() {
		return provinciaNascitaDescrizione;
	}

	public void setProvinciaNascitaDescrizione(String provinciaNascitaDescrizione) {
		this.provinciaNascitaDescrizione = provinciaNascitaDescrizione;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public String getProvinciaResidenzaDescrizione() {
		return provinciaResidenzaDescrizione;
	}

	public void setProvinciaResidenzaDescrizione(String provinciaResidenzaDescrizione) {
		this.provinciaResidenzaDescrizione = provinciaResidenzaDescrizione;
	}

	public String getSiglaProvinciaNascita() {
		return siglaProvinciaNascita;
	}

	public void setSiglaProvinciaNascita(String siglaProvinciaNascita) {
		this.siglaProvinciaNascita = siglaProvinciaNascita;
	}

	public String getSiglaProvinciaResidenza() {
		return siglaProvinciaResidenza;
	}

	public void setSiglaProvinciaResidenza(String siglaProvinciaResidenza) {
		this.siglaProvinciaResidenza = siglaProvinciaResidenza;
	}

	public String getStatoEsteroNascita() {
		return statoEsteroNascita;
	}

	public void setStatoEsteroNascita(String statoEsteroNascita) {
		this.statoEsteroNascita = statoEsteroNascita;
	}

	public String getStatoEsteroNascitaDescrizione() {
		return statoEsteroNascitaDescrizione;
	}

	public void setStatoEsteroNascitaDescrizione(String statoEsteroNascitaDescrizione) {
		this.statoEsteroNascitaDescrizione = statoEsteroNascitaDescrizione;
	}

	public String getStatoEsteroResidenza() {
		return statoEsteroResidenza;
	}

	public void setStatoEsteroResidenza(String statoEsteroResidenza) {
		this.statoEsteroResidenza = statoEsteroResidenza;
	}

	public String getStatoEsteroResidenzaDescrizione() {
		return statoEsteroResidenzaDescrizione;
	}

	public void setStatoEsteroResidenzaDescrizione(String statoEsteroResidenzaDescrizione) {
		this.statoEsteroResidenzaDescrizione = statoEsteroResidenzaDescrizione;
	}

}
