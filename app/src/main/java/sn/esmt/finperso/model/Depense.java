package sn.esmt.finperso.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "depenses",
        foreignKeys = {
                @ForeignKey(entity = Categorie.class, parentColumns = "id", childColumns = "categorieId"),
                @ForeignKey(entity = Rubrique.class, parentColumns = "id", childColumns = "rubriqueId")
        },
        indices = {@Index("categorieId"), @Index("rubriqueId")}
)
public class Depense {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double montant;
    public int categorieId;
    public Integer rubriqueId;
    public long date;
    public String description;
    public String moyenPaiement;
    public long createdAt;

    public Depense(double montant, int categorieId, Integer rubriqueId,
                   long date, String description, String moyenPaiement) {
        this.montant = montant;
        this.categorieId = categorieId;
        this.rubriqueId = rubriqueId;
        this.date = date;
        this.description = description;
        this.moyenPaiement = moyenPaiement;
        this.createdAt = System.currentTimeMillis();
    }
}