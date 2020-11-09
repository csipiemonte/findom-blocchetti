/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.dichiarazioniSA;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

import java.util.List;

public class ElementoDichiarazioniVO extends CommonalityVO{
	
	@MapTo(target=INHERIT)
	Integer id;
	
	@MapTo(target=INHERIT)
	Integer idPadre;
	
	@MapTo(target=INHERIT)
	String typeField; // checkbox, radio, testo, list
	
	@MapTo(target=INHERIT)
	String valueSelectedField;
	
	@MapTo(target=INHERIT)
	String valueNotSelectedField;
	
	@MapTo(target=INHERIT)
	String defaultValueField;
	
	@MapTo(target=INHERIT)
	String nameField;

	@MapTo(target=INHERIT)
	String outputNameField;
	
	@MapTo(target=INHERIT)
	String labelField;
	
	@MapTo(target=INHERIT)
	String classField;
	
	@MapTo(target=INHERIT)
	Boolean mandatory;
	
	@MapTo(target=INHERIT)
	String tagXml;

	@MapTo(target=INHERIT)
	String dependsOn;

	@MapTo(target=INHERIT)
	String dependsOnValue;

	@MapTo(target=INHERIT)
	String maxlength;
	
	@MapTo(target=INHERIT)
	String includeHTML;
	
	@MapTo(target=INHERIT)
	List<ElementoDichiarazioniVO> lista;

	@MapTo(target=INHERIT)
	Integer ordinamento;
	
	
	public Integer getId() {
		return id;
	}

	public Integer getIdPadre() {
		return idPadre;
	}

	public String getClassField() {
		return classField;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}

	public void setClassField(String classField) {
		this.classField = classField;
	}

	public String getTypeField() {
		return typeField;
	}

	public String getNameField() {
		return nameField;
	}

	public String getLabelField() {
		return labelField;
	}

	public List<ElementoDichiarazioniVO> getLista() {
		return lista;
	}

	public void setTypeField(String typeField) {
		this.typeField = typeField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public void setLista(List<ElementoDichiarazioniVO> lista) {
		this.lista = lista;
	}

	public String getValueSelectedField() {
		return valueSelectedField;
	}

	public String getValueNotSelectedField() {
		return valueNotSelectedField;
	}

	public void setValueSelectedField(String valueSelectedField) {
		this.valueSelectedField = valueSelectedField;
	}

	public void setValueNotSelectedField(String valueNotSelectedField) {
		this.valueNotSelectedField = valueNotSelectedField;
	}

	public String getDefaultValueField() {
		return defaultValueField;
	}

	public void setDefaultValueField(String defaultValueField) {
		this.defaultValueField = defaultValueField;
	}

	public String getOutputNameField() {
		return outputNameField;
	}

	public void setOutputNameField(String outputNameField) {
		this.outputNameField = outputNameField;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getTagXml() {
		return tagXml;
	}

	public void setTagXml(String tagXml) {
		this.tagXml = tagXml;
	}

	public String getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public String getDependsOnValue() {
		return dependsOnValue;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public void setDependsOnValue(String dependsOnValue) {
		this.dependsOnValue = dependsOnValue;
	}

	public String getIncludeHTML() {
		return includeHTML;
	}

	public void setIncludeHTML(String includeHTML) {
		this.includeHTML = includeHTML;
	}
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("ElementoDichiarazioniVO [");
		sb.append("id=" + id );
		sb.append(", ordinamento=" + ordinamento );
		sb.append(", idPadre=" + idPadre );
		sb.append(", typeField=" + typeField );
		sb.append(", valueSelectedField=" + valueSelectedField );
		sb.append(", valueNotSelectedField=" + valueNotSelectedField );
		sb.append(", defaultValueField=" + defaultValueField );
		sb.append(", nameField=" + nameField );
		sb.append(", outputNameField=" + outputNameField );
		sb.append(", labelField=" + labelField );
		sb.append(", classField=" + classField );
		sb.append(", mandatory=" + mandatory );
		sb.append(", tagXml=" + tagXml );
		sb.append(", dependsOn=" + dependsOn );
		sb.append(", dependsOnValue=" + dependsOnValue );
		sb.append(", maxlength=" + maxlength );
		sb.append(", includeHTML=" + includeHTML );
		sb.append(", lista=" + lista );
		sb.append("]");
		
		return sb.toString();
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	
}
