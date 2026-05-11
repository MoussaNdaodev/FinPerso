package sn.esmt.finperso.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import sn.esmt.finperso.R;
import sn.esmt.finperso.adapter.CategorieAdapter;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Rubrique;
import sn.esmt.finperso.viewmodel.CategorieViewModel;

public class CategoriesFragment extends Fragment {

    private CategorieViewModel viewModel;
    private RecyclerView recyclerView;
    private CategorieAdapter adapter;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_categories);
        fab = view.findViewById(R.id.fab_add_categorie);

        viewModel = new ViewModelProvider(this).get(CategorieViewModel.class);

        adapter = new CategorieAdapter(new CategorieAdapter.OnCategorieClick() {
            @Override
            public void onCategorieClick(Categorie categorie) {
                showEditCategorieDialog(categorie);
            }

            @Override
            public void onAjouterRubrique(Categorie categorie) {
                showAjouterRubriqueDialog(categorie);
            }

            @Override
            public void onSupprimerRubrique(Rubrique rubrique, Categorie categorie) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation")
                        .setMessage("Supprimer la rubrique \"" + rubrique.nom + "\" ?")
                        .setPositiveButton("Oui", (d, w) -> {
                            viewModel.deleteRubrique(rubrique);
                            Snackbar.make(requireView(), "Rubrique supprimée", Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Non", null)
                        .show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.setData(categories);
            for (Categorie cat : categories) {
                viewModel.getRubriquesByCategorie(cat.id).observe(getViewLifecycleOwner(),
                        rubriques -> adapter.setRubriques(cat.id, rubriques));
            }
        });

        fab.setOnClickListener(v -> showAddCategorieDialog());
    }

    private void showAddCategorieDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_categorie, null);
        EditText etNom = dialogView.findViewById(R.id.et_nom_categorie);
        Spinner spinnerCouleur = dialogView.findViewById(R.id.spinner_couleur);

        String[] couleurs = {"#F44336 (Rouge)", "#2196F3 (Bleu)", "#9C27B0 (Violet)",
                "#4CAF50 (Vert)", "#FF9800 (Orange)", "#00BCD4 (Cyan)", "#E91E63 (Rose)", "#607D8B (Gris)"};
        ArrayAdapter<String> couleurAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.item_spinner, couleurs);
        couleurAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerCouleur.setAdapter(couleurAdapter);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Nouvelle catégorie")
                .setView(dialogView)
                .setPositiveButton("Ajouter", null)
                .setNegativeButton("Annuler", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String nom = etNom.getText().toString().trim();
                if (nom.isEmpty()) {
                    etNom.setError("Nom requis");
                    return;
                }
                String[] couleursHex = {"#F44336", "#2196F3", "#9C27B0", "#4CAF50", "#FF9800", "#00BCD4", "#E91E63", "#607D8B"};
                int idx = spinnerCouleur.getSelectedItemPosition();
                String couleur = couleursHex[Math.min(idx, couleursHex.length - 1)];

                viewModel.insertCategorie(nom, couleur);
                Snackbar.make(requireView(), "Catégorie ajoutée", Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showEditCategorieDialog(Categorie categorie) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_categorie, null);
        EditText etNom = dialogView.findViewById(R.id.et_nom_categorie);
        Spinner spinnerCouleur = dialogView.findViewById(R.id.spinner_couleur);

        etNom.setText(categorie.nom);

        String[] couleursAffichees = {"#F44336 (Rouge)", "#2196F3 (Bleu)", "#9C27B0 (Violet)",
                "#4CAF50 (Vert)", "#FF9800 (Orange)", "#00BCD4 (Cyan)", "#E91E63 (Rose)", "#607D8B (Gris)"};
        ArrayAdapter<String> couleurAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.item_spinner, couleursAffichees);
        couleurAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerCouleur.setAdapter(couleurAdapter);

        String[] couleursHex = {"#F44336", "#2196F3", "#9C27B0", "#4CAF50", "#FF9800", "#00BCD4", "#E91E63", "#607D8B"};
        for (int i = 0; i < couleursHex.length; i++) {
            if (couleursHex[i].equals(categorie.couleur)) {
                spinnerCouleur.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Modifier la catégorie")
                .setView(dialogView)
                .setPositiveButton("Sauvegarder", null)
                .setNeutralButton("Ajouter rubrique", null)
                .setNegativeButton("Annuler", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String nom = etNom.getText().toString().trim();
                if (nom.isEmpty()) { etNom.setError("Nom requis"); return; }
                int idx = spinnerCouleur.getSelectedItemPosition();
                categorie.nom = nom;
                categorie.couleur = couleursHex[Math.min(idx, couleursHex.length - 1)];
                viewModel.updateCategorie(categorie);
                Snackbar.make(requireView(), "Catégorie modifiée", Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                showAjouterRubriqueDialog(categorie);
            });
        });

        dialog.setOnDismissListener(d -> {
            viewModel.getTotalDepensesByCategorie(categorie.id).observe(this, total -> {
                if (total != null && total > 0) {
                } else {
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Supprimer", (d2, w) -> {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Confirmation")
                                .setMessage("Supprimer la catégorie \"" + categorie.nom + "\" ?")
                                .setPositiveButton("Oui", (dd, ww) -> {
                                    viewModel.deleteCategorie(categorie);
                                    Snackbar.make(requireView(), "Catégorie supprimée", Snackbar.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Non", null)
                                .show();
                    });
                }
            });
        });

        dialog.show();
    }

    private void showAjouterRubriqueDialog(Categorie categorie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Ajouter une rubrique à " + categorie.nom);

        final EditText input = new EditText(requireContext());
        input.setHint("Nom de la rubrique");
        input.setPadding(40, 20, 40, 20);
        builder.setView(input);

        builder.setPositiveButton("Ajouter", (d, w) -> {
            String nom = input.getText().toString().trim();
            if (nom.isEmpty()) {
                Toast.makeText(requireContext(), "Nom requis", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.insertRubrique(categorie.id, nom);
            Snackbar.make(requireView(), "Rubrique \"" + nom + "\" ajoutée", Snackbar.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }
}
