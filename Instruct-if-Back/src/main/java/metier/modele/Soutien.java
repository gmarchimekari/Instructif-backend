/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author pbrossat
 */
@Entity
public class Soutien {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDebut;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFin;
    
    private String description;
    
    private Integer autoEval;
    
    private Boolean status;
    
    @ManyToOne
    private Eleve eleve;
    
    @ManyToOne
    private Intervenant intervenant;
    
    @ManyToOne
    private Matiere matiere;
    
    public Soutien(){
        this.status = false;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAutoEval() {
        return autoEval;
    }

    public void setAutoEval(Integer autoEval) {
        this.autoEval = autoEval;
    }

    
    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Intervenant getIntervenant() {
        return intervenant;
    }

    public void setIntervenant(Intervenant intervenant) {
        this.intervenant = intervenant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    @Override
    public String toString() {
        return "Soutien{" + "id=" + id + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", description=" + description + ", autoEval=" + autoEval + ", status=" + status + ", eleve=" + eleve + ", intervenant=" + intervenant + ", matiere=" + matiere + '}';
    }
    
 
}
