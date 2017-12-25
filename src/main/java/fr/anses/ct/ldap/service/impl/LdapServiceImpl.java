package fr.anses.ct.ldap.service.impl;

import java.util.List;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.anses.ct.ldap.domaine.ILdapDomaine;
import fr.anses.ct.ldap.persistance.impl.oe.SearchFilter;
import fr.anses.ct.ldap.persistance.impl.oe.User;
import fr.anses.ct.ldap.persistance.impl.oe.UserAuthentification;
import fr.anses.ct.ldap.persistance.impl.oe.UserPasseword;
import fr.anses.ct.ldap.service.ILdapService;
import fr.anses.ct.ldap.transverse.PropertyManager;
import fr.anses.ct.common.transverse.RfaException;

/**
 * Classe Test service.
 * 
 * @author $Author: hhichri $
 * @version $Revision: 0 $
 */
public class LdapServiceImpl implements ILdapService {

  /** LDAP Domaine. */
  private ILdapDomaine ldapDomaine;

  /** Le logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger("LDAP");

 
  /**
   * {@inheritDoc}
   * 
   * @throws NamingException
   */
  public User ldap_s01_search(final String dn) throws RfaException {
    return ldapDomaine.ldap_s01_search(dn);
  }

  /**
   * {@inheritDoc}
   */
  public String ldap_s02_create_modify_user(final User user) throws RfaException {
    return ldapDomaine.ldap_s02_create_modify_user(user);
  }

  /**
   * {@inheritDoc}
   */
  public void ldap_s03_delete_user(final String dn) throws RfaException {
    ldapDomaine.ldap_s03_delete_user(dn);
  }

  /**
   * {@inheritDoc}
   */
  public User ldap_s04_modify_user_passeword(final UserPasseword userPasseword) throws RfaException {
    return ldapDomaine.ldap_s04_modify_user_passeword(userPasseword);
  }

  

  /**
   * {@inheritDoc}
   */
  public User ldap_s05_authentify(final UserAuthentification userAuthentification) throws RfaException {
    return ldapDomaine.ldap_s05_authentify(userAuthentification);
  }

  /**
   * {@inheritDoc}
   */
	public List<User> ldap_s06_search_by_filter(String baseDn, String filter){
		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setBaseDn(baseDn);
		searchFilter.setFilter(filter);
		return ldapDomaine.ldap_s06_search_by_filter(searchFilter);
	}
	
/**
 * 
 * @return
 */
  public ILdapDomaine getLdapDomaine() {
    return ldapDomaine;
  }

  /**
   * 
   * @param ldapDomaine
   */
  public void setLdapDomaine(final ILdapDomaine ldapDomaine) {
    this.ldapDomaine = ldapDomaine;
  }

public User ldap_s06_search_for_authentication(String login) {
	 User retUser = null;
	 String filter = PropertyManager.getAuthLdapSeachUserFilter();
  	 String targetFilter = filter.replace("%", login);
  	 List<User> listUser = this.ldap_s06_search_by_filter(PropertyManager.getUserBaseDn(), targetFilter);
  	if ((listUser != null) && (listUser.size() > 0)) {
  		retUser = listUser.get(0);
  		retUser.setUsername(login);
  	}
  	return retUser;
}
  

  

}
