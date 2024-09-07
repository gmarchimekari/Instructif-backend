package metier.modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Soutien;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-04-04T14:25:52")
@StaticMetamodel(Intervenant.class)
public class Intervenant_ { 

    public static volatile SingularAttribute<Intervenant, String> motDePasse;
    public static volatile SingularAttribute<Intervenant, String> mail;
    public static volatile ListAttribute<Intervenant, Soutien> soutiens;
    public static volatile SingularAttribute<Intervenant, String> telephone;
    public static volatile SingularAttribute<Intervenant, Long> id;
    public static volatile SingularAttribute<Intervenant, String> nom;
    public static volatile SingularAttribute<Intervenant, String> prenom;
    public static volatile SingularAttribute<Intervenant, Integer> niveauMin;
    public static volatile SingularAttribute<Intervenant, Integer> niveauMax;
    public static volatile SingularAttribute<Intervenant, Boolean> disponible;

}