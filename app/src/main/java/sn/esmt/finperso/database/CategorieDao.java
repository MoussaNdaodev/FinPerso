package sn.esmt.finperso.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.finperso.model.Categorie;

@Dao
public interface CategorieDao {
    @Insert
    void insert(Categorie categorie);

    @Update
    void update(Categorie categorie);

    @Delete
    void delete(Categorie categorie);

    @Query("SELECT * FROM categories ORDER BY nom ASC")
    LiveData<List<Categorie>> getAllCategories();

    @Query("SELECT * FROM categories ORDER BY nom ASC")
    List<Categorie> getAllCategoriesSync();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Categorie getCategorieById(int id);

    @Query("DELETE FROM categories")
    void deleteAll();
}
