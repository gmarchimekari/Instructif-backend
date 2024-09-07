/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author pbrossat
 */
@Entity
public class Etablissement {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String uai;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String secteur;
    
    @Column(nullable = false)
    private String codeCommune;
    
    @Column(nullable = false)
    private String nomCommune;
    
    @Column(nullable = false)
    private String codeDepartement;
    
    @Column(nullable = false)
    private String nomDepartement;
    
    @Column(nullable = false)
    private String academie;
    
    @Column(nullable = false)
    private String ips;

    protected Etablissement(){
    }
    
    public Etablissement(String uai, String nom, String secteur, String codeCommune, String nomCommune, String codeDepartement, String nomDepartement, String academie, String ips) {
        this.uai = uai;
        this.nom = nom;
        this.secteur = secteur;
        this.codeCommune = codeCommune;
        this.nomCommune = nomCommune;
        this.codeDepartement = codeDepartement;
        this.nomDepartement = nomDepartement;
        this.academie = academie;
        this.ips = ips;
    }
    
    
    public String getUai() {
        return uai;
    }

    public String getNom() {
        return nom;
    }

    public String getSecteur() {
        return secteur;
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public String getAcademie() {
        return academie;
    }

    public String getIps() {
        return ips;
    }

    public void setUai(String uai) {
        this.uai = uai;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public void setAcademie(String academie) {
        this.academie = academie;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Etablissement{" + "uai=" + uai + ", nom=" + nom + ", secteur=" + secteur + ", codeCommune=" + codeCommune + ", nomCommune=" + nomCommune + ", codeDepartement=" + codeDepartement + ", nomDepartement=" + nomDepartement + ", academie=" + academie + ", ips=" + ips + '}';
    }

    
    
}
