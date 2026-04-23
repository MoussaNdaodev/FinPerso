package sn.esmt.finperso.model;

/**
 * Résultat JOIN Budget + Categorie + dépenses consommées.
 */
public class BudgetAvecProgression {
    public int budgetId;
    public Integer categorieId;
    public String categorieNom;
    public String categorieCouleur;
    public double montantPlafond;
    public double montantConsomme;
    public int mois;
    public int annee;

    public double getPourcentage() {
        if (montantPlafond <= 0) return 0;
        return (montantConsomme / montantPlafond) * 100.0;
    }

    public double getReliquat() {
        return montantPlafond - montantConsomme;
    }
}