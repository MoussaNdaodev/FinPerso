package sn.esmt.finperso.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sn.esmt.finperso.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.emailForgot);
        btnReset = findViewById(R.id.btnResetPassword);

        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Entrez votre email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lien de réinitialisation envoyé à " + email, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}