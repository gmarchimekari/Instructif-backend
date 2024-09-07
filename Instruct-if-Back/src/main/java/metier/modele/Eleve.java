package metier.modele;


import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;



/**
 *
 * @author pbrossat
 */
@Entity
public class Eleve {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false,unique=true)
    private String mail;
    
    @Column
    private String motDePasse;
    
    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    
    @Column
    private Integer niveau;

    @ManyToOne
    private Etablissement etablissement;
    
    @OneToMany(mappedBy = "eleve")
    private List<Soutien> soutiens;

    protected Eleve() {
    }

    public Eleve(String nom, String prenom, String mail, Date dateNaissance, Integer niveau) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.dateNaissance = dateNaissance;
        this.niveau = niveau;
    }
    
    public Long getId(){
        return this.id;
    }
    
    public String getNom(){
        return this.nom;
    }
    
    public String getPrenom(){
        return this.prenom;
    }
    
    public String getMail(){
        return this.mail;
    }
    
    public Date getDateNaissance() {
        return dateNaissance;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
   

    public Etablissement getEtablissement() {
        return etablissement;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    
    public void setMail(String mail){
        this.mail = mail;
    }
    
    public void setMotDePasse(String motDePasse){
        this.motDePasse = motDePasse;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
    
    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public List<Soutien> getSoutiens() {
        return soutiens;
    }
    
    public void ajouterSoutien(Soutien soutien){
        this.soutiens.add(soutien);
    }

    @Override
    public String toString() {
        return "Eleve{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", motDePasse=" + motDePasse + ", dateNaissance=" + dateNaissance + '}';
    }    

    
}
