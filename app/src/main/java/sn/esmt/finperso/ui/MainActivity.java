package sn.esmt.finperso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sn.esmt.finperso.R;
import sn.esmt.finperso.ui.fragment.BudgetsFragment;
import sn.esmt.finperso.ui.fragment.CategoriesFragment;
import sn.esmt.finperso.ui.fragment.DashboardFragment;
import sn.esmt.finperso.ui.fragment.DepensesFragment;
import sn.esmt.finperso.ui.fragment.RevenusFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        bottomNav = findViewById(R.id.bottom_navigation);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_categories) {
            loadFragment(new CategoriesFragment());
            return true;
        } else if (id == R.id.action_parametres) {
            startActivity(new Intent(this, ParametresActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
