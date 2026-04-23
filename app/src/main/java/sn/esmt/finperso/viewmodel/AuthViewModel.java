package sn.esmt.finperso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import sn.esmt.finperso.database.AppDatabase;
import sn.esmt.finperso.model.Utilisateur;
import sn.esmt.finperso.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository repository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<Utilisateur> login(String email, String password) {
        MutableLiveData<Utilisateur> result = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Utilisateur user = repository.login(email, password);
            result.postValue(user);
        });
        return result;
    }

    public void register(Utilisateur user) {
        repository.register(user);
    }
}