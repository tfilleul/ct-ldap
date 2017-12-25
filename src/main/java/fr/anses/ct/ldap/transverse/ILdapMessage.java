package fr.anses.ct.ldap.transverse;

public interface ILdapMessage {
	public final String ERREUR_INIT_LDAP_CONTEXT = "ERREUR_INIT_LDAP_CONTEXT";
	public final String ERREUR_APPEL_LDAP = "ERREUR_APPEL_LDAP";
	public final String ERREUR_ENCODING = "ERREUR_ENCODING";
	public final String ERREUR_READ_DN = "ERREUR_READ_DN";
	public final String ERREUR_AUTHEN_DN = "ERREUR_AUTHEN_DN";
	public final String ERREUR_MULTI_VALUED = "ERREUR_MULTI_VALUED";
}