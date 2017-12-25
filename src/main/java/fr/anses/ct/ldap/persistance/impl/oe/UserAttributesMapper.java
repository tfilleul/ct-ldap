package fr.anses.ct.ldap.persistance.impl.oe;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.directory.Attributes;

public class UserAttributesMapper implements AttributesMapper<User> {
    
	private int nbAttr = 34;
	private String[] tabAttr = {    
			"lockoutTime" ,
		    "dSCorePropagationData" ,
		    "givenName" ,
		    "lastLogonTimestamp",
		    "sAMAccountType" ,
		    "userPrincipalName",
		    "whenChanged" ,
		    "logonCount" ,
		    "sAMAccountName" ,
		    "primaryGroupID" ,
		    "name" ,
		    "objectCategory" ,
		    "logonHours",
		    "distinguishedName" ,
		    "uSNChanged" ,
		    "objectSid",
		    "whenCreated" ,
		    "badPasswordTime",
		    "userAccountControl",
		    "countryCode",
		    "uSNCreated",
		    "objectClass" ,
		    "badPwdCount" ,
		    "instanceType" ,
		    "userWorkstations" ,
		    "pwdLastSet",
		    "accountExpires",
		    "cn",
		    "codePage",
		    "lastLogon" ,
		    "displayName",
		    "objectGUID",
		    "memberOf",
		    "dn"};
	
	public User mapFromAttributes(Attributes attrs) throws NamingException  {
        User user = new User();
        Map<String, Object> mapAttrs = new HashMap<String, Object>();
        
        for (int i=0; i<nbAttr; i++) {
	        String attribut = tabAttr[i];
	        if (attrs.get(attribut) != null){
	        	mapAttrs.put(attribut, (String) attrs.get(attribut).get());
	        }
        }
        
        user.setAttributes(mapAttrs);
        return user;
     }
	
	
}