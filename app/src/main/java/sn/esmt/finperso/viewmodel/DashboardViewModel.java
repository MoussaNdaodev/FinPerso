package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.BudgetAvecProgression;
import sn.esmt.finperso.model.DepenseAvecCategorie;
import sn.esmt.finperso.repository.DepenseRepository;
import sn.esmt.finperso.repository.RevenuRepository;

public class DashboardViewModel extends AndroidViewModel {

    private final DepenseRepository depenseRepo;
    private final RevenuRepository revenuRepo;
    private final AppDatabase db;
    private final MediatorLiveData<Double> soldeLiveData = new MediatorLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        depenseRepo = new DepenseRepository(application);
        revenuRepo = new RevenuRepository(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<DepenseAvecCategorie>> getDernieresCinq() {
        return depenseRepo.getDernieresCinq();
    }

    public LiveData<List<BudgetAvecProgression>> getBudgetsCritiques(int mois, int annee) {
        return db.budgetDao().getBudgetsAvecProgressionParMois(mois, annee);
    }

    public LiveData<Double> getSolde(String mois, String annee) {
        LiveData<Double> totalRevenus = revenuRepo.getTotalParMois(mois, annee);
        LiveData<Double> totalDepenses = depenseRepo.getTotalParMois(mois, annee);

        soldeLiveData.addSource(totalRevenus, r -> {
            Double d = totalDepenses.getValue();
            if (r != null && d != null) {
                soldeLiveData.setValue(r - d);
            }
        });
        soldeLiveData.addSource(totalDepenses, d -> {
            Double r = totalRevenus.getValue();
            if (r != null && d != null) {
                soldeLiveData.setValue(r - d);
            }
        });
        return soldeLiveData;
    }

    public LiveData<Double> getTotalDepenses(String mois, String annee) {
        return depenseRepo.getTotalParMois(mois, annee);
    }

    public LiveData<Double> getTotalRevenus(String mois, String annee) {
        return revenuRepo.getTotalParMois(mois, annee);
    }
}
