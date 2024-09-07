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
import metier.modele.Matiere;


/**
 *
 * @author dhabib
 */
public class MatiereDao {
    public static void createMatiere(Matiere matiere) {
        JpaUtil.obtenirContextePersistance().persist(matiere);
    }

    public static List<Matiere> getListeMatieres(){
        List<Matiere> listeMatieres = new ArrayList();
        EntityManager em = JpaUtil.obtenirContextePersistance();
        try {
            TypedQuery<Matiere> query = em.createQuery("SELECT m FROM Matiere m ORDER BY m.nom", Matiere.class);
            listeMatieres = query.getResultList();
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return listeMatieres;
    }
}
