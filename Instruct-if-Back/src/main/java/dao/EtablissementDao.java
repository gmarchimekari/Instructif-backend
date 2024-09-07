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
import metier.modele.Etablissement;

/**
 *
 * @author pbrossat
 */
public class EtablissementDao {
    
    
    public static Etablissement getEtablissementByUai(String uai){

        Etablissement etablissement = null;
        EntityManager em = JpaUtil.obtenirContextePersistance();

        try{
        TypedQuery<Etablissement> query = em.createQuery("SELECT e FROM Etablissement e WHERE e.uai = :uai", Etablissement.class);
        query.setParameter("uai", uai);

        etablissement = query.getSingleResult();
        }
        catch(Exception e){
            e.printStackTrace();

        }
        return etablissement;
    }
    
    public static List<Etablissement> getEtablissementByCodeDepartement(String codeDepartement){
        List<Etablissement> etablissements = new ArrayList();
        EntityManager em = JpaUtil.obtenirContextePersistance();

        try{
            TypedQuery<Etablissement> query = em.createQuery("SELECT e FROM Etablissement e WHERE e.codeDepartement = :codeDepartement", Etablissement.class);
            query.setParameter("codeDepartement", codeDepartement);

            etablissements = query.getResultList();
        }
        catch(Exception e){
            e.printStackTrace();

        }
        return etablissements;
    }
    
    public static Long getNombresEtablissements() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        Long nbEtablissements = 0L; 
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Etablissement e ", Long.class);
            nbEtablissements = query.getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbEtablissements;
    }
    
    public static void createEtablissement(Etablissement etablissement){
        JpaUtil.obtenirContextePersistance().persist(etablissement);
    }
}
