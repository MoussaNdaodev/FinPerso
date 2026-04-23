package sn.esmt.finperso.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sn.esmt.finperso.R;
import sn.esmt.finperso.model.BudgetAvecProgression;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {

    private List<BudgetAvecProgression> list = new ArrayList<>();
    private final OnClick listener;

    public interface OnClick {
        void onClick(BudgetAvecProgression budget);
    }

    public BudgetAdapter(OnClick listener) {
        this.listener = listener;
    }

    public void setData(List<BudgetAvecProgression> newData) {
        this.list = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategorieNom, tvBudgetMontant, tvConsomme, tvRestant, tvDepassement;
        private final ProgressBar progressBudget;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategorieNom = itemView.findViewById(R.id.tv_categorie_nom);
            tvBudgetMontant = itemView.findViewById(R.id.tv_budget_montant);
            tvConsomme = itemView.findViewById(R.id.tv_consomme);
            tvRestant = itemView.findViewById(R.id.tv_restant);
            tvDepassement = itemView.findViewById(R.id.tv_depassement);
            progressBudget = itemView.findViewById(R.id.progress_budget);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClick(list.get(pos));
                }
            });
        }

        void bind(BudgetAvecProgression budget) {
            DecimalFormat df = new DecimalFormat("#,###");
            tvCategorieNom.setText(budget.categorieNom != null ? budget.categorieNom : "Catégorie");
            tvBudgetMontant.setText(df.format(budget.montantPlafond) + " Fcfa");

            double consomme = budget.montantConsomme;
            double restant = budget.montantPlafond - consomme;
            int progression = 0;
            if (budget.montantPlafond > 0) {
                progression = (int) ((consomme / budget.montantPlafond) * 100);
            }

            progressBudget.setProgress(progression);
            tvConsomme.setText(df.format(consomme) + " Fcfa dépensé");

            if (restant >= 0) {
                tvRestant.setText(df.format(restant) + " Fcfa restant");
                tvRestant.setTextColor(Color.parseColor("#4CAF50"));
                tvDepassement.setVisibility(View.GONE);
            } else {
                tvRestant.setText(df.format(Math.abs(restant)) + " Fcfa dépassé");
                tvRestant.setTextColor(Color.parseColor("#F44336"));
                progressBudget.setProgress(100);
                tvDepassement.setVisibility(View.VISIBLE);
            }
        }
    }
}