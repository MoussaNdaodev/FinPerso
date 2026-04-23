package sn.esmt.finperso.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sn.esmt.finperso.R;
import sn.esmt.finperso.adapter.DepenseAdapter;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Depense;
import sn.esmt.finperso.model.DepenseAvecCategorie;
import sn.esmt.finperso.viewmodel.DepenseViewModel;

public class DepensesFragment extends Fragment {

    private DepenseViewModel viewModel;
    private RecyclerView recyclerView;
    private DepenseAdapter adapter;
    private FloatingActionButton fab;
    private List<Categorie> categoriesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_depenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_depenses);
        fab = view.findViewById(R.id.fab_add_depense);

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);

        adapter = new DepenseAdapter(depense -> showDialog(depense));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getAllDepenses().observe(getViewLifecycleOwner(), depenses -> adapter.setData(depenses));

        viewModel.getCategories().observe(getViewLifecycleOwner(), cats -> categoriesList = cats);

        fab.setOnClickListener(v -> showDialog(null));
    }

    private void showDialog(DepenseAvecCategorie existing) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_depense, null);

        EditText etMontant = dialogView.findViewById(R.id.et_montant);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etDate = dialogView.findViewById(R.id.et_date);
        Spinner spinnerCategorie = dialogView.findViewById(R.id.spinner_categorie);
        Spinner spinnerPaiement = dialogView.findViewById(R.id.spinner_paiement);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        final long[] selectedDate = {cal.getTimeInMillis()};
        etDate.setText(sdf.format(cal.getTime()));

        etDate.setOnClickListener(v -> new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
            cal.set(y, m, d);
            selectedDate[0] = cal.getTimeInMillis();
            etDate.setText(sdf.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show());

        if (categoriesList != null) {
            ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoriesList.stream().map(c -> c.nom).toArray(String[]::new));
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorie.setAdapter(catAdapter);
        }

        String[] paiements = {"Espèces", "Mobile Money", "Carte", "Autre"};
        ArrayAdapter<String> paiAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, paiements);
        paiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaiement.setAdapter(paiAdapter);

        if (existing != null) {
            etMontant.setText(String.valueOf(existing.montant));
            etDescription.setText(existing.description);
            etDate.setText(sdf.format(existing.date));
            selectedDate[0] = existing.date;
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(existing == null ? "Nouvelle dépense" : "Modifier la dépense")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", null)
                .setNegativeButton("Annuler", null)
                .create();

        if (existing != null) {
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Supprimer", (d, w) -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation")
                        .setMessage("Supprimer cette dépense ?")
                        .setPositiveButton("Oui", (dd, ww) -> {
                            viewModel.deleteById(existing.id);
                            Snackbar.make(requireView(), "Dépense supprimée", Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Non", null)
                        .show();
            });
        }

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String montantStr = etMontant.getText().toString().trim();
                if (montantStr.isEmpty()) {
                    etMontant.setError("Montant requis");
                    return;
                }
                double montant = Double.parseDouble(montantStr);
                int catIndex = spinnerCategorie.getSelectedItemPosition();
                if (categoriesList == null || catIndex < 0) return;
                int categorieId = categoriesList.get(catIndex).id;
                String description = etDescription.getText().toString().trim();
                String paiement = spinnerPaiement.getSelectedItem().toString();

                if (existing == null) {
                    viewModel.insert(new Depense(montant, categorieId, null, selectedDate[0], description, paiement));
                    Snackbar.make(requireView(), "Dépense ajoutée", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.updateById(existing.id, montant, categorieId, null, selectedDate[0], description, paiement);
                    Snackbar.make(requireView(), "Dépense modifiée", Snackbar.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}