/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import metier.modele.Intervenant;
import metier.modele.Soutien;

/**
 *
 * @author pbrossat
 */
public class SoutienDao {
    
    public static void createSoutien(Soutien soutien){
        JpaUtil.obtenirContextePersistance().persist(soutien);
    }
    
    public static void updateSoutien(Soutien soutien){
        JpaUtil.obtenirContextePersistance().merge(soutien);
    }
    
    public static Long getNombresSoutiens() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        Long nbSoutiens = 0L; 
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(s) FROM Soutien s ", Long.class);
            nbSoutiens = query.getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbSoutiens;
    }
}
