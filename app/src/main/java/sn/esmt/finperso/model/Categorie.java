package sn.esmt.finperso.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Categorie {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nom;
    public String couleur;   // ex: "#FF5722"
    public boolean estDefaut;

    public Categorie(String nom, String couleur, boolean estDefaut) {
        this.nom = nom;
        this.couleur = couleur;
        this.estDefaut = estDefaut;
    }
}