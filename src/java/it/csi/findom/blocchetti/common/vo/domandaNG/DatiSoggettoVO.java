/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.vo.domandaNG;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class DatiSoggettoVO extends CommonalityVO {
			
  @MapTo(target = INHERIT, name = "idSoggetto")
  Integer idSoggetto;

  @MapTo(target = INHERIT, name = "codiceFiscale")
  String codiceFiscale;
  
  @MapTo(target = INHERIT, name = "denominazione")
  String denominazione;

  @MapTo(target = INHERIT, name = "idFormaGiuridica")
  Integer idFormaGiuridica;

  @MapTo(target = INHERIT, name = "cognome")
  String cognome;
  
  @MapTo(target = INHERIT, name = "nome")
  String nome;
  
  @MapTo(target = INHERIT, name = "codiceUnitaOrganizzativa")
  String codiceUnitaOrganizzativa;
  
  @MapTo(target = INHERIT, name = "siglaNazione")
  String siglaNazione;


  @Override
  public String toString() {
	  return "DatiSoggettoVO [idSoggetto=" + idSoggetto + ", codiceFiscale=" + codiceFiscale + ", denominazione=" + denominazione + ", idFormaGiuridica=" + idFormaGiuridica + 
			  "cognome=" + cognome + ", nome=" + nome + ", codiceUnitaOrganizzativa=" + codiceUnitaOrganizzativa + ", siglaNazione = " + siglaNazione +  "]";
  }


  public Integer getIdSoggetto() {
	  return idSoggetto;
  }


  public void setIdSoggetto(Integer idSoggetto) {
	  this.idSoggetto = idSoggetto;
  }


  public String getDenominazione() {
	  return denominazione;
  }


  public void setDenominazione(String denominazione) {
	  this.denominazione = denominazione;
  }


  public Integer getIdFormaGiuridica() {
	  return idFormaGiuridica;
  }


  public void setIdFormaGiuridica(Integer idFormaGiuridica) {
	  this.idFormaGiuridica = idFormaGiuridica;
  }


  public String getCognome() {
	  return cognome;
  }


  public void setCognome(String cognome) {
	  this.cognome = cognome;
  }


  public String getNome() {
	  return nome;
  }


  public void setNome(String nome) {
	  this.nome = nome;
  }


  public String getCodiceUnitaOrganizzativa() {
	  return codiceUnitaOrganizzativa;
  }


  public void setCodiceUnitaOrganizzativa(String codiceUnitaOrganizzativa) {
	  this.codiceUnitaOrganizzativa = codiceUnitaOrganizzativa;
  }


  public String getSiglaNazione() {
	  return siglaNazione;
  }


  public void setSiglaNazione(String siglaNazione) {
	  this.siglaNazione = siglaNazione;
  }

}
