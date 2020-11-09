/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.ldap;

import it.csi.findom.blocchetti.common.vo.dipartimento.DipartimentoVO;
import it.csi.findom.blocchetti.commonality.Ldap;
import it.csi.findom.blocchetti.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

public class IpaLdap {

	
	private static Properties initProps(Logger logger) throws IOException{
		logger.info("[IpaLdap::initProps] BEGIN");
		String ldapUrl= Ldap.getInstance().getProperty("ldapUrl");
		String ldapUser= Ldap.getInstance().getProperty("ldapUser");
		String ldapPwd= Ldap.getInstance().getProperty("ldapPwd");		
		
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapUser);
		env.put(Context.SECURITY_CREDENTIALS, ldapPwd);
		logger.info("[IpaLdap::initProps]  END");
		return env;
	}

	public static List<DipartimentoVO> cercaDipartimentiIpa(String codiceFiscale, Logger logger) throws Exception {
		logger.info("[IpaLdap::cercaDipartimentiIpa] BEGIN");
		List<DipartimentoVO> dipartimentoIpaList = new ArrayList<>();
		
		if(StringUtils.isBlank(codiceFiscale)){
			return null;			
		}

		try{
			Properties env = initProps(logger);
			logger.info("[IpaLdap::cercaDipartimentiIpa] env inizializzato ");
			
			DirContext ctx = new InitialDirContext(env);			
			logger.info("[IpaLdap::cercaDipartimentiIpa] ctx inizializzato ");

			SearchControls ctls = new SearchControls();			 
			logger.info("[IpaLdap::cercaDipartimentiIpa] ctls inizializzato");

			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			logger.info("[IpaLdap::cercaDipartimentiIpa] searchScope settato");

			String radix = "c=it";			  

			// Faccio due ricerche : prima cerco il nodo padre e poi cerco i dipartimenti
			// Cerco nodo padre
			String filterRoot = "(&((objectclass=amministrazione) (codiceFiscaleAmm="+codiceFiscale+")))";			  
			logger.info("[IpaLdap::cercaDipartimentiIpa] filter="+filterRoot);

			NamingEnumeration<SearchResult> answer1 = ctx.search(radix, filterRoot, ctls);
			if(answer1==null){				  
				logger.info("[IpaLdap::cercaDipartimentiIpa] answer1 NULL, ritorno null");
				return null;
			}else{ 
				logger.info("[IpaLdap::cercaDipartimentiIpa] answer1.hasMoreElements = "+answer1.hasMoreElements());
			}

			String o = null;			  
			while (answer1.hasMoreElements()){

				SearchResult SearchResultCorrente = (SearchResult) answer1.nextElement();
				Attributes attributiSearchResultCorrente = SearchResultCorrente.getAttributes();

				if (attributiSearchResultCorrente == null){			    	
					logger.info("[IpaLdap::cercaDipartimentiIpa]  Attributes non valorizzato in SearchResultCorrenteAttributi di answer1 ");
					return null;
				}else{	
					
					o = getIpaValue(attributiSearchResultCorrente.get("o"), logger);			    	
					logger.info("[IpaLdap::cercaDipartimentiIpa] elemento con ID = o di Attributes vale:  " + o);
				}			
			}
			//cerco i dipartimenti del nodo trovato
			//prima era  String filterRoot = "(&((objectclass=amministrazione) (codiceFiscaleAmm="+queryParam+")))";	
			String filterDipartimenti = "(&((objectclass=*) (codiceFiscaleSFE="+codiceFiscale+")))";

			if(o != null && o != ""){
				radix = "o=" + o + "," + radix;
			}			 
			logger.info("[IpaLdap::cercaDipartimentiIpa] filterDipartimenti vale: " + filterDipartimenti);

			NamingEnumeration<SearchResult> answer2 = ctx.search(radix,filterDipartimenti, ctls);

			if(answer2 ==null){
				logger.info("[IpaLdap::cercaDipartimentiIpa] answer2 NULL ");
				return null;
			}else{
				logger.info("[IpaLdap::cercaDipartimentiIpa] answer2 hasMoreElements = "+answer2.hasMoreElements());
			}

			int i = 0;
			while (answer2.hasMoreElements()){
                
				SearchResult SearchResultDipCorrente = (SearchResult) answer2.nextElement();			   
				logger.info("[IpaLdap::cercaDipartimentiIpa] ciclo su answer2.hasMoreElements(), iterazione = "+ ++i + " -------------------------");

				Attributes AttributiSearchResultDipCorrente = SearchResultDipCorrente.getAttributes();    

				if (AttributiSearchResultDipCorrente == null){			    	 
					logger.info("[IpaLdap::cercaDipartimentiIpa]  Attributes non valorizzato in AttributiSearchResultDipCorrente ");
					return null;
				}else{
					DipartimentoVO curDipIpa = new DipartimentoVO();					
//					curDipIpa.setCodiceUnivocoUO(getIpaValue(AttributiSearchResultDipCorrente.get("CodiceUnivocoUO"), logger));
//					curDipIpa.setCodiceUO(getIpaValue(AttributiSearchResultDipCorrente.get("codiceUO"), logger));
//					curDipIpa.setCodice(getIpaValue(AttributiSearchResultDipCorrente.get("ou"), logger));					
					curDipIpa.setId(0L);
					curDipIpa.setCodice(getIpaValue(AttributiSearchResultDipCorrente.get("CodiceUnivocoUO"), logger));	
					curDipIpa.setDescrizione(getIpaValue(AttributiSearchResultDipCorrente.get("description"), logger));
					dipartimentoIpaList.add(curDipIpa);
					
					logger.info("[IpaLdap::cercaDipartimentiIpa] numero di attributi di AttributiSearchResultDipCorrente = "+AttributiSearchResultDipCorrente.size());
				}
			}
		} catch(Exception ne){			
			logger.error("[IpaLdap::cercaDipartimentiIpa] si e' verificato un errore: ", ne);	
			return null;
		}		
		logger.info("[IpaLdap::cercaDipartimentiIpa] END");

		return dipartimentoIpaList; 
	}

	private static String getIpaValue(Attribute attr, Logger logger) {
		String r = null;
		if(attr!=null){
			try {
				r = (String)attr.get();
			} catch (NamingException e) {
				logger.error("[IpaLdap::getIpaValue] si e' verificato un errore (NamingException) durante il recupero del valore dell'attributo avente ID = " + attr.getID() + "; l'errore e' ", e);
			}
		}
		return r;
	}
}
