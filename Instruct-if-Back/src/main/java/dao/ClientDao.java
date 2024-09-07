/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import metier.modele.Eleve;

/**
 *
 * @author pbrossat
 */
public class ClientDao {
    
    public static void createEleve(Eleve eleve){
        JpaUtil.obtenirContextePersistance().persist(eleve);
    }
    
    /*
    public static Client getClientParMail(String mail) {
        Client client = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.mail = :mail", Client.class);
            query.setParameter("mail", mail);
            List<Client> resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                // Si un client avec l'adresse e-mail donnée est trouvé, retournez le premier élément de la liste (puisque le mail est unique)
                client = resultList.get(0);
            }
        }catch(Exception e){
                System.out.println("erreur : "+e);
        } finally {
            em.close();
        }
        return client;
    }
    
    public static Client getClientAvecId(Long id){
        Client client = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.id = :id", Client.class);
            query.setParameter("id", id);
            List<Client> resultList = query.getResultList();
            if (!resultList.isEmpty()) { 
                client = resultList.get(0);
            }
        }catch(Exception e){
                System.out.println("erreur : "+e);
        } finally {
            em.close();
        }
        return client;
    }
    
    public static List<Client> getListeClients(){
        List<Client> lclient = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c ORDER BY c.nom ASC, c.prenom ASC", Client.class);
            lclient = query.getResultList();  
            System.out.println("taille de la liste : "+ lclient.size());
        }
        catch(Exception e){
                System.out.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
        }
        finally {
            em.close();
        }
        return lclient;
    }
*/
}
