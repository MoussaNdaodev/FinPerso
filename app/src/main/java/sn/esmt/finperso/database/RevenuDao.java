package sn.esmt.finperso.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.finperso.model.Revenu;

@Dao
public interface RevenuDao {
    @Insert
    void insert(Revenu revenu);

    @Update
    void update(Revenu revenu);

    @Delete
    void delete(Revenu revenu);

    @Query("SELECT * FROM revenus ORDER BY date DESC")
    LiveData<List<Revenu>> getAllRevenus();

    @Query("SELECT * FROM revenus " +
            "WHERE strftime('%m', datetime(date/1000,'unixepoch')) = :mois " +
            "AND strftime('%Y', datetime(date/1000,'unixepoch')) = :annee " +
            "ORDER BY date DESC")
    LiveData<List<Revenu>> getRevenusFiltres(String mois, String annee);

    @Query("SELECT COALESCE(SUM(montant), 0) FROM revenus " +
            "WHERE strftime('%m', datetime(date/1000,'unixepoch')) = :mois " +
            "AND strftime('%Y', datetime(date/1000,'unixepoch')) = :annee")
    LiveData<Double> getTotalRevenusParMois(String mois, String annee);

    @Query("SELECT * FROM revenus WHERE id = :id LIMIT 1")
    Revenu getRevenuById(int id);
}
