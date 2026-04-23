package sn.esmt.finperso.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.database.DepenseDao;
import sn.esmt.finperso.model.Depense;
import sn.esmt.finperso.model.DepenseAvecCategorie;

public class DepenseRepository {

    private final DepenseDao depenseDao;

    public DepenseRepository(Application application) {
        depenseDao = AppDatabase.getInstance(application).depenseDao();
    }

    public void insert(Depense d) {
        AppDatabase.databaseWriteExecutor.execute(() -> depenseDao.insert(d));
    }

    public void update(Depense d) {
        AppDatabase.databaseWriteExecutor.execute(() -> depenseDao.update(d));
    }

    public void delete(Depense d) {
        AppDatabase.databaseWriteExecutor.execute(() -> depenseDao.delete(d));
    }

    public LiveData<List<DepenseAvecCategorie>> getAll() {
        return depenseDao.getAllDepensesAvecCategorie();
    }

    public LiveData<List<DepenseAvecCategorie>> getDernieresCinq() {
        return depenseDao.getDernieresCinqDepenses();
    }

    public double getTotalParMois(String mois, String annee) {
        return depenseDao.getTotalDepensesParMois(mois, annee);
    }

    public Depense getById(int id) {
        return depenseDao.getDepenseById(id);
    }
}