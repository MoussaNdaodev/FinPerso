package sn.esmt.finperso.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import sn.esmt.finperso.R;
import sn.esmt.finperso.adapter.DepenseAdapter;
import sn.esmt.finperso.model.BudgetAvecProgression;
import sn.esmt.finperso.viewmodel.DashboardViewModel;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    private TextView tvSolde, tvTotalDepenses, tvTotalRevenus, tvMoisAnnee, tvSectionBudgets;
    private RecyclerView rvDernieres;
    private DepenseAdapter adapter;
    private LinearLayout layoutAlertesBudgets;
    private FloatingActionButton fabQuickAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSolde = view.findViewById(R.id.tv_solde);
        tvTotalDepenses = view.findViewById(R.id.tv_total_depenses);
        tvTotalRevenus = view.findViewById(R.id.tv_total_revenus);
        tvMoisAnnee = view.findViewById(R.id.tv_mois_annee);
        tvSectionBudgets = view.findViewById(R.id.tv_section_budgets);
        rvDernieres = view.findViewById(R.id.rv_dernieres_depenses);
        layoutAlertesBudgets = view.findViewById(R.id.layout_alertes_budgets);
        fabQuickAdd = view.findViewById(R.id.fab_quick_add_depense);

        Calendar cal = Calendar.getInstance();
        String moisStr = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH).format(cal.getTime());
        tvMoisAnnee.setText(Character.toUpperCase(moisStr.charAt(0)) + moisStr.substring(1));

        adapter = new DepenseAdapter(depense -> {});
        rvDernieres.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDernieres.setAdapter(adapter);
        rvDernieres.setNestedScrollingEnabled(false);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        String mois = String.format(Locale.getDefault(), "%02d", cal.get(Calendar.MONTH) + 1);
        String annee = String.valueOf(cal.get(Calendar.YEAR));

        viewModel.getDernieresCinq().observe(getViewLifecycleOwner(), depenses -> adapter.setData(depenses));

        viewModel.getSolde(mois, annee).observe(getViewLifecycleOwner(), solde -> {
            DecimalFormat df = new DecimalFormat("#,###");
            tvSolde.setText(df.format(solde) + " Fcfa");
            tvSolde.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        });

        viewModel.getTotalDepenses(mois, annee).observe(getViewLifecycleOwner(), total -> {
            tvTotalDepenses.setText(new DecimalFormat("#,###").format(total) + " Fcfa");
        });

        viewModel.getTotalRevenus(mois, annee).observe(getViewLifecycleOwner(), total -> {
            tvTotalRevenus.setText(new DecimalFormat("#,###").format(total) + " Fcfa");
        });

        viewModel.getBudgetsCritiques(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))
                .observe(getViewLifecycleOwner(), budgets -> {
                    afficherAlertesBudgets(budgets);
                });

        fabQuickAdd.setOnClickListener(v -> {
            requireActivity().findViewById(R.id.bottom_navigation).post(() -> {
                try {
                    com.google.android.material.bottomnavigation.BottomNavigationView nav =
                            requireActivity().findViewById(R.id.bottom_navigation);
                    nav.setSelectedItemId(R.id.nav_depenses);
                } catch (Exception e) {
                    DepensesFragment frag = new DepensesFragment();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, frag)
                            .commit();
                }
            });
        });
    }

    private void afficherAlertesBudgets(java.util.List<BudgetAvecProgression> budgets) {
        layoutAlertesBudgets.removeAllViews();
        boolean aAlertes = false;

        for (BudgetAvecProgression b : budgets) {
            if (b.montantPlafond <= 0) continue;
            double progression = (b.montantConsomme / b.montantPlafond) * 100;
            if (progression >= 70) {
                aAlertes = true;
                View alertView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_budget_mini, layoutAlertesBudgets, false);

                TextView tvNom = alertView.findViewById(R.id.tv_alerte_nom);
                TextView tvProgression = alertView.findViewById(R.id.tv_alerte_progression);
                View indicator = alertView.findViewById(R.id.view_alerte_indicator);

                String nom = b.categorieNom != null ? b.categorieNom : "Budget Global";
                tvNom.setText(nom);

                DecimalFormat df = new DecimalFormat("#,###");
                tvProgression.setText(df.format(b.montantConsomme) + " / " + df.format(b.montantPlafond) + " Fcfa (" + (int)progression + "%)");

                int color;
                if (progression >= 90) color = ContextCompat.getColor(requireContext(), R.color.negative);
                else if (progression >= 70) color = ContextCompat.getColor(requireContext(), R.color.warning);
                else color = ContextCompat.getColor(requireContext(), R.color.positive);
                indicator.setBackgroundColor(color);

                layoutAlertesBudgets.addView(alertView);
            }
        }

        tvSectionBudgets.setVisibility(aAlertes ? View.VISIBLE : View.GONE);
    }
}
