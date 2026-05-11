package sn.esmt.finperso.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sn.esmt.finperso.R;
import sn.esmt.finperso.model.Rubrique;

public class RubriqueAdapter extends RecyclerView.Adapter<RubriqueAdapter.ViewHolder> {

    private List<Rubrique> list = new ArrayList<>();
    private final OnRubriqueDelete listener;

    public interface OnRubriqueDelete {
        void onDelete(Rubrique rubrique);
    }

    public RubriqueAdapter(List<Rubrique> list, OnRubriqueDelete listener) {
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rubrique, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNom, btnSupprimer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tv_nom_rubrique);
            btnSupprimer = itemView.findViewById(R.id.btn_supprimer_rubrique);
        }

        void bind(Rubrique rubrique) {
            tvNom.setText(rubrique.nom);
            btnSupprimer.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(rubrique);
            });
        }
    }
}
