package sn.esmt.finperso.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sn.esmt.finperso.R;
import sn.esmt.finperso.model.Categorie;
import sn.esmt.finperso.model.Rubrique;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.ViewHolder> {

    private List<Categorie> list = new ArrayList<>();
    private final OnCategorieClick listener;
    private java.util.Map<Integer, List<Rubrique>> rubriquesMap = new java.util.HashMap<>();

    public interface OnCategorieClick {
        void onCategorieClick(Categorie categorie);
        void onAjouterRubrique(Categorie categorie);
        void onSupprimerRubrique(Rubrique rubrique, Categorie categorie);
    }

    public CategorieAdapter(OnCategorieClick listener) {
        this.listener = listener;
    }

    public void setData(List<Categorie> newData) {
        this.list = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setRubriques(int categorieId, List<Rubrique> rubriques) {
        rubriquesMap.put(categorieId, rubriques);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == categorieId) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categorie, parent, false);
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
        private final View viewCouleur;
        private final TextView tvNom;
        private final TextView tvRubriquesCount;
        private final RecyclerView rvRubriques;
        private Categorie currentCategorie;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewCouleur = itemView.findViewById(R.id.view_couleur);
            tvNom = itemView.findViewById(R.id.tv_nom_categorie);
            tvRubriquesCount = itemView.findViewById(R.id.tv_rubriques_count);
            rvRubriques = itemView.findViewById(R.id.rv_rubriques);

            itemView.setOnClickListener(v -> {
                if (listener != null && currentCategorie != null) {
                    listener.onCategorieClick(currentCategorie);
                }
            });
        }

        void bind(Categorie categorie) {
            currentCategorie = categorie;
            tvNom.setText(categorie.nom);

            try {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(android.graphics.Color.parseColor(categorie.couleur));
                viewCouleur.setBackground(drawable);
            } catch (Exception e) {
                viewCouleur.setBackgroundColor(android.graphics.Color.parseColor("#607D8B"));
            }

            List<Rubrique> rubriques = rubriquesMap.get(categorie.id);
            if (rubriques != null) {
                tvRubriquesCount.setText(rubriques.size() + " rubriques");
                RubriqueAdapter rubAdapter = new RubriqueAdapter(rubriques, rubrique -> {
                    if (listener != null) listener.onSupprimerRubrique(rubrique, categorie);
                });
                rvRubriques.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                rvRubriques.setAdapter(rubAdapter);
                rvRubriques.setNestedScrollingEnabled(false);
                rvRubriques.setVisibility(View.VISIBLE);
            } else {
                tvRubriquesCount.setText("0 rubriques");
                rvRubriques.setVisibility(View.GONE);
            }
        }
    }
}
