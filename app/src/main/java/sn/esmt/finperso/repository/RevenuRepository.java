package sn.esmt.finperso.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.database.RevenuDao;
import sn.esmt.finperso.model.Revenu;

public class RevenuRepository {

    private final RevenuDao revenuDao;

    public RevenuRepository(Application application) {
        revenuDao = AppDatabase.getInstance(application).revenuDao();
    }

    public void insert(Revenu r) {
        AppDatabase.databaseWriteExecutor.execute(() -> revenuDao.insert(r));
    }

    public void update(Revenu r) {
        AppDatabase.databaseWriteExecutor.execute(() -> revenuDao.update(r));
    }

    public void delete(Revenu r) {
        AppDatabase.databaseWriteExecutor.execute(() -> revenuDao.delete(r));
    }

    public LiveData<List<Revenu>> getAll() {
        return revenuDao.getAllRevenus();
    }

    public LiveData<List<Revenu>> getParMois(String mois, String annee) {
        return revenuDao.getRevenusByMois(mois, annee);
    }

    public double getTotalParMois(String mois, String annee) {
        return revenuDao.getTotalRevenusParMois(mois, annee);
    }

    public Revenu getById(int id) {
        return revenuDao.getRevenuById(id);
    }
}