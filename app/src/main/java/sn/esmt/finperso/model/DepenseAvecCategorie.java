package sn.esmt.finperso.model;

/**
 * Objet de résultat pour les requêtes JOIN Depense + Categorie + Rubrique.
 */
public class DepenseAvecCategorie {
    public int id;
    public double montant;
    public long date;
    public String description;
    public String moyenPaiement;
    public String categorieNom;
    public String categorieCouleur;
    public String rubriqueNom;  // peut être null
}