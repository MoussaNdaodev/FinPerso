package sn.esmt.finperso.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "utilisateurs")
public class Utilisateur {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nom;
    public String email;
    public String motDePasse;

    public Utilisateur(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
}