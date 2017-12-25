package fr.anses.ct.ldap.domaine.impl;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import fr.anses.ct.ldap.domaine.ILdapDomaine;
import fr.anses.ct.ldap.persistance.ILdapPersistance;
import fr.anses.ct.ldap.persistance.impl.oe.SearchFilter;
import fr.anses.ct.ldap.persistance.impl.oe.User;
import fr.anses.ct.ldap.persistance.impl.oe.UserAuthentification;
import fr.anses.ct.ldap.persistance.impl.oe.UserPasseword;
import fr.anses.ct.common.transverse.RfaException;

/**
 * Implémentation de la couche domaine de LDAP.
 * 
 * @author $Author: hhichri $
 * @version $Revision: 0 $
 */
public class LdapDomaineImpl implements ILdapDomaine {

  /** Ldap persistance. **/
  private ILdapPersistance ldapPersistance;

  /**
   * {@inheritDoc}
   * 
   * @throws NamingException
   */
  public User ldap_s01_search(final String baseDn) throws RfaException {
    return ldapPersistance.ldap_s01_search(baseDn);
  }

  /**
   * {@inheritDoc}
   */
  public String ldap_s02_create_modify_user(final User user) throws RfaException {
    if (user != null) {
      return ldapPersistance.ldap_s02_create_modify_user(user);
    } else {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void ldap_s03_delete_user(final String dn) throws RfaException {
    ldapPersistance.ldap_s03_delete_user(dn);
  }

  /**
   * {@inheritDoc}
   */
  public User ldap_s04_modify_user_passeword(final UserPasseword userPasseword) throws RfaException {
    return ldapPersistance.ldap_s04_modify_user_passeword(userPasseword);
  }



  /**
   * {@inheritDoc}
   */
  public User ldap_s05_authentify(final UserAuthentification userAuthentification) throws RfaException {
    return ldapPersistance.ldap_s05_authentify(userAuthentification);
  }

  /**
   * {@inheritDoc}
   */
  public List<User> ldap_s06_search_by_filter(SearchFilter searchFilter) {
	  return ldapPersistance.ldap_s06_search_by_filter(searchFilter);
	}
  
  /**
   * 
   * @return
   */
  public ILdapPersistance getLdapPersistance() {
    return ldapPersistance;
  }

  /**
   * 
   * @param ldapPersistance
   */
  public void setLdapPersistance(final ILdapPersistance ldapPersistance) {
    this.ldapPersistance = ldapPersistance;
  }



}
