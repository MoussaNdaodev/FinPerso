package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.Budget;
import sn.esmt.finperso.model.BudgetAvecProgression;
import sn.esmt.finperso.model.Categorie;

public class BudgetViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public BudgetViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<Budget>> getAllBudgets() {
        return db.budgetDao().getAllBudgets();
    }

    public LiveData<List<BudgetAvecProgression>> getBudgetsAvecProgression(int mois, int annee) {
        return db.budgetDao().getBudgetsAvecProgressionParMois(mois, annee);
    }

    public LiveData<List<Categorie>> getCategories() {
        return db.categorieDao().getAllCategories();
    }

    public void insert(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.budgetDao().insert(budget));
    }

    public void update(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.budgetDao().update(budget));
    }

    public void delete(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.budgetDao().delete(budget));
    }

    public void insertOrUpdate(Integer categorieId, double montantPlafond, int mois, int annee) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (categorieId != null) {
                Budget existing = db.budgetDao().getBudgetByCategorie(categorieId, mois, annee);
                if (existing != null) {
                    existing.montantPlafond = montantPlafond;
                    db.budgetDao().update(existing);
                } else {
                    db.budgetDao().insert(new Budget(categorieId, montantPlafond, mois, annee));
                }
            } else {
                Budget global = db.budgetDao().getBudgetGlobal(mois, annee);
                if (global != null) {
                    global.montantPlafond = montantPlafond;
                    db.budgetDao().update(global);
                } else {
                    db.budgetDao().insert(new Budget(null, montantPlafond, mois, annee));
                }
            }
        });
    }
}
