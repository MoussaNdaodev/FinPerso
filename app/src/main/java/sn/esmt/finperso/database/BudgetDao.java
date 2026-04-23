package sn.esmt.finperso.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.finperso.model.Budget;
import sn.esmt.finperso.model.BudgetAvecProgression;

@Dao
public interface BudgetDao {
    @Insert
    void insert(Budget budget);

    @Update
    void update(Budget budget);

    @Delete
    void delete(Budget budget);

    @Query("SELECT * FROM budgets ORDER BY mois DESC, annee DESC")
    LiveData<List<Budget>> getAllBudgets();

    @Query("SELECT * FROM budgets WHERE categorieId = :categorieId AND mois = :mois AND annee = :annee LIMIT 1")
    Budget getBudgetByCategorie(int categorieId, int mois, int annee);

    @Query("SELECT b.id AS budgetId, b.categorieId, c.nom AS categorieNom, " +
            "c.couleur AS categorieCouleur, b.montantPlafond, b.mois, b.annee, " +
            "COALESCE((SELECT SUM(d.montant) FROM depenses d " +
            "WHERE d.categorieId = b.categorieId " +
            "AND strftime('%m', datetime(d.date/1000,'unixepoch')) = printf('%02d', b.mois) " +
            "AND strftime('%Y', datetime(d.date/1000,'unixepoch')) = CAST(b.annee AS TEXT)" +
            "), 0) AS montantConsomme " +
            "FROM budgets b " +
            "LEFT JOIN categories c ON b.categorieId = c.id " +
            "WHERE b.categorieId IS NOT NULL AND b.mois = :mois AND b.annee = :annee " +
            "ORDER BY c.nom ASC")
    LiveData<List<BudgetAvecProgression>> getBudgetsAvecProgressionParMois(int mois, int annee);
}