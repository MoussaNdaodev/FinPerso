package sn.esmt.finperso.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "rubriques",
        foreignKeys = @ForeignKey(
                entity = Categorie.class,
                parentColumns = "id",
                childColumns = "categorieId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("categorieId")}
)
public class Rubrique {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int categorieId;
    public String nom;

    public Rubrique(int categorieId, String nom) {
        this.categorieId = categorieId;
        this.nom = nom;
    }
}