package sn.esmt.finperso.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "budgets",
        foreignKeys = @ForeignKey(
                entity = Categorie.class,
                parentColumns = "id",
                childColumns = "categorieId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("categorieId")}
)
public class Budget {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Integer categorieId;
    public double montantPlafond;
    public int mois;
    public int annee;

    public Budget(Integer categorieId, double montantPlafond, int mois, int annee) {
        this.categorieId = categorieId;
        this.montantPlafond = montantPlafond;
        this.mois = mois;
        this.annee = annee;
    }
}