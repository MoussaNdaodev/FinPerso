package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.DepenseAvecCategorie;
import sn.esmt.finperso.repository.DepenseRepository;
import sn.esmt.finperso.repository.RevenuRepository;

public class DashboardViewModel extends AndroidViewModel {

    private final DepenseRepository depenseRepo;
    private final RevenuRepository revenuRepo;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        depenseRepo = new DepenseRepository(application);
        revenuRepo = new RevenuRepository(application);
    }

    public LiveData<List<DepenseAvecCategorie>> getDernieresCinq() {
        return depenseRepo.getDernieresCinq();
    }

    public LiveData<Double> getTotalDepenses(String mois, String annee) {
        MutableLiveData<Double> result = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            double total = depenseRepo.getTotalParMois(mois, annee);
            result.postValue(total);
        });
        return result;
    }

    public LiveData<Double> getTotalRevenus(String mois, String annee) {
        MutableLiveData<Double> result = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            double total = revenuRepo.getTotalParMois(mois, annee);
            result.postValue(total);
        });
        return result;
    }

    public LiveData<Double> getSolde(String mois, String annee) {
        MutableLiveData<Double> result = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            double revenus = revenuRepo.getTotalParMois(mois, annee);
            double depenses = depenseRepo.getTotalParMois(mois, annee);
            result.postValue(revenus - depenses);
        });
        return result;
    }
}