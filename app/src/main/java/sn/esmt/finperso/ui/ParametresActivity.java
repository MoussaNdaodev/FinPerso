package sn.esmt.finperso.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import sn.esmt.finperso.R;
import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.Utilisateur;
import sn.esmt.finperso.viewmodel.AuthViewModel;

public class ParametresActivity extends AppCompatActivity {

    private EditText etNom;
    private Spinner spinnerDevise;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        etNom = findViewById(R.id.et_nom_utilisateur);
        spinnerDevise = findViewById(R.id.spinner_devise);
        Button btnSaveProfil = findViewById(R.id.btn_sauvegarder_profil);
        Button btnSaveDevise = findViewById(R.id.btn_sauvegarder_devise);
        Button btnReinitialiser = findViewById(R.id.btn_reinitialiser);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        String[] devises = {"FCFA (XOF)", "Euro (EUR)", "Dollar (USD)", "Franc CFA (XAF)"};
        ArrayAdapter<String> deviseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, devises);
        deviseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDevise.setAdapter(deviseAdapter);

        SharedPreferences prefs = getSharedPreferences("finperso_settings", MODE_PRIVATE);
        String savedDevise = prefs.getString("devise", "FCFA (XOF)");
        for (int i = 0; i < devises.length; i++) {
            if (devises[i].equals(savedDevise)) {
                spinnerDevise.setSelection(i);
                break;
            }
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Utilisateur user = db.utilisateurDao().getUserByEmail("admin@finperso.sn");
            if (user != null) {
                runOnUiThread(() -> etNom.setText(user.nom));
            }
        });

        btnSaveProfil.setOnClickListener(v -> {
            String nom = etNom.getText().toString().trim();
            if (nom.isEmpty()) {
                etNom.setError("Nom requis");
                return;
            }
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                Utilisateur user = db.utilisateurDao().getUserByEmail("admin@finperso.sn");
                if (user != null) {
                    user.nom = nom;
                    db.utilisateurDao().update(user);
                    runOnUiThread(() -> Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show());
                }
            });
        });

        btnSaveDevise.setOnClickListener(v -> {
            String devise = spinnerDevise.getSelectedItem().toString();
            prefs.edit().putString("devise", devise).apply();
            Toast.makeText(this, "Devise changée : " + devise, Toast.LENGTH_SHORT).show();
        });

        btnReinitialiser.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Réinitialisation")
                    .setMessage("Voulez-vous vraiment supprimer toutes les données ? Cette action est irréversible.")
                    .setPositiveButton("Oui, tout supprimer", (d, w) -> {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            AppDatabase db = AppDatabase.getInstance(this);
                            db.clearAllTables();
                            db.close();
                            AppDatabase.getInstance(this);
                        });
                        Toast.makeText(this, "Données réinitialisées", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finishAffinity();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }
}
