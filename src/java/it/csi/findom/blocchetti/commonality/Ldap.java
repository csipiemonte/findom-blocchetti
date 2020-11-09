/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.commonality;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Ldap {
  
  private static Ldap instance = new Ldap();

  private Properties properties;
  
  public static Ldap getInstance() {
    return instance;
  }
  
  public String getProperty(String key) throws IOException {
    if(properties==null) {
      Properties properties = new Properties();
      InputStream stream = this.getClass().getResourceAsStream("/ipa.properties");
      properties.load(stream);
      this.properties = properties;
    }
    return properties.getProperty(key);
  }
}
