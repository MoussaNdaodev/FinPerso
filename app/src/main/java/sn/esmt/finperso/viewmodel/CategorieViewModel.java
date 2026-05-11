package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.database.CategorieDao;
import sn.esmt.finperso.database.RubriqueDao;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Rubrique;

public class CategorieViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public CategorieViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return db.categorieDao().getAllCategories();
    }

    public LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId) {
        return db.rubriqueDao().getRubriquesByCategorie(categorieId);
    }

    public void insertCategorie(String nom, String couleur) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.categorieDao().insert(new Categorie(nom, couleur, false));
        });
    }

    public void updateCategorie(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.categorieDao().update(categorie));
    }

    public void deleteCategorie(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.categorieDao().delete(categorie));
    }

    public void insertRubrique(int categorieId, String nom) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.rubriqueDao().insert(new Rubrique(categorieId, nom));
        });
    }

    public void deleteRubrique(Rubrique rubrique) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.rubriqueDao().delete(rubrique));
    }

    public LiveData<Double> getTotalDepensesByCategorie(int categorieId) {
        return db.depenseDao().getTotalDepensesByCategorie(categorieId);
    }
}
