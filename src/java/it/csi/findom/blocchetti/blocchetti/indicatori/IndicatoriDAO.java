/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.blocchetti.indicatori;

import it.csi.findom.blocchetti.common.util.DecimalFormat;
import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityException;
import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndicatoriDAO {

  private static final String QUERY_INDICATORI = "SELECT a.id_indicatore AS idIndicatore, " 
		  + " b.codice AS codIndicatore,  " 
		  + " b.descrizione AS descrIndicatore,   "       
		  + " b.unita_misura AS unitaMisuraIndicatore, " 
		  + " b.id_tipo_indicatore AS idTipoIndicatore, " 
		  + " b.link AS linkIndicatore, "
		  + " c.descrizione AS descrTipoIndicatore, " 
		  + " a.flag_obbligatorio AS flagObbligatorio, "
		  + " a.flag_alfa::text AS flagAlfa, "
		  + " '' AS valoreIndicatore "
		  + " FROM findom_r_bandi_indicatori a,  " 
		  + " findom_d_indicatori b,  " 
		  + " findom_d_tipi_indicatori c  " 
		  + " WHERE id_bando = :idBando and "      
		  + " a.id_indicatore = b.id_indicatore and  " 
		  + " b.id_tipo_indicatore = c.id_tipo_indicatore  "; 
  
  public static	List<IndicatoreResultVO> getIndicatoriList(String idBando, String dataInvio, Logger logger) throws CommonalityException {		
			      
	    String query = QUERY_INDICATORI;
	    
		if(StringUtils.isBlank(dataInvio)) {
		    query +=" and (date_trunc('day', a.dt_inizio) <= CURRENT_DATE and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= CURRENT_DATE))  ";
		}else{
		    query +=" and (date_trunc('day',a.dt_inizio) <= to_date('" + dataInvio + "', 'dd/MM/yyyy') and (a.dt_fine is null or date_trunc('day',a.dt_fine) >= to_date('" + dataInvio + "', 'dd/MM/yyyy')))  ";
		}
			
		query += " order by c.descrizione ASC, b.descrizione ASC ";
							
		logger.debug("IndicatoriDAO::getIndicatoriList]  idBando="+ idBando + " , dataInvio: " + dataInvio + ") query:" + query);
		
	  
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate((DataSource) ServiceBeanLocator.getBeanByName("dataSource"));
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("idBando", idBando, Types.NUMERIC);
	    List<IndicatoreResultVO> tmpList = null;
	    
	    try {
	    	 tmpList = jdbcTemplate.query(query, 
	    			 namedParameters, new BeanPropertyRowMapper<>(IndicatoreResultVO.class));
	    }
	    catch (DataAccessException e) {
	    	throw new CommonalityException(e);
	    }
		    
	    return tmpList;
  }
  
  @SuppressWarnings("rawtypes")
  public static TipoIndicatoreVO[] raggruppaIndicatoriPerTipoIndicatore(List<IndicatoreResultVO> resultIndicatoriList, Logger logger) throws CommonalityException {		

		logger.debug("IndicatoriDAO::raggruppaIndicatoriPerTipoIndicatore] BEGIN resultIndicatoriList:" + resultIndicatoriList);
		
		if(resultIndicatoriList==null || resultIndicatoriList.isEmpty()){
			return new TipoIndicatoreVO[0];
	  	}
				
	    
		List parIndicatoriList = raggruppaInLista(resultIndicatoriList, logger);
	  
	  	if(parIndicatoriList==null || parIndicatoriList.isEmpty()){
			return new TipoIndicatoreVO[0];
	  	}		 

	  	TipoIndicatoreVO[] retList = new TipoIndicatoreVO[parIndicatoriList.size()];
	  	
	  	for(int i=0; i<parIndicatoriList.size();i++){			   		           
	  		
	  		Map curTipoIndicatore=(Map)parIndicatoriList.get(i);
	  		TipoIndicatoreVO tmpTipoIndicatoreMap = new TipoIndicatoreVO();
	  		tmpTipoIndicatoreMap.setIdTipoIndicatore((String)curTipoIndicatore.get("idTipoIndicatore"));
	  		tmpTipoIndicatoreMap.setDescrTipoIndicatore((String)curTipoIndicatore.get("descrTipoIndicatore"));

	  		List curIndicatoriList = (List)curTipoIndicatore.get("indicatoriList");
	  		
	  		IndicatoreItemVO[] indicatoriList = new IndicatoreItemVO[curIndicatoriList.size()];
	  		for(int j=0; j<curIndicatoriList.size();j++){
	  			 
		  		Map curIndicatore=(Map)curIndicatoriList.get(j);
	  			IndicatoreItemVO tmpIndicatoreMap = new IndicatoreItemVO();		
				 
				  tmpIndicatoreMap.setIdIndicatore((String)curIndicatore.get("idIndicatore"));				      
				  tmpIndicatoreMap.setCodIndicatore((String)curIndicatore.get("codIndicatore"));			      
				  tmpIndicatoreMap.setDescrIndicatore((String)curIndicatore.get("descrIndicatore"));
				  tmpIndicatoreMap.setUnitaMisuraIndicatore((String)curIndicatore.get("unitaMisuraIndicatore"));			      
			      tmpIndicatoreMap.setLinkIndicatore((String)curIndicatore.get("linkIndicatore"));
				  tmpIndicatoreMap.setFlagObbligatorio((String)curIndicatore.get("flagObbligatorio")); 
				  tmpIndicatoreMap.setFlagAlfa((String)curIndicatore.get("flagAlfa")); 
				  tmpIndicatoreMap.setValoreIndicatore(DecimalFormat.decimalFormat((String)curIndicatore.get("valoreIndicatore"),2));
				  
				  indicatoriList[j] = tmpIndicatoreMap;	  			
	  		}
	  		tmpTipoIndicatoreMap.setIndicatoriList(indicatoriList);
	  	
	  		retList[i] = tmpTipoIndicatoreMap;
	  	}
	  	
		logger.debug("IndicatoriDAO::raggruppaIndicatoriPerTipoIndicatore] END retList:" + retList);

		return retList;
	}
  
  private static List<Map<String, Object>> raggruppaInLista(List<IndicatoreResultVO> parIndicatoriList, Logger logger){
	  logger.debug("IndicatoriDAO::raggruppaInLista] BEGIN "); 	   
	  
	  List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();	   
	  if(parIndicatoriList==null || parIndicatoriList.isEmpty()){
			return retList;
	  }		 

      String idTipoIndicatoreCorrente = ""; //valorizzato con gli idTipoIndicatore via via trovati nel corso dell'iterazione
       
	   for(int i=0; i<parIndicatoriList.size();i++){			   		           
		  IndicatoreResultVO curIndicatore=(IndicatoreResultVO)parIndicatoriList.get(i);
		  if(curIndicatore!=null){
		     //gli elementi 'piatti' dell'indicatore corrente
		     String idTipoIndicatore      = curIndicatore.getIdTipoIndicatore()     == null ? "" : ((String)curIndicatore.getIdTipoIndicatore());
		     String descrTipoIndicatore   = curIndicatore.getDescrTipoIndicatore()  == null ? "" : ((String)curIndicatore.getDescrTipoIndicatore());
		     String idIndicatore          = curIndicatore.getIdIndicatore()         == null ? "" : ((String)curIndicatore.getIdIndicatore());
		     String codIndicatore         = curIndicatore.getCodIndicatore()        == null ? "" : ((String)curIndicatore.getCodIndicatore());
		     String descrIndicatore       = curIndicatore.getDescrIndicatore()      == null ? "" : ((String)curIndicatore.getDescrIndicatore());
		     String unitaMisuraIndicatore = curIndicatore.getUnitaMisuraIndicatore()== null ? "" : ((String)curIndicatore.getUnitaMisuraIndicatore());
		     String valoreIndicatore      = curIndicatore.getValoreIndicatore()     == null ? "" : ((String)curIndicatore.getValoreIndicatore());
		     String linkIndicatore        = curIndicatore.getLinkIndicatore()       == null ? "" : ((String)curIndicatore.getLinkIndicatore());
		     String flagObbligatorio      = curIndicatore.getFlagObbligatorio()     == null ? "" : ((String)curIndicatore.getFlagObbligatorio());
		     String flagAlfa      		  = curIndicatore.getFlagAlfa()     		== null ? "" : ((String)curIndicatore.getFlagAlfa());
		
		     //mappa con l'indicatore corrente, che successivamente si stabilisce a quale elemento di retList attribuire
		     Map<String, String> tmpIndicatoreMap = new LinkedHashMap<String, String>();		        
		     tmpIndicatoreMap.put("idIndicatore",idIndicatore);
		     tmpIndicatoreMap.put("codIndicatore",codIndicatore);
		     tmpIndicatoreMap.put("descrIndicatore",descrIndicatore);
		     tmpIndicatoreMap.put("unitaMisuraIndicatore",unitaMisuraIndicatore);
		     tmpIndicatoreMap.put("valoreIndicatore",DecimalFormat.decimalFormat(valoreIndicatore,2));
		     tmpIndicatoreMap.put("linkIndicatore",linkIndicatore);
		     tmpIndicatoreMap.put("flagObbligatorio",flagObbligatorio);
		     tmpIndicatoreMap.put("flagAlfa",flagAlfa);
		      
		       
		     if(!idTipoIndicatoreCorrente.equals(idTipoIndicatore)){
		    	 logger.debug("IndicatoriDAO::raggruppaInLista] sono nell'if;  idTipoIndicatore = " + idTipoIndicatore + " ; descrTipoIndicatore = " + descrTipoIndicatore); 		        
		        //sono alla prima iterazione o e' variato l'id Tipo Indicatore rispetto all'iterazione precedente
		        Map<String, Object> tmpTipoIndicatoreMap = new LinkedHashMap<String, Object>();		        
		        tmpTipoIndicatoreMap.put("idTipoIndicatore", idTipoIndicatore);
		        tmpTipoIndicatoreMap.put("descrTipoIndicatore", descrTipoIndicatore);		        		        
		        
		        List<Map<String, String>> curIndicatoriList = new ArrayList<Map<String, String>>();		        
		        curIndicatoriList.add(tmpIndicatoreMap);   
		        tmpTipoIndicatoreMap.put("indicatoriList",curIndicatoriList);	
		        logger.debug("IndicatoriDAO::raggruppaInLista] if;  valorizzato la indicatoriList di tmpTipoIndicatoreMap"); 		        
		       	        		        
		        retList.add(tmpTipoIndicatoreMap);
		        
		        idTipoIndicatoreCorrente = idTipoIndicatore;
		           
		     }else { 
		          //sto considerando un indicatore avente un id tipo indicatore gia' considerato all'iterazione precedente
		    	 logger.debug("IndicatoriDAO::raggruppaInLista] else;  idTipoIndicatore = " + idTipoIndicatore + " ; descrTipoIndicatore = " + descrTipoIndicatore); 		        
		          
		          if(retList!=null && !retList.isEmpty()){
		             for(int j=0; j<retList.size(); j++ ){
	                    Map<String, Object> tmp = (Map<String, Object>) retList.get(j);
	                    if(tmp!=null){
	                       idTipoIndicatore = (tmp.get("idTipoIndicatore") ==null ? "" : ((String) tmp.get("idTipoIndicatore")));
	                       if(idTipoIndicatore.equals(idTipoIndicatoreCorrente)){
	                    	  logger.debug("IndicatoriDAO::raggruppaInLista] else; ho trovato in retList il tipo indicatore  idTipoIndicatore = " + idTipoIndicatore + " ; descrTipoIndicatore = " + descrTipoIndicatore); 		        
		         
	                          List<Map<String, String>> curIndicatoriList = (tmp.get("indicatoriList") == null ? new ArrayList<Map<String, String>>() : ((List<Map<String, String>>) tmp.get("indicatoriList")));
	                          logger.debug("IndicatoriDAO::raggruppaInLista] else; la indicatoriList del tipo indicatore  " + idTipoIndicatore + " ha  " + curIndicatoriList.size() + " elementi; aggiungo la mappa avente idIndicatore " + idIndicatore ); 		        
		         
		                      curIndicatoriList.add(tmpIndicatoreMap);
		                      tmp.put("indicatoriList",curIndicatoriList);
	                          break;
	                       }
	                    }
	                 }//chiude for su retList		             
		         } //chiude test null su retList
		         
		     } //chiude if - else su valore di idTipoIndicatoreCorrente		     
		  } //chiude test null su curIndicatore		  
	   } //chiude il for su parIndicatoriList
		  
	   logger.debug("IndicatoriDAO::raggruppaInLista] END "); 
		return retList;
	}

}
