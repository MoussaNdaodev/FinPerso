package sn.esmt.finperso.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.finperso.model.Rubrique;

@Dao
public interface RubriqueDao {
    @Insert
    void insert(Rubrique rubrique);

    @Update
    void update(Rubrique rubrique);

    @Delete
    void delete(Rubrique rubrique);

    @Query("SELECT * FROM rubriques ORDER BY nom ASC")
    LiveData<List<Rubrique>> getAllRubriques();

    @Query("SELECT * FROM rubriques WHERE categorieId = :categorieId ORDER BY nom ASC")
    LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId);

    @Query("SELECT * FROM rubriques WHERE id = :id LIMIT 1")
    Rubrique getRubriqueById(int id);

    @Query("DELETE FROM rubriques")
    void deleteAll();
}
