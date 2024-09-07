/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import metier.modele.Eleve;
import metier.modele.Intervenant;

/**
 *
 * @author pbrossat
 */
public class IntervenantDao {
    
    public static void createIntervenant(Intervenant intervenant){
        JpaUtil.obtenirContextePersistance().persist(intervenant);
    }
    
    public static void updateIntervenant(Intervenant intervenant){
        JpaUtil.obtenirContextePersistance().merge(intervenant);
    }
    
    public static Intervenant getIntervenantByMail(String mail) {
        Intervenant intervenant = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            TypedQuery<Intervenant> query = em.createQuery("SELECT i FROM Intervenant i WHERE i.mail = :mail", Intervenant.class);
            query.setParameter("mail", mail);
            intervenant = query.getSingleResult();
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return intervenant;
    }
    
    public static Intervenant getIntervenantDisponibleEtBonNiveauEtMinimumParticipation(Integer niveau){
        List<Intervenant> intervenants = new ArrayList<>();
        Intervenant intervenant = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            // L'intervenant doit être disponible et avoir le bon niveau d'expertise
            TypedQuery<Intervenant> query = em.createQuery("SELECT i FROM Intervenant i "
                    + "WHERE i.disponible = TRUE AND :niveau <= i.niveauMin AND :niveau >= i.niveauMax" , Intervenant.class);
            query.setParameter("niveau", niveau);
            intervenants = query.getResultList();
 
            
            if(!intervenants.isEmpty()){
                // S'il existe un (ou plusieurs) intervenants cohérents
                
                // On récupère l'intervenant avec le moins de participations à des soutiens
                Integer minimumParticipation = intervenants.get(0).getSoutiens().size();
                intervenant = intervenants.get(0);
                for(Intervenant i : intervenants) {
                    if(minimumParticipation > i.getSoutiens().size()){
                        minimumParticipation = i.getSoutiens().size(); // Maj min
                        intervenant = i; // maj intervenant

                    }
                }
            }  
        }catch(Exception e){
            e.printStackTrace();
        } 
        
        return intervenant;
    }
    
    public static Long getNombresIntervenants() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        Long nbIntervenants = 0L; 
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(i) FROM Intervenant i ", Long.class);
            nbIntervenants = query.getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbIntervenants;
    }
}
