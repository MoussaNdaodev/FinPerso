package sn.esmt.finperso.repository;

import android.app.Application;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.database.UtilisateurDao;
import sn.esmt.finperso.model.Utilisateur;

public class AuthRepository {

    private final UtilisateurDao utilisateurDao;

    public AuthRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        utilisateurDao = db.utilisateurDao();
    }

    public Utilisateur login(String email, String password) {
        return utilisateurDao.login(email, password);
    }

    public Utilisateur getUserByEmail(String email) {
        return utilisateurDao.getUserByEmail(email);
    }

    public void register(Utilisateur user) {
        AppDatabase.databaseWriteExecutor.execute(() -> utilisateurDao.insert(user));
    }

    public void updateUser(Utilisateur user) {
        AppDatabase.databaseWriteExecutor.execute(() -> utilisateurDao.update(user));
    }
}