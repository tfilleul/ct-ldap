package fr.anses.ct.ldap.persistance.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;




import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;




import static org.springframework.ldap.query.LdapQueryBuilder.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.anses.ct.ldap.persistance.ILdapPersistance;
import fr.anses.ct.ldap.persistance.impl.oe.UserAttributesMapper;
import fr.anses.ct.ldap.transverse.ILdapMessage;
import fr.anses.ct.ldap.transverse.PropertyManager;
import fr.anses.ct.common.transverse.ExceptionFactory;
import fr.anses.ct.common.transverse.LdapUtils;
import fr.anses.ct.ldap.persistance.impl.oe.SearchFilter;
import fr.anses.ct.ldap.persistance.impl.oe.User;
import fr.anses.ct.ldap.persistance.impl.oe.UserAuthentification;
import fr.anses.ct.ldap.persistance.impl.oe.UserContextMapper;
import fr.anses.ct.ldap.persistance.impl.oe.UserPasseword;

public class LdapPersistanceImpl implements ILdapPersistance {
	
  private ExceptionFactory exceptionFactory;
  
  private LdapTemplate ldapTemplate;

  /** Le logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(LdapPersistanceImpl.class);
  
  
  /**
   * 
   * @param dn
   * @return
   */
  @SuppressWarnings("unchecked")
public User ldap_s01_search(final String dn) {
	  User retUser = null;	  
	  LOGGER.info("ldap_s01_search(dn=" + dn + ")"); 
	  try {
			 LdapContextSource ldapCtx = (LdapContextSource) ldapTemplate.getContextSource();
			 if (ldapCtx.getUrls() != null){
				 int lg = ldapCtx.getUrls().length;
				 for (int i=0; i<lg; i++) {
					 LOGGER.debug("Url" + i + " : " + ldapCtx.getUrls()[i]);
				 }
			 }
			 
		  retUser = ldapTemplate.lookup(dn, new UserContextMapper());		 
		  retUser.initDn();
		  String attrLogin = PropertyManager.getAuthAttributLogin();
		  String login = (String) retUser.getAttributes().get(StringUtils.upperCase(attrLogin));
		  retUser.setUsername(login);
		  LOGGER.info("Found user dn : " + retUser.getDn());
		  return retUser;
	  }
	  catch (NameNotFoundException nnfe){
		  LOGGER.info("Utilisateur suivant non trouvé : " + dn);
		  return null;
	  }
  }
  
 
  
  /**
   * {@inheritDoc}
   */
  public String ldap_s02_create_modify_user(final User user){
	    User userCherche = ldap_s01_search(user.getDn());
	    if (userCherche == null) {
	      return ldap_s02_create_user(user);
	    }
	    else {
	    	return ldap_s02_modify_user(user);
	    }
  }
  
  /**
   * Création d'un utilisateur
   * @param user
   * @return dn
   */
  private String ldap_s02_create_user(final User user){
	  String dn = user.getDn();
      DirContextAdapter context = new DirContextAdapter(dn); 
      mapToContext(user, context);
      ldapTemplate.bind(context);
      
	  UserPasseword up = new UserPasseword();
	  up.setDn(dn);
	  up.setNewUserPasseword(PropertyManager.getDefaultPassword());
	  this.ldap_s04_modify_user_passeword(up);

	  LOGGER.info("Utilisateur créé avec succès : " + dn);
	  
	  return dn;
  }
  
  
  /**
   * Modification des attributs d'un utilisateur
   * @param user
   * @return dn
   */
  private String ldap_s02_modify_user(final User user){
	  String dn = user.getDn();
	  DirContextOperations context = ldapTemplate.lookupContext(dn);
	  mapToContext(user, context);
      ldapTemplate.modifyAttributes(context);	  
      LOGGER.info("Utilisateur modifié avec succès : " + dn);
	  return dn;
 }

  /**
   * Construction des attributs d'un utilisateur
   * @param user
   * @return attributes
   */
  private Attributes buildAttributes(User user) {
      Attributes attrs = new BasicAttributes();
      if (PropertyManager.getLdapType().equalsIgnoreCase("activedirectory")){
    	  attrs.put("objectclass", "user");
      }
      attrs.put("objectclass", "person");
      Map<String, Object> mapAttr = user.getAttributes();
      for (String cle : mapAttr.keySet()) {
    	  attrs.put(cle, mapAttr.get(cle));
      }
      
      /** Initialisation du mot de passe */
      String defaultPassword = PropertyManager.getDefaultPassword();
      if (PropertyManager.getLdapType().equalsIgnoreCase("activedirectory")){
	      attrs.put("userAccountControl", "" + 512);
	      attrs.put("unicodepwd", encodePassword(defaultPassword));
      }
      else 
      {	
    	  // Hypothèse : openLdap
    	  attrs.put("userPassword", defaultPassword);
      }
      return attrs;
   }
  
  /**
   * ldap_s02_create_user_spring
   * @param user
   * @return
   */
  @Deprecated
  private String ldap_s02_create_user_spring(final User user)  {
	  		LOGGER.info("Appel de ldap_s02_create_user_spring");
	  		String dn = "CN=testSpring,OU=Groupes test sed,OU=Groupes,DC=afssa,DC=fr";
			Attributes userAttributes = new BasicAttributes();
			userAttributes.put("objectclass", "person");
			userAttributes.put("objectclass", "user");
			userAttributes.put("userPrincipalName", "testSpring@gmail.com");
			userAttributes.put("sAMAccountName", "testSpring");
			userAttributes.put("givenName", "testSpring");
			userAttributes.put("sn", "testSpring");
			userAttributes.put("cn", "testSpring");
			userAttributes.put("displayName","testSpring");
			userAttributes.put("userAccountControl", "" + 512);
			userAttributes.put("unicodepwd", encodePassword("Password2015"));
			ldapTemplate.bind(dn, null, userAttributes);
			return dn;
	  }
  
 
  /**
   * {@inheritDoc}
   */
  public void ldap_s03_delete_user(final String dn) {
	  User user = ldap_s01_search(dn);
	  if (user != null) {
		  ldapTemplate.unbind(dn);
		  LOGGER.info("Utilisateur supprimé avec succès : " + dn);
	  }
	  else {
		  LOGGER.info("Utilisateur déjà supprimé : " + dn);
	  }
  }
  
  
  
  /**
   * {@inheritDoc}
   */
  public User ldap_s04_modify_user_passeword(final UserPasseword userPasseword) {
	  String dn = userPasseword.getDn();
	  User user = ldap_s01_search(dn);
	  if (user != null){
		  Attribute attr = null;
		  if (PropertyManager.getLdapType().equalsIgnoreCase("activedirectory")){
			  attr = new BasicAttribute("unicodepwd", encodePassword(userPasseword.getNewUserPasseword()));
		  } 
		  else {
			  attr = new BasicAttribute("userPassword", userPasseword.getNewUserPasseword());
		  }
		  ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
		  ldapTemplate.modifyAttributes(dn, new ModificationItem[] {item});
		  LOGGER.info("Modification du mot de passe effectué avec succès pour " + dn + "(" + userPasseword.getNewUserPasseword() + ")");
		  return user;
	  }
	  else {
		  LOGGER.info("Pas de modification du mot de passe car utilisateur inexistant : " + dn);
		  return null;
	  }
  }
  
 
 

  /**
   * {@inheritDoc}
   */
  public User ldap_s05_authentify(final UserAuthentification userAuthentification) {
	// On recherche d'abord l'utilisateur dans l'annuaire sur la base du paramètre ldapSeachAuthUserFilter
	 String filter = PropertyManager.getAuthLdapSeachUserFilter();
	 String targetFilter = filter.replace("%", userAuthentification.getLogin());
	 SearchFilter searchFilter = new SearchFilter();
	 searchFilter.setBaseDn(PropertyManager.getUserBaseDn());
	 searchFilter.setFilter(targetFilter);
	 List<User> listUser = ldap_s06_search_by_filter(searchFilter);
	 
	 if ((listUser == null) || (listUser.size() == 0)) {
		 LOGGER.info("Erreur lors de l'authentification. Login inconnu");
		 return null;
	 }
	  
	User userFound = listUser.get(0);
    LOGGER.info("ldap_s05_authentify(" + userFound.getDn() + ")");

    String baseDn = LdapUtils.getOuFromDn(userFound.getDn());
    boolean authenticated = ldapTemplate.authenticate(baseDn, targetFilter, userAuthentification.getPassword());
   
    if (authenticated){
    	LOGGER.info("Authentification effectué avec succès");
    	String attrLogin = PropertyManager.getAuthAttributLogin();
		String login = (String) userFound.getAttributes().get(StringUtils.upperCase(attrLogin));
		userFound.setUsername(login);
    	return userFound;
    }
    else {
    	LOGGER.info("Erreur lors de l'authentification. Verifier le mot de passe");
    	return null;
    }
}
  
  /**
   * {@inheritDoc}
   */
	public List<User> ldap_s06_search_by_filter(SearchFilter searchFilter) {
		try {
			LOGGER.info("ldap_s06_search_by_filter(baseDn=" + searchFilter.getBaseDn() + ",filter=" + searchFilter.getFilter() + ")");
			LdapQuery query = query()
					  .base(searchFilter.getBaseDn())
					  .searchScope(SearchScope.SUBTREE)
					  .filter(searchFilter.getFilter());
			List<User> listUser = ldapTemplate.search(query, new UserContextMapper());
			for (User user : listUser){
				user.initDn();
				LOGGER.info("Found user dn : " + user.getDn());
			}
			return listUser;
			
		  }
		  catch (NameNotFoundException nnfe){
			  LOGGER.info("Aucune élément trouvé pour : " + searchFilter.toString());
			  return null;
		  }
	}
	
	/**
	   * Constuit la map de mise et création
	   * @param user
	   * @param context
	   */
	  protected void mapToContext (User user, DirContextOperations context) {
		  Map<String, Object> mapAttrs = new HashMap<String, Object>();
		  UserContextMapper  uc = new UserContextMapper();
		  Set<String> listAttrUser = user.getAttributes().keySet();
	      
	      for (String attr : listAttrUser) {
	    	  // l'attribut est fourni dans la requete
	    	  if (PropertyManager.getMapSchema().containsKey(StringUtils.upperCase(attr))){
	    		// L'attribut est mappé dans le schema
	    	  	if (uc.isMutliValueAttribute(StringUtils.upperCase(attr))){
	    	  		// Attribut multi value
	    	  		if (user.getAttributes().get(attr) instanceof List<?>){
	    	  			List<String> listAttr = (List<String>) user.getAttributes().get(attr);
	    	  			context.setAttributeValues(attr, listAttr.toArray()); 
	    	  		}
	    	  		else 
	    	  		{
	    	  			exceptionFactory.throwRfaException(ILdapMessage.ERREUR_MULTI_VALUED, attr);
	    	  		}
	    	  	}
	    	  	else
	    	  	{
	    	  		// Attribut mono-value
	    	  		context.setAttributeValue(attr, user.getAttributes().get(attr));
	    	  	}
	    	  }
	      }
	 }

  
  /**
   * encodePassword
   * @param password
   * @return
   */
    private byte[] encodePassword(String password)  {
	    String newQuotedPassword = "\"" + password + "\"";
	    try {
			return newQuotedPassword.getBytes("UTF-16LE");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Impossible d'encoder le mot de passe : " + password);
			return null;
		}  
	}

	/**
	 * @return the exceptionFactory
	 */
	public ExceptionFactory getExceptionFactory() {
		return exceptionFactory;
	}
	
	/**
	 * @param exceptionFactory the exceptionFactory to set
	 */
	public void setExceptionFactory(ExceptionFactory exceptionFactory) {
		this.exceptionFactory = exceptionFactory;
	}

	/**
	 * @return the ldapTemplate
	 */
	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	/**
	 * @param ldapTemplate the ldapTemplate to set
	 */
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}



  

}
