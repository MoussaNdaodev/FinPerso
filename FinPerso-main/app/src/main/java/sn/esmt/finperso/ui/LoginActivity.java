package sn.esmt.finperso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import sn.esmt.finperso.R;
import sn.esmt.finperso.model.Utilisateur;
import sn.esmt.finperso.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        // Initialisation du ViewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Action du bouton de connexion
        loginBtn.setOnClickListener(v -> {
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();

            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilisateur user = viewModel.login(e, p);

            if (user != null) {
                // Connexion réussie → redirection vers MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                // Identifiants incorrects
                Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
