/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.business;

import freemarker.core.Environment.Namespace;
import freemarker.template.Configuration;
import freemarker.template.Template;
import it.csi.findom.blocchetti.progetti.FinDirettiva;
import it.csi.findom.blocchetti.util.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLXML;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class Assembler2 {
  
  private static final String SOURCE_PATH = "src/java/it/csi/findom/blocchetti/progetti/";
  private static final String PATH_GEN_TEMP = "build/gen_temp/";
  private static final String PATH_GEN = "build/gen/";
  private static final String RUNCLASS = "!RUNCLASS!:it.csi.findom.blocchetti.progetti.";
  static private final String ASSEMBLER_FTL_PLACEHOLDER = "FTL_PLACEHOLDER";
  static private final String ASSEMBLER_FTL_PLACEHOLDER_DUMMY = "${" + ASSEMBLER_FTL_PLACEHOLDER + "}";
  static private final String ASSEMBLER_FTL_PLACEHOLDER_VALUE = "${";
  
  // se valorizzata salva solo la pagina indicata, se null salva tutto il bando
//  static private final String UPDATE_ONLY_PAGE = "S4_P1";
  
  private static void updateDirettiva(String applicativo, String bando, int templateId, String ambiente, String onlyThisPage, Connection conn) throws Exception {
    System.out.println("Update Direttiva "+applicativo+"-"+bando+" su "+ambiente);
    FinDirettiva direttiva = (FinDirettiva)Class.forName("it.csi.findom.blocchetti.progetti."+applicativo+"."+bando+".Direttiva").getConstructor(TreeMap.class,Namespace.class,TreeMap.class).newInstance(null,null,null);
    direttiva.initConfigurations();
    Map<String,String> conf = direttiva.getConfiguration();
    File[] dirs = new File(SOURCE_PATH+applicativo+"/"+bando).listFiles();
    for(File dir: dirs) {
      if(dir.isDirectory()) {
    	  if(!".svn".equals(dir.getName())){
    		  updateSezione(applicativo,bando,dir.getName(),templateId,ambiente,onlyThisPage,conf,conn);
    	  }
      }
    }
    if(StringUtils.isBlank(onlyThisPage)){
	    String runclass = RUNCLASS+applicativo+"."+bando+".Direttiva";
	    String sql = "UPDATE aggr_t_template set model_validation_rules=null,command_validation_rules=null,global_validation_rules=? WHERE template_id=?";
	    System.out.println("  "+sql+" [templateId="+templateId+"]");
	    PreparedStatement ps = conn.prepareStatement(sql);
	    ps.setString(1, runclass);
	    ps.setInt(2, templateId);
	    ps.execute();
	    ps.close();
    }else{
    	System.out.println("Update Direttiva skipped");
    }
  }
  
  private static void updateSezione(String applicativo, String bando, String sezione, int templateId, String ambiente, String onlyThisPage, Map<String,String> conf, Connection conn) throws Exception {
    System.out.println("Update Sezione "+applicativo+"-"+bando+"."+sezione+" su "+ambiente);
    File[] dirs = new File(SOURCE_PATH+applicativo+"/"+bando+"/"+sezione).listFiles();
    for(File dir: dirs) {
      if(dir.isDirectory()) {
    	  if(!".svn".equals(dir.getName())){
    		  updatePagina(applicativo,bando,sezione,dir.getName(),templateId,ambiente,onlyThisPage,conf,conn);
    	  }
      }
    }
    if(StringUtils.isBlank(onlyThisPage)){
	    String runclass = RUNCLASS+applicativo+"."+bando+"."+sezione+".Section";
	    String xpath = "//*[@id='"+sezione+"']";
	    String sql = "UPDATE aggr_t_template_index SET index_rules=?,template_page=null,model_validation_rules_page=null,command_validation_rules_page=null WHERE template_fk=? and xpath_query=?";
	    System.out.println("  "+sql+" [templateId="+templateId+",xpath="+xpath+"]");
	    PreparedStatement ps = conn.prepareStatement(sql);
	    ps.setString(1, runclass);
	    ps.setInt(2, templateId);
	    ps.setString(3, xpath);
	    ps.execute();
	    ps.close();
    }else{
    	System.out.println("Update Sezione skipped");
    }
  }
  
  private static void updatePagina(String applicativo, String bando, String sezione, String pagina, int templateId, String ambiente, String onlyThisPage, Map<String,String> conf, Connection conn) throws Exception {
    System.out.println("Update Pagina "+applicativo+"-"+bando+"."+sezione+"."+pagina+"."+templateId+" su "+ambiente);
    String dirName = applicativo+"/"+bando+"/"+sezione+"/"+pagina;
    System.out.println("dirName "+dirName);
    String runclass = RUNCLASS+applicativo+"."+bando+"."+sezione+"."+pagina+".Page";
    
    if(StringUtils.isBlank(onlyThisPage) || StringUtils.equals(sezione+"_"+pagina, onlyThisPage)){
	    writeTemplate1(SOURCE_PATH, PATH_GEN_TEMP, dirName,conf);
	    String template = writeTemplate2(PATH_GEN_TEMP, PATH_GEN, dirName,conf, runclass);
	    String xpath = "//*[@id='"+sezione+"_"+pagina+"']";
	    String sql = "UPDATE aggr_t_template_index SET index_rules=?,template_page=?,model_validation_rules_page=?,command_validation_rules_page=? WHERE template_fk=? and xpath_query=?";
	    System.out.println("PAG  "+sql+" [templateId="+templateId+",xpath="+xpath+"]");
	    PreparedStatement ps = conn.prepareStatement(sql);
	    ps.setString(1, runclass);
	    SQLXML templateXML = conn.createSQLXML();
	    templateXML.setString(template);
	    ps.setSQLXML(2, templateXML);
	    ps.setString(3, runclass);
	    ps.setString(4, runclass);
	    ps.setInt(5, templateId);
	    ps.setString(6, xpath);
	    ps.execute();
	    ps.close();
    }else{
    	System.out.println("Update Sezione " +sezione+" ,Pagina " +pagina+" skipped");
    }
  }
  
  private static void writeTemplate1(String dirIn, String dirOut, String dirName, Map<String,String> conf) throws Exception {
    writeTemplate(dirIn, dirOut, dirName, ASSEMBLER_FTL_PLACEHOLDER_DUMMY,conf);
  }
  
  private static String writeTemplate2(String dirIn, String dirOut, String dirName, Map<String,String> conf, String runclass) throws Exception {
    writeTemplate(dirIn, dirOut, dirName, ASSEMBLER_FTL_PLACEHOLDER_VALUE,conf);
    String s = new String(Files.readAllBytes(Paths.get(dirOut+dirName+"/template.xhtml")));
    int i = s.indexOf("[/@InjectModel]");
    s = "[#ftl/]\n"+s.substring(0, i)+runclass+"\n\t\t"+s.substring(i);
    Files.write(Paths.get(dirOut+dirName+"/template.xhtml"), Arrays.asList(s), Charset.forName("UTF-8"));
    return s;
  }
  
  private static void writeTemplate(String dirIn, String dirOut, String dirName, String ftlPlaceholder, Map<String,String> conf) throws Exception {
    Configuration cfg = new Configuration();
    Template tmpl = cfg.getTemplate(dirIn+dirName+"/template.xhtml");
    Map<String, Object> replacer = new HashMap<String, Object>();
    replacer.putAll(conf);
    replacer.put(ASSEMBLER_FTL_PLACEHOLDER, ftlPlaceholder);
    File dir = new File(dirOut+dirName);
    if(!dir.exists()) dir.mkdirs();
    System.out.println("     dirOut="+dirOut+dirName);
    OutputStream os = new FileOutputStream(dirOut+dirName+"/template.xhtml");
    Writer out = new OutputStreamWriter(os);
    tmpl.process(replacer, out);
    out.close();
    os.flush();
    os.close();
  }
  
  private static int getTemplateId(String bando, Connection conn) throws Exception {
    int templateId = -1;
    String sql = "select template_id from aggr_t_template where template_code=?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, bando);
    ResultSet rs = ps.executeQuery();
    if(rs.next()) {
      templateId = rs.getInt(1);
    }
    rs.close();
    ps.close();
    return templateId;
  }
  
  private static void cleanup() throws Exception {
    File directoryGen = new File(PATH_GEN);
    if (directoryGen.exists()) {
      FileUtils.deleteDirectory(directoryGen);
    }
    File directoryTmp = new File(PATH_GEN_TEMP);
    if (directoryTmp.exists()) {
      FileUtils.deleteDirectory(directoryTmp);
    }
  }

  private static Connection getConnection(String ambiente) throws Exception {
    Properties p = new Properties();
    p.load(new FileReader(new File("buildfiles/assembler-"+ambiente+".properties")));
    Class.forName("org.postgresql.Driver");
    Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"));
    System.out.println("Ottenuta connessione per ambiente "+ambiente);
    return con;
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 4) {
      System.out.println("Usage: Assembler2 <applicativo>[presentazione/istruttoria] <bando> ambiente[local/dev/tst/coll/prod] <bandopack> <pagina>");
      System.exit(0);
    } else {
      String applicativo = args[0];
      String bando = args[1];
      String ambiente = args[2];
      String bando_pack = args[3];
      String onlyThisPage = null;
      if(args.length>4)
    	  onlyThisPage = args[4];  // se valorizzata crea e salva sul db solo questa pagina
      cleanup();
      Connection conn = getConnection(ambiente);
      try {
        int templateId = getTemplateId(bando, conn);
        if(templateId==-1) {
          System.out.println("Bando "+bando+" inesistente su ambiente "+ambiente);
        }
        else {
            updateDirettiva(applicativo,bando_pack,templateId,ambiente,onlyThisPage,conn);
        }
      }
      finally {
        conn.close();
      }
    }

  }
}
