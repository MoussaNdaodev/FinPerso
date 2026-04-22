package sn.esmt.finperso.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import sn.esmt.finperso.model.Utilisateur;

@Dao
public interface UtilisateurDao {

    @Insert
    void insert(Utilisateur user);

    @Query("SELECT * FROM utilisateurs WHERE email = :email LIMIT 1")
    Utilisateur getUserByEmail(String email);

    @Query("SELECT * FROM utilisateurs WHERE email = :email AND motDePasse = :password LIMIT 1")
    Utilisateur login(String email, String password);

    @Update
    void update(Utilisateur user);
}