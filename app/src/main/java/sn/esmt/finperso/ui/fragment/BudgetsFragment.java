package sn.esmt.finperso.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

import sn.esmt.finperso.R;
import sn.esmt.finperso.adapter.BudgetAdapter;
import sn.esmt.finperso.model.BudgetAvecProgression;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.viewmodel.BudgetViewModel;

public class BudgetsFragment extends Fragment {

    private BudgetViewModel viewModel;
    private RecyclerView recyclerView;
    private BudgetAdapter adapter;
    private FloatingActionButton fab;
    private List<Categorie> categoriesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budgets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_budgets);
        fab = view.findViewById(R.id.fab_add_budget);

        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        adapter = new BudgetAdapter(budget -> showDialog(budget));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();
        int mois = cal.get(Calendar.MONTH) + 1;
        int annee = cal.get(Calendar.YEAR);

        viewModel.getBudgetsAvecProgression(mois, annee).observe(getViewLifecycleOwner(), budgets -> {
            adapter.setData(budgets);
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), cats -> categoriesList = cats);

        fab.setOnClickListener(v -> showDialog(null));
    }

    private void showDialog(BudgetAvecProgression existing) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_budget, null);

        Spinner spinnerCategorie = dialogView.findViewById(R.id.spinner_categorie_budget);
        EditText etMontant = dialogView.findViewById(R.id.et_montant_budget);

        Calendar cal = Calendar.getInstance();
        int currentMois = cal.get(Calendar.MONTH) + 1;
        int currentAnnee = cal.get(Calendar.YEAR);

        if (categoriesList != null) {
            ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoriesList.stream().map(c -> c.nom).toArray(String[]::new));
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorie.setAdapter(catAdapter);
        }

        if (existing != null && categoriesList != null) {
            for (int i = 0; i < categoriesList.size(); i++) {
                if (categoriesList.get(i).id == existing.categorieId) {
                    spinnerCategorie.setSelection(i);
                    break;
                }
            }
            etMontant.setText(String.valueOf(existing.montantPlafond));
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(existing == null ? "Nouveau Budget" : "Modifier le Budget")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", null)
                .setNegativeButton("Annuler", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String montantStr = etMontant.getText().toString().trim();
                if (montantStr.isEmpty()) {
                    etMontant.setError("Montant requis");
                    return;
                }
                double montant = Double.parseDouble(montantStr);
                int catIndex = spinnerCategorie.getSelectedItemPosition();
                if (categoriesList == null || catIndex < 0) {
                    Toast.makeText(requireContext(), "Sélectionnez une catégorie", Toast.LENGTH_SHORT).show();
                    return;
                }
                int categorieId = categoriesList.get(catIndex).id;

                viewModel.insertOrUpdate(categorieId, montant, currentMois, currentAnnee);
                Snackbar.make(requireView(), "Budget enregistré", Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}