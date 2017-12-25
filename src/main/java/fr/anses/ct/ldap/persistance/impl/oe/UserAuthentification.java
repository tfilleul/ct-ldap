package fr.anses.ct.ldap.persistance.impl.oe;

import java.io.Serializable;

/**
 * User authentification.
 * 
 * @author $Author: hhichri $
 * @version $Revision: 0 $
 */
public class UserAuthentification implements Serializable {

  /** serialVersionUID. **/
  private static final long serialVersionUID = 1896220768086544188L;

  /** Passeword. **/
  private String password;

  /** Le login. **/
  private String login;

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(final String login) {
    this.login = login;
  }

}
