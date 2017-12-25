package fr.anses.ct.ldap.persistance.impl.oe;

import java.io.Serializable;

/**
 * UserPasseword.
 * 
 * @author $Author: hhichri $
 * @version $Revision: 0 $
 */
public class UserPasseword implements Serializable {

  /** serialVersionUID **/
  private static final long serialVersionUID = 4194539912715374142L;

  /** le dn. **/
  private String dn;

  /** le nouveau passeword. **/
  private String newUserPasseword;

  public String getDn() {
    return dn;
  }

  public void setDn(final String dn) {
    this.dn = dn;
  }

  public String getNewUserPasseword() {
    return newUserPasseword;
  }

  public void setNewUserPasseword(final String newUserPasseword) {
    this.newUserPasseword = newUserPasseword;
  }
}
