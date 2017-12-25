package fr.anses.ct.ldap.service.test;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;





import fr.anses.ct.ldap.persistance.impl.oe.User;
import fr.anses.ct.ldap.persistance.impl.oe.UserAuthentification;
import fr.anses.ct.ldap.persistance.impl.oe.UserPasseword;
import fr.anses.ct.ldap.service.ILdapService;
import fr.anses.ct.ldap.transverse.PropertyManager;
import fr.anses.ct.common.transverse.RfaException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/ldap-tech-context.xml", "/spring/ldap-persistance-context.xml", "/spring/ldap-domaine-context.xml", "/spring/ldap-service-context.xml"})
public class LdapServiceTest {
	 /** Ldap Service. **/
	  @Autowired
	  private ILdapService ldapService;
	  
	  /** Le logger. */
	  private static final Logger LOGGER = LoggerFactory.getLogger(LdapServiceTest.class);
	  
	  /** Flag de Nettoyage */
	  static private boolean cleanDone = false;
	  
	  @Test
	  public void ldap_s01_search_test1() {
		  
		LOGGER.info("ldap_s01_search_test1");
		if (cleanDone == false){
			clean();
		}
		
		try {
		    User user = ldapService.ldap_s01_search("cn=do not delete,ou=test unitaire,ou=people,dc=afssa,dc=fr");
		    Assert.assertNotNull(user);
		    Assert.assertEquals("do not delete", user.getAttributes().get("CN"));
		    Assert.assertEquals("cn=do not delete,ou=test unitaire,ou=people,dc=afssa,dc=fr", user.getDn());
		}
	    catch (RfaException re){
	    	LOGGER.error("Erreur dans le test ldap_s01_search_test1 : " + re.getMessage());
	    	Assert.assertNotNull(null);
	    }
	  }
	  

	  @Test
	  public void ldap_s02_create_modify_user_test2() {
		  LOGGER.info("ldap_s02_create_modify_user_test2");
		  if (cleanDone == false){
				clean();
			}
		  
		try {
		    User user = new User();
		    user.setDn("cn=test2,ou=test unitaire,ou=people,dc=afssa,dc=fr");
		    Map<String, Object> mapAttr = new HashMap<String, Object>();
		    mapAttr.put("objectClass", Arrays.asList("person"));
		    mapAttr.put(PropertyManager.getDnAttribut(), user.getDn());
		    mapAttr.put("cn", "test2");
		    mapAttr.put("displayName", "test2");
		    mapAttr.put("country", "FR");
		    mapAttr.put("departement", "Service étude et développement");
		    user.setAttributes(mapAttr);
		    ldapService.ldap_s02_create_modify_user(user);
		}
	    catch (RfaException re){
	    	LOGGER.error("Erreur dans le test ldap_s02_create_modify_user_test2 : " + re.getMessage());
	    	Assert.assertNotNull(null);
	    }
	  }
	  
	  @Test
	  public void ldap_s02_create_modify_user_test3() {
		  LOGGER.info("ldap_s02_create_modify_user_test3");
		  if (cleanDone == false){
				clean();
			}
		try {
			// création de test3
		    User user = new User();
		    user.setDn("cn=test3,ou=test unitaire,ou=people,dc=afssa,dc=fr");
		    Map<String, Object> mapAttr = new HashMap<String, Object>();
		    mapAttr.put("objectClass", Arrays.asList("person"));
		    mapAttr.put(PropertyManager.getDnAttribut(), user.getDn());
		    mapAttr.put("cn", "test3");
		    mapAttr.put("displayName", "test3");
		    mapAttr.put("country", "FR");
		    mapAttr.put("departement", "Service étude et développement");
		    user.setAttributes(mapAttr);
		    ldapService.ldap_s02_create_modify_user(user);
		    
		    // Modification de test3
		    User userToChange = new User();
		    userToChange.setDn("cn=test3,ou=test unitaire,ou=people,dc=afssa,dc=fr");
		    Map<String, Object> mapAttrChange = new HashMap<String, Object>();
		    mapAttrChange.put("displayName", "test3modified");
		    userToChange.setAttributes(mapAttrChange);
		    ldapService.ldap_s02_create_modify_user(userToChange);
		    
		    // Recherche de test3
		    User userChanged = ldapService.ldap_s01_search("cn=test3,ou=test unitaire,ou=people,dc=afssa,dc=fr");
		    Assert.assertEquals("test3modified", userChanged.getAttributes().get("DISPLAYNAME"));
		}
	    catch (RfaException re){
	    	LOGGER.error("Erreur dans le test ldap_s02_create_modify_user_test3 : " + re.getMessage());
	    	Assert.assertNotNull(null);
	    }
	  }
	  
	  @Test
	  public void ldap_s03_delete_user_test4() {
		  LOGGER.info("ldap_s03_delete_user_test4");
		  if (cleanDone == false){
				clean();
			}
		  	try {
				// création de test4
				User user = new User();
				user.setDn("cn=test4,ou=test unitaire,ou=people,dc=afssa,dc=fr");
				Map<String, Object> mapAttr = new HashMap<String, Object>();
				mapAttr.put("cn", "test4");
				mapAttr.put("objectClass", Arrays.asList("person"));
				mapAttr.put(PropertyManager.getDnAttribut(), user.getDn());
				mapAttr.put("displayName", "test4");
				mapAttr.put("country", "FR");
				mapAttr.put("departement", "Service étude et développement");
				user.setAttributes(mapAttr);
				ldapService.ldap_s02_create_modify_user(user);
				User userCreated = ldapService.ldap_s01_search("cn=test4,ou=test unitaire,ou=people,dc=afssa,dc=fr");
				Assert.assertNotNull(userCreated);
				ldapService.ldap_s03_delete_user(userCreated.getDn());
				User userDeleted = ldapService.ldap_s01_search(userCreated.getDn());
				Assert.assertNull(userDeleted);
			} catch (RfaException e) {
				LOGGER.error("Erreur dans le test ldap_s03_delete_user_test4 : " + e.getMessage());
				Assert.assertNotNull(null); 
			}
	  }
	  
	  @Test
	  public void ldap_s04_modify_password_test5() {
		  LOGGER.info("ldap_s04_modify_password_test5");
		  if (cleanDone == false){
				clean();
			}
		    try {
				// création de test5 avec le mot de passe par défaut
				User user = new User();
				user.setDn("cn=test5,ou=test unitaire,ou=people,dc=afssa,dc=fr");
				Map<String, Object> mapAttr = new HashMap<String, Object>();
				mapAttr.put("objectClass", Arrays.asList("person"));
				mapAttr.put(PropertyManager.getDnAttribut(), user.getDn());
				mapAttr.put("cn", "test5");
				mapAttr.put("displayName", "test5");
				mapAttr.put("country", "FR");
				mapAttr.put("departement", "Service étude et développement");
				user.setAttributes(mapAttr);
				ldapService.ldap_s02_create_modify_user(user);
				
				// Authentification avec le mot de passe par défaut
				UserAuthentification userAuthentification = new UserAuthentification();
				userAuthentification.setLogin("test5");
				userAuthentification.setPassword(PropertyManager.getDefaultPassword());
				User userAuthent = ldapService.ldap_s05_authentify(userAuthentification);
				Assert.assertNotNull(userAuthent);
				
				// Modification du mot de passe
				UserPasseword userPasseword = new UserPasseword();
				userPasseword.setDn("cn=test5,ou=test unitaire,ou=people,dc=afssa,dc=fr");
				userPasseword.setNewUserPasseword("Password2020");
				User userPwdModified = ldapService.ldap_s04_modify_user_passeword(userPasseword);
				Assert.assertNotNull(userPwdModified);
				
				// Authentification avec le mot de passe modifié
				userAuthentification.setPassword("Password2020");
				userAuthent = ldapService.ldap_s05_authentify(userAuthentification);
				Assert.assertNotNull(userAuthent);
			} catch (RfaException e) {
				LOGGER.error("Erreur dans le test ldap_s04_modify_password_test5 : " + e.getMessage());
				Assert.assertNotNull(null); 
			}
	  }
	  
	  
	  @Test
	  public void ldap_s05_search_by_filter_test6() {
		  	try {
				// création de test6 avec le mot de passe par défaut
				User user = new User();
				user.setDn("cn=test6,ou=test unitaire,ou=people,dc=afssa,dc=fr");
				Map<String, Object> mapAttr = new HashMap<String, Object>();
				mapAttr.put("objectClass", Arrays.asList("person"));
				mapAttr.put(PropertyManager.getDnAttribut(), user.getDn());
				mapAttr.put("cn", "test6");
				mapAttr.put("displayName", "test6");
				mapAttr.put("country", "FR");
				mapAttr.put("departement", "Service étude et développement");
				user.setAttributes(mapAttr);
				ldapService.ldap_s02_create_modify_user(user);
				
				// création de test7 avec le mot de passe par défaut
				user.setDn("cn=test7,ou=test unitaire,ou=people,dc=afssa,dc=fr");
				user.getAttributes().put(PropertyManager.getDnAttribut(), user.getDn());
				user.getAttributes().put("cn", "test7");
				user.getAttributes().put("displayName", "test7");
				ldapService.ldap_s02_create_modify_user(user);
				
				String baseDn = "ou=test unitaire,ou=people,dc=afssa,dc=fr";
				String filter = ("(&(objectClass=person)(cn=test*))");
				List<User> listUser = ldapService.ldap_s06_search_by_filter(baseDn, filter);
				Assert.assertEquals(5, listUser.size());
			} catch (RfaException e) {
				LOGGER.error("Erreur dans le test ldap_s05_search_by_filter_test6 : " + e.getMessage());
				Assert.assertNotNull(null); 
			}
	  }
	  
	
	  /**
	   * Desactivé par défaut.
	   * Permet de créer un utilisateur en intégration
	   */
	  private void ldap_s02_create_modify_user_for_integration() {
		  LOGGER.info("ldap_s02_create_modify_user_test2");
		
		try {
		    User user = new User();
		    user.setDn("cn=hhichri,ou=integration,ou=people,dc=afssa,dc=fr");
		    Map<String, Object> mapAttr = new HashMap<String, Object>();
		    mapAttr.put("objectClass", Arrays.asList("person"));
		    mapAttr.put(PropertyManager.getDnAttribut(), user.getDn());
		    mapAttr.put("cn", "hhichri");
		    mapAttr.put("displayName", "Houda Hichri");
		    mapAttr.put("country", "FR");
		    mapAttr.put("memberOf", "UNIT3");
		    mapAttr.put("departement", "Service étude et développement");
		    user.setAttributes(mapAttr);
		    ldapService.ldap_s02_create_modify_user(user);
		}
	    catch (RfaException re){
	    	LOGGER.error("Erreur dans le test ldap_s02_create_modify_user_test2 : " + re.getMessage());
	    	Assert.assertNotNull(null);
	    }
	  }
	  
	@Test
	public void ldap_s03_delete_user_test20(){
		ldapService.ldap_s03_delete_user("cn=test2,ou=test unitaire,ou=people,dc=afssa,dc=fr");
	}
	  
	@SuppressWarnings("static-access")
	private void clean()
	  {
		  try {
			  LOGGER.info("Suppression des données de la précédente exécution");
			  ldapService.ldap_s03_delete_user("cn=test2,ou=test unitaire,ou=people,dc=afssa,dc=fr");
			  ldapService.ldap_s03_delete_user("cn=test3,ou=test unitaire,ou=people,dc=afssa,dc=fr");
			  ldapService.ldap_s03_delete_user("cn=test4,ou=test unitaire,ou=people,dc=afssa,dc=fr");
			  ldapService.ldap_s03_delete_user("cn=test5,ou=test unitaire,ou=people,dc=afssa,dc=fr");
			  ldapService.ldap_s03_delete_user("cn=test6,ou=test unitaire,ou=people,dc=afssa,dc=fr");
			  ldapService.ldap_s03_delete_user("cn=test7,ou=test unitaire,ou=people,dc=afssa,dc=fr");
			  this.cleanDone = true;
		  }
		  catch (RfaException re){
			  LOGGER.error("Impossible de supprimer les données de la précédente exécution : " + re.getMessage());
		    }
	  }
}
