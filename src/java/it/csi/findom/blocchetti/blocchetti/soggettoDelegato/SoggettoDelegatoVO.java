/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.soggettoDelegato;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.findom.blocchetti.common.vo.tipodocriconoscimento.TipoDocRiconoscimentoVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SoggettoDelegatoVO extends CommonalityVO {
	
	private static final long serialVersionUID = 1L;
	
	@MapTo(target = INHERIT)
	String codiceFiscale;
	@MapTo(target = INHERIT)
	String cognome;
	@MapTo(target = INHERIT)
	String nome;
	@MapTo(target = INHERIT)
	String genere;
	@MapTo(target = INHERIT)
	String dataNascita;
	
	@MapTo(target=INHERIT)
	String comuneNascita;
	@MapTo(target=INHERIT)
	String provinciaNascita;
	@MapTo(target=INHERIT)
	String luogoNascita;
	@MapTo(target=INHERIT)
	String statoEsteroNascita;
	
	@MapTo(target=INHERIT)
	TipoDocRiconoscimentoVO documento;
	
	@MapTo(target=INHERIT)
	String comuneResidenza;
	@MapTo(target=INHERIT)
	String provinciaResidenza;
	@MapTo(target=INHERIT)
	String luogoResidenza;
	@MapTo(target=INHERIT)
	String statoEsteroResidenza;
	@MapTo(target=INHERIT)
	String cittaEsteraResidenza;
	@MapTo(target=INHERIT)
	String indirizzo;
	@MapTo(target=INHERIT)
	String numCivico;
	@MapTo(target=INHERIT)
	String cap;
	
	
	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
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

	public String getNome() {
		return nome;
	}

	public String getGenere() {
		return genere;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public String getStatoEsteroNascita() {
		return statoEsteroNascita;
	}

	public TipoDocRiconoscimentoVO getDocumento() {
		return documento;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public String getLuogoResidenza() {
		return luogoResidenza;
	}

	public String getStatoEsteroResidenza() {
		return statoEsteroResidenza;
	}

	public String getCittaEsteraResidenza() {
		return cittaEsteraResidenza;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getNumCivico() {
		return numCivico;
	}

	public String getCap() {
		return cap;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public void setStatoEsteroNascita(String statoEsteroNascita) {
		this.statoEsteroNascita = statoEsteroNascita;
	}

	public void setDocumento(TipoDocRiconoscimentoVO documento) {
		this.documento = documento;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public void setLuogoResidenza(String luogoResidenza) {
		this.luogoResidenza = luogoResidenza;
	}

	public void setStatoEsteroResidenza(String statoEsteroResidenza) {
		this.statoEsteroResidenza = statoEsteroResidenza;
	}

	public void setCittaEsteraResidenza(String cittaEsteraResidenza) {
		this.cittaEsteraResidenza = cittaEsteraResidenza;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}


	
}
