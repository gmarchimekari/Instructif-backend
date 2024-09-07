/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import metier.modele.Eleve;

/**
 *
 * @author pbrossat
 */
public class EleveDao {
    public static void createEleve(Eleve eleve){
        JpaUtil.obtenirContextePersistance().persist(eleve);
    }

    
    public static Eleve getEleveByMail(String mail) {
        Eleve eleve = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            TypedQuery<Eleve> query = em.createQuery("SELECT e FROM Eleve e WHERE e.mail = :mail", Eleve.class);
            query.setParameter("mail", mail);
            eleve = query.getSingleResult();
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return eleve;
    }
    
    
    public static Long getNombresEleves() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        Long nbEleves = 0L; 
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Eleve e ", Long.class);
            nbEleves = query.getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbEleves;
    }
}
