package sn.esmt.finperso.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "revenus")
public class Revenu {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double montant;
    public String source;
    public long date;
    public String description;
    public long createdAt;

    public Revenu(double montant, String source, long date, String description) {
        this.montant = montant;
        this.source = source;
        this.date = date;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }
}