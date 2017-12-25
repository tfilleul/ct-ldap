package fr.anses.ct.ldap.persistance.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.support.LdapContextSource;

import fr.anses.ct.ldap.transverse.PropertyManager;

public class RfaLdapContextSource extends LdapContextSource {
	
	 /** Le logger. */
	  private static final Logger LOGGER = LoggerFactory.getLogger(RfaLdapContextSource.class);
	
	public RfaLdapContextSource() {
		super();
		LOGGER.info("Initialisation des informations de connexion à l'annuaire");
		this.setUrl(PropertyManager.getLdapUrl());
		this.setBase("");
		this.setUserDn(PropertyManager.getLdapUsername());
		this.setPassword(PropertyManager.getLdapPassword());
		this.setReferral("follow");
		LOGGER.info("URL : " + this.getUrls()[0]);
		LOGGER.info("UserDn : " + this.getUserDn());
		Map<String, Object> mapProp = new HashMap<String, Object>();
		mapProp.put("java.naming.security.authentication", "simple");
		this.setBaseEnvironmentProperties(mapProp);	
	}
	
}
