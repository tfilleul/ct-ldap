package fr.anses.ct.ldap.transverse;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import fr.anses.ct.common.transverse.ExceptionFactory;
import fr.anses.ct.common.transverse.RfaException;


/**
 * Interceptor de conversion des exceptions DataAcces en RfaException.
 * 
 * @org.apache.xbean.XBean element="persistanceinterceptor"
 */
@Aspect
public class PersistanceInterceptor {

  /** Le loggeur de l'interceptor. */
  private static final Logger LOGGER = LoggerFactory.getLogger(PersistanceInterceptor.class);

  /** Factory des exceptions. */
  private ExceptionFactory exceptionFactory;

  /** Point de jonction AOP. */
  public static final String POINTCUT_MAS = "execution(* fr.anses..persistance.*.*(..))";
  
  /**
   * Advice pour cette AOP.
   * 
   * @param ex
   *          Exception levée
   * @throws Throwable
   *           Exception transformée
   */
  @AfterThrowing(pointcut = POINTCUT_MAS, throwing = "ex")
  public final void transform(final Exception ex) throws Throwable {
    if (ex instanceof RfaException) {
      throw ex;
    } else {
      exceptionFactory.throwRfaException(ILdapMessage.ERREUR_APPEL_LDAP, ex.getMessage(), ex);
    }
  }

  /**
   * Post Constructeur du l'intercepteur.
   */
  @PostConstruct
  public final void after() {
    LOGGER.info("Interceptor AOP pour les fonctions suivantes");
    LOGGER.info(POINTCUT_MAS);
  }

/**
 * @return the exceptionFactory
 */
public ExceptionFactory getExceptionFactory() {
	return exceptionFactory;
}

/**
 * @param exceptionFactory the exceptionFactory to set
 */
public void setExceptionFactory(ExceptionFactory exceptionFactory) {
	this.exceptionFactory = exceptionFactory;
}
  
  
}
