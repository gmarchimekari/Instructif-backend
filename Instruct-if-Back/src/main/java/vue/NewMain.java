package vue;


import dao.JpaUtil;
import java.util.List;
import metier.modele.Eleve;
import metier.modele.Etablissement;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pbrossat
 */
public class NewMain {
    
    /**
     * @param args the command line arguments
    */
    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance();
        
        Service.chargerIntervenats();
        Service.chargerMatieres();
        
        
        Eleve eleve1 = new Eleve("Favro","Samuel","favro@gmail.com", null,2);
        Eleve eleve2 = new Eleve("danial","hbib","jean.deupont@mail.fr", null,2);
        Eleve eleve3 = new Eleve("pierrick","brossat","pbrossat@gmail.com", null,2);
        
        Matiere math = new Matiere("Maths");
        Service.ajouterMatiere(math);

        Service.inscrireEleve(eleve1, "0691664J","mdp");
        Service.inscrireEleve(eleve2, "0692338S","mdp");
        Service.inscrireEleve(eleve3, "0421677V","mdp");
        
        
        Soutien soutien = Service.demanderSoutien(eleve1, math, "descritpion soutien maths");
        List<Etablissement> lEtab = Service.getEtablissementsByCodeDepartement("069");
        
        for (Etablissement e: lEtab){
            System.out.println(e);
        }
        
        
        System.out.println("Nombre d'etablissements dans la bd : "+ Service.getNombreEtablissements());
        System.out.println("Nombre d'eleves dans la bd : "+ Service.getNombreEleves());
        System.out.println("Nombre de soutien dans la bd : "+ Service.getNombreSoutiens());
        System.out.println("Nombre d'intervenant dans la bd : "+ Service.getNombreIntervenants());
        /*
        Boolean visioLancee = Service.lancerVisio(soutien);
        
        Boolean visioQuittee = Service.quitterVisio(soutien);
        
        if(soutien != null){
            System.out.println("Le soutien a bien ete cree");
        }
        */
        /*
        Object utilisateurAuthentifie = Service.authentifierUtilisateur("jean.dupont@mail.fr", "mdpIdentitquePourToutLeMonde");
        

        // Vérification du type de l'utilisateur authentifié
        if (utilisateurAuthentifie instanceof Eleve) {
            // L'utilisateur authentifié est un élève
            Eleve eleve = (Eleve) utilisateurAuthentifie;
            // Utilisez l'objet Eleve selon vos besoins
            System.out.println("L'utilisateur authentifié est un élève :"+ eleve);
        } else if (utilisateurAuthentifie instanceof Intervenant) {
            // L'utilisateur authentifié est un intervenant
            Intervenant intervenant = (Intervenant) utilisateurAuthentifie;
            // Utilisez l'objet Intervenant selon vos besoins
            System.out.println("L'utilisateur authentifié est un intervenant : "+ intervenant);
        } else {
            // L'utilisateur authentifié n'est ni un élève ni un intervenant
            System.out.println("L'utilisateur authentifié n'est ni un élève ni un intervenant.");
        }
        
        
        
       Eleve elveAuthen = Service.authentifierEleve("favro@gmail.com", "mdp");
       Service
       
       if(elveAuthen != null) {
           System.out.println("Reussie");
       }
       else {
           System.out.println("Echouee");
       }
       
        */
        JpaUtil.fermerFabriquePersistance();
    }

        
}
