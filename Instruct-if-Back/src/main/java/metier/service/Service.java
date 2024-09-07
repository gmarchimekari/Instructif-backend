/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;


import dao.EleveDao;
import dao.EtablissementDao;
import dao.IntervenantDao;
import dao.JpaUtil;
import dao.MatiereDao;
import dao.SoutienDao;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import metier.modele.Autre;
import metier.modele.Eleve;
import metier.modele.Enseignant;
import metier.modele.Etablissement;
import metier.modele.Etudiant;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien;
import util.EducNetApi;
import util.Message;

/**
 *
 * @author pbrossat
 */
public class Service {
    
        public static Boolean inscrireEleve(Eleve eleve, String uaiEtablissement, String mdp){
            
            Boolean inscriptionOk = false;
            Message message = null;
            EducNetApi api = new EducNetApi();
            Etablissement etablissement = null;

            try {
     
                // Création du contexte de persistance
                JpaUtil.creerContextePersistance();

                // Ouverture de la transaction
                JpaUtil.ouvrirTransaction();

                etablissement = EtablissementDao.getEtablissementByUai(uaiEtablissement);
                // Obtenir l'établissement de l'élève
                if(etablissement == null){
                    List<String> result = api.getInformationCollege(uaiEtablissement);
                    if (result != null) {
                        String uai = result.get(0);
                        String nom = result.get(1);
                        String secteur = result.get(2);
                        String codeCommune = result.get(3);
                        String nomCommune = result.get(4);
                        String codeDepartement = result.get(5);
                        String nomDepartement = result.get(6);
                        String academie = result.get(7);
                        String ips = result.get(8);

                        etablissement = new Etablissement(
                            uai,
                            nom,
                            secteur,
                            codeCommune,
                            nomCommune,
                            codeDepartement,
                            nomDepartement,
                            academie,
                            ips
                        );

                        EtablissementDao.createEtablissement(etablissement);

                } else {
                    System.out.println("Etablissement avec l'uai : "+ uaiEtablissement + "est inconnu");
                }
            }

            eleve.setEtablissement(etablissement);
            
            eleve.setMotDePasse(mdp);
             
            // Verification du mail de l'élève pour eviter qu'il ai le même 
            // mail qu'un Intervenant
            if(IntervenantDao.getIntervenantByMail(eleve.getMail()) == null){
                //Ajout de l'élève dans la BD
                EleveDao.createEleve(eleve);

                JpaUtil.validerTransaction();

                inscriptionOk = true;
            }else{
                // Annulation de la transaction car le mail de l'élève est déjà présent dans la table Intervenant 
                JpaUtil.annulerTransaction();
            }
         
           
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();

        } finally {    
            // Fermeture du contexte de persistance
            JpaUtil.fermerContextePersistance();
        }
            
        String mailExpediteur = "contact@instruct.if";
        String mailEleve = eleve.getMail();
        String prenomEleve = eleve.getPrenom();
        
        if(inscriptionOk){
            //Envoyer le mail de confirmation
            message.envoyerMail(mailExpediteur,
                    mailEleve,
                    "Bienvenue sur le réseau INSTRUCT'IF",
                    "Bonjour "+ prenomEleve + 
                    ", nous te confirmons ton inscription sur le réseau INSTRUCT'IF. " +
                    "Si tu as besoin d'un soutien pour tes leçons ou tes devoirs," +
                    " rends-toi sur notre site pour une mise en relation avec un intervenant."
                    ); 
        } else {
            //Envoyer le mail d'infirmation
            message.envoyerMail(mailExpediteur, 
                                mailEleve, 
                                "Echec de l'inscription sur le réseau INSTRUCT'IF",
                                "Bonjour "+ prenomEleve +  
                                ", ton inscription sur le réseau INSTRUCT'IF a malencontreusement échoué... " +
                                "Merci de recommencer ultérieurement.");
        }
            
        return inscriptionOk;
    }
        
    
    public static Soutien demanderSoutien(Eleve eleve, Matiere matiere, String description){
       
        Soutien soutien = null;
       
        // création du soutien 
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();

            Intervenant intervenant = IntervenantDao.getIntervenantDisponibleEtBonNiveauEtMinimumParticipation(eleve.getNiveau());
            
            // creation du soutien
            soutien = new Soutien();
            soutien.setEleve(eleve);
            soutien.setMatiere(matiere);
            soutien.setDescription(description);
            
            if(intervenant != null){
                // On a trouvé un intervenant cohérent
                System.out.println("intervenant selectionné pour le soutien : "+ intervenant);
                
                // Récupération de l'heure pour le mail
                LocalTime lt = LocalTime.now(); 
                Integer heure = lt.getHour();
                Integer minute = lt.getMinute();

                soutien.setIntervenant(intervenant);
                soutien.setStatus(Boolean.TRUE);
                
                // ajout du soutien à la liste des soutiens de l'intervenant et de l'élève
                intervenant.ajouterSoutien(soutien);
                eleve.ajouterSoutien(soutien);
                
                Message.envoyerNotification(intervenant.getTelephone(), 
                "Bonjour "+ intervenant.getPrenom() + 
                        ". Merci de prendre en charge la demande de soutien en \" "+
                        matiere.getNom() + "\" demandée à "+ heure + "h" + minute + " par " +
                        eleve.getPrenom() + " en classe de " +
                        eleve.getNiveau() +"ème.");
            }
            
            
            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();

            SoutienDao.createSoutien(soutien);
            
            JpaUtil.validerTransaction();
        }catch (Exception e) {
            soutien = null;
            JpaUtil.annulerTransaction();
            e.printStackTrace();

        } finally {    
            // Fermeture du contexte de persistance
            JpaUtil.fermerContextePersistance();
        }
        
        
        return soutien;
    }
      
    // methode permettant de récuperer le profil de l'élève à partir d'un soutien
    public static Eleve getEleveSoutien(Soutien soutien){
        return soutien.getEleve();
    }
    
    
    // Methode permettant de recupérer tous les soutien d'un élève
    public static List<Soutien> getHistoriqueEleve(Eleve eleve){
        return eleve.getSoutiens();
    }
    
    // Methode permettant de recupérer tous les soutien d'un intervenant
    public static List<Soutien> getHistoriqueIntervenant(Intervenant intervenant){
        return intervenant.getSoutiens();
    }
    
    // Methode permettant de récuperer sous forme de liste toutes les matières
    public static List<Matiere> getListeMatieres(){
        return MatiereDao.getListeMatieres();
    }
    
    // Methode permettant à l'élève d'évaluer le soutien qui vient d'avoir lieu
    public static Boolean evaluerSoutien(Soutien soutien, Integer evaluation){
        Boolean modifOk = false;
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();

            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();
            
            // Maj de l'auto évaluation du soutien
            soutien.setAutoEval(evaluation);
            SoutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();
            modifOk = true;
        }catch(Exception e){ 
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return modifOk;
    }
    
    // Méthode permettant d'envoyer un mail à l'élève à partir du bilan de l'intervenant
    public static void redigerBilan(String titreBilan, String bilan, Soutien soutien){
        Message.envoyerMail(soutien.getIntervenant().getMail(),
                            soutien.getEleve().getMail(),
                            titreBilan,
                            bilan);
    }
  
    // Méthode permettant de récuperer tous les etablissements (de la BD) d'un département 
    // Précondition : le codeDepartement commence forcément par un 0
    public static List<Etablissement> getEtablissementsByCodeDepartement(String codeDepartement){
        List<Etablissement> etablissements = new ArrayList();
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récuperation des données
            etablissements = EtablissementDao.getEtablissementByCodeDepartement(codeDepartement);
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
       return etablissements;
    }
    
    
    public static Long getNombreEtablissements(){
        Long nbEtablissements = 0L;
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récuperation de la donné
            nbEtablissements = EtablissementDao.getNombresEtablissements();
            
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
       return nbEtablissements;
    }
    
    public static Long getNombreEleves(){
        Long nbEleves = 0L;
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récuperation de la donné
            nbEleves = EleveDao.getNombresEleves();
            
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
       return nbEleves;
    }
    
    public static Long getNombreSoutiens(){
        Long nbSoutiens = 0L;
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récuperation de la donné
            nbSoutiens = SoutienDao.getNombresSoutiens();
            
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
       return nbSoutiens;
    }

    
    public static Long getNombreIntervenants(){
        Long nbIntervenants = 0L;
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récuperation de la donné
            nbIntervenants = IntervenantDao.getNombresIntervenants();
            
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
       return nbIntervenants;
    }
    
    
    public static Boolean creationSoutien(Soutien soutien, Eleve eleve, Intervenant intervenant , Matiere matiere) {
        Boolean creationOk = false;
        try {
     
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();

            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();

            soutien.setEleve(eleve);
            soutien.setIntervenant(intervenant);
            soutien.setMatiere(matiere);
            
            SoutienDao.createSoutien(soutien);
            
            JpaUtil.validerTransaction();
            creationOk = true;
            
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();

        } finally {    
            // Fermeture du contexte de persistance
            JpaUtil.fermerContextePersistance();
        }
        
        return creationOk;
    }   
    
    private static Eleve authentifierEleve(String mail, String motDePasse){
        Eleve eleve = null;
        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récupération du client
            eleve = EleveDao.getEleveByMail(mail);
            
            if(eleve != null){
                if(!eleve.getMotDePasse().equals(motDePasse)){
                   // Dans le cas où le mot de passe n'est pas correct
                   eleve = null;
                } 
            }
            
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return eleve;
    }
    
    
    private static Intervenant authentifierIntervenant(String mail, String motDePasse){
        Intervenant intervenant = null;
        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récupération du client
            intervenant = IntervenantDao.getIntervenantByMail(mail);
            
            if(intervenant != null){
                if(!intervenant.getMotDePasse().equals(motDePasse)){
                   // Dans le cas où le mot de passe n'est pas correct
                   intervenant = null;
                } 
            }
            
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return intervenant;
    }
    
    
    public static Object authentifierUtilisateur(String mail, String motDePasse) {
    
        // utilisateur correspond à l'utilisateur que l'on identifie
        Object utilisateur = null;

        // Vérification si c'est un élève
        Eleve eleve = authentifierEleve(mail, motDePasse);
        if (eleve != null) {
            utilisateur = eleve;
        } else {
            // Si ce n'est pas un élève, vérification si c'est un intervenant
            Intervenant intervenant = authentifierIntervenant(mail, motDePasse);
            if (intervenant != null) {
                utilisateur = intervenant;
            }
        }

        return utilisateur;
    }
    
    
    public static void chargerIntervenats() {
        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();
            
            List<Intervenant> listeIntervenants = new ArrayList ();
            Intervenant intervenant1 = new Etudiant("Université de Paris","Mathématique","Dupont", "Jean", "jean.dupont@mail.fr", "0123456789", 3,0);
            Intervenant intervenant2 = new Etudiant("Université de Paris","Langue","Brossat", "Daniel", "dhhaha@mail.fr", "0123458789", 3, 0);
            Intervenant intervenant3 = new Etudiant("Université de Lyon 1","Informatique","Pascal", "Lucie", "luceP@mail.fr", "0183758789", 6,1);
            Intervenant intervenant4 = new Enseignant("Lycée","Lefevre", "Pierre", "pierre.lefevre@mail.fr", "0456789012", 0,0);
            Intervenant intervenant5 = new Enseignant("Collège","Dubois", "Sophie", "sophie.dubois@mail.fr", "0567890123", 1,0);
            Intervenant intervenant6 = new Autre("Pompier","Bernard", "Paul", "paul.bernard@mail.fr", "0678901234", 6,5);
            Intervenant intervenant7 = new Autre("Plombier","Thomas", "Anne", "anne.thomas@mail.fr", "0789012345", 4,1);
            
            
            listeIntervenants.add(intervenant1);
            listeIntervenants.add(intervenant2);
            listeIntervenants.add(intervenant3);
            listeIntervenants.add(intervenant4);
            listeIntervenants.add(intervenant5);
            listeIntervenants.add(intervenant6);
            listeIntervenants.add(intervenant7);
            
            
            for(Intervenant intervenant : listeIntervenants) {
                intervenant.setMotDePasse("mdpIdentitquePourToutLeMonde");
                IntervenantDao.createIntervenant(intervenant);
                
            }
            JpaUtil.validerTransaction();
        }catch(Exception e){ 
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
    }
    
    
    public static void chargerMatieres(){
        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();
            
            List<Matiere> listeMatieres = new ArrayList ();
            
            Matiere maths = new Matiere("Mathématiques");
            Matiere physique = new Matiere("Physique");
            Matiere chimie = new Matiere("Chimie");
            Matiere biologie = new Matiere("Biologie");
            Matiere histoire = new Matiere("Histoire");
            Matiere géographie = new Matiere("Géographie");
            Matiere français = new Matiere("Français");
            Matiere anglais = new Matiere("Anglais");
            Matiere espagnol = new Matiere("Espagnol");
            Matiere art = new Matiere("Art");
            Matiere informatique = new Matiere("Informatique");
            
            listeMatieres.add(maths);
            listeMatieres.add(physique);
            listeMatieres.add(chimie);
            listeMatieres.add(biologie);
            listeMatieres.add(histoire);
            listeMatieres.add(géographie);
            listeMatieres.add(français);
            listeMatieres.add(anglais);
            listeMatieres.add(espagnol);
            listeMatieres.add(art);
            listeMatieres.add(informatique);
            
            for(Matiere m : listeMatieres){
                MatiereDao.createMatiere(m);
            }
            
            JpaUtil.validerTransaction();
        }catch(Exception e){ 
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
    }
    
    
    public static Boolean ajouterMatiere(Matiere matiere) {
        
        Boolean ajoutOk = false;
        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();
            
            MatiereDao.createMatiere(matiere);
            
            JpaUtil.validerTransaction();
            ajoutOk = true;
        }catch(Exception e){ 
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return ajoutOk;
    }
    
    /*
    public Client rechercherClientParID(Long id){
        Client client = null;
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            // Récupération du client
            client = ClientDao.getClientAvecId(id);

        }catch(Exception e){ 
            System.out.println("Echec pour la raison suivante :" + e);
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return client;
    }
    
    
    public List<Client> getListeClients(){
        List<Client> lclients=null;
        try{
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();
            
            lclients = ClientDao.getListeClients();
        }catch(Exception e){
            System.out.println("Echec pour la raison suivante :" + e);
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return lclients;
    } 
*/
    
    public static Boolean lancerVisio(Soutien soutien){
        
        Boolean modifOk = false;        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();

            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();
            System.out.println("disponibilite intervenant : "+soutien.getIntervenant().getDisponible());
            
            Intervenant intervenant = soutien.getIntervenant();
            intervenant.setDisponible(false);
            IntervenantDao.updateIntervenant(intervenant);
            
            System.out.println("disponibilite intervenant : "+soutien.getIntervenant().getDisponible());

            LocalDateTime localDateTime = LocalDateTime.now(); 
            // mettre a jour la date de debut
            soutien.setDateDebut(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())); 
            
            SoutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();
            modifOk = true;
        }catch(Exception e){ 
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        System.out.println("visio est bien lancee");
        
        return modifOk;
    }
    
    
    public static Boolean quitterVisio(Soutien soutien){
        
        Boolean modifOk = false;        
        try {
            // Création du contexte de persistance
            JpaUtil.creerContextePersistance();

            // Ouverture de la transaction
            JpaUtil.ouvrirTransaction();

            Intervenant intervenant = soutien.getIntervenant();
            intervenant.setDisponible(true);
            IntervenantDao.updateIntervenant(intervenant);

            LocalDateTime localDateTime = LocalDateTime.now(); 
            // mettre a jour la date de debut
            soutien.setDateFin(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())); 
            
            SoutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();
            modifOk = true;
        }catch(Exception e){ 
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return modifOk;
    }
}
