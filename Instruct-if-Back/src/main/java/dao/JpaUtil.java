package dao;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 * Cette classe fournit des mÃ©thodes statiques utiles pour accÃ©der aux
 * fonctionnalitÃ©s de JPA (Entity Manager, Entity Transaction). Le nom de
 * l'unitÃ© de persistance (PERSISTENCE_UNIT_NAME) doit Ãªtre conforme Ã  la
 * configuration indiquÃ©e dans le fichier persistence.xml du projet.
 *
 * @author DASI Team
 */
public class JpaUtil {

    // *************************************************************************************
    // * TODO: IMPORTANT -- Adapter le nom de l'UnitÃ© de Persistance (cf. persistence.xml) *
    // *************************************************************************************
    /**
     * Nom de l'unitÃ© de persistance utilisÃ©e par la Factory de Entity Manager.
     * <br><strong>VÃ©rifier le nom de l'unitÃ© de persistance
     * (cf.&nbsp;persistence.xml)</strong>
     */
    public static final String PERSISTENCE_UNIT_NAME = "fr.insalyon.dasi.test_DASI.INSTRUCTIF";
    /**
     * Factory de Entity Manager liÃ©e Ã  l'unitÃ© de persistance.
     * <br/><strong>VÃ©rifier le nom de l'unitÃ© de persistance indiquÃ©e dans
     * l'attribut statique PERSISTENCE_UNIT_NAME
     * (cf.&nbsp;persistence.xml)</strong>
     */
    private static EntityManagerFactory entityManagerFactory = null;
    /**
     * GÃ¨re les instances courantes de Entity Manager liÃ©es aux Threads.
     * L'utilisation de ThreadLocal garantie une unique instance courante par
     * Thread.
     */
    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<EntityManager>() {

        @Override
        protected EntityManager initialValue() {
            return null;
        }
    };
    /**
     * Indicateur d'affichage du Log de JpaUtil.
     * Par dÃ©faut, le Log de JpaUtil s'affiche dans la console.
     * Utiliser la mÃ©thode <code>desactiverLog()</code> pour dÃ©sactiver cet affichage.
     */
    private static boolean JPAUTIL_LOG_ACTIVE = true;

    /**
     * MÃ©thode privÃ©e d'affichage du Log sur la console.
     * @param message Message Ã  afficher dans le Log
     */
    private static void log(String message) {
        if (JPAUTIL_LOG_ACTIVE) {
            System.out.println("[JpaUtil:Log] " + message);
        }
    }
    
    /**
     * MÃ©thode pour dÃ©sactiver l'affichage du Log de JpaUtil.
     * Ã€ utiliser avant la mÃ©thode <code>creerFabriquePersistance()</code> pour
     * Ã©galement dÃ©sactiver le Log de la librairie EclipseLink.
     */    
    public static void desactiverLog() {
        JPAUTIL_LOG_ACTIVE = false;
    }

    /**
     * Initialise la Fabrique (Factory) de Contexte de Persistance (Entity Manager).
     * <br><strong>Ã€ utiliser uniquement au dÃ©but de la mÃ©thode main() [projet
     * Java Application] ou dans la mÃ©thode init() de la Servlet ContrÃ´leur
     * (ActionServlet) [projet Web Application].</strong>
     */
    public static synchronized void creerFabriquePersistance() {
        log("CrÃ©ation de la fabrique de contexte de persistance");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        Map<String, String> propertyMap = new HashMap<>();
        if (!JPAUTIL_LOG_ACTIVE) {
            propertyMap.put("eclipselink.logging.level", "OFF");
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,propertyMap);
    }

    /**
     * LibÃ¨re la Fabrique (Factory) de Contexte de Persistance (Entity Manager).
     * <br><strong>Ã€ utiliser uniquement Ã  la fin de la mÃ©thode main() [projet
     * Java Application] ou dans la mÃ©thode destroy() de la Servlet ContrÃ´leur
     * (ActionServlet) [projet Web Application].</strong>
     */
    public static synchronized void fermerFabriquePersistance() {
        log("Fermeture de la fabrique de contexte de persistance");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    /**
     * CrÃ©Ã©e l'instance courante du Contexte de Persistance (Entity Manager), liÃ©e Ã  ce Thread.
     * <br><strong>Ã€ utiliser uniquement au niveau Service.</strong>
     */
    public static void creerContextePersistance() {
        log("CrÃ©ation du contexte de persistance");
        threadLocalEntityManager.set(entityManagerFactory.createEntityManager());
    }

    /**
     * Ferme l'instance courante du Contexte de Persistance (Entity Manager), liÃ©e Ã  ce Thread.
     * <br><strong>Ã€ utiliser uniquement au niveau Service.</strong>
     */
    public static void fermerContextePersistance() {
        log("Fermeture du contexte de persistance");
        EntityManager em = threadLocalEntityManager.get();
        em.close();
        threadLocalEntityManager.set(null);
    }

    /**
     * DÃ©marre une transaction sur l'instance courante du Contexte de Persistance (Entity Manager).
     * <br><strong>Ã€ utiliser uniquement au niveau Service.</strong>
     */
    public static void ouvrirTransaction() throws Exception {
        log("Ouverture de la transaction (begin)");
        try {
            EntityManager em = threadLocalEntityManager.get();
            em.getTransaction().begin();
        } catch (Exception ex) {
            log("Erreur lors de l'ouverture de la transaction");
            throw ex;
        }
    }

    /**
     * Valide la transaction courante sur l'instance courante du Contexte de Persistance (Entity Manager).
     * <br><strong>Ã€ utiliser uniquement au niveau Service.</strong>
     *
     * @exception RollbackException lorsque le <em>commit</em> n'a pas rÃ©ussi.
     */
    public static void validerTransaction() throws RollbackException, Exception {
        log("Validation de la transaction (commit)");
        try {
            EntityManager em = threadLocalEntityManager.get();
            em.getTransaction().commit();
        } catch (Exception ex) {
            log("Erreur lors de la validation (commit) de la transaction");
            throw ex;
        }
    }

    /**
     * Annule la transaction courante sur l'instance courante du Contexte de Persistance (Entity Manager).
     * Si la transaction courante n'est pas dÃ©marrÃ©e, cette mÃ©thode n'effectue
     * aucune opÃ©ration.
     * <br><strong>Ã€ utiliser uniquement au niveau Service.</strong>
     */
    public static void annulerTransaction() {
        try {
            log("Annulation de la transaction (rollback)");

            EntityManager em = threadLocalEntityManager.get();
            if (em.getTransaction().isActive()) {
                log("Annulation effective de la transaction (rollback d'une transaction active)");
                em.getTransaction().rollback();
            }

        } catch (Exception ex) {
            log("Erreur lors de l'annulation (rollback) de la transaction");
        }
    }

    /**
     * Retourne l'instance courante de Entity Manager.
     * <br><strong>Ã€ utiliser uniquement au niveau DAO.</strong>
     *
     * @return instance de Entity Manager
     */
    protected static EntityManager obtenirContextePersistance() {
        log("Obtention du contexte de persistance");
        return threadLocalEntityManager.get();
    }
}