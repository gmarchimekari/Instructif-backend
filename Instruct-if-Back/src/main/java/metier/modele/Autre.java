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
public class Autre extends Intervenant {
    
    
    private String activite;

    public Autre() {
    }

    public Autre(String activite, String nom, String prenom, String mail, String telephone, Integer niveauMin, Integer niveauMax) {
        super(nom, prenom, mail, telephone, niveauMin, niveauMax);
        this.activite = activite;
    }

    
    
    
    
    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }
    
    
}
