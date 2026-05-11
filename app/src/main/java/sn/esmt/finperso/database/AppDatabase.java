package sn.esmt.finperso.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sn.esmt.finperso.model.Budget;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Depense;
import sn.esmt.finperso.model.Revenu;
import sn.esmt.finperso.model.Rubrique;
import sn.esmt.finperso.model.Utilisateur;

@Database(
        entities = {Utilisateur.class, Categorie.class, Rubrique.class,
                Depense.class, Revenu.class, Budget.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public abstract UtilisateurDao utilisateurDao();
    public abstract CategorieDao categorieDao();
    public abstract RubriqueDao rubriqueDao();
    public abstract DepenseDao depenseDao();
    public abstract RevenuDao revenuDao();
    public abstract BudgetDao budgetDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "finperso_db"
                            )
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> seedDatabase(INSTANCE));
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void resetFinancialData(Context context) {
        databaseWriteExecutor.execute(() -> {
            AppDatabase db = getInstance(context);
            db.runInTransaction(() -> {
                db.depenseDao().deleteAll();
                db.revenuDao().deleteAll();
                db.budgetDao().deleteAll();
                db.rubriqueDao().deleteAll();
                db.categorieDao().deleteAll();
            });
            seedDatabase(db);
        });
    }

    private static void seedDatabase(AppDatabase db) {
        UtilisateurDao utilisateurDao = db.utilisateurDao();
        CategorieDao catDao = db.categorieDao();
        RubriqueDao rubDao = db.rubriqueDao();

        if (utilisateurDao.getUserByEmail("admin@finperso.sn") == null) {
            Utilisateur defaultUser = new Utilisateur("Utilisateur", "admin@finperso.sn", "admin123");
            utilisateurDao.insert(defaultUser);
        }

        if (catDao.getAllCategoriesSync().isEmpty()) {
            String[][] categories = {
                    {"Alimentation",  "#F44336"},
                    {"Transport",     "#2196F3"},
                    {"Logement",      "#9C27B0"},
                    {"Santé",         "#4CAF50"},
                    {"Education",     "#FF9800"},
                    {"Loisirs",       "#00BCD4"},
                    {"Habillement",   "#E91E63"},
                    {"Autre",         "#607D8B"},
            };

            String[][] rubriques = {
                    {"Alimentation",  "Restaurant"},
                    {"Alimentation",  "Marché"},
                    {"Alimentation",  "Épicerie"},
                    {"Transport",     "Taxi"},
                    {"Transport",     "Bus"},
                    {"Transport",     "Carburant"},
                    {"Logement",      "Loyer"},
                    {"Logement",      "Électricité"},
                    {"Logement",      "Eau"},
                    {"Santé",         "Pharmacie"},
                    {"Santé",         "Consultation"},
                    {"Education",     "Frais scolaires"},
                    {"Education",     "Fournitures"},
                    {"Loisirs",       "Divertissement"},
                    {"Loisirs",       "Sport"},
                    {"Habillement",   "Vêtements"},
                    {"Habillement",   "Chaussures"},
            };

            for (String[] cat : categories) {
                Categorie c = new Categorie(cat[0], cat[1], true);
                catDao.insert(c);
            }

            for (String[] rub : rubriques) {
                Categorie cat = catDao.getAllCategoriesSync()
                        .stream().filter(c -> c.nom.equals(rub[0])).findFirst().orElse(null);
                if (cat != null) {
                    rubDao.insert(new Rubrique(cat.id, rub[1]));
                }
            }
        }
    }
}
