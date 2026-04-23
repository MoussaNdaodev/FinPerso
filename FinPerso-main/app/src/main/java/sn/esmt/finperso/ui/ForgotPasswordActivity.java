package sn.esmt.finperso.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import sn.esmt.finperso.R;
import sn.esmt.finperso.viewmodel.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email, newPassword;
    private Button resetBtn;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialisation des vues (IDs corrigés pour correspondre au XML)
        email = findViewById(R.id.email);
        newPassword = findViewById(R.id.newPassword);
        resetBtn = findViewById(R.id.resetBtn);

        // Initialisation du ViewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Action du bouton
        resetBtn.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String newPasswordText = newPassword.getText().toString().trim();

            if (!emailText.isEmpty() && !newPasswordText.isEmpty()) {
                viewModel.resetPassword(emailText, newPasswordText);
            }
        });
    }
}
