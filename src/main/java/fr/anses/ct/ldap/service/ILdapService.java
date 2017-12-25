package fr.anses.ct.ldap.service;

import java.util.List;

import javax.naming.NamingException;

import fr.anses.ct.ldap.persistance.impl.oe.User;
import fr.anses.ct.ldap.persistance.impl.oe.UserAuthentification;
import fr.anses.ct.ldap.persistance.impl.oe.UserPasseword;
import fr.anses.ct.common.transverse.RfaException;

public interface ILdapService {

  /**
   * Méthode qui permet de rechercher un objet dans l’annuaire à partir de son DN (Distinguished
   * Name). L'objet peut être un compte, un groupe ou un OU.
   * 
   * @param dn
   *          la base DN
   * @return {@link User}
   */
  User ldap_s01_search(String dn) throws RfaException;

  /**
   * Méthode qui crée un compte utilisateur
   * 
   * @param user
   *          l'utilisateur à créer
   * @return le DN du compte créé
   * @throws NamingException
   */
  String ldap_s02_create_modify_user(User user) throws RfaException;

  /**
   * Méthode qui supprime un compte AD
   * 
   * @param dn
   *          le DN
   * @throws NamingException
   */
  void ldap_s03_delete_user(String dn) throws RfaException;

  /**
   * Méthode qui modifie le mot de passe d'un utilisateur.
   * 
   * @param userPasseword
   *          le nouveau mot de passe + le dn
   * @throws NamingException
   */
  User ldap_s04_modify_user_passeword(UserPasseword userPasseword) throws RfaException;

  /**
   * Vérifier le login et le mot de passe de l’utilisateur et renvoi son contenu.
   * 
   * @param userAuthentification
   * @return
   * @throws NamingException
   */
  User ldap_s05_authentify(UserAuthentification userAuthentification) throws RfaException;
  
  /**
   * Effectue une recherche dans l'annuaire sur la base d'un filtre et d'un base DN
   * @param baseDn
   * @param filter
   * @return
   * @throws RfaException
   */
  List<User> ldap_s06_search_by_filter(String baseDn, String filter)  throws RfaException;

  /**
   * recherche retourne l'utilisateur en utilisant les parametres du ldap.properties
   * @return
   */
  User ldap_s06_search_for_authentication(String login);

}
