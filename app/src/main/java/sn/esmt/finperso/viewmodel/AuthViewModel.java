package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import sn.esmt.finperso.model.Utilisateur;
import sn.esmt.finperso.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository repository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    // Connexion
    public Utilisateur login(String email, String password){
        return repository.login(email, password);
    }

    // Récupérer un utilisateur par email
    public Utilisateur getUserByEmail(String email){
        return repository.getUserByEmail(email);
    }

    // Mettre à jour le mot de passe
    public void updatePassword(Utilisateur user, String newPassword){
        user.motDePasse = newPassword;
        repository.updateUser(user);
    }

    // Nouvelle méthode pour ForgotPasswordActivity
    public void resetPassword(String email, String newPassword){
        Utilisateur user = repository.getUserByEmail(email);
        if (user != null) {
            updatePassword(user, newPassword);
        }
        // Tu peux ajouter une logique de feedback (Toast, LiveData, etc.)
    }
}
