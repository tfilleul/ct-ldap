package fr.anses.ct.ldap.transverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author $Author: hhichri $
 * @version $Revision: 0 $
 */
public final class PropertyManager {
  /** Log */
  private static final Logger LOG = LoggerFactory.getLogger(PropertyManager.class);

  /** String */
  private static final String PROPERTIES_FILE_NAME = "ldap.properties";
  /** String */
  private static final String DEFAULT_PROPERTIES_FILE_NAME = "ldap.default.properties";
  /** String */
  private static final String SCHEMA_PROPERTIES = "schema.properties";
  /** String */
  private static final String DEFAULT_SCHEMA_PROPERTIES = "schema.default.properties";
  /** Properties */
  private static final Properties PROPERTIES = new Properties();
  /** Input Stream */
  private static InputStream is = null;
  private static InputStream is_schema = null;
  /** Map Schema */
  private static Map<String, String> mapSchema = new HashMap<String, String>();
  
  static {
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
    if (is == null) {
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE_NAME);
      if (is == null) {
    	  LOG.error("Aucun fichier de configiration trouvé");
      }
      else {
    	  LOG.info("Utilisation du fichier " + DEFAULT_PROPERTIES_FILE_NAME);
      }
    }
    else {
    	LOG.info("Utilisation du fichier " + PROPERTIES_FILE_NAME);
    }
    
    if (is != null){
      try {
        PROPERTIES.load(is);
      } catch (IOException e) {
        LOG.error("Impossible de charger la configuration ldap : " + e.getMessage());
      }
    }
    
    is_schema = Thread.currentThread().getContextClassLoader().getResourceAsStream(SCHEMA_PROPERTIES);
    if (is_schema == null) {
    	is_schema = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_SCHEMA_PROPERTIES);
      if (is_schema == null) {
    	  LOG.error("Aucun fichier de définition du schéma de l'annuaire trouvé");
      }
      else {
    	  LOG.info("Utilisation du fichier " + DEFAULT_SCHEMA_PROPERTIES);
      }
    }
    else {
    	LOG.info("Utilisation du fichier " + SCHEMA_PROPERTIES);
    }
    
    if (is_schema != null) {
    	loadMapSchema();
    }
  }

 
  /**
   * @return String
   */
  public static String getLdapUrl() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("ldapUrl");
    }
    return result;
  }

  /**
   * @return String
   */
  public static String getLdapUsername() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("ldapUsername");
    }
    return result;
  }

  /**
   * @return String
   */
  public static String getLdapPassword() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("ldapPassword");
    }
    return result;
  }

  /**
   * @return String
   */
  public static String getAttributUserPasseword() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("userPasseword");
    }
    return result;
  }

  /**
   * @return
   */
  public static String getUnicodePwd() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("uniCodePwd");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getLdapType() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("ldapType");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getDefaultPassword() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("defaultPassword");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getUserBaseDn() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("userBaseDn");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getAuthAttributLogin() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("authAttributLogin");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getAuthLdapSeachUserFilter() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("authLdapSeachUserFilter");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getAuthUserObjectClass() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("authUserObjectClass");
    }
    return result;
  }
  
  /**
   * @return
   */
  public static String getDnAttribut() {
    String result = null;
    if (PROPERTIES != null) {
      result = PROPERTIES.getProperty("dnAttribut");
    }
    return result;
  }
  
  
  
  /**
   * @return
   */
  public static Map<String, String> getMapSchema() {
    return mapSchema;
  }
  
  
  
  private static void loadMapSchema() {
	  LOG.info("Chargement du schéma");
	  InputStreamReader isr = new InputStreamReader(is_schema);
	  CSVParser parser = null;
	try {
		 parser = new CSVParser(isr, CSVFormat.newFormat(';'));		  
		  for (CSVRecord record : parser.getRecords()){
			  String attr = record.get(0);
			  String conf = record.get(1);
			  // On met en majscule car ldap n'est pas case sensitive
			  mapSchema.put(StringUtils.upperCase(attr), conf);
		  }
	} catch (IOException e) {
		LOG.error("Erreur lors de la lecture du fichier de configuration du schéma");
	}
	finally {
		try {
			if (isr != null){
			isr.close();
			}
			if (parser != null) {
				parser.close();
			}
		} catch (IOException e) {
			LOG.info("Impossible de fermer le stream du fichier de configuration du schéma");
		}
	}

  }

}
