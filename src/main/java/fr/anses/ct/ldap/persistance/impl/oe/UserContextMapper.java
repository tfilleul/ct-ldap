package fr.anses.ct.ldap.persistance.impl.oe;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import fr.anses.ct.ldap.transverse.PropertyManager;

public class UserContextMapper implements ContextMapper<User> {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserContextMapper.class);
	
	private static final Map<String, String> mapConfAttribut = PropertyManager.getMapSchema();
	
 
	 public User mapFromContext(Object ctx) {
	      DirContextAdapter context = (DirContextAdapter)ctx;
	      User user = new User();
	      Map<String, Object> mapAttrs = new HashMap<String, Object>();
	      
	      for (String attr : mapConfAttribut.keySet()) {
	    	    LOGGER.trace("Mapping de " + attr);
	    	  	if (mapConfAttribut.get(attr).equalsIgnoreCase("one")){
	    	  		// Attribut mono value
		        	mapAttrs.put(attr, context.getStringAttribute(attr));
	    	  	}
	    	  	else
	    	  	{
	    	  		// Attribut multi-value
	    	  		mapAttrs.put(attr, context.getStringAttributes(attr));
	    	  	}
	      }
	      
	      user.setAttributes(mapAttrs);
	      return user;
	   }
	 
	
	
	 public boolean isMutliValueAttribute(String attr){
			return mapConfAttribut.get(attr).equalsIgnoreCase("multi");
		}
	 
	 
}
