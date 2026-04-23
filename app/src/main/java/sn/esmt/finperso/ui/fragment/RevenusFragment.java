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
import java.util.Locale;

import sn.esmt.finperso.R;
import sn.esmt.finperso.adapter.RevenuAdapter;
import sn.esmt.finperso.model.Revenu;
import sn.esmt.finperso.viewmodel.RevenuViewModel;

public class RevenusFragment extends Fragment {

    private RevenuViewModel viewModel;
    private RecyclerView recyclerView;
    private RevenuAdapter adapter;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revenus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_revenus);
        fab = view.findViewById(R.id.fab_add_revenu);

        viewModel = new ViewModelProvider(this).get(RevenuViewModel.class);

        adapter = new RevenuAdapter(revenu -> showDialog(revenu));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getAllRevenus().observe(getViewLifecycleOwner(), revenus -> adapter.setData(revenus));

        fab.setOnClickListener(v -> showDialog(null));
    }

    private void showDialog(Revenu existing) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_revenu, null);

        EditText etMontant = dialogView.findViewById(R.id.et_montant_revenu);
        EditText etDescription = dialogView.findViewById(R.id.et_description_revenu);
        EditText etDate = dialogView.findViewById(R.id.et_date_revenu);
        Spinner spinnerSource = dialogView.findViewById(R.id.spinner_source);

        String[] sources = {"Salaire", "Commerce", "Freelance", "Don", "Autre"};
        ArrayAdapter<String> srcAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, sources);
        srcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(srcAdapter);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        final long[] selectedDate = {cal.getTimeInMillis()};
        etDate.setText(sdf.format(cal.getTime()));

        etDate.setOnClickListener(v -> new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
            cal.set(y, m, d);
            selectedDate[0] = cal.getTimeInMillis();
            etDate.setText(sdf.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show());

        if (existing != null) {
            etMontant.setText(String.valueOf(existing.montant));
            etDescription.setText(existing.description);
            etDate.setText(sdf.format(existing.date));
            selectedDate[0] = existing.date;
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(existing == null ? "Nouveau revenu" : "Modifier le revenu")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", null)
                .setNegativeButton("Annuler", null)
                .create();

        if (existing != null) {
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Supprimer", (d, w) -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation")
                        .setMessage("Supprimer ce revenu ?")
                        .setPositiveButton("Oui", (dd, ww) -> {
                            viewModel.delete(existing);
                            Snackbar.make(requireView(), "Revenu supprimé", Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Non", null).show();
            });
        }

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String montantStr = etMontant.getText().toString().trim();
                if (montantStr.isEmpty()) { etMontant.setError("Requis"); return; }
                double montant = Double.parseDouble(montantStr);
                String source = spinnerSource.getSelectedItem().toString();
                String desc = etDescription.getText().toString().trim();

                if (existing == null) {
                    viewModel.insert(new Revenu(montant, source, selectedDate[0], desc));
                    Snackbar.make(requireView(), "Revenu ajouté", Snackbar.LENGTH_SHORT).show();
                } else {
                    existing.montant = montant;
                    existing.source = source;
                    existing.date = selectedDate[0];
                    existing.description = desc;
                    viewModel.update(existing);
                    Snackbar.make(requireView(), "Revenu modifié", Snackbar.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}