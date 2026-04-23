package sn.esmt.finperso.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sn.esmt.finperso.R;
import sn.esmt.finperso.ui.fragment.BudgetsFragment;
import sn.esmt.finperso.ui.fragment.DashboardFragment;
import sn.esmt.finperso.ui.fragment.DepensesFragment;
import sn.esmt.finperso.ui.fragment.RevenusFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        // Charger le fragment par défaut
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                fragment = new DashboardFragment();
            } else if (id == R.id.nav_depenses) {
                fragment = new DepensesFragment();
            } else if (id == R.id.nav_revenus) {
                fragment = new RevenusFragment();
            } else if (id == R.id.nav_budgets) {
                fragment = new BudgetsFragment();
            } else {
                return false;
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}