package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.Revenu;
import sn.esmt.finperso.repository.RevenuRepository;

public class RevenuViewModel extends AndroidViewModel {

    private final RevenuRepository repository;

    public RevenuViewModel(@NonNull Application application) {
        super(application);
        repository = new RevenuRepository(application);
    }

    public LiveData<List<Revenu>> getAllRevenus() {
        return repository.getAll();
    }

    public LiveData<List<Revenu>> getRevenusParMois(String mois, String annee) {
        return repository.getParMois(mois, annee);
    }

    public void insert(Revenu revenu) {
        repository.insert(revenu);
    }

    public void update(Revenu revenu) {
        repository.update(revenu);
    }

    public void delete(Revenu revenu) {
        repository.delete(revenu);
    }
}