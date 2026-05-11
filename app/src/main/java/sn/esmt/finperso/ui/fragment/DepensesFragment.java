package sn.esmt.finperso.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sn.esmt.finperso.R;
import sn.esmt.finperso.adapter.DepenseAdapter;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Depense;
import sn.esmt.finperso.model.DepenseAvecCategorie;
import sn.esmt.finperso.model.Rubrique;
import sn.esmt.finperso.viewmodel.DepenseViewModel;

public class DepensesFragment extends Fragment {

    private DepenseViewModel viewModel;
    private RecyclerView recyclerView;
    private DepenseAdapter adapter;
    private FloatingActionButton fab;
    private Spinner spinnerFiltrePeriode, spinnerFiltreCategorie;
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
        spinnerFiltrePeriode = view.findViewById(R.id.spinner_filtre_periode);
        spinnerFiltreCategorie = view.findViewById(R.id.spinner_filtre_categorie);

        viewModel = new ViewModelProvider(this).get(DepenseViewModel.class);

        adapter = new DepenseAdapter(depense -> showDialog(depense));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        setupFiltres();

        viewModel.getDepensesFiltrees().observe(getViewLifecycleOwner(), depenses -> adapter.setData(depenses));

        viewModel.getCategories().observe(getViewLifecycleOwner(), cats -> {
            categoriesList = cats;
            List<String> catNoms = new ArrayList<>();
            catNoms.add("Toutes");
            for (Categorie c : cats) catNoms.add(c.nom);
            ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, catNoms);
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFiltreCategorie.setAdapter(catAdapter);
        });

        fab.setOnClickListener(v -> showDialog(null));
    }

    private void setupFiltres() {
        String[] periodes = {"Ce mois-ci", "Cette semaine", "Aujourd'hui"};
        ArrayAdapter<String> periodeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, periodes);
        periodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltrePeriode.setAdapter(periodeAdapter);

        spinnerFiltrePeriode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appliquerFiltres();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerFiltreCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appliquerFiltres();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void appliquerFiltres() {
        Calendar cal = Calendar.getInstance();
        String periode = spinnerFiltrePeriode.getSelectedItem().toString();
        int mois = cal.get(Calendar.MONTH) + 1;
        int annee = cal.get(Calendar.YEAR);

        switch (periode) {
            case "Aujourd'hui":
                break;
            case "Cette semaine":
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                break;
            case "Ce mois-ci":
            default:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
        }

        viewModel.setFiltrePeriode(
                String.format(Locale.getDefault(), "%02d", mois),
                String.valueOf(annee)
        );

        int catPosition = spinnerFiltreCategorie.getSelectedItemPosition();
        int catId = catPosition > 0 && categoriesList != null ? categoriesList.get(catPosition - 1).id : 0;
        viewModel.setFiltreCategorie(catId);
    }

    private void showDialog(DepenseAvecCategorie existing) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_depense, null);

        com.google.android.material.textfield.TextInputEditText etMontant = dialogView.findViewById(R.id.et_montant);
        com.google.android.material.textfield.TextInputEditText etDescription = dialogView.findViewById(R.id.et_description);
        com.google.android.material.textfield.TextInputEditText etDate = dialogView.findViewById(R.id.et_date);
        Spinner spinnerCategorie = dialogView.findViewById(R.id.spinner_categorie);
        Spinner spinnerRubrique = dialogView.findViewById(R.id.spinner_rubrique);
        Spinner spinnerPaiement = dialogView.findViewById(R.id.spinner_paiement);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        final long[] selectedDate = {cal.getTimeInMillis()};
        etDate.setText(sdf.format(cal.getTime()));

        etDate.setOnClickListener(v -> new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
            cal.set(y, m, d);
            if (cal.getTimeInMillis() > System.currentTimeMillis()) {
                Snackbar.make(requireView(), "La date ne peut pas être dans le futur", Snackbar.LENGTH_SHORT).show();
                return;
            }
            selectedDate[0] = cal.getTimeInMillis();
            etDate.setText(sdf.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show());

        if (categoriesList != null) {
            ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoriesList.stream().map(c -> c.nom).toArray(String[]::new));
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorie.setAdapter(catAdapter);

            spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    chargerRubriques(spinnerRubrique, categoriesList.get(position).id);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        String[] paiements = {"Espèces", "Mobile Money", "Carte", "Autre"};
        ArrayAdapter<String> paiAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, paiements);
        paiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaiement.setAdapter(paiAdapter);

        String[] rubriquesVide = {"Aucune"};
        ArrayAdapter<String> videAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, rubriquesVide);
        videAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRubrique.setAdapter(videAdapter);

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
                if (montant <= 0) {
                    Snackbar.make(requireView(), "Le montant doit être supérieur à 0", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                int catIndex = spinnerCategorie.getSelectedItemPosition();
                if (categoriesList == null || catIndex < 0) return;
                int categorieId = categoriesList.get(catIndex).id;
                String description = etDescription.getText().toString().trim();
                String paiement = spinnerPaiement.getSelectedItem().toString();

                Integer rubriqueId = null;
                if (spinnerRubrique.getSelectedItemPosition() > 0) {
                    List<Rubrique> rubriques = viewModel.getRubriquesByCategorie(categorieId).getValue();
                    if (rubriques != null && spinnerRubrique.getSelectedItemPosition() - 1 < rubriques.size()) {
                        rubriqueId = rubriques.get(spinnerRubrique.getSelectedItemPosition() - 1).id;
                    }
                }

                if (existing == null) {
                    viewModel.insert(new Depense(montant, categorieId, rubriqueId, selectedDate[0], description, paiement));
                    Snackbar.make(requireView(), "Dépense ajoutée", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.updateById(existing.id, montant, categorieId, rubriqueId, selectedDate[0], description, paiement);
                    Snackbar.make(requireView(), "Dépense modifiée", Snackbar.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void chargerRubriques(Spinner spinnerRubrique, int categorieId) {
        viewModel.getRubriquesByCategorie(categorieId).observe(getViewLifecycleOwner(), rubriques -> {
            List<String> noms = new ArrayList<>();
            noms.add("Aucune");
            if (rubriques != null) {
                for (Rubrique r : rubriques) noms.add(r.nom);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, noms);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRubrique.setAdapter(adapter);
        });
    }
}
