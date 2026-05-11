package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Depense;
import sn.esmt.finperso.model.DepenseAvecCategorie;
import sn.esmt.finperso.model.Rubrique;
import sn.esmt.finperso.repository.DepenseRepository;

public class DepenseViewModel extends AndroidViewModel {

    private final DepenseRepository repository;
    private final AppDatabase db;
    private final MutableLiveData<String> moisFiltre = new MutableLiveData<>();
    private final MutableLiveData<String> anneeFiltre = new MutableLiveData<>();
    private final MutableLiveData<Integer> categorieFiltre = new MutableLiveData<>(0);
    private final LiveData<List<DepenseAvecCategorie>> depensesFiltrees;

    public DepenseViewModel(@NonNull Application application) {
        super(application);
        repository = new DepenseRepository(application);
        db = AppDatabase.getInstance(application);

        depensesFiltrees = Transformations.switchMap(categorieFiltre, catId ->
                Transformations.switchMap(moisFiltre, mois ->
                        Transformations.switchMap(anneeFiltre, annee ->
                                repository.getDepensesFiltrees(mois, annee, catId)
                        )
                )
        );
    }

    public LiveData<List<DepenseAvecCategorie>> getAllDepenses() {
        return repository.getAll();
    }

    public LiveData<List<DepenseAvecCategorie>> getDepensesFiltrees() {
        return depensesFiltrees;
    }

    public LiveData<List<Categorie>> getCategories() {
        return db.categorieDao().getAllCategories();
    }

    public LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId) {
        return db.rubriqueDao().getRubriquesByCategorie(categorieId);
    }

    public void setFiltrePeriode(String mois, String annee) {
        moisFiltre.setValue(mois);
        anneeFiltre.setValue(annee);
    }

    public void setFiltreCategorie(int categorieId) {
        categorieFiltre.setValue(categorieId);
    }

    public void reinitialiserFiltres() {
        categorieFiltre.setValue(0);
    }

    public void insert(Depense depense) {
        repository.insert(depense);
    }

    public void deleteById(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Depense d = db.depenseDao().getDepenseById(id);
            if (d != null) db.depenseDao().delete(d);
        });
    }

    public void updateById(int id, double montant, int categorieId, Integer rubriqueId,
                           long date, String description, String moyenPaiement) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Depense d = db.depenseDao().getDepenseById(id);
            if (d != null) {
                d.montant = montant;
                d.categorieId = categorieId;
                d.rubriqueId = rubriqueId;
                d.date = date;
                d.description = description;
                d.moyenPaiement = moyenPaiement;
                db.depenseDao().update(d);
            }
        });
    }
}
