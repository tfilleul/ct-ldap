package fr.anses.ct.ldap.persistance.impl.oe;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fr.anses.ct.ldap.transverse.PropertyManager;

/**
 * Classer User.
 * 
 * @author $Author: hhichri $
 * @version $Revision: 0 $
 */
public class User implements UserDetails, Serializable {

  /** serialVersionUID. **/
  private static final long serialVersionUID = -337005530425162447L;

  /** On y mettra le login de l'utilisateur */
  private String username;
  
  /** Le dn. **/
  private String dn;

  /** Champs. **/
  private Map < String, Object > attributes;
  
  /** Password */
  private String password;
  
  /** Unités d'apartenance de l'utilisateur (attribut memberOf) */
  private Set<String> unites = new HashSet<String>();
  
  
  public User(){
  }
  
  public User(String dn){
	  this.dn = dn;
  }
  
  public String getDn() {
	  return this.dn;
  }

  public void setDn(final String dn) {
    this.dn = dn;
  }

  public Map < String, Object > getAttributes() {
	    if (attributes == null) {
	      attributes = new HashMap < String, Object >();
	    }
	    return attributes;
	  }
  
  public void setAttributes(final Map < String, Object > attributes) {
    this.attributes = attributes;
  }

  public Collection<? extends GrantedAuthority> getAuthorities()
	{
		Set<String> entites = this.getUnites();

		if (entites == null) {
			return Collections.emptyList();
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (String entite : entites) {
			authorities.add(new SimpleGrantedAuthority(entite));
		}

		return authorities;
	}
  
  public void initDn(){
	  if (dn == null){
		  String dnAttr = StringUtils.upperCase(PropertyManager.getDnAttribut());
		  if (attributes.containsKey(dnAttr)){
			  if (attributes.get(dnAttr) != null) {
				  this.dn = (String) attributes.get(dnAttr);
				  return;
			  }
		  }
	  }
  }
  
public String getPassword() {
	return password;
}


public boolean isAccountNonExpired() {
	return true;
}

public boolean isAccountNonLocked() {
	return true;
}

public boolean isCredentialsNonExpired() {
	return true;
}

public boolean isEnabled() {
	return true;
}

/**
 * @return the unites
 */
public Set<String> getUnites() {
	return unites;
}

/**
 * @param unites the unites to set
 */
public void setUnites(Set<String> unites) {
	this.unites = unites;
}

/**
 * @param password the password to set
 */
public void setPassword(String password) {
	this.password = password;
}

public String getUsername() {
	return username;
}

/**
 * @param username the username to set
 */
public void setUsername(String username) {
	this.username = username;
}




}
