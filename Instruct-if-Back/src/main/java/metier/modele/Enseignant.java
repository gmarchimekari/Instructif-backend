/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import javax.persistence.Entity;

/**
 *
 * @author pbrossat
 */
@Entity
public class Enseignant extends Intervenant {
    
    
    private String typeEtablissement;

    public Enseignant() {
    }

    public Enseignant(String typeEtablissement, String nom, String prenom, String mail, String telephone, Integer niveauMin, Integer niveauMax) {
        super(nom, prenom, mail, telephone, niveauMin, niveauMax);
        this.typeEtablissement = typeEtablissement;
    }

    
    
    
    
    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }
    
    
}
