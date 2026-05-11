package sn.esmt.finperso.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.finperso.model.Depense;
import sn.esmt.finperso.model.DepenseAvecCategorie;

@Dao
public interface DepenseDao {
    @Insert
    void insert(Depense depense);

    @Update
    void update(Depense depense);

    @Delete
    void delete(Depense depense);

    @Query("SELECT * FROM depenses ORDER BY date DESC")
    LiveData<List<Depense>> getAllDepenses();

    @Query("SELECT d.id, d.montant, d.date, d.description, d.moyenPaiement, " +
            "c.nom AS categorieNom, c.couleur AS categorieCouleur, r.nom AS rubriqueNom " +
            "FROM depenses d " +
            "LEFT JOIN categories c ON d.categorieId = c.id " +
            "LEFT JOIN rubriques r ON d.rubriqueId = r.id " +
            "ORDER BY d.date DESC")
    LiveData<List<DepenseAvecCategorie>> getAllDepensesAvecCategorie();

    @Query("SELECT d.id, d.montant, d.date, d.description, d.moyenPaiement, " +
            "c.nom AS categorieNom, c.couleur AS categorieCouleur, r.nom AS rubriqueNom " +
            "FROM depenses d " +
            "LEFT JOIN categories c ON d.categorieId = c.id " +
            "LEFT JOIN rubriques r ON d.rubriqueId = r.id " +
            "ORDER BY d.date DESC LIMIT 5")
    LiveData<List<DepenseAvecCategorie>> getDernieresCinqDepenses();

    @Query("SELECT COALESCE(SUM(montant), 0) FROM depenses " +
            "WHERE strftime('%m', datetime(date/1000,'unixepoch')) = :mois " +
            "AND strftime('%Y', datetime(date/1000,'unixepoch')) = :annee")
    LiveData<Double> getTotalDepensesParMois(String mois, String annee);

    @Query("SELECT COALESCE(SUM(montant), 0) FROM depenses " +
            "WHERE strftime('%m', datetime(date/1000,'unixepoch')) = :mois " +
            "AND strftime('%Y', datetime(date/1000,'unixepoch')) = :annee " +
            "AND categorieId = :categorieId")
    LiveData<Double> getTotalParCategorie(int categorieId, String mois, String annee);

    @Query("SELECT d.id, d.montant, d.date, d.description, d.moyenPaiement, " +
            "c.nom AS categorieNom, c.couleur AS categorieCouleur, r.nom AS rubriqueNom " +
            "FROM depenses d " +
            "LEFT JOIN categories c ON d.categorieId = c.id " +
            "LEFT JOIN rubriques r ON d.rubriqueId = r.id " +
            "WHERE strftime('%m', datetime(d.date/1000,'unixepoch')) = :mois " +
            "AND strftime('%Y', datetime(d.date/1000,'unixepoch')) = :annee " +
            "AND (:categorieId = 0 OR d.categorieId = :categorieId) " +
            "ORDER BY d.date DESC")
    LiveData<List<DepenseAvecCategorie>> getDepensesFiltrees(String mois, String annee, int categorieId);

    @Query("SELECT d.id, d.montant, d.date, d.description, d.moyenPaiement, " +
            "c.nom AS categorieNom, c.couleur AS categorieCouleur, r.nom AS rubriqueNom " +
            "FROM depenses d " +
            "LEFT JOIN categories c ON d.categorieId = c.id " +
            "LEFT JOIN rubriques r ON d.rubriqueId = r.id " +
            "WHERE strftime('%m', datetime(d.date/1000,'unixepoch')) = :mois " +
            "AND strftime('%Y', datetime(d.date/1000,'unixepoch')) = :annee " +
            "ORDER BY d.date DESC")
    LiveData<List<DepenseAvecCategorie>> getDepensesByMois(String mois, String annee);

    @Query("SELECT * FROM depenses WHERE id = :id LIMIT 1")
    Depense getDepenseById(int id);

    @Query("SELECT COALESCE(SUM(montant), 0) FROM depenses WHERE categorieId = :categorieId")
    LiveData<Double> getTotalDepensesByCategorie(int categorieId);
}
