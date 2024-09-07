/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author pbrossat
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Intervenant {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    private String motDePasse;
    
    @Column(nullable = false, unique = true)
    private String telephone;
    
    private Integer niveauMin;
    
    private Integer niveauMax;
    
    private Boolean disponible;
    
    @OneToMany(mappedBy = "intervenant")
    private List<Soutien> soutiens;

    public Intervenant() {
    }
    
    public Intervenant(String nom, String prenom, String mail, String telephone, Integer niveauMin, Integer niveauMax) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.telephone = telephone;
        this.niveauMin = niveauMin;
        this.niveauMax = niveauMax;
        this.disponible = true; // De base est disponible
    }

    @Override
    public String toString() {
        return "Intervenant{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", motDePasse=" + motDePasse + ", telephone=" + telephone + ", niveauMin=" + niveauMin + ", niveauMax=" + niveauMax + ", disponible=" + disponible + ", soutiens=" + soutiens + '}';
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getNiveauMin() {
        return niveauMin;
    }

    public void setNiveauMin(Integer niveauMin) {
        this.niveauMin = niveauMin;
    }

    public Integer getNiveauMax() {
        return niveauMax;
    }

    public void setNiveauMax(Integer niveauMax) {
        this.niveauMax = niveauMax;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public List<Soutien> getSoutiens() {
        return soutiens;
    }

    public void ajouterSoutien(Soutien soutien) {
        this.soutiens.add(soutien); // On ajoute le soutien à la liste 
    }
    
}
